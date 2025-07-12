package gui.consultas;

import dao.consultas.ConsultaProductoDAO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import modelo.consultas.ConsultaProducto;

public class ConsultaProductoFrame extends JFrame {
    
    private JTable tablaEstadisticas;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JButton btnActualizar, btnSalir;
    private JLabel lblTotalClasificaciones, lblTotalProductos, lblPromedioGlobal;
    
    private ConsultaProductoDAO estadisticasDAO;
    private DecimalFormat decimalFormat = new DecimalFormat("'S/'#,##0.00");
    
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
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return String.class;    // Código
                    case 1: return String.class;    // Descripción
                    case 2: return Integer.class;   // Total Productos
                    case 3: return BigDecimal.class; // Precio Promedio
                    case 4: return BigDecimal.class; // Precio Máximo
                    case 5: return Integer.class;   // Stock Mínimo
                    default: return Object.class;
                }
            }
        };
        
        tablaEstadisticas = new JTable(tableModel);
        tablaEstadisticas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Alinear todas las celdas de la tabla a la izquierda
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        // Aplicar el renderizador a todas las columnas por defecto
        for (int i = 0; i < tablaEstadisticas.getColumnCount(); i++) {
            tablaEstadisticas.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }

                
        // Configurar el sorter con comparadores personalizados
        sorter = new TableRowSorter<>(tableModel);
        tablaEstadisticas.setRowSorter(sorter);
        
        // Configurar comparadores personalizados para todas las columnas
        // Columna 0: Código (String pero ordenado numéricamente)
        sorter.setComparator(0, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                try {
                    // Intentar convertir a número para ordenamiento numérico
                    Integer num1 = Integer.parseInt(String.valueOf(o1));
                    Integer num2 = Integer.parseInt(String.valueOf(o2));
                    return num1.compareTo(num2);
                } catch (NumberFormatException e) {
                    // Si no son números, ordenar como string
                    return String.valueOf(o1).compareTo(String.valueOf(o2));
                }
            }
        });
        
        // Columna 1: Descripción (String)
        sorter.setComparator(1, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return String.valueOf(o1).compareTo(String.valueOf(o2));
            }
        });
        
        // Columna 2: Total Productos (Integer)
        sorter.setComparator(2, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                Integer i1 = (Integer) o1;
                Integer i2 = (Integer) o2;
                return i1.compareTo(i2);
            }
        });
        
        // Columna 3: Precio Promedio (BigDecimal)
        sorter.setComparator(3, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof BigDecimal && o2 instanceof BigDecimal) {
                    return ((BigDecimal) o1).compareTo((BigDecimal) o2);
                }
                return 0;
            }
        });
        
        // Columna 4: Precio Máximo (BigDecimal)
        sorter.setComparator(4, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof BigDecimal && o2 instanceof BigDecimal) {
                    return ((BigDecimal) o1).compareTo((BigDecimal) o2);
                }
                return 0;
            }
        });
        
        // Columna 5: Stock Mínimo (Integer)
        sorter.setComparator(5, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                Integer i1 = (Integer) o1;
                Integer i2 = (Integer) o2;
                return i1.compareTo(i2);
            }
        });
        
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
                est.getPrecioPromedio(), // Guardamos el BigDecimal original para ordenamiento
                est.getPrecioMaximo(),   // Guardamos el BigDecimal original para ordenamiento
                est.getStockMinimo()
            };
            tableModel.addRow(row);
            
            totalProductos += est.getTotalProductos();
            if (est.getPrecioPromedio() != null) {
                sumaPromedios = sumaPromedios.add(est.getPrecioPromedio());
                contadorClasificaciones++;
            }
        }
        
        // Configurar el renderer personalizado para mostrar formato monetario
        DefaultTableCellRenderer promedioRenderer = new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                if (value instanceof BigDecimal) {
                    setText(decimalFormat.format(value));
                } else {
                    setText(value != null ? value.toString() : "N/A");
                }
            }
        };
        promedioRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        tablaEstadisticas.getColumnModel().getColumn(3).setCellRenderer(promedioRenderer);

        
        DefaultTableCellRenderer maximoRenderer = new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                if (value instanceof BigDecimal) {
                    setText(decimalFormat.format(value));
                } else {
                    setText(value != null ? value.toString() : "N/A");
                }
            }
        };
        maximoRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        tablaEstadisticas.getColumnModel().getColumn(4).setCellRenderer(maximoRenderer);

        
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