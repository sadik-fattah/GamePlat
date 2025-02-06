package com.guercifzone.gameplate.Games.Snake;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.guercifzone.gameplate.R;

import java.util.LinkedList;
import java.util.Random;

public class Snake_GameView extends View {
    public Snake_GameView(Context context) {
        super(context);
    }

    public Snake_GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Snake_GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private static final String TAG = "GameView";

    private static final int MAP_SIZE = 20;
    private static final int START_X = 5;
    private static final int START_Y = 10;

    private final Snake_Point[][] mPoints = new Snake_Point[MAP_SIZE][MAP_SIZE];
    private final LinkedList<Snake_Point> mSnake = new LinkedList<>();
    private Snake_Direction mDir;

    private Snake_ScoreUpdatedListener mScoreUpdatedListener;

    private boolean mGameOver = false;

    private int mBoxSize;
    private int mBoxPadding;

    private final Paint mPaint = new Paint();

    public void init() {
        mBoxSize = getContext().getResources()
                .getDimensionPixelSize(R.dimen.game_size) / MAP_SIZE;
        mBoxPadding = mBoxSize / 10;
    }

    public void newGame() {
        mGameOver = false;
        mDir = Snake_Direction.RIGHT;
        initMap();
        updateScore();
    }

    public void setGameScoreUpdatedListener(Snake_ScoreUpdatedListener scoreUpdatedListener) {
        mScoreUpdatedListener = scoreUpdatedListener;
    }

    private void initMap() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                mPoints[i][j] = new Snake_Point(j, i);
            }
        }
        mSnake.clear();
        for (int i = 0; i < 3; i++) {
            Snake_Point point = getPoint(START_X + i, START_Y);
            point.type = Snake_PointType.SNAKE;
            mSnake.addFirst(point);
        }
        randomApple();
    }

    private void randomApple() {
        Random random = new Random();
        while (true) {
            Snake_Point point = getPoint(random.nextInt(MAP_SIZE),
                    random.nextInt(MAP_SIZE));
            if (point.type == Snake_PointType.EMPTY) {
                point.type = Snake_PointType.APPLE;
                break;
            }
        }
    }

    private Snake_Point getPoint(int x, int y) {
        return mPoints[y][x];
    }

    public void next() {
        Snake_Point first = mSnake.getFirst();
        Log.d(TAG, "first: " + first.x + " " + first.y);
        Snake_Point next = getNext(first);
        Log.d(TAG, "next: " + next.x + " " + next.y);

        switch (next.type) {
            case EMPTY:
                Log.d(TAG, "next: empty");
                next.type = Snake_PointType.SNAKE;
                mSnake.addFirst(next);
                mSnake.getLast().type = Snake_PointType.EMPTY;
                mSnake.removeLast();
                break;
            case APPLE:
                Log.d(TAG, "next: apple");
                next.type = Snake_PointType.SNAKE;
                mSnake.addFirst(next);
                randomApple();
                updateScore();
                break;
            case SNAKE:
                Log.d(TAG, "next: snake");
                mGameOver = true;
                break;
        }
    }

    public void updateScore() {
        if (mScoreUpdatedListener != null) {
            int score = mSnake.size() - 3;
            mScoreUpdatedListener.onScoreUpdated(score);
        }
    }

    public void setSnake_Direction(Snake_Direction dir) {
        if ((dir == Snake_Direction.LEFT || dir == Snake_Direction.RIGHT) &&
                (mDir == Snake_Direction.LEFT || mDir == Snake_Direction.RIGHT)) {
            return;
        }
        if ((dir == Snake_Direction.UP || dir == Snake_Direction.DOWN) &&
                (mDir == Snake_Direction.UP || mDir == Snake_Direction.DOWN)) {
            return;
        }
        mDir = dir;
    }

    private Snake_Point getNext(Snake_Point point) {
        int x = point.x;
        int y = point.y;

        switch (mDir) {
            case UP:
                y = y == 0 ? MAP_SIZE - 1 : y - 1;
                break;
            case DOWN:
                y = y == MAP_SIZE - 1 ? 0 : y + 1;
                break;
            case LEFT:
                x = x == 0 ? MAP_SIZE - 1 : x - 1;
                break;
            case RIGHT:
                x = x == MAP_SIZE - 1 ? 0 : x + 1;
                break;
        }
        return getPoint(x, y);
    }

    public boolean isGameOver() {
        return mGameOver;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int y = 0; y < MAP_SIZE; y++) {
            for (int x = 0; x < MAP_SIZE; x++) {
                int left = mBoxSize * x;
                int right = left + mBoxSize;
                int top = mBoxSize * y;
                int bottom = top + mBoxSize;
                switch (getPoint(x, y).type) {
                    case APPLE:
                        mPaint.setColor(Color.RED);
                        break;
                    case SNAKE:
                        mPaint.setColor(Color.BLACK);
                        canvas.drawRect(left, top, right, bottom, mPaint);
                        mPaint.setColor(Color.WHITE);
                        left += mBoxPadding;
                        right -= mBoxPadding;
                        top += mBoxPadding;
                        bottom -= mBoxPadding;
                        break;
                    case EMPTY:
                        mPaint.setColor(Color.BLACK);
                        break;
                }
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }
}