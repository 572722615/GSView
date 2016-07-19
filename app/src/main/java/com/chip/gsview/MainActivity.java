package com.chip.gsview;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean debug = true;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TextView textviewX;
    private TextView textviewY;
    private TextView textviewZ;
    private TextView textviewF;
    private ImageView back;
    private TextView title;

    WindowManager wm;
    int screenWidth;


    //int fx;
    boolean isTextAnim;
    boolean isBackAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();


        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// TYPE_GRAVITY
        if (null == mSensorManager) {
            Log.d(TAG, "deveice not support SensorManager");
        }
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);// SENSOR_DELAY_GAME
    }

    private void initView() {
        textviewX = (TextView) findViewById(R.id.textView);
        textviewY = (TextView) findViewById(R.id.textView3);
        textviewZ = (TextView) findViewById(R.id.textView4);
        textviewF = (TextView) findViewById(R.id.textView2);
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        if (debug) {
            textviewX.setVisibility(View.VISIBLE);
            textviewY.setVisibility(View.VISIBLE);
            textviewZ.setVisibility(View.VISIBLE);
            textviewF.setVisibility(View.VISIBLE);
        } else {
            textviewX.setVisibility(View.GONE);
            textviewY.setVisibility(View.GONE);
            textviewZ.setVisibility(View.GONE);
            textviewF.setVisibility(View.GONE);
        }

        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        screenWidth = wm.getDefaultDisplay().getWidth();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor == null) {
            return;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            int x = (int) event.values[0];
            int y = (int) event.values[1];
            int z = (int) event.values[2];

            textviewX.setText("x:" + String.valueOf(x));
            textviewY.setText("y:" + String.valueOf(y));
            textviewZ.setText("z:" + String.valueOf(z));



            if (x <= -3) {
                verticalRun(title, (screenWidth -title.getMeasuredWidth())/2);
                verticalRun(back, screenWidth-title.getMeasuredWidth()-back.getMeasuredWidth());
                textviewF.setText("向右");

            } else if (x >= 3) {
                textviewF.setText("向左");
                verticalRun(back, 0);
                verticalRun(title, - ((screenWidth -title.getMeasuredWidth())/2)+back.getMeasuredWidth());

            } else {
                textviewF.setText("平躺");
                verticalRun(back, 0);
                verticalRun(title, 0);
                isTextAnim = false;
                isBackAnim = false;
            }
            /*if (x <= -3) {
                fx -= x;
                textviewF.setText("向右");
                verticalRun(back, 2 * fx);

            } else if (x >= 3) {
                fx -= x;
                textviewF.setText("向左");
                verticalRun(back, 2 * fx);

            } else {
                textviewF.setText("平躺");
                verticalRun(back, 0);
                fx = 0;
            }*/


        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void verticalRun(final View view, final int x) {
        if (!isBackAnim || !isTextAnim) {
            if (view.equals(title)) {
                isTextAnim = true;
            } else if (view.equals(back)) {
                isBackAnim = true;
            }
            ValueAnimator animator;
            animator = ValueAnimator.ofFloat(x);
//        animator = ValueAnimator.ofFloat(x, x);
            animator.setTarget(view);
            animator.setDuration(1000).start();
//      animator.setInterpolator(value)
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    view.setTranslationX((Float) animation.getAnimatedValue());
                }
            });
        } else {
            return;
        }


    }

}
