package org.example.Recognition;

// ����� ��������� ������������� �����������
// ���� - ���������� ���������, ����������� ���������� ����� �������� �����������, ��������1, ��������2,
// ����������� � ������������ �������.
// ������ - ������� � ��������� �����
public class RecognitionParameters {

    private double dp;

    private double denominatorOfMinDist;

    private double param1;

    private double param2;

    private int minRadius;

    private int maxRadius;

    public RecognitionParameters()
    {
        ;
    }

    /**
     *
     * @param DP    dp - �������� ��������� ���������� ������������ � ���������� �����������.
     *              ��������, ���� dp=1, ����������� ����� �� �� ����������, ��� � ������� �����������. ���� dp=2,
     *              ����������� ����� �������� ������ � ������. ��� #HOUGH_GRADIENT_ALT ������������� �������� dp=1,5,
     *              ���� ������ �� ����� ������������ ����� ��������� �����.
     * @param DenominatorOfMinDist      minDist - ����������� ���������� ����� �������� ������������ ������. ���� �������� �������
     *                                  ���, � ���������� � ��������� ����� ���� ����� ���������� ��������� �������� ������.
     *                                  ���� �� ������� �������, ��������� ����� ����� ���� ���������.
     * @param Param1    param1 - ������ ��������, ����������� ��� ������. � ������ #HOUGH_GRADIENT � #HOUGH_GRADIENT_ALT
     *                  ��� ����� ������� ����� �� ����, ���������� ��������� ����� ����� (������ � ��� ���� ������).
     *                  �������� ��������, ��� #HOUGH_GRADIENT_ALT ���������� �������� #Scharr ��� ���������� �����������
     *                  �����������, ������� ��������� �������� ������ ������ ���� ����, �������� 300 ��� ���������
     *                  ��������������� � ����������� �����������.
     * @param Param2    param2 - ������ ��������, ����������� ��� ������. � ������ #HOUGH_GRADIENT ��� ����� ���������� ���
     *                  ������� ������ �� ����� �����������. ��� �� ������, ��� ������ ������ ������ ����� ���� ����������.
     *                  ������, ��������������� ������� ��������� ������������, ����� ���������� �������. � ������ ���������
     *                  #HOUGH_GRADIENT_ALT ��� ���� "������������" �����. ��� �� ����� � 1, ��� ����� �������� �������� ������.
     *                  � ����������� ������� 0,9 ������ ���� ����������. ���� �� ������ �������� ����������� ��������� ������,
     *                  �� ������ ��������� ��� �� 0,85, 0,8 ��� ���� ������. �� ����� ������������ ���������� �������� ������
     *                  [minRadius, maxRadius], ����� �������� ��������� ������ ������.
     * @param MinRadius     minRadius - ����������� ������ �����.
     * @param MaxRadius     maxRadius - ������������ ������ ����������. ���� ������ ���� ����� 0, ������������ ������������ ������ �����������.
     *                      ���� ������ 0, #HOUGH_GRADIENT ���������� ������ ��� ���������� �������. #HOUGH_GRADIENT_ALT ������ ���������
     *                      ������� ������. ����������: fitEllipse, minEnclosingCircle
     */
    public RecognitionParameters(double DP, double DenominatorOfMinDist, double Param1, double Param2, int MinRadius, int MaxRadius)
    {
        setDp(DP);
        setDenominatorOfMinDist(DenominatorOfMinDist);
        setParam1(Param1);
        setParam2(Param2);
        setMinRadius(MinRadius);
        setMaxRadius(MaxRadius);
    }

    public void SetRecognitionParameters(double DP, double DenominatorOfMinDist, double Param1, double Param2, int MinRadius, int MaxRadius)
    {
        setDp(DP);
        setDenominatorOfMinDist(DenominatorOfMinDist);
        setParam1(Param1);
        setParam2(Param2);
        setMinRadius(MinRadius);
        setMaxRadius(MaxRadius);
    }


    public double getDp() {
        return dp;
    }

    public void setDp(double dp) {
        this.dp = dp;
    }

    public double getDenominatorOfMinDist() {
        return denominatorOfMinDist;
    }

    public void setDenominatorOfMinDist(double denominatorOfMinDist) {
        this.denominatorOfMinDist = denominatorOfMinDist;
    }

    public double getParam1() {
        return param1;
    }

    public void setParam1(double param1) {
        this.param1 = param1;
    }

    public double getParam2() {
        return param2;
    }

    public void setParam2(double param2) {
        this.param2 = param2;
    }

    public int getMinRadius() {
        return minRadius;
    }

    public void setMinRadius(int minRadius) {
        this.minRadius = minRadius;
    }

    public int getMaxRadius() {
        return maxRadius;
    }

    public void setMaxRadius(int maxRadius) {
        this.maxRadius = maxRadius;
    }
}
