package ru.mycrg.carbonreporter.dto;

public class CoordinateDto {
    private int num;
    private double x;
    private double y;

    public CoordinateDto() {
    }

    public CoordinateDto(int num, double x, double y) {
        this.num = num;
        this.x = x;
        this.y = y;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
