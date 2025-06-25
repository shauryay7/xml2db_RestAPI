package com.example.xml2db.model;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "insert")
@XmlAccessorType(XmlAccessType.FIELD)
public class InsertRequest {

    @XmlElement(name = "yks_item")
    private YksItem yksItem;

    @XmlElement(name = "yks_item_img")
    private YksItemImg yksItemImg;

    public YksItem getYksItem() {
        return yksItem;
    }

    public void setYksItem(YksItem yksItem) {
        this.yksItem = yksItem;
    }

    public YksItemImg getYksItemImg() {
        return yksItemImg;
    }

    public void setYksItemImg(YksItemImg yksItemImg) {
        this.yksItemImg = yksItemImg;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class YksItem {

        @XmlElement(name = "item_id")
        private String itemId;

        @XmlElement(name = "item_description")
        private String itemDescription;

        @XmlElement(name = "stock")
        private Integer stock;

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getItemDescription() {
            return itemDescription;
        }

        public void setItemDescription(String itemDescription) {
            this.itemDescription = itemDescription;
        }

        public Integer getStock() {
            return stock;
        }

        public void setStock(Integer stock) {
            this.stock = stock;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class YksItemImg {

        @XmlElement(name = "img_url")
        private String imgUrl;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }
}
