package es.rafapuig.sensors.acceleration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorEventListener accelerometerListener;
    SensorManager sensorManager;

    Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            processAcelerometerChange(sensorEvent.values);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }


    @Override
    protected void onResume() {
        super.onResume();
        registerSensorListener();
    }

    private void registerSensorListener() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterSensorListener();
    }

    private void unregisterSensorListener() {
        sensorManager.unregisterListener(this);
    }

    private void processAcelerometerChange(float[] values) {
        double globalAcceleration = getGlobalAcceleration(values[0], values[1], values[2]);
        setDisplayRedColorIntensity((int) (globalAcceleration * 7 % 256));
    }

    private static double getGlobalAcceleration(float x, float y, float z) {
        return Math.sqrt(x * x + y * y + z * z);
    }

    private void setDisplayRedColorIntensity(int intensity) {
        FrameLayout frameLayoutScreen = findViewById(R.id.frameLayoutScreen);
        frameLayoutScreen.setBackgroundColor(Color.rgb(intensity, 0, 0));
    }
}