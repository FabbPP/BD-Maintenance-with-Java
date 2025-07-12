package dao;
//Corregido
import modelo.FabricanteProducto;
import conexion.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FabricanteProductoDAO {
    
    // Método para insertar un nuevo fabricante
    public String insertarFabricante(FabricanteProducto fabricante) {
        String sql = "INSERT INTO FABRICANTE_PRODUCTO (FabNom, FabEstReg) VALUES (?, ?)";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, fabricante.getFabNom());
            stmt.setString(2, String.valueOf(fabricante.getFabEstReg()));
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                // Obtener el ID generado automáticamente
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        fabricante.setFabCod(generatedKeys.getInt(1));
                    }
                }
                return null; // No error, return null to indicate success
            } else {
                return "Error desconocido al insertar el fabricante.";
            }
            
        } catch (SQLException e) {
            String errorMsg = e.getMessage().toLowerCase();
            
            if (errorMsg.contains("chk_fab_est")) {
                return "El estado del registro debe ser A, I o *.";
            } else {
                return "Error al insertar fabricante: " + e.getMessage();
            }
        }
    }
    
    // Método para actualizar un fabricante existente
    public String actualizarFabricante(FabricanteProducto fabricante) {
        String sql = "UPDATE FABRICANTE_PRODUCTO SET FabNom = ?, FabEstReg = ? WHERE FabCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, fabricante.getFabNom());
            stmt.setString(2, String.valueOf(fabricante.getFabEstReg()));
            stmt.setInt(3, fabricante.getFabCod());
            
            return (stmt.executeUpdate() > 0) ? null : "Error al actualizar fabricante.";
            
        } catch (SQLException e) {
            String errorMsg = e.getMessage().toLowerCase();
            
            if (errorMsg.contains("chk_fab_est")) {
                return "El estado del registro debe ser A, I o *.";
            } else {
                return "Error al actualizar fabricante: " + e.getMessage();
            }
        }
    }
    
    // Método para eliminar lógicamente un fabricante (cambiar estado a '*')
    public boolean eliminarLogicamenteFabricante(int fabCod) {
        String sql = "UPDATE FABRICANTE_PRODUCTO SET FabEstReg = '*' WHERE FabCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, fabCod);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente fabricante: " + e.getMessage());
            return false;
        }
    }
    
    // Método para inactivar fabricante
    public boolean inactivarFabricante(int fabCod) {
        String sql = "UPDATE FABRICANTE_PRODUCTO SET FabEstReg = 'I' WHERE FabCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, fabCod);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al inactivar fabricante: " + e.getMessage());
            return false;
        }
    }
    
    // Método para reactivar fabricante
    public boolean reactivarFabricante(int fabCod) {
        String sql = "UPDATE FABRICANTE_PRODUCTO SET FabEstReg = 'A' WHERE FabCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, fabCod);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al reactivar fabricante: " + e.getMessage());
            return false;
        }
    }
    
    // Método para eliminar físicamente un fabricante
    public boolean eliminarFabricante(int fabCod) {
        String sql = "DELETE FROM FABRICANTE_PRODUCTO WHERE FabCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, fabCod);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar fabricante: " + e.getMessage());
            return false;
        }
    }
    
    // Método para buscar fabricante por código
    public FabricanteProducto obtenerFabricantePorCodigo(int fabCod) {
        String sql = "SELECT * FROM FABRICANTE_PRODUCTO WHERE FabCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, fabCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new FabricanteProducto(
                    rs.getInt("FabCod"),
                    rs.getString("FabNom"),
                    rs.getString("FabEstReg").charAt(0)
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener fabricante por código: " + e.getMessage());
        }
        
        return null;
    }
    
    // Método para buscar fabricantes por nombre (coincidencia parcial)
    public List<FabricanteProducto> buscarFabricantesPorNombre(String nombre) {
        String sql = "SELECT * FROM FABRICANTE_PRODUCTO WHERE FabNom LIKE ? ORDER BY FabNom";
        List<FabricanteProducto> fabricantes = new ArrayList<>();
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                FabricanteProducto fab = new FabricanteProducto(
                    rs.getInt("FabCod"),
                    rs.getString("FabNom"),
                    rs.getString("FabEstReg").charAt(0)
                );
                fabricantes.add(fab);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar fabricantes por nombre: " + e.getMessage());
        }
        
        return fabricantes;
    }
    
    // Método para obtener todos los fabricantes
    public List<FabricanteProducto> obtenerTodosFabricantes() {
        String sql = "SELECT * FROM FABRICANTE_PRODUCTO ORDER BY FabCod";
        List<FabricanteProducto> fabricantes = new ArrayList<>();
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                FabricanteProducto fab = new FabricanteProducto(
                    rs.getInt("FabCod"),
                    rs.getString("FabNom"),
                    rs.getString("FabEstReg").charAt(0)
                );
                fabricantes.add(fab);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los fabricantes: " + e.getMessage());
        }
        
        return fabricantes;
    }
    
    // Método para obtener todos los fabricantes activos
    public List<FabricanteProducto> obtenerFabricantesActivos() {
        String sql = "SELECT * FROM FABRICANTE_PRODUCTO WHERE FabEstReg = 'A' ORDER BY FabCod";
        List<FabricanteProducto> fabricantes = new ArrayList<>();
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                FabricanteProducto fab = new FabricanteProducto(
                    rs.getInt("FabCod"),
                    rs.getString("FabNom"),
                    rs.getString("FabEstReg").charAt(0)
                );
                fabricantes.add(fab);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener fabricantes activos: " + e.getMessage());
        }
        
        return fabricantes;
    }
    
    // Método para verificar si existe un fabricante
    public boolean existeFabricante(int fabCod) {
        String sql = "SELECT COUNT(*) FROM FABRICANTE_PRODUCTO WHERE FabCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, fabCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de fabricante: " + e.getMessage());
        }
        
        return false;
    }
    
    // Método para verificar si existe un fabricante con el mismo nombre (para evitar duplicados)
    public boolean existeNombreFabricante(String nombre, int excludeId) {
        String sql = "SELECT COUNT(*) FROM FABRICANTE_PRODUCTO WHERE FabNom = ? AND FabCod != ? AND FabEstReg != '*'";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            stmt.setInt(2, excludeId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de nombre: " + e.getMessage());
        }
        
        return false;
    }
    
    // Método para verificar si un fabricante tiene productos asociados
    public boolean tieneProductosAsociados(int fabCod) {
        String sql = "SELECT COUNT(*) FROM PRODUCTO WHERE FabCod = ? AND ProdEstReg != '*'";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, fabCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar productos asociados: " + e.getMessage());
        }
        
        return false;
    }
    
    // Método para cambiar el estado de un fabricante
    public boolean cambiarEstadoFabricante(int fabCod, char nuevoEstado) {
        // Validar que el estado sea válido según el CHECK constraint
        if (nuevoEstado != 'A' && nuevoEstado != 'I' && nuevoEstado != '*') {
            System.err.println("Estado inválido. Debe ser 'A', 'I' o '*'");
            return false;
        }
        
        String sql = "UPDATE FABRICANTE_PRODUCTO SET FabEstReg = ? WHERE FabCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, String.valueOf(nuevoEstado));
            stmt.setInt(2, fabCod);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado del fabricante: " + e.getMessage());
            return false;
        }
    }
    
    // Método para contar fabricantes por estado
    public int contarFabricantesPorEstado(char estado) {
        String sql = "SELECT COUNT(*) FROM FABRICANTE_PRODUCTO WHERE FabEstReg = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, String.valueOf(estado));
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar fabricantes por estado: " + e.getMessage());
        }
        
        return 0;
    }
}