package com.brikton.labapps.mspedidos.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.brikton.labapps.mspedidos.dao.PedidoRepository;
import com.brikton.labapps.mspedidos.domain.DetallePedido;
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
    public Pedido actualizarEstadoPedido(Integer idPedido, EstadoPedido nuevoEstadoPedido) throws RecursoNoEncontradoException, GeneraSaldoDeudorException {
        Optional<Pedido> pedido = null;
		pedido = pedidoRepository.findById(idPedido);
		if (pedido.isPresent()) {
			/*
			Logica de actualizar
			*/
			//TODO b y c, y ademas esto solo es para pasar a confirmado
			if (existeStock(pedido.get())){
				if (verificaSaldo(pedido.get())){
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
    
	private Boolean existeStock(Pedido p){
		Boolean ok = true;
		List<DetallePedido> detalles = p.getDetalle();
		for (DetallePedido dPedido : detalles) {
			if (productoSrv.stockDisponible(dPedido.getProducto()) 
				< dPedido.getCantidad())
			{
				ok = false;
				break;
			}
		}
		return ok;
	}

	private boolean verificaSaldo(Pedido p){
		//TODO necesito conocer el cliente del pedido??
		return true;
	}

	@Override
	public ArrayList<Pedido> pedidosPorObra(Integer idObra) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Pedido> pedidosPorEstado(EstadoPedido valueOf) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Pedido> pedidosPorCliente(Integer idCliente) {
		// TODO Auto-generated method stub
		return null;
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
}
