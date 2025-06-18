package com.example.xml2db.service;

import com.example.xml2db.model.DeleteRequest;
import com.example.xml2db.model.InsertRequest;
import com.example.xml2db.model.UpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DynamicCrudService {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public void insert(InsertRequest request) {
        String table = request.getTable();
        Map<String, String> row = request.getRow();

        StringBuilder sql = new StringBuilder("INSERT INTO ")
                .append(table).append(" (")
                .append(String.join(", ", row.keySet()))
                .append(") VALUES (");

        String paramStr = String.join(", ", row.keySet().stream().map(k -> ":" + k).toList());
        sql.append(paramStr).append(")");

        jdbcTemplate.update(sql.toString(), row);
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

        row.put("id", id); // re-insert for binding
        jdbcTemplate.update(sql, row);
    }

    public void delete(DeleteRequest request) {
        String sql = "DELETE FROM " + request.getTable() + " WHERE id = :id";
        Map<String, String> params = Map.of("id", request.getId());
        jdbcTemplate.update(sql, params);
    }
}
