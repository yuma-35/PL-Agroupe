package client.displays;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Account extends JPanel {
	//ボタン
	JButton toSounds = new JButton("音量調整");
	JButton toMain = new JButton("戻る");

	JButton toKoma = new JButton("コマの着せ替え");
	JButton toBoard = new JButton("盤面の着せ替え");
	JButton toBack = new JButton("背景の着せ替え");
	JButton toIcon = new JButton("アイコンの変更");
	JButton toComment = new JButton("ひとこと編集");

	Color color = new Color(51, 102, 255);

	//タイトル
	JLabel name = new JLabel("アカウント");

	static Music subWindow;

	Account() {
		//画面サイズは固定
		this.setBounds(0, 0, 1000, 600);
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

		//変更選択ボタン
		toKoma.setFont(new Font("MS ゴシック", Font.BOLD, 15));
		toKoma.setForeground(Color.WHITE);
		toKoma.setBackground(color);
		toKoma.setBounds(430, 150, 200, 40);
		toKoma.addActionListener(new toStartK());
		this.add(toKoma);
		toBoard.setFont(new Font("MS ゴシック", Font.BOLD, 15));
		toBoard.setForeground(Color.WHITE);
		toBoard.setBackground(color);
		toBoard.setBounds(430, 210, 200, 40);
		toBoard.addActionListener(new toStartI1());
		this.add(toBoard);
		toBack.setFont(new Font("MS ゴシック", Font.BOLD, 15));
		toBack.setForeground(Color.WHITE);
		toBack.setBackground(color);
		toBack.setBounds(430, 270, 200, 40);
		toBack.addActionListener(new toStartI2());
		this.add(toBack);
		toIcon.setFont(new Font("MS ゴシック", Font.BOLD, 15));
		toIcon.setForeground(Color.WHITE);
		toIcon.setBackground(color);
		toIcon.setBounds(430, 330, 200, 40);
		toIcon.addActionListener(new toStartI3());
		this.add(toIcon);
		toComment.setFont(new Font("MS ゴシック", Font.BOLD, 15));
		toComment.setForeground(Color.WHITE);
		toComment.setBackground(color);
		toComment.setBounds(430, 400, 200, 40);
		toComment.addActionListener(new toStartC());
		this.add(toComment);

		//タイトル
		name.setFont(new Font("MS ゴシック", Font.BOLD, 20));
		name.setForeground(Color.WHITE);
		name.setBounds(200, 70, 300, 100);
		this.add(name);
	}

	//グレーの四角
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(170, 90, 680, 370);
	}

	//音量調整ボタン
	public class toStartS implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Music.se();
			subWindow = new Music(Disp.disp, ModalityType.MODELESS);
			subWindow.setLocation(440, 220);
			//	this.addLogMessage("サブダイアログのモーダル表示処理を開始");
			subWindow.setVisible(true);
			//	this.addLogMessage("サブダイアログのモーダル表示処理が完了==結果==> " + subWindow.getResult());

			//Disp.ChangeDisp(Disp.music);
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

	//コマの着せ替え
	public class toStartK implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Disp.ChangeDisp(Disp.changekoma);
		}
	}

	//盤面の着せ替え
	public class toStartI1 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Disp.ChangeDisp(Disp.changeboard);
		}
	}

	//背景の着せ替え
	public class toStartI2 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Disp.ChangeDisp(Disp.changeback);
		}
	}

	//アイコンの変更
	public class toStartI3 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Disp.ChangeDisp(Disp.changeicon);
		}
	}

	//ひとこと編集
	public class toStartC implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Disp.ChangeDisp(Disp.comment);
		}
	}

}
