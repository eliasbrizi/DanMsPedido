package com.brikton.labapps.mspedidos.service;

import com.brikton.labapps.mspedidos.domain.Pedido;
import com.brikton.labapps.mspedidos.exception.RiesgoException;

public interface PedidoService {

    public Pedido crearPedido(Pedido p) throws RiesgoException;
    
}
