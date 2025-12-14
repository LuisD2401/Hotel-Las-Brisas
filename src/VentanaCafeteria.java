import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class VentanaCafeteria extends JFrame {

    private ArrayList<JSpinner> spinners = new ArrayList<>();
    private ArrayList<ProductoCafeteria> productosMostrados = new ArrayList<>();
    private JPanel panelProductos;
    private JLabel lblTotal;

    public static void gestionarProductosEstatico() {
        new VentanaCafeteria(true);
    }
    public VentanaCafeteria(boolean soloGestion) {
        if (!soloGestion) return;

        setTitle("Gestionar Productos");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        abrirGestionProductos();
    }


    public VentanaCafeteria() {

        setTitle("Venta Cafeteria");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===============================
        // COLOR DE FONDO DE LA VENTANA
        // ===============================
        getContentPane().setBackground(new Color(220, 245, 250)); // Fondo de JFrame

        // ===============================
        // PANEL DE PRODUCTOS
        // ===============================
        panelProductos = new JPanel();
        panelProductos.setLayout(new BoxLayout(panelProductos, BoxLayout.Y_AXIS));
        panelProductos.setBackground(new Color(220, 245, 250)); // Fondo del panel

        JScrollPane scroll = new JScrollPane(panelProductos);
        scroll.getViewport().setBackground(new Color(220, 245, 250)); // Fondo del scroll
        add(scroll, BorderLayout.CENTER);

        // ===============================
        // LABEL TOTAL
        // ===============================
        lblTotal = new JLabel("Total: $0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 22));
        lblTotal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblTotal.setOpaque(true); // necesario para que se vea el color si quieres cambiarlo
        lblTotal.setBackground(new Color(220, 245, 250)); // mismo color que fondo
        add(lblTotal, BorderLayout.NORTH);

        // ===============================
        // PANEL SUR BOTONES
        // ===============================
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSur.setBackground(new Color(220, 245, 250)); // Fondo del panel de botones

        JButton btnFinalizar = new JButton("Finalizar Compra");
        btnFinalizar.setFont(new Font("Arial", Font.BOLD, 22));
        panelSur.add(btnFinalizar);
        add(panelSur, BorderLayout.SOUTH);

        // ===============================
        // FUNCIONALIDAD
        // ===============================
        cargarProductos();   // 👈 MUY IMPORTANTE
        actualizarTotal();   // 👈

        for (JSpinner sp : spinners) {
            sp.addChangeListener(e -> actualizarTotal());
        }

        btnFinalizar.addActionListener(e -> procesarCompra());

        setVisible(true);
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
        ventana.setSize(1000, 700);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(new BorderLayout());

        // Cambiar el fondo del JFrame
        ventana.getContentPane().setBackground(new Color(220, 245, 250));

        // Panel principal donde van las filas
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(220, 245, 250)); // <- fondo del panel
        JScrollPane scroll = new JScrollPane(panel);
        scroll.getViewport().setBackground(new Color(220, 245, 250)); // <- fondo del scroll
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

        // Panel inferior con botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setBackground(new Color(220, 245, 250));

        ventana.add(botones, BorderLayout.SOUTH);
        ventana.setVisible(true);
    // ==== BOTÓN AGREGAR CON ICONO ====
        ImageIcon iconAgregar = new ImageIcon(getClass().getResource("/imagen/agregar-archivo.png") // <-- tu icono aquí
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

                    // Crear producto SIN categoría
                    Main.productos.add(new ProductoCafeteria(n, pr, s));
                    Persistencia.guardar(Main.productos, Main.ARCHIVO_PRODUCTOS);
                    construirFilas.run();  // Actualiza la lista de productos
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
    private void cargarProductos() {
        panelProductos.removeAll();
        spinners.clear();
        productosMostrados.clear();

        Color colorFondo = new Color(220, 245, 250);

        for (ProductoCafeteria p : Main.productos) {
            if (p.stock <= 0) continue;  // Omitir productos sin stock

            JPanel fila = new JPanel(new GridBagLayout());
            fila.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            fila.setBackground(colorFondo); // ✅ Color de fondo de cada fila
            GridBagConstraints c = new GridBagConstraints();
            c.gridy = 0;
            c.anchor = GridBagConstraints.WEST;

            // Nombre del producto
            c.gridx = 0;
            JLabel lblNombre = new JLabel(p.nombre);
            lblNombre.setFont(new Font("Arial", Font.BOLD, 20));
            lblNombre.setPreferredSize(new Dimension(200, 25));
            fila.add(lblNombre, c);

            // Precio
            c.gridx = 1;
            JLabel lblPrecio = new JLabel("$" + (int)p.precio);
            lblPrecio.setFont(new Font("Arial", Font.PLAIN, 20));
            lblPrecio.setPreferredSize(new Dimension(80, 25));
            fila.add(lblPrecio, c);

            // Stock
            c.gridx = 2;
            JLabel lblStock = new JLabel("Stock: " + p.stock);
            lblStock.setFont(new Font("Arial", Font.PLAIN, 20));
            lblStock.setPreferredSize(new Dimension(120, 25));
            fila.add(lblStock, c);

            // Cantidad a comprar
            c.gridx = 3;
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, p.stock, 1));
            spinner.setPreferredSize(new Dimension(60, 25));
            spinner.setFont(new Font("Arial", Font.PLAIN, 16));
            spinner.addChangeListener(e -> actualizarTotal());
            spinners.add(spinner);
            productosMostrados.add(p);
            fila.add(spinner, c);

            panelProductos.add(fila);
        }

        panelProductos.revalidate();
        panelProductos.repaint();
    }
    private void procesarCompra() {

        double total = 0;
        StringBuilder detalle = new StringBuilder();

        // Fecha y hora
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        detalle.append("──────── HOTEL LAS BRISAS ────────\n");
        detalle.append("            CAFETERÍA\n\n");
        detalle.append("Fecha: ").append(formato.format(ahora)).append("\n\n");
        detalle.append("---------------------------------\n");

        // Productos comprados
        for (int i = 0; i < spinners.size(); i++) {
            int cantidad = (int) spinners.get(i).getValue();
            ProductoCafeteria p = productosMostrados.get(i);

            if (cantidad > 0) {
                double subtotal = cantidad * p.precio;
                total += subtotal;

                detalle.append(String.format(
                        "%-15s x%-2d $%7.0f\n",
                        p.nombre,
                        cantidad,
                        subtotal
                ));
            }
        }

        if (total == 0) {
            JOptionPane.showMessageDialog(this, "No seleccionó productos.");
            return;
        }

        detalle.append("---------------------------------\n");
        detalle.append(String.format("TOTAL:              $%7.0f\n\n", total));

        // Método de pago
        String[] opciones = {"Efectivo", "Débito"};
        int pago = JOptionPane.showOptionDialog(
                this,
                "Seleccione método de pago",
                "Pago",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (pago == -1) return;

        if (pago == 0) { // EFECTIVO
            String montoStr = JOptionPane.showInputDialog(this, "¿Con cuánto paga?");
            try {
                double monto = Double.parseDouble(montoStr);
                if (monto < total) {
                    JOptionPane.showMessageDialog(this, "Monto insuficiente.");
                    return;
                }

                double vuelto = monto - total;
                detalle.append("Pago: Efectivo\n");
                detalle.append(String.format("Recibido:           $%7.0f\n", monto));
                detalle.append(String.format("Vuelto:             $%7.0f\n", vuelto));

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Monto inválido.");
                return;
            }
        } else { // DÉBITO
            detalle.append("Pago: Débito\n");
            detalle.append("Recibido:           $").append(String.format("%.0f", total)).append("\n");
            detalle.append("Vuelto:             $0\n");
        }

        detalle.append("\n¡Gracias por su compra!\n");
        detalle.append("---------------------------------");

        // Descontar stock
        for (int i = 0; i < spinners.size(); i++) {
            int cantidad = (int) spinners.get(i).getValue();
            ProductoCafeteria p = productosMostrados.get(i);
            if (cantidad > 0) {
                p.stock -= cantidad;
            }
        }

        Persistencia.guardar(Main.productos, Main.ARCHIVO_PRODUCTOS);

        // Mostrar boleta
        JTextArea area = new JTextArea(detalle.toString());
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setEditable(false);

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(area),
                "Boleta de Venta",
                JOptionPane.INFORMATION_MESSAGE
        );
        // ===== REGISTRAR VENTA =====
        ArrayList<ProductoCafeteria> vendidos = new ArrayList<>();
        ArrayList<Integer> cantidadesVendidas = new ArrayList<>();

        for (int i = 0; i < spinners.size(); i++) {
            int cant = (int) spinners.get(i).getValue();
            if (cant > 0) {
                vendidos.add(productosMostrados.get(i));
                cantidadesVendidas.add(cant);
            }
        }

        Venta venta = new Venta(
                total,
                "Cafetería",
                pago == 0 ? "Efectivo" : "Débito",
                "Boleta",
                vendidos,          // solo los productos comprados
                cantidadesVendidas // solo cantidades correspondientes
        );


        Main.ventas.add(venta);
        Persistencia.guardar(Main.ventas, Main.ARCHIVO_VENTAS);


        dispose();
    }



}