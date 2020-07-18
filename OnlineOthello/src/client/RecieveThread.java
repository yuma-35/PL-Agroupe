package client;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

//import com.sun.org.glassfish.external.probe.provider.PluginPoint;

import client.displays.Disp;
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
			Disp.disp.othello.pasLabel.setText("");
			ArrayList<Point> pack=(ArrayList<Point>)data;
			Point action = pack.get(0);
			
			if (action.x == 0) {
				Point xy=pack.get(1);
				Disp.othello.flip(xy.x, xy.y);
				Disp.disp.othello.passEndListenner = false;
			} else if (action.x == 11) {
				Point xy=pack.get(1);
				Disp.disp.othello.receiveItem1(xy);
				Disp.disp.othello.enemyItem1Button.setEnabled(false);
				Disp.disp.othello.passEndListenner = false;
				Disp.disp.othello.pasLabel.setText("相手がBombを使用しました");
			} else if (action.x == 12) {
				Point xy=pack.get(1);
				Disp.disp.othello.receiveItem2(xy);
				Disp.disp.othello.enemyItem2Button.setEnabled(false);
				Disp.disp.othello.passEndListenner = false;
				Disp.disp.othello.pasLabel.setText("相手がDiceを使用しました");
			} else if (action.x == 13) {
				Disp.disp.othello.enemyItem3Button.setEnabled(false);
				Disp.disp.othello.passEndListenner = false;
				Disp.disp.othello.pasLabel.setText("相手がSkipを使用しました");
			} else if (action.x == 14) {
				Point xy=pack.get(1);
				Disp.disp.othello.receiveItem4(xy);
				Disp.disp.othello.enemyItem4Button.setEnabled(false);
				Disp.disp.othello.passEndListenner = false;
				Disp.disp.othello.pasLabel.setText("相手がStealを使用しました");
			} else if (action.x == 15) {
				Point xy=pack.get(1);
				Disp.disp.othello.receiveItem5(xy);
				Disp.disp.othello.enemyItem5Button.setEnabled(false);
				Disp.disp.othello.passEndListenner = false;
				Disp.disp.othello.pasLabel.setText("相手がSpyを使用しました");
			} else if (action.x == 9) {
				
				if (Disp.disp.othello.passEndListenner&&!Disp.disp.othello.end) {
					if (Disp.disp.othello.bw == 0) {// hostならroomを消す
						OthelloClient.send("deleteRoom", Client.myPlayer.id);
					}
					ArrayList<Point> p=new ArrayList<Point>();
					p.add(new Point(9, 9));
					OthelloClient.send("SendAction", p);
					Disp.disp.othello.gameEnd(Disp.disp.othello.countEnd());
					Disp.disp.othello.end=true;
					return;
				}
				Disp.disp.othello.passEndListenner = true;
				Disp.disp.othello.pasLabel.setText("相手がパスしました");
			} else if (action.x == 10) {
				Disp.disp.othello.gameEnd(2);
				Disp.disp.othello.pasLabel.setText("相手が投了しました");
				return;
			}

			Disp.othello.myTurn = true;
			if (Disp.othello.boardRepaint()) {
				if(!(!Disp.disp.othello.fullFlag&&Disp.disp.othello.myItem2Button.isEnabled())
					&&!(!Disp.disp.othello.fullFlag&&Disp.disp.othello.myItem5Button.isEnabled())&&!(Disp.disp.othello.myItem4Button.isEnabled()&&Disp.disp.othello.stealFlag)&&!Disp.disp.othello.myItem1Button.isEnabled()) {
				ArrayList<Point> p=new ArrayList<Point>();
				p.add(new Point(9, 9));
				Disp.disp.othello.turnLabel.setText("相手のターンです");
				Disp.disp.othello.pasLabel.setText("できる行動がありませんでした");
				OthelloClient.send("SendAction", p);
				if (Disp.disp.othello.passEndListenner&&!Disp.disp.othello.end) {
					if (Disp.disp.othello.bw == 0) {// hostならroomを消す
						OthelloClient.send("deleteRoom", Client.myPlayer.id);
					}
					Disp.disp.othello.gameEnd(Disp.disp.othello.countEnd());
					Disp.disp.othello.end=true;
				}
				Disp.disp.othello.passEndListenner = true;

				Disp.othello.myTurn = false;
				return;
			}else {
				Disp.othello.pasLabel.setText("アイテムを使ってください");
			}
				}
		}
		if (operation.equals("chat")) {
			String newChat = (String) data;
			Disp.disp.othello.chatBuild.append(Disp.disp.othello.enemyPlayer.id + "：" + newChat + "\n");
			Disp.disp.othello.chatArea.setText(Disp.disp.othello.chatBuild.toString());
		}
		if (operation.equals("EnemyDisconected")) {
			
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
			ArrayList<String> packag=new ArrayList<String>();
			
				ArrayList<String> packa=(ArrayList<String>)data;
				String enemyID = packa.get(0);
				int getrule=Integer.parseInt(packa.get(1));
				packag.add(enemyID);
				if (Disp.disp.battleApply.enemyID.getText().equals(enemyID)&&Disp.disp.battleApply.rule==getrule) {
				OthelloClient.send("setStatus", 3);
					Disp.othello.startOthello(Disp.disp.battleApply.rule, 0, enemyID);
				Disp.disp.ChangeDisp(Disp.othello);
				packag.add("0");
				OthelloClient.send("ApplyMSG", packag);
			} else {
				packag.add("1");
					OthelloClient.send("ApplyMSG",packag);

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
			ruleJLabel.setBounds(150, 150,200,50 );
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
					ArrayList<String> pack =new ArrayList<String>();
					pack.add(request);
					pack.add(String.valueOf(rulebox));
					OthelloClient.send("OKRequest", pack);
					InputStream is = OthelloClient.socket1.getInputStream();
					ObjectInputStream ois = new ObjectInputStream(is);
				int	message=(int)ois.readObject();
					if(message==0) {//成功
						Disp.ChangeDisp(disp.othello);
						OthelloClient.send("setStatus", 3);
						disp.othello.startOthello(rulebox, 1,request);
						
					}else {
						dispose();
						JOptionPane.showMessageDialog(Disp.disp, "申込が取り下げられました");
					
					}
				
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
