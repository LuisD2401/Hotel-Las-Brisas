import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MenuHabitaciones extends JFrame {

    public MenuHabitaciones() {
        setTitle("Menú Habitaciones");
        setSize(1500, 1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===============================
        //      PANEL SUPERIOR
        // ===============================
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(178, 224, 216));
        panelSuperior.setPreferredSize(new Dimension(1500, 150));
        panelSuperior.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        try {
            ImageIcon icono = new ImageIcon(getClass().getResource("/imagen/habitacion.png"));
            Image img = icono.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            panelSuperior.add(new JLabel(new ImageIcon(img)));
        } catch (Exception e) {
            System.out.println("⚠ Icono habitacion.png no encontrado");
        }

        JLabel lblTitulo = new JLabel("Habitaciones Hotel Las Brisas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 50));
        panelSuperior.add(lblTitulo);

        add(panelSuperior, BorderLayout.NORTH);

        // ===============================
        //      PANEL CENTRAL DEGRADADO
        // ===============================
        JPanel panel = crearPanelDegradado(
                new Color(0, 255, 255),
                new Color(0, 150, 255)
        );

        panel.setLayout(new GridLayout(5, 1, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(60, 200, 60, 200));

        JButton btnReservar = crearBoton("Hacer Reserva", "habitacion.png");
        JButton btnGestionar = crearBoton("Gestionar Habitaciones Y Reservas", "listadoPago.png");
        JButton btnVentas = crearBoton("Ver Reservas", "recoleccion-de-datos.png");
        JButton btnMenu = crearBoton("Volver al Menú Principal", "volver.png");
        JButton btnSalir = crearBoton("Salir", "cancelar.png");

        panel.add(btnReservar);
        panel.add(btnGestionar);
        panel.add(btnVentas);
        panel.add(btnMenu);
        panel.add(btnSalir);

        add(panel, BorderLayout.CENTER);

        // ===============================
        //            ACCIONES
        // ===============================
        btnReservar.addActionListener(e -> {
            dispose();
            new VentanaReserva();
        });

        btnGestionar.addActionListener(e -> {
            String password = JOptionPane.showInputDialog(this, "Ingrese contraseña del recepcionista:");
            if (!"123".equals(password)) {
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta.");
                return;
            }
            gestionarHabitaciones();
        });


        btnVentas.addActionListener(e -> {
            String password = JOptionPane.showInputDialog(this, "Ingrese contraseña del recepcionista:");
            if (!"123".equals(password)) {
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta.");
                return;
            }
            new VentanaVentasH();
        });

        btnMenu.addActionListener(e -> {
            dispose();
            new HotelGUI();
        });

        btnSalir.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    // ===============================
    //  PANEL DEGRADADO
    // ===============================
    private JPanel crearPanelDegradado(Color c1, Color c2) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, c1, 0, getHeight(), c2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }

    // ===============================
    //  BOTONES GRANDES
    // ===============================
    private JButton crearBoton(String texto, String iconoNombre) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 26));

        try {
            ImageIcon img = new ImageIcon(getClass().getResource("/imagen/" + iconoNombre));
            Image esc = img.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(esc));
        } catch (Exception e) {
            System.out.println("⚠ Icono no encontrado: " + iconoNombre);
        }

        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(20);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        return btn;
    }

    // ===============================
    //  GESTIÓN (SIN CAMBIOS)
    // ===============================
    public void gestionarHabitaciones() {

        JFrame ventana = new JFrame("Gestión de Habitaciones");
        ventana.setSize(1000, 700);
        ventana.setLocationRelativeTo(this);
        ventana.setLayout(new BorderLayout());
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Color colorFondo = new Color(220, 245, 250);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(220, 245, 250));

        // ===============================
        // TABLA DE HABITACIONES
        // ===============================
        String[] columnas = {"Número", "Tipo", "Precio", "Disponible"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(28); // 30 píxeles de alto, puedes aumentar a 40 o más
        tabla.setFont(new Font("Arial", Font.PLAIN, 18));
        tabla.setBackground(colorFondo);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(650, 200));
        scroll.getViewport().setBackground(colorFondo);

        Runnable cargarTabla = () -> {
            modelo.setRowCount(0);
            for (Habitacion h : Main.habitaciones) {
                modelo.addRow(new Object[]{
                        h.numero,
                        h.tipo,
                        h.precio,
                        h.disponible ? "Sí" : "No"
                });
            }
        };

        cargarTabla.run();

        ventana.add(scroll, BorderLayout.CENTER);

        // ===============================
        // PANEL DE BOTONES
        // ===============================
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(colorFondo);
        // Ajustar el tamaño de los botones
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnAnular = new JButton("Anular Reserva");
        JButton btnCerrar = new JButton("Cerrar");

        // Ajustar tamaño y fuente de los botones
        Dimension botonDimension = new Dimension(150, 40);  // Tamaño preferido de los botones
        btnAgregar.setPreferredSize(botonDimension);
        btnEliminar.setPreferredSize(botonDimension);
        btnAnular.setPreferredSize(botonDimension);
        btnCerrar.setPreferredSize(botonDimension);

        btnAgregar.setFont(new Font("Arial", Font.BOLD, 14));
        btnEliminar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAnular.setFont(new Font("Arial", Font.BOLD, 14));
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 14));

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnAnular);
        panelBotones.add(btnCerrar);

        ventana.add(panelBotones, BorderLayout.SOUTH);
        ventana.getContentPane().setBackground(colorFondo);
        // ===============================
        // ACCIONES
        // ===============================

        btnAnular.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(
                        ventana,
                        "Seleccione una habitación",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            Habitacion h = Main.habitaciones.get(fila);

            // Buscar reserva activa asociada a la habitación
            VentaHabitacion ventaActiva = Main.ventasHabitaciones.stream()
                    .filter(v -> !v.anulada && v.habitacion.numero == h.numero)
                    .findFirst()
                    .orElse(null);

            if (ventaActiva == null) {
                JOptionPane.showMessageDialog(ventana,
                        "La habitación no tiene una reserva activa",
                        "Aviso",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int conf = JOptionPane.showConfirmDialog(
                    ventana,
                    "¿Anular la reserva?\n\n" + ventaActiva.resumen(),
                    "Confirmar anulación",
                    JOptionPane.YES_NO_OPTION
            );

            if (conf != JOptionPane.YES_OPTION) return;

            ventaActiva.anular();

            // Guardar cambios
            Persistencia.guardar(Main.habitaciones, Main.ARCHIVO_HABITACIONES);
            Persistencia.guardar(
                    Main.ventasHabitaciones,
                    Main.ARCHIVO_VENTAS_HABITACIONES
            );

            JOptionPane.showMessageDialog(
                    ventana,
                    "Reserva anulada correctamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarTabla.run();
        });

        btnAgregar.addActionListener(e -> {
            JTextField txtNumero = new JTextField();
            JTextField txtPrecio = new JTextField();
            JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Sencilla", "Doble", "Suite"});

            Object[] campos = {
                    "Número:", txtNumero,
                    "Tipo:", comboTipo,
                    "Precio:", txtPrecio
            };

            int op = JOptionPane.showConfirmDialog(
                    ventana, campos, "Agregar Habitación",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (op == JOptionPane.OK_OPTION) {
                try {
                    int numero = Integer.parseInt(txtNumero.getText());
                    double precio = Double.parseDouble(txtPrecio.getText());
                    String tipo = (String) comboTipo.getSelectedItem();

                    boolean existe = Main.habitaciones.stream()
                            .anyMatch(h -> h.numero == numero);

                    if (existe) {
                        JOptionPane.showMessageDialog(ventana,
                                "Ya existe una habitación con ese número.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Main.habitaciones.add(new Habitacion(numero, tipo, precio));
                    Persistencia.guardar(Main.habitaciones, Main.ARCHIVO_HABITACIONES);
                    cargarTabla.run();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ventana,
                            "Datos inválidos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(ventana,
                        "Seleccione una habitación.",
                        "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Habitacion h = Main.habitaciones.get(fila);

            if (!h.disponible) {
                JOptionPane.showMessageDialog(ventana,
                        "No se puede eliminar una habitación ocupada.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int conf = JOptionPane.showConfirmDialog(
                    ventana,
                    "¿Eliminar habitación " + h.numero + "?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );

            if (conf == JOptionPane.YES_OPTION) {
                Main.habitaciones.remove(h);
                Persistencia.guardar(Main.habitaciones, Main.ARCHIVO_HABITACIONES);
                cargarTabla.run();
            }
        });

        btnCerrar.addActionListener(e -> ventana.dispose());

        ventana.setVisible(true);
    }


}

