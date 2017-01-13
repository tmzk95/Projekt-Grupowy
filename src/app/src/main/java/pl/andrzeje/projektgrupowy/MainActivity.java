package pl.andrzeje.projektgrupowy;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button listOfSensors = (Button) findViewById(R.id.ListOfSensorsButton);
        Button proximitySensor = (Button) findViewById(R.id.ProximitySensorButton);
        Button orientationSensor = (Button) findViewById(R.id.OrientationSensorButton);

        listOfSensors.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                context = getApplicationContext();
                Intent intent = new Intent(context, Stats.class);
                startActivity(intent);
            }
        });

        proximitySensor.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                context = getApplicationContext();
                Intent intent = new Intent(context, NewPlaces.class);
                startActivity(intent);
            }
        });

        orientationSensor.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                context = getApplicationContext();
                Intent intent = new Intent(context, Map.class);
                startActivity(intent);
            }
        });

    }
}