package dao.consultas;

import conexion.ConexionBD;
import modelo.consultas.ConsultaProducto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultaProductoDAO {
    
    public List<ConsultaProducto> obtenerConsultasProductos() {
        List<ConsultaProducto> estadisticas = new ArrayList<>();
        String sql = "SELECT " +
                     "cp.ClasProCod, " +
                     "cp.ClasProDesc, " +
                     "COUNT(p.ProdCod) AS TotalProductos, " +
                     "AVG(p.ProdPre) AS PrecioPromedio, " +
                     "MAX(p.ProdPre) AS PrecioMaximo, " +
                     "MIN(p.ProdStock) AS StockMinimo " +
                     "FROM CLASIFICACION_PRODUCTO cp " +
                     "LEFT JOIN PRODUCTO p ON cp.ClasProCod = p.ClasProCod AND p.ProdEstReg = 'A' " +
                     "WHERE cp.ClasProEstReg = 'A' " +
                     "GROUP BY cp.ClasProCod, cp.ClasProDesc " +
                     "ORDER BY PrecioPromedio DESC";
        
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ConsultaProducto estadistica = new ConsultaProducto(
                    rs.getInt("ClasProCod"),
                    rs.getString("ClasProDesc"),
                    rs.getInt("TotalProductos"),
                    rs.getBigDecimal("PrecioPromedio"),
                    rs.getBigDecimal("PrecioMaximo"),
                    rs.getInt("StockMinimo")
                );
                estadisticas.add(estadistica);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener estadísticas de productos: " + e.getMessage());
        }
        return estadisticas;
    }
}