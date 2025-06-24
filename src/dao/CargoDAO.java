package dao;

import conexion.ConexionBD;
import modelo.Cargo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CargoDAO {

    public List<Cargo> obtenerTodosCargos() {
        List<Cargo> cargos = new ArrayList<>();
        String sql = "SELECT CarCod, CarNom, CarSue, CarEstReg FROM cargo";
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cargo cargo = new Cargo();
                cargo.setCarCod(rs.getInt("CarCod"));
                cargo.setCarDesc(rs.getString("CarNom"));
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
        String sql = "SELECT CarCod, CarNom, CarSue, CarEstReg FROM cargo WHERE CarCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, codigo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Cargo cargo = new Cargo();
                    cargo.setCarCod(rs.getInt("CarCod"));
                    cargo.setCarDesc(rs.getString("CarNom"));
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

    public boolean insertarCargo(Cargo cargo) {
        String sql = "INSERT INTO cargo (CarCod, CarNom, CarSue, CarEstReg) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cargo.getCarCod());
            pstmt.setString(2, cargo.getCarDesc());
            pstmt.setDouble(3, cargo.getCarSue());
            pstmt.setString(4, String.valueOf(cargo.getCarEstReg()));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar cargo: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarCargo(Cargo cargo) {
        String sql = "UPDATE cargo SET CarNom = ?, CarSue = ?, CarEstReg = ? WHERE CarCod = ?";
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
        String sql = "UPDATE cargo SET CarEstReg = '*' WHERE CarCod = ?";
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
        String sql = "UPDATE cargo SET CarEstReg = 'I' WHERE CarCod = ?";
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
        String sql = "UPDATE cargo SET CarEstReg = 'A' WHERE CarCod = ?";
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
