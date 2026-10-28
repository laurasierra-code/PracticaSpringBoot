package org.iesvdm.service;

import org.iesvdm.dao.ClienteDAO;
import org.iesvdm.modelo.Cliente;
import org.springframework.stereotype.Service;
import java.util.List;

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
}
