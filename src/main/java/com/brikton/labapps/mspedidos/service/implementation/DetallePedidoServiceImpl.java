package com.brikton.labapps.mspedidos.service.implementation;

import com.brikton.labapps.mspedidos.dao.DetallePedidoRepository;
import com.brikton.labapps.mspedidos.domain.DetallePedido;
import com.brikton.labapps.mspedidos.domain.Pedido;
import com.brikton.labapps.mspedidos.exception.RecursoNoEncontradoException;
import com.brikton.labapps.mspedidos.service.DetallePedidoService;
import com.brikton.labapps.mspedidos.service.PedidoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService {

    @Autowired
    DetallePedidoRepository detallePedidoRepository;

    @Autowired
    PedidoService pedidoService;

    @Override
    public DetallePedido agregarDetalle(DetallePedido detalle, Integer idPedido) throws RecursoNoEncontradoException {
        Pedido pedido = pedidoService.getPedido(idPedido);
        pedido.agregarDetalle(detalle);
        pedidoService.actualizarPedido(pedido);
        return detalle;
    }

    @Override
    public void actualizarDetalle(DetallePedido detalle) throws RecursoNoEncontradoException {
        if (detallePedidoRepository.existsById(detalle.getId())){
            detallePedidoRepository.save(detalle);
        } else {
            throw new RecursoNoEncontradoException("Detalle de pedido no encontrado: ", detalle.getId());
        }
    }

    @Override
    public void eliminarDetalle(DetallePedido detalle) throws RecursoNoEncontradoException {
        if (detallePedidoRepository.existsById(detalle.getId())){
            detallePedidoRepository.delete(detalle);
        } else {
            throw new RecursoNoEncontradoException("Detalle de pedido no encontrado: ", detalle.getId());
        }
    }
    
}
