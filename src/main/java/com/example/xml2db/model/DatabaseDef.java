package com.example.xml2db.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "database")
public class DatabaseDef {
    private List<TableDef> tables;

    @XmlElement(name = "table")
    public List<TableDef> getTables() {
        return tables;
    }
    public void setTables(List<TableDef> tables) {
        this.tables = tables;
    }
}
