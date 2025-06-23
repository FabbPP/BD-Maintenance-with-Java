package gui;

import dao.EstadoSistemaDAO;
import modelo.EstadoSistema;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class EstadoSistemaFrame extends JFrame {

    private JTextField txtCodigo;
    private JTextField txtDescripcion;
    private JTextField txtEstadoRegistro;
    private JTable tablaEstados;
    private DefaultTableModel tableModel;

    private JButton btnAdicionar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnInactivar;
    private JButton btnReactivar;
    private JButton btnActualizar;
    private JButton btnCancelar;
    private JButton btnSalir;

    private EstadoSistemaDAO estadoSistemaDAO;
    private int flagCarFlaAct = 0;
    private String operacionActual = "";

    public EstadoSistemaFrame() {
        setTitle("Mantenimiento de Estado de Sistema");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        estadoSistemaDAO = new EstadoSistemaDAO();
        initComponents();
        cargarTablaEstados();
        habilitarControles(false);
        habilitarBotonesIniciales();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EstadoSistemaFrame().setVisible(true));
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panelRegistro = new JPanel(new GridBagLayout());
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registro de Estado de Sistema"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelRegistro.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtCodigo = new JTextField(15);
        panelRegistro.add(txtCodigo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        txtDescripcion = new JTextField(30);
        panelRegistro.add(txtDescripcion, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Estado Registro:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        txtEstadoRegistro = new JTextField(5);
        txtEstadoRegistro.setEditable(false);
        panelRegistro.add(txtEstadoRegistro, gbc);

        add(panelRegistro, BorderLayout.NORTH);


        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Estados del Sistema"));
        tableModel = new DefaultTableModel(new Object[]{"Código", "Descripción", "Estado Registro"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaEstados = new JTable(tableModel);
        panelTabla.add(new JScrollPane(tablaEstados), BorderLayout.CENTER);

        tablaEstados.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (operacionActual.isEmpty() && e.getClickCount() == 1) {
                    int selectedRow = tablaEstados.getSelectedRow();
                    if (selectedRow != -1) {
                        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
                        txtDescripcion.setText(tableModel.getValueAt(selectedRow, 1).toString());
                        txtEstadoRegistro.setText(tableModel.getValueAt(selectedRow, 2).toString());
                        habilitarBotonesParaSeleccion();
                    }
                }
            }
        });
        add(panelTabla, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new GridLayout(2, 4, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnAdicionar = new JButton("Adicionar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnCancelar = new JButton("Cancelar");
        btnInactivar = new JButton("Inactivar"); // "Inactivar" aquí es 'S'
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
        txtCodigo.setEditable(true);
        operacionActual = "ADICIONAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        txtCodigo.requestFocusInWindow();
    }

    private void comandoActualizar() {
        if (flagCarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay ninguna operación pendiente de actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String codigoStr = txtCodigo.getText().trim();
        char codigo = codigoStr.charAt(0);
        String descripcion = txtDescripcion.getText().trim();
        String estadoRegistroStr = txtEstadoRegistro.getText().trim();
        char estadoRegistro = estadoRegistroStr.charAt(0);

        if (codigo.isEmpty() || descripcion.isEmpty() || estadoRegistro.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos (Código, Descripción, Estado Registro) son obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (operacionActual.equals("ADICIONAR") || operacionActual.equals("MODIFICAR")) {
            if (!codigo.matches("A|S|B")) {
                JOptionPane.showMessageDialog(this, "El Código solo puede ser 'A', 'S' o 'B'.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (!estadoRegistro.matches("A|S|B|\\*")) {
             JOptionPane.showMessageDialog(this, "El Estado de Registro solo puede ser 'A', 'S', 'B' o '*'.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
             return;
        }


        EstadoSistema estado = new EstadoSistema(codigo, descripcion, estadoRegistro);
        boolean exito = false;
        String mensaje = "";

        switch (operacionActual) {
            case "ADICIONAR":
                exito = estadoSistemaDAO.insertarEstado(estado);
                mensaje = exito ? "Estado del sistema adicionado con éxito." : "Error al adicionar estado del sistema. Revise el código o restricciones.";
                break;
            case "MODIFICAR":
            case "ELIMINAR":
            case "INACTIVAR":
            case "REACTIVAR":
                exito = estadoSistemaDAO.actualizarEstado(estado);
                mensaje = exito ? "Estado del sistema actualizado con éxito." : "Error al actualizar estado del sistema.";
                break;
            default:
                JOptionPane.showMessageDialog(this, "Operación no reconocida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }

        if (exito) {
            JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            habilitarControles(false);
            cargarTablaEstados();
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
        tablaEstados.clearSelection();
    }

    private void comandoModificar() {
        int selectedRow = tablaEstados.getSelectedRow();
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
        txtDescripcion.requestFocusInWindow();
    }

    private void comandoEliminar() {
        int selectedRow = tablaEstados.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtDescripcion.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtEstadoRegistro.setText("*");
        habilitarControles(false);
        operacionActual = "ELIMINAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará para eliminación lógica ('*').\nPresione 'Actualizar' para confirmar.", "Eliminación Lógica", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoInactivar() {
        int selectedRow = tablaEstados.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para inactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtDescripcion.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtEstadoRegistro.setText("S");
        habilitarControles(false);
        operacionActual = "INACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como inactivo.\nPresione 'Actualizar' para confirmar.", "Inactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoReactivar() {
        int selectedRow = tablaEstados.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para reactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtDescripcion.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtEstadoRegistro.setText("A");
        habilitarControles(false);
        operacionActual = "REACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como activo ('A').\nPresione 'Actualizar' para confirmar.", "Reactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cargarTablaEstados() {
        tableModel.setRowCount(0);
        List<EstadoSistema> estados = estadoSistemaDAO.obtenerTodosEstados();
        for (EstadoSistema es : estados) {
            tableModel.addRow(new Object[]{es.getUsuEstSistCod(), es.getUsuEstSistDesc(), es.getUsuEstSistEstReg()});
        }
    }

    private void habilitarControles(boolean habilitar) {
        txtCodigo.setEditable(habilitar);
        txtDescripcion.setEditable(habilitar);
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtDescripcion.setText("");
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
        btnReactivar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnSalir.setEnabled(true);
    }
    

    private void comandoSalir() {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea salir del mantenimiento de Estados del Sistema?", "Confirmar Salida", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
}