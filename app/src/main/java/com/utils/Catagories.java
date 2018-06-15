package com.utils;

/**
 * Created by devin on 2016-12-20.
 */
public class Catagories {
    private String category_id;
    private String cat_name;
    private String cat_desc;
    private String cat_base_price;
    private String cat_fare_per_km;
    private String cat_fare_per_min;
    private String cat_max_size;
    private String cat_is_fixed_price;
    private String cat_prime_time_percentage;
    private String cat_status;
    private String cat_created;
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    boolean isSelected;


    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_desc() {
        return cat_desc;
    }

    public void setCat_desc(String cat_desc) {
        this.cat_desc = cat_desc;
    }

    public String getCat_base_price() {
        return cat_base_price;
    }

    public void setCat_base_price(String cat_base_price) {
        this.cat_base_price = cat_base_price;
    }

    public String getCat_fare_per_km() {
        return cat_fare_per_km;
    }

    public void setCat_fare_per_km(String cat_fare_per_km) {
        this.cat_fare_per_km = cat_fare_per_km;
    }

    public String getCat_fare_per_min() {
        return cat_fare_per_min;
    }

    public void setCat_fare_per_min(String cat_fare_per_min) {
        this.cat_fare_per_min = cat_fare_per_min;
    }

    public String getCat_max_size() {
        return cat_max_size;
    }

    public void setCat_max_size(String cat_max_size) {
        this.cat_max_size = cat_max_size;
    }

    public String getCat_is_fixed_price() {
        return cat_is_fixed_price;
    }

    public void setCat_is_fixed_price(String cat_is_fixed_price) {
        this.cat_is_fixed_price = cat_is_fixed_price;
    }

    public String getCat_prime_time_percentage() {
        return cat_prime_time_percentage;
    }

    public void setCat_prime_time_percentage(String cat_prime_time_percentage) {
        this.cat_prime_time_percentage = cat_prime_time_percentage;
    }

    public String getCat_status() {
        return cat_status;
    }

    public void setCat_status(String cat_status) {
        this.cat_status = cat_status;
    }

    public String getCat_created() {
        return cat_created;
    }

    public void setCat_created(String cat_created) {
        this.cat_created = cat_created;
    }

    public String getCat_modified() {
        return cat_modified;
    }

    public void setCat_modified(String cat_modified) {
        this.cat_modified = cat_modified;
    }

    private String cat_modified;

}
