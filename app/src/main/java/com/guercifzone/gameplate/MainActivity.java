package com.guercifzone.gameplate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.guercifzone.gameplate.Adapters.GridAdapter;
import com.guercifzone.gameplate.Games.Breakout;
import com.guercifzone.gameplate.Games.Invaders;
import com.guercifzone.gameplate.Games.Pong.Pong;
import com.guercifzone.gameplate.Games.Snake.Snake;

public class MainActivity extends AppCompatActivity {
    Integer[] imageIDs = {
            R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background, R.drawable.ic_launcher_background
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        GridView gridView = findViewById(R.id.gridView);
        GridAdapter adapter = new GridAdapter(this, imageIDs);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    startActivity(new Intent(MainActivity.this, Snake.class));
                    break;
                case 1:
                    startActivity(new Intent(MainActivity.this, Pong.class));
                    break;
                case 2:
                    startActivity(new Intent(MainActivity.this, Breakout.class));
                    break;
                case 3:
                    startActivity(new Intent(MainActivity.this, Invaders.class));
                    break;
                case 4:
                    startActivity(new Intent(MainActivity.this, Breakout.class));
                    break;
            }



            Toast.makeText(getApplicationContext(), "Item clicked at position " + position, Toast.LENGTH_SHORT).show();
        });

    }
    }
