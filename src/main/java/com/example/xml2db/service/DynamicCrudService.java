package com.example.xml2db.service;

import com.example.xml2db.model.DeleteRequest;
import com.example.xml2db.model.InsertRequest;
import com.example.xml2db.model.UpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DynamicCrudService {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public void insert(InsertRequest request) {
        InsertRequest.YksItem yksItem = request.getYksItem();
        InsertRequest.YksItemImg yksItemImg = request.getYksItemImg();

        if (yksItem != null && yksItemImg != null) {
            // 1. Insert into yks_item (parent)
            String insertItemSql = "INSERT INTO yks_item (item_id, item_description) VALUES (?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.getJdbcTemplate().update(connection -> {
                PreparedStatement ps = connection.prepareStatement(insertItemSql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, yksItem.getItem_id());
                ps.setString(2, yksItem.getItem_description());
                return ps;
            }, keyHolder);

            Number itemKey = keyHolder.getKey();
            if (itemKey == null) {
                throw new IllegalStateException("Failed to retrieve generated item_key for yks_item.");
            }

            // 2. Insert into yks_item_img (child)
            String insertImgSql = "INSERT INTO yks_item_img (item_key, img_url) VALUES (:item_key, :img_url)";
            Map<String, Object> imgParams = new HashMap<>();
            imgParams.put("item_key", itemKey.longValue());
            imgParams.put("img_url", yksItemImg.getImg_url());
            jdbcTemplate.update(insertImgSql, imgParams);

        } else {
            throw new IllegalArgumentException("Invalid insert request: missing yks_item or yks_item_img.");
        }
    }

    public List<Map<String, Object>> getAll(String table) {
        String sql = "SELECT * FROM " + table;
        return jdbcTemplate.getJdbcTemplate().queryForList(sql);
    }

    public Map<String, Object> getById(String table, String id) {
        String sql = "SELECT * FROM " + table + " WHERE id = ?";
        return jdbcTemplate.getJdbcTemplate().queryForMap(sql, id);
    }

    public void update(UpdateRequest request) {
        String table = request.getTable();
        Map<String, String> row = request.getRow();

        if (!row.containsKey("id")) {
            throw new RuntimeException("Missing 'id' for update");
        }

        String id = row.remove("id");
        String setClause = String.join(", ", row.keySet().stream().map(k -> k + "=:" + k).toList());
        String sql = "UPDATE " + table + " SET " + setClause + " WHERE id = :id";

        row.put("id", id);
        jdbcTemplate.update(sql, row);
    }

    public void delete(DeleteRequest request) {
        String sql = "DELETE FROM " + request.getTable() + " WHERE id = :id";
        Map<String, String> params = Map.of("id", request.getId());
        jdbcTemplate.update(sql, params);
    }
}
