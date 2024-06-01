package org.example.network;


import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;
import org.example.network.util.NetworkUtil;
import org.example.scene.Scene;

/**
 * Scene environment that is used to train the network.
 */
public class Environment implements MDP<SceneState, Integer, DiscreteSpace> {

    /**
     * Количество доступных действий
     */
    private final DiscreteSpace actionSpace = new DiscreteSpace(6);

    /**
     * Сцена, в которой происходят действия
     */
    private final Scene scene;

    /**
     * Конструктор со сценой
     * @param scene - сцена
     */
    public Environment(final Scene scene) {
        this.scene = scene;
    }

    /**
     * Получить наблюдаемое пространство
     * @return
     */
    @Override
    public ObservationSpace<SceneState> getObservationSpace() {
        return new SceneObservationSpace();
    }

    /**
     * Получить действие
     * @return
     */
    @Override
    public DiscreteSpace getActionSpace() {
        return actionSpace;
    }

    /**
     * перезагрузить
     * @return - сцена
     */
    @Override
    public SceneState reset() {
        return scene.initializeScene();
    }

    /**
     * Закрыть
     */
    @Override
    public void close() {}

    /**
     * Один шаг
     * @param actionIndex - номер действия
     * @return - результат
     */
    @Override
    public StepReply<SceneState> step(final Integer actionIndex) {
        // Find action based on action index
        final Action actionToTake = Action.getActionByIndex(actionIndex);

        // Change direction based on action and move the snake in that direction
        scene.changeDirection(actionToTake);
        scene.move();

        // If you want to see what is the snake doing while training increase this value
        NetworkUtil.waitMs(0);

        // Get reward
        double reward = scene.calculateRewardForActionToTake(actionToTake);

        // Get current state
        final SceneState observation = scene.buildStateObservation();

        return new StepReply<>(
                observation,
                reward,
                isDone(),
                "SceneDl4j"
        );
    }

    /**
     * Завершено или нет
     * @return - результат проверки
     */
    @Override
    public boolean isDone() {
        return !scene.isWork();
    }

    /**
     * Новый случай
     * @return - MDP
     */
    @Override
    public MDP<SceneState, Integer, DiscreteSpace> newInstance() {
        scene.initializeScene();
        return new Environment(scene);
    }
}
