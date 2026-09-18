package edu.umg.programacion2.proyecto.dao;

import edu.umg.programacion2.proyecto.db.ConexionBD;
import edu.umg.programacion2.proyecto.modelo.Empleado;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    // Método para obtener todos los empleados de la base de datos
    public List<Empleado> obtenerTodos() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, departamento, salario, fecha_contratacion, activo FROM empleados";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String departamento = rs.getString("departamento");
                double salario = rs.getDouble("salario");
                LocalDate fecha = rs.getDate("fecha_contratacion").toLocalDate();
                boolean activo = rs.getBoolean("activo");

                lista.add(new Empleado(id, nombre, departamento, salario, fecha, activo));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar empleados: " + e.getMessage());
        }
        return lista;
    }

    // Método para insertar un nuevo empleado
    public boolean guardar(Empleado emp) {
        String sql = "INSERT INTO empleados (nombre, departamento, salario, fecha_contratacion, activo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, emp.getNombre());
            stmt.setString(2, emp.getDepartamento());
            stmt.setDouble(3, emp.getSalario());
            stmt.setDate(4, Date.valueOf(emp.getFechaContratacion()));
            stmt.setBoolean(5, emp.isActivo());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar empleado: " + e.getMessage());
            return false;
        }
    }
}