package com.teto.domain.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import com.teto.domain.target.BaseTarget;
import com.teto.domain.target.TargetType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Setter
@Getter
public class SocialMediaUser extends BaseTarget implements Comparable<SocialMediaUser>{
    @Meta(tag = Tag.Url)
    private String url;
    @Meta(tag = Tag.Alias)
    private String alias;
    @Meta(tag = Tag.Platform)
    private String platform;
    @Meta(tag = Tag.FullName)
    private String fulleName;
    @Meta(tag = Tag.ICQ)
    private String icg;
    @Meta(tag = Tag.Location)
    private String locaton;
    @Meta(tag = Tag.LinkedIn)
    private String linkedin;
    @Meta(tag = Tag.ProfileName)
    private String profileName;

    public SocialMediaUser(String name, Integer parentId, Integer level) {
        super(name, TargetType.SocialMediaUser.name(), parentId, level);
    }

    @Override
    public int compareTo(@NonNull SocialMediaUser o) {
        return getUrl().compareTo(o.getUrl());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SocialMediaUser that = (SocialMediaUser) o;
        return Objects.equals(url, that.url) && Objects.equals(alias, that.alias) && Objects.equals(platform, that.platform) && Objects.equals(fulleName, that.fulleName) && Objects.equals(icg, that.icg) && Objects.equals(locaton, that.locaton) && Objects.equals(linkedin, that.linkedin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, alias, platform, fulleName, icg, locaton, linkedin);
    }
}
