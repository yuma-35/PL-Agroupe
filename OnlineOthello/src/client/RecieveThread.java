package client;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class RecieveThread extends Thread {
	public RecieveThread() {
	}

	public void run() {
		try {
			while (!OthelloClient.socket2.isClosed()) {
				recieve();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void recieve() throws IOException, ClassNotFoundException, EOFException {
		InputStream is = OthelloClient.socket2.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(is);
		// オペレーションを読み込む
		String operation = (String) ois.readObject();
		// データを読み込む
		Object data = ois.readObject();
	}
}
