package com.brikton.labapps.mspedidos.service;

import com.brikton.labapps.mspedidos.domain.Producto;

public interface ProductoService {

    Integer stockDisponible(Producto producto);
    
}
