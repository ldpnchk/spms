package nc.ukma.thor.spms.repository.jdbcImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import nc.ukma.thor.spms.entity.Role;
import nc.ukma.thor.spms.entity.User;
import nc.ukma.thor.spms.repository.RoleRepository;

@Repository
public class RoleRepositoryImpl implements RoleRepository{
	
	private static final String GET_ROLE_BY_ID_SQL = "SELECT * FROM role WHERE id=?;";
	private static final String GET_ROLE_BY_NAME_SQL = "SELECT * FROM role WHERE role=?;";
	private static final String GET_ROLE_BY_USER_SQL = "SELECT * FROM role "
			+ "INNER JOIN user_role ON id=role_id "
			+ "WHERE user_id=?;";
	private static final String GET_ALL_ROLES_SQL = "SELECT * FROM role;";
	
	private static final RowMapper<Role> ROLE_MAPPER = new RoleMapper();
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public Role getRoleById(Long id) {
		try {
			return jdbcTemplate.queryForObject(GET_ROLE_BY_ID_SQL,
						new Object[] { id }, ROLE_MAPPER);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	@Override
	public Role getRoleByName(String name) {
		try {
			return jdbcTemplate.queryForObject(GET_ROLE_BY_NAME_SQL,
					new Object[] { name }, ROLE_MAPPER);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	@Override
	public Role getRoleByUser(Long userId) {
		try{
			return jdbcTemplate.queryForObject(GET_ROLE_BY_USER_SQL,
						new Object[] { userId }, ROLE_MAPPER);
		}catch(EmptyResultDataAccessException e){
			return null;
		}
	}
	
	@Override
	public List<Role> getAllRoles() {
		return jdbcTemplate.query(GET_ALL_ROLES_SQL, ROLE_MAPPER);
	}
	
	private static final class RoleMapper implements RowMapper<Role>{
		@Override
		public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
			Role role = Role.valueOf(rs.getString("role").toUpperCase());
			role.setId(rs.getShort("id"));
			return role;
		}
	}
}
