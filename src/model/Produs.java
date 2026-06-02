package model;
public abstract class Produs implements Comparable<Produs>{
    private int id;
    private String nume;
    private String descriere;
    protected double pret;
    private int calorii;
    private Restaurant restaurant;

    public Produs(String nume, String descriere, double pret, int calorii) {
        this.nume = nume;
        this.descriere = descriere;
        this.pret = pret;
        this.calorii = calorii;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public double getPret() {
        return pret;
    }

    public void setPret(double pret) {
        this.pret = pret;
    }

    public int getCalorii() {
        return calorii;
    }

    public void setCalorii(int calorii) {
        this.calorii = calorii;
    }

    @Override
    public int compareTo(Produs other) {
        return Double.compare(this.pret, other.pret);
    }

    @Override
    public String toString() {
        return String.format("%s - %.2f RON, kcal: %d", nume, pret, calorii);
    }
}

