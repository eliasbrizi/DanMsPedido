package com.brikton.labapps.mspedidos.domain;

import java.time.Instant;
import java.util.List;

public class Pedido {

    private Integer id;
	private Instant fechaPedido;
    private Obra obra;
    private EstadoPedido estado;
    private List<DetallePedido> detalle;

    
    public Pedido(){
        this.obra = null;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Instant getFechaPedido() {
        return fechaPedido;
    }
    public void setFechaPedido(Instant fechaPedido) {
        this.fechaPedido = fechaPedido;
    }
    public Obra getObra() {
        return obra;
    }
    public void setObra(Obra obra) {
        this.obra = obra;
    }
    public List<DetallePedido> getDetalle() {
        return detalle;
    }
    public void setDetalle(List<DetallePedido> detalle) {
        this.detalle = detalle;
    }
    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }


}
