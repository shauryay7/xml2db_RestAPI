package com.example.xml2db.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping
    public String purchaseItem(@RequestParam String item_id) {
        try {
            // Step 1: Fetch item with stock > 0
            List<Map<String, Object>> items = jdbcTemplate.queryForList(
                    "SELECT * FROM yks_item WHERE item_id = ? AND stock > 0 LIMIT 1", item_id);

            if (items.isEmpty()) {
                return "Item not found or out of stock.";
            }

            Map<String, Object> item = items.get(0);
            int item_key = (int) item.get("item_key");
            String item_description = (String) item.get("item_description");

            // Step 2: Insert into purchased_order
            String sql = "INSERT INTO purchased_order (item_key, item_id, item_description) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, item_key, item_id, item_description);

            // Step 3: Decrease stock
            jdbcTemplate.update("UPDATE yks_item SET stock = stock - 1 WHERE item_key = ?", item_key);

            return "Item purchased successfully.";
        } catch (Exception e) {
            return "Error during purchase: " + e.getMessage();
        }
    }

    // ✅ Get all purchase history
    @GetMapping("/history")
    public List<Map<String, Object>> getAllPurchases() {
        return jdbcTemplate.queryForList("SELECT * FROM purchased_order");
    }
}
