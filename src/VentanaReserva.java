import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaReserva extends JFrame {

    private JComboBox<String> comboHabitaciones;
    private JTextArea txtDescripcion;

    public VentanaReserva() {
        setTitle("Reservar Habitación");
        setSize(750, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(220, 245, 250));

        // ===============================
        // FORMULARIO
        // ===============================
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setOpaque(false);
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos de la Reserva"));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 15, 10, 15);
        c.anchor = GridBagConstraints.WEST;

        JTextField txtRut = new JTextField(14);
        JTextField txtNombres = new JTextField(14);
        JTextField txtApellidos = new JTextField(14);
        JTextField txtTelefono = new JTextField("+56 ");
        JTextField txtDias = new JTextField(5);

        comboHabitaciones = new JComboBox<>();
        actualizarHabitaciones();

        txtDescripcion = new JTextArea(8, 28);
        txtDescripcion.setEditable(false);
        txtDescripcion.setBorder(
                BorderFactory.createTitledBorder("Características de la Habitación")
        );

        int y = 0;

        c.gridx = 0; c.gridy = y;
        panelForm.add(new JLabel("RUT:"), c);
        c.gridx = 1;
        panelForm.add(txtRut, c); y++;

        c.gridx = 0; c.gridy = y;
        panelForm.add(new JLabel("Nombres:"), c);
        c.gridx = 1;
        panelForm.add(txtNombres, c); y++;

        c.gridx = 0; c.gridy = y;
        panelForm.add(new JLabel("Apellidos:"), c);
        c.gridx = 1;
        panelForm.add(txtApellidos, c); y++;

        c.gridx = 0; c.gridy = y;
        panelForm.add(new JLabel("Teléfono:"), c);
        c.gridx = 1;
        panelForm.add(txtTelefono, c); y++;

        c.gridx = 0; c.gridy = y;
        panelForm.add(new JLabel("Días de estadía:"), c);
        c.gridx = 1;
        panelForm.add(txtDias, c); y++;

        c.gridx = 0; c.gridy = y;
        panelForm.add(new JLabel("Habitación:"), c);
        c.gridx = 1;
        panelForm.add(comboHabitaciones, c); y++;

        c.gridx = 0; c.gridy = y;
        c.gridwidth = 2;
        panelForm.add(txtDescripcion, c);

        add(panelForm, BorderLayout.CENTER);

        // ===============================
        // BOTONES
        // ===============================
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVolver = new JButton("Volver al Menú");
        JButton btnReservar = new JButton("Reservar");

        panelBotones.add(btnVolver);
        panelBotones.add(btnReservar);
        add(panelBotones, BorderLayout.SOUTH);

        // ===============================
        // ACCIONES
        // ===============================
        comboHabitaciones.addActionListener(e -> actualizarDescripcion());

        btnReservar.addActionListener(e -> {

            List<Habitacion> disponibles = Main.habitacionesDisponibles();
            int idx = comboHabitaciones.getSelectedIndex();

            if (disponibles.isEmpty() || idx < 0) {
                JOptionPane.showMessageDialog(this,
                        "No hay habitaciones disponibles.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // ===============================
                //  VALIDAR DATOS
                // ===============================
                String rut = txtRut.getText().trim();
                String nombres = txtNombres.getText().trim();
                String apellidos = txtApellidos.getText().trim();
                String telefono = txtTelefono.getText().trim();
                int dias = Integer.parseInt(txtDias.getText());

                if (rut.isEmpty() || nombres.isEmpty() || apellidos.isEmpty()
                        || telefono.length() < 7 || dias <= 0) {
                    throw new Exception();
                }

                Habitacion h = disponibles.get(idx);
                h.disponible = false;
                Persistencia.guardar(Main.habitaciones, Main.ARCHIVO_HABITACIONES);

                String nombreCompleto = nombres + " " + apellidos;
                double total = h.precio * dias;

// 👉 PROCESAR PAGO
                Object[] pago = procesarPagoReserva(total);
                if (pago == null) return;

                String medioPago = (String) pago[0];
                double montoPagado = (double) pago[1];
                double vuelto = (double) pago[2];

// 👉 CREAR VENTA
                VentaHabitacion venta = new VentaHabitacion(
                        rut,
                        nombreCompleto,
                        telefono,
                        h,
                        dias,
                        total,
                        medioPago,
                        montoPagado,
                        vuelto
                );

                Main.ventasHabitaciones.add(venta);
                Persistencia.guardar(
                        Main.ventasHabitaciones,
                        Main.ARCHIVO_VENTAS_HABITACIONES
                );

                mostrarBoletaReserva(venta);

                dispose();
                new MenuHabitaciones();


            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Complete correctamente todos los campos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        btnVolver.addActionListener(e -> {
            dispose();
            new MenuHabitaciones();
        });

        actualizarDescripcion();
        setVisible(true);
    }

    private void mostrarBoletaReserva(VentaHabitacion v) {

        JDialog dialog = new JDialog(this, "Boleta de Reserva", true);
        dialog.setSize(450, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.white);

        JLabel titulo = new JLabel("HOTEL LAS BRISAS");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Boleta de Reserva");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titulo);
        panel.add(subtitulo);
        panel.add(Box.createVerticalStrut(20));

        panel.add(new JLabel("Fecha emisión: " + v.getFechaVenta()));
        panel.add(new JSeparator());

        panel.add(new JLabel("CLIENTE"));
        panel.add(new JLabel("Nombre: " + v.nombreCliente));
        panel.add(new JLabel("RUT: " + v.rut));
        panel.add(new JLabel("Teléfono: " + v.telefono));
        panel.add(new JSeparator());

        panel.add(new JLabel("HABITACIÓN"));
        panel.add(new JLabel("Número: " + v.habitacion.numero));
        panel.add(new JLabel("Tipo: " + v.habitacion.tipo));
        panel.add(new JLabel("Precio diario: $" + v.habitacion.precio));
        panel.add(new JLabel("Días: " + v.dias));
        panel.add(new JSeparator());

        panel.add(new JLabel("Estadía: " +
                v.getFechaInicio() + " → " + v.getFechaTermino()));
        panel.add(new JSeparator());

        panel.add(new JLabel("PAGO"));
        panel.add(new JLabel("Medio: " + v.medioPago));
        panel.add(new JLabel("Total: $" + String.format("%.2f", v.total)));

        if (v.medioPago.equals("Efectivo")) {
            panel.add(new JLabel("Pagado: $" + String.format("%.2f", v.montoPagado)));
            panel.add(new JLabel("Vuelto: $" + String.format("%.2f", v.vuelto)));
        }

        panel.add(Box.createVerticalStrut(20));

        JLabel gracias = new JLabel("Gracias por su preferencia");
        gracias.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(gracias);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dialog.dispose());

        JPanel panelBtn = new JPanel();
        panelBtn.add(btnCerrar);

        dialog.add(new JScrollPane(panel), BorderLayout.CENTER);
        dialog.add(panelBtn, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private Object[] procesarPagoReserva(double total) {

        String[] pagos = {"Efectivo", "Débito"};
        String medioPago = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione medio de pago:",
                "Pago",
                JOptionPane.QUESTION_MESSAGE,
                null,
                pagos,
                pagos[0]
        );

        if (medioPago == null) return null;

        double montoPagado = total;
        double vuelto = 0;

        if (medioPago.equals("Efectivo")) {
            boolean valido = false;
            while (!valido) {
                String input = JOptionPane.showInputDialog(
                        this,
                        "Total: $" + String.format("%.2f", total) +
                                "\nIngrese monto pagado:",
                        "Pago en efectivo",
                        JOptionPane.PLAIN_MESSAGE
                );
                if (input == null) return null;

                try {
                    montoPagado = Double.parseDouble(input);
                    if (montoPagado < total) {
                        JOptionPane.showMessageDialog(this,
                                "Monto insuficiente.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        vuelto = montoPagado - total;
                        valido = true;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                            "Ingrese un monto válido.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        return new Object[]{medioPago, montoPagado, vuelto};
    }


    // ===============================
    // MÉTODOS
    // ===============================
    private void actualizarHabitaciones() {
        comboHabitaciones.removeAllItems();
        for (Habitacion h : Main.habitacionesDisponibles()) {
            comboHabitaciones.addItem(
                    "Hab " + h.numero + " - " + h.tipo + " - $" + h.precio
            );
        }
    }

    private void actualizarDescripcion() {
        int idx = comboHabitaciones.getSelectedIndex();
        List<Habitacion> disponibles = Main.habitacionesDisponibles();

        if (idx >= 0 && idx < disponibles.size()) {
            txtDescripcion.setText(
                    getDescripcionHabitacion(disponibles.get(idx).tipo)
            );
        }
    }

    private String getDescripcionHabitacion(String tipo) {
        return switch (tipo) {
            case "Sencilla" -> """
                    • 1 baño
                    • 2 dormitorios simples
                    • Cocina
                    • Living
                    • 10 - 12 m²
                    """;
            case "Doble" -> """
                    • 2 baños
                    • 1 dormitorio matrimonial
                    • 1 dormitorio simple
                    • Cocina
                    • Living
                    • 20 - 25 m²
                    """;
            case "Suite" -> """
                    • 2 baños
                    • 1 dormitorio matrimonial
                    • 1 dormitorio simple
                    • Cocina
                    • Gimnasio
                    • Mini bar
                    • Terraza
                    • 40 - 45 m²
                    """;
            default -> "Sin información";
        };
    }
}