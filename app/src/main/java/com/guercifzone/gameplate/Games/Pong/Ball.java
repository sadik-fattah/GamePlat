package com.guercifzone.gameplate.Games.Pong;


import android.graphics.Paint;

 class Ball {

     float cx ,cy,dx,dy;
     int radius;
     Paint paint;

     public Ball(int radius, Paint paint) {
         this.radius = radius;
         this.paint = paint;
     }
 }
