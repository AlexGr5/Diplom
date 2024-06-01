package org.example.HighLevel;


import org.example.LowLevel.ArduinoMotorsImplComPort;
import org.example.LowLevel.LEFTRIGHT;

import java.util.ArrayList;
import java.util.List;

/**
 * Ось движения части модели
 */
public class Axis2 {

    /**
     * Iндексы моторов в IArduinoMotors и их реализаций
     */
    List<Integer> allMotors = new ArrayList<>();

    /**
     * Направления движения моторов
     */
    List<LEFTRIGHT> directions = new ArrayList<>();

    /**
     * Нужный градус поворота мотора
     */
    List<Integer> degrees = new ArrayList<>();

    /**
     * Конструктор по-умолчанию
     */
    public Axis2() {

    }

    /**
     * Добавить одну часть в ось
     * @param indexOneMotor - индекс мотора
     * @param direction     - направление
     */
    public void addOnePart(Integer indexOneMotor, LEFTRIGHT direction) {
        allMotors.add(indexOneMotor);
        directions.add(direction);
        degrees.add(8);
    }

    /**
     * Один шаг вперед по оси
     * @param arduinoMotors - соединение с ардуино
     */
    public void oneStepForward(ArduinoMotorsImplComPort arduinoMotors) {

        if (arduinoMotors.isConnected()) {
            arduinoMotors.ManyAndAnyRotation(allMotors, directions, degrees);
        }
        else {
            System.out.println("ArduinoMotors is not connected with Arduino!");
        }
    }

    /**
     * Один шаг назад по оси
     * @param arduinoMotors - соединение с ардуино
     */
    public void oneStepBack(ArduinoMotorsImplComPort arduinoMotors) {
        if (arduinoMotors.isConnected()) {
            List<LEFTRIGHT> reverseDirections = new ArrayList<>();

            for (int i = 0; i < directions.size(); i++) {
                if (directions.get(i) == LEFTRIGHT.LEFT)
                    reverseDirections.add(LEFTRIGHT.RIGHT);
                if (directions.get(i) == LEFTRIGHT.RIGHT)
                    reverseDirections.add(LEFTRIGHT.LEFT);
            }

            arduinoMotors.ManyAndAnyRotation(allMotors, reverseDirections, degrees);
        }
        else {
            System.out.println("ArduinoMotors is not connected with Arduino!");
        }
    }
}
