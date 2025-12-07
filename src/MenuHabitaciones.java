import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

class MenuHabitaciones extends JFrame {

    public MenuHabitaciones() {
        setTitle("Menú Habitaciones");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JButton btnReservar = new JButton("Reservar Habitación");
        JButton btnGestionar = new JButton("Gestionar Habitaciones");
        JButton btnVentas = new JButton("Ver Ventas");
        JButton btnMenu = new JButton("Volver al Menú Principal");
        JButton btnSalir = new JButton("Salir");

        add(btnReservar);
        add(btnGestionar);
        add(btnVentas);
        add(btnMenu);
        add(btnSalir);

        // Eventos
        btnReservar.addActionListener(e -> new VentanaReserva());

        btnGestionar.addActionListener(e -> {
            String password = JOptionPane.showInputDialog(this, "Ingrese contraseña del recepcionista:");
            if (!"marco123".equals(password)) {
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta.");
                return;
            }
            new MenuHabitaciones().gestionarHabitaciones(); //Necesita que hagas pública la función
        });

        btnVentas.addActionListener(e -> {
            String password = JOptionPane.showInputDialog(this, "Ingrese contraseña del recepcionista:");
            if (!"marco123".equals(password)) {
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta.");
                return;
            }
            new VentanaVentas();
        });

        btnMenu.addActionListener(e -> {
            dispose();
            new HotelGUI();
        });

        btnSalir.addActionListener(e -> System.exit(0));

        setVisible(true);
    }
    public void gestionarHabitaciones() {
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
}
