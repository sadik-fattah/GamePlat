package com.guercifzone.gameplate.Games;



import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.guercifzone.gameplate.R;

public class Pong extends AppCompatActivity {

    private static final int PADDLE_WIDTH = 150;
    private static final int PADDLE_HEIGHT = 30;
    private static final int BALL_SIZE = 30;

    private int paddle1X, paddle2X, ballX, ballY, ballVelocityX, ballVelocityY;
    private int screenHeight, screenWidth, paddleWidth;
    private Handler handler;
    private Runnable runnable;
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pong);

        gameView = new GameView(this);
        setContentView(gameView);

        handler = new Handler();
        paddle1X = 300;
        paddle2X = 300;
        ballX = 500;
        ballY = 500;
        ballVelocityX = 10;
        ballVelocityY = 10;
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.invalidate(); // Force redrawing of the view
    }

    private class GameView extends View {

        private Paint paint;

        public GameView(Pong context) {
            super(context);
            paint = new Paint();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            screenWidth = w;
            screenHeight = h;
            paddleWidth = screenWidth / 5; // 20% of screen width
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            // Draw paddles and ball
            paint.setColor(0xFFFFFFFF); // White color
            canvas.drawRect(paddle1X, screenHeight - PADDLE_HEIGHT, paddle1X + paddleWidth, screenHeight, paint);
            canvas.drawRect(paddle2X, 0, paddle2X + paddleWidth, PADDLE_HEIGHT, paint);
            canvas.drawRect(ballX, ballY, ballX + BALL_SIZE, ballY + BALL_SIZE, paint);

            // Update ball position
            ballX += ballVelocityX;
            ballY += ballVelocityY;

            // Ball bouncing off walls
            if (ballX <= 0 || ballX + BALL_SIZE >= screenWidth) {
                ballVelocityX = -ballVelocityX;
            }

            if (ballY <= 0 || ballY + BALL_SIZE >= screenHeight) {
                ballVelocityY = -ballVelocityY;
            }

            // Ball bouncing off paddles
            if (ballY + BALL_SIZE >= screenHeight - PADDLE_HEIGHT && ballX + BALL_SIZE >= paddle1X && ballX <= paddle1X + paddleWidth) {
                ballVelocityY = -ballVelocityY;
            }

            if (ballY <= PADDLE_HEIGHT && ballX + BALL_SIZE >= paddle2X && ballX <= paddle2X + paddleWidth) {
                ballVelocityY = -ballVelocityY;
            }

            // Redraw every 16ms (60 FPS)
            handler.postDelayed(runnable = new Runnable() {
                @Override
                public void run() {
                    invalidate();
                }
            }, 16);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float touchX = event.getX();

            // Move paddles
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                paddle1X = (int) touchX - paddleWidth / 2;
            }

            // Ensure paddle doesn't go out of bounds
            if (paddle1X < 0) {
                paddle1X = 0;
            }
            if (paddle1X + paddleWidth > screenWidth) {
                paddle1X = screenWidth - paddleWidth;
            }

            return true;
        }
    }
}
