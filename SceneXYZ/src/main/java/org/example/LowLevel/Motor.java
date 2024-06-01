package org.example.LowLevel;

/**
 *  ласс мотора
 * ’ранит –≈Ћ≈, которое принадлежит ему
 * и номер пина управлени€ самого мотора (сервопривода)
 * ћинимальное и максимальное значени€ поворота в микросекундах
 * ћаксимальное вращение в градусах
 * » текущее положение в микросекундах
 */
public class Motor {

    /**
     * ”правл€ющий порт
     */
    protected int port;

    /**
     * ћинимальное значение микросекунд дл€ управлени€ поворотом
     */
    protected int minMicroseconds;

    /**
     * ћаксимальное значение микросекунд дл€ управлени€ поворотом
     */
    protected int maxMicroseconds;

    /**
     * ћаксимальный градус поворота двигател€ в градусах
     */
    protected int maxDegree;

    /**
     * ћинимальный градус поворота двигател€ в градусах
     */
    protected int minDegree;

    /**
     * “екущее положение мотора
     */
    protected int currentMicroseconds;

    /**
     * –еле дл€ управлени€ питани€ мотором
     */
    protected Relay motorRelay;

    /**
     *  онструктор дл€ двигателей без управлени€, включение по реле
     * @param motorRelay - реле
     */
    //public LowLevel.Motor(LowLevel.Relay motorRelay) {
    //    this.motorRelay = motorRelay;
    //}

    /**
     *  онструктор дл€ двигателей с управл€ющим портом через реле
     * @param port                  - порт управлени€ двигателем
     * @param minMicroseconds       - минимальные значени€ микросекунд
     * @param maxMicroseconds       - максимальные значени€ микросекунд
     * @param motorRelay            - реле
     * @param maxDegree             - максимальный градус поворота
     * @param currentMicroseconds   - текущее положение в микросекундах
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
     * ѕолучить номер порта
     * @return  - номер порта
     */
    public int getPort() {
        return port;
    }

    /**
     * ”становить номер порта
     * @param port - номер порта
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * ѕолучить номер реле
     * @return - номер реле
     */
    public Relay getMotorRelay() {
        return motorRelay;
    }

    /**
     * «адать реле
     * @param motorRelay - реле
     */
    public void setMotorRelay(Relay motorRelay) {
        this.motorRelay = motorRelay;
    }

    /**
     * ѕолучить текущее положение в микросекундах
     * @return - микросекунды
     */
    public int getCurrentMicroseconds() {
        return currentMicroseconds;
    }

    /**
     * ”становить текущее положение в микросекундах
     * @param currentMicroseconds - микросекунды
     */
    public void setCurrentMicroseconds(int currentMicroseconds) {
        this.currentMicroseconds = currentMicroseconds;
    }

    /**
     * ¬ращение двигател€ влево
     * @param degree - градус
     * @return  - результат поворота в микросекундах
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
     * ¬ращение двигател€ вправо
     * @param degree - градус
     * @return  - результат поворота в микросекундах
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
