public class Admin extends User{
    Admin() {
        super();
        this.tipUser = "admin";
    }

    public Admin(String nume, String email, String telefon, String parola) {
        super(nume, email, telefon, parola);
        this.tipUser = "admin";
    }
}
