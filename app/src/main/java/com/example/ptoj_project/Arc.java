package com.example.ptoj_project;


public class Arc {
    /*
    Arc myarc = new Arc(startMin, sweepMin); 으로 생성하면 startAngle, sweepAngle을 계산해 저장해준다.
    myarc.getStartAngle(), myarc.getSweepAngle() 을 통해 startAngle, sweepAngle 을 구할 수 있다.     */
    private float startAngle;
    private float sweepAngle;
    public float startMinute;
    public float sweepMinute;
    private int routine;

    public Arc(int startMin, int sweepMin, int routine) {
        startAngle = (270 + (startMin / (float)4)) % 360;
        sweepAngle = sweepMin / (float)4;
        startMinute = startMin;
        sweepMinute = sweepMin;
        this.routine = routine;
    }
    public float getStartAngle() {
        return startAngle;
    }

    public float getSweepAngle() {
        return sweepAngle;
    }

    public int getRoutine() { return routine; }
}
