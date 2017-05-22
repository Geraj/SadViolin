package com.android.gerajjjj.sadviolin;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.gerajjjj.usefull.PlayBackAction;
import com.android.gerajjjj.usefull.SensorListenerWithAction;
import com.android.gerajjjj.usefull.TouchWithAction;

/**
 *
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Sad-Violin" ;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private SensorListenerWithAction mMySensorListener;
    private TouchWithAction mTouchWithAction;
    private MediaPlayer mMediaPlayer;
    private ImageView mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mView = (ImageView)findViewById(R.id.imageView);
        mView.setImageDrawable(getResources().getDrawable(R.drawable.violintrans2));
        mView.setBackgroundColor(Color.BLACK);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getActionBar()!=null)
            getActionBar().hide();
        //createSoundPool();
        mMediaPlayer = MediaPlayer.create(this, R.raw.sadviolin);
        PlayBackAction playBackAction = new PlayBackAction(mMediaPlayer);
        mMySensorListener  = new SensorListenerWithAction(playBackAction);
        mTouchWithAction = new TouchWithAction(playBackAction);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                boolean reactOnAcceleration = sharedPref.getBoolean("checkbox_acceleration", true);
                boolean reactOnTouch = sharedPref.getBoolean("checkbox_touch", true);
                String test = sharedPref.getString("test", "none");
                sharedPref.edit().putString("test", "newValue").commit();
                if (reactOnAcceleration) {
                    mSensorManager.registerListener(mMySensorListener, mAccelerometer,
                            SensorManager.SENSOR_DELAY_UI);
                }
                if (reactOnTouch) {
                    mView.setOnTouchListener(mTouchWithAction);
                }
                if (!reactOnTouch && !reactOnAcceleration){
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.no_actions), Toast.LENGTH_LONG).show();
                }
            }
        });



    }

//    SoundPool
//    private SoundPool mSoundPool;
//    protected void createSoundPool() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            createNewSoundPool();
//        } else {
//            createOldSoundPool();
//        }
//    }
//
//    private void createNewSoundPool() {
//        AudioAttributes attributes = new AudioAttributes.Builder()
//                .setUsage(AudioAttributes.USAGE_MEDIA)
//                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                .build();
//
//        mSoundPool = new SoundPool.Builder()
//                .setAudioAttributes(attributes)
//                .setMaxStreams(NR_OF_STREAMS)
//                .build();
//    }
//
//    @SuppressWarnings("deprecation")
//    protected void createOldSoundPool() {
//        mSoundPool = new SoundPool(NR_OF_STREAMS, AudioManager.STREAM_MUSIC, 0);
//    }

    @Override
    protected void onPause() {
        mMediaPlayer.release();
        mSensorManager.unregisterListener(mMySensorListener);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        if (settingsIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(settingsIntent);
        }
        return true;
    }



}
