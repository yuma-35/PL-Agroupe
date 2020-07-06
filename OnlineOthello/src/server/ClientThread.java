package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;

public class ClientThread extends Thread {
	private OthelloServer server = OthelloServer.getInstance();
	public Socket socket1;
	public Socket socket2;
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
	}
}
