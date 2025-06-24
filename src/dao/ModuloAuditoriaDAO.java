package dao;

import conexion.ConexionBD;
import modelo.ModuloAuditoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModuloAuditoriaDAO {

    public List<ModuloAuditoria> obtenerTodosModulos() {
        List<ModuloAuditoria> modulos = new ArrayList<>();
        String sql = "SELECT ModAudiCod, ModAudiDesc, ModAudiEstReg FROM AUD_MODULO";
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ModuloAuditoria modulo = new ModuloAuditoria();
                modulo.setModAudiCod(rs.getInt("ModAudiCod"));
                modulo.setModAudiDesc(rs.getString("ModAudiDesc"));
                modulo.setModAudiEstReg(rs.getString("ModAudiEstReg").charAt(0));
                modulos.add(modulo);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los módulos de auditoría: " + e.getMessage());
        }
        return modulos;
    }

    public ModuloAuditoria obtenerModuloPorCodigo(int codigo) {
        String sql = "SELECT ModAudiCod, ModAudiDesc, ModAudiEstReg FROM AUD_MODULO WHERE ModAudiCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, codigo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ModuloAuditoria modulo = new ModuloAuditoria();
                    modulo.setModAudiCod(rs.getInt("ModAudiCod"));
                    modulo.setModAudiDesc(rs.getString("ModAudiDesc"));
                    modulo.setModAudiEstReg(rs.getString("ModAudiEstReg").charAt(0));
                    return modulo;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener módulo por código: " + e.getMessage());
        }
        return null;
    }

    public boolean insertarModulo(ModuloAuditoria modulo) {
        String sql = "INSERT INTO AUD_MODULO (ModAudiCod, ModAudiDesc, ModAudiEstReg) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, modulo.getModAudiCod());
            pstmt.setString(2, modulo.getModAudiDesc());
            pstmt.setString(3, String.valueOf(modulo.getModAudiEstReg()));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar módulo de auditoría: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarModulo(ModuloAuditoria modulo) {
        String sql = "UPDATE AUD_MODULO SET ModAudiDesc = ?, ModAudiEstReg = ? WHERE ModAudiCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, modulo.getModAudiDesc());
            pstmt.setString(2, String.valueOf(modulo.getModAudiEstReg()));
            pstmt.setInt(3, modulo.getModAudiCod());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar módulo de auditoría: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarLogicamenteModulo(int codigo) {
        String sql = "UPDATE AUD_MODULO SET ModAudiEstReg = '*' WHERE ModAudiCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente módulo: " + e.getMessage());
            return false;
        }
    }

    public boolean inactivarModulo(int codigo) {
        String sql = "UPDATE AUD_MODULO SET ModAudiEstReg = 'I' WHERE ModAudiCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al inactivar módulo: " + e.getMessage());
            return false;
        }
    }

    public boolean reactivarModulo(int codigo) {
        String sql = "UPDATE AUD_MODULO SET ModAudiEstReg = 'A' WHERE ModAudiCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al reactivar módulo: " + e.getMessage());
            return false;
        }
    }
}