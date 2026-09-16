package com.teto.domain.web;

import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;

public class WebSite {
    @Meta(id = true, tag = Tag.ID)
    private Integer id;
    @Meta(tag = Tag.CustomerId)
    private Integer customerId;
    @Meta(tag = Tag.Name)
    private String name;
    @Meta(tag = Tag.Url, unique = true)
    private String url;
    @Meta(tag = Tag.IFrameLocator)
    private String iframeLocator;
    @Meta(tag = Tag.GDPRLocator)
    private String gdprLocator;
    @Meta(tag = Tag.PageLoadTime)
    private Integer pageLoadTime = 30;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIframeLocator() {
        return iframeLocator;
    }

    public void setIframeLocator(String iframeLocator) {
        this.iframeLocator = iframeLocator;
    }

    public String getGdprLocator() {
        return gdprLocator;
    }

    public void setGdprLocator(String gdprLocator) {
        this.gdprLocator = gdprLocator;
    }

    public Integer getPageLoadTime() {
        return pageLoadTime;
    }

    public void setPageLoadTime(Integer pageLoadTime) {
        this.pageLoadTime = pageLoadTime;
    }
}
