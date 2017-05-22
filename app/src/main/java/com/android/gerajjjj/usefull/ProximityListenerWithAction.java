package com.android.gerajjjj.usefull;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * React on proximity change
 *
 * Created by Gergely on 5/22/2017.
 */

public class ProximityListenerWithAction implements SensorEventListener {
    private ActionInterface action;
    private boolean playing;
    public ProximityListenerWithAction(ActionInterface action){
        this.action = action;
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if ((event.sensor.getType() == Sensor.TYPE_PROXIMITY)){
            if (event.values[0] < event.sensor.getMaximumRange()) {
                if (!playing) {
                    action.start(this);
                    playing = true;
                }
            } else {
                if (playing) {
                    action.pause(this);
                    playing = false;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }
}
