package com.guercifzone.gameplate.Games.Pong;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PongView extends SurfaceView implements SurfaceHolder.Callback {
    private Pong_Thread thread;
    private Paddle playerPaddle;
    private Paddle aiPaddle;
    private Ball ball;

    private int screenWidth;
    private int screenHeight;

    public PongView(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread = new Pong_Thread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        screenWidth = getWidth();
        screenHeight = getHeight();

        // Initialize paddles and ball
        playerPaddle = new Paddle(screenWidth / 4, screenHeight / 2 - 150, 20, 150);
        aiPaddle = new Paddle(3 * screenWidth / 4, screenHeight / 2 - 150, 20, 150);
        ball = new Ball(screenWidth / 2, screenHeight / 2, 20);

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            playerPaddle.setY(y);
        }
        return true;
    }

    public void update() {
        ball.update();
        aiPaddle.update(ball);

        // Check for collisions
        if (ball.collidesWith(playerPaddle) || ball.collidesWith(aiPaddle)) {
            ball.reverseXDirection();
        }

        // Check if ball goes out of bounds
        if (ball.getX() < 0 || ball.getX() > screenWidth) {
            ball.reset(screenWidth / 2, screenHeight / 2);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawColor(Color.BLACK);

        // Draw paddles and ball
        playerPaddle.draw(canvas);
        aiPaddle.draw(canvas);
        ball.draw(canvas);
    }

    public void setThreadRunning(boolean running) {
        thread.setRunning(running);
    }
}
