package com.example.xml2db.model;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "delete")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteRequest {

    @XmlElement(name = "table")
    private String table;

    @XmlElement(name = "id")
    private String id;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
