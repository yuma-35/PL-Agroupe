package database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import enums.LogInStatus;
import model.GameRecordToPlayer;
import model.Match;
import model.Player;
import model.SendIcon;

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
		String sql = "INSERT INTO players(id, password, question, answer, win, lose, draw, conceed, icon_image, player_rank, rank_point, comment, status) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, player.id);
		pstmt.setString(2, player.password);
		pstmt.setString(3, player.question);
		pstmt.setString(4, player.answer);
		pstmt.setInt(5, 0);
		pstmt.setInt(6, 0);
		pstmt.setInt(7, 0);
		pstmt.setInt(8, 0);
		pstmt.setString(9, "ユーザーのアイコン素材.png");
		pstmt.setInt(10, 0);
		pstmt.setInt(11, 0);
		pstmt.setString(12, "初心者です");
		pstmt.setInt(13, LogInStatus.ONLINE.code);
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
	public boolean addComment(String playerId, String hitokoto) throws SQLException {
		String sql = "update players SET comment = ? where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, hitokoto);
		pstmt.setString(2, playerId);
		pstmt.executeUpdate();

		return true;
	}

	//アイコン編集
	public boolean addIcon(String playerId, String iconName) throws SQLException {
		//今のアイコン画像をファイルから削除
/*		String sql0 = "SELECT icon_image  FROM players WHERE id = ?";
		PreparedStatement statement = connection.prepareStatement(sql0);
		statement.setString(1, playerId);
		ResultSet result = statement.executeQuery();
		result.next();
		String name0 = result.getString("icon_image");

		//元の設定がデフォルト設定以外だったら、元の画像を消す
		if (!name0.equals("ユーザーのアイコン素材.png")) {
			String name = "サーバ画像/" + name0;
			File d = new File(name);
			if (d.exists()) {
				//削除実行
				d.delete();
				System.out.println("ファイルを削除しました。");
			} else {
				System.out.println("ファイルが存在しません。");
			}
		}
*/
		String sql = "update players SET icon_image = ? where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, iconName);
		pstmt.setString(2, playerId);
		pstmt.executeUpdate();

		return true;
	}

	//アイコン情報送信
	public SendIcon sendIcon(String playerId) throws SQLException {
		//アイコン名取得
		String sql =  "SELECT icon_image  FROM players WHERE id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, playerId);
		ResultSet result = pstmt.executeQuery();
		result.next();
		String name0 = result.getString("icon_image");

		String name = "サーバ画像/" + name0;
		File f = new File(name);

		SendIcon send = new SendIcon(playerId,name0,f);


		return send;
	}

	//対局記録取得
	public ArrayList<GameRecordToPlayer> getGameRecords(String playerId) throws SQLException {
		String sql = "select opponent_id, result, count(*) from game_records where player_id = ? group by opponent_id, result";
		PreparedStatement pstmt = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		pstmt.setString(1, playerId);
		ResultSet rs = pstmt.executeQuery();

		// 人ごとの対局記録のリスト
		ArrayList<GameRecordToPlayer> gameRecordToPlayers = new ArrayList<GameRecordToPlayer>();
		// opponentIdの境目を判定するために利用する
		String previousRawOpponentId = "";
		// 人ごとの対局記録
		GameRecordToPlayer gameRecordToPlayer = new GameRecordToPlayer();

		// まず1行読んで、結果が0行か判定
		if (!rs.next()) {
			return null;
		}
		previousRawOpponentId = rs.getString("opponent_id");
		gameRecordToPlayer.opponentId = rs.getString("opponent_id");
		rs.previous();

		while (rs.next()) {
			// opponentIdの境目だった場合
			if (!previousRawOpponentId.equals(rs.getString("opponent_id"))) {
				// 前の人の対局記録をリストに追加
				gameRecordToPlayers.add(gameRecordToPlayer);
				// 次の人の対局記録を新たに作成
				gameRecordToPlayer = new GameRecordToPlayer();
				gameRecordToPlayer.opponentId = rs.getString("opponent_id");
				//次の人へ
				previousRawOpponentId = rs.getString("opponent_id");
			}
			if (rs.getInt("result") == 1) {
				gameRecordToPlayer.win = rs.getInt("count(*)");
			} else if (rs.getInt("result") == 2) {
				gameRecordToPlayer.lose = rs.getInt("count(*)");
			} else if (rs.getInt("result") == 3) {
				gameRecordToPlayer.draw = rs.getInt("count(*)");
			} else if (rs.getInt("result") == 4) {
				gameRecordToPlayer.conceed = rs.getInt("count(*)");
			}
		}
		//最後の人に対する対局記録を追加
		gameRecordToPlayers.add(gameRecordToPlayer);

		for (GameRecordToPlayer record : gameRecordToPlayers) {
			System.out.println("opponentId = " + record.opponentId);
			System.out.println("win = " + record.win);
			System.out.println("lose = " + record.lose);
			System.out.println("draw = " + record.draw);
			System.out.println("conceed = " + record.conceed);
		}
		return gameRecordToPlayers;
	}

	public String getQuestion(String playerId) throws SQLException {
		String sql = "select id, question from players where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, playerId);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		String dbQuestion = rs.getString("question");
		return dbQuestion;
	}

	public boolean isValidAnswer(String playerId, String answer) throws SQLException {
		String sql = "select id, answer from players where id=?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, playerId);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		String dbAnswer = rs.getString("answer");
		if (answer.equals(dbAnswer)) {
			return true;
		} else {
			return false;
		}
	}

	public void setPassword(String playerId, String password) throws SQLException {
		String sql = "update players SET password = ? where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, password);
		pstmt.setString(2, playerId);
		pstmt.executeUpdate();
	}

	public Player getPlayer(String playerId, String otherId) throws SQLException {
		String sql = "select id, win, lose, draw, conceed, icon_image, player_rank, comment,rank_point from players where id = ?";
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
		player.rankPoint = rs.getInt("rank_point");

		String sql_2 = "select player_id, opponent_id from friends where player_id = ? AND opponent_id = ?";
		PreparedStatement pstmt_2 = connection.prepareStatement(sql_2);
		pstmt_2.setString(1, playerId);
		pstmt_2.setString(2, otherId);
		ResultSet rs_2 = pstmt_2.executeQuery();
		PreparedStatement pstmt_2_2 = connection.prepareStatement(sql_2);
		pstmt_2_2.setString(1, otherId);
		pstmt_2_2.setString(2, playerId);
		ResultSet rs_2_2 = pstmt_2_2.executeQuery();

		String sql_3 = "select send_player_id, recieve_player_id from friend_requests where send_player_id = ? AND recieve_player_id = ?";
		PreparedStatement pstmt_3 = connection.prepareStatement(sql_3);
		pstmt_3.setString(1, playerId);
		pstmt_3.setString(2, otherId);
		ResultSet rs_3 = pstmt_3.executeQuery();

		if (rs_2.next() == false) {
			player.frflag = 0;
		} else if (rs_2_2.next() == false) {
			player.frflag = 0;
		} else if (rs_3.next() == false) {
			player.frflag = 0;
		} else {
			player.frflag = 1;
		}

		return player;
	}

	public void addMatch(Match newMatch) throws SQLException {
		String sql = "INSERT INTO matches(player_id,rule,password,player_rank,t_limit,comment) values(?, ?, ?, ?, ?, ?)";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, newMatch.playerId);
		pstmt.setInt(2, newMatch.rule);
		pstmt.setString(3, newMatch.password);
		pstmt.setInt(4, newMatch.playerRank);
		pstmt.setInt(5, newMatch.t_limit);
		pstmt.setString(6, newMatch.comment);
		pstmt.executeUpdate();
	}

	public void deleteMatch(String deleteID) throws SQLException {
		String sql = "DELETE FROM matches where Player_id= ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, deleteID);
		pstmt.executeUpdate();
	}

	public void insertFriend(String playerId, String otherId) throws SQLException {
		String sql = "DELETE FROM friend_requests where recieve_player_id = ? AND send_player_id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, playerId);
		pstmt.setString(2, otherId);
		pstmt.executeUpdate();

		String sql_2 = "INSERT INTO friends(player_id, opponent_id) values(?, ?)";
		PreparedStatement pstmt_2 = connection.prepareStatement(sql_2);
		pstmt_2.setString(1, playerId);
		pstmt_2.setString(2, otherId);
		pstmt.executeUpdate();
	}

	public void deleteFriendrequest(String playerId, String otherId) throws SQLException {
		String sql = "DELETE FROM friend_requests where recieve_player_id = ? AND send_player_id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, playerId);
		pstmt.setString(2, otherId);
		pstmt.executeUpdate();

	}

	//フレンドリクエスト一覧の取得
	public ArrayList<String> getFriendrequest(String playerId) throws SQLException {
		String sql = "select recieve_player_id, send_player_id from friend_requests where recieve_player_id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, playerId);
		ResultSet rs = pstmt.executeQuery();
		ArrayList<String> dbRequest = new ArrayList<String>();
		while (rs.next()) {
			dbRequest.add(rs.getString("send_player_id"));
		}

		return dbRequest;
	}

	public void updatePlayerDB(Player updatePlayer) throws SQLException {
		// TODO 自動生成されたメソッド・スタブ
		String sql = "update players SET win = ?,lose = ?,draw = ?,conceed = ?,player_rank = ?,rank_point = ?,status = ? where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, updatePlayer.win);
		pstmt.setInt(2, updatePlayer.lose);
		pstmt.setInt(3, updatePlayer.draw);
		pstmt.setInt(4, updatePlayer.conceed);
		pstmt.setInt(5, updatePlayer.playerRank);
		pstmt.setInt(6, updatePlayer.rankPoint);
		pstmt.setInt(7, 1);
		pstmt.setString(8, updatePlayer.id);
		pstmt.executeUpdate();

	}

	public void makeGameRecordDB(ArrayList<String> endSet) throws SQLException {
		// TODO 自動生成されたメソッド・スタブ
		String sql = "INSERT INTO game_records(player_id,opponent_id,result) values(?, ?, ?)";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		String myID = endSet.get(0);
		String enemyID = endSet.get(1);
		int endcase = Integer.parseInt(endSet.get(2));
		pstmt.setString(1, myID);
		pstmt.setString(2, enemyID);
		if (endcase == 0 || endcase == 2 || endcase == 4) {
			pstmt.setInt(3, 1);
		} else if (endcase == 1) {
			pstmt.setInt(3, 2);
		} else if (endcase == 3) {
			pstmt.setInt(3, 4);
		} else if (endcase == 5) {
			pstmt.setInt(3, 3);
		}

	}

	//friend_requestsに追加
	public void insertFriendrequest(String playerId, String otherId) throws SQLException {
		String sql = "INSERT INTO friend_requests(recieve_player_id,send_player_id) values(?, ?)";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, otherId);
		pstmt.setString(2, playerId);
		pstmt.executeUpdate();

	}

	//フレンド解除
	public void deleteFriend(String playerId, String otherId) throws SQLException {
		String sql = "DELETE FROM friends where player_id = ? AND opponent_id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, playerId);
		pstmt.setString(2, otherId);
		pstmt.executeUpdate();
		PreparedStatement pstmt_2 = connection.prepareStatement(sql);
		pstmt_2.setString(1, otherId);
		pstmt_2.setString(2, playerId);
		pstmt_2.executeUpdate();
	}
}
