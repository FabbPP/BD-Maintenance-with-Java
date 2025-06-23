package dao;

import conexion.ConexionBD;
import modelo.UsuarioRol;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRolDAO {

    public List<UsuarioRol> obtenerTodosRoles() {
        List<UsuarioRol> lista = new ArrayList<>();
        String sql = "SELECT RolUsuCod, RolUsuDesc, RolUsuProEstReg FROM USUARIO_ROL";
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                UsuarioRol rol = new UsuarioRol();
                rol.setRolUsuCod(rs.getString("RolUsuCod").charAt(0));
                rol.setRolUsuDesc(rs.getString("RolUsuDesc"));
                rol.setRolUsuProEstReg(rs.getString("RolUsuProEstReg").charAt(0));
                lista.add(rol);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener roles de usuario: " + e.getMessage());
        }
        return lista;
    }

    public UsuarioRol obtenerRolPorCodigo(char codigo) {
        String sql = "SELECT RolUsuCod, RolUsuDesc, RolUsuProEstReg FROM USUARIO_ROL WHERE RolUsuCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, String.valueOf(codigo));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new UsuarioRol(
                        rs.getString("RolUsuCod").charAt(0),
                        rs.getString("RolUsuDesc"),
                        rs.getString("RolUsuProEstReg").charAt(0)
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar rol por código: " + e.getMessage());
        }
        return null;
    }

    public boolean insertarRol(UsuarioRol rol) {
        String sql = "INSERT INTO USUARIO_ROL (RolUsuCod, RolUsuDesc, RolUsuProEstReg) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, String.valueOf(rol.getRolUsuCod()));
            pstmt.setString(2, rol.getRolUsuDesc());
            pstmt.setString(3, String.valueOf(rol.getRolUsuProEstReg()));

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar rol: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarRol(UsuarioRol rol) {
        String sql = "UPDATE USUARIO_ROL SET RolUsuDesc = ?, RolUsuProEstReg = ? WHERE RolUsuCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, rol.getRolUsuDesc());
            pstmt.setString(2, String.valueOf(rol.getRolUsuProEstReg()));
            pstmt.setString(3, String.valueOf(rol.getRolUsuCod()));

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar rol: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarLogico(char codigo) {
        String sql = "UPDATE USUARIO_ROL SET RolUsuProEstReg = '*' WHERE RolUsuCod = ?";
        return ejecutarCambioEstado(sql, codigo);
    }

    public boolean inactivar(char codigo) {
        String sql = "UPDATE USUARIO_ROL SET RolUsuProEstReg = 'I' WHERE RolUsuCod = ?";
        return ejecutarCambioEstado(sql, codigo);
    }

    public boolean reactivar(char codigo) {
        String sql = "UPDATE USUARIO_ROL SET RolUsuProEstReg = 'A' WHERE RolUsuCod = ?";
        return ejecutarCambioEstado(sql, codigo);
    }

    private boolean ejecutarCambioEstado(String sql, char codigo) {
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, String.valueOf(codigo));
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado del rol: " + e.getMessage());
            return false;
        }
    }
}
