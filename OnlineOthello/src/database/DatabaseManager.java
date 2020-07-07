package database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import enums.LogInStatus;
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

	//ひとこと編集
	public boolean addComment(String playerId, String hitokoto)  throws SQLException {
		String sql = "update players SET comment = ? where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, hitokoto);
		pstmt.setString(2, playerId);
		pstmt.executeUpdate();

		return true;
	}

	//アイコン編集
	public boolean addIcon(String playerId, String iconName)  throws SQLException {
		//今のアイコン画像をファイルから削除

		String sql0 = "SELECT icon_image  FROM players WHERE id = ?";
		PreparedStatement statement = connection.prepareStatement(sql0);
		statement.setString(1, playerId);
		ResultSet result = statement.executeQuery();
		result.next();
		String name0 = result.getString("icon_image");

		String name = "サーバ画像/" + name0;
		File d = new File(name);
		if (d.exists()) {
            //削除実行
            d.delete();
            System.out.println("ファイルを削除しました。");
        } else {
            System.out.println("ファイルが存在しません。");
        }

		String sql = "update players SET icon_image = ? where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, iconName);
		pstmt.setString(2, playerId);
		pstmt.executeUpdate();

		return true;
	}
}
