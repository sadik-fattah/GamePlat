package com.guercifzone.gameplate.Games.Pong;




import android.graphics.Canvas;
import android.view.SurfaceHolder;



public class Pong_Thread extends Thread {
    private static final int TARGET_FPS = 60;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private PongView gameView;
    private boolean running;

    public Pong_Thread(SurfaceHolder surfaceHolder, PongView gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;

        while (running) {
            startTime = System.nanoTime();

            Canvas canvas = null;
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
            waitTime = (1000 / TARGET_FPS) - timeMillis;

            try {
                if (waitTime > 0) {
                    sleep(waitTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;

            if (frameCount == TARGET_FPS) {
                averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
