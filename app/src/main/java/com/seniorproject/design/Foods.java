package com.seniorproject.design;

import com.google.firebase.Timestamp;

public class Foods {

    String foodName;
    String ExpDate;
    String foodType;
    Timestamp timestamp;

    public Foods() {
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getExpDate() {
        return ExpDate;
    }

    public void setExpDate(String expDate) {
        ExpDate = expDate;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String expDate) {
        foodType = foodType;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
