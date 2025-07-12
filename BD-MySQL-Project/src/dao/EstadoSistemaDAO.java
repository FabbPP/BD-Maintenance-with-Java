package dao;

import conexion.ConexionBD;
import modelo.EstadoSistema;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstadoSistemaDAO {

    public List<EstadoSistema> obtenerTodosEstados() {
        List<EstadoSistema> estados = new ArrayList<>();
        String sql = "SELECT UsuEstSistCod, UsuEstSistDesc, UsuEstSistEstReg FROM ESTADO_SISTEMA";

        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                EstadoSistema estado = new EstadoSistema();
                estado.setUsuEstSistCod(rs.getInt("UsuEstSistCod"));
                estado.setUsuEstSistDesc(rs.getString("UsuEstSistDesc"));
                estado.setUsuEstSistEstReg(rs.getString("UsuEstSistEstReg").charAt(0));
                estados.add(estado);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los estados del sistema: " + e.getMessage());
        }

        return estados;
    }

    public EstadoSistema obtenerEstadoPorCodigo(int codigo) {
        String sql = "SELECT UsuEstSistCod, UsuEstSistDesc, UsuEstSistEstReg FROM ESTADO_SISTEMA WHERE UsuEstSistCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    EstadoSistema estado = new EstadoSistema();
                    estado.setUsuEstSistCod(rs.getInt("UsuEstSistCod"));
                    estado.setUsuEstSistDesc(rs.getString("UsuEstSistDesc"));
                    estado.setUsuEstSistEstReg(rs.getString("UsuEstSistEstReg").charAt(0));
                    return estado;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener estado por código: " + e.getMessage());
        }

        return null;
    }

    public boolean insertarEstado(EstadoSistema estado) {
        String sql = "INSERT INTO ESTADO_SISTEMA (UsuEstSistDesc, UsuEstSistEstReg) VALUES (?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, estado.getUsuEstSistDesc());
            pstmt.setString(2, String.valueOf(estado.getUsuEstSistEstReg()));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        estado.setUsuEstSistCod(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al insertar estado del sistema: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarEstado(EstadoSistema estado) {
        String sql = "UPDATE ESTADO_SISTEMA SET UsuEstSistDesc = ?, UsuEstSistEstReg = ? WHERE UsuEstSistCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, estado.getUsuEstSistDesc());
            pstmt.setString(2, String.valueOf(estado.getUsuEstSistEstReg()));
            pstmt.setInt(3, estado.getUsuEstSistCod());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar estado del sistema: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarLogicamenteEstado(int codigo) {
        String sql = "UPDATE ESTADO_SISTEMA SET UsuEstSistEstReg = '*' WHERE UsuEstSistCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente el estado del sistema: " + e.getMessage());
            return false;
        }
    }

    public boolean suspenderEstado(int codigo) {
        String sql = "UPDATE ESTADO_SISTEMA SET UsuEstSistEstReg = 'S' WHERE UsuEstSistCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al suspender estado del sistema: " + e.getMessage());
            return false;
        }
    }

    public boolean reactivarEstado(int codigo) {
        String sql = "UPDATE ESTADO_SISTEMA SET UsuEstSistEstReg = 'A' WHERE UsuEstSistCod = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al reactivar estado del sistema: " + e.getMessage());
            return false;
        }
    }
}
