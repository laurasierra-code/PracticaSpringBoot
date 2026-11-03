package org.iesvdm.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.iesvdm.modelo.Comercial;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ComercialDAOImpl implements ComercialDAO {

	private final JdbcTemplate jdbcTemplate;

	ComercialDAOImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public synchronized void create(Comercial comercial) {
		String sqlInsert = """
				INSERT INTO comercial (nombre, apellido1, apellido2, comisión)
				VALUES (?, ?, ?, ?)
				""";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		int rows = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[] { "id" });
			int index = 1;
			ps.setString(index++, comercial.getNombre());
			ps.setString(index++, comercial.getApellido1());
			ps.setString(index++, comercial.getApellido2());
			if (comercial.getComision() == null) {
				ps.setObject(index, null);
			} else {
				ps.setFloat(index, comercial.getComision());
			}
			return ps;
		}, keyHolder);

		comercial.setId(keyHolder.getKey().intValue());
		log.info("Insertados {} comerciales.", rows);
	}

	@Override
	public List<Comercial> getAll() {
		List<Comercial> comerciales = jdbcTemplate.query(
				"SELECT * FROM comercial",
				(rs, rowNum) -> new Comercial(
						rs.getInt("id"),
						rs.getString("nombre"),
						rs.getString("apellido1"),
						rs.getString("apellido2"),
						rs.getObject("comisión", Float.class)));

		log.info("Devueltos {} comerciales.", comerciales.size());
		return comerciales;
	}

	@Override
	public Optional<Comercial> find(int id) {
		Comercial comercial = jdbcTemplate.query(
				"SELECT * FROM comercial WHERE id = ?",
				(rs, rowNum) -> new Comercial(
						rs.getInt("id"),
						rs.getString("nombre"),
						rs.getString("apellido1"),
						rs.getString("apellido2"),
						rs.getObject("comisión", Float.class)),
				id).stream().findFirst().orElse(null);

		return Optional.ofNullable(comercial);
	}

	@Override
	public boolean hasPedidos(int id) {
		Integer pedidos = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM pedido WHERE id_comercial = ?", Integer.class, id);
		return pedidos != null && pedidos > 0;
	}

	@Override
	public void update(Comercial comercial) {
		int rows = jdbcTemplate.update("""
				UPDATE comercial SET
					nombre = ?,
					apellido1 = ?,
					apellido2 = ?,
					comisión = ?
				WHERE id = ?
				""",
				comercial.getNombre(),
				comercial.getApellido1(),
				comercial.getApellido2(),
				comercial.getComision(),
				comercial.getId());

		log.info("Update de Comercial con {} registros actualizados.", rows);
	}

	@Override
	public void delete(int id) {
		int rows = jdbcTemplate.update("DELETE FROM comercial WHERE id = ?", id);
		log.info("Delete de Comercial con {} registros eliminados.", rows);
	}
}
