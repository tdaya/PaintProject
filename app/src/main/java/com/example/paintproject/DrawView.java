package com.example.paintproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Tahira on 8/23/2016.
 */
public class DrawView extends View {
    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    public Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = 0xFF660000;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;            //a bitmap is a class of andriod graphics, it defines display space and a colour for each pixel = bit


    private ArrayList<Path> paths = new ArrayList<Path>();            //list to hold the path drawn on the screen
    private ArrayList<Path> undonePaths = new ArrayList<Path>();      //array list to hold path removed


    //constructor
    public DrawView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();                 //calling this method b/c its where variables are initialized
    }

    private void setupDrawing(){    //get drawing area setup for interaction, initialize variables
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {            //changes view given size
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }


    @Override
    protected void onDraw(Canvas canvas) {      //this method is where drawing for all views happens
    //  canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);   //if I comment this in, nothing works
     // canvas.drawPath(drawPath, drawPaint);
        for (Path p : paths){
            canvas.drawPath(p, drawPaint);
        }
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    // this method appends the points to the path when the user touches the screen. when the user pushes down start a path and when they drag connect the points
    public boolean onTouchEvent(MotionEvent event) {        //detect user touch
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(touchX, touchY);
               // invalidate();
                //drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(touchX, touchY);
               // invalidate();
                //drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                //invalidate();
               // drawCanvas.drawPath(drawPath, drawPaint);
               // drawPath.reset();
                break;

            default:
              return false;
        }
        invalidate();
        return true;
    }

    //method to start a new canvas
    public void eraseAll() {
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public Bitmap getBitmap() {
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);
        return bmp;
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        undonePaths.clear();
        drawPath.reset();
        drawPath.moveTo(x, y);
        mX = x;
        mY = y;
    }
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            drawPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }
    private void touch_up() {
        drawPath.lineTo(mX, mY);
        // commit the path to our offscreen
        drawCanvas.drawPath(drawPath, drawPaint);
        // kill this so we don't double draw
        paths.add(drawPath);
        drawPath = new Path();
    }

    public void onClickUndo() {

        if (paths.size()>0)
        {
            undonePaths.add(paths.remove(paths.size()-1));
            invalidate();
        }
    }

    public void onClickRedo(){
        if (undonePaths.size()>0)
        {
            paths.add(undonePaths.remove(undonePaths.size()-1));
            invalidate();
        }
    }

}
