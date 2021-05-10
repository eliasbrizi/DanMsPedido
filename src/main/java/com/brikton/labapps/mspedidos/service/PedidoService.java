package com.brikton.labapps.mspedidos.service;

import java.util.Optional;

import com.brikton.labapps.mspedidos.domain.EstadoPedido;
import com.brikton.labapps.mspedidos.domain.Pedido;
import com.brikton.labapps.mspedidos.exception.RecursoNoEncontradoException;
import com.brikton.labapps.mspedidos.exception.RiesgoException;

public interface PedidoService {

    public Pedido crearPedido(Pedido p) throws RiesgoException;
    public Pedido actualizarEstadoPedido(Integer id, EstadoPedido estadoPedido) throws RecursoNoEncontradoException;
    
}
