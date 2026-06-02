package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client extends User{
    private Adresa adresaImplicita;
    private List<Comanda> istoricComenzi;
    private Map<Produs, Integer> cosCumparaturi;

    public Client(String nume, String email, String telefon, String parola, Adresa adresa) {
        super(nume, email, telefon, parola);
        this.adresaImplicita = adresa;
        this.istoricComenzi = new ArrayList<>();
        this.cosCumparaturi = new HashMap<>();
    }

    public Adresa getAdresaImplicita() {
        return adresaImplicita;
    }

    public void setAdresaImplicita(Adresa adresaImplicita) {
        this.adresaImplicita = adresaImplicita;
    }

    public List<Comanda> getIstoricComenzi() {
        return istoricComenzi;
    }

    public Map<Produs, Integer> getCosCumparaturi() {
        return cosCumparaturi;
    }

    public void adaugaComandaInIstoric(Comanda comanda) {
        istoricComenzi.add(comanda);
    }

    public void adaugaInCos(Produs p, int cantitate) {
        cosCumparaturi.put(p, cosCumparaturi.getOrDefault(p, 0) + cantitate);
    }

    public void golesteCos() {
        cosCumparaturi.clear();
    }

    @Override
    public String toString() {
        return "Client: " + getNume() + " | Contact: " + getEmail() + " / " + getTelefon();
    }
}

