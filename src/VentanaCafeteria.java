import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VentanaCafeteria extends JFrame {

    private ArrayList<JSpinner> spinners = new ArrayList<>();
    private ArrayList<ProductoCafeteria> productosMostrados = new ArrayList<>();
    private JPanel panelProductos;
    private JLabel lblTotal;

    public static void gestionarProductosEstatico() {
        VentanaCafeteria v = new VentanaCafeteria();
        v.setVisible(false);      // ← Nunca se muestra la ventana de compras
        v.abrirGestionProductos();
    }

    public VentanaCafeteria() {
        setTitle("Cafetería");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        panelProductos = new JPanel();
        panelProductos.setLayout(new BoxLayout(panelProductos, BoxLayout.Y_AXIS));

        lblTotal = new JLabel("Total: $0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTotal, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(panelProductos);
        add(scroll, BorderLayout.CENTER);

        JButton btnComprar = new JButton("Comprar");
        add(btnComprar, BorderLayout.SOUTH);

        cargarProductos();

        btnComprar.addActionListener(e -> {
            double total = 0;
            ArrayList<ProductoCafeteria> productosComprados = new ArrayList<>();
            ArrayList<Integer> cantidadesCompradas = new ArrayList<>();

            for (int i = 0; i < spinners.size(); i++) {
                int cantidad = (int) spinners.get(i).getValue();
                ProductoCafeteria p = productosMostrados.get(i);

                if (cantidad > 0) {

                    if (cantidad > p.stock) {
                        JOptionPane.showMessageDialog(this,
                                "El producto " + p.nombre + " no tiene suficiente stock.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    productosComprados.add(p);
                    cantidadesCompradas.add(cantidad);
                    total += p.precio * cantidad;
                }
            }

            if (total == 0) {
                JOptionPane.showMessageDialog(this, "Seleccione al menos un producto.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Venta v = Main.procesarPagoGUI(total, "Cafetería", productosComprados, cantidadesCompradas, -1);
            if (v == null) return;

            for (int i = 0; i < productosComprados.size(); i++) {
                ProductoCafeteria p = productosComprados.get(i);
                int cantidad = cantidadesCompradas.get(i);
                p.stock = Math.max(p.stock - cantidad, 0);
            }

            Persistencia.guardar(Main.productos, Main.ARCHIVO_PRODUCTOS);
            JOptionPane.showMessageDialog(this, "Compra realizada con éxito.", "Cafetería", JOptionPane.INFORMATION_MESSAGE);
            cargarProductos();
        });
    }

    private void cargarProductos() {
        panelProductos.removeAll();
        spinners.clear();
        productosMostrados.clear();

        for (ProductoCafeteria p : Main.productos) {
            if (p.stock <= 0) continue;

            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

            JLabel lblNombre = new JLabel(p.nombre + " - $" + p.precio + " - Stock: " + p.stock);
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, p.stock, 1));

            spinners.add(spinner);
            productosMostrados.add(p);

            spinner.addChangeListener(e -> actualizarTotal());

            panel.add(lblNombre);
            panel.add(new JLabel("Cantidad:"));
            panel.add(spinner);

            panelProductos.add(panel);
        }

        panelProductos.revalidate();
        panelProductos.repaint();
        actualizarTotal();
    }

    private void actualizarTotal() {
        double total = 0;

        for (int i = 0; i < spinners.size(); i++) {
            int cantidad = (int) spinners.get(i).getValue();
            ProductoCafeteria p = productosMostrados.get(i);
            total += cantidad * p.precio;
        }

        lblTotal.setText("Total: $" + String.format("%.2f", total));
    }
    public void abrirGestionProductos() {

        JFrame ventana = new JFrame("Gestionar Productos");
        ventana.setSize(700, 500);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(panel);
        ventana.add(scroll, BorderLayout.CENTER);

        Runnable construirFilas = () -> {
            panel.removeAll();

            for (ProductoCafeteria p : Main.productos) {
                JPanel fila = new JPanel();
                fila.setLayout(new BorderLayout(10, 0)); // separación horizontal entre componentes
                fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35)); // altura fija

                JLabel lblInfo = new JLabel(p.nombre + "  —  $" + String.format("%.2f", p.precio) + "  —  Stock: " + p.stock);

                JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
                JButton btnMinus5 = new JButton("-5");
                JButton btnMinus = new JButton("-");
                JButton btnPlus = new JButton("+");
                JButton btnPlus5 = new JButton("+5");

                // acciones
                btnPlus.addActionListener(ev -> {
                    p.stock++;
                    Persistencia.guardar(Main.productos, Main.ARCHIVO_PRODUCTOS);
                    lblInfo.setText(p.nombre + "  —  $" + String.format("%.2f", p.precio) + "  —  Stock: " + p.stock);
                });
                btnMinus.addActionListener(ev -> {
                    if (p.stock > 0) p.stock--;
                    Persistencia.guardar(Main.productos, Main.ARCHIVO_PRODUCTOS);
                    lblInfo.setText(p.nombre + "  —  $" + String.format("%.2f", p.precio) + "  —  Stock: " + p.stock);
                });
                btnPlus5.addActionListener(ev -> {
                    p.stock += 5;
                    Persistencia.guardar(Main.productos, Main.ARCHIVO_PRODUCTOS);
                    lblInfo.setText(p.nombre + "  —  $" + String.format("%.2f", p.precio) + "  —  Stock: " + p.stock);
                });
                btnMinus5.addActionListener(ev -> {
                    p.stock = Math.max(0, p.stock - 5);
                    Persistencia.guardar(Main.productos, Main.ARCHIVO_PRODUCTOS);
                    lblInfo.setText(p.nombre + "  —  $" + String.format("%.2f", p.precio) + "  —  Stock: " + p.stock);
                });

                panelBotones.add(btnMinus5);
                panelBotones.add(btnMinus);
                panelBotones.add(btnPlus);
                panelBotones.add(btnPlus5);

                fila.add(lblInfo, BorderLayout.CENTER);
                fila.add(panelBotones, BorderLayout.EAST);

                panel.add(fila);
            }

            panel.revalidate();
            panel.repaint();
        };

        construirFilas.run();

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnAgregar = new JButton("Agregar Producto");
        btnAgregar.addActionListener(e -> {
            JTextField nombre = new JTextField();
            JTextField precio = new JTextField();
            JTextField stock = new JTextField();

            Object[] campos = {
                    "Nombre:", nombre,
                    "Precio:", precio,
                    "Stock inicial:", stock
            };

            int op = JOptionPane.showConfirmDialog(ventana, campos, "Agregar Producto", JOptionPane.OK_CANCEL_OPTION);
            if (op == JOptionPane.OK_OPTION) {
                try {
                    String n = nombre.getText().trim();
                    double pr = Double.parseDouble(precio.getText());
                    int s = Integer.parseInt(stock.getText());
                    if (n.isEmpty() || pr < 0 || s < 0) throw new Exception();

                    Main.productos.add(new ProductoCafeteria(n, pr, s));
                    Persistencia.guardar(Main.productos, Main.ARCHIVO_PRODUCTOS);
                    construirFilas.run();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ventana, "Datos inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        botones.add(btnAgregar);

        JButton btnEliminar = new JButton("Eliminar Producto");
        btnEliminar.addActionListener(e -> {
            String[] nombres = Main.productos.stream().map(p -> p.nombre).toArray(String[]::new);

            if (nombres.length == 0) {
                JOptionPane.showMessageDialog(ventana, "No hay productos para eliminar.");
                return;
            }

            String seleccionado = (String) JOptionPane.showInputDialog(ventana, "Seleccione producto a eliminar:",
                    "Eliminar", JOptionPane.QUESTION_MESSAGE, null, nombres, nombres[0]);
            if (seleccionado == null) return;

            ProductoCafeteria remove = null;
            for (ProductoCafeteria p : Main.productos) {
                if (p.nombre.equals(seleccionado)) remove = p;
            }

            if (remove != null) {
                int ok = JOptionPane.showConfirmDialog(ventana, "¿Eliminar " + remove.nombre + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (ok == JOptionPane.YES_OPTION) {
                    Main.productos.remove(remove);
                    Persistencia.guardar(Main.productos, Main.ARCHIVO_PRODUCTOS);
                    construirFilas.run();
                }
            }
        });
        botones.add(btnEliminar);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> ventana.dispose());
        botones.add(btnCerrar);

        ventana.add(botones, BorderLayout.SOUTH);
        ventana.setVisible(true);
    }

}