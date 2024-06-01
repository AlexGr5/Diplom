package org.example.LowLevel;

import arduino.Arduino;

import java.util.List;

/**
 * Реализация взаимодействия JAVA программы с Arduino
 * для управления сервоприводами и реле
 */
public class ArduinoMotorsImplComPort implements IArduinoMotors {

    /**
     * Ардуино
     */
    Arduino arduino = null;     // new Arduino("COM6", 9600);

    /**
     * Влаг соединения с Ардуино
     */
    boolean connected = false;  // arduino.openConnection();

    /**
     * Временная задержка на отправку одного сообщения
     * для корректной работы по последовательному порту
     */
    static int DELAY = 6000;

    /**
     * Конструктор, передаются параметры для АРДУNНО
     * @param serialPort - последовательный порт (например "COM7")
     * @param speed      - скорость передачи сообщений (обычно 9600)
     */
    public ArduinoMotorsImplComPort(String serialPort, int speed)
    {
        this.arduino = new Arduino(serialPort, speed);
        connected = arduino.openConnection();
        System.out.println("Connection established: " + connected);
    }

    /**
     * Конструктор, с уже созданным АРДУИНО
     * @param arduino   - Ардуино
     */
    public ArduinoMotorsImplComPort(Arduino arduino) {
        this.arduino = arduino;
        connected = arduino.openConnection();
        System.out.println("Connection established: " + connected);
    }

    /**
     * Добавить мотор в список всех моторов
     * @param one   - один сервопривод
     */
    @Override
    public void addMotor(Motor one)
    {
        Motors.add(one);
        initializationServo();
    }

    /**
     * Повернуть мотор влево
     * @param indexMotor    - номер двигателя
     * @param degree        - градус поворота
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
     * Повернуть мотор вправо
     * @param indexMotor    - номер двигателя
     * @param degree        - градус поворота
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
     * Повернуть несколько двигателей влево
     * @param indexesMotors - номера двигателей
     * @param degrees       - градусы поворота для каждого двигателя по порядку индексов
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
     * Повернуть несколько двигателей вправо
     * @param indexesMotors - номера двигателей
     * @param degrees       - градусы поворота для каждого двигателя по порядку индексов
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
     * Повернуть несколько двигателей в разные направления
     * @param indexesMotors - номера двигателей
     * @param side          - сторона поворота (лево, право)
     * @param degrees       - градусы поворота для каждого двигателя по порядку индексов
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
     * Инициализация мотора.
     * Поворачивается на 5 градусов в зад назад, если это СЕРВОПРИВОД
     * Если просто электродвигатель, то включается.
     *
     * Метод нужен, для того чтобы избежать СТРАННОЙ работы сервопривода при первой
     * передачи сообщения на Ардуино. Но на всякий случай применяется ко всем сервоприводам.
     *
     * *СТРАННАЯ работа сервопривода - при первом сообщении на Ардуино
     * сервопривод поворачивается не на нужный градус, а сразу на 90 градусов
     * в нужную сторону. Не понятно почему это происходит. Для этого был создан этот метод.
     *
     * Плюс - проверяет работоспособность всех двигателей.
     * Минус - проверка всех двигателей занимает время.
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
     * Проверка соединния с Ардуино
     * @return  - есть соединение или нет.
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Закрытие соединения c Ардуино
     */
    public void closeConnection()
    {
        if (connected) {
            arduino.closeConnection();
            connected = false;
        }
    }
}
