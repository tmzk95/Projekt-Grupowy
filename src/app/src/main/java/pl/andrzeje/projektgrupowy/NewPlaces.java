package pl.andrzeje.projektgrupowy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NewPlaces extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_places);

        this.overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left); /// slide między aktywnościami

    }
    public void onBackPressed() {
        super.onBackPressed();

    }
    public void onStop()
    {
        super.onStop();
        finish();
    }
}
