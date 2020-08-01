package client.displays;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.OthelloClient;
import model.Client;

public class OthelloWait extends JPanel {

	Timer timer = new Timer();
	JButton deleteBoard = new JButton("卓を削除");
	JLabel myID = new JLabel("taro1234");
	JLabel rule = new JLabel("Item Battle");
	JLabel waitLabel = new JLabel("対戦相手を待っています");
	int waitLabelcount = 0;
	JLabel taiki = new JLabel("対戦待機画面");
	JLabel senkouJLabel = new JLabel("先攻　黒");
	JLabel koukouJLabel = new JLabel("後攻　白");
	Font a = new Font("MS ゴシック", Font.BOLD, 24);

	OthelloWait() {
		setSize(1000, 600);
		this.setLayout(null);

		taiki.setBounds(260, 60, 200, 100);

		senkouJLabel.setBounds(280, 150, 200, 100);

		koukouJLabel.setBounds(280, 220, 200, 100);

		myID.setBounds(400, 150, 200, 100);

		waitLabel.setBounds(400, 350, 200, 40);

		rule.setBounds(600, 150, 200, 100);

		deleteBoard.setBounds(400, 450, 200, 40);
		deleteBoard.addActionListener(new deleteB());
		deleteBoard.setBackground(new Color(51, 102, 255));
		deleteBoard.setForeground(Color.white);
		senkouJLabel.setFont(a);
		koukouJLabel.setFont(a);
		myID.setFont(a);
		rule.setFont(a);
		this.add(deleteBoard);
		this.add(myID);
		this.add(rule);
		this.add(senkouJLabel);
		this.add(koukouJLabel);
		this.add(waitLabel);
		this.add(taiki);
		timer.cancel();
		timer = null;
	}

	public void reloadOthelloWait(int item) {
		myID.setText(Client.myPlayer.id);
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				if (waitLabelcount == 0) {
					waitLabel.setText("対戦相手を待っています・");
					waitLabelcount++;
				} else if (waitLabelcount == 1) {
					waitLabel.setText("対戦相手を待っています・・");
					waitLabelcount++;
				} else if (waitLabelcount == 2) {
					waitLabel.setText("対戦相手を待っています・・・");
					waitLabelcount++;
				} else if (waitLabelcount == 3) {
					waitLabel.setText("対戦相手を待っています");
					waitLabelcount = 0;
				}
			}
		}, 0, 1000);
		if (item == 1) {
			rule.setText("アイテム戦");
		} else if (item == 0) {
			rule.setText("通常戦");
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(250, 100, 500, 300);//矩形の塗りつぶし
	}

	public class deleteB implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			timer.cancel();
			timer = null;
			try {
				OthelloClient.send("deleteMatch",Client.myPlayer.id);
				OthelloClient.send("deleteRoom",Client.myPlayer.id);
				OthelloClient.send("setStatus", 1);	
			} catch (IOException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
			try {
				Disp.disp.mainmenu.reloadMainmenu();
			} catch (ClassNotFoundException | IOException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
			Disp.ChangeDisp(Disp.mainmenu);
		}
	}

}
