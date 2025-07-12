package dao;

import conexion.ConexionBD;
import modelo.Ciudad;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CiudadDAO{

    public List<Ciudad> obtenerTodasCiudades() {
        List<Ciudad> ciudades = new ArrayList<>();
        String sql = "SELECT CiuCod, RegCod, CiuNom, CiuEstReg FROM CIUDADES";
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Ciudad ciudad = new Ciudad();
                ciudad.setCiuCod(rs.getInt("CiuCod"));
                ciudad.setRegCod(rs.getInt("RegCod"));
                ciudad.setCiuNom(rs.getString("CiuNom"));
                ciudad.setCiuEstReg(rs.getString("CiuEstReg").charAt(0));
                ciudades.add(ciudad);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las ciudades: " + e.getMessage());
        }
        return ciudades;
    }

    public Ciudad obtenerCiudadPorCodigo(int codigo) {
        String sql = "SELECT CiuCod, RegCod, CiuNom, CiuEstReg FROM CIUDADES WHERE CiuCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Ciudad ciudad = new Ciudad();
                    ciudad.setCiuCod(rs.getInt("CiuCod"));
                    ciudad.setRegCod(rs.getInt("RegCod"));
                    ciudad.setCiuNom(rs.getString("CiuNom"));
                    ciudad.setCiuEstReg(rs.getString("CiuEstReg").charAt(0));
                    return ciudad;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ciudad por código: " + e.getMessage());
        }
        return null;
    }

    public boolean insertarCiudad(Ciudad ciudad) {
        String sql = "INSERT INTO CIUDADES (RegCod, CiuNom, CiuEstReg) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, ciudad.getRegCod());
            pstmt.setString(2, ciudad.getCiuNom());
            pstmt.setString(3, String.valueOf(ciudad.getCiuEstReg()));

            if (pstmt.executeUpdate() > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ciudad.setCiuCod(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar ciudad: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizarCiudad(Ciudad ciudad) {
        String sql = "UPDATE CIUDADES SET RegCod = ?, CiuNom = ?, CiuEstReg = ? WHERE CiuCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, ciudad.getRegCod());
            pstmt.setString(2, ciudad.getCiuNom());
            pstmt.setString(3, String.valueOf(ciudad.getCiuEstReg()));
            pstmt.setInt(4, ciudad.getCiuCod());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar ciudad: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarLogicamenteCiudad(int ciuCod) {
        String sql = "UPDATE CIUDADES SET CiuEstReg = '*' WHERE CiuCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, ciuCod);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente ciudad: " + e.getMessage());
            return false;
        }
    }

    public boolean inactivarCiudad(int ciuCod) {
        String sql = "UPDATE CIUDADES SET CiuEstReg = 'I' WHERE CiuCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, ciuCod);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al inactivar ciudad: " + e.getMessage());
            return false;
        }
    }

    public boolean reactivarCiudad(int ciuCod) {
        String sql = "UPDATE CIUDADES SET CiuEstReg = 'A' WHERE CiuCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, ciuCod);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al reactivar ciudad: " + e.getMessage());
            return false;
        }
    }
}