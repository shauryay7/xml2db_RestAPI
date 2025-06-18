package com.example.xml2db.service;

import com.example.xml2db.model.TableDef;
import com.example.xml2db.model.ColumnDef;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.List;

@Service
public class CodeGeneratorService {

    private static final String BASE_PACKAGE = "com.example.generated";

    public void generateCode(TableDef tableDef) throws Exception {
        String tableName = tableDef.getName();
        List<ColumnDef> columns = tableDef.getColumns();

        String className = capitalize(tableName);
        String packagePath = "src/main/java/" + BASE_PACKAGE.replace('.', '/') + "/" + tableName;

        Files.createDirectories(new File(packagePath).toPath());

        generateModel(packagePath, BASE_PACKAGE + "." + tableName, className, columns);
        generateRepository(packagePath, BASE_PACKAGE + "." + tableName, className);
        generateService(packagePath, BASE_PACKAGE + "." + tableName, className);
        generateController(packagePath, BASE_PACKAGE + "." + tableName, className);
    }

    private void generateModel(String path, String pkg, String className, List<ColumnDef> columns) throws Exception {
        StringBuilder sb = new StringBuilder("package ").append(pkg).append(";\n\n");
        sb.append("import jakarta.persistence.*;\n\n");
        sb.append("@Entity\npublic class ").append(className).append(" {\n");

        for (ColumnDef col : columns) {
            if ("id".equalsIgnoreCase(col.getName())) {
                sb.append("    @Id\n    @GeneratedValue(strategy = GenerationType.IDENTITY)\n");
            }
            sb.append("    private ").append(mapToJavaType(col.getType())).append(" ").append(col.getName()).append(";\n\n");
        }

        for (ColumnDef col : columns) {
            String type = mapToJavaType(col.getType());
            String name = col.getName();
            sb.append("    public ").append(type).append(" get").append(capitalize(name)).append("() { return ").append(name).append("; }\n");
            sb.append("    public void set").append(capitalize(name)).append("(").append(type).append(" ").append(name).append(") { this.").append(name).append(" = ").append(name).append("; }\n\n");
        }

        sb.append("}");
        saveFile(path + "/" + className + ".java", sb.toString());
    }

    private void generateRepository(String path, String pkg, String className) throws Exception {
        String code = String.format("""
            package %s;

            import org.springframework.data.jpa.repository.JpaRepository;

            public interface %sRepository extends JpaRepository<%s, Integer> {
            }
            """, pkg, className, className);

        saveFile(path + "/" + className + "Repository.java", code);
    }

    private void generateService(String path, String pkg, String className) throws Exception {
        String code = String.format("""
            package %s;

            import org.springframework.beans.factory.annotation.Autowired;
            import org.springframework.stereotype.Service;

            import java.util.List;
            import java.util.Optional;

            @Service
            public class %sService {

                @Autowired
                private %sRepository repository;

                public List<%s> getAll() {
                    return repository.findAll();
                }

                public Optional<%s> getById(Integer id) {
                    return repository.findById(id);
                }

                public %s save(%s entity) {
                    return repository.save(entity);
                }

                public void delete(Integer id) {
                    repository.deleteById(id);
                }
            }
            """, pkg, className, className, className, className, className, className);

        saveFile(path + "/" + className + "Service.java", code);
    }

    private void generateController(String path, String pkg, String className) throws Exception {
        String lower = className.toLowerCase();
        String code = String.format("""
            package %s;

            import org.springframework.beans.factory.annotation.Autowired;
            import org.springframework.web.bind.annotation.*;

            import java.util.List;
            import java.util.Optional;

            @RestController
            @RequestMapping("/api/%s")
            public class %sController {

                @Autowired
                private %sService service;

                @GetMapping
                public List<%s> getAll() {
                    return service.getAll();
                }

                @GetMapping("/{id}")
                public Optional<%s> getById(@PathVariable Integer id) {
                    return service.getById(id);
                }

                @PostMapping
                public %s create(@RequestBody %s entity) {
                    return service.save(entity);
                }

                @PutMapping
                public %s update(@RequestBody %s entity) {
                    return service.save(entity);
                }

                @DeleteMapping("/{id}")
                public void delete(@PathVariable Integer id) {
                    service.delete(id);
                }
            }
            """, pkg, lower, className, className, className, className, className, className, className, className);

        saveFile(path + "/" + className + "Controller.java", code);
    }

    private String mapToJavaType(String sqlType) {
        return switch (sqlType.toUpperCase()) {
            case "INT", "INTEGER" -> "Integer";
            case "VARCHAR", "TEXT", "CHAR" -> "String";
            case "FLOAT", "DOUBLE", "DECIMAL" -> "Double";
            default -> "String";
        };
    }

    private void saveFile(String filePath, String content) throws Exception {
        try (FileWriter fw = new FileWriter(filePath)) {
            fw.write(content);
        }
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
