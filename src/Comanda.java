import java.awt.event.ComponentAdapter;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;

public class Comanda {
    private int id;
    private Client client;
    private Adresa adresa;
    private Curier curier;
    private Restaurant restaurant;
    private Map<Produs, Integer> produseComandate;
    private String status;
    private LocalDateTime dataPlasarii;
    private double pretTotal;

    public Comanda(Client client, Restaurant restaurant, Adresa adresa, Map<Produs, Integer> cosCumaparturi) {
        this.client = client;
        this.restaurant = restaurant;
        this.adresa = adresa;
        this.produseComandate = new HashMap<>(cosCumaparturi);
        this.status = "IN_ASTEPTARE";
        this.dataPlasarii = LocalDateTime.now();
        this.pretTotal = calculeazaPret();
    }

    private double calculeazaPret() {
        double pret = 0.0;
        for (Map.Entry<Produs, Integer> entry : produseComandate.entrySet()) {
            pret += entry.getKey().getPret() * entry.getValue();
        }

        return pret;
    }

    public void setCurier(Curier curier) {
        this.curier = curier;
    }

    public Curier getCurier() {
        return curier;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Client getClient() {
        return client;
    }

    public Adresa getAdresa() {
        return adresa;
    }

    public Map<Produs, Integer> getProduseComandate() {
        return produseComandate;
    }

    public LocalDateTime getDataPlasarii() {
        return dataPlasarii;
    }

    public void setDataPlasarii(LocalDateTime dataPlasarii) {
        this.dataPlasarii = dataPlasarii;
    }

    public double getPretTotal() {
        return pretTotal;
    }

    public void setPretTotal(double pretTotal) {
        this.pretTotal = pretTotal;
    }

    @Override
    public String toString() {
        return "Comanda " + id + ", plasata la " + dataPlasarii +
                ", Status: " + status + ", Pret: " + calculeazaPret();
    }
}
