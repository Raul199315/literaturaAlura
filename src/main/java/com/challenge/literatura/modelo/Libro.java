package com.challenge.literatura.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jdk.jfr.Enabled;
@Entity
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String autor;
    private Integer anionacimiento;
    private Integer anioMuerte;
    private Integer descargas;
    private String idioma;

    public Libro(Integer anioMuerte, Integer anionacimiento, String autor, Integer descargas, String idioma, String titulo) {
        this.anioMuerte = anioMuerte;
        this.anionacimiento = anionacimiento;
        this.autor = autor;
        this.descargas = descargas;
        this.idioma = idioma;
        this.titulo = titulo;
    }

    public Libro() {
    }

    public Integer getDescargas() {
        return descargas;
    }

    public int getAnionacimiento() {
        return anionacimiento;
    }

    public void setAnionacimiento(int anionacimiento) {
        this.anionacimiento = anionacimiento;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getAnioMuerte() {
        return anioMuerte;
    }

    public void setAnioMuerte(Integer anioMuerte) {
        this.anioMuerte = anioMuerte;
    }

    public void setAnionacimiento(Integer anionacimiento) {
        this.anionacimiento = anionacimiento;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }
}
