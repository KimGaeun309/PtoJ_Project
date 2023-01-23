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

    public float isHourAvailable(int hour) {
        float s_hour, e_hour;
        for(int i=0; i<idx; i++) {
            s_hour = ArcList[i].startMinute / 60;
            e_hour = (s_hour + (ArcList[i].sweepMinute / 60)) % 24;
            if (s_hour >= hour && s_hour < hour+1)
                return -1;
            if (e_hour > hour && e_hour <= hour+1)
                return -1;
            if (s_hour < e_hour) {
                if (s_hour >= hour && e_hour <= hour+1)
                    return -1;
            }
            else if (s_hour < hour || e_hour > hour)
                return -1;
        }
        return 0;
    }

    public int isMinAvailable(int start_min, int sweep_min) {
        float s_hour, e_hour;
        float hour, sweep_hour;
        hour = start_min / 60;
        sweep_hour = sweep_min / 60;
        for(int i=0; i<idx; i++) {
            s_hour = ArcList[i].startMinute / 60;
            e_hour = (s_hour + (ArcList[i].sweepMinute / 60)) % 24;

            if (s_hour >= hour && s_hour < hour + sweep_hour)
                return -1;
            if (e_hour > hour && e_hour <= hour+sweep_hour)
                return -2;
            if (s_hour <= e_hour) {
                if (s_hour >= hour && e_hour <= hour+sweep_hour)
                    return -3;
            }
            else{
                if (s_hour <= hour || e_hour >= (hour+sweep_hour)%24)
                    return -4;
            }
        }
        return 0;
    }

    public int findRoutine(float angle) {
        for (int i=0; i<idx; i++) {
            float start_angle, end_angle;
            start_angle = ArcList[i].getStartAngle();
            end_angle = start_angle + ArcList[i].getSweepAngle();
            if (start_angle <= angle && angle <= end_angle)
                return i;
        }
        return -1;
    }

    public void deleteRoutine(int routine_idx) {
        for(int i = routine_idx+1; i < idx; i++) {
            ArcList[i-1] = ArcList[i];
        }
        idx -= 1;
    }
}
