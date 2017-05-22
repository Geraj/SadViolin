package com.android.gerajjjj.usefull;

/**
 * Created by Gerajjjj on 1/28/2017.
 */

public interface ActionInterface {
    boolean isActive();
    void start(Object trigger);
    void stop(Object trigger);
    void pause(Object trigger);
}
