package org.iesvdm.dao;

import java.util.List;

import org.iesvdm.modelo.ClienteComercial;

public interface ClienteComercialDAO {

	List<ClienteComercial> findByClienteId(int clienteId);

	List<org.iesvdm.modelo.Comercial> findComercialesDisponibles(int clienteId);

	void create(ClienteComercial clienteComercial);

	void delete(int clienteId, int comercialId);
}
