package com.brikton.labapps.mspedidos.service.implementation;

import java.util.Optional;

import com.brikton.labapps.mspedidos.dao.PedidoRepository;
import com.brikton.labapps.mspedidos.domain.EstadoPedido;
import com.brikton.labapps.mspedidos.domain.Obra;
import com.brikton.labapps.mspedidos.domain.Pedido;
import com.brikton.labapps.mspedidos.domain.Producto;
import com.brikton.labapps.mspedidos.exception.GeneraSaldoDeudorException;
import com.brikton.labapps.mspedidos.exception.RecursoNoEncontradoException;
import com.brikton.labapps.mspedidos.exception.RiesgoException;
import com.brikton.labapps.mspedidos.service.ClienteService;
import com.brikton.labapps.mspedidos.service.PedidoService;
import com.brikton.labapps.mspedidos.service.ProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    PedidoRepository pedidoRepository;
    
    @Autowired
	ClienteService clienteSrv;

    @Autowired
    ProductoService productoSrv;

    @Override
    public Pedido crearPedido(Pedido p) throws RiesgoException {
        // TODO Auto-generated method stub
        boolean hayStock = p.getDetalle()
		.stream()
		.allMatch(dp -> verificarStock(dp.getProducto(),dp.getCantidad()));
		
		Double totalOrden = p.getDetalle()
				.stream()
				.mapToDouble( dp -> dp.getCantidad() * dp.getPrecio())
				.sum();
		
		Double saldoCliente = clienteSrv.deudaCliente(p.getObra());		
		Double nuevoSaldo = saldoCliente - totalOrden;
		
		Boolean generaDeuda= nuevoSaldo<0;
		if(hayStock ) {
				if(!generaDeuda || (generaDeuda && this.esDeBajoRiesgo(p.getObra(),nuevoSaldo) ))  {
					p.setEstado(EstadoPedido.NUEVO);
				} else {
					throw new RiesgoException("riesgo");
				}
		} else {
			p.setEstado(EstadoPedido.PENDIENTE);
		}
		return this.pedidoRepository.save(p);
    }

    private boolean esDeBajoRiesgo(Obra obra, Double nuevoSaldo) {
		return false;
	}

	private Boolean verificarStock(Producto producto, Integer cantidad) {
        return productoSrv.stockDisponible(producto)>=cantidad;
    }

    @Override
    public Pedido actualizarEstadoPedido(Integer idPedido, EstadoPedido nuevoEstadoPedido) throws RecursoNoEncontradoException {
        Optional<Pedido> pedido = null;
		pedido = pedidoRepository.findById(idPedido);
		if (pedido.isPresent()) {
			/*
			Logica de actualizar
			*/
			//TODO b y c, y ademas esto solo es para pasar a confirmado
			if (existeStock(pedido.get())){
				if (/* b o c */){
					pedido.get().setEstado(EstadoPedido.ACEPTADO);
					pedidoRepository.save(pedido.get());
				} else {
					pedido.get().setEstado(EstadoPedido.RECHAZADO);
					pedidoRepository.save(pedido.get());
					throw new GeneraSaldoDeudorException("El pedido" + idPedido +" genera saldo deudor");
				}
			} else {
				pedido.get().setEstado(EstadoPedido.PENDIENTE);
				pedidoRepository.save(pedido.get());
			}
			return pedido.get();
		}
		else throw new RecursoNoEncontradoException("No se encontro el pedido: ", idPedido);
    }
    
	private boolean existeStock(Pedido p){
		//TODO
		return true;
	}
}