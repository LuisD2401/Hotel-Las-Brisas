import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class VentanaVentas extends JFrame {

    private java.util.List<Venta> ventasCafeteria;
    private DefaultTableModel modelo;
    private JTable tabla;

    public VentanaVentas() {
        setTitle("Ventas Cafetería");
        setSize(850, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Modelo de tabla: Tipo | Monto | Fecha y Hora | Ver Boleta | Eliminar
        modelo = new DefaultTableModel(new Object[]{"Tipo", "Monto", "Fecha y Hora", "Ver Boleta", "Eliminar"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 3;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(30);

        // Llenar tabla solo con ventas de tipo "Cafetería"
        cargarTabla();

        // Renderers y editores para botones
        tabla.getColumn("Ver Boleta").setCellRenderer(new ButtonRenderer());
        tabla.getColumn("Ver Boleta").setCellEditor(new ButtonEditor(new JCheckBox(), true));

        tabla.getColumn("Eliminar").setCellRenderer(new ButtonRenderer());
        tabla.getColumn("Eliminar").setCellEditor(new ButtonEditor(new JCheckBox(), false));

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        setVisible(true);
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        ventasCafeteria = new java.util.ArrayList<>();

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Venta v : Main.ventas) {
            if (!"Cafetería".equals(v.tipo)) continue;

            ventasCafeteria.add(v);

            modelo.addRow(new Object[]{
                    v.tipo,
                    "$" + String.format("%.0f", v.monto),
                    v.fecha.format(df),
                    "Ver Boleta",
                    "Eliminar"
            });
        }
    }

    // Renderer para botones
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() { setOpaque(true); }
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // Editor para botones
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean verBoleta; // true: ver boleta, false: eliminar
        private int row;

        public ButtonEditor(JCheckBox checkBox, boolean verBoleta) {
            super(checkBox);
            this.verBoleta = verBoleta;
            button = new JButton();
            button.setOpaque(true);

            button.addActionListener(e -> {
                if (verBoleta) {
                    Venta v = Main.ventas.stream()
                            .filter(venta -> "Cafetería".equals(venta.tipo))
                            .toList().get(row);

                    mostrarBoletaGrande(v);
                } else {
                    eliminarVenta(row);
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            button.setText((value == null) ? "" : value.toString());
            this.row = row;
            return button;
        }

        public Object getCellEditorValue() { return button.getText(); }
    }

    private void mostrarBoleta(int fila) {
        if (fila < 0 || fila >= ventasCafeteria.size()) return;

        Venta v = ventasCafeteria.get(fila);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        StringBuilder b = new StringBuilder();

        b.append("══════════════════════════════\n");
        b.append("      HOTEL LAS BRISAS\n");
        b.append("         CAFETERÍA\n");
        b.append("══════════════════════════════\n");
        b.append("Fecha: ").append(v.fecha.format(df)).append("\n");
        b.append("Pago: ").append(v.medioPago).append("\n");
        b.append("--------------------------------\n");

        double total = 0;

        for (int i = 0; i < v.productos.size(); i++) {
            ProductoCafeteria p = v.productos.get(i);
            int c = v.cantidades.get(i);
            double sub = p.precio * c;
            total += sub;

            b.append(String.format("%-15s x%-2d $%6.0f\n", p.nombre, c, sub));
        }

        b.append("--------------------------------\n");
        b.append("TOTAL: $").append(String.format("%.0f", total)).append("\n");
        b.append("\nGracias por su compra 💙\n");

        JTextArea area = new JTextArea(b.toString());
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setEditable(false);

        JOptionPane.showMessageDialog(this, new JScrollPane(area),
                "Boleta", JOptionPane.INFORMATION_MESSAGE);
    }


    private void eliminarVenta(int fila) {
        if (fila < 0 || fila >= ventasCafeteria.size()) return;

        Venta v = ventasCafeteria.get(fila);

        int op = JOptionPane.showConfirmDialog(this,
                "¿Eliminar esta venta?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (op == JOptionPane.YES_OPTION) {
            Main.ventas.remove(v);
            Persistencia.guardar(Main.ventas, Main.ARCHIVO_VENTAS);
            cargarTabla();
        }
    }
    private void mostrarBoletaGrande(Venta v) {
        JDialog dialog = new JDialog(this, "Boleta de Venta", true);
        dialog.setSize(600, 700);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 16));

        StringBuilder b = new StringBuilder();
        b.append("====================================\n");
        b.append("        HOTEL LAS BRISAS\n");
        b.append("              BOLETA\n");
        b.append("====================================\n\n");

        b.append("Fecha: ")
                .append(v.fecha.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .append("\n");

        b.append("Tipo: ").append(v.tipo).append("\n");
        b.append("Medio de pago: ").append(v.medioPago).append("\n");

        if (v.numeroHabitacion != -1) {
            b.append("Habitación: ").append(v.numeroHabitacion).append("\n");
        }

        b.append("\n------------------------------------\n");
        b.append("DETALLE\n");
        b.append("------------------------------------\n");

        double total = 0;
        for (int i = 0; i < v.productos.size(); i++) {
            ProductoCafeteria p = v.productos.get(i);
            int c = v.cantidades.get(i);
            double sub = p.precio * c;
            total += sub;

            b.append(String.format("%-20s x%2d $%7.2f\n",
                    p.nombre, c, sub));
        }

        b.append("------------------------------------\n");
        b.append(String.format("TOTAL: $%.2f\n", total));

        if ("Efectivo".equals(v.medioPago)) {
            b.append("Pago en efectivo\n");
        }

        b.append("\n====================================\n");
        b.append("        ¡GRACIAS POR SU COMPRA!\n");
        b.append("====================================\n");

        area.setText(b.toString());

        JScrollPane scroll = new JScrollPane(area);
        dialog.add(scroll, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 18));
        btnCerrar.addActionListener(e -> dialog.dispose());

        JPanel sur = new JPanel();
        sur.add(btnCerrar);
        dialog.add(sur, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }


}




