package com.brikton.labapps.mspedidos.domain;

public class EstadoPedido {

    private String estadoStr;
    private Integer id;
    
    public EstadoPedido(Integer id,String estadoStr){
        this.estadoStr = estadoStr;
        this.id=id;
    }

    public String getEstadoStr() {
        return estadoStr;
    }

    public void setEstadoStr(String estadoStr) {
        this.estadoStr = estadoStr;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    
}
