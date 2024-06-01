package org.example.LowLevel;

import arduino.Arduino;

import java.util.List;

/**
 * ���������� �������������� JAVA ��������� � Arduino
 * ��� ���������� �������������� � ����
 */
public class ArduinoMotorsImplComPort implements IArduinoMotors {

    /**
     * �������
     */
    Arduino arduino = null;     // new Arduino("COM6", 9600);

    /**
     * ���� ���������� � �������
     */
    boolean connected = false;  // arduino.openConnection();

    /**
     * ��������� �������� �� �������� ������ ���������
     * ��� ���������� ������ �� ����������������� �����
     */
    static int DELAY = 6000;

    /**
     * �����������, ���������� ��������� ��� ����N��
     * @param serialPort - ���������������� ���� (�������� "COM7")
     * @param speed      - �������� �������� ��������� (������ 9600)
     */
    public ArduinoMotorsImplComPort(String serialPort, int speed)
    {
        this.arduino = new Arduino(serialPort, speed);
        connected = arduino.openConnection();
        System.out.println("Connection established: " + connected);
    }

    /**
     * �����������, � ��� ��������� �������
     * @param arduino   - �������
     */
    public ArduinoMotorsImplComPort(Arduino arduino) {
        this.arduino = arduino;
        connected = arduino.openConnection();
        System.out.println("Connection established: " + connected);
    }

    /**
     * �������� ����� � ������ ���� �������
     * @param one   - ���� �����������
     */
    @Override
    public void addMotor(Motor one)
    {
        Motors.add(one);
        initializationServo();
    }

    /**
     * ��������� ����� �����
     * @param indexMotor    - ����� ���������
     * @param degree        - ������ ��������
     */
    @Override
    public void leftRotation(int indexMotor, int degree) {

        if (indexMotor >= 0 && indexMotor < Motors.size()) {
            arduino.serialWrite(String.valueOf(Motors.get(indexMotor).getMotorRelay().getPort()));
            arduino.serialWrite('#');
            arduino.serialWrite(String.valueOf(Motors.get(indexMotor).getPort()));
            arduino.serialWrite('#');
            arduino.serialWrite(String.valueOf(Motors.get(indexMotor).leftRotation(degree)));
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * ��������� ����� ������
     * @param indexMotor    - ����� ���������
     * @param degree        - ������ ��������
     */
    @Override
    public void rightRotation(int indexMotor, int degree) {
        if (indexMotor >= 0 && indexMotor < Motors.size()) {
            arduino.serialWrite(String.valueOf(Motors.get(indexMotor).getMotorRelay().getPort()));
            arduino.serialWrite('#');
            arduino.serialWrite(String.valueOf(Motors.get(indexMotor).getPort()));
            arduino.serialWrite('#');
            arduino.serialWrite(String.valueOf(Motors.get(indexMotor).rightRotation(degree)));
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * ��������� ��������� ���������� �����
     * @param indexesMotors - ������ ����������
     * @param degrees       - ������� �������� ��� ������� ��������� �� ������� ��������
     */
    @Override
    public void leftManyRotation(List<Integer> indexesMotors, List<Integer> degrees) {
        boolean success = false;
        for (int i = 0; i < indexesMotors.size(); i++)
        {
            if (indexesMotors.get(i) >= 0 && indexesMotors.get(i) < Motors.size() &&
                    indexesMotors.size() == degrees.size()) {
                arduino.serialWrite(String.valueOf(Motors.get(indexesMotors.get(i)).getMotorRelay().getPort()));
                arduino.serialWrite('#');
                arduino.serialWrite(String.valueOf(Motors.get(indexesMotors.get(i)).getPort()));
                arduino.serialWrite('#');
                arduino.serialWrite(String.valueOf(Motors.get(indexesMotors.get(i)).leftRotation(degrees.get(i))));
                success = true;

                if (i != indexesMotors.size() - 1)
                {
                    arduino.serialWrite('&');
                }
            }
        }
        if (success) {
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * ��������� ��������� ���������� ������
     * @param indexesMotors - ������ ����������
     * @param degrees       - ������� �������� ��� ������� ��������� �� ������� ��������
     */
    @Override
    public void rightManyRotation(List<Integer> indexesMotors, List<Integer> degrees) {
        boolean success = false;
        for (int i = 0; i < indexesMotors.size(); i++)
        {
            if (indexesMotors.get(i) >= 0 && indexesMotors.get(i) < Motors.size() &&
                    indexesMotors.size() == degrees.size()) {
                arduino.serialWrite(String.valueOf(Motors.get(indexesMotors.get(i)).getMotorRelay().getPort()));
                arduino.serialWrite('#');
                arduino.serialWrite(String.valueOf(Motors.get(indexesMotors.get(i)).getPort()));
                arduino.serialWrite('#');
                arduino.serialWrite(String.valueOf(Motors.get(indexesMotors.get(i)).rightRotation(degrees.get(i))));
                success = true;

                if (i != indexesMotors.size() - 1)
                {
                    arduino.serialWrite('&');
                }
            }
        }
        if (success) {
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * ��������� ��������� ���������� � ������ �����������
     * @param indexesMotors - ������ ����������
     * @param side          - ������� �������� (����, �����)
     * @param degrees       - ������� �������� ��� ������� ��������� �� ������� ��������
     */
    @Override
    public void ManyAndAnyRotation(List<Integer> indexesMotors, List<LEFTRIGHT> side, List<Integer> degrees) {
        boolean success = false;
        if (indexesMotors.size() == side.size() && side.size() == degrees.size()) {
            for (int i = 0; i < indexesMotors.size(); i++) {
                if (indexesMotors.get(i) >= 0 && indexesMotors.get(i) < Motors.size() &&
                        indexesMotors.size() == degrees.size()) {
                    arduino.serialWrite(String.valueOf(Motors.get(indexesMotors.get(i)).getMotorRelay().getPort()));
                    arduino.serialWrite('#');
                    arduino.serialWrite(String.valueOf(Motors.get(indexesMotors.get(i)).getPort()));
                    arduino.serialWrite('#');

                    if (side.get(i) == LEFTRIGHT.RIGHT)
                        arduino.serialWrite(String.valueOf(Motors.get(indexesMotors.get(i)).rightRotation(degrees.get(i))));
                    else if (side.get(i) == LEFTRIGHT.LEFT)
                        arduino.serialWrite(String.valueOf(Motors.get(indexesMotors.get(i)).leftRotation(degrees.get(i))));
                    success = true;

                    if (i != indexesMotors.size() - 1) {
                        arduino.serialWrite('&');
                    }
                }
            }
            if (success) {
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /*
    @Override
    public void ManyAndAnyRotationOutsideMotors(List<Motor> motors, List<LEFTRIGHT> side, List<Integer> degrees) {
        boolean success = false;
        if (motors.size() == side.size() && side.size() == degrees.size()) {
            for (int i = 0; i < motors.size(); i++) {

                    arduino.serialWrite(String.valueOf(motors.get(i).getMotorRelay().getPort()));
                    arduino.serialWrite('#');
                    arduino.serialWrite(String.valueOf(motors.get(i).getPort()));
                    arduino.serialWrite('#');

                    if (side.get(i) == LEFTRIGHT.RIGHT)
                        arduino.serialWrite(String.valueOf(motors.get(i).rightRotation(degrees.get(i))));
                    else if (side.get(i) == LEFTRIGHT.LEFT)
                        arduino.serialWrite(String.valueOf(motors.get(i).leftRotation(degrees.get(i))));
                    success = true;

                    if (i != motors.size() - 1) {
                        arduino.serialWrite('&');
                    }

            }
            if (success) {
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    */

    /**
     * ������������� ������.
     * �������������� �� 5 �������� � ��� �����, ���� ��� �����������
     * ���� ������ ����������������, �� ����������.
     *
     * ����� �����, ��� ���� ����� �������� �������� ������ ������������ ��� ������
     * �������� ��������� �� �������. �� �� ������ ������ ����������� �� ���� �������������.
     *
     * *�������� ������ ������������ - ��� ������ ��������� �� �������
     * ����������� �������������� �� �� ������ ������, � ����� �� 90 ��������
     * � ������ �������. �� ������� ������ ��� ����������. ��� ����� ��� ������ ���� �����.
     *
     * ���� - ��������� ����������������� ���� ����������.
     * ����� - �������� ���� ���������� �������� �����.
     */
    void initializationServo() {
        if (Motors.size() > 0)
        {
            if (Motors.size() - 1 == 0)
            {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            if (Motors.get(Motors.size() - 1).currentMicroseconds == 2500) {
                leftRotation(Motors.size() - 1, 5);
                rightRotation(Motors.size() - 1, 5);
            }
            else {
                rightRotation(Motors.size() - 1, 5);
                leftRotation(Motors.size() - 1, 5);
            }
        }
    }

    /**
     * �������� ��������� � �������
     * @return  - ���� ���������� ��� ���.
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * �������� ���������� c �������
     */
    public void closeConnection()
    {
        if (connected) {
            arduino.closeConnection();
            connected = false;
        }
    }
}
