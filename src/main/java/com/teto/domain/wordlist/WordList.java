package com.teto.domain.wordlist;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Setter
@Getter
public class WordList implements Comparable<WordList> {
    @Meta(tag = Tag.Name)
    private String name;
    @Meta(tag = Tag.ID, id = true)
    private Integer id;
    @Meta(tag = Tag.Provenance)
    private String provenance;
    @Meta(tag = Tag.ShortName)
    private String shortName;
    @Meta(tag = Tag.FileName)
    private String fileName;
    @Meta(tag = Tag.LineCount)
    private Integer lineCount;
    @Meta(tag = Tag.FileSize)
    private String fileSize;
    @Meta(tag = Tag.Date)
    private Double date;
    @Meta(tag = Tag.Uri)
    private String downLoad;
    @Meta(tag = Tag.Cached)
    private Boolean cached;
    @Meta(tag = Tag.KeyWords)
    private String keyWords;

    public WordList(String name, String fileName, String provenance) {
        this.name = name;
        this.fileName = fileName;
        this.provenance = provenance;
    }

    @Override
    public int compareTo(@NonNull WordList o) {
        int cmp = getFileName().compareTo(o.getFileName());
        if(cmp != 0) return cmp;
        return getProvenance().compareTo(o.getProvenance());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WordList wordList = (WordList) o;
        return Objects.equals(shortName, wordList.shortName) && Objects.equals(fileName, wordList.fileName) && Objects.equals(lineCount, wordList.lineCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shortName, fileName, lineCount);
    }

    @Override
    public String toString() {
        return "WordList{" +
                "name='" + name + '\'' +
                ", fileName='" + fileName + '\'' +
                ", provenance='" + provenance + '\'' +
                '}';
    }
}
