//5:ID入力

package client.displays;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;

import client.OthelloClient;

public class Forget extends JPanel {

	String i;	//IDを入れる
	String question;	//秘密の質問を入れる

	//IDを入力するためのテキストフィールド
	JTextField id = new JTextField(32);

	JLabel errors;

	Forget() {
		this.setLayout(null);
		setSize(1000, 600);

		ImageIcon icon = new ImageIcon("logo.png");
		JLabel othello = new JLabel(icon);
		othello.setBounds(405, 25, 170, 80);
		JLabel name = new JLabel("パスワードを忘れた方");
		name.setFont(new Font("MS ゴシック", Font.BOLD, 20));
		name.setForeground(Color.WHITE);
		name.setBounds(380, 100, 250, 50);

		errors = new JLabel();
		errors.setFont(new Font("MS ゴシック", Font.BOLD, 14));
		errors.setForeground(Color.red);
		errors.setBounds(380, 135, 250, 50);

		JLabel label = new JLabel("ID");
		label.setFont(new Font("MS ゴシック", Font.BOLD, 22));
		label.setForeground(Color.WHITE);
		label.setBounds(380, 270, 250, 25);

		id.setBounds(415, 270, 150, 25);

		JButton to_question = new JButton("次へ");
		to_question.setFont(new Font("MS ゴシック", Font.BOLD, 20));
		to_question.setBounds(415, 450, 150, 50);
		to_question.setForeground(Color.WHITE);
		to_question.setBackground(new Color(51, 102, 255));
		to_question.addActionListener(new toQuestion());

		JButton back = new JButton("戻る");
		back.setFont(new Font("MS ゴシック", Font.BOLD, 15));
		back.setBounds(850, 30, 80, 30);
		back.setForeground(Color.WHITE);
		back.setBackground(new Color(51, 102, 255));
		back.addActionListener(new toStartdisp());

		this.add(othello);
		this.add(name);
		this.add(errors);
		this.add(label);
		this.add(id);
		this.add(to_question);
		this.add(back);

	}

	//次へを押されたとき
	public class toQuestion implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			i = id.getText();//入力されたIDを取得
			//引数をi(ID)とするメソッド→IDをサーバに送って、秘密の質問を受け取る

			if (StringUtils.isEmpty(i)) {
				errors.setText("IDを入力してください");
				return;
			}

			try {
				OthelloClient.send("forget", i);
				InputStream is = OthelloClient.socket1.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				String message = (String)ois.readObject();
				if(message.equals("failed")) {
					errors.setText("このIDのアカウントは存在しません");
					return;
				}
				if(message.equals("success")) {
					question =  (String) ois.readObject();
					Disp.question.reloadQuestion(i, question);
					Disp.ChangeDisp(Disp.question);
				}

			} catch (IOException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}

		}
	}

	//startdispへ画面遷移
	public class toStartdisp implements ActionListener{
		public void actionPerformed(ActionEvent e) {

			Disp.ChangeDisp(Disp.start);
		}
	}


}
