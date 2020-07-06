//21:届いた申請

package client.displays;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FriendRequest extends JPanel implements MouseListener{


	JLabel f1 = new JLabel("たろう");

	FriendRequest(){

		this.setLayout(null);
		setSize(1000,600);

		JLabel label1 = new JLabel("フレンド");
		label1.setFont(new Font("MS ゴシック", Font.BOLD, 20));
		label1.setForeground(Color.WHITE);
		label1.setBounds(190, 105, 150, 20);
		JLabel label2 = new JLabel("届いた申請");
		label2.setFont(new Font("MS ゴシック", Font.BOLD, 20));
		label2.setForeground(Color.WHITE);
		label2.setBounds(450, 140, 150, 50);

		JButton back = new JButton("戻る");
		back.setFont(new Font("MS ゴシック", Font.BOLD, 20));
		back.setBounds(420, 500, 150, 50);
		back.setForeground(Color.WHITE);
		back.setBackground(new Color(51, 102, 255));
		back.addActionListener(new toFriendRegister());
		JButton volume = new JButton("音量調整");
		volume.setFont(new Font("MS ゴシック", Font.BOLD, 10));
		volume.setBounds(870, 5, 90, 25);
		volume.setForeground(Color.WHITE);
		volume.setBackground(new Color(51, 102, 255));
		volume.addActionListener(new toVolume());

		//以下、フレンド申請があれば表示(例として1つ作っとく)

		f1.setFont(new Font("MS ゴシック", Font.BOLD, 15));
		f1.setForeground(Color.WHITE);
		f1.setBounds(250, 220, 200, 20);
		f1.addMouseListener(this);
		button_approval(220);

		this.add(label1);
		this.add(label2);
		this.add(f1);
		this.add(back);
		this.add(volume);

	}

	//マウスがボタンの枠内に入った時の処理
	public void mouseEntered(MouseEvent evt) {
		f1.setForeground(Color.YELLOW);
	}
	public void mouseExited(MouseEvent e) {
		f1.setForeground(Color.WHITE);

	}
	public void mouseClicked(MouseEvent e) {

	}
	public void mousePressed(MouseEvent e) {
		f1.setForeground(Color.RED);
		GetProfile sub = new GetProfile(Disp.disp, ModalityType.MODELESS);
		sub.setLocation(400, 260);
		sub.setVisible(true);

	}
	public void mouseReleased(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	public void paintComponent(Graphics g) {
	   super.paintComponent(g);
	   Graphics2D g2d = (Graphics2D) g;
	   g2d.setColor(Color.LIGHT_GRAY);
	   g2d.fillRect(160,90, 680, 370);
	}

	//20:フレンド登録へ遷移
	public class toFriendRegister implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			Disp.ChangeDisp(Disp.friendregister);
		}
	}

	//9:音量調整へ遷移
	public class toVolume implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			Disp.ChangeDisp(Disp.friendregister);
		}
	}

	//承認、拒否ボタンを作るメソッド
	public void button_approval(int y) {
		JButton yes = new JButton("承認");
		yes.setFont(new Font("MS ゴシック", Font.BOLD, 15));
		yes.setBounds(420, y, 70, 20);
		yes.setForeground(Color.WHITE);
		yes.setBackground(new Color(51, 102, 255));
		yes.addActionListener(new toFriendRegister_yes());
		JButton no = new JButton("拒否");
		no.setFont(new Font("MS ゴシック", Font.BOLD, 15));
		no.setBounds(510, y, 70, 20);
		no.setForeground(Color.WHITE);
		no.setBackground(new Color(51, 102, 255));
		no.addActionListener(new toFriendRegister_no());

		this.add(yes);
		this.add(no);

	}

	//承認が押されたときのアクション
	public class toFriendRegister_yes implements ActionListener{
		public void actionPerformed(ActionEvent e) {

			Disp.ChangeDisp(Disp.friendregister);
		}
	}
	//拒否が押されたときのアクション
	public class toFriendRegister_no implements ActionListener{
		public void actionPerformed(ActionEvent e) {

			Disp.ChangeDisp(Disp.friendregister);
		}
	}



	public class toProfile implements ActionListener{
		public void actionPerformed(ActionEvent e) {

			GetProfile sub = new GetProfile(Disp.disp, ModalityType.MODELESS);
			sub.setLocation(400, 260);
			sub.setVisible(true);

		}
	}

	//フレンド申請情報を更新
	void reloadFriendRequest() {

	}

}

//プロフィールを表示するためのサブモーダル

class GetProfile extends JDialog{

	//メインフレーム
	Disp disp;


	public GetProfile(Disp disp, ModalityType mt) {
		super(disp,mt);

		this.disp = disp;

		this.setSize(600,300);
		this.setLayout(null);
		this.getContentPane().setBackground(Color.LIGHT_GRAY);

		//タイトルバーを消す
		dispose();
	    setUndecorated(!isUndecorated());
	    setVisible(true);

		JButton ok = new JButton("フレンド申請");
		ok.setFont(new Font("MS ゴシック", Font.BOLD, 12));
		ok.setBounds(220, 250, 150, 30);
		ok.setForeground(Color.WHITE);
		ok.setBackground(new Color(51, 102, 255));
		ok.addActionListener(new friendrequest());

		this.add(ok);

		JButton back = new JButton("戻る");
		back.setFont(new Font("MS ゴシック", Font.BOLD, 10));
		back.setBounds(500, 10, 70, 20);
		back.setForeground(Color.WHITE);
		back.setBackground(new Color(51, 102, 255));
		back.addActionListener(new toBack());

		this.add(back);
	}


	//「フレンド申請」ボタンを押されたときの処理
	public class friendrequest implements ActionListener{
		public void actionPerformed(ActionEvent e) {

			setVisible(false);	//サブモータルを消す
		}
	}

	//「戻る」ボタンを押されたときの処理
	public class toBack implements ActionListener{
		public void actionPerformed(ActionEvent e) {

			setVisible(false);	//サブモータルを消す
		}
	}


}



