# XML-to-Database Dynamic CRUD API (Spring Boot + MySQL)

This project allows you to upload an XML file to create MySQL tables dynamically and perform CRUD operations (Insert, Read, Update, Delete) using XML payloads through REST APIs.

---

## 🛠 Technologies Used
- Spring Boot 3.x
- Java 17+
- MySQL
- JAXB (XML Parsing)
- Spring Data JPA
- NamedParameterJdbcTemplate

---

## ⚙️ Setup Instructions

### 1. Clone the Project
```
git clone https://github.com/your-username/xml2db.git
cd xml2db
```

### 2. Configure MySQL

Create a database in MySQL:
```sql
CREATE DATABASE xml2db;
```

Update `src/main/resources/application.properties` with your DB credentials:
```
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Build & Run
```
./mvnw spring-boot:run
```
OR
```
mvn clean install
java -jar target/xml2db-0.0.1-SNAPSHOT.jar
```

---

## 📦 API Endpoints

### ✅ Upload XML to Create Tables
**POST** `/upload-xml`  
**Body (XML):**
```xml
<database>
    <table name="student">
        <column name="id" type="INT"/>
        <column name="name" type="VARCHAR(100)"/>
        <column name="email" type="VARCHAR(100)"/>
    </table>
</database>
```

---

### ✅ Insert Data (Dynamic)
**POST** `/insert-xml`  
**Body (XML):**
```xml
<insert table="student">
    <row>
        <id>1</id>
        <name>John</name>
        <email>john@example.com</email>
    </row>
</insert>
```

---

### ✅ Get All Data
**GET** `/data/student`

---

### ✅ Get by ID
**GET** `/data/student/1`

---

### ✅ Update Data
**PUT** `/update-xml`  
**Body (XML):**
```xml
<update table="student">
    <row>
        <id>1</id>
        <name>John Updated</name>
        <email>john.updated@example.com</email>
    </row>
</update>
```

---

### ✅ Delete Data
**DELETE** `/delete-xml`  
**Body (XML):**
```xml
<delete table="student">
    <id>1</id>
</delete>
```

---

## 📌 Notes
- All operations are dynamic for any table created from XML.
- All insert/update/delete payloads must be valid XML.
- Use Postman to test all APIs with `Content-Type: application/xml`.

---

## 📧 Contact
For questions or suggestions, feel free to raise an issue or PR.
