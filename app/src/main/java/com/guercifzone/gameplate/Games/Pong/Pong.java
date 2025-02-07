package com.guercifzone.gameplate.Games.Pong;



import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.guercifzone.gameplate.R;

public class Pong extends AppCompatActivity {
private PongView PongView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(PongView);

        }
    @Override
    protected void onPause() {
        super.onPause();
        PongView.setThreadRunning(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PongView.setThreadRunning(true);
    }
    }
