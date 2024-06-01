package org.example.Recognition;

// Класс с параметрами цвета распозноваемых окружностей
// Поля - автомтическая или ручная настройка параметров, цвет в виде R, G, B, и дельта - отличие цвета на изображении в соответсвии с выбранным
// Методы - получить, установить
public class ColorParams {

    // Автоматические настройки распознования
    private boolean ISAutomaticColor = true;

    // RGB
    private double red = 255;
    private double green = 0;
    private double blue = 0;

    // Разница в выбранном цвете и пикселями
    private int Delta = 50;

    public ColorParams() {;}

    public ColorParams(boolean automaticColor, double red, double green, double blue, int Delta)
    {
        setISAutomaticColor(automaticColor);
        this.setRed(red);
        this.setGreen(green);
        this.setBlue(blue);
        this.setDelta(Delta);
    }

    public void SetColorParams(boolean automaticColor, double red, double green, double blue, int Delta)
    {
        setISAutomaticColor(automaticColor);
        this.setRed(red);
        this.setGreen(green);
        this.setBlue(blue);
        this.setDelta(Delta);
    }

    public boolean isISAutomaticColor() {
        return ISAutomaticColor;
    }

    public void setISAutomaticColor(boolean ISAutomaticColor) {
        this.ISAutomaticColor = ISAutomaticColor;
    }

    public double getRed() {
        return red;
    }

    public void setRed(double red) {
        this.red = red;
    }

    public double getGreen() {
        return green;
    }

    public void setGreen(double green) {
        this.green = green;
    }

    public double getBlue() {
        return blue;
    }

    public void setBlue(double blue) {
        this.blue = blue;
    }

    public int getDelta() {
        return Delta;
    }

    public void setDelta(int delta) {
        Delta = delta;
    }
}
