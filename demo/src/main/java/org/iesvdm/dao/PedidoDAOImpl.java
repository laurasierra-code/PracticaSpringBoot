package org.iesvdm.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.iesvdm.modelo.Pedido;
import org.iesvdm.modelo.PedidoDetalle;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PedidoDAOImpl implements PedidoDAO {

	private final JdbcTemplate jdbcTemplate;

	PedidoDAOImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public synchronized void create(Pedido pedido) {
		String sqlInsert = """
				INSERT INTO pedido (total, fecha, id_cliente, id_comercial)
				VALUES (?, ?, ?, ?)
				""";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		int rows = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[] { "id" });
			int index = 1;
			ps.setBigDecimal(index++, pedido.getTotal());
			if (pedido.getFecha() == null) {
				ps.setObject(index++, null);
			} else {
				ps.setDate(index++, Date.valueOf(pedido.getFecha()));
			}
			ps.setInt(index++, pedido.getClienteId());
			ps.setInt(index, pedido.getComercialId());
			return ps;
		}, keyHolder);

		pedido.setId(keyHolder.getKey().intValue());
		log.info("Insertados {} pedidos.", rows);
	}

	@Override
	public List<Pedido> getAll() {
		List<Pedido> pedidos = jdbcTemplate.query(
				"SELECT * FROM pedido",
				(rs, rowNum) -> mapPedido(rs.getInt("id"), rs.getBigDecimal("total"),
						rs.getDate("fecha"), rs.getInt("id_cliente"), rs.getInt("id_comercial")));

		log.info("Devueltos {} pedidos.", pedidos.size());
		return pedidos;
	}

	@Override
	public Optional<Pedido> find(int id) {
		Pedido pedido = jdbcTemplate.query(
				"SELECT * FROM pedido WHERE id = ?",
				(rs, rowNum) -> mapPedido(rs.getInt("id"), rs.getBigDecimal("total"),
						rs.getDate("fecha"), rs.getInt("id_cliente"), rs.getInt("id_comercial")),
				id).stream().findFirst().orElse(null);

		return Optional.ofNullable(pedido);
	}

	@Override
	public List<PedidoDetalle> getAllDetalles() {
		List<PedidoDetalle> pedidos = jdbcTemplate.query("""
				SELECT p.id, p.total, p.fecha, p.id_cliente, p.id_comercial,
				       CONCAT_WS(' ', c.nombre, c.apellido1, c.apellido2) AS cliente_nombre,
				       CONCAT_WS(' ', co.nombre, co.apellido1, co.apellido2) AS comercial_nombre
				FROM pedido p
				JOIN cliente c ON c.id = p.id_cliente
				JOIN comercial co ON co.id = p.id_comercial
				""",
				(rs, rowNum) -> mapPedidoDetalle(
						rs.getInt("id"),
						rs.getBigDecimal("total"),
						rs.getDate("fecha"),
						rs.getInt("id_cliente"),
						rs.getInt("id_comercial"),
						rs.getString("cliente_nombre"),
						rs.getString("comercial_nombre")));

		log.info("Devueltos {} detalles de pedidos.", pedidos.size());
		return pedidos;
	}

	@Override
	public Optional<PedidoDetalle> findDetalle(int id) {
		PedidoDetalle pedido = jdbcTemplate.query("""
				SELECT p.id, p.total, p.fecha, p.id_cliente, p.id_comercial,
				       CONCAT_WS(' ', c.nombre, c.apellido1, c.apellido2) AS cliente_nombre,
				       CONCAT_WS(' ', co.nombre, co.apellido1, co.apellido2) AS comercial_nombre
				FROM pedido p
				JOIN cliente c ON c.id = p.id_cliente
				JOIN comercial co ON co.id = p.id_comercial
				WHERE p.id = ?
				""",
				(rs, rowNum) -> mapPedidoDetalle(
						rs.getInt("id"),
						rs.getBigDecimal("total"),
						rs.getDate("fecha"),
						rs.getInt("id_cliente"),
						rs.getInt("id_comercial"),
						rs.getString("cliente_nombre"),
						rs.getString("comercial_nombre")),
				id).stream().findFirst().orElse(null);

		return Optional.ofNullable(pedido);
	}
	@Override
	public List<PedidoDetalle> findByComercialId(int comercialId){
		List<PedidoDetalle> pedidos = jdbcTemplate.query("""
				SELECT p.id, p.total, p.fecha, p.id_cliente, p.id_comercial,
				       CONCAT_WS(' ', c.nombre, c.apellido1, c.apellido2) AS cliente_nombre,
				       CONCAT_WS(' ', co.nombre, co.apellido1, co.apellido2) AS comercial_nombre
				FROM pedido p
				JOIN cliente c ON c.id = p.id_cliente
				JOIN comercial co ON co.id = p.id_comercial
				WHERE p.id_comercial = ?
				""",
				(rs, rowNum) -> mapPedidoDetalle(
						rs.getInt("id"),
						rs.getBigDecimal("total"),
						rs.getDate("fecha"),
						rs.getInt("id_cliente"),
						rs.getInt("id_comercial"),
						rs.getString("cliente_nombre"),
						rs.getString("comercial_nombre")),
				comercialId);
		log.info("Devueltos {} detalles de pedidos para el comercial con id {}.", pedidos.size(), comercialId);
		return pedidos;
	}


	@Override
	public void update(Pedido pedido) {
		int rows = jdbcTemplate.update("""
				UPDATE pedido SET
					total = ?,
					fecha = ?,
					id_cliente = ?,
					id_comercial = ?
				WHERE id = ?
				""",
				pedido.getTotal(),
				pedido.getFecha() == null ? null : Date.valueOf(pedido.getFecha()),
				pedido.getClienteId(),
				pedido.getComercialId(),
				pedido.getId());

		log.info("Update de Pedido con {} registros actualizados.", rows);
	}

	@Override
	public void delete(int id) {
		int rows = jdbcTemplate.update("DELETE FROM pedido WHERE id = ?", id);
		log.info("Delete de Pedido con {} registros eliminados.", rows);
	}

	private Pedido mapPedido(int id, BigDecimal total, Date fecha, int clienteId, int comercialId) {
		return new Pedido(id, total, fecha == null ? null : fecha.toLocalDate(), clienteId, comercialId);
	}

	private PedidoDetalle mapPedidoDetalle(int id, BigDecimal total, Date fecha, int clienteId, int comercialId,
			String clienteNombre, String comercialNombre) {
		return new PedidoDetalle(id, total, fecha == null ? null : fecha.toLocalDate(), clienteId, comercialId,
				clienteNombre, comercialNombre);
	}
}