//20:フレンド登録

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
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.OthelloClient;
import model.FriendList;
import model.Player;

public class FriendRegister extends JPanel {
	Music msbox;
	FriendList friendlist = new FriendList();

	String id;

	//検索IDを入力するためのテキストフィールド
	JTextField friend_id = new JTextField(32);

	JLabel errors;

	FriendRegister() {
		this.setLayout(null);
		setSize(1000, 600);

		JLabel label1 = new JLabel("フレンド");
		label1.setFont(new Font("MS ゴシック", Font.BOLD, 20));
		label1.setForeground(Color.WHITE);
		label1.setBounds(190, 105, 150, 20);
		JLabel label2 = new JLabel("ID検索");
		label2.setFont(new Font("MS ゴシック", Font.BOLD, 22));
		label2.setForeground(Color.WHITE);
		label2.setBounds(310, 190, 150, 25);

		friend_id.setBounds(420, 190, 150, 25);

		errors = new JLabel();
		errors.setFont(new Font("MS ゴシック", Font.BOLD, 14));
		errors.setForeground(Color.red);
		errors.setBounds(380, 135, 300, 50);

		JButton search = new JButton("検索");
		search.setFont(new Font("MS ゴシック", Font.BOLD, 20));
		search.setBounds(420, 240, 150, 40);
		search.setForeground(Color.WHITE);
		search.setBackground(new Color(51, 102, 255));
		search.addActionListener(new toFriendData());
		JButton request = new JButton("届いた申請を見る");
		request.setFont(new Font("MS ゴシック", Font.BOLD, 15));
		request.setBounds(350, 390, 300, 40);
		request.setForeground(Color.WHITE);
		request.setBackground(new Color(51, 102, 255));
		request.addActionListener(new toFriendRequest());
		JButton back = new JButton("戻る");
		back.setFont(new Font("MS ゴシック", Font.BOLD, 20));
		back.setBounds(420, 500, 150, 50);
		back.setForeground(Color.WHITE);
		back.setBackground(new Color(51, 102, 255));
		back.addActionListener(new toMainmenu());
		JButton volume = new JButton("音量調整");
		volume.setFont(new Font("MS ゴシック", Font.BOLD, 10));
		volume.setBounds(870, 5, 90, 25);
		volume.setForeground(Color.WHITE);
		volume.setBackground(new Color(51, 102, 255));
		volume.addActionListener(new toStartS());

		this.add(label1);
		this.add(label2);
		this.add(friend_id);
		this.add(errors);
		this.add(search);
		this.add(request);
		this.add(back);
		this.add(volume);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(160, 90, 680, 370);
	}

	//22:フレンド ID検索結果へ遷移
	public class toFriendData implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String f_id = friend_id.getText();//入力されたIDを取得

			try {
				ArrayList<String> data = new ArrayList<String>();
				data.add(id);
				data.add(f_id);
				OthelloClient.send("getProfile", data);
				InputStream is = OthelloClient.socket1.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				String message = (String)ois.readObject();
				if(message.equals("failed")) {
					errors.setText("該当するアカウントが見つかりません");
					return;
				}
				if(message.equals("success")) {
					errors.setText("");
					Player player = (Player)ois.readObject();
					displayProfile sub = new displayProfile(Disp.disp, ModalityType.MODELESS);
					sub.reloadProfile(player.id, player.playerRank, player.win, player.lose, player.draw, player.conceed, player.comment, player.iconImage, player.frflag);
					sub.getId(id, player.id);
					sub.setLocation(400, 260);
					sub.setVisible(true);
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

	//21:届いた申請へ遷移
	public class toFriendRequest implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			ArrayList<String> requestData = new ArrayList<String>();

			try {
				OthelloClient.send("getFriendrequest", id);
				InputStream is = OthelloClient.socket1.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				String message = (String) ois.readObject();
				if(message.equals("success")) {
					requestData = (ArrayList<String>)ois.readObject();
					Disp.friendrequest.reloadFriendRequest(id,requestData);
					Disp.ChangeDisp(Disp.friendrequest);

				}
				if(message.equals("failed")){
					Disp.friendrequest.reloadFriendRequest(id,requestData);
					Disp.ChangeDisp(Disp.friendrequest);
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

	//7:メインメニューへ遷移
	public class toMainmenu implements ActionListener {
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

	//9:音量調整へ遷移
	public class toVolume implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Disp.ChangeDisp(Disp.friendregister);
		}
	}

	public class toStartS implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			msbox = new Music(Disp.disp, ModalityType.MODELESS);
			msbox.setLocation(440, 220);
			//	this.addLogMessage("サブダイアログのモーダル表示処理を開始");
			msbox.setVisible(true);
			//	this.addLogMessage("サブダイアログのモーダル表示処理が完了==結果==> " + subWindow.getResult());

			//Disp.ChangeDisp(Disp.music);
		}
	}

	public void reloadFriendRegister(String id) {
		this.id = id;

	}

}
