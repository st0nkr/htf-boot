package com.teto.domain.contact;

public class ContactName  implements Comparable<ContactName>{
    private String name;
    private String source;

    @Override
    public int compareTo(ContactName o) {
        if(name != null && o.getName() != null) {
            return name.compareTo(o.getName());
        }
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
