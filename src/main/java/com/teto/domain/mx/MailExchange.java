package com.teto.domain.mx;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.teto.domain.annotation.Meta;
import com.teto.domain.meta.Tag;
import com.teto.domain.target.BaseTarget;
import com.teto.domain.target.TargetType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Setter
@Getter
public class MailExchange extends BaseTarget implements Comparable<MailExchange> {
    @Meta(tag = Tag.MailExchangeName)
    private String mailExchangeName;

    public MailExchange(String name, Integer parentId, Integer level) {
        super(name, TargetType.MailExchange.name(), parentId, level);
    }

    @Override
    public int compareTo(MailExchange o) {
        return getMailExchangeName().compareTo(o.getMailExchangeName());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MailExchange that = (MailExchange) o;
        return Objects.equals(mailExchangeName, that.mailExchangeName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mailExchangeName);
    }
}
