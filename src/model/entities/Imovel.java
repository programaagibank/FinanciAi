package model.entities;

public class Imovel {

    private int id;
    private TipoImovel tipoImovel;
    private Double valorImovel;

    public Imovel(){}

    public Imovel(int id, TipoImovel tipoImovel, Double valorImovel) {
        this.id = id;
        this.tipoImovel = tipoImovel;
        this.valorImovel = valorImovel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
