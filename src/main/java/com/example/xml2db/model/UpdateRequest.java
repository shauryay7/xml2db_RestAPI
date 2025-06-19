package com.example.xml2db.model;

import jakarta.xml.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import org.w3c.dom.Element;

@XmlRootElement(name = "update")
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateRequest {

    @XmlElement(name = "table")
    private String table;

    @XmlElement(name = "row")
    private MapElement row;

    public String getTable() {
        return table;
    }

    public Map<String, String> getRow() {
        return row != null ? row.getData() : null;
    }

    // Helper to parse XML row into Map
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class MapElement {
        @XmlAnyElement(lax = true)
        private List<Element> elements;

        public Map<String, String> getData() {
            Map<String, String> map = new LinkedHashMap<>();
            for (Element e : elements) {
                map.put(e.getTagName(), e.getTextContent());
            }
            return map;
        }
    }
}
