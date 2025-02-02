package com.guercifzone.gameplate.Games;



import android.os.Bundle;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class Invaders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));  // Set the custom GameView as the content view
    }

    public class GameView extends SurfaceView implements Runnable {

        private Thread gameThread;
        private boolean isPlaying;
        private SurfaceHolder surfaceHolder;
        private Paint paint;

        private int playerX = 300, playerY = 800;
        private int playerWidth = 80, playerHeight = 40;

        private List<Alien> aliens = new ArrayList<>();
        private List<Bullet> bullets = new ArrayList<>();

        public GameView(Invaders context) {
            super(context);
            surfaceHolder = getHolder();
            paint = new Paint();
            paint.setColor(Color.WHITE);

            // Initialize aliens in a grid
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    aliens.add(new Alien(100 + i * 100, 100 + j * 80));
                }
            }
        }

        @Override
        public void run() {
            while (isPlaying) {
                updateGame();
                drawGame();
                controlFPS();
            }
        }

        private void updateGame() {
            for (Bullet bullet : bullets) {
                bullet.update();
            }
            for (Alien alien : aliens) {
                alien.update();
            }
            checkCollisions();
        }

        private void checkCollisions() {
            for (Bullet bullet : bullets) {
                for (Alien alien : aliens) {
                    if (bullet.getBounds().intersect(alien.getBounds())) {
                        aliens.remove(alien);
                        bullets.remove(bullet);
                        break;
                    }
                }
            }
        }

        private void drawGame() {
            if (surfaceHolder.getSurface().isValid()) {
                Canvas canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(Color.BLACK);  // Background color

                // Draw player
                canvas.drawRect(new Rect(playerX, playerY, playerX + playerWidth, playerY + playerHeight), paint);

                // Draw aliens
                for (Alien alien : aliens) {
                    canvas.drawRect(new Rect(alien.x, alien.y, alien.x + alien.width, alien.y + alien.height), paint);
                }

                // Draw bullets
                for (Bullet bullet : bullets) {
                    canvas.drawRect(new Rect(bullet.x, bullet.y, bullet.x + bullet.width, bullet.y + bullet.height), paint);
                }

                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }

        private void controlFPS() {
            try {
                Thread.sleep(17);  // Aim for ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void pause() {
            isPlaying = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void resume() {
            isPlaying = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                playerX = (int) event.getX();  // Move player with touch
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                bullets.add(new Bullet(playerX + playerWidth / 2, playerY));  // Shoot bullet
            }
            return true;
        }

        // Bullet class (Nested inside GameView)
        public class Bullet {
            private int x, y;
            private int width = 10, height = 30;

            public Bullet(int x, int y) {
                this.x = x;
                this.y = y;
            }

            public void update() {
                y -= 10;  // Move bullet upwards
            }

            public Rect getBounds() {
                return new Rect(x, y, x + width, y + height);
            }
        }

        // Alien class (Nested inside GameView)
        public class Alien {
            private int x, y;
            private int width = 60, height = 40;

            public Alien(int x, int y) {
                this.x = x;
                this.y = y;
            }

            public void update() {
                // Aliens can move left-right or just sit idle for now
            }

            public Rect getBounds() {
                return new Rect(x, y, x + width, y + height);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GameView gameView = (GameView) findViewById(android.R.id.content);
        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GameView gameView = (GameView) findViewById(android.R.id.content);
        gameView.pause();
    }
}
