import java.io.Serializable;
import java.time.LocalDateTime;

public class Venta implements Serializable {
    double monto;
    String tipo;
    String medioPago;
    String tipoDocumento;
    LocalDateTime fecha;
    int numeroHabitacion;

    public Venta(double monto, String tipo, String medioPago, String tipoDocumento, int numeroHabitacion) {
        this.monto = monto;
        this.tipo = tipo;
        this.medioPago = medioPago;
        this.tipoDocumento = tipoDocumento;
        this.fecha = LocalDateTime.now();
        this.numeroHabitacion = numeroHabitacion;
    }
    public Venta(double monto, String tipo, String medioPago, String tipoDocumento) {
        this(monto, tipo, medioPago, tipoDocumento, -1);
    }

    @Override
    public String toString() {
        return tipo + " - $" + monto + " - " + medioPago + " - " + tipoDocumento + " - " + fecha;
    }
}
