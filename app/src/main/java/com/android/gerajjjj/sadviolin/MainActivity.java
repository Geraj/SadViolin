package com.android.gerajjjj.sadviolin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.android.gerajjjj.usefull.AccelometerListenerWithAction;
import com.android.gerajjjj.usefull.PlayBackAction;
import com.android.gerajjjj.usefull.ProximityListenerWithAction;
import com.android.gerajjjj.usefull.TouchWithAction;

/**
 * Main activity of app, listeners with action are added and maintained her
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Sad-Violin";

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mProximitySensor;
    private AccelometerListenerWithAction mAccelometerSensorListener;
    private ProximityListenerWithAction mProximitySensorListener;
    private TouchWithAction mTouchWithAction;
    private MediaPlayer mMediaPlayer;
    private ImageView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mView = (ImageView) findViewById(R.id.imageView);
        mView.setImageDrawable(getResources().getDrawable(R.drawable.violintrans2));
        mView.setBackgroundColor(Color.BLACK);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Toast.makeText(MainActivity.this,"YeeeY AWS", Toast.LENGTH_LONG).show();
            }
        }).execute();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getActionBar() != null)
            getActionBar().hide();
        //createSoundPool();
        mMediaPlayer = MediaPlayer.create(this, R.raw.sadviolin);
        PlayBackAction playBackAction = new PlayBackAction(mMediaPlayer);
        mAccelometerSensorListener = new AccelometerListenerWithAction(playBackAction);
        mProximitySensorListener = new ProximityListenerWithAction(playBackAction);
        mTouchWithAction = new TouchWithAction(playBackAction);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                boolean reactOnAcceleration = sharedPref.getBoolean("checkbox_acceleration", true);
                boolean reactOnTouch = sharedPref.getBoolean("checkbox_touch", true);
                boolean reactOnProxy = sharedPref.getBoolean("checkbox_proxy", true);
                String test = sharedPref.getString("test", "none");
                sharedPref.edit().putString("test", "newValue").commit();
                if (reactOnProxy) {
                    mSensorManager.registerListener(mProximitySensorListener, mProximitySensor, SensorManager.SENSOR_DELAY_UI);
                }
                if (reactOnAcceleration) {
                    mSensorManager.registerListener(mAccelometerSensorListener, mAccelerometer,
                            SensorManager.SENSOR_DELAY_UI);
                }
                if (reactOnTouch) {
                    mView.setOnTouchListener(mTouchWithAction);
                }
                if (!reactOnTouch && !reactOnAcceleration && !reactOnProxy) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.no_actions), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onPause() {
        mMediaPlayer.release();
        mSensorManager.unregisterListener(mAccelometerSensorListener);
        mSensorManager.unregisterListener(mProximitySensorListener);
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
