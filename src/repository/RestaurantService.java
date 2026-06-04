package repository;
import model.*;
import java.sql.*;
import java.util.*;

public class RestaurantService implements GenericService<Restaurant> {
    
    private static RestaurantService instance;
    private RestaurantService() {}
    public static RestaurantService getInstance() {
        if (instance == null) {
            instance = new RestaurantService();
        }
        return instance;
    }

    @Override
    public void create(Restaurant entity) {
        String sql = "INSERT INTO restaurante (nume, specific, rating, numar_recenzii) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, entity.getNume());
            pstmt.setString(2, entity.getSpecific());
            pstmt.setDouble(3, entity.getRating());
            pstmt.setInt(4, entity.getNumarRecenzii());

            int rowsAffected = pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                }
            }
            
            System.out.println("S-a adaugat restaurantul: " + rowsAffected);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Restaurant> read() {
        String sql = "SELECT * FROM restaurante";
        List<Restaurant> restaurante = new ArrayList<>();
        
        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                Restaurant restaurant = new Restaurant(rs.getString("nume"), rs.getString("specific"), rs.getDouble("rating"), rs.getInt("numar_recenzii"));
                restaurant.setId(rs.getInt("id"));
                restaurante.add(restaurant);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return restaurante;
    }
    
    @Override
    public Restaurant readOneEntity(int id) {
        String sql = "SELECT * FROM restaurante WHERE id = ?";

        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                Restaurant restaurant = new Restaurant(rs.getString("nume"), rs.getString("specific"), rs.getDouble("rating"), rs.getInt("numar_recenzii"));
                restaurant.setId(id);
                return restaurant;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void update(Restaurant entity) {
        String sql = "UPDATE restaurante SET nume = ?, specific = ?, rating = ?, numar_recenzii = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setString(1, entity.getNume());
            pstmt.setString(2, entity.getSpecific());
            pstmt.setDouble(3, entity.getRating());
            pstmt.setInt(4, entity.getNumarRecenzii());
            pstmt.setInt(5, entity.getId());

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("S-a actualizat restaurantul: " + rowsAffected);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM restaurante WHERE id = ?";
        
        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("S-a sters restaurantul: " + rowsAffected);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
