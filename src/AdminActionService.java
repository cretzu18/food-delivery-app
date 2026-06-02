import java.util.List;

public class AdminActionService {
    private UserService userService;
    private RestaurantService restaurantService;
    private ProdusService produsService;

    public AdminActionService() {
        this.userService = new UserService();
        this.restaurantService = new RestaurantService();
        this.produsService = new ProdusService();
    }

    private boolean checkAdmin() {
        User userLogat = AuthService.getInstance().getUserLogat();
        if (userLogat == null || !(userLogat instanceof Admin)) {
            System.out.println("Acces Interzis! Doar Adminul poate efectua aceasta actiune!");
            return false;
        }
        return true;
    }

    public void adminVizualizareUtilizatori() {
        if (!checkAdmin()) return;

        List<User> utilizatori = userService.read();

        if (utilizatori.isEmpty()) {
            System.out.println("Nu exista utilizatori creati.");
            return;
        }

        System.out.println("\n======= LISTA UTILIZATORI SISTEM =======");
        System.out.printf("%-15s | %-20s | %-15s | %-10s\n", "NUME", "EMAIL", "TELEFON", "ROL");
        System.out.println("------------------------------------------------------------");

        for (User u : utilizatori) {
            String rol = u.getClass().getSimpleName();

            System.out.printf("%-15s | %-20s | %-15s | %-10s\n",
                    u.getNume(),
                    u.getEmail(),
                    u.getTelefon(),
                    rol);
        }
        System.out.println("------------------------------------------------------------\n");
    }

    public void adminAdaugaRestaurant(Restaurant r) {
        if (!checkAdmin()) return;
        restaurantService.create(r);
    }

    public void adminAdaugaProdus(String numeRestaurant, Produs produs) {
        if (!checkAdmin()) return;

        List<Restaurant> restaurante = restaurantService.read();
        Restaurant restaurantFound = null;
        for (Restaurant r : restaurante) {
            if (r.getNume().equalsIgnoreCase(numeRestaurant)) {
                restaurantFound = r;
                break;
            }
        }

        if (restaurantFound != null) {
            produs.setRestaurant(restaurantFound);
            produsService.create(produs);
        } else {
            System.out.println("Eroare: restaurantul '" + numeRestaurant + "' nu exista in baza de date!");
        }
    }
}
