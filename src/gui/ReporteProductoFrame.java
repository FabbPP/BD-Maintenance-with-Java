package gui;

import dao.ReporteProductoDAO;
import dao.ProductoDAO;
import modelo.ReporteProducto;
import modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ReporteProductoFrame extends JFrame {

    private JComboBox<Producto> comboProductos;
    private JTextField txtCodigo;
    private JTextField txtStockMinimo;
    private JTextField txtStockMaximo;
    private JTextField txtEstadoRegistro;

    private JTable tablaReportes;
    private DefaultTableModel tableModel;

    private JButton btnAdicionar, btnModificar, btnEliminar, btnActualizar, btnCancelar, btnInactivar, btnReactivar, btnSalir;

    private ReporteProductoDAO reporteDAO = new ReporteProductoDAO();
    private ProductoDAO productoDAO = new ProductoDAO();

    private int codigoSeleccionado = 0;
    private String operacion = "";

    public ReporteProductoFrame() {
        setTitle("Mantenimiento de Reportes de Producto");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        cargarComboProductos();
        cargarTabla();
        habilitarControles(false);
        habilitarBotonesIniciales();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel formulario
        JPanel panelForm = new JPanel(new GridLayout(5, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Reporte"));

        txtCodigo = new JTextField();
        txtCodigo.setEditable(false);

        comboProductos = new JComboBox<>();

        txtStockMinimo = new JTextField();
        txtStockMaximo = new JTextField();
        txtEstadoRegistro = new JTextField();
        txtEstadoRegistro.setEditable(false);

        panelForm.add(new JLabel("Código:"));
        panelForm.add(txtCodigo);
        panelForm.add(new JLabel("Producto:"));
        panelForm.add(comboProductos);
        panelForm.add(new JLabel("Stock Mínimo:"));
        panelForm.add(txtStockMinimo);
        panelForm.add(new JLabel("Stock Máximo:"));
        panelForm.add(txtStockMaximo);
        panelForm.add(new JLabel("Estado:"));
        panelForm.add(txtEstadoRegistro);

        add(panelForm, BorderLayout.NORTH);

        // Tabla
        tableModel = new DefaultTableModel(new Object[]{"Código", "Producto", "Min", "Max", "Estado"}, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tablaReportes = new JTable(tableModel);
        tablaReportes.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tablaReportes.getSelectedRow();
                if (fila != -1 && operacion.isEmpty()) {
                    txtCodigo.setText(tableModel.getValueAt(fila, 0).toString());
                    int prodCod = (int) tableModel.getValueAt(fila, 1);
                    seleccionarProductoEnCombo(prodCod);
                    txtStockMinimo.setText(tableModel.getValueAt(fila, 2).toString());
                    txtStockMaximo.setText(tableModel.getValueAt(fila, 3).toString());
                    txtEstadoRegistro.setText(tableModel.getValueAt(fila, 4).toString());
                    codigoSeleccionado = Integer.parseInt(txtCodigo.getText());
                    habilitarBotonesParaSeleccion();
                }
            }
        });
        add(new JScrollPane(tablaReportes), BorderLayout.CENTER);

        // Panel botones
        JPanel panelBotones = new JPanel(new FlowLayout());

        btnAdicionar = new JButton("Adicionar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnInactivar = new JButton("Inactivar");
        btnReactivar = new JButton("Reactivar");
        btnActualizar = new JButton("Actualizar");
        btnCancelar = new JButton("Cancelar");
        btnSalir = new JButton("Salir");

        panelBotones.add(btnAdicionar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnInactivar);
        panelBotones.add(btnReactivar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnSalir);

        add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnAdicionar.addActionListener(e -> {
            operacion = "ADICIONAR";
            limpiarCampos();
            habilitarControles(true);
            txtEstadoRegistro.setText("A");
            habilitarBotonesParaOperacion();
        });

        btnModificar.addActionListener(e -> {
            if (codigoSeleccionado == 0) return;
            operacion = "MODIFICAR";
            habilitarControles(true);
            habilitarBotonesParaOperacion();
        });

        btnEliminar.addActionListener(e -> {
            if (codigoSeleccionado != 0) {
                operacion = "ELIMINAR";
                txtEstadoRegistro.setText("*");
                habilitarBotonesParaOperacion();
            }
        });

        btnInactivar.addActionListener(e -> {
            if (codigoSeleccionado != 0) {
                operacion = "INACTIVAR";
                txtEstadoRegistro.setText("I");
                habilitarBotonesParaOperacion();
            }
        });

        btnReactivar.addActionListener(e -> {
            if (codigoSeleccionado != 0) {
                operacion = "REACTIVAR";
                txtEstadoRegistro.setText("A");
                habilitarBotonesParaOperacion();
            }
        });

        btnActualizar.addActionListener(e -> {
            Producto prod = (Producto) comboProductos.getSelectedItem();
            int stockMin = Integer.parseInt(txtStockMinimo.getText());
            int stockMax = Integer.parseInt(txtStockMaximo.getText());
            char estado = txtEstadoRegistro.getText().charAt(0);

            ReporteProducto reporte = new ReporteProducto();
            reporte.setProdCod(prod.getProdCod());
            reporte.setReporProdMin(stockMin);
            reporte.setReporProdMax(stockMax);
            reporte.setReporProdEstReg(estado);

            boolean exito = false;

            switch (operacion) {
                case "ADICIONAR":
                    exito = reporteDAO.insertarReporte(reporte) == null;
                    break;
                case "MODIFICAR":
                    reporte.setReporProdCod(codigoSeleccionado);
                    exito = reporteDAO.actualizarReporte(reporte) == null;
                    break;
                case "ELIMINAR":
                    exito = reporteDAO.eliminarLogicamenteReporte(codigoSeleccionado);
                    break;
                case "INACTIVAR":
                    exito = reporteDAO.inactivarReporte(codigoSeleccionado);
                    break;
                case "REACTIVAR":
                    exito = reporteDAO.reactivarReporte(codigoSeleccionado);
                    break;
            }

            if (exito) {
                JOptionPane.showMessageDialog(this, "Operación exitosa");
                cargarTabla();
                limpiarCampos();
                operacion = "";
                habilitarControles(false);
                habilitarBotonesIniciales();
            } else {
                JOptionPane.showMessageDialog(this, "Error en la operación");
            }
        });

        btnCancelar.addActionListener(e -> {
            limpiarCampos();
            habilitarControles(false);
            operacion = "";
            habilitarBotonesIniciales();
        });

        btnSalir.addActionListener(e -> dispose());
    }

    private void cargarComboProductos() {
        comboProductos.removeAllItems();
        List<Producto> productos = productoDAO.obtenerTodosProductos();
        for (Producto p : productos) {
            comboProductos.addItem(p);
        }
    }

    private void seleccionarProductoEnCombo(int prodCod) {
        for (int i = 0; i < comboProductos.getItemCount(); i++) {
            if (comboProductos.getItemAt(i).getProdCod() == prodCod) {
                comboProductos.setSelectedIndex(i);
                break;
            }
        }
    }

    private void cargarTabla() {
        tableModel.setRowCount(0);
        List<ReporteProducto> lista = reporteDAO.obtenerTodosReportes();
        for (ReporteProducto r : lista) {
            tableModel.addRow(new Object[]{
                r.getReporProdCod(),
                r.getProdCod(),
                r.getReporProdMin(),
                r.getReporProdMax(),
                r.getReporProdEstReg()
            });
        }
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtStockMinimo.setText("");
        txtStockMaximo.setText("");
        txtEstadoRegistro.setText("");
        codigoSeleccionado = 0;
    }

    private void habilitarControles(boolean b) {
        comboProductos.setEnabled(b);
        txtStockMinimo.setEditable(b);
        txtStockMaximo.setEditable(b);
    }

    private void habilitarBotonesIniciales() {
        btnAdicionar.setEnabled(true);
        btnModificar.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnInactivar.setEnabled(true);
        btnReactivar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnCancelar.setEnabled(false);
    }

    private void habilitarBotonesParaOperacion() {
        btnAdicionar.setEnabled(false);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnInactivar.setEnabled(false);
        btnReactivar.setEnabled(false);
        btnActualizar.setEnabled(true);
        btnCancelar.setEnabled(true);
    }

    private void habilitarBotonesParaSeleccion() {
        btnModificar.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnInactivar.setEnabled(true);
        btnReactivar.setEnabled(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReporteProductoFrame().setVisible(true));
    }
}
