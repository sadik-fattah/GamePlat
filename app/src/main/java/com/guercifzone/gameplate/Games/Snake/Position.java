package com.guercifzone.gameplate.Games.Snake;

import androidx.annotation.Nullable;

public class Position {
    public int x,y;
    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }
}
