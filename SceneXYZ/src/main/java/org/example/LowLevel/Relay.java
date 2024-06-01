package org.example.LowLevel;

/**
 * Класс РЕЛЕ
 * Хранит номер пина реле
 */
public class Relay {

    /**
     * Порт реле
     */
    protected int port;

    /**
     * Реле
     * @param port - порт управления реле
     */
    public Relay(int port) {
        this.port = port;
    }

    /**
     * Вернуть номер порта
     * @return - номер порта
     */
    public int getPort() {
        return port;
    }

    /**
     * Установить номер порта
     * @param port - номер порта
     */
    public void setPort(int port) {
        this.port = port;
    }
}
