package org.iesvdm.service;

import org.iesvdm.dao.ClienteDAO;
import org.iesvdm.modelo.Cliente;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service 
public class ClienteService {

    private ClienteDAO clienteDAO;

    //Se utiliza inyeccion automatica por constructor del framework Spring.
    //Por tanto, se puede omitir la anotacion Autowired 
    //@Autowired
    public ClienteService(ClienteDAO clienteDAO){
        this.clienteDAO = clienteDAO;
    }
    public List<Cliente> listAll() {
        return clienteDAO.getAll();
    }

    public List<Cliente> getByCiudad(String ciudad) {
        return clienteDAO.getByCiudad(ciudad);
    }

    public Optional<Cliente> find(int id) {
        return clienteDAO.find(id);
    }

    public void create(Cliente cliente) {
        clienteDAO.create(cliente);
    }

    public void update(Cliente cliente) {
        clienteDAO.update(cliente);
    }

    public void delete(int id) {
        clienteDAO.delete(id);
    }

    public boolean hasPedidos(int id) {
        return clienteDAO.hasPedidos(id);
    }
}
