package be.thomasmore.fonologischverkennen;

import android.content.ClipData;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
// SPEL VERLOOP :
// _resetVorigeRonde_0
// _maakVolgendeVraag_1
// _toonVolgendeVraag_2
// _startRonde_3
// _speelVerder_4

public class TrainingOef1bActivity extends AppCompatActivity {
    private MediaPlayer mp = new MediaPlayer();
    private MediaPlayer mp2;
    String groep;

    List<Woord> woordenMetEigenschappen;
    int vragenRondeTeller, aantalVragenRondes, woordenTeller = 0; // er zijn 1 tot X rondes | er zijn X-aantal rondes | er 4 woorden per ronde

    Woord huidigWoord; // van 1 ronde
    String huidigFoutAntwoord; // van 1 ronde
    List<String> huidigeMogelijkeAntwoorden; // van 1 ronde
    boolean antwoordenVanSpelerZijnCorrect = true; // van 1 ronde

    ImageView background, skipButton, nextButton;
    TextView tv1, tv2, tv3, tv4, tvTarget1, tvTarget2, tvTarget3, huidigTarget;
    LinearLayout dragTarget;

    int dragTeller = 0; // aantal success-drag&drops bijhouden
    boolean dragable = true; // om drag en drop uit te schakelen na 3 drags via de dragTeller

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_oef1b);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        groep = "B";
        _fillListWoordenMetEigenschappen(); // in deze functie wordt ook de eerste vraag getriggerd

        // typeCasting
        background = (ImageView) findViewById(R.id.background);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        tvTarget1 = (TextView) findViewById(R.id.tvTarget1);
        tvTarget2 = (TextView) findViewById(R.id.tvTarget2);
        tvTarget3 = (TextView) findViewById(R.id.tvTarget3);
        dragTarget = (LinearLayout) findViewById(R.id.dragTarget);
        skipButton = (ImageView) findViewById(R.id.skipButton);
        nextButton = (ImageView) findViewById(R.id.nextButton);

        // Listeners
        dragTarget.setOnDragListener(dragListener);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipButton.setVisibility(View.GONE);
                mp.reset();
                _speelVerder_4();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _resetVorigeRonde_0();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mp != null) {
            mp.stop();
            mp.reset();
            mp.release();
        }
        if (mp2 != null) {
            mp2.stop();
            mp2.reset();
            mp2.release();
        }
        finish();
    }

    //PROGRAMMA
    private void _fillListWoordenMetEigenschappen() {
        HttpReader httpReader = new HttpReader();
        httpReader.setOnResultReadyListener(new HttpReader.OnResultReadyListener() {
            @Override
            public void resultReady(String result) {
                JsonHelper jsonHelper = new JsonHelper();
                woordenMetEigenschappen = jsonHelper.getWoordenMetEigenschappen(result);
                aantalVragenRondes = woordenMetEigenschappen.size();
                play("start_oef1");

                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        //indien er "10" rondes zijn geweest, terug gaan naar de hoofdActivity van training
                        _maakVolgendeVraag_1();
                    }
                });


            }
        });
        httpReader.execute("http://axelpauwels.onthewifi.com/android/index.php/woord/getWoordenMetEigenschappen/" + groep);
    }

    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadow = new View.DragShadowBuilder(view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                view.startDragAndDrop(data, shadow, null, 0);
            } else {
                view.startDrag(data, shadow, view, 0);
            }
            return true;
        }
    };

    View.OnDragListener dragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int dragEvent = event.getAction();
            final View view = (View) event.getLocalState();
            String text;

            huidigTarget = tvTarget1;
            text = view.getTag().toString();

            // bepalen welk het target is (1,2 of 3) aan de hand van de dragTeller die hieronder geset wordt in dragevent "DROP"
            switch (dragTeller + 1) {
                case 2:
                    huidigTarget = tvTarget2;
                    break;
                case 3:
                    huidigTarget = tvTarget3;
                    break;
                default:
                    break;
            }

            // indien het dragable is (nog geen 3 keer gedropt) de dragevents uitvoeren
            if (dragable) {
                switch (dragEvent) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        huidigTarget.setText(text);
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        huidigTarget.setText("");
                        break;
                    case DragEvent.ACTION_DROP:
                        if (text.equals(huidigFoutAntwoord)) {
                            antwoordenVanSpelerZijnCorrect = false;
                        }
                        mp.reset();
                        huidigTarget.setText(text);
                        view.setVisibility(View.INVISIBLE);

                        dragTeller++;
                        if (dragTeller == 3) {
                            dragable = false;
                            skipButton.setVisibility(View.GONE);

                            if (antwoordenVanSpelerZijnCorrect) {
                                if (vragenRondeTeller == aantalVragenRondes) {
                                    play("goed_gedaan_andere_oefening");
                                } else {
                                    play("goed_gedaan_volgende");
                                }
                            } else {
                                play("goed_gedaan_opnieuw");
                            }
                            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    //indien er "10" rondes zijn geweest, terug gaan naar de hoofdActivity van training
                                    if (vragenRondeTeller == aantalVragenRondes) {
                                        finish();
                                    } else {
                                        if (!antwoordenVanSpelerZijnCorrect) {
                                            vragenRondeTeller = vragenRondeTeller - 1;
                                        }
                                        nextButton.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        } else {
                            _speelVerder_4();
                        }
                    case DragEvent.ACTION_DRAG_ENDED:
                        break;
                    default:
                        break;
                }
            }
            return true;
        }
    };

    private void _resetVorigeRonde_0() {
        tv1.setVisibility(View.VISIBLE);
        tv2.setVisibility(View.VISIBLE);
        tv3.setVisibility(View.VISIBLE);
        tv4.setVisibility(View.VISIBLE);

        tv1.setText("");
        tv2.setText("");
        tv3.setText("");
        tv4.setText("");

        tv1.setBackgroundResource(R.color.colorTransparant);
        tv2.setBackgroundResource(R.color.colorTransparant);
        tv3.setBackgroundResource(R.color.colorTransparant);
        tv4.setBackgroundResource(R.color.colorTransparant);

        tvTarget1.setText("");
        tvTarget2.setText("");
        tvTarget3.setText("");

        tv4.setOnLongClickListener(null);

        dragable = true;
        dragTeller = 0;
        woordenTeller = 0;
        antwoordenVanSpelerZijnCorrect = true;

        _maakVolgendeVraag_1();
    }

    private void _maakVolgendeVraag_1() {
        // vragen maken
        vragenRondeTeller++;
        huidigWoord = woordenMetEigenschappen.get(vragenRondeTeller - 1);
        huidigFoutAntwoord = huidigWoord.getWoordFouteEigenschap().getNaam();

        // voeg de 3 juiste antwoorden én het fout antwoord samen aan een array en shuffle deze array
        List<String> randomStringArray = new ArrayList<String>();
        randomStringArray.add(huidigFoutAntwoord);
        for (int i = 0; i < 3; i++) {
            randomStringArray.add(huidigWoord.getWoordEigenschappen().get(i).getNaam());
        }
        Collections.shuffle(randomStringArray);
        huidigeMogelijkeAntwoorden = randomStringArray;

        _toonVolgendeVraag_2();
    }

    private void _toonVolgendeVraag_2() {
        // Views opvullen
        background.setImageResource(getResources().getIdentifier(huidigWoord.getNaam(), "drawable", getPackageName()));
        background.setTag(huidigWoord.getNaam());

        tv1.setText(huidigeMogelijkeAntwoorden.get(0));
        tv1.setTag(huidigeMogelijkeAntwoorden.get(0));
        tv2.setText(huidigeMogelijkeAntwoorden.get(1));
        tv2.setTag(huidigeMogelijkeAntwoorden.get(1));
        tv3.setText(huidigeMogelijkeAntwoorden.get(2));
        tv3.setTag(huidigeMogelijkeAntwoorden.get(2));
        tv4.setText(huidigeMogelijkeAntwoorden.get(3));
        tv4.setTag(huidigeMogelijkeAntwoorden.get(3));

        _startRonde_3();
    }

    private void _startRonde_3() {
        skipButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        play("oef1_wat_hoort_bij");
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                play2(huidigWoord.getNaam());
                mp2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp2) {
                        nextButton.setVisibility(View.GONE);
                        skipButton.setVisibility(View.VISIBLE);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        _speelVerder_4();
                    }
                });
            }
        });
    }

    private void _speelVerder_4() {
        if (mp != null) {
            mp.reset();
        }
        if (mp2 != null) {
            mp2.reset();
        }
        woordenTeller++;
        switch (woordenTeller) {
            case 1:
                play(huidigeMogelijkeAntwoorden.get(0));
                tv1.setBackgroundResource(R.color.colorLightYellow);
                tv1.setOnLongClickListener(longClickListener);
                break;
            case 2:
                play(huidigeMogelijkeAntwoorden.get(1));
                tv1.setOnLongClickListener(null);
                tv1.setBackgroundResource(R.color.colorTransparant);
                tv2.setBackgroundResource(R.color.colorLightYellow);
                tv2.setOnLongClickListener(longClickListener);
                break;
            case 3:
                play(huidigeMogelijkeAntwoorden.get(2));
                tv2.setOnLongClickListener(null);
                tv2.setBackgroundResource(R.color.colorTransparant);
                tv3.setBackgroundResource(R.color.colorLightYellow);
                tv3.setOnLongClickListener(longClickListener);
                break;
            case 4:
                play(huidigeMogelijkeAntwoorden.get(3));
                tv3.setOnLongClickListener(null);
                tv3.setBackgroundResource(R.color.colorTransparant);
                tv4.setBackgroundResource(R.color.colorLightYellow);
                tv4.setOnLongClickListener(longClickListener);
                break;
            default:
                break;
        }
    }

    private void play(String geluidsBestandsNaam) {
        String bestandsNaam = geluidsBestandsNaam.replace(' ', '_');
        mp = MediaPlayer.create(this, getResources().getIdentifier(bestandsNaam, "raw", getPackageName()));
        mp.start();
    }

    private void play2(String geluidsBestandsNaam) {
        String bestandsNaam = geluidsBestandsNaam.replace(' ', '_');
        mp2 = MediaPlayer.create(this, getResources().getIdentifier(bestandsNaam, "raw", getPackageName()));
        mp2.start();
    }

    private void toon(String tekst) {
        Toast.makeText(getBaseContext(), tekst, Toast.LENGTH_SHORT).show();
    }

}
