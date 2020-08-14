package client.displays;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import client.OthelloClient;
import model.Client;
import model.SendIcon;

public class ChangeIcon extends JPanel {
	String str = "アイコンの変更";

	//ファイル名を取得
	File file = new File("image/アイコン");
	File files[] = file.listFiles();

	//変更を保存するファイル
	File f = new File("save-data/icon.txt");

	//JRadioButton
	JRadioButton radio1;
	JRadioButton radio2;
	JRadioButton radio3;
	JRadioButton radio4;
	JRadioButton radio5;

	//ボタン
	JButton toSounds = new JButton("音量調整");
	JButton toMain = new JButton("決定");

	Color color = new Color(51, 102, 255);

	//ラベル
	JLabel name = new JLabel();
	JLabel error = new JLabel();

	JPanel p = new JPanel();
	int j = 0;

	ChangeIcon() {
		p.setLayout(null);

		//画面サイズは固定
		setSize(1000, 600);
		this.setLayout(null);

		//タイトル
		name.setFont(new Font("MS ゴシック", Font.BOLD, 20));
		name.setText(str);
		name.setForeground(Color.WHITE);
		name.setBounds(200, 70, 300, 100);
		this.add(name);
		error.setFont(new Font("MS ゴシック", Font.BOLD, 15));
		error.setForeground(Color.RED);
		error.setBounds(170, 500, 300, 50);
		this.add(error);

		//ボタン
		ButtonTest();

		//画像
		for (int i = 0; i < files.length; i++) {
			ImageTest(files[i].getPath());
		}

		p.setBackground(Color.LIGHT_GRAY);
		p.setBounds(170, 90, 680, 370);
		this.add(p);

	}

	//ボタン
	public void ButtonTest() {
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

		//選択ボタン
		radio1 = new JRadioButton("", true);
		//radio2 = new JRadioButton("", false);
		//radio3 = new JRadioButton("", false);
		//radio4 = new JRadioButton("", false);
		radio1.setBackground(Color.LIGHT_GRAY);
		//radio2.setBackground(Color.LIGHT_GRAY);
		//radio3.setBackground(Color.LIGHT_GRAY);
		//radio4.setBackground(Color.LIGHT_GRAY);
		ButtonGroup group = new ButtonGroup();
		group.add(radio1);
		//group.add(radio2);
		//group.add(radio3);
		//group.add(radio4);
		radio1.setBounds(220, 230, 100, 25);
		//radio2.setBounds(350, 230, 100, 25);
		//radio3.setBounds(480, 230, 100, 25);
		//radio4.setBounds(610, 230, 100, 25);
		this.add(radio1);
		//this.add(radio2);
		//this.add(radio3);
		//this.add(radio4);

		//オリジナル
		switch (files.length) {
		case 1:
			break;
		case 2:
			radio2 = new JRadioButton("", false);
			radio2.setBackground(Color.LIGHT_GRAY);
			group.add(radio2);
			radio2.setBounds(350, 230, 100, 25);
			this.add(radio2);
			break;
		case 3:
			radio2 = new JRadioButton("", false);
			radio2.setBackground(Color.LIGHT_GRAY);
			group.add(radio2);
			radio2.setBounds(350, 230, 100, 25);
			this.add(radio2);

			radio3 = new JRadioButton("", false);
			radio3.setBackground(Color.LIGHT_GRAY);
			group.add(radio3);
			radio3.setBounds(480, 230, 100, 25);
			this.add(radio3);
			break;
		case 4:
			radio2 = new JRadioButton("", false);
			radio2.setBackground(Color.LIGHT_GRAY);
			group.add(radio2);
			radio2.setBounds(350, 230, 100, 25);
			this.add(radio2);

			radio3 = new JRadioButton("", false);
			radio3.setBackground(Color.LIGHT_GRAY);
			group.add(radio3);
			radio3.setBounds(480, 230, 100, 25);
			this.add(radio3);

			radio4 = new JRadioButton("", false);
			radio4.setBackground(Color.LIGHT_GRAY);
			group.add(radio4);
			radio4.setBounds(610, 230, 100, 25);
			this.add(radio4);
			break;
		case 5:
			radio2 = new JRadioButton("", false);
			radio2.setBackground(Color.LIGHT_GRAY);
			group.add(radio2);
			radio2.setBounds(350, 230, 100, 25);
			this.add(radio2);

			radio3 = new JRadioButton("", false);
			radio3.setBackground(Color.LIGHT_GRAY);
			group.add(radio3);
			radio3.setBounds(480, 230, 100, 25);
			this.add(radio3);

			radio4 = new JRadioButton("", false);
			radio4.setBackground(Color.LIGHT_GRAY);
			group.add(radio4);
			radio4.setBounds(610, 230, 100, 25);
			this.add(radio4);

			radio5 = new JRadioButton("", false);
			radio5.setBackground(Color.LIGHT_GRAY);
			group.add(radio5);

			radio5.setBounds(740, 230, 100, 25);
			this.add(radio5);
			break;
		default:
			radio2 = new JRadioButton("", false);
			radio2.setBackground(Color.LIGHT_GRAY);
			group.add(radio2);
			radio2.setBounds(350, 230, 100, 25);
			this.add(radio2);

			radio3 = new JRadioButton("", false);
			radio3.setBackground(Color.LIGHT_GRAY);
			group.add(radio3);
			radio3.setBounds(480, 230, 100, 25);
			this.add(radio3);

			radio4 = new JRadioButton("", false);
			radio4.setBackground(Color.LIGHT_GRAY);
			group.add(radio4);
			radio4.setBounds(610, 230, 100, 25);
			this.add(radio4);

			radio5 = new JRadioButton("", false);
			radio5.setBackground(Color.LIGHT_GRAY);
			group.add(radio5);

			radio5.setBounds(740, 230, 100, 25);
			this.add(radio5);
			JOptionPane.showMessageDialog(Disp.disp, "アカウント編集画面でファイルは計5つまで表示できます。\n選択したいファイルが表示されない可能性があります。");
			break;

		}

	}

	//画像
	public void ImageTest(String str) {
		ImageIcon icon = new ImageIcon(str);

		//getScaledInstanceで大きさを変更
		//Image smallImg = icon.getImage().getScaledInstance((int) (icon.getIconWidth() * 0.07), -1,Image.SCALE_SMOOTH);
		//ImageIcon smallIcon = new ImageIcon(smallImg);

		//JLabelにアイコンを設定
		JLabel l = new JLabel(icon);

		l.setBounds(35 + j, 70, 50, 50);
		p.add(l);
		j = j + 130;
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
			//ファイルのパス名
			String str = null;
			//ファイル名
			String str2 = null;

			boolean status1 = radio1.isSelected();
			boolean status2 = false;
			boolean status3 = false;
			boolean status4 = false;
			boolean status5 = false;

			switch (files.length) {
			case 1:
				if (status1 == true) {
					str = files[0].getPath();
					str2 = files[0].getName();
				}
				break;
			case 2:
				status2 = radio2.isSelected();

				if (status1 == true) {
					str = files[0].getPath();
					str2 = files[0].getName();
				} else if (status2 == true) {
					str = files[1].getPath();
					str2 = files[1].getName();
				}
				break;
			case 3:
				status2 = radio2.isSelected();
				status3 = radio3.isSelected();

				if (status1 == true) {
					str = files[0].getPath();
					str2 = files[0].getName();
				} else if (status2 == true) {
					str = files[1].getPath();
					str2 = files[1].getName();
				} else if (status3 == true) {
					str = files[2].getPath();
					str2 = files[2].getName();
				}
				break;
			case 4:
				status2 = radio2.isSelected();
				status3 = radio3.isSelected();
				status4 = radio4.isSelected();

				if (status1 == true) {
					str = files[0].getPath();
					str2 = files[0].getName();
				} else if (status2 == true) {
					str = files[1].getPath();
					str2 = files[1].getName();
				} else if (status3 == true) {
					str = files[2].getPath();
					str2 = files[2].getName();
				} else if (status4 == true) {
					str = files[3].getPath();
					str2 = files[3].getName();
				}
				break;
			default:
				status2 = radio2.isSelected();
				status3 = radio3.isSelected();
				status4 = radio4.isSelected();
				status5 = radio5.isSelected();

				if (status1 == true) {
					str = files[0].getPath();
					str2 = files[0].getName();
				} else if (status2 == true) {
					str = files[1].getPath();
					str2 = files[1].getName();
				} else if (status3 == true) {
					str = files[2].getPath();
					str2 = files[2].getName();
				} else if (status4 == true) {
					str = files[3].getPath();
					str2 = files[3].getName();
				} else if (status5 == true) {
					str = files[4].getPath();
					str2 = files[4].getName();
				}
				break;

			}

			//ファイルに書き込む
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(f));
				//bw.newLine();//OSデフォルトの改行を挿入
				bw.write(str);
				bw.close();

				//Disp.ChangeDisp(Disp.mainmenu);
			} catch (IOException e1) {
				System.out.println(e1);
			}

			//サーバへデータ送信
			try {
				//Client.myPlayer.id = "peach";  //実験用
				SendIcon send = new SendIcon(Client.myPlayer.id, str2, new File(str));
				OthelloClient.send("addIIcon", send);

				OutputStream os = OthelloClient.socket2.getOutputStream();

				byte[] buffer = new byte[512]; // ファイル送信時のバッファ

				// 送信用
				File iconData;

				iconData = new File(str);
				// ファイルをストリームで送信

				int fileLength;
				InputStream inputStream = new FileInputStream(iconData);

				while ((fileLength = inputStream.read(buffer)) > 0) {

					os.write(buffer, 0, fileLength);

				}
				// 終了処理

				os.flush();

				inputStream.close();

				InputStream is = OthelloClient.socket1.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				String message = (String) ois.readObject();
				if (message.equals("failed")) {
					error.setText("エラーが発生しました");
					return;
				}
				if (message.equals("success")) {
					Disp.ChangeDisp(Disp.account);
				}
			} catch (IOException | ClassNotFoundException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
		}
	}

	//テキストエリアの中身を返す
	public String getIcon() {
		String str2 = null;
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
