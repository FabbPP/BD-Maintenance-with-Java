package dao;

import conexion.ConexionBD;
import modelo.DisponibilidadProducto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DisponibilidadProductoDAO {

    public List<DisponibilidadProducto> obtenerTodasDisponibilidades() {
        List<DisponibilidadProducto> disponibilidades = new ArrayList<>();
        String sql = "SELECT DispoProdCod, DispoProdDesc, DispoProdEstReg FROM PROD_DISPO";
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                DisponibilidadProducto disponibilidad = new DisponibilidadProducto();
                disponibilidad.setDispoProdCod(rs.getByte("DispoProdCod"));
                disponibilidad.setDispoProdDesc(rs.getString("DispoProdDesc"));
                disponibilidad.setDispoProdEstReg(rs.getString("DispoProdEstReg").charAt(0));
                disponibilidades.add(disponibilidad);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las disponibilidades de producto: " + e.getMessage());
        }
        return disponibilidades;
    }

    public DisponibilidadProducto obtenerDisponibilidadPorCodigo(byte codigo) {
        String sql = "SELECT DispoProdCod, DispoProdDesc, DispoProdEstReg FROM PROD_DISPO WHERE DispoProdCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setByte(1, codigo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    DisponibilidadProducto disponibilidad = new DisponibilidadProducto();
                    disponibilidad.setDispoProdCod(rs.getByte("DispoProdCod"));
                    disponibilidad.setDispoProdDesc(rs.getString("DispoProdDesc"));
                    disponibilidad.setDispoProdEstReg(rs.getString("DispoProdEstReg").charAt(0));
                    return disponibilidad;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener disponibilidad por código: " + e.getMessage());
        }
        return null;
    }

    public boolean insertarDisponibilidad(DisponibilidadProducto disponibilidad) {
        String sql = "INSERT INTO PROD_DISPO (DispoProdCod, DispoProdDesc, DispoProdEstReg) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setByte(1, disponibilidad.getDispoProdCod());
            pstmt.setString(2, disponibilidad.getDispoProdDesc());
            pstmt.setString(3, String.valueOf(disponibilidad.getDispoProdEstReg()));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar disponibilidad de producto: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarDisponibilidad(DisponibilidadProducto disponibilidad) {
        String sql = "UPDATE PROD_DISPO SET DispoProdDesc = ?, DispoProdEstReg = ? WHERE DispoProdCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, disponibilidad.getDispoProdDesc());
            pstmt.setString(2, String.valueOf(disponibilidad.getDispoProdEstReg()));
            pstmt.setByte(3, disponibilidad.getDispoProdCod());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar disponibilidad de producto: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarLogicamenteDisponibilidad(byte codigo) {
        String sql = "UPDATE PROD_DISPO SET DispoProdEstReg = '*' WHERE DispoProdCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setByte(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente disponibilidad: " + e.getMessage());
            return false;
        }
    }

    public boolean inactivarDisponibilidad(byte codigo) {
        String sql = "UPDATE PROD_DISPO SET DispoProdEstReg = 'I' WHERE DispoProdCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setByte(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al inactivar disponibilidad: " + e.getMessage());
            return false;
        }
    }

    public boolean reactivarDisponibilidad(byte codigo) {
        String sql = "UPDATE PROD_DISPO SET DispoProdEstReg = 'A' WHERE DispoProdCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setByte(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al reactivar disponibilidad: " + e.getMessage());
            return false;
        }
    }
}