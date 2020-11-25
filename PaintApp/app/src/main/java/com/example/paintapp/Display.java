package com.example.paintapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


public class Display extends View {

    private int w;
    private int h;
    private boolean loadFile;

    private Path drawPath;
    private Path path;
    private Paint drawPaint, canvasPaint;
    private int paintColor = 0xff000000;
    private int previousColor = paintColor;

    public Canvas getDrawCanvas() {
        return drawCanvas;
    }

    public void setDrawCanvas(Canvas drawCanvas) {
        this.drawCanvas = drawCanvas;
    }

    public Bitmap getCanvasBitmap() {
        return canvasBitmap;
    }

    public void setCanvasBitmap(Bitmap canvasBitmap) {
        this.canvasBitmap = canvasBitmap;
    }


    private Canvas imageCanvas;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private Bitmap imageBitmap;
    private Bitmap selectedBitmap;
    private float brushSize, lastBrushSize;
    private boolean erase = false;

    public Display(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init();
    }

    private void Init() {
        drawPath = new Path();
        path = new Path();
        drawPaint = new Paint();


        drawPaint.setAntiAlias(true);
        drawPaint.setColor(paintColor);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeCap((Paint.Cap.ROUND));
        drawPaint.setStrokeJoin(Paint.Join.ROUND);


        canvasPaint = new Paint(Paint.DITHER_FLAG);

        // Default brush size
        brushSize = 20;
        lastBrushSize = brushSize;
        drawPaint.setStrokeWidth(brushSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        if (loadFile) {
            imageBitmap = selectedBitmap.copy(Bitmap.Config.ARGB_8888, true);
        } else {
            imageBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        }


        imageCanvas = new Canvas(imageBitmap);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(x, y);
                path.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(x, y);
                path.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                if (erase) {
                    drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                }
                imageCanvas.drawPath(drawPath, drawPaint);
                drawCanvas.drawPath(path, drawPaint);
                drawPath.reset();
                path.reset();
                drawPaint.setXfermode(null);
                break;
             default:
                 return false;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawBitmap(imageBitmap, 0, 0, canvasPaint);
        canvas.drawPath(path, drawPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    public void loadFile(Bitmap selectedBitmap) {
        this.selectedBitmap = selectedBitmap;
        loadFile = true;

        onSizeChanged(w, h, w, h);
        invalidate();
    }

    public void setColor(String newColor){
        // invalidate the view
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
        previousColor = paintColor;
    }

    public void setBrushSize(float newSize){
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize=pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }
    public float getLastBrushSize(){
        return lastBrushSize;
    }

    public void setErase(boolean isErase){
        //set erase true or false
        erase = isErase;
        if(erase) {
            drawPaint.setColor(Color.WHITE);
            //drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        else {
            drawPaint.setColor(previousColor);
            drawPaint.setXfermode(null);
        }
    }

    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        imageCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }
}
