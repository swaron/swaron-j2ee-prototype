package org.app.repo.jdbc.dao;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.PostConstruct;

import org.app.repo.jpa.model.SecUser;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository("jdbcUserDao")
public class UserDao extends AbstractJdbcSupport {
	protected SimpleJdbcInsert jdbcInsert;

	@PostConstruct
	public void initialize() {
		jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
		jdbcInsert.withTableName("sec_user").usingGeneratedKeyColumns("sec_user_id");
	}

	public List<SecUser> findAll() {
		return jdbcTemplate.query("select * from sec_user", BeanPropertyRowMapper.newInstance(SecUser.class));
	}

	public SecUser persist(SecUser user) {
		user.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
		Number key = jdbcInsert.executeAndReturnKey(parameterSource);
		user.setSecUserId(key.intValue());
		return user;
	}

	public SecUser update(SecUser user) {
		user.setUpdateTime(new Timestamp(System.currentTimeMillis()));

		jdbcTemplate
				.update("update sec_user set email = ?, enabled =?, username = ?, login_failed_count = ? where sec_user_id = ?",
						user.getEmail(), user.getEnabled(), user.getUsername(), user.getLoginFailedCount(),
						user.getSecUserId());
		return user;
	}

	public void delete(int userId) {
		jdbcTemplate.update("delete from sec_user where sec_user_id = ?", userId);
	}
}
