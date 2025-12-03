import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HotelGUI extends JFrame {

    public HotelGUI() {
        setTitle("APLICACION HOTEL LAS BRISAS");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5,1,10,10));

        JButton btnReserva = new JButton("Reservar Habitación");
        JButton btnCafeteria = new JButton("Comprar en Cafetería");
        JButton btnGestionarProductos = new JButton("Gestionar Productos");
        btnGestionarProductos.addActionListener(e -> {
            String password = JOptionPane.showInputDialog(this, "Ingrese contraseña del recepcionista:");
            if (!"marco123".equals(password)) {
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta.");
                return;
            }
            gestionarProductos();
        });
        add(btnGestionarProductos);
        JButton btnVentas = new JButton("Ver Ventas");
        JButton btnSalir = new JButton("Salir");
        JButton btnGestionarHabitaciones = new JButton("Gestionar Habitaciones");
        btnGestionarHabitaciones.addActionListener(e -> {
            String password = JOptionPane.showInputDialog(this, "Ingrese contraseña del recepcionista:");
            if (!"marco123".equals(password)) {
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta.");
                return;
            }

            gestionarHabitaciones();
        });
        add(btnGestionarHabitaciones);
        add(btnReserva);
        add(btnCafeteria);
        add(btnVentas);
        add(btnSalir);

        // Eventos
        btnReserva.addActionListener(e -> new VentanaReserva());
        btnCafeteria.addActionListener(e -> new VentanaCafeteria());
        btnVentas.addActionListener(e -> verVentas());
        btnSalir.addActionListener(e -> System.exit(0));

        setVisible(true);
    }
    private void gestionarHabitaciones() {
        JFrame ventana = new JFrame("Gestión de Habitaciones");
        ventana.setSize(600, 400);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(new BorderLayout());

        // Tabla de habitaciones
        String[] columnas = {"Número", "Tipo", "Precio", "Disponible"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // solo lectura
            }
        };
        JTable tabla = new JTable(modelo);

        Runnable cargarTabla = () -> {
            modelo.setRowCount(0);
            for (Habitacion h : Main.habitaciones) {
                Object[] fila = {h.numero, h.tipo, h.precio, h.disponible ? "Sí" : "No"};
                modelo.addRow(fila);
            }
        };
        cargarTabla.run();

        ventana.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();

        JButton btnAgregar = new JButton("Agregar");
        JButton btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        ventana.add(panelBotones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> {
            JTextField numero = new JTextField();
            String[] tipos = {"Sencilla", "Doble", "Suite"};
            JComboBox<String> tipo = new JComboBox<>(tipos);
            JTextField precio = new JTextField();

            Object[] campos = {
                    "Número:", numero,
                    "Tipo:", tipo,
                    "Precio:", precio
            };

            int opcion = JOptionPane.showConfirmDialog(ventana, campos, "Agregar Habitación", JOptionPane.OK_CANCEL_OPTION);
            if (opcion == JOptionPane.OK_OPTION) {
                try {
                    int n = Integer.parseInt(numero.getText());

                    boolean existe = Main.habitaciones.stream().anyMatch(h -> h.numero == n);
                    if (existe) {
                        JOptionPane.showMessageDialog(ventana, "Ya existe una habitación con ese número.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String t = (String) tipo.getSelectedItem();
                    double p = Double.parseDouble(precio.getText());

                    Main.habitaciones.add(new Habitacion(n, t, p));
                    Persistencia.guardar(Main.habitaciones, Main.ARCHIVO_HABITACIONES);
                    cargarTabla.run(); // actualizar tabla
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ventana, "Datos incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                Habitacion h = Main.habitaciones.get(fila);

                if (!h.disponible) {
                    JOptionPane.showMessageDialog(ventana, "No se puede eliminar una habitación ocupada.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(ventana, "¿Eliminar habitación " + h.numero + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Main.habitaciones.remove(h);
                    Persistencia.guardar(Main.habitaciones, Main.ARCHIVO_HABITACIONES);
                    cargarTabla.run();
                }
            } else {
                JOptionPane.showMessageDialog(ventana, "Seleccione una habitación para eliminar.", "Atención", JOptionPane.WARNING_MESSAGE);
            }
        });

        ventana.setVisible(true);
    }


    private void gestionarProductos() {
        JFrame ventana = new JFrame("Gestionar Productos");
        ventana.setSize(600, 400);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(new BorderLayout());

        // Tabla de productos
        String[] columnas = {"Nombre", "Precio", "Stock"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // solo editable mediante botones
            }
        };
        JTable tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        ventana.add(scroll, BorderLayout.CENTER);

        Runnable cargarTabla = () -> {
            modelo.setRowCount(0);
            for (ProductoCafeteria p : Main.productos) {
                modelo.addRow(new Object[]{p.nombre, p.precio, p.stock});
            }
        };
        cargarTabla.run();

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());

        // Botón Agregar
        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(e -> {
            JTextField nombre = new JTextField();
            JTextField precio = new JTextField();
            JTextField stock = new JTextField();

            Object[] campos = {
                    "Nombre:", nombre,
                    "Precio:", precio,
                    "Stock:", stock
            };

            int opcion = JOptionPane.showConfirmDialog(ventana, campos, "Agregar Producto", JOptionPane.OK_CANCEL_OPTION);
            if (opcion == JOptionPane.OK_OPTION) {
                try {
                    String n = nombre.getText();
                    double p = Double.parseDouble(precio.getText());
                    int s = Integer.parseInt(stock.getText());
                    if (s < 0 || p < 0) throw new NumberFormatException();

                    Main.productos.add(new ProductoCafeteria(n, p, s));
                    Persistencia.guardar(Main.productos, Main.ARCHIVO_PRODUCTOS);
                    cargarTabla.run();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ventana, "Datos incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelBotones.add(btnAgregar);

        // Botón Editar
        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                ProductoCafeteria p = Main.productos.get(fila);

                JTextField stockField = new JTextField(String.valueOf(p.stock));
                stockField.setColumns(5);

                Object[] campos = {
                        "Nombre:", new JLabel(p.nombre),
                        "Precio:", new JLabel(String.valueOf(p.precio)),
                        "Stock:", stockField
                };

                int opcion = JOptionPane.showConfirmDialog(ventana, campos, "Editar Producto", JOptionPane.OK_CANCEL_OPTION);
                if (opcion == JOptionPane.OK_OPTION) {
                    try {
                        int nuevoStock = Integer.parseInt(stockField.getText());
                        if (nuevoStock < 0) {
                            JOptionPane.showMessageDialog(ventana, "El stock debe ser positivo.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        p.stock = nuevoStock;
                        Persistencia.guardar(Main.productos, Main.ARCHIVO_PRODUCTOS);
                        cargarTabla.run();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(ventana, "Stock inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            } else {
                JOptionPane.showMessageDialog(ventana, "Seleccione un producto para editar.", "Atención", JOptionPane.WARNING_MESSAGE);
            }
        });
        panelBotones.add(btnEditar);

        // Botón Eliminar
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                ProductoCafeteria p = Main.productos.get(fila);
                int confirm = JOptionPane.showConfirmDialog(ventana,
                        "¿Eliminar " + p.nombre + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Main.productos.remove(fila);
                    Persistencia.guardar(Main.productos, Main.ARCHIVO_PRODUCTOS);
                    cargarTabla.run();
                }
            } else {
                JOptionPane.showMessageDialog(ventana, "Seleccione un producto para eliminar.", "Atención", JOptionPane.WARNING_MESSAGE);
            }
        });
        panelBotones.add(btnEliminar);

        ventana.add(panelBotones, BorderLayout.SOUTH);

        ventana.setVisible(true);
    }


    private void verVentas() {
        String password = JOptionPane.showInputDialog(this, "Ingrese contraseña del vendedor:");
        if (!"marco123".equals(password)) {
            JOptionPane.showMessageDialog(this, "Contraseña incorrecta.");
            return;
        }
        new VentanaVentas();
    }


}
