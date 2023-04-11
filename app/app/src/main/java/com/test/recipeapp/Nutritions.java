package com.test.recipeapp;

public class Nutritions {

    String title,amount,indented,percentOfDailyNeeds;

    public Nutritions(String title, String amount, String indented, String percentOfDailyNeeds) {
        this.title = title;
        this.amount = amount;
        this.indented = indented;
        this.percentOfDailyNeeds = percentOfDailyNeeds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIndented() {
        return indented;
    }

    public void setIndented(String indented) {
        this.indented = indented;
    }

    public String getPercentOfDailyNeeds() {
        return percentOfDailyNeeds;
    }

    public void setPercentOfDailyNeeds(String percentOfDailyNeeds) {
        this.percentOfDailyNeeds = percentOfDailyNeeds;
    }

}
