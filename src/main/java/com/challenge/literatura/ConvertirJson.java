package com.challenge.literatura;

import com.challenge.literatura.servicio.ConsumoApi;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConvertirJson {
    private final ConsumoApi consumoApi;
    private int opcion;
    private boolean bandera = true;
    private String funcionBuscar = "search=";
    private String funcionBuscarIdioma = "languages=";

    private String nombre;
    private Scanner teclado = new Scanner(System.in);
    private Scanner teclado1 = new Scanner(System.in);
    private Scanner teclado2 = new Scanner(System.in);

    public ConvertirJson(ConsumoApi consumoApi) {
        this.consumoApi = consumoApi;
    }

    public void menu(){
        while (bandera){
            System.out.println("""
               ********************************************
                Ingrese una opcion
               
                1 buscar un libro en la Api
                2 lista de libros registrados
                3 lista de autores registrados
                4 lista de autores vivos en determinado año
                5 lista de libros por idioma
                6 Salir
               """);
            opcion = teclado2.nextInt();
            switch (opcion){

                case 1:{
                    int option = 0;
                    System.out.println("""
                            1 buscar por titulo del libro o autor
                            2 buscar libros por idioma
                            3 volver al menu anterior
                                                      """);
                    option = teclado2.nextInt();

                    if (option == 1){
                        System.out.println("Ingrese un nombre de un libro");
                        nombre= teclado.nextLine();
                        consumoApi.consumo(nombre,funcionBuscar);
                    } else if (option == 2) {
                        System.out.println("""
                                Ingrese
                                es para Español
                                fr para Frances
                                en para Ingles
                                de Aleman
                                it Italiano
                                pt Portuges
                                """);
                        nombre = teclado.nextLine();
                        consumoApi.consumo(nombre,funcionBuscarIdioma);
                    }


                    break;
                }
                case 2:{
                    consumoApi.mostrarLibrosGuardados();
                    break;
                }
                case 3:{
                    consumoApi.mostrarAutoresGuardados();
                    break;
                }
                case 4:{
                    System.out.println("Ingrese el año deseado");
                    opcion = teclado1.nextInt();
                    consumoApi.mostrarAutoresVivosEn(opcion);

                    break;
                }
                case 5:{
                    System.out.println("dasosug");
                    break;
                }
                case 6:{
                    System.out.println("Saliendo.........................");
                    bandera = false;
                }
            }
        }
    }
}
