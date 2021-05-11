package com.brikton.labapps.mspedidos.dao;

import java.util.ArrayList;

import com.brikton.labapps.mspedidos.domain.Pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido,Integer> {

    @Query("select p from Pedidos p where p.id_obra = :idObra")
    ArrayList<Pedido> getPedidosPorObra(@Param("idObra") Integer idObra);

    @Query("select p from Pedidos p where p.estado = :estadoPedido")
    ArrayList<Pedido> getPedidosPorEstado(@Param("estadoPedido") int estadoPedido);
}
