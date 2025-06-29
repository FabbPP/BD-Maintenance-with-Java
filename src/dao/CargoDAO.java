package dao;

import conexion.ConexionBD;
import modelo.Cargo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CargoDAO {

    public List<Cargo> obtenerTodosCargos() {
        List<Cargo> cargos = new ArrayList<>();
        String sql = "SELECT CarCod, CarDesc, CarSue, CarEstReg FROM CARGO";

        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cargo cargo = new Cargo();
                cargo.setCarCod(rs.getInt("CarCod"));
                cargo.setCarDesc(rs.getString("CarDesc"));
                cargo.setCarSue(rs.getDouble("CarSue"));
                cargo.setCarEstReg(rs.getString("CarEstReg").charAt(0));
                cargos.add(cargo);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los cargos: " + e.getMessage());
        }

        return cargos;
    }

    public Cargo obtenerCargoPorCodigo(int codigo) {
        String sql = "SELECT CarCod, CarDesc, CarSue, CarEstReg FROM CARGO WHERE CarCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Cargo cargo = new Cargo();
                    cargo.setCarCod(rs.getInt("CarCod"));
                    cargo.setCarDesc(rs.getString("CarDesc"));
                    cargo.setCarSue(rs.getDouble("CarSue"));
                    cargo.setCarEstReg(rs.getString("CarEstReg").charAt(0));
                    return cargo;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cargo por código: " + e.getMessage());
        }

        return null;
    }
    
    public List<Cargo> obtenerCargosActivos() {
        List<Cargo> cargos = new ArrayList<>();
        String sql = "SELECT CarCod, CarDesc, CarSue, CarEstReg FROM CARGO WHERE CarEstReg = 'A' ORDER BY CarCod";
        
        try (Connection conn = ConexionBD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                int carCod = rs.getInt("CarCod");
                String carDesc = rs.getString("CarDesc");
                double carSue = rs.getDouble("CarSue"); 
                char carEstReg = rs.getString("CarEstReg").charAt(0); 

                Cargo cargo = new Cargo(carCod, carDesc, carSue, carEstReg);
                cargos.add(cargo);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cargos activos: " + e.getMessage());
        }
        return cargos;
    }

    public boolean insertarCargo(Cargo cargo) {
        String sql = "INSERT INTO CARGO (CarDesc, CarSue, CarEstReg) VALUES (?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, cargo.getCarDesc());
            pstmt.setDouble(2, cargo.getCarSue());
            pstmt.setString(3, String.valueOf(cargo.getCarEstReg()));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        cargo.setCarCod(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al insertar cargo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarCargo(Cargo cargo) {
        String sql = "UPDATE CARGO SET CarDesc = ?, CarSue = ?, CarEstReg = ? WHERE CarCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cargo.getCarDesc());
            pstmt.setDouble(2, cargo.getCarSue());
            pstmt.setString(3, String.valueOf(cargo.getCarEstReg()));
            pstmt.setInt(4, cargo.getCarCod());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar cargo: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarLogicamenteCargo(int codigo) {
        String sql = "UPDATE CARGO SET CarEstReg = '*' WHERE CarCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente cargo: " + e.getMessage());
            return false;
        }
    }

    public boolean inactivarCargo(int codigo) {
        String sql = "UPDATE CARGO SET CarEstReg = 'I' WHERE CarCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al inactivar cargo: " + e.getMessage());
            return false;
        }
    }

    public boolean reactivarCargo(int codigo) {
        String sql = "UPDATE CARGO SET CarEstReg = 'A' WHERE CarCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al reactivar cargo: " + e.getMessage());
            return false;
        }
    }
}
