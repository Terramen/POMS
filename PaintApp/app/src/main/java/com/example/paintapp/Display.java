package com.example.paintapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
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


    private float x0;
    private float y0;
    private int status;

    public void setStatus(int status) {
        this.status = status;
    }

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
    
    public boolean onTouchEvent(MotionEvent event) {
        switch (status) {
            case 0: drawCircle(event.getX(), event.getY(), event);
                break;
            case 1: drawTriangle(event.getX(), event.getY(), event);
                break;
            case 2: drawLine(event.getX(), event.getY(), event);
                break;
            case 3: drawRectangle(event.getX(), event.getY(), event);
                break;
            case 4: drawErase(event.getX(), event.getY(), event);
                break;
        }
        invalidate();
        return true;
    }

    public void drawRectangle(float touchX, float touchY, MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                x0 = touchX;
                y0 = touchY;
                break;

            case MotionEvent.ACTION_MOVE:
                drawPath.reset();
                path.reset();

                drawPath.addRect(new RectF(x0, y0, touchX, touchY), Path.Direction.CW);
                drawPath.addRect(new RectF(x0, touchY, touchX, y0), Path.Direction.CW);

                drawPath.addRect(new RectF(touchX, y0, x0, touchY), Path.Direction.CW);
                drawPath.addRect(new RectF(touchX, touchY, x0, y0), Path.Direction.CW);

                path.addRect(new RectF(x0, y0, touchX, touchY), Path.Direction.CW);
                path.addRect(new RectF(x0, touchY, touchX, y0), Path.Direction.CW);

                path.addRect(new RectF(touchX, y0, x0, touchY), Path.Direction.CW);
                path.addRect(new RectF(touchX, touchY, x0, y0), Path.Direction.CW);

                break;

            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(path, drawPaint);
                imageCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                path.reset();
                break;
        }
    }

    public void drawCircle(float touchX, float touchY, MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                x0 = touchX;
                y0 = touchY;
                break;

            case MotionEvent.ACTION_MOVE:

                drawPath.reset();
                path.reset();

                if (Math.abs(touchY - y0) > Math.abs(touchX - x0)) {
                    if (y0 < touchY) {
                        drawPath.addCircle(x0, y0, touchY - y0, Path.Direction.CW);
                    } else {
                        drawPath.addCircle(x0, y0, y0 - touchY, Path.Direction.CW);
                    }
                } else {
                    if (x0 < touchX) {
                        drawPath.addCircle(x0, y0, touchX - x0, Path.Direction.CW);
                    } else {
                        drawPath.addCircle(x0, y0, x0 - touchX, Path.Direction.CW);
                    }
                }

                if (Math.abs(touchY - y0) > Math.abs(touchX - x0)) {
                    if (y0 < touchY) {
                        path.addCircle(x0, y0, touchY - y0, Path.Direction.CW);
                    } else {
                        path.addCircle(x0, y0, y0 - touchY, Path.Direction.CW);
                    }
                } else {
                    if (x0 < touchX) {
                        path.addCircle(x0, y0, touchX - x0, Path.Direction.CW);
                    } else {
                        path.addCircle(x0, y0, x0 - touchX, Path.Direction.CW);
                    }
                }

                break;

            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                imageCanvas.drawPath(path, drawPaint);
                drawPath.reset();
                path.reset();
                break;
        }
    }

    public void drawTriangle(float touchX, float touchY, MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                x0 = touchX;
                y0 = touchY;
                break;

            case MotionEvent.ACTION_MOVE:

                drawPath.reset();
                path.reset();

                if (x0 < touchX) {
                    drawPath.moveTo(x0, y0);
                    drawPath.lineTo(touchX, touchY);

                    drawPath.moveTo(x0, y0);
                    drawPath.lineTo((x0 - Math.abs(x0 - touchX)), touchY);

                    drawPath.lineTo(touchX, touchY);
                } else {

                    drawPath.moveTo(x0, y0);
                    drawPath.lineTo(touchX, touchY);

                    drawPath.moveTo(x0, y0);
                    drawPath.lineTo((x0 + Math.abs(x0 - touchX)), touchY);

                    drawPath.lineTo(touchX, touchY);

                }

                if (x0 < touchX) {
                    path.moveTo(x0, y0);
                    path.lineTo(touchX, touchY);

                    path.moveTo(x0, y0);
                    path.lineTo((x0 - Math.abs(x0 - touchX)), touchY);

                    path.lineTo(touchX, touchY);
                } else {

                    path.moveTo(x0, y0);
                    path.lineTo(touchX, touchY);

                    path.moveTo(x0, y0);
                    path.lineTo((x0 + Math.abs(x0 - touchX)), touchY);

                    path.lineTo(touchX, touchY);

                }

                break;

            case MotionEvent.ACTION_UP:

                drawCanvas.drawPath(drawPath, drawPaint);
                imageCanvas.drawPath(path, drawPaint);
                drawPath.reset();
                path.reset();
                break;
        }
    }

    public void drawLine(float touchX, float touchY, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                path.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                path.lineTo(touchX, touchY);
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
        }
    }

    public void drawErase(float touchX, float touchY, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                path.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                path.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                imageCanvas.drawPath(drawPath, drawPaint);
                drawCanvas.drawPath(path, drawPaint);
                drawPath.reset();
                path.reset();
                drawPaint.setXfermode(null);
                break;
        }
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
