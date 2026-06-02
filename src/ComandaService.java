import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComandaService implements GenericService<Comanda> {

    @Override
    public void create(Comanda entity) {
        String sqlComanda = "INSERT INTO comenzi (client_id, curier_id, restaurant_id, adresa_oras, adresa_strada, adresa_numar, status, data_plasarii, pret_total) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlProduse = "INSERT INTO comanda_produs (comanda_id, produs_id, cantitate) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sqlComanda, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, entity.getClient().getId());
            if (entity.getCurier() != null) {
                pstmt.setInt(2, entity.getCurier().getId());
            } else {
                pstmt.setNull(2, java.sql.Types.INTEGER);
            }
            pstmt.setInt(3, entity.getRestaurant().getId());
            pstmt.setString(4, entity.getAdresa().getOras());
            pstmt.setString(5, entity.getAdresa().getStrada());
            pstmt.setInt(6, entity.getAdresa().getNumar());
            pstmt.setString(7, entity.getStatus());
            pstmt.setTimestamp(8, Timestamp.valueOf(entity.getDataPlasarii()));
            pstmt.setDouble(9, entity.getPretTotal());
            
            int rowsAffected = pstmt.executeUpdate();
            
            int comandaId = -1;
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    comandaId = generatedKeys.getInt(1);
                    entity.setId(comandaId);
                }
            }
            
            // Dupa ce am salvat comanda, salvam si produsele comandate in tabelul de legatura
            if (comandaId != -1 && entity.getProduseComandate() != null) {
                try (PreparedStatement pstmtProduse = DatabaseConnection.getInstance().getConnection().prepareStatement(sqlProduse)) {
                    for (Map.Entry<Produs, Integer> entry : entity.getProduseComandate().entrySet()) {
                        pstmtProduse.setInt(1, comandaId);
                        pstmtProduse.setInt(2, entry.getKey().getId());
                        pstmtProduse.setInt(3, entry.getValue());
                        pstmtProduse.executeUpdate();
                    }
                }
            }
            
            System.out.println("S-a adaugat comanda: " + rowsAffected);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Comanda> read() {
        String sql = "SELECT * FROM comenzi";
        List<Comanda> comenzi = new ArrayList<>();
        
        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                comenzi.add(mapResultSetToComanda(rs));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return comenzi;
    }

    @Override
    public Comanda readOneEntity(int id) {
        String sql = "SELECT * FROM comenzi WHERE id = ?";
        
        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                return mapResultSetToComanda(rs);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void update(Comanda entity) {
        String sql = "UPDATE comenzi SET curier_id = ?, status = ? WHERE id = ?";
        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            if (entity.getCurier() != null) {
                pstmt.setInt(1, entity.getCurier().getId());
            } else {
                pstmt.setNull(1, java.sql.Types.INTEGER);
            }
            pstmt.setString(2, entity.getStatus());
            pstmt.setInt(3, entity.getId());

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("S-a actualizat comanda: " + rowsAffected);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM comenzi WHERE id = ?";
        
        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("S-a sters comanda cu id-ul " + id + ": " + rowsAffected);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // Metoda auxiliara pentru a converti randul din DB intr-un obiect Comanda
    private Comanda mapResultSetToComanda(ResultSet rs) throws SQLException {
        UserService userService = new UserService();
        RestaurantService restaurantService = new RestaurantService();
        ProdusService produsService = new ProdusService();
        
        int id = rs.getInt("id");
        int clientId = rs.getInt("client_id");
        int restaurantId = rs.getInt("restaurant_id");
        
        Client client = (Client) userService.readOneEntity(clientId);
        Restaurant restaurant = restaurantService.readOneEntity(restaurantId);
        
        Adresa adresa = new Adresa(rs.getString("adresa_oras"), rs.getString("adresa_strada"), rs.getInt("adresa_numar"));
        
        // Cauta produsele pentru comanda
        Map<Produs, Integer> produseMap = new HashMap<>();
        String produseSql = "SELECT produs_id, cantitate FROM comanda_produs WHERE comanda_id = ?";
        try (PreparedStatement p = DatabaseConnection.getInstance().getConnection().prepareStatement(produseSql)) {
            p.setInt(1, id);
            ResultSet rsProd = p.executeQuery();
            while (rsProd.next()) {
                int prodId = rsProd.getInt("produs_id");
                int cantitate = rsProd.getInt("cantitate");
                Produs prod = produsService.readOneEntity(prodId);
                if (prod != null) {
                    produseMap.put(prod, cantitate);
                }
            }
        }
        
        Comanda comanda = new Comanda(client, restaurant, adresa, produseMap);
        comanda.setId(id);
        comanda.setStatus(rs.getString("status"));
        comanda.setDataPlasarii(rs.getTimestamp("data_plasarii").toLocalDateTime());
        comanda.setPretTotal(rs.getDouble("pret_total"));
        
        int curierId = rs.getInt("curier_id");
        if (!rs.wasNull()) {
            Curier curier = (Curier) userService.readOneEntity(curierId);
            comanda.setCurier(curier);
        }
        
        return comanda;
    }
}
