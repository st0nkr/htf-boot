package com.teto;

import com.teto.domain.html.*;
import com.teto.domain.http.HttpResponseCode;
import com.teto.domain.jsoup.NodeLocator;
import com.teto.domain.jsoup.NodeLocatorFactory;
import com.teto.domain.meta.ConfigProperty;
import com.teto.domain.meta.Tag;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;

public interface IJSoup extends IOptional, IStream,IString{

    default Optional<Document> fetchHTTPSDocument(String url , String userAgent, boolean followRedirects, int timeout) {
        return fetchDocument(url, userAgent, followRedirects, timeout, trustAllCerts());
    }
    default SSLSocketFactory trustAllCerts() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Failed to create a SSL socket factory", e);
        }
    }
    default String getReason(int rc) {
        HttpResponseCode rsp = HttpResponseCode.fromInt(rc);
        if(rsp == null) {
            return "Dunno";
        }
        return rsp.name();
    }

    default Optional<Document> fetchDocument(String url , String userAgent, boolean followRedirects, int timeout,SSLSocketFactory ssl) {
        try {
            return Optional.of(Jsoup.connect(url)
                    .sslSocketFactory(ssl)
                    .header("Accept-Encoding", "gzip, deflate")
                    .userAgent(userAgent)
                    .followRedirects(followRedirects)
                    .timeout(timeout)
                    .get());
        } catch(HttpStatusException hse) {
            System.out.println(getReason(hse.getStatusCode()));
            return Optional.empty();
        }
        catch (IOException e) {
            return Optional.empty();
        }
    }
    default List<String> getComments(Element e) {
        final List<String> comments = new ArrayList<>();
        List<Node> nodes = collectCommentNodes(e);
        for(Node node : nodes) {
            String text = node.attr("text");
            System.out.println(text);
        }

        return comments;
    }
    default List<Node> collectCommentNodes(Element e) {
        List<Node> comments = new ArrayList<>();
        if(e == null || e.children() == null) {
            return comments;
        }
        comments = e.childNodes().stream()
                .filter(n -> n.nodeName().equals("#comment")).toList();
        return comments;
    }
    default Optional<String> getFavicon(String url, String userAgent) {
        Optional<Document> doc = fetchDocument(url, userAgent, true, 50000);
        if(doc.isPresent()) {
            Element element = doc.get().head().select("link[href~=.*\\.(ico|png)]").first();
            if(element == null) {
                return Optional.empty();
            }
            String href = element.attr("href");
            if(href.startsWith("/")) {
                href = url+href;
            }
            return optional(href);
        }
        return Optional.empty();
    }

    default List<String> getLinks(Document doc) {
        final List<String> ret = new ArrayList<>();

        try {
            Elements links = doc.select("a[href]");
            if(links != null && links.size() > 0) {
                links.forEach((link) -> {
                    var lnk = link.attr("abs:href");
                    if (lnk != null && !lnk.isEmpty()) {
                        ret.add(lnk);
                    } else {
                        lnk = link.attr("href");
                        if (lnk != null && !lnk.isEmpty()) {
                            ret.add(lnk);
                        }
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    default Optional<Map<String, NodeLocator>> getIDLocators(Document doc, String... ids) {
        Map<String, NodeLocator> ret = new HashMap<>();
        for (String id : ids) {
            Element e = doc.getElementById(id);
            NodeLocator idLocator = normalizeLocator(e);
            Elements children = e.children();
            List<NodeLocator> childLocators = createNodeLocators(children);
            idLocator.setChildren(childLocators);
            ret.put(id, idLocator);
        }
        return Optional.ofNullable(ret);
    }

    default Optional<Collection<NodeLocator>> getTagLocators(Document doc, boolean recurse, HtmlTag... tags) {
        String[] strTags = tagsToStrings(tags);
        return getTagLocators(doc, recurse, strTags);
    }

    default String[] tagsToStrings(HtmlTag[] tags) {
        Set<String> ret = new HashSet<>();
        for(var tag : tags) {
            ret.add(tag.name());
        }
        return ret.toArray(new String[0]);
    }

    default Optional<Collection<NodeLocator>> getTagLocators(NodeLocator node, HtmlTag...tags) {
        final Collection<NodeLocator> ret = new HashSet<>();

        var jsoupElement = (Element) node.getJsoupElement();
        for(HtmlTag tag : tags) {
            final Elements nodes = jsoupElement.select(tag.name());
            if(nodes.size() > 0) {
                for (Iterator<Element> iter = nodes.iterator(); iter.hasNext(); ) {
                    final Element child = iter.next();
                    NodeLocator locator = normalizeLocator(child);
                    ret.add(locator);
                }
            }
        }
        return Optional.ofNullable(ret);
    }
    default Optional<Collection<NodeLocator>> getTagLocators(Document doc, boolean recurse,String... tags) {
        final Collection<NodeLocator> ret = new HashSet<>();
        String xpath = "/html[1]/";
        for (String tag : tags) {
            final Elements nodes = doc.getElementsByTag(tag);
            if(nodes.size() > 0) {
                int idx = 0;
                for (Iterator<Element> iter = nodes.iterator(); iter.hasNext(); ) {
                    final Element child = iter.next();
                    xpath = xpath + child.nodeName();
                    NodeLocator parentLocator = normalizeLocator(child);
                    parentLocator.put(ConfigProperty.XPATH,xpath+"/"+child.nodeName()+"["+idx+"]");
                    idx++;
                    parentLocator.put(ConfigProperty.TAG, tag);
                    Element grandParent = child.parent();
                    if(grandParent != null) {
                        parentLocator.setParent(grandParent);
                    }
                    if (parentLocator.hasChildren() && recurse) {
                        parentLocator.setChildren(createNodeLocators(parentLocator,recurse));
                    }
                    ret.add(parentLocator);
                }
            }
        }
        return Optional.ofNullable(ret);
    }

    default Optional<Document> fetchDocument(String url , String userAgent, boolean followRedirects, int timeout, Proxy proxy) {
        try {
            return Optional.of(Jsoup.connect(url)
                            .proxy(proxy)
                    .userAgent(userAgent)
                    .followRedirects(followRedirects)
                    .timeout(timeout)
                    .get());
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    default Optional<Document> fetchDocument(String url , String userAgent, boolean followRedirects, int timeout) {
        try {
            return Optional.of(Jsoup.connect(url)
                    .header("Accept-Encoding", "gzip, deflate")
                    .userAgent(userAgent)
                    .followRedirects(followRedirects)
                    .timeout(timeout)
                    .get());
        } catch(HttpStatusException hse) {
            System.out.println(getReason(hse.getStatusCode()));
            return Optional.empty();
        }
        catch (IOException e) {
            return Optional.empty();
        }
    }

    default int cssLocatorLevel(NodeLocator node) {
        if (node == null || node.getAttribute(ConfigProperty.CSS_SELECTOR) == null ||
                node.getAttribute(ConfigProperty.CSS_SELECTOR).isEmpty()) {
            return 0;
        }
        String css = node.getAttribute(ConfigProperty.CSS_SELECTOR);
        String[] levels = css.split(" > ");
        if (levels != null) {
            return levels.length;
        }
        return 1;
    }

    default List<NodeLocator> createNodeLocators(NodeLocator parentLocator,  boolean createChildren) {
        final Set<NodeLocator> loc = new HashSet<>();
        Elements children = ((Element)parentLocator.getJsoupElement()).children();
        if (children != null) {
            int idx = 0;
            for (Iterator<Element> iter = children.iterator(); iter.hasNext(); ) {
                final Element node = iter.next();

                NodeLocator child = normalizeLocator(node);
                child.put(Tag.xpath.name(), parentLocator.get(Tag.xpath.name())+"/"+node.nodeName()+"["+idx+"]");
                idx++;
                // We need to walk back up the dom
                child.setParent(parentLocator);
                loc.add(child);
                if (createChildren && child.hasChildren()) {
                    List<NodeLocator> locs = createNodeLocators(child, createChildren);
                    if(locs != null && !locs.isEmpty()) {
                        child.setChildren(locs);
                    }
                }
            }
        }
        return new ArrayList<>(loc);
    }

    default List<NodeLocator> createNodeLocators(Elements elements, boolean createChildren) {
        final Set<NodeLocator> loc = new HashSet<>();
        if (elements != null) {
            for (Iterator<Element> iter = elements.iterator(); iter.hasNext(); ) {
                Element node = iter.next();
                NodeLocator root = normalizeLocator(node);
                int level = cssLocatorLevel(root);
                loc.add(root);
                if (createChildren && node.childrenSize() > 0) {
                    List<NodeLocator> childLocators = createNodeLocators(root, createChildren);
                    if (childLocators != null && !childLocators.isEmpty()) {
                        for (NodeLocator child : childLocators) {
                            int childLevel = cssLocatorLevel(child);
                            if (childLevel == (level + 1)) {
                                loc.add(child);
                            }
                        }
                    }
                }
            }
        }
        return new ArrayList<>(loc);
    }

    default List<NodeLocator> createNodeLocators(Elements elements) {
        return createNodeLocators(elements, true);
    }

    default NodeLocator getRoot(Document doc) {
        Element root = doc.root();
        return createNodeLocator(root);
    }

    default NodeLocator createNodeLocator(Element e) {
        return normalizeLocator(e);
    }

    default NodeLocator createLocator(Element e) {
        return normalizeLocator(e);
    }

    default String mungeSoapNameSpace(String str) {
        if(str == null || str.isEmpty() || !str.contains(":")) {
            return str;
        }
        String[] parts = str.split(":");
        return capitalize(stringLower(parts[0]))+capitalize(stringLower(parts[1]));

    }

    default Document extractHtml(String output) {
        int idx = output.indexOf("<!DOCTYPE html>");
        if(idx != -1) {
            String html =  output.substring(idx);
            return parseHTML(html);
        }
        return null;
    }
    default Document parseHTML(String html) {
        if(html == null || html.isEmpty()) {
            return null;
        }
        return Jsoup.parse(html);
    }
    default Document parse(String html) {
        if(html == null || html.isEmpty()) {
            return null;
        }
        return Jsoup.parse(html);
    }

    default NodeLocator createNode (Element node) {
        NodeLocator root = normalizeLocator(node);
        return root;
    }
    default NodeLocator normalizeLocator(Element element) {
        NodeLocator node = NodeLocatorFactory.normalize(element);
        return node;
    }

    default List<DescriptiveList> getDescriptiveLists(Document doc) {
        Elements dls = doc.select(HtmlTag.dl.name());
        final List<DescriptiveList> lists = new ArrayList<>();
        if(dls.isEmpty()) {
            return lists;
        }
        for(int i = 0; i < dls.size(); i++) {
            DescriptiveList dl = new DescriptiveList(createNode(dls.get(i)));
            lists.add(dl);
            Elements children = ((Element)dl.getNode().getJsoupElement()).select(HtmlTag.dt.name());
            if(!children.isEmpty()) {
                for(int ci = 0 ; ci < children.size(); ci++) {
                    Element child = children.get(ci);
                    DescriptiveTerm dt = new DescriptiveTerm(createNode(child));
                    dl.getChildren().add(dt);
                    Elements details = child.select(HtmlTag.dd.name());
                    if(!details.isEmpty()) {

                    }
                }
            }
        }
        return lists;
    }

    default List<DescriptiveTerm> locateDescriptiveTerms(DescriptiveList dl) {
        Element node = (Element)dl.getNode().getJsoupElement();
        Elements elements = node.select(HtmlTag.dt.name());
        final List<DescriptiveTerm> terms = new ArrayList<>();
        if(elements != null) {
            for(Element element : elements) {
                DescriptiveTerm dt = new DescriptiveTerm(createNode(element));
                terms.add(dt);
            }
        }
        return terms;
    }

    default List<DescriptiveDetail> locateDescriptiveDetail(DescriptiveList dl) {
        Element node = (Element) dl.getNode().getJsoupElement();
        Elements terms = node.select(HtmlTag.dd.name());
        final List<DescriptiveDetail> details = new ArrayList<>();
        if(terms != null) {
            for(Element element : terms) {
                DescriptiveDetail dt = new DescriptiveDetail(createNode(element));
                List<ParaGraph> paras = locateParagraphs(dt);
                if(paras != null) {
                    dt.setParagraphs(paras);
                }
                details.add(dt);
            }
        }
        return details;
    }

    default List<ParaGraph> locateParagraphs(DescriptiveDetail dt) {
        List<ParaGraph> paras = new ArrayList<>();
        Elements elements = ((Element)dt.getNode().getJsoupElement()).select(HtmlTag.p.name());
        if(elements != null) {
            for(Element element : elements) {
                ParaGraph pg = new ParaGraph(createNode(element));
                List<Code> codes = locateCodes(pg);
                if(codes != null) {
                    pg.setCodes(codes);
                }
                paras.add(pg);
            }
        }
        return paras;
    }

    default List<Code> locateCodes(ParaGraph pg) {
        List<Code> codes = new ArrayList<>();
        var elements = ((Element)pg.getNode().getJsoupElement()).select(HtmlTag.code.name());
        if(elements != null) {
            for(Element element : elements) {
                Code code = new Code(createNode(element));
                codes.add(code);
            }
        }
        return codes;
    }

    default List<DescriptiveList> locateDescriptiveLists(Document doc) {
        Elements dls = doc.select(HtmlTag.dl.name());
        final List<DescriptiveList> lists = new ArrayList<>();
        if(dls.isEmpty()) {
            return lists;
        }
        for(int i = 0; i < dls.size(); i++) {
            DescriptiveList dl = new DescriptiveList(createNode(dls.get(i)));
            final List<DescriptiveTerm> terms = locateDescriptiveTerms(dl);
            final List<DescriptiveDetail> details = locateDescriptiveDetail(dl);
            for(int j = 0 ; j < terms.size(); j++) {
                terms.get(j).setDetail(details.get(j));
            }
            dl.setChildren(terms);
            lists.add(dl);
        }
        return lists;
    }

    default List<DescriptiveList> getNMapDescriptiveLists(Document doc) {
        final List<DescriptiveList> lists = locateDescriptiveLists(doc);
        if(lists.isEmpty()) {
            return lists;
        }
        return lists;
    }

    default List<String> parseTable(Document doc, int tableOrder) {
        Element table = doc.select("table").get(tableOrder);
        Element tbody = table.select("tbody").get(0);
        Elements dataRows = tbody.select("tr");
        Elements headerRow = table.select("tr")
                .get(0)
                .select("th,td");

        List<String> headers = new ArrayList<String>();
        for (Element header : headerRow) {
            headers.add(header.text());
        }

        List<String> parsedDataRows = new ArrayList<>();
        for (int row = 0; row < dataRows.size(); row++) {
            Elements colVals = dataRows.get(row).select("th,td");

            int colCount = 0;
            StringBuilder ip = new StringBuilder();
            Map<String, String> dataRow = new HashMap<String, String>();
            for (int idx = 0 ; idx < colVals.size(); idx++) {
                String text = colVals.get(idx).text();
                if(idx == 10) {
                    ip.append("::");
                }
                if(idx > 10) {
                    continue;
                }
                ip.append(text);
                dataRow.put(headers.get(colCount++), colVals.get(idx).text());
            }
            parsedDataRows.add(ip.toString());
        }
        return parsedDataRows;
    }

    default boolean hasParent(Element e, HtmlTag htmlTag) {
        List<HtmlTag> tags = getParentTags(e);
        for(HtmlTag tag : tags) {
            if(tag.equals(htmlTag)) {
                return true;
            }
        }
        return false;
    }
    default List<HtmlTag> getParentTags(Element e) {
        final List<HtmlTag> list = new Stack<>();
        Element element = e;
        while(true) {
            Element parent = element.parent();
            String tag = parent.tag().toString();
            HtmlTag ht = HtmlTag.fromString(tag);
            list.add(ht);
            if(HtmlTag.html.equals(ht)) {
                break;
            }
            element = element.parent();
        }
        return list;
    }

}
