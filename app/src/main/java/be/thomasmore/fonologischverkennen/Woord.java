package be.thomasmore.fonologischverkennen;

import java.util.List;

public class Woord {
    private int id;
    private String naam;
    private String afbeelding;
    private WoordEigenschap fouteEigenschap;
    private List<WoordEigenschap> juisteEigenschappen;

    public Woord() {
    }

    public Woord(int id, String naam, String afbeelding) {
        this.id = id;
        this.naam = naam;
        this.afbeelding = afbeelding;
    }

    public Woord(int id, String naam, String afbeelding, WoordEigenschap fouteEigenschap, List<WoordEigenschap> juisteEigenschappen) {
        this.id = id;
        this.naam = naam;
        this.afbeelding = afbeelding;
        this.fouteEigenschap = fouteEigenschap;
        this.juisteEigenschappen = juisteEigenschappen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getAfbeelding() {
        return afbeelding;
    }

    public void setAfbeelding(String afbeelding) {
        this.afbeelding = afbeelding;
    }

    public WoordEigenschap getWoordFouteEigenschap() {
        return fouteEigenschap;
    }

    public void setWoordFouteEigenschap(WoordEigenschap fouteEigenschap) {
        this.fouteEigenschap = fouteEigenschap;
    }

    public List<WoordEigenschap> getWoordEigenschappen() {
        return juisteEigenschappen;
    }

    public void setWoordEigenschappen(List<WoordEigenschap> juisteEigenschappen) {
        this.juisteEigenschappen = juisteEigenschappen;
    }

    @Override
    public String toString () {
        return naam;
    }

}
