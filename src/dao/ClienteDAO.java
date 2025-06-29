package dao;
//corregido
import conexion.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.Cliente;

public class ClienteDAO {

    public List<Cliente> obtenerTodosClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT c.CliCod, c.CliEmp, c.RepCod, c.CliLim, c.CliNom, c.CliApePat, c.CliApeMat, " +
                     "c.CiuCod, c.CliDirDetalle, c.CliTel, c.CliCor, c.CatCliCod, c.CliEstReg, " +
                     "r.RepNom as RepresentanteNombre, cc.CatCliDesc as CategoriaDescripcion, " +
                     "ci.CiuNom as CiudadNombre " +
                     "FROM CLIENTE c " +
                     "LEFT JOIN REPVENTA r ON c.RepCod = r.RepCod " +
                     "LEFT JOIN CLASCLIENTE cc ON c.CatCliCod = cc.CatCliCod " +
                     "LEFT JOIN CIUDADES ci ON c.CiuCod = ci.CiuCod " +
                     "ORDER BY c.CliCod";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getInt("CliCod"),
                    rs.getString("CliEmp"),
                    (Integer) rs.getObject("RepCod"),
                    rs.getString("CliLim"),
                    rs.getString("CliNom"),
                    rs.getString("CliApePat"),
                    rs.getString("CliApeMat"),
                    (Integer) rs.getObject("CiuCod"),
                    rs.getString("CliDirDetalle"),
                    (Long) rs.getObject("CliTel"),
                    rs.getString("CliCor"),
                    rs.getInt("CatCliCod"),
                    rs.getString("CliEstReg").charAt(0),
                    rs.getString("RepresentanteNombre"),
                    rs.getString("CategoriaDescripcion"),
                    rs.getString("CiudadNombre")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los clientes: " + e.getMessage());
        }
        return clientes;
    }

    public Cliente obtenerClientePorCodigo(int cliCod) {
        String sql = "SELECT c.CliCod, c.CliEmp, c.RepCod, c.CliLim, c.CliNom, c.CliApePat, c.CliApeMat, " +
                     "c.CiuCod, c.CliDirDetalle, c.CliTel, c.CliCor, c.CatCliCod, c.CliEstReg, " +
                     "r.RepNom as RepresentanteNombre, cc.CatCliDesc as CategoriaDescripcion, " +
                     "ci.CiuNom as CiudadNombre " +
                     "FROM CLIENTE c " +
                     "LEFT JOIN REPVENTA r ON c.RepCod = r.RepCod " +
                     "LEFT JOIN CLASCLIENTE cc ON c.CatCliCod = cc.CatCliCod " +
                     "LEFT JOIN CIUDADES ci ON c.CiuCod = ci.CiuCod " +
                     "WHERE c.CliCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cliCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Cliente(
                    rs.getInt("CliCod"),
                    rs.getString("CliEmp"),
                    (Integer) rs.getObject("RepCod"),
                    rs.getString("CliLim"),
                    rs.getString("CliNom"),
                    rs.getString("CliApePat"),
                    rs.getString("CliApeMat"),
                    (Integer) rs.getObject("CiuCod"),
                    rs.getString("CliDirDetalle"),
                    (Long) rs.getObject("CliTel"),
                    rs.getString("CliCor"),
                    rs.getInt("CatCliCod"),
                    rs.getString("CliEstReg").charAt(0),
                    rs.getString("RepresentanteNombre"),
                    rs.getString("CategoriaDescripcion"),
                    rs.getString("CiudadNombre")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cliente: " + e.getMessage());
        }
        return null;
    }

    public String insertarCliente(Cliente cliente) {
        String sql = "INSERT INTO CLIENTE (CliEmp, RepCod, CliLim, CliNom, CliApePat, CliApeMat, " +
                     "CiuCod, CliDirDetalle, CliTel, CliCor, CatCliCod, CliEstReg) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cliente.getCliEmp());
            if (cliente.getRepCod() != null) {
                stmt.setInt(2, cliente.getRepCod());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setString(3, cliente.getCliLim());
            stmt.setString(4, cliente.getCliNom());
            stmt.setString(5, cliente.getCliApePat());
            stmt.setString(6, cliente.getCliApeMat());
            if (cliente.getCiuCod() != null) {
                stmt.setInt(7, cliente.getCiuCod());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            stmt.setString(8, cliente.getCliDirDetalle());
            if (cliente.getCliTel() != null) {
                stmt.setLong(9, cliente.getCliTel());
            } else {
                stmt.setNull(9, Types.BIGINT);
            }
            stmt.setString(10, cliente.getCliCor());
            stmt.setInt(11, cliente.getCatCliCod());
            stmt.setString(12, String.valueOf(cliente.getCliEstReg()));
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return null; // No error, return null to indicate success
            } else {
                return "Error desconocido al insertar el cliente.";
            }
        } catch (SQLException e) {
            String errorMsg = e.getMessage().toLowerCase();
            
            if (errorMsg.contains("chk_cli_tel")) {
                return "El teléfono debe tener al menos 9 dígitos.";
            } else if (errorMsg.contains("fk_cli_representante")) {
                return "El código de representante no existe o está inactivo.";
            } else if (errorMsg.contains("fk_cli_categoria")) {
                return "El código de categoría de cliente no existe o está inactivo.";
            } else if (errorMsg.contains("fk_cli_ciudad")) {
                return "El código de ciudad no existe o está inactivo.";
            } else if (errorMsg.contains("chk_cli_est")) {
                return "El estado del registro debe ser A, I o *.";
            } else {
                return "Error al insertar cliente: " + e.getMessage();
            }
        }
    }

    public String actualizarCliente(Cliente cliente) {
        String sql = "UPDATE CLIENTE SET CliEmp = ?, RepCod = ?, CliLim = ?, CliNom = ?, CliApePat = ?, " +
                     "CliApeMat = ?, CiuCod = ?, CliDirDetalle = ?, CliTel = ?, CliCor = ?, " +
                     "CatCliCod = ?, CliEstReg = ? WHERE CliCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cliente.getCliEmp());
            if (cliente.getRepCod() != null) {
                stmt.setInt(2, cliente.getRepCod());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setString(3, cliente.getCliLim());
            stmt.setString(4, cliente.getCliNom());
            stmt.setString(5, cliente.getCliApePat());
            stmt.setString(6, cliente.getCliApeMat());
            if (cliente.getCiuCod() != null) {
                stmt.setInt(7, cliente.getCiuCod());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            stmt.setString(8, cliente.getCliDirDetalle());
            if (cliente.getCliTel() != null) {
                stmt.setLong(9, cliente.getCliTel());
            } else {
                stmt.setNull(9, Types.BIGINT);
            }
            stmt.setString(10, cliente.getCliCor());
            stmt.setInt(11, cliente.getCatCliCod());
            stmt.setString(12, String.valueOf(cliente.getCliEstReg()));
            stmt.setInt(13, cliente.getCliCod());
            
            return (stmt.executeUpdate() > 0) ? null : "Error al actualizar cliente.";
        } catch (SQLException e) {
            String errorMsg = e.getMessage().toLowerCase();
            
            if (errorMsg.contains("chk_cli_tel")) {
                return "El teléfono debe tener al menos 9 dígitos.";
            } else if (errorMsg.contains("fk_cli_representante")) {
                return "El código de representante no existe o está inactivo.";
            } else if (errorMsg.contains("fk_cli_categoria")) {
                return "El código de categoría de cliente no existe o está inactivo.";
            } else if (errorMsg.contains("fk_cli_ciudad")) {
                return "El código de ciudad no existe o está inactivo.";
            } else if (errorMsg.contains("chk_cli_est")) {
                return "El estado del registro debe ser A, I o *.";
            } else {
                return "Error al actualizar cliente: " + e.getMessage();
            }
        }
    }

    public boolean eliminarLogicamenteCliente(int cliCod) {
        String sql = "UPDATE CLIENTE SET CliEstReg = '*' WHERE CliCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cliCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar lógicamente cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean inactivarCliente(int cliCod) {
        String sql = "UPDATE CLIENTE SET CliEstReg = 'I' WHERE CliCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cliCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al inactivar cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean reactivarCliente(int cliCod) {
        String sql = "UPDATE CLIENTE SET CliEstReg = 'A' WHERE CliCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cliCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al reactivar cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarCliente(int cliCod) {
        String sql = "DELETE FROM CLIENTE WHERE CliCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cliCod);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }

    public List<Cliente> obtenerClientesActivos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT c.CliCod, c.CliEmp, c.RepCod, c.CliLim, c.CliNom, c.CliApePat, c.CliApeMat, " +
                     "c.CiuCod, c.CliDirDetalle, c.CliTel, c.CliCor, c.CatCliCod, c.CliEstReg, " +
                     "r.RepNom as RepresentanteNombre, cc.CatCliDesc as CategoriaDescripcion, " +
                     "ci.CiuNom as CiudadNombre " +
                     "FROM CLIENTE c " +
                     "LEFT JOIN REPVENTA r ON c.RepCod = r.RepCod " +
                     "LEFT JOIN CLASCLIENTE cc ON c.CatCliCod = cc.CatCliCod " +
                     "LEFT JOIN CIUDADES ci ON c.CiuCod = ci.CiuCod " +
                     "WHERE c.CliEstReg = 'A' ORDER BY c.CliCod";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getInt("CliCod"),
                    rs.getString("CliEmp"),
                    (Integer) rs.getObject("RepCod"),
                    rs.getString("CliLim"),
                    rs.getString("CliNom"),
                    rs.getString("CliApePat"),
                    rs.getString("CliApeMat"),
                    (Integer) rs.getObject("CiuCod"),
                    rs.getString("CliDirDetalle"),
                    (Long) rs.getObject("CliTel"),
                    rs.getString("CliCor"),
                    rs.getInt("CatCliCod"),
                    rs.getString("CliEstReg").charAt(0),
                    rs.getString("RepresentanteNombre"),
                    rs.getString("CategoriaDescripcion"),
                    rs.getString("CiudadNombre")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener clientes activos: " + e.getMessage());
        }
        return clientes;
    }

    public List<Cliente> buscarClientesPorNombre(String nombre) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT c.CliCod, c.CliEmp, c.RepCod, c.CliLim, c.CliNom, c.CliApePat, c.CliApeMat, " +
                     "c.CiuCod, c.CliDirDetalle, c.CliTel, c.CliCor, c.CatCliCod, c.CliEstReg, " +
                     "r.RepNom as RepresentanteNombre, cc.CatCliDesc as CategoriaDescripcion, " +
                     "ci.CiuNom as CiudadNombre " +
                     "FROM CLIENTE c " +
                     "LEFT JOIN REPVENTA r ON c.RepCod = r.RepCod " +
                     "LEFT JOIN CLASCLIENTE cc ON c.CatCliCod = cc.CatCliCod " +
                     "LEFT JOIN CIUDADES ci ON c.CiuCod = ci.CiuCod " +
                     "WHERE c.CliNom LIKE ? OR c.CliApePat LIKE ? OR c.CliApeMat LIKE ? " +
                     "ORDER BY c.CliCod";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + nombre + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getInt("CliCod"),
                    rs.getString("CliEmp"),
                    (Integer) rs.getObject("RepCod"),
                    rs.getString("CliLim"),
                    rs.getString("CliNom"),
                    rs.getString("CliApePat"),
                    rs.getString("CliApeMat"),
                    (Integer) rs.getObject("CiuCod"),
                    rs.getString("CliDirDetalle"),
                    (Long) rs.getObject("CliTel"),
                    rs.getString("CliCor"),
                    rs.getInt("CatCliCod"),
                    rs.getString("CliEstReg").charAt(0),
                    rs.getString("RepresentanteNombre"),
                    rs.getString("CategoriaDescripcion"),
                    rs.getString("CiudadNombre")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar clientes por nombre: " + e.getMessage());
        }
        return clientes;
    }

    public List<Cliente> buscarClientesPorRepresentante(int repCod) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT c.CliCod, c.CliEmp, c.RepCod, c.CliLim, c.CliNom, c.CliApePat, c.CliApeMat, " +
                     "c.CiuCod, c.CliDirDetalle, c.CliTel, c.CliCor, c.CatCliCod, c.CliEstReg, " +
                     "r.RepNom as RepresentanteNombre, cc.CatCliDesc as CategoriaDescripcion, " +
                     "ci.CiuNom as CiudadNombre " +
                     "FROM CLIENTE c " +
                     "LEFT JOIN REPVENTA r ON c.RepCod = r.RepCod " +
                     "LEFT JOIN CLASCLIENTE cc ON c.CatCliCod = cc.CatCliCod " +
                     "LEFT JOIN CIUDADES ci ON c.CiuCod = ci.CiuCod " +
                     "WHERE c.RepCod = ? ORDER BY c.CliCod";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, repCod);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getInt("CliCod"),
                    rs.getString("CliEmp"),
                    (Integer) rs.getObject("RepCod"),
                    rs.getString("CliLim"),
                    rs.getString("CliNom"),
                    rs.getString("CliApePat"),
                    rs.getString("CliApeMat"),
                    (Integer) rs.getObject("CiuCod"),
                    rs.getString("CliDirDetalle"),
                    (Long) rs.getObject("CliTel"),
                    rs.getString("CliCor"),
                    rs.getInt("CatCliCod"),
                    rs.getString("CliEstReg").charAt(0),
                    rs.getString("RepresentanteNombre"),
                    rs.getString("CategoriaDescripcion"),
                    rs.getString("CiudadNombre")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar clientes por representante: " + e.getMessage());
        }
        return clientes;
    }

    public boolean existeCliente(int cliCod) {
        String sql = "SELECT COUNT(*) FROM CLIENTE WHERE CliCod = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cliCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de cliente: " + e.getMessage());
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

    public boolean validarReferenciaCategoria(int catCliCod) {
        String sql = "SELECT COUNT(*) FROM CLASCLIENTE WHERE CatCliCod = ? AND CatCliEstReg = 'A'";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, catCliCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al validar referencia de categoría: " + e.getMessage());
        }
        return false;
    }

    public boolean validarReferenciaCiudad(int ciuCod) {
        String sql = "SELECT COUNT(*) FROM CIUDADES WHERE CiuCod = ? AND CiuEstReg = 'A'";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ciuCod);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al validar referencia de ciudad: " + e.getMessage());
        }
        return false;
    }
}