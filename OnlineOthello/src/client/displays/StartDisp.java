package client.displays;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import client.OthelloClient;

public class StartDisp extends JPanel {
	JButton toForget;
	JButton toMakeAccount;
	JLabel errors;
	JLabel iDLabel;
	JLabel pasLabel;
	JTextField IDin;
	JTextField pasin;
	JButton login;
	JLabel logoLabel = new JLabel(new ImageIcon("image/SystemImage/ろご.png"));

	StartDisp() {
		setSize(1000, 600);
		this.setLayout(null);
		logoLabel.setBounds(300, 50, 400, 200);
		this.add(logoLabel);
		Font a = new Font("MS ゴシック", Font.BOLD, 14);

		toForget = new JButton("パスワードを忘れた方");
		toMakeAccount = new JButton("アカウント作成");
		iDLabel = new JLabel("ID");
		pasLabel = new JLabel("パスワード");
		logoLabel.setBounds(300, 50, 400, 200);
		errors = new JLabel("");
		errors.setFont(new Font("MS ゴシック", Font.BOLD, 14));
		errors.setForeground(Color.red);
		errors.setBounds(400, 220, 250, 50);

		iDLabel.setBounds(380, 280, 100, 10);
		IDin = new JTextField();
		IDin.setBounds(400, 270, 200, 30);

		pasLabel.setBounds(335, 305, 100, 20);
		pasin = new JTextField();
		pasin.setBounds(400, 300, 200, 30);

		toForget.setBounds(400, 450, 200, 40);
		toForget.addActionListener(new toForgetB());
		toForget.setBackground(new Color(51, 102, 255));
		toForget.setForeground(Color.white);
		toForget.setFont(a);

		toMakeAccount.setBounds(400, 400, 200, 40);
		toMakeAccount.addActionListener(new toMakeAccountB());
		toMakeAccount.setBackground(new Color(51, 102, 255));
		toMakeAccount.setForeground(Color.white);
		toMakeAccount.setFont(a);

		login = new JButton("ログイン");
		login.setBounds(450, 335, 100, 50);
		login.addActionListener(new Login());
		login.setBackground(new Color(51, 102, 255));
		login.setForeground(Color.white);
		login.setFont(a);
		this.add(toMakeAccount);
		this.add(errors);
		this.add(pasin);
		this.add(IDin);
		this.add(toForget);
		this.add(iDLabel);
		this.add(login);
		this.add(pasLabel);

	}

	public class toForgetB implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Disp.ChangeDisp(Disp.forget);
		}
	}

	public class Login implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {

				// if(失敗){サブモーダルで失敗表示}

				// else
				String id = IDin.getText();
				String password = pasin.getText();

				if (StringUtils.isEmpty(id)) {
					errors.setText("IDを入力してください");
					return;
				}
				if (StringUtils.isEmpty(password)) {
					errors.setText("パスワードを入力してください");
					return;
				}
				String hashedPassword = DigestUtils.sha1Hex(password);

				ArrayList<String> logInData = new ArrayList<String>();
				logInData.add(id);
				logInData.add(hashedPassword);
				OthelloClient.send("logIn", logInData);

				InputStream is = OthelloClient.socket1.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				String message = (String) ois.readObject();
				if (message.equals("failed")) {
					errors.setText("ログインできませんでした");
				} else if (message.equals("success")) {
					Disp.mainmenu.reloadMyPlayer(IDin.getText());
					Disp.mainmenu.reloadMainmenu();
					Disp.ChangeDisp(Disp.mainmenu);

				}

				// Disp.myPlayer=サーバから来たオブジェクト
			} catch (IOException err) {
				err.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.lightGray);
		g2d.fillRect(370, 270, 60, 30);
		g2d.fillRect(330, 300, 80, 30);

	}

	public class toMakeAccountB implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			// OthelloClient.send("makeAccount", object);
			Disp.ChangeDisp(Disp.makeaccount);
		}
	}

}