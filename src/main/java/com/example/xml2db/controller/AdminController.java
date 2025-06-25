package com.example.xml2db.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // DELETE all rows from main tables
    @DeleteMapping("/clear")
    public String clearAllData() {
        try {
            // Delete from child tables first due to foreign key constraints
            jdbcTemplate.update("DELETE FROM yks_item_img");
            jdbcTemplate.update("DELETE FROM purchased_order");
            jdbcTemplate.update("DELETE FROM yks_item");
            return "✅ All data cleared successfully.";
        } catch (Exception e) {
            return "❌ Failed to clear data: " + e.getMessage();
        }
    }
}
