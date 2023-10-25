package com.example.t1bicing;

import java.io.Serializable;

public class Estacion implements Serializable {

    private int id;
    private String nombre;
    private String configuracionFisica;
    private double latitud;
    private double longitud;
    private double altitud;
    private String direccion;
    private String codigoPostal;
    private int capacidad;
    private boolean esEstacionCarga;
    private double distanciaCercana;
    private boolean esCompatibleCodigoRide;
    private EstadoEstacion estadoEstacion;

    private String estacionMasCercana = "";

    private boolean favorito = false;

    public Estacion(int id, String nombre, String configuracionFisica, double latitud, double longitud, double altitud, String direccion, String codigoPostal, int capacidad, boolean esEstacionCarga, double distanciaCercana, boolean esCompatibleCodigoRide, EstadoEstacion estadoEstacion) {
        this.id = id;
        this.nombre = nombre;
        this.configuracionFisica = configuracionFisica;
        this.latitud = latitud;
        this.longitud = longitud;
        this.altitud = altitud;
        this.direccion = direccion;
        this.codigoPostal = codigoPostal;
        this.capacidad = capacidad;
        this.esEstacionCarga = esEstacionCarga;
        this.distanciaCercana = distanciaCercana;
        this.esCompatibleCodigoRide = esCompatibleCodigoRide;
        this.estadoEstacion = estadoEstacion;
    }

    public Estacion() {

    }

    // getters y setters para cada atributo

    public String getEstacionMasCercana() {
        return estacionMasCercana;
    }

    public void setEstacionMasCercana(String estacionMasCercana) {
        this.estacionMasCercana = estacionMasCercana;
    }
    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getConfiguracionFisica() {
        return configuracionFisica;
    }

    public void setConfiguracionFisica(String configuracionFisica) {
        this.configuracionFisica = configuracionFisica;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getAltitud() {
        return altitud;
    }

    public void setAltitud(double altitud) {
        this.altitud = altitud;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public boolean isEsEstacionCarga() {
        return esEstacionCarga;
    }

    public void setEsEstacionCarga(boolean esEstacionCarga) {
        this.esEstacionCarga = esEstacionCarga;
    }

    public double getDistanciaCercana() {
        return distanciaCercana;
    }

    public void setDistanciaCercana(double distanciaCercana) {
        this.distanciaCercana = distanciaCercana;
    }

    public boolean isEsCompatibleCodigoRide() {
        return esCompatibleCodigoRide;
    }

    public void setEsCompatibleCodigoRide(boolean esCompatibleCodigoRide) {
        this.esCompatibleCodigoRide = esCompatibleCodigoRide;
    }

    public EstadoEstacion getEstadoEstacion() {
        return estadoEstacion;
    }

    public void setEstadoEstacion(EstadoEstacion estadoEstacion) {
        this.estadoEstacion = estadoEstacion;
    }



    public String getInfo() {
        String info = "Tipo de estación: " + this.configuracionFisica + "\n";
        info += "Dirección: " + this.direccion + "\n";
        info += "Número de bicicletas disponibles: " + estadoEstacion.getNumeroBicicletas() + "\n";
        info += "Número de anclajes disponibles: " + estadoEstacion.getNumeroAnclajes() + "\n";
        info += "Estado de la estación: " + estadoEstacion.getStatus() + "\n";
        info += "Estacion mas cercana: " + getEstacionMasCercana() + "\n";

        return info;
    }

    public String getInfoNota() {
        String info = "Tipo de estación: " + this.configuracionFisica + "\n";
        info += "Número de bicicletas disponibles: " + estadoEstacion.getNumeroBicicletas() + "\n";
        info += "Número de anclajes disponibles: " + estadoEstacion.getNumeroAnclajes() + "\n";
        info += "Estacion mas cercana: " + getEstacionMasCercana() + "\n";

        return info;
    }

    @Override
    public String toString() {
        return "Estacion{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", configuracionFisica='" + configuracionFisica + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", altitud=" + altitud +
                ", direccion='" + direccion + '\'' +
                ", codigoPostal='" + codigoPostal + '\'' +
                ", capacidad=" + capacidad +
                ", esEstacionCarga=" + esEstacionCarga +
                ", distanciaCercana=" + distanciaCercana +
                ", esCompatibleCodigoRide=" + esCompatibleCodigoRide +
                ", estadoEstacion=" + estadoEstacion +
                '}';
    }

    public String stringEstacion() {
        String info = "Id: " + getId() + "\n";
        info += "Configuracion Fisica: " + getConfiguracionFisica() + "\n";
        info += "Latitud: " + getLatitud() + "\n";
        info += "Longitud: " + getLongitud() + "\n";
        info += "Altitud: " + getAltitud() + "\n";
        info += "Direccion: " + getDireccion() + "\n";
        info += "Codigo postal: " + getCodigoPostal() + "\n";
        info += "Capacidad: " + getCapacidad() + "\n";
        info += "Tiene estacion de Carga: " + isEsEstacionCarga() + "\n";
        info += "Distancia cercana: " + getDistanciaCercana() + "\n";
        info += "Es compatible Codigo Ride: " + isEsCompatibleCodigoRide() + "\n";
        info += "Estacion mas cercana: " + getEstacionMasCercana() + "\n";
        info += "Estado Estacion: " + getEstadoEstacion().getStatus() + "\n";
        info += "Numero de Anclajes: " + getEstadoEstacion().getNumeroAnclajes() + "\n";
        info += "Numero de bicicletas: " + getEstadoEstacion().getNumeroBicicletas() + "\n";
        info += "Disponibilidad bicis mecanicas: " + getEstadoEstacion().getDisponibilidadDeBicicletas().getMecánicas() + "\n";
        info += "Disponibilidad ebikes: " + getEstadoEstacion().getDisponibilidadDeBicicletas().getEBikes() + "\n";
        return info;
    }


}
