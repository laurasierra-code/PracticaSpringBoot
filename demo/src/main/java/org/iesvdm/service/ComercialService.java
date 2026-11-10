package org.iesvdm.service;

import java.math.BigDecimal;
import java.util.List;

import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.dto.ClienteTotalPedidosDTO;
import org.iesvdm.dto.EstadisticasPedidosDTO;
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

    public List<ClienteTotalPedidosDTO> findClientesConPedidosPorComercial(int comercialId) {
        return pedidoDAO.findClientesConPedidosPorComercial(comercialId);
    }

    public EstadisticasPedidosDTO calcularEstadisticas(List<PedidoDetalle> pedidos) {
        BigDecimal totalPedidos = BigDecimal.ZERO;
        BigDecimal maximoPedido = null;
        BigDecimal minimoPedido = null;
        int cantidadPedidos = 0;

        for (PedidoDetalle pedido : pedidos) {
            if (pedido.getTotal() == null) {
                continue;
            }

            BigDecimal importe = pedido.getTotal();
            totalPedidos = totalPedidos.add(importe);
            maximoPedido = maximoPedido == null || importe.compareTo(maximoPedido) > 0
                    ? importe
                    : maximoPedido;
            minimoPedido = minimoPedido == null || importe.compareTo(minimoPedido) < 0
                    ? importe
                    : minimoPedido;
            cantidadPedidos++;
        }

        BigDecimal mediaPedidos = cantidadPedidos == 0
                ? BigDecimal.ZERO
                : totalPedidos.divide(BigDecimal.valueOf(cantidadPedidos), 2, java.math.RoundingMode.HALF_UP);

        return new EstadisticasPedidosDTO(
                cantidadPedidos,
                totalPedidos,
                mediaPedidos,
                maximoPedido,
                minimoPedido);
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