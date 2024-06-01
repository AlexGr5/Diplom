package org.example.network;

import org.deeplearning4j.rl4j.space.ObservationSpace;
import org.example.network.util.NetworkUtil;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * Пространство наблюдения за сценой. Форма равна [1, 6],
 * поскольку мы наблюдаем 6 входа. Мы «смотрим» на текущее положение ВВЕРХ, ВПРАВО, ВНиЗ и ВЛЕВО.
 */
public class SceneObservationSpace implements ObservationSpace<SceneState> {
    /**
     * Минимальные значения
     */
    private static final double[] LOWS = SceneObservationSpace.createValueArray(NetworkUtil.LOW_VALUE);
    /**
     * Максимальные значения
     */
    private static final double[] HIGHS = SceneObservationSpace.createValueArray(NetworkUtil.HIGH_VALUE);

    /**
     * Вернуть имя
     * @return - имя
     */
    @Override
    public String getName() {
        return "SceneObservationSpace";
    }

    /**
     * Вернуть форму
     * @return - форма
     */
    @Override
    public int[] getShape() {
        return new int[] {
                1, NetworkUtil.NUMBER_OF_INPUTS
        };
    }

    /**
     * Вернуть минимальный
     * @return - минимальный
     */
    @Override
    public INDArray getLow() {
        return Nd4j.create(LOWS);
    }

    /**
     * Вернуть максимальный
     * @return - максимальный
     */
    @Override
    public INDArray getHigh() {
        return Nd4j.create(HIGHS);
    }

    /**
     * Создать массив значений
     * @param value - значение
     * @return - массив
     */
    private static double[] createValueArray(final double value) {
        final double[] values = new double[NetworkUtil.NUMBER_OF_INPUTS];
        for (int i = 0; i < NetworkUtil.NUMBER_OF_INPUTS; i++) {
            values[i] = value;
        }

        return values;
    }
}
