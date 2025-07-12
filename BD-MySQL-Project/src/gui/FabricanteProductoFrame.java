    package gui;

    import dao.FabricanteProductoDAO;
    import modelo.FabricanteProducto;
    import java.awt.*;
    import java.awt.event.MouseAdapter;
    import java.awt.event.MouseEvent;
    import java.util.List;
    import javax.swing.*;
    import javax.swing.table.DefaultTableModel;

    public class FabricanteProductoFrame extends JFrame {

        private JTextField txtCodigo;
        private JTextField txtNombre;
        private JTextField txtEstadoRegistro;
        private JTable tablaFabricantes;
        private DefaultTableModel tableModel;

        private JButton btnAdicionar, btnModificar, btnEliminar, btnInactivar, btnReactivar, btnActualizar, btnCancelar, btnSalir;

        private FabricanteProductoDAO fabricanteDAO;
        private int flagCarFlaAct = 0;
        private String operacionActual = "";
        private int codigoSeleccionado = 0;

        public FabricanteProductoFrame() {
            setTitle("Mantenimiento de Fabricantes de Productos");
            setSize(800, 600);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);

            fabricanteDAO = new FabricanteProductoDAO();
            initComponents();
            cargarTablaFabricantes();
            habilitarControles(false);
            habilitarBotonesIniciales();
        }

        /**
         * @param args
         */
        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> new FabricanteProductoFrame().setVisible(true));
        }

        private void initComponents() {
            setLayout(new BorderLayout());
            
            // Panel de registro
            JPanel panelRegistro = new JPanel(new GridBagLayout());
            panelRegistro.setBorder(BorderFactory.createTitledBorder("Registro de Fabricante de Producto"));
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
            panelRegistro.add(new JLabel("Estado Registro:"), gbc);
            gbc.gridx = 3;
            txtEstadoRegistro = new JTextField(5);
            txtEstadoRegistro.setEditable(false);
            panelRegistro.add(txtEstadoRegistro, gbc);

            // Segunda fila
            gbc.gridx = 0; gbc.gridy = 1;
            panelRegistro.add(new JLabel("Nombre:"), gbc);
            gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
            txtNombre = new JTextField(40);
            panelRegistro.add(txtNombre, gbc);

            // Tercera fila - Nota sobre estados
            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4; gbc.weightx = 0;
            JLabel lblEstados = new JLabel("* Estados: A = Activo, I = Inactivo, * = Eliminado");
            lblEstados.setFont(lblEstados.getFont().deriveFont(Font.ITALIC, 10f));
            lblEstados.setForeground(Color.GRAY);
            panelRegistro.add(lblEstados, gbc);

            add(panelRegistro, BorderLayout.NORTH);

            // Panel de tabla
            JPanel panelTabla = new JPanel(new BorderLayout());
            panelTabla.setBorder(BorderFactory.createTitledBorder("Fabricantes de Productos"));
            tableModel = new DefaultTableModel(new Object[]{"Código", "Nombre", "Estado"}, 0) {
                public boolean isCellEditable(int row, int col) { 
                    return false; 
                }
            };
            tablaFabricantes = new JTable(tableModel);
            panelTabla.add(new JScrollPane(tablaFabricantes), BorderLayout.CENTER);

            // Listener para selección en tabla
            tablaFabricantes.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (operacionActual.isEmpty() && e.getClickCount() == 1) {
                        int i = tablaFabricantes.getSelectedRow();
                        if (i != -1) {
                            txtCodigo.setText(tableModel.getValueAt(i, 0).toString());
                            txtNombre.setText(tableModel.getValueAt(i, 1).toString());
                            txtEstadoRegistro.setText(tableModel.getValueAt(i, 2).toString());
                            codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(i, 0).toString());
                            habilitarBotonesParaSeleccion();
                        }
                    }
                }
            });
            add(panelTabla, BorderLayout.CENTER);

            // Panel de botones
            JPanel panelBotones = new JPanel(new GridLayout(2, 4, 10, 10));
            btnAdicionar = new JButton("Adicionar");
            btnModificar = new JButton("Modificar");
            btnEliminar = new JButton("Eliminar");
            btnCancelar = new JButton("Cancelar");
            btnInactivar = new JButton("Inactivar");
            btnReactivar = new JButton("Reactivar");
            btnActualizar = new JButton("Actualizar");
            btnSalir = new JButton("Salir");

            for (JButton b : new JButton[]{btnAdicionar, btnModificar, btnEliminar, btnCancelar, 
                                        btnInactivar, btnReactivar, btnActualizar, btnSalir}) {
                panelBotones.add(b);
            }
            add(panelBotones, BorderLayout.SOUTH);

            // Listeners de botones
            btnAdicionar.addActionListener(e -> comandoAdicionar());
            btnActualizar.addActionListener(e -> comandoActualizar());
            btnCancelar.addActionListener(e -> comandoCancelar());
            btnSalir.addActionListener(e -> comandoSalir());
            btnModificar.addActionListener(e -> comandoModificar());
            btnEliminar.addActionListener(e -> comandoEliminar());
            btnInactivar.addActionListener(e -> comandoInactivar());
            btnReactivar.addActionListener(e -> comandoReactivar());
        }

        private void comandoAdicionar() {
            limpiarCampos();
            habilitarControles(true);
            txtEstadoRegistro.setText("A");
            operacionActual = "ADICIONAR";
            flagCarFlaAct = 1;
            habilitarBotonesParaOperacion();
            txtNombre.requestFocus();
        }

        private void comandoActualizar() {
            if (flagCarFlaAct == 0) {
                JOptionPane.showMessageDialog(this, "No hay ninguna operación pendiente de actualizar.", 
                                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nombre = txtNombre.getText().trim();
            String estadoStr = txtEstadoRegistro.getText().trim();

            if (nombre.isEmpty() || estadoStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar que el nombre no esté duplicado (excepto para modificaciones del mismo registro)
            if (operacionActual.equals("ADICIONAR") || operacionActual.equals("MODIFICAR")) {
                int excludeId = operacionActual.equals("MODIFICAR") ? codigoSeleccionado : 0;
                if (fabricanteDAO.existeNombreFabricante(nombre, excludeId)) {
                    JOptionPane.showMessageDialog(this, "Ya existe un fabricante con ese nombre.", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            FabricanteProducto fabricante = new FabricanteProducto();
            fabricante.setFabNom(nombre);
            fabricante.setFabEstReg(estadoStr.charAt(0));

            boolean exito = false;
            String mensaje = "";

            switch (operacionActual) {
                case "ADICIONAR":
                    String mensajeErrorAdi = fabricanteDAO.insertarFabricante(fabricante);
                    if (mensajeErrorAdi == null) {
                        exito = true;
                        mensaje = "Fabricante registrado con éxito.";
                    } else {
                        exito = false;
                        mensaje = mensajeErrorAdi;
                    }
                    break;
                    
                case "MODIFICAR":
                    fabricante.setFabCod(codigoSeleccionado);
                    String mensajeErrorMod = fabricanteDAO.actualizarFabricante(fabricante);
                    if (mensajeErrorMod == null) {
                        exito = true;
                        mensaje = "Fabricante modificado con éxito.";
                    } else {
                        exito = false;
                        mensaje = mensajeErrorMod;
                    }
                    break;
                    
                case "ELIMINAR":
                    // Verificar si tiene productos asociados antes de eliminar
                    if (fabricanteDAO.tieneProductosAsociados(codigoSeleccionado)) {
                        JOptionPane.showMessageDialog(this, 
                            "No se puede eliminar el fabricante porque tiene productos asociados.", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    exito = fabricanteDAO.eliminarLogicamenteFabricante(codigoSeleccionado);
                    mensaje = exito ? "Fabricante eliminado con éxito." : "Error al eliminar fabricante.";
                    break;
                    
                case "INACTIVAR":
                    exito = fabricanteDAO.inactivarFabricante(codigoSeleccionado);
                    mensaje = exito ? "Fabricante inactivado con éxito." : "Error al inactivar fabricante.";
                    break;
                    
                case "REACTIVAR":
                    exito = fabricanteDAO.reactivarFabricante(codigoSeleccionado);
                    mensaje = exito ? "Fabricante reactivado con éxito." : "Error al reactivar fabricante.";
                    break;
                    
                default:
                    JOptionPane.showMessageDialog(this, "Operación no reconocida.", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                    return;
            }

            if (exito) {
                JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarTablaFabricantes();
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

        private void comandoModificar() {
            int selectedRow = tablaFabricantes.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un registro para modificar.", 
                                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            habilitarControles(true);
            operacionActual = "MODIFICAR";
            flagCarFlaAct = 1;
            habilitarBotonesParaOperacion();
            txtNombre.requestFocus();
        }

        private void comandoEliminar() {
            int selectedRow = tablaFabricantes.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un registro para eliminar.", 
                                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Verificar si tiene productos asociados
            if (fabricanteDAO.tieneProductosAsociados(codigoSeleccionado)) {
                JOptionPane.showMessageDialog(this, 
                    "No se puede eliminar el fabricante porque tiene productos asociados.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtNombre.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtEstadoRegistro.setText("*");
            codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            habilitarControles(false);
            operacionActual = "ELIMINAR";
            flagCarFlaAct = 1;
            habilitarBotonesParaOperacion();
            JOptionPane.showMessageDialog(this, 
                "El registro se marcará como eliminado ('*').\nPresione 'Actualizar' para confirmar.", 
                "Eliminación Lógica", JOptionPane.INFORMATION_MESSAGE);
        }

        private void comandoInactivar() {
            int selectedRow = tablaFabricantes.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un registro para inactivar.", 
                                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtNombre.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtEstadoRegistro.setText("I");
            codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            habilitarControles(false);
            operacionActual = "INACTIVAR";
            flagCarFlaAct = 1;
            habilitarBotonesParaOperacion();
            JOptionPane.showMessageDialog(this, 
                "El registro se marcará como inactivo ('I').\nPresione 'Actualizar' para confirmar.", 
                "Inactivar Registro", JOptionPane.INFORMATION_MESSAGE);
        }

        private void comandoReactivar() {
            int selectedRow = tablaFabricantes.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un registro para reactivar.", 
                                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtNombre.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtEstadoRegistro.setText("A");
            codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            habilitarControles(false);
            operacionActual = "REACTIVAR";
            flagCarFlaAct = 1;
            habilitarBotonesParaOperacion();
            JOptionPane.showMessageDialog(this, 
                "El registro se marcará como activo ('A').\nPresione 'Actualizar' para confirmar.", 
                "Reactivar Registro", JOptionPane.INFORMATION_MESSAGE);
        }

        private void cargarTablaFabricantes() {
            tableModel.setRowCount(0);
            List<FabricanteProducto> lista = fabricanteDAO.obtenerTodosFabricantes();
            for (FabricanteProducto f : lista) {
                tableModel.addRow(new Object[]{
                    f.getFabCod(),
                    f.getFabNom(),
                    f.getFabEstReg()
                });
            }
        }

        private void habilitarControles(boolean b) {
            txtNombre.setEditable(b);
            // txtCodigo y txtEstadoRegistro siempre permanecen no editables
        }

        private void limpiarCampos() {
            txtCodigo.setText("");
            txtNombre.setText("");
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
            btnSalir.setEnabled(true);
        }

        private void habilitarBotonesParaOperacion() {
            btnAdicionar.setEnabled(false);
            btnActualizar.setEnabled(true);
            btnCancelar.setEnabled(true);
            btnModificar.setEnabled(false);
            btnEliminar.setEnabled(false);
            btnInactivar.setEnabled(false);
            btnReactivar.setEnabled(false);
            btnSalir.setEnabled(true);
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
    }