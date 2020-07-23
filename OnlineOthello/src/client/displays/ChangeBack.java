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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class ChangeBack extends JPanel {
	String str = "背景の着せ替え";

	//ファイル名を取得
	File file = new File("image/背景");
	File files[] = file.listFiles();

	//変更を保存するファイル
	File f = new File("save-data/back.txt");

	//JRadioButton
	JRadioButton radio1;
	JRadioButton radio2;
	JRadioButton radio3;
	JRadioButton radio4;
	JRadioButton radio5;
	JRadioButton radio6;
	JRadioButton radio7;
	JRadioButton radio8;
	JRadioButton radio9;
	JRadioButton radio0;

	//ボタン
	JButton toSounds = new JButton("音量調整");
	JButton toMain = new JButton("決定");

	Color color = new Color(51, 102, 255);

	//ラベル
	JLabel name = new JLabel();

	JPanel p = new JPanel();
	int j = 0;

	ChangeBack() {
		p.setLayout(null);

		//画面サイズは固定
		setSize(1000, 600);
		this.setLayout(null);

		//タイトル
		name.setFont(new Font("MS ゴシック", Font.BOLD, 20));
		name.setForeground(Color.WHITE);
		name.setText(str);
		name.setBounds(200, 70, 300, 100);
		this.add(name);

		//ボタン
		ButtonTest();

		//画像
		for (int i = 0; i < 5; i++) {
			ImageTest(files[i].getPath());
		}
		j = 0;
		for (int j = 5; j < files.length; j++) {
			ImageTest2(files[j].getPath());
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
		radio5 = new JRadioButton("", false);
		radio6 = new JRadioButton("", false);
		radio7 = new JRadioButton("", false);
		radio8 = new JRadioButton("", false);
		radio9 = new JRadioButton("", false);
		radio0 = new JRadioButton("", false);
		radio1.setBackground(Color.LIGHT_GRAY);
		radio2.setBackground(Color.LIGHT_GRAY);
		radio3.setBackground(Color.LIGHT_GRAY);
		radio4.setBackground(Color.LIGHT_GRAY);
		radio5.setBackground(Color.LIGHT_GRAY);
		radio6.setBackground(Color.LIGHT_GRAY);
		radio7.setBackground(Color.LIGHT_GRAY);
		radio8.setBackground(Color.LIGHT_GRAY);
		radio9.setBackground(Color.LIGHT_GRAY);
		radio0.setBackground(Color.LIGHT_GRAY);
		ButtonGroup group = new ButtonGroup();
		group.add(radio1);
		group.add(radio2);
		group.add(radio3);
		group.add(radio4);
		group.add(radio5);
		group.add(radio6);
		group.add(radio7);
		group.add(radio8);
		group.add(radio9);
		group.add(radio0);
		radio1.setBounds(220, 230, 100, 25);
		radio2.setBounds(350, 230, 100, 25);
		radio3.setBounds(480, 230, 100, 25);
		radio4.setBounds(610, 230, 100, 25);
		radio5.setBounds(740, 230, 100, 25);
		radio6.setBounds(220, 350, 100, 25);
		radio7.setBounds(350, 350, 100, 25);
		radio8.setBounds(480, 350, 100, 25);
		radio9.setBounds(610, 350, 100, 25);
		radio0.setBounds(740, 350, 100, 25);
		this.add(radio1);
		this.add(radio2);
		this.add(radio3);
		this.add(radio4);
		this.add(radio5);
		this.add(radio6);
		this.add(radio7);
		this.add(radio8);
		this.add(radio9);
		this.add(radio0);

		//オリジナル
		/*	if (files.length == 5) {
				radio5 = new JRadioButton("", false);
				radio5.setBackground(Color.LIGHT_GRAY);
				group.add(radio5);

				radio5.setBounds(740, 230, 100, 25);
				this.add(radio5);
			}*/

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

	public void ImageTest2(String str) {
		ImageIcon icon = new ImageIcon(str);

		//getScaledInstanceで大きさを変更
		Image smallImg = icon.getImage().getScaledInstance((int) (icon.getIconWidth() * 0.07), -1,
				Image.SCALE_SMOOTH);
		ImageIcon smallIcon = new ImageIcon(smallImg);

		//JLabelにアイコンを設定
		JLabel l = new JLabel(smallIcon);

		l.setBounds(10 + j, 170, 100, 100);
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
			boolean status6 = radio6.isSelected();
			boolean status7 = radio7.isSelected();
			boolean status8 = radio8.isSelected();
			boolean status9 = radio9.isSelected();
			boolean status0 = radio0.isSelected();

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
			} else if (status6 == true) {
				str = files[5].getPath();
			} else if (status7 == true) {
				str = files[6].getPath();
			} else if (status8 == true) {
				str = files[7].getPath();
			} else if (status9 == true) {
				str = files[8].getPath();
			} else if (status0 == true) {
				str = files[9].getPath();
			}

			//ファイルに書き込む
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(f));
				//bw.newLine();//OSデフォルトの改行を挿入
				bw.write(str);
				bw.close();

				Disp.setBack();
				Disp.ChangeDisp(Disp.account);

			} catch (IOException e1) {
				System.out.println(e1);
				JOptionPane.showMessageDialog(Disp.disp, "エラーが発生しました");
			}
		}

		//テキストエリアの中身を返す
		public String getBack() {
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
				JOptionPane.showMessageDialog(Disp.disp, "エラーが発生しました");
			}
			return str2;

		}
	}

	//テキストエリアの中身を返す
	public String getBack() {
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
			JOptionPane.showMessageDialog(Disp.disp, "エラーが発生しました");
		}
		return str2;

	}

}
