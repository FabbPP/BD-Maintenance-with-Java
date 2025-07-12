package dao;

import conexion.ConexionBD;
import modelo.ClasificacionProducto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClasificacionProductoDAO {

    public List<ClasificacionProducto> obtenerTodasClasificaciones() {
        List<ClasificacionProducto> clasificaciones = new ArrayList<>();
        String sql = "SELECT ClasProCod, ClasProDesc, ClasProEstReg FROM CLASIFICACION_PRODUCTO";

        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ClasificacionProducto clasificacion = new ClasificacionProducto();
                clasificacion.setClasProCod(rs.getInt("ClasProCod"));
                clasificacion.setClasProDesc(rs.getString("ClasProDesc"));
                clasificacion.setClasProEstReg(rs.getString("ClasProEstReg").charAt(0));
                clasificaciones.add(clasificacion);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las clasificaciones de producto: " + e.getMessage());
        }

        return clasificaciones;
    }

    public ClasificacionProducto obtenerClasificacionPorCodigo(int codigo) {
        String sql = "SELECT ClasProCod, ClasProDesc, ClasProEstReg FROM CLASIFICACION_PRODUCTO WHERE ClasProCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ClasificacionProducto clasificacion = new ClasificacionProducto();
                    clasificacion.setClasProCod(rs.getInt("ClasProCod"));
                    clasificacion.setClasProDesc(rs.getString("ClasProDesc"));
                    clasificacion.setClasProEstReg(rs.getString("ClasProEstReg").charAt(0));
                    return clasificacion;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener clasificación por código: " + e.getMessage());
        }

        return null;
    }

    public boolean insertarClasificacion(ClasificacionProducto clasificacion) {
        String sql = "INSERT INTO CLASIFICACION_PRODUCTO (ClasProDesc, ClasProEstReg) VALUES (?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, clasificacion.getClasProDesc());
            pstmt.setString(2, String.valueOf(clasificacion.getClasProEstReg()));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        clasificacion.setClasProCod(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al insertar clasificación de producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarClasificacion(ClasificacionProducto clasificacion) {
        String sql = "UPDATE CLASIFICACION_PRODUCTO SET ClasProDesc = ?, ClasProEstReg = ? WHERE ClasProCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, clasificacion.getClasProDesc());
            pstmt.setString(2, String.valueOf(clasificacion.getClasProEstReg()));
            pstmt.setInt(3, clasificacion.getClasProCod());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar clasificación de producto: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarLogicamenteClasificacion(int codigo) {
        String sql = "UPDATE CLASIFICACION_PRODUCTO SET ClasProEstReg = '*' WHERE ClasProCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente clasificación: " + e.getMessage());
            return false;
        }
    }

    public boolean inactivarClasificacion(int codigo) {
        String sql = "UPDATE CLASIFICACION_PRODUCTO SET ClasProEstReg = 'I' WHERE ClasProCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al inactivar clasificación: " + e.getMessage());
            return false;
        }
    }

    public boolean reactivarClasificacion(int codigo) {
        String sql = "UPDATE CLASIFICACION_PRODUCTO SET ClasProEstReg = 'A' WHERE ClasProCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al reactivar clasificación: " + e.getMessage());
            return false;
        }
    }
}
