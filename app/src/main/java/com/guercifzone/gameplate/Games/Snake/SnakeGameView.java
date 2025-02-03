package com.guercifzone.gameplate.Games.Snake;



import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;
import java.util.Random;

public class SnakeGameView extends View {

    private static final int TILE_SIZE = 100;
    private static int WIDTH = 16;
    private static int HEIGHT = 16;
    private static final int INITIAL_SNAKE_SIZE = 3;
    private LinkedList<Position> snake;
    private Position food;
    private int direction; // 0 = Up, 1 = Right, 2 = Down, 3 = Left
    private boolean gameOver;
    private Handler handler;
    private Runnable runnable;

    private int delay =500; // Delay between game updates (in milliseconds)
    private Paint snakePaint;
    private Paint foodPaint;
    private Paint backgroundPaint;

    public SnakeGameView(Context context) {
        super(context);

        snake = new LinkedList<>();
        handler = new Handler();

        snakePaint = new Paint();
        snakePaint.setColor(Color.GREEN);

        foodPaint = new Paint();
        foodPaint.setColor(Color.RED);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLACK);

        initializeGame();
    }

    private void initializeGame() {
        snake.clear();
        for (int i = 0; i < INITIAL_SNAKE_SIZE; i++) {
            snake.add(new Position(WIDTH / 2, HEIGHT / 2 + i));
        }
        direction = 0; // Start moving up
        spawnFood();
        gameOver = false;

        // Create a Runnable that updates the game at the specified delay interval
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!gameOver) {
                    updateGame();
                    invalidate();  // Redraw the game
                    handler.postDelayed(this, delay);  // Re-run this method after the delay
                }
            }
        };
        handler.postDelayed(runnable, delay);  // Start the game loop
    }

    private void spawnFood() {
        Random random = new Random();
        food = new Position(random.nextInt(WIDTH), random.nextInt(HEIGHT));
    }

    private void updateGame() {
        if (gameOver) return;

        Position head = snake.getFirst();
        Position newHead = new Position(head.x, head.y);

        // Move the snake
        switch (direction) {
            case 0:
                newHead.y--;
                break;
            case 1:
                newHead.x++;
                break;
            case 2:
                newHead.y++;
                break;
            case 3:
                newHead.x--;
                break;
        }

        // Check if the snake collides with the wall
        if (newHead.x < 0 || newHead.x >= WIDTH || newHead.y < 0 || newHead.y >= HEIGHT) {
            gameOver = false;
            return;
        }

        // Check if the snake collides with itself
        if (snake.contains(newHead)) {
            gameOver = false;
            return;
        }

        snake.addFirst(newHead);

        // Check if the snake eats food
        if (newHead.equals(food)) {
            spawnFood();
        } else {
            snake.removeLast();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (gameOver) {
            Paint gameOverPaint = new Paint();
            gameOverPaint.setColor(Color.BLACK);
            gameOverPaint.setTextSize(100);
            canvas.drawText("Game Over!", WIDTH * TILE_SIZE / 4, HEIGHT * TILE_SIZE / 2, gameOverPaint);
            return;
        }

        // Draw background
        canvas.drawRect(0, 0, WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE, backgroundPaint);

        // Draw snake
        for (Position position : snake) {
            canvas.drawRect(position.x * TILE_SIZE, position.y * TILE_SIZE,
                    (position.x + 1) * TILE_SIZE, (position.y + 1) * TILE_SIZE, snakePaint);
        }

        // Draw food
        canvas.drawRect(food.x * TILE_SIZE, food.y * TILE_SIZE,
                (food.x + 1) * TILE_SIZE, (food.y + 1) * TILE_SIZE, foodPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gameOver) {
            initializeGame();
        } else {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                float x = event.getX();
                float y = event.getY();

                // Change direction based on touch
                if (x < getWidth() / 2 && y < getHeight() / 2) {
                    direction = 0; // Up
                } else if (x > getWidth() / 2 && y < getHeight() / 2) {
                    direction = 1; // Right
                } else if (x < getWidth() / 2 && y > getHeight() / 2) {
                    direction = 3; // Left
                } else if (x > getWidth() / 2 && y > getHeight() / 2) {
                    direction = 2; // Down
                }
            }
        }
        return true;
    }

    // Method to change the speed of the snake
    public void setSpeed(int newSpeed) {
        delay = newSpeed;
        handler.removeCallbacks(runnable);  // Remove the old game loop
        handler.postDelayed(runnable, delay);  // Restart the game loop with the new speed
    }
}




