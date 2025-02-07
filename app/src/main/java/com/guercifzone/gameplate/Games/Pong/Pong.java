package com.guercifzone.gameplate.Games.Pong;



import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.guercifzone.gameplate.R;

public class Pong extends AppCompatActivity {

    private static final int MENU_NEW_GAME = 1;
    private static final int MENU_RESUME = 2;
    private static final int MENU_EXIT = 3;
    private Pong_Thread mGameThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pong);


    }

}
