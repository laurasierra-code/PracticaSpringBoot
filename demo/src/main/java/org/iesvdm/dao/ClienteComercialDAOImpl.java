package org.iesvdm.dao;

import java.sql.Date;
import java.util.List;

import org.iesvdm.modelo.ClienteComercial;
import org.iesvdm.modelo.Comercial;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ClienteComercialDAOImpl implements ClienteComercialDAO {

	private final JdbcTemplate jdbcTemplate;

	ClienteComercialDAOImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<ClienteComercial> findByClienteId(int clienteId) {
		List<ClienteComercial> vinculaciones = jdbcTemplate.query("""
				SELECT cc.cliente_id, cc.comercial_id,
				       co.nombre AS comercial_nombre,
				       co.apellido1 AS comercial_apellido1,
				       co.apellido2 AS comercial_apellido2,
				       cc.fecha_asociacion,
				       cc.prioridad
				FROM cliente_has_comercial cc
				JOIN comercial co ON co.id = cc.comercial_id
				WHERE cc.cliente_id = ?
				ORDER BY cc.prioridad, co.nombre, co.apellido1, co.apellido2
				""",
				(rs, rowNum) -> new ClienteComercial(
						rs.getInt("cliente_id"),
						rs.getInt("comercial_id"),
						rs.getString("comercial_nombre"),
						rs.getString("comercial_apellido1"),
						rs.getString("comercial_apellido2"),
						rs.getDate("fecha_asociacion") == null
								? null
								: rs.getDate("fecha_asociacion").toLocalDate(),
						rs.getObject("prioridad", Integer.class)),
				clienteId);

		log.info("Devueltos {} comerciales vinculados al cliente con id {}.",
				vinculaciones.size(), clienteId);
		return vinculaciones;
	}

	@Override
	public void create(ClienteComercial clienteComercial) {
		int rows = jdbcTemplate.update("""
				INSERT INTO cliente_has_comercial
					(cliente_id, comercial_id, fecha_asociacion, prioridad)
				VALUES (?, ?, ?, ?)
				""",
				clienteComercial.getClienteId(),
				clienteComercial.getComercialId(),
				clienteComercial.getFechaAsociacion() == null
						? null
						: Date.valueOf(clienteComercial.getFechaAsociacion()),
				clienteComercial.getPrioridad());

		log.info("Insertado vínculo entre cliente {} y comercial {} ({} registros).",
				clienteComercial.getClienteId(), clienteComercial.getComercialId(), rows);
	}

	@Override
	public List<Comercial> findComercialesDisponibles(int clienteId) {
		return jdbcTemplate.query("""
				SELECT co.id, co.nombre, co.apellido1, co.apellido2, co.comisión
				FROM comercial co
				WHERE NOT EXISTS (
					SELECT 1
					FROM cliente_has_comercial cc
					WHERE cc.cliente_id = ? AND cc.comercial_id = co.id
				)
				ORDER BY co.nombre, co.apellido1, co.apellido2
				""",
				(rs, rowNum) -> new Comercial(
						rs.getInt("id"),
						rs.getString("nombre"),
						rs.getString("apellido1"),
						rs.getString("apellido2"),
						rs.getObject("comisión", Float.class)),
				clienteId);
	}

	@Override
	public void delete(int clienteId, int comercialId) {
		int rows = jdbcTemplate.update("""
				DELETE FROM cliente_has_comercial
				WHERE cliente_id = ? AND comercial_id = ?
				""",
				clienteId, comercialId);

		log.info("Eliminado vínculo entre cliente {} y comercial {} ({} registros).",
				clienteId, comercialId, rows);
	}
}
