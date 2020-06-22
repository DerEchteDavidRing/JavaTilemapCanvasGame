package com.example.zawarudo;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Villager {

    public int numberOfVillagers = 1;

    public int targetX;
    public int targetY;

    public boolean selectedState = false;

    public void setSelectedState(boolean yesOrNo){
        selectedState=yesOrNo;
    }
    public boolean getSelectedState(){
        return selectedState;
    }
    public boolean hasReachedGoal = true;
    public void isAtGoal(boolean yesOrNo){
        hasReachedGoal=yesOrNo;
    }
    public boolean getisAtGoal(){
        return hasReachedGoal;
    }
    public int movespeed = 10;



    public int x;
    public int y;
    public int mouseX;
    public int mouseY;
    public String type;
    public Bitmap villagerBitmap;

    Villager(String type, Bitmap bitmap, int x, int y){
        this.villagerBitmap=bitmap;
        this.type=type;
        this.x=x;
        this.y=y;
    }


    public void giveMouseCoordinates(int mouseX,int mouseY){
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }


    public boolean checkSelected(){
        if (mouseX>getVillagerX() && mouseX<getVillagerX()+villagerBitmap.getWidth()){
            if (mouseY>getVillagerY() &&mouseY<getVillagerY()+villagerBitmap.getHeight()){
                selectedState = true;
            }
        }
        else{
            //selectedState=false;
        }
        return selectedState;
    }

    public void unselect(){
        selectedState=false;
    }


    public void move(){
        if (targetX>getVillagerX()){
            addToVillagerX(movespeed*2);
        }
        if (targetX<getVillagerX()){
            addToVillagerX(-movespeed*2);
        }
        if (targetY>getVillagerY()){
            addToVillagerY(movespeed);
        }
        if (targetY<getVillagerY()){
            addToVillagerY(-movespeed);
        }

    }


    public void setTargetX(int num){
        targetX = num;
    }
    public void setTargetY(int num){
        targetY = num;
    }

    public void setVillagerBitmap(Bitmap image){villagerBitmap = image;
    }

    public Bitmap getVillagerBitmap(){
        return villagerBitmap;
    }

    public void addToVillagerX(int amount){
       x+=amount;
    }

    public void addToVillagerY(int amount){
        y+=amount;
    }


    //Y AND X SWAPPED!! .. ?

    public int getVillagerY() {
        return y+cameradistanceY;
    }
    public int getVillagerX() {
        return x+cameradistanceX;
    }

    public int getTargetX() {
        return targetX+cameradistanceX;
    }
    public int getTargetY() {
        return targetY+cameradistanceY;
    }


    public int cameradistanceX=0;
    public int cameradistanceY=0;

    public void setCameraDistance(int x,int y){
        cameradistanceX= x;
        cameradistanceY= y;
    }


    public Rect[] villagerRect = new Rect[numberOfVillagers];
    public int getNumberOfVillagers;

    public void setNumberOfVillagers(int num){
        numberOfVillagers = num;
    }

}
