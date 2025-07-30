package com.challenge.literatura.servicio;

import com.challenge.literatura.LibroRepository;
import com.challenge.literatura.modelo.Autor;
import com.challenge.literatura.modelo.Libro;
import com.challenge.literatura.modelo.LibroGutendex;
import com.challenge.literatura.modelo.RespuestaGutendex;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;


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
                String contenido = leerContenido(con);
                Gson gson = new Gson();
                RespuestaGutendex respuesta = gson.fromJson(contenido, RespuestaGutendex.class);

                for (LibroGutendex lg : respuesta.results) {
                    if (!lg.authors.isEmpty()) {
                        Autor autor = lg.authors.get(0);

                        String idioma = (lg.languages != null && !lg.languages.isEmpty()) ? lg.languages.get(0) : "desconocido";
                        Libro libro = new Libro(
                                autor.death_year,
                                autor.birth_year,
                                autor.name,
                                lg.download_count,
                                idioma,
                                lg.title
                        );
                        System.out.println("📚 Libro encontrado:");
                        System.out.println("Título     : " + libro.getTitulo());
                        System.out.println("Autor      : " + libro.getAutor());
                        System.out.println("Idioma     : " + libro.getIdioma());
                        System.out.println("Año Nac.   : " + libro.getAnionacimiento());
                        System.out.println("Descargas: " + lg.download_count);

                        System.out.println("-------------------------------");

                        // Evitar duplicados
                        List<Libro> existente = libroRepository.findByTituloAndAutor(libro.getTitulo(), libro.getAutor());
                        if (existente.isEmpty()) {
                            libroRepository.save(libro);
                            System.out.println("✅ Libro guardado: " + libro.getTitulo());
                        } else {
                            System.out.println("⚠️ Libro ya existe: " + libro.getTitulo());
                        }
                    }
                }
            } else {
                System.out.println("Error en la conexión: " + con.getResponseCode());
            }
        } catch (Exception e) {
            System.out.println("⚠️ Ocurrió un error al consumir la API:");
            e.printStackTrace();
        }
    }

    private String leerContenido(HttpURLConnection con) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder contenido = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            contenido.append(inputLine);
        }

        in.close();
        return contenido.toString();
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
                System.out.println("Titulo: " + libro.getTitulo());
                System.out.println("Autor: " + libro.getAutor());
                System.out.println("Descargas: " + libro.getDescargas());
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
    public void mostrarAutoresVivosEn(int anio) {
        List<Libro> autoresVivos = libroRepository.findAll().stream()
                .filter(libro -> {
                    Integer nacimiento = libro.getAnionacimiento();
                    Integer muerte = libro.getAnioMuerte();
                    return nacimiento != null && anio >= nacimiento && (muerte == null || anio < muerte);
                })
                .collect(Collectors.toList());

        System.out.println("\n🗓️ Autores vivos en el año " + anio + ":");
        System.out.println("============================================");

        if (autoresVivos.isEmpty()) {
            System.out.println("⚠️ No se encontraron autores vivos.");
            return;
        }

        int contador = 0;
        for (Libro libro : autoresVivos) {
            contador++;
            imprimirAutorVivo(libro, contador);
        }
    }

    public void pedirYMostrarAutoresVivosDesdeConsola() {
        Scanner scanner = new Scanner(System.in);
        Integer anio = null;

        while (anio == null) {
            System.out.print("\n📅 Ingresa el año (solo números): ");
            String entrada = scanner.nextLine().trim();

            try {
                anio = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("❌ Año inválido. Intenta nuevamente.");
            }
        }

        mostrarAutoresVivosEn(anio);
    }
    private void imprimirAutorVivo(Libro libro, int contador) {
        System.out.printf("👤 Autor #%d: %s\n", contador, libro.getAutor());
        System.out.println("📘 Título    : " + libro.getTitulo());
        System.out.println("🌐 Idioma    : " + libro.getIdioma());
        System.out.println("🎂 Nació     : " + libro.getAnionacimiento());
        System.out.println("💀 Falleció  : " + (libro.getAnioMuerte() != null ? libro.getAnioMuerte() : "aún vivo"));
        System.out.println("--------------------------------------------");
    }

    public void mostrarAutoresConCantidadDeLibrosYTitulos() {
        Map<String, List<Libro>> librosPorAutor = libroRepository.findAll().stream()
                .collect(Collectors.groupingBy(Libro::getAutor));

        if (librosPorAutor.isEmpty()) {
            System.out.println("⚠️ No hay autores registrados.");
        } else {
            System.out.println("\n🧾 Autores con cantidad de libros y sus títulos:");
            System.out.println("==============================================");

            int contador = 1;
            for (Map.Entry<String, List<Libro>> entry : librosPorAutor.entrySet()) {
                String autor = entry.getKey();
                List<Libro> libros = entry.getValue();

                System.out.println("👤 Autor #" + contador);
                System.out.println("🖋️ Nombre     : " + autor);
                System.out.println("📚 Cantidad   : " + libros.size());
                System.out.println("📘 Títulos    :");

                for (Libro libro : libros) {
                    System.out.println("   - " + libro.getTitulo());
                }

                System.out.println("----------------------------------------------");
                contador++;
            }
        }
    }
    public void pedirYMostrarLibrosPorIdioma() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\n🌐 Ingresa el idioma que deseas consultar: ");
        System.out.println("""
                         Idiomas disponibles:
                        es - Español
                        en - Inglés
                        fr - Francés
                        de - Alemán
                        it - Italiano
                        pt - Portugués
                        """);
        String idiomaElegido = scanner.nextLine().trim().toLowerCase();

        List<Libro> librosPorIdioma = libroRepository.findAll().stream()
                .filter(libro -> libro.getIdioma() != null &&
                        libro.getIdioma().toLowerCase().contains(idiomaElegido))
                .collect(Collectors.toList());

        System.out.println("\n📚 Libros encontrados en idioma: " + idiomaElegido);
        System.out.println("==============================================");

        if (librosPorIdioma.isEmpty()) {
            System.out.println("⚠️ No se encontraron libros para ese idioma.");
        } else {
            int contador = 1;
            for (Libro libro : librosPorIdioma) {
                System.out.println("🔖 Libro #" + contador);
                System.out.println("📘 Título    : " + libro.getTitulo());
                System.out.println("🖋️ Autor     : " + libro.getAutor());
                System.out.println("📥 Descargas : " + libro.getDescargas());
                System.out.println("🌐 Idioma    : " + libro.getIdioma());
                System.out.println("----------------------------------------------");
                contador++;
            }
        }
    }


}
