package com.guercifzone.gameplate.Games.Pong;



import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

class Player {
 int paddleHeight,paddleWidth ,collision,score;
 RectF bounds;
 Paint paint;

    public Player(Paint paint, int paddleHeight, int paddleWidth) {
        this.paint = paint;
        this.paddleHeight = paddleHeight;
        this.paddleWidth = paddleWidth;
    }

    public Player(int paddleHeight, int paddleWidth, Paint humanPlayerPaint) {
        this.paint = humanPlayerPaint;
        this.paddleHeight = paddleHeight;
        this.paddleWidth = paddleWidth;

    }
}
