package org.example.Recognition;

import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Набор данных:
 * - Изображение Mat
 * - X
 * - Y
 * - Z
 * - Radius
 */
public class MatImgAndXYZRadius {

    public Mat img;
    public List<Double> X;
    public List<Double> Y;
    public List<Double> Z;
    public List<Double> Radius;

    public boolean isRecogn;

    public MatImgAndXYZRadius(Mat img) {
        this.img = img;
        X = new LinkedList<>();
        Y = new LinkedList<>();
        Z = new LinkedList<>();
        Radius = new LinkedList<>();
        isRecogn = false;
    }

    public MatImgAndXYZRadius() {
        X = new ArrayList<>();
        Y = new ArrayList<>();
        Z = new ArrayList<>();
        Radius = new ArrayList<>();
        isRecogn = false;
    }

    public Mat getImg() {
        if (img != null)
            return img;
        else
            return new Mat();
    }

    public void setImg(Mat img) {
        this.img = img;
    }

    public List<Double> getX() {
        return X;
    }

    public void setX(double x) {
        X = Collections.singletonList(x);
    }

    public List<Double> getY() {
        return Y;
    }

    public void setY(double y) {
        Y = Collections.singletonList(y);
    }

    public List<Double> getZ() {
        return Z;
    }

    public void setZ(double z) {
        Z = Collections.singletonList(z);
    }

    public List<Double> getRadius() {
        return Radius;
    }

    public void setRadius(double radius) {
        Radius = Collections.singletonList(radius);
    }

    public void addX(double x) {
        X.add(x);
    }

    public void addY(double y) {
        Y.add(y);
    }

    public void addZ(double z) {
        Z.add(z);
    }

    public void addRadius(double radius) {
        Radius.add(radius);
    }

    public void clearAll() {
        if (X.size() > 0)
            X = new ArrayList<>();
        if (Y.size() > 0)
            Y = new ArrayList<>();
        if (Z.size() > 0)
            Z = new ArrayList<>();
        if (Radius.size() > 0)
            Radius = new ArrayList<>();
        isRecogn = false;
    }

    public double getAverageRadius() {
        double res = 0;
        if (Radius.size() > 0) {
            for (int i = 0; i < Radius.size(); i++) {
                res += Radius.get(i);
            }
            return res / Radius.size();
        }
        return 0;
    }

    public double getAverageX() {
        double res = 0;
        if (X.size() > 0) {
            for (int i = 0; i < X.size(); i++) {
                res += X.get(i);
            }
            return res / X.size();
        }
        return 0;
    }

    public double getAverageY() {
        double res = 0;
        if (Y.size() > 0) {
            for (int i = 0; i < Y.size(); i++) {
                res += Y.get(i);
            }
            return res / Y.size();
        }
        return 0;
    }

    public double getFirstRadius() {
        double res = 0;
        if (Radius.size() > 0) {
            return Radius.get(0);
        }
        return 0;
    }

    public double getFirstX() {
        double res = 0;
        if (X.size() > 0) {
            return X.get(0);
        }
        return 0;
    }

    public double getFirstY() {
        double res = 0;
        if (Y.size() > 0) {
            return Y.get(0);
        }
        return 0;
    }

    public boolean isRecogn() {
        return isRecogn;
    }

    public void setRecogn(boolean recogn) {
        isRecogn = recogn;
    }
}
