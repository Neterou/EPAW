package epaw.lab1.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.stream.Collectors;

public class DBManager implements AutoCloseable {

	private Connection connection = null;
	private static final String DB_FILE = "lab1.db";

	public DBManager() throws Exception {
		// SQLite connection
		Class.forName("org.sqlite.JDBC");
		boolean dbExists = Files.exists(Paths.get(DB_FILE));
		connection = DriverManager.getConnection("jdbc:sqlite:" + DB_FILE);

		// Enable foreign keys in SQLite
		try (Statement stmt = connection.createStatement()) {
			stmt.execute("PRAGMA foreign_keys = ON;");
		}

		if (!dbExists) {
			initDatabase();
		}
	}

	private void initDatabase() throws Exception {
		String schemaPath = "DB.txt";
		try (BufferedReader reader = new BufferedReader(new FileReader(schemaPath))) {
			String schema = reader.lines().collect(Collectors.joining("\n"));
			String[] statements = schema.split(";");
			try (Statement stmt = connection.createStatement()) {
				for (String sql : statements) {
					if (!sql.trim().isEmpty()) {
						stmt.execute(sql);
					}
				}
			}
		}
	}

	public PreparedStatement prepareStatement(String query) throws SQLException {
		return connection.prepareStatement(query);
	}

	// Métodos de validación para el Lab 2
	public boolean emailExists(String email) throws SQLException {
		String query = "SELECT COUNT(*) FROM users WHERE email = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		}
		return false;
	}

	public boolean usernameExists(String username) throws SQLException {
		String query = "SELECT COUNT(*) FROM users WHERE username = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		}
		return false;
	}

	public boolean dniExists(String dni) throws SQLException {
		String query = "SELECT COUNT(*) FROM users WHERE dni = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, dni);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		}
		return false;
	}

	// Insertar usuario (sin hashing de contraseña - TODO: agregar hashing en producción)
	public boolean insertUser(String name, String surname, String birthDate, String email,
			String username, String dni, String country, String city, String zip,
			String phone, String gender, String password) throws SQLException {
		String query = "INSERT INTO users (name, surname, birth_date, email, username, " +
				"dni, country, city, zip, phone, gender, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, name);
			stmt.setString(2, surname);
			stmt.setString(3, birthDate);
			stmt.setString(4, email);
			stmt.setString(5, username);
			stmt.setString(6, dni);
			stmt.setString(7, country);
			stmt.setString(8, city);
			stmt.setString(9, zip);
			stmt.setString(10, phone);
			stmt.setString(11, gender);
			stmt.setString(12, password);

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		}
	}

	@Override
	public void close() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
		}
	}
}