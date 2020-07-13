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
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import client.OthelloClient;
import model.Client;
import model.GameRecordToPlayer;

public class Gamerecords extends JPanel {
	//受け取り用
	ArrayList<GameRecordToPlayer> data = new ArrayList<GameRecordToPlayer>();

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

	//記録数取得
	private void getRecordsNum() {
		if (data == null) {
			recordsnum.setText(0 + "/100");
		} else {
			recordsnum.setText(data.size() + "/100");
		}
	}

	//全体記録数
	private void getTotalNum() {
		// TODO 自動生成されたメソッド・スタブ
		totalnum.setText(Client.myPlayer.getWin() + "勝  " + Client.myPlayer.getLose() + "敗  "
				+ Client.myPlayer.getDraw() + "引き分け  "
				+ Client.myPlayer.getConceed() + "投了");

	}

	//テキストエリアの中身
	private void getText() {
		//クリア
		textarea.setText("");
		if (data != null) {
			for (int i = 1; i <= 100; i++) {
				if (i <= data.size()) {
					textarea.append(i + "   " + data.get(i - 1).getOpponentId() + "  "
							+ data.get(i - 1).getWin() + "勝  "
							+ data.get(i - 1).getLose() + "敗  "
							+ data.get(i - 1).getDraw() + "引き分け  "
							+ data.get(i - 1).getConceed() + "投了" + "\n");//対戦記録を取得する
				} else {
					;
				}
			}
		} else {
			textarea.setText("                              対局記録がありません");
		}
	}

	//myPlayer記録の書き換え
	/*void setPlayerrecord() {
		//reset
		Client.myPlayer.win = 0;
		Client.myPlayer.lose = 0;
		Client.myPlayer.draw = 0;
		Client.myPlayer.conceed = 0;

		if (data != null) {
			for (int i = 0; i < data.size(); i++) {
				Client.myPlayer.win += data.get(i).getWin();
				Client.myPlayer.lose += data.get(i).getLose();
				Client.myPlayer.draw += data.get(i).getDraw();
				Client.myPlayer.conceed += data.get(i).getConceed();
			}
		}
	}*/

	//再読み込み
	void reloadGamerecords() {
		try {
			//サーバへデータ送信
			//Client.myPlayer.id = "peach"; //実験用
			OthelloClient.send("gamerecord", Client.myPlayer.id);

			//受け取り
			InputStream is = OthelloClient.socket1.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			String message = (String) ois.readObject();
			if (message.equals("failed")) {
				JOptionPane.showMessageDialog(Disp.disp, "エラーが発生しました");
				return;
			}
			if (message.equals("success")) {
				data = (ArrayList<GameRecordToPlayer>) ois.readObject();
			}
		} catch (IOException | ClassNotFoundException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}

		getRecordsNum();
		getTotalNum();
		getText();
	}
}
