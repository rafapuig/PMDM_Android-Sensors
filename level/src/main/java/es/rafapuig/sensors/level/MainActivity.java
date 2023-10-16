package es.rafapuig.sensors.level;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    ProgressBar pbVertical;
    ProgressBar pbHorizontal;

    FrameLayout target;

    Sensor accelerometer;
    SensorManager sensorManager;

    SensorEventListener accelerometerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getUIViewsReferences();
        initSAccelerometerSensor();
    }

    private void getUIViewsReferences() {
        pbVertical = findViewById(R.id.pb_vertical);
        pbHorizontal = findViewById(R.id.pb_horizontal);
        target = findViewById(R.id.fl_target);
    }


    private void initSAccelerometerSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        accelerometerListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                processAccelerometerReading(sensorEvent.values);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }

    private static final int DELTA = 5;
    private static final int OFFSET = 100;
    private static final int SCALE_FACTOR = 10;


    private void processAccelerometerReading(float[] values) {

        int xAxis = computeAxisValue(values[0]);
        int yAxis = computeAxisValue(values[1]);

        pbHorizontal.setProgress(xAxis);
        pbVertical.setProgress(yAxis);

        updateColor();
    }

    private static int computeAxisValue(float accelerometerAxisReadingValue) {
        return (int) (accelerometerAxisReadingValue * SCALE_FACTOR + OFFSET);
    }

    private void updateColor() {
        if (isXAxisCentered() && isYAxisCentered()) {
            target.setBackgroundColor(Color.GREEN);
        } else {
            target.setBackgroundColor(Color.RED);
        }
    }

    private boolean isXAxisCentered() {
        return isProgressBarCentered(pbHorizontal);
    }

    private boolean isYAxisCentered() {
        return isProgressBarCentered(pbVertical);
    }

    private static boolean isProgressBarCentered(ProgressBar pb) {
        int max = pb.getMax();
        int min = pb.getMin();
        int axisValue = pb.getProgress();
        return isCentered(axisValue, min, max, DELTA);
    }

    private static boolean isCentered(int value, int min, int max, int delta) {
        int center = (max - min) / 2;
        return Math.abs(value - center) < delta;
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerAccelerometerListener();
    }

    private void registerAccelerometerListener() {
        sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        //unregisterAccelerometerListener();
        super.onPause();
    }

    private void unregisterAccelerometerListener() {
        sensorManager.unregisterListener(accelerometerListener);
    }
}