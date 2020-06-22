package com.example.zawarudo;

import android.graphics.Bitmap;

public class Building {

    public int x;
    public int y;
    public String type;
    public Bitmap buildingBitmap;

    Building(String type, Bitmap bitmap, int x, int y){
        this.buildingBitmap=bitmap;
        this.type=type;
        this.x=x;
        this.y=y;
    }

    public Bitmap getBuildingBitmap() {
        return buildingBitmap;
    }


}
