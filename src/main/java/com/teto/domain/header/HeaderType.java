package com.teto.domain.header;

public enum HeaderType {
    AcceptRanges("accept-ranges"),
    AccessControlAllowOrigin("access-control-allow-origin"),
    AltSvc("alt-svc"),
    CacheControl("cache-control"),
    CfCacheStatus("cf-cache-status"),
    CfRay("cf-ray"),
    Connection("connection"),
    ContentEncoding("content-encoding"),
    ContentLanguage("content-language"),
    ContentLength("content-length"),
    ContentSecurityPolicy("content-security-policy"),
    ContentType("content-type"),
    CrossOriginOpenerPolicy("cross-origin-opener-policy"),
    Date("date"),
    Etag("etag"),
    Expires("expires"),
    FeaturePolicy("feature-policy"),
    LastModified("last-modified"),
    Nel("nel"),
    OnionLocation("onion-location"),
    PermissionsPolicy("permissions-policy"),
    Pragma("pragma"),
    PublicKeyPinsReportOnly("public-key-pins-report-only"),
    ReferrerPolicy("referrer-policy"),
    ReportTo("report-to"),
    ReportingEndpoints("reporting-endpoints"),
    Server("server"),
    SetCookie("set-cookie"),
    StrictTransportSecurity("strict-transport-security"),
    TransferEncoding("transfer-encoding"),
    Upgrade("upgrade"),
    Vary("vary"),
    XBuildId("x-build-id"),
    XContentTypeOptions("x-content-type-options"),
    XFrameOptions("x-frame-options"),
    XXssProtection("x-xss-protection");

    private String[] texts;

    HeaderType(String... texts) {
        this.texts = texts;
    }

    public static HeaderType fromString(String str) {
        for(HeaderType ht : values()) {
            if(ht.name().equals(str)) {
                return ht;
            }
            for(String text : ht.texts) {
                if(text.equals(str)) {
                    return ht;
                }
            }
        }
        return null;
    }
}
