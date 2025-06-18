package com.example.xml2db.model;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "delete")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteRequest {

    @XmlAttribute
    private String table;

    @XmlElement
    private String id;

    public String getTable() {
        return table;
    }

    public String getId() {
        return id;
    }
}
