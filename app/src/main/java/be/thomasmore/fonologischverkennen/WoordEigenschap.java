package be.thomasmore.fonologischverkennen;

public class WoordEigenschap {
    private int id;
    private int woordId;
    private String naam;
    private String geluid;

    public WoordEigenschap() {
    }

    public WoordEigenschap(int id, int woordId, String naam,String geluid) {
        this.id = id;
        this.woordId = woordId;
        this.naam = naam;
        this.geluid = geluid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWoordId() {
        return woordId;
    }

    public void setwoordId(int woordId) {
        this.woordId = woordId;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getGeluid() {
        return geluid;
    }

    public void setGeluid(String geluid) {
        this.geluid = geluid;
    }

    @Override
    public String toString () {
        return naam;
    }

}
