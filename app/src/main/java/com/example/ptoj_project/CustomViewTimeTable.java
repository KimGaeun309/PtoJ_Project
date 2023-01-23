package com.example.ptoj_project;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;

public class CustomViewTimeTable extends View {
    private Paint paint;
    private Paint paintCircle;
    private ArcArrSingleton myArcAttr;
    private String[] routineColors = {"#ffe4b5", "#48d1cc", "#f0e68c", "#ffc0cb", "#87cddb", "#ffa07a", "#8fbc8f"};
    private int[][] routineCircles = new int[6][3];
    private String[] routineNames = {"Sleep", "Study", "Exercise", "Rest", "Eat", "Outdoor"};
    private int[][] routineCirclesBase = new int[6][3];
    private int[] timeCircle = new int[3];
    private double[][] hourXYs = new double[24][2]; // hourXYs[i] 에는 i 시간에 해당하는 좌표를 2차원 배열로 저장
    int curr_routine = -1;
    int curr_color;
    boolean selectingTime; // 원의 중점에 루틴을 가져간 경우 시간을 선택해야 한다. 시간을 선택하는 중이라면 true, 아니라면 false이다.
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
        selectingTime = false;
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
        canvas.drawPoint(timeCircle[0], timeCircle[1], paintCircle);

        if (routineCircles[0][0] == -1 ) {
            for (int i = 0; i < 6; i++) {
                routineCircles[i][0] = canvas_width * ((i % 3) * 4 + 2) / 12;
                routineCircles[i][1] = canvas_height * ((i / 3) * 2 + 9) / 12;
                routineCircles[i][2] = radius / 4;
            }
            // 처음 draw할 때 hourXYs를 초기화.
            int tmpAngle = 90;
            for(int i=0; i<24; i++) {
                hourXYs[i][0] = midpoint_x + radius * Math.cos(Math.toRadians(tmpAngle));
                hourXYs[i][1] = midpoint_y + radius * -(Math.sin(Math.toRadians(tmpAngle)));
                tmpAngle -= 15;
                if (tmpAngle < 0) {
                    tmpAngle = 345;
                }
                Log.i("태그", "cos(45) : "+Math.cos(45));
                Log.i("태그", "sin(45) : "+Math.sin(45));
            }

            hourXYs[0][0] = midpoint_x;
            hourXYs[0][1] = midpoint_y - radius;
            hourXYs[6][0] = midpoint_x + radius;
            hourXYs[6][1] = midpoint_y;
            hourXYs[12][0] = midpoint_x;
            hourXYs[12][1] = midpoint_y + radius;
            hourXYs[18][0] = midpoint_x - radius;
            hourXYs[18][1] = midpoint_y;



        }

        for (int i = 0; i < 6; i++) {
            routineCirclesBase[i][0] = canvas_width * ((i % 3) * 4 + 2) / 12;
            routineCirclesBase[i][1] = canvas_height * ((i / 3) * 2 + 9) / 12;
            routineCirclesBase[i][2] = radius / 4;
        }

        for (int i=0; i<6; i++) {
            paintCircle.setColor(Color.parseColor(routineColors[i]));
            canvas.drawCircle(routineCircles[i][0], routineCircles[i][1], routineCircles[i][2], paintCircle);
            canvas.drawCircle(routineCirclesBase[i][0], routineCirclesBase[i][1], routineCirclesBase[i][2], paintCircle);
        }

        // 시간을 선택해야 하는 경우 시간마다 파란색으로 표시한다.
        if (selectingTime == true) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(8f);
            for(int i=0; i<24; i++) {
                if (hourXYs[i][0] == 0 || hourXYs[i][1] == 0)
                    continue;
                if (myArcAttr.isHourAvailable(i) == -1)
                    continue;
                canvas.drawCircle((float) hourXYs[i][0], (float) hourXYs[i][1], routineCircles[0][2]/4, paint);
            }
            //canvas.drawLine(0, 0, midpoint_x, midpoint_y, paint);
            //Toast.makeText(getContext(), "dot painted(" + hourXYs[3][0] + ", " + hourXYs[3][1] + ")" , Toast.LENGTH_SHORT).show();

        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean value = super.onTouchEvent(event);

//        int down_x, down_y;
        int up_x, up_y;
//        int start_hour = -1;
        int stop_hour = -1;
        // 시간을 선택하는 중이 아니라면 루틴을 선택해 드래그 앤 드롭을 하는 기능을 해야 한다.
        if (selectingTime == false) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    return true;
                }

                case MotionEvent.ACTION_MOVE: {
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    double dx, dy;

                    for (int i = 0; i < 6; i++) {
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
                    if (dx + dy < Math.pow(routineCircles[0][2], 2)) {
                        selectingTime = true;
                        routineCircles[curr_routine][0] = routineCirclesBase[curr_routine][0];
                        routineCircles[curr_routine][1] = routineCirclesBase[curr_routine][1];
                        routineCircles[curr_routine][2] = routineCirclesBase[curr_routine][2];
                        curr_color = curr_routine;
                        //Toast.makeText(getContext(), "curr color : "+curr_color, Toast.LENGTH_SHORT).show();
                        curr_routine = -1;
                        postInvalidate();
                        return false;
                    } else if (dx + dy < Math.pow(timeCircle[2], 2)) {
                        for (int i = 0; i < 6; i++) {
                            dx = Math.pow(x - routineCircles[i][0], 2);
                            dy = Math.pow(y - routineCircles[i][1], 2);
                            if (dx + dy < Math.pow(routineCircles[i][2], 2)) {
                                // Dropped
                                // Toast.makeText(getContext(), "dropped", Toast.LENGTH_SHORT).show();


                                Intent myintent = new Intent(getContext(), TimePicker.class);
                                myintent.putExtra("idx", i);
                                myintent.putExtra("arclist_idx", myArcAttr.getLength());
                                getContext().startActivity(myintent);

                                //myArcAttr.addArc(500, 60,  i);

                                routineCircles[i][0] = routineCirclesBase[i][0];
                                routineCircles[i][1] = routineCirclesBase[i][1];
                                routineCircles[i][2] = routineCirclesBase[i][2];

                                curr_routine = -1;
                                postInvalidate();
                                return false;
                            }
                        }
                        // 루틴을 드래그 앤 드롭한 것이 아니라 타임테이블 원 안을 그냥 터치한 경우
                        // 클릭한 위치에 배정된 루틴이 있는 경우 (= 사용자가 시간표에 얹어진 루틴을 클릭한 경우) 를 판별해
                        // 이 경우 터치된 루틴을 수정 / 삭제하는 코드가 작성되어야 한다.

                        int routine_idx;
                        float angle;
                        angle = (float) Math.toDegrees(Math.atan2(y - timeCircle[1], x - timeCircle[0]));
                        if (angle < 0) angle = 360 + angle; // 터치한 곳의 좌표에 따른 좌표 계산
                        routine_idx = myArcAttr.findRoutine(angle);
                        if (routine_idx >= 0) {
                            int startH = 0, startM = 0, sweepH = 0, sweepM = 0;
                            startH = (int) (myArcAttr.ArcList[routine_idx].startMinute / 60);
                            startM = (int) myArcAttr.ArcList[routine_idx].startMinute - 60 * startH;
                            sweepH = (int) (myArcAttr.ArcList[routine_idx].sweepMinute / 60);
                            sweepM = (int) myArcAttr.ArcList[routine_idx].sweepMinute - 60 * sweepH;

                            AlertDialog.Builder builder;

                            String[] dialog_menus;

                            dialog_menus = getResources().getStringArray(R.array.dialog_menu);
                            builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("[" + routineNames[myArcAttr.ArcList[routine_idx].getRoutine()] + "] " + startH + "시 " + startM + "분 부터 " + sweepH + "시간 " + sweepM + "분 동안");

                            builder.setItems(dialog_menus, new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   //Toast.makeText(getContext(), "선택된 것은 " + dialog_menus[which] , Toast.LENGTH_SHORT).show();
                                   if (which == 0) { // Delete
                                       AlertDialog.Builder msgBuilder = new AlertDialog.Builder(getContext())
                                               .setTitle("루틴 삭제")
                                               .setMessage("이 루틴을 정말 삭제하시겠습니까?")
                                               .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialogInterface, int i) {
                                                       myArcAttr.deleteRoutine(routine_idx);
                                                       postInvalidate();
                                                   }
                                               })
                                               .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialogInterface, int i) {
                                                   }
                                               });
                                       AlertDialog msgDlg = msgBuilder.create();
                                       msgDlg.show();
                                   }
                                   else if (which == 1) { // Modify
                                       Intent myintent = new Intent(getContext(), TimePicker.class);
                                       myintent.putExtra("idx", myArcAttr.ArcList[routine_idx].getRoutine());
                                       myintent.putExtra("arclist_idx", routine_idx);
                                       getContext().startActivity(myintent);

                                   }
                               }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        }

                    }

                    return value;
                }

            }
        }
        // 시간을 선택하는 중이라면 시간을 선택해 1시간짜리 루틴을 설정하도록 해야 한다.
        else {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN: {

                    return true;
                }

                case MotionEvent.ACTION_MOVE: {

//                    down_x = (int) event.getX();
//                    down_y = (int) event.getY();
//
//                    for(int i=0; i<24; i++) {
//                        double dx = Math.pow(down_x - hourXYs[i][0], 2);
//                        double dy = Math.pow(down_y - hourXYs[i][1], 2);
//                        if (dx + dy < Math.pow(routineCircles[0][2] / 4, 2)) {
//                            if (start_hour == -1) {
//                                start_hour = i;
//                            }
//
//                        }
//                    }

                    return value;
                }
                case MotionEvent.ACTION_UP: {
                    up_x = (int) event.getX();
                    up_y = (int) event.getY();
                    for(int i=0; i<24; i++) {
                        double dx = Math.pow(up_x - hourXYs[i][0], 2);
                        double dy = Math.pow(up_y - hourXYs[i][1], 2);
                        if (dx + dy < Math.pow(routineCircles[0][2] / 4, 2)) {
                            stop_hour = i;
                            myArcAttr.addArc(stop_hour * 60,  60,  curr_color);
                            //Toast.makeText(getContext(), "stopHour:"+stop_hour, Toast.LENGTH_SHORT).show();

                            selectingTime = false;
                            postInvalidate();
                        }
                    }

                    selectingTime = false;
                    postInvalidate();

                    return value;
                }

            }
        }

        return value;
    }


}
