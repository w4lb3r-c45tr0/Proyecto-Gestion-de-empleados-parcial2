package edu.umg.programacion2.proyecto.db; // Ajusta si tu paquete se llama distinto

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBD {
    
    private static final Properties properties = new Properties();

    // Este bloque estático se ejecuta una sola vez al iniciar el programa
    static {
        try (InputStream input = ConexionBD.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                System.err.println("¡Error! No se encontró el archivo database.properties");
            } else {
                properties.load(input);
                // Cargar el driver moderno explícitamente
                Class.forName("com.mysql.cj.jdbc.Driver");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la configuración de la BD: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        // Lee las propiedades directamente del archivo cargado
        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password")
        );
    }
}