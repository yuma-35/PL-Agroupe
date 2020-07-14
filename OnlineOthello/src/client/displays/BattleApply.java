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

public class BattleApply extends JPanel {

	Timer timer = new Timer();
	JButton deleteBoard = new JButton("キャンセル");
	public JLabel enemyID = new JLabel("taro1234");
	JLabel rulelabel = new JLabel("Item Battle");
	JLabel waitLabel = new JLabel("対戦相手を待っています");
	int waitLabelcount = 0;
	JLabel taiki = new JLabel("対戦申込画面");
public int rule;
	Font a = new Font("MS ゴシック", Font.BOLD, 24);

	public BattleApply() {

		setSize(1000, 600);
		this.setLayout(null);

		taiki.setBounds(260, 60, 200, 100);

		enemyID.setBounds(400, 180, 200, 100);
		enemyID.setHorizontalAlignment(JLabel.CENTER);
		waitLabel.setBounds(395, 250, 200, 40);

		rulelabel.setBounds(400, 270, 200, 100);
		rulelabel.setHorizontalAlignment(JLabel.CENTER);
		deleteBoard.setBounds(400, 450, 200, 40);
		deleteBoard.addActionListener(new deleteB());
		deleteBoard.setBackground(new Color(51, 102, 255));
		deleteBoard.setForeground(Color.white);
		enemyID.setFont(a);
		rulelabel.setFont(a);
		this.add(deleteBoard);
		this.add(enemyID);
		this.add(rulelabel);
		this.add(waitLabel);
		this.add(taiki);
		timer.cancel();
		timer = null;
	}

	public void reloadBattleApply(int item,String name) {
		//	enemyID.setText(Client.myPlayer.id);
		rule=item;
		enemyID.setText(name);
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				if (waitLabelcount == 0) {
					waitLabel.setText( "に対戦を申し込んでいます");
					waitLabelcount++;
				} else if (waitLabelcount == 1) {
					waitLabel.setText("に対戦を申し込んでいます・");
					waitLabelcount++;
				} else if (waitLabelcount == 2) {
					waitLabel.setText("に対戦を申し込んでいます・・");
					waitLabelcount++;
				} else if (waitLabelcount == 3) {
					waitLabel.setText("に対戦を申し込んでいます・・・");
					waitLabelcount = 0;
				}
			}
		}, 0, 1000);
		if (item == 1) {
			rulelabel.setText("アイテム戦");
		} else {
			rulelabel.setText("通常戦");
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
				enemyID.setText("");
				rule=3;
			timer.cancel();
			timer = null;
			try {
				Disp.mainmenu.reloadMainmenu();
			} catch (ClassNotFoundException | IOException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
			Disp.ChangeDisp(Disp.mainmenu);
		}
	}

}
