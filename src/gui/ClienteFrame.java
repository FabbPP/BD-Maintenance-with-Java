package gui;

import dao.CategoriaClienteDAO;
import dao.CiudadDAO;
import dao.ClienteDAO;
import dao.RepVentaDAO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.CategoriaCliente;
import modelo.Ciudad;
import modelo.Cliente;
import modelo.RepVenta;

public class ClienteFrame extends JFrame {

    private JTextField txtCodigo;
    private JTextField txtEmpresa;
    private JComboBox<ComboItem> cbRepresentante;
    private JTextField txtNombre;
    private JTextField txtApellidoPaterno;
    private JTextField txtApellidoMaterno;
    private JComboBox<ComboItem> cbCiudad;
    private JTextField txtDireccion;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JComboBox<ComboItem> cbCategoria;
    private JTextField txtEstadoRegistro;
    private JTable tablaClientes;
    private DefaultTableModel tableModel;

    private JButton btnAdicionar, btnModificar, btnEliminar, btnInactivar, btnReactivar, btnActualizar, btnCancelar, btnSalir;
    private JButton btnBuscarNombre, btnBuscarRepresentante, btnMostrarActivos, btnMostrarTodos;

    private ClienteDAO clienteDAO;
    private RepVentaDAO representanteDAO;
    private CategoriaClienteDAO categoriaDAO;
    private CiudadDAO ciudadDAO;
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

    public ClienteFrame() {
        setTitle("Mantenimiento de Clientes");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        clienteDAO = new ClienteDAO();
        representanteDAO = new RepVentaDAO();
        categoriaDAO = new CategoriaClienteDAO();
        ciudadDAO = new CiudadDAO();
        initComponents();
        cargarComboBoxes();
        cargarTablaClientes();
        habilitarControles(false);
        habilitarBotonesIniciales();
    }
    
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new ClienteFrame().setVisible(true));
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Panel principal de registro
        JPanel panelRegistro = new JPanel(new GridBagLayout());
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registro de Cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Primera fila - Código y Empresa
        gbc.gridx = 0; gbc.gridy = 0;
        panelRegistro.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        txtCodigo = new JTextField(10);
        txtCodigo.setEditable(false);
        panelRegistro.add(txtCodigo, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        panelRegistro.add(new JLabel("Empresa:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtEmpresa = new JTextField(20);
        panelRegistro.add(txtEmpresa, gbc);

        // Segunda fila - RepVenta y Límite
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelRegistro.add(new JLabel("RepVenta:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cbRepresentante = new JComboBox<>();
        cbRepresentante.setPreferredSize(new Dimension(200, cbRepresentante.getPreferredSize().height));
        panelRegistro.add(cbRepresentante, gbc);

        // Tercera fila - Nombre y Apellido Paterno
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNombre = new JTextField(20);
        panelRegistro.add(txtNombre, gbc);

        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Apellido Paterno:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtApellidoPaterno = new JTextField(20);
        panelRegistro.add(txtApellidoPaterno, gbc);

        // Cuarta fila - Apellido Materno y Ciudad
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Apellido Materno:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtApellidoMaterno = new JTextField(20);
        panelRegistro.add(txtApellidoMaterno, gbc);

        gbc.gridx = 2; gbc.gridy = 3; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Ciudad:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        cbCiudad = new JComboBox<>();
        cbCiudad.setPreferredSize(new Dimension(200, cbCiudad.getPreferredSize().height));
        panelRegistro.add(cbCiudad, gbc);

        // Quinta fila - Dirección
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        txtDireccion = new JTextField();
        panelRegistro.add(txtDireccion, gbc);

        // Sexta fila - Teléfono y Correo
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtTelefono = new JTextField(15);
        panelRegistro.add(txtTelefono, gbc);

        gbc.gridx = 2; gbc.gridy = 5; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Correo:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtCorreo = new JTextField(25);
        panelRegistro.add(txtCorreo, gbc);

        // Séptima fila - Categoría y Estado
        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cbCategoria = new JComboBox<>();
        cbCategoria.setPreferredSize(new Dimension(200, cbCategoria.getPreferredSize().height));
        panelRegistro.add(cbCategoria, gbc);

        gbc.gridx = 2; gbc.gridy = 6; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0;
        txtEstadoRegistro = new JTextField(5);
        txtEstadoRegistro.setEditable(false);
        panelRegistro.add(txtEstadoRegistro, gbc);

        // Octava fila - Notas
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 4;
        JLabel lblNotas = new JLabel("* Teléfono debe tener al menos 9 dígitos | Estado: A=Activo, I=Inactivo, *=Eliminado");
        lblNotas.setFont(lblNotas.getFont().deriveFont(Font.ITALIC, 10f));
        lblNotas.setForeground(Color.GRAY);
        panelRegistro.add(lblNotas, gbc);

        add(panelRegistro, BorderLayout.NORTH);

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("Búsqueda y Filtros"));
        
        btnBuscarNombre = new JButton("Buscar por Nombre");
        btnBuscarRepresentante = new JButton("Buscar por RepVenta");
        btnMostrarActivos = new JButton("Mostrar Activos");
        btnMostrarTodos = new JButton("Mostrar Todos");
        
        panelBusqueda.add(btnBuscarNombre);
        panelBusqueda.add(btnBuscarRepresentante);
        panelBusqueda.add(btnMostrarActivos);
        panelBusqueda.add(btnMostrarTodos);

        // Panel de tabla
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Clientes"));
        panelTabla.add(panelBusqueda, BorderLayout.NORTH);
        
        tableModel = new DefaultTableModel(new Object[]{
            "Código", "Empresa", "RepVenta", "Nombre", "Ap. Paterno", 
            "Ap. Materno", "Ciudad", "Teléfono", "Correo", "Categoría", "Estado",
            "Nombre Rep.", "Desc. Categoría", "Nombre Ciudad"
        }, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablaClientes = new JTable(tableModel);
        tablaClientes.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // Configurar anchos de columnas
        int[] anchos = {60, 120, 80, 80, 100, 100, 100, 80, 100, 150, 80, 60, 150, 150, 100};
        for (int i = 0; i < anchos.length && i < tablaClientes.getColumnCount(); i++) {
            tablaClientes.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
        
        JScrollPane scrollPane = new JScrollPane(tablaClientes);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panelTabla.add(scrollPane, BorderLayout.CENTER);

        tablaClientes.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (operacionActual.isEmpty() && e.getClickCount() == 1) {
                    int i = tablaClientes.getSelectedRow();
                    if (i != -1) {
                        cargarDatosDesdeTabla(i);
                        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(i, 0).toString());
                        habilitarBotonesParaSeleccion();
                    }
                }
            }
        });
        add(panelTabla, BorderLayout.CENTER);

        // Panel de botones
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

        for (JButton b : new JButton[]{btnAdicionar, btnModificar, btnEliminar, btnCancelar, 
                                       btnInactivar, btnReactivar, btnActualizar, btnSalir}) {
            panelBotones.add(b);
        }
        add(panelBotones, BorderLayout.SOUTH);

        // Event listeners
        btnAdicionar.addActionListener(e -> comandoAdicionar());
        btnActualizar.addActionListener(e -> comandoActualizar());
        btnCancelar.addActionListener(e -> comandoCancelar());
        btnSalir.addActionListener(e -> comandoSalir());
        btnModificar.addActionListener(e -> comandoModificar());
        btnEliminar.addActionListener(e -> comandoEliminar());
        btnInactivar.addActionListener(e -> comandoInactivar());
        btnReactivar.addActionListener(e -> comandoReactivar());
        
        btnBuscarNombre.addActionListener(e -> comandoBuscarNombre());
        btnBuscarRepresentante.addActionListener(e -> comandoBuscarRepresentante());
        btnMostrarActivos.addActionListener(e -> comandoMostrarActivos());
        btnMostrarTodos.addActionListener(e -> comandoMostrarTodos());
    }

    private void cargarComboBoxes() {
        // Cargar representantes
        try {
            List<RepVenta> representantes = representanteDAO.obtenerTodosRepresentantes();
            cbRepresentante.removeAllItems();
            cbRepresentante.addItem(new ComboItem(0, "-- Sin RepVenta --"));
            for (RepVenta rep : representantes) {
                cbRepresentante.addItem(new ComboItem(rep.getRepCod(), rep.getRepNom()));
            }
        } catch (Exception e) {
            System.err.println("Error al cargar representantes: " + e.getMessage());
        }

        // Cargar categorías
        try {
            List<CategoriaCliente> categorias = categoriaDAO.obtenerTodasCategorias();
            cbCategoria.removeAllItems();
            cbCategoria.addItem(new ComboItem(0, "-- Seleccionar Categoría --"));
            for (CategoriaCliente cat : categorias) {
                cbCategoria.addItem(new ComboItem(cat.getCatCliCod(), cat.getCatCliDesc()));
            }
        } catch (Exception e) {
            System.err.println("Error al cargar categorías: " + e.getMessage());
        }

        // Cargar ciudades
        try {
            List<Ciudad> ciudades = ciudadDAO.obtenerTodasCiudades();
            cbCiudad.removeAllItems();
            cbCiudad.addItem(new ComboItem(0, "-- Sin Ciudad --"));
            for (Ciudad ciudad : ciudades) {
                cbCiudad.addItem(new ComboItem(ciudad.getCiuCod(), ciudad.getCiuNom()));
            }
        } catch (Exception e) {
            System.err.println("Error al cargar ciudades: " + e.getMessage());
        }
    }

    private void seleccionarEnComboBoxPorCodigo(JComboBox<ComboItem> comboBox, Integer codigo) {
        if (codigo == null) {
            comboBox.setSelectedIndex(0);
            return;
        }
        
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            ComboItem item = comboBox.getItemAt(i);
            if (item.getCodigo() == codigo) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
        comboBox.setSelectedIndex(0);
    }

    private void comandoAdicionar() {
        limpiarCampos();
        habilitarControles(true);
        txtEstadoRegistro.setText("A");
        cbRepresentante.setSelectedIndex(0);
        cbCategoria.setSelectedIndex(0);
        cbCiudad.setSelectedIndex(0);
        
        operacionActual = "ADICIONAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
    }

    private void comandoActualizar() {
        if (flagCarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay ninguna operación pendiente de actualizar.", 
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validaciones básicas
        String empresa = txtEmpresa.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellidoPaterno = txtApellidoPaterno.getText().trim();
        String estadoStr = txtEstadoRegistro.getText().trim();

        if (empresa.isEmpty() || nombre.isEmpty() || apellidoPaterno.isEmpty() || estadoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Los campos empresa, nombre, apellido paterno y estado son obligatorios.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar categoría (obligatoria)
        ComboItem categoriaSeleccionada = (ComboItem) cbCategoria.getSelectedItem();
        if (categoriaSeleccionada == null || categoriaSeleccionada.getCodigo() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una categoría.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear objeto cliente
        Cliente cliente = new Cliente();
        cliente.setCliEmp(empresa);
        
        ComboItem repSeleccionado = (ComboItem) cbRepresentante.getSelectedItem();
        if (repSeleccionado != null && repSeleccionado.getCodigo() > 0) {
            cliente.setRepCod(repSeleccionado.getCodigo());
        }
        cliente.setCliNom(nombre);
        cliente.setCliApePat(apellidoPaterno);
        cliente.setCliApeMat(txtApellidoMaterno.getText().trim());
        
        ComboItem ciudadSeleccionada = (ComboItem) cbCiudad.getSelectedItem();
        if (ciudadSeleccionada != null && ciudadSeleccionada.getCodigo() > 0) {
            cliente.setCiuCod(ciudadSeleccionada.getCodigo());
        }
        
        cliente.setCliDirDetalle(txtDireccion.getText().trim());
        
        // Validar teléfono
        String telefonoStr = txtTelefono.getText().trim();
        if (!telefonoStr.isEmpty()) {
            try {
                Long telefono = Long.valueOf(telefonoStr);
                cliente.setCliTel(telefono);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El teléfono debe ser un número válido.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        cliente.setCliCor(txtCorreo.getText().trim());
        cliente.setCatCliCod(categoriaSeleccionada.getCodigo());
        cliente.setCliEstReg(estadoStr.charAt(0));

        boolean exito = false;
        String mensaje = "";

        switch (operacionActual) {
            case "ADICIONAR":
                String mensajeErrorAdi = clienteDAO.insertarCliente(cliente);
                if (mensajeErrorAdi == null) {
                    exito = true;
                    mensaje = "Cliente registrado con éxito.";
                } else {
                    exito = false;
                    mensaje = mensajeErrorAdi;
                }
                break;
            case "MODIFICAR":
                cliente.setCliCod(codigoSeleccionado);
                String mensajeErrorMod = clienteDAO.actualizarCliente(cliente);
                if (mensajeErrorMod == null) {
                    exito = true;
                    mensaje = "Cliente modificado con éxito.";
                } else {
                    exito = false;
                    mensaje = mensajeErrorMod;
                }
                break;
            case "ELIMINAR":
                exito = clienteDAO.eliminarLogicamenteCliente(codigoSeleccionado);
                mensaje = exito ? "Cliente eliminado con éxito." : "Error al eliminar cliente.";
                break;
            case "INACTIVAR":
                exito = clienteDAO.inactivarCliente(codigoSeleccionado);
                mensaje = exito ? "Cliente inactivado con éxito." : "Error al inactivar cliente.";
                break;
            case "REACTIVAR":
                exito = clienteDAO.reactivarCliente(codigoSeleccionado);
                mensaje = exito ? "Cliente reactivado con éxito." : "Error al reactivar cliente.";
                break;
            default:
                JOptionPane.showMessageDialog(this, "Operación no reconocida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }

        if (exito) {
            JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTablaClientes();
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

    private void cargarTablaClientes() {
        tableModel.setRowCount(0);
        List<Cliente> lista = clienteDAO.obtenerTodosClientes();
        for (Cliente c : lista) {
            tableModel.addRow(new Object[]{
                c.getCliCod(),
                c.getCliEmp(),
                c.getRepCod(),
                c.getCliNom(),
                c.getCliApePat(),
                c.getCliApeMat(),
                c.getCiuCod(),
                c.getCliTel(),
                c.getCliCor(),
                c.getCatCliCod(),
                c.getCliEstReg(),
                c.getRepresentanteNombre(),
                c.getCategoriaDescripcion(),
                c.getCiudadNombre()
            });
        }
    }

    private void habilitarControles(boolean b) {
        txtEmpresa.setEditable(b);
        cbRepresentante.setEnabled(b);
        txtNombre.setEditable(b);
        txtApellidoPaterno.setEditable(b);
        txtApellidoMaterno.setEditable(b);
        cbCiudad.setEnabled(b);
        txtDireccion.setEditable(b);
        txtTelefono.setEditable(b);
        txtCorreo.setEditable(b);
        cbCategoria.setEnabled(b);
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtEmpresa.setText("");
        cbRepresentante.setSelectedIndex(0);
        txtNombre.setText("");
        txtApellidoPaterno.setText("");
        txtApellidoMaterno.setText("");
        cbCiudad.setSelectedIndex(0);
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        cbCategoria.setSelectedIndex(0);
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
        
        // Botones de búsqueda siempre habilitados
        btnBuscarNombre.setEnabled(true);
        btnBuscarRepresentante.setEnabled(true);
        btnMostrarActivos.setEnabled(true);
        btnMostrarTodos.setEnabled(true);
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
        
        // Deshabilitar botones de búsqueda durante operaciones
        btnBuscarNombre.setEnabled(false);
        btnBuscarRepresentante.setEnabled(false);
        btnMostrarActivos.setEnabled(false);
        btnMostrarTodos.setEnabled(false);
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
        
        // Mantener botones de búsqueda habilitados
        btnBuscarNombre.setEnabled(true);
        btnBuscarRepresentante.setEnabled(true);
        btnMostrarActivos.setEnabled(true);
        btnMostrarTodos.setEnabled(true);
    }

    private void comandoModificar() {
        int selectedRow = tablaClientes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para modificar.", 
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        habilitarControles(true);
        operacionActual = "MODIFICAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
    }

    private void comandoEliminar() {
        int selectedRow = tablaClientes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para eliminar.", 
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        cargarDatosDesdeTabla(selectedRow);
        txtEstadoRegistro.setText("*");
        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        habilitarControles(false);
        operacionActual = "ELIMINAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como eliminado ('*').\nPresione 'Actualizar' para confirmar.", 
                                    "Eliminación Lógica", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoInactivar() {
        int selectedRow = tablaClientes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para inactivar.", 
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        cargarDatosDesdeTabla(selectedRow);
        txtEstadoRegistro.setText("I");
        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        habilitarControles(false);
        operacionActual = "INACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como inactivo ('I').\nPresione 'Actualizar' para confirmar.", 
                                    "Inactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }

        private void comandoReactivar() {
        int selectedRow = tablaClientes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para reactivar.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        cargarDatosDesdeTabla(selectedRow);
        txtEstadoRegistro.setText("A");
        codigoSeleccionado = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        habilitarControles(false);
        operacionActual = "REACTIVAR";
        flagCarFlaAct = 1;
        habilitarBotonesParaOperacion();
        JOptionPane.showMessageDialog(this, "El registro se marcará como activo ('A').\nPresione 'Actualizar' para confirmar.",
                "Reactivar Registro", JOptionPane.INFORMATION_MESSAGE);
    }

    private void comandoBuscarNombre() {
        String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre a buscar:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            List<Cliente> lista = clienteDAO.buscarClientesPorNombre(nombre.trim());
            tableModel.setRowCount(0);
            for (Cliente c : lista) {
                tableModel.addRow(new Object[]{
                    c.getCliCod(), c.getCliEmp(), c.getRepCod(), c.getCliNom(),
                    c.getCliApePat(), c.getCliApeMat(), c.getCiuCod(), c.getCliTel(), c.getCliCor(),
                    c.getCatCliCod(), c.getCliEstReg(), c.getRepresentanteNombre(),
                    c.getCategoriaDescripcion(), c.getCiudadNombre()
                });
            }
        }
    }

    private void comandoBuscarRepresentante() {
    String input = JOptionPane.showInputDialog(this, "Ingrese el código del representante:");
    if (input != null && !input.trim().isEmpty()) {
        try {
            int repCod = Integer.parseInt(input.trim());
            List<Cliente> lista = clienteDAO.buscarClientesPorRepresentante(repCod);
            tableModel.setRowCount(0);
            for (Cliente c : lista) {
                tableModel.addRow(new Object[]{
                    c.getCliCod(), c.getCliEmp(), c.getRepCod(), c.getCliNom(),
                    c.getCliApePat(), c.getCliApeMat(), c.getCiuCod(), c.getCliTel(), c.getCliCor(),
                    c.getCatCliCod(), c.getCliEstReg(), c.getRepresentanteNombre(),
                    c.getCategoriaDescripcion(), c.getCiudadNombre()
                });
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Código inválido. Ingrese un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


    private void comandoMostrarActivos() {
        List<Cliente> lista = clienteDAO.obtenerClientesActivos();
        tableModel.setRowCount(0);
        for (Cliente c : lista) {
            tableModel.addRow(new Object[]{
                c.getCliCod(), c.getCliEmp(), c.getRepCod(), c.getCliNom(),
                c.getCliApePat(), c.getCliApeMat(), c.getCiuCod(), c.getCliTel(), c.getCliCor(),
                c.getCatCliCod(), c.getCliEstReg(), c.getRepresentanteNombre(),
                c.getCategoriaDescripcion(), c.getCiudadNombre()
            });
        }
    }

    private void comandoMostrarTodos() {
        cargarTablaClientes();
    }

    private void cargarDatosDesdeTabla(int rowIndex) {
        txtCodigo.setText(tableModel.getValueAt(rowIndex, 0).toString());
        txtEmpresa.setText((String) tableModel.getValueAt(rowIndex, 1));
        seleccionarEnComboBoxPorCodigo(cbRepresentante, (Integer) tableModel.getValueAt(rowIndex, 2));
        txtNombre.setText((String) tableModel.getValueAt(rowIndex, 3));
        txtApellidoPaterno.setText((String) tableModel.getValueAt(rowIndex, 4));
        txtApellidoMaterno.setText((String) tableModel.getValueAt(rowIndex, 5));
        seleccionarEnComboBoxPorCodigo(cbCiudad, (Integer) tableModel.getValueAt(rowIndex, 6));
        txtTelefono.setText(tableModel.getValueAt(rowIndex, 8) != null ? tableModel.getValueAt(rowIndex, 7).toString() : "");
        txtCorreo.setText((String) tableModel.getValueAt(rowIndex, 8));
        seleccionarEnComboBoxPorCodigo(cbCategoria, (Integer) tableModel.getValueAt(rowIndex, 9));
        txtEstadoRegistro.setText(tableModel.getValueAt(rowIndex, 10).toString());
    }
}
