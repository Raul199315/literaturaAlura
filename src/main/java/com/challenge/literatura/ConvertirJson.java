package com.challenge.literatura;

import com.challenge.literatura.servicio.ConsumoApi;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConvertirJson {
    private final ConsumoApi consumoApi;
    private boolean bandera = true;
    private final String funcionBuscar = "search=";
    private final String funcionBuscarIdioma = "languages=";

    private final Scanner teclado = new Scanner(System.in);

    public ConvertirJson(ConsumoApi consumoApi) {
        this.consumoApi = consumoApi;
    }

    public void menu() {
        while (bandera) {
            System.out.println("""
               ********************************************
                MENÚ PRINCIPAL
               
               1️ Buscar un libro en la API
               2️ Lista de libros registrados
               3 Lista de autores registrados
               4️ Autores vivos en determinado año
               5️ Lista de libros por idioma
               6️ Salir
               """);

            String entrada = teclado.nextLine().trim();
            int opcion = -1;

            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println(" Opción inválida. Por favor ingresa un número.");
                continue;
            }

            switch (opcion) {
                case 1 -> buscarLibro();
                case 2 -> consumoApi.mostrarLibrosGuardados();
                case 3 -> consumoApi.mostrarAutoresConCantidadDeLibrosYTitulos();
                case 4 -> consumoApi.pedirYMostrarAutoresVivosDesdeConsola();
                case 5 -> consumoApi.pedirYMostrarLibrosPorIdioma();
                case 6 ->
                {
                    System.out.println("👋 Saliendo... ¡Gracias por usar el sistema!");
                    bandera = false;
                }
                default -> System.out.println("⚠️ Opción no reconocida.");
            }
        }
    }

    private void buscarLibro() {
        System.out.println("""
                🔍 Buscar por:
                1️ Título del libro o nombre de autor
                2️ Idioma del libro
                3️ Volver al menú anterior
                """);

        String entrada = teclado.nextLine().trim();
        int subOpcion = -1;

        try {
            subOpcion = Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida.");
            return;
        }

        switch (subOpcion) {
            case 1 -> {
                System.out.print(" Ingresa el título o autor: ");
                String nombre = teclado.nextLine();
                consumoApi.consumo(nombre, funcionBuscar);
            }
            case 2 -> {
                System.out.println("""
                         Idiomas disponibles:
                        es - Español
                        en - Inglés
                        fr - Francés
                        de - Alemán
                        it - Italiano
                        pt - Portugués
                        """);
                System.out.print("Ingresa el código del idioma: ");
                String idioma = teclado.nextLine();
                consumoApi.consumo(idioma, funcionBuscarIdioma);
            }
            case 3 -> System.out.println("↩️ Volviendo al menú principal...");
            default -> System.out.println("⚠️ Opción no válida.");
        }
    }

    private void mostrarLibrosPorIdioma() {
        System.out.println("⚠️ Esta opción aún no está implementada.");
    }
}