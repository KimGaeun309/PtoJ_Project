package com.example.ptoj_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {
    ArcArrSingleton myArcAttr = ArcArrSingleton.getInstance();
    private CustomViewTimeTable CustomViewTT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // ArcArrSingleton 객체의 addArc(int startMin, int sweepMin, String color) 메서드를 사용하여
        // startMin 분 부터 sweepMin 분 동안의 시간을 color 로 칠해야 한다는 부채꼴 그릴 때 필요한 정보들을
        // 차례로 배열에 저장한다. (Singleton 객체이므로 이 배열은 전체 프로젝트에서 하나만 존재한다.)
        // myArcAttr.addArc(0, 120, 0);
        // myArcAttr.addArc(360, 90, 6);


        //Toast.makeText(getApplicationContext(), "addArc() 를 사용해 부채꼴 두 개 생성", Toast.LENGTH_SHORT).show();

        // activity_menu.xml 에 만들어 둔 CustomViewTimeTable 뷰를 id로 가져오기
        CustomViewTT = (CustomViewTimeTable) findViewById(R.id.customViewTT);
    }
}