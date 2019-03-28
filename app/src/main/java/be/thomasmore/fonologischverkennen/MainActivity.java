package be.thomasmore.fonologischverkennen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    int metingId; // O om voormeting op te slaan, 1 om nameting op te slaan

    String woordenString = "";
    String woordenStringA = "";
    String woordenStringB = "";
    private String klassenString = new String();
    private String text;
    List<String> listKlassen = new ArrayList<String>();

    private boolean voormeting = true;

    private Button voorMetingknop;
    private Button naMetingknop;
    private Button training;

    private EditText editNaam;

    private Spinner dropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        voorMetingOfNaMetingTonen();
        toonInvulGegevensDialoog();

    }

    private void toonInvulGegevensDialoog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        final View viewInflater = inflater.inflate(R.layout.dialog_signin, null);
        builder.setTitle(R.string.dialog_title)
                .setIcon(R.mipmap.settings)
                .setView(viewInflater);


        spinnerOpVullen(viewInflater);

        final AlertDialog dialog = builder.create();

        dialog.show();
        dialog.setCancelable(false);

        Button validateForm;
        validateForm = (Button) viewInflater.findViewById(R.id.validate);

        validateForm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editNaam = (EditText) viewInflater.findViewById(R.id.naam);
                naam = editNaam.getText().toString();
                Spinner spinner = (Spinner) viewInflater.findViewById(R.id.spinner1);
                text = spinner.getSelectedItem().toString();
                klas = spinner.getSelectedItem().toString();
                Log.i("***klass", text);
                RadioButton checkBoxA = (RadioButton) viewInflater.findViewById(R.id.groepA);
                RadioButton checkBoxB = (RadioButton) viewInflater.findViewById(R.id.groepB);
                RadioGroup radioGroup = (RadioGroup) viewInflater.findViewById(R.id.radioGroup);

                dialoogFormulierInputControlen(checkBoxA, checkBoxB, radioGroup, spinner, dialog);
            }
        });
    }

    private void dialoogFormulierInputControlen(RadioButton checkBoxA, RadioButton checkBoxB, RadioGroup radioGroup, Spinner spinner, AlertDialog dialog) {

        if (checkBoxA.isChecked()) {
            groep = "A";
            Log.i("***groep", groep);
        } else {
            groep = "B";
            Log.i("***groep", groep);
        }
        if (naam.isEmpty()) {
            editNaam.setError("Verplicht");
        } else if (radioGroup.getCheckedRadioButtonId() <= 0) {
            checkBoxB.setError("selekteer klas");//Set error to last Radio button
        } else if (spinner.getSelectedItem().toString().equals("Selecteer klas")) {
            Toast toast = Toast.makeText(getBaseContext(),"Selecteer een klas aub", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 10);
            toast.show();
        } else {
            dialog.dismiss();
            _fillListWoorden();

        }
    }

    private void spinnerOpVullen(View viewInflater) {
        fillListKlassen();
        Log.i("***klassenString", klassenString);

        listKlassen.add("Selecteer klas");
        dropdown = viewInflater.findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listKlassen);
        dropdown.setAdapter(adapter);
    }

    private void voorMetingOfNaMetingTonen() {
        voorMetingknop = (Button) findViewById(R.id.voorMeting);
        naMetingknop = (Button) findViewById(R.id.naMeting);
        training = (Button) findViewById(R.id.training);
        if (voormeting == true) {
            voorMetingknop.setEnabled(true);
            naMetingknop.setEnabled(false);
            training.setEnabled(true);
        } else {
            naMetingknop.setEnabled(true);
            voorMetingknop.setEnabled(false);
            training.setEnabled(true);
        }
    }

    private String klas = "naMeetingKlas";
    private String naam = "naMeetingNaam";
    private String groep = "naMeetingGroep";

    //data ophalen aan de hhand van id v intent
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String duurInSeconden = data.getStringExtra("tijdInSeconden");
                String aantalAntwoordenJuist = data.getStringExtra("juisteAntwoorden");
                String klass = data.getStringExtra("klass");

                Log.i("***juisteantwoorden", aantalAntwoordenJuist);
                int aantalAntwoordenInt = Integer.parseInt(aantalAntwoordenJuist);

                String hulp = data.getStringExtra("totaalVragen");
                Log.i("***test", hulp);

                int totaalVragen = Integer.parseInt(hulp);

                Log.i("***totaalVragen", String.valueOf(totaalVragen));
                int fouten = totaalVragen - aantalAntwoordenInt;
                Log.i("***duurInSeconden", duurInSeconden);
                Log.i("***AntwoordenJuist", aantalAntwoordenJuist);

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date vandaag = Calendar.getInstance().getTime();
                String datumString = df.format(vandaag);
                Date duur = Calendar.getInstance().getTime();

                Log.i("***klass", klas);
                Log.i("***naam", naam);
                Log.i("***groep", groep);


                Meting meting = new Meting(vandaag, datumString, aantalAntwoordenInt, fouten, totaalVragen, duur, duurInSeconden, naam, klas, groep);
                JsonHelper jsonHelper = new JsonHelper();

                HttpWriter httpWriter = new HttpWriter();

                String foutenwoorden = data.getStringExtra("foutenwoorden");
                String[] separated = foutenwoorden.split(",");
                Log.i("***foutenwoorden", foutenwoorden);
                ArrayList<String> stringArray = new ArrayList<String>(Arrays.asList(separated));

                httpWriter.setJsonObject(jsonHelper.getJsonMeting(meting, stringArray));
                httpWriter.setOnResultReadyListener(new HttpWriter.OnResultReadyListener() {
                    @Override
                    public void resultReady(String result) {
                        String intString = result.substring(0, result.length() - 1);
                        metingId = Integer.parseInt(intString);
                    }
                });
                httpWriter.execute("http://axelpauwels.onthewifi.com/android/index.php/meting/insert/" + metingId);
                if (voormeting == true) {
                    voormeting = false;
                } else {
                    voormeting = true;
                }
                voorMetingOfNaMetingTonen();
            }
        }
    }

    private void _fillListWoorden() {
        HttpReader httpReader = new HttpReader();
        httpReader.setOnResultReadyListener(new HttpReader.OnResultReadyListener() {
            @Override
            public void resultReady(String result) {
                JsonHelper jsonHelper = new JsonHelper();
                List<Woord> woorden = jsonHelper.getWoorden(result);
                Log.i("***woorden", woorden.toString());
                for (int i = 0; i < woorden.size(); i++) {
                    woordenStringA += woorden.get(i).getNaam() + " - ";
                }
            }
        });
        httpReader.execute("http://axelpauwels.onthewifi.com/android/index.php/woord/getAll/" + groep);
    }

    private void fillListKlassen() {
        HttpReader httpReader = new HttpReader();
        httpReader.setOnResultReadyListener(new HttpReader.OnResultReadyListener() {
            @Override
            public void resultReady(String result) {
                JsonHelper jsonHelper = new JsonHelper();
                List<Klas> klassen = jsonHelper.getKlassen(result);
                Log.i("***klassen", klassen.toString());
                //woorden = _sorteerWoorden(woorden);
                for (int i = 0; i < klassen.size(); i++) {
                    listKlassen.add(klassen.get(i).getNaam());
                }
            }
        });
        httpReader.execute("http://axelpauwels.onthewifi.com/android/index.php/klas/getAll");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void buttonVoormeting_Click(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("woorden", woordenStringA);

        bundle.putString("klassenString", klassenString);
        Intent intent = new Intent(this, voormeting.class);
        intent.putExtras(bundle);

        startActivityForResult(intent, 1);
    }

    public void buttonNaMeting_Click(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("woorden", woordenStringA);


        Intent intent = new Intent(this, nameting.class);
        intent.putExtras(bundle);

        startActivityForResult(intent, 1);
    }


    public void trainingMain_click(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("woorden", woordenStringA);

        Intent intent = new Intent(this, TrainingMainActivity.class);
        intent.putExtras(bundle);

        startActivityForResult(intent, 1);
    }


}
