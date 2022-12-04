package com.example.ptoj_project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.Nullable;

public class CustomViewTimeTable extends View {
    private Paint paint;
    private Paint paintCircle;
    private ArcArrSingleton myArcAttr;
    private String[] routineColors = {"#ffe4b5", "#48d1cc", "#f0e68c", "#ffc0cb", "#87cddb", "#ffa07a", "#8fbc8f"};
    private int[][] routineCircles = new int[6][3];
    private int[][] routineCirclesBase = new int[6][3];
    private int[] timeCircle = new int[3];
    int curr_routine = -1;

    public CustomViewTimeTable(Context context) {
        super(context);
        init(context);
    }

    public CustomViewTimeTable(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        myArcAttr = ArcArrSingleton.getInstance();
        routineCircles[0][0] = -1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        paint.setStrokeWidth(6f);
        paint.setColor(Color.parseColor("#FF0000"));
        paint.setStyle(Paint.Style.FILL);

        paintCircle.setStrokeWidth(6f);
        paintCircle.setColor(Color.parseColor("#000000"));
        paintCircle.setStyle(Paint.Style.STROKE);

        int canvas_width = canvas.getWidth();
        int canvas_height = canvas.getHeight();
        int midpoint_x = canvas_width / 2;
        int midpoint_y = canvas_height / 3;
        int radius;
        if (midpoint_x < midpoint_y)
            radius = midpoint_x * 9 / 10;
        else
            radius = midpoint_y * 9 / 10;


        RectF rect = new RectF();
        rect.set(midpoint_x-radius, midpoint_y-radius, midpoint_x+radius, midpoint_y+radius);

        Arc myArc;
        for (int i=0; i<myArcAttr.getLength(); i++) {
            myArc = myArcAttr.ArcList[i];
            paint.setColor(Color.parseColor(routineColors[myArc.getRoutine()]));
            canvas.drawArc(rect, myArc.getStartAngle(), myArc.getSweepAngle(), true, paint);
        }

        timeCircle[0] = midpoint_x;
        timeCircle[1] = midpoint_y;
        timeCircle[2] = radius;
        canvas.drawCircle(timeCircle[0], timeCircle[1], timeCircle[2], paintCircle);

        if (routineCircles[0][0] == -1 ) {
            for (int i = 0; i < 6; i++) {
                routineCircles[i][0] = canvas_width * ((i % 3) * 4 + 2) / 12;
                routineCircles[i][1] = canvas_height * ((i / 3) * 2 + 9) / 12;
                routineCircles[i][2] = radius / 4;
            }
        }

        for (int i = 0; i < 6; i++) {
            routineCirclesBase[i][0] = canvas_width * ((i % 3) * 4 + 2) / 12;
            routineCirclesBase[i][1] = canvas_height * ((i / 3) * 2 + 9) / 12;
            routineCirclesBase[i][2] = radius / 4;
        }

//        canvas.drawCircle(canvas_width / 6, canvas_height * 9 / 12, radius / 4, paintCircle);
//        canvas.drawCircle(canvas_width / 2, canvas_height * 9 / 12, radius / 4, paintCircle);
//        canvas.drawCircle(canvas_width * 5 / 6, canvas_height * 9 / 12, radius / 4, paintCircle);
//        canvas.drawCircle(canvas_width / 6, canvas_height * 11 / 12, radius / 4, paintCircle);
//        canvas.drawCircle(canvas_width / 2, canvas_height * 11 / 12, radius / 4,  paintCircle);
//        canvas.drawCircle(canvas_width * 5 / 6, canvas_height * 11 / 12, radius / 4, paintCircle);

        for (int i=0; i<6; i++) {
            paintCircle.setColor(Color.parseColor(routineColors[i]));
            canvas.drawCircle(routineCircles[i][0], routineCircles[i][1], routineCircles[i][2], paintCircle);
            canvas.drawCircle(routineCirclesBase[i][0], routineCirclesBase[i][1], routineCirclesBase[i][2], paintCircle);
        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean value = super.onTouchEvent(event);
//        setWillNotDraw(false);



        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                return true;
            }

            case MotionEvent.ACTION_MOVE: {
                int x = (int) event.getX();
                int y = (int) event.getY();
                double dx, dy;


                for(int i=0; i<6; i++) {
                    dx = Math.pow(x - routineCircles[i][0], 2);
                    dy = Math.pow(y - routineCircles[i][1], 2);
                    if (dx + dy < Math.pow(routineCircles[i][2], 2)) {
                        // Touched
                        if (curr_routine == -1 || curr_routine == i) {
                            curr_routine = i;
                            routineCircles[i][0] = x;
                            routineCircles[i][1] = y;

                            postInvalidate();
                            return true;
                        }
                    }
                }
                return value;
            }
            case MotionEvent.ACTION_UP: {
                int x = (int) event.getX();
                int y = (int) event.getY();

                double dx = Math.pow(x - timeCircle[0], 2);
                double dy = Math.pow(y - timeCircle[1], 2);
                if (dx + dy < Math.pow(timeCircle[2], 2)) {
                    for(int i=0; i<6; i++) {
                        dx = Math.pow(x - routineCircles[i][0], 2);
                        dy = Math.pow(y - routineCircles[i][1], 2);
                        if (dx + dy < Math.pow(routineCircles[i][2], 2)) {
                            // Dropped

                            Toast.makeText(getContext(), "dropped", Toast.LENGTH_SHORT).show();

                            myArcAttr.addArc(500, 60,  i);

                            routineCircles[i][0] = routineCirclesBase[i][0];
                            routineCircles[i][1] = routineCirclesBase[i][1];
                            routineCircles[i][2] = routineCirclesBase[i][2];

                            curr_routine = -1;
                            postInvalidate();
                            return false;
                        }
                    }
                }

                return value;
            }

        }

        return value;
    }


}
