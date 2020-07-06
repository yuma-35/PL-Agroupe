package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.UIManager;

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
}