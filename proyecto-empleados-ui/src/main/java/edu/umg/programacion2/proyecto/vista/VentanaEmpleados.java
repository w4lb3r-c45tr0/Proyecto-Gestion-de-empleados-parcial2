package edu.umg.programacion2.proyecto.vista;

import edu.umg.programacion2.proyecto.dao.EmpleadoDAO;
import edu.umg.programacion2.proyecto.modelo.Empleado;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaEmpleados extends JFrame {
    
    private EmpleadoDAO dao;
    private JTable tablaEmpleados;
    private DefaultTableModel modeloTabla;

    public VentanaEmpleados() {
        // Instanciamos el DAO que viene de nuestro módulo Core
        dao = new EmpleadoDAO();
        
        configurarVentana();
        inicializarUI();
        cargarDatosEnTabla();
    }

    private void configurarVentana() {
        setTitle("Sistema de Gestión de Empleados - Arquitectura Multi-Módulo");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla
    }

    private void inicializarUI() {
        // Configurar las columnas de la tabla
        String[] columnas = {"ID", "Nombre", "Departamento", "Salario", "Contratación", "Activo"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaEmpleados = new JTable(modeloTabla);
        
        // Agregar la tabla a un panel con scroll y ponerla en el centro de la ventana
        add(new JScrollPane(tablaEmpleados), BorderLayout.CENTER);
    }

    private void cargarDatosEnTabla() {
        // Limpiar tabla por si acaso
        modeloTabla.setRowCount(0);
        
        // Usar el DAO para traer los datos de MySQL
        List<Empleado> empleados = dao.obtenerTodos();
        
        for (Empleado emp : empleados) {
            Object[] fila = {
                    emp.getId(),
                    emp.getNombre(),
                    emp.getDepartamento(),
                    "Q " + emp.getSalario(),
                    emp.getFechaContratacion(),
                    emp.isActivo() ? "Sí" : "No"
            };
            modeloTabla.addRow(fila);
        }
    }

    public static void main(String[] args) {
        // Hilo de ejecución seguro para Java Swing
        SwingUtilities.invokeLater(() -> {
            VentanaEmpleados ventana = new VentanaEmpleados();
            ventana.setVisible(true);
        });
    }
}