package com.example.xml2db.controller;

import com.example.xml2db.model.ColumnDef;
import com.example.xml2db.model.DatabaseDef;
import com.example.xml2db.model.ForeignKeyDef;
import com.example.xml2db.model.TableDef;
import com.example.xml2db.service.CodeGeneratorService;
import com.example.xml2db.util.XmlParser;
import jakarta.xml.bind.JAXBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class XmlUploadController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    @PostMapping("/upload-xml")
    public String uploadXml(@RequestBody String xmlContent) {
        try {
            DatabaseDef db = XmlParser.parse(xmlContent);

            for (TableDef table : db.getTables()) {
                StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
                sb.append(table.getName()).append(" (");

                // Add columns with flags
                for (ColumnDef col : table.getColumns()) {
                    sb.append(col.getName())
                            .append(" ")
                            .append(col.getType());

                    if (col.isAutoIncrement()) {
                        sb.append(" AUTO_INCREMENT NOT NULL"); // MySQL requires NOT NULL with AUTO_INCREMENT
                    }

                    sb.append(", ");
                }

                // Add primary key constraint separately (to support composite keys later if needed)
                boolean primaryKeyAdded = false;
                for (ColumnDef col : table.getColumns()) {
                    if (col.isPrimaryKey()) {
                        sb.append("PRIMARY KEY (").append(col.getName()).append("), ");
                        primaryKeyAdded = true;
                    }
                }

                // Add foreign keys if any
                if (table.getForeignKeys() != null) {
                    for (ForeignKeyDef fk : table.getForeignKeys()) {
                        String[] refParts = fk.getReferences().split("\\(");
                        String refTable = refParts[0];
                        String refColumn = refParts[1].replace(")", "");
                        sb.append("FOREIGN KEY (")
                                .append(fk.getColumn())
                                .append(") REFERENCES ")
                                .append(refTable)
                                .append("(")
                                .append(refColumn)
                                .append("), ");
                    }
                }

                // Remove trailing comma and space
                if (sb.lastIndexOf(", ") == sb.length() - 2) {
                    sb.setLength(sb.length() - 2);
                }

                sb.append(");");

                // Execute SQL
                jdbcTemplate.execute(sb.toString());

                // Generate CRUD Java code dynamically
                codeGeneratorService.generateCode(table);
            }

            return "Tables created and source code generated successfully.";
        } catch (JAXBException e) {
            return "XML parsing error: " + e.getMessage();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
