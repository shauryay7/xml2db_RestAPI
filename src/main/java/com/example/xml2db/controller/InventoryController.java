package com.example.xml2db.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/buy/{item_id}")
    public String buyItem(@PathVariable String item_id, @RequestParam(defaultValue = "1") int quantity) {
        try {
            // Step 1: Fetch item by item_id
            Map<String, Object> item = jdbcTemplate.queryForMap("SELECT * FROM yks_item WHERE item_id = ?", item_id);

            // Step 2: Check stock availability
            int stock = item.get("stock") != null ? ((Number) item.get("stock")).intValue() : 0;
            if (stock < quantity) {
                return "Insufficient stock.";
            }

            // Step 3: Reduce stock
            jdbcTemplate.update("UPDATE yks_item SET stock = stock - ? WHERE item_id = ?", quantity, item_id);

            // Step 4: Insert into purchased_order
            jdbcTemplate.update(
                    "INSERT INTO purchased_order (item_key, item_id, quantity, timestamp) VALUES (?, ?, ?, ?)",
                    item.get("item_key"),
                    item_id,
                    quantity,
                    new Timestamp(System.currentTimeMillis())
            );

            return "Purchase successful.";
        } catch (EmptyResultDataAccessException e) {
            return "Item not found.";
        } catch (Exception e) {
            return "Error during purchase: " + e.getMessage();
        }
    }
}
