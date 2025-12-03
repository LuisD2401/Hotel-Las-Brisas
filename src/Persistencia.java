import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Persistencia {
    static List<Venta> ventas = new ArrayList<>();
    private static final String ARCHIVO_VENTAS = "ventas.dat";

    List<Venta> v = (List<Venta>) Persistencia.cargar(ARCHIVO_VENTAS);

    public static void guardar(Object obj, String archivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object cargar(String archivo) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }
}