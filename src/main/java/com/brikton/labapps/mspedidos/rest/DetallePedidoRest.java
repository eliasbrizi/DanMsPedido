package com.brikton.labapps.mspedidos.rest;

import com.brikton.labapps.mspedidos.domain.DetallePedido;
import com.brikton.labapps.mspedidos.service.DetallePedidoService;

import org.springframework.beans.factory.annotation.Autowired;
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
    //post put delete

    @PostMapping()
    public ResponseEntity<?> agregarItem(@RequestBody DetallePedido detalle, @RequestParam Integer idPedido){
        service.agregarDetalle(detalle,idPedido);
    }

    @PutMapping
    public ResponseEntity<?> modificarDetalle(@RequestBody DetallePedido detalle){
        service.actualizarDetalle(detalle);
    }

    @DeleteMapping
    public ResponseEntity<?> eliminarDetalle(@RequestBody DetallePedido detalle){
        service.eliminarDetalle(detalle);
    }
}
