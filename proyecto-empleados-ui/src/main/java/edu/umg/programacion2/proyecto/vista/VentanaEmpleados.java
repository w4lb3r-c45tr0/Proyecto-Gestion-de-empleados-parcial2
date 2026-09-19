package edu.umg.programacion2.proyecto.vista;

import edu.umg.programacion2.proyecto.dao.EmpleadoDAO;
import edu.umg.programacion2.proyecto.modelo.Empleado;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class VentanaEmpleados extends JFrame {
    
    private EmpleadoDAO dao;
    private JTable tablaEmpleados;
    private DefaultTableModel modeloTabla;

    // Componentes del formulario
    private JTextField txtId, txtNombre, txtDepartamento, txtSalario, txtFecha;
    private JCheckBox chkActivo;
    private JComboBox<String> cbTipoContrato;
    private static final String[] OPCIONES_CONTRATO = {"Permanente", "Temporal", "Por hora"};
    
    // Componentes de la Mejora #9 (Análisis de Salarios)
    private JLabel lblTotalSalarios;
    private JLabel lblPromedioSalarios;

    private JButton btnGuardar, btnActualizar, btnEliminar, btnLimpiar;

    public VentanaEmpleados() {
        dao = new EmpleadoDAO();
        configurarVentana();
        inicializarUI();
        cargarDatosEnTabla();
    }

    private void configurarVentana() {
        setTitle("Sistema de Gestión de Empleados - CRUD Completo");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void inicializarUI() {
        setLayout(new BorderLayout());

        // --- PANEL SUPERIOR: FORMULARIO ---
        JPanel panelFormulario = new JPanel(new GridLayout(0, 4, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Gestión de Datos"));

        panelFormulario.add(new JLabel("ID (Automático):"));
        txtId = new JTextField();
        txtId.setEditable(false);
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Departamento:"));
        txtDepartamento = new JTextField();
        panelFormulario.add(txtDepartamento);

        panelFormulario.add(new JLabel("Salario (Q):"));
        txtSalario = new JTextField();
        panelFormulario.add(txtSalario);

        panelFormulario.add(new JLabel("Fecha (YYYY-MM-DD):"));
        txtFecha = new JTextField(LocalDate.now().toString());
        panelFormulario.add(txtFecha);

        panelFormulario.add(new JLabel("Estado:"));
        chkActivo = new JCheckBox("Activo");
        chkActivo.setSelected(true);
        panelFormulario.add(chkActivo);

        panelFormulario.add(new JLabel("Tipo Contrato:"));
        cbTipoContrato = new JComboBox<>(OPCIONES_CONTRATO);
        panelFormulario.add(cbTipoContrato);

        add(panelFormulario, BorderLayout.NORTH);

        // --- PANEL CENTRAL: TABLA ---
        String[] columnas = {"ID", "Nombre", "Departamento", "Salario", "Contratación", "Activo", "Tipo Contrato"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaEmpleados = new JTable(modeloTabla);
        
        tablaEmpleados.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaEmpleados.getSelectedRow() != -1) {
                llenarFormularioDesdeTabla();
            }
        });

        add(new JScrollPane(tablaEmpleados), BorderLayout.CENTER);

        // --- PANEL INFERIOR: BOTONES Y ESTADÍSTICAS (Mejora #9) ---
        JPanel panelInferior = new JPanel(new BorderLayout());

        // Subpanel de estadísticas
        JPanel panelEstadisticas = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
        lblTotalSalarios = new JLabel("Total Salarios: Q0.00");
        lblPromedioSalarios = new JLabel("Promedio: Q0.00");
        lblTotalSalarios.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblPromedioSalarios.setFont(new Font("SansSerif", Font.BOLD, 12));

        panelEstadisticas.add(lblTotalSalarios);
        panelEstadisticas.add(lblPromedioSalarios);

        // Subpanel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnGuardar = new JButton("Guardar Nuevo");
        btnActualizar = new JButton("Actualizar Seleccionado");
        btnEliminar = new JButton("Eliminar Seleccionado");
        btnLimpiar = new JButton("Limpiar Campos");

        btnBotonesAdd(panelBotones);

        panelInferior.add(panelEstadisticas, BorderLayout.NORTH);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);

        // --- FUNCIONES DE LOS BOTONES ---
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> guardarEmpleado());
        btnActualizar.addActionListener(e -> actualizarEmpleado());
        btnEliminar.addActionListener(e -> eliminarEmpleado());
    }

    private void btnBotonesAdd(JPanel panel) {
        panel.add(btnLimpiar);
        panel.add(btnGuardar);
        panel.add(btnActualizar);
        panel.add(btnEliminar);
    }

    private void cargarDatosEnTabla() {
        modeloTabla.setRowCount(0);
        List<Empleado> empleados = dao.obtenerTodos();

        double totalSalarios = 0.0;

        for (Empleado emp : empleados) {
            Object[] fila = {
                    emp.getId(), emp.getNombre(), emp.getDepartamento(), 
                    emp.getSalario(), emp.getFechaContratacion(), emp.isActivo() ? "Sí" : "No",
                    emp.getTipoContrato()
            };
            modeloTabla.addRow(fila);

            // Cálculo iterativo en Java
            totalSalarios += emp.getSalario();
        }

        double promedioSalarios = empleados.isEmpty() ? 0.0 : (totalSalarios / empleados.size());

        // Actualización de etiquetas
        lblTotalSalarios.setText(String.format("Total Salarios: Q%.2f", totalSalarios));
        lblPromedioSalarios.setText(String.format("Promedio: Q%.2f", promedioSalarios));
    }

    private void llenarFormularioDesdeTabla() {
        int fila = tablaEmpleados.getSelectedRow();
        txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
        txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
        txtDepartamento.setText(modeloTabla.getValueAt(fila, 2).toString());
        txtSalario.setText(modeloTabla.getValueAt(fila, 3).toString());
        txtFecha.setText(modeloTabla.getValueAt(fila, 4).toString());
        chkActivo.setSelected(modeloTabla.getValueAt(fila, 5).toString().equals("Sí"));
        cbTipoContrato.setSelectedItem(modeloTabla.getValueAt(fila, 6).toString());
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtDepartamento.setText("");
        txtSalario.setText("");
        txtFecha.setText(LocalDate.now().toString());
        chkActivo.setSelected(true);
        cbTipoContrato.setSelectedIndex(0);
        tablaEmpleados.clearSelection();
    }

    private void guardarEmpleado() {
        if (txtNombre.getText().trim().isEmpty() || txtDepartamento.getText().trim().isEmpty() || txtSalario.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El Nombre, Departamento y Salario son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Empleado emp = new Empleado(
                    txtNombre.getText(), txtDepartamento.getText(), 
                    Double.parseDouble(txtSalario.getText()), LocalDate.parse(txtFecha.getText()), 
                    chkActivo.isSelected(), cbTipoContrato.getSelectedItem().toString()
            );
            if (dao.guardar(emp)) {
                JOptionPane.showMessageDialog(this, "Empleado guardado exitosamente.");
                cargarDatosEnTabla();
                limpiarFormulario();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Revisa los datos: Salario numérico y Fecha en formato YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarEmpleado() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un empleado de la tabla para actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (txtNombre.getText().trim().isEmpty() || txtDepartamento.getText().trim().isEmpty() || txtSalario.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El Nombre, Departamento y Salario son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return; 
        }
        
        try {
            Empleado emp = new Empleado(
                    Integer.parseInt(txtId.getText()), txtNombre.getText(), txtDepartamento.getText(),
                    Double.parseDouble(txtSalario.getText()), LocalDate.parse(txtFecha.getText()), 
                    chkActivo.isSelected(), cbTipoContrato.getSelectedItem().toString()
            );
            if (dao.actualizar(emp)) {
                JOptionPane.showMessageDialog(this, "Empleado actualizado.");
                cargarDatosEnTabla();
                limpiarFormulario();
            }
        } catch (Exception e) {
             JOptionPane.showMessageDialog(this, "Revisa los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarEmpleado() {
        if (txtId.getText().isEmpty()) return;
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar este registro?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (dao.eliminar(Integer.parseInt(txtId.getText()))) {
                JOptionPane.showMessageDialog(this, "Empleado eliminado.");
                cargarDatosEnTabla();
                limpiarFormulario();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaEmpleados().setVisible(true));
    }
}