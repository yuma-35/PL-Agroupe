//21:届いた申請

package client.displays;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.JPanel;

import client.OthelloClient;
import model.Player;

public class FriendRequest extends JPanel implements MouseListener{


	JLabel f1;
	JLabel label;

//
	//ImageIcon enemyIcon = new ImageIcon();
	//JLabel eIcon = new JLabel();
//

	String playerId;	//自分のID
	String otherId;	//相手のID
	ArrayList<String> requestData = new ArrayList<String>();

	JButton yes;
	JButton no;

	int count = 0;


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

		label = new JLabel();
		label.setFont(new Font("MS ゴシック", Font.BOLD, 14));
		label.setForeground(Color.YELLOW);
		label.setBounds(380, 180, 300, 25);


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



		this.add(label1);
		this.add(label2);
		this.add(label);
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
		int location = f1.getLocation().y;
		int m = (location - 220) / 25;
		otherId = requestData.get(m);

		try {
			ArrayList<String> data = new ArrayList<String>();
			data.add(playerId);
			data.add(otherId);
			OthelloClient.send("getProfile", data);
			InputStream is = OthelloClient.socket1.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			String message = (String)ois.readObject();
			if(message.equals("failed")) {
				return;
			}
			if(message.equals("success")) {
				Player player = (Player)ois.readObject();
				GetProfile sub = new GetProfile(Disp.disp, ModalityType.MODELESS);
				sub.reloadProfile(player.id, player.playerRank, player.win, player.lose, player.draw, player.conceed, player.comment, player.iconImage, player.frflag);
				sub.getId(playerId, player.id);
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
			label.setText("");
			if(count == 1) {
				remove(f1);
				remove(yes);
				remove(no);
				count--;
			}
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
		yes = new JButton("承認");
		yes.setFont(new Font("MS ゴシック", Font.BOLD, 15));
		yes.setBounds(420, y, 70, 20);
		yes.setForeground(Color.WHITE);
		yes.setBackground(new Color(51, 102, 255));
		//a承認が押されたときのアクション
		class toFriendRegister_yes implements ActionListener{
			public void actionPerformed(ActionEvent e) {
yes.setEnabled(false);
no.setEnabled(false);
				try {

					int location = yes.getLocation().y;
					int m = (location - 220) / 25;

					otherId = requestData.get(m);
					ArrayList<String> data = new ArrayList<String>();
					data.add(playerId);
					data.add(otherId);
					OthelloClient.send("friend_add", data);
					InputStream is = OthelloClient.socket1.getInputStream();
					ObjectInputStream ois = new ObjectInputStream(is);
					String message = (String) ois.readObject();
					if(message.equals("success")) {
						label.setText(otherId+" をフレンドに追加しました");


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
		yes.addActionListener(new toFriendRegister_yes());
		class toFriendRegister_no implements ActionListener{

			public void actionPerformed(ActionEvent e) {
				yes.setEnabled(false);
				no.setEnabled(false);
				try {
					int location = yes.getLocation().y;
					int m = (location - 220) / 25;
					otherId = requestData.get(m);
					ArrayList<String> data = new ArrayList<String>();
					data.add(playerId);
					data.add(otherId);
					OthelloClient.send("friend_refuse", data);
					InputStream is = OthelloClient.socket1.getInputStream();
					ObjectInputStream ois = new ObjectInputStream(is);
					String message = (String) ois.readObject();
					if(message.equals("success")) {
						label.setText(otherId+" からの申請を拒否しました");


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
		no = new JButton("拒否");
		no.setFont(new Font("MS ゴシック", Font.BOLD, 15));
		no.setBounds(510, y, 70, 20);
		no.setForeground(Color.WHITE);
		no.setBackground(new Color(51, 102, 255));
		no.addActionListener(new toFriendRegister_no());

		this.add(yes);
		this.add(no);

	}



	//拒否が押されたときのアクション


	//承認が押されたときのアクション
	public class toFriendRegister_yes implements ActionListener{
		public void actionPerformed(ActionEvent e) {

			try {

				int location = yes.getLocation().y;
				int m = (location - 220) / 25;

				otherId = requestData.get(m);
				ArrayList<String> data = new ArrayList<String>();
				data.add(playerId);
				data.add(otherId);
				OthelloClient.send("friend_add", data);
				InputStream is = OthelloClient.socket1.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				String message = (String) ois.readObject();
				if(message.equals("success")) {
					label.setText(otherId+" をフレンドに追加しました");


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
	//拒否が押されたときのアクション
	public class toFriendRegister_no implements ActionListener{
		public void actionPerformed(ActionEvent e) {

			try {
				int location = yes.getLocation().y;
				int m = (location - 220) / 25;
				otherId = requestData.get(m);
				ArrayList<String> data = new ArrayList<String>();
				data.add(playerId);
				data.add(otherId);
				OthelloClient.send("friend_refuse", data);
				InputStream is = OthelloClient.socket1.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				String message = (String) ois.readObject();
				if(message.equals("success")) {
					label.setText(otherId+" からの申請を拒否しました");


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




	public class toProfile implements ActionListener{
		public void actionPerformed(ActionEvent e) {

			GetProfile sub = new GetProfile(Disp.disp, ModalityType.MODELESS);
			sub.setLocation(400, 260);
			sub.setVisible(true);

		}
	}

	//フレンド申請情報を更新
	void reloadFriendRequest(String playerId, ArrayList<String> requestData) {
		this.playerId = playerId;
		this.requestData = requestData;

		if(this.requestData.isEmpty()) {
			label.setText("表示するフレンド申請はありません");
		}else {

			int n = requestData.size();


			for(int i=0;i<n;i++) {
				f1 = new JLabel(requestData.get(i));
				f1.setFont(new Font("MS ゴシック", Font.BOLD, 15));
				f1.setForeground(Color.WHITE);
				f1.setBounds(300, 220 + (i * 25), 200, 20);
				f1.addMouseListener(this);
				button_approval(220 + (i * 25));
				this.add(f1);


				//アイコン
				//アイコン要求
				try {
					ImageIcon enemyIcon = new ImageIcon();
					JLabel eIcon = new JLabel();

					OthelloClient.send("geticon", requestData.get(i));
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

		          enemyIcon = new ImageIcon(img);
					Image smallImg = enemyIcon.getImage().getScaledInstance((int) (enemyIcon.getIconWidth() * 0.4), -1,
							Image.SCALE_SMOOTH);
					ImageIcon smallIcon = new ImageIcon(smallImg);
					eIcon.setIcon(smallIcon);

					eIcon.setBounds(260, 222 + (i * 25), 20, 20);
					this.add(eIcon);

					//ファイル削除
						f.delete();

				} catch (IOException /*| ClassNotFoundException*/ e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
//				//旧アイコン要求
				/*try {
					ImageIcon enemyIcon = new ImageIcon();
					JLabel eIcon = new JLabel();

					OthelloClient.send("geticon", requestData.get(i));
					//受け取り
					SendIcon iconData ;
					InputStream is2 = OthelloClient.socket1.getInputStream();
					ObjectInputStream ois2 = new ObjectInputStream(is2);
					iconData = (SendIcon) ois2.readObject();

					File f = iconData.getImage();
					BufferedImage img = ImageIO.read(f);
					enemyIcon = new ImageIcon(img);
					Image smallImg = enemyIcon.getImage().getScaledInstance((int) (enemyIcon.getIconWidth() * 0.4), -1,
				            Image.SCALE_SMOOTH);
				    ImageIcon smallIcon = new ImageIcon(smallImg);
					eIcon.setIcon(smallIcon);

					eIcon.setBounds(260, 222 + (i * 25), 20, 20);
					this.add(eIcon);
				} catch (IOException | ClassNotFoundException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}*/
	//

			}
			count++;
		}

	}



}

//プロフィールを表示するためのサブモーダル

class GetProfile extends JDialog{

	//メインフレーム
	Disp disp;

	JLabel label1;
	JLabel label2;
	JLabel label3;
	JLabel label4;
	JLabel label5;

	JButton ok;

	String playerId;
	String otherId;

//
	ImageIcon eIcon = new ImageIcon();
//

	public GetProfile(Disp disp, ModalityType mt) {
		super(disp,mt);

		this.disp = disp;

		this.setSize(600,300);
		this.setLayout(null);
		this.setResizable(false);
		this.getContentPane().setBackground(Color.LIGHT_GRAY);

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
		label4.setBounds(150, 130, 300, 25);
		label4.setHorizontalAlignment(JLabel.CENTER);

		label5 = new JLabel();
		label5.setFont(new Font("MS ゴシック", Font.BOLD, 16));
		label5.setForeground(Color.WHITE);
		label5.setBounds(150, 180, 400, 25);
		label5.setHorizontalAlignment(JLabel.CENTER);

		ok = new JButton("フレンド申請");
		ok.setFont(new Font("MS ゴシック", Font.BOLD, 12));
		ok.setBounds(220, 220, 150, 30);
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

		this.add(ok);
		this.add(label1);
		this.add(label2);
		this.add(label3);
		this.add(label4);
		this.add(label5);
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

	public void reloadProfile(String id, int rank, int win, int lose, int draw, int conceed, String comment, String icon, int flag) {
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
		//旧アイコン要求
		/*try {
			OthelloClient.send("geticon", id);
			//受け取り
			SendIcon iconData;
			InputStream is2 = OthelloClient.socket1.getInputStream();
			ObjectInputStream ois2 = new ObjectInputStream(is2);
			iconData = (SendIcon) ois2.readObject();

			File f = iconData.getImage();
			BufferedImage img = ImageIO.read(f);
			eIcon = new ImageIcon(img);
			Image smallImg = eIcon.getImage().getScaledInstance((int) (eIcon.getIconWidth() * 0.7), -1,
					Image.SCALE_SMOOTH);
			ImageIcon smallIcon = new ImageIcon(smallImg);
			label2.setIcon(smallIcon);
			//label2.setIcon(eIcon);


		} catch (IOException | ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}*/
//
		//label2.setText(icon);
		label3.setText("ランク "+ rank);
		label4.setText("対戦成績: "+win+"勝 "+lose+"敗 "+draw+"引き分け "+conceed+"投了");
		label5.setText(comment);
		ok.setEnabled(false);
	}

	public void getId(String playerId, String otherId) {
		this.playerId = playerId;
		this.otherId = otherId;
	}


}



