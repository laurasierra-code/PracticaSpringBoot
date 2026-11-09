package org.iesvdm.dao;

import java.util.List;
import java.util.Optional;

import org.iesvdm.modelo.Pedido;
import org.iesvdm.modelo.PedidoDetalle;

public interface PedidoDAO {

	List<Pedido> getAll();
	
	Optional<Pedido> find(int id);
	
	List<PedidoDetalle> getAllDetalles();
	
	Optional<PedidoDetalle> findDetalle(int id);
	
	List<PedidoDetalle> findByComercialId(int comercialId);

	List<PedidoDetalle> findByClienteId(int clienteId);
	
	void create(Pedido pedido);
	
	void update(Pedido pedido);
	
	void delete(int id);

}