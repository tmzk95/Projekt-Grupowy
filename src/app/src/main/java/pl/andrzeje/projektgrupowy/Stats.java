package pl.andrzeje.projektgrupowy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

public class Stats extends AppCompatActivity {
    //komentarz
    private TextView total;
    private TextView current;
    private TextView percent;
    private TextView ocena;
    private static Integer liczbatotal=250;
    private static Integer liczbacurr=90;
    private Button back;
    private ImageView obrazek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        this.overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);  /// slide między aktywnościami

        total = (TextView) findViewById(R.id.textView1);
        current = (TextView) findViewById(R.id.textView2);
        percent = (TextView) findViewById(R.id.textView3);
        ocena = (TextView) findViewById(R.id.textView);
        total.setText("Calowita liczba punktow: "+liczbatotal.toString());
        current.setText("Liczba punktow odwiedzonych: " + liczbacurr.toString());
        Integer liczbaprocent=(liczbacurr*100)/liczbatotal;
        percent.setText("Odwiedziles " + liczbaprocent.toString() + "% punktów dostępnych w aplikacji");
        back = (Button) findViewById(R.id.button);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        obrazek= (ImageView) findViewById(R.id.imageView3);
        if(liczbaprocent<=20)
        {
            obrazek.setImageResource(R.drawable.jedna);
            ocena.setText("Za swoje dotychczasowe postępy otrzymujesz 1 gwiazdkę");
        }
        if(20<liczbaprocent&&liczbaprocent<=40)
        {
            obrazek.setImageResource(R.drawable.dwie);
            ocena.setText("Za swoje dotychczasowe postępy otrzymujesz 2 gwiazdki");
        }
        if(40<liczbaprocent&&liczbaprocent<=60)
        {
            obrazek.setImageResource(R.drawable.trzy);
            ocena.setText("Za swoje dotychczasowe postępy otrzymujesz 3 gwiazdki");
        }
        if(60<liczbaprocent&&liczbaprocent<=80)
        {
            obrazek.setImageResource(R.drawable.cztery);
            ocena.setText("Za swoje dotychczasowe postępy otrzymujesz 4 gwiazdki");
        }
        if(80<liczbaprocent)
        {
            obrazek.setImageResource(R.drawable.piec);
            ocena.setText("Za swoje dotychczasowe postępy otrzymujesz 5 gwiazdek! GRATULACJE LOLL");
        }

    }
    protected void onStop()
    {
        super.onStop();

    }
}
