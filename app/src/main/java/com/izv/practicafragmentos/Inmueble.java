package com.izv.practicafragmentos;

/**
 * Created by Alex on 03/12/2014.
 */
public class Inmueble {

    private int id;
    private String localidad, localizacion,tipo;
    private int precio;

    public Inmueble() {
    }

    public Inmueble(String localidad, String localizacion, String tipo, int precio) {
        this.localidad = localidad;
        this.localizacion = localizacion;
        this.tipo = tipo;
        this.precio = precio;
    }

    public Inmueble(int id, String localidad, String localizacion, String tipo, int precio) {
        this.id = id;
        this.localidad = localidad;
        this.localizacion = localizacion;
        this.tipo = tipo;
        this.precio = precio;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
