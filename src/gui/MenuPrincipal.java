package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipal extends JFrame{

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }

    public MenuPrincipal(){
        setTitle("Sistema de Mantenimiento");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Seleccione una opción del menú", SwingConstants.CENTER);
        add(welcomeLabel);

        JMenuBar menuBar = new JMenuBar();
        JMenu menuMantenimiento = new JMenu("Mantenimiento");

        JMenuItem itemAuditoria = new JMenuItem("Mantenimiento de Auditoria");
        JMenuItem itemCargo = new JMenuItem("Mantenimiento de Cargo");
        JMenuItem itemCategoriaCliente = new JMenuItem("Mantenimiento de Categoría Cliente");
        JMenuItem itemCiudad = new JMenuItem("Mantenimiento de Ciudad");
        JMenuItem itemClasificacionProducto = new JMenuItem("Mantenimiento de Clasificación Producto");
        JMenuItem itemCliente = new JMenuItem("Mantenimiento de Cliente");
        JMenuItem itemDepartamento = new JMenuItem("Mantenimiento de Departamento");
        JMenuItem itemDetalleFactura = new JMenuItem("Mantenimiento de Detalle Factura");
        JMenuItem itemDisponibilidadProducto = new JMenuItem("Mantenimiento de Disponibilidad Producto");
        JMenuItem itemEstadoSistema = new JMenuItem("Mantenimiento de Estado Sistema");
        JMenuItem itemFabricanteProducto = new JMenuItem("Mantenimiento de Fabricante Producto");
        JMenuItem itemFactura = new JMenuItem("Mantenimiento de Factura");
        JMenuItem itemModuloAuditoria = new JMenuItem("Mantenimiento de Modulo Auditoria");
        JMenuItem itemOficina = new JMenuItem("Mantenimiento de Oficina");
        JMenuItem itemProducto = new JMenuItem("Mantenimiento de Producto");
        JMenuItem itemProdUnidadMedida = new JMenuItem("Mantenimiento de Unidad Medida Producto");
        JMenuItem itemRegion = new JMenuItem("Mantenimiento de Región");
        JMenuItem itemReporRepVenta = new JMenuItem("Mantenimiento de Reporte Representante de Venta");
        JMenuItem itemReporteProducto = new JMenuItem("Mantenimiento de Reporte de Producto");
        JMenuItem itemRepVenta = new JMenuItem("Mantenimiento de Representante de Venta");
        JMenuItem itemUsuarioSistema = new JMenuItem("Mantenimiento de Usuario Sistema");         
        JMenuItem itemSalir = new JMenuItem("Salir");

        for (JMenuItem b : new JMenuItem[]{itemAuditoria, itemCargo, itemCategoriaCliente, itemCiudad, itemClasificacionProducto, itemCliente, itemDepartamento, itemDetalleFactura, itemDisponibilidadProducto, itemEstadoSistema, itemFabricanteProducto, itemFactura, itemModuloAuditoria, itemOficina, itemProducto, itemProdUnidadMedida, itemRegion, itemReporRepVenta, itemReporteProducto, itemRepVenta, itemUsuarioSistema, itemSalir}) {
            menuMantenimiento.add(b);
        }
        menuBar.add(menuMantenimiento);
        setJMenuBar(menuBar);

        itemAuditoria.addActionListener(e -> new AuditoriaFrame().setVisible(true));
        itemCargo.addActionListener(e -> new CargoFrame().setVisible(true));
        itemCategoriaCliente.addActionListener(e -> new CategoriaClienteFrame().setVisible(true));
        itemCiudad.addActionListener(e -> new CiudadFrame().setVisible(true));
        itemClasificacionProducto.addActionListener(e -> new ClasificacionProductoFrame().setVisible(true));
        itemCliente.addActionListener(e -> new ClienteFrame().setVisible(true));
        itemDepartamento.addActionListener(e -> new DepartamentoFrame().setVisible(true));
        itemDetalleFactura.addActionListener(e -> new DetalleFacturaFrame().setVisible(true));
        itemDisponibilidadProducto.addActionListener(e -> new DisponibilidadProductoFrame().setVisible(true));
        itemEstadoSistema.addActionListener(e -> new EstadoSistemaFrame().setVisible(true));
        itemFabricanteProducto.addActionListener(e -> new FabricanteProductoFrame().setVisible(true));
        itemFactura.addActionListener(e -> new FacturaFrame().setVisible(true));
        itemModuloAuditoria.addActionListener(e -> new ModuloAuditoriaFrame().setVisible(true));
        itemOficina.addActionListener(e -> new OficinaFrame().setVisible(true));
        itemProducto.addActionListener(e -> new ProductoFrame().setVisible(true));
        itemProdUnidadMedida.addActionListener(e -> new ProdUnidadMedidaFrame().setVisible(true));
        itemRegion.addActionListener(e -> new RegionFrame().setVisible(true));
        itemReporRepVenta.addActionListener(e -> new ReporRepVentaFrame().setVisible(true));
        itemReporteProducto.addActionListener(e -> new ReporteProductoFrame().setVisible(true));
        itemRepVenta.addActionListener(e -> new RepVentaFrame().setVisible(true));
        itemUsuarioSistema.addActionListener(e -> new UsuarioSistemaFrame().setVisible(true));

        itemSalir.addActionListener(e -> System.exit(0));
    }
}