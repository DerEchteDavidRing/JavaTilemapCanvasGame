package com.example.zawarudo;

public class Camera {
    public int cameraX;
    public int cameraY;


    public void addToCameraX(int num){
        cameraX += num;
    }
    public void addToCameraY(int num){
        cameraY += num;
    }


    public int getCameraX(){
        return cameraY;
    }
    public int getCameraY(){
        return cameraX;
    }


}
