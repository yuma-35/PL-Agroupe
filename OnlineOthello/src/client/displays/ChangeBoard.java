package client.displays;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class ChangeBoard extends JPanel {
	String str = "盤面の着せ替え";

	//ファイル名を取得
	File file = new File("image/盤面");
	File files[] = file.listFiles();

	//変更を保存するファイル
	File f = new File("save-data/board.txt");

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

	JPanel p = new JPanel();
	int j = 0;

	ChangeBoard() {
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

		//ボタン
		ButtonTest();

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
		radio2 = new JRadioButton("", false);
		radio3 = new JRadioButton("", false);
		radio4 = new JRadioButton("", false);
		radio1.setBackground(Color.LIGHT_GRAY);
		radio2.setBackground(Color.LIGHT_GRAY);
		radio3.setBackground(Color.LIGHT_GRAY);
		radio4.setBackground(Color.LIGHT_GRAY);
		ButtonGroup group = new ButtonGroup();
		group.add(radio1);
		group.add(radio2);
		group.add(radio3);
		group.add(radio4);
		radio1.setBounds(220, 230, 100, 25);
		radio2.setBounds(350, 230, 100, 25);
		radio3.setBounds(480, 230, 100, 25);
		radio4.setBounds(610, 230, 100, 25);
		this.add(radio1);
		this.add(radio2);
		this.add(radio3);
		this.add(radio4);

		//オリジナル
		if (files.length == 5) {
			radio5 = new JRadioButton("", false);
			radio5.setBackground(Color.LIGHT_GRAY);
			group.add(radio5);

			radio5.setBounds(740, 230, 100, 25);
			this.add(radio5);
		}
	}

	//画像
	public void ImageTest(String str) {
		ImageIcon icon = new ImageIcon(str);

		//getScaledInstanceで大きさを変更
		Image smallImg = icon.getImage().getScaledInstance((int) (icon.getIconWidth() * 0.07), -1,
				Image.SCALE_SMOOTH);
		ImageIcon smallIcon = new ImageIcon(smallImg);

		//JLabelにアイコンを設定
		JLabel l = new JLabel(smallIcon);

		l.setBounds(10 + j, 50, 100, 100);
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
			boolean status1 = radio1.isSelected();
			boolean status2 = radio2.isSelected();
			boolean status3 = radio3.isSelected();
			boolean status4 = radio4.isSelected();
			boolean status5 = radio5.isSelected();

			//ファイルのパス名
			String str = null;

			if (status1 == true) {
				str = files[0].getPath();
			} else if (status2 == true) {
				str = files[1].getPath();
			} else if (status3 == true) {
				str = files[2].getPath();
			} else if (status4 == true) {
				str = files[3].getPath();
			} else if (status5 == true) {
				str = files[4].getPath();
			}

			//ファイルに書き込む
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(f));
				//bw.newLine();//OSデフォルトの改行を挿入
				bw.write(str);
				bw.close();

				Disp.ChangeDisp(Disp.account);
			} catch (IOException e1) {
				System.out.println(e1);
			}
		}
	}

	//テキストエリアの中身を返す
	public String getBoard() {
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
