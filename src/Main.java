import model.*;
import repository.*;
import service.*;
import ui.*;

import java.util.Scanner;

public class Main {
    private static FoodDeliveryService service = new FoodDeliveryService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- FOOD DELIVERY APP ---\n");
            System.out.println("1. Login");
            System.out.println("2. Creaza User Nou");
            System.out.println("3. Vezi restaurante");
            System.out.println("4. Vezi produse");
            System.out.println("0. Iesire");
            System.out.print("Optiune: ");

            try {
                int optiune = Integer.parseInt(scanner.nextLine());

                switch (optiune) {
                    case 1:
                        fereastraLogin();
                        break;
                    case 2:
                        fereastraInregistrare();
                        break;
                    case 3:
                        service.afiseazaRestaurante();
                        break;
                    case 4:
                        service.afiseazaRestaurante();
                        System.out.print("Restaurantul pentru care vrei sa vezi produsele: ");
                        String restaurant = scanner.nextLine();
                        service.afiseazaProduse(restaurant);
                        break;
                    case 0:
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

    private static void fereastraLogin() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Parola: ");
        String parola = scanner.nextLine();

        if (service.login(email, parola)) {
            User logat = service.getUserLogat();
            if (logat instanceof Admin) {
                new AdminUI(service, scanner).start();
            } else if (logat instanceof Client) {
                new ClientUI(service, scanner).start();
            } else if (logat instanceof Curier) {
                new CurierUI(service, scanner).start();
            }
        }
    }

    private static void fereastraInregistrare() {
        try {
            System.out.println("Tipul contului este: 1. Client sau 2. Curier ?");
            int optiune = Integer.parseInt(scanner.nextLine());
            if (optiune != 1 && optiune != 2) {
                System.out.println("Optiune invalida");
                return;
            }


            System.out.print("Nume: ");
            String nume = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Telefon: ");
            String telefon = scanner.nextLine();
            String tipVehicul = null;
            Adresa adresa = null;
            if (optiune == 1) {
                System.out.println("Adresa:");
                System.out.print("Oras: ");
                String oras = scanner.nextLine();
                System.out.print("Strada: ");
                String strada = scanner.nextLine();
                System.out.print("Numar: ");
                int numar = Integer.parseInt(scanner.nextLine());
                adresa = new Adresa(oras, strada, numar);
            } else {
                System.out.print("Tip vehicul: ");
                tipVehicul = scanner.nextLine();
            }
            System.out.print("Parola: ");
            String parola = scanner.nextLine();
            if (optiune == 1) {
                service.inregistrareUser(new Client(nume, email, telefon, parola, adresa));
            } else {
                service.inregistrareUser(new Curier(nume, email, telefon, parola, tipVehicul));
            }
        } catch (NumberFormatException e) {
                System.out.println("Optiunea trebuia sa fie un numar intreg.");
        }
    }
}
