package gui.consultas;

import dao.consultas.ConsultaProductoDAO;
import modelo.consultas.ConsultaProducto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class ConsultaProductoFrame extends JFrame {
    
    private JTable tablaEstadisticas;
    private DefaultTableModel tableModel;
    private JButton btnActualizar, btnSalir;
    private JLabel lblTotalClasificaciones, lblTotalProductos, lblPromedioGlobal;
    
    private ConsultaProductoDAO estadisticasDAO;
    private DecimalFormat decimalFormat =  new DecimalFormat("'S/'#,##0.00");
    
    public ConsultaProductoFrame() {
        setTitle("Estadísticas de Productos - Análisis por Clasificación");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        estadisticasDAO = new ConsultaProductoDAO();
        
        initComponents();
        cargarEstadisticas();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Panel superior con información general
        JPanel panelInfo = new JPanel(new GridLayout(1, 3, 10, 10));
        panelInfo.setBorder(BorderFactory.createTitledBorder("Resumen General"));
        
        lblTotalClasificaciones = new JLabel("Total Clasificaciones: 0");
        lblTotalProductos = new JLabel("Total Productos: 0");
        lblPromedioGlobal = new JLabel("Precio Promedio Global: S/. 0.00");
        
        panelInfo.add(lblTotalClasificaciones);
        panelInfo.add(lblTotalProductos);
        panelInfo.add(lblPromedioGlobal);
        
        add(panelInfo, BorderLayout.NORTH);
        
        // Panel central con tabla
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Estadísticas por Clasificación de Producto"));
        
        tableModel = new DefaultTableModel(
            new Object[]{"Código", "Descripción", "Total Productos", "Precio Promedio", "Precio Máximo", "Stock Mínimo"}, 
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaEstadisticas = new JTable(tableModel);
        tablaEstadisticas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Configurar el ancho de las columnas
        tablaEstadisticas.getColumnModel().getColumn(0).setPreferredWidth(80);
        tablaEstadisticas.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaEstadisticas.getColumnModel().getColumn(2).setPreferredWidth(120);
        tablaEstadisticas.getColumnModel().getColumn(3).setPreferredWidth(120);
        tablaEstadisticas.getColumnModel().getColumn(4).setPreferredWidth(120);
        tablaEstadisticas.getColumnModel().getColumn(5).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(tablaEstadisticas);
        panelTabla.add(scrollPane, BorderLayout.CENTER);
        
        add(panelTabla, BorderLayout.CENTER);
        
        // Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        
        btnActualizar = new JButton("Actualizar Datos");
        btnSalir = new JButton("Salir");
        
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarEstadisticas();
                JOptionPane.showMessageDialog(
                    ConsultaProductoFrame.this,
                    "Datos actualizados correctamente.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        panelBotones.add(btnActualizar);
        panelBotones.add(btnSalir);
        
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void cargarEstadisticas() {
        tableModel.setRowCount(0);
        
        List<ConsultaProducto> estadisticas = estadisticasDAO.obtenerConsultasProductos();
        
        int totalProductos = 0;
        BigDecimal sumaPromedios = BigDecimal.ZERO;
        int contadorClasificaciones = 0;
        
        for (ConsultaProducto est : estadisticas) {
            Object[] row = {
                est.getClasProCod(),
                est.getClasProDesc(),
                est.getTotalProductos(),
                est.getPrecioPromedio() != null ? decimalFormat.format(est.getPrecioPromedio()) : "N/A",
                est.getPrecioMaximo() != null ? decimalFormat.format(est.getPrecioMaximo()) : "N/A",
                est.getStockMinimo()
            };
            tableModel.addRow(row);
            
            totalProductos += est.getTotalProductos();
            if (est.getPrecioPromedio() != null) {
                sumaPromedios = sumaPromedios.add(est.getPrecioPromedio());
                contadorClasificaciones++;
            }
        }
        
        // Calcular promedio global
        BigDecimal promedioGlobal = BigDecimal.ZERO;
        if (contadorClasificaciones > 0) {
            promedioGlobal = sumaPromedios.divide(new BigDecimal(contadorClasificaciones), 2, RoundingMode.HALF_UP);

        }
        
        // Actualizar labels de resumen
        lblTotalClasificaciones.setText("Total Clasificaciones: " + estadisticas.size());
        lblTotalProductos.setText("Total Productos: " + totalProductos);
        lblPromedioGlobal.setText("Precio Promedio Global: " + decimalFormat.format(promedioGlobal));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ConsultaProductoFrame().setVisible(true);
        });
    }
}