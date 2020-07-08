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

	public String getQuestion(String playerId) throws SQLException{
		String sql = "select id, question from players where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, playerId);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		String dbQuestion = rs.getString("question");
		return dbQuestion;
	}

	public boolean isValidAnswer(String playerId, String answer)throws SQLException {
		String sql = "select id, answer from players where id=?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, playerId);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		String dbAnswer = rs.getString("answer");
		if (answer.equals(dbAnswer)) {
			return true;
		}else {
			return false;
		}
	}

	public void setPassword(String playerId, String password)throws SQLException {
		String sql = "update players SET password = ? where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, password);
		pstmt.setString(2, playerId);
		pstmt.executeUpdate();
	}


	public Player getPlayer(String playerId, String otherId)throws SQLException{
		String sql = "select id, win, lose, draw, conceed, icon_image, player_rank, comment from players where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, otherId);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		Player player = new Player();
		player.id = rs.getString("id");
		player.win = rs.getInt("win");
		player.lose = rs.getInt("lose");
		player.draw = rs.getInt("draw");
		player.conceed = rs.getInt("conceed");
		player.iconImage = rs.getString("icon_image");
		player.playerRank = rs.getInt("player_rank");
		player.comment = rs.getString("comment");

		String sql_2 = "select player_id, opponent_id from friends where player_id = ?";
		PreparedStatement pstmt_2 = connection.prepareStatement(sql_2);
		pstmt_2.setString(1, playerId);
		ResultSet rs_2 = pstmt_2.executeQuery();

		String sql_3 = "select recieve_player_id, send_player_id from friend_requests where recieve_player_id = ?";
		PreparedStatement pstmt_3 = connection.prepareStatement(sql_3);
		pstmt_3.setString(1, playerId);
		ResultSet rs_3 = pstmt_3.executeQuery();

		if(rs_2.next() || rs_3.next()) {
			player.frflag = 1;
		}else {
			player.frflag = 0;
		}

		return player;
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
