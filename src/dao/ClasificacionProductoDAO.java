package dao;

import conexion.ConexionBD;
import modelo.ClasificacionProducto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClasificacionProductoDAO {

    public List<ClasificacionProducto> obtenerTodasClasificaciones() {
        List<ClasificacionProducto> lista = new ArrayList<>();
        String sql = "SELECT ClasProCod, ClasProDesc, ClasProEstReg FROM CLASIFICACION_PRODUCTO";
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ClasificacionProducto clas = new ClasificacionProducto();
                clas.setClasProCod(rs.getString("ClasProCod").charAt(0));
                clas.setClasProDesc(rs.getString("ClasProDesc"));
                clas.setClasProEstReg(rs.getString("ClasProEstReg").charAt(0));
                lista.add(clas);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener clasificaciones: " + e.getMessage());
        }
        return lista;
    }

    public ClasificacionProducto obtenerPorCodigo(char codigo) {
        String sql = "SELECT ClasProCod, ClasProDesc, ClasProEstReg FROM CLASIFICACION_PRODUCTO WHERE ClasProCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, String.valueOf(codigo));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new ClasificacionProducto(
                    rs.getString("ClasProCod").charAt(0),
                    rs.getString("ClasProDesc"),
                    rs.getString("ClasProEstReg").charAt(0)
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar clasificación: " + e.getMessage());
        }
        return null;
    }

    public boolean insertar(ClasificacionProducto clas) {
        String sql = "INSERT INTO CLASIFICACION_PRODUCTO (ClasProCod, ClasProDesc, ClasProEstReg) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, String.valueOf(clas.getClasProCod()));
            pstmt.setString(2, clas.getClasProDesc());
            pstmt.setString(3, String.valueOf(clas.getClasProEstReg()));
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar clasificación: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(ClasificacionProducto clas) {
        String sql = "UPDATE CLASIFICACION_PRODUCTO SET ClasProDesc = ?, ClasProEstReg = ? WHERE ClasProCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, clas.getClasProDesc());
            pstmt.setString(2, String.valueOf(clas.getClasProEstReg()));
            pstmt.setString(3, String.valueOf(clas.getClasProCod()));
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar clasificación: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarLogico(char codigo) {
        String sql = "UPDATE CLASIFICACION_PRODUCTO SET ClasProEstReg = '*' WHERE ClasProCod = ?";
        return ejecutarCambioEstado(sql, codigo);
    }

    public boolean inactivar(char codigo) {
        String sql = "UPDATE CLASIFICACION_PRODUCTO SET ClasProEstReg = 'I' WHERE ClasProCod = ?";
        return ejecutarCambioEstado(sql, codigo);
    }

    public boolean reactivar(char codigo) {
        String sql = "UPDATE CLASIFICACION_PRODUCTO SET ClasProEstReg = 'A' WHERE ClasProCod = ?";
        return ejecutarCambioEstado(sql, codigo);
    }

    private boolean ejecutarCambioEstado(String sql, char codigo) {
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, String.valueOf(codigo));
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado: " + e.getMessage());
            return false;
        }
    }
}
