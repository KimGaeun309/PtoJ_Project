package com.example.ptoj_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {
    ArcArrSingleton myArcAttr = ArcArrSingleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // ArcArrSingleton 객체의 addArc(int startMin, int sweepMin, String color) 메서드를 사용하여
        // startMin 분 부터 sweepMin 분 동안의 시간을 color 로 칠해야 한다는 부채꼴 그릴 때 필요한 정보들을
        // 차례로 배열에 저장한다. (Singleton 객체이므로 이 배열은 전체 프로젝트에서 하나만 존재한다.)
        myArcAttr.addArc(0, 120, "#FF0000");
        myArcAttr.addArc(360, 90, "#00FF00");

        // 커스텀뷰인 CustomViewTimeTable을 새로 만들어 setContentView()에 인자로 전달하면
        // CustomViewTimeTable 에서 만든 커스텀뷰가 화면에 나타난다.
        // CustomViewTimeTable 에서는 ArcArrSingleton 에 있는 배열에서 정보들을 가져와 부채꼴을 그린다.
        CustomViewTimeTable view = new CustomViewTimeTable(this);
        setContentView(view);
    }
}