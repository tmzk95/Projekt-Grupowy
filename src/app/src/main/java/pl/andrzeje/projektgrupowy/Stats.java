package pl.andrzeje.projektgrupowy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Stats extends AppCompatActivity {
    //komentarz
    private TextView total;
    private TextView current;
    private TextView percent;
    private static Integer liczbatotal=250;
    private static Integer liczbacurr=25;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        total = (TextView) findViewById(R.id.textView1);
        current = (TextView) findViewById(R.id.textView2);
        percent = (TextView) findViewById(R.id.textView3);
        total.setText("Calowita liczba punktow: "+liczbatotal.toString());
        current.setText("Liczba punktow odwiedzonych: "+liczbacurr.toString());
        Integer liczbaprocent=(liczbacurr*100)/liczbatotal;
        percent.setText("Odwiedziles "+liczbaprocent.toString()+ " % punktów dostępnych w aplikacji");
        back = (Button) findViewById(R.id.button);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(Stats.this, MainActivity.class);
                Stats.this.startActivity(activityChangeIntent);
            }
        });
    }
}
