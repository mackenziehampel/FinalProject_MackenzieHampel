package com.example.finalproject_hampel.db;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Product {
    @PrimaryKey(autoGenerate = true)
    private int p_id;
    private String id;
    private String img_src;
    private String earth_date;
    private String rover_name;
    private String price;
    private String acres;
    private String pricePerSqFt;

    @Override
    public String toString() {
        return "Photos{" +
                "p_id=" + p_id +
                ", id='" + id + '\'' +
                ", img_src='" + img_src + '\'' +
                ", earth_date='" + earth_date + '\'' +
                '}';
    }

    public Product(String id, String img_src, String earth_date, String rover_name, String price, String acres, String pricePerSqFt) {
        this.id = id;
        this.img_src = img_src;
        this.earth_date = earth_date;
        this.rover_name = rover_name;
        this.price = price;
        this.acres = acres;
        this.pricePerSqFt = pricePerSqFt;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg_src() {
        return img_src;
    }

    public void setImg_src(String img_src) {
        this.img_src = img_src;
    }

    public String getEarth_date() {
        return earth_date;
    }

    public void setEarth_date(String earth_date) {
        this.earth_date = earth_date;
    }
    public String getRover_name() {
        return rover_name;
    }

    public void setRover_name(String rover_name) {
        this.rover_name = rover_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAcres() {
        return acres;
    }

    public void setAcres(String acres) {
        this.acres = acres;
    }

    public String getPricePerSqFt() {
        return pricePerSqFt;
    }

    public void setPricePerSqFt(String pricePerSqFt) {
        this.pricePerSqFt = pricePerSqFt;
    }
}
