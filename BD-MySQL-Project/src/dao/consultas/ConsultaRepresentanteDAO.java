package dao.consultas;

import conexion.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.consultas.ConsultaRepresentante;

public class ConsultaRepresentanteDAO {
    
    public List<ConsultaRepresentante> obtenerConsultasRepresentantes() {
        List<ConsultaRepresentante> estadisticas = new ArrayList<>();
        String sql = "SELECT " +
                     "r.RepCod, " +
                     "r.RepNom, " +
                     "COUNT(f.FacCod) AS TotalFacturas, " +
                     "SUM(f.Faclmp) AS MontoTotalVentas " +
                     "FROM REPVENTA r " +
                     "LEFT JOIN FACTURA f ON r.RepCod = f.RepCod AND f.FacEstReg = 'A' " +
                     "WHERE r.RepEstReg = 'A' " +
                     "GROUP BY r.RepCod, r.RepNom " +
                     "ORDER BY MontoTotalVentas DESC";
        
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ConsultaRepresentante estadistica = new ConsultaRepresentante(
                    rs.getInt("RepCod"),
                    rs.getString("RepNom"),
                    rs.getInt("TotalFacturas"),
                    rs.getBigDecimal("MontoTotalVentas")
                );
                estadisticas.add(estadistica);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener estadísticas de representantes: " + e.getMessage());
        }
        return estadisticas;
    }
}