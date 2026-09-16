package com.teto.domain.parser.nmap.internal;

import com.teto.command.Context;
import com.teto.domain.nmap.*;
import com.teto.domain.parser.nmap.NMapOutputParser;
import com.teto.hex.HexUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import us.springett.parsers.cpe.Cpe;
import us.springett.parsers.cpe.CpeParser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XmlParser  {

  private static final String ADDRESS = "address";
  private static final String TRACE = "trace";
  private static final String HOP = "hop";
  private static final String STATUS = "status";
  private static final String STATE = "state";
  private static Context context;

  private static final DocumentBuilderFactory FACTORY = DocumentBuilderFactory.newInstance();

  protected static Document getDocument(String xmlAsString)
          throws ParserConfigurationException, SAXException, IOException {
    DocumentBuilder builder = FACTORY.newDocumentBuilder();
    return builder.parse(new InputSource(new StringReader(xmlAsString)));
  }

  public static NmapRun parse(Context ctx, String xmlAsString, String prov) throws ParserConfigurationException, SAXException, IOException {
    Document document = getDocument(xmlAsString);
    context = ctx;
    var documentElement = document.getDocumentElement();
    NmapRun ret = parseNmapRun(documentElement, prov);

    return ret;
  }

  protected static NmapRun parseNmapRun(Element nmapRunElement, String prov) {

    validateNodeName(nmapRunElement, "nmaprun");
    var hostHintElements = nmapRunElement.getElementsByTagName("hosthint");
    var hostHints = new ArrayList<HostHint>();
    for (int i = 0; i < hostHintElements.getLength(); i++) {
      hostHints.add(parseHostHint((Element) hostHintElements.item(i)));
    }

    var hostElements = nmapRunElement.getElementsByTagName("host");
    var hosts = new ArrayList<Host>();
    for (int i = 0; i < hostElements.getLength(); i++) {
      hosts.add(parseHost((Element) hostElements.item(i), prov));
    }
    NmapRun ret = new NmapRun(nmapRunElement.getAttribute("scanner"), nmapRunElement.getAttribute("args"),
            Long.parseLong(nmapRunElement.getAttribute("start")), nmapRunElement.getAttribute("startstr"),
            nmapRunElement.getAttribute("version"), nmapRunElement.getAttribute("xmloutputversion"),
            parseScanInfos(getMultiChildElement(nmapRunElement, "scaninfo")),
            parseVerbose(getSingleChildElement(nmapRunElement, "verbose")),
            parseDebugging(getSingleChildElement(nmapRunElement, "debugging")), hostHints, hosts,
            parseRunStats(getSingleChildElement(nmapRunElement, "runstats")));
    return ret;
  }

  protected static RunStats parseRunStats(Element runStatsElement) {
    validateNodeName(runStatsElement, "runstats");
    return new RunStats(parseFinished(getSingleChildElement(runStatsElement, "finished")),
            parseHosts(getSingleChildElement(runStatsElement, "hosts")));
  }

  private static Hosts parseHosts(Element hostsElement) {
    validateNodeName(hostsElement, "hosts");
    Hosts ret = new Hosts(Long.parseLong(hostsElement.getAttribute("up")), Long.parseLong(hostsElement.getAttribute("down")),
            Long.parseLong(hostsElement.getAttribute("total")));
    return ret;
  }

  private static Finished parseFinished(Element finishedElement) {
    validateNodeName(finishedElement, "finished");
    return new Finished(Long.parseLong(finishedElement.getAttribute("time")), finishedElement.getAttribute("timestr"),
            finishedElement.getAttribute("summary"), finishedElement.getAttribute("elapsed"),
            finishedElement.getAttribute("exit"));
  }

  protected static Debugging parseDebugging(Element debuggingElement) {
    validateNodeName(debuggingElement, "debugging");
    return new Debugging(debuggingElement.getAttribute("level"));
  }

  private static Verbose parseVerbose(Element verboseElement) {
    validateNodeName(verboseElement, "verbose");
    return new Verbose(verboseElement.getAttribute("level"));
  }

  protected static ScanInfo[] parseScanInfos(Element[] scanInfoElements) {
    List<ScanInfo> infos = new ArrayList<>();
    for(int i = 0 ; i < scanInfoElements.length; i++) {
      ScanInfo si = parseScanInfo(scanInfoElements[i]);
      infos.add(si);
    }
    ScanInfo[] ret = infos.toArray(new ScanInfo[0]);
    return ret;
  }
  protected static ScanInfo parseScanInfo(Element scanInfoElement) {
    validateNodeName(scanInfoElement, "scaninfo");
    if(scanInfoElement == null) {
      return new ScanInfo();
    }
    return new ScanInfo(scanInfoElement.getAttribute("type"), scanInfoElement.getAttribute("protocol"),
            Long.parseLong(scanInfoElement.getAttribute("numservices")), scanInfoElement.getAttribute("services"));
  }

  protected static Address parseAddress(Element elem) {
      validateNodeName(elem, ADDRESS);

      String addr = elem.getAttribute("addr");
      String addrType = elem.getAttribute("addrtype");
      String vendor = elem.getAttribute("vendor");
      String mac = (vendor != null) ? addr: null;
    return new Address(addr, addrType, mac, vendor);
  }
  protected static List<Address> parseAddresses(Element[] elems) {
    final List<Address> addresses = new ArrayList<>();
    for(Element elem : elems) {
      validateNodeName(elem, ADDRESS);
      String addr = elem.getAttribute("addr");
      String addrType = elem.getAttribute("addrtype");
      String vendor = elem.getAttribute("vendor");
      String mac = (vendor != null) ? addr: null;
      addresses.add(new Address(addr, addrType, mac, vendor));
    }
     return addresses;
  }

  protected static Trace parseTrace(Element e) {
     if(e != null) {
       validateNodeName(e, TRACE);
       var port = e.getAttribute("port");
       var proto = e.getAttribute("proto");
       var hopElements = e.getElementsByTagName(HOP);
       List<Hop> hops = new ArrayList<>();
       for (int i = 0; i < hopElements.getLength(); i++) {
         hops.add(parseHop((Element) hopElements.item(i)));
       }
       return new Trace(port, proto, hops);
     }
     return null;
  }

  private static Hop parseHop(Element item) {
    if(item != null) {
      var host = item.getAttribute("host");
      var ipaddr = item.getAttribute("ipaddr");
      Double rtt = atod(item.getAttribute("rtt"));
      Integer ttl = atoi(item.getAttribute("ttl"));
      return new Hop(host, ipaddr, rtt, ttl);
    }
    return null;
  }

  private static Double atod(String str) {
    try {
      return Double.parseDouble(str);
    } catch (Exception e) {
      return null;
    }
  }

  private static Integer atoi(String str) {
    try {
      return Integer.parseInt(str);
    } catch(Exception e) {
      return null;
    }
  }

  protected static Status parseStatus(Element statusElement) {
    validateNodeName(statusElement, STATUS);
    Status ret = new Status(statusElement.getAttribute(STATE), statusElement.getAttribute("reason"),
            statusElement.getAttribute("reason_ttl"));
    long end = System.currentTimeMillis();
    return ret;
  }

  protected static State parseState(Element stateElement) {
    validateNodeName(stateElement, STATE);
    return new State(stateElement.getAttribute(STATE), stateElement.getAttribute("reason"),
            stateElement.getAttribute("reason_ttl"));
  }

  private static PortUsed parsePortUsed(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "portused");
    return new PortUsed(el.getAttribute(STATE), el.getAttribute("proto"), Long.parseLong(el.getAttribute("portid")));
  }

  protected static Service parseService(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "service");
    var cpeElement = el.getElementsByTagName("cpe");
    var cpes = new ArrayList<Cpe>();
    for (int i = 0; i < cpeElement.getLength(); i++) {
      cpes.addAll(createCpes(cpeElement.item(i).getTextContent()));
    }
    //Element cpeNode = getSingleChildElement(el, "cpe");
    //String cpe = null;
    //if(cpeNode != null) {
    //  cpe = cpeNode.getTextContent();
    //}
    return new Service(el.getAttribute("name"), el.getAttribute("product"), el.getAttribute("extrainfo"),
            el.getAttribute("tunnel"), el.getAttribute("method"), el.getAttribute("conf"), el.getAttribute("servicefp"), cpes.toArray(new Cpe[0]));
  }

  protected static HostHint parseHostHint(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "hosthint");
    var status = parseStatus(getSingleChildElement(el, STATUS));
    Element[] addrs = getMultiChildElement(el, ADDRESS);
    var addresses = parseAddresses(addrs);
    var hostNames = parseHostNames(getSingleChildElement(el, "hostnames"));
    return new HostHint(status, addresses, hostNames);
  }

  protected static HostName parseHostName(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "hostname");
    return new HostName(el.getAttribute("name"), el.getAttribute("type"));
  }

  protected static ExtraReasons parseExtraReasons(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "extrareasons");
    var countString = el.getAttribute("count");
    var count = Long.parseLong(countString);
    return new ExtraReasons(el.getAttribute("reason"), count, el.getAttribute("proto"), el.getAttribute("ports"));
  }

  protected static ExtraPorts parseExtraPorts(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "extraports");
    var countString = el.getAttribute("count");
    var count = Long.parseLong(countString);
    var extraReasons = new ArrayList<ExtraReasons>();
    var extraReasonsElement = el.getElementsByTagName("extrareasons");
    for (int j = 0; j < extraReasonsElement.getLength(); j++) {
      extraReasons.add(parseExtraReasons((Element) extraReasonsElement.item(j)));
    }
    return new ExtraPorts(el.getAttribute("filtered"), count, extraReasons);
  }

  protected static HostNames parseHostNames(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "hostnames");
    var hostNames = new ArrayList<HostName>();
    var hostNameElement = el.getElementsByTagName("hostname");
    for (int j = 0; j < hostNameElement.getLength(); j++) {
      hostNames.add(parseHostName((Element) hostNameElement.item(j)));
    }
    return new HostNames(hostNames);
  }

  protected static Ports parsePorts(Element el, String prov) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "ports");

    var extraPorts = el.getElementsByTagName("extraports");

    List<ExtraPorts> xtraPorts = new ArrayList<>();
    if(extraPorts != null && extraPorts.getLength() > 0) {
      for (int j = 0; j < extraPorts.getLength(); j++) {
        var element = (Element) extraPorts.item(j);
        xtraPorts.add(parseExtraPorts(element));
      }
    }

    var portElement = el.getElementsByTagName("port");
    var ports = new ArrayList<Port>();
    for (int j = 0; j < portElement.getLength(); j++) {
        ports.add(parsePort((Element) portElement.item(j), prov));
    }
    Ports ret = new Ports(xtraPorts, ports);
    return ret;
  }

  private static Port parsePort(Element el, String prov) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "port");

    var portId = Long.parseLong(el.getAttribute("portid"));
    var stateElement = getSingleChildElement(el, STATE);
    String portState = stateElement.getAttribute("state");
    var serviceElement = getSingleChildElement(el, "service");
    var scriptElement = el.getElementsByTagName("script");
    final List<NSEScript> scripts = new ArrayList<>();
    String protocol = el.getAttribute("protocol");
    if(scriptElement != null) {
      for (int i = 0; i < scriptElement.getLength(); i++) {
        scripts.add(parseNSEScript(portState, protocol, portId, (Element) scriptElement.item(i), prov));
      }
    }
    Port ret = new Port(protocol, portId, parseState(stateElement), parseService(serviceElement),
            scripts);
    return ret;
  }

  private static NSEScript parseNSEScript(String portState, String protocol, long portId, Element el,String prov) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "script");
    var elems = new ArrayList<Elem>();
    var elemElement = el.getElementsByTagName("elem");
    for (int i = 0; i < elemElement.getLength(); i++) {
      elems.add(parseElem((Element) elemElement.item(i)));
    }
    var tables = new ArrayList<Table>();
    var tableElement = el.getElementsByTagName("table");
    for (int i = 0; i < tableElement.getLength(); i++) {
      tables.add(parseTable((Element) tableElement.item(i)));
    }

    var output =  el.getAttribute("output");
    NSEScript ret = new NSEScript(context, portState, protocol, portId, el.getAttribute("id"), output, elems, tables);
    if(output != null && !output.isBlank()) {
      NSEInfo info = new NMapOutputParser(output, prov).parse();
      if(info != null) {
        ret.setInfo(info);
      }
    }
    return ret;
  }

  private static Table parseTable(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "table");
    var elems = new ArrayList<Elem>();
    var elemElement = el.getElementsByTagName("elem");
    for (int i = 0; i < elemElement.getLength(); i++) {
      elems.add(parseElem((Element) elemElement.item(i)));
    }
    var tables = new ArrayList<Table>();
    var tableElement = el.getElementsByTagName("table");
    for (int i = 0; i < tableElement.getLength(); i++) {
      tables.add(parseTable((Element) tableElement.item(i)));
    }
    return new Table(el.getAttribute("key"), elems, tables);
  }

  private static Elem parseElem(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "elem");
    return new Elem(el.getAttribute("key"), el.getTextContent());
  }

  private static OS parseOS(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "os");
    var portsUsed = new ArrayList<PortUsed>();
    var osMatches = new ArrayList<OSMatch>();
    var fingerPrints = new ArrayList<FingerPrint>();

    var portUsedElement = el.getElementsByTagName("portused");
    for (int j = 0; j < portUsedElement.getLength(); j++) {
      portsUsed.add(parsePortUsed((Element) portUsedElement.item(j)));
    }
    var osMatchElement = el.getElementsByTagName("osmatch");
    for (int j = 0; j < osMatchElement.getLength(); j++) {
      osMatches.add(parseOSMatch((Element) osMatchElement.item(j)));
    }
    var fingerPrintElement = el.getElementsByTagName("osfingerprint");
    if(fingerPrintElement != null) {
      for (int j = 0; j < fingerPrintElement.getLength(); j++) {
        var fpe = fingerPrintElement.item(j);
        fingerPrints.add(parseFingerPrint((Element) fpe));
      }
    }
    return new OS(portsUsed, osMatches, fingerPrints);
  }

  private static FingerPrint parseFingerPrint(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "osfingerprint");
    var fp = el.getAttribute("fingerprint");
    var fprint = new FingerPrint();
    fprint.setFingerPrint(fp);
    return fprint;
  }

  private static OSMatch parseOSMatch(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "osmatch");
    var osClasses = new ArrayList<OSClass>();
    var osClassElement = el.getElementsByTagName("osclass");
    for (int j = 0; j < osClassElement.getLength(); j++) {
      osClasses.add(parseOSClass((Element) osClassElement.item(j)));
    }
    return new OSMatch(el.getAttribute("name"), Long.parseLong(el.getAttribute("accuracy")),
            Long.parseLong(el.getAttribute("line")), osClasses);
  }

  private static OSClass parseOSClass(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "osclass");
    var cpeElement = el.getElementsByTagName("cpe");
    var cpes = new ArrayList<Cpe>();
    for (int i = 0; i < cpeElement.getLength(); i++) {
      cpes.addAll(createCpes(cpeElement.item(i).getTextContent()));
    }

    return new OSClass(el.getAttribute("name"), el.getAttribute("vendor"), el.getAttribute("osfamily"),
            el.getAttribute("osgen"), Long.parseLong(el.getAttribute("accuracy")), cpes);
  }

  private static List<Cpe> createCpes(String text) {
        final List<Cpe> cpes = new ArrayList<>();
        Cpe cpe = null;
        try {
            cpe = CpeParser.parse(text);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        cpes.add(cpe);
        return cpes;
  }

  protected static Host parseHost(Element el, String prov) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "host");
    Trace trace = parseTrace(getSingleChildElement(el,"trace"));
    Host ret = new Host(el.getAttribute("starttime"), el.getAttribute("endtime"),
            parseStatus(getSingleChildElement(el, STATUS)),
            parseAddresses(getMultiChildElement(el, ADDRESS)),
            parseHostNames(getSingleChildElement(el, "hostnames")),
            parsePorts(getSingleChildElement(el, "ports"), prov),
            parseHostScript(getSingleChildElement(el,"hostscript")),
            parseOS(getSingleChildElement(el, "os")),
            parseUptime(getSingleChildElement(el, "uptime")),
            parseTcpSequence(getSingleChildElement(el, "tcpsequence")),
            parseIdIpSequence(getSingleChildElement(el, "ipidsequence")),
            parseTcpTsSequence(getSingleChildElement(el, "tcptssequence")),
            parseTimes(getSingleChildElement(el, "times")), trace);
    return ret;
  }

  private static String decode(String text) {
      try {
        return URLDecoder.decode(text);
      } catch(Exception e) {
        return text;
      }
  }
  private static HostScript parseHostScript(Element el) {
    if(el == null) {
      return null;
    }
    validateNodeName(el, "hostscript");
    HostScript hs = new HostScript();
    var childElements = el.getElementsByTagName("script");
    for(int i = 0 ; i < childElements.getLength(); i++) {
      Element child = (Element) childElements.item(i);
      String id = child.getAttribute("id");
      String output = decode(child.getAttribute("output"));
      Script script = new Script();
      script.setId(id);
      script.setOutput(output);
      if(output.contains("PMTU")) {

        String[] parts = output.split("==");
        script.setMtu(Integer.parseInt(parts[1].strip()));
      }
      hs.getScripts().add(script);
    }
    return hs;
  }

  private static Uptime parseUptime(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "uptime");
    return new Uptime(Long.parseLong(el.getAttribute("seconds")), el.getAttribute("lastboot"));
  }

  private static TcpSequence parseTcpSequence(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "tcpsequence");
    return new TcpSequence(Long.parseLong(el.getAttribute("index")), el.getAttribute("difficulty"),
            el.getAttribute("values"));
  }

  private static IpIdSequence parseIdIpSequence(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "ipidsequence");
    return new IpIdSequence(el.getAttribute("class"),  convertHexToIntegers((String)el.getAttribute("values")));
  }

  private static List<Integer> convertHexToIntegers(String values) {
    String[] parts = values.split(",");
    final List<Integer> ints = new ArrayList<>();
    if(parts == null || parts.length == 0) {
      return ints;
    }
    for(String hex : parts) {
      BigInteger decimaL = HexUtils.hexToDecimal(hex);
      Integer i = decimaL.intValue();
      ints.add(i);
    }
    Collections.sort(ints);
    return ints;
  }

  private static TcpTsSequence parseTcpTsSequence(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "tcptssequence");
    return new TcpTsSequence(el.getAttribute("class"), el.getAttribute("values"));
  }

  private static Times parseTimes(Element el) {
    if (el == null) {
      return null;
    }
    validateNodeName(el, "times");
    return new Times(Long.parseLong(el.getAttribute("srtt")), Long.parseLong(el.getAttribute("rttvar")),
            Long.parseLong(el.getAttribute("to")));
  }

  protected static Element[] getMultiChildElement(Element el, String tagName) {
    if (el == null) {
      return null;
    }
    final List<Element> elems = new ArrayList<>();
    var childElements = el.getElementsByTagName(tagName);
    for(int i = 0 ; i < childElements.getLength(); i++) {
      elems.add((Element) childElements.item(i));
    }
    return elems.toArray(new Element[0]);
  }
  protected static Element getSingleChildElement(Element el, String tagName) {
    if (el == null) {
      return null;
    }
    var childElements = el.getElementsByTagName(tagName);
    return switch (childElements.getLength()) {
      case 1 -> (Element) childElements.item(0);
      case 0 -> null;
      default -> throw new IllegalArgumentException(tagName + " should only appear once in " + el);
    };
  }

  protected static void validateNodeName(Element element, String nodeName) {
    if (element != null && !element.getNodeName().equals(nodeName)) {
      throw new IllegalArgumentException(element + " has wrong node name. Should be " + nodeName);
    }
  }

}
