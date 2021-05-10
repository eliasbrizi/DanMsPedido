package com.brikton.labapps.mspedidos.dao;

import com.brikton.labapps.mspedidos.domain.Pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido,Integer> {}
