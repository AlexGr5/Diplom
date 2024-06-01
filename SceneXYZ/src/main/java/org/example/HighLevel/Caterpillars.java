package org.example.HighLevel;


import org.example.LowLevel.ArduinoMotorsImplComPort;
import org.example.LowLevel.LEFTRIGHT;

/**
 * Гусеницы
 */
public class Caterpillars {

    /**
     * Комбинация направлений двигателей для движения вперёд
     */
    Axis2 forward = new Axis2();

    /**
     * Комбинация направлений двигателей для движения назад
     */
    Axis2 back = new Axis2();

    /**
     * Комбинация направлений двигателей для движения влево
     */
    Axis2 left = new Axis2();

    /**
     * Комбинация направлений двигателей для движения вправо
     */
    Axis2 right = new Axis2();

    /**
     * Конструктор без параметров
     */
    public Caterpillars() {

    }

    /**
     * Установка моторов для гусениц
     * @param indexLeftMotor1   - номер первого мотора (направление вперед)
     * @param indexLeftMotor2   - номер первого мотора (направление назад)
     * @param indexRightMotor3  - номер второго мотора (направление вперед)
     * @param indexRightMotor4  - номер второго мотора (направление назад)
     * @param direction1        - направление первого мотора вперед
     * @param direction2        - направление первого мотора назад
     * @param direction3        - направление второго мотора вперед
     * @param direction4        - направление второго мотора назад
     */
    public void setCaterpillars(int indexLeftMotor1, int indexLeftMotor2,
                                int indexRightMotor3, int indexRightMotor4,
                                LEFTRIGHT direction1, LEFTRIGHT direction2,
                                LEFTRIGHT direction3, LEFTRIGHT direction4) {
        forward.addOnePart(indexLeftMotor1, direction1);
        forward.addOnePart(indexRightMotor3, direction3);

        back.addOnePart(indexLeftMotor2, direction2);
        back.addOnePart(indexRightMotor4, direction4);

        left.addOnePart(indexLeftMotor1, direction1);
        left.addOnePart(indexRightMotor4, direction4);

        right.addOnePart(indexLeftMotor2, direction2);
        right.addOnePart(indexRightMotor3, direction3);
    }

    /**
     * Ехать вперед
     * @param arduinoMotors - соединение с ардуино
     */
    public void goForward(ArduinoMotorsImplComPort arduinoMotors) {
        forward.oneStepForward(arduinoMotors);
    }

    /**
     * Ехать назад
     * @param arduinoMotors - соединение с ардуино
     */
    public void goBack(ArduinoMotorsImplComPort arduinoMotors) {
        back.oneStepForward(arduinoMotors);
    }

    /**
     * Повернуть влево
     * @param arduinoMotors - соединение с ардуино
     */
    public void goLeft(ArduinoMotorsImplComPort arduinoMotors) {
        left.oneStepForward(arduinoMotors);
    }

    /**
     * Повернуть вправо
     * @param arduinoMotors - соединение с ардуино
     */
    public void goRight(ArduinoMotorsImplComPort arduinoMotors) {
        right.oneStepForward(arduinoMotors);
    }
}
