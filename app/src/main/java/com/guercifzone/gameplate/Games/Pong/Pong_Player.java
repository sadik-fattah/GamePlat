package com.guercifzone.gameplate.Games.Pong;

import android.graphics.Paint;
import android.graphics.RectF;

public class Pong_Player {
    int paddleWidth;
    int paddleHeight;
    Paint paint;
    int score;
    RectF bounds;
    int collision;

    public Pong_Player(int paddleWidth, int paddleHeight, Paint paint) {
        this.paddleWidth = paddleWidth;
        this.paddleHeight = paddleHeight;
        this.paint = paint;
        this.score = 0;
        this.bounds = new RectF(0,0,paddleWidth,paddleHeight);
        this.collision = 0;
    }
}
