package es.rafapuig.sensors.touch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    View[] touchableViews;
    TextView tvCoords;
    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.layout_screen);

        tvCoords = findViewById(R.id.tv_coords);

        touchableViews = new View[]{
                findViewById(R.id.imageView),
                findViewById(R.id.imageView2),
                findViewById(R.id.imageView3)
        };

        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onTouchImageView(v, event);
            }
        };

        //or

        touchListener = this::onTouchImageView; // Method reference

        for (View view : touchableViews) {
            view.setOnTouchListener(touchListener);
            //view.setOnTouchListener(this::onTouchImageView); //other way
            //view.setOnTouchListener((v, event) -> this.onTouchEvent(v, event)); //other way
        }

    }

    float touchX, touchY;


    private boolean onTouchImageView(View view, MotionEvent motionEvent) {

        int action = motionEvent.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                touchX = motionEvent.getX();
                touchY = motionEvent.getY();
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_MOVE:

                int[] parentLocation = new int[2];

                ConstraintLayout layout = (ConstraintLayout) view.getParent();

                layout.getLocationOnScreen(parentLocation); //an output parameter array where coords are written

                float posX = motionEvent.getRawX() - parentLocation[0] - touchX; //view.getWidth() / 2;
                float posY = motionEvent.getRawY() - parentLocation[1] - touchY; //view.getHeight() / 2;

                view.setX(posX);
                view.setY(posY);
        }

        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        View v = findViewById(R.id.layout_screen);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                v.setBackgroundColor(Color.RED);
                break;
            case MotionEvent.ACTION_UP:
                v.setBackgroundColor(Color.WHITE);
                break;
            case MotionEvent.ACTION_MOVE:
                tvCoords.setText("(" + event.getX() + ", " + event.getY() + ")");

        }
        return super.onTouchEvent(event);
    }


}