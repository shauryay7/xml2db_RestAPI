package com.example.xml2db.model;

import jakarta.xml.bind.annotation.XmlAttribute;

public class ColumnDef {
    private String name;
    private String type;

    @XmlAttribute
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
