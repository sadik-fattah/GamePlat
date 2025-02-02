package com.guercifzone.gameplate.Games;



import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.appcompat.app.AppCompatActivity;

public class Breakout extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        setContentView(gameView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gameView.onTouchEvent(event) || super.onTouchEvent(event);
    }

    // GameView class - All in MainActivity

    private class GameView extends SurfaceView implements SurfaceHolder.Callback {

        private GameThread gameThread;
        private Paddle paddle;
        private Ball ball;
        private Brick[][] bricks;
        private int rows = 5;
        private int cols = 7;
        private int brickWidth;
        private int brickHeight;

        public GameView(Context context) {
            super(context);
            getHolder().addCallback(this);
            setFocusable(true);

            gameThread = new GameThread(getHolder(), this);
            paddle = new Paddle();
            ball = new Ball();
            bricks = new Brick[rows][cols];

            brickWidth = getWidth() / cols;
            brickHeight = 100;

            // Create bricks
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    bricks[row][col] = new Brick(col * brickWidth, row * brickHeight);
                }
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            gameThread.setRunning(true);
            gameThread.start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // Not needed for this basic game
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            boolean retry = true;
            while (retry) {
                try {
                    gameThread.setRunning(false);
                    gameThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                retry = false;
            }
        }

        public void update() {
            paddle.update();
            ball.update(paddle, bricks);

            // Check for ball collisions with bricks
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    Brick brick = bricks[row][col];
                    if (brick != null && Rect.intersects(ball.getBounds(), brick.getBounds())) {
                        ball.reverseYDirection();
                        bricks[row][col] = null;
                    }
                }
            }
        }

        public void draw(Canvas canvas) {
            super.draw(canvas);
            Paint paint = new Paint();
            canvas.drawColor(Color.BLACK);

            // Draw the bricks
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    Brick brick = bricks[row][col];
                    if (brick != null) {
                        paint.setColor(Color.RED);
                        canvas.drawRect(brick.getBounds(), paint);
                    }
                }
            }

            // Draw paddle
            paint.setColor(Color.GREEN);
            canvas.drawRect(paddle.getBounds(), paint);

            // Draw ball
            paint.setColor(Color.YELLOW);
            canvas.drawCircle(ball.getX(), ball.getY(), ball.getRadius(), paint);
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                paddle.setX(event.getX());
            }
            return true;
        }

        private class GameThread extends Thread {
            private SurfaceHolder surfaceHolder;
            private GameView gameView;
            private boolean running;

            public GameThread(SurfaceHolder surfaceHolder, GameView gameView) {
                this.surfaceHolder = surfaceHolder;
                this.gameView = gameView;
            }

            public void setRunning(boolean running) {
                this.running = running;
            }

            @Override
            public void run() {
                Canvas canvas;
                long startTime;
                long timeMillis;
                long waitTime;
                int targetTime = 1000 / 60; // 60 FPS

                while (running) {
                    startTime = System.nanoTime();
                    canvas = null;

                    try {
                        canvas = surfaceHolder.lockCanvas();
                        synchronized (surfaceHolder) {
                            gameView.update();
                            gameView.draw(canvas);
                        }
                    } finally {
                        if (canvas != null) {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }

                    timeMillis = (System.nanoTime() - startTime) / 1000000;
                    waitTime = targetTime - timeMillis;

                    try {
                        if (waitTime > 0) {
                            sleep(waitTime);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // Paddle class
        private class Paddle {
            private float x, y;
            private static final int PADDLE_WIDTH = 200;
            private static final int PADDLE_HEIGHT = 30;

            public Paddle() {
                x = getWidth() / 2 - PADDLE_WIDTH / 2;
                y = getHeight() - PADDLE_HEIGHT - 50;
            }

            public void update() {
                if (x < 0) x = 0;
                if (x > getWidth() - PADDLE_WIDTH) x = getWidth() - PADDLE_WIDTH;
            }

            public void setX(float x) {
                this.x = x - PADDLE_WIDTH / 2;
            }

            public Rect getBounds() {
                return new Rect((int) x, (int) y, (int) (x + PADDLE_WIDTH), (int) (y + PADDLE_HEIGHT));
            }
        }

        // Ball class
        private class Ball {
            private float x, y, dx, dy;
            private static final int RADIUS = 15;

            public Ball() {
                x = getWidth() / 2;
                y = getHeight() / 2;
                dx = 5;
                dy = -5;
            }

            public void update(Paddle paddle, Brick[][] bricks) {
                x += dx;
                y += dy;

                if (x <= 0 || x >= getWidth()) {
                    dx = -dx;
                }
                if (y <= 0) {
                    dy = -dy;
                }

                if (Rect.intersects(getBounds(), paddle.getBounds())) {
                    dy = -dy;
                }

                if (y >= getHeight()) {
                    reset();
                }
            }

            public Rect getBounds() {
                return new Rect((int) (x - RADIUS), (int) (y - RADIUS), (int) (x + RADIUS), (int) (y + RADIUS));
            }

            public float getX() {
                return x;
            }

            public float getY() {
                return y;
            }

            public int getRadius() {
                return RADIUS;
            }

            public void reverseYDirection() {
                dy = -dy;
            }

            private void reset() {
                x = getWidth() / 2;
                y = getHeight() / 2;
                dx = 5;
                dy = -5;
            }
        }

        // Brick class
        private class Brick {
            private float x, y;
            private static final int BRICK_WIDTH = 150;
            private static final int BRICK_HEIGHT = 50;

            public Brick(float x, float y) {
                this.x = x;
                this.y = y;
            }

            public Rect getBounds() {
                return new Rect((int) x, (int) y, (int) (x + BRICK_WIDTH), (int) (y + BRICK_HEIGHT));
            }
        }
    }
}
