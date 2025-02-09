package com.guercifzone.gameplate.Games.Pong;



import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Paddle {
    private float x, y;
    float width;
    float height;
    private Paint paint;

    public Paddle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.paint = new Paint();
        paint.setColor(Color.WHITE);
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(x, y, x + width, y + height, paint);
    }

    public void update(Ball ball) {
        if (ball.getX() > x + width / 2) {
            y = ball.getY() - height / 2;
        }
    }

    public boolean collidesWith(Ball ball) {
        return ball.getX() > x && ball.getX() < x + width && ball.getY() > y && ball.getY() < y + height;
    }

    public void setY(float y) {
        this.y = y - height / 2;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
