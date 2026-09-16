package com.teto.seclist;

import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;

public class SecListFile {
    @Meta(id = true, tag = Tag.ID, index = 0)
    private Integer id;
    @Meta(notnull = true, tag = Tag.Name, index = 1)
    private String name;
    @Meta(notnull = true, tag = Tag.NumberOfFieldPerLine, index = 2)
    private Integer numberOfFieldsPerLine;
    @Meta(notnull = true, tag = Tag.TotalRecords, index = 3)
    private Integer totalRecords;
    @Meta(notnull = true, tag = Tag.MimeType, index = 4)
    private String mime;
    @Meta(notnull = true, tag = Tag.KeyWords, index = 5)
    private String keyWords;
    @Meta(ignore = true, tag = Tag.Matched, index = 6)
    private transient Double matched;
    @Meta(notnull = true, tag = Tag.FileName, index = 7)
    private String fileName;
    @Meta(tag = Tag.ContentType, index = 8)
    private String contentType;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getNumberOfFieldsPerLine() {
        return numberOfFieldsPerLine;
    }

    public void setNumberOfFieldsPerLine(int numberOfFieldsPerLine) {
        this.numberOfFieldsPerLine = numberOfFieldsPerLine;
    }
    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public Double getMatched() {
        return matched;
    }

    public void setMatched(Double matched) {
        this.matched = matched;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberOfFieldsPerLine(Integer numberOfFieldsPerLine) {
        this.numberOfFieldsPerLine = numberOfFieldsPerLine;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    @Override
    public String toString() {
        return name;
    }
}
