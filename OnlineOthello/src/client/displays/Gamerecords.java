package client.displays;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.Client;

public class Gamerecords extends JPanel {
	//ボタン
	JButton toSounds = new JButton("音量調整");
	JButton toMain = new JButton("戻る");

	Color color = new Color(51, 102, 255);

	//テキスト
	JLabel name = new JLabel("対局記録");
	JLabel recordsnum = new JLabel();
	JLabel totalnum = new JLabel();
	JLabel opponent = new JLabel("相手");

	//テキストエリア
	JTextArea textarea = new JTextArea();
	JScrollPane scrollpane = new JScrollPane(textarea);

	Gamerecords() {
		//画面サイズは固定
		setSize(1000, 600);
		this.setLayout(null);

		//音量調整ボタン
		toSounds.setFont(new Font("MS ゴシック", Font.BOLD, 10));
		toSounds.setForeground(Color.WHITE);
		toSounds.setBackground(color);
		toSounds.setBounds(870, 5, 90, 25);
		toSounds.addActionListener(new toStartS());
		this.add(toSounds);

		//戻るボタン
		toMain.setFont(new Font("MS ゴシック", Font.BOLD, 10));
		toMain.setForeground(Color.WHITE);
		toMain.setBackground(color);
		toMain.setBounds(487, 510, 90, 25);
		toMain.addActionListener(new toStartM());
		this.add(toMain);

		//タイトル、記録数
		name.setFont(new Font("MS ゴシック", Font.BOLD, 20));
		name.setForeground(Color.WHITE);
		name.setBounds(200, 80, 100, 100);
		this.add(name);
		getRecordsNum();
		recordsnum.setFont(new Font("MS ゴシック", Font.BOLD, 20));
		recordsnum.setForeground(Color.WHITE);
		recordsnum.setBounds(300, 80, 100, 100);
		this.add(recordsnum);

		//全体記録
		getTotalNum();
		totalnum.setFont(new Font("MS ゴシック", Font.BOLD, 20));
		totalnum.setForeground(Color.WHITE);
		totalnum.setBounds(220, 110, 300, 100);
		this.add(totalnum);

		//相手
		opponent.setFont(new Font("MS ゴシック", Font.BOLD, 20));
		opponent.setForeground(Color.WHITE);
		opponent.setBounds(180, 160, 100, 100);
		this.add(opponent);

		//テキストエリア
		getText();
		textarea.setEditable(false);//書き換え不可
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		textarea.setFont(new Font("MS ゴシック", Font.BOLD, 20));
		textarea.setForeground(Color.WHITE);
		textarea.setBackground(Color.LIGHT_GRAY);
		scrollpane.setBounds(200, 220, 600, 210);
		this.add(scrollpane);

	}

	//グレーの四角
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(170, 90, 650, 400);
	}

	//音量調整ボタン
	public class toStartS implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Account.subWindow = new Music(Disp.disp, ModalityType.MODELESS);
			Account.subWindow.setLocation(440, 220);
			Account.subWindow.setVisible(true);
		}
	}

	//戻るボタン
	public class toStartM implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Disp.mainmenu.reloadMainmenu();
			Disp.ChangeDisp(Disp.mainmenu);
		}
	}

	//記録数取得
	private void getRecordsNum() {
		recordsnum.setText("n" + "/100");//nを書き換える
	}

	//全体記録数
	private void getTotalNum() {
		// TODO 自動生成されたメソッド・スタブ
		totalnum.setText(Client.myPlayer.win + "勝  " + Client.myPlayer.lose + "敗  "
				+ Client.myPlayer.draw + "引き分け  "
				+ Client.myPlayer.conceed + "投了");

	}

	//テキストエリアの中身
	private void getText() {
		//クリア
		textarea.setText("");
		for (int i = 1; i <= 100; i++) {
			textarea.append(i + "\n");//対戦記録を取得する
		}
	}

	//再読み込み
	void reloadGamerecords() {
		getRecordsNum();
		getTotalNum();
		getText();
	}
}
