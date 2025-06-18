package com.example.xml2db.model;

import jakarta.xml.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@XmlRootElement(name = "insert")
@XmlAccessorType(XmlAccessType.FIELD)
public class InsertRequest {

    @XmlAttribute(name = "table")
    private String table;

    @XmlElement(name = "row")
    private MapElement row;

    public String getTable() {
        return table;
    }

    public Map<String, String> getRow() {
        return row != null ? row.getData() : null;
    }

    // Nested helper for row
    public static class MapElement {
        @XmlAnyElement(lax = true)
        private java.util.List<org.w3c.dom.Element> elements;

        public Map<String, String> getData() {
            Map<String, String> map = new LinkedHashMap<>();
            for (org.w3c.dom.Element e : elements) {
                map.put(e.getTagName(), e.getTextContent());
            }
            return map;
        }
    }
}
