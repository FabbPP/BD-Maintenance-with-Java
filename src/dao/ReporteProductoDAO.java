package dao;

import conexion.ConexionBD;
import modelo.ReporteProducto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReporteProductoDAO {
    public String insertarReporte(ReporteProducto reporteProducto) {
        String sql = "INSERT INTO REPORPROD (ReporProdMin, ReporProdMax, ReporProdEstReg) VALUES (?, ?, ?)";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, reporteProducto.getReporProdMin());
            stmt.setInt(2, reporteProducto.getReporProdMax());
            stmt.setString(3, String.valueOf(reporteProducto.getReporProdEstReg()));
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    reporteProducto.setReporProdCod(generatedKeys.getInt(1));
                }
                return null; // No error, return null to indicate success
            } else {
                return "Error desconocido al insertar el reporte de producto.";
            }
        } catch (SQLException e) {
            String errorMsg = e.getMessage().toLowerCase();
            
            if (errorMsg.contains("chk_reporte_stock")) {
                return "El stock mínimo debe ser menor que el stock máximo.";
            } else if (errorMsg.contains("chk_reporte_min_positivo")) {
                return "El stock mínimo debe ser mayor a 0.";
            } else if (errorMsg.contains("chk_reporte_max_positivo")) {
                return "El stock máximo debe ser mayor a 0.";
            } else if (errorMsg.contains("chk_reporte_estreg")) {
                return "El estado del registro debe ser A, I o *.";
            } else {
                return "Error al insertar reporte de producto: " + e.getMessage();
            }
        }
    }
    
    // Buscar reporte por código
    public ReporteProducto obtenerReportePorCodigo(int reporteProdCod) {
        String sql = "SELECT * FROM REPORPROD WHERE ReporProdCod = ? AND ReporProdEstReg != '*'";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reporteProdCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearReporteProducto(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener reporte de producto: " + e.getMessage());
        }
        return null;
    }
    
    // Listar todos los reportes activos
    public List<ReporteProducto> obtenerTodosReportes() {
        List<ReporteProducto> reportes = new ArrayList<>();
        String sql = "SELECT * FROM REPORPROD WHERE ReporProdEstReg = 'A' ORDER BY ReporProdCod";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                reportes.add(mapearReporteProducto(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los reportes de producto: " + e.getMessage());
        }
        return reportes;
    }
    
    // Listar reportes por rango de stock mínimo
    public List<ReporteProducto> buscarReportesPorStockMinimo(int stockMinInicio, int stockMinFin) {
        List<ReporteProducto> reportes = new ArrayList<>();
        String sql = "SELECT * FROM REPORPROD WHERE ReporProdMin BETWEEN ? AND ? AND ReporProdEstReg = 'A' ORDER BY ReporProdMin";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, stockMinInicio);
            stmt.setInt(2, stockMinFin);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                reportes.add(mapearReporteProducto(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar reportes por stock mínimo: " + e.getMessage());
        }
        return reportes;
    }
    
    // Listar reportes por rango de stock máximo
    public List<ReporteProducto> buscarReportesPorStockMaximo(int stockMaxInicio, int stockMaxFin) {
        List<ReporteProducto> reportes = new ArrayList<>();
        String sql = "SELECT * FROM REPORPROD WHERE ReporProdMax BETWEEN ? AND ? AND ReporProdEstReg = 'A' ORDER BY ReporProdMax";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, stockMaxInicio);
            stmt.setInt(2, stockMaxFin);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                reportes.add(mapearReporteProducto(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar reportes por stock máximo: " + e.getMessage());
        }
        return reportes;
    }
    
    // Buscar reportes con stock crítico (donde mínimo >= máximo)
    public List<ReporteProducto> obtenerReportesStockCritico() {
        List<ReporteProducto> reportes = new ArrayList<>();
        String sql = "SELECT * FROM REPORPROD WHERE ReporProdMin >= ReporProdMax AND ReporProdEstReg = 'A' ORDER BY ReporProdMin DESC";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                reportes.add(mapearReporteProducto(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener reportes con stock crítico: " + e.getMessage());
        }
        return reportes;
    }
    
    // Actualizar reporte de producto
    public String actualizarReporte(ReporteProducto reporteProducto) {
        String sql = "UPDATE REPORPROD SET ReporProdMin = ?, ReporProdMax = ?, ReporProdEstReg = ? WHERE ReporProdCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reporteProducto.getReporProdMin());
            stmt.setInt(2, reporteProducto.getReporProdMax());
            stmt.setString(3, String.valueOf(reporteProducto.getReporProdEstReg()));
            stmt.setInt(4, reporteProducto.getReporProdCod());
            
            return (stmt.executeUpdate() > 0) ? null : "Error al actualizar reporte de producto.";
        } catch (SQLException e) {
            String errorMsg = e.getMessage().toLowerCase();
            
            if (errorMsg.contains("chk_reporte_stock")) {
                return "El stock mínimo debe ser menor que el stock máximo.";
            } else if (errorMsg.contains("chk_reporte_min_positivo")) {
                return "El stock mínimo debe ser mayor a 0.";
            } else if (errorMsg.contains("chk_reporte_max_positivo")) {
                return "El stock máximo debe ser mayor a 0.";
            } else if (errorMsg.contains("chk_reporte_estreg")) {
                return "El estado del registro debe ser A, I o *.";
            } else {
                return "Error al actualizar reporte de producto: " + e.getMessage();
            }
        }
    }
    
    // Eliminar lógico (cambiar estado a eliminado)
    public boolean eliminarLogicamenteReporte(int reporteProdCod) {
        String sql = "UPDATE REPORPROD SET ReporProdEstReg = '*' WHERE ReporProdCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reporteProdCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente reporte: " + e.getMessage());
            return false;
        }
    }
    
    // Activar reporte
    public boolean reactivarReporte(int reporteProdCod) {
        String sql = "UPDATE REPORPROD SET ReporProdEstReg = 'A' WHERE ReporProdCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reporteProdCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al reactivar reporte: " + e.getMessage());
            return false;
        }
    }
    
    // Inactivar reporte
    public boolean inactivarReporte(int reporteProdCod) {
        String sql = "UPDATE REPORPROD SET ReporProdEstReg = 'I' WHERE ReporProdCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reporteProdCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al inactivar reporte: " + e.getMessage());
            return false;
        }
    }
    
    // Eliminar físico
    public boolean eliminarReporte(int reporteProdCod) {
        String sql = "DELETE FROM REPORPROD WHERE ReporProdCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reporteProdCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar reporte: " + e.getMessage());
            return false;
        }
    }
    
    // Contar total de reportes activos
    public int contarTotalReportes() {
        String sql = "SELECT COUNT(*) FROM REPORPROD WHERE ReporProdEstReg = 'A'";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al contar reportes: " + e.getMessage());
        }
        return 0;
    }
    
    // Verificar si existe un reporte con el código especificado
    public boolean existeReporte(int reporteProdCod) {
        String sql = "SELECT COUNT(*) FROM REPORPROD WHERE ReporProdCod = ? AND ReporProdEstReg != '*'";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reporteProdCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia del reporte: " + e.getMessage());
        }
        return false;
    }
    
    // Buscar reportes con stock bajo (donde mínimo es mayor a un porcentaje del máximo)
    public List<ReporteProducto> obtenerReportesStockBajo(double porcentaje) {
        List<ReporteProducto> reportes = new ArrayList<>();
        String sql = "SELECT * FROM REPORPROD WHERE (CAST(ReporProdMin AS DECIMAL) / CAST(ReporProdMax AS DECIMAL)) >= ? AND ReporProdEstReg = 'A' ORDER BY (CAST(ReporProdMin AS DECIMAL) / CAST(ReporProdMax AS DECIMAL)) DESC";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, porcentaje / 100.0);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                reportes.add(mapearReporteProducto(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener reportes con stock bajo: " + e.getMessage());
        }
        return reportes;
    }
    
    // Método auxiliar para mapear ResultSet a objeto ReporteProducto
    private ReporteProducto mapearReporteProducto(ResultSet rs) throws SQLException {
        ReporteProducto reporte = new ReporteProducto();
        reporte.setReporProdCod(rs.getInt("ReporProdCod"));
        reporte.setReporProdMin(rs.getInt("ReporProdMin"));
        reporte.setReporProdMax(rs.getInt("ReporProdMax"));
        reporte.setReporProdEstReg(rs.getString("ReporProdEstReg").charAt(0));
        return reporte;
    }
}