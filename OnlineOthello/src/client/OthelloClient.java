package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.UIManager;

import model.Client;
import model.Match;
import model.Player;
import client.displays.Disp;

public class OthelloClient {
	static public Socket socket1;
	static public Socket socket2;
	public String server = "localhost";
	public int port = 4231;

	public static void main(String args[]) {
		new OthelloClient();
	}

	public OthelloClient() {

		try {
			// ソケットを2つ作成
			// サーバ受信用のソケット
			socket1 = new Socket(server, port);
			// クライアント受信用のソケット
			socket2 = new Socket(server, port);

			RecieveThread recieveThread = new RecieveThread();
			recieveThread.start();

			// Macで表示が崩れないようにする設定
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

			Disp.disp = new Disp("オセロ");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static public void send(String operation, Object object) throws IOException {
		OutputStream os = socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		// オペレーション書き込み
		oos.writeObject(operation);
		// 送信データ書き込み
		oos.writeObject(object);
	}
	static public int receiveMSG() throws IOException, ClassNotFoundException {
		int i;
		InputStream is = OthelloClient.socket1.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(is);
		i=(int)ois.readObject();
	
		return i;
	}
	static public Player getEnemy(String iDString) throws IOException, ClassNotFoundException {
		Player enePlayer;
		ArrayList<String> pk = new ArrayList<String>();
		pk.add(Client.myPlayer.id);
		pk.add(iDString);
		OthelloClient.send("getProfile", pk);
		InputStream is2 = OthelloClient.socket1.getInputStream();
		ObjectInputStream ois2 = new ObjectInputStream(is2);
		String message=(String) ois2.readObject();
		
	
		enePlayer=(Player) ois2.readObject();
		return enePlayer;
	}
}


