package com.example.paintapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;

import static com.example.paintapp.Display.colorList;
import static com.example.paintapp.Display.currentBrush;
import static com.example.paintapp.Display.pathList;

public class MainActivity extends AppCompatActivity {
    public static Path path = new Path();
    public static Paint paintBrush = new Paint();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void magentaColor(View view) {
        paintBrush.setColor(Color.MAGENTA);
        currentColor(paintBrush.getColor());
    }

    public void redColor(View view) {
        paintBrush.setColor(Color.RED);
        currentColor(paintBrush.getColor());
    }

    public void blueColor(View view) {
        paintBrush.setColor(Color.BLUE);
        currentColor(paintBrush.getColor());
    }

    public void greenColor(View view) {
        paintBrush.setColor(Color.GREEN);
        currentColor(paintBrush.getColor());
    }

    public void yellowColor(View view) {
        paintBrush.setColor(Color.YELLOW);
        currentColor(paintBrush.getColor());
    }

    public void pencil(View view) {
        paintBrush.setColor(Color.BLACK);
        currentColor(paintBrush.getColor());
    }

    public void eraser(View view) {
        path.reset();
        pathList.clear();
        colorList.clear();
    }

    public void currentColor(int c) {
        currentBrush = c;
        path = new Path();
    }
}
