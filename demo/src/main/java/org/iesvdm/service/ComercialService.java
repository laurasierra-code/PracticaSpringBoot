package org.iesvdm.service;

import java.util.List;

import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.PedidoDetalle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ComercialService {
    @Autowired 
    private  ComercialDAO comercialDAO;

    @Autowired 
    private  PedidoService pedidoDAO;

    public ComercialService(ComercialDAO comercialDAO) {
        this.comercialDAO = comercialDAO;
    }

    public List<Comercial> listAll() {
        return comercialDAO.getAll();
    }

    public Optional<Comercial> find(int id) {
        return comercialDAO.find(id);
    }

    public void create(Comercial comercial) {
        comercialDAO.create(comercial);
    }

    public List<PedidoDetalle> findPedidosByComercialId(int comercialId) {
        return pedidoDAO.findByComercialId(comercialId);
    }
    public void update(Comercial comercial) {
        comercialDAO.update(comercial);
    }

    public void delete(int id) {
        comercialDAO.delete(id);
    }

    public boolean hasPedidos(int id) {
        return comercialDAO.hasPedidos(id);
    }
}