package org.example.scene.util;


import org.example.network.Action;
import org.example.scene.helper.Direction;
import org.example.scene.helper.Position;

import java.util.Objects;

/**
 * ������ ������ ����������� ��������
 */
public final class RewardUtil {

    private RewardUtil() {}

    /**
     * ������������ ��� ������� �������������� �� ����������� ��������.
     *
     * @param action ����������� ��������.
     * @param currentPosition ������� ������� ����.
     * @param targetPosition ������� ������� ���.
     * @return ���������� ������������ �������� ��������������.
     */
    public static double calculateRewardForActionToTake(final Action action,
                                                        final Position currentPosition,
                                                        final Position targetPosition) {
        Direction nextDirection = Direction.UP;
        switch (action) {
            case GO_UP -> {}
            case GO_RIGHT -> nextDirection = Direction.RIGHT;
            case GO_DOWN -> nextDirection = Direction.DOWN;
            case GO_LEFT -> nextDirection = Direction.LEFT;
            case GO_DEEP -> nextDirection = Direction.DEEP;
            case GO_OUTSIDE -> nextDirection = Direction.OUTSIDE;
        }

        final Position nextPosition = NewPositionCalculate.getNextPosition(
                currentPosition.clone(),
                /*currentPosition,*/
                nextDirection
        );

        return getRewardForPosition(nextDirection, nextPosition, currentPosition, targetPosition);
    }

    /**
     * �������� ������ ��� �������
     * @param nextDirection - ��������� �����������
     * @param nextPosition - ��������� �������
     * @param currentPosition - ������� �������
     * @param targetPosition - �������� �������
     * @return - ������
     */
    private static double getRewardForPosition(final Direction nextDirection,
                                               final Position nextPosition,
                                               final Position currentPosition,
                                               final Position targetPosition) {

        if (nextPosition.isOutsideBounds()) {
//            System.out.println("Outside:");
//            System.out.println("Current: " + currentPosition.stringXYZ());
//            System.out.println("Next: " + nextPosition.stringXYZ());
//            System.out.println("Direction: " + nextDirection);
            return -100.0;
        }

        if (Objects.equals(currentPosition, nextPosition)) {
//            System.out.println("Equals:");
//            System.out.println("Current: " + currentPosition.stringXYZ());
//            System.out.println("Next: " + nextPosition.stringXYZ());
//            System.out.println("Direction: " + nextDirection);
            return -100.0;
        }

        if (nextPosition.isNear(targetPosition, 60)) {
            return 100.0;
        }

        if (NewPositionCalculate.isPositionCloserToTargetPosition(currentPosition, nextPosition, targetPosition, nextDirection)) {
            //if (nextPosition.countXYZIsEqual(targetPosition, 2) > 0)
                //return 10.0;
                //return 5.0;

            if (targetPosition.distance(nextPosition) < targetPosition.distance(currentPosition))
                return 10.0;
            else
                return -10.0;

            //return 2.0;
        }

        return -1.0;
    }
}
