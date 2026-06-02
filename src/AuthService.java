import java.util.List;

public class AuthService {
    private static AuthService instance;
    private User userLogat = null;
    private UserService userService;

    private AuthService() {
        this.userService = new UserService();
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public User getUserLogat() {
        return userLogat;
    }

    public void inregistrareUser(User u) {
        List<User> utilizatori = userService.read();
        
        for (User userDB : utilizatori) {
            if (userDB.getEmail().equals(u.getEmail())) {
                System.out.println("Email-ul " + u.getEmail() + " este deja utilizat pentru alt cont!");
                return;
            }
        }

        userService.create(u);
        System.out.println("Utilizatorul " + u.getNume() + " (" + u.getClass().getSimpleName() + ") inregistrat cu succes in baza de date.");
    }

    public boolean login(String email, String parola) {
        List<User> utilizatori = userService.read();
        
        for (User u : utilizatori) {
            if (u.getEmail().equals(email) && u.verificaParola(parola)) {
                userLogat = u;
                System.out.println("Logarea a avut succes! Bine ai revenit, " + userLogat.getNume() + "!");
                return true;
            }
        }
        
        System.out.println("Logarea a esuat! Email sau parola incorecta.");
        return false;
    }

    public void logout() {
        if (userLogat != null) {
            System.out.println("La revedere, " + userLogat.getNume() + "!");
            userLogat = null;
            return;
        }

        System.out.println("Nu este niciun cont logat.");
    }
}
