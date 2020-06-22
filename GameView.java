package com.example.zawarudo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView{
    public GameMap tileMap = new GameMap();
    Bitmap[] grassTileMapHolder = new Bitmap[tileMap.getNumberOfTiles()];
    private SurfaceHolder holder;
    private GameLoopThread gameThread;


    public int screenheight =1800;
    //public int screenwidth =1080;
    public int screenwidth = 1080;

    //sprite data
    static Paint paint = new Paint();
    static Paint textpaint = new Paint();
    public int numberOfTiles = tileMap.numberOfTiles;

    private Bitmap bmp;
    private Bitmap xy;

    private Rect r = new Rect(0, 0, 50,50);
    //private Rect screenR = new Rect(0, 0,screenwidth+200 ,screenheight+200);
    private Rect screenR = new Rect(0, 0,screenheight+200 ,screenwidth+200);

    private Rect cameraUpRectangle = new Rect(0, 0, 50,50);
    private Rect cameraDownRectangle = new Rect(0, 0, 50,50);
    private Rect cameraLeftRectangle = new Rect(0, 0, 50,50);
    private Rect cameraRightRectangle = new Rect(0, 0, 50,50);


    public int movespeed = 10;

    //////////////////////////////////////////////////////////////////////////////////




    public GameView(Context context){
        super(context);



        createMap();
        prepareBitmaps();
        readWhereToPlaceBuildings();

        createVillagers();

        screenR.offsetTo(-250, -screenwidth-200);
        gameThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                gameThread.setRunning(true);
                gameThread.start();
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
            public void surfaceChanged(SurfaceHolder holder) {
            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameThread.setRunning(false);
                while(retry){
                    try{
                        gameThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public static void initialize(){
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.MAGENTA);
        textpaint.setStyle(Paint.Style.FILL);
        textpaint.setColor(Color.RED);
        textpaint.setTextSize(90);

    }


    public int numberOfVillagers = 1;
    Villager[] villager = new Villager[numberOfVillagers];

    public void createVillagers(){
        for (int i=0;i<numberOfVillagers;i++) {
            villager[i] = new Villager("farmer",villagerbitmap,i*100,i*300);
        }
    }



    Bitmap villagerbitmap;
    Bitmap pines;
    Bitmap mountain;
    Bitmap groundtexture;

    Bitmap arrow_up;
    Bitmap arrow_left;
    Bitmap arrow_right;
    Bitmap arrow_down;


    public void prepareBitmaps(){
        Bitmap[] grassTile = new Bitmap[tileMap.getNumberOfTiles()];
        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.xy);
        xy = BitmapFactory.decodeResource(getResources(),R.drawable.xy);


        //BUILDINGS

        pines = BitmapFactory.decodeResource(getResources(),R.drawable.pines);
        mountain = BitmapFactory.decodeResource(getResources(),R.drawable.mountain);
        groundtexture = BitmapFactory.decodeResource(getResources(),R.drawable.groundtexture);

        //UI

        arrow_up = BitmapFactory.decodeResource(getResources(),R.drawable.arrow_up);
        arrow_down = BitmapFactory.decodeResource(getResources(),R.drawable.arrow_down);
        arrow_left = BitmapFactory.decodeResource(getResources(),R.drawable.arrow_left);
        arrow_right = BitmapFactory.decodeResource(getResources(),R.drawable.arrow_right);

        cameraUpRectangle.offsetTo(100, 200);
        cameraDownRectangle.offsetTo(100, 100);
        cameraLeftRectangle.offsetTo(50, 150);
        cameraRightRectangle.offsetTo(150, 150);


        for (int i=0;i<tileMap.numberOfTiles;i++) {
            grassTile[i] = paintMap(i);
            grassTileMapHolder[i]=grassTile[i];
        }

        villagerbitmap = new BitmapFactory().decodeResource(getResources(),R.drawable.player);

    }

    public void createMap(){
        //cameraUpRectangle.offsetTo(100, -200);

        GameMap.initialize();
        initializeBitmaps();
        tileMap.createTiles();
    }


    private boolean isPressingMouse = false;
    private int mouseclickpositionintX=-5000;
    private int mouseclickpositionintY=-5000;
    private int mouseclickpositionintY_UI =-5000;
    private int mouseclickpositionintXRelative=-5000;
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //isPressingMouse = true;
                /*
                mouseclickpositionintYRelative = (int) (event.getX()-currentCameraOffsetY-screenwidth);
                mouseclickpositionintXRelative = (int) (event.getY()-currentCameraOffsetX);

                 */


                //mouseclickpositionintY = (int) (-1*(event.getX()-screenwidth));
                mouseclickpositionintY_UI = (int) (event.getX());
                mouseclickpositionintY = (int) (-1*(event.getX()));
                mouseclickpositionintX = (int) event.getY();

                break;


            case MotionEvent.ACTION_BUTTON_RELEASE:
                //isPressingMouse = false;
        }

        return true;
    }



    public int currentCameraOffsetX = 0;
    public int currentCameraOffsetY = 0;

    Building buildingNumeroUno;


    Building[] buildingsArray = new Building[50];


    public void readWhereToPlaceBuildings(){
        for (int i=0;i<tileMap.numberOfTiles;i++) {
            if (tileMap.getBuildingTypeData(i*3)==1){
                prepareBuildings(i,"1", pines, tileMap.getX(i),tileMap.getY(i));
            }
        }
    }

    public void prepareBuildings(int i, String type, Bitmap bitmap, int x, int y){
        buildingsArray[i]=new Building(type,bitmap,x,y);
    }


    protected void drawBuildings(Canvas canvas){
        for (int i = 0; i< buildingsArray.length; i++){
            if (buildingsArray[i]!=null&&buildingsArray[i].type!="null") {
                if (screenR.contains(buildingsArray[i].x+currentCameraOffsetX, buildingsArray[i].y+currentCameraOffsetY)) {
                    canvas.drawBitmap(buildingsArray[i].getBuildingBitmap(), buildingsArray[i].x+currentCameraOffsetX, buildingsArray[i].y+currentCameraOffsetY, null);
                }
            }
        }


        // canvas.drawBitmap(buildingNumeroUno.getBuildingBitmap(),buildingNumeroUno.x+currentCameraOffsetX,buildingNumeroUno.y+currentCameraOffsetY,null);
         //canvas.drawBitmap(trees[0].getBuildingBitmap(),trees[0].x+currentCameraOffsetX,trees[0].y+currentCameraOffsetY,null);

         /*
         canvas.drawBitmap(pines,buildingNumeroUno.x+currentCameraOffsetX+50,buildingNumeroUno.y+currentCameraOffsetY-124,null);
         canvas.drawBitmap(pines,buildingNumeroUno.x+currentCameraOffsetX-125,buildingNumeroUno.y+currentCameraOffsetY+25,null);
         canvas.drawBitmap(mountain,buildingNumeroUno.x+currentCameraOffsetX-525,buildingNumeroUno.y+currentCameraOffsetY-525,null);
         canvas.drawBitmap(groundtexture,buildingNumeroUno.x+currentCameraOffsetX+525,buildingNumeroUno.y+currentCameraOffsetY-525,null);

         */
    }
    protected void onDraw(Canvas canvas) {
        moveCamera();



        //canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        canvas.drawColor(Color.rgb(19,131,201));

        //TODO preparetodraw?
        grass.prepareToDraw();


        for (int i = 0; i < tileMap.getNumberOfTiles(); i++) {
            if (screenR.contains(tileMap.getX(i), tileMap.getY(i))) {
                canvas.drawBitmap(tilemapBitmaps[tileMap.getTilemapData(i*3)], tileMap.getX(i), tileMap.getY(i), null);
                //canvas.drawBitmap(grass, 50+ currentCameraOffsetX , 50+currentCameraOffsetY, null);
            }
        }


        for (int j=0;j<numberOfVillagers;j++) {
            villager[j].setCameraDistance(currentCameraOffsetX,currentCameraOffsetY);
            villager[j].giveMouseCoordinates(mouseclickpositionintX,mouseclickpositionintY);
            //Log.d(String.valueOf(villager[j].getVillagerX()),"villX");
            //Log.d(String.valueOf(villager[j].getVillagerY()),"villY");
            if (villager[j].checkSelected() == true){
                if (!(mouseclickpositionintX>villager[j].getVillagerX() && mouseclickpositionintX<villager[j].getVillagerX()+villager[j].getVillagerBitmap().getWidth())){
                    if (!(mouseclickpositionintY>villager[j].getVillagerY() &&mouseclickpositionintX<villager[j].getVillagerY()+villager[j].villagerBitmap.getHeight())){
                        villager[j].setSelectedState(false);
                        villager[j].setTargetX(mouseclickpositionintX);
                        villager[j].setTargetY(mouseclickpositionintY);
                    }
                }
            }

            if (!(villager[j].getVillagerX()==villager[j].getTargetX() && villager[j].getVillagerY() == villager[j].getTargetY())){
                villager[j].move();
            }


            Log.d(String.valueOf(villager[j].getTargetX()),"villXtarget");
            Log.d(String.valueOf(villager[j].getTargetY()),"villYtarget");

            //Log.d(String.valueOf(villagerbitmap.getWidth()),"villagerwidth");
            //Log.d(String.valueOf(villagerbitmap.getHeight()),"villagerheight");



            canvas.drawBitmap(villager[j].getVillagerBitmap(), villager[j].getVillagerX(), villager[j].getVillagerY(), null);
        }

        drawBuildings(canvas);



            //____________________________UI_____________________//

        canvas.drawText((String.valueOf(mouseclickpositionintX)),500,-500,textpaint);
        canvas.drawText("X",700,-500,textpaint);

        canvas.drawText((String.valueOf(mouseclickpositionintY)),650,-800,textpaint);;
        canvas.drawText("Y",850,-800,textpaint);

        canvas.rotate(-90);

        /*
        canvas.drawBitmap(arrow_up,100,-150,null);
        canvas.drawBitmap(arrow_down,100,-50,null);
        canvas.drawBitmap(arrow_left,50,-100,null);
        canvas.drawBitmap(arrow_right,150,-100,null);
        */
        canvas.drawRect(cameraUpRectangle,paint);
        canvas.drawRect(cameraDownRectangle,paint);
        canvas.drawRect(cameraLeftRectangle,paint);
        canvas.drawRect(cameraRightRectangle,paint);



        //canvas.drawText((String.valueOf(mouseclickpositionintX)),-200,-200,textpaint);
        //canvas.drawText((String.valueOf(mouseclickpositionintY)),-600,-600,textpaint);

        //canvas.drawText("xR",200,200,textpaint);;
        //canvas.drawText("yR",600,600,textpaint);;

        //canvas.drawText((String.valueOf(villagerRect[0].left)),10,500,textpaint);
        //canvas.drawText((String.valueOf(villagerRect[0].top)),10,600,textpaint);


    }




    public boolean clickedOnVillager = false;


    public void yeetMouseOffscreen(){
        mouseclickpositionintX = 50000;
        mouseclickpositionintY = 50000;
    }

    public void moveVillagers(){
        /*
        Log.d(String.valueOf(mouseclickpositionintX),"mouseX");
        Log.d(String.valueOf(mouseclickpositionintY),"mouseY");
        Log.d(String.valueOf(mouseclickpositionintXRelative),"mouseXRELATIVE");
        Log.d(String.valueOf(mouseclickpositionintYRelative),"mouseYRELATIVE");
        */

        /*
        //
        for (int i=0;i<numberOfVillagers;i++) {
            /

            if (villager[i].getSelectedState()==false) {
                if (villagerRect[i].contains(mouseclickpositionintX, mouseclickpositionintY)) {
                    villager[i].setTargetX(mouseclickpositionintXRelative);
                    villager[i].setTargetY(mouseclickpositionintYRelative);
                    //yeetMouseOffscreen();
                    Log.d("contains", "indeed");
                    villager[i].setSelectedState(true);
                }
            }
            if (villager[i].getSelectedState() == true) {
                if (!villagerRect[i].contains(mouseclickpositionintX, mouseclickpositionintY)) {
                    villager[i].setSelectedState(false);
                    villager[i].setTargetX(mouseclickpositionintXRelative);
                    villager[i].setTargetY(mouseclickpositionintYRelative);
                }
                }
            if (!villagerRect[i].contains(targetX,targetY)) {
                villager[i].move();
            }
            }
        */
        }


    Camera zaCamera =  new Camera();
    public int cameraspeedX = 15;
    public int cameraspeedY = 15;

    public void moveCamera(){

            if (cameraUpRectangle.contains(mouseclickpositionintY_UI,mouseclickpositionintX)){
                zaCamera.addToCameraY(-cameraspeedY);
                currentCameraOffsetX=zaCamera.getCameraX();
                currentCameraOffsetY=zaCamera.getCameraY();
            }
            else if (cameraDownRectangle.contains(mouseclickpositionintY_UI,mouseclickpositionintX)){
                zaCamera.addToCameraY(cameraspeedY);
                currentCameraOffsetX=zaCamera.getCameraX();
                currentCameraOffsetY=zaCamera.getCameraY();
            }
            else if (cameraLeftRectangle.contains(mouseclickpositionintY_UI,mouseclickpositionintX)){
                zaCamera.addToCameraX(-cameraspeedX);
                currentCameraOffsetX=zaCamera.getCameraX();
                currentCameraOffsetY=zaCamera.getCameraY();
            }
            else if (cameraRightRectangle.contains(mouseclickpositionintY_UI,mouseclickpositionintX)){
                zaCamera.addToCameraX(cameraspeedX);
                currentCameraOffsetX=zaCamera.getCameraX();
                currentCameraOffsetY=zaCamera.getCameraY();
            }

        tileMap.setCameraDistance(currentCameraOffsetX,currentCameraOffsetY);

        }

    private Bitmap[] tilemapBitmaps = new Bitmap[255];
    Bitmap grass;
    public void initializeBitmaps(){
        for (int i = 0; i< tilemapBitmaps.length; i++){
            tilemapBitmaps[i]=BitmapFactory.decodeResource(getResources(),R.drawable.grass);
        }
        tilemapBitmaps[197]= BitmapFactory.decodeResource(getResources(),R.drawable.dirt_high);
        tilemapBitmaps[139]= BitmapFactory.decodeResource(getResources(),R.drawable.grass);
        tilemapBitmaps[164]= BitmapFactory.decodeResource(getResources(),R.drawable.water);
        tilemapBitmaps[195]= BitmapFactory.decodeResource(getResources(),R.drawable.water_corner_soth);
        tilemapBitmaps[134]= BitmapFactory.decodeResource(getResources(),R.drawable.water_corner_east);
        tilemapBitmaps[135]= BitmapFactory.decodeResource(getResources(),R.drawable.water_corner_north);
        tilemapBitmaps[136]= BitmapFactory.decodeResource(getResources(),R.drawable.water_corner_west);
        tilemapBitmaps[172]= BitmapFactory.decodeResource(getResources(),R.drawable.water_beach_south);
        tilemapBitmaps[173]= BitmapFactory.decodeResource(getResources(),R.drawable.water_beach_west);
        tilemapBitmaps[174]= BitmapFactory.decodeResource(getResources(),R.drawable.water_beach_north);
        tilemapBitmaps[175]= BitmapFactory.decodeResource(getResources(),R.drawable.water_beach_east);
        grass= BitmapFactory.decodeResource(getResources(),R.drawable.grass);


    }


    public Bitmap paintMap(int i){

        return tilemapBitmaps[tileMap.getTilemapData(i*3)];
    }



}