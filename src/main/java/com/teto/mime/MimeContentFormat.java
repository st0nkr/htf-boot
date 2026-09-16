package com.teto.mime;

public enum MimeContentFormat {
    json("json"),xml("xml"),cbor("cbor"),cborSeq("cbor-seq"),wbxml("wbxml"),
    XML("XML"),jwt("jwt"),fastinfoset("fastinfoset"),
    yml("yml"), yaml("yaml"),base64("base64"),wsdl("wsdl"),unknown("UNKNOWN"),
    gzip("gzip"),jsonSeq("json.seq"),sqlite3("sqlite3"),tlv("tlv"),zip("zip");

    private String value;

    private MimeContentFormat(String value) {
        this.value = value.toUpperCase();
    }

    public static MimeContentFormat fromString(String str) {
        for(MimeContentFormat fmt : values()) {
            if(fmt.value.equals(str.toUpperCase())) {
                return fmt;
            }
        }
        return null;
    }
}
