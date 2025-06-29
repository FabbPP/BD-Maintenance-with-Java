package dao;

import conexion.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.RepVenta;

public class RepVentaDAO {

    public List<RepVenta> obtenerTodosRepresentantes() {
        List<RepVenta> representantes = new ArrayList<>();
        String sql = "SELECT r.RepCod, r.RepNom, r.RepEdad, r.OfiCod, r.CarCod, r.RepCon, r.RepEstReg, " +
                     "o.OfiCiu as OficinaDesc, c.CarDesc as CargoDesc " +
                     "FROM REPVENTA r " +
                     "LEFT JOIN OFICINA o ON r.OfiCod = o.OfiCod " +
                     "LEFT JOIN CARGO c ON r.CarCod = c.CarCod " +
                     "ORDER BY r.RepCod";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                RepVenta rep = new RepVenta(
                    rs.getInt("RepCod"),
                    rs.getString("RepNom"),
                    rs.getInt("RepEdad"),
                    rs.getInt("OfiCod"),
                    rs.getInt("CarCod"),
                    rs.getDate("RepCon").toLocalDate(),
                    rs.getString("RepEstReg").charAt(0),
                    rs.getString("OficinaDesc"),
                    rs.getString("CargoDesc")
                );
                representantes.add(rep);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los representantes: " + e.getMessage());
        }
        return representantes;
    }

    public RepVenta obtenerRepresentantePorCodigo(int repCod) {
        String sql = "SELECT r.RepCod, r.RepNom, r.RepEdad, r.OfiCod, r.CarCod, r.RepCon, r.RepEstReg, " +
                     "o.OfiCiu as OficinaDesc, c.CarDesc as CargoDesc " +
                     "FROM REPVENTA r " +
                     "LEFT JOIN OFICINA o ON r.OfiCod = o.OfiCod " +
                     "LEFT JOIN CARGO c ON r.CarCod = c.CarCod " +
                     "WHERE r.RepCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, repCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new RepVenta(
                    rs.getInt("RepCod"),
                    rs.getString("RepNom"),
                    rs.getInt("RepEdad"),
                    rs.getInt("OfiCod"),
                    rs.getInt("CarCod"),
                    rs.getDate("RepCon").toLocalDate(),
                    rs.getString("RepEstReg").charAt(0),
                    rs.getString("OficinaDesc"),
                    rs.getString("CargoDesc")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener representante: " + e.getMessage());
        }
        return null;
    }

    public String insertarRepresentante(RepVenta representante) {
        String sql = "INSERT INTO REPVENTA (RepNom, RepEdad, OfiCod, CarCod, RepCon, RepEstReg) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, representante.getRepNom());
            stmt.setInt(2, representante.getRepEdad());
            stmt.setInt(3, representante.getOfiCod());
            stmt.setInt(4, representante.getCarCod());
            stmt.setDate(5, Date.valueOf(representante.getRepCon()));
            stmt.setString(6, String.valueOf(representante.getRepEstReg()));
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return null; // No error, return null to indicate success
            } else {
                return "Error desconocido al insertar el representante."; // You can customize this error message
            }
        } catch (SQLException e) {
            String errorMsg = e.getMessage().toLowerCase();
            
            if (errorMsg.contains("chk_rep_edad")) {
                return "La edad debe estar entre 18 y 65 años.";
            } else if (errorMsg.contains("chk_rep_contrato")) {
                return "La fecha de contratación debe ser igual o posterior al 01/01/2024.";
            } else if (errorMsg.contains("fk_rep_oficina")) {
                return "El código de oficina no existe o está inactivo.";
            } else if (errorMsg.contains("fk_rep_cargo")) {
                return "El código de cargo no existe o está inactivo.";
            } else if (errorMsg.contains("chk_rep_estreg")) {
                return "El estado del registro debe ser A, I o *.";
            } else {
                return "Error al insertar representante: " + e.getMessage();
            }
        }
    }

    public String actualizarRepresentante(RepVenta representante) {
        String sql = "UPDATE REPVENTA SET RepNom = ?, RepEdad = ?, OfiCod = ?, CarCod = ?, RepCon = ?, RepEstReg = ? WHERE RepCod = ?";
        try (Connection conn = ConexionBD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, representante.getRepNom());
            stmt.setInt(2, representante.getRepEdad());
            stmt.setInt(3, representante.getOfiCod());
            stmt.setInt(4, representante.getCarCod());
            stmt.setDate(5, Date.valueOf(representante.getRepCon()));
            stmt.setString(6, String.valueOf(representante.getRepEstReg()));
            stmt.setInt(7, representante.getRepCod());
            
            // Si la actualización es exitosa, no se devuelve nada (null)
            return (stmt.executeUpdate() > 0) ? null : "Error al actualizar representante.";
        } catch (SQLException e) {
            String errorMsg = e.getMessage().toLowerCase();
            
            if (errorMsg.contains("chk_rep_edad")) {
                return "La edad debe estar entre 18 y 65 años.";
            } else if (errorMsg.contains("chk_rep_contrato")) {
                return "La fecha de contratación debe ser igual o posterior al 01/01/2024.";
            } else if (errorMsg.contains("fk_rep_oficina")) {
                return "El código de oficina no existe o está inactivo.";
            } else if (errorMsg.contains("fk_rep_cargo")) {
                return "El código de cargo no existe o está inactivo.";
            } else if (errorMsg.contains("chk_rep_estreg")) {
                return "El estado del registro debe ser A, I o *.";
            } else {
                return "Error al actualizar representante: " + e.getMessage();
            }
        }
    }


    public boolean eliminarLogicamenteRepresentante(int repCod) {
        String sql = "UPDATE REPVENTA SET RepEstReg = '*' WHERE RepCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, repCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente representante: " + e.getMessage());
            return false;
        }
    }

    public boolean inactivarRepresentante(int repCod) {
        String sql = "UPDATE REPVENTA SET RepEstReg = 'I' WHERE RepCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, repCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al inactivar representante: " + e.getMessage());
            return false;
        }
    }

    public boolean reactivarRepresentante(int repCod) {
        String sql = "UPDATE REPVENTA SET RepEstReg = 'A' WHERE RepCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, repCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al reactivar representante: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarRepresentante(int repCod) {
        String sql = "DELETE FROM REPVENTA WHERE RepCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, repCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar representante: " + e.getMessage());
            return false;
        }
    }

    public List<RepVenta> obtenerRepresentantesActivos() {
        List<RepVenta> representantes = new ArrayList<>();
        String sql = "SELECT r.RepCod, r.RepNom, r.RepEdad, r.OfiCod, r.CarCod, r.RepCon, r.RepEstReg, " +
                     "o.OfiCiu as OficinaDesc, c.CarDesc as CargoDesc " +
                     "FROM REPVENTA r " +
                     "LEFT JOIN OFICINA o ON r.OfiCod = o.OfiCod " +
                     "LEFT JOIN CARGO c ON r.CarCod = c.CarCod " +
                     "WHERE r.RepEstReg = 'A' ORDER BY r.RepCod";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                RepVenta rep = new RepVenta(
                    rs.getInt("RepCod"),
                    rs.getString("RepNom"),
                    rs.getInt("RepEdad"),
                    rs.getInt("OfiCod"),
                    rs.getInt("CarCod"),
                    rs.getDate("RepCon").toLocalDate(),
                    rs.getString("RepEstReg").charAt(0),
                    rs.getString("OficinaDesc"),
                    rs.getString("CargoDesc")
                );
                representantes.add(rep);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener representantes activos: " + e.getMessage());
        }
        return representantes;
    }

    public List<RepVenta> buscarRepresentantesPorNombre(String nombre) {
        List<RepVenta> representantes = new ArrayList<>();
        String sql = "SELECT r.RepCod, r.RepNom, r.RepEdad, r.OfiCod, r.CarCod, r.RepCon, r.RepEstReg, " +
                     "o.OfiCiu as OficinaDesc, c.CarDesc as CargoDesc " +
                     "FROM REPVENTA r " +
                     "LEFT JOIN OFICINA o ON r.OfiCod = o.OfiCod " +
                     "LEFT JOIN CARGO c ON r.CarCod = c.CarCod " +
                     "WHERE r.RepNom LIKE ? ORDER BY r.RepCod";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                RepVenta rep = new RepVenta(
                    rs.getInt("RepCod"),
                    rs.getString("RepNom"),
                    rs.getInt("RepEdad"),
                    rs.getInt("OfiCod"),
                    rs.getInt("CarCod"),
                    rs.getDate("RepCon").toLocalDate(),
                    rs.getString("RepEstReg").charAt(0),
                    rs.getString("OficinaDesc"),
                    rs.getString("CargoDesc")
                );
                representantes.add(rep);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar representantes por nombre: " + e.getMessage());
        }
        return representantes;
    }

    public List<RepVenta> buscarRepresentantesPorOficina(int ofiCod) {
        List<RepVenta> representantes = new ArrayList<>();
        String sql = "SELECT r.RepCod, r.RepNom, r.RepEdad, r.OfiCod, r.CarCod, r.RepCon, r.RepEstReg, " +
                     "o.OfiCiu as OficinaDesc, c.CarDesc as CargoDesc " +
                     "FROM REPVENTA r " +
                     "LEFT JOIN OFICINA o ON r.OfiCod = o.OfiCod " +
                     "LEFT JOIN CARGO c ON r.CarCod = c.CarCod " +
                     "WHERE r.OfiCod = ? ORDER BY r.RepCod";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ofiCod);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                RepVenta rep = new RepVenta(
                    rs.getInt("RepCod"),
                    rs.getString("RepNom"),
                    rs.getInt("RepEdad"),
                    rs.getInt("OfiCod"),
                    rs.getInt("CarCod"),
                    rs.getDate("RepCon").toLocalDate(),
                    rs.getString("RepEstReg").charAt(0),
                    rs.getString("OficinaDesc"),
                    rs.getString("CargoDesc")
                );
                representantes.add(rep);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar representantes por oficina: " + e.getMessage());
        }
        return representantes;
    }

    public boolean existeRepresentante(int repCod) {
        String sql = "SELECT COUNT(*) FROM REPVENTA WHERE RepCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, repCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de representante: " + e.getMessage());
        }
        return false;
    }

    public boolean validarReferenciaOficina(int ofiCod) {
        String sql = "SELECT COUNT(*) FROM OFICINA WHERE OfiCod = ? AND OfiEstReg = 'A'";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ofiCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al validar referencia de oficina: " + e.getMessage());
        }
        return false;
    }

    public boolean validarReferenciaCargo(int carCod) {
        String sql = "SELECT COUNT(*) FROM CARGO WHERE CarCod = ? AND CarEstReg = 'A'";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, carCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al validar referencia de cargo: " + e.getMessage());
        }
        return false;
    }
}