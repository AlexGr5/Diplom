package org.example.scene.util;


import org.example.scene.helper.Direction;
import org.example.scene.helper.Position;

/**
 * Нахождение новой позиции
 */
public final class NewPositionCalculate {

    private NewPositionCalculate() {}

    /**
     * Считаем новую позицию
     *
     * @param currentPosition   - текущая позиция
     * @param direction         - направление
     * @return                  - новая позиция
     */
    public static Position getNextPosition(final Position currentPosition,
                                           final Direction direction) {

        Position newPosition = currentPosition.clone();
        //Position newPosition = currentPosition;

        if (direction == Direction.UP) {
            newPosition.plusOneToY();
            return newPosition;
        }

        if (direction == Direction.RIGHT) {
            newPosition.plusOneToX();
            return newPosition;
        }

        if (direction == Direction.DOWN) {
            newPosition.minusOneToY();
            return newPosition;
        }

        if (direction == Direction.DEEP) {
            //newPosition.minusOneToZ();
            newPosition.plusOneToZ();
            return newPosition;
        }

        if (direction == Direction.OUTSIDE) {
            //newPosition.plusOneToZ();
            newPosition.minusOneToZ();
            return newPosition;
        }

        // LEFT
        newPosition.minusOneToX();
        return newPosition;
    }

    /**
     * Нуждается для проверки того, находится ли перенаправленное положение ближе к цели.
     *
     * @param nextPosition      - следующая позиция.
     * @param targetPosition    - желаемая позиция.
     * @param nextDirection     - следующее направление.
     * @return                  - является ли следующее направление оптимальным.
     */
    public static boolean isPositionCloserToTargetPosition(final Position currentPosition,
                                                           final Position nextPosition,
                                                           final Position targetPosition,
                                                           final Direction nextDirection) {

        if (nextDirection == Direction.UP) {
            return targetPosition.distance(currentPosition) > targetPosition.distance(nextPosition);
        }

        if (nextDirection == Direction.RIGHT) {
            return targetPosition.distance(currentPosition) > targetPosition.distance(nextPosition);
        }

        if (nextDirection == Direction.DOWN) {
            return targetPosition.distance(currentPosition) > targetPosition.distance(nextPosition);
        }

        if (nextDirection == Direction.LEFT) {
            return targetPosition.distance(currentPosition) > targetPosition.distance(nextPosition);
        }

        if (nextDirection == Direction.DEEP) {
            return targetPosition.distance(currentPosition) > targetPosition.distance(nextPosition);
        }

        if (nextDirection == Direction.OUTSIDE) {
            return targetPosition.distance(currentPosition) > targetPosition.distance(nextPosition);
        }

        return false;
    }
    /*
    public static boolean isPositionCloserToTargetPosition(final Position nextPosition,
                                                           final Position targetPosition,
                                                           final Direction nextDirection) {
        if (nextDirection == Direction.UP) {
            return targetPosition.getY() > nextPosition.getY();
        }

        if (nextDirection == Direction.RIGHT) {
            return targetPosition.getX() > nextPosition.getX();
        }

        if (nextDirection == Direction.DOWN) {
            return targetPosition.getY() < nextPosition.getY();
        }

        if (nextDirection == Direction.LEFT) {
            return targetPosition.getX() < nextPosition.getX();
        }

        if (nextDirection == Direction.DEEP) {
            return targetPosition.getZ() < nextPosition.getZ();
        }

        if (nextDirection == Direction.OUTSIDE) {
            return targetPosition.getZ() > nextPosition.getZ();
        }

        return false;
    }
    */
}
