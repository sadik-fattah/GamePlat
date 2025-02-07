package com.guercifzone.gameplate.Games.Pong;

import android.graphics.Paint;

public class Pong_ball {
    float cx;
    float cy;
    float dx;
    float dy;
    int radius;
    Paint paint;

    public Pong_ball(int radius, Paint paint) {
        this.radius = radius;
        this.paint = paint;
    }
}
