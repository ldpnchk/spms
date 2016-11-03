package nc.ukma.thor.spms.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import nc.ukma.thor.spms.entity.Project;
import nc.ukma.thor.spms.entity.Team;
import nc.ukma.thor.spms.entity.User;

@Repository
public class TeamRepositoryJdbcImpl implements MyRepository<Team>, TeamRepository{
	
	private static final String INSERT_TEAM_SQL = "INSERT INTO team (name, project_id) VALUES(?,?);";
	private static final String UPDATE_TEAM_SQL = "UPDATE team SET name=?, project_id=? WHERE id = ?;";
	private static final String DELETE_TEAM_SQL = "DELETE FROM team WHERE id = ?;";
	private static final String GET_TEAM_BY_ID_SQL = "SELECT * FROM team WHERE id = ?;";
	private static final String GET_TEAMS_BY_USER_SQL = "SELECT * FROM team "
			+ "INNER JOIN user_team ON team.id = user_team.team_id "
			+ "WHERE user_team.user_id = ?;";
	private static final String GET_TEAMS_BY_PROJECT_SQL = "SELECT * FROM team WHERE project_id = ?;";
	
	private static final RowMapper<Team> TEAM_MAPPER = new TeamMapper();

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void add(Team t) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(INSERT_TEAM_SQL, new String[] {"id"});
			ps.setString(1, t.getName());
			ps.setLong(2, t.getProject().getId());
			return ps;
		}, keyHolder);
		t.setId(keyHolder.getKey().longValue());
	}

	@Override
	public void update(Team t) {
		jdbcTemplate.update(UPDATE_TEAM_SQL, new Object[] {t.getName(), t.getProject().getId(), t.getId()});
	}

	@Override
	public void delete(Team t) {
		jdbcTemplate.update(DELETE_TEAM_SQL, t.getId());
	}

	@Override
	public Team getById(long id) {
		try{
			return jdbcTemplate.queryForObject(GET_TEAM_BY_ID_SQL,
					new Object[] {id}, TEAM_MAPPER);
		}catch(EmptyResultDataAccessException e){
			return null;
		}
	}
	
	@Override
	public List<Team> getTeamsByUser(User user) {
		return jdbcTemplate.query(GET_TEAMS_BY_USER_SQL,
				new Object[] { user.getId() }, TEAM_MAPPER);
	}

	@Override
	public List<Team> getTeamsByProject(Project project) {
		return jdbcTemplate.query(GET_TEAMS_BY_PROJECT_SQL,
					new Object[] { project.getId() }, TEAM_MAPPER);
	}
	
	private static final class TeamMapper implements RowMapper<Team>{
		@Override
		public Team mapRow(ResultSet rs, int rowNum) throws SQLException {
			Team team = new Team();
			team.setId(rs.getLong("id"));
			team.setName(rs.getString("name"));
			team.setProject(new Project(rs.getLong("id")));
			return team;
		}
	}

}
