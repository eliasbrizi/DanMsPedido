package com.brikton.labapps.mspedidos.domain;


public class DetallePedido {
	
	private Integer id;
	private Material material;
	private Double cantidad;

	public DetallePedido(Material material, int id, double cantidad) {
		this.id = id;
		this.material = material;
		this.cantidad = cantidad;
    }
    public DetallePedido() {
    }
    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Double getCantidad() {
		return cantidad;
	}
	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}
	public Material getMaterial() {
		return material;
	}
	public void setMaterial(Material material) {
		this.material = material;
	}
	public Double getPrecio(){
		//return material.getPrecio() * cantidad;
		return cantidad;
	}	
}
