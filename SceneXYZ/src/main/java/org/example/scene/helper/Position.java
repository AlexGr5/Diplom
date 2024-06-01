package org.example.scene.helper;

import org.example.HighLevel.ModelRobot;
import org.example.Recognition.ColorParams;
import org.example.Recognition.Mark;

/**
 * Координаты позиции в пространстве
 */
public class Position {

    private ModelRobot modelRobot = null;

    private XYZ currentXYZ = new XYZ();

    private XYZ minXYZ = new XYZ();
    private XYZ maxXYZ = new XYZ();

    // изменение угла на одном шаге в радианах
    //final double oneDegreeStep = 0.070; // 0.085
    final double oneDegreeStep = 0.140; // 8 градусов

    // изменение радиуса
    final int oneRadiusStep = 12;

    private double radius = 1;
    private double teta = 1;
    private double fi = 1;

    public double getX() {
        return currentXYZ.getX();
    }

    public double getY() {
        return currentXYZ.getY();
    }

    public double getZ() {
        return currentXYZ.getZ();
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getTeta() {
        return teta;
    }

    public void setTeta(double teta) {
        this.teta = teta;
    }

    public double getFi() {
        return fi;
    }

    public void setFi(double fi) {
        this.fi = fi;
    }

    public void setCurrentXYZ(double x, double y, double z) {
        this.currentXYZ.setX(x);
        this.currentXYZ.setY(y);
        this.currentXYZ.setZ(z);
    }

    public void setCurrentXYZ(XYZ xyz) {
        this.currentXYZ.setX(xyz.getX());
        this.currentXYZ.setY(xyz.getY());
        this.currentXYZ.setZ(xyz.getZ());
    }

    public Position(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, ModelRobot modelRobot) {

        this.minXYZ = new XYZ(minX, minY, minZ);
        this.maxXYZ = new XYZ(maxX, maxY, maxZ);
        this.modelRobot = modelRobot;
        //generateNewXYZ();
    }

    public Position(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {

        this.minXYZ = new XYZ(minX, minY, minZ);
        this.maxXYZ = new XYZ(maxX, maxY, maxZ);
        this.modelRobot = null;
        //generateNewXYZ();
    }

    public boolean isOutsideBounds() {
        return currentXYZ.getX() > maxXYZ.getX() || currentXYZ.getY() > maxXYZ.getY() ||
                currentXYZ.getZ() > maxXYZ.getZ() || currentXYZ.getX() < minXYZ.getX() ||
                currentXYZ.getY() < minXYZ.getY() || currentXYZ.getZ() < minXYZ.getZ();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Position position = (Position) o;
        return currentXYZ.getX() == position.getX() &&
                currentXYZ.getY() == position.getY() &&
                currentXYZ.getZ() == position.getZ();
    }

    public String toString() {
        return "Position{x=" + currentXYZ.getX() + ", y=" + currentXYZ.getY()
                + ", z=" + currentXYZ.getZ()  +'}';
    }


    public void generateNewXYZ() {
        currentXYZ.setX(Math.ceil(minXYZ.getX() +60 + (Math.random() * (maxXYZ.getX() - minXYZ.getX() -120))));
        currentXYZ.setY(Math.ceil(minXYZ.getY() +60 + (Math.random() * (maxXYZ.getY() - minXYZ.getY() -120))));
        currentXYZ.setZ(Math.ceil(minXYZ.getZ() +60 + (Math.random() * (maxXYZ.getZ() - minXYZ.getZ() -120))));
        /*
        System.out.println("New X = " + currentXYZ.getX() +
                            ", Y = " + currentXYZ.getY() +
                            ", Z = " + currentXYZ.getZ());
        */
        countRadiusTetaFi();
    }


    // Метод для подсчета расстояния между точками
    public double distance(Position other) {
        // Вычисляем разность координат
        double dx = other.getX() - currentXYZ.getX();
        double dy = other.getY() - currentXYZ.getY();
        double dz = other.getZ() - currentXYZ.getZ();

        // Возвращаем корень из суммы квадратов разностей
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public void displayMotion(String motion) {
        System.out.println(motion);
    }

    /**
     * Флаг для быстрого ручного изменения управления системой координат
     * 1 - управление как в декартовой системе координат
     * 2 - управление как в сферической классической системе координат
     * 3 - управление в моей модели сферических координат (управление радиусом, тетой, фи в зависимости от положения текущей точки)
     * 4 - управление роботом
     *
     */
    private final int CoordinateSystemFlag = 2;

    public void plusOneToX() {

        switch (CoordinateSystemFlag) {
            case 1: {
                //=========================================
                // Декартова система координат
                currentXYZ.setX(currentXYZ.getX() + 1);
                //=========================================
                break;
            }
            case 2: {
                //=========================================
                // Классическая сферическая система координат
                currentXYZ.setXYZ(plusOneToFiSphere());
                //=========================================
                break;
            }
            case 3: {
                //========================================================
                // Сферическая система координат с улучшением
                if (fi >= - Math.PI * 5 / 6 && fi <= - Math.PI / 6)
                    currentXYZ.setXYZ(plusOneToFiSphere());
                else
                    if (fi <= Math.PI * 5 / 6 && fi >= Math.PI / 6)
                        currentXYZ.setXYZ(minusOneToFiSphere());
                    else
                        if (currentXYZ.getX() > 0)
                            currentXYZ.setXYZ(plusOneToRadiusSphere());
                        else
                            currentXYZ.setXYZ(minusOneToRadiusSphere());
                //========================================================
                break;
            }
            case 4: {


                break;
            }
            default: {
                break;
            }
        }


        //displayXYZ();
        //displayRTF();
        //displayMotion("plusOneToX");

    }

    public void plusOneToY() {

        switch (CoordinateSystemFlag) {
            case 1: {
                //=========================================
                // Декартова система координат
                currentXYZ.setY(currentXYZ.getY() + 1);
                //=========================================
                break;
            }
            case 2: {
                //=========================================
                // Классическая сферическая система координат
                currentXYZ.setXYZ(plusOneToTetaSphere());
                //=========================================
                break;
            }
            case 3: {
                //========================================================
                // Сферическая система координат с улучшением
                if (fi >= - Math.PI / 3 && fi <= 0)
                    currentXYZ.setXYZ(plusOneToFiSphere());
                else
                    if (fi <= - Math.PI * 2 / 3 && fi >= - Math.PI)
                        currentXYZ.setXYZ(minusOneToFiSphere());
                    else
                        if (fi <= Math.PI / 3 && fi >= 0)
                            currentXYZ.setXYZ(plusOneToFiSphere());
                        else
                            if (fi >= Math.PI * 2 / 3 && fi <= Math.PI)
                                currentXYZ.setXYZ(minusOneToFiSphere());
                            else
                                if (currentXYZ.getY() > 0)
                                    currentXYZ.setXYZ(plusOneToRadiusSphere());
                                else
                                    currentXYZ.setXYZ(minusOneToRadiusSphere());
                //========================================================
                break;
            }
            case 4: {


                break;
            }
            default: {
                break;
            }
        }




        //displayXYZ();
        //displayRTF();
        //displayMotion("plusOneToY");





//        if (fi >= - Math.PI / 4 && fi <= 0)
//            currentXYZ.setXYZ(plusOneToFiSphere());
//        else
//            if (fi <= - Math.PI * 5 / 8 && fi >= - Math.PI)
//                currentXYZ.setXYZ(minusOneToFiSphere());
//            else
//                if (fi <= Math.PI / 4 && fi >= 0)
//                    currentXYZ.setXYZ(plusOneToFiSphere());
//                else
//                    if (fi >= Math.PI * 5 / 8 && fi <= Math.PI)
//                        currentXYZ.setXYZ(minusOneToFiSphere());
//                    else
//                        if (currentXYZ.getY() > 0)
//                            currentXYZ.setXYZ(plusOneToRadiusSphere());
//                        else
//                            currentXYZ.setXYZ(minusOneToRadiusSphere());


    }

    public void plusOneToZ() {

        switch (CoordinateSystemFlag) {
            case 1: {
                //=========================================
                // Декартова система координат
                currentXYZ.setZ(currentXYZ.getZ() + 1);
                //=========================================
                break;
            }
            case 2: {
                //=========================================
                // Классическая сферическая система координат
                currentXYZ.setXYZ(plusOneToRadiusSphere());
                //=========================================
                break;
            }
            case 3: {
                //========================================================
                // Сферическая система координат с улучшением
                if (teta > Math.PI / 6)
                    currentXYZ.setXYZ(minusOneToTetaSphere());
                else
                    currentXYZ.setXYZ(plusOneToRadiusSphere());
                //========================================================
                break;
            }
            case 4: {


                break;
            }
            default: {
                break;
            }
        }


        //displayXYZ();
        //displayRTF();
        //displayMotion("plusOneToZ");
        //currentXYZ.setXYZ(minusOneToTetaSphere());

        // если 3/4 PI > fi > 1/4 PI    -    radius++
        // иначе если fi > 3/4 PI       -    fi--
        //       иначе                  -    fi++

        //System.out.println("r = " + radius + " t = " + teta + " f = " + fi);

        //if (fi < (double) 3 / 4 * Math.PI && fi > (double) 1 / 4 * Math.PI) {





//        if (teta <= Math.PI * 5 / 12 && teta >= 0)
//            currentXYZ.setXYZ(minusOneToTetaSphere());
//        else
//            if(teta >= Math.PI * 3 / 4 && teta <= Math.PI)
//                currentXYZ.setXYZ(minusOneToTetaSphere());


//        }
//        else {
//            if (fi >= (double) 3 / 4 * Math.PI) {
//                currentXYZ.setXYZ(minusOneToFiSphere());
//            }
//            else
//                currentXYZ.setXYZ(plusOneToFiSphere());
//        }
    }

    public void minusOneToX() {

        switch (CoordinateSystemFlag) {
            case 1: {
                //=========================================
                // Декартова система координат
                currentXYZ.setX(currentXYZ.getX() - 1);
                //=========================================
                break;
            }
            case 2: {
                //=========================================
                // Классическая сферическая система координат
                currentXYZ.setXYZ(minusOneToFiSphere());
                //=========================================
                break;
            }
            case 3: {
                //========================================================
                // Сферическая система координат с улучшением
                if (fi >= - Math.PI * 5 / 6 && fi <= - Math.PI / 6)
                    currentXYZ.setXYZ(minusOneToFiSphere());
                else
                    if (fi <= Math.PI * 5 / 6 && fi >= Math.PI / 6)
                        currentXYZ.setXYZ(plusOneToFiSphere());
                    else
                        if (currentXYZ.getX() > 0)
                            currentXYZ.setXYZ(minusOneToRadiusSphere());
                        else
                            currentXYZ.setXYZ(plusOneToRadiusSphere());
                //========================================================
                break;
            }
            case 4: {


                break;
            }
            default: {
                break;
            }
        }





        //displayXYZ();
        //displayRTF();
        //displayMotion("minusOneToX");






//        if (fi >= - Math.PI * 5 / 8 && fi <= - Math.PI / 30)
//            currentXYZ.setXYZ(minusOneToFiSphere());
//        else
//            if (fi <= Math.PI * 5 / 8 && fi >= Math.PI / 30)
//                currentXYZ.setXYZ(plusOneToFiSphere());
//            else
//                if (currentXYZ.getX() > 0)
//                    currentXYZ.setXYZ(minusOneToRadiusSphere());
//                else
//                    currentXYZ.setXYZ(plusOneToRadiusSphere());

    }

    public void minusOneToY() {

        switch (CoordinateSystemFlag) {
            case 1: {
                //=========================================
                // Декартова система координат
                currentXYZ.setY(currentXYZ.getY() - 1);
                //=========================================
                break;
            }
            case 2: {
                //=========================================
                // Классическая сферическая система координат
                currentXYZ.setXYZ(minusOneToTetaSphere());
                //=========================================
                break;
            }
            case 3: {
                //========================================================
                // Сферическая система координат с улучшением
                if (fi >= - Math.PI / 3 && fi <= 0)
                    currentXYZ.setXYZ(minusOneToFiSphere());
                else
                    if (fi <= - Math.PI * 2 / 3 && fi >= - Math.PI)
                        currentXYZ.setXYZ(plusOneToFiSphere());
                    else
                        if (fi <= Math.PI / 3 && fi >= 0)
                            currentXYZ.setXYZ(minusOneToFiSphere());
                        else
                            if (fi >= Math.PI * 2 / 3 && fi <= Math.PI)
                                currentXYZ.setXYZ(plusOneToFiSphere());
                            else
                                if (currentXYZ.getY() > 0)
                                    currentXYZ.setXYZ(minusOneToRadiusSphere());
                                else
                                    currentXYZ.setXYZ(plusOneToRadiusSphere());
                //========================================================
                break;
            }
            case 4: {


                break;
            }
            default: {
                break;
            }
        }




        //displayXYZ();
        //displayRTF();
        //displayMotion("minusOneToY");

//        if (currentXYZ.getZ() == -1 || currentXYZ.getZ() == 1 || currentXYZ.getZ() == 0)
//        {
//            currentXYZ.setXYZ(minusOneToRadiusSphere());
//        }
//        else {
//            if (currentXYZ.getZ() > 0)
//                currentXYZ.setXYZ(minusOneToTetaSphere());
//            else
//                currentXYZ.setXYZ(plusOneToTetaSphere());
//        }



    }

    public void minusOneToZ() {

        switch (CoordinateSystemFlag) {
            case 1: {
                //=========================================
                // Декартова система координат
                currentXYZ.setZ(currentXYZ.getZ() - 1);
                //=========================================
                break;
            }
            case 2: {
                //=========================================
                // Классическая сферическая система координат
                currentXYZ.setXYZ(minusOneToRadiusSphere());
                //=========================================
                break;
            }
            case 3: {
                //========================================================
                // Сферическая система координат с улучшением
                if(teta < Math.PI * 5 / 6)
                    currentXYZ.setXYZ(plusOneToTetaSphere());
                else
                    currentXYZ.setXYZ(minusOneToRadiusSphere());
                //========================================================
                break;
            }
            case 4: {


                break;
            }
            default: {
                break;
            }
        }





        //displayXYZ();
        //displayRTF();
        //displayMotion("minusOneToZ");
        //currentXYZ.setXYZ(plusOneToTetaSphere());

        // если 3/4 PI < fi < 1/4 PI    -    radius--
        // иначе если fi > 3/4 PI       -    fi++
        //       иначе                  -    fi--

        //System.out.println("r = " + radius + " t = " + teta + " f = " + fi);

        //if (fi <  (double) 3 / 4 * Math.PI && fi >  (double) 1 / 4 * Math.PI) {






//        if (teta <= Math.PI * 2 / 10 && teta >= 0)
//            currentXYZ.setXYZ(plusOneToTetaSphere());
//        else
//            if(teta >= Math.PI * 4 / 7 && teta <= Math.PI)
//                currentXYZ.setXYZ(plusOneToTetaSphere());



//        }
//        else {
//            if (fi >= (double) 3 / 4 * Math.PI) {
//                currentXYZ.setXYZ(plusOneToFiSphere());
//            }
//            else
//                currentXYZ.setXYZ(minusOneToFiSphere());
//        }

    }


    public XYZ plusOneToTetaSphere() {
//        double radius, teta, fi;
//
//        radius = getRadiusForCurrent();
//        teta = getTetaForCurrent();
//        fi = getFiForCurrent();

        teta += oneDegreeStep;

        //displayMotion("plusOneToTetaSphere");
        //System.out.println("r = " + radius + " t = " + teta + " f = " + fi);

        return countCurrentXYZ(radius, teta, fi);
    }

    public XYZ plusOneToFiSphere() {
//        double radius, teta, fi;
//
//        radius = getRadiusForCurrent();
//        teta = getTetaForCurrent();
//        fi = getFiForCurrent();

        fi += oneDegreeStep;

        //displayMotion("plusOneToFiSphere");
        //System.out.println("r = " + radius + " t = " + teta + " f = " + fi);

        return countCurrentXYZ(radius, teta, fi);
    }

    public XYZ plusOneToRadiusSphere() {
//        double radius, teta, fi;
//
//        radius = getRadiusForCurrent();
//        teta = getTetaForCurrent();
//        fi = getFiForCurrent();

        radius += oneRadiusStep;

        //displayMotion("plusOneToRadiusSphere");
        return countCurrentXYZ(radius, teta, fi);
    }

    public XYZ minusOneToTetaSphere() {
//        double radius, teta, fi;
//
//        radius = getRadiusForCurrent();
//        teta = getTetaForCurrent();
//        fi = getFiForCurrent();

        teta -= oneDegreeStep;

        //displayMotion("minusOneToTetaSphere");
        //System.out.println("r = " + radius + " t = " + teta + " f = " + fi);

        return countCurrentXYZ(radius, teta, fi);
    }

    public XYZ minusOneToFiSphere() {
//        double radius, teta, fi;
//
//        radius = getRadiusForCurrent();
//        teta = getTetaForCurrent();
//        fi = getFiForCurrent();

        fi -= oneDegreeStep;

        //displayMotion("minusOneToFiSphere");
        //System.out.println("r = " + radius + " t = " + teta + " f = " + fi);

        return countCurrentXYZ(radius, teta, fi);
    }

    public XYZ minusOneToRadiusSphere() {
//        double radius, teta, fi;
//
//        radius = getRadiusForCurrent();
//        teta = getTetaForCurrent();
//        fi = getFiForCurrent();

        radius -= oneRadiusStep;

        //displayMotion("minusOneToRadiusSphere");
        return countCurrentXYZ(radius, teta, fi);
    }

    public void countRadiusTetaFi() {
        this.radius = getRadiusForCurrent();
        this.teta = getTetaForCurrent();
        this.fi = getFiForCurrent();
    }

    /**
     *
     * @return
     */
    public double getRadiusForCurrent() {
        double radius = Math.sqrt(currentXYZ.getX() * currentXYZ.getX() +
                currentXYZ.getY() * currentXYZ.getY() +
                currentXYZ.getZ() * currentXYZ.getZ());
        if (Double.isNaN(radius) || radius == 0)
            radius = 0.1;
        return radius;
    }

    /**
     *
     * @return
     */
    public double getTetaForCurrent() {
        double teta = Math.acos(currentXYZ.getZ() / getRadiusForCurrent());
        if (Double.isNaN(teta) || teta == 0)
            teta = 0.00001;
        return teta;
    }

    /**
     *
     * @return
     */
    public double getFiForCurrent() {
        double fi = Math.atan2(currentXYZ.getY(), currentXYZ.getX());
        if (Double.isNaN(fi) || fi == 0)
            fi = 0.00001;
        return fi;
    }

    /**
     *
     * @param radius
     * @param teta
     * @param fi
     * @return
     */
    public XYZ countCurrentXYZ(double radius, double teta, double fi) {
        currentXYZ.setXYZ((radius * Math.sin(teta) * Math.cos(fi)),
                (radius * Math.sin(teta) * Math.sin(fi)),
                (radius * Math.cos(teta)));

        return currentXYZ;
        //return new XYZ((radius * Math.sin(teta) * Math.cos(fi)),
        //               (radius * Math.sin(teta) * Math.sin(fi)),
        //               (radius * Math.cos(teta)));
    }

    /*
    public double[] findRTFForX(int x, double radius, double teta, double fi) {

        double startRadius = radius - 3;
        double startTeta = teta - oneDegreeStep * 3;
        double startFi = fi - oneDegreeStep * 3;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                for (int k = 0; k < 6; k++) {
                    int formula = (int) Math.ceil(startRadius * Math.sin(startTeta) * Math.cos(startFi));

                    if (x - 1 == formula || x == formula || x + 1 == formula)
                        return new double[] {startRadius, startTeta, startFi};

                    startFi += oneDegreeStep;
                }
                startTeta += oneDegreeStep;
            }
            startRadius += 1;
        }

        return new double[] {radius, teta, fi};
    }

    public double[] findRTFForY(int y, double radius, double teta, double fi) {

        double startRadius = radius - 3;
        double startTeta = teta - oneDegreeStep * 3;
        double startFi = fi - oneDegreeStep * 3;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                for (int k = 0; k < 6; k++) {
                    int formula = (int) Math.ceil(startRadius * Math.sin(startTeta) * Math.sin(startFi));

                    if (y - 1 == formula || y == formula || y + 1 == formula)
                        return new double[] {startRadius, startTeta, startFi};

                    startFi += oneDegreeStep;
                }
                startTeta += oneDegreeStep;
            }
            startRadius += 1;
        }

        return new double[] {radius, teta, fi};
    }

    public double[] findRTFForZ(int z, double radius, double teta, double fi) {

        double startRadius = radius - 3;
        double startTeta = teta - oneDegreeStep * 3;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                    int formula = (int) Math.ceil(startRadius * Math.cos(startTeta));

                    if (z - 1 == formula || z == formula || z + 1 == formula)
                        return new double[] {startRadius, startTeta, fi};

                startTeta += oneDegreeStep;
            }
            startRadius += 1;
        }

        return new double[] {radius, teta, fi};
    }
    */

    public String stringXYZ() {
        return ("X = " + currentXYZ.getX() +
                ", Y = " + currentXYZ.getY() +
                ", Z = " + currentXYZ.getZ());
    }

    public void displayXYZ() {
        System.out.println("X = " + currentXYZ.getX() +
                ", Y = " + currentXYZ.getY() +
                ", Z = " + currentXYZ.getZ());
    }

    public void displayRTF() {
        System.out.println ("Radius = " + radius +
                ", Teta = " + teta +
                ", Fi = " + fi);
    }

    public String stringRTF() {
        return ("Radius = " + radius +
                ", Teta = " + teta +
                ", Fi = " + fi);
    }

    /**
     * Клонировать объект
     * @return - клонированный объект
     */
    public Position clone() {
        Position newPosition = new Position(minXYZ.getX(), minXYZ.getY(), minXYZ.getZ(),
                maxXYZ.getX(), maxXYZ.getY(), maxXYZ.getZ(), modelRobot);
        newPosition.setCurrentXYZ(currentXYZ.getX(), currentXYZ.getY(), currentXYZ.getZ());
        newPosition.setRadius(radius);
        newPosition.setTeta(teta);
        newPosition.setFi(fi);
        return newPosition;
    }

    /**
     * Проверить текущий X на близость к желаемому X
     * @param other - другая координата
     * @param radius - радиус
     * @return - результат
     */
    public boolean countXIsEqual(Position other, int radius) {
        boolean res = false;

        if ((currentXYZ.getX() >= other.getX() - radius) && (currentXYZ.getX() <= other.getX() + radius))
            res = true;

        return res;
    }

    /**
     * Проверить текущий Y на близость к желаемому Y
     * @param other - другая координата
     * @param radius - радиус
     * @return - результат
     */
    public boolean countYIsEqual(Position other, int radius) {
        boolean res = false;

        if ((currentXYZ.getY() >= other.getY() - radius) && (currentXYZ.getY() <= other.getY() + radius))
            res = true;

        return res;
    }

    /**
     * Проверить текущий Z на близость к желаемому Z
     * @param other - другая координата
     * @param radius - радиус
     * @return - результат
     */
    public boolean countZIsEqual(Position other, int radius) {
        boolean res = false;

        if ((currentXYZ.getZ() >= other.getZ() - radius) && (currentXYZ.getZ() <= other.getZ() + radius))
            res = true;

        return res;
    }

    /**
     * Проверка, что текущая координата рядом с желаемой на каком-то радиусе
     * @param other - другая координата
     * @param radius - радиус
     * @return - результат проверки
     */
    public boolean isNear(Position other, int radius) {
        boolean result = false;

        if (((currentXYZ.getX() >= other.getX() - radius) && (currentXYZ.getX() <= other.getX() + radius)) &&
            ((currentXYZ.getY() >= other.getY() - radius) && (currentXYZ.getY() <= other.getY() + radius)) &&
            ((currentXYZ.getZ() >= other.getZ() - radius) && (currentXYZ.getZ() <= other.getZ() + radius)))
            result = true;

        return result;
    }

    /**
     * Установить текущую координату в середину
     */
    public void setCurrentMiddle() {
        currentXYZ.setX((maxXYZ.getX() - minXYZ.getX()) / 2);
        currentXYZ.setY((maxXYZ.getY() - minXYZ.getY()) / 2);
        currentXYZ.setZ((maxXYZ.getZ() - minXYZ.getZ()) / 2);
    }

    /**
     * Получить модель робота
     * @return - модель
     */
    public ModelRobot getModelRobot() {
        return modelRobot;
    }

    /**
     * Установить модель робота
     * @param modelRobot - модель робота
     */
    public void setModelRobot(ModelRobot modelRobot) {
        this.modelRobot = modelRobot;
    }


    // Получить текущие координаты по камере
    public boolean getXYZOnCamera(ColorParams color, int indexCamera) {
        boolean result = false;

        System.out.println("\n\nGet XYZ on camera. Please wait...");

        double viewXYZ[] = Mark.getXYZ(5, color, indexCamera);

        if (viewXYZ[0] != 0 && viewXYZ[1] != 0 && viewXYZ[2] != 0)
        {
            currentXYZ.setX(viewXYZ[0]);
            currentXYZ.setY(viewXYZ[1]);
            currentXYZ.setZ(viewXYZ[2]);
            countRadiusTetaFi();
            System.out.println(currentXYZ);
            result = true;
        }

        return result;
    }

    // Получить текущие координаты по камере
    public boolean getCurrentXYZOnCamera() {
        boolean result = false;
        ColorParams orange = new ColorParams(false, 225, 99, 59, 50);

        System.out.println("\n\nGet XYZ on camera. Please wait...");

        double viewXYZ[] = Mark.getXYZ(5, orange, 0);

        if (viewXYZ[0] != 0 && viewXYZ[1] != 0 && viewXYZ[2] != 0)
        {
            currentXYZ.setX(viewXYZ[0]);
            currentXYZ.setY(viewXYZ[1]);
            currentXYZ.setZ(viewXYZ[2]);
            countRadiusTetaFi();
            System.out.println(currentXYZ);
            result = true;
        }

        return result;
    }

    // Получить желаемые координаты по камере
    public boolean getTargetXYZOnCamera() {
        boolean result = false;
        ColorParams yellow2 = new ColorParams(false, 150, 150, 20, 41);

        System.out.println("\n\nGet XYZ on camera. Please wait...");

        double viewXYZ[] = Mark.getXYZ(5, yellow2, 0);

        if (viewXYZ[0] != 0 && viewXYZ[1] != 0 && viewXYZ[2] != 0)
        {
            currentXYZ.setX(viewXYZ[0]);
            currentXYZ.setY(viewXYZ[1]);
            currentXYZ.setZ(viewXYZ[2]);
            countRadiusTetaFi();
            System.out.println(currentXYZ);
            result = true;
        }

        return result;
    }

    /**
     * Установить тестовую текущую позицию
     */
    public void testCurrentPosition()
    {
        currentXYZ.setX(-294);
        currentXYZ.setY(105);
        currentXYZ.setZ(593);
        countRadiusTetaFi();
    }

    /**
     * Установить тестовую желаемую позицию
     */
    public void testTargetPosition()
    {
        currentXYZ.setX(-257);
        currentXYZ.setY(134);
        currentXYZ.setZ(517);
        countRadiusTetaFi();
    }

    /**
     * Применяем новую позицию
     *
     * @param direction         - направление
     */
    public void useNextPosition(Direction direction) {

        if (modelRobot != null) {
            if (direction == Direction.UP) {
                modelRobot.forwardY();
                modelRobot.backZ();
            }

            if (direction == Direction.RIGHT) {
                modelRobot.backX();
            }

            if (direction == Direction.DOWN) {
                modelRobot.backY();
                modelRobot.forwardZ();
            }

            if (direction == Direction.DEEP) {
                modelRobot.forwardZ();
                //modelRobot.forwardY();
            }

            if (direction == Direction.OUTSIDE) {
                modelRobot.backZ();
                //modelRobot.backY();
            }

            if (direction == Direction.LEFT) {
                modelRobot.forwardX();
            }
        }
    }

    public static void main(String[] args) {
        Position position = new Position(-5, -5, -5, 15, 15 ,15);
        Position position2 = new Position(-5, -5, -5, 15, 15 ,15);
        position.generateNewXYZ();
        position.setCurrentXYZ(2, 5, 6);
        position2.generateNewXYZ();
        position2.setCurrentXYZ(4, 4, 4);
        position.countRadiusTetaFi();
        position2.countRadiusTetaFi();

        System.out.println(position.isNear(position2, 1));
        System.out.println(position.countXIsEqual(position2, 1));
        System.out.println(position.countYIsEqual(position2, 1));
        System.out.println(position.countZIsEqual(position2, 1));


        System.out.println("New values" + position);
        System.out.println(position.stringRTF());
        for (int i = 0; i < 3; i++) {
            position.plusOneToX();
            System.out.println("Plus X: " + position);
        }

        for (int i = 0; i < 3; i++) {
            position.minusOneToX();
            System.out.println("Minus X: " + position);
        }

        for (int i = 0; i < 6; i++) {
            position.plusOneToY();
            System.out.println("Plus Y: " + position);
        }

        for (int i = 0; i < 6; i++) {
            position.minusOneToY();
            System.out.println("Minus Y: " + position);
        }

        for (int i = 0; i < 10; i++) {
            position.plusOneToZ();
            System.out.println("Plus Z: " + position);
        }

        for (int i = 0; i < 10; i++) {
            position.minusOneToZ();
            System.out.println("Minus Z: " + position);
        }

    }
}
