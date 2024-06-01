package org.example.network.util;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.rl4j.learning.configuration.QLearningConfiguration;
import org.deeplearning4j.rl4j.network.configuration.DQNDenseNetworkConfiguration;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.nd4j.linalg.learning.config.RmsProp;

import java.io.File;
import java.io.IOException;

/**
 * ����� Util, ���������� ������ ��� ���������� ��������� ���� � �� ���������.
 *
 */
public final class NetworkUtil {
    /**
     * ���������� ������ ��������� ����.
     */
    public static final int NUMBER_OF_INPUTS = 6;
    /**
     * ���������� �������� ���������� (��������, ����� ����� -1, ������ �� ���������� 0, ����������� � ��� 1)
     */
    public static final double LOW_VALUE = -1;
    /**
     * ��������� �������� ���������� (��������, ����� ����� -1, ������ �� ���������� 0, ����������� � ��� 1)
     */
    public static final double HIGH_VALUE = 1;

    /**
     * ����������� ��-���������
     */
    private NetworkUtil() {}

    /**
     * ������������ ��������� ����
     * @return - ������������������ ��������� ����
     */
    public static QLearningConfiguration buildConfig() {
        return QLearningConfiguration.builder()
                .seed(123L)
                .maxEpochStep(300)         //300
                .maxStep(35000)             //15000
                .expRepMaxSize(150000)
                .batchSize(128)
                .targetDqnUpdateFreq(500)
                .updateStart(10)
                .rewardFactor(0.01)
                .gamma(0.99)
                .errorClamp(1.0)
                .minEpsilon(0.1f)
                .epsilonNbStep(1000)
                .doubleDQN(true)
                .build();
    }

    /**
     * �������������� ������������
     * @return - ������� DNQ
     */
    public static DQNFactoryStdDense buildDQNFactory() {
        final DQNDenseNetworkConfiguration build = DQNDenseNetworkConfiguration.builder()
                .l2(0.001)
                .updater(new RmsProp(0.000025))
                .numHiddenNodes(300)
                .numLayers(2)
                .build();

        return new DQNFactoryStdDense(build);
    }

    /**
     * �������� ������������ ��������� ����
     * @param networkName - �������� �����
     * @return - �������� ��������� ����
     */
    public static MultiLayerNetwork loadNetwork(final String networkName) {
        try {
            return MultiLayerNetwork.load(new File(networkName), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * ������������ ��� ���������� ���� ����, ����� ������������ ��� ������, ��� ����������.
     *
     * @param ms ���������� �����������, � ������� �������� ����� ������ ���������� � ������ ��������.
     */
    public static void waitMs(final long ms) {
        if (ms == 0) {
            return;
        }

        try {
            Thread.sleep(ms);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
