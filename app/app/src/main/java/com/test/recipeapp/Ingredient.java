
package com.test.recipeapp;

public class Ingredient {
    private String name;
    private String Thumbnail;
    private boolean selected;

    Ingredient(String name, String thumbnail) {
        this.name = name;
        Thumbnail = "https://spoonacular.com/cdn/ingredients_100x100/" + thumbnail;
        selected = false;
    }

    public String getName() {
        return name;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected() {
        selected = !selected;
    }
}
