package com.android.gerajjjj.usefull;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

/**
 * Reacts on touch events and invokes the action
 *
 * Created by Gerajjjj on 1/28/2017.
 */

public class TouchWithAction implements View.OnTouchListener {
    // Action to invoke
    private ActionInterface action;
    private VelocityTracker mVelocityTracker = null;
    private static final int MINIMUM_VELOCITY = 75;
    private static final int MAX_CONSECUTIVE_MINIMUM = 5;
    private int strikes = 0;
    private int actionTracked = -1;

    /**
     * Constructs an instance
     * @param action
     */
    public TouchWithAction(ActionInterface action){
        this.action = action;
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (actionTracked == -1){
            actionTracked = event.getActionIndex();
        }
        if (event.getActionIndex() == actionTracked) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    if (mVelocityTracker == null) {
                        // Retrieve a new VelocityTracker object to watch the velocity
                        // of a motion.
                        mVelocityTracker = VelocityTracker.obtain();
                    } else {
                        // Reset the velocity tracker back to its initial state.
                        mVelocityTracker.clear();
                    }
                    // Add a user's movement to the tracker.
                    mVelocityTracker.addMovement(event);
                case MotionEvent.ACTION_MOVE:
                    mVelocityTracker.addMovement(event);
                    // When you want to determine the velocity, call
                    // computeCurrentVelocity(). Then call getXVelocity()
                    // and getYVelocity() to retrieve the velocity for each pointer ID.
                    mVelocityTracker.computeCurrentVelocity(1000);
                    Log.i(PlayBackAction.class.getName(), Math.abs(mVelocityTracker.getXVelocity()) + " " + Math.abs(mVelocityTracker.getYVelocity()));
                    if (Math.abs(mVelocityTracker.getXVelocity()) + Math.abs(mVelocityTracker.getYVelocity()) < MINIMUM_VELOCITY) {
                        strikes++;
                        if (strikes > MAX_CONSECUTIVE_MINIMUM) {
                            action.pause(this);
                        }
                    } else {
                        strikes = 0;
                        action.start(this);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    actionTracked = -1;
                    action.pause(this);
            }
            return true;
        }
        return false;
    }
}
