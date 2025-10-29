package org.iesvdm.ventas;

import org.iesvdm.ventas.modelo.Cliente;
import org.iesvdm.ventas.modelo.Comercial;
import org.iesvdm.ventas.modelo.Pedido;
import org.iesvdm.ventas.repositorio.ClienteRepository;
import org.iesvdm.ventas.repositorio.ComercialRepository;
import org.iesvdm.ventas.repositorio.PedidoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class VentasApplicationTests {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ComercialRepository comercialRepository;


    @Test
    void contextLoads() {

    }

    @Test
    void test() {
        fail("Not yet implemented");
    }


    @Test
    void testSkeletonCliente() {

            List<Cliente> list = clienteRepository.findAll();
            list.forEach(System.out::println);
            //TODO STREAMS

    }


    @Test
    void testSkeletonComercial() {

            List<Comercial> list = comercialRepository.findAll();
            list.forEach(System.out::println);
            //TODO STREAMS

    }

    @Test
    void testSkeletonPedido() {

            List<Pedido> list = pedidoRepository.findAll();
            list.forEach(System.out::println);
            //TODO STREAMS


    }
    /**
     * 1. Devuelve un listado de todos los pedidos que se realizaron durante el año 2017,
     * cuya cantidad total sea superior a 500€.
     * @throws ParseException
     */
    @Test
    void test1() throws ParseException {

        // 1️⃣ Obtenemos todos los pedidos de la base de datos
        List<Pedido> list = pedidoRepository.findAll();

        // 2️⃣ Filtramos pedidos que cumplan:
        // - Año 2017
        // - Total > 500
        List<Pedido> pedidosFiltrados = list.stream()
                .filter(p -> (p.getFecha().getYear() + 1900) == 2017) // Date.getYear() + 1900
                .filter(p -> p.getTotal() > 500)
                .toList();

        // 3️⃣ Imprimimos los resultados
        pedidosFiltrados.forEach(p -> System.out.println(
                "Pedido ID: " + p.getId() +
                        ", Cliente: " + p.getCliente().getNombre() + " " + p.getCliente().getApellido1() +
                        ", Fecha: " + p.getFecha() +
                        ", Total: " + p.getTotal()
        ));
    }


    /**
     * 2. Devuelve un listado con los identificadores de los clientes que NO han realizado algún pedido.
     *
     */
    @Test
    void test2() {

        // 1️⃣ Obtenemos todos los clientes de la base de datos
        List<Cliente> list = clienteRepository.findAll();

        // 2️⃣ Filtramos los clientes que no tienen pedidos asociados
        //    - c.getPedidos() == null → no tiene ningún pedido
        //    - c.getPedidos().isEmpty() → el conjunto de pedidos está vacío
        // 3️⃣ Transformamos la lista de clientes en una lista de IDs
        List<Integer> clientesSinPedidos = list.stream()
                .filter(c -> c.getPedidos() == null || c.getPedidos().isEmpty())
                .map(Cliente::getId)
                .toList();

        // 4️⃣ Mostramos la lista de IDs de clientes sin pedidos por consola
        System.out.println("Clientes sin pedidos: " + clientesSinPedidos);


    }

    /**
     * 3. Devuelve el valor de la comisión de mayor valor que existe en la tabla comercial
     */
    @Test
    void test3() {
        // 1️⃣ Obtenemos todos los comerciales de la base de datos
        List<Comercial> list = comercialRepository.findAll();

        // 2️⃣ Creamos un stream para:
        //    - Transformar cada comercial en su comisión
        //    - Filtrar valores nulos (por si algún comercial no tiene comisión)
        //    - Obtener el valor máximo
        Optional<Float> maxComision = list.stream()
                .map(Comercial::getComision)
                .filter(Objects::nonNull) // por si hay comisiones nulas
                .max(Float::compare);

        // Mostramos el resultado por consola
        if (maxComision.isPresent()) {
            System.out.println("La comisión máxima es: " + maxComision.get());
        } else {
            System.out.println("No hay comisiones registradas.");
        }

    }

    /**
     * 4. Devuelve el identificador, nombre y primer apellido de aquellos clientes cuyo segundo apellido no es NULL.
     * El listado deberá estar ordenado alfabéticamente por apellidos y nombre.
     */
    @Test
    void test4() {

        List<Cliente> list = clienteRepository.findAll();

        // 2️⃣ Filtramos y ordenamos:
        //    - Filtramos solo clientes cuyo segundo apellido no sea null
        //    - Ordenamos alfabéticamente por apellido1, luego apellido2 y luego nombre
        //    - Transformamos cada cliente a una cadena con ID, nombre, apellido1 y apellido2
        List<String> clientesFiltrados = list.stream()
                .filter(c -> c.getApellido2() != null)
                .sorted(Comparator.comparing(Cliente::getApellido1)
                        .thenComparing(Cliente::getApellido2)
                        .thenComparing(Cliente::getNombre))
                .map(c -> c.getId() + " - " + c.getNombre() + " " + c.getApellido1())
                .toList();

        // 3️⃣ Mostramos el resultado por consola
        clientesFiltrados.forEach(System.out::println);



    }

    /**
     * 5. Devuelve un listado con los nombres de los comerciales que terminan por "el" o "o".
     *  Tenga en cuenta que se deberán eliminar los nombres repetidos.
     */
    @Test
    void test5() {

        // 1️⃣ Obtenemos todos los comerciales de la base de datos
        List<Comercial> list = comercialRepository.findAll();

        // 2️⃣ Filtramos nombres que terminan en "el" o "o" y eliminamos duplicados
        List<String> comercialesFiltrados = list.stream()
                .map(Comercial::getNombre)                   // Tomamos solo el nombre de cada comercial
                .filter(n -> n.endsWith("el") || n.endsWith("o")) // Filtramos los nombres que terminan en "el" o "o"
                .distinct()                                  // Eliminamos nombres repetidos
                .toList();                                   // Convertimos a lista

        // 3️⃣ Mostramos el resultado por consola
        System.out.println("Comerciales cuyos nombres terminan en 'el' o 'o':");
        comercialesFiltrados.forEach(System.out::println);
    }


    /**
     * 6. Devuelve un listado de todos los clientes que realizaron un pedido durante el año 2017, cuya cantidad esté entre 300 € y 1000 €.
     */
    @Test
    void test6() {

        // 1️⃣ Obtenemos todos los pedidos de la base de datos
        List<Pedido> list = pedidoRepository.findAll();

        // 2️⃣ Filtramos pedidos:
        //    - Convertimos la fecha de java.util.Date a LocalDate
        //    - Solo pedidos del año 2017
        //    - Total entre 300€ y 1000€
        List<Cliente> clientesFiltrados = list.stream()
                .filter(p -> {
                    LocalDate fecha = p.getFecha().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate(); // Convertimos Date a LocalDate
                    return fecha.getYear() == 2017 && p.getTotal() >= 300 && p.getTotal() <= 1000;
                })
                .map(Pedido::getCliente) // Obtenemos el cliente del pedido
                .distinct()              // Eliminamos clientes repetidos
                .toList();               // Creamos la lista final

        // 3️⃣ Mostramos los resultados
        clientesFiltrados.forEach(c ->
                System.out.println(c.getId() + " - " + c.getNombre() + " " + c.getApellido1())
        );

    }


    /**
     * 7. Calcula la media del campo total de todos los pedidos realizados por el comercial Daniel Sáez
     */
    @Test
    void test7() {

        // 1️⃣ Obtenemos todos los comerciales de la base de datos
        List<Comercial> list = comercialRepository.findAll();

        // 2️⃣ Filtramos solo al comercial "Daniel Sáez" y obtenemos todos sus pedidos
        List<Pedido> pedidosFiltrados = list.stream()
                .filter(c -> c.getNombre().equals("Daniel Sáez")) // Solo Daniel Sáez
                .flatMap(c -> c.getPedidos().stream())           // Obtenemos todos sus pedidos
                .toList();                                       // Convertimos a lista

        // 3️⃣ Calculamos la media de los totales de esos pedidos
        double total = pedidosFiltrados.stream()
                .mapToDouble(Pedido::getTotal)  // Tomamos el campo total
                .average()                      // Calculamos la media
                .orElse(0);                     // Si no hay pedidos, devolvemos 0

        // 4️⃣ Mostramos el resultado por consola
        System.out.println("Media del campo total de pedidos de Daniel Sáez: " + total);
    }


    /**
     * 8. Devuelve un listado con todos los pedidos que se han realizado.
     *  Los pedidos deben estar ordenados por la fecha de realización
     * , mostrando en primer lugar los pedidos más recientes
     */
    @Test
    void test8() {

        // 1️⃣ Obtenemos todos los pedidos de la base de datos
        List<Pedido> list = pedidoRepository.findAll();

        // 2️⃣ Ordenamos los pedidos por fecha de manera descendente (más recientes primero)
        List<Pedido> pedidosFiltrados = list.stream()
                .sorted(Comparator.comparing(Pedido::getFecha).reversed()) // Orden descendente
                .toList();                                                 // Convertimos a lista

        // 3️⃣ Mostramos los resultados por consola
        System.out.println("Pedidos ordenados por fecha de realización (más recientes primero):");
        pedidosFiltrados.forEach(p ->
                System.out.println(
                        p.getId() +
                                " - Cliente ID: " + p.getCliente().getId() +
                                " - Fecha: " + p.getFecha()
                )
        );
    }

    /**
     * 9. Devuelve todos los datos de los dos pedidos de mayor valor.
     */
    @Test
    void test9() {

        // 1️⃣ Obtenemos todos los pedidos de la base de datos
        List<Pedido> list = pedidoRepository.findAll();

        // 2️⃣ Ordenamos los pedidos por total de manera descendente y tomamos los 2 primeros
        List<Pedido> pedidosFiltrados = list.stream()
                .sorted(Comparator.comparing(Pedido::getTotal).reversed()) // Orden descendente por total
                .limit(2)                                                  // Solo los 2 primeros
                .toList();                                                 // Convertimos a lista

        // 3️⃣ Mostramos los resultados por consola
        pedidosFiltrados.forEach(p ->
                System.out.println(
                        p.getId() +
                                " - Cliente ID: " + p.getCliente().getId() +
                                " - Total: " + p.getTotal()
                )
        );
    }

    /**
     * 10. Devuelve un listado con los identificadores de los clientes que han realizado algún pedido.
     * Tenga en cuenta que no debe mostrar identificadores que estén repetidos.
     */
    @Test
    void test10() {


        // 1️⃣ Obtenemos todos los pedidos de la base de datos
        List<Pedido> list = pedidoRepository.findAll();

        // 2️⃣ Transformamos el stream:
        //    - Tomamos el cliente de cada pedido
        //    - Obtenemos su ID
        //    - Eliminamos IDs duplicados
        List<Integer> clientesFiltrados = list.stream()
                .map(Pedido::getCliente) // Obtenemos el cliente de cada pedido
                .map(Cliente::getId)     // Obtenemos el ID del cliente
                .distinct()              // Eliminamos duplicados
                .toList();               // Creamos la lista final

        // 3️⃣ Mostramos los IDs por consola
        System.out.println("Identificadores de clientes que han realizado pedidos:");
        clientesFiltrados.forEach(System.out::println);
    }

    /**
     * 11. Devuelve un listado con el nombre y los apellidos de los comerciales que tienen una comisión entre 0.05 y 0.11.
     *
     */
    @Test
    void test11() {

        // 1️⃣ Obtenemos todos los comerciales de la base de datos
        List<Comercial> list = comercialRepository.findAll();

        // 2️⃣ Filtramos y transformamos:
        //    - Solo comerciales con comisión no nula
        //    - Comisión entre 0.05 y 0.11 inclusive
        //    - Mapeamos a un String con nombre y apellidos
        //    - Eliminamos posibles duplicados
        List<String> comercialesFiltrados = list.stream()
                .filter(c -> c.getComision() != null && c.getComision() >= 0.05 && c.getComision() <= 0.11)
                .map(c -> c.getNombre() + " - " + c.getApellido1() + " " + c.getApellido2())
                .distinct()
                .toList();

        // 3️⃣ Mostramos los resultados por consola
        System.out.println("Comerciales con comisión entre 0.05 y 0.11:");
        comercialesFiltrados.forEach(System.out::println);
    }


    /**
     * 12. Devuelve el valor de la comisión de menor valor que existe para los comerciales.
     *
     */
    @Test
    void test12() {

        // 1️⃣ Obtenemos todos los comerciales de la base de datos
        List<Comercial> list = comercialRepository.findAll();

        // 2️⃣ Buscamos la comisión mínima:
        //    - Convertimos cada comercial a su comisión
        //    - Filtramos valores nulos
        //    - Tomamos la mínima usando Float::compare
        Optional<Float> minComision = list.stream()
                .map(Comercial::getComision)   // Tomamos la comisión
                .filter(Objects::nonNull)      // Ignoramos nulos
                .min(Float::compare);           // Buscamos el mínimo

        // 3️⃣ Mostramos el resultado por consola
        System.out.println("Valor de la comisión de menor valor: " + minComision.orElse(null));
    }

    /**
     * 13. Devuelve un listado de los nombres de los clientes que
     * empiezan por A y terminan por n y también los nombres que empiezan por P.
     * El listado deberá estar ordenado alfabéticamente.
     *
     */
    @Test
    void test13() {

        // 1️⃣ Obtenemos todos los clientes de la base de datos
        List<Cliente> list = clienteRepository.findAll();

        // 2️⃣ Filtramos los nombres según las condiciones del enunciado
        List<String> clientesFiltrados = list.stream()
                .map(Cliente::getNombre) // Tomamos solo el nombre
                .filter(n -> (n.startsWith("A") && n.endsWith("n")) || n.startsWith("P")) // Filtramos nombres
                .distinct()              // Eliminamos duplicados
                .sorted()                // Orden alfabético
                .toList();               // Convertimos a lista

        // 3️⃣ Mostramos los resultados por consola
        System.out.println("Clientes cuyos nombres empiezan por A y terminan por n, o empiezan por P:");
        clientesFiltrados.forEach(System.out::println);
    }

    /**
     * 14. Devuelve un listado de los nombres de los clientes
     * que empiezan por A y terminan por n y también los nombres que empiezan por P.
     * El listado deberá estar ordenado alfabéticamente.
     */
    @Test
    void test14() {

        // 1️⃣ Obtenemos todos los clientes de la base de datos
        List<Cliente> list = clienteRepository.findAll();

        // 2️⃣ Filtramos los nombres según las condiciones:
        //    - Empiezan por A y terminan por n
        //    - O empiezan por P
        List<String> clientesFiltrados = list.stream()
                .map(Cliente::getNombre) // Tomamos solo el nombre
                .filter(n -> (n.startsWith("A") && n.endsWith("n")) || n.startsWith("P"))
                .distinct()              // Eliminamos duplicados
                .sorted()                // Orden alfabético
                .toList();               // Convertimos a lista

        // 3️⃣ Mostramos los resultados
        System.out.println("Clientes cuyos nombres empiezan por A y terminan por n, o empiezan por P:");
        clientesFiltrados.forEach(System.out::println);
    }

    /**
     * 15. Devuelve un listado de los clientes cuyo nombre no empieza por A.
     * El listado deberá estar ordenado alfabéticamente por nombre y apellidos.
     */
    @Test
    void test15() {

        // 1️⃣ Obtenemos todos los clientes de la base de datos
        List<Cliente> list = clienteRepository.findAll();

        // 2️⃣ Filtramos clientes cuyo nombre NO empieza por A
        //    Ordenamos alfabéticamente por nombre, apellido1 y apellido2
        //    Creamos un string con ID, nombre y apellidos
        List<String> clientesFiltrados = list.stream()
                .filter(c -> !c.getNombre().startsWith("A")) // Nombre no empieza por A
                .sorted(Comparator.comparing(Cliente::getNombre)
                        .thenComparing(Cliente::getApellido1)
                        .thenComparing(Cliente::getApellido2)) // Orden alfabético
                .map(c -> c.getId() + " - " + c.getNombre() + " " + c.getApellido1() + " " + c.getApellido2()) // Incluimos ambos apellidos
                .toList();

        // 3️⃣ Mostramos los resultados
        System.out.println("Clientes cuyo nombre no empieza por A:");
        clientesFiltrados.forEach(System.out::println);
    }


    /**
     * 16. Devuelve un listado con el identificador, nombre y los apellidos de todos
     * los clientes que han realizado algún pedido.
     * El listado debe estar ordenado alfabéticamente por apellidos y nombre y se deben eliminar los elementos repetidos.
     */
    @Test
    void test16() {

        List<Pedido> list = pedidoRepository.findAll();
        List<String> clientesFiltrados = list.stream()
                .map(Pedido::getCliente)
                .filter(Objects::nonNull)
                .distinct()
                .sorted(
                        Comparator.comparing(Cliente::getApellido1, Comparator.nullsLast(String::compareTo))
                                .thenComparing(Cliente::getApellido2, Comparator.nullsLast(String::compareTo))
                                .thenComparing(Cliente::getNombre, Comparator.nullsLast(String::compareTo))
                )
                .map(c -> String.format("%d - %s %s %s",
                        c.getId(),
                        c.getNombre() != null ? c.getNombre() : "",
                        c.getApellido1() != null ? c.getApellido1() : "",
                        c.getApellido2() != null ? c.getApellido2() : ""))
                .toList();

        System.out.println("Clientes que han realizado pedidos:");
        clientesFiltrados.forEach(System.out::println);

    }

    /**
     * 17. Devuelve un listado que muestre todos los pedidos que ha realizado cada cliente.
     * El resultado debe mostrar todos los datos del cliente primero junto con un sublistado de sus pedidos.
     * El listado debe mostrar los datos de los clientes ordenados alfabéticamente por nombre y apellidos.
     *
     Cliente [id=1, nombre=Aar�n, apellido1=Rivero, apellido2=G�mez, ciudad=Almer�a, categor�a=100]
     Pedido [id=2, cliente=Cliente [id=1, nombre=Aar�n, apellido1=Rivero, apellido2=G�mez, ciudad=Almer�a, categor�a=100], comercial=Comercial [id=5, nombre=Antonio, apellido1=Carretero, apellido2=Ortega, comisi�n=0.12], total=270.65, fecha=2016-09-10]
     Pedido [id=16, cliente=Cliente [id=1, nombre=Aar�n, apellido1=Rivero, apellido2=G�mez, ciudad=Almer�a, categor�a=100], comercial=Comercial [id=5, nombre=Antonio, apellido1=Carretero, apellido2=Ortega, comisi�n=0.12], total=2389.23, fecha=2019-03-11]
     Pedido [id=15, cliente=Cliente [id=1, nombre=Aar�n, apellido1=Rivero, apellido2=G�mez, ciudad=Almer�a, categor�a=100], comercial=Comercial [id=5, nombre=Antonio, apellido1=Carretero, apellido2=Ortega, comisi�n=0.12], total=370.85, fecha=2019-03-11]
     Cliente [id=2, nombre=Adela, apellido1=Salas, apellido2=D�az, ciudad=Granada, categor�a=200]
     Pedido [id=12, cliente=Cliente [id=2, nombre=Adela, apellido1=Salas, apellido2=D�az, ciudad=Granada, categor�a=200], comercial=Comercial [id=1, nombre=Daniel, apellido1=S�ez, apellido2=Vega, comisi�n=0.15], total=3045.6, fecha=2017-04-25]
     Pedido [id=7, cliente=Cliente [id=2, nombre=Adela, apellido1=Salas, apellido2=D�az, ciudad=Granada, categor�a=200], comercial=Comercial [id=1, nombre=Daniel, apellido1=S�ez, apellido2=Vega, comisi�n=0.15], total=5760.0, fecha=2015-09-10]
     Pedido [id=3, cliente=Cliente [id=2, nombre=Adela, apellido1=Salas, apellido2=D�az, ciudad=Granada, categor�a=200], comercial=Comercial [id=1, nombre=Daniel, apellido1=S�ez, apellido2=Vega, comisi�n=0.15], total=65.26, fecha=2017-10-05]
     ...
     */
    @Test
    void test17() {

        List<Cliente> list = clienteRepository.findAll();
        // Ordenar por nombre y apellidos
        List<Cliente> clientesOrdenados = list.stream()
                .sorted(Comparator.comparing(Cliente::getNombre, Comparator.nullsLast(String::compareTo))
                        .thenComparing(Cliente::getApellido1, Comparator.nullsLast(String::compareTo))
                        .thenComparing(Cliente::getApellido2, Comparator.nullsLast(String::compareTo)))
                .toList();

        // Recorrer clientes
        clientesOrdenados.forEach(c -> {
            // Mostrar los datos del cliente
            System.out.println("Cliente " + c);

            // Mostrar los pedidos del cliente
            c.getPedidos().stream()
                    .sorted(Comparator.comparing(Pedido::getId))
                    .forEach(p -> System.out.println("Pedido " + p));
        });
    }

    /**
     * 18. Devuelve un listado que muestre todos los pedidos en los que ha participado un comercial.
     * El resultado debe mostrar todos los datos de los comerciales y el sublistado de pedidos.
     * El listado debe mostrar los datos de los comerciales ordenados alfabéticamente por apellidos.
     */
    @Test
    void test18() {

        List<Comercial> list = comercialRepository.findAll();
        List<String> comercialesFiltrados = list.stream()
                .map(c -> String.format("%d - %s %s %s",
                        c.getId(),
                        c.getNombre() != null ? c.getNombre() : "",
                        c.getApellido1() != null ? c.getApellido1() : "",
                        c.getApellido2() != null ? c.getApellido2() : ""))
                .toList();
        System.out.println("Comerciales que han realizado pedidos:");
        comercialesFiltrados.forEach(System.out::println);

    }

    /**
     * 19. Devuelve el nombre y los apellidos de todos los comerciales que ha participado
     * en algún pedido realizado por María Santana Moreno.
     */
    @Test
    void test19() {

        List<Pedido> list = pedidoRepository.findAll();
        List<Comercial> comerciales = list.stream()
                .filter(p -> p.getCliente().getNombre().equals("María") &&
                        p.getCliente().getApellido1().equals("Santana") &&
                        p.getCliente().getApellido2().equals("Moreno"))
                .map(Pedido::getComercial)
                .distinct()
                .toList();
        System.out.println("Comerciales que han participado en pedidos de María Santana Moreno:");
        comerciales.forEach(c -> System.out.println(c.getNombre() + " " + c.getApellido1() + " " + c.getApellido2()));

    }


    /**
     * 20. Devuelve un listado que solamente muestre los comerciales que no han realizado ningún pedido.
     */
    @Test
    void test20() {

        // Obtiene todos los comerciales almacenados en la base de datos
        List<Comercial> list = comercialRepository.findAll();

        // Crea una nueva lista con los comerciales que no tienen pedidos asociados
        // (es decir, cuyo conjunto de pedidos está vacío)
        List<Comercial> comercialesSinPedidos = list.stream()
                .filter(c -> c.getPedidos().isEmpty())
                .toList();

        // Muestra un mensaje inicial en la consola
        System.out.println("Comerciales que no han realizado ningún pedido:");

        // Recorre la lista filtrada e imprime el nombre completo de cada comercial sin pedidos
        comercialesSinPedidos.forEach(c -> System.out.println(c.getNombre() + " " + c.getApellido1() + " " + c.getApellido2()));

    }

    /**
     * 21. Calcula el número total de comerciales distintos que aparecen en la tabla pedido
     */
    @Test
    void test21() {

        // Obtiene todos los registros de la tabla 'pedido' desde la base de datos
        List<Pedido> list = pedidoRepository.findAll();

        // Crea un flujo (stream) a partir de la lista de pedidos
        // Luego transforma cada pedido en su comercial correspondiente (.map)
        // Elimina los comerciales duplicados (.distinct)
        // Y finalmente cuenta cuántos comerciales distintos hay (.count)
        long numComercialesDistintos = list.stream()
                .map(Pedido::getComercial)
                .distinct()
                .count();

        // Muestra en consola el número total de comerciales distintos encontrados
        System.out.println("Número total de comerciales distintos en la tabla pedido: " + numComercialesDistintos);

    }

    /**
     * 22. Calcula el máximo y el mínimo de total de pedido en un solo stream, transforma el pedido a un array de 2 double total, utiliza reduce junto con el array de double para calcular ambos valores.
     */
    @Test
    void test22() {

        // Obtiene todos los pedidos de la base de datos
        List<Pedido> list = pedidoRepository.findAll();

        // Calcula el mínimo y máximo de total de pedidos usando un solo stream y reduce
        // Se utiliza un array de double [min, max] como acumulador
        double[] minMax = list.stream()
                // Extraemos el valor total de cada pedido
                .map(Pedido::getTotal)
                // Aplicamos reduce con:
                // - Valor inicial: [Double.MAX_VALUE, Double.MIN_VALUE] (para asegurar que se actualicen correctamente)
                // - Función acumuladora: compara el valor actual (b) con los acumulados (a) y devuelve un nuevo array [min, max]
                // - Función combinadora: combina dos resultados parciales en caso de streams paralelos
                .reduce(new double[]{Double.MAX_VALUE, Double.MIN_VALUE},
                        (a, b) -> new double[]{
                                Math.min(a[0], b), // Calcula el mínimo entre el acumulado y el valor actual
                                Math.max(a[1], b)  // Calcula el máximo entre el acumulado y el valor actual
                        },
                        (a1, a2) -> new double[]{
                                Math.min(a1[0], a2[0]), // Combina mínimos de dos acumuladores
                                Math.max(a1[1], a2[1])  // Combina máximos de dos acumuladores
                        });

        // Imprime el mínimo total de pedido
        System.out.println("Mínimo total de pedido: " + minMax[0]);
        // Imprime el máximo total de pedido
        System.out.println("Máximo total de pedido: " + minMax[1]);
    }


    /**
     * 23. Calcula cuál es el valor máximo de categoría para cada una de las ciudades que aparece en cliente
     */
    @Test
    void test23() {

        // 1️⃣ Obtiene todos los clientes de la base de datos
        List<Cliente> list = clienteRepository.findAll();

        // 2️⃣ Agrupa los clientes por ciudad y calcula la máxima categoría de cada ciudad
        Map<String, Integer> maxCategoriaPorCiudad = list.stream()
                // Filtramos clientes que tengan categoría no nula para evitar NullPointerException
                .filter(c -> c.getCategoria() != null)
                // Agrupamos por ciudad
                .collect(Collectors.groupingBy(
                        Cliente::getCiudad,
                        // Calculamos el máximo de categoría en cada grupo
                        Collectors.collectingAndThen(
                                Collectors.mapping(Cliente::getCategoria,
                                        Collectors.maxBy(Integer::compareTo)),
                                // Si no hay valores (Optional.empty), ponemos 0 como valor seguro
                                opt -> opt.orElse(0)
                        )
                ));

        // 3️⃣ Imprime el resultado: Ciudad -> Máxima categoría
        maxCategoriaPorCiudad.forEach((ciudad, maxCategoria) ->
                System.out.println("Ciudad: " + ciudad + ", Máxima categoría: " + maxCategoria));
    }


    /**
     * 24. Calcula cuál es el máximo valor de los pedidos realizados
     * durante el mismo día para cada uno de los clientes. Es decir, el mismo cliente puede haber
     * realizado varios pedidos de diferentes cantidades el mismo día. Se pide que se calcule cuál es
     * el pedido de máximo valor para cada uno de los días en los que un cliente ha realizado un pedido.
     * Muestra el identificador del cliente, nombre, apellidos, la fecha y el valor de la cantidad.
     * Pista: utiliza collect, groupingBy, maxBy y comparingDouble métodos estáticos de la clase Collectors
     */
    @Test
    void test24() {

        // 1️⃣ Obtener todos los pedidos de la base de datos
        List<Pedido> list = pedidoRepository.findAll();

        // 2️⃣ Agrupar los pedidos primero por cliente, luego por fecha (java.util.Date)
        Map<Cliente, Map<Date, Optional<Pedido>>> maxPedidoPorClienteYFecha = list.stream()
                .collect(Collectors.groupingBy(
                        Pedido::getCliente, // Clave principal: cliente
                        Collectors.groupingBy(
                                Pedido::getFecha, // Clave secundaria: fecha del pedido (Date)
                                Collectors.maxBy(Comparator.comparingDouble(Pedido::getTotal)) // Pedido de mayor valor
                        )
                ));

        // 3️⃣ Recorrer el mapa para imprimir los resultados
        maxPedidoPorClienteYFecha.forEach((cliente, pedidosPorFecha) -> {
            pedidosPorFecha.forEach((fecha, pedidoOpt) -> {
                pedidoOpt.ifPresent(pedido -> System.out.println(
                        "Cliente ID: " + cliente.getId() +
                                ", Nombre: " + cliente.getNombre() +
                                " " + cliente.getApellido1() +
                                " " + cliente.getApellido2() +
                                ", Fecha: " + fecha +
                                ", Valor del pedido: " + pedido.getTotal()
                ));
            });
        });

    }

    /**
     *  25. Calcula cuál es el máximo valor de los pedidos realizados durante el mismo día para cada uno de los clientes,
     *  teniendo en cuenta que sólo queremos mostrar aquellos pedidos que superen la cantidad de 2000 €.
     *  Pista: utiliza collect, groupingBy, filtering, maxBy y comparingDouble métodos estáticos de la clase Collectors
     */
    @Test
    void test25() {

        List<Pedido> list = pedidoRepository.findAll();

        Map<Cliente, Map<Date, Optional<Pedido>>> maxPedidoPorClienteYFecha = list.stream()
                .collect(Collectors.groupingBy(
                        Pedido::getCliente,
                        Collectors.groupingBy(
                                Pedido::getFecha,
                                Collectors.filtering(
                                        p -> p.getTotal() > 2000, // filtramos solo > 2000€
                                        Collectors.maxBy(Comparator.comparingDouble(Pedido::getTotal))
                                )
                        )
                ));

        maxPedidoPorClienteYFecha.forEach((cliente, pedidosPorFecha) -> {
            pedidosPorFecha.forEach((fecha, pedidoOpt) -> {
                pedidoOpt.ifPresent(p -> System.out.println(
                        "Cliente ID: " + cliente.getId() +
                                ", Nombre: " + cliente.getNombre() + " " + cliente.getApellido1() + " " + cliente.getApellido2() +
                                ", Fecha: " + fecha +
                                ", Valor: " + p.getTotal()
                ));
            });
        });
    }

    /**
     *  26. Devuelve un listado con el identificador de cliente, nombre y apellidos
     *  y el número total de pedidos que ha realizado cada uno de clientes durante el año 2017.
     * @throws ParseException
     */
    @Test
    void test26() throws ParseException {

        List<Cliente> list = clienteRepository.findAll();

        list.forEach(cliente -> {
            long total2017 = cliente.getPedidos().stream()
                    .filter(p -> {
                        @SuppressWarnings("deprecation")
                        int year = p.getFecha().getYear() + 1900; // Date.getYear() devuelve año - 1900
                        return year == 2017;
                    })
                    .count();

            System.out.println("Cliente ID: " + cliente.getId() +
                    ", Nombre: " + cliente.getNombre() + " " + cliente.getApellido1() + " " + cliente.getApellido2() +
                    ", Total pedidos 2017: " + total2017);
        });
    }


    /**
     * 27. Devuelve cuál ha sido el pedido de máximo valor que se ha realizado cada año. El listado debe mostrarse ordenado por año.
     */
    @Test
    void test27() {

        List<Pedido> list = pedidoRepository.findAll();

        Map<Integer, Optional<Pedido>> maxPedidoPorAnio = list.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getFecha().getYear() + 1900, // Extraemos el año
                        Collectors.maxBy(Comparator.comparingDouble(Pedido::getTotal))
                ));

        maxPedidoPorAnio.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> e.getValue().ifPresent(p -> System.out.println(
                        "Año: " + e.getKey() +
                                ", Pedido ID: " + p.getId() +
                                ", Valor: " + p.getTotal()
                )));
    }


    /**
     *  28. Devuelve el número total de pedidos que se han realizado cada año.
     */
    @Test
    void test28() {

        List<Pedido> list = pedidoRepository.findAll();

        Map<Integer, Long> pedidosPorAnio = list.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getFecha().getYear() + 1900,
                        Collectors.counting()
                ));

        pedidosPorAnio.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> System.out.println(
                        "Año: " + e.getKey() + ", Total pedidos: " + e.getValue()
                ));
    }

    /**
     *  29. Devuelve los datos del cliente que realizó el pedido
     *
     *   más caro en el año 2019.
     * @throws ParseException
     */
    @Test
    void test29() throws ParseException {


        List<Pedido> list = pedidoRepository.findAll();

        list.stream()
                .filter(p -> p.getFecha().getYear() + 1900 == 2019)
                .max(Comparator.comparingDouble(Pedido::getTotal))
                .ifPresent(p -> {
                    Cliente c = p.getCliente();
                    System.out.println("Cliente ID: " + c.getId() +
                            ", Nombre: " + c.getNombre() + " " + c.getApellido1() + " " + c.getApellido2() +
                            ", Pedido ID: " + p.getId() +
                            ", Valor: " + p.getTotal());
                });
    }


    /**
     *  30. Calcula la estadísticas de total de todos los pedidos.
     *  Pista: utiliza collect con summarizingDouble
     */
    @Test
    void test30() throws ParseException {

        List<Pedido> list = pedidoRepository.findAll();

        DoubleSummaryStatistics stats = list.stream()
                .collect(Collectors.summarizingDouble(Pedido::getTotal));

        System.out.println("Número de pedidos: " + stats.getCount());
        System.out.println("Suma total: " + stats.getSum());
        System.out.println("Valor mínimo: " + stats.getMin());
        System.out.println("Valor máximo: " + stats.getMax());
        System.out.println("Valor medio: " + stats.getAverage());
    }


}
