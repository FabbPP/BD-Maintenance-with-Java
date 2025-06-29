package dao;

import conexion.ConexionBD;
import modelo.Region;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegionDAO{

    public List<Region> obtenerTodasRegiones() {
        List<Region> regiones = new ArrayList<>();
        String sql = "SELECT RegCod, DepCod, RegNom, RegEstReg FROM REGION";
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Region reg = new Region(
                	rs.getInt("RegCod"), 
                        rs.getInt("DepCod"), 
                        rs.getString("RegNom"), 
                        rs.getString("RegEstReg").charAt(0)
                );
                regiones.add(reg);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las regiones: " + e.getMessage());
        }
        return regiones;
    }

    public Region obtenerRegionPorCodigo(int codigo) {
        String sql = "SELECT RegCod, DepCod, RegNom, RegEstReg FROM REGION WHERE RegCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Region reg = new Region(rs.getInt("RegCod"), rs.getInt("DepCod"), rs.getString("RegNom"), rs.getString("RegEstReg"));
                    return reg;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener región por código: " + e.getMessage());
        }
        return null;
    }

    public boolean insertarRegion(Region region) {
        String sql = "INSERT INTO REGION (DepCod, RegNom, RegEstReg) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, region.getDepCod());
            pstmt.setString(2, region.getRegNom());
            pstmt.setString(3, String.valueOf(region.getRegEstReg()));

            if (pstmt.executeUpdate() > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        region.setRegCod(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar región: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizarRegion(Region region) {
        String sql = "UPDATE REGION SET DepCod = ?, RegNom = ?, RegEstReg = ? WHERE RegCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, region.getDepCod());
            pstmt.setString(2, region.getRegNom());
            pstmt.setString(3, String.valueOf(region.getRegEstReg()));
            pstmt.setInt(4, region.getRegCod());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar región: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarLogicamenteRegion(int regCod) {
        String sql = "UPDATE REGION SET RegEstReg = '*' WHERE RegCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, regCod);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente región: " + e.getMessage());
            return false;
        }
    }

    public boolean inactivarRegion(int regCod) {
        String sql = "UPDATE REGION SET RegEstReg = 'I' WHERE RegCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, regCod);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al inactivar región: " + e.getMessage());
            return false;
        }
    }

    public boolean reactivarRegion(int regCod) {
        String sql = "UPDATE REGION SET RegEstReg = 'A' WHERE RegCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, regCod);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al reactivar región: " + e.getMessage());
            return false;
        }
    }
}