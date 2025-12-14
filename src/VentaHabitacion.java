import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VentaHabitacion implements Serializable {

    private static final long serialVersionUID = 1L;

    public String rut;
    public String nombreCliente;
    public String telefono;

    public Habitacion habitacion;
    public int dias;
    public double total;

    public String medioPago;
    public double montoPagado;
    public double vuelto;

    public LocalDate fechaInicio;
    public LocalDate fechaTermino;
    public LocalDateTime fechaVenta;

    public VentaHabitacion(String rut,
                           String nombreCliente,
                           String telefono,
                           Habitacion habitacion,
                           int dias,
                           double total,
                           String medioPago,
                           double montoPagado,
                           double vuelto) {

        this.rut = rut;
        this.nombreCliente = nombreCliente;
        this.telefono = telefono;
        this.habitacion = habitacion;
        this.dias = dias;
        this.total = total;
        this.medioPago = medioPago;
        this.montoPagado = montoPagado;
        this.vuelto = vuelto;

        this.fechaInicio = LocalDate.now();
        this.fechaTermino = fechaInicio.plusDays(dias);
        this.fechaVenta = LocalDateTime.now();
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
    @Override
    public String toString() {
        return """
        ========= BOLETA RESERVA =========
        Fecha: %s

        Cliente:
        RUT: %s
        Nombre: %s
        Teléfono: %s

        Habitación:
        Tipo: %s
        N°: %d
        Precio por día: $%.0f
        Días: %d

        Estadia:
        Desde: %s
        Hasta: %s

        Pago:
        Medio: %s
        Pagado: $%.0f
        Vuelto: $%.0f

        TOTAL: $%.0f
        =================================
        """.formatted(
                fechaVenta,
                rut, nombreCliente, telefono,
                habitacion.tipo, habitacion.numero, habitacion.precio, dias,
                fechaInicio, fechaTermino,
                medioPago, montoPagado, vuelto,
                total
        );
    }

}
