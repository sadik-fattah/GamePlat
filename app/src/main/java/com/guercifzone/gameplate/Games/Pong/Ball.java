package com.guercifzone.gameplate.Games.Pong;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Ball {
    private float x, y;
    private float radius;
    private float dx, dy;
    private Paint paint;

    public Ball(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.dx = 10;
        this.dy = 10;
        this.paint = new Paint();
        paint.setColor(Color.WHITE);
    }

    public void update() {
        x += dx;
        y += dy;

        // Bounce off top and bottom
        if (y - radius < 0 || y + radius > 1200) {
            dy = -dy;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(x, y, radius, paint);
    }

    public boolean collidesWith(Paddle paddle) {
        return x + radius > paddle.getX() && x - radius < paddle.getX() + paddle.width && y + radius > paddle.getY() && y - radius < paddle.getY() + paddle.height;
    }

    public void reverseXDirection() {
        dx = -dx;
    }

    public void reset(float x, float y) {
        this.x = x;
        this.y = y;
        this.dx = -dx;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
