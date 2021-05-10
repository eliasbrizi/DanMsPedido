package com.brikton.labapps.mspedidos.service;

import com.brikton.labapps.mspedidos.domain.DetallePedido;

public interface DetallePedidoService {
    //TODO de todo
    void agregarDetalle(DetallePedido detalle, Integer idPedido);

    void actualizarDetalle(DetallePedido detalle);

    void eliminarDetalle(DetallePedido detalle);

}
