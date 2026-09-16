package com.teto.mime;

public enum MimeCategory {
    // $MimeCategory
    unknown,
    application,
    audio,font,image,message,model,multipart,text,video;

    public static MimeCategory fromString(String str) {
        for(MimeCategory cat : values()) {
            if(cat.name().equals(str)) {
                return cat;
            }
        }
        return null;
    }
}
