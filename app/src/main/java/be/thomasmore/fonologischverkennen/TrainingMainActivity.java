package be.thomasmore.fonologischverkennen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class TrainingMainActivity extends AppCompatActivity {

    private String woorden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        woorden = bundle.getString("woorden");
        Log.i("woorden",woorden);
    }

    public void training_oef1_click(View v) {
        Intent intent_trainingOef1Activity = new Intent(this, TrainingOef1Activity.class);
        startActivity(intent_trainingOef1Activity);
    }

    public void training_oef2_click(View v) {
        Intent intent_trainingOef2Activity = new Intent(this, TrainingOef2Activity.class);
        startActivity(intent_trainingOef2Activity);
    }

    public void training_oef3_click(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("woorden", woorden);

        Intent intent = new Intent(this, oefening2.class);
        intent.putExtras(bundle);

        startActivityForResult(intent, 1);

    }

}
