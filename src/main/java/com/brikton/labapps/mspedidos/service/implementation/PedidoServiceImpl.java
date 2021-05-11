package com.brikton.labapps.mspedidos.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.brikton.labapps.mspedidos.dao.PedidoRepository;
import com.brikton.labapps.mspedidos.domain.EstadoPedido;
import com.brikton.labapps.mspedidos.domain.Obra;
import com.brikton.labapps.mspedidos.domain.Pedido;
import com.brikton.labapps.mspedidos.domain.Producto;
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
        boolean hayStock = p.getDetalle()
		.stream()
		.allMatch(dp -> verificarStock(dp.getProducto(),dp.getCantidad()));
		
		Double totalOrden = p.getDetalle()
				.stream()
				.mapToDouble( dp -> dp.getCantidad() * dp.getPrecio())
				.sum();
		
		Double saldoCliente = 0.0;
		try {
			saldoCliente = clienteSrv.deudaCliente(p.getObra());
		} catch (RecursoNoEncontradoException e) {
			e.printStackTrace();
		}		
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
	
    @Override
    public Pedido actualizarEstadoPedido(Integer idPedido, EstadoPedido nuevoEstadoPedido) throws RecursoNoEncontradoException, RiesgoException {
        Optional<Pedido> pedido = null;
		pedido = pedidoRepository.findById(idPedido);
		if (pedido.isPresent()) {
			/*
			Logica de actualizar
			*/
			Pedido p = pedido.get();
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
					p.setEstado(EstadoPedido.ACEPTADO);
				} else {
					p.setEstado(EstadoPedido.RECHAZADO);
					pedidoRepository.save(p);
					throw new RiesgoException("El pedido" + idPedido +" genera saldo deudor");
				}
			} else {
				p.setEstado(EstadoPedido.PENDIENTE);
			}
			return this.pedidoRepository.save(p);
		}
		else throw new RecursoNoEncontradoException("No se encontro el pedido: ", idPedido);
    }
    
	@Override
	public ArrayList<Pedido> pedidosPorObra(Obra obra) {
		// TODO Auto-generated method stub
		return pedidoRepository.getPedidosPorObra(obra.getId());
	}
	
	@Override
	public ArrayList<Pedido> pedidosPorEstado(EstadoPedido estadoPedido) {
		// TODO Auto-generated method stub
		return pedidoRepository.getPedidosPorEstado(estadoPedido.ordinal());
	}
	
	@Override
	public ArrayList<Pedido> pedidosPorCliente(Integer idCliente) throws RecursoNoEncontradoException {
		// TODO Auto-generated method stub
		/*
		Pido obras segun idCliente y despues pido los pedidos segun las obras
		*/
		List<Obra> obras = clienteSrv.getObrasCliente(idCliente);
		ArrayList<Pedido> pedidos = new ArrayList<>();
		for(Obra o: obras) pedidos.addAll(pedidoRepository.getPedidosPorObra(o.getId()));
		return pedidos;
	}
	
	@Override
	public Pedido getPedido(Integer idPedido) throws RecursoNoEncontradoException {
		Optional<Pedido> pedido = pedidoRepository.findById(idPedido);
		if (pedido.isPresent()) return pedido.get();
		else throw new RecursoNoEncontradoException("No se encontro el pedido: ", idPedido);
	}
	
	@Override
	public void actualizarPedido(Pedido p){
		pedidoRepository.save(p);
	}
	
	private boolean esDeBajoRiesgo(Obra obra, Double nuevoSaldo) {
		//TODO riesgo bcra
		return false;
	}
	
	private Boolean verificarStock(Producto producto, Integer cantidad) {
		return productoSrv.stockDisponible(producto)>=cantidad;
	}
}
