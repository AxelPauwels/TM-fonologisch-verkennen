package be.thomasmore.fonologischverkennen;

public class Klas {
    private int id;
    private String naam;

    public Klas() {
    }

    public Klas(int id, String naam, String afbeelding) {
        this.id = id;
        this.naam = naam;
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

    @Override
    public String toString () {
        return naam;
    }
}
