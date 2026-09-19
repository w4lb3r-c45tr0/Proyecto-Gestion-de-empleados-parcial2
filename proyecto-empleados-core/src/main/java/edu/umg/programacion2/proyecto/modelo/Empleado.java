package edu.umg.programacion2.proyecto.modelo;

import java.time.LocalDate;

/**
 * Modelo inmutable que representa la entidad Empleado.
 */
public class Empleado {
    private final int id;
    private final String nombre;
    private final String departamento;
    private final double salario;
    private final LocalDate fechaContratacion;
    private final boolean activo;
    private final String tipoContrato; 
    // Constructor completo para registros que vienen de la base de datos
    public Empleado(int id, String nombre, String departamento, double salario, LocalDate fechaContratacion, boolean activo, String tipoContrato) {
        this.id = id;
        this.nombre = nombre;
        this.departamento = departamento;
        this.salario = salario;
        this.fechaContratacion = fechaContratacion;
        this.activo = activo;
        this.tipoContrato = tipoContrato;
    }

    // Constructor sin ID para nuevos empleados a registrar
    public Empleado(String nombre, String departamento, double salario, LocalDate fechaContratacion, boolean activo, String tipoContrato) {
        this(0, nombre, departamento, salario, fechaContratacion, activo, tipoContrato);
    }

    // Métodos Getter (sin setters para garantizar inmutabilidad)
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDepartamento() { return departamento; }
    public double getSalario() { return salario; }
    public LocalDate getFechaContratacion() { return fechaContratacion; }
    public boolean isActivo() { return activo; }
    public String getTipoContrato() { return tipoContrato; }

    @Override
    public String toString() {
        return String.format("%d | %s | %s | Q%.2f | %s | %s | %s",
                id, nombre, departamento, salario, fechaContratacion, activo ? "Activo" : "Inactivo", tipoContrato);
    }
}