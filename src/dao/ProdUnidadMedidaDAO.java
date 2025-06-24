package dao;

import conexion.ConexionBD;
import modelo.ProdUnidadMedida;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdUnidadMedidaDAO {

    public List<ProdUnidadMedida> obtenerTodasUnidades() {
        List<ProdUnidadMedida> unidades = new ArrayList<>();
        String sql = "SELECT UniMedProCod, UniMedProDesc, UniMedProEstReg FROM PROD_UNIDAD_MEDIDA";
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ProdUnidadMedida unidad = new ProdUnidadMedida();
                unidad.setUniMedProCod(rs.getString("UniMedProCod"));
                unidad.setUniMedProDesc(rs.getString("UniMedProDesc"));
                unidad.setUniMedProEstReg(rs.getString("UniMedProEstReg").charAt(0));
                unidades.add(unidad);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las unidades de medida de producto: " + e.getMessage());
        }
        return unidades;
    }

    public ProdUnidadMedida obtenerUnidadPorCodigo(String codigo) {
        String sql = "SELECT UniMedProCod, UniMedProDesc, UniMedProEstReg FROM PROD_UNIDAD_MEDIDA WHERE UniMedProCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ProdUnidadMedida unidad = new ProdUnidadMedida();
                    unidad.setUniMedProCod(rs.getString("UniMedProCod"));
                    unidad.setUniMedProDesc(rs.getString("UniMedProDesc"));
                    unidad.setUniMedProEstReg(rs.getString("UniMedProEstReg").charAt(0));
                    return unidad;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener unidad de medida por código: " + e.getMessage());
        }
        return null;
    }

    public boolean insertarUnidad(ProdUnidadMedida unidad) {
        String sql = "INSERT INTO PROD_UNIDAD_MEDIDA (UniMedProCod, UniMedProDesc, UniMedProEstReg) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, unidad.getUniMedProCod());
            pstmt.setString(2, unidad.getUniMedProDesc());
            pstmt.setString(3, String.valueOf(unidad.getUniMedProEstReg()));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("Error: El código '" + unidad.getUniMedProCod() + "' ya existe o no cumple con las restricciones de valor. " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.err.println("Error al insertar unidad de medida de producto: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarUnidad(ProdUnidadMedida unidad) {
        String sql = "UPDATE PROD_UNIDAD_MEDIDA SET UniMedProDesc = ?, UniMedProEstReg = ? WHERE UniMedProCod = ?";
        try (Connection conn = ConexionBD.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) { 

            pstmt.setString(1, unidad.getUniMedProDesc());
            pstmt.setString(2, String.valueOf(unidad.getUniMedProEstReg())); // convertir char a String
            pstmt.setString(3, unidad.getUniMedProCod()); // corregido: este es el código

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar unidad de medida de producto: " + e.getMessage());
            return false;
        }
    }


    public boolean eliminarLogicamenteUnidad(String codigo) {
        String sql = "UPDATE PROD_UNIDAD_MEDIDA SET UniMedProEstReg = '*' WHERE UniMedProCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente unidad de medida: " + e.getMessage());
            return false;
        }
    }

    public boolean inactivarUnidad(String codigo) {
        String sql = "UPDATE PROD_UNIDAD_MEDIDA SET UniMedProEstReg = 'I' WHERE UniMedProCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al inactivar unidad de medida: " + e.getMessage());
            return false;
        }
    }

    public boolean reactivarUnidad(String codigo) {
        String sql = "UPDATE PROD_UNIDAD_MEDIDA SET UniMedProEstReg = 'A' WHERE UniMedProCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al reactivar unidad de medida: " + e.getMessage());
            return false;
        }
    }
}
