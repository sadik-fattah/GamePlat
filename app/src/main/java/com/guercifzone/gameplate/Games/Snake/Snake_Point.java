package com.guercifzone.gameplate.Games.Snake;

public class Snake_Point {
    public final int x, y;
    public Snake_PointType type;
    public Snake_Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.type = Snake_PointType.EMPTY;
    }
}
