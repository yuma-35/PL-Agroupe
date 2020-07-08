package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



import enums.LogInStatus;
import model.Match;
import model.Player;

public class DatabaseManager {
	private Connection connection = null;

	public DatabaseManager() {
		try {
			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost/othello?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
					"root",
					"");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isAlreadyExists(String playerId) throws SQLException {
		String sql = "select count(*) from players where id=?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, playerId);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		int count = rs.getInt("count(*)");
		if (count == 0) {
			return false;
		}
		return true;
	}

	public void insertPlayer(Player player) throws SQLException {
		String sql = "INSERT INTO players(id, password, question, answer, win, lose, draw, conceed, player_rank, rank_point, status) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, player.id);
		pstmt.setString(2, player.password);
		pstmt.setString(3, player.question);
		pstmt.setString(4, player.answer);
		pstmt.setInt(5, 0);
		pstmt.setInt(6, 0);
		pstmt.setInt(7, 0);
		pstmt.setInt(8, 0);
		pstmt.setInt(9, 0);
		pstmt.setInt(10, 0);
		pstmt.setInt(11, LogInStatus.ONLINE.code);
		System.out.println(pstmt);
		pstmt.executeUpdate();
	}

	public boolean isValidPassword(String playerId, String inputPassword) throws SQLException {
		String sql = "select id, password from players where id=?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, playerId);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		String dbPassword = rs.getString("password");
		if (inputPassword.equals(dbPassword)) {
			return true;
		}
		return false;
	}

	public void setStatusToLogIn(String playerId) throws SQLException {
		String sql = "update players SET status = ? where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, LogInStatus.ONLINE.code);
		pstmt.setString(2, playerId);
		pstmt.executeUpdate();
	}
	
	public void addMatch(Match newMatch) throws SQLException {
		String sql="INSERT INTO matches(player_id,rule,password,player_rank,t_limit,comment) values(?, ?, ?, ?, ?, ?)";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1,newMatch.playerId);
		pstmt.setInt(2,newMatch.rule);
		pstmt.setString(3,newMatch.password);
		pstmt.setInt(4, newMatch.playerRank);
		pstmt.setInt(5, newMatch.t_limit);
		pstmt.setString(6, newMatch.comment);
		pstmt.executeUpdate();
		}
	
public void deleteMatch(String deleteID) throws SQLException {
	String sql="DELETE FROM matches where Player_id= ?";
	PreparedStatement pstmt = connection.prepareStatement(sql);
	pstmt.setString(1, deleteID);
	pstmt.executeUpdate();
}
}
