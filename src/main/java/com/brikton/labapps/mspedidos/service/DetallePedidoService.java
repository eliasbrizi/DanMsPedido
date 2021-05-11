package com.brikton.labapps.mspedidos.service;

import com.brikton.labapps.mspedidos.domain.DetallePedido;
import com.brikton.labapps.mspedidos.exception.RecursoNoEncontradoException;

public interface DetallePedidoService {
    //TODO de todo
    public DetallePedido agregarDetalle(DetallePedido detalle, Integer idPedido) throws RecursoNoEncontradoException;

    public void actualizarDetalle(DetallePedido detalle) throws RecursoNoEncontradoException;

    void eliminarDetalle(DetallePedido detalle) throws RecursoNoEncontradoException;

}
