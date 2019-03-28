package be.thomasmore.fonologischverkennen;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class voormeting extends AppCompatActivity {

    private TextView textViewJuisteWoord;
    private LinearLayout linearLayoutAfbeeldingen;
    private ImageView img;

    private MediaPlayer mp;

    List<String> listWoordden;
    List<String> listKlassen;
    List<String> randomWoorden = new ArrayList<>();

    private int indexJuisteWoord = 0;
    private int juisteAntwoord = 0;
    private String juisteWoord;
    private long startTime;
    private long verschil;
    private long seconden;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voormeting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nativeToolbarVerbergen();
        linearLayoutAfbeeldingen = (LinearLayout)findViewById(R.id.afbeeldingen);
        linearLayoutAfbeeldingen.setVisibility(View.INVISIBLE);

        //woorden ophalen van 1ste scherm
        String naamWoorden = getWoordenHoofdScherm();
        String naamKlassen = getKlassenHoofdScherm();

        //om arraylist met woorden op tevullen
        opvullenArrayListWoorden(naamWoorden);
        opvullenArrayListKlassen(naamKlassen);

        //start timer
        startTime = System.currentTimeMillis();
        volgendeVraag();
    }



    private void nativeToolbarVerbergen() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void buttonnaOpnieuwAfspelen_Click(View v) {
        mp.start();
    }

    private void volgendeVraag() {

        linearLayoutAfbeeldingen = (LinearLayout)findViewById(R.id.afbeeldingen);
        linearLayoutAfbeeldingen.setVisibility(View.VISIBLE);

        //Juiste woorde selecteren en tonen in label woord
        juistWoordSelecteren();

        mp = MediaPlayer.create(this, getResources().getIdentifier(juisteWoord, "raw", getPackageName()));
        mp.start();

        //random generen voor anderen woorden om scherm te tonen
        Random r = new Random();
        randomWoorden = new ArrayList<>();

        //Lijst van 4 woorden opvullen met juiste woord, nogdoen geen 2de zelfde woorde generen
        woordKeuzenGenereren(r);

        //woordlijst shuffelen voor ze tonen op het scherm
        Collections.shuffle(randomWoorden);

        schermAfbeeldingenOpvullen();
    }
    private long minutes;
    private long seconds;

    private String test = "";
    private String foutewoord = "";
    public void clickOpAfbeelding(View v) {
        mp.stop();


        //minuten best door de server laten bereken

        Log.i("***secondentussenantw", String.valueOf(seconden));

        String clickAfbeelding = v.getTag().toString();
        if(clickAfbeelding == juisteWoord){
            indexJuisteWoord++;
            juisteAntwoord++;
            Log.i("***teller", String.valueOf(indexJuisteWoord));
            if(indexJuisteWoord==listWoordden.size()){
                gaNaarHoofdScherm();
            } else {
                volgendeVraag();
            }
        } else {
            foutewoord += juisteWoord+ ",";
            Log.i("***test", foutewoord);

            indexJuisteWoord++;
            Log.i("***teller", String.valueOf(indexJuisteWoord));
            if(indexJuisteWoord==listWoordden.size()){
                gaNaarHoofdScherm();
            } else {
                volgendeVraag();
            }
        }
    }

    //om arraylist met woorden op tevullen
    private void opvullenArrayListWoorden(String naamWoorden) {
        listWoordden = Arrays.asList(naamWoorden.split("\\s*-\\s*"));
        Collections.shuffle(listWoordden);
        Log.i("***",listWoordden.toString());
    }

    private void opvullenArrayListKlassen(String naamKlassen) {
        listKlassen = Arrays.asList(naamKlassen.split("\\s*-\\s*"));
        Log.i("***listKlassen",listKlassen.toString());
    }

    //woorden ophalen van 1ste scherm
    private String getWoordenHoofdScherm() {
        Bundle bundle = getIntent().getExtras();
        return bundle.getString("woorden");
    }
    private String getKlassenHoofdScherm() {
        Bundle bundle = getIntent().getExtras();
        return bundle.getString("klassenString");
    }



    //terug naar hoofdscherm na 10 woorden geluid moet nog stop gezet worden
    private void gaNaarHoofdScherm() {
        verschil = System.currentTimeMillis() - startTime;
        seconden = verschil / 1000;
        minutes = (seconden % 3600) / 60;
        seconds = seconden % 60;
        String helper = "";
        if(seconds<9){
            helper = "0" + String.valueOf(seconds);
        } else {
            helper = String.valueOf(seconds);
        }
        String test = String.valueOf(minutes)+ String.valueOf(helper);

        Bundle bundle = new Bundle();
        Log.i("TIJD", test);
        bundle.putString("tijdInSeconden",String.valueOf(test));

        bundle.putString("totaalVragen",String.valueOf(listWoordden.size()));


        bundle.putString("foutenwoorden",foutewoord);
        bundle.putString("soortmetig","soortmetig");
        String juisteAntwoordString = String.valueOf(juisteAntwoord);
        bundle.putString("juisteAntwoorden",juisteAntwoordString);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    //Juiste woorde selecteren en tonen in label woord
    private void juistWoordSelecteren() {
        juisteWoord = listWoordden.get(indexJuisteWoord);
        textViewJuisteWoord = (TextView)findViewById(R.id.woord);
        textViewJuisteWoord.setText(String.valueOf(juisteWoord));
        Log.i("***Juistewoord",juisteWoord);
    }

    //Lijst van 4 woorden opvullen met juiste woord
    private void woordKeuzenGenereren(Random r) {
        randomWoorden.add(0,juisteWoord);//woord toevoegen
        for (int i = 1; i < 4;i++){//list random woorden aanvullen
            int randomGetal = r.nextInt(listWoordden.size());
            while(randomWoorden.contains(listWoordden.get(randomGetal))){
                randomGetal = r.nextInt(listWoordden.size());
            }
            randomWoorden.add(i,listWoordden.get(randomGetal));
        }
        Log.i("***randomWoorden",randomWoorden.toString());
    }

    private void schermAfbeeldingenOpvullen() {
        linearLayoutAfbeeldingen = (LinearLayout)findViewById(R.id.afbeeldingen);
        for (int i = 0; i < linearLayoutAfbeeldingen.getChildCount() ; i++ ) {
            img = (ImageView) findViewById(getResources().getIdentifier("afbeelding"+ String.valueOf(i), "id", getPackageName()));
            img.setImageResource(getResources().getIdentifier(randomWoorden.get(i), "drawable", getPackageName()));
            img.setTag(randomWoorden.get(i));
        }
    }
    public String woordenStringA = "";


}

