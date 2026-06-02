package model;
public class Bautura extends Produs{
    private int volum;
    private boolean contineAlcool;

    public Bautura(String nume, String descriere, double pret, int calorii, int volum, boolean contineAlcool) {
        super(nume, descriere, pret, calorii);
        this.volum = volum;
        this.contineAlcool = contineAlcool;
    }

    public boolean contineAlcool() {
        return contineAlcool;
    }

    public void setContineAlcool(boolean contineAlcool) {
        this.contineAlcool = contineAlcool;
    }

    public int getVolum() {
        return volum;
    }

    public void setVolum(int volum) {
        this.volum = volum;
    }

    @Override
    public String toString() {
        String tip = contineAlcool ? "Alcoolic" : "Non-alcoolic";
        return super.toString() + " [" + volum + "ml, " + tip + "]";
    }
}

