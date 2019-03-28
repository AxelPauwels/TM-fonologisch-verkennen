package be.thomasmore.fonologischverkennen;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class TrainingOef1Activity extends AppCompatActivity {
    private MediaPlayer mp;
    private ImageView arrowImage;
    private ImageView textBallonImage;
    private ImageView arrowVerderImage;
    private FloatingActionButton tomsPositionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_oef1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        arrowImage = (ImageView) findViewById(R.id.arrow);
        textBallonImage = (ImageView) findViewById(R.id.textBallon);
        arrowVerderImage = (ImageView) findViewById(R.id.verderKnop);
        tomsPositionButton = (FloatingActionButton) findViewById(R.id.tomsPositionButton);
        tomsPositionButton.setEnabled(false);

        tomsPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrowImage.setVisibility(View.GONE);
                textBallonImage.setVisibility(View.VISIBLE);
                tomsPositionButton.setEnabled(false);
                playIntro3("ik_ben_tom");
            }
        });

        playIntro("intro", 4);
//        arrowVerderImage.setVisibility(View.VISIBLE);

    }

    @Override
    public void onBackPressed() {
        mp.stop();
        mp.reset();
        mp.release();
        finish();
    }

    private void play(String geluidsBestandsNaam) {
        mp = MediaPlayer.create(this, getResources().getIdentifier(geluidsBestandsNaam, "raw", getPackageName()));
        mp.start();
    }

    // geluid: intro ...
    public void playIntro(String geluidsBestandsNaam, final int delay) {
        play(geluidsBestandsNaam);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                try {
                    Thread.sleep(delay * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                playIntro2("zie_jij_tom");
            }
        });
    }

    // geluid: zie jij tom ?
    public void playIntro2(String geluidsBestandsNaam) {
        play(geluidsBestandsNaam);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                arrowImage.setVisibility(View.VISIBLE);
                tomsPositionButton.setEnabled(true);
            }
        });
    }

    // geluid: ik ben tom ...
    public void playIntro3(String geluidsBestandsNaam) {
        play(geluidsBestandsNaam);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                textBallonImage.setVisibility(View.GONE);
                arrowVerderImage.setVisibility(View.VISIBLE);
            }
        });
    }

    public void verder_click(View v) {
        arrowVerderImage.setVisibility(View.GONE);
        Intent intent_trainingOef1bActivity = new Intent(this, TrainingOef1bActivity.class);
        startActivity(intent_trainingOef1bActivity);
    }

}
