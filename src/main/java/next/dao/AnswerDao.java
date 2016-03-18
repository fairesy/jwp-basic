package next.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import next.model.Answer;
import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;

public class AnswerDao {

	public Object findAllByQuestionId(int questionId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
	    String sql = "SELECT answerId, writer, contents, createdDate, questionId FROM ANSWERS WHERE questionId=?";
	    RowMapper<Answer> rm = new RowMapper<Answer>() {
	    	@Override
	    	public Answer mapRow(ResultSet rs) throws SQLException {
	    		return new Answer(rs.getInt("answerId"),
	    				rs.getString("writer"),
	    				rs.getString("contents"),
	    				rs.getTimestamp("createdDate"),
	    				rs.getInt("questionId"));
	    	}
	    };
	    return jdbcTemplate.query(sql, rm, questionId);
	}
}