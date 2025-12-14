import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VentanaCafeteria extends JFrame {

    private ArrayList<JSpinner> spinners = new ArrayList<>();
    private ArrayList<ProductoCafeteria> productosMostrados = new ArrayList<>();
    private JPanel panelProductos;
    private JLabel lblTotal;
    private String categoria;
    private JFrame padre;
    private Carrito carrito = new Carrito();
    private JPanel panelCategorias;


    public static void gestionarProductosEstatico() {
        VentanaCafeteria v = new VentanaCafeteria();
        v.setVisible(false);
        v.abrirGestionProductos();
    }

    public VentanaCafeteria() {
        setTitle("Menú Cafetería");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel pnlCategorias = new JPanel(new GridLayout(3, 2, 25, 25));
        pnlCategorias.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        agregarBotonCategoria(pnlCategorias, "Bebidas Calientes");
        agregarBotonCategoria(pnlCategorias, "Bebidas Frías");
        agregarBotonCategoria(pnlCategorias, "Repostería/Panadería");
        agregarBotonCategoria(pnlCategorias, "Snacks");
        agregarBotonCategoria(pnlCategorias, "Postres");
        agregarBotonCategoria(pnlCategorias, "Dulces");

        add(pnlCategorias, BorderLayout.CENTER);

        JButton btnVolver = new JButton("Volver al Menú Principal");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 22));
        btnVolver.addActionListener(e -> {
            dispose();
            new MenuCafeteria();
        });

        JPanel pnlSur = new JPanel();
        pnlSur.add(btnVolver);
        add(pnlSur, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void agregarBotonCategoria(JPanel panel, String nombreCategoria) {
        JButton btn = new JButton(nombreCategoria);
        btn.setFont(new Font("Arial", Font.BOLD, 22));
        btn.setBackground(new Color(178, 224, 216));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btn.setPreferredSize(new Dimension(150, 150));

        btn.addActionListener(e -> {
            // Abrimos nueva ventana de la categoría, pasando carrito actual
            new VentanaCafeteria(nombreCategoria, this, carrito);
            setVisible(false);
        });

        panel.add(btn);
    }


    public VentanaCafeteria(boolean soloGestion) {
        if (!soloGestion) return;
        setTitle("Gestionar Productos");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        // Este panel no se usa, pero evita null pointers
        panelProductos = new JPanel();
        lblTotal = new JLabel("");
        // 2) en el constructor (reemplaza la variable local por la de la clase)
        panelCategorias = new JPanel(new GridLayout(2, 4, 20, 20));
        panelCategorias.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panelCategorias, BorderLayout.CENTER);
        // NO cargamos productos aquí, porque abrirGestionProductos() lo hará }
    }

    public VentanaCafeteria(String categoria, JFrame padre, Carrito carrito) {
        this.categoria = categoria;
        this.padre = padre;
        this.carrito = carrito;
        setTitle("Productos - " + categoria);
        setSize(800, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        panelProductos = new JPanel();
        panelProductos.setLayout(new BoxLayout(panelProductos, BoxLayout.Y_AXIS));
        add(new JScrollPane(panelProductos), BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVolver = new JButton("Volver");
        JButton btnAgregarCarrito = new JButton("Agregar al Carrito");
        JButton btnVerCarrito = new JButton("Ver Carrito");
        JButton btnComprar = new JButton("Comprar");

        panelSur.add(btnVolver);
        panelSur.add(btnAgregarCarrito);
        panelSur.add(btnVerCarrito);
        panelSur.add(btnComprar);
        add(panelSur, BorderLayout.SOUTH);

        cargarProductos();

        btnVolver.addActionListener(e -> {
            if (padre != null) padre.setVisible(true);
            dispose();
        });

        btnAgregarCarrito.addActionListener(e -> {
            for (int i = 0; i < spinners.size(); i++) {
                int cantidad = (int) spinners.get(i).getValue();
                ProductoCafeteria p = productosMostrados.get(i);
                carrito.agregarProducto(p, cantidad);
            }
            JOptionPane.showMessageDialog(this, "Productos agregados al carrito.");
        });
        btnComprar.addActionListener(e -> {
            for (int i = 0; i < spinners.size(); i++) {
                int cantidad = (int) spinners.get(i).getValue();
                ProductoCafeteria p = productosMostrados.get(i);
                carrito.agregarProducto(p, cantidad); // carrito compartido actualizado
            }
            JOptionPane.showMessageDialog(this, "Productos agregados al carrito.", "Cafetería", JOptionPane.INFORMATION_MESSAGE);

            // Mostrar ventana de categorías y carrito
            if (padre != null) padre.setVisible(true);
            carrito.setVisible(true);
            dispose(); // cerrar ventana de productos
        });

        btnVerCarrito.addActionListener(e -> carrito.setVisible(true));

        setVisible(true);
    }


    private void cargarProductos() {
        panelProductos.removeAll();
        spinners.clear();
        productosMostrados.clear();

        for (ProductoCafeteria p : Main.productos) {
            if (!p.categoria.equalsIgnoreCase(categoria) || p.stock <= 0) continue;

            JPanel fila = new JPanel(new GridBagLayout());
            fila.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            GridBagConstraints c = new GridBagConstraints();
            c.gridy = 0;
            c.anchor = GridBagConstraints.WEST;

            c.gridx = 0;
            JLabel lblNombre = new JLabel(p.nombre);
            lblNombre.setFont(new Font("Arial", Font.BOLD, 20));
            lblNombre.setPreferredSize(new Dimension(200, 25));
            fila.add(lblNombre, c);

            c.gridx = 1;
            JLabel lblPrecio = new JLabel("$" + (int) p.precio);
            lblPrecio.setFont(new Font("Arial", Font.PLAIN, 20));
            lblPrecio.setPreferredSize(new Dimension(80, 25));
            fila.add(lblPrecio, c);

            c.gridx = 2;
            JLabel lblStock = new JLabel("Stock: " + p.stock);
            lblStock.setFont(new Font("Arial", Font.PLAIN, 20));
            lblStock.setPreferredSize(new Dimension(120, 25));
            fila.add(lblStock, c);

            c.gridx = 3;
            // Spinner inicializado en 0
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, p.stock, 1));
            spinner.setPreferredSize(new Dimension(60, 25));
            spinner.setFont(new Font("Arial", Font.PLAIN, 16));

            spinners.add(spinner);
            productosMostrados.add(p);
            fila.add(spinner, c);

            panelProductos.add(fila);
        }

        panelProductos.revalidate();
        panelProductos.repaint();
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
                JButton btnMinus5 = new JButton("-5");
                JButton btnMinus = new JButton("-");
                JButton btnPlus = new JButton("+");
                JButton btnPlus5 = new JButton("+5");
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
        ImageIcon iconAgregar = new ImageIcon(getClass().getResource("/imagen/agregar-archivo.png") // <-- tu icono aquí
        );
        JButton btnAgregar = new JButton("Agregar Producto", iconAgregar);
        btnAgregar.addActionListener(e -> {
            JTextField nombre = new JTextField();
            JTextField precio = new JTextField();
            JTextField stock = new JTextField();

            // Agregamos JComboBox para seleccionar categoría
            String[] categorias = {
                    "Bebidas Calientes"
                    , "Bebidas Frías"
                    , "Repostería/Panadería"
                    , "Snacks"
                    , "Postres"
                    , "Dulces"
            };
            JComboBox<String> comboCategoria = new JComboBox<>(categorias);
            Object[] campos = {
                    "Nombre:", nombre,
                    "Precio:", precio,
                    "Stock inicial:", stock,
                    "Categoría:", comboCategoria
            };
            int op = JOptionPane.showConfirmDialog(ventana, campos, "Agregar Producto", JOptionPane.OK_CANCEL_OPTION);
            if (op == JOptionPane.OK_OPTION) {
                try {
                    String n = nombre.getText().trim();
                    double pr = Double.parseDouble(precio.getText());
                    int s = Integer.parseInt(stock.getText());
                    String cat = (String) comboCategoria.getSelectedItem();
                    // nueva categoría
                    if (n.isEmpty() || pr < 0 || s < 0 || cat == null) throw new Exception();
                    // Crear producto con categoría
                    Main.productos.add(new ProductoCafeteria(n, pr, s, cat));
                    Persistencia.guardar(Main.productos, Main.ARCHIVO_PRODUCTOS);
                    construirFilas.run();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ventana, "Datos inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        botones.add(btnAgregar);
        // ==== BOTÓN ELIMINAR CON ICONO ====
        ImageIcon iconEliminar = new ImageIcon(getClass().getResource("/imagen/eliminar.png") // Asegúrate: carpeta 'imagen'
        );
        JButton btnEliminar = new JButton("Eliminar Producto", iconEliminar);
        btnEliminar.addActionListener(e -> {
            String[] nombres = Main.productos.stream().map(p -> p.nombre).toArray(String[]::new);
            if (nombres.length == 0) {
                JOptionPane.showMessageDialog(ventana, "No hay productos para eliminar.");
                return;
            }
            String seleccionado = (String) JOptionPane.showInputDialog(ventana, "Seleccione producto a eliminar:", "Eliminar", JOptionPane.QUESTION_MESSAGE, null, nombres, nombres[0]);
            if (seleccionado == null)
                return;
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
        // ==== BOTÓN CERRAR CON ICONO ====
        ImageIcon iconCerrar = new ImageIcon(getClass().getResource("/imagen/cancelar.png") // <-- tu icono aquí
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
