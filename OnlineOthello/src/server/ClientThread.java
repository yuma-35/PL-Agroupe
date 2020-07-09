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

import com.mysql.cj.xdevapi.DbDoc;
import com.sun.java.swing.plaf.windows.WindowsBorders.DashedBorder;
import com.sun.org.glassfish.external.probe.provider.PluginPoint;

import model.Match;

public class ClientThread extends Thread {
	private OthelloServer server = OthelloServer.getInstance();
	public Socket socket1;
	public Socket socket2;
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
		} catch (Exception e) {
			e.printStackTrace();
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
			return;
		}
		if (operation.equals("logIn")) {
			server.logIn(data, this);
			return;
		}

		if (operation.equals("addCComment")) {
			server.addCComment(data, this);
			return;
		}
		if (operation.equals("addIIcon")) {
			server.addIIcon(data, this);
		}
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
		if(operation.equals("makeMatch")) {
			server.makeMatch(data, this);
			return;
		}
		if(operation.equals("updatePlayer")) {
			server.updatePlayer(data,this);
		}
		if(operation.equals("deleteRoom")) {
			server.deleteRoom(data,this);
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
			ArrayList<String> datasetArrayList=(ArrayList<String>)data;
			String hostname = datasetArrayList.get(0);
			String myname=datasetArrayList.get(1);
			
			for (OthelloRoom room : server.roomList) {
			
				if (room.hostID.equals(hostname)) {
					
					room.enterSocket1 = socket1;
					room.enterSocket2 = socket2;
					enemySocket1 = room.hostSocket1;
					enemySocket2 = room.hostSocket2;
					for (Match deleteMatch : server.matchList) {
						if (deleteMatch.playerId.equals(hostname)) {
							ArrayList<String> sendpack=new ArrayList<String>();
							sendpack.add(String.valueOf(deleteMatch.rule));
							sendpack.add(myname);
							sendToEnemy("BattleStart", sendpack);
							server.matchList.remove(server.matchList.indexOf(deleteMatch));
							
							return;
						}
					}
				}
			}
		}
		if (operation.equals("StartSet")) {
			String nameString = (String) data;
			for (OthelloRoom room : server.roomList) {
				if (room.hostID.equals(nameString)) {
					enemySocket1=room.enterSocket1;
					enemySocket2=room.enterSocket2;
					return;
				}
			}
		}
		if(operation.equals("SendAction")) {
			Point actionpoint=(Point)data;
			sendToEnemy("action",actionpoint);
		}
		if(operation.equals("SendChat")) {
			String messageString=(String)data;
			sendToEnemy("chat",messageString);
		}
		if(operation.equals("MakeGameRecord")) {
			server.makeGameRecord(data,this);
			return;
		}
		
	}
}
