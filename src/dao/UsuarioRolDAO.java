package dao;

import conexion.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.UsuarioRol;

public class UsuarioRolDAO {

    // Obtener todos los roles de usuario
    public List<UsuarioRol> obtenerTodosRoles() {
        List<UsuarioRol> roles = new ArrayList<>();
        String sql = "SELECT RolUsuCod, RolUsuDesc, RolUsuProEstReg FROM usuario_rol";

        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                UsuarioRol rol = new UsuarioRol();
                rol.setRolUsuCod(rs.getInt("RolUsuCod"));
                rol.setRolUsuDesc(rs.getString("RolUsuDesc"));
                rol.setRolUsuProEstReg(rs.getString("RolUsuProEstReg").charAt(0));
                roles.add(rol);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los roles de usuario: " + e.getMessage());
        }

        return roles;
    }

    // Obtener un rol de usuario por código
    public UsuarioRol obtenerRolPorCodigo(int codigo) {
        String sql = "SELECT RolUsuCod, RolUsuDesc, RolUsuProEstReg FROM usuario_rol WHERE RolUsuCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    UsuarioRol rol = new UsuarioRol();
                    rol.setRolUsuCod(rs.getInt("RolUsuCod"));
                    rol.setRolUsuDesc(rs.getString("RolUsuDesc"));
                    rol.setRolUsuProEstReg(rs.getString("RolUsuProEstReg").charAt(0));
                    return rol;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener rol por código: " + e.getMessage());
        }

        return null;
    }

    public List<UsuarioRol> obtenerRolesActivos() {
        List<UsuarioRol> roles = new ArrayList<>();
        String sql = "SELECT RolUsuCod, RolUsuDesc, RolUsuProEstReg FROM usuario_rol WHERE RolUsuProEstReg = 'A' ORDER BY RolUsuCod";

        try (Connection conn = ConexionBD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UsuarioRol rol = new UsuarioRol();
                rol.setRolUsuCod(rs.getInt("RolUsuCod"));
                rol.setRolUsuDesc(rs.getString("RolUsuDesc"));
                rol.setRolUsuProEstReg(rs.getString("RolUsuProEstReg").charAt(0));
                roles.add(rol);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener roles activos: " + e.getMessage());
        }

        return roles;
    }



    // Insertar un nuevo rol de usuario
    public boolean insertarRol(UsuarioRol rol) {
        String sql = "INSERT INTO usuario_rol (RolUsuDesc, RolUsuProEstReg) VALUES (?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, rol.getRolUsuDesc());
            pstmt.setString(2, String.valueOf(rol.getRolUsuProEstReg()));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        rol.setRolUsuCod(generatedKeys.getInt(1)); // Asignar la PK generada al objeto
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al insertar rol de usuario: " + e.getMessage());
            e.printStackTrace(); // Para ver más detalles del error
            return false;
        }
    }

    // Actualizar un rol de usuario
    public boolean actualizarRol(UsuarioRol rol) {
        String sql = "UPDATE usuario_rol SET RolUsuDesc = ?, RolUsuProEstReg = ? WHERE RolUsuCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, rol.getRolUsuDesc());
            pstmt.setString(2, String.valueOf(rol.getRolUsuProEstReg()));
            pstmt.setInt(3, rol.getRolUsuCod());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar rol de usuario: " + e.getMessage());
            return false;
        }
    }

    // Eliminar un rol lógicamente (cambiar estado)
    public boolean eliminarLogicamenteRol(int codigo) {
        String sql = "UPDATE usuario_rol SET RolUsuProEstReg = '*' WHERE RolUsuCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente rol: " + e.getMessage());
            return false;
        }
    }

    // Inactivar un rol de usuario (poner estado como 'I')
    public boolean inactivarRol(int codigo) {
        String sql = "UPDATE usuario_rol SET RolUsuProEstReg = 'I' WHERE RolUsuCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al inactivar rol: " + e.getMessage());
            return false;
        }
    }

    // Reactivar un rol de usuario (poner estado como 'A')
    public boolean reactivarRol(int codigo) {
        String sql = "UPDATE usuario_rol SET RolUsuProEstReg = 'A' WHERE RolUsuCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al reactivar rol: " + e.getMessage());
            return false;
        }
    }
}
