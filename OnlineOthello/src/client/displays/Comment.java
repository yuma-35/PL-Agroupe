package client.displays;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import client.OthelloClient;
import model.Client;

public class Comment extends JPanel {

	//ひとこと情報が書かれたテキストファイル
	File f = new File("save-data/comment.txt");

	//ボタン
	JButton toSounds = new JButton("音量調整");
	JButton toMain = new JButton("決定");

	Color color = new Color(51, 102, 255);

	//テキスト
	JLabel name = new JLabel("ひとこと編集");
	JLabel name2 = new JLabel("ひとこと");
	JLabel error = new JLabel("");

	//テキストエリア
	JTextArea textarea = new JTextArea();

	Comment() {
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

		//決定ボタン
		toMain.setFont(new Font("MS ゴシック", Font.BOLD, 10));
		toMain.setForeground(Color.WHITE);
		toMain.setBackground(color);
		toMain.setBounds(487, 510, 90, 25);
		toMain.addActionListener(new toStartM());
		this.add(toMain);

		//タイトル、
		name.setFont(new Font("MS ゴシック", Font.BOLD, 20));
		name.setForeground(Color.WHITE);
		name.setBounds(200, 70, 300, 100);
		this.add(name);
		name2.setFont(new Font("MS ゴシック", Font.BOLD, 15));
		name2.setForeground(Color.WHITE);
		name2.setBounds(350, 150, 100, 100);
		this.add(name2);
		error.setFont(new Font("MS ゴシック", Font.BOLD, 15));
		error.setForeground(Color.RED);
		error.setBounds(170, 500, 300, 50);
		this.add(error);

		//テキストエリア
		getText();
		textarea.setFont(new Font("MS ゴシック", Font.BOLD, 15));
		textarea.setBounds(430, 190, 200, 100);
		this.add(textarea);

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(170, 90, 680, 370);
	}

	//音量調整ボタン
	public class toStartS implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Account.subWindow = new Music(Disp.disp, ModalityType.MODELESS);
			Account.subWindow.setLocation(440, 220);
			Account.subWindow.setVisible(true);
		}
	}

	//決定ボタン
	public class toStartM implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//テキスト取得
			String str = textarea.getText();

			//ファイルに書き込む
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(f));
				//bw.newLine();//OSデフォルトの改行を挿入
				bw.write(str);
				bw.close();

			} catch (IOException e1) {
				System.out.println(e1);
			}

			//サーバへデータ送信
			try {
				ArrayList<String> data = new ArrayList<String>();
				//Client.myPlayer.id = "peach";  //実験用
				data.add(Client.myPlayer.id);
				data.add(str);
				OthelloClient.send("addCComment", data);
				InputStream is = OthelloClient.socket1.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				String message = (String) ois.readObject();
				if(message.equals("failed")) {
					error.setText("エラーが発生しました");
					return;
				}
				if(message.equals("success")) {
					Disp.ChangeDisp(Disp.account);
				}
			} catch (IOException | ClassNotFoundException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
		}
	}

	//テキストエリアの中身を取得
	void getText() {
		//現在のひとことを取得
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));

			String str;
			while ((str = br.readLine()) != null) {
				textarea.append(str);
			}

			br.close();

		} catch (IOException e1) {
			System.out.println(e1);
		}
	}

	//テキストエリアの中身を返す
	public String getComment() {
		String str2 = null;
		//現在のひとことを取得
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));

			String str;
			while ((str = br.readLine()) != null) {
				str2 = str;
			}

			br.close();

		} catch (IOException e1) {
			System.out.println(e1);
		}
		return str2;
	}

}
