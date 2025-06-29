package dao;

import conexion.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Oficina;

public class OficinaDAO {

    public List<Oficina> obtenerTodasOficinas() {
        List<Oficina> oficinas = new ArrayList<>();
        String sql = "SELECT OfiCod, OfiCiu, OfiReg, OfiDir, OfiEmp, OfiObj, OfiEstReg FROM OFICINA ORDER BY OfiCod";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Oficina oficina = new Oficina(
                    rs.getInt("OfiCod"),
                    rs.getString("OfiCiu"),
                    rs.getString("OfiReg"),
                    rs.getString("OfiDir"),
                    rs.getString("OfiEmp"),
                    rs.getDouble("OfiObj"),
                    rs.getString("OfiEstReg").charAt(0)
                );
                oficinas.add(oficina);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las oficinas: " + e.getMessage());
        }
        return oficinas;
    }

    
    public Oficina obtenerOficinaPorCodigo(int ofiCod) {
        String sql = "SELECT OfiCod, OfiCiu, OfiReg, OfiDir, OfiEmp, OfiObj, OfiEstReg FROM OFICINA WHERE OfiCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ofiCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Oficina(
                    rs.getInt("OfiCod"),
                    rs.getString("OfiCiu"),
                    rs.getString("OfiReg"),
                    rs.getString("OfiDir"),
                    rs.getString("OfiEmp"),
                    rs.getDouble("OfiObj"),
                    rs.getString("OfiEstReg").charAt(0)
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener oficina: " + e.getMessage());
        }
        return null;
    }



    public boolean insertarOficina(Oficina oficina) {
        String sql = "INSERT INTO OFICINA (OfiCiu, OfiReg, OfiDir, OfiEmp, OfiObj, OfiEstReg) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, oficina.getOfiCiu());
            stmt.setString(2, oficina.getOfiReg());
            stmt.setString(3, oficina.getOfiDir());
            stmt.setString(4, oficina.getOfiEmp());
            stmt.setDouble(5, oficina.getOfiObj());
            stmt.setString(6, String.valueOf(oficina.getOfiEstReg()));
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar oficina: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarOficina(Oficina oficina) {
        String sql = "UPDATE OFICINA SET OfiCiu = ?, OfiReg = ?, OfiDir = ?, OfiEmp = ?, OfiObj = ?, OfiEstReg = ? WHERE OfiCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, oficina.getOfiCiu());
            stmt.setString(2, oficina.getOfiReg());
            stmt.setString(3, oficina.getOfiDir());
            stmt.setString(4, oficina.getOfiEmp());
            stmt.setDouble(5, oficina.getOfiObj());
            stmt.setString(6, String.valueOf(oficina.getOfiEstReg()));
            stmt.setInt(7, oficina.getOfiCod());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar oficina: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarLogicamenteOficina(int ofiCod) {
        String sql = "UPDATE OFICINA SET OfiEstReg = '*' WHERE OfiCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ofiCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente oficina: " + e.getMessage());
            return false;
        }
    }

    public boolean inactivarOficina(int ofiCod) {
        String sql = "UPDATE OFICINA SET OfiEstReg = 'I' WHERE OfiCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ofiCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al inactivar oficina: " + e.getMessage());
            return false;
        }
    }

    public boolean reactivarOficina(int ofiCod) {
        String sql = "UPDATE OFICINA SET OfiEstReg = 'A' WHERE OfiCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ofiCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al reactivar oficina: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarOficina(int ofiCod) {
        String sql = "DELETE FROM OFICINA WHERE OfiCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ofiCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar oficina: " + e.getMessage());
            return false;
        }
    }


    public List<Oficina> obtenerOficinasActivas() {
        List<Oficina> oficinas = new ArrayList<>();
        String sql = "SELECT OfiCod, OfiCiu, OfiReg, OfiDir, OfiEmp, OfiObj, OfiEstReg FROM OFICINA WHERE OfiEstReg = 'A' ORDER BY OfiCod";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Oficina oficina = new Oficina(
                    rs.getInt("OfiCod"),
                    rs.getString("OfiCiu"),
                    rs.getString("OfiReg"),
                    rs.getString("OfiDir"),
                    rs.getString("OfiEmp"),
                    rs.getDouble("OfiObj"),
                    rs.getString("OfiEstReg").charAt(0)
                );
                oficinas.add(oficina);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener oficinas activas: " + e.getMessage());
        }
        return oficinas;
    }

    public List<Oficina> buscarOficinasPorCiudad(String ciudad) {
        List<Oficina> oficinas = new ArrayList<>();
        String sql = "SELECT OfiCod, OfiCiu, OfiReg, OfiDir, OfiEmp, OfiObj, OfiEstReg FROM OFICINA WHERE OfiCiu LIKE ? ORDER BY OfiCod";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + ciudad + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Oficina oficina = new Oficina(
                    rs.getInt("OfiCod"),
                    rs.getString("OfiCiu"),
                    rs.getString("OfiReg"),
                    rs.getString("OfiDir"),
                    rs.getString("OfiEmp"),
                    rs.getDouble("OfiObj"),
                    rs.getString("OfiEstReg").charAt(0)
                );
                oficinas.add(oficina);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar oficinas por ciudad: " + e.getMessage());
        }
        return oficinas;
    }

    public List<Oficina> buscarOficinasPorRegion(String region) {
        List<Oficina> oficinas = new ArrayList<>();
        String sql = "SELECT OfiCod, OfiCiu, OfiReg, OfiDir, OfiEmp, OfiObj, OfiEstReg FROM OFICINA WHERE OfiReg LIKE ? ORDER BY OfiCod";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + region + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Oficina oficina = new Oficina(
                    rs.getInt("OfiCod"),
                    rs.getString("OfiCiu"),
                    rs.getString("OfiReg"),
                    rs.getString("OfiDir"),
                    rs.getString("OfiEmp"),
                    rs.getDouble("OfiObj"),
                    rs.getString("OfiEstReg").charAt(0)
                );
                oficinas.add(oficina);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar oficinas por región: " + e.getMessage());
        }
        return oficinas;
    }

    public boolean existeOficina(int ofiCod) {
        String sql = "SELECT COUNT(*) FROM OFICINA WHERE OfiCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ofiCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de oficina: " + e.getMessage());
        }
        return false;
    }
}