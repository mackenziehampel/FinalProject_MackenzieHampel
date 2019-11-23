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

    @Override
    public String toString() {
        return "Photos{" +
                "p_id=" + p_id +
                ", id='" + id + '\'' +
                ", img_src='" + img_src + '\'' +
                ", earth_date='" + earth_date + '\'' +
                '}';
    }

    public Product(String id, String img_src, String earth_date) {
        this.id = id;
        this.img_src = img_src;
        this.earth_date = earth_date;
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
}
