import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProdusService implements GenericService<Produs> {

    @Override
    public void create(Produs entity) {
        String sql = "INSERT INTO produse (restaurant_id, nume, descriere, pret, calorii, tip_produs, mancare_gramaj, bautura_volum, bautura_contine_alcool) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (entity.getRestaurant() != null) {
                pstmt.setInt(1, entity.getRestaurant().getId());
            } else {
                throw new SQLException("Produsul trebuie sa apartina de un restaurant.");
            }
            pstmt.setString(2, entity.getNume());
            pstmt.setString(3, entity.getDescriere());
            pstmt.setDouble(4, entity.getPret());
            pstmt.setInt(5, entity.getCalorii());
            pstmt.setString(6, entity.getClass().getSimpleName()); // 'Mancare' sau 'Bautura'
            
            // Default null
            pstmt.setNull(7, java.sql.Types.DECIMAL);
            pstmt.setNull(8, java.sql.Types.INTEGER);
            pstmt.setNull(9, java.sql.Types.BOOLEAN);

            if (entity instanceof Mancare) {
                Mancare m = (Mancare) entity;
                pstmt.setDouble(7, m.getGramaj());
            } else if (entity instanceof Bautura) {
                Bautura b = (Bautura) entity;
                pstmt.setInt(8, b.getVolum());
                pstmt.setBoolean(9, b.contineAlcool());
            }

            int rowsAffected = pstmt.executeUpdate();
            
            // Setam id-ul generat inapoi in obiect
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                }
            }
            
            System.out.println("S-a adaugat produsul: " + rowsAffected);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Produs> read() {
        String sql = "SELECT * FROM produse";
        List<Produs> produse = new ArrayList<>();
        RestaurantService restaurantService = new RestaurantService();
        
        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                produse.add(mapResultSetToProdus(rs, restaurantService));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return produse;
    }

    @Override
    public Produs readOneEntity(int id) {
        String sql = "SELECT * FROM produse WHERE id = ?";
        RestaurantService restaurantService = new RestaurantService();

        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                return mapResultSetToProdus(rs, restaurantService);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void update(Produs entity) {
        String sql = "UPDATE produse SET nume = ?, descriere = ?, pret = ?, calorii = ?, mancare_gramaj = ?, bautura_volum = ?, bautura_contine_alcool = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setString(1, entity.getNume());
            pstmt.setString(2, entity.getDescriere());
            pstmt.setDouble(3, entity.getPret());
            pstmt.setInt(4, entity.getCalorii());
            
            pstmt.setNull(5, java.sql.Types.DECIMAL);
            pstmt.setNull(6, java.sql.Types.INTEGER);
            pstmt.setNull(7, java.sql.Types.BOOLEAN);

            if (entity instanceof Mancare) {
                Mancare m = (Mancare) entity;
                pstmt.setDouble(5, m.getGramaj());
            } else if (entity instanceof Bautura) {
                Bautura b = (Bautura) entity;
                pstmt.setInt(6, b.getVolum());
                pstmt.setBoolean(7, b.contineAlcool());
            }
            
            pstmt.setInt(8, entity.getId());

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("S-a actualizat produsul: " + rowsAffected);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM produse WHERE id = ?";
        
        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("S-a sters produsul: " + rowsAffected);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private Produs mapResultSetToProdus(ResultSet rs, RestaurantService rsService) throws SQLException {
        String tipProdus = rs.getString("tip_produs");
        Produs produs = null;
        
        String nume = rs.getString("nume");
        String descriere = rs.getString("descriere");
        double pret = rs.getDouble("pret");
        int calorii = rs.getInt("calorii");

        if (tipProdus.equals("Mancare")) {
            double gramaj = rs.getDouble("mancare_gramaj");
            produs = new Mancare(nume, descriere, pret, calorii, gramaj);
        } else if (tipProdus.equals("Bautura")) {
            int volum = rs.getInt("bautura_volum");
            boolean contineAlcool = rs.getBoolean("bautura_contine_alcool");
            produs = new Bautura(nume, descriere, pret, calorii, volum, contineAlcool);
        }

        if (produs != null) {
            produs.setId(rs.getInt("id"));
            int restaurantId = rs.getInt("restaurant_id");
            Restaurant r = rsService.readOneEntity(restaurantId);
            produs.setRestaurant(r);
        }

        return produs;
    }
}
