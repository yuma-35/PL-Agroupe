package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;



import database.DatabaseManager;
import model.Match;
import model.Player;

class OthelloServer {
	private static OthelloServer instance;
	public ServerSocket ss;
	public ArrayList<ClientThread> clientList;
	public ArrayList<Match> matchList=new ArrayList<Match>();
	public static DatabaseManager db;
	public int port = 4231;
    public ArrayList<OthelloRoom> roomList =new ArrayList<OthelloRoom>();
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
	public void makeMatch(Object data,ClientThread client)throws IOException,SQLException{
		Match newMatch=(Match) data;
		matchList.add(newMatch);
		db.addMatch(newMatch);
		OthelloRoom newRoom=new OthelloRoom();
		newRoom.BlackSocket1=client.socket1;
		newRoom.BlackSocket2=client.socket2;
		roomList.add(newRoom);
	}
	public void deleteMatch(Object data,ClientThread client)throws IOException,SQLException{
		String deleteID=(String)data;
		int i=0;
		do {
			if(roomList.get(i).hostID!=deleteID ) {
				roomList.remove(i);
			}
			i++;
		}while(i<roomList.size()) ;
		
		i=0;
		do {
			if(matchList.get(i).playerId!=deleteID ) {
				matchList.remove(i);
			}
			i++;
		}while(i<matchList.size()) ;
		return;
		}
		//db.deleteMatch(deleteID);
	
	public void reloadMatchRequest(Object data,ClientThread client) throws IOException {
		OutputStream os = client.socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(matchList);
		return;
	}
}
