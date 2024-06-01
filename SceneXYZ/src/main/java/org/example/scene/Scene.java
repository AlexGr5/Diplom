package org.example.scene;

import org.example.HighLevel.ModelRobot;
import org.example.network.Action;
import org.example.network.SceneState;
import org.example.network.util.SceneStateUtil;
import org.example.scene.helper.Direction;
import org.example.scene.helper.Position;
import org.example.scene.util.NewPositionCalculate;
import org.example.scene.util.RewardUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;

/**
 * Сцена, в которой работают математическая и физическая модели робота
 */
public class Scene {
    private static final Logger LOG = LoggerFactory.getLogger(Scene.class);

    // Текущая позиция
    private transient Position currentPosition;

    // Направление движения текущей позиции
    private Direction currentDirection = Direction.RIGHT;

    // Желаемая позиция
    private transient Position targetPosition;

    // Флаг, что текущая позиция добралась до желаемой
    boolean isWork = true;

    // Количество шагов для одной цели
    int countStepsForOneTarget = 0;

    int loop = 0;

    public Scene() {
        //initializeScene();


        ModelRobot robotModel = new ModelRobot("COM7", 9600);
        robotModel.myRobotInit();

        System.out.println("\n\n*** Time 10s to change locations balls ***\n\n");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        currentPosition = new Position(-660, -310, 250, 480, 530 ,750, robotModel);
        targetPosition = new Position(-660, -310, 250, 480, 530 ,750);
//        currentPosition = new Position(-620, -230, 250, 550, 510 ,750);
//        targetPosition = new Position(-620, -230, 250, 550, 510 ,750);
//        currentPosition = new Position(-36, -36, -36, 36, 36 ,36);
//        targetPosition = new Position(-36, -36, -36, 36, 36 ,36);

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//        currentPosition.generateNewXYZ();
//        targetPosition.generateNewXYZ();
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        currentPosition.getCurrentXYZOnCamera();
        targetPosition.getTargetXYZOnCamera();

//        currentPosition.testCurrentPosition();
//        targetPosition.testTargetPosition();
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        isWork = true;

        System.out.println("\n\n===================Start work!===================");
    }

    public boolean actionPerformed(ActionEvent e) {
        boolean result = false;

        if (isWork()) {
            if (isWin()) {

                win();

                // Set food on a new position

                //!!!!!!!!!!!!!
                // Влияет на логическое завершение
                // Если убрать это, то будет работать как змейка
                isWork = false;
                // А это раскомментировать:
//                generateNewTargetPosition();
//                System.out.println("\n-------------------------");
//                System.out.println("Generate new target!");
//                System.out.println("Target: " + targetPosition.stringXYZ());
//                System.out.println("Current is: " + currentPosition.stringXYZ());
                //!!!!!!!!!!!!!

                countStepsForOneTarget = 0;
                result = true;
            } else {
                isWork = !currentPosition.isOutsideBounds();

                if (currentPosition.isOutsideBounds())
                    countStepsForOneTarget = 0;

                if (countStepsForOneTarget >= 200) {
                    countStepsForOneTarget = 0;
                    isWork = false;
                    loop++;
                }

                // Поправка по изображению каждые 10 шагов
//                if (countStepsForOneTarget % 10 == 0) {
//                    currentPosition.getCurrentXYZOnCamera();
//                    targetPosition.getTargetXYZOnCamera();
//                }

                if (!isWork)
                    System.out.println("Position is outside!");
            }
        }

        if (!isWork) {
            LOG.debug("Scene is over :(");
        }

        return result;
    }

    public int getLoop() {
        return loop;
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentPosition(Position currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void setCurrentDirection(Direction currentDirection) {
        this.currentDirection = currentDirection;
    }

    public void setTargetPosition(Position targetPosition) {
        this.targetPosition = targetPosition;
    }

    /**
     * Move the player and check for collisions
     */
    public boolean move() {

        printStep();

        //==================================================
        // изменение позиции
        // Математическое изменение
        currentPosition = NewPositionCalculate.getNextPosition(currentPosition, currentDirection);

        // Изменение робота
        currentPosition.useNextPosition(currentDirection);

        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // Получение новых координат после изменений с камеры
        //currentPosition.getCurrentXYZOnCamera();
        //==================================================

        //printStep();

        countStepsForOneTarget++;
//        if (countStepsForOneTarget >= 100) {
//            //targetPosition.setCurrentMiddle();
//            printStep();
//        }

        // Проверки хода
        boolean result = actionPerformed(null);

        return result;
    }

    /**
     * Change direction based on forwarded action.
     *
     * @param action Action based on which direction is changed.
     */
    public void changeDirection(final Action action) {
        LOG.debug("Current direction {}, changing to {}", currentDirection, action);

        switch (action) {
            case GO_UP:
                if (currentDirection == Direction.DOWN) break;
                currentDirection = Direction.UP;
                break;
            case GO_RIGHT:
                if (currentDirection == Direction.LEFT) break;
                currentDirection = Direction.RIGHT;
                break;
            case GO_DOWN:
                if (currentDirection == Direction.UP) break;
                currentDirection = Direction.DOWN;
                break;
            case GO_LEFT:
                if (currentDirection == Direction.RIGHT) break;
                currentDirection = Direction.LEFT;
                break;
            case GO_DEEP:
                if (currentDirection == Direction.OUTSIDE) break;
                currentDirection = Direction.DEEP;
                break;
            case GO_OUTSIDE:
                if (currentDirection == Direction.DEEP) break;
                currentDirection = Direction.OUTSIDE;
                break;
        }
    }

    /**
     * Initializes game world and places the food and player on starting position
     */
    public SceneState initializeScene() {
//        currentPosition = new Position(-10, -10, -10, 10, 10 ,10);
//        targetPosition = new Position(-10, -10, -10, 10, 10 ,10);

        // Желаемая позиция
        //generateNewTargetPosition();


        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//        currentPosition.generateNewXYZ();
//        targetPosition.generateNewXYZ();
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        currentPosition.getCurrentXYZOnCamera();
        targetPosition.getTargetXYZOnCamera();

//        currentPosition.testCurrentPosition();
//        targetPosition.testTargetPosition();
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        System.out.println("\n-------------------------");
        System.out.println("Generate new numbers!");
        System.out.println("Current: " + currentPosition.stringXYZ());
        System.out.println("Target: " + targetPosition.stringXYZ());

        // Mark that player is in game
        isWork = true;

        // Return observation of the current game state
        return buildStateObservation();
    }

    /**
     * Used to check if the game is still ongoing.
     *
     * @return Returns true if player is still alive and in the game.
     */
    public boolean isWork() {
        return isWork;
    }

    /**
     * Used to end game.
     */
    public void endGame() {
        this.isWork = false;
    }

    /**
     * Get current game state observation. Snake can observe 4 states. They are positions in 4 directions around the
     * head. For example if head is at pos(50 50) => pos(x y), snake can observe position UP pos(50 40), DOWN pos(50 60),
     * RIGHT pos(60 50) and LEFT pos(40 50).
     *
     * @return Returns an object representing current game state observation.
     */
    public SceneState buildStateObservation() {
        return new SceneState(new double[] {
                SceneStateUtil.getStateForDirection(currentPosition, targetPosition, Direction.UP),
                SceneStateUtil.getStateForDirection(currentPosition, targetPosition, Direction.RIGHT),
                SceneStateUtil.getStateForDirection(currentPosition, targetPosition, Direction.DOWN),
                SceneStateUtil.getStateForDirection(currentPosition, targetPosition, Direction.LEFT),
                SceneStateUtil.getStateForDirection(currentPosition, targetPosition, Direction.DEEP),
                SceneStateUtil.getStateForDirection(currentPosition, targetPosition, Direction.OUTSIDE),
        });
    }

    /**
     * Used to calculate the reward for action that was taken.
     *
     * @param action Taken action.
     * @return Returns calculated reward.
     */
    public double calculateRewardForActionToTake(final Action action) {
        return RewardUtil.calculateRewardForActionToTake(
                action,
                currentPosition,
                targetPosition
        );
    }

    // Если текущая позиция полностью совпадает с желаемой или
    // текущая позиция рядом (на радиусе 1) с желаемой и
    // текущая позиция не за пределами поля, то робот победил
    public boolean isWin() {
        return (currentPosition.equals(targetPosition) ||
                currentPosition.isNear(targetPosition, 60))
                && !currentPosition.isOutsideBounds();
    }

    public void win() {
        System.out.println("WIN!\n");
    }

    public void generateNewTargetPosition() {
        //System.out.println("Scene generate new target!");
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //targetPosition.generateNewXYZ();
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        targetPosition.getTargetXYZOnCamera();

        //targetPosition.testTargetPosition();;
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    public void displayDirection() {
        System.out.println("Direction: " + currentDirection);
    }

    public void setNewTargetPosition(int x, int y, int z) {
        targetPosition.setCurrentXYZ(x, y, z);
    }

    public Position getTargetPosition() {
        return targetPosition;
    }

    public void printStep() {
        System.out.println("------------Move------------");
        System.out.println("Current = " + currentPosition.stringXYZ());
        System.out.println("Target = " + targetPosition.stringXYZ());
        System.out.println("Use direction: " + currentDirection);
        System.out.println("----------------------------");
    }

    // Поиск оптимального пути для следующего шага
    public Direction findOptimisticDirectionForCurrent() {
        Direction result = currentDirection;

        double array[] = new double[] {
                SceneStateUtil.getStateForDirection(currentPosition, targetPosition, Direction.UP),
                SceneStateUtil.getStateForDirection(currentPosition, targetPosition, Direction.RIGHT),
                SceneStateUtil.getStateForDirection(currentPosition, targetPosition, Direction.DOWN),
                SceneStateUtil.getStateForDirection(currentPosition, targetPosition, Direction.LEFT),
                SceneStateUtil.getStateForDirection(currentPosition, targetPosition, Direction.DEEP),
                SceneStateUtil.getStateForDirection(currentPosition, targetPosition, Direction.OUTSIDE),
        };

        int resultIndex = -1;
        int flagOne = 0;
        int flagZero = 0;
        for (int i = 0; i < 6; i++){
            if (array[i] == 1.0)
                flagOne = 1;
            if (array[i] == 0.0)
                flagZero = 1;
        }

        if (flagOne == 1)
        {
            while (resultIndex == -1) {
                int tmp = (int) (-1 +  Math.ceil(Math.random() * 6));
                if (array[tmp] == 1.0)
                    resultIndex = tmp;
            }
        }
        else {
            if (flagZero == 1)
            {
                //==================================================================================
                // Попытка уйти от зацикливания, путем изменения радиуса
                //if (array[0] == 0.0 && array[1] == 0.0 && array[2] == 0.0 && array[3] == 0.0)
                if (array[0] == 0.0 && array[2] == 0.0 || array[1] == 0.0 && array[3] == 0.0 ||
                        array[0] == 0.0 && array[1] == 0.0 && array[2] == 0.0 && array[3] == 0.0 ||
                        array[0] == 0.0 && array[2] == -1.0 || array[1] == 0.0 && array[3] == -1.0 ||
                        array[0] == -1.0 && array[2] == 0.0 || array[1] == -1.0 && array[3] == 0.0)
                {
                    Position plusRadius = currentPosition.clone();
                    Position minusRadius = currentPosition.clone();

                    plusRadius.plusOneToRadiusSphere();
                    minusRadius.minusOneToRadiusSphere();

                    if (targetPosition.distance(plusRadius) > targetPosition.distance(minusRadius))
                    {
                        //currentPosition.setCurrentXYZ(currentPosition.minusOneToRadiusSphere());
                        return Direction.OUTSIDE;
                    }
                    else
                    {
                        //currentPosition.setCurrentXYZ(currentPosition.plusOneToRadiusSphere());
                        return Direction.DEEP;
                    }
                }
                //==================================================================================

                while (resultIndex == -1) {
                    int tmp = (int) (-1 + Math.ceil(Math.random() * 6));
                    if (array[tmp] == 0.0)
                        resultIndex = tmp;
                }
            }
        }

        if (resultIndex != -1)
        {
            switch (resultIndex) {
                case 0: {
                    result = Direction.UP;
                    break;
                }
                case 1: {
                    result = Direction.RIGHT;
                    break;
                }
                case 2: {
                    result = Direction.DOWN;
                    break;
                }
                case 3: {
                    result = Direction.LEFT;
                    break;
                }
                case 4: {
                    result = Direction.DEEP;
                    break;
                }
                case 5: {
                    result = Direction.OUTSIDE;
                    break;
                }
            }

        }

//        for (int i = 0; i < 6; i++)
//            System.out.print(array[i] + " ");
//        System.out.println("NEW " + result);

        return result;
    }
}
