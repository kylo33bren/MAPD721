package com.test.recipeapp;

import java.io.Serializable;

public class CategoryRecipeModel implements Serializable {


    String cat_name;
    int image;

    public CategoryRecipeModel(String cat_name, int image) {
        this.cat_name = cat_name;
        this.image = image;
    }

    public CategoryRecipeModel() {
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
