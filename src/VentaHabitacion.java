import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VentaHabitacion implements Serializable {


    String rut;
    String nombreCliente;
    String telefono;
    public boolean anulada = false;
    Habitacion habitacion;
    int dias;
    double total;
    String medioPago;
    double montoPagado;
    double vuelto;

    LocalDateTime fechaVenta;
    LocalDateTime fechaInicio;
    LocalDateTime fechaTermino;


    public VentaHabitacion(
            String rut,
            String nombreCliente,
            String telefono,
            Habitacion habitacion,
            int dias,
            double total,
            String medioPago,
            double montoPagado,
            double vuelto
    ) {
        this.rut = rut;
        this.nombreCliente = nombreCliente;
        this.telefono = telefono;
        this.habitacion = habitacion;
        this.dias = dias;
        this.total = total;
        this.medioPago = medioPago;
        this.montoPagado = montoPagado;
        this.vuelto = vuelto;

        this.fechaVenta = LocalDateTime.now();
        this.fechaInicio = LocalDateTime.now();
        this.fechaTermino = fechaInicio.plusDays(dias);

    }

    @Override
    public String toString() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return """
        ========= BOLETA RESERVA =========
        Fecha venta: %s

        Cliente:
        RUT: %s
        Nombre: %s
        Teléfono: %s

        Habitación:
        Tipo: %s
        Nº: %d
        Precio por día: $%.0f
        Días: %d

        Estadía:
        Desde: %s
        Hasta: %s

        Pago:
        Medio: %s
        Monto pagado: $%.0f
        Vuelto: $%.0f

        TOTAL: $%.0f
        =================================
        """.formatted(
                fechaVenta.format(f),
                rut, nombreCliente, telefono,
                habitacion.tipo, habitacion.numero, habitacion.precio, dias,
                fechaInicio.format(f), fechaTermino.format(f),
                medioPago, montoPagado, vuelto,
                total
        );
    }
    public Object[] toFilaTabla() {
        return new Object[]{
                habitacion.numero,
                nombreCliente,
                habitacion.tipo,
                dias,
                "$" + Math.round(total),
                medioPago,
                anulada ? "ANULADA" : "ACTIVA"
        };
    }
    public boolean estaActiva() {
        return !anulada;
    }

    public String resumen() {
        return String.format(
                "Hab %d | %s | %d días | $%.0f | %s",
                habitacion.numero,
                nombreCliente,
                dias,
                total,
                medioPago
        );
    }
    public void anular() {
        if (!anulada) {
            anulada = true;
            Habitacion hReal = Main.getHabitacionPorNumero(habitacion.numero);
            if (hReal != null) hReal.disponible = true;
        }
    }



    public String getFechaInicio() {
        return fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getFechaTermino() {
        return fechaTermino.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }


    public String getFechaVenta() {
        return fechaVenta.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }


}
