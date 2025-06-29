package com.challenge.literatura;

import com.challenge.literatura.servicio.ConsumoApi;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConvertirJson {
    private final ConsumoApi consumoApi;
    private int opcion;
    private String funcionBuscar = "search=";
    private String nombre;
    private Scanner teclado = new Scanner(System.in);
    private Scanner teclado1 = new Scanner(System.in);
    private Scanner teclado2 = new Scanner(System.in);

    public ConvertirJson(ConsumoApi consumoApi) {
        this.consumoApi = consumoApi;
    }

    public void menu(){
        System.out.println("""
               ********************************************
                Ingrese una opcion
               
                1 buscar por nombre del libro
                2 buscar por autor
                3
                4
                5 Salir
               """);
        opcion = teclado2.nextInt();
        switch (opcion){

            case 1:{
                System.out.println("Ingrese un nombre de un libro");
                nombre= teclado.nextLine();
                consumoApi.consumo(nombre,funcionBuscar);
                break;
            }
            case 2:{
                break;
            }
            case 3:{
                break;
            }
            case 4:{
                break;
            }
            case 5:{
                System.out.println("Saliendo");
                break;
            }
        }
    }
}
