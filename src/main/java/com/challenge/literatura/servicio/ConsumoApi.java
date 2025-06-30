package com.challenge.literatura.servicio;

import com.challenge.literatura.LibroRepository;
import com.challenge.literatura.modelo.Autor;
import com.challenge.literatura.modelo.Libro;
import com.challenge.literatura.modelo.LibroGutendex;
import com.challenge.literatura.modelo.RespuestaGutendex;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import javax.swing.event.ListDataEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Service
public class ConsumoApi {
   private final LibroRepository libroRepository;

    public ConsumoApi(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public void consumo(String valor, String funcion) {
        try {
            String urlStr = "https://gutendex.com/books?" + funcion + valor;
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            if (con.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder contenido = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    contenido.append(inputLine);
                }

                in.close();
                Gson gson = new Gson();
                RespuestaGutendex respuesta = gson.fromJson(contenido.toString(), RespuestaGutendex.class);

                List<LibroGutendex> librosGuten = respuesta.results;
                for (LibroGutendex lg : librosGuten) {
                    if (!lg.authors.isEmpty()) {
                        Autor autor = lg.authors.get(0);
                        String idioma = (lg.languages != null && !lg.languages.isEmpty()) ? lg.languages.get(0) : "desconocido";
                        Integer anioLanzamiento = lg.download_count; // o null si no sabes qué usar

                        com.challenge.literatura.modelo.Libro libro = new com.challenge.literatura.modelo.Libro(lg.title,autor.name,autor.birth_year,anioLanzamiento,idioma);
                        libroRepository.save(libro);
                        System.out.println("Libro Guardado: " + libro.getTitulo());
                    }
                }

            } else {
                System.out.println("Error en la conexión: " + con.getResponseCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void mostrarLibrosGuardados(){
        int contador = 0;
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()){
            System.out.println("No hay libros guardados");
        }else{
            System.out.println("Libros guardados en la base de datos");
            for(Libro libro : libros){
                contador++;
                System.out.println("Libro numero: "+ contador);
                System.out.println();
                System.out.println("Titulo: " + libro.getTitulo());
                System.out.println("Autor: " + libro.getAutor());
                System.out.println("Año de nacimiento del autor: " + libro.getAnioLanzamiento() );
                System.out.println("Año de publicacion: ");
                System.out.println("Idioma: ");
                System.out.println("------------------------------------------------");
            }
        }
    }
    public void mostrarAutoresGuardados(){
        int contador = 0;
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()){
            System.out.println("No hay libros guardados");
        }else{
            System.out.println("Libros guardados en la base de datos");
            for(Libro libro : libros){
                contador++;
                System.out.println("Autor numero: "+ contador);
                System.out.println("Autor: " + libro.getAutor());
                System.out.println("------------------------------------------------");
            }
        }
    }
}
