package com.guercifzone.gameplate.Games;



import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.Random;

public class Snak extends AppCompatActivity {

    private SnakeGameView snakeGameView;
    private Button startButton, pauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the SnakeGameView
        snakeGameView = new SnakeGameView(this);

        // Create the layout
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(snakeGameView);

        // Create the control buttons
        LinearLayout controls = new LinearLayout(this);
        controls.setOrientation(LinearLayout.HORIZONTAL);

        startButton = new Button(this);
        startButton.setText("Start");
        startButton.setOnClickListener(v -> snakeGameView.startGame());

        pauseButton = new Button(this);
        pauseButton.setText("Pause");
        pauseButton.setOnClickListener(v -> snakeGameView.pauseGame());

        controls.addView(startButton);
        controls.addView(pauseButton);

        layout.addView(controls);
        setContentView(layout);
    }

    public class SnakeGameView extends SurfaceView implements SurfaceHolder.Callback {
        private GameThread gameThread;
        private Snake snake;
        private Food food;
        private int screenWidth, screenHeight;
        private boolean isGameOver = false;
        private boolean isPaused = false;

        public SnakeGameView(Snak context) {
            super(context);
            getHolder().addCallback(this);
            setFocusable(true);
            snake = new Snake();
            food = new Food();
            gameThread = new GameThread(getHolder(), this);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            screenWidth = getWidth();
            screenHeight = getHeight();
            food.spawnFood();
            gameThread.setRunning(true);
            gameThread.start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            boolean retry = true;
            gameThread.setRunning(false);
            while (retry) {
                try {
                    gameThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void update() {
            if (isGameOver || isPaused) return;

            snake.move();
            if (snake.hasCollidedWithWall(screenWidth, screenHeight) || snake.hasCollidedWithSelf()) {
                isGameOver = true;
          //      Toast.makeText(getContext(), "Game Over!", Toast.LENGTH_SHORT).show();
            }

            if (snake.hasEatenFood(food)) {
                snake.grow();
                food.spawnFood();
            }
        }

        public void draw(Canvas canvas) {
            super.draw(canvas);
            canvas.drawColor(Color.BLACK);

            Paint paint = new Paint();
            paint.setColor(Color.GREEN);
            for (Rect segment : snake.getBody()) {
                canvas.drawRect(segment, paint);
            }

            paint.setColor(Color.RED);
            canvas.drawRect(food.getPosition(), paint);

            if (isGameOver) {
                paint.setColor(Color.WHITE);
                paint.setTextSize(80);
                canvas.drawText("Game Over!", screenWidth / 3, screenHeight / 2, paint);
            }

            if (isPaused) {
                paint.setColor(Color.WHITE);
                paint.setTextSize(80);
                canvas.drawText("Paused", screenWidth / 3, screenHeight / 2, paint);
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (isGameOver) {
                    startGame(); // Restart game when game is over
                } else if (!isPaused) {
                    snake.changeDirection(event.getX(), event.getY(), screenWidth, screenHeight);
                }
            }
            return true;
        }

        // Start the game
        public void startGame() {
            snake.reset();
            food.spawnFood();
            isGameOver = false;
            isPaused = false;
        }

        // Pause the game
        public void pauseGame() {
            isPaused = !isPaused;
        }
    }

    public class GameThread extends Thread {
        private SurfaceHolder surfaceHolder;
        private SnakeGameView snakeGameView;
        private boolean isRunning;

        public GameThread(SurfaceHolder surfaceHolder, SnakeGameView snakeGameView) {
            this.surfaceHolder = surfaceHolder;
            this.snakeGameView = snakeGameView;
        }

        public void setRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }

        @Override
        public void run() {
            while (isRunning) {
                Canvas canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        snakeGameView.update();
                        snakeGameView.draw(canvas);
                    }
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }

    public class Snake {
        private LinkedList<Rect> body;
        private int direction = 0; // 0 = RIGHT, 1 = DOWN, 2 = LEFT, 3 = UP
        private static final int BLOCK_SIZE = 40;

        public Snake() {
            body = new LinkedList<>();
            body.add(new Rect(100, 100, 140, 140)); // Start position
        }

        public void move() {
            Rect head = body.getFirst();
            int newHeadLeft = head.left;
            int newHeadTop = head.top;

            switch (direction) {
                case 0: newHeadLeft += BLOCK_SIZE; break;
                case 1: newHeadTop += BLOCK_SIZE; break;
                case 2: newHeadLeft -= BLOCK_SIZE; break;
                case 3: newHeadTop -= BLOCK_SIZE; break;
            }

            Rect newHead = new Rect(newHeadLeft, newHeadTop, newHeadLeft + BLOCK_SIZE, newHeadTop + BLOCK_SIZE);
            body.addFirst(newHead);
            body.removeLast();
        }

        public boolean hasCollidedWithWall(int width, int height) {
            Rect head = body.getFirst();
            return head.left < 0 || head.top < 0 || head.right > width || head.bottom > height;
        }

        public boolean hasCollidedWithSelf() {
            Rect head = body.getFirst();
            for (int i = 1; i < body.size(); i++) {
                if (head.intersects(body.get(i).left, body.get(i).top, body.get(i).right, body.get(i).bottom)) {
                    return true;
                }
            }
            return false;
        }

        public boolean hasEatenFood(Food food) {
            Rect head = body.getFirst();
            return head.equals(food.getPosition());
        }

        public void grow() {
            body.addLast(new Rect(body.getLast()));
        }

        public void reset() {
            body.clear();
            body.add(new Rect(100, 100, 140, 140)); // Reset snake
            direction = 0;
        }

        public void changeDirection(float x, float y, int width, int height) {
            if (x < width / 2) {
                if (y < height / 2) {
                    direction = 3; // UP
                } else {
                    direction = 1; // DOWN
                }
            } else {
                if (y < height / 2) {
                    direction = 2; // LEFT
                } else {
                    direction = 0; // RIGHT
                }
            }
        }

        public LinkedList<Rect> getBody() {
            return body;
        }
    }

    public class Food {
        private Rect foodPosition;

        public Food() {
            foodPosition = new Rect();
        }

        public void spawnFood() {
            Random random = new Random();
            int x = random.nextInt(10) * 40;
            int y = random.nextInt(10) * 40;
            foodPosition.set(x, y, x + 40, y + 40);
        }

        public Rect getPosition() {
            return foodPosition;
        }
    }
}
