package com.example.xml2db.controller;

import com.example.xml2db.model.DeleteRequest;
import com.example.xml2db.model.InsertRequest;
import com.example.xml2db.model.UpdateRequest;
import com.example.xml2db.service.DynamicCrudService;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.StringReader;

@RestController
public class DynamicCrudController {

    @Autowired
    private DynamicCrudService crudService;

    @PostMapping("/insert-xml")
    public ResponseEntity<String> insertXml(@RequestBody String xmlData) {
        try {
            JAXBContext context = JAXBContext.newInstance(InsertRequest.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InsertRequest request = (InsertRequest) unmarshaller.unmarshal(new StringReader(xmlData));
            crudService.insert(request);
            return ResponseEntity.ok("Insert successful");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Insert failed: " + e.getMessage());
        }
    }

    @GetMapping("/data/{table}")
    public ResponseEntity<?> getAll(@PathVariable String table) {
        try {
            return ResponseEntity.ok(crudService.getAll(table));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/data/{table}/{id}")
    public ResponseEntity<?> getById(@PathVariable String table, @PathVariable String id) {
        try {
            return ResponseEntity.ok(crudService.getById(table, id));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/update-xml")
    public ResponseEntity<String> updateXml(@RequestBody String xmlData) {
        try {
            JAXBContext context = JAXBContext.newInstance(UpdateRequest.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            UpdateRequest request = (UpdateRequest) unmarshaller.unmarshal(new StringReader(xmlData));
            crudService.update(request);
            return ResponseEntity.ok("Update successful");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Update failed: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-xml")
    public ResponseEntity<String> deleteXml(@RequestBody String xmlData) {
        try {
            JAXBContext context = JAXBContext.newInstance(DeleteRequest.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            DeleteRequest request = (DeleteRequest) unmarshaller.unmarshal(new StringReader(xmlData));
            crudService.delete(request);
            return ResponseEntity.ok("Delete successful");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Delete failed: " + e.getMessage());
        }
    }
}
