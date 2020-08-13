package server;

import java.awt.Point;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Match;
import model.OthelloRoom;
import model.SendIcon;

public class ClientThread extends Thread {
	private OthelloServer server = OthelloServer.getInstance();
	public String playerIDString;
	public Socket socket1;
	public Socket socket2;
	public String enemyIDString;
	public Socket enemySocket1;
	public Socket enemySocket2;
	public int number;

	public ClientThread(Socket socket1, Socket socket2) {
		this.socket1 = socket1;
		this.socket2 = socket2;
	}

	public void run() {
		try {
			while (!socket1.isClosed()) {
				recieve();
			}
		} catch (Exception e) {// a切断時の処理
			e.printStackTrace();
			try {
				if (playerIDString != null) {
					int st = server.getStatus(playerIDString);
					server.setStatus(playerIDString, 0);

					if (st == 1) {

					} else if (st == 2) {
						server.deleteMatch(playerIDString, this);
						server.deleteRoom(playerIDString, this);
					} else if (st == 3) {
						sendToEnemy("EnemyDisconected", 0);
						server.deleteRoom(playerIDString, this);
						ArrayList<String> pack = new ArrayList<String>();
						pack.add(playerIDString);
						pack.add(enemyIDString);
						pack.add("6");
						server.makeGameRecord(pack, this);
						server.disconePenaltyForResult(playerIDString);
					}
				}
			} catch (SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}

		}
	}

	public void sendToEnemy(String operation, Object object) throws IOException {
		OutputStream osToE = enemySocket2.getOutputStream();
		ObjectOutputStream oosToE = new ObjectOutputStream(osToE);
		// オペレーション書き込み
		oosToE.writeObject(operation);
		// 送信データ書き込み
		oosToE.writeObject(object);
	}

	public void send(String operation, Object object) throws IOException {
		OutputStream os = socket2.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		// オペレーション書き込み
		oos.writeObject(operation);
		// 送信データ書き込み
		oos.writeObject(object);
	}

	public void recieve() throws IOException, ClassNotFoundException, EOFException, SQLException {
		InputStream is = socket1.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(is);
		// オペレーションを読み込む
		String operation = (String) ois.readObject();
		// データを読み込む
		Object data = ois.readObject();

		if (operation.equals("makeAccount")) {
			server.makeAccount(data, this);
			ArrayList<String> accountData = (ArrayList<String>) data;
			String name = accountData.get(0);
			playerIDString = name;
			return;
		}
		if (operation.equals("logIn")) {
			server.logIn(data, this);
			ArrayList<String> logInData = (ArrayList<String>) data;
			String id = logInData.get(0);
			playerIDString = id;
			return;
		}

		if (operation.equals("addCComment")) {
			server.addCComment(data, this);
			return;
		}
//
		if (operation.equals("addIIcon")) {
			//ArrayList<SendIcon> iconData = (ArrayList<SendIcon>) data;
			SendIcon iconData = (SendIcon) data;
			server.addIIcon(iconData , this);
			//server.addIIcon(data, this);
		}
//

		if (operation.equals("forget")) {

			server.forget(data, this);
			return;
		}
		if (operation.equals("newPassword")) {
			server.newPassword(data, this);
			return;
		}

		if (operation.equals("getProfile")) {

			server.getProfile(data, this);
		}
		if (operation.equals("makeMatch")) {
			server.makeMatch(data, this);
			return;
		}
		if (operation.equals("updatePlayer")) {
			server.updatePlayer(data, this);
		}
		if (operation.equals("deleteRoom")) {
			server.deleteRoom(data, this);
		}
		if (operation.equals("deleteMatch")) {
			server.deleteMatch(data, this);
			return;
		}
		if (operation.equals("reloadMatch")) {
			server.reloadMatchRequest(data, this);
			return;
		}
		if (operation.equals("getMyPlayer")) {
			server.getMyPlayer(data, this);
			return;
		}
		if (operation.equals("BattleEnter")) {
			ArrayList<String> datasetArrayList = (ArrayList<String>) data;
			String hostname = datasetArrayList.get(0);
			String myname = datasetArrayList.get(1);
			enemyIDString = hostname;
			OutputStream os = this.socket1.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);

			for (OthelloRoom room : server.roomList) {

				if (room.hostID.equals(hostname)) {

					room.enterSocket1 = socket1;
					room.enterSocket2 = socket2;
					enemySocket1 = room.hostSocket1;
					enemySocket2 = room.hostSocket2;
					for (Match deleteMatch : server.matchList) {
						if (deleteMatch.playerId.equals(hostname)) {
							ArrayList<String> sendpack = new ArrayList<String>();
							sendpack.add(String.valueOf(deleteMatch.rule));
							sendpack.add(myname);
							sendToEnemy("BattleStart", sendpack);
							server.matchList.remove(server.matchList.indexOf(deleteMatch));

							oos.writeObject(1);

							return;
						}
					}
				}
			}

			oos.writeObject(0);
			return;
		}
		if (operation.equals("StartSet")) {
			String nameString = (String) data;
			enemyIDString = nameString;
			for (OthelloRoom room : server.roomList) {
				if (room.hostID.equals(nameString)) {
					enemySocket1 = room.enterSocket1;
					enemySocket2 = room.enterSocket2;
					return;
				}
			}
		}
		if (operation.equals("SendAction")) {
ArrayList<Point> send=(ArrayList<Point>)data;
			sendToEnemy("action", send);
		}
		if (operation.equals("SendChat")) {
			String messageString = (String) data;
			sendToEnemy("chat", messageString);
		}
		if (operation.equals("MakeGameRecord")) {
			server.makeGameRecord(data, this);
			return;
		}

		if (operation.equals("friend_add")) {
			server.friend_add(data, this);

			return;
		}
		if (operation.equals("friend_refuse")) {
			server.friend_refuse(data, this);

			return;
		}
		if (operation.equals("getFriendrequest")) {
			server.getFriendrequest(data, this);

			return;
		}
		if (operation.equals("friendrequest")) {
			server.friendrequest(data, this);

			return;
		}
		if (operation.equals("delfriend")) {
			server.delfriend(data, this);
			return;
		}
		if (operation.equals("getfr")) {
			server.getfr(data, this);
		}
		if (operation.equals("reloadFriendList")) {
			server.getFriendList(data, this);
			return;
		}
		if (operation.equals("setStatus")) {
			int st = (int) data;
			server.setStatus(playerIDString, st);
			return;
		}

		if (operation.equals("gamerecord")) {
			server.gamerecord(data, this);

			return;
		}
//
		if (operation.equals("geticon")) {
			//ArrayList<SendIcon> iconData = (ArrayList<SendIcon>) data;
			//SendIcon iconData = (SendIcon) data;



			server.geticon(data, this);
			//server.geticon(iconData , this);
//

			return;
		}
		if (operation.equals("ApplyBattle")) {

			if (server.applyBattleSet(data, this)) {
				ArrayList<String> recievedata = (ArrayList<String>) data;
				ArrayList<String> pack = new ArrayList<String>();
				pack.add(playerIDString);
				pack.add(recievedata.get(1));
				sendToEnemy("ReceiveBattleApply", pack);
			}
			return;
		}
		if (operation.equals("RefuseRequest")) {
			server.getEnemyThread(data, this);
			sendToEnemy("RefuseBattleApply", 0);
			return;
		}
		if (operation.equals("OKRequest")) {

			ArrayList<String> packArrayList = (ArrayList<String>) data;
			ArrayList<String> sendpack = new ArrayList<String>();
			sendpack.add(playerIDString);
			sendpack.add(packArrayList.get(1));
			server.getEnemyThread(packArrayList.get(0), this);
			sendToEnemy("OKBattleApply", sendpack);
			return;
		}
		if (operation.equals("ApplyMSG")) {
			ArrayList<String> pack = (ArrayList<String>) data;
			String enemyid = pack.get(0);
			int msg = Integer.parseInt(pack.get(1));
			int i = 0;

			if (server.clientList.size() != 0) {
				do {
					if (server.clientList.get(i).playerIDString.equals(enemyid)) {

						OutputStream os = server.clientList.get(i).socket1.getOutputStream();
						ObjectOutputStream oos = new ObjectOutputStream(os);
						oos.writeObject(msg);

					}
					i++;
				} while (i < server.clientList.size());

			}

		}

	}
}
