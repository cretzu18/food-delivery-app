public abstract class User {
    private int id;
    private String nume;
    private String telefon;
    private String email;
    private String parola;

    public User(String nume, String email, String telefon, String parola) {
        this.nume = nume;
        this.telefon = telefon;
        this.email = email;
        this.parola = parola;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean verificaParola(String parola) {
        return this.parola.equals(parola);
    }
}
