package model;
public class Mancare extends Produs{
    private double gramaj;


    public Mancare(String nume, String descriere, double pret, int calorii, double gramaj) {
        super(nume, descriere, pret, calorii);
        this.gramaj = gramaj;
    }

    public double getGramaj() {
        return gramaj;
    }

    public void setGramaj(double gramaj) {
        this.gramaj = gramaj;
    }

    @Override
    public String toString() {
        return super.toString() + " [" + gramaj + "g]";
    }
}

