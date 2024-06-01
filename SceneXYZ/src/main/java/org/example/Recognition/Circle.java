package org.example.Recognition;

/**
* Класс окружность
* Поля класса - координаты Х, У и радиус окружности
* Методы - получить, установить поля
 */
public class Circle {

    /**
     * Координата Х
     */
    private double X;

    /**
     * Координата У
     */
    private double Y;

    /**
     * Радиус
     */
    private double Radius;

    /**
     * Конструктор по-умолчанию
     */
    public Circle()
    {
        ;
    }

    /**
     * Конструктор с параметром
     * @param circle - окружность
     */
    public Circle(Circle circle)
    {
        X = circle.getX();
        Y = circle.getY();
        Radius = circle.getRadius();
    }

    /**
     * Конструктор с параметрами
     * @param x - х
     * @param y - у
     * @param radius - радиус
     */
    public Circle(double x, double y, double radius)
    {
        X = x;
        Y = y;
        Radius = radius;
    }

    /**
     * Установить окружность
     * @param x - х
     * @param y - у
     * @param radius - радиус
     */
    public void SetCircle(double x, double y, double radius)
    {
        X = x;
        Y = y;
        Radius = radius;
    }

    /**
     * установить окружность
     * @param circle - окружность
     */
    public void SetCircle(Circle circle)
    {
        X = circle.getX();
        Y = circle.getY();
        Radius = circle.getRadius();
    }

    /**
     * Получить Х
     * @return - Х
     */
    public double getX() {
        return X;
    }

    /**
     * Установить Х
     * @param x - Х
     */
    public void setX(double x) {
        X = x;
    }

    /**
     * Получить У
     * @return - У
     */
    public double getY() {
        return Y;
    }

    /**
     * Установить У
     * @param y - У
     */
    public void setY(double y) {
        Y = y;
    }

    /**
     * Получить радиус
     * @return - радиус
     */
    public double getRadius() {
        return Radius;
    }

    /**
     * Установить радиус
     * @param radius - радиус
     */
    public void setRadius(double radius) {
        Radius = radius;
    }

    /**
     * В строку
     * @return - строка
     */
    @Override
    public String toString() {
        return "Circle{" +
                "X=" + X +
                ", Y=" + Y +
                ", Radius=" + Radius +
                '}';
    }

    /**
     * Является ли рядом
     * @param other - другая окружность
     * @param radius - радиус
     * @return - результат проверки
     */
    public boolean isNear(Circle other, int radius) {
        boolean result = false;

        if (((X >= other.getX() - radius) && (X <= other.getX() + radius)) &&
                ((Y >= other.getY() - radius) && (Y <= other.getY() + radius)))
            result = true;

        return result;
    }

    /**
     * Является ли нулем
     * @return - результат проверки
     */
    public boolean isZero()
    {
        return X == 0 && Y == 0 && Radius == 0;
    }
}
