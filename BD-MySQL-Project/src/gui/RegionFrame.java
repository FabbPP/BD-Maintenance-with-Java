package gui;

import dao.DepartamentoDAO;
import dao.RegionDAO;
import modelo.Departamento;
import modelo.Region;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;

public class RegionFrame extends JFrame{

    private JTextField txtCodigo;
    private JComboBox<String> cmbDepartamento;
    private JTextField txtNombre;
    private JTextField txtEstadoRegistro;
    private JTable tablaRegiones;
    private DefaultTableModel tableModel;

    private JButton btnAdicionar, btnModificar, btnEliminar, btnInactivar, btnReactivar, btnActualizar, btnCancelar, btnSalir;

    private RegionDAO regionDAO;
    private DepartamentoDAO departamentoDAO;
    private int flagCarFlaAct = 0;
    private String operacionActual = "";

    private List<Departamento> listaDepartamentos;

    public RegionFrame() {
        setTitle("Mantenimiento de Región");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        regionDAO = new RegionDAO();
        departamentoDAO = new DepartamentoDAO();
        initComponents();
        cargarDepartamentosEnComboBox();
        cargarTablaRegiones();
        habilitarControles(false);
        habilitarBotonesIniciales();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegionFrame().setVisible(true));
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel panelRegistro = new JPanel(new GridBagLayout());
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registro de Región"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelRegistro.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtCodigo = new JTextField(10);
        txtCodigo.setEditable(false);
        panelRegistro.add(txtCodigo, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Departamento:"), gbc);
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 1.0;
        cmbDepartamento = new JComboBox<>();
        panelRegistro.add(cmbDepartamento, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        txtNombre = new JTextField(50);
        panelRegistro.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Estado Registro:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
        txtEstadoRegistro = new JTextField(1);
        txtEstadoRegistro.setEditable(false);
        panelRegistro.add(txtEstadoRegistro, gbc);

        add(panelRegistro, BorderLayout.NORTH);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Regiones"));
        tableModel = new DefaultTableModel(new Object[]{"Código Reg.", "Cód. Depto.", "Nombre Reg.", "Estado"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaRegiones = new JTable(tableModel);
        panelTabla.add(new JScrollPane(tablaRegiones), BorderLayout.CENTER);

        tablaRegiones.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (operacionActual.isEmpty() && e.getClickCount() == 1) {
                    int i = tablaRegiones.getSelectedRow();
                    if (i != -1) {
                        txtCodigo.setText(tableModel.getValueAt(i, 0).toString());
                        // Mostrar el nombre del departamento en el ComboBox
                        int depCod = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
                        String depNom = getDepartamentoNombre(depCod);
                        cmbDepartamento.setSelectedItem(depNom);

                        txtNombre.setText(tableModel.getValueAt(i, 2).toString());
                        txtEstadoRegistro.setText(tableModel.getValueAt(i, 3).toString());
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

    private void cargarDepartamentosEnComboBox() {
        listaDepartamentos = departamentoDAO.obtenerTodosDepartamentos();
        cmbDepartamento.removeAllItems();
        for (Departamento dep : listaDepartamentos) {
            cmbDepartamento.addItem(dep.getDepNom() + " (ID: " + dep.getDepCod() + ")");
        }
    }

    private int getDepartamentoCodigo(String nombreConId) {
        if (nombreConId == null || nombreConId.isEmpty()) return -1;
        try {
            int startIndex = nombreConId.indexOf("(ID: ") + 5;
            int endIndex = nombreConId.indexOf(")", startIndex);
            return Integer.parseInt(nombreConId.substring(startIndex, endIndex));
        } catch (Exception e) {
            System.err.println("Error al parsear ID de departamento: " + e.getMessage());
            return -1;
        }
    }

    private String getDepartamentoNombre(int codigo) {
        for (Departamento dep : listaDepartamentos) {
            if (dep.getDepCod() == codigo) {
                return dep.getDepNom() + " (ID: " + dep.getDepCod() + ")";
            }
        }
        return "";
    }

    private void comandoAdicionar() {
        limpiarCampos();
        habilitarControles(true);
        txtEstadoRegistro.setText("A");
        operacionActual = "ADICIONAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        cmbDepartamento.requestFocusInWindow();
    }

    private void comandoActualizar() {
        if (flagCarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay ninguna operación pendiente de actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombreRegion = txtNombre.getText().trim();
        String estadoRegistro = txtEstadoRegistro.getText().trim();
        int depCod = getDepartamentoCodigo(cmbDepartamento.getSelectedItem().toString());

        if (depCod == -1 || nombreRegion.isEmpty() || estadoRegistro.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios. Asegúrese de seleccionar un departamento válido.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!estadoRegistro.matches("A|I|\\*")) {
             JOptionPane.showMessageDialog(this, "El Estado de Registro solo puede ser 'A', 'I' o '*'.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
             return;
        }

        Region region = new Region();
        region.setDepCod(depCod);
        region.setRegNom(nombreRegion);
        region.setRegEstReg(estadoRegistro.charAt(0));

        boolean exito = false;
        String mensaje = "";

        switch (operacionActual) {
            case "ADICIONAR":
                exito = regionDAO.insertarRegion(region);
                mensaje = exito ? "Región adicionada con éxito." : "Error al adicionar región.";
                break;
            case "MODIFICAR":
                region.setRegCod(Integer.parseInt(txtCodigo.getText()));
                exito = regionDAO.actualizarRegion(region);
                mensaje = exito ? "Región modificada con éxito." : "Error al modificar región.";
                break;
            case "ELIMINAR":
                int confirmDelete = JOptionPane.showConfirmDialog(this, "¿Está seguro de marcar este registro como ELIMINADO LÓGICAMENTE ('*')?", "Confirmar Eliminación Lógica", JOptionPane.YES_NO_OPTION);
                if (confirmDelete == JOptionPane.YES_OPTION) {
                    exito = regionDAO.eliminarLogicamenteRegion(Integer.parseInt(txtCodigo.getText()));
                    mensaje = exito ? "Región eliminada lógicamente con éxito." : "Error al eliminar lógicamente región.";
                } else {
                    mensaje = "Operación de eliminación cancelada.";
                    exito = true;
                }
                break;
            case "INACTIVAR":
                exito = regionDAO.inactivarRegion(Integer.parseInt(txtCodigo.getText()));
                mensaje = exito ? "Región inactivada con éxito." : "Error al inactivar región.";
                break;
            case "REACTIVAR":
                exito = regionDAO.reactivarRegion(Integer.parseInt(txtCodigo.getText()));
                mensaje = exito ? "Región reactivada con éxito." : "Error al reactivar región.";
                break;
            default:
                JOptionPane.showMessageDialog(this, "Operación no reconocida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }

        if (exito) {
            JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            habilitarControles(false);
            cargarTablaRegiones();
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
        tablaRegiones.clearSelection();
    }

    private void comandoSalir() {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea salir del mantenimiento de Regiones?", "Confirmar Salida", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    private void cargarTablaRegiones() {
        tableModel.setRowCount(0);
        List<Region> regiones = regionDAO.obtenerTodasRegiones();
        for (Region reg : regiones) {
            tableModel.addRow(new Object[]{
                reg.getRegCod(),
                reg.getDepCod(),
                reg.getRegNom(),
                reg.getRegEstReg()
            });
        }
    }

    private void habilitarControles(boolean b) {
        txtCodigo.setEditable(false); 
        cmbDepartamento.setEnabled(b);
        txtNombre.setEditable(b);
        txtEstadoRegistro.setEditable(false);
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        cmbDepartamento.setSelectedIndex(0);
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
        btnReactivar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnSalir.setEnabled(true);
    }

    private void comandoModificar() {
        int selectedRow = tablaRegiones.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        habilitarControles(true);
        txtCodigo.setEditable(false);
        cmbDepartamento.setEnabled(true);
        txtEstadoRegistro.setEditable(false);
        operacionActual = "MODIFICAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        txtNombre.requestFocusInWindow();
    }

    private void comandoEliminar() {
        int selectedRow = tablaRegiones.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        cmbDepartamento.setSelectedItem(getDepartamentoNombre((int)tableModel.getValueAt(selectedRow, 1)));
        txtNombre.setText(tableModel.getValueAt(selectedRow, 2).toString());
        txtEstadoRegistro.setText("*");
        habilitarControles(false);
        operacionActual = "ELIMINAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará para eliminación lógica ('*').\nPresione 'Actualizar' para confirmar.", "Eliminación Lógica", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoInactivar() {
        int selectedRow = tablaRegiones.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para inactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        cmbDepartamento.setSelectedItem(getDepartamentoNombre((int)tableModel.getValueAt(selectedRow, 1)));
        txtNombre.setText(tableModel.getValueAt(selectedRow, 2).toString());
        txtEstadoRegistro.setText("I");
        habilitarControles(false);
        operacionActual = "INACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como inactivo ('I').\nPresione 'Actualizar' para confirmar.", "Inactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoReactivar() {
        int selectedRow = tablaRegiones.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla para reactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        cmbDepartamento.setSelectedItem(getDepartamentoNombre((int)tableModel.getValueAt(selectedRow, 1)));
        txtNombre.setText(tableModel.getValueAt(selectedRow, 2).toString());
        txtEstadoRegistro.setText("A");
        habilitarControles(false);
        operacionActual = "REACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como activo ('A').\nPresione 'Actualizar' para confirmar.", "Reactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }
}