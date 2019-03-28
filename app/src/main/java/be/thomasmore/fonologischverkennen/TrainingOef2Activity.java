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

public class TrainingOef2Activity extends AppCompatActivity {
    private MediaPlayer mp = new MediaPlayer();
    private MediaPlayer mp2;
    String groep;

    List<Woord> woordenMetMeerdereAfbeeldingen;
    int vragenRondeTeller, aantalVragenRondes, woordenTeller = 0; // er zijn 1 tot X rondes | er zijn X-aantal rondes | er 4 woorden per ronde

    Woord huidigWoord; // van 1 ronde
    String huidigFoutAntwoord; // van 1 ronde
    List<String> huidigeMogelijkeAntwoorden; // van 1 ronde
    boolean antwoordenVanSpelerZijnCorrect = true; // van 1 ronde

    ImageView skipButton, nextButton, iv1, iv2, iv3, iv4, ivTarget1, ivTarget2, ivTarget3, huidigTarget;
    TextView woord;
    LinearLayout dragTarget;

    int dragTeller = 0; // aantal success-drag&drops bijhouden
    boolean dragable = true; // om drag en drop uit te schakelen na 3 drags via de dragTeller

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_oef2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        groep = "A";
        _fillListWoordenMetMeerdereAfbeeldingen(); // in deze functie wordt ook de eerste vraag getriggerd

        // typeCasting
        woord = (TextView) findViewById(R.id.woord);
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        iv3 = (ImageView) findViewById(R.id.iv3);
        iv4 = (ImageView) findViewById(R.id.iv4);
        ivTarget1 = (ImageView) findViewById(R.id.ivTarget1);
        ivTarget2 = (ImageView) findViewById(R.id.ivTarget2);
        ivTarget3 = (ImageView) findViewById(R.id.ivTarget3);
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
    private void _fillListWoordenMetMeerdereAfbeeldingen() {
        HttpReader httpReader = new HttpReader();
        httpReader.setOnResultReadyListener(new HttpReader.OnResultReadyListener() {
            @Override
            public void resultReady(String result) {
                JsonHelper jsonHelper = new JsonHelper();
                woordenMetMeerdereAfbeeldingen = jsonHelper.getWoordenMetMeerdereAfbeeldingen(result);
                aantalVragenRondes = woordenMetMeerdereAfbeeldingen.size();
                play("start_oef2");

                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        //indien er "10" rondes zijn geweest, terug gaan naar de hoofdActivity van training
                        _maakVolgendeVraag_1();
                    }
                });
            }
        });
        httpReader.execute("http://axelpauwels.onthewifi.com/android/index.php/woord/getWoordenMetMeerdereAfbeeldingen/" + groep);
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

            huidigTarget = ivTarget1;
            text = view.getTag().toString();

            // bepalen welk het target is (1,2 of 3) aan de hand van de dragTeller die hieronder geset wordt in dragevent "DROP"
            switch (dragTeller + 1) {
                case 2:
                    huidigTarget = ivTarget2;
                    break;
                case 3:
                    huidigTarget = ivTarget3;
                    break;
                default:
                    break;
            }

            // indien het dragable is (nog geen 3 keer gedropt) de dragevents uitvoeren
            if (dragable) {
                switch (dragEvent) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        huidigTarget.setImageResource(getResources().getIdentifier(text, "drawable", getPackageName()));

                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        huidigTarget.setImageDrawable(null);
                        break;
                    case DragEvent.ACTION_DROP:
                        if (text.equals(huidigFoutAntwoord)) {
                            antwoordenVanSpelerZijnCorrect = false;
                        }
                        mp.reset();
                        huidigTarget.setImageResource(getResources().getIdentifier(text, "drawable", getPackageName()));
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
        iv1.setVisibility(View.VISIBLE);
        iv2.setVisibility(View.VISIBLE);
        iv3.setVisibility(View.VISIBLE);
        iv4.setVisibility(View.VISIBLE);

        iv1.setImageDrawable(null);
        iv2.setImageDrawable(null);
        iv3.setImageDrawable(null);
        iv4.setImageDrawable(null);

        iv1.setAlpha(0.4f);
        iv2.setAlpha(0.4f);
        iv3.setAlpha(0.4f);
        iv4.setAlpha(0.4f);

        ivTarget1.setImageDrawable(null);
        ivTarget2.setImageDrawable(null);
        ivTarget3.setImageDrawable(null);

        iv4.setOnLongClickListener(null);

        dragable = true;
        dragTeller = 0;
        woordenTeller = 0;
        antwoordenVanSpelerZijnCorrect = true;

        _maakVolgendeVraag_1();
    }

    private void _maakVolgendeVraag_1() {
        // vragen maken
        vragenRondeTeller++;
        huidigWoord = woordenMetMeerdereAfbeeldingen.get(vragenRondeTeller - 1);
        huidigFoutAntwoord = huidigWoord.getNaam() + "4";

        // voeg de 3 juiste afbeeldingen (bv kompas, kompas2 en kompas3) én de foute afbeelding (kompas4) samen aan een array en shuffle deze array
        List<String> randomStringArray = new ArrayList<String>();
        randomStringArray.add(huidigWoord.getNaam());
        for (int i = 0; i < 3; i++) {
            randomStringArray.add(huidigWoord.getNaam() + (i + 2));
        }
        Collections.shuffle(randomStringArray);
        huidigeMogelijkeAntwoorden = randomStringArray;

        _toonVolgendeVraag_2();
    }

    private void _toonVolgendeVraag_2() {
        // Views opvullen
        woord.setText(huidigWoord.getNaam());
        woord.setTag(huidigWoord.getNaam());

        iv1.setImageResource(getResources().getIdentifier(huidigeMogelijkeAntwoorden.get(0), "drawable", getPackageName()));
        iv1.setTag(huidigeMogelijkeAntwoorden.get(0));
        iv2.setImageResource(getResources().getIdentifier(huidigeMogelijkeAntwoorden.get(1), "drawable", getPackageName()));
        iv2.setTag(huidigeMogelijkeAntwoorden.get(1));
        iv3.setImageResource(getResources().getIdentifier(huidigeMogelijkeAntwoorden.get(2), "drawable", getPackageName()));
        iv3.setTag(huidigeMogelijkeAntwoorden.get(2));
        iv4.setImageResource(getResources().getIdentifier(huidigeMogelijkeAntwoorden.get(3), "drawable", getPackageName()));
        iv4.setTag(huidigeMogelijkeAntwoorden.get(3));

        _startRonde_3();
    }

    private void _startRonde_3() {
        skipButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        play(huidigWoord.getNaam());
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                play2("oef2_hoe_ziet_dat_er_uit");
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
                iv1.setAlpha(1.0f);
                iv1.setOnLongClickListener(longClickListener);
                break;
            case 2:
                iv1.setOnLongClickListener(null);
                iv1.setAlpha(0.4f);
                iv2.setAlpha(1.0f);
                iv2.setOnLongClickListener(longClickListener);
                break;
            case 3:
                iv2.setOnLongClickListener(null);
                iv2.setAlpha(0.4f);
                iv3.setAlpha(1.0f);
                iv3.setOnLongClickListener(longClickListener);
                break;
            case 4:
                iv3.setOnLongClickListener(null);
                iv3.setAlpha(0.4f);
                iv4.setAlpha(1.0f);
                iv4.setOnLongClickListener(longClickListener);
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
