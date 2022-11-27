package com.example.ptoj_project;


public class Arc {
    /*
    Arc myarc = new Arc(startMin, sweepMin); 으로 생성하면 startAngle, sweepAngle을 계산해 저장해준다.
    myarc.getStartAngle(), myarc.getSweepAngle() 을 통해 startAngle, sweepAngle 을 구할 수 있다.     */
    private float startAngle;
    private float sweepAngle;
    private String color;

    public Arc(int startMin, int sweepMin, String color) {
        startAngle = (270 + (startMin / (float)4)) % 360;
        sweepAngle = sweepMin / (float)4;
        this.color = color;
    }
    public float getStartAngle() {
        return startAngle;
    }

    public float getSweepAngle() {
        return sweepAngle;
    }

    public String getColor() {
        return color;
    }
}
