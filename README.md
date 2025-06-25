# XML2DB Inventory System - Postman API Guide

This project allows you to manage inventory data using XML upload, dynamic SQL table creation, and full CRUD functionality via Postman.

---

## 🔁 1. Upload & Insert Inventory Item via XML
- **Endpoint:** `POST /upload-xml`
- **Headers:** `Content-Type: application/xml`
- **Body (raw, XML):**
```xml
<insert>
    <yks_item>
        <item_id>ITEM001</item_id>
        <item_description>Sample item description</item_description>
        <stock>5</stock>
    </yks_item>
    <yks_item_img>
        <img_url>https://example.com/image.jpg</img_url>
    </yks_item_img>
</insert>
```

---

## 🏗️ 2. Create SQL Tables Dynamically via XML
- **Endpoint:** `POST /create-tables`
- **Headers:** `Content-Type: application/xml`
- **Body:**
```xml
<database>
    <table name="yks_item">
        <column name="item_key" type="INT" primaryKey="true" autoIncrement="true"/>
        <column name="item_id" type="VARCHAR(100)"/>
        <column name="item_description" type="TEXT"/>
        <column name="stock" type="INT"/>
    </table>
    <table name="yks_item_img">
        <column name="item_img_key" type="INT" primaryKey="true" autoIncrement="true"/>
        <column name="item_key" type="INT"/>
        <column name="img_url" type="TEXT"/>
    </table>
    <foreignKey table="yks_item_img" column="item_key" references="yks_item(item_key)"/>
</database>
```

---

## 🛒 3. Purchase Item
- **Endpoint:** `POST /purchase?item_id=ITEM001`
- Decreases stock by 1 if available and logs purchase in `purchased_order`.

---

## 📜 4. View Purchase History
- **Endpoint:** `GET /purchase/history`
- Returns all purchase records.

---

## 🧾 5. View All Inventory
- **Endpoint:** `GET /yks_item`
- Returns all inventory items.

---

## ♻️ 6. Update Item (e.g., change stock)
- **Endpoint:** `POST /update`
- **Headers:** `Content-Type: application/xml`
- **Body:**
```xml
<update table="yks_item">
    <row>
        <id>1</id>
        <stock>10</stock>
        <item_description>Updated description</item_description>
    </row>
</update>
```

---

## 🗑️ 7. Delete Item and Its Image
- **Endpoint:** `POST /delete`
- **Headers:** `Content-Type: application/xml`
- **Body:**
```xml
<delete table="yks_item">
    <id>1</id>
</delete>
```

This deletes the record from `yks_item` and also removes the associated `yks_item_img` via item_key.

---
