package org.iesvdm.service;

import java.util.List;

import org.iesvdm.dao.ClienteComercialDAO;
import org.iesvdm.modelo.ClienteComercial;
import org.iesvdm.modelo.Comercial;
import org.springframework.stereotype.Service;

@Service
public class ClienteComercialService {

	private final ClienteComercialDAO clienteComercialDAO;

	public ClienteComercialService(ClienteComercialDAO clienteComercialDAO) {
		this.clienteComercialDAO = clienteComercialDAO;
	}

	public List<ClienteComercial> findByClienteId(int clienteId) {
		return clienteComercialDAO.findByClienteId(clienteId);
	}

	public List<Comercial> findComercialesDisponibles(int clienteId) {
		return clienteComercialDAO.findComercialesDisponibles(clienteId);
	}

	public void create(ClienteComercial clienteComercial) {
		clienteComercialDAO.create(clienteComercial);
	}

	public void delete(int clienteId, int comercialId) {
		clienteComercialDAO.delete(clienteId, comercialId);
	}
}
