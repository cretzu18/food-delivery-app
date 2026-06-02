package service;
import model.*;
import repository.*;
public class FoodDeliveryService {
    private AdminActionService adminService = new AdminActionService();
    private ClientActionService clientService = new ClientActionService();
    private CurierActionService curierService = new CurierActionService();

    public User getUserLogat() {
        return AuthService.getInstance().getUserLogat();
    }

    // Creaza un user nou si il adauga in baza de date
    public void inregistrareUser(User u) {
        AuthService.getInstance().inregistrareUser(u);
    }

    // Realizeaza conectarea la cont pe baza email-ului
    public boolean login(String email, String parola) {
        return AuthService.getInstance().login(email, parola);
    }

    // Deconecteaza user-ul curent
    public void logout() {
        AuthService.getInstance().logout();
    }

    // Afiseaza restaurantele pe baza rating-ului
    public void afiseazaRestaurante() {
        clientService.afiseazaRestaurante();
    }

    // Afiseaza toate produsele specifice numelui restaurantului
    public void afiseazaProduse(String numeRestaurant) {
        clientService.afiseazaProduse(numeRestaurant);
    }

    // OPERATII ADMIN
    public void adminVizualizareUtilizatori() {
        adminService.adminVizualizareUtilizatori();
    }

    public void adminAdaugaRestaurant(Restaurant r) {
        adminService.adminAdaugaRestaurant(r);
    }

    public void adminAdaugaProdus(String numeRestaurant, Produs produs) {
        adminService.adminAdaugaProdus(numeRestaurant, produs);
    }
    // END OPERATII ADMIN

    // OPERATII CLIENT
    public void clientVizualizareCos() {
        clientService.vizualizareCos();
    }

    public void clientVizualizareIstoric() {
        clientService.vizualizareIstoric();
    }

    public void clientVizualizareCurieri() {
        clientService.vizualizareCurieri();
    }

    public void clientAdaugaInCos(String numeRestaurant, String numeProdus, int cantitate) {
        clientService.adaugaInCos(numeRestaurant, numeProdus, cantitate);
    }

    public void clientPlaseazaComanda(Adresa adresa) {
        clientService.plaseazaComanda(adresa);
    }

    public void clientLasaRating(String numeRestaurant, double rating) {
        clientService.lasaRating(numeRestaurant, rating);
    }
    // END ACTIUNI CLIENT

    // ACTIUNI CURIER
    public void curierPreiaComanda(int idComanda) {
        curierService.curierPreiaComanda(idComanda);
    }

    public void curierFinalizeazaComanda() {
        curierService.curierFinalizeazaComanda();
    }

    public void curierAfiseazaComenziDisponibile() {
        curierService.curierAfiseazaComenziDisponibile();
    }
    // END ACTIUNI CURIER
}

