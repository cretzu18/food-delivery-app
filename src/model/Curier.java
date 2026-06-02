package model;
public class Curier extends User{
    private String tipVehicul;
    private boolean esteDisponibil;
    private Comanda comandaCurenta;

    public Curier(String nume, String telefon, String email, String parola, String tipVehicul) {
        super(nume, telefon, email, parola);
        this.tipVehicul = tipVehicul;
        this.esteDisponibil = true;
        this.comandaCurenta = null;
    }

    public Comanda getComandaCurenta() {
        return comandaCurenta;
    }

    public void setComandaCurenta(Comanda comandaCurenta) {
        this.comandaCurenta = comandaCurenta;
    }

    public String getTipVehicul() {
        return tipVehicul;
    }

    public void setTipVehicul(String tipVehicul) {
        this.tipVehicul = tipVehicul;
    }

    public boolean esteDisponibil() {
        return esteDisponibil;
    }

    public void setEsteDisponibil(boolean esteDisponibil) {
        this.esteDisponibil = esteDisponibil;
    }

    @Override
    public String toString() {
        String status = esteDisponibil ? "Disponibil" : "In cursa";
        return String.format("Curier: %s [%s] - Status: %s",
                getNume(), tipVehicul, status);
    }
}

