package org.example.LowLevel;

/**
 * ����� ������
 * ������ ����, ������� ����������� ���
 * � ����� ���� ���������� ������ ������ (������������)
 * ����������� � ������������ �������� �������� � �������������
 * ������������ �������� � ��������
 * � ������� ��������� � �������������
 */
public class Motor {

    /**
     * ����������� ����
     */
    protected int port;

    /**
     * ����������� �������� ����������� ��� ���������� ���������
     */
    protected int minMicroseconds;

    /**
     * ������������ �������� ����������� ��� ���������� ���������
     */
    protected int maxMicroseconds;

    /**
     * ������������ ������ �������� ��������� � ��������
     */
    protected int maxDegree;

    /**
     * ����������� ������ �������� ��������� � ��������
     */
    protected int minDegree;

    /**
     * ������� ��������� ������
     */
    protected int currentMicroseconds;

    /**
     * ���� ��� ���������� ������� �������
     */
    protected Relay motorRelay;

    /**
     * ����������� ��� ���������� ��� ����������, ��������� �� ����
     * @param motorRelay - ����
     */
    //public LowLevel.Motor(LowLevel.Relay motorRelay) {
    //    this.motorRelay = motorRelay;
    //}

    /**
     * ����������� ��� ���������� � ����������� ������ ����� ����
     * @param port                  - ���� ���������� ����������
     * @param minMicroseconds       - ����������� �������� �����������
     * @param maxMicroseconds       - ������������ �������� �����������
     * @param motorRelay            - ����
     * @param maxDegree             - ������������ ������ ��������
     * @param currentMicroseconds   - ������� ��������� � �������������
     */
    public Motor(int port, Relay motorRelay, int minMicroseconds, int maxMicroseconds, int minDegree, int maxDegree, int currentMicroseconds) {
        this.port = port;
        this.minMicroseconds = minMicroseconds;
        this.maxMicroseconds = maxMicroseconds;
        this.motorRelay = motorRelay;
        this.maxDegree = maxDegree;
        this.currentMicroseconds = currentMicroseconds;
        this.minDegree = minDegree;
    }

    /**
     * �������� ����� �����
     * @return  - ����� �����
     */
    public int getPort() {
        return port;
    }

    /**
     * ���������� ����� �����
     * @param port - ����� �����
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * �������� ����� ����
     * @return - ����� ����
     */
    public Relay getMotorRelay() {
        return motorRelay;
    }

    /**
     * ������ ����
     * @param motorRelay - ����
     */
    public void setMotorRelay(Relay motorRelay) {
        this.motorRelay = motorRelay;
    }

    /**
     * �������� ������� ��������� � �������������
     * @return - ������������
     */
    public int getCurrentMicroseconds() {
        return currentMicroseconds;
    }

    /**
     * ���������� ������� ��������� � �������������
     * @param currentMicroseconds - ������������
     */
    public void setCurrentMicroseconds(int currentMicroseconds) {
        this.currentMicroseconds = currentMicroseconds;
    }

    /**
     * �������� ��������� �����
     * @param degree - ������
     * @return  - ��������� �������� � �������������
     */
    public int leftRotation(int degree)
    {
        int resultMicroseconds = currentMicroseconds;

        if (currentMicroseconds > minMicroseconds) {
//            if (degree < minDegree)
//                degree = minDegree;
//            if (degree > maxDegree)
//                degree = maxDegree;

            int oneDegreeInMicroseconds = (maxMicroseconds - minMicroseconds) / (maxDegree - minDegree);

            //!!!!!!!!!!!!!!!!!!!!!!!!!!!
            System.out.println("Current ms = " + currentMicroseconds);
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!

            resultMicroseconds = currentMicroseconds - oneDegreeInMicroseconds * degree;
            if (resultMicroseconds < minMicroseconds)
                resultMicroseconds = minMicroseconds;

            //!!!!!!!!!!!!!!!!!!!!!!!!!!!
            System.out.println("New ms = " + resultMicroseconds);
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!

            currentMicroseconds = resultMicroseconds;
        }

        return resultMicroseconds;
    }

    /**
     * �������� ��������� ������
     * @param degree - ������
     * @return  - ��������� �������� � �������������
     */
    public int rightRotation(int degree)
    {
        int resultMicroseconds = currentMicroseconds;

        if (currentMicroseconds < maxMicroseconds) {
//            if (degree < minDegree)
//                degree = minDegree;
//            if (degree > maxDegree)
//                degree = maxDegree;

            int oneDegreeInMicroseconds = (maxMicroseconds - minMicroseconds) / (maxDegree - minDegree);

            //!!!!!!!!!!!!!!!!!!!!!!!!!!!
            System.out.println("Current ms = " + currentMicroseconds);
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!

            resultMicroseconds = currentMicroseconds + oneDegreeInMicroseconds * degree;
            if (resultMicroseconds > maxMicroseconds)
                resultMicroseconds = maxMicroseconds;
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!
            System.out.println("New ms = " + resultMicroseconds);
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!

            currentMicroseconds = resultMicroseconds;
        }
        return resultMicroseconds;
    }
}
