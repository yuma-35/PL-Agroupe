package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import database.DatabaseManager;
import model.Player;

class OthelloServer {
	private static OthelloServer instance;
	public ServerSocket ss;
	private ArrayList<ClientThread> clientList;
	public static DatabaseManager db;
	public int port = 4231;

	public static void main(String[] args) {
		OthelloServer socketServer = OthelloServer.getInstance();
		socketServer.start();
	}

	//サーバーはシングルトン設計。唯一のインスタンス
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

	//main メソッドから呼び出される
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
				//クライアント受信用ソケット
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

		if(db.isAlreadyExists(player.id)) {
			oos.writeObject("failed");
		}else {
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

		if(!db.isAlreadyExists(id)) {
			oos.writeObject("failed");
			return;
		}
		if(!db.isValidPassword(id, hashedPassword)) {
			oos.writeObject("failed");
			return;
		}
		db.setStatusToLogIn(id);
		oos.writeObject("success");

	}

	public void forget(Object i, ClientThread client) throws IOException, SQLException{
		String id = (String)i;
		OutputStream os = client.socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		if(!db.isAlreadyExists(id)) {
			oos.writeObject("failed");
			return;
		}

		String question = db.getQuestion(id);
		oos.writeObject("success");
		oos.writeObject(question);
	}

	public void newPassword(Object data, ClientThread client)throws IOException, SQLException{

		ArrayList<String> pw_data = (ArrayList<String>) data;
		String id = pw_data.get(0);
		String hashedPassword = pw_data.get(1);
		String answer = pw_data.get(2);
		OutputStream os = client.socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);

		if(!db.isValidAnswer(id, answer)) {
			oos.writeObject("failed");
		}else{
			db.setPassword(id,hashedPassword);
			oos.writeObject("success");
		}
	}

	public void getProfile(Object data, ClientThread client) throws IOException, SQLException{
		ArrayList<String> idData = (ArrayList<String>) data;
		String myId = idData.get(0);
		String otherId = idData.get(1);
		OutputStream os = client.socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);

		if(!db.isAlreadyExists(otherId)) {
			oos.writeObject("failed");
			return;
		}

		Player player = db.getPlayer(myId, otherId);
		oos.writeObject("success");
		oos.writeObject(player);
	}

}
