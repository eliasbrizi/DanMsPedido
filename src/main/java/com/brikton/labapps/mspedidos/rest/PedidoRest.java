package com.brikton.labapps.mspedidos.rest;

import com.brikton.labapps.mspedidos.domain.DetallePedido;
import com.brikton.labapps.mspedidos.domain.Pedido;
import com.brikton.labapps.mspedidos.exception.RiesgoException;
import com.brikton.labapps.mspedidos.service.PedidoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedido")
// @Api(value = "PedidoRest", description = "Permite gestionar los pedidos de la empresa")
public class PedidoRest {
    @Autowired
    PedidoService pedidoService;

    @PostMapping
    // @ApiResponses(value = {
    //     @ApiResponse(code = 200, message = "Creado correctamente"),
    //     @ApiResponse(code = 400, message = "El pedido no es correcto")})
    public ResponseEntity<?> crearPedido(@RequestBody Pedido nuevoPedido){
        
        /*
        Valido que obra, detalle y el detalle tenga productos y cantidad
        */

        Pedido creado = null;
        if (validarPedido(nuevoPedido)) {
            try {
                creado = this.pedidoService.crearPedido(nuevoPedido);
            } catch (RiesgoException e2) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e2.getMessage());
            }
            return ResponseEntity.ok(creado);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    private Boolean validarPedido(Pedido p) {
        Boolean valido = true;
        if (p.getDetalle() != null) {
            for(DetallePedido d : p.getDetalle()) {
                if ((d.getProducto() == null) || (d.getCantidad() == null))
                    valido = false;
            }
        } else {
            valido = false;
        }
        return valido;
    }

    @PatchMapping(path = "/{id}")
    public Pedido actualizarEstadoPedido(@PathVariable Integer id){
        //TODO
        Pedido pedido = null;
        return pedido;
    }
}
