package client.displays;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import client.OthelloClient;
import client.displays.Othello.sendchat;
import model.Client;
import model.Match;
import model.Player;

public class Mainmenu extends JPanel {
	Music msbox;
	FriendBattleRequest frbox;
	RemoveCheck rbox;
	Join joinbox;
	JLabel logoLabel = new JLabel(new ImageIcon("image/SystemImage/ろご.png"));
	JButton toMakematch = new JButton("卓作成");
	JPanel friendlistPanel = new JPanel();
	JPanel lobiPanel = new JPanel();
	JScrollPane lobiScroll = new JScrollPane(lobiPanel);
	JScrollPane friendScroll = new JScrollPane(friendlistPanel);
	JLabel lobiLabel = new JLabel("ロビー");
	JLabel FriendTitleLabel = new JLabel("フレンドリスト");
	JButton toFriendRegister = new JButton("フレンド登録");
	JButton toGameRecord = new JButton("対局記録");
	JLabel recordWinLabel = new JLabel();
	JLabel recordDrawLabel = new JLabel();
	JLabel recordLoseLabel = new JLabel();
	JLabel recordConceedLabel = new JLabel();
	JLabel recordLabel = new JLabel("対局記録");
	JButton toAccount = new JButton("アカウント");
	JLabel rankLabel = new JLabel();
	JLabel rankpointLabel = new JLabel();
	JLabel idLabel = new JLabel("ID:yuma123558");
	JButton toSounds = new JButton("音量調整");
	Font a = new Font("MS ゴシック", Font.BOLD, 20);
	Font b = new Font("MS ゴシック", Font.BOLD, 10);
	JButton reloadButton=new JButton("更新");
//
	ImageIcon myIcon = new ImageIcon();
	JLabel mIcon = new JLabel();
//

	Mainmenu() {
		setSize(1000, 600);
		this.setLayout(null);

		lobiPanel.setBackground(Color.white);
		lobiScroll.setBounds(260, 190, 480, 230);
		lobiPanel.setLayout(null);

		this.add(lobiScroll);

		friendlistPanel.setBackground(Color.white);
		friendScroll.setBounds(40, 80, 180, 450);
		friendlistPanel.setLayout(null);

		this.add(friendScroll);

		reloadButton.setBounds(470, 155, 60, 30);
		reloadButton.setBackground(new Color(51, 102, 255));
		reloadButton.setForeground(Color.WHITE);
		reloadButton.addActionListener(new reload());
		this.add(reloadButton);
		
		toMakematch.setBounds(460, 425, 80, 30);
		toMakematch.setBackground(new Color(51, 102, 255));
		toMakematch.setForeground(Color.WHITE);
		toMakematch.addActionListener(new toMakematchB());
		this.add(toMakematch);

		toSounds.setForeground(Color.WHITE);
		toSounds.setBackground(new Color(51, 102, 255));
		toSounds.setBounds(870, 5, 90, 25);
		toSounds.addActionListener(new toStartS());
		toSounds.setFont(b);
		this.add(toSounds);

		idLabel.setFont(a);
		idLabel.setBounds(800, 40, 200, 50);

		rankLabel.setFont(a);
		rankLabel.setBounds(870, 90, 150, 50);

		logoLabel.setBounds(300, 0, 400, 150);
		this.add(logoLabel);
		rankpointLabel.setFont(a);
		rankpointLabel.setBounds(830, 175, 150, 50);

		toAccount.setBounds(800, 225, 145, 35);
		toAccount.addActionListener(new toAccountB());
		toAccount.setBackground(new Color(51, 102, 255));
		toAccount.setForeground(Color.white);
		toAccount.setFont(new Font("MS ゴシック", Font.BOLD, 14));

		recordLabel.setFont(a);
		recordLabel.setBounds(780, 260, 150, 50);

		recordWinLabel.setFont(a);
		recordWinLabel.setBounds(780, 290, 150, 50);

		recordLoseLabel.setFont(a);
		recordLoseLabel.setBounds(780, 320, 150, 50);

		recordDrawLabel.setFont(a);
		recordDrawLabel.setBounds(780, 350, 150, 50);

		recordConceedLabel.setFont(a);
		recordConceedLabel.setBounds(780, 380, 150, 50);

		lobiLabel.setBounds(260, 120, 100, 100);
		lobiLabel.setFont(a);
		this.add(lobiLabel);

		FriendTitleLabel.setBounds(35, 40, 150, 30);
		FriendTitleLabel.setFont(a);

		toFriendRegister.setBounds(400, 520, 200, 40);
		toFriendRegister.addActionListener(new toFriendRegisterB());
		toFriendRegister.setBackground(new Color(51, 102, 255));
		toFriendRegister.setForeground(Color.white);
		toFriendRegister.setFont(new Font("MS ゴシック", Font.BOLD, 14));

		toGameRecord.setBounds(400, 470, 200, 40);
		toGameRecord.addActionListener(new toGamerecordsB());
		toGameRecord.setBackground(new Color(51, 102, 255));
		toGameRecord.setForeground(Color.white);
		toGameRecord.setFont(new Font("MS ゴシック", Font.BOLD, 14));

//
		mIcon.setBounds(800, 70, 80, 80);
		this.add(mIcon);

//

		this.add(toFriendRegister);
		this.add(toGameRecord);
		this.add(FriendTitleLabel);
		this.add(idLabel);
		this.add(rankLabel);
		this.add(rankpointLabel);
		this.add(recordLabel);
		this.add(recordWinLabel);
		this.add(recordLoseLabel);
		this.add(recordDrawLabel);
		this.add(recordConceedLabel);
		this.add(toAccount);

	}

//
	//アイコン用
	void icon() {
		myIcon = new ImageIcon(Disp.changeicon.getIcon());
		mIcon.setIcon(myIcon);

	}
//

	void reloadMatch() throws IOException, ClassNotFoundException {
		lobiPanel.removeAll();
		OthelloClient.send("reloadMatch", 0);
		InputStream is = OthelloClient.socket1.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(is);
		ArrayList<Match> matches = (ArrayList<Match>) ois.readObject();
		if (matches.size() != 0) {
			lobiPanel.setPreferredSize(new Dimension(470, 80 * matches.size()));
			for (int i = 0; i < matches.size(); i++) {
				lobiPanel.add(new MatchPanel(i, matches.get(i)));
			}
		}

	}

	public class toStartS implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Music.se();
			msbox = new Music(Disp.disp, ModalityType.MODELESS);
			msbox.setLocation(440, 220);
			// this.addLogMessage("サブダイアログのモーダル表示処理を開始");
			msbox.setVisible(true);
			// this.addLogMessage("サブダイアログのモーダル表示処理が完了==結果==> " + subWindow.getResult());

			// Disp.ChangeDisp(Disp.music);
		}
	}

	void reloadFriendlist() throws IOException, ClassNotFoundException {
		OthelloClient.send("reloadFriendList", Client.myPlayer.id);
		InputStream is = OthelloClient.socket1.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(is);
		ArrayList<Player> friendList = (ArrayList<Player>) ois.readObject();
		friendlistPanel.removeAll();
		friendScroll.removeAll();
		
		friendlistPanel.setPreferredSize(new Dimension(160, 80 * friendList.size()));
		for (int i = 0; i < friendList.size(); i++) {
			friendlistPanel.add(new FriendPanel(friendList.get(i), i));
		}
friendScroll.add(friendlistPanel);
	}

	public void reloadMainmenu() throws ClassNotFoundException, IOException {
		this.idLabel.setText("ID: " + Client.myPlayer.id);
		this.recordWinLabel.setText(Client.myPlayer.win + "勝");
		this.recordLoseLabel.setText(Client.myPlayer.lose + "敗");
		this.recordDrawLabel.setText(Client.myPlayer.draw + "引き分け");
		this.recordConceedLabel.setText(Client.myPlayer.conceed + "投了");
		this.rankLabel.setText("ランク" + Client.myPlayer.playerRank);
		this.rankpointLabel.setText("あと" + (100 - Client.myPlayer.rankPoint) + "pt");
		reloadFriendlist();
		reloadMatch();
		icon();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(250, 150, 500, 310);
		g2d.fillRect(30, 40, 200, 500);
		g2d.fillRect(770, 40, 200, 500);
		g2d.setColor(Color.white);
		g2d.fillRect(788, 150, 165, 30);
		g2d.setColor(Color.blue);
		g2d.fillRect(788, 150, 165 * Client.myPlayer.rankPoint / 100, 30);
	}

	public class toFriendRegisterB implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			Disp.friendregister.reloadFriendRegister(Client.myPlayer.id);

			//Disp.friendregister.reloadFriendRegister("mina");

			Disp.ChangeDisp(Disp.friendregister);
		}
	}

	public class toGamerecordsB implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Disp.gamerecords.reloadGamerecords();
			Disp.ChangeDisp(Disp.gamerecords);
		}
	}

	public class toAccountB implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Disp.ChangeDisp(Disp.account);
		}
	}

	public class toMakematchB implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Disp.ChangeDisp(Disp.maketable);
		}
	}
	public class reload implements ActionListener {
		public void actionPerformed(ActionEvent e) {
		    try {
				reloadMainmenu();
				repaint();
			} catch (ClassNotFoundException | IOException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
		    
		}
	}
	public class MatchPanel extends JPanel implements MouseListener {
		JLabel enemyIDLabel = new JLabel("aaaaaaaaaa");
		JLabel ruleLabel = new JLabel();
		JLabel matchRankLabel = new JLabel("ランク8");
		JButton battleButton = new JButton("参加");
		JLabel limitLabel = new JLabel("ランク8以上");
		JLabel pasIcon = new JLabel(new ImageIcon("image/SystemImage/rock.png"));
		//
		JTextArea commentArea = new JTextArea();
		Match matchbox;

		class Battle implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if (matchbox.t_limit == 1) {
					if (matchbox.playerRank + 1 < Client.myPlayer.playerRank
							|| matchbox.playerRank - 1 > Client.myPlayer.playerRank) {
						JOptionPane.showMessageDialog(Disp.disp, "ランク制限のある卓です");
						return;
					}
				} else if (matchbox.t_limit == 2) {
					if (matchbox.playerRank > Client.myPlayer.playerRank) {
						JOptionPane.showMessageDialog(Disp.disp, "ランク制限のある卓です");
						return;
					}
				} else if (matchbox.t_limit == 3) {
					ArrayList<String> names = new ArrayList<String>();
					names.add(Client.myPlayer.id);
					names.add(matchbox.playerId);
					try {
						OthelloClient.send("getfr", names);
						InputStream is = OthelloClient.socket1.getInputStream();
						ObjectInputStream ois = new ObjectInputStream(is);
						if ((int) ois.readObject() == 0) {
							JOptionPane.showMessageDialog(Disp.disp, "フレンド限定の卓です");
							return;
						}
					} catch (IOException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					}

					// aフレンドかどうか確認 フレンドだったら

				}
				if (matchbox.password != null) {
					joinbox = new Join(Disp.disp, ModalityType.APPLICATION_MODAL, matchbox);
					joinbox.setLocation(440, 220);
					joinbox.setVisible(true);
				} else {

					try {
						ArrayList<String> sendPack = new ArrayList<String>();
						sendPack.add(matchbox.playerId);
						sendPack.add(Client.myPlayer.id);
						OthelloClient.send("BattleEnter", sendPack);
						InputStream is = OthelloClient.socket1.getInputStream();
						ObjectInputStream ois = new ObjectInputStream(is);
						int message = (int) ois.readObject();
						if(message==1) {
							Disp.disp.othello.startOthello(matchbox.rule, 1, matchbox.playerId);
							Disp.ChangeDisp(Disp.othello);
							OthelloClient.send("setStatus",3);
						}else if(message==0) {
							JOptionPane.showMessageDialog(Disp.disp, "既に削除された卓です");
							reloadMainmenu();
							Disp.mainmenu.repaint();
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
		}

		public MatchPanel(int index, Match match) {
			this.setLayout(null);
			this.setBounds(0, 80 * index, 480, 80);
			this.setBorder(new LineBorder(Color.GRAY, 2, true));
			matchbox = match;
			enemyIDLabel.setBounds(0, 0, 200, 30);
			enemyIDLabel.setFont(new Font("MS ゴシック", Font.BOLD, 20));

			enemyIDLabel.addMouseListener(this);


			this.add(enemyIDLabel);
			limitLabel.setBounds(220, 35, 200, 30);
			limitLabel.setFont(new Font("MS ゴシック", Font.BOLD, 20));
			this.add(limitLabel);
			commentArea.setBounds(5, 30, 200, 40);
			commentArea.setEditable(false);
			commentArea.setLineWrap(true);
			commentArea.setText(match.comment);
			enemyIDLabel.setText("ID: " + match.playerId);
			this.add(commentArea);
			if (match.t_limit == 0) {
				limitLabel.setText("");
			} else if (match.t_limit == 1) {
				limitLabel.setText("ランク±1以内");
			} else if (match.t_limit == 2) {
				limitLabel.setText("ランク" + match.playerRank + "以上");
			} else if (match.t_limit == 3) {
				limitLabel.setText("フレンド限定");
			}

			matchRankLabel.setBounds(120, 0, 200, 30);
			matchRankLabel.setFont(new Font("MS ゴシック", Font.BOLD, 20));
			matchRankLabel.setText("ランク" + match.playerRank);
			this.add(matchRankLabel);

			ruleLabel.setBounds(220, 0, 200, 30);
			ruleLabel.setFont(new Font("MS ゴシック", Font.BOLD, 20));
			this.add(ruleLabel);
			if (match.rule == 0) {
				ruleLabel.setText("通常戦");
			} else if (match.rule == 1) {
				ruleLabel.setText("アイテム戦");
			}
			battleButton.setBounds(380, 10, 60, 60);
			battleButton.setBackground(new Color(51, 102, 255));
			battleButton.setForeground(Color.white);
			battleButton.setFont(b);
			battleButton.addActionListener(new Battle());
			this.add(battleButton);
			pasIcon.setBackground(Color.yellow);
			if (match.password != null) {
				pasIcon.setBounds(330, 5, 30, 30);
				this.add(pasIcon);
			}
		}

		// マウスがボタンの枠内に入った時の処理
		public void mouseEntered(MouseEvent evt) {
			enemyIDLabel.setForeground(Color.YELLOW);
		}

		public void mouseExited(MouseEvent e) {
			enemyIDLabel.setForeground(Color.WHITE);

		}

		public void mouseClicked(MouseEvent e) {

		}

		public void mousePressed(MouseEvent e) {
			enemyIDLabel.setForeground(Color.RED);
			String otherId = matchbox.playerId;

			try {
				ArrayList<String> data = new ArrayList<String>();
				data.add(Client.myPlayer.id);
				data.add(otherId);
				OthelloClient.send("getProfile", data);
				InputStream is = OthelloClient.socket1.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				String message = (String) ois.readObject();
				if (message.equals("failed")) {
					return;
				}
				if (message.equals("success")) {
					Player player = (Player) ois.readObject();
					displayProfile sub = new displayProfile(Disp.disp, ModalityType.MODELESS);
					sub.reloadProfile(player.id, player.playerRank, player.win, player.lose, player.draw,
							player.conceed, player.comment, player.iconImage, player.frflag);
					sub.getId(Client.myPlayer.id, player.id);
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

	}

	public class FriendPanel extends JPanel implements MouseListener {
		JLabel friendID = new JLabel("yumaaaaaaa");
		JLabel statusLabel = new JLabel();
		JButton friendbattleButton = new JButton();
		JButton friendremoveButton = new JButton("解除");
		
		Player friendPlayerBox = new Player();

		class friendBattleB implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if(friendPlayerBox.frMatch.t_limit==1) {
					if(friendPlayerBox.frMatch.playerRank+1<Client.myPlayer.playerRank||friendPlayerBox.frMatch.playerRank-1>Client.myPlayer.playerRank) {
						JOptionPane.showMessageDialog(Disp.disp,"ランク制限のある卓です");
						return;
					}
				}else if(friendPlayerBox.frMatch.t_limit==2) {
					if(friendPlayerBox.frMatch.playerRank>Client.myPlayer.playerRank){
						JOptionPane.showMessageDialog(Disp.disp,"ランク制限のある卓です");
						return;
					}
				}
					
				if (friendPlayerBox.frMatch.password != null) {
					joinbox = new Join(Disp.disp, ModalityType.APPLICATION_MODAL, friendPlayerBox.frMatch);
					joinbox.setLocation(440, 220);
					joinbox.setVisible(true);
				} else {

					try {
						ArrayList<String> sendPack =new ArrayList<String>();
		    		sendPack.add(friendPlayerBox.frMatch.playerId);
		    		sendPack.add(Client.myPlayer.id);
					OthelloClient.send("BattleEnter", sendPack);
			
					int message = OthelloClient.receiveMSG();
					if(message==0) {
						JOptionPane.showMessageDialog(Disp.disp, "既に削除された卓です");
						Disp.disp.mainmenu.reloadMainmenu();
						Disp.mainmenu.repaint();
					return;
					}
					
					OthelloClient.send("setStatus",3);
					Disp.disp.ChangeDisp(Disp.disp.othello);
					Disp.disp.othello.startOthello(friendPlayerBox.frMatch.rule, 1,friendPlayerBox.id);
					
					} catch (IOException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					}
					
					
				}
			}
			
		}


		FriendPanel(Player friendPlayer, int index) {
			friendID.setFont(new Font("MS ゴシック", Font.BOLD, 16));
			statusLabel.setFont(new Font("MS ゴシック", Font.BOLD, 10));
			friendPlayerBox = friendPlayer;
			this.setBounds(0, 0 + 60 * index, 180, 60);
			this.setLayout(null);
			this.setBorder(new LineBorder(Color.GRAY, 2, true));
			friendbattleButton.setBounds(115, 4, 60, 25);
			friendbattleButton.setBackground(new Color(51, 102, 255));
			friendbattleButton.setForeground(Color.white);
			friendbattleButton.setFont(new Font("MS ゴシック", Font.BOLD, 12));
			friendremoveButton.setBounds(115, 31, 60, 25);
			friendremoveButton.setBackground(new Color(51, 102, 255));
			friendremoveButton.setForeground(Color.white);
			friendremoveButton.setFont(new Font("MS ゴシック", Font.BOLD, 12));
			class friendRemoveB implements ActionListener {
				public void actionPerformed(ActionEvent e) {
					rbox = new RemoveCheck(Disp.disp, ModalityType.APPLICATION_MODAL, friendPlayer.id);
					rbox.setLocation(440, 220);
					rbox.setVisible(true);
				}}
			
			class friendbattleRequestB implements ActionListener {
				public void actionPerformed(ActionEvent e) {
					frbox = new FriendBattleRequest(Disp.disp, ModalityType.APPLICATION_MODAL, friendPlayer.id);
					frbox.setLocation(440, 220);
					frbox.setVisible(true);
				}}
			
			friendID.setBounds(5, 0, 200, 40);
			friendID.addMouseListener(this);
			statusLabel.setBounds(5, 25, 200, 40);
			
			friendremoveButton.addActionListener(new friendRemoveB());
			friendID.setText(friendPlayer.id);
			if (friendPlayer.status == 0) {
				statusLabel.setForeground(Color.DARK_GRAY);
				statusLabel.setText("オフライン");
			} else if (friendPlayer.status == 1) {
				this.add(friendbattleButton);
				friendbattleButton.addActionListener(new friendbattleRequestB());
				statusLabel.setForeground(Color.green);
				statusLabel.setText("オンライン");
				friendbattleButton.setText("申込");
			} else if (friendPlayer.status == 2) {
				this.add(friendbattleButton);
				friendbattleButton.addActionListener(new friendBattleB());
				friendbattleButton.setText("参加");
				statusLabel.setForeground(Color.ORANGE);
				if (friendPlayer.frMatch.rule == 0) {
					statusLabel.setText("対戦待ち(通常戦)");
				} else if (friendPlayer.frMatch.rule == 1) {
					statusLabel.setText("対戦待ち(アイテム戦)");
				}
			} else if (friendPlayer.status == 3) {
				statusLabel.setForeground(Color.red);
				statusLabel.setText("対戦中");
			}

			this.add(friendID);
			this.add(statusLabel);

			this.add(friendremoveButton);

			
		}

		// マウスがボタンの枠内に入った時の処理
		public void mouseEntered(MouseEvent evt) {
			friendID.setForeground(Color.YELLOW);
		}

		public void mouseExited(MouseEvent e) {
			friendID.setForeground(Color.WHITE);

		}

		public void mouseClicked(MouseEvent e) {

		}

		public void mousePressed(MouseEvent e) {
			friendID.setForeground(Color.RED);
			String otherId = friendPlayerBox.id;

			try {
				ArrayList<String> data = new ArrayList<String>();
				data.add(Client.myPlayer.id);
				data.add(otherId);
				OthelloClient.send("getProfile", data);
				InputStream is = OthelloClient.socket1.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				String message = (String) ois.readObject();
				if (message.equals("failed")) {
					return;
				}
				if (message.equals("success")) {
					Player player = (Player) ois.readObject();
					displayProfile sub = new displayProfile(Disp.disp, ModalityType.MODELESS);
					sub.reloadProfile(player.id, player.playerRank, player.win, player.lose, player.draw,
							player.conceed, player.comment, player.iconImage, player.frflag);
					sub.getId(Client.myPlayer.id, player.id);
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

		
	}

	public class FriendBattleRequest extends JDialog {
		Disp disp;
		JButton request = new JButton("申込");
		JLabel nameLabel = new JLabel();
		ButtonGroup bgroup = new ButtonGroup();
		JRadioButton nomal = new JRadioButton("通常戦");
		JRadioButton item = new JRadioButton("アイテム戦");
		String friendName;
		public FriendBattleRequest(Disp disp, ModalityType mt, String name) {
			super(disp, mt);
			friendName=name;
			this.disp = disp;
			this.setResizable(false);
			this.setSize(500, 300);
			this.setTitle("対戦申込");
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setLayout(null);
			nameLabel.setText(name + "への対戦申し込み");
			nameLabel.setHorizontalAlignment(JLabel.CENTER);
			nameLabel.setBounds(50, 80, 400, 50);
			nameLabel.setFont(new Font("MS ゴシック", Font.BOLD, 20));
			this.add(nameLabel);

			nomal.setFont(a);
			item.setFont(a);
			bgroup.add(nomal);
			bgroup.add(item);
			nomal.setSelected(true);
			nomal.setBounds(150, 130, 100, 50);
			item.setBounds(250, 130, 150, 50);
			request.setBounds(210, 200, 80, 40);
			request.setBackground(new Color(51, 102, 255));
			request.setForeground(Color.white);
			request.addActionListener(new RequestB());
			this.add(nomal);
			this.add(item);
			this.add(request);

		}

		class RequestB implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				try {
					ArrayList<String> pack=new ArrayList<String>();
					pack.add(friendName);
					if (nomal.isSelected()) {
						Disp.battleApply.reloadBattleApply(0,friendName);
						pack.add("0");
					}else {
						Disp.battleApply.reloadBattleApply(1,friendName);
						pack.add("1");
					}
					OthelloClient.send("ApplyBattle",pack);
					InputStream is = OthelloClient.socket1.getInputStream();
					ObjectInputStream ois = new ObjectInputStream(is);
					int a=(int)ois.readObject();
					if(a==0) {
					Disp.ChangeDisp(Disp.battleApply);
					
					}else if(a==1) {
						JOptionPane.showMessageDialog(Disp.disp, "そのフレンドは現在取り込み中です");
					reloadMainmenu();
					repaint();
					}
					dispose();
					
				} catch (IOException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				}
			
			}
		}

	}

	public class RemoveCheck extends JDialog {
		Disp disp;
		JLabel checkJLabel = new JLabel();
		JButton yes = new JButton("はい");
		JButton no = new JButton("いいえ");

		public RemoveCheck(Disp disp, ModalityType mt, String name) {
			super(disp, mt);
			this.disp = disp;
			this.setSize(500, 300);
			this.setTitle("フレンド解除");
			this.setResizable(false);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setLayout(null);
			checkJLabel.setText("本当に" + name + "をフレンド解除しますか？");
			checkJLabel.setBounds(50, 80, 400, 50);
			checkJLabel.setFont(new Font("MS ゴシック", Font.BOLD, 20));
			checkJLabel.setHorizontalAlignment(JLabel.CENTER);
			this.add(checkJLabel);
			class YesB implements ActionListener {
				public void actionPerformed(ActionEvent e) {
					ArrayList<String> request=new ArrayList<String>();
					request.add(Client.myPlayer.id);
					request.add(name);
					try {
						OthelloClient.send("delfriend", request);
						reloadMainmenu();
					} catch (IOException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					}
					disp.mainmenu.repaint();
					rbox.dispose();
				}
			}
			yes.setBounds(150, 150, 80, 50);
			yes.setBackground(new Color(51, 102, 255));
			yes.setForeground(Color.white);
			yes.addActionListener(new YesB());
			this.add(yes);
			no.setBounds(260, 150, 80, 50);
			no.setBackground(new Color(51, 102, 255));
			no.setForeground(Color.white);
			no.addActionListener(new NoB());
			this.add(no);
		
		}

		

		class NoB implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				rbox.dispose();
			}
		}
	}

	public void reloadMyPlayer(String nameString) throws IOException, ClassNotFoundException {
		// TODO 自動生成されたメソッド・スタブ
		OthelloClient.send("getMyPlayer", nameString);
		InputStream is = OthelloClient.socket1.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(is);
		Player box = (Player) ois.readObject();
		// System.out.println(box.win+" "+box.lose);
		Client.myPlayer = box;
	}
}
