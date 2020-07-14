package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.Dialog.ModalityType;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import client.RecieveThread.FriendBattleRequestDialog;
import client.displays.BattleApply;

//import com.sun.org.glassfish.external.probe.provider.PluginPoint;

import client.displays.Disp;
import client.displays.BattleRequest.toRelease;
import client.displays.Mainmenu.FriendBattleRequest;
import model.Client;

public class RecieveThread extends Thread {
	public FriendBattleRequestDialog fbrdBox;

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
			OthelloClient.send("setStatus", 3);
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
			} else if (actionPoint.x == 10) {
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
		if (operation.equals("EnemyDisconected")) {
			System.out.println("終わった");
			if (Disp.disp.othello.bw == 0) {// hostならroomを消す
				OthelloClient.send("deleteRoom", Client.myPlayer.id);
			}
			Disp.disp.othello.gameEnd(4);

		}
		if (operation.equals("ReceiveBattleApply")) {
			ArrayList<String> receivePack = (ArrayList<String>) data;
			String nameString = receivePack.get(0);
			int rule = Integer.parseInt(receivePack.get(1));
			fbrdBox = new FriendBattleRequestDialog(Disp.disp, ModalityType.APPLICATION_MODAL, nameString, rule);
			fbrdBox.setLocation(440, 220);
			fbrdBox.setVisible(true);
		}
		if (operation.equals("OKBattleApply")) {
			if (Disp.disp.nowPanel == Disp.disp.battleApply) {
				String enemyID = (String) data;
				Disp.othello.startOthello(Disp.disp.battleApply.rule, 0, enemyID);
				Disp.disp.ChangeDisp(Disp.othello);
			} else {
				// aもうとりさげたよ

			}

		}
		if (operation.equals("RefuseBattleApply")) {
			JOptionPane.showMessageDialog(Disp.disp, "対戦申込が拒否されました");
			Disp.disp.mainmenu.reloadMainmenu();
			Disp.disp.ChangeDisp(Disp.disp.mainmenu);
		}
	}

	public class FriendBattleRequestDialog extends JDialog {
		Disp disp;
		String request;
		int rulebox;

		public FriendBattleRequestDialog(Disp disp, ModalityType mt, String name, int rule) {
			super(disp, mt);
			this.disp = disp;
			this.setLayout(null);
			this.setSize(500, 300);
			request = name;
			rulebox=rule;
			JLabel enemyJLabel=new JLabel();
			JLabel ruleJLabel=new JLabel();
			enemyJLabel.setText(request);
			enemyJLabel.setBounds(150, 100, 200, 50);
			enemyJLabel.setHorizontalAlignment(JLabel.CENTER);
			enemyJLabel.setFont(new Font("MS ゴシック",Font.BOLD,20));
			
			this.add(enemyJLabel);
			if(rule==0) {
			ruleJLabel.setText("通常戦");
			}else {
				ruleJLabel.setText("アイテム戦");
			}
			ruleJLabel.setBounds(150, 170,200,50 );
			ruleJLabel.setFont(new Font("MS ゴシック",Font.BOLD,20));
			ruleJLabel.setHorizontalAlignment(JLabel.CENTER);
			this.add(ruleJLabel);
			JLabel label = new JLabel("フレンドから対戦申し込みが届きました");
			label.setFont(new Font("MS ゴシック", Font.BOLD, 15));
			label.setForeground(Color.BLACK);
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBounds(100, 50, 300, 20);
			this.add(label);

			JButton no = new JButton("拒否");
			no.setFont(new Font("MS ゴシック", Font.BOLD, 10));
			no.setBounds(270, 200, 60, 30);
			no.setForeground(Color.WHITE);
			no.setBackground(new Color(51, 102, 255));
			no.addActionListener(new toRelease());
			this.add(no);

			JButton yes = new JButton("承認");
			yes.setFont(new Font("MS ゴシック", Font.BOLD, 10));
			yes.setBounds(170, 200, 60, 30);
			yes.setForeground(Color.WHITE);
			yes.setBackground(new Color(51, 102, 255));
			yes.addActionListener(new OK());
			this.add(yes);
			this.addWindowListener(new WinListener());
		}

		class WinListener implements WindowListener {

			public void windowOpened(WindowEvent e) {
				/* 処理したい内容をここに記述する */
			}

			public void windowClosing(WindowEvent e) {
				// for (int i = 0; i < request.length; i++) {
				try {
					OthelloClient.send("RefuseRequest", request);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			// }

			public void windowClosed(WindowEvent e) {
				/* 処理したい内容をここに記述する */
			}

			public void windowIconified(WindowEvent e) {
				/* 処理したい内容をここに記述する */
			}

			public void windowDeiconified(WindowEvent e) {
				/* 処理したい内容をここに記述する */
			}

			public void windowActivated(WindowEvent e) {
				/* 処理したい内容をここに記述する */
			}

			public void windowDeactivated(WindowEvent e) {
				/* 処理したい内容をここに記述する */
			}
		}

		public class toRelease implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				try {
					OthelloClient.send("RefuseRequest", request);
				} catch (IOException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				}
				dispose();
			}
		}

		public class OK implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				try {
					OthelloClient.send("OKRequest", request);
					Disp.ChangeDisp(disp.othello);
					OthelloClient.send("setStatus", 3);
					disp.othello.startOthello(rulebox, 1,request);
				} catch (IOException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				}
				dispose();
			}
		}
	}
}
