package com.zackstrikesback.bouncingball;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class CustomView extends View {
    private List<Ball> balls = new ArrayList<>();
    private int r;
    private int dir;
    private int sp;

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomView,
                0, 0);
        try {
            r = a.getInteger(R.styleable.CustomView_radius, 0);
            dir = a.getInteger(R.styleable.CustomView_bounce, 0);
            Log.d("direction", String.valueOf(dir));
            sp = a.getInteger(R.styleable.CustomView_speed, 0);
        } finally {
            a.recycle();
        }
        initialize();
    }

    private void initialize() {
        balls.add(new Ball(50, 50, r, sp, dir, Color.RED));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        for(Ball ball : balls){
            ball.bounce(canvas);
            canvas.drawOval(ball.oval, ball.paint);
        }
        invalidate();
    }

    public class Ball{
        public int x,y;
        public int rad;
        public int v;
        public int direction;
        public Paint paint;
        public RectF oval;

        public Ball(int x, int y, int radius, int speed, int direction, int color){
            this.x = x;
            this.y = y;
            this.rad = radius;
            this.v = speed;
            this.direction = direction;
            this.paint = new Paint();
            this.paint.setColor(color);
        }

        public void bounce(Canvas canvas){
            if(direction == 0){ //up and down
                this.y += v;
            }else { //left and right
                this.x += v;
            }
            this.oval = new RectF(x-rad, y-rad, x+rad, y+rad);

            Rect bounds = new Rect();
            this.oval.roundOut(bounds);

            if(!canvas.getClipBounds().contains(bounds)){
                if(this.x-rad*2 < 0 || this.x+rad*2 > canvas.getWidth()){
                    v = v*-1;
                }
                if(this.y-rad*2 < 0 || this.y+rad*2 > canvas.getHeight()){
                    v = v*-1;
                }
            }
        }
    }
}
