package com.example.ptoj_project;

public class ArcArrSingleton {
    /*
     내부에 Arc[] 객체 배열을 가지는 싱글톤 객체.
     싱글톤이기 때문에 단 한 번만 생성되므로, MainActivity.java 와 CustomViewTimeTable.java 에서
     DrawArcAttr.getInstance(); 를 통해 가져온 객체는 동일하며, 이 객체 속에 있는 클래스 변수 Arc[] 또한 하나만 있다.
     addArc() 를 통해 Arc[] 배열에 새로운 객체를 추가할 수 있다. (delArc() 함수도 후에 구현할 예정.)
      */
    private static final ArcArrSingleton myArcAttr = new ArcArrSingleton();
    public Arc[] ArcList;
    private int idx;

    ArcArrSingleton() {
        ArcList = new Arc[128];
        idx = 0;
    }

    public static ArcArrSingleton getInstance() {
        return myArcAttr;
    }

    public void addArc(int startMin, int sweepMin, int routine) {
        ArcList[idx] = new Arc(startMin, sweepMin, routine);
        idx += 1;
    }

    public int getLength() {
        return idx;
    }
}
