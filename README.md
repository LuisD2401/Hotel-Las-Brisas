# Sistema de Reserva y Venta del hotel Las Brisas

Esta aplicación es un sistema de gestion de las reservas de habitaciones y las ventas en cafeteria para el hotel Las Brisas desde el lado del recepcionista o vendedor respectivamente. 
En el area de la reserva la aplicación permite crear y anular reservas de habitaciones, crear y eliminar habitaciones del sistema y revisar las reservas existentes.
En el area de la cafeteria la aplicación permite realizar ventas, gestionar el inventario de productos y revisar todas las ventas realizadas.

## Caracteristicas
Reserva:
- Realizar reservas y anularlas si se desea ademas de la gestión del pago de estas con efectivo o tarjeta.
- Gestionar las habitaciones del hotel permitiendo crear o elimianar habitaciones, ademas de revisar cuales estan disponibles.
- Revisar una lista de todas las reservas actuales, revisar sus boletas y la opcion de eliminarlas.
Cafeteria:
- Realizar ventas de productos y aceptar pagos en efectivo o debito entregando boleta.
- Gestionar el inventario entregando las opciones de crear y eliminar productos y editando su inventario.
- Revisar una lista de todas las ventas hechas con la posibilidad de revisar las boletas y eliminar las ventas del inventario.

## Tecnologías usadas
- Lenguaje: Java SE
- Interfaz Gráfica: Swing
- Persistencia: Serialización de objetos (archivo binario)
## Instalación
1) Importar el proyecto en tu IDE favorito (IntelliJ IDEA, Eclipse, NetBeans)
2) Asegurarse de que el JDK esté configurado correctamente
3) Ejecutar la clase Main.java
## Acceso
Hay diversas funcionalidades que requieren una clave para usarlas. La clave es 123.
## Uso del sistema
Al iniciar la aplicación aparece un menú con 2 opciones:
-  Habitaciones: Abre la sección de gestión de reservas y habitaciones.
-  Cafetería: Abre la sección de gestion de ventas y productos de la cafetería.
### Habitaciones
- Hacer reserva: Formulario para ingresar datos del huesped, los dias de estadia y para elegír habitación. Muestra detalles de la habitación al final. Luego se elige el metodo de pago y la cantidad con la que se pagara. Finalmente se imprime la boleta.
- Gestionar habitaciones y reservas: Pide contraseña. Tabla de las habitaciones mostrando Numero, Tipo, Precio y Disponibilidad. Abajo botones para agregar o eliminar habitaciones, anular reservas o cerrar la pestaña.
- Ver Reservas: Pide contraseña. Tabla con todas las reservaciones mostrando  Numero de la habitación, Rut del huesped, Fecha, Estado de la reserva y opción de ver la boleta. Al final 2 botones para eliminar una venta y cerrar la pestaña.
- Volver al menu principal: Vuelve al menu de inicio.
- Salir: Cierra el programa.
### Cafetería
- Venta en Cafetería: Lista de productos a la venta con precio y stocl y la capacidad de añadirlos al carrito. Al final boton para pagar que abre una pestaña preguntando metodo de pago y luego cantidad con la que se paga. Luego muestra la boleta.
- Gestionar Productos: Lista con todos los productos disponibles, su stock actual y botones para aumentar o disminuir el stock de cada producto en cantidades de 1 o 5. Al final hay botones para agregar o eliminar productos y cerrar la pestaña. Al agregar un producto pide Nombre, Precio y Stock inicial. Al elegir eliminar muestra una lista con los nombres de todos los productos para elegir el que se desea eliminar.
- Ver ventas: Pide contraseña. Tabla con tipo de venta, Monto, Fecha y Hora, boton para ver la boleta y boton para eliminar la venta de la lista.
- Volver al menu principal: Vuelve al menu de inicio.
- Salir: Cierra el programa.
