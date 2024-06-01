package org.example.network.util;

import org.example.scene.helper.Direction;
import org.example.scene.helper.Position;
import org.example.scene.util.NewPositionCalculate;

/**
 * Класс с вспомогательными методами
 */
public final class SceneStateUtil {

    private SceneStateUtil() {}

    /**
     * Рассчитайте состояние для заданного направления.
     * В зависимости от направления мы проверяем, может ли текущее положение
     * двигаться в этом направлении, а также есть ли в этом направлении желаемая позиция.
     * -1,0 означает, нельзя идти в этом направлении,
     * 1,0 означает, что желаемая позиция находится в этом направлении, а 0,0 означает,
     * что мы можем идти этим путем, желаемого положения нет.
     *
     * @param currentPosition   - Текущая позиция.
     * @param targetPosition    - Желаемая позиция.
     * @param newDirection      - Новое направление.
     * @return                  - Подходит ли нам новое направление.
     */
    public static double getStateForDirection(final Position currentPosition,
                                              final Position targetPosition,
                                              final Direction newDirection) {

        final Position nextPosition = NewPositionCalculate.getNextPosition(currentPosition.clone(), newDirection);

        if (isHeadUnableToMoveToNextPosition(nextPosition)) {
            return -1.0;
        }

        if (newDirection == Direction.UP) {
            if (targetPosition.distance(currentPosition) > targetPosition.distance(nextPosition)) {
                return 1.0;
            }

            return 0.0;
        }

        if (newDirection == Direction.RIGHT) {
            if (targetPosition.distance(currentPosition) > targetPosition.distance(nextPosition)) {
                return 1.0;
            }

            return 0.0;
        }

        if (newDirection == Direction.DOWN) {
            if (targetPosition.distance(currentPosition) > targetPosition.distance(nextPosition)) {
                return 1.0;
            }

            return 0.0;
        }

        if (newDirection == Direction.LEFT) {
            if (targetPosition.distance(currentPosition) > targetPosition.distance(nextPosition)) {
                return 1.0;
            }

            return 0.0;
        }

        if (newDirection == Direction.DEEP) {
            if (targetPosition.distance(currentPosition) > targetPosition.distance(nextPosition)) {
                return 1.0;
            }

            return 0.0;
        }

        if (newDirection == Direction.OUTSIDE) {
            if (targetPosition.distance(currentPosition) > targetPosition.distance(nextPosition)) {
                return 1.0;
            }

            return 0.0;
        }

        return 0.0;
    }
    /*
    public static double getStateForDirection(final Position currentPosition,
                                              final Position targetPosition,
                                              final Direction newDirection) {

        final Position nextPosition = NewPositionCalculate.getNextPosition(currentPosition.clone(), newDirection);

        if (isHeadUnableToMoveToNextPosition(nextPosition)) {
            return -1.0;
        }

        if (newDirection == Direction.UP) {
            if (currentPosition.getY() < targetPosition.getY()) {
                return 1.0;
            }

            return 0.0;
        }

        if (newDirection == Direction.RIGHT) {
            if (currentPosition.getX() < targetPosition.getX()) {
                return 1.0;
            }

            return 0.0;
        }

        if (newDirection == Direction.DOWN) {
            if (currentPosition.getY() > targetPosition.getY()) {
                return 1.0;
            }

            return 0.0;
        }

        if (newDirection == Direction.LEFT) {
            if (currentPosition.getX() > targetPosition.getX()) {
                return 1.0;
            }

            return 0.0;
        }

        if (newDirection == Direction.DEEP) {
            if (currentPosition.getZ() > targetPosition.getZ()) {
                return 1.0;
            }

            return 0.0;
        }

        if (newDirection == Direction.OUTSIDE) {
            if (currentPosition.getZ() < targetPosition.getZ()) {
                return 1.0;
            }

            return 0.0;
        }

        return 0.0;
    }
    */

    /**
     * Получить индекс максимального положения в массиве
     *
     * @param values    - значения массива.
     * @return          - вернуть найденные значения.
     */
    public static int getMaxValueIndex(final double[] values) {
        int maxAt = 0;

        for (int i = 0; i < values.length; i++) {
            maxAt = values[i] > values[maxAt] ? i : maxAt;
        }

        return maxAt;
    }

    /**
     * Проверка на выход за пределы
     * @param nextPosition - следующая позиция
     * @return - вышла ли за пределы
     */
    private static boolean isHeadUnableToMoveToNextPosition(final Position nextPosition) {
        return nextPosition.isOutsideBounds();
    }
}
