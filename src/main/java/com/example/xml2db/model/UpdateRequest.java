package com.example.xml2db.model;

import jakarta.xml.bind.annotation.*;
import org.w3c.dom.Element;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = "update")
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateRequest {
    @XmlAttribute
    private String table;

    @XmlElement(name = "row")
    private InsertRequest.MapElement row;

    public String getTable() {
        return table;
    }

    public Map<String, String> getRow() {
        return row != null ? row.getData() : null;
    }
}
