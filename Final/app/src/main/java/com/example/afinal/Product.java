package com.example.afinal;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class Product implements Parcelable {
    private int id;
    private String brand;
    private String name;
    private double price;
    private String price_sign;
    private String currency;
    private String image_link;
    private String product_link;
    private String website_link;
    private String description;

    public Product(int id, String name, String image_link, double price, String rating, String description, String product_type) {
        this.id = id;
        this.name = name;
        this.image_link = image_link;
        this.price = price;
        this.rating = rating;
        this.description = description;
        this.product_type = product_type;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    private String rating;
    private List<String> tag_list;
    private String category;
    private String product_type;
    private List<ProductColor> product_colors;

    // Constructor
    public Product(int id, String brand, String name, double price, String price_sign, String currency, String image_link, String product_link, String website_link, String description, List<String> tag_list, String category, String product_type, List<ProductColor> product_colors,String rating) {
        this.id = id;
        this.brand = brand;
        this.name = name;
        this.price = price;
        this.price_sign = price_sign;
        this.currency = currency;
        this.image_link = image_link;
        this.product_link = product_link;
        this.website_link = website_link;
        this.description = description;
        this.tag_list = tag_list;
        this.category = category;
        this.product_type = product_type;
        this.product_colors = product_colors;
        this.rating=rating;
    }

    protected Product(Parcel in) {
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
        tag_list = in.createStringArrayList();
        category = in.readString();
        product_type = in.readString();
        rating = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    // Getter methods
    public int getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getPrice_sign() {
        return price_sign;
    }

    public String getCurrency() {
        return currency;
    }

    public String getImage_link() {
        return image_link;
    }

    public String getProduct_link() {
        return product_link;
    }

    public String getWebsite_link() {
        return website_link;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getTag_list() {
        return tag_list;
    }

    public String getCategory() {
        return category;
    }

    public String getProduct_type() {
        return product_type;
    }

    public List<ProductColor> getProduct_colors() {
        return product_colors;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
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
        dest.writeStringList(tag_list);
        dest.writeString(category);
        dest.writeString(product_type);
        dest.writeString(rating);

    }

    // Setter methods (if needed)
    // Note: It's a good practice to include setter methods for mutable fields

    // Inner class representing product color
    public static class ProductColor {
        private String hex_value;
        private String colour_name;

        public ProductColor(String hex_value, String colour_name) {
            this.hex_value = hex_value;
            this.colour_name = colour_name;
        }

        public String getHex_value() {
            return hex_value;
        }

        public String getColour_name() {
            return colour_name;
        }
    }
}
