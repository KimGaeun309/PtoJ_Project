package com.example.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomViewTimeTable extends View {
    Paint paint;
    Paint paintCircle;
    ArcArrSingleton myArcAttr;

    public CustomViewTimeTable(Context context) {
        super(context);
        init(context);
    }

    public CustomViewTimeTable(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paintCircle = new Paint();
        myArcAttr = ArcArrSingleton.getInstance();
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

        int midpoint_x = canvas.getWidth() / 2;
        int midpoint_y = canvas.getHeight() / 3;
        int radius;
        if (midpoint_x < midpoint_y)
            radius = midpoint_x * 9 / 10;
        else
            radius = midpoint_y * 9 / 10;


        RectF rect = new RectF();
        rect.set(midpoint_x-radius, midpoint_y-radius, midpoint_x+radius, midpoint_y+radius);

        Arc myArc;
        for (int i=0; i<myArcAttr.getLength(); i++) {
            myArc = myArcAttr.arcList[i];
            paint.setColor(Color.parseColor(myArc.getColor()));
            canvas.drawArc(rect, myArc.getStartAngle(), myArc.getSweepAngle(), true, paint);
        }
        canvas.drawArc(rect, 0, 360, true, paintCircle);
    }

}
