package conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Crud {
    
    // Método para insertar un nuevo cliente
    public static boolean insertarCliente(String descripcion, double limiteCredito, char estadoRegistro) {
        String sql = "INSERT INTO CLASCLIENTE (CatCliDesc, CatCliLimCred, CatCliEstReg) VALUES (?, ?, ?)";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, descripcion);
            pstmt.setDouble(2, limiteCredito);
            pstmt.setString(3, String.valueOf(estadoRegistro));
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar cliente: " + e.getMessage());
            return false;
        }
    }
    
    // Método para consultar todos los clientes
    public static List<String> consultarTodosClientes() {
        String sql = "SELECT * FROM CLASCLIENTE";
        List<String> clientes = new ArrayList<>();
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                String cliente = "ID: " + rs.getInt("CatCliCod") + 
                               ", Descripción: " + rs.getString("CatCliDesc") + 
                               ", Límite Crédito: " + rs.getDouble("CatCliLimCred") + 
                               ", Estado: " + rs.getString("CatCliEstReg");
                clientes.add(cliente);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al consultar clientes: " + e.getMessage());
        }
        
        return clientes;
    }
    
    // Método para consultar un cliente por ID
    public static String consultarClientePorId(int id) {
        String sql = "SELECT * FROM CLASCLIENTE WHERE CatCliCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return "ID: " + rs.getInt("CatCliCod") + 
                       ", Descripción: " + rs.getString("CatCliDesc") + 
                       ", Límite Crédito: " + rs.getDouble("CatCliLimCred") + 
                       ", Estado: " + rs.getString("CatCliEstReg");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al consultar cliente: " + e.getMessage());
        }
        
        return null;
    }
    
    // Método para actualizar un cliente
    public static boolean actualizarCliente(int id, String descripcion, double limiteCredito, char estadoRegistro) {
        String sql = "UPDATE CLASCLIENTE SET CatCliDesc = ?, CatCliLimCred = ?, CatCliEstReg = ? WHERE CatCliCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, descripcion);
            pstmt.setDouble(2, limiteCredito);
            pstmt.setString(3, String.valueOf(estadoRegistro));
            pstmt.setInt(4, id);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }
    
    // Método para eliminar un cliente
    public static boolean eliminarCliente(int id) {
        String sql = "DELETE FROM CLASCLIENTE WHERE CatCliCod = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }
    
    // Método principal para probar las operaciones
    public static void main(String[] args) {
        // Ejemplo de uso
        System.out.println("=== PRUEBAS CRUD CLASCLIENTE ===");
        
        // Insertar
        System.out.println("Insertando cliente...");
        boolean insertado = insertarCliente("Cliente Premium", 5000.00, 'A');
        System.out.println("Cliente insertado: " + insertado);
        
        // Consultar todos
        System.out.println("\nConsultando todos los clientes:");
        List<String> todos = consultarTodosClientes();
        for (String cliente : todos) {
            System.out.println(cliente);
        }
        
        // Consultar por ID
        System.out.println("\nConsultando cliente por ID 1:");
        String cliente = consultarClientePorId(1);
        if (cliente != null) {
            System.out.println(cliente);
        } else {
            System.out.println("Cliente no encontrado");
        }
        
        // Actualizar
        System.out.println("\nActualizando cliente ID 1...");
        boolean actualizado = actualizarCliente(1, "Cliente VIP", 10000.00, 'A');
        System.out.println("Cliente actualizado: " + actualizado);
        
        // Eliminar
        System.out.println("\nEliminando cliente ID 1...");
        boolean eliminado = eliminarCliente(1);
        System.out.println("Cliente eliminado: " + eliminado);
    }
}