package gui;

import dao.DepartamentoDAO;
import modelo.Departamento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class DepartamentoFrame extends JFrame {

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtEstadoRegistro;
    private JTable tablaDepartamentos;
    private DefaultTableModel tableModel;

    private JButton btnAdicionar, btnModificar, btnEliminar, btnInactivar, btnReactivar, btnActualizar, btnCancelar, btnSalir;

    private DepartamentoDAO departamentoDAO;
    private int flagCarFlaAct = 0;
    private String operacionActual = "";

    public DepartamentoFrame() {
        setTitle("Mantenimiento de Departamento");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        departamentoDAO = new DepartamentoDAO();
        initComponents();
        cargarTablaDepartamentos();
        habilitarControles(false);
        habilitarBotonesIniciales();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DepartamentoFrame().setVisible(true));
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel panelRegistro = new JPanel(new GridBagLayout());
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registro de Departamento"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelRegistro.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtCodigo = new JTextField(10);
        txtCodigo.setEditable(false); 
        panelRegistro.add(txtCodigo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        txtNombre = new JTextField(30);
        panelRegistro.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Estado Registro:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        txtEstadoRegistro = new JTextField(5);
        txtEstadoRegistro.setEditable(false);
        panelRegistro.add(txtEstadoRegistro, gbc);

        add(panelRegistro, BorderLayout.NORTH);

        // Panel de Tabla
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Departamentos"));
        tableModel = new DefaultTableModel(new Object[]{"Código", "Nombre", "Estado"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaDepartamentos = new JTable(tableModel);
        panelTabla.add(new JScrollPane(tablaDepartamentos), BorderLayout.CENTER);

        tablaDepartamentos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (operacionActual.isEmpty() && e.getClickCount() == 1) {
                    int i = tablaDepartamentos.getSelectedRow();
                    if (i != -1) {
                        txtCodigo.setText(tableModel.getValueAt(i, 0).toString());
                        txtNombre.setText(tableModel.getValueAt(i, 1).toString());
                        txtEstadoRegistro.setText(tableModel.getValueAt(i, 2).toString());
                        habilitarBotonesParaSeleccion();
                    }
                }
            }
        });
        add(panelTabla, BorderLayout.CENTER);

        // Panel de Botones
        JPanel panelBotones = new JPanel(new GridLayout(2, 4, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnAdicionar = new JButton("Adicionar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnCancelar = new JButton("Cancelar");
        btnInactivar = new JButton("Inactivar");
        btnReactivar = new JButton("Reactivar");
        btnActualizar = new JButton("Actualizar");
        btnSalir = new JButton("Salir");

        for (JButton b : new JButton[]{btnAdicionar, btnModificar, btnEliminar, btnCancelar, btnInactivar, btnReactivar, btnActualizar, btnSalir}) {
            panelBotones.add(b);
        }
        add(panelBotones, BorderLayout.SOUTH);

        btnAdicionar.addActionListener(e -> comandoAdicionar());
        btnModificar.addActionListener(e -> comandoModificar());
        btnEliminar.addActionListener(e -> comandoEliminar());
        btnInactivar.addActionListener(e -> comandoInactivar());
        btnReactivar.addActionListener(e -> comandoReactivar());
        btnActualizar.addActionListener(e -> comandoActualizar());
        btnCancelar.addActionListener(e -> comandoCancelar());
        btnSalir.addActionListener(e -> comandoSalir());
    }

    private void comandoAdicionar() {
        limpiarCampos();
        habilitarControles(true);
        txtEstadoRegistro.setText("A");
        operacionActual = "ADICIONAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        txtNombre.requestFocusInWindow();
    }

    private void comandoActualizar() {
        if (flagCarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay ninguna operación pendiente de actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombre = txtNombre.getText().trim();
        String estadoRegistro = txtEstadoRegistro.getText().trim();

        if (nombre.isEmpty() || estadoRegistro.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El Nombre y Estado Registro son obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!estadoRegistro.matches("A|I|\\*")) {
             JOptionPane.showMessageDialog(this, "El Estado de Registro solo puede ser 'A', 'I' o '*'.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
             return;
        }

        Departamento departamento = new Departamento();
        departamento.setDepNom(nombre);
        departamento.setDepEstReg(estadoRegistro.charAt(0));

        boolean exito = false;
        String mensaje = "";

        switch (operacionActual) {
            case "ADICIONAR":
                exito = departamentoDAO.insertarDepartamento(departamento);
                mensaje = exito ? "Departamento adicionado con éxito." : "Error al adicionar departamento.";
                break;
            case "MODIFICAR":
                departamento.setDepCod(Integer.parseInt(txtCodigo.getText()));
                exito = departamentoDAO.actualizarDepartamento(departamento);
                mensaje = exito ? "Departamento modificado con éxito." : "Error al modificar departamento.";
                break;
            case "ELIMINAR":
                int confirmDelete = JOptionPane.showConfirmDialog(this, "¿Está seguro de marcar este registro como ELIMINADO LÓGICAMENTE ('*')?", "Confirmar Eliminación Lógica", JOptionPane.YES_NO_OPTION);
                if (confirmDelete == JOptionPane.YES_OPTION) {
                    exito = departamentoDAO.eliminarLogicamenteDepartamento(Integer.parseInt(txtCodigo.getText()));
                    mensaje = exito ? "Departamento eliminado lógicamente con éxito." : "Error al eliminar lógicamente departamento.";
                } else {
                    mensaje = "Operación de eliminación cancelada.";
                    exito = true;
                }
                break;
            case "INACTIVAR":
                exito = departamentoDAO.inactivarDepartamento(Integer.parseInt(txtCodigo.getText()));
                mensaje = exito ? "Departamento inactivado con éxito." : "Error al inactivar departamento.";
                break;
            case "REACTIVAR":
                exito = departamentoDAO.reactivarDepartamento(Integer.parseInt(txtCodigo.getText()));
                mensaje = exito ? "Departamento reactivado con éxito." : "Error al reactivar departamento.";
                break;
            default:
                JOptionPane.showMessageDialog(this, "Operación no reconocida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }

        if (exito) {
            JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            habilitarControles(false);
            cargarTablaDepartamentos();
            flagCarFlaAct = 0;
            operacionActual = "";
            habilitarBotonesIniciales();
        } else {
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void comandoCancelar() {
        limpiarCampos();
        habilitarControles(false);
        flagCarFlaAct = 0;
        operacionActual = "";
        habilitarBotonesIniciales();
        tablaDepartamentos.clearSelection();
    }

    private void comandoSalir() {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea salir del mantenimiento de Departamentos?", "Confirmar Salida", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    private void cargarTablaDepartamentos() {
        tableModel.setRowCount(0);
        List<Departamento> departamentos = departamentoDAO.obtenerTodosDepartamentos();
        for (Departamento dep : departamentos) {
            tableModel.addRow(new Object[]{
                dep.getDepCod(),
                dep.getDepNom(),
                dep.getDepEstReg()
            });
        }
    }


    private void habilitarControles(boolean b) {
        txtCodigo.setEditable(false);
        txtNombre.setEditable(b);
        txtEstadoRegistro.setEditable(false);
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtEstadoRegistro.setText("");
    }

    private void habilitarBotonesIniciales() {
        btnAdicionar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnInactivar.setEnabled(false);
        btnReactivar.setEnabled(false);
        btnActualizar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnSalir.setEnabled(true);
    }

    private void habilitarBotonesParaOperacion() {
        btnAdicionar.setEnabled(false);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnInactivar.setEnabled(false);
        btnReactivar.setEnabled(false);
        btnActualizar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnSalir.setEnabled(false);
    }

    private void habilitarBotonesParaSeleccion() {
        btnAdicionar.setEnabled(false);
        btnModificar.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnInactivar.setEnabled(true);
        RegCodRegEstRegctivar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnSalir.setEnabled(true);
    }

    private void comandoModificar() {
        int selectedRow = tablaDepartamentos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        habilitarControles(true);
        txtCodigo.setEditable(false);
        txtEstadoRegistro.setEditable(false);
        operacionActual = "MODIFICAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        txtNombre.requestFocusInWindow();
    }

    private void comandoEliminar() {
        int selectedRow = tablaDepartamentos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtNombre.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtEstadoRegistro.setText("*");
        habilitarControles(false);
        operacionActual = "ELIMINAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará para eliminación lógica ('*').\nPresione 'Actualizar' para confirmar.", "Eliminación Lógica", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoInactivar() {
        int selectedRow = tablaDepartamentos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para inactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtNombre.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtEstadoRegistro.setText("I");
        habilitarControles(false);
        operacionActual = "INACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como inactivo ('I').\nPresione 'Actualizar' para confirmar.", "Inactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoReactivar() {
        int selectedRow = tablaDepartamentos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para reactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtNombre.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtEstadoRegistro.setText("A");
        habilitarControles(false);
        operacionActual = "REACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como activo ('A').\nPresione 'Actualizar' para confirmar.", "Reactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }
}