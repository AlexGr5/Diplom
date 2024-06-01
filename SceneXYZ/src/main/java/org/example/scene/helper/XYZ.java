package org.example.scene.helper;

/**
 * Координаты X, Y, Z
 */
public class XYZ {
    private double x = 0;
    private double y = 0;
    private double z = 0;

    public XYZ(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public XYZ() {
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

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setXYZ(XYZ xyz) {
        this.x = xyz.getX();
        this.y = xyz.getY();
        this.z = xyz.getZ();
    }

    public void setXYZ(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "XYZ{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
