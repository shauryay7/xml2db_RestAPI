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
        @XmlElement
        private String item_id;

        @XmlElement
        private String item_description;

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getItem_description() {
            return item_description;
        }

        public void setItem_description(String item_description) {
            this.item_description = item_description;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class YksItemImg {
        @XmlElement
        private String img_url;

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }
    }
}
