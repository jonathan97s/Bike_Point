package com.example.t1bicing;

import java.io.Serializable;

public class DisponibilidadDeBicicletas implements Serializable {
    private int mecánicas;
    private int eBikes;

    public DisponibilidadDeBicicletas(int mecánicas, int eBikes) {
        this.mecánicas = mecánicas;
        this.eBikes = eBikes;
    }

    public int getMecánicas() {
        return mecánicas;
    }

    public int getEBikes() {
        return eBikes;
    }

    public void setMecánicas(int mecánicas) {
        this.mecánicas = mecánicas;
    }

    public void setEBikes(int eBikes) {
        this.eBikes = eBikes;
    }
}
