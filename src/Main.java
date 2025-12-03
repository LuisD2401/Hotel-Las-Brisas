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
        } catch (Exception e) {
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
    public static Venta procesarPagoGUI(double monto, String tipo, int numeroHab) {
        String[] pagos = {"Tarjeta", "Efectivo", "Transferencia"};
        String medioPago = (String) JOptionPane.showInputDialog(
                null, "Seleccione medio de pago:", "Pago",
                JOptionPane.QUESTION_MESSAGE, null, pagos, pagos[0]);

        String[] docs = {"Boleta", "Factura"};
        String tipoDocumento = (String) JOptionPane.showInputDialog(
                null, "Seleccione tipo de documento:", "Documento",
                JOptionPane.QUESTION_MESSAGE, null, docs, docs[0]);

        Venta v;
        if (tipo.equals("Reserva")) {
            v = new Venta(monto, tipo, medioPago, tipoDocumento, numeroHab);
        } else {
            v = new Venta(monto, tipo, medioPago, tipoDocumento);
        }

        ventas.add(v);
        Persistencia.guardar(ventas, ARCHIVO_VENTAS);
        return v;
    }

    public static Venta procesarPagoGUI(double monto, String tipo) {
        return procesarPagoGUI(monto, tipo, -1);
    }
}

