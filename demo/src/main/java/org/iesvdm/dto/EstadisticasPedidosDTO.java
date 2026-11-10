package org.iesvdm.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EstadisticasPedidosDTO {

	private int cantidadPedidos;
	private BigDecimal totalPedidos;
	private BigDecimal mediaPedidos;
	private BigDecimal maximoPedido;
	private BigDecimal minimoPedido;
}
