import java.io.Serializable;

class Habitacion implements Serializable {
    int numero;
    String tipo;
    double precio;
    boolean disponible;

    public Habitacion(int numero, String tipo, double precio) {
        this.numero = numero;
        this.tipo = tipo;
        this.precio = precio;
        this.disponible = true;
    }

    @Override
    public String toString() {
        return "Hab " + numero + " - " + tipo + " - $" + precio + " - " +
                (disponible ? "Disponible" : "Ocupada");
    }
}