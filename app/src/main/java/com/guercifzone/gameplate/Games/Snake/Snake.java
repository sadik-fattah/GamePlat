package com.guercifzone.gameplate.Games.Snake;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.guercifzone.gameplate.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Snake extends AppCompatActivity implements SurfaceHolder.Callback {
private SurfaceView surfaceView;
private TextView scoreTv;
private SurfaceHolder surfaceHolder;
private final List<SnakePoints> snakePositionList = new ArrayList<>();
private String movingPosition = "right";
private int score = 0;
private static final int pointSize = 28;
private static final int defaultPoints = 3;
private static final int snakeColor = Color.GREEN;
private static final int snakeMovingSpeed = 800;
private  int positionX,positionY;
private Timer timer;
private Canvas canvas = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.snake);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
  surfaceView = findViewById(R.id.surfaceView);
  scoreTv = findViewById(R.id.scoreTv);
final AppCompatImageButton topBtn = findViewById(R.id.arrowUp);
final AppCompatImageButton leftBtn = findViewById(R.id.arrowleft);
final AppCompatImageButton rightBtn = findViewById(R.id.arrowright);
final AppCompatImageButton bottomBtn = findViewById(R.id.arrowdown);

surfaceView.getHolder().addCallback(this);
topBtn.setOnClickListener(v -> {
if(movingPosition.equals("down")){
    movingPosition = "up";

}
});
leftBtn.setOnClickListener(v -> {
    if(movingPosition.equals("right")) {
        movingPosition = "left";
    }
});
rightBtn.setOnClickListener(v -> {
    if(movingPosition.equals("left")) {
        movingPosition = "right";
    }
});
bottomBtn.setOnClickListener(v -> {
    if(movingPosition.equals("up")) {
        movingPosition = "down";
    }
});

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
  this.surfaceHolder = holder;
  inity();
    }



    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
    private void inity() {
        snakePositionList.clear();
        scoreTv.setText("0");
        score = 0;
        movingPosition = "right";
     int startPositionX = (pointSize)* defaultPoints;
for (int i = 0; i < defaultPoints; i++) {
    SnakePoints snakePoint = new SnakePoints(startPositionX, pointSize);
    snakePositionList.add(snakePoint);
    startPositionX = startPositionX -  (pointSize * 2);
}
        addPoint();
        moveSnake();
    }

    private void addPoint () {
        int surfaceWidth = surfaceView.getWidth() - (pointSize * 2);
        int surfaceHeight = surfaceView.getHeight() - (pointSize * 2);
int randomXPosition = new Random().nextInt(surfaceWidth / pointSize);
int randomYPosition = new Random().nextInt(surfaceHeight / pointSize);
  if ((randomXPosition % 2) != 0){
      randomXPosition = randomXPosition +1;
  }
if ((randomYPosition % 2) != 0){
    randomYPosition = randomYPosition + 1;

}
positionX = (pointSize * randomXPosition) + pointSize;
positionY = (pointSize * randomYPosition) + pointSize;

    }

    private void moveSnake() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
              int hradPositionX = snakePositionList.get(0).getPositionX();
              int hradPositionY = snakePositionList.get(0).getPositionY();
          if (hradPositionX == positionX && hradPositionY == positionY){
growsnake();
addPoint();
          }
          switch (movingPosition) {
              case "right":
                  snakePositionList.get(0).setPositionX(hradPositionX + (pointSize * 2));
                  snakePositionList.get(0).setPositionY(hradPositionY);
                  break;
              case "left":
                  snakePositionList.get(0).setPositionX(hradPositionX - (pointSize * 2));
                  snakePositionList.get(0).setPositionY(hradPositionY);
                  break;
                case "up":
                    snakePositionList.get(0).setPositionX(hradPositionX );
                    snakePositionList.get(0).setPositionY(hradPositionY - (pointSize * 2));
                    break;
              case "down":
                  snakePositionList.get(0).setPositionX(hradPositionX);
                  snakePositionList.get(0).setPositionY(hradPositionY + (pointSize * 2));
                  break;

          }
          if (checkGameOver(hradPositionX,hradPositionY)){
              timer.purge();
              timer.cancel();
              AlertDialog.Builder builder = new AlertDialog.Builder(Snake.this);
              builder.setMessage("your score = " + score);
              builder.setTitle("Game over");
              builder.setCancelable(false);
              builder.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
inity();
                  }
              });
runOnUiThread(new Runnable() {
    @Override
    public void run() {
        builder.show();
    }
});

          }else{
              canvas = surfaceHolder.lockCanvas();
              canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
              canvas.drawCircle(snakePositionList.get(0).getPositionX(), snakePositionList.get(0).getPositionY(), pointSize, new Paint());

          }
            }
        }, 1000, snakeMovingSpeed);

    }

    private void growsnake() {

    }
    private boolean checkGameOver(int headPositionX, int hradPositionY) {
  boolean gameOver = false;
  return  gameOver;

    }

}

