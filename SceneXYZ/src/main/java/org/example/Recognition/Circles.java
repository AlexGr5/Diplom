package org.example.Recognition;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

// ����� ��� ������ ����������� �� �����������
// ���� - ������ �����������
// ������ - ����� ���������� �� �����������, ���������� ���������� ������ �� �����������
public class Circles {

    /**
     * ������ �����������
     */
    private List<Circle> ListCircles = new ArrayList<Circle>();

    /**
     * �������� ������ �����������
     * @return - ������
     */
    public List<Circle> getListCircles() {
        return ListCircles;
    }

    // ����� ���� ����������� �� �����������
    // ��������� - �����-����� �����������, ��������� ������������� �� ������������ ����������
    public boolean FindCircles(Mat ImgGray, RecognitionParameters parameters)
    {
        boolean Res = false;

        Mat workImg = ImgGray;

        if (workImg.empty())
            return false;

        Mat circles = new Mat();

        //==============================================================================================================
        // ������� ����� �� ����������� � ��������� ������ � ������� �������������� ����. ������� ������� ����� ��
        // ����������� � ��������� ������, ��������� ����������� �������������� ����. ������: :
        // INCLUDE: snippets/imgproc_HoughLinesCircles.cpp
        // ����������: ������ ������� ������ ���������� ������ ������. ������ �� ����� �� ����� ���������� �������.
        // �� ������ ������ �������, ������ �������� ������� ( minRadius � maxRadius ), ���� �� ��� ������.
        // ���, � ������ ������ #HOUGH_GRADIENT, �� ������ ���������� maxRadius � ������������� �����, ����� ����������
        // ������ ������ ��� ������ ������� � ����� ���������� ������ � ������� �������������� ���������. ��� �����
        // �������� ������� �������� �����������, ���� ��� ��� �� ������. ��������, ����� ������ GaussianBlur() � �����
        // 7x7 � ������ 1,5x1,5 ��� �������� ���������.
        //
        // ���������:
        //     image � 8-������ ������������� ������� ����������� � ��������� ������.
        //     ����� - �������� ������ ��������� ������. ������ ������ ���������� ��� 3-� ��� 4-� ����������
        //     ������ � ��������� ������� (x, y, ������).
        //
        // ��� (x,y,������,������)
        //     .
        //     method � ����� �����������, ��. #HoughModes. �������� ��������� ������:
        //     #HOUGH_GRADIENT � #HOUGH_GRADIENT_ALT.
        //
        //     dp - �������� ��������� ���������� ������������ � ���������� �����������.
        //     ��������, ���� dp=1, ����������� ����� �� �� ����������, ��� � ������� �����������. ���� dp=2,
        //     ����������� ����� �������� ������ � ������. ��� #HOUGH_GRADIENT_ALT ������������� �������� dp=1,5,
        //     ���� ������ �� ����� ������������ ����� ��������� �����.
        //
        //     minDist - ����������� ���������� ����� �������� ������������ ������. ���� �������� �������
        //     ���, � ���������� � ��������� ����� ���� ����� ���������� ��������� �������� ������.
        //     ���� �� ������� �������, ��������� ����� ����� ���� ���������.
        //
        //     param1 - ������ ��������, ����������� ��� ������. � ������ #HOUGH_GRADIENT � #HOUGH_GRADIENT_ALT
        //     ��� ����� ������� ����� �� ����, ���������� ��������� ����� ����� (������ � ��� ���� ������).
        //     �������� ��������, ��� #HOUGH_GRADIENT_ALT ���������� �������� #Scharr ��� ���������� �����������
        //     �����������, ������� ��������� �������� ������ ������ ���� ����, �������� 300 ��� ���������
        //     ��������������� � ����������� �����������.
        //
        //     param2 - ������ ��������, ����������� ��� ������. � ������ #HOUGH_GRADIENT ��� ����� ���������� ���
        //     ������� ������ �� ����� �����������. ��� �� ������, ��� ������ ������ ������ ����� ���� ����������.
        //     ������, ��������������� ������� ��������� ������������, ����� ���������� �������. � ������ ���������
        //     #HOUGH_GRADIENT_ALT ��� ���� "������������" �����. ��� �� ����� � 1, ��� ����� �������� �������� ������.
        //     � ����������� ������� 0,9 ������ ���� ����������. ���� �� ������ �������� ����������� ��������� ������,
        //     �� ������ ��������� ��� �� 0,85, 0,8 ��� ���� ������. �� ����� ������������ ���������� �������� ������
        //     [minRadius, maxRadius], ����� �������� ��������� ������ ������.
        //
        //     minRadius - ����������� ������ �����.
        //
        //     maxRadius - ������������ ������ ����������. ���� <= 0, ������������ ������������ ������ �����������.
        //     ���� < 0, #HOUGH_GRADIENT ���������� ������ ��� ���������� �������. #HOUGH_GRADIENT_ALT ������ ���������
        //     ������� ������. ����������: fitEllipse, minEnclosingCircle
        //
        Imgproc.HoughCircles(workImg, circles, Imgproc.HOUGH_GRADIENT, parameters.getDp(), workImg.rows() / parameters.getDenominatorOfMinDist(),
                parameters.getParam1(), parameters.getParam2(), parameters.getMinRadius(), parameters.getMaxRadius());
        //==============================================================================================================


        for (int i = 0, r = circles.rows(); i < r; i++) {
            for (int j = 0, c = circles.cols(); j < c; j++) {
                double[] circle = circles.get(i, j);

                ListCircles.add(new Circle (circle[0], circle[1], circle[2]));

            }
        }

        if (ListCircles.size() > 0)
            Res = true;

        return Res;
    }

    // ��������� ��������� ������ ��������� �����������
    // ��������� - ������������ �����������
    public boolean DrawCircles(Mat photo)
    {
        boolean Res = false;

        Mat workImg = photo;

        if (workImg.empty())
            return false;

        Scalar purple = new Scalar(255, 0, 0);

        if (ListCircles.size() > 0) {
            for (int i = 0; i < ListCircles.size(); i++) {

                Circle temp = new Circle(ListCircles.get(i));

                double[] rect = {temp.getX() - temp.getRadius(), temp.getY() - temp.getRadius(), temp.getX() + temp.getRadius(), temp.getY() + temp.getRadius()};

                Imgproc.rectangle(workImg, new Point(rect[0], rect[1]),
                        new Point(rect[2], rect[3]), purple, 5);
            }
            Res = true;
        }

        return Res;
    }

    // ����� ����������� �� ������� �������� ������� �����
    public boolean findCirclesForMaxMinPixels(Mat im2)
    {
        boolean res = false;
        int i, j, x1, x2, y1, y2, x, y, RadiusX, RadiusY;
        double[] c1 = new double[3];
        // ���������� ��������������, ��������������� ��������� ����
        x1 = im2.cols() + 1;
        x2 = -1;
        y1 = im2.rows() + 1;
        y2 = -1;
        // ���� ��� � ���� ���������� ����� �����
        for (i = 0; i < im2.cols(); i++)
            for (j = 0; j < im2.rows(); j++)
            {
                c1 = im2.get(j, i);
                if (c1[0] == 255) // ����� �����
                {
                    if (i < x1) { x1 = i; }
                    if (i > x2) { x2 = i; }
                    if (j < y1) { y1 = j; }
                    if (j > y2) { y2 = j; }
                }
            }

        // ���� �����
        if ((x1 < im2.cols()) && (x2 > 0) && (y1 < im2.rows()) && (y2 > 0)) {
            x = (x1 + x2) / 2;
            y = (y1 + y2) / 2;
            RadiusX = (x2 - x1) / 2;
            RadiusY = (y2 - y1) / 2;
            ListCircles.add(new Circle (x, y, RadiusX));
            res = true;
        }

        return res;
    }

    // ����� ��� ������ � ������� �� ��� (���������� ������, ���������� ����)
    // ��������� - (�����-����� �����������), (�����������, �� ������� �������� ������� ��������), (��������� ������������� �����������)
    public boolean FindAndDrawCircles(Mat WightBlackImg, Mat DrawImg, RecognitionParameters parameters)
    {
        boolean Res = false;

        if (WightBlackImg.empty() || DrawImg.empty())
            return false;

        //if (FindCircles(WightBlackImg, parameters))
        if (findCirclesForMaxMinPixels(WightBlackImg))
        {

//            bubbleSort();
//            deleteAloneCircles(120);
//            filterCircles(120);

            //countAverageRadiusCircles(150);
            //selectMaxRadius();

            if (DrawCircles(DrawImg))
            {
                Res = true;
            }
        }

        return Res;
    }

    /**
     * ������ �����������
     * @param radius - ������ �����������
     */
    public void filterCircles(int radius)
    {
        if (ListCircles.size() > 0)
        {
            List<Circle> newListCircles = new ArrayList<Circle>();

            for (int i = 0; i < ListCircles.size(); i++)
            {
                if (!newListCircles.contains(ListCircles.get(i)))
                {
                    if (newListCircles.size() == 0)
                        newListCircles.add(ListCircles.get(i));
                    else
                    {
                        boolean flagNear = false;

                        for (int j = 0; j < newListCircles.size(); j++)
                        {
                            if (ListCircles.get(i).isNear(newListCircles.get(j), radius))
                            {
                                flagNear = true;
                            }
                        }

                        if (flagNear == false)
                        {
                            newListCircles.add(ListCircles.get(i));
                        }
                    }
                }
            }
            ListCircles.clear();
            ListCircles = newListCircles;
        }
    }

    /**
     * ���������� ���������
     */
    public void bubbleSort(){
        if (ListCircles.size() > 0) {
            for (int i = 0; i < ListCircles.size() - 1; i++) {
                for (int j = 0; j < ListCircles.size() - i - 1; j++) {
                    if (ListCircles.get(j + 1).getRadius() > ListCircles.get(j).getRadius()) {
                        Circle swap = ListCircles.get(j);
                        ListCircles.set(j, ListCircles.get(j + 1));
                        ListCircles.set(j + 1, swap);
                    }
                }
            }
        }
    }

    /**
     * ������� ������������ ������
     */
    public void selectMaxRadius()
    {
        if (ListCircles.size() > 0) {
            Circle max = ListCircles.get(0);
            for (int i = 0; i < ListCircles.size(); i++)
            {
                if (max.getRadius() < ListCircles.get(i).getRadius())
                    max = ListCircles.get(i);
            }
            ListCircles.clear();
            ListCircles.add(max);
        }
    }

    /**
     * ��������� � ������� ������� ������
     * @param radius - ������
     */
    public void countAverageRadiusCircles(int radius)
    {
        if (ListCircles.size() > 0)
        {
            List<Circle> newListCircles = new ArrayList<Circle>();

            for (int i = 0; i < ListCircles.size(); i++)
            {
                if (!newListCircles.contains(ListCircles.get(i)))
                {
                    if (newListCircles.size() == 0)
                        newListCircles.add(ListCircles.get(i));
                    else
                    {
                        boolean flagNear = false;

                        for (int j = 0; j < newListCircles.size(); j++)
                        {
                            if (ListCircles.get(i).isNear(newListCircles.get(j), radius))
                            {
                                Circle average = new Circle();
                                flagNear = true;
                                average.setX((ListCircles.get(i).getX() + newListCircles.get(j).getX()) / 2);
                                average.setY((ListCircles.get(i).getY() + newListCircles.get(j).getY()) / 2);
                                average.setRadius((ListCircles.get(i).getRadius() + newListCircles.get(j).getRadius()) / 2);

                                newListCircles.get(j).SetCircle(average);
                            }
                        }

                        if (flagNear == false)
                        {
                            newListCircles.add(ListCircles.get(i));
                        }
                    }
                }
            }

            ListCircles = newListCircles;
        }
    }

    /**
     * ������� �������� ����������
     * @param radius - ������
     */
    public void deleteAloneCircles(int radius)
    {
        if (ListCircles.size() > 0)
        {
            for (int i = 0; i < ListCircles.size(); i++)
            {
                boolean flagAlone = false;
                for (int j = i; j < ListCircles.size(); j++)
                {
                    if (ListCircles.get(i).isNear(ListCircles.get(j), radius) && !ListCircles.get(i).isZero())
                    {
                        flagAlone = true;
                    }
                }
                if (flagAlone == false)
                {
                    ListCircles.set(i, new Circle(0,0,0));
                    //ListCircles.remove(i);
                }
            }

            bubbleSort();
            for (int i = ListCircles.size() - 1; i >= 0; i--)
            {
                if (ListCircles.get(i).isZero())
                    ListCircles.remove(i);
            }
        }
    }
}
