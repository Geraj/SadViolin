package com.android.gerajjjj.usefull;

/**
 * An action that can be performed
 * Created by Gerajjjj on 1/28/2017.
 */

public interface ActionInterface {
    /**
     * returns true if the action is currently active
     */
    boolean isActive();

    /**
     *
     * starts the action
     */
    void start(Object trigger);

    /**
     * stops the action
     */
    void stop(Object trigger);

    /**
     * Pauses the action
     */
    void pause(Object trigger);
}
