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
            String insertItemSql = "INSERT INTO yks_item (item_id, item_description, stock) VALUES (?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.getJdbcTemplate().update(connection -> {
                PreparedStatement ps = connection.prepareStatement(insertItemSql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, yksItem.getItemId());
                ps.setString(2, yksItem.getItemDescription());
                ps.setObject(3, yksItem.getStock() != null ? yksItem.getStock() : 0); // default stock to 0
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
            imgParams.put("img_url", yksItemImg.getImgUrl());
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
        if (!"yks_item".equalsIgnoreCase(request.getTable())) {
            throw new UnsupportedOperationException("Delete only supported for 'yks_item' table in this implementation.");
        }

        // 1. Fetch item_key using item_id
        String getKeySql = "SELECT item_key FROM yks_item WHERE item_id = :item_id";
        Map<String, Object> param = Map.of("item_id", request.getId());

        List<Map<String, Object>> result = jdbcTemplate.queryForList(getKeySql, param);

        if (result.isEmpty()) {
            throw new IllegalArgumentException("Item with item_id '" + request.getId() + "' not found.");
        }

        int itemKey = (int) result.get(0).get("item_key");

        // 2. Delete from yks_item_img (child table)
        String deleteChildSql = "DELETE FROM yks_item_img WHERE item_key = :item_key";
        jdbcTemplate.update(deleteChildSql, Map.of("item_key", itemKey));

        // 3. Delete from yks_item (parent table)
        String deleteParentSql = "DELETE FROM yks_item WHERE item_key = :item_key";
        jdbcTemplate.update(deleteParentSql, Map.of("item_key", itemKey));
    }

}
