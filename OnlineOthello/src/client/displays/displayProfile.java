package client.displays;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import client.OthelloClient;

class displayProfile extends JDialog {

	//メインフレーム
	Disp disp;

	JLabel label1;
	JLabel label2;
	JLabel label3;
	JLabel label4;
	JLabel label5;

	JButton ok;
	JLabel label;

	String playerId;
	String otherId;

	//
	ImageIcon eIcon = new ImageIcon();

	//

	public displayProfile(Disp disp, ModalityType mt) {
		super(disp, mt);

		this.disp = disp;

		this.setSize(600, 300);
		this.setLayout(null);
		this.getContentPane().setBackground(Color.LIGHT_GRAY);
		this.setResizable(false);

		label1 = new JLabel();
		label1.setFont(new Font("MS ゴシック", Font.BOLD, 22));
		label1.setForeground(Color.WHITE);
		label1.setBounds(170, 50, 250, 30);
		label1.setHorizontalAlignment(JLabel.CENTER);

		label2 = new JLabel();
		label2.setFont(new Font("MS ゴシック", Font.BOLD, 12));
		label2.setForeground(Color.WHITE);

		label2.setBounds(280, 10, 35, 35);
		label2.setHorizontalAlignment(JLabel.CENTER);

		label3 = new JLabel();
		label3.setFont(new Font("MS ゴシック", Font.BOLD, 12));
		label3.setForeground(Color.WHITE);
		label3.setBounds(170, 80, 250, 25);
		label3.setHorizontalAlignment(JLabel.CENTER);
		label4 = new JLabel();
		label4.setFont(new Font("MS ゴシック", Font.BOLD, 16));
		label4.setForeground(Color.WHITE);
		label4.setBounds(150, 120, 300, 25);
		label4.setHorizontalAlignment(JLabel.CENTER);

		label5 = new JLabel();
		label5.setFont(new Font("MS ゴシック", Font.BOLD, 16));
		label5.setForeground(Color.WHITE);
		label5.setBounds(100, 170, 400, 25);
		label5.setHorizontalAlignment(JLabel.CENTER);

		ok = new JButton("フレンド申請");
		ok.setFont(new Font("MS ゴシック", Font.BOLD, 12));
		ok.setBounds(220, 220, 150, 30);
		ok.setForeground(Color.WHITE);
		ok.setBackground(new Color(51, 102, 255));
		ok.addActionListener(new friendregister());

		label = new JLabel();
		label.setFont(new Font("MS ゴシック", Font.BOLD, 12));
		label.setForeground(Color.YELLOW);
		label.setBounds(150, 190, 300, 25);
		label.setHorizontalAlignment(JLabel.CENTER);

		this.add(ok);
		this.add(label1);
		this.add(label2);
		this.add(label3);
		this.add(label4);
		this.add(label5);
		this.add(label);

		JButton back = new JButton("戻る");
		back.setFont(new Font("MS ゴシック", Font.BOLD, 10));
		back.setBounds(500, 10, 70, 20);
		back.setForeground(Color.WHITE);
		back.setBackground(new Color(51, 102, 255));
		back.addActionListener(new toBack());

		this.add(back);
	}

	//「フレンド申請」ボタンを押されたときの処理
	public class friendregister implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				ArrayList<String> data = new ArrayList<String>();

				data.add(playerId);
				data.add(otherId);
				OthelloClient.send("friendrequest", data);
				InputStream is = OthelloClient.socket1.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				String message = (String) ois.readObject();
				if (message.equals("success")) {
					label.setText("フレンド申請しました");
					ok.setEnabled(false);
				} else if (message.equals("failed")) {
					label.setText("申請が来ていたためフレンドに追加されました");
					ok.setEnabled(false);
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

	//「戻る」ボタンを押されたときの処理
	public class toBack implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			setVisible(false); //サブモータルを消す
		}
	}

	public void reloadProfile(String id, int rank, int win, int lose, int draw, int conceed, String comment,
			String icon, int flag) {
		label1.setText(id);
		//
		//アイコン要求
		try {
			OthelloClient.send("geticon", id);
			//受け取り
			InputStream is2 = OthelloClient.socket1.getInputStream();

			File f  = new File("f.out");
			FileOutputStream fileOutStream = new FileOutputStream( f);
			int waitCount = 0;
			int recvFileSize;       //InputStreamから受け取ったファイルのサイズ
		    byte[] fileBuff = new byte[512];      //サーバからのファイル出力を受け取る


			 while( true )
          {
            //ストリームから読み込める時
            if( is2.available() > 0 )
            {
              //受け取ったbyteをファイルに書き込み
              recvFileSize = is2.read(fileBuff);
              fileOutStream.write( fileBuff , 0 , recvFileSize );
            }

            //タイムアウト処理
            else
            {
              waitCount++;
              Thread.sleep(100);
              if (waitCount > 10)break;
            }
          }

          //ファイルの書き込みを閉じる
          fileOutStream.close();

          //ここから読み込んで、表示
          BufferedImage img = ImageIO.read(f);

			eIcon = new ImageIcon(img);
			Image smallImg = eIcon.getImage().getScaledInstance((int) (eIcon.getIconWidth() * 0.7), -1,
					Image.SCALE_SMOOTH);
			ImageIcon smallIcon = new ImageIcon(smallImg);
			label2.setIcon(smallIcon);

			//ファイル削除
				f.delete();

		} catch (IOException /*| ClassNotFoundException*/ e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		//
		//label2.setText(icon);
		label3.setText("ランク " + rank);
		label4.setText("対戦成績: " + win + "勝 " + lose + "敗 " + draw + "引き分け " + conceed + "投了");
		label5.setText(comment);

		if (flag == 1) {
			ok.setEnabled(false);
		}
	}

	public void getId(String playerId, String otherId) {
		this.playerId = playerId;
		this.otherId = otherId;
	}

}
