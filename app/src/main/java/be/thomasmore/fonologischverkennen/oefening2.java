package be.thomasmore.fonologischverkennen;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class oefening2 extends AppCompatActivity {


    private MediaPlayer mp;

    private ImageView textViewAfbeelding1;
    private TextView woordtraining;
    private Button volgende;
    private ImageView down;
    private ImageView up;
    private List<String> listWoordden = new ArrayList<>();
    private int teller = 0;
    private int hoeveelAntwoorden = 0;
    private String bepaaldWoord;
    private String woorden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oefening2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Random r = new Random();

        goedoffout = r.nextInt(2)+0;
        Log.i("goedoffout",String.valueOf(goedoffout));

        listWoordden.add("klimtouw");
        listWoordden.add("kompas");
        Bundle bundle = getIntent().getExtras();
        woorden = bundle.getString("woorden");
        Log.i("woordenoefmartijn",woorden);
        bepaaldWoord = listWoordden.get(teller);
        Log.i("bepaaldwxoord",bepaaldWoord);

        //Dit is de code die de string opvraagt van vorige schermen en naar list omzet
//        listWoordden = Arrays.asList(woorden.split("\\s*-\\s*"));
//        Log.i("woordenoefmartijn",listWoordden.toString());

        //afbeelding woord disable
        textViewAfbeelding1 = (ImageView) findViewById(R.id.linearlayoutAfbeelding1);
        textViewAfbeelding1.setEnabled(false);

        volgende = (Button) findViewById(R.id.buttonvolgendwoord);
        volgende.setVisibility(View.INVISIBLE);
        up = (ImageView) findViewById(R.id.up);
        up.setVisibility(View.INVISIBLE);
        down = (ImageView) findViewById(R.id.down);
        down.setVisibility(View.INVISIBLE);

        klickAfbeelding();
        klickVolgende();
        beginOefening();
    }

    // 1 Wat zien 	we hier? Druk op de foto en ik vertel je alles 	over het klimtouw.
    private void beginOefening() {
        hoeveelAntwoorden =0;
        bepaaldWoord = listWoordden.get(teller);
        Log.i("bepaaldwxoord",bepaaldWoord);
        textViewAfbeelding1 = (ImageView) findViewById(R.id.linearlayoutAfbeelding1);
        textViewAfbeelding1.setImageResource(getResources().getIdentifier(bepaaldWoord, "drawable", getPackageName()));

        woordtraining = (TextView) findViewById(R.id.woordlabel);
        woordtraining.setText(bepaaldWoord);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                watZienWeHier();
                textViewAfbeelding1.setEnabled(true);
            }
        },1000);
    }



    private void klickAfbeelding() {
        textViewAfbeelding1 = (ImageView) findViewById(R.id.linearlayoutAfbeelding1);
        textViewAfbeelding1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weetJijWatIs();
            }
        });
    }
    private void klickVolgende() {
        volgende = (Button) findViewById(R.id.buttonvolgendwoord);
        volgende.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volgendeknop();
            }
        });
    }

    private void weetJijWatIs() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                infoWoord();
            }
        },500);
    }

    private void tussenDelay() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                drukOpDeFoto();
            }
        },3000);
    }

    // SPRAAK 1 Wat zien 	we hier? Druk op de foto en ik vertel je alles 	over het klimtouw.
    private void watZienWeHier() {
        mp = MediaPlayer.create(this, getResources().getIdentifier("watzienwehier", "raw", getPackageName()));
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                drukOpDeFoto();
            }
        });
    }

    private void drukOpDeFoto() {
        mp = MediaPlayer.create(this, getResources().getIdentifier("vertel"+bepaaldWoord, "raw", getPackageName()));
        mp.start();
    }
    private int goedoffout;
    private boolean goed;

    private void volgendeknop() {
        mp = MediaPlayer.create(this, getResources().getIdentifier("goedslechtzin"+bepaaldWoord, "raw", getPackageName()));
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                up.setVisibility(View.VISIBLE);
                down.setVisibility(View.VISIBLE);


                if(goedoffout == 1){
                    woordgoedafspelen();
                } else {
                    woordslechtafspelen();
                }
            }
        });
    }

    private void woordgoedafspelen() {
        mp = MediaPlayer.create(this, getResources().getIdentifier("goedezin"+bepaaldWoord, "raw", getPackageName()));
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hoeveelAntwoorden++;

                        goedGedaan();
                    }
                });
                down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        probeernogeens();
                    }
                });
            }
        });
    }
    private void woordslechtafspelen() {
        mp = MediaPlayer.create(this, getResources().getIdentifier("slechtezin"+bepaaldWoord, "raw", getPackageName()));
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hoeveelAntwoorden++;
                        goedGedaan();
                    }
                });

                up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        probeernogeens();
                    }
                });
            }
        });
    }

    private void leukWoord() {
        mp = MediaPlayer.create(this, getResources().getIdentifier("leukwoord"+bepaaldWoord, "raw", getPackageName()));
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                volgende.setVisibility(View.VISIBLE);
            }
        });
    }

    private void goedGedaan() {
        if(hoeveelAntwoorden == 2){

            textViewAfbeelding1 = (ImageView) findViewById(R.id.linearlayoutAfbeelding1);
            textViewAfbeelding1.setEnabled(false);

            volgende = (Button) findViewById(R.id.buttonvolgendwoord);
            volgende.setVisibility(View.INVISIBLE);
            up = (ImageView) findViewById(R.id.up);
            up.setVisibility(View.INVISIBLE);
            down = (ImageView) findViewById(R.id.down);
            down.setVisibility(View.INVISIBLE);
            teller++;
            if(teller == 2){
                teller = 0;
            }
            beginOefening();
        } else {
            mp = MediaPlayer.create(this, getResources().getIdentifier("goedgedaan", "raw", getPackageName()));
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    if(goedoffout == 1) {
                        woordslechtafspelen();
                        } else {
                        woordgoedafspelen();

                    }
                }
            });
        }
    }
    private void probeernogeens() {
        mp = MediaPlayer.create(this, getResources().getIdentifier("probeernogeens", "raw", getPackageName()));
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {

                if(goedoffout == 1) {
                    woordslechtafspelen();
                } else {
                    woordgoedafspelen();

                }
            }
        });
    }

    private void infoWoord() {
        mp = MediaPlayer.create(this, getResources().getIdentifier("weet"+bepaaldWoord, "raw", getPackageName()));
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                leukWoord();
            }
        });
    }



}
