import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService implements GenericService<User> {
    
    @Override 
    public void create(User entity) {
        String sql = """
            INSERT INTO users(nume, telefon, email, parola, tip_user, adresa_oras, adresa_strada, adresa_numar, tip_vehicul)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setString(1, entity.getNume());
            pstmt.setString(2, entity.getTelefon());
            pstmt.setString(3, entity.getEmail());
            pstmt.setString(4, entity.getParola());
            pstmt.setString(5, entity.getClass().getSimpleName());
            
            // Setam null initial pt campurile specifice pentru client sau curier
            pstmt.setNull(6, java.sql.Types.VARCHAR);
            pstmt.setNull(7, java.sql.Types.VARCHAR);
            pstmt.setNull(8, java.sql.Types.INTEGER);
            pstmt.setNull(9, java.sql.Types.VARCHAR);

            if (entity instanceof Client) { // daca user-ul este client, setam si adresa
                Client c = (Client) entity;
                if (c.getAdresaImplicita() != null) {
                    pstmt.setString(6, c.getAdresaImplicita().getOras());
                    pstmt.setString(7, c.getAdresaImplicita().getStrada());
                    pstmt.setInt(8, c.getAdresaImplicita().getNumar());
                }
            } else if (entity instanceof Curier) { // daca user-ul este curier, setam si tipul vehiculului
                Curier c = (Curier) entity;
                pstmt.setString(9, c.getTipVehicul());
            }

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("S-a adaugat un user nou: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override 
    public List<User> read() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String tipUser = rs.getString("tip_user");
                
                switch(tipUser) {
                    case "Admin":
                        break;
                    case "Client":
                        Adresa adresa = new Adresa(rs.getString("adresa_oras"), rs.getString("adresa_strada"), rs.getInt("adresa_numar"));
                        Client client = new Client(rs.getString("nume"), rs.getString("email"), rs.getString("telefon"), rs.getString("parola"), adresa);
                        users.add(client);
                        break;
                    case "Curier":
                        Curier curier = new Curier(rs.getString("nume"), rs.getString("telefon"), rs.getString("email"), rs.getString("parola"), rs.getString("tip_vehicul"));
                        curier.setEsteDisponibil(rs.getBoolean("este_disponibil"));
                        users.add(curier);
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return users;
    }

    @Override
    public T readOneEntity(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) 
        {
            String tipUser = rs.getString("tip_user");

            switch(tipUser) {
                case "Admin":
                    break;
                case "Client":
                    Adresa adresa = new Adresa(rs.getString("adresa_oras"), rs.getString("adresa_strada"), rs.getInt("adresa_numar"));
                    Client client = new Client(rs.getString("nume"), rs.getString("email"), rs.getString("telefon"), rs.getString("parola"), adresa);
                    return (T) client;
                case "Curier":
                    Curier curier = new Curier(rs.getString("nume"), rs.getString("telefon"), rs.getString("email"), rs.getString("parola"), rs.getString("tip_vehicul"));
                    curier.setEsteDisponibil(rs.getBoolean("este_disponibil"));
                    return (T) curier;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override 
    public void update(User entity) {
        String sql = """
            UPDATE users SET nume=?, telefon=?, parola=?, 
            adresa_oras=?, adresa_strada=?, adresa_numar=?, tip_vehicul=?, este_disponibil=?
            WHERE id=?
        """;
        
        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setString(1, entity.getNume());
            pstmt.setString(2, entity.getTelefon());
            pstmt.setString(3, entity.getParola());
            
            pstmt.setNull(4, java.sql.Types.VARCHAR);
            pstmt.setNull(5, java.sql.Types.VARCHAR);
            pstmt.setNull(6, java.sql.Types.INTEGER);
            pstmt.setNull(7, java.sql.Types.VARCHAR);
            pstmt.setBoolean(8, true); // default pt curier
            
            if (entity instanceof Client) {
                Client c = (Client) entity;
                if (c.getAdresaImplicita() != null) {
                    pstmt.setString(4, c.getAdresaImplicita().getOras());
                    pstmt.setString(5, c.getAdresaImplicita().getStrada());
                    pstmt.setInt(6, c.getAdresaImplicita().getNumar());
                }
            } else if (entity instanceof Curier) {
                Curier c = (Curier) entity;
                pstmt.setString(7, c.getTipVehicul());
                pstmt.setBoolean(8, c.esteDisponibil());
            }
            
            pstmt.setInt(9, entity.getId()); 
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("S-a actualizat user-ul: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override 
    public void delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("S-a sters user-ul cu id-ul " + id + ": " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}