package com.teto;


public interface IScriptArgProvider {
    String getSpoofMAC();

    String getSubnetMask();

    String getOutputFileName();

    String getUrl();

    String getUserAgent();
}
