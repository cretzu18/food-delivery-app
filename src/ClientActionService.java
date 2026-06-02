import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientActionService {
    private UserService userService;
    private RestaurantService restaurantService;
    private ProdusService produsService;
    private ComandaService comandaService;

    public ClientActionService() {
        this.userService = new UserService();
        this.restaurantService = new RestaurantService();
        this.produsService = new ProdusService();
        this.comandaService = new ComandaService();
    }

    private Client checkClient() {
        User userLogat = AuthService.getInstance().getUserLogat();
        if (userLogat == null || !(userLogat instanceof Client)) {
            System.out.println("Eroare: Trebuie sa fii logat ca si Client pentru a accesa aceasta actiune!");
            return null;
        }
        return (Client) userLogat;
    }

    // Actiuni Publice 
    public void afiseazaRestaurante() {
        List<Restaurant> restaurante = restaurantService.read();
        
        if (restaurante.isEmpty()) {
            System.out.println("Nu exista restaurante.");
            return;
        }

        // Sortam restaurantele daca e nevoie (Restaurante extinde Comparable de aceea am pus null)
        restaurante.sort(null);

        System.out.println("\n--- Lista Restaurante Sortate Dupa Rating ---\n");
        for (Restaurant r: restaurante) {
            System.out.println(r);
        }
    }

    public void afiseazaProduse(String numeRestaurant) {
        List<Restaurant> restaurante = restaurantService.read();
        Restaurant restaurant = null;
        for (Restaurant r: restaurante) {
            if (r.getNume().equalsIgnoreCase(numeRestaurant)) {
                restaurant = r;
                break;
            }
        }

        if (restaurant == null) {
            System.out.println("Eroare: restaurantul nu exista!");
            return;
        }

        List<Produs> meniu = produsService.read(); // Citim toate produsele
        List<Produs> meniuRestaurant = new ArrayList<>();
        for (Produs p : meniu) {
            if (p.getRestaurant() != null && p.getRestaurant().getId() == restaurant.getId()) {
                meniuRestaurant.add(p);
            }
        }

        if (meniuRestaurant.isEmpty()) {
            System.out.println("Restaurantul " + restaurant.getNume() + " nu are produse!");
        } else {
            System.out.println("\n--- Meniu " + restaurant.getNume() + " ---\n");
            for (Produs p: meniuRestaurant) {
                System.out.println("- " + p);
            }
        }
    }

    // Actiuni Client
    public void vizualizareCos() {
        Client client = checkClient();
        if (client == null) return;

        Map<Produs, Integer> cos = client.getCosCumparaturi();

        if (cos.isEmpty()) {
            System.out.println("\nCosul este gol.\n");
            return;
        }

        System.out.println("\n--- Cosul De Cumparaturi ---\n");
        double totalCos = 0;
        for (Map.Entry<Produs, Integer> entry: cos.entrySet()) {
            Produs p = entry.getKey();
            int cantitate = entry.getValue();
            double subtotal = p.getPret() * cantitate;
            totalCos += subtotal;
            System.out.printf("- %s x %d buc. | Subtotal: %.2f RON\n", p.getNume(), cantitate, subtotal);
        }

        System.out.printf("TOTAL: %.2f RON\n", totalCos);
        System.out.println("------------------------------\n\n");
    }

    public void vizualizareIstoric() {
        Client client = checkClient();
        if (client == null) return;

        List<Comanda> toateComenzile = comandaService.read();
        List<Comanda> istoric = new ArrayList<>();
        
        for (Comanda c : toateComenzile) {
            if (c.getClient().getId() == client.getId()) {
                istoric.add(c);
            }
        }

        if (istoric.isEmpty()) {
            System.out.println("\nNu ai nicio comanda plasata anterior.\n");
            return;
        }

        System.out.println("\n--- Istoric Comenzi ---\n");
        for(Comanda c: istoric) {
            System.out.println(c);
        }
    }

    public void vizualizareCurieri() {
        Client client = checkClient();
        if (client == null) return;

        List<User> utilizatori = userService.read();
        List<Curier> listaCurieri = new ArrayList<>();
        for (User u: utilizatori) {
            if (u instanceof Curier) {
                listaCurieri.add((Curier) u);
            }
        }

        if (listaCurieri.isEmpty()) {
            System.out.println("Nu exista curieri in aplicatie!");
            return;
        }

        System.out.println("\n--- Lista Curieri ---\n");
        for (Curier c: listaCurieri) {
            System.out.println(c);
        }
    }

    public void adaugaInCos(String numeRestaurant, String numeProdus, int cantitate) {
        Client client = checkClient();
        if (client == null) return;

        List<Restaurant> restaurante = restaurantService.read();
        Restaurant restaurant = null;
        for (Restaurant r: restaurante) {
            if (r.getNume().equalsIgnoreCase(numeRestaurant)) {
                restaurant = r;
                break;
            }
        }

        if (restaurant == null) {
            System.out.println("Eroare: restaurantul nu exista!");
            return;
        }

        List<Produs> produse = produsService.read();
        Produs produs = null;
        for (Produs p: produse) {
            if (p.getRestaurant() != null && p.getRestaurant().getId() == restaurant.getId() && p.getNume().equalsIgnoreCase(numeProdus)) {
                produs = p;
                break;
            }
        }

        if (produs == null) {
            System.out.println("Eroare: produsul nu exista!");
            return;
        }

        client.adaugaInCos(produs, cantitate);
        System.out.println("Produsul a fost adaugat cu succes in cos!");
    }

    public void plaseazaComanda(Adresa adresa) {
        Client client = checkClient();
        if (client == null) return;

        Map<Produs, Integer> cos = client.getCosCumparaturi();

        if (cos.isEmpty()) {
            System.out.println("Eroare: Cosul tau este gol!");
            return;
        }

        Map<Restaurant, Map<Produs, Integer>> comenziPeRestaurante = new HashMap<>();

        for (Map.Entry<Produs, Integer> entry: cos.entrySet()) {
            Produs p = entry.getKey();
            int cantitate = entry.getValue();
            Restaurant r = p.getRestaurant();

            comenziPeRestaurante.putIfAbsent(r, new HashMap<>());
            comenziPeRestaurante.get(r).put(p, cantitate);
        }

        for (Restaurant r: comenziPeRestaurante.keySet()) {
            Map<Produs, Integer> produseSubcomanda = comenziPeRestaurante.get(r);
            Comanda comandaNoua = new Comanda(client, r, adresa, produseSubcomanda);
            
            // Salvare in DB
            comandaService.create(comandaNoua);
            System.out.println("Comanda " + comandaNoua.getId() + " a fost plasata cu succes!");
        }

        client.golesteCos();
    }

    public void lasaRating(String numeRestaurant, double rating) {
        Client client = checkClient();
        if (client == null) return;

        if (rating < 1 || rating > 5) {
            System.out.println("Eroare: Nota trebuie sa fie intre 1 si 5");
            return;
        }

        List<Comanda> toateComenzile = comandaService.read();
        boolean eligibil = false;
        for (Comanda c: toateComenzile) {
            if (c.getClient().getId() == client.getId() && c.getRestaurant().getNume().equalsIgnoreCase(numeRestaurant)) {
                if (c.getStatus().equals("FINALIZAT")) {
                    eligibil = true;
                    break;
                }
            }
        }

        if (!eligibil) {
            System.out.println("Eroare: Poti lasa un rating doar restaurantelor de la care ai facut cel putin o comanda livrata!");
            return;
        }

        List<Restaurant> restaurante = restaurantService.read();
        for (Restaurant r: restaurante) {
            if (r.getNume().equalsIgnoreCase(numeRestaurant)) {
                r.adaugaRecenzie(rating);
                // Update in baza de date
                restaurantService.update(r);
                System.out.println("Rating-ul a fost inregistrat!");
                return;
            }
        }
    }
}
