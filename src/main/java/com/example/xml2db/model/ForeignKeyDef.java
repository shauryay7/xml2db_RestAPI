package com.example.xml2db.model;

import jakarta.xml.bind.annotation.XmlAttribute;

public class ForeignKeyDef {
    private String column;
    private String references;

    @XmlAttribute
    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    @XmlAttribute
    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }
}
