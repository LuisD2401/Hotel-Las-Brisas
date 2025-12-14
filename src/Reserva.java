import java.io.Serializable;

public class Reserva implements Serializable {
    String cliente;
    Habitacion habitacion;
    int dias;
    double total;

    public Reserva(String cliente, Habitacion habitacion, int dias) {
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.dias = dias;
        this.total = dias * habitacion.precio;
    }

    @Override
    public String toString() {
        return "\n--- RESERVA ---" +
                "\nCliente: " + cliente +
                "\nHabitación: " + habitacion.numero +
                "\nTipo: " + habitacion.tipo +
                "\nDías: " + dias +
                "\nTotal: $" + total;
    }
}

