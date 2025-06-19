# XML to Database + Dynamic Code Generator

This Spring Boot project allows you to:
1. Upload an XML file that defines table structure.
2. Create the corresponding table in a MySQL database.
3. Dynamically generate full CRUD-enabled Java source files (`Model`, `Repository`, `Service`, `Controller`) for each table.

## ✅ Features
- Upload XML via REST API
- Creates MySQL tables dynamically
- Generates Java CRUD components for each table in: `src/main/java/com/example/generated/{table}/`
- Insert/Update/Delete/Get APIs work via XML too

## 🚀 How to Run

### 1. Configure MySQL
Update your `application.properties` with your MySQL credentials.

### 2. Run the app
```bash
./mvnw spring-boot:run
```

### 3. Upload XML
Use Postman to POST the following to `http://localhost:8080/upload-xml`:

```xml
<database>
    <table name="student">
        <column name="id" type="INT"/>
        <column name="name" type="VARCHAR(100)"/>
        <column name="email" type="VARCHAR(100)"/>
    </table>
</database>
```

### 4. Check output
- MySQL: `student` table is created
- Java: CRUD files generated in `src/main/java/com/example/generated/student/`

## 🧪 Test Other APIs

### Insert
POST `/insert-xml`
```xml
<insert table="student">
    <row>
        <id>1</id>
        <name>John</name>
        <email>john@example.com</email>
    </row>
</insert>
```

### Update
PUT `/update-xml`
```xml
<update table="student">
    <row>
        <id>1</id>
        <name>John Updated</name>
        <email>updated@example.com</email>
    </row>
</update>
```

### Delete
DELETE `/delete-xml`
```xml
<delete table="student">
    <id>1</id>
</delete>
```

## 📁 Output Example

```
src/main/java/com/example/generated/student/
├── Student.java
├── StudentRepository.java
├── StudentService.java
└── StudentController.java
```

---

## 👨‍💻 Author
Made using Spring Boot and MySQL
