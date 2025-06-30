package dao;

import conexion.ConexionBD;
import modelo.UsuarioSistema;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioSistemaDAO {

    public List<UsuarioSistema> obtenerTodosUsuarios() {
        List<UsuarioSistema> usuarios = new ArrayList<>();
        String sql = "SELECT UsuCod, RepCod, UsuNom, UsuContr, UsuEstReg FROM USUARIOSISTEMA";

        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                UsuarioSistema usuario = new UsuarioSistema();
                usuario.setUsuCod(rs.getInt("UsuCod"));
                usuario.setRepCod(rs.getInt("RepCod"));
                usuario.setUsuNom(rs.getString("UsuNom"));
                usuario.setUsuContr(rs.getString("UsuContr"));
                usuario.setUsuEstReg(rs.getString("UsuEstReg").charAt(0));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los usuarios: " + e.getMessage());
        }

        return usuarios;
    }

    public UsuarioSistema obtenerUsuarioPorCodigo(int codigo) {
        String sql = "SELECT UsuCod, RepCod, UsuNom, UsuContr, UsuEstReg FROM USUARIOSISTEMA WHERE UsuCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    UsuarioSistema usuario = new UsuarioSistema();
                    usuario.setUsuCod(rs.getInt("UsuCod"));
                    usuario.setRepCod(rs.getInt("RepCod"));
                    usuario.setUsuNom(rs.getString("UsuNom"));
                    usuario.setUsuContr(rs.getString("UsuContr"));
                    usuario.setUsuEstReg(rs.getString("UsuEstReg").charAt(0));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por código: " + e.getMessage());
        }

        return null;
    }

    public UsuarioSistema obtenerUsuarioPorNombre(String nombreUsuario) {
        String sql = "SELECT UsuCod, RepCod, UsuNom, UsuContr, UsuEstReg FROM USUARIOSISTEMA WHERE UsuNom = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombreUsuario);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    UsuarioSistema usuario = new UsuarioSistema();
                    usuario.setUsuCod(rs.getInt("UsuCod"));
                    usuario.setRepCod(rs.getInt("RepCod"));
                    usuario.setUsuNom(rs.getString("UsuNom"));
                    usuario.setUsuContr(rs.getString("UsuContr"));
                    usuario.setUsuEstReg(rs.getString("UsuEstReg").charAt(0));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por nombre: " + e.getMessage());
        }

        return null;
    }

    public boolean insertarUsuario(UsuarioSistema usuario) {
        String sql = "INSERT INTO USUARIOSISTEMA (RepCod, UsuNom, UsuContr, UsuEstReg) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, usuario.getRepCod());
            pstmt.setString(2, usuario.getUsuNom());
            pstmt.setString(3, usuario.getUsuContr());
            pstmt.setString(5, String.valueOf(usuario.getUsuEstReg()));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        usuario.setUsuCod(generatedKeys.getInt(1)); // Asignar la PK generada al objeto
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            e.printStackTrace(); // Para ver más detalles del error
            return false;
        }
    }

    public boolean actualizarUsuario(UsuarioSistema usuario) {
        String sql = "UPDATE USUARIOSISTEMA SET RepCod = ?, UsuNom = ?, UsuContr = ?, UsuEstReg = ? WHERE UsuCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, usuario.getRepCod());
            pstmt.setString(2, usuario.getUsuNom());
            pstmt.setString(3, usuario.getUsuContr());
            pstmt.setString(5, String.valueOf(usuario.getUsuEstReg()));
            pstmt.setInt(6, usuario.getUsuCod());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarLogicamenteUsuario(int codigo) {
        String sql = "UPDATE USUARIOSISTEMA SET UsuEstReg = '*' WHERE UsuCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean inactivarUsuario(int codigo) {
        String sql = "UPDATE USUARIOSISTEMA SET UsuEstReg = 'I' WHERE UsuCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al inactivar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean reactivarUsuario(int codigo) {
        String sql = "UPDATE USUARIOSISTEMA SET UsuEstReg = 'A' WHERE UsuCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al reactivar usuario: " + e.getMessage());
            return false;
        }
    }

    // Método específico para autenticación
    public UsuarioSistema autenticarUsuario(String nombreUsuario, String contraseña) {
        String sql = "SELECT UsuCod, RepCod, UsuNom, UsuContr, UsuEstReg FROM USUARIOSISTEMA WHERE UsuNom = ? AND UsuContr = ? AND UsuEstReg = 'A'";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombreUsuario);
            pstmt.setString(2, contraseña);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    UsuarioSistema usuario = new UsuarioSistema();
                    usuario.setUsuCod(rs.getInt("UsuCod"));
                    usuario.setRepCod(rs.getInt("RepCod"));
                    usuario.setUsuNom(rs.getString("UsuNom"));
                    usuario.setUsuContr(rs.getString("UsuContr"));
                    usuario.setUsuEstReg(rs.getString("UsuEstReg").charAt(0));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al autenticar usuario: " + e.getMessage());
        }

        return null;
    }
}