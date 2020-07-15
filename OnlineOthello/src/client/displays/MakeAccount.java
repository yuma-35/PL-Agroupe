//4:アカウント作成

package client.displays;

import java.awt.Color;
import java.awt.Font;
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

public class MakeAccount extends JPanel {

	JTextField id = new JTextField(32); //id
	JTextField question = new JTextField(32); //a秘密の質問
	JTextField ans = new JTextField(32); //a秘密の質問の答え
	JTextField pw = new JTextField(32); //aパスワード
	JTextField re_pw = new JTextField(32); //パスワードの確認
	JLabel errors;

	MakeAccount() {
		this.setLayout(null); //レイアウトマネージャーを無効
		setSize(1000, 600);

		ImageIcon icon = new ImageIcon("logo.png");
		JLabel othello = new JLabel(icon);
		othello.setBounds(405, 25, 170, 80);
		JLabel name = new JLabel("アカウント作成");
		name.setFont(new Font("MS ゴシック", Font.BOLD, 20));
		name.setForeground(Color.WHITE);
		name.setBounds(415, 100, 250, 50);

		errors = new JLabel();
		errors.setFont(new Font("MS ゴシック", Font.BOLD, 14));
		errors.setForeground(Color.red);
		errors.setBounds(415, 135, 250, 50);

		JLabel label1 = new JLabel("ID(プレイヤ名)");
		label1.setFont(new Font("MS ゴシック", Font.BOLD, 22));
		label1.setForeground(Color.WHITE);
		label1.setBounds(150, 190, 250, 25);
		JLabel label2 = new JLabel("秘密の質問");
		label2.setFont(new Font("MS ゴシック", Font.BOLD, 22));
		label2.setForeground(Color.WHITE);
		label2.setBounds(150, 230, 250, 25);
		JLabel label3 = new JLabel("答え");
		label3.setFont(new Font("MS ゴシック", Font.BOLD, 22));
		label3.setForeground(Color.WHITE);
		label3.setBounds(150, 270, 250, 25);
		JLabel label4 = new JLabel("新しいパスワード");
		label4.setFont(new Font("MS ゴシック", Font.BOLD, 22));
		label4.setForeground(Color.WHITE);
		label4.setBounds(150, 310, 250, 25);
		JLabel label5 = new JLabel("新しいパスワード(確認)");
		label5.setFont(new Font("MS ゴシック", Font.BOLD, 22));
		label5.setForeground(Color.WHITE);
		label5.setBounds(150, 350, 250, 25);

		id.setBounds(415, 190, 150, 25);
		question.setBounds(415, 230, 150, 25);
		ans.setBounds(415, 270, 150, 25);
		pw.setBounds(415, 310, 150, 25);
		re_pw.setBounds(415, 350, 150, 25);

		JButton back = new JButton("戻る");
		back.setFont(new Font("MS ゴシック", Font.BOLD, 15));
		back.setBounds(850, 30, 80, 30);
		back.setForeground(Color.WHITE);
		back.setBackground(new Color(51, 102, 255));
		back.addActionListener(new toStartdisp());
		JButton completion = new JButton("完了");
		completion.setFont(new Font("MS ゴシック", Font.BOLD, 20));
		completion.setBounds(415, 450, 150, 50);
		completion.setForeground(Color.WHITE);
		completion.setBackground(new Color(51, 102, 255));
		completion.addActionListener(new toMainmenu());

		this.add(othello);
		this.add(name);
		this.add(errors);
		this.add(label1);
		this.add(id);
		this.add(label2);
		this.add(question);
		this.add(label3);
		this.add(ans);
		this.add(label4);
		this.add(pw);
		this.add(label5);
		this.add(re_pw);
		this.add(completion);
	}

	//"完了"が押されたとき
	public class toMainmenu implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				//入力情報を取得
				String i = id.getText();
				String q = question.getText();
				String a = ans.getText();
				String p = pw.getText();
				String rp = re_pw.getText();

				if (StringUtils.isEmpty(i)) {
					errors.setText("IDを入力してください");
					return;
				}
				if (StringUtils.isEmpty(q)) {
					errors.setText("秘密の質問を入力してください");
					return;
				}
				if (StringUtils.isEmpty(a)) {
					errors.setText("秘密の質問の答えを入力してください");
					return;
				}
				if (StringUtils.isEmpty(p) || StringUtils.isEmpty(rp)) {
					errors.setText("パスワードを入力してください");
					return;
				}
				if (!p.equals(rp)) {
					errors.setText("パスワードが異なっています");
					return;
				}
				String hashedPassword = DigestUtils.sha1Hex(p);
				ArrayList<String> data = new ArrayList<String>();
				data.add(i);
				data.add(q);
				data.add(a);
				data.add(hashedPassword);
				OthelloClient.send("makeAccount", data);
				InputStream is = OthelloClient.socket1.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				String message = (String) ois.readObject();
				if(message.equals("failed")) {
					errors.setText("このIDは既に使われています");
					return;
				}
				if(message.equals("success")) {
					Disp.mainmenu.reloadMyPlayer(id.getText());
					Disp.mainmenu.reloadMainmenu();
					OthelloClient.send("setStatus", 1);
					Disp.ChangeDisp(Disp.mainmenu);
				}

			} catch (IOException | ClassNotFoundException err) {
				err.printStackTrace();
			}
		}
	}
	public class toStartdisp implements ActionListener{
		public void actionPerformed(ActionEvent e) {

			Disp.ChangeDisp(Disp.start);
		}
	}
}
