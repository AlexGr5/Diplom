package org.example.LowLevel;

import java.util.ArrayList;
import java.util.List;


/**
 * Интерфейс взаимодействия с сервоприводами
 */
public interface IArduinoMotors {

    /**
     * Набор всех сервоприводов
     */
    List<Motor> Motors = new ArrayList<>();

    /**
     * Добавить сервопривод
     * @param one   - один сервопривод
     */
    public default void addMotor(Motor one)
    {
        Motors.add(one);
    }

    /**
     * Получение мотора по индексу
     * @param index - индекс
     * @return      - мотор
     */
    public default Motor getMotor(int index) {
        if (index >=0 && index < Motors.size())
            return Motors.get(index);
        return null;
    }

    /**
     * Получить индекс в списке по мотору
     * @param one   - мотор
     * @return      - индекс
     */
    public default int getIndexForMotor(Motor one) {
        if (Motors.contains(one))
            return Motors.indexOf(one);
        return -1;
    }

    /**
     * Уделить сервопривод из набора по индексу
     * @param indexMotor    - индекс
     */
    public default void delMotor(int indexMotor)
    {
        if (indexMotor >= 0 && indexMotor < Motors.size())
        {
            Motors.remove(indexMotor);
        }
    }

    /**
     * Поворот налево на градус
     * @param indexMotor    - номер двигателя
     * @param degree        - градус поворота
     */
    public void leftRotation(int indexMotor, int degree);


    /**
     * Поворот направо на градус
     * @param indexMotor    - номер двигателя
     * @param degree        - градус поворота
     */
    public void rightRotation(int indexMotor, int degree);

    /**
     * Поворот налево для нескольких двигателей одновременно
     * @param indexesMotors - номера двигателей
     * @param degrees       - градусы поворота для каждого двигателя по порядку индексов
     */
    public void leftManyRotation(List<Integer> indexesMotors, List<Integer> degrees);

    /**
     * Поворот направо для нескольких двигателей одновременно
     * @param indexesMotors - номера двигателей
     * @param degrees       - градусы поворота для каждого двигателя по порядку индексов
     */
    public void rightManyRotation(List<Integer> indexesMotors, List<Integer> degrees);

    /**
     * Поворот двигателя направо или налево на выбор для нескольких двигателей одновременно
     * @param indexesMotors - номера двигателей
     * @param side          - сторона поворота (лево, право)
     * @param degrees       - градусы поворота для каждого двигателя по порядку индексов
     */
    public void ManyAndAnyRotation(List<Integer> indexesMotors, List<LEFTRIGHT> side, List<Integer> degrees);

    /**
     * Поворот двигателя направо или налево на выбор для нескольких двигателей одновременно
     * @param motors - номера двигателей
     * @param side          - сторона поворота (лево, право)
     * @param degrees       - градусы поворота для каждого двигателя по порядку индексов
     */
    //public void ManyAndAnyRotationOutsideMotors(List<Motor> motors, List<LEFTRIGHT> side, List<Integer> degrees);
}
