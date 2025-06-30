package gui;

import dao.RepVentaDAO;
import dao.UsuarioSistemaDAO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.RepVenta;
import modelo.UsuarioSistema;

public class UsuarioSistemaFrame extends JFrame {

    private JTextField txtCodigo;
    private JComboBox<ComboItem> cbRepresentante;
    private JTextField txtNombreUsuario;
    private JPasswordField txtContrasena;
    private JTextField txtEstadoRegistro;
    private JTable tablaUsuarios;
    private DefaultTableModel tableModel;

    private JButton btnAdicionar, btnModificar, btnEliminar, btnInactivar, btnReactivar, btnActualizar, btnCancelar, btnSalir;

    private UsuarioSistemaDAO usuarioSistemaDAO;
    private RepVentaDAO repVentaDAO;
    private int flagCarFlaAct = 0;
    private String operacionActual = "";
    private int codigoSeleccionado = 0;

    // Clase auxiliar para manejar items del ComboBox
    private static class ComboItem {
        private int codigo;
        private String descripcion;
        
        public ComboItem(int codigo, String descripcion) {
            this.codigo = codigo;
            this.descripcion = descripcion;
        }
        
        public int getCodigo() {
            return codigo;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
        
        @Override
        public String toString() {
            return codigo + " - " + descripcion;
        }
    }

    public UsuarioSistemaFrame() {
        setTitle("Mantenimiento de Usuarios del Sistema");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        usuarioSistemaDAO = new UsuarioSistemaDAO();
        repVentaDAO = new RepVentaDAO();
        initComponents();
        cargarComboBoxes();
        cargarTablaUsuarios();
        habilitarControles(false);
        habilitarBotonesIniciales();
    }
    
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new UsuarioSistemaFrame().setVisible(true));
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel panelRegistro = new JPanel(new GridBagLayout());
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registro de Usuario del Sistema"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Primera fila
        gbc.gridx = 0; gbc.gridy = 0;
        panelRegistro.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        txtCodigo = new JTextField(10);
        txtCodigo.setEditable(false); // El código es AUTO_INCREMENT
        panelRegistro.add(txtCodigo, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        panelRegistro.add(new JLabel("Representante:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        cbRepresentante = new JComboBox<>();
        cbRepresentante.setPreferredSize(new Dimension(250, cbRepresentante.getPreferredSize().height));
        panelRegistro.add(cbRepresentante, gbc);

        // Segunda fila
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Nombre de Usuario:"), gbc);
        gbc.gridx = 1;
        txtNombreUsuario = new JTextField(20);
        panelRegistro.add(txtNombreUsuario, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        panelRegistro.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtContrasena = new JPasswordField(20);
        panelRegistro.add(txtContrasena, gbc);

        gbc.gridx = 3; gbc.gridy = 2; gbc.gridwidth = 1;
        panelRegistro.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 3; gbc.gridy = 3;
        txtEstadoRegistro = new JTextField(5);
        txtEstadoRegistro.setEditable(false);
        panelRegistro.add(txtEstadoRegistro, gbc);

        // Cuarta fila - Nota sobre seguridad
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4;
        JLabel lblNota = new JLabel("* El nombre de usuario debe ser único en el sistema");
        lblNota.setFont(lblNota.getFont().deriveFont(Font.ITALIC, 10f));
        lblNota.setForeground(Color.GRAY);
        panelRegistro.add(lblNota, gbc);

        add(panelRegistro, BorderLayout.NORTH);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Usuarios del Sistema"));
        tableModel = new DefaultTableModel(new Object[]{"Código", "Rep. Código", "Usuario", "Contraseña", "Estado", "Nombre Representante"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaUsuarios = new JTable(tableModel);
        panelTabla.add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);

        tablaUsuarios.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (operacionActual.isEmpty() && e.getClickCount() == 1) {
                    int i = tablaUsuarios.getSelectedRow();
                    if (i != -1) {
                        txtCodigo.setText(tableModel.getValueAt(i, 0).toString());
                        
                        // Seleccionar representante por código
                        int codigoRep = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
                        seleccionarEnComboBoxPorCodigo(cbRepresentante, codigoRep);
                        
                        txtNombreUsuario.setText(tableModel.getValueAt(i, 2).toString());
                        txtContrasena.setText(tableModel.getValueAt(i, 3).toString());
                        
                        txtEstadoRegistro.setText(tableModel.getValueAt(i, 5).toString());
                        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(i, 0).toString());
                        habilitarBotonesParaSeleccion();
                    }
                }
            }
        });
        add(panelTabla, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new GridLayout(2, 4, 10, 10));
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
        btnActualizar.addActionListener(e -> comandoActualizar());
        btnCancelar.addActionListener(e -> comandoCancelar());
        btnSalir.addActionListener(e -> comandoSalir());
        btnModificar.addActionListener(e -> comandoModificar());
        btnEliminar.addActionListener(e -> comandoEliminar());
        btnInactivar.addActionListener(e -> comandoInactivar());
        btnReactivar.addActionListener(e -> comandoReactivar());
    }

    private void cargarComboBoxes() {
        // Cargar representantes activos
        List<RepVenta> representantes = repVentaDAO.obtenerRepresentantesActivos();
        cbRepresentante.removeAllItems();
        cbRepresentante.addItem(new ComboItem(0, "-- Seleccionar Representante --"));
        for (RepVenta rep : representantes) {
            cbRepresentante.addItem(new ComboItem(rep.getRepCod(), rep.getRepNom()));
        }

    }

    private void seleccionarEnComboBoxPorCodigo(JComboBox<ComboItem> comboBox, int codigo) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            ComboItem item = comboBox.getItemAt(i);
            if (item.getCodigo() == codigo) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
        comboBox.setSelectedIndex(0); // Si no encuentra, selecciona el primer item
    }

    private void comandoAdicionar() {
        limpiarCampos();
        habilitarControles(true);
        txtEstadoRegistro.setText("A");
        cbRepresentante.setSelectedIndex(0);
        operacionActual = "ADICIONAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
    }

    private void comandoActualizar() {
        if (flagCarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay ninguna operación pendiente de actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombreUsuario = txtNombreUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword()).trim();
        String estadoStr = txtEstadoRegistro.getText().trim();

        if (nombreUsuario.isEmpty() || contrasena.isEmpty() || estadoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar selección de representante
        ComboItem representanteSeleccionado = (ComboItem) cbRepresentante.getSelectedItem();
        if (representanteSeleccionado == null || representanteSeleccionado.getCodigo() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un representante.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        int repCodigo = representanteSeleccionado.getCodigo();

        UsuarioSistema usuario = new UsuarioSistema();
        usuario.setRepCod(repCodigo);
        usuario.setUsuNom(nombreUsuario);
        usuario.setUsuContr(contrasena);
        usuario.setUsuEstReg(estadoStr.charAt(0));

        boolean exito = false;
        String mensaje = "";

        switch (operacionActual) {
            case "ADICIONAR":
                exito = usuarioSistemaDAO.insertarUsuario(usuario);
                mensaje = exito ? "Usuario registrado con éxito." : "Error al registrar usuario. Verifique que el nombre de usuario no exista.";
                break;
            case "MODIFICAR":
                usuario.setUsuCod(codigoSeleccionado);
                exito = usuarioSistemaDAO.actualizarUsuario(usuario);
                mensaje = exito ? "Usuario modificado con éxito." : "Error al modificar usuario.";
                break;
            case "ELIMINAR":
                exito = usuarioSistemaDAO.eliminarLogicamenteUsuario(codigoSeleccionado);
                mensaje = exito ? "Usuario eliminado con éxito." : "Error al eliminar usuario.";
                break;
            case "INACTIVAR":
                exito = usuarioSistemaDAO.inactivarUsuario(codigoSeleccionado);
                mensaje = exito ? "Usuario inactivado con éxito." : "Error al inactivar usuario.";
                break;
            case "REACTIVAR":
                exito = usuarioSistemaDAO.reactivarUsuario(codigoSeleccionado);
                mensaje = exito ? "Usuario reactivado con éxito." : "Error al reactivar usuario.";
                break;
            default:
                JOptionPane.showMessageDialog(this, "Operación no reconocida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }

        if (exito) {
            JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTablaUsuarios();
            comandoCancelar();
        } else {
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void comandoCancelar() {
        limpiarCampos();
        habilitarControles(false);
        operacionActual = "";
        flagCarFlaAct = 0;
        codigoSeleccionado = 0;
        habilitarBotonesIniciales();
    }

    private void comandoSalir() {
        dispose();
    }

    private void cargarTablaUsuarios() {
        tableModel.setRowCount(0);
        List<UsuarioSistema> lista = usuarioSistemaDAO.obtenerTodosUsuarios();
        for (UsuarioSistema u : lista) {
            // Obtener descripciones para mostrar en la tabla
            String nombreRep = "";
            
            // Buscar nombre del representante
            RepVenta rep = repVentaDAO.obtenerRepresentantePorCodigo(u.getRepCod());
            if (rep != null) {
                nombreRep = rep.getRepNom();
            }
            
            tableModel.addRow(new Object[]{
                u.getUsuCod(), 
                u.getRepCod(), 
                u.getUsuNom(), 
                u.getUsuContr(), 
                u.getUsuEstReg(),
                nombreRep
            });
        }
    }

    private void habilitarControles(boolean b) {
        cbRepresentante.setEnabled(b);
        txtNombreUsuario.setEditable(b);
        txtContrasena.setEditable(b);
        // txtCodigo y txtEstadoRegistro siempre permanecen no editables
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        cbRepresentante.setSelectedIndex(0);
        txtNombreUsuario.setText("");
        txtContrasena.setText("");
        txtEstadoRegistro.setText("");
    }

    private void habilitarBotonesIniciales() {
        btnAdicionar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnInactivar.setEnabled(false);
        btnReactivar.setEnabled(false);
    }

    private void habilitarBotonesParaOperacion() {
        btnAdicionar.setEnabled(false);
        btnActualizar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnInactivar.setEnabled(false);
        btnReactivar.setEnabled(false);
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
        int selectedRow = tablaUsuarios.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        habilitarControles(true);
        operacionActual = "MODIFICAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
    }

    private void comandoEliminar() {
        int selectedRow = tablaUsuarios.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        cargarDatosSeleccionados(selectedRow);
        txtEstadoRegistro.setText("*");
        habilitarControles(false);
        operacionActual = "ELIMINAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como eliminado ('*').\nPresione 'Actualizar' para confirmar.", "Eliminación Lógica", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoInactivar() {
        int selectedRow = tablaUsuarios.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para inactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        cargarDatosSeleccionados(selectedRow);
        txtEstadoRegistro.setText("I");
        habilitarControles(false);
        operacionActual = "INACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como inactivo ('I').\nPresione 'Actualizar' para confirmar.", "Inactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoReactivar() {
        int selectedRow = tablaUsuarios.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para reactivar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        cargarDatosSeleccionados(selectedRow);
        txtEstadoRegistro.setText("A");
        habilitarControles(false);
        operacionActual = "REACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como activo ('A').\nPresione 'Actualizar' para confirmar.", "Reactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cargarDatosSeleccionados(int selectedRow) {
        txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
        
        // Seleccionar representante por código
        int codigoRep = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString());
        seleccionarEnComboBoxPorCodigo(cbRepresentante, codigoRep);
        
        txtNombreUsuario.setText(tableModel.getValueAt(selectedRow, 2).toString());
        txtContrasena.setText(tableModel.getValueAt(selectedRow, 3).toString());
        
        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
    }
}