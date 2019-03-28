package be.thomasmore.fonologischverkennen;

import java.util.Date;
import java.util.GregorianCalendar;

public class Meting {
    private Date datum;
    private String datumString;
    private int aantalJuist;
    private int aantalFout;
    private int aantalTotaal;
    private Date duur;
    private String duurString;
    private String naam;
    private String klas;
    private String groep;


    public Meting() {
    }

    public Meting(Date datum, String datumString, int aantalJuist, int aantalFout, int aantalTotaal, Date duur, String duurString) {
        this.datum = datum;
        this.datumString = datumString;
        this.aantalJuist = aantalJuist;
        this.aantalFout = aantalFout;
        this.aantalTotaal = aantalTotaal;
        this.duur = duur;
        this.duurString = duurString;
    }
    public Meting(Date datum, String datumString, int aantalJuist, int aantalFout, int aantalTotaal, Date duur, String duurString,String naam, String klas,String groep) {
        this.datum = datum;
        this.datumString = datumString;
        this.aantalJuist = aantalJuist;
        this.aantalFout = aantalFout;
        this.aantalTotaal = aantalTotaal;
        this.duur = duur;
        this.duurString = duurString;
        this.naam = naam;
        this.klas = klas;
        this.groep = groep;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public String getDatumString() {
        return datumString;
    }

    public void setDatumString(String datumString) {
        this.datumString = datumString;
    }

    public int getAantalJuist() {
        return aantalJuist;
    }

    public void setAantalJuist(int aantalJuist) {
        this.aantalJuist = aantalJuist;
    }

    public int getAantalFout() {
        return aantalFout;
    }

    public void setAantalFout(int aantalFout) {
        this.aantalFout = aantalFout;
    }

    public int getAantalTotaal() {
        return aantalTotaal;
    }

    public void setAantalTotaal(int aantalTotaal) {
        this.aantalTotaal = aantalTotaal;
    }

    public Date getDuur() {
        return duur;
    }

    public void setDuur(Date duur) {
        this.duur = duur;
    }

    public String getDuurString() {
        return duurString;
    }

    public void setDuurString(String duurString) {
        this.duurString = duurString;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getKlas() {
        return klas;
    }

    public void setKlas(String klas) {
        this.klas = klas;
    }

    public String getGroep() {
        return groep;
    }

    public void setGroep(String groep) {
        this.groep = groep;
    }

    @Override
    public String toString () {
        return "Meting.java / ToString() is empty";
    }

}
