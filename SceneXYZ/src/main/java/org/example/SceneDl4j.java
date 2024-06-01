package org.example;


import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.example.network.Action;
import org.example.network.Environment;
import org.example.network.SceneState;
import org.example.network.util.NetworkUtil;
import org.example.network.util.SceneStateUtil;
import org.example.scene.Scene;
import org.example.scene.helper.Direction;
import org.example.scene.helper.Position;
import org.example.scene.util.NewPositionCalculate;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.example.network.util.SceneStateUtil.getStateForDirection;
import static org.example.scene.util.NewPositionCalculate.isPositionCloserToTargetPosition;

/**
 * Главный класс для запуска всего проекта
 */
public class SceneDl4j {
    private static final Logger LOG = LoggerFactory.getLogger(SceneDl4j.class);

    public SceneDl4j() {
    }

    public void work(Scene scene, String networkName) {
        //final Scene scene = new Scene();

        //final Thread thread = new Thread(() -> {
            // Give a name to the network we are about to train
            //final String networkName = "NetworkSceneXYZ" + ".zip";

            // Create our training environment
            final Environment mdp = new Environment(scene);
            final QLearningDiscreteDense<SceneState> dql = new QLearningDiscreteDense<>(
                    mdp,
                    NetworkUtil.buildDQNFactory(),
                    NetworkUtil.buildConfig()
            );

            // Start the training
            dql.train();
            mdp.close();

            // Save network
            try {
                dql.getNeuralNet().save(networkName);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
            // Reset the scene
            scene.initializeScene();

            // Evaluate just trained network
//            System.out.println("\n\n\n");
//            System.out.println("================Start true work!=================");
//            evaluateNetwork(scene, networkName);
//            System.out.println("===================End work!=====================");
        //});

        //thread.start();
    }

    public void evaluateNetwork(Scene scene, String networkName) {
        final MultiLayerNetwork multiLayerNetwork = NetworkUtil.loadNetwork(networkName);

        int countBadSteps = 0;

        // Количество повторений выполнения задачи
        //int countIterations = 100000;
        int countIterations = 1;


        int successIterations = 0;

        for (int i = 0; i < countIterations; i++) {
            while (scene.isWork()) {
                try {
                    final SceneState state = scene.buildStateObservation();
                    final INDArray output = multiLayerNetwork.output(state.getMatrix(), false);
                    double[] data = output.data().asDouble();
                    int maxValueIndex = SceneStateUtil.getMaxValueIndex(data);

                    scene.changeDirection(Action.getActionByIndex(maxValueIndex));
                    //*************************************
                    //System.out.println("********************************");
                    Direction newDirection = scene.getCurrentDirection();
                    Position currentPosition = scene.getCurrentPosition();
                    Position targetPosition = scene.getTargetPosition();
                    Position nextPosition = NewPositionCalculate.getNextPosition(currentPosition.clone(), newDirection);
                    boolean resultDirection = isPositionCloserToTargetPosition(currentPosition, nextPosition, targetPosition, newDirection);
                    //System.out.println(resultDirection);

                    //**********************************************************************
                    // ПОМОЩЬ НЕЙРОННОЙ СЕТ
                    // Если выбор следующего шага Нейронной Сети не оптимальный, то
                    if (resultDirection == false)
                    {
                        // Помощь в достижении нужной любой одной оси
                        if (countBadSteps == 15) {
                            helpToNetworkGoToTargetOneAxis(scene);
                            countBadSteps = 0;
                            System.err.println("****************HELP******************");
                        }
                        else {
                            // Помощь в выборе следующего пути (ОЧЕНЬ ВАЖЕН!)
                            if (countBadSteps >= 10) {
                                newDirection = scene.findOptimisticDirectionForCurrent();
                                scene.setCurrentDirection(newDirection);
                            }
                        }
                        countBadSteps++;
                    }
                    //**********************************************************************

//                    newDirection = scene.findOptimisticDirectionForCurrent();
//                    scene.setCurrentDirection(newDirection);
//


//                    nextPosition = NewPositionCalculate.getNextPosition(currentPosition.clone(), newDirection);
//                    resultDirection = isPositionCloserToTargetPosition(currentPosition, nextPosition, targetPosition, newDirection);
//                    System.out.println(resultDirection);


//
//                    if (resultDirection == false)
//                        helpToNetworkGoToTargetOneAxis(scene);

//                    System.out.println(getStateForDirection(currentPosition, targetPosition, Direction.UP));
//                    System.out.println(getStateForDirection(currentPosition, targetPosition, Direction.DOWN));
//                    System.out.println(getStateForDirection(currentPosition, targetPosition, Direction.LEFT));
//                    System.out.println(getStateForDirection(currentPosition, targetPosition, Direction.RIGHT));
//                    System.out.println(getStateForDirection(currentPosition, targetPosition, Direction.DEEP));
//                    System.out.println(getStateForDirection(currentPosition, targetPosition, Direction.OUTSIDE));

//                    System.out.println("Target:");
//                    scene.getTargetPosition().displayXYZ();
//                    System.out.println("Current:");
//                    scene.getCurrentPosition().displayXYZ();
//                    scene.getCurrentPosition().displayRTF();
//                    scene.displayDirection();
//                    System.out.println("Distance: " + scene.getTargetPosition().distance(scene.getCurrentPosition()));

                    //System.out.println("********************************");
                    //*************************************

                    if (scene.move())
                        successIterations++;

                    // Needed so that we can see easier what is the scene doing
                    NetworkUtil.waitMs(0);
                } catch (final Exception e) {
                    LOG.error(e.getMessage(), e);
                    Thread.currentThread().interrupt();
                    scene.endGame();
                }
            }

            // Обнуляем значения
            scene.initializeScene();
        }
        LOG.info("Finished evaluation of the network!");

        System.out.println("\n\n*** Success work: " + (double) successIterations / countIterations + " ***");
        System.out.println("*** Count loop: " + scene.getLoop() + " ***");

    }


    // Помощь Нейронной Сети в достижении случайной одной нужной координаты X, Y, Z
    public void helpToNetworkGoToTargetOneAxis(Scene scene) {
        int numberAxis = (int) (-1 +  Math.ceil(Math.random() * 3));

        switch (numberAxis) {
            // Ось X
            case 0: {
                if (scene.getCurrentPosition().getX() < scene.getTargetPosition().getX())
                {
                    while (scene.getCurrentPosition().countXIsEqual(scene.getCurrentPosition(), 1) != true)
                    {
                        Direction myDirection = Direction.RIGHT;
                        scene.setCurrentDirection(myDirection);
                        scene.move();
                    }
                }
                else
                {
                    if (scene.getCurrentPosition().getX() > scene.getTargetPosition().getX())
                    {
                        while (scene.getCurrentPosition().countXIsEqual(scene.getCurrentPosition(), 1) != true)
                        {
                            Direction myDirection = Direction.LEFT;
                            scene.setCurrentDirection(myDirection);
                            scene.move();
                        }
                    }
                }
                break;
            }

            // Ось Y
            case 1: {
                if (scene.getCurrentPosition().getY() < scene.getTargetPosition().getY())
                {
                    while (scene.getCurrentPosition().countYIsEqual(scene.getCurrentPosition(), 1) != true)
                    {
                        Direction myDirection = Direction.UP;
                        scene.setCurrentDirection(myDirection);
                        scene.move();
                    }
                }
                else
                {
                    if (scene.getCurrentPosition().getY() > scene.getTargetPosition().getY())
                    {
                        while (scene.getCurrentPosition().countYIsEqual(scene.getCurrentPosition(), 1) != true)
                        {
                            Direction myDirection = Direction.DOWN;
                            scene.setCurrentDirection(myDirection);
                            scene.move();
                        }
                    }
                }
                break;
            }

            // Ось Z
            case 2: {
                if (scene.getCurrentPosition().getZ() < scene.getTargetPosition().getZ())
                {
                    while (scene.getCurrentPosition().countZIsEqual(scene.getCurrentPosition(), 1) != true)
                    {
                        Direction myDirection = Direction.DEEP;
                        scene.setCurrentDirection(myDirection);
                        scene.move();
                    }
                }
                else
                {
                    if (scene.getCurrentPosition().getZ() > scene.getTargetPosition().getZ())
                    {
                        while (scene.getCurrentPosition().countZIsEqual(scene.getCurrentPosition(), 1) != true)
                        {
                            Direction myDirection = Direction.OUTSIDE;
                            scene.setCurrentDirection(myDirection);
                            scene.move();
                        }
                    }
                }
                break;
            }

            default: {
                break;
            }
        }
    }


    public static void main(String[] args) {
        final Scene scene = new Scene();
        final String networkName = "NetworkSceneXYZ" + ".zip";

        SceneDl4j sceneDl4j = new SceneDl4j();
        //sceneDl4j.work(scene, networkName);

        System.out.println("\n\n\n");
        System.out.println("================Start true work!=================");
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        sceneDl4j.evaluateNetwork(scene, networkName);
        System.out.println("===================End work!=====================");
    }
}
