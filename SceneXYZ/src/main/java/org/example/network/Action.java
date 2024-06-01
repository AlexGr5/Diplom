package org.example.network;

import java.util.List;

/**
 * Действия, которые можно совершить над позицией
 */
public enum Action {
    /**
     * Двигаться вверх
     */
    GO_UP,
    /**
     * Двигаться вправо
     */
    GO_RIGHT,
    /**
     * Двигаться вниз
     */
    GO_DOWN,
    /**
     * Двигаться влево
     */
    GO_LEFT,

    /**
     * Двигаться вглубь
     */
    GO_DEEP,

    /**
     * Двигаться наружу
     */
    GO_OUTSIDE;

    /**
     * Список действий
     */
    private static final List<Action> VALUES = List.of(values());

    /**
     * Получить действие по индексу
     *
     * @param index - индекс действия
     * @return      - действие
     */
    public static Action getActionByIndex(final int index) {
        return VALUES.get(index);
    }
}
