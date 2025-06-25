package com.example.xml2db.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/yks_item")
public class ItemController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<Map<String, Object>> getAllItemsWithImages() {
        String sql = """
            SELECT i.item_key, i.item_id, i.item_description, i.stock, img.img_url
            FROM yks_item i
            LEFT JOIN yks_item_img img ON i.item_key = img.item_key
        """;
        return jdbcTemplate.queryForList(sql);
    }
}
