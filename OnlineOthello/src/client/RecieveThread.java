package client;

import java.awt.Point;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

//import com.sun.org.glassfish.external.probe.provider.PluginPoint;

import client.displays.Disp;
import model.Client;

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

		if (operation.equals("BattleStart")) {
			ArrayList<String> pack = (ArrayList<String>) data;
			int rule = Integer.parseInt(pack.get(0));
			String enemyID = pack.get(1);
			OthelloClient.send("StartSet", Client.myPlayer.id);
			Disp.ChangeDisp(Disp.othello);
			Disp.othello.startOthello(rule, 0, enemyID);
		}
		if (operation.equals("action")) {
			Point actionPoint = (Point) data;
			if (actionPoint.x < 9) {
				Disp.othello.flip(actionPoint.x, actionPoint.y);
				Disp.disp.othello.passEndListenner = false;
			} else if (actionPoint.x == 11) {
				Disp.othello.Item1();
				Disp.disp.othello.passEndListenner = false;
			} else if (actionPoint.x == 12) {
				Disp.othello.Item2();
				Disp.disp.othello.passEndListenner = false;
			} else if (actionPoint.x == 13) {
				Disp.othello.Item3();
				Disp.disp.othello.passEndListenner = false;
			} else if (actionPoint.x == 14) {
				Disp.othello.Item4();
				Disp.disp.othello.passEndListenner = false;
			} else if (actionPoint.x == 15) {
				Disp.othello.Item5();
				Disp.disp.othello.passEndListenner = false;
			} else if (actionPoint.x == 9) {
				// aパスなのでスル-
				if (Disp.disp.othello.passEndListenner) {
					if (Disp.disp.othello.bw == 0) {// hostならroomを消す
						OthelloClient.send("deleteRoom", Client.myPlayer.id);
					}
					Disp.disp.othello.gameEnd(Disp.disp.othello.countEnd());
				return;
				}
				Disp.disp.othello.passEndListenner = true;
				}
			 else if (actionPoint.x == 10) {
				Disp.disp.othello.gameEnd(2);
				return;
			}

			Disp.othello.myTurn = true;
			if (Disp.othello.boardRepaint()) {
				OthelloClient.send("SendAction", new Point(9, 9));
				if (Disp.disp.othello.passEndListenner) {
					if (Disp.disp.othello.bw == 0) {// hostならroomを消す
						OthelloClient.send("deleteRoom", Client.myPlayer.id);
					}
					Disp.disp.othello.gameEnd(Disp.disp.othello.countEnd());
				}
				Disp.disp.othello.passEndListenner = true;

				Disp.othello.myTurn = false;
				return;
			}
		}
		if (operation.equals("chat")) {
			String newChat = (String) data;
			Disp.disp.othello.chatBuild.append(Disp.disp.othello.enemyPlayer.id + "：" + newChat + "\n");
			Disp.disp.othello.chatArea.setText(Disp.disp.othello.chatBuild.toString());
		}
	}
}
