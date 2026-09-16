package com.teto;

import com.teto.command.Context;
import com.teto.command.regex.RegexCIDRNames;
import com.teto.domain.cidr.CIDR;
import com.teto.domain.target.Target;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.InetAddressValidator;

import java.net.*;
import java.util.*;
import java.util.regex.Pattern;

public interface IIPAddresses extends IOptional {

    default boolean isDomain(String d) {
        return DomainValidator.getInstance().isValid(d);
    }
    default List<String> subnet(String subnet) {
        SubnetUtils utils = new SubnetUtils(subnet);
        return Arrays.asList(utils.getInfo().getAllAddresses());
    }

    default String generateRandomIPAddress() {
        return randomNumber() + "." + randomNumber() + "." + randomNumber() + "." + randomNumber();
    }

    default String generateRandomIPAddresses(int num) {

        StringBuilder sb = new StringBuilder();
        for(int i = 0 ; i < num; i++) {
            sb.append(generateRandomIPAddress()).append(" ");
        }
        return sb.toString().strip();
    }
    default int randomNumber() {
        return new Random().nextInt((255 - 1) + 1) + 1;
    }


    default boolean isValidUrl(String url) {
        try {
            new URI(url).toURL();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
    default String getIpAddress(String url)  {
        try {
            InetAddress address;
            if(isValidUrl(url)) {
                address = InetAddress.getByName(new URL(url).getHost());
            } else {
                address = InetAddress.getByName(url);
            }
            String ip = address.getHostAddress();
            return ip;
        } catch(Exception e) {
            return null;
        }
    }

    default boolean isValidIPV4(Target target) {
        return isValidIPV4(target.getUri());
    }

    static int ipV4Compare(String o1, String o2) {
        String[] ip1 = o1.split("\\.");
        String ipFormatted1 = String.format("%3s.%3s.%3s.%3s", ip1[0],ip1[1],ip1[2],ip1[3]);
        String[] ip2 = o2.split("\\.");
        String ipFormatted2 = String.format("%3s.%3s.%3s.%3s",  ip2[0],ip2[1],ip2[2],ip2[3]);
        return ipFormatted1.compareTo(ipFormatted2);
    }

    default boolean isValidIPV6(Target target) {
        return isValidIPV6(target.getUri());
    }

    default boolean isCIDR(Context ctx, String str) {
        return !getCDRNames(ctx, str).isEmpty();
    }

    default boolean isValidCIDR(Context ctx, CIDR c) {
        return isCIDR(ctx, c.getUri());
    }
    default List<String> getCDRNames(Context ctx, String str) {
        Optional<List<String>> ips = ctx.apply(new RegexCIDRNames(str));
        if(ips.isPresent()) {
            return ips.get();
        }
        return new ArrayList<>();
    }

    default boolean isValidDomain(Context ctx, String addr) {

        final String DOMAIN_NAME_PATTERN =  "^[A-Za-z0-9-_]{1,63}\\.[A-Za-z]{2,6}$";
        try {
            Pattern pattern = Pattern.compile(DOMAIN_NAME_PATTERN);
            boolean ret = pattern.matcher(addr).find();
            return ret;
        } catch(Exception e) {
            return false;
        }
    }


    default boolean isValidIPV6(String addr) {
        if(isValidCIDR(addr)) {
            return false;
        }
        InetAddressValidator validator = InetAddressValidator.getInstance();
        return validator.isValidInet6Address(addr);
    }
    default boolean isValidCIDR(Target target) {
        return isValidCIDR(target.getUri());
    }

    default boolean isValidCIDR(String cidr) {
        if(cidr == null || !cidr.contains("/")) {
            return false;
        }
        String[] parts = cidr.split("/");
        if(isValidIPV4(parts[0]) || isValidIPV6(parts[0])) {
            return true;
        }
        return false;
    }

    default boolean isValidIPV4(String addr) {
        if(isValidCIDR(addr)) {
            return false;
        }
        InetAddressValidator validator = InetAddressValidator.getInstance();
        return validator.isValidInet4Address(addr);
    }
}
