package dao;

import conexion.ConexionBD;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.ReporRepVenta;

public class ReporRepVentaDAO {

    public List<ReporRepVenta> obtenerTodosReportes() {
        List<ReporRepVenta> reportes = new ArrayList<>();
        String sql = "SELECT rr.RepoRepVentCod, rr.RepCod, rr.RepoRepVentObj, rr.RepoRepVentNum, " +
                     "rr.RepoRepVentCuo, rr.CarCod, rr.RepoRepVentEstReg, " +
                     "r.RepNom as RepresentanteNombre, c.CarDesc as CargoDescripcion " +
                     "FROM REPORREPVENTA rr " +
                     "LEFT JOIN REPVENTA r ON rr.RepCod = r.RepCod " +
                     "LEFT JOIN CARGO c ON rr.CarCod = c.CarCod " +
                     "ORDER BY rr.RepoRepVentCod";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                ReporRepVenta reporte = new ReporRepVenta(
                    rs.getInt("RepoRepVentCod"),
                    rs.getInt("RepCod"),
                    rs.getBigDecimal("RepoRepVentObj"),
                    rs.getInt("RepoRepVentNum"),
                    rs.getInt("RepoRepVentCuo"),
                    rs.getInt("CarCod"),
                    rs.getString("RepoRepVentEstReg").charAt(0),
                    rs.getString("RepresentanteNombre"),
                    rs.getString("CargoDescripcion")
                );
                reportes.add(reporte);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los reportes: " + e.getMessage());
        }
        return reportes;
    }

    public ReporRepVenta obtenerReportePorCodigo(int repoRepVentCod, int repCod) {
        String sql = "SELECT rr.RepoRepVentCod, rr.RepCod, rr.RepoRepVentObj, rr.RepoRepVentNum, " +
                     "rr.RepoRepVentCuo, rr.CarCod, rr.RepoRepVentEstReg, " +
                     "r.RepNom as RepresentanteNombre, c.CarDesc as CargoDescripcion " +
                     "FROM REPORREPVENTA rr " +
                     "LEFT JOIN REPVENTA r ON rr.RepCod = r.RepCod " +
                     "LEFT JOIN CARGO c ON rr.CarCod = c.CarCod " +
                     "WHERE rr.RepoRepVentCod = ? AND rr.RepCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, repoRepVentCod);
            stmt.setInt(2, repCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new ReporRepVenta(
                    rs.getInt("RepoRepVentCod"),
                    rs.getInt("RepCod"),
                    rs.getBigDecimal("RepoRepVentObj"),
                    rs.getInt("RepoRepVentNum"),
                    rs.getInt("RepoRepVentCuo"),
                    rs.getInt("CarCod"),
                    rs.getString("RepoRepVentEstReg").charAt(0),
                    rs.getString("RepresentanteNombre"),
                    rs.getString("CargoDescripcion")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener reporte: " + e.getMessage());
        }
        return null;
    }

    public String insertarReporte(ReporRepVenta reporte) {
        String sql = "INSERT INTO REPORREPVENTA (RepCod, RepoRepVentObj, RepoRepVentNum, RepoRepVentCuo, CarCod, RepoRepVentEstReg) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reporte.getRepCod());
            stmt.setBigDecimal(2, reporte.getRepoRepVentObj());
            stmt.setInt(3, reporte.getRepoRepVentNum());
            stmt.setInt(4, reporte.getRepoRepVentCuo());
            stmt.setInt(5, reporte.getCarCod());
            stmt.setString(6, String.valueOf(reporte.getRepoRepVentEstReg()));
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return null; // No error, return null to indicate success
            } else {
                return "Error desconocido al insertar el reporte.";
            }
        } catch (SQLException e) {
            String errorMsg = e.getMessage().toLowerCase();
            
            if (errorMsg.contains("chk_repoventa_num")) {
                return "El número de ventas concretas debe ser mayor o igual a 0.";
            } else if (errorMsg.contains("chk_repoventa_cuo")) {
                return "La cuota debe ser mayor o igual a 0.";
            } else if (errorMsg.contains("fk_repoventa_representante")) {
                return "El código de representante no existe.";
            } else if (errorMsg.contains("fk_repoventa_cargo")) {
                return "El código de cargo no existe.";
            } else if (errorMsg.contains("chk_repoventa_estreg")) {
                return "El estado del registro debe ser A, I o *.";
            } else if (errorMsg.contains("primary")) {
                return "Ya existe un reporte con este código para el representante especificado.";
            } else {
                return "Error al insertar reporte: " + e.getMessage();
            }
        }
    }

    public String actualizarReporte(ReporRepVenta reporte) {
        String sql = "UPDATE REPORREPVENTA SET RepoRepVentObj = ?, RepoRepVentNum = ?, RepoRepVentCuo = ?, " +
                     "CarCod = ?, RepoRepVentEstReg = ? WHERE RepoRepVentCod = ? AND RepCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, reporte.getRepoRepVentObj());
            stmt.setInt(2, reporte.getRepoRepVentNum());
            stmt.setInt(3, reporte.getRepoRepVentCuo());
            stmt.setInt(4, reporte.getCarCod());
            stmt.setString(5, String.valueOf(reporte.getRepoRepVentEstReg()));
            stmt.setInt(6, reporte.getRepoRepVentCod());
            stmt.setInt(7, reporte.getRepCod());
            
            return (stmt.executeUpdate() > 0) ? null : "Error al actualizar reporte.";
        } catch (SQLException e) {
            String errorMsg = e.getMessage().toLowerCase();
            
            if (errorMsg.contains("chk_repoventa_num")) {
                return "El número de ventas concretas debe ser mayor o igual a 0.";
            } else if (errorMsg.contains("chk_repoventa_cuo")) {
                return "La cuota debe ser mayor o igual a 0.";
            } else if (errorMsg.contains("fk_repoventa_cargo")) {
                return "El código de cargo no existe.";
            } else if (errorMsg.contains("chk_repoventa_estreg")) {
                return "El estado del registro debe ser A, I o *.";
            } else {
                return "Error al actualizar reporte: " + e.getMessage();
            }
        }
    }

    public boolean eliminarLogicamenteReporte(int repoRepVentCod, int repCod) {
        String sql = "UPDATE REPORREPVENTA SET RepoRepVentEstReg = '*' WHERE RepoRepVentCod = ? AND RepCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, repoRepVentCod);
            stmt.setInt(2, repCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente reporte: " + e.getMessage());
            return false;
        }
    }

    public boolean inactivarReporte(int repoRepVentCod, int repCod) {
        String sql = "UPDATE REPORREPVENTA SET RepoRepVentEstReg = 'I' WHERE RepoRepVentCod = ? AND RepCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, repoRepVentCod);
            stmt.setInt(2, repCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al inactivar reporte: " + e.getMessage());
            return false;
        }
    }

    public boolean reactivarReporte(int repoRepVentCod, int repCod) {
        String sql = "UPDATE REPORREPVENTA SET RepoRepVentEstReg = 'A' WHERE RepoRepVentCod = ? AND RepCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, repoRepVentCod);
            stmt.setInt(2, repCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al reactivar reporte: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarReporte(int repoRepVentCod, int repCod) {
        String sql = "DELETE FROM REPORREPVENTA WHERE RepoRepVentCod = ? AND RepCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, repoRepVentCod);
            stmt.setInt(2, repCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar reporte: " + e.getMessage());
            return false;
        }
    }

    public List<ReporRepVenta> obtenerReportesActivos() {
        List<ReporRepVenta> reportes = new ArrayList<>();
        String sql = "SELECT rr.RepoRepVentCod, rr.RepCod, rr.RepoRepVentObj, rr.RepoRepVentNum, " +
                     "rr.RepoRepVentCuo, rr.CarCod, rr.RepoRepVentEstReg, " +
                     "r.RepNom as RepresentanteNombre, c.CarDesc as CargoDescripcion " +
                     "FROM REPORREPVENTA rr " +
                     "LEFT JOIN REPVENTA r ON rr.RepCod = r.RepCod " +
                     "LEFT JOIN CARGO c ON rr.CarCod = c.CarCod " +
                     "WHERE rr.RepoRepVentEstReg = 'A' ORDER BY rr.RepoRepVentCod";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                ReporRepVenta reporte = new ReporRepVenta(
                    rs.getInt("RepoRepVentCod"),
                    rs.getInt("RepCod"),
                    rs.getBigDecimal("RepoRepVentObj"),
                    rs.getInt("RepoRepVentNum"),
                    rs.getInt("RepoRepVentCuo"),
                    rs.getInt("CarCod"),
                    rs.getString("RepoRepVentEstReg").charAt(0),
                    rs.getString("RepresentanteNombre"),
                    rs.getString("CargoDescripcion")
                );
                reportes.add(reporte);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener reportes activos: " + e.getMessage());
        }
        return reportes;
    }

    public List<ReporRepVenta> buscarReportesPorRepresentante(int repCod) {
        List<ReporRepVenta> reportes = new ArrayList<>();
        String sql = "SELECT rr.RepoRepVentCod, rr.RepCod, rr.RepoRepVentObj, rr.RepoRepVentNum, " +
                     "rr.RepoRepVentCuo, rr.CarCod, rr.RepoRepVentEstReg, " +
                     "r.RepNom as RepresentanteNombre, c.CarDesc as CargoDescripcion " +
                     "FROM REPORREPVENTA rr " +
                     "LEFT JOIN REPVENTA r ON rr.RepCod = r.RepCod " +
                     "LEFT JOIN CARGO c ON rr.CarCod = c.CarCod " +
                     "WHERE rr.RepCod = ? ORDER BY rr.RepoRepVentCod";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, repCod);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ReporRepVenta reporte = new ReporRepVenta(
                    rs.getInt("RepoRepVentCod"),
                    rs.getInt("RepCod"),
                    rs.getBigDecimal("RepoRepVentObj"),
                    rs.getInt("RepoRepVentNum"),
                    rs.getInt("RepoRepVentCuo"),
                    rs.getInt("CarCod"),
                    rs.getString("RepoRepVentEstReg").charAt(0),
                    rs.getString("RepresentanteNombre"),
                    rs.getString("CargoDescripcion")
                );
                reportes.add(reporte);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar reportes por representante: " + e.getMessage());
        }
        return reportes;
    }

    public List<ReporRepVenta> buscarReportesPorCargo(int carCod) {
        List<ReporRepVenta> reportes = new ArrayList<>();
        String sql = "SELECT rr.RepoRepVentCod, rr.RepCod, rr.RepoRepVentObj, rr.RepoRepVentNum, " +
                     "rr.RepoRepVentCuo, rr.CarCod, rr.RepoRepVentEstReg, " +
                     "r.RepNom as RepresentanteNombre, c.CarDesc as CargoDescripcion " +
                     "FROM REPORREPVENTA rr " +
                     "LEFT JOIN REPVENTA r ON rr.RepCod = r.RepCod " +
                     "LEFT JOIN CARGO c ON rr.CarCod = c.CarCod " +
                     "WHERE rr.CarCod = ? ORDER BY rr.RepoRepVentCod";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, carCod);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ReporRepVenta reporte = new ReporRepVenta(
                    rs.getInt("RepoRepVentCod"),
                    rs.getInt("RepCod"),
                    rs.getBigDecimal("RepoRepVentObj"),
                    rs.getInt("RepoRepVentNum"),
                    rs.getInt("RepoRepVentCuo"),
                    rs.getInt("CarCod"),
                    rs.getString("RepoRepVentEstReg").charAt(0),
                    rs.getString("RepresentanteNombre"),
                    rs.getString("CargoDescripcion")
                );
                reportes.add(reporte);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar reportes por cargo: " + e.getMessage());
        }
        return reportes;
    }

    public boolean existeReporte(int repoRepVentCod, int repCod) {
        String sql = "SELECT COUNT(*) FROM REPORREPVENTA WHERE RepoRepVentCod = ? AND RepCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, repoRepVentCod);
            stmt.setInt(2, repCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de reporte: " + e.getMessage());
        }
        return false;
    }

    public boolean validarReferenciaRepresentante(int repCod) {
        String sql = "SELECT COUNT(*) FROM REPVENTA WHERE RepCod = ? AND RepEstReg = 'A'";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, repCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al validar referencia de representante: " + e.getMessage());
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

    // Método auxiliar para mostrar errores
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error de Validación", JOptionPane.ERROR_MESSAGE);
    }
}