package com.brikton.labapps.mspedidos.rest;

import com.brikton.labapps.mspedidos.domain.DetallePedido;
import com.brikton.labapps.mspedidos.exception.RecursoNoEncontradoException;
import com.brikton.labapps.mspedidos.service.DetallePedidoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/detallepedido")
public class DetallePedidoRest {
    
    @Autowired
    DetallePedidoService service;

    @PostMapping()
    public ResponseEntity<?> agregarItem(@RequestBody DetallePedido detalle, @RequestParam Integer idPedido){
        try {
            detalle = service.agregarDetalle(detalle,idPedido);
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok(detalle);
    }

    @PutMapping
    public ResponseEntity<?> modificarDetalle(@RequestBody DetallePedido detalle){
        try {
           detalle = service.actualizarDetalle(detalle);
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> eliminarDetalle(@RequestBody DetallePedido detalle){
        try {
            service.eliminarDetalle(detalle);
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok("Detalle Eliminado");
    }
}
