package com.teto;


public interface IRegexPatterns {

    String OCTET = "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
    String ipv4 = "^" + OCTET + "\\." + OCTET + "\\." + OCTET + "\\." + OCTET + "$";

    String ipv6 = "(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))";
    String email = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    String hostName = "(?:[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?\\.)+[a-z0-9][a-z0-9-]{0,61}[a-z0-9]";
    String url = "((((https?|http|ftps?|gopher|telnet|nntp)://)|(mailto:|news:))([-%()_.!~*';/?:@&=+$,A-Za-z0-9])+)";
    String domain = "([a-z0-9]+\\.)*[a-z0-9]+\\.[a-z]+";
    String https = "((https):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
    String username = "^[a-zA-Z0-9._-]+$";
    String userName = username;
    String password = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";


    String MATCH_255     = "(?:\\d|[1-9]\\d|1\\d{2}|2(?:[0-4]\\d|5[0-5]))";
    String CAPTURE_255   = "(\\d|[1-9]\\d|1\\d{2}|2(?:[0-4]\\d|5[0-5])|\\*)";
    String CAPTURE_32    = "([1-9]|[12]\\d|3[0-2])";
    String CAPTURE_IP    = "("+MATCH_255+"\\."+MATCH_255+"\\."+MATCH_255+"\\."+MATCH_255+")";
    String CIDR         = CAPTURE_255+ "(?:\\."+CAPTURE_255+
            "(?:\\."+CAPTURE_255+
            "(?:\\."+CAPTURE_255+
            ")?)?)?(?:/"+CAPTURE_32+"|-"+CAPTURE_IP+")?";
    String file = "^/([^/]+/)*[^/]+\\."+brackets("txt")+"$";
    String brackets =  scopeFor("(",")");

    static String scopeFor(String start, String end) {
        String str = "(\\"+start+".*?\\"+end+")";
        return str;
    }

    private static String brackets(String...things) {
        StringBuilder sb = new StringBuilder("(");
        for(String thing : things) {
            sb.append(thing).append(",");
        }
        String str = sb.toString().substring(0, sb.toString().length()-1);
        str = str+")";
        return str;
    }

}
