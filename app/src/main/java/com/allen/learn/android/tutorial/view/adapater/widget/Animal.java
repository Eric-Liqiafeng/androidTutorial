package com.allen.learn.android.tutorial.view.adapater.widget;

public class Animal {

    private String animal;
    private int imgId;

    public Animal(String animal, int imgId) {
        this.animal = animal;
        this.imgId = imgId;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
