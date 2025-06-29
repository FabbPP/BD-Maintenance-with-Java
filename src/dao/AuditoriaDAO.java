package dao;

import conexion.ConexionBD;
import modelo.Auditoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditoriaDAO {
    
    // Insertar nueva auditoría
    public String insertarAuditoria(Auditoria auditoria) {
        String sql = "INSERT INTO AUDITORIA (UsuCod, AudiFecha, AudiHora, AudiDescri, AudiDet, ModAudiCod, AudiEstReg) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, auditoria.getUsuCod());
            stmt.setDate(2, auditoria.getAudiFecha());
            stmt.setTime(3, auditoria.getAudiHora());
            stmt.setString(4, auditoria.getAudiDescri());
            stmt.setString(5, auditoria.getAudiDet());
            stmt.setInt(6, auditoria.getModAudiCod());
            stmt.setString(7, String.valueOf(auditoria.getAudiEstReg()));
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    auditoria.setAudiCod(generatedKeys.getInt(1));
                }
                return null; // No error, return null to indicate success
            } else {
                return "Error desconocido al insertar la auditoría.";
            }
        } catch (SQLException e) {
            String errorMsg = e.getMessage().toLowerCase();
            
            if (errorMsg.contains("fk_auditoria_usuario")) {
                return "El código de usuario no existe o está inactivo.";
            } else if (errorMsg.contains("fk_auditoria_modulo")) {
                return "El código de módulo de auditoría no existe o está inactivo.";
            } else if (errorMsg.contains("chk_audi_fecha")) {
                return "La fecha de auditoría no puede ser futura.";
            } else if (errorMsg.contains("chk_audi_descri")) {
                return "La descripción de auditoría no puede estar vacía.";
            } else if (errorMsg.contains("chk_audi_estreg")) {
                return "El estado del registro debe ser A, I o *.";
            } else {
                return "Error al insertar auditoría: " + e.getMessage();
            }
        }
    }
    
    // Buscar auditoría por código
    public Auditoria obtenerAuditoriaPorCodigo(int audiCod) {
        String sql = "SELECT a.*, u.UsuNom as UsuarioNom, m.ModAudiDesc as ModuloDesc " +
                     "FROM AUDITORIA a " +
                     "LEFT JOIN USUARIOSISTEMA u ON a.UsuCod = u.UsuCod " +
                     "LEFT JOIN AUD_MODULO m ON a.ModAudiCod = m.ModAudiCod " +
                     "WHERE a.AudiCod = ? AND a.AudiEstReg != '*'";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, audiCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearAuditoria(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener auditoría: " + e.getMessage());
        }
        return null;
    }
    
    // Listar todas las auditorías activas
    public List<Auditoria> obtenerTodasAuditorias() {
        List<Auditoria> auditorias = new ArrayList<>();
        String sql = "SELECT a.*, u.UsuNom as UsuarioNom, m.ModAudiDesc as ModuloDesc " +
                     "FROM AUDITORIA a " +
                     "LEFT JOIN USUARIOSISTEMA u ON a.UsuCod = u.UsuCod " +
                     "LEFT JOIN AUD_MODULO m ON a.ModAudiCod = m.ModAudiCod " +
                     "WHERE a.AudiEstReg = 'A' ORDER BY a.AudiFecha DESC, a.AudiHora DESC";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                auditorias.add(mapearAuditoria(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las auditorías: " + e.getMessage());
        }
        return auditorias;
    }
    
    // Listar auditorías por usuario
    public List<Auditoria> buscarAuditoriasPorUsuario(int usuCod) {
        List<Auditoria> auditorias = new ArrayList<>();
        String sql = "SELECT a.*, u.UsuNom as UsuarioNom, m.ModAudiDesc as ModuloDesc " +
                     "FROM AUDITORIA a " +
                     "LEFT JOIN USUARIOSISTEMA u ON a.UsuCod = u.UsuCod " +
                     "LEFT JOIN AUD_MODULO m ON a.ModAudiCod = m.ModAudiCod " +
                     "WHERE a.UsuCod = ? AND a.AudiEstReg = 'A' ORDER BY a.AudiFecha DESC, a.AudiHora DESC";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, usuCod);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                auditorias.add(mapearAuditoria(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar auditorías por usuario: " + e.getMessage());
        }
        return auditorias;
    }
    
    // Listar auditorías por rango de fechas
    public List<Auditoria> buscarAuditoriasPorRangoFechas(Date fechaInicio, Date fechaFin) {
        List<Auditoria> auditorias = new ArrayList<>();
        String sql = "SELECT a.*, u.UsuNom as UsuarioNom, m.ModAudiDesc as ModuloDesc " +
                     "FROM AUDITORIA a " +
                     "LEFT JOIN USUARIOSISTEMA u ON a.UsuCod = u.UsuCod " +
                     "LEFT JOIN AUD_MODULO m ON a.ModAudiCod = m.ModAudiCod " +
                     "WHERE a.AudiFecha BETWEEN ? AND ? AND a.AudiEstReg = 'A' ORDER BY a.AudiFecha DESC, a.AudiHora DESC";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, fechaInicio);
            stmt.setDate(2, fechaFin);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                auditorias.add(mapearAuditoria(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar auditorías por rango de fechas: " + e.getMessage());
        }
        return auditorias;
    }
    
    // Listar auditorías por módulo
    public List<Auditoria> buscarAuditoriasPorModulo(int modAudiCod) {
        List<Auditoria> auditorias = new ArrayList<>();
        String sql = "SELECT a.*, u.UsuNom as UsuarioNom, m.ModAudiDesc as ModuloDesc " +
                     "FROM AUDITORIA a " +
                     "LEFT JOIN USUARIOSISTEMA u ON a.UsuCod = u.UsuCod " +
                     "LEFT JOIN AUD_MODULO m ON a.ModAudiCod = m.ModAudiCod " +
                     "WHERE a.ModAudiCod = ? AND a.AudiEstReg = 'A' ORDER BY a.AudiFecha DESC, a.AudiHora DESC";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, modAudiCod);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                auditorias.add(mapearAuditoria(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar auditorías por módulo: " + e.getMessage());
        }
        return auditorias;
    }
    
    // Buscar auditorías por descripción
    public List<Auditoria> buscarAuditoriasPorDescripcion(String descripcion) {
        List<Auditoria> auditorias = new ArrayList<>();
        String sql = "SELECT a.*, u.UsuNom as UsuarioNom, m.ModAudiDesc as ModuloDesc " +
                     "FROM AUDITORIA a " +
                     "LEFT JOIN USUARIOSISTEMA u ON a.UsuCod = u.UsuCod " +
                     "LEFT JOIN AUD_MODULO m ON a.ModAudiCod = m.ModAudiCod " +
                     "WHERE a.AudiDescri LIKE ? AND a.AudiEstReg = 'A' ORDER BY a.AudiFecha DESC, a.AudiHora DESC";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + descripcion + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                auditorias.add(mapearAuditoria(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar auditorías por descripción: " + e.getMessage());
        }
        return auditorias;
    }
    
    // Actualizar auditoría
    public String actualizarAuditoria(Auditoria auditoria) {
        String sql = "UPDATE AUDITORIA SET UsuCod = ?, AudiFecha = ?, AudiHora = ?, AudiDescri = ?, AudiDet = ?, ModAudiCod = ?, AudiEstReg = ? WHERE AudiCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, auditoria.getUsuCod());
            stmt.setDate(2, auditoria.getAudiFecha());
            stmt.setTime(3, auditoria.getAudiHora());
            stmt.setString(4, auditoria.getAudiDescri());
            stmt.setString(5, auditoria.getAudiDet());
            stmt.setInt(6, auditoria.getModAudiCod());
            stmt.setString(7, String.valueOf(auditoria.getAudiEstReg()));
            stmt.setInt(8, auditoria.getAudiCod());
            
            return (stmt.executeUpdate() > 0) ? null : "Error al actualizar auditoría.";
        } catch (SQLException e) {
            String errorMsg = e.getMessage().toLowerCase();
            
            if (errorMsg.contains("fk_auditoria_usuario")) {
                return "El código de usuario no existe o está inactivo.";
            } else if (errorMsg.contains("fk_auditoria_modulo")) {
                return "El código de módulo de auditoría no existe o está inactivo.";
            } else if (errorMsg.contains("chk_audi_fecha")) {
                return "La fecha de auditoría no puede ser futura.";
            } else if (errorMsg.contains("chk_audi_descri")) {
                return "La descripción de auditoría no puede estar vacía.";
            } else if (errorMsg.contains("chk_audi_estreg")) {
                return "El estado del registro debe ser A, I o *.";
            } else {
                return "Error al actualizar auditoría: " + e.getMessage();
            }
        }
    }
    
    // Eliminar lógico (cambiar estado a eliminado)
    public boolean eliminarLogicamenteAuditoria(int audiCod) {
        String sql = "UPDATE AUDITORIA SET AudiEstReg = '*' WHERE AudiCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, audiCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente auditoría: " + e.getMessage());
            return false;
        }
    }
    
    // Inactivar auditoría
    public boolean inactivarAuditoria(int audiCod) {
        String sql = "UPDATE AUDITORIA SET AudiEstReg = 'I' WHERE AudiCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, audiCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al inactivar auditoría: " + e.getMessage());
            return false;
        }
    }
    
    // Reactivar auditoría
    public boolean reactivarAuditoria(int audiCod) {
        String sql = "UPDATE AUDITORIA SET AudiEstReg = 'A' WHERE AudiCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, audiCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al reactivar auditoría: " + e.getMessage());
            return false;
        }
    }
    
    // Eliminar físico
    public boolean eliminarAuditoria(int audiCod) {
        String sql = "DELETE FROM AUDITORIA WHERE AudiCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, audiCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar auditoría: " + e.getMessage());
            return false;
        }
    }
    
    // Contar total de auditorías activas
    public int contarTotalAuditorias() {
        String sql = "SELECT COUNT(*) FROM AUDITORIA WHERE AudiEstReg = 'A'";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al contar auditorías: " + e.getMessage());
        }
        return 0;
    }
    
    // Contar auditorías por usuario
    public int contarAuditoriasPorUsuario(int usuCod) {
        String sql = "SELECT COUNT(*) FROM AUDITORIA WHERE UsuCod = ? AND AudiEstReg = 'A'";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, usuCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al contar auditorías por usuario: " + e.getMessage());
        }
        return 0;
    }
    
    // Verificar si existe auditoría
    public boolean existeAuditoria(int audiCod) {
        String sql = "SELECT COUNT(*) FROM AUDITORIA WHERE AudiCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, audiCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de auditoría: " + e.getMessage());
        }
        return false;
    }
    
    // Validar referencia de usuario
    public boolean validarReferenciaUsuario(int usuCod) {
        String sql = "SELECT COUNT(*) FROM USUARIOSISTEMA WHERE UsuCod = ? AND UsuEstReg = 'A'";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, usuCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al validar referencia de usuario: " + e.getMessage());
        }
        return false;
    }
    
    // Validar referencia de módulo de auditoría
    public boolean validarReferenciaAUD_MODULO(int modAudiCod) {
        String sql = "SELECT COUNT(*) FROM AUD_MODULO WHERE ModAudiCod = ? AND ModAudiEstReg = 'A'";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, modAudiCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al validar referencia de módulo de auditoría: " + e.getMessage());
        }
        return false;
    }
    
    // Obtener auditorías por fecha específica
    public List<Auditoria> buscarAuditoriasPorFecha(Date fecha) {
        List<Auditoria> auditorias = new ArrayList<>();
        String sql = "SELECT a.*, u.UsuNom as UsuarioNom, m.ModAudiDesc as ModuloDesc " +
                     "FROM AUDITORIA a " +
                     "LEFT JOIN USUARIOSISTEMA u ON a.UsuCod = u.UsuCod " +
                     "LEFT JOIN AUD_MODULO m ON a.ModAudiCod = m.ModAudiCod " +
                     "WHERE a.AudiFecha = ? AND a.AudiEstReg = 'A' ORDER BY a.AudiHora DESC";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, fecha);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                auditorias.add(mapearAuditoria(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar auditorías por fecha: " + e.getMessage());
        }
        return auditorias;
    }
    
    // Método auxiliar para mapear ResultSet a objeto Auditoria
    private Auditoria mapearAuditoria(ResultSet rs) throws SQLException {
        Auditoria auditoria = new Auditoria();
        auditoria.setAudiCod(rs.getInt("AudiCod"));
        auditoria.setUsuCod(rs.getInt("UsuCod"));
        auditoria.setAudiFecha(rs.getDate("AudiFecha"));
        auditoria.setAudiHora(rs.getTime("AudiHora"));
        auditoria.setAudiDescri(rs.getString("AudiDescri"));
        auditoria.setAudiDet(rs.getString("AudiDet"));
        auditoria.setModAudiCod(rs.getInt("ModAudiCod"));
        auditoria.setAudiEstReg(rs.getString("AudiEstReg").charAt(0));
        
        return auditoria;
    }
}