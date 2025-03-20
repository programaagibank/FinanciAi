package org.financiai.model.entities;

import org.financiai.model.enums.TipoImovel;

public class Imovel {

    private TipoImovel tipoImovel;
    private Double valorImovel;

    public Imovel(){}

    public Imovel(TipoImovel tipoImovel, Double valorImovel) {
        this.tipoImovel = tipoImovel;
        this.valorImovel = valorImovel;
    }


    public TipoImovel getTipoImovel() {
        return tipoImovel;
    }

    public void setTipoImovel(TipoImovel tipoImovel) {
        this.tipoImovel = tipoImovel;
    }

    public Double getValorImovel() {
        return valorImovel;
    }

    public void setValorImovel(Double valorImovel) {
        this.valorImovel = valorImovel;
    }


}
