import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static ArrayList<Habitacion> habitaciones = new ArrayList<>();
    public static ArrayList<ProductoCafeteria> productos = new ArrayList<>();
    public static ArrayList<Venta> ventas = new ArrayList<>();

    public static final String ARCHIVO_HABITACIONES = "habitaciones.dat";
    public static final String ARCHIVO_PRODUCTOS = "productos.dat";
    public static final String ARCHIVO_VENTAS = "ventas.dat";

    public static void main(String[] args) {
        cargarDatos();
        inicializarHabitaciones();
        new HotelGUI();
    }

    public static void cargarDatos() {
        try {
            habitaciones = (ArrayList<Habitacion>) Persistencia.cargar(ARCHIVO_HABITACIONES);
            productos = (ArrayList<ProductoCafeteria>) Persistencia.cargar(ARCHIVO_PRODUCTOS);
            ventas = (ArrayList<Venta>) Persistencia.cargar(ARCHIVO_VENTAS);

            // 👇 Asegurar que nunca queden null
            if (habitaciones == null) habitaciones = new ArrayList<>();
            if (productos == null) productos = new ArrayList<>();
            if (ventas == null) ventas = new ArrayList<>();

        } catch (Exception e) {
            // Si ocurre cualquier error, inicializar limpio
            habitaciones = new ArrayList<>();
            productos = new ArrayList<>();
            ventas = new ArrayList<>();
        }
    }

    public static void inicializarHabitaciones() {
        if (habitaciones.isEmpty()) {
            Persistencia.guardar(habitaciones, ARCHIVO_HABITACIONES);
        }
    }

    public static List<Habitacion> habitacionesDisponibles() {
        return habitaciones.stream().filter(h -> h.disponible).toList();
    }


    // Procesar pago
    public static Venta procesarPagoGUI(double montoTotal, String tipoVenta, List<ProductoCafeteria> productos, List<Integer> cantidades, int numeroHabitacion) {

        String[] pagos = {"Efectivo", "Tarjeta"};
        String medioPago = (String) JOptionPane.showInputDialog(
                null, "Seleccione medio de pago:", "Pago",
                JOptionPane.QUESTION_MESSAGE, null, pagos, pagos[0]);

        if (medioPago == null) return null;

        double montoPagado = montoTotal;
        double vuelto = 0;

        if (medioPago.equals("Efectivo")) {
            boolean valido = false;
            while (!valido) {
                String input = JOptionPane.showInputDialog(
                        null,
                        "Total: $" + String.format("%.2f", montoTotal) + "\nIngrese monto pagado:",
                        "Pago en efectivo",
                        JOptionPane.PLAIN_MESSAGE
                );
                if (input == null) return null;
                try {
                    montoPagado = Double.parseDouble(input);
                    if (montoPagado < montoTotal) {
                        JOptionPane.showMessageDialog(null, "El monto es insuficiente.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        valido = true;
                        vuelto = montoPagado - montoTotal;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // Crear venta
        Venta v = new Venta(montoTotal, tipoVenta, medioPago, "Boleta",
                numeroHabitacion, productos, cantidades);

        ventas.add(v); // <-- ahora nunca será null
        Persistencia.guardar(ventas, ARCHIVO_VENTAS);

        // Mostrar boleta
        StringBuilder boleta = new StringBuilder();
        boleta.append("Compra realizada correctamente.\n");
        boleta.append("Venta registrada en ").append(tipoVenta).append("\n\n");

        boleta.append("Tipo de pago: ").append(medioPago).append("\n");
        boleta.append("Fecha de compra: ").append(
                v.fecha.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        ).append("\n\n");

        if (medioPago.equals("Efectivo")) {
            boleta.append("Monto pagado: $").append(String.format("%.2f", montoPagado)).append("\n");
            boleta.append("Vuelto: $").append(String.format("%.2f", vuelto)).append("\n\n");
        }

        boleta.append("Detalles de la compra:\n");
        for (int i = 0; i < productos.size(); i++) {
            ProductoCafeteria p = productos.get(i);
            int cant = cantidades.get(i);
            double subtotal = p.precio * cant;

            boleta.append("- ").append(p.nombre)
                    .append(" x").append(cant)
                    .append(" = $").append(String.format("%.2f", subtotal))
                    .append("\n");
        }

        boleta.append("\nTotal de la compra: $").append(String.format("%.2f", montoTotal));

        JOptionPane.showMessageDialog(null, boleta.toString(), "Boleta", JOptionPane.INFORMATION_MESSAGE);

        return v;
    }
}


