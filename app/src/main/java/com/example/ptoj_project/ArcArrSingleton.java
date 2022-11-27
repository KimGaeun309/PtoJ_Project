package com.example.custom_view;

public class ArcArrSingleton {
    /*
     내부에 Arc[] 객체 배열을 가지는 싱글톤 객체.
     싱글톤이기 때문에 단 한 번만 생성되므로, MainActivity.java 와 CustomViewTimeTable.java 에서
     DrawArcAttr.getInstance(); 를 통해 가져온 객체는 동일하며, 이 객체 속에 있는 클래스 변수 Arc[] 또한 하나만 있다.
     addNewArc() 를 통해
      */
    private static final ArcArrSingleton myArcAttr = new ArcArrSingleton();
    public Arc[] arcList;
    private int idx;

    ArcArrSingleton() {
        arcList = new Arc[128];
        idx = 0;
    }

    public static ArcArrSingleton getInstance() {
        return myArcAttr;
    }

    public void addNewArc(int startMin, int sweepMin, String color) {
        arcList[idx] = new Arc(startMin, sweepMin, color);
        idx += 1;
    }

    public int getLength() {
        return idx;
    }
}
