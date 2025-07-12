package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
	private static final String URL = "jdbc:mysql://localhost:3308/sistema_ventas";
	private static final String USER = "root"; 
	private static final String PASSWORD = "12345678Admin"; 

	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("Conexión exitosa a la base de datos.");
		} catch (ClassNotFoundException e) {
			System.err.println("Error: Driver JDBC no encontrado. " + e.getMessage());
		} catch (SQLException e) {
			System.err.println("Error de conexión a la base de datos: " + e.getMessage());
		}
		return conn;
	}

	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
				System.out.println("Conexión cerrada.");
			} catch (SQLException e) {
				System.err.println("Error al cerrar la conexión: " + e.getMessage());
			}
		}
	}
}