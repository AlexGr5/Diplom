package org.example.LowLevel;

import java.util.ArrayList;
import java.util.List;


/**
 * ��������� �������������� � ��������������
 */
public interface IArduinoMotors {

    /**
     * ����� ���� �������������
     */
    List<Motor> Motors = new ArrayList<>();

    /**
     * �������� �����������
     * @param one   - ���� �����������
     */
    public default void addMotor(Motor one)
    {
        Motors.add(one);
    }

    /**
     * ��������� ������ �� �������
     * @param index - ������
     * @return      - �����
     */
    public default Motor getMotor(int index) {
        if (index >=0 && index < Motors.size())
            return Motors.get(index);
        return null;
    }

    /**
     * �������� ������ � ������ �� ������
     * @param one   - �����
     * @return      - ������
     */
    public default int getIndexForMotor(Motor one) {
        if (Motors.contains(one))
            return Motors.indexOf(one);
        return -1;
    }

    /**
     * ������� ����������� �� ������ �� �������
     * @param indexMotor    - ������
     */
    public default void delMotor(int indexMotor)
    {
        if (indexMotor >= 0 && indexMotor < Motors.size())
        {
            Motors.remove(indexMotor);
        }
    }

    /**
     * ������� ������ �� ������
     * @param indexMotor    - ����� ���������
     * @param degree        - ������ ��������
     */
    public void leftRotation(int indexMotor, int degree);


    /**
     * ������� ������� �� ������
     * @param indexMotor    - ����� ���������
     * @param degree        - ������ ��������
     */
    public void rightRotation(int indexMotor, int degree);

    /**
     * ������� ������ ��� ���������� ���������� ������������
     * @param indexesMotors - ������ ����������
     * @param degrees       - ������� �������� ��� ������� ��������� �� ������� ��������
     */
    public void leftManyRotation(List<Integer> indexesMotors, List<Integer> degrees);

    /**
     * ������� ������� ��� ���������� ���������� ������������
     * @param indexesMotors - ������ ����������
     * @param degrees       - ������� �������� ��� ������� ��������� �� ������� ��������
     */
    public void rightManyRotation(List<Integer> indexesMotors, List<Integer> degrees);

    /**
     * ������� ��������� ������� ��� ������ �� ����� ��� ���������� ���������� ������������
     * @param indexesMotors - ������ ����������
     * @param side          - ������� �������� (����, �����)
     * @param degrees       - ������� �������� ��� ������� ��������� �� ������� ��������
     */
    public void ManyAndAnyRotation(List<Integer> indexesMotors, List<LEFTRIGHT> side, List<Integer> degrees);

    /**
     * ������� ��������� ������� ��� ������ �� ����� ��� ���������� ���������� ������������
     * @param motors - ������ ����������
     * @param side          - ������� �������� (����, �����)
     * @param degrees       - ������� �������� ��� ������� ��������� �� ������� ��������
     */
    //public void ManyAndAnyRotationOutsideMotors(List<Motor> motors, List<LEFTRIGHT> side, List<Integer> degrees);
}
