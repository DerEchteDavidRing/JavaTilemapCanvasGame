package com.example.zawarudo;

import android.graphics.Canvas;

public class GameLoopThread extends Thread {
    private GameView view;
    private boolean running = false;

    public GameLoopThread(GameView view){
        this.view = view;
    }
    public void setRunning(boolean run){
        running = run;
    }

    @Override
    public void run(){
        Canvas c = null;

        while (running){

            try {
                currentThread().sleep(17);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try{
                c = view.getHolder().lockHardwareCanvas();
                c.rotate(90);
                synchronized (view.getHolder()){
                    view.onDraw(c);
                }
            } finally {
                {

    view.getHolder().unlockCanvasAndPost(c);

                        }
                    }
                }
            }
        }