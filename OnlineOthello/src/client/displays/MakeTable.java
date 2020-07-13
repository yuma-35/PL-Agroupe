package client.displays;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import client.OthelloClient;
import client.displays.Othello.sendchat;
import model.Client;
import model.Match;

public class MakeTable extends JPanel {
	// ボタン
	JButton toSounds = new JButton("音量調整");
	JButton toMain = new JButton("戻る");
	JButton makeT = new JButton("卓を作る");

	Color color = new Color(51, 102, 255);

	// テキスト入力部
	JTextField password = new JTextField();
	JTextField comment = new JTextField();
	Match newMatch = new Match();
	// テキスト
	JLabel name = new JLabel("卓の作成");
	JLabel name2 = new JLabel("コメント");
	JLabel name3 = new JLabel("ルール");

	// JRadioButton
	JRadioButton radio1;
	// JRadioButton radio2;
	JRadioButton radio3;
	JRadioButton radio4;
	JRadioButton radio5;

	// 選択肢
	String[] select = { "ランク±1以内", "自分のランク以上", "フレンド限定" };
	JComboBox combo = new JComboBox(select);

	MakeTable() {
		// 画面サイズは固定
		setSize(1000, 600);
		this.setLayout(null);

		// ボタン
		ButtonTest();

		// テキスト
		name.setFont(new Font("MS ゴシック", Font.BOLD, 20));
		name.setForeground(Color.WHITE);
		name.setBounds(200, 70, 300, 100);
		this.add(name);
		name2.setFont(new Font("MS ゴシック", Font.BOLD, 15));
		name2.setForeground(Color.WHITE);
		name2.setBounds(220, 200, 200, 100);
		this.add(name2);
		name3.setFont(new Font("MS ゴシック", Font.BOLD, 15));
		name3.setForeground(Color.WHITE);
		name3.setBounds(610, 110, 200, 100);
		this.add(name3);

		// テキスト入力部
		password.setBounds(200, 210, 230, 25);
		this.add(password);
		comment.setBounds(200, 270, 230, 25);
		this.add(comment);

	}

	// ボタン
	private void ButtonTest() {
		// TODO 自動生成されたメソッド・スタブ
		// 音量調整ボタン
		toSounds.setFont(new Font("MS ゴシック", Font.BOLD, 10));
		toSounds.setForeground(Color.WHITE);
		toSounds.setBackground(color);
		toSounds.setBounds(870, 5, 90, 25);
		toSounds.addActionListener(new toStartS());
		this.add(toSounds);

		// 戻るボタン
		toMain.setFont(new Font("MS ゴシック", Font.BOLD, 10));
		toMain.setForeground(Color.WHITE);
		toMain.setBackground(color);
		toMain.setBounds(487, 510, 90, 25);
		toMain.addActionListener(new toStartM());
		this.add(toMain);

		// 卓作成ボタン
		makeT.setFont(new Font("MS ゴシック", Font.BOLD, 15));
		makeT.setForeground(Color.WHITE);
		makeT.setBackground(color);
		makeT.setBounds(430, 380, 200, 40);
		makeT.addActionListener(new toStartK());
		this.add(makeT);

		// radiobutton
		radio1 = new JRadioButton("パスワード設定", true);
		radio1.setHorizontalTextPosition(JRadioButton.LEFT);// 文字をボタンの左に
		// radio2 = new JRadioButton("フレンド限定", true);
		// radio2.setHorizontalTextPosition(JRadioButton.LEFT);
		radio3 = new JRadioButton("制限", true);
		radio3.setHorizontalTextPosition(JRadioButton.LEFT);
		radio4 = new JRadioButton("通常ルール", true);
		radio5 = new JRadioButton("アイテム", false);
		radio1.setForeground(Color.WHITE);
		// radio2.setForeground(Color.WHITE);
		radio3.setForeground(Color.WHITE);
		radio4.setForeground(Color.WHITE);
		radio5.setForeground(Color.WHITE);
		radio1.setBackground(Color.LIGHT_GRAY);
		// radio2.setBackground(Color.LIGHT_GRAY);
		radio3.setBackground(Color.LIGHT_GRAY);
		radio4.setBackground(color);
		radio5.setBackground(color);
		ButtonGroup group = new ButtonGroup();
		group.add(radio4);
		group.add(radio5);
		radio1.setBounds(220, 180, 300, 25);
		// radio2.setBounds(220, 280, 200, 25);
		radio3.setBounds(600, 250, 100, 25);
		radio4.setBounds(610, 180, 100, 25);
		radio5.setBounds(610, 200, 100, 25);
		this.add(radio1);
		// this.add(radio2);
		this.add(radio3);
		this.add(radio4);
		this.add(radio5);

		// 選択肢
		combo.setSelectedIndex(0);
		combo.setBounds(590, 280, 230, 25);
		this.add(combo);

	}

	// グレー、青の四角
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(180, 90, 650, 250);

		Graphics2D g2d2 = (Graphics2D) g;
		g2d2.setColor(color);
		g2d2.fillRect(600, 150, 180, 80);
	}

	// 音量調整ボタン
	public class toStartS implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Account.subWindow = new Music(Disp.disp, ModalityType.MODELESS);
			Account.subWindow.setLocation(440, 220);
			Account.subWindow.setVisible(true);
		}
	}

	// 戻るボタン
	public class toStartM implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				Disp.mainmenu.reloadMainmenu();
			} catch (ClassNotFoundException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
			Disp.ChangeDisp(Disp.mainmenu);
		}
	}

	// 卓を作るボタン
	public class toStartK implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			boolean status1 = radio1.isSelected();// パスワード
			// boolean status2 = radio2.isSelected();
			boolean status3 = radio3.isSelected();// 制限
			boolean status4 = radio4.isSelected();// 通常戦
			boolean status5 = radio5.isSelected();// アイテム
			newMatch.playerId = Client.myPlayer.id;
			newMatch.playerRank = Client.myPlayer.playerRank;
			newMatch.comment = comment.getText();
			if (status4) {
				newMatch.rule = 0;
			} else if (status5) {
				newMatch.rule = 1;
			}
			if (status1) {
				newMatch.password = password.getText();
			}

			if (status3) {
				newMatch.t_limit = combo.getSelectedIndex()+1;
			} else {
				newMatch.t_limit = 0;
			}

			try {
				OthelloClient.send("makeMatch",newMatch);
				OthelloClient.send("setStatus", 2);
			} catch (IOException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
			if (radio4.isSelected()) {
				Disp.othellowait.reloadOthelloWait(0);
			} else if (radio5.isSelected()) {
				Disp.othellowait.reloadOthelloWait(1);
			}
			
		
			Disp.ChangeDisp(Disp.othellowait);
		}

	}
}
