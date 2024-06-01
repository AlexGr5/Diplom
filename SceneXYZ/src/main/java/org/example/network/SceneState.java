package org.example.network;

import org.deeplearning4j.rl4j.space.Encodable;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.Arrays;

/**
 * Состояние сцены
 */
public class SceneState implements Encodable {
    /**
     * Входы
     */
    private final double[] inputs;

    /**
     * Конструктор с параметрами
     * @param inputs - входы
     */
    public SceneState(final double[] inputs) {
        this.inputs = inputs;
    }

    /**
     * В массив
     * @return - массив
     */
    @Override
    public double[] toArray() {
        return inputs;
    }

    /**
     * Пропустить
     * @return - false
     */
    @Override
    public boolean isSkipped() {
        return false;
    }

    /**
     * Получить данные
     * @return - массив
     */
    @Override
    public INDArray getData() {
        return Nd4j.create(inputs);
    }

    /**
     * Получить матрицу
     * @return - матрица
     */
    public INDArray getMatrix() {
        return Nd4j.create(new double[][] {
                inputs
        });
    }

    /**
     * Пусто
     * @return - null
     */
    @Override
    public Encodable dup() {
        return null;
    }

    /**
     * В строку
     * @return - строка
     */
    @Override
    public String toString() {
        return "SceneState{" +
                "inputs=" + Arrays.toString(inputs) +
                '}';
    }
}
