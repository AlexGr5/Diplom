package org.example.HighLevel;


import org.example.LowLevel.ArduinoMotorsImplComPort;
import org.example.LowLevel.LEFTRIGHT;
import org.example.LowLevel.Motor;
import org.example.LowLevel.Relay;

/**
 * Модель робота с осями X, Y, Z, захватом и гусеницами.
 */
public class ModelRobot {

    /**
     * Ось Х
     */
    Axis2 axisX = new Axis2();

    /**
     * Ось Y
     */
    Axis2 axisY = new Axis2();

    /**
     * Ось Z
     */
    Axis2 axisZ = new Axis2();

    /**
     * Захват
     */
    Axis2 axisHand = new Axis2();

    /**
     * Гусеницы
     */
    Caterpillars caterpillars = new Caterpillars();

    /**
     * Соединение с Ардуино через последовательный порт
     */
    ArduinoMotorsImplComPort arduinoMotors = null;

    /**
     * Конструктор с параметрами для создания соединения с Ардуино
     * @param serialPort    - последовательный порт (например "COM7")
     * @param speed         - скорость передачи данных (обычно 9600)
     */
    public ModelRobot(String serialPort, int speed) {
        arduinoMotors = new ArduinoMotorsImplComPort(serialPort, speed);
    }

    /**
     * Добавить двигатель в ось X
     * @param motor     - двигатель
     * @param direction - направление
     */
    public void addMotorToX(Motor motor, LEFTRIGHT direction) {
        arduinoMotors.addMotor(motor);
        axisX.addOnePart(arduinoMotors.getIndexForMotor(motor), direction);
    }

    /**
     * Добавить двигатель в ось Y
     * @param motor     - двигатель
     * @param direction - направление
     */
    public void addMotorToY(Motor motor, LEFTRIGHT direction) {
        arduinoMotors.addMotor(motor);
        axisY.addOnePart(arduinoMotors.getIndexForMotor(motor), direction);
    }

    /**
     * Добавить двигатель в ось Z
     * @param motor     - двигатель
     * @param direction - направление
     */
    public void addMotorToZ(Motor motor, LEFTRIGHT direction) {
        arduinoMotors.addMotor(motor);
        axisZ.addOnePart(arduinoMotors.getIndexForMotor(motor), direction);
    }

    /**
     * Установить гусеницы
     * @param leftMotor1    - первый мотор (движение вперёд)
     * @param leftMotor2    - первый мотор (движение назад)
     * @param rightMotor3   - второй мотор (движение вперёд)
     * @param rightMotor4   - второй мотор (движение назад)
     * @param direction1    - направление
     * @param direction2    - направление
     * @param direction3    - направление
     * @param direction4    - направление
     */
    public void setCaterpillars(Motor leftMotor1, Motor leftMotor2,
                                Motor rightMotor3, Motor rightMotor4,
                                LEFTRIGHT direction1, LEFTRIGHT direction2,
                                LEFTRIGHT direction3, LEFTRIGHT direction4) {
        arduinoMotors.addMotor(leftMotor1);
        arduinoMotors.addMotor(leftMotor2);
        arduinoMotors.addMotor(rightMotor3);
        arduinoMotors.addMotor(rightMotor4);

        caterpillars.setCaterpillars(arduinoMotors.getIndexForMotor(leftMotor1), arduinoMotors.getIndexForMotor(leftMotor2),
                arduinoMotors.getIndexForMotor(rightMotor3), arduinoMotors.getIndexForMotor(rightMotor4),
                direction1, direction2, direction3, direction4);
    }

    /**
     * Вперёд по Х
     */
    public void forwardX() {
        System.out.println("\n=======ForwardX=======");
        axisX.oneStepForward(arduinoMotors);
        System.out.println("======================");
    }

    /**
     * Назад по Х
     */
    public void backX() {
        System.out.println("\n=======BackX=======");
        axisX.oneStepBack(arduinoMotors);
        System.out.println("===================");
    }

    /**
     * Вперед по У
     */
    public void forwardY() {
        System.out.println("\n=======ForwardY=======");
        axisY.oneStepForward(arduinoMotors);
        System.out.println("======================");
    }

    /**
     * Назад по У
     */
    public void backY() {
        System.out.println("\n=======BackY=======");
        axisY.oneStepBack(arduinoMotors);
        System.out.println("===================");
    }

    /**
     * Вперед по Z
     */
    public void forwardZ() {
        System.out.println("\n=======ForwardZ=======");
        axisZ.oneStepForward(arduinoMotors);
        System.out.println("======================");
    }

    /**
     * Назад по Z
     */
    public void backZ() {
        System.out.println("\n=======BackZ=======");
        axisZ.oneStepBack(arduinoMotors);
        System.out.println("===================");
    }

    /**
     * Взять захватом
     */
    public void takeHand() {
        System.out.println("\n=======TakeHand=======");
        axisHand.oneStepForward(arduinoMotors);
        System.out.println("======================");
    }

    /**
     * Положить захватом
     */
    public void throwHand() {
        System.out.println("\n=======ThrowHand=======");
        axisHand.oneStepBack(arduinoMotors);
        System.out.println("======================");
    }

    /**
     * Поехать вперед
     */
    public void forwardCat() {
        System.out.println("\n=======ForwardCat=======");
        caterpillars.goForward(arduinoMotors);
        System.out.println("========================");
    }

    /**
     * Поехать назад
     */
    public void backCat() {
        System.out.println("\n=======BackCat=======");
        caterpillars.goBack(arduinoMotors);
        System.out.println("=====================");
    }

    /**
     * Повернуть влево
     */
    public void leftCat() {
        System.out.println("\n=======LeftCat=======");
        caterpillars.goLeft(arduinoMotors);
        System.out.println("=====================");
    }

    /**
     * Повернуть вправо
     */
    public void rightCat() {
        System.out.println("\n=======RightCat=======");
        caterpillars.goRight(arduinoMotors);
        System.out.println("======================");
    }

    /**
     * Инициализировать мою модель робота
     * Конфигурация по умолчанию
     */
    public void myRobotInit() {
        System.out.println("----------------Initialization-Motors-START--------------");
        Relay x = new Relay(50);
        Relay y21 = new Relay(51);
        Relay y22 = new Relay(52);
        Relay z = new Relay(53);
//        Relay hand = new Relay(46);
//        Relay cat11 = new Relay(41);
//        Relay cat12 = new Relay(42);
//        Relay cat21 = new Relay(43);
//        Relay cat22 = new Relay(44);

        Motor m1 = new Motor(5, x,600, 2100, 9,  144, 1400);
        // 6, y21,700, 1350,18, 77, 1100
        Motor m21 = new Motor(6, y21,700, 1350,18, 107, 1100);
        // 8, y22,1150, 1800, 59, 118, 1400
        Motor m22 = new Motor(8, y22,1150, 1800, 59, 148, 1400);
        // 10, z,1100, 1450, 54, 86, 1200
        Motor m3 = new Motor(10, z,1100, 1450, 54, 116, 1200);
//        Motor mhand = new Motor(4, hand,500, 2500, 180, 2500);
//        Motor motorCat11 = new Motor(100, cat11, 500,2500, 180, 1500);
//        Motor motorCat12 = new Motor(100, cat12, 500,2500, 180, 1500);
//        Motor motorCat21 = new Motor(100, cat21, 500,2500, 180, 1500);
//        Motor motorCat22 = new Motor(100, cat22, 500,2500, 180, 1500);

        arduinoMotors.addMotor(m1);
        arduinoMotors.addMotor(m21);
        arduinoMotors.addMotor(m22);
        arduinoMotors.addMotor(m3);
//        arduinoMotors.addMotor(mhand);
//        arduinoMotors.addMotor(motorCat11);
//        arduinoMotors.addMotor(motorCat12);
//        arduinoMotors.addMotor(motorCat21);
//        arduinoMotors.addMotor(motorCat22);

        axisX.addOnePart(0, LEFTRIGHT.LEFT);
        axisZ.addOnePart(1, LEFTRIGHT.LEFT);
        axisZ.addOnePart(2, LEFTRIGHT.RIGHT);
        axisY.addOnePart(3, LEFTRIGHT.LEFT);
//        axisHand.addOnePart(4, LEFTRIGHT.LEFT);
//        caterpillars.setCaterpillars(5, 6,
//                7, 8,
//                LEFTRIGHT.RIGHT, LEFTRIGHT.LEFT, LEFTRIGHT.LEFT, LEFTRIGHT.RIGHT);
        System.out.println("-----------------Initialization-Motors-END---------------");
    }

    /**
     * Тестовая конфигурация
     */
    public void myTest2MotorsInit() {
        Relay y21 = new Relay(2);
        Relay y22 = new Relay(4);

        Motor m21 = new Motor(6, y21,500, 2500, 0, 180, 1500);
        Motor m22 = new Motor(5, y22,500, 2500, 0, 180, 1500);

        arduinoMotors.addMotor(m21);
        arduinoMotors.addMotor(m22);

        axisY.addOnePart(0, LEFTRIGHT.LEFT);
        axisY.addOnePart(1, LEFTRIGHT.RIGHT);
    }


    /*
    public static void main(String[] args) {
        ModelRobot testModel = new ModelRobot("COM6", 9600);
        testModel.myTest2MotorsInit();
        testModel.backY();
        testModel.forwardY();

    }
    */

    /**
     * Закрыть соединение с Ардуино
     */
    public void closeConnectionWithArduino() {
        arduinoMotors.closeConnection();
    }

    /**
     * Проверка работоспособности модели
     * @param args  - аргументы
     */
    public static void main(String[] args) {
        ModelRobot robotModel = new ModelRobot("COM7", 9600);

        System.out.println("------------------Initialization-Motors-----------------");
        robotModel.myRobotInit();
        System.out.println("----------------Initialization-Motors-END---------------");

        System.out.println("\n\n=======================Work=============================");
        robotModel.forwardX();
        robotModel.backX();

        robotModel.forwardY();
        robotModel.backY();

        robotModel.forwardZ();
        robotModel.backZ();

        /*
        robotModel.forwardCat();
        robotModel.backCat();

        robotModel.leftCat();
        robotModel.rightCat();
        */

        robotModel.closeConnectionWithArduino();
        System.out.println("\n\n=======================Work=END=========================");
    }
}
