package be.thomasmore.fonologischverkennen;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonHelper {

    public List<Woord> getWoorden(String jsonTekst) {
        List<Woord> woorden = new ArrayList<Woord>();

        try {
            JSONArray jsonArrayWoorden = new JSONArray(jsonTekst);
            for (int i = 0; i < jsonArrayWoorden.length(); i++) {

                JSONObject jsonObjectWoord = jsonArrayWoorden.getJSONObject(i);

                Woord woord = new Woord();
                woord.setId(jsonObjectWoord.getInt("id"));
                woord.setNaam(jsonObjectWoord.getString("naam"));
                woord.setAfbeelding(jsonObjectWoord.getString("afbeelding"));
                woorden.add(woord);
            }
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return woorden;
    }

    public List<Klas> getKlassen(String jsonTekst) {
        List<Klas> klassen = new ArrayList<Klas>();

        try {
            JSONArray jsonArrayKlassen = new JSONArray(jsonTekst);
            for (int i = 0; i < jsonArrayKlassen.length(); i++) {

                JSONObject jsonObjectWoord = jsonArrayKlassen.getJSONObject(i);

                Klas klas = new Klas();
                klas.setId(jsonObjectWoord.getInt("id"));
                klas.setNaam(jsonObjectWoord.getString("naam"));
                klassen.add(klas);
            }
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return klassen;
    }

    public List<Woord> getWoordenMetEigenschappen(String jsonTekst) {
        List<Woord> woorden = new ArrayList<Woord>();

        try {
            JSONArray jsonArrayWoorden = new JSONArray(jsonTekst);

            for (int i = 0; i < jsonArrayWoorden.length(); i++) {

                JSONObject jsonObjectWoord = jsonArrayWoorden.getJSONObject(i);
                JSONObject jsonObjectFouteEigenschap = jsonObjectWoord.getJSONObject("fouteEigenschap");
                JSONArray jsonArrayJuisteEigenschappen = jsonObjectWoord.getJSONArray("juisteEigenschappen");

                // fouteEigenschap opslaan
                WoordEigenschap fouteEigenschap = new WoordEigenschap();
                fouteEigenschap.setId(jsonObjectFouteEigenschap.getInt("id"));
                fouteEigenschap.setwoordId(jsonObjectFouteEigenschap.getInt("woordId"));
                fouteEigenschap.setNaam(jsonObjectFouteEigenschap.getString("naam"));
                fouteEigenschap.setGeluid(jsonObjectFouteEigenschap.getString("geluid"));

                // array met eigenschappen opslaan (=juiste eigenschappen)
                List<WoordEigenschap> juisteEigenschappen = new ArrayList<WoordEigenschap>();

                for (int j = 0; j < jsonArrayJuisteEigenschappen.length(); j++) {
                    WoordEigenschap juisteEigenschap = new WoordEigenschap();
                    juisteEigenschap.setId(jsonArrayJuisteEigenschappen.getJSONObject(j).getInt("id"));
                    juisteEigenschap.setwoordId(jsonArrayJuisteEigenschappen.getJSONObject(j).getInt("woordId"));
                    juisteEigenschap.setNaam(jsonArrayJuisteEigenschappen.getJSONObject(j).getString("naam"));
                    juisteEigenschap.setGeluid(jsonArrayJuisteEigenschappen.getJSONObject(j).getString("geluid"));
                    juisteEigenschappen.add(juisteEigenschap);
                }

                // woord met eigenschappen toevoegen aan array "woorden"
                Woord woord = new Woord();
                woord.setId(jsonObjectWoord.getInt("id"));
                woord.setNaam(jsonObjectWoord.getString("naam"));
                woord.setAfbeelding(jsonObjectWoord.getString("afbeelding"));
                woord.setWoordEigenschappen(juisteEigenschappen);
                woord.setWoordFouteEigenschap(fouteEigenschap);
                woorden.add(woord);
            }
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return woorden;
    }

    public List<Woord> getWoordenMetMeerdereAfbeeldingen(String jsonTekst) {
        List<Woord> woorden = new ArrayList<Woord>();

        try {
            JSONArray jsonArrayWoorden = new JSONArray(jsonTekst);
            for (int i = 0; i < jsonArrayWoorden.length(); i++) {
                JSONObject jsonObjectWoord = jsonArrayWoorden.getJSONObject(i);

                // woord met eigenschappen toevoegen aan array "woorden"
                Woord woord = new Woord();
                woord.setId(jsonObjectWoord.getInt("id"));
                woord.setNaam(jsonObjectWoord.getString("naam"));
                woord.setAfbeelding(jsonObjectWoord.getString("afbeelding"));
                woorden.add(woord);
            }
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return woorden;
    }

    public JSONObject getJsonMeting (Meting meting, ArrayList list) {
        JSONObject jsonObjectMeting = new JSONObject();
        try {
            jsonObjectMeting.put("datum", meting.getDatumString());
            jsonObjectMeting.put("juist", meting.getAantalJuist());
            jsonObjectMeting.put("fout", meting.getAantalFout());
            jsonObjectMeting.put("totaal", meting.getAantalTotaal());
            jsonObjectMeting.put("duur", meting.getDuurString());
            jsonObjectMeting.put("naam", meting.getNaam());
            jsonObjectMeting.put("klas", meting.getKlas());
            jsonObjectMeting.put("groep", meting.getGroep());
            jsonObjectMeting.put("fouteAntwoorden", list);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return jsonObjectMeting;
    }
}

