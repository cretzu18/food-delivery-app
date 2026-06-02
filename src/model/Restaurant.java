package model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Restaurant implements Comparable<Restaurant>{
    private int id;
    private String nume;
    private String specific;
    private double rating;
    private int numarRecenzii;
    private List<Produs> meniu;

    public Restaurant(String nume, String specific) {
        this.nume = nume;
        this.specific = specific;
        this.rating = 0.0;
        this.numarRecenzii = 0;
        this.meniu = new ArrayList<>();
    }

    public Restaurant(String nume, String specific, double rating, int numarRecenzii) {
        this.nume = nume;
        this.specific = specific;
        this.rating = rating;
        this.numarRecenzii = numarRecenzii;
        this.meniu = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getSpecific() {
        return specific;
    }

    public void setSpecific(String specific) {
        this.specific = specific;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumarRecenzii() {
        return numarRecenzii;
    }

    public void setNumarRecenzii(int numarRecenzii) {
        this.numarRecenzii = numarRecenzii;
    }

    public void adaugaProdus(Produs p) {
        meniu.add(p);
    }

    public boolean eliminaProdus(String numeProds) {
        return meniu.removeIf(p -> p.getNume().equalsIgnoreCase(numeProds));
    }

    public List<Produs> getMeniu() {
        return meniu;
    }

    public void adaugaRecenzie(double nota) {
        this.rating = (this.rating * numarRecenzii + nota) / (numarRecenzii + 1);
        numarRecenzii++;
    }

    public List<Produs> getMeniuSortatDupaPret() {
        List<Produs> meniuSortat = new ArrayList<>(this.meniu);
        Collections.sort(meniuSortat);
        return meniuSortat;
    }

    @Override
    public int compareTo(Restaurant other) {
        int rezultatRating = Double.compare(other.rating, this.rating);
        if (rezultatRating == 0) {
            return this.nume.compareTo(other.nume);
        }
        return rezultatRating;
    }

    @Override
    public String toString() {
        return String.format("Restaurant %s [%s] - Rating: %.1f stele", nume, specific, rating);
    }
}

