package server;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import database.DatabaseManager;
import model.GameRecordToPlayer;
import model.Match;
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
	public void addIIcon(Object data, ClientThread client) throws IOException, SQLException {
		ArrayList<SendIcon> iconData = (ArrayList<SendIcon>) data;

		SendIcon sendIcon = iconData.get(0);
		BufferedImage img = ImageIO.read(sendIcon.image);

		// イメージ保存
		try {
			File f = new File("サーバ画像/" + sendIcon.iconName);
			ImageIO.write(img, "PNG", f);
		} catch (Exception e) {
			e.printStackTrace();
		}

		OutputStream os = client.socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);

		if (db.addIcon(sendIcon.id, sendIcon.iconName)) {
			oos.writeObject("success");
		} else {
			// db.insertPlayer(player);
			oos.writeObject("failed");
		}
	}

	//対局記録取得
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

	//アイコン取得
	public void geticon(Object data, ClientThread client) throws IOException, SQLException {
		String Id = (String) data;
		OutputStream os = client.socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);

		//送信用
		SendIcon iconData ;

		iconData = db.sendIcon(Id);
		oos.writeObject(iconData);
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
		do {
			if (roomList.get(i).hostID != deleteID) {
				roomList.remove(i);
			}
			i++;

		} while (i < roomList.size());

		i = 0;

		do {
			if (matchList.get(i).playerId != deleteID) {
				matchList.remove(i);
			}
			i++;
		} while (i < matchList.size());
		return;

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
		String otherId = idData.get(0);
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
		db.insertFriendrequest(playerId, otherId);
		oos.writeObject("success");
	}

	//フレンド解除
	public void delfriend(Object data, ClientThread client) throws IOException, SQLException {

		ArrayList<String> request = (ArrayList<String>) data;
		String playerId = request.get(0);
		String otherId = request.get(1);
		OutputStream os = client.socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		db.deleteFriend(playerId, otherId);
		oos.writeObject("success");
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
		do {
			if (roomList.get(i).hostID != hostName) {
				roomList.remove(i);
			}
			i++;
		} while (i < roomList.size());

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

}
