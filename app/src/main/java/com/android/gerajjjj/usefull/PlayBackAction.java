package com.android.gerajjjj.usefull;

import android.media.MediaPlayer;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Gerajjjj on 1/28/2017.
 */

public class PlayBackAction implements ActionInterface {
    private MediaPlayer mMediaplPlayer;
    private Set<Object> activeTriggers = new HashSet<Object>();

    public PlayBackAction(MediaPlayer mediaPlayer) {
        mMediaplPlayer = mediaPlayer;
    }

    @Override
    public boolean isActive() {
        return mMediaplPlayer.isPlaying();
    }

    @Override
    public void start(Object trigger) {
        Log.i(PlayBackAction.class.getName(), "Start action: " + activeTriggers.size() + " " + trigger.toString());
        synchronized (activeTriggers) {
            if (activeTriggers.isEmpty()) {
                mMediaplPlayer.start();
            }
            activeTriggers.add(trigger);
        }
    }

    @Override
    public void stop(Object trigger) {
        pause(trigger);
    }

    @Override
    public void pause(Object trigger) {
        Log.i(PlayBackAction.class.getName(), "Stop actoin: " + activeTriggers.size() + " " + trigger.toString());
        synchronized (activeTriggers) {
            activeTriggers.remove(trigger);
            if (activeTriggers.isEmpty() && mMediaplPlayer.isPlaying()) {
                mMediaplPlayer.pause();
            }
        }
    }
}
