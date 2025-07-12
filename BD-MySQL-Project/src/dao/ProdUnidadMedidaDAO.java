package dao;

import conexion.ConexionBD;
import modelo.ProdUnidadMedida;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdUnidadMedidaDAO {

    // Método para obtener todas las unidades de medida
    public List<ProdUnidadMedida> obtenerTodasUnidades() {
        List<ProdUnidadMedida> unidades = new ArrayList<>();
        String sql = "SELECT UniMedProCod, UniMedProDesc, UniMedEstReg FROM prod_unidad_medida";

        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ProdUnidadMedida unidad = new ProdUnidadMedida();
                unidad.setUniMedProCod(rs.getInt("UniMedProCod"));
                unidad.setUniMedProDesc(rs.getString("UniMedProDesc"));
                unidad.setUniMedProEstReg(rs.getString("UniMedEstReg").charAt(0));
                unidades.add(unidad);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las unidades de medida: " + e.getMessage());
        }

        return unidades;
    }

    // Método para obtener una unidad de medida por código
    public ProdUnidadMedida obtenerUnidadPorCodigo(int codigo) {
        String sql = "SELECT UniMedProCod, UniMedProDesc, UniMedEstReg FROM prod_unidad_medida WHERE UniMedProCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ProdUnidadMedida unidad = new ProdUnidadMedida();
                    unidad.setUniMedProCod(rs.getInt("UniMedProCod"));
                    unidad.setUniMedProDesc(rs.getString("UniMedProDesc"));
                    unidad.setUniMedProEstReg(rs.getString("UniMedEstReg").charAt(0));
                    return unidad;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener unidad de medida por código: " + e.getMessage());
        }

        return null;
    }

    // Método para insertar una nueva unidad de medida
    public boolean insertarUnidad(ProdUnidadMedida unidad) {
        String sql = "INSERT INTO prod_unidad_medida (UniMedProDesc, UniMedEstReg) VALUES (?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, unidad.getUniMedProDesc());
            pstmt.setString(2, String.valueOf(unidad.getUniMedProEstReg()));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        unidad.setUniMedProCod(generatedKeys.getInt(1)); // Asignar la PK generada al objeto
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al insertar unidad de medida: " + e.getMessage());
            e.printStackTrace(); // Para ver más detalles del error
            return false;
        }
    }

    // Método para actualizar una unidad de medida existente
    public boolean actualizarUnidad(ProdUnidadMedida unidad) {
        String sql = "UPDATE prod_unidad_medida SET UniMedProDesc = ?, UniMedEstReg = ? WHERE UniMedProCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, unidad.getUniMedProDesc());
            pstmt.setString(2, String.valueOf(unidad.getUniMedProEstReg()));
            pstmt.setInt(3, unidad.getUniMedProCod());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar unidad de medida: " + e.getMessage());
            return false;
        }
    }

    // Método para eliminar lógicamente una unidad de medida (marcar como eliminada)
    public boolean eliminarLogicamenteUnidad(int codigo) {
        String sql = "UPDATE prod_unidad_medida SET UniMedEstReg = '*' WHERE UniMedProCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente unidad de medida: " + e.getMessage());
            return false;
        }
    }

    // Método para inactivar una unidad de medida (marcar como inactiva)
    public boolean inactivarUnidad(int codigo) {
        String sql = "UPDATE prod_unidad_medida SET UniMedEstReg = 'I' WHERE UniMedProCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al inactivar unidad de medida: " + e.getMessage());
            return false;
        }
    }

    // Método para reactivar una unidad de medida (marcar como activa)
    public boolean reactivarUnidad(int codigo) {
        String sql = "UPDATE prod_unidad_medida SET UniMedEstReg = 'A' WHERE UniMedProCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al reactivar unidad de medida: " + e.getMessage());
            return false;
        }
    }
}
