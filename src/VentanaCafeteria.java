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
        v.setVisible(false);
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

        // === ICONO PARA EL BOTÓN COMPRAR ===
        JButton btnComprar = new JButton(
                "Comprar",
                UIManager.getIcon("OptionPane.informationIcon")
        );
        btnComprar.setFont(new Font("Arial", Font.BOLD, 16));
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

            JPanel fila = new JPanel(new GridBagLayout());
            fila.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            GridBagConstraints c = new GridBagConstraints();
            c.gridy = 0;
            c.anchor = GridBagConstraints.WEST;

            // === NOMBRE CON ÍCONO DE PRODUCTO ===
            c.gridx = 0;
            JLabel lblNombre = new JLabel(p.nombre);

            lblNombre.setFont(new Font("Arial", Font.BOLD, 20));
            lblNombre.setPreferredSize(new Dimension(200, 25));
            fila.add(lblNombre, c);

            // PRECIO
            c.gridx = 1;
            JLabel lblPrecio = new JLabel("$" + (int)p.precio);
            lblPrecio.setFont(new Font("Arial", Font.PLAIN, 20));
            lblPrecio.setPreferredSize(new Dimension(80, 25));
            fila.add(lblPrecio, c);

            // STOCK
            c.gridx = 2;
            JLabel lblStock = new JLabel("Stock: " + p.stock);
            lblStock.setFont(new Font("Arial", Font.PLAIN, 20));
            lblStock.setPreferredSize(new Dimension(120, 25));
            fila.add(lblStock, c);

            // SPINNER
            c.gridx = 3;
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, p.stock, 1));
            spinner.setPreferredSize(new Dimension(60, 25));
            spinner.setFont(new Font("Arial", Font.PLAIN, 16));

            spinners.add(spinner);
            productosMostrados.add(p);

            spinner.addChangeListener(e -> actualizarTotal());

            fila.add(spinner, c);

            panelProductos.add(fila);
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
        ventana.setSize(900, 600);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(panel);
        ventana.add(scroll, BorderLayout.CENTER);

        Runnable construirFilas = () -> {
            panel.removeAll();

            for (ProductoCafeteria p : Main.productos) {

                JPanel fila = new JPanel(new GridBagLayout());
                fila.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                GridBagConstraints c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                c.anchor = GridBagConstraints.WEST;

                // NOMBRE
                JLabel lblNombre = new JLabel(p.nombre);
                lblNombre.setFont(new Font("Arial", Font.BOLD, 22));
                lblNombre.setPreferredSize(new Dimension(200, 25));
                fila.add(lblNombre, c);

                // PRECIO
                c.gridx++;
                JLabel lblPrecio = new JLabel("$" + String.format("%.0f", p.precio));
                lblPrecio.setFont(new Font("Arial", Font.PLAIN, 22));
                lblPrecio.setPreferredSize(new Dimension(100, 25));
                fila.add(lblPrecio, c);

                // STOCK
                c.gridx++;
                JLabel lblStock = new JLabel("Stock: " + p.stock);
                lblStock.setFont(new Font("Arial", Font.PLAIN, 22));
                lblStock.setPreferredSize(new Dimension(120, 25));
                fila.add(lblStock, c);

                // BOTONERA CON ICONOS
                c.gridx++;
                JPanel botonera = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

                JButton btnMinus5 = new JButton("-5", UIManager.getIcon("Tree.closedIcon"));
                JButton btnMinus = new JButton("-", UIManager.getIcon("Tree.closedIcon"));
                JButton btnPlus = new JButton("+", UIManager.getIcon("Tree.openIcon"));
                JButton btnPlus5 = new JButton("+5", UIManager.getIcon("Tree.openIcon"));

                botonera.add(btnMinus5);
                botonera.add(btnMinus);
                botonera.add(btnPlus);
                botonera.add(btnPlus5);

                fila.add(botonera, c);

                // ACCIONES
                btnPlus.addActionListener(ev -> {
                    p.stock++;
                    Persistencia.guardar(Main.productos, Main.ARCHIVO_PRODUCTOS);
                    lblStock.setText("Stock: " + p.stock);
                });

                btnMinus.addActionListener(ev -> {
                    if (p.stock > 0) p.stock--;
                    Persistencia.guardar(Main.productos, Main.ARCHIVO_PRODUCTOS);
                    lblStock.setText("Stock: " + p.stock);
                });

                btnPlus5.addActionListener(ev -> {
                    p.stock += 5;
                    Persistencia.guardar(Main.productos, Main.ARCHIVO_PRODUCTOS);
                    lblStock.setText("Stock: " + p.stock);
                });

                btnMinus5.addActionListener(ev -> {
                    p.stock = Math.max(0, p.stock - 5);
                    Persistencia.guardar(Main.productos, Main.ARCHIVO_PRODUCTOS);
                    lblStock.setText("Stock: " + p.stock);
                });

                panel.add(fila);
            }

            panel.revalidate();
            panel.repaint();
        };

        construirFilas.run();

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // ==== BOTÓN AGREGAR CON ICONO ====
        ImageIcon iconAgregar = new ImageIcon(
                getClass().getResource("/imagen/agregar-archivo.png") // <-- tu icono aquí
        );

        JButton btnAgregar = new JButton("Agregar Producto", iconAgregar);


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

        // ==== BOTÓN ELIMINAR CON ICONO ====
        ImageIcon iconEliminar = new ImageIcon(
                getClass().getResource("/imagen/eliminar.png") // Asegúrate: carpeta 'imagen'
        );

        JButton btnEliminar = new JButton("Eliminar Producto", iconEliminar);


        btnEliminar.addActionListener(e -> {
            String[] nombres = Main.productos.stream().map(p -> p.nombre).toArray(String[]::new);

            if (nombres.length == 0) {
                JOptionPane.showMessageDialog(ventana, "No hay productos para eliminar.");
                return;
            }

            String seleccionado = (String) JOptionPane.showInputDialog(
                    ventana, "Seleccione producto a eliminar:",
                    "Eliminar", JOptionPane.QUESTION_MESSAGE,
                    null, nombres, nombres[0]
            );
            if (seleccionado == null) return;

            ProductoCafeteria remove = null;
            for (ProductoCafeteria p : Main.productos) {
                if (p.nombre.equals(seleccionado)) remove = p;
            }

            if (remove != null) {
                int ok = JOptionPane.showConfirmDialog(ventana,
                        "¿Eliminar " + remove.nombre + "?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION);

                if (ok == JOptionPane.YES_OPTION) {
                    Main.productos.remove(remove);
                    Persistencia.guardar(Main.productos, Main.ARCHIVO_PRODUCTOS);
                    construirFilas.run();
                }
            }
        });

        botones.add(btnEliminar);

        // ==== BOTÓN CERRAR CON ICONO ====
        ImageIcon iconCerrar = new ImageIcon(
                getClass().getResource("/imagen/cancelar.png") // <-- tu icono aquí
        );

        JButton btnCerrar = new JButton("Cerrar", iconCerrar);
        btnCerrar.addActionListener(e -> ventana.dispose());

        botones.add(btnCerrar);

        ventana.add(botones, BorderLayout.SOUTH);
        ventana.setVisible(true);
    }
    // usa getResource (empaquetado en JAR)
    private ImageIcon cargarIcono(String resourcePath, int w, int h) {
        try {
            java.net.URL url = getClass().getResource(resourcePath);
            if (url == null) return null;
            ImageIcon original = new ImageIcon(url);
            Image img = original.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            return null;
        }
    }

    // alternativa por archivo (sólo durante desarrollo, evita en JAR)
    private ImageIcon cargarIconoArchivo(String path, int w, int h) {
        try {
            ImageIcon original = new ImageIcon(path);
            if (original.getIconWidth() == -1) return null;
            Image img = original.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            return null;
        }
    }


}
