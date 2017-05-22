package com.android.gerajjjj.usefull;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

/**
 * Listens to sensor events and starts/stops/pauses the action based on sensor values
 *
 * Created by Gerajjjj on 1/28/2017.
 */

public class SensorListenerWithAction implements SensorEventListener {

    private final float mAlpha = 0.8f;
    // Arrays for storing filtered values
    private float[] mGravity = new float[3];
    private float[] mAccel = new float[3];
    private long mLastUpdate, mPreviousActionCount;

    private static final int MAX_VALID_ACTION_COUNT = 10;
    private static final int START_ACTION_COUNT = -5;
    private static final int START_SONG_ACTION_COUNT = 8;
    private static final int STOP_SONG_ACTION_COUNT = 6;
    private static final int STOP_UPDATING_ACTION_COUNT = 4;
    private static final float CONSIDERED_ACTION = 0.5f;
    private static final String TAG = "SensorListenerWithAction";

    private ActionInterface actinToPerform;

    /**
     *
     * @param action - action to perform
     */
    public SensorListenerWithAction(ActionInterface action){
        actinToPerform = action;
        mPreviousActionCount = START_ACTION_COUNT;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long actualTime = System.currentTimeMillis();
            if (actualTime - mLastUpdate > 100) {
                mLastUpdate = actualTime;

                float rawX = event.values[0];
                float rawY = event.values[1];
                float rawZ = event.values[2];
                // Apply low-pass filter
                mGravity[0] = lowPass(rawX, mGravity[0]);
                mGravity[1] = lowPass(rawY, mGravity[1]);
                mGravity[2] = lowPass(rawZ, mGravity[2]);
                mAccel[0] = highPass(rawX, mGravity[0]);
                mAccel[1] = highPass(rawY, mGravity[1]);
                mAccel[2] = highPass(rawZ, mGravity[2]);

                boolean isAction = Math.abs(mAccel[0]) + Math.abs(mAccel[1]) + Math.abs(mAccel[2]) > CONSIDERED_ACTION;
                Log.i(TAG, "Action value: " +  Math.abs(mAccel[0])+Math.abs(mAccel[1])+ Math.abs(mAccel[2]) + " " +  mAccel[0] + " " + mAccel[1] + " " + mAccel[2] );
                //take into acount the previous actions
                if (isAction) {
                    if (mPreviousActionCount <= MAX_VALID_ACTION_COUNT) {
                        mPreviousActionCount++;
                    }
                } else {
                    if (mPreviousActionCount > 0) {
                        mPreviousActionCount--;
                    }
                }
                if (isAction) {
                    if (mPreviousActionCount > START_SONG_ACTION_COUNT) {
                        Log.i(TAG, "Start " + mLastUpdate);
                        //mSoundPool.play(mSoundID, mStreamVolume, mStreamVolume, 10, -1, 1);
                        actinToPerform.start(this);
                    }
                }
                if (!isAction  && mPreviousActionCount> STOP_UPDATING_ACTION_COUNT && mPreviousActionCount < STOP_SONG_ACTION_COUNT) {
                    Log.i(TAG, "Pause " + mLastUpdate);
                    actinToPerform.pause(this);
                    //mSoundPool.pause(mSoundID);
                    //isPlaying = false;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // DO NOTHING
    }

    // Deemphasize constant forces
    private float highPass(float current, float gravity) {
        return current - gravity;
    }

    // Deemphasize transient forces
    private float lowPass(float current, float gravity) {
        return gravity * mAlpha + current * (1 - mAlpha);
    }
}