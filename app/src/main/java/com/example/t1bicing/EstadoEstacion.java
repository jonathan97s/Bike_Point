package com.example.t1bicing;

import java.io.Serializable;

public class EstadoEstacion implements Serializable {

    private int id;
    private int numeroBicicletas;
    private int numeroAnclajes;
    private String status;

    private DisponibilidadDeBicicletas disponibilidadDeBicicletas;

    public EstadoEstacion()  {
    }

    public EstadoEstacion(int id, int numeroBicicletas, int numeroAnclajes, String status) {
        this.id = id;
        this.numeroBicicletas = numeroBicicletas;
        this.numeroAnclajes = numeroAnclajes;
        this.status = status;
    }

    public DisponibilidadDeBicicletas getDisponibilidadDeBicicletas() {
        return disponibilidadDeBicicletas;
    }

    public void setDisponibilidadDeBicicletas(DisponibilidadDeBicicletas disponibilidadDeBicicletas) {
        this.disponibilidadDeBicicletas = disponibilidadDeBicicletas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumeroBicicletas() {
        return numeroBicicletas;
    }

    public void setNumeroBicicletas(int numeroBicicletas) {
        this.numeroBicicletas = numeroBicicletas;
    }

    public int getNumeroAnclajes() {
        return numeroAnclajes;
    }

    public void setNumeroAnclajes(int numeroAnclajes) {
        this.numeroAnclajes = numeroAnclajes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

