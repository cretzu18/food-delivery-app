package ui;

import model.Bautura;
import model.Mancare;
import model.Restaurant;
import service.FoodDeliveryService;

import java.util.Scanner;

public class AdminUI {
    private FoodDeliveryService service;
    private Scanner scanner;

    public AdminUI(FoodDeliveryService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    public void start() {
        boolean running = true;
        while (running) {
            try {
                System.out.println("\n--- PANOU CONTROL ADMIN ---\n");
                System.out.println("1. Adauga Restaurant");
                System.out.println("2. Adauga Mancare intr-un restaurant");
                System.out.println("3. Adauga Bautura intr-un restaurant");
                System.out.println("4. Vezi toti utlizatorii");
                System.out.println("5. Vezi restaurante");
                System.out.println("6. Vezi produse");
                System.out.println("0. Logout");
                System.out.print("Optiune: ");

                int optiune = Integer.parseInt(scanner.nextLine());
                switch (optiune) {
                    case 1:
                        System.out.print("Nume Restaurant: ");
                        String nume = scanner.nextLine();
                        System.out.print("Specific: ");
                        String specific = scanner.nextLine();
                        service.adminAdaugaRestaurant(new Restaurant(nume, specific));
                        System.out.println("Restaurantul a fost adaugat cu succes!");
                        break;

                    case 2:
                        service.afiseazaRestaurante();
                        System.out.print("Numele restaurantului pentru produs: ");
                        String numeRestaurant = scanner.nextLine();
                        System.out.print("Nume mancare: ");
                        String numeMancare = scanner.nextLine();
                        System.out.print("Descriere: ");
                        String descMancare = scanner.nextLine();
                        System.out.print("Pret: ");
                        double pretMancare = Double.parseDouble(scanner.nextLine());
                        System.out.print("Calorii: ");
                        int calMancare = Integer.parseInt(scanner.nextLine());
                        System.out.print("Gramaj (g): ");
                        double gramajMancare = Double.parseDouble(scanner.nextLine());

                        service.adminAdaugaProdus(numeRestaurant, new Mancare(numeMancare, descMancare, pretMancare, calMancare, gramajMancare));
                        break;

                    case 3:
                        service.afiseazaRestaurante();
                        System.out.print("Numele restaurantului pentru produs: ");
                        String numeRes = scanner.nextLine();
                        System.out.print("Nume Bautura: ");
                        String numeBautura = scanner.nextLine();
                        System.out.print("Descriere: ");
                        String descBautura = scanner.nextLine();
                        System.out.print("Pret: ");
                        double pretBautura = Double.parseDouble(scanner.nextLine());
                        System.out.print("Calorii: ");
                        int calBautura = Integer.parseInt(scanner.nextLine());
                        System.out.print("Volum (ml): ");
                        int volBautura = Integer.parseInt(scanner.nextLine());
                        System.out.print("Este alcoolica? (true/false): ");
                        boolean alcBautura = Boolean.parseBoolean(scanner.nextLine());

                        service.adminAdaugaProdus(numeRes, new Bautura(numeBautura, descBautura, pretBautura, calBautura, volBautura, alcBautura));
                        break;

                    case 4:
                        service.adminVizualizareUtilizatori();
                        break;

                    case 5:
                        service.afiseazaRestaurante();
                        break;

                    case 6:
                        service.afiseazaRestaurante();
                        System.out.print("Restaurantul pentru care vrei sa vezi produsele: ");
                        String restaurant = scanner.nextLine();
                        service.afiseazaProduse(restaurant);
                        break;

                    case 0:
                        service.logout();
                        running = false;
                        break;

                    default:
                        System.out.println("Optiune invalida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Optiunea trebuia sa fie un numar intreg.");
            }
        }
    }
}
