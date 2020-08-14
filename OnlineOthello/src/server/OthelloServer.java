package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import database.DatabaseManager;
import model.GameRecordToPlayer;
import model.Match;
import model.OthelloRoom;
import model.Player;
import model.SendIcon;

class OthelloServer {

	private static OthelloServer instance;
	public ServerSocket ss;
	public ArrayList<ClientThread> clientList;
	public ArrayList<Match> matchList = new ArrayList<Match>();
	public static DatabaseManager db;
	public int port = 4231;
	public ArrayList<OthelloRoom> roomList = new ArrayList<OthelloRoom>();

	public static void main(String[] args) {
		OthelloServer socketServer = OthelloServer.getInstance();
		socketServer.start();
	}

	// サーバーはシングルトン設計。唯一のインスタンス
	public static OthelloServer getInstance() {
		if (instance == null) {
			instance = new OthelloServer();
		}
		return instance;
	}

	private OthelloServer() {
		db = new DatabaseManager();
		clientList = new ArrayList<ClientThread>();
	}

	// main メソッドから呼び出される
	public void start() {
		try {
			// サーバーソケットを作成
			ss = new ServerSocket(port);
			System.out.println("server start");

			while (true) {
				// クライアントからの要求を受け取る

				// クライアントごとにソケットを2つ生成
				// サーバ受信用のソケット
				Socket socket1 = ss.accept();
				// クライアント受信用ソケット
				Socket socket2 = ss.accept();
				System.out.println("接続成功");

				ClientThread clientThread = new ClientThread(socket1, socket2);
				clientThread.start();
				addClient(clientThread);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addClient(ClientThread client) {
		clientList.add(client);
	}

	public void makeAccount(Object data, ClientThread client) throws IOException, SQLException {
		ArrayList<String> accountData = (ArrayList<String>) data;
		Player player = new Player();

		player.id = accountData.get(0);
		player.question = accountData.get(1);
		player.answer = accountData.get(2);
		player.password = accountData.get(3);

		OutputStream os = client.socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);

		if (db.isAlreadyExists(player.id)) {
			oos.writeObject("failed");
		} else {
			db.insertPlayer(player);
			oos.writeObject("success");
		}
	}

	public void logIn(Object data, ClientThread client) throws IOException, SQLException {
		ArrayList<String> logInData = (ArrayList<String>) data;
		String id = logInData.get(0);
		String hashedPassword = logInData.get(1);

		OutputStream os = client.socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);

		if (!db.isAlreadyExists(id)) {
			oos.writeObject("failed");
			return;
		}
		if (!db.isValidPassword(id, hashedPassword)) {
			oos.writeObject("failed");
			return;
		}
		if (0 != db.getStatusDB(id)) {
			oos.writeObject("failed");
			return;
		}

		db.setStatusToLogIn(id);
		oos.writeObject("success");
		return;
	}

	// ひとこと編集
	public void addCComment(Object data, ClientThread client) throws IOException, SQLException {
		ArrayList<String> commentData = (ArrayList<String>) data;

		String id = commentData.get(0);
		String comment = commentData.get(1);

		OutputStream os = client.socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);

		if (db.addComment(id, comment)) {
			oos.writeObject("success");
		} else {
			// db.insertPlayer(player);
			oos.writeObject("failed");
		}
	}

	// アイコン編集
	public void addIIcon(SendIcon iconData, ClientThread client) throws IOException, SQLException {
		//ArrayList<SendIcon> iconData = (ArrayList<SendIcon>) data;

		InputStream is = client.socket2.getInputStream();

		//受け取り

		File f = new File("サーバ画像/" + iconData.iconName);
		FileOutputStream fileOutStream = new FileOutputStream( f);
		int waitCount = 0;
		int recvFileSize;       //InputStreamから受け取ったファイルのサイズ
	    byte[] fileBuff = new byte[512];      //サーバからのファイル出力を受け取る


		 while( true )
      {
        //ストリームから読み込める時
        if( is.available() > 0 )
        {
          //受け取ったbyteをファイルに書き込み
          recvFileSize = is.read(fileBuff);
          fileOutStream.write( fileBuff , 0 , recvFileSize );
        }

        //タイムアウト処理
        else
        {
          waitCount++;
          try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
          if (waitCount > 10)break;
        }
      }

      //ファイルの書き込みを閉じる
      fileOutStream.close();


//ここまで
		/*BufferedImage img = ImageIO.read(is);

		// イメージ保存
		try {
			File f = new File("サーバ画像/" + iconData.iconName);
			ImageIO.write(img, "PNG", f);
		} catch (Exception e) {
			e.printStackTrace();
		}*/

		OutputStream os = client.socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);

		if (db.addIcon(iconData.id, iconData.iconName)) {
			oos.writeObject("success");
		} else {
			// db.insertPlayer(player);
			oos.writeObject("failed");
		}
	}

	// 対局記録取得
	public void gamerecord(Object data, ClientThread client) {
		String playerId = (String) data;

		OutputStream os;
		try {
			os = client.socket1.getOutputStream();

			ObjectOutputStream oos = new ObjectOutputStream(os);
			ArrayList grtp = new ArrayList<GameRecordToPlayer>();
			grtp = db.getGameRecords(playerId);

			oos.writeObject("success");
			oos.writeObject(grtp);
		} catch (IOException | SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

	// アイコン取得
	public void geticon(Object data, ClientThread client) throws IOException, SQLException {
		String Id = (String) data;
		OutputStream os = client.socket1.getOutputStream();
		//ObjectOutputStream oos = new ObjectOutputStream(os);

		byte[] buffer = new byte[512]; // ファイル送信時のバッファ

		// 送信用
				File iconData;

		iconData = db.sendIcon(Id);

		// ファイルをストリームで送信

		int fileLength;
		InputStream inputStream = new FileInputStream(iconData);

		while ((fileLength = inputStream.read(buffer)) > 0) {

			os.write(buffer, 0, fileLength);

		}
		// 終了処理

		os.flush();

		inputStream.close();
	}

	public void forget(Object i, ClientThread client) throws IOException, SQLException {
		String id = (String) i;
		OutputStream os = client.socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		if (!db.isAlreadyExists(id)) {
			oos.writeObject("failed");
			return;
		}

		String question = db.getQuestion(id);
		oos.writeObject("success");
		oos.writeObject(question);
	}

	public void newPassword(Object data, ClientThread client) throws IOException, SQLException {

		ArrayList<String> pw_data = (ArrayList<String>) data;
		String id = pw_data.get(0);
		String hashedPassword = pw_data.get(1);
		String answer = pw_data.get(2);
		OutputStream os = client.socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);

		if (!db.isValidAnswer(id, answer)) {
			oos.writeObject("failed");
		} else {
			db.setPassword(id, hashedPassword);
			oos.writeObject("success");
		}
	}

	public void getProfile(Object data, ClientThread client) throws IOException, SQLException {
		ArrayList<String> idData = (ArrayList<String>) data;
		String myId = idData.get(0);
		String otherId = idData.get(1);
		OutputStream os = client.socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);

		if (!db.isAlreadyExists(otherId)) {

			oos.writeObject("failed");
			return;
		}

		Player player = db.getPlayer(myId, otherId);
		oos.writeObject("success");
		oos.writeObject(player);
	}

	public void deleteMatch(Object data, ClientThread client) throws IOException, SQLException {
		String deleteID = (String) data;
		int i = 0;
		if (matchList.size() != 0) {
			do {
				if (roomList.get(i).hostID != deleteID) {
					roomList.remove(i);
				}
				i++;

			} while (i < roomList.size());
		}
		i = 0;
		if (matchList.size() != 0) {
			do {
				if (matchList.get(i).playerId != deleteID) {
					matchList.remove(i);
				}
				i++;
			} while (i < matchList.size());
			return;

		}
	}

	// db.deleteMatch(deleteID);
	public void makeMatch(Object data, ClientThread client) throws IOException, SQLException {
		Match newMatch = (Match) data;
		matchList.add(newMatch);
		OthelloRoom newRoom = new OthelloRoom();
		newRoom.hostID = newMatch.playerId;
		newRoom.hostSocket1 = client.socket1;
		newRoom.hostSocket2 = client.socket2;
		roomList.add(newRoom);
	}

	public void reloadMatchRequest(Object data, ClientThread client) throws IOException {
		OutputStream os = client.socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(matchList);
		return;

	}

	public void friend_add(Object data, ClientThread client) throws IOException, SQLException {

		ArrayList<String> idData = (ArrayList<String>) data;
		String playerId = idData.get(0);
		String otherId = idData.get(1);
		OutputStream os = client.socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);

		db.insertFriend(playerId, otherId);
		oos.writeObject("success");
	}

	public void friend_refuse(Object data, ClientThread client) throws IOException, SQLException {

		ArrayList<String> idData = (ArrayList<String>) data;
		String playerId = idData.get(0);
		String otherId = idData.get(1);
		OutputStream os = client.socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);

		db.deleteFriendrequest(playerId, otherId);
		oos.writeObject("success");
	}

	public void getFriendrequest(Object data, ClientThread client) throws IOException, SQLException {

		String playerId = (String) data;
		OutputStream os = client.socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		ArrayList dbRequest = new ArrayList<String>();
		dbRequest = db.getFriendrequest(playerId);
		if (dbRequest.isEmpty()) {
			oos.writeObject("failed");
			return;
		}
		oos.writeObject("success");
		oos.writeObject(dbRequest);
	}

	public void friendrequest(Object data, ClientThread client) throws IOException, SQLException {

		ArrayList<String> request = (ArrayList<String>) data;
		String playerId = request.get(0);
		String otherId = request.get(1);
		OutputStream os = client.socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		boolean q = db.insertFriendrequest(playerId, otherId);
		if (q) {
			oos.writeObject("success");
		} else {
			oos.writeObject("failed");
		}

	}

	// フレンド解除

	public void delfriend(Object data, ClientThread client) throws IOException, SQLException {

		ArrayList<String> request = (ArrayList<String>) data;
		String playerId = request.get(0);
		String otherId = request.get(1);
		db.deleteFriend(playerId, otherId);

	}

	public void getMyPlayer(Object data, ClientThread client) throws IOException, SQLException {
		String idString = (String) data;
		OutputStream os = client.socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		Player playerdata = db.getPlayer(idString, idString);
		oos.writeObject(playerdata);

	}

	public void deleteRoom(Object data, ClientThread clientThread) {
		// TODO 自動生成されたメソッド・スタブ
		String hostName = (String) data;
		int i = 0;
		if (roomList.size() != 0) {
			do {
				if (roomList.get(i).hostID == hostName) {
					roomList.remove(i);
				}
				i++;
			} while (i < roomList.size());
		}
	}

	public void updatePlayer(Object data, ClientThread clientThread) throws SQLException {
		// TODO 自動生成されたメソッド・スタブ
		Player updatePlayer = (Player) data;
		db.updatePlayerDB(updatePlayer);
	}

	public void makeGameRecord(Object data, ClientThread clientThread) throws SQLException {
		// TODO 自動生成されたメソッド・スタブ
		ArrayList<String> endSet = (ArrayList<String>) data;
		db.makeGameRecordDB(endSet);
	}

	public void getfr(Object data, ClientThread clientThread) throws IOException, SQLException {
		ArrayList<String> names = (ArrayList<String>) data;
		OutputStream os = clientThread.socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(db.getfr(names.get(0), names.get(1)));

	}

	public void getFriendList(Object data, ClientThread clientThread) throws SQLException, IOException {
		// TODO 自動生成されたメソッド・スタブ
		String name = (String) data;
		ArrayList<Player> friendList = db.getFriendList(db.getFriendNameList(name));
		int i = 0;
		if (friendList.size() != 0 && matchList.size() != 0) {
			do {
				if (friendList.get(i).status == 2) {

					int z = 0;

					do {

						if (matchList.get(z).playerId.equals(friendList.get(i).id)) {

							friendList.get(i).frMatch = matchList.get(z);
						}
						z++;
					} while (z < matchList.size());

				}
				i++;
			} while (i < friendList.size());
		}
		OutputStream os = clientThread.socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(friendList);
	}

	public int getStatus(String playerID) throws SQLException {
		return db.getStatusDB(playerID);

	}

	public void setStatus(String playerID, int i) throws SQLException {

		db.setStatusDB(playerID, i);
		return;
	}

	public void disconePenaltyForResult(String playerIDString) throws SQLException {
		Player penaPlayer;
		penaPlayer = db.getPlayer(playerIDString, playerIDString);
		penaPlayer.conceed++;
		penaPlayer.rankPoint = penaPlayer.rankPoint - 50;
		if (penaPlayer.rankPoint < 0) {
			penaPlayer.rankPoint = penaPlayer.rankPoint + 100;
			penaPlayer.playerRank--;
			if (penaPlayer.playerRank < 0) {
				penaPlayer.rankPoint = 0;
				penaPlayer.playerRank = 0;
			}
		}
		db.updatePlayerDB(penaPlayer);
	}

	public boolean applyBattleSet(Object data, ClientThread clientThread) throws SQLException, IOException {
		// TODO 自動生成されたメソッド・スタブ
		ArrayList<String> pack = (ArrayList<String>) data;
		String enemyid = pack.get(0);

		int i = 0;
		int st;
		st = db.getStatusDB(enemyid);
		OutputStream os = clientThread.socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);

		if (clientList.size() != 0 && st != 3 && st != 2 && st != 0) {
			do {
				if (clientList.get(i).playerIDString.equals(enemyid)) {
					clientThread.enemySocket1 = clientList.get(i).socket1;
					clientThread.enemySocket2 = clientList.get(i).socket2;
					oos.writeObject(0);
					return true;
				}
				i++;
			} while (i < clientList.size());

		}
		oos.writeObject(1);
		return false;
	}

	public void getEnemyThread(Object data, ClientThread clientThread) throws SQLException {
		String enemyID = (String) data;
		int st;
		st = db.getStatusDB(enemyID);
		int i = 0;
		if (clientList.size() != 0 && st != 0) {
			do {
				if (clientList.get(i).playerIDString.equals(enemyID)) {
					clientThread.enemySocket1 = clientList.get(i).socket1;
					clientThread.enemySocket2 = clientList.get(i).socket2;
					return;
				}
				i++;
			} while (i < clientList.size());

		}

	}

}
