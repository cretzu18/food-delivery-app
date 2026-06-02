import java.util.List;

public class CurierActionService {
    private ComandaService comandaService;
    private UserService userService;

    public CurierActionService() {
        this.comandaService = new ComandaService();
        this.userService = new UserService();
    }

    private Curier checkCurier() {
        User userLogat = AuthService.getInstance().getUserLogat();
        if (userLogat == null || !(userLogat instanceof Curier)) {
            System.out.println("Eroare: Doar curierul poate efectua aceasta actiune!");
            return null;
        }
        return (Curier) userLogat;
    }

    public void curierPreiaComanda(int idComanda) {
        Curier curier = checkCurier();
        if (curier == null) return;

        if (!curier.esteDisponibil()) {
            System.out.println("Eroare: Esti deja intr-o cursa! Finalizeaza comanda curenta inainte de a prelua alta.");
            return;
        }

        Comanda comanda = comandaService.readOneEntity(idComanda);

        if (comanda == null) {
            System.out.println("Eroare: Comanda " + idComanda + " nu exista in baza de date!");
            return;
        }

        if (!comanda.getStatus().equals("IN_ASTEPTARE")) {
            System.out.println("Eroare: Comanda " + idComanda + " nu este disponibila (Status: " + comanda.getStatus() + ")!");
            return;
        }

        // Modificari in memorie
        comanda.setCurier(curier);
        comanda.setStatus("IN_LIVRARE");
        curier.setEsteDisponibil(false);
        curier.setComandaCurenta(comanda);

        // Salvare modificari in BD
        comandaService.update(comanda);
        userService.update(curier);

        System.out.println("Succes: Ai preluat Comanda " + idComanda);
    }

    public void curierFinalizeazaComanda() {
        Curier curier = checkCurier();
        if (curier == null) return;

        Comanda comanda = curier.getComandaCurenta();

        // In cazul in care curierul abia s-a logat si avea o comanda in livrare, trebuie sa o incarcam din BD
        if (comanda == null) {
            List<Comanda> comenzi = comandaService.read();
            for (Comanda c : comenzi) {
                if (c.getCurier() != null && c.getCurier().getId() == curier.getId() && c.getStatus().equals("IN_LIVRARE")) {
                    comanda = c;
                    curier.setComandaCurenta(c);
                    curier.setEsteDisponibil(false);
                    break;
                }
            }
        }

        if (comanda == null) {
            System.out.println("Eroare: Nu ai nicio comanda in livrare!");
            return;
        }

        comanda.setStatus("FINALIZAT");
        curier.setComandaCurenta(null);
        curier.setEsteDisponibil(true);

        comandaService.update(comanda);
        userService.update(curier);

        System.out.println("Succes: Comanda " + comanda.getId() + " a fost finalizata.");
    }

    public void curierAfiseazaComenziDisponibile() {
        boolean exista = false;
        List<Comanda> comenzi = comandaService.read();

        for (Comanda c: comenzi) {
            if (c.getStatus().equals("IN_ASTEPTARE")) {
                System.out.println(c);
                exista = true;
            }
        }

        if (!exista) {
            System.out.println("Nu exista comenzi disponibile in asteptare.");
        }
    }
}
