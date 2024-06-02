package com.example.afinal;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResult implements Parcelable {
    @SerializedName("id")
    private int id;
    private String brand;
    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private double price;
    private String price_sign;

    public SearchResult(int id, String name,double price,String image_link , String product_type, String rating,String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image_link = image_link;
        this.product_type = product_type;
        this.rating = rating;
        this.description = description;
    }

    private String currency;
    @SerializedName("image_link")
    private String image_link;
    private String product_link;
    private String website_link;
    @SerializedName("description")
    private String description;
    @SerializedName("id")
    private String rating;
    private List<String> tag_list;
    private String category;
    @SerializedName("product_type")
    private String product_type;
    private List<Product.ProductColor> product_colors;

    protected SearchResult(Parcel in) {
        id = in.readInt();
        brand = in.readString();
        name = in.readString();
        price = in.readDouble();
        price_sign = in.readString();
        currency = in.readString();
        image_link = in.readString();
        product_link = in.readString();
        website_link = in.readString();
        description = in.readString();
        rating = in.readString();
        tag_list = in.createStringArrayList();
        category = in.readString();
        product_type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(brand);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeString(price_sign);
        dest.writeString(currency);
        dest.writeString(image_link);
        dest.writeString(product_link);
        dest.writeString(website_link);
        dest.writeString(description);
        dest.writeString(rating);
        dest.writeStringList(tag_list);
        dest.writeString(category);
        dest.writeString(product_type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SearchResult> CREATOR = new Creator<SearchResult>() {
        @Override
        public SearchResult createFromParcel(Parcel in) {
            return new SearchResult(in);
        }

        @Override
        public SearchResult[] newArray(int size) {
            return new SearchResult[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPrice_sign() {
        return price_sign;
    }

    public void setPrice_sign(String price_sign) {
        this.price_sign = price_sign;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getProduct_link() {
        return product_link;
    }

    public void setProduct_link(String product_link) {
        this.product_link = product_link;
    }

    public String getWebsite_link() {
        return website_link;
    }

    public void setWebsite_link(String website_link) {
        this.website_link = website_link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public List<String> getTag_list() {
        return tag_list;
    }

    public void setTag_list(List<String> tag_list) {
        this.tag_list = tag_list;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public List<Product.ProductColor> getProduct_colors() {
        return product_colors;
    }

    public void setProduct_colors(List<Product.ProductColor> product_colors) {
        this.product_colors = product_colors;
    }
}
