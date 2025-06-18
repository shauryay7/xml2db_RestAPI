package com.example.xml2db.controller;

import com.example.xml2db.model.DatabaseDef;
import com.example.xml2db.model.TableDef;
import com.example.xml2db.util.XmlParser;
import jakarta.xml.bind.JAXBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class XmlUploadController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/upload-xml")
    public String uploadXml(@RequestBody String xmlContent) {
        try {
            DatabaseDef db = XmlParser.parse(xmlContent);

            for (TableDef table : db.getTables()) {
                StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
                sb.append(table.getName()).append(" (");

                table.getColumns().forEach(col -> {
                    sb.append(col.getName())
                            .append(" ")
                            .append(col.getType())
                            .append(", ");
                });

                sb.setLength(sb.length() - 2);
                sb.append(");");

                jdbcTemplate.execute(sb.toString());
            }

            return "Tables created successfully.";
        } catch (JAXBException e) {
            return "XML parsing error: " + e.getMessage();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
