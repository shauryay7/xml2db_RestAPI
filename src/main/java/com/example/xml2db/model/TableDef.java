package com.example.xml2db.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.List;

public class TableDef {
    private String name;
    private List<ColumnDef> columns;

    @XmlAttribute
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "column")
    public List<ColumnDef> getColumns() {
        return columns;
    }
    public void setColumns(List<ColumnDef> columns) {
        this.columns = columns;
    }
}
