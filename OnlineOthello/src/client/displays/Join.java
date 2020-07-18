package client.displays;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;


import javax.swing.JTextField;

import client.OthelloClient;
import model.Client;
import model.Match;

public class Join extends JDialog {
	//メインフレーム
		Disp disp;

	//ボタン
		JButton join = new JButton("参加");
		JButton back = new JButton("戻る");
        JLabel wrongJLabel=new JLabel("パスワードが違います");
		Color color = new Color(51,102,255);
       Match amatch;
	//テキスト
		JLabel name = new JLabel("卓へ参加");
		JLabel name2 = new JLabel("パスワード付きの卓です");
		JLabel name3 = new JLabel("パスワード");

	//テキスト入力部
		JTextField password = new JTextField();

	//返すパスワード
		String pass;

		//モーダル表示
		Join(Disp disp, ModalityType mt,Match match){

			super(disp,mt);
			this.disp = disp;
            amatch=match;
			//画面サイズ
			//this.setTitle("卓へ参加");
			this.getContentPane().setBackground(Color.LIGHT_GRAY);
			this.setSize(520, 250);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setLayout(null);
	        this.setResizable(false);
            wrongJLabel.setFont(new Font("MS ゴシック",Font.BOLD,10));
			wrongJLabel.setForeground(Color.red);
			wrongJLabel.setBounds(200,50,300,50);
			this.add(wrongJLabel);
			wrongJLabel.setVisible(false);
            //参加ボタン
			 join.setFont(new Font("MS ゴシック", Font.BOLD, 10));
			 join.setForeground(Color.WHITE);
			 join.setBackground(color);
			 join.setBounds(220, 160, 80, 25);
			 join.addActionListener(new toStart());
			 this.add(join);

			//テキスト
			 name.setFont(new Font("MS ゴシック", Font.BOLD, 20));
			 name.setForeground(Color.WHITE);
			 name.setBounds(210,5,300,50);
			 this.add(name);
			 name2.setFont(new Font("MS ゴシック", Font.BOLD, 15));
			 name2.setForeground(Color.WHITE);
			 name2.setBounds(160,25,300,50);
			 this.add(name2);
			 name3.setFont(new Font("MS ゴシック", Font.BOLD, 15));
			 name3.setForeground(Color.WHITE);
			 name3.setBounds(80,60,100,100);
			 this.add(name3);

			//テキスト入力部
			 password.setBounds(170, 100, 230, 25);
			 this.add(password);

		}

		//参加ボタン
		public class toStart implements ActionListener{
		    public  void actionPerformed(ActionEvent e) {
		    if(password.getText().equals(amatch.password)) {
		    	try {
		    		ArrayList<String> sendPack =new ArrayList<String>();
		    		sendPack.add(amatch.playerId);
		    		sendPack.add(Client.myPlayer.id);
					OthelloClient.send("BattleEnter", sendPack);
					InputStream is = OthelloClient.socket1.getInputStream();
					ObjectInputStream ois = new ObjectInputStream(is);
					int message = (int) ois.readObject();
					if(message==0) {
						JOptionPane.showMessageDialog(Disp.disp, "既に削除された卓です");
						disp.mainmenu.reloadMainmenu();
						Disp.mainmenu.repaint();
					return;
					}
					
					OthelloClient.send("setStatus",3);
					disp.othello.startOthello(amatch.rule, 1,amatch.playerId);
			    	disp.ChangeDisp(disp.othello);
			    	dispose();
				} catch (IOException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				}
		    	
		    	
		    }else {
		    wrongJLabel.setVisible(true);

		    }


		     }
		 }


}
