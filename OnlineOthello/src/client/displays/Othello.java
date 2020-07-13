package client.displays;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import client.OthelloClient;
import model.Client;
import model.Player;
import model.SendIcon;

//turn変わるたびにおける場所検索のメソッド起動
//aラベルに条件付けして自動で変わるようにする

public class Othello extends JPanel implements MouseListener {
	Result rsbox;
	public boolean myTurn = true;
	public Player enemyPlayer = new Player();
	public int bw;// 黒なら0で先手 白なら1で後手 2ならあいてるけど置けない3なら置ける 境界なら8
	int bwE;
	boolean flipFlag = false;// flip用
	boolean enableFlag = false;
	public boolean passEndListenner;
	Timer timer = new Timer();
	ArrayList<Point> potential = new ArrayList<Point>();
	JLabel[][] BoardLabels = new JLabel[8][8];
	int[][] BoardInformation = new int[10][10];
	boolean doubleAction = false;
	Music msbox;
	JLabel tekikomahyouziJLabel = new JLabel();
	JLabel zikomahyouziJLabel = new JLabel();
	ImageIcon blackIcon = new ImageIcon();
	ImageIcon whiteIcon = new ImageIcon();
	ImageIcon enable = new ImageIcon("image\\SystemImage\\enable.png");

//
	ImageIcon enemyIcon = new ImageIcon();
	ImageIcon myIcon = new ImageIcon();
//

	public StringBuilder chatBuild = new StringBuilder();
	JLabel myLabel = new JLabel("自分");
	JLabel enemyLabel = new JLabel("相手");
	JLabel myIDLabel = new JLabel("yuma123");
	JLabel enemyIDLabel = new JLabel("taro123");
	JLabel blackkoma = new JLabel(blackIcon);
	JLabel whitekoma = new JLabel(whiteIcon);
//
	JLabel eIcon = new JLabel();
	JLabel mIcon = new JLabel();
//
	JLabel ziKomaLabel = new JLabel("自分:");
	JLabel tekiKomaLabel = new JLabel("相手:");
	JLabel turnLabel = new JLabel("あなたのターンです");
	JButton toSounds = new JButton("音量調整");
	JLabel BoardBackLabel = new JLabel();
	JButton conceedButton = new JButton("投了");
	JButton myItem1Button = new JButton(new ImageIcon("image\\SystemImage\\bomb.jpg"));
	JButton myItem2Button = new JButton();
	JButton myItem3Button = new JButton();
	JButton myItem4Button = new JButton();
	JButton myItem5Button = new JButton();
	JButton enemyItem1Button = new JButton(new ImageIcon("image\\SystemImage\\bomb.jpg"));
	JButton enemyItem2Button = new JButton(new ImageIcon("image\\SystemImage\\bomb.jpg"));
	JButton enemyItem3Button = new JButton(new ImageIcon("image\\SystemImage\\bomb.jpg"));
	JButton enemyItem4Button = new JButton(new ImageIcon("image\\SystemImage\\bomb.jpg"));
	JButton enemyItem5Button = new JButton(new ImageIcon("image\\SystemImage\\bomb.jpg"));
	public JLabel pasLabel = new JLabel("");
	public JTextArea chatArea = new JTextArea();
	JTextArea chatIn = new JTextArea();
	JButton sendchatButton = new JButton("送信");

	Font a = new Font("MS ゴシック", Font.BOLD, 20);

	Othello() {
		setSize(1000, 600);
		this.setLayout(null);
		BoardBackLabel.setBounds(300, 100, 399, 399);
		BoardBackLabel.addMouseListener(this);

		for (int s = 0; s < 8; s++) {
			for (int t = 0; t < 8; t++) {
				BoardLabels[s][t] = new JLabel();
				BoardLabels[s][t].setBorder(new LineBorder(Color.black, 1, true));
				BoardLabels[s][t].setHorizontalAlignment(JLabel.CENTER);
				BoardLabels[s][t].setBounds(300 + 50 * s, 100 + 50 * t, 50, 50);
				this.add(BoardLabels[s][t]);
			}

		}

		this.add(BoardBackLabel);
		chatArea.setForeground(Color.white);
		toSounds.setForeground(Color.WHITE);
		toSounds.setBackground(new Color(51, 102, 255));
		toSounds.setBounds(870, 5, 90, 25);
		toSounds.addActionListener(new Client.toSoundB());
		this.add(toSounds);

		conceedButton.setForeground(Color.WHITE);
		conceedButton.setBackground(new Color(51, 102, 255));
		conceedButton.setBounds(800, 5, 90, 25);
		conceedButton.addActionListener(new conceed());

		turnLabel.setFont(a);
		turnLabel.setHorizontalAlignment(JLabel.CENTER);
		turnLabel.setBounds(400, 490, 200, 50);
		this.add(turnLabel);

		ziKomaLabel.setBounds(350, 30, 200, 50);
		ziKomaLabel.setFont(a);
		this.add(ziKomaLabel);
		zikomahyouziJLabel.setBounds(425, 30, 50, 50);
		this.add(zikomahyouziJLabel);
		tekikomahyouziJLabel.setBounds(575, 30, 50, 50);
		this.add(tekikomahyouziJLabel);
		blackkoma.setBounds(420, 30, 50, 50);
		whitekoma.setBounds(570, 30, 50, 50);
		tekiKomaLabel.setBounds(500, 30, 200, 50);
		tekiKomaLabel.setFont(a);
		this.add(tekiKomaLabel);
		pasLabel.setBounds(400, 530, 200, 30);
		pasLabel.setHorizontalAlignment(JLabel.CENTER);
		pasLabel.setFont(a);
		this.add(pasLabel);
		myLabel.setBounds(30, 50, 200, 50);
		myLabel.setFont(a);
		this.add(myLabel);

		enemyLabel.setBounds(30, 270, 200, 50);
		enemyLabel.setFont(a);
		this.add(enemyLabel);

		myIDLabel.setBounds(80, 90, 200, 50);
		myIDLabel.setFont(a);
		this.add(myIDLabel);

		enemyIDLabel.setBounds(80, 310, 100, 50);
		enemyIDLabel.setFont(a);
		enemyIDLabel.addMouseListener(this);
		this.add(enemyIDLabel);

//
		eIcon.setBounds(40, 300, 80, 80);
		this.add(eIcon);
		mIcon.setBounds(40, 80, 80, 80);
		this.add(mIcon);

//

		myItem1Button.setBounds(80, 150, 50, 50);
		myItem1Button.addActionListener(new Item1());
		myItem1Button.setToolTipText("アイテムの説明");

		myItem2Button.setBounds(140, 150, 50, 50);
		myItem2Button.addActionListener(new Item2());
		myItem2Button.setToolTipText("アイテムの説明");

		myItem3Button.setBounds(50, 210, 50, 50);
		myItem3Button.addActionListener(new Item3());
		myItem3Button.setToolTipText("アイテムの説明");

		myItem4Button.setBounds(110, 210, 50, 50);
		myItem4Button.addActionListener(new Item4());
		myItem4Button.setToolTipText("アイテムの説明");

		myItem5Button.setBounds(170, 210, 50, 50);
		myItem5Button.addActionListener(new Item5());
		myItem5Button.setToolTipText("アイテムの説明");

		enemyItem1Button.setBounds(80, 370, 50, 50);
		enemyItem1Button.setToolTipText("アイテムの説明");
		enemyItem2Button.setBounds(140, 370, 50, 50);
		enemyItem2Button.setToolTipText("アイテムの説明");
		enemyItem3Button.setBounds(50, 430, 50, 50);
		enemyItem3Button.setToolTipText("アイテムの説明");
		enemyItem4Button.setBounds(110, 430, 50, 50);
		enemyItem4Button.setToolTipText("アイテムの説明");
		enemyItem5Button.setBounds(170, 430, 50, 50);
		enemyItem5Button.setToolTipText("アイテムの説明");

		conceedButton.setBounds(160, 60, 70, 30);
		conceedButton.setFont(new Font("MS ゴシック", Font.BOLD, 16));
		this.add(conceedButton);
		sendchatButton.setFont(a);
		sendchatButton.setBounds(810, 490, 100, 40);
		sendchatButton.addActionListener(new sendchat());
		this.add(sendchatButton);

		chatIn.setBounds(770, 420, 180, 50);
		this.add(chatIn);

		JScrollPane chatPane = new JScrollPane(chatArea);
		chatArea.setEditable(false);
		chatArea.setBackground(Color.black);
		chatPane.setBounds(770, 60, 180, 350);

		this.add(blackkoma);
		this.add(whitekoma);


		this.add(chatPane);
		this.add(myItem1Button);
		this.add(myItem2Button);
		this.add(myItem3Button);
		this.add(myItem4Button);
		this.add(myItem5Button);
		this.add(enemyItem1Button);
		this.add(enemyItem2Button);
		this.add(enemyItem3Button);
		this.add(enemyItem4Button);
		this.add(enemyItem5Button);



	}

	public class toStartS implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			msbox = new Music(Disp.disp, ModalityType.MODELESS);
			msbox.setLocation(440, 220);
			// this.addLogMessage("サブダイアログのモーダル表示処理を開始");
			msbox.setVisible(true);
			// this.addLogMessage("サブダイアログのモーダル表示処理が完了==結果==> " + subWindow.getResult());

			// Disp.ChangeDisp(Disp.music);
		}
	}

	public void getEnemy(String iDString) throws IOException, ClassNotFoundException {
		ArrayList<String> pk = new ArrayList<String>();
		pk.add(Client.myPlayer.id);
		pk.add(iDString);
		OthelloClient.send("getProfile", pk);
		InputStream is2 = OthelloClient.socket1.getInputStream();
		ObjectInputStream ois2 = new ObjectInputStream(is2);
		ois2.readObject();
		enemyPlayer = (Player) ois2.readObject();
	}

	public void startOthello(int rule, int setBW, String enemyID) throws IOException, ClassNotFoundException {
		getEnemy(enemyID);

		// a相手のアイコン表示
//		//アイコン要求
		OthelloClient.send("geticon", enemyPlayer.getId());

		//受け取り
		SendIcon iconData ;
		InputStream is2 = OthelloClient.socket1.getInputStream();
		ObjectInputStream ois2 = new ObjectInputStream(is2);
		//ois2.readObject();
		iconData = (SendIcon) ois2.readObject();

		File f = iconData.getImage();
		BufferedImage img = ImageIO.read(f);
		enemyIcon = new ImageIcon(img);
		Image smallImg = enemyIcon.getImage().getScaledInstance((int) (enemyIcon.getIconWidth() * 0.6), -1,
	            Image.SCALE_SMOOTH);
	    ImageIcon smallIcon = new ImageIcon(smallImg);
		eIcon.setIcon(smallIcon);

		myIcon = new ImageIcon(Disp.changeicon.getIcon());
		Image smallImg2 = myIcon.getImage().getScaledInstance((int) (myIcon.getIconWidth() * 0.6), -1,
	            Image.SCALE_SMOOTH);
	    ImageIcon smallIcon2 = new ImageIcon(smallImg2);
		mIcon.setIcon(smallIcon2 );

//

		BoardBackLabel.setIcon(new ImageIcon(Disp.changeboard.getBoard()));
		blackIcon = new ImageIcon(Disp.changekoma.getBlack());
		whiteIcon = new ImageIcon(Disp.changekoma.getWhite());
		bw = setBW;
		if (bw == 0) {
			bwE = 1;
			zikomahyouziJLabel.setIcon(blackIcon);
			tekikomahyouziJLabel.setIcon(whiteIcon);
		} else {
			bwE = 0;
			zikomahyouziJLabel.setIcon(whiteIcon);
			tekikomahyouziJLabel.setIcon(blackIcon);
		}

		if (bw == 0) {
			myTurn = true;
		} else {
			myTurn = false;
		}

		for (int s = 0; s < 10; s++) {// a初期盤面セット
			for (int t = 0; t < 10; t++) {
				if ((s == 4 && t == 5) || (s == 5 && t == 4)) {
					BoardInformation[s][t] = 0;// a黒
				} else if ((s == 4 && t == 4) || (s == 5 && t == 5)) {
					BoardInformation[s][t] = 1;// a白
				} else if (s == 0 || t == 0 || s == 9 || t == 9) {
					BoardInformation[s][t] = 8;
				} else {
					BoardInformation[s][t] = 2;
				}
			}
		}
		resetpotential();
		if (rule == 0) {// aアイテムリセット

			myItem1Button.setEnabled(false);
			myItem2Button.setEnabled(false);
			myItem3Button.setEnabled(false);
			myItem4Button.setEnabled(false);
			myItem5Button.setEnabled(false);
			enemyItem1Button.setEnabled(false);
			enemyItem2Button.setEnabled(false);
			enemyItem3Button.setEnabled(false);
			enemyItem4Button.setEnabled(false);
			enemyItem5Button.setEnabled(false);
		} else {
			myItem1Button.setEnabled(true);
			myItem2Button.setEnabled(true);
			myItem3Button.setEnabled(true);
			myItem4Button.setEnabled(true);
			myItem5Button.setEnabled(true);
			enemyItem1Button.setEnabled(true);
			enemyItem2Button.setEnabled(true);
			enemyItem3Button.setEnabled(true);
			enemyItem4Button.setEnabled(true);
			enemyItem5Button.setEnabled(true);

		}
		chatBuild.setLength(0);
		chatBuild.append("      <<対局が開始しました>>" + "\n");
		chatArea.setText(chatBuild.toString());
		myIDLabel.setText(Client.myPlayer.id);
		enemyIDLabel.setText(this.enemyPlayer.id);
		boardRepaint();

	}

	public boolean boardRepaint() {
		if (myTurn) {
			conceedButton.setEnabled(true);
		} else {
			conceedButton.setEnabled(false);
		}
		getEnable();
		boolean pasFlag = true;
		for (int s = 1; s < 9; s++) {// a初期盤面セット
			for (int t = 1; t < 9; t++) {
				if (BoardInformation[s][t] == 0) {
					BoardLabels[s - 1][t - 1].setIcon(blackIcon);
				} else if (BoardInformation[s][t] == 1) {
					BoardLabels[s - 1][t - 1].setIcon(whiteIcon);
				} else if (BoardInformation[s][t] == 3) {
					pasFlag = false;
					if (myTurn) {
						BoardLabels[s - 1][t - 1].setIcon(enable);
					} else {
						BoardLabels[s - 1][t - 1].setIcon(null);
					}
				} else {
					BoardLabels[s - 1][t - 1].setIcon(null);

				}
			}
		}
		if (myTurn) {
			if (pasFlag) {
				turnLabel.setText("相手のターンです");
				pasLabel.setText("パスされました");
			} else {
				turnLabel.setText("あなたのターンです");
				pasLabel.setText("");
			}
		} else {
			turnLabel.setText("相手のターンです");
			pasLabel.setText("");
		}
		return pasFlag;
	}

	public void getEnable() {// o置ける場所検索
		int i = 0;
		do {
			for (int s = -1; s <= 1; s++) {
				for (int t = -1; t <= 1; t++) {
					if (!(s == 0 && t == 0)) {
						if (potential.size() != 0) {
							flipserch(s, t, potential.get(i).x, potential.get(i).y, 0, myTurn);

							if (flipFlag) {
								s = 10;
								t = 10;
								BoardInformation[potential.get(i).x][potential.get(i).y] = 3;
							} else {
								BoardInformation[potential.get(i).x][potential.get(i).y] = 2;
							}
						}
						flipFlag = false;
						enableFlag = false;
					} else {
						// aパスを送る
					}
				}
			}
			i++;
		} while (i < potential.size());
	}

	public void flip(int xx, int yy) {
		if (myTurn)
			BoardInformation[xx][yy] = bw;
		else
			BoardInformation[xx][yy] = bwE;
		for (int s = -1; s <= 1; s++) {
			for (int t = -1; t <= 1; t++) {
				if (!(s == 0 && t == 0)) {
					flipserch(s, t, xx, yy, 1, myTurn);
					flipFlag = false;
				}
			}
		}
		potential.remove(new Point(xx, yy));
		for (int s = -1; s <= 1; s++) {// getenable用の処理
			for (int t = -1; t <= 1; t++) {
				if (!(s == 0 && t == 0)) {
					if (BoardInformation[xx + s][yy + t] == 2)
						potential.add(new Point(xx + s, yy + t));
				}
			}
		}
		potential = new ArrayList<Point>(new HashSet<>(potential));
	}

	public int countEnd() {// a勝ったら0 負けたら１ 引き分け5
		int black = 0;
		int white = 0;
		for (int s = 1; s < 9; s++) {// a初期盤面セット
			for (int t = 1; t < 9; t++) {
				if (BoardInformation[s][t] == 0) {
					black++;
				} else if (BoardInformation[s][t] == 1) {
					white++;
				}
			}
		}
		System.out.println(black + " " + white + " " + bw);
		if (black == white) {
			return 5;
		} else if (black > white) {
			if (bw == 0) {
				return 0;
			} else {
				return 1;
			}
		} else if (white > black) {
			if (bw == 1) {
				return 0;
			} else {
				return 1;
			}
		}else {
			System.out.print("エラー");
			return 0;
		}

	}

	public void flipserch(int distX, int distY, int xx, int yy, int mode, boolean turn) {
		// mode==1 ひっくり返す用 mode==0 置けるか判定
		if (xx + distX < 9 && xx + distX > 0 && yy + distY < 9 && yy + distY > 0) {
			if (!turn) {
				if (BoardInformation[xx + distX][yy + distY] == bw) {
					if (mode == 0) {
						enableFlag = true;
					}
					flipserch(distX, distY, xx + distX, yy + distY, mode, turn);
					if (flipFlag && mode == 1) {
						BoardInformation[xx + distX][yy + distY] = bwE;
					}

				} else if (BoardInformation[xx + distX][yy + distY] == bwE) {
					if (mode == 1) {
						flipFlag = true;
					} else if (mode == 0 && enableFlag) {
						flipFlag = true;
					}
				}

			} else {

				if (BoardInformation[xx + distX][yy + distY] == bwE) {
					if (mode == 0) {
						enableFlag = true;
					}
					flipserch(distX, distY, xx + distX, yy + distY, mode, turn);
					if (flipFlag && mode == 1) {
						BoardInformation[xx + distX][yy + distY] = bw;
					}

				} else if (BoardInformation[xx + distX][yy + distY] == bw) {
					if (mode == 1) {
						flipFlag = true;
					} else if (mode == 0 && enableFlag) {
						flipFlag = true;
					}
				}
			}
		}

	}

	public void mouseEntered(MouseEvent e) {
		Object obj = e.getSource();
		if(obj == enemyIDLabel) {
			enemyIDLabel.setForeground(Color.YELLOW);
		}
	}

	public void mouseExited(MouseEvent e) {
		Object obj = e.getSource();
		if(obj == enemyIDLabel) {
			enemyIDLabel.setForeground(Color.WHITE);
		}
	}

	public void mousePressed(MouseEvent e) {
		Object obj = e.getSource();
		if(obj == enemyIDLabel) {
			enemyIDLabel.setForeground(Color.RED);
			String otherId = enemyPlayer.id;

			try {
				ArrayList<String> data = new ArrayList<String>();
				data.add(Client.myPlayer.id);
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
					displayProfile sub = new displayProfile(Disp.disp, ModalityType.MODELESS);
					sub.reloadProfile(player.id, player.playerRank, player.win, player.lose, player.draw, player.conceed, player.comment, player.iconImage, player.frflag);
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

	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {

		Object obj = e.getSource();
		if(obj == enemyIDLabel) {

		}else {

			if (myTurn) {
				int x = 1 + (int) e.getPoint().x / 50;
				int y = 1 + (int) e.getPoint().y / 50;
				try {
					if (BoardInformation[x][y] == 3) {
						action(x, y);
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

	public void Item1() {
		// potentialとboardinformationをいじる
	}

	public void Item2() {

	}

	public void Item3() {

	}

	public void Item4() {

	}

	public void Item5() {

	}

	public void action(int x, int y) throws IOException, ClassNotFoundException {
		if (x == 11 && y == 11) {
			Item1();
		} else if (x == 12 && y == 12) {
			Item2();
		} else if (x == 13 && y == 13) {
			Item3();
		} else if (x == 14 && y == 14) {
			Item4();
		} else if (x == 15 && y == 15) {
			Item5();
		} else if (x == 10 && y == 10) {
			Point pointmessage = new Point(x, y);
			OthelloClient.send("SendAction", pointmessage);
			gameEnd(3);
			return;
		} else if (BoardInformation[x][y] == 3) {
			flip(x, y);
		}
		Point pointmessage = new Point(x, y);
		myTurn = false;
		boardRepaint();
		OthelloClient.send("SendAction", pointmessage);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(20, 40, 230, 500);
		g2d.fillRect(750, 40, 220, 500);
		g2d.fillRect(400, 490, 200, 70);

	}

	public class Item1 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (myTurn) {
				myItem1Button.setEnabled(false);
				// action(11, 11);
			}
		}
	}

	public class Item2 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (myTurn) {
				myItem2Button.setEnabled(false);
				try {
					action(12, 12);
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

	public class Item3 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (myTurn) {
				myItem3Button.setEnabled(false);
				try {
					action(13, 13);
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

	public class Item4 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (myTurn) {
				myItem4Button.setEnabled(false);
				try {
					action(14, 14);
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

	public class Item5 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (myTurn) {
				myItem5Button.setEnabled(false);
				try {
					action(15, 15);
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

	public class sendchat implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				OthelloClient.send("SendChat", chatIn.getText());
			} catch (IOException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
			if (chatIn.getText() != "") {
				chatBuild.append(Client.myPlayer.id + "：" + chatIn.getText() + "\n");
				chatIn.setText("");
				chatArea.setText(chatBuild.toString());
			}

		}
	}

	public class conceed implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			try {
				action(10, 10);
			} catch (IOException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}

		}
	}

	void resetpotential() {
		potential.clear();
		potential.add(new Point(3, 3));
		potential.add(new Point(4, 3));
		potential.add(new Point(5, 3));
		potential.add(new Point(6, 3));
		potential.add(new Point(6, 4));
		potential.add(new Point(6, 5));
		potential.add(new Point(6, 6));
		potential.add(new Point(5, 6));
		potential.add(new Point(4, 6));
		potential.add(new Point(3, 6));
		potential.add(new Point(3, 5));
		potential.add(new Point(3, 4));
	}

	public void gameEnd(int endcase) throws ClassNotFoundException, IOException {// 0勝利、1敗北,2相手の投了,3自分の投了,4相手の切断5,引き分け
		rsbox = new Result(Disp.disp, ModalityType.APPLICATION_MODAL, endcase);
		rsbox.setLocationRelativeTo(null);
		rsbox.setVisible(true);
		Disp.disp.ChangeDisp(Disp.mainmenu);
		Disp.mainmenu.reloadMainmenu();
	}

	public class Result extends JDialog {
		Disp disp;
		int end;
		JLabel enemyResultLabel = new JLabel();
		JButton friendRequestButton = new JButton("フレンド申請");
		JLabel resultLabel = new JLabel();
		JLabel endcaseLabel = new JLabel();
		JLabel rankpointJLabel = new JLabel();
		JLabel rankLabel = new JLabel();
		JButton backButton = new JButton("終了");
		JProgressBar rankBar = new JProgressBar();
		JLabel rankChangeLabel = new JLabel();

		public class friendRequest implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				friendRequestButton.setEnabled(false);
			}
		}

		public Result(Disp disp, ModalityType mt, int endcase) {
			super(disp, mt);
			this.disp = disp;
			this.setSize(700, 500);
			this.setTitle("対局結果");
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setLayout(null);
			friendRequestButton.setBounds(250, 300, 200, 50);
			friendRequestButton.setHorizontalAlignment(JLabel.CENTER);
			friendRequestButton.setFont(new Font("MS　ゴシック", Font.BOLD, 20));
			friendRequestButton.setForeground(Color.WHITE);
			friendRequestButton.setBackground(new Color(51, 102, 255));
			friendRequestButton.addActionListener(new friendRequest());
			this.add(friendRequestButton);
			if (enemyPlayer.frflag == 1) {
				friendRequestButton.setEnabled(false);
			}
			class Back implements ActionListener {
				public void actionPerformed(ActionEvent e) {
					timer.cancel();
					timer = null;
					dispose();
				}
			}

			rankChangeLabel.setBounds(250, 160, 200, 50);
			rankChangeLabel.setHorizontalAlignment(JLabel.CENTER);
			this.add(rankChangeLabel);
			rankBar.setBounds(150, 200, 400, 50);
			rankBar.setValue(Client.myPlayer.rankPoint);
			this.add(rankBar);
			rankLabel.setText("ランク" + Client.myPlayer.playerRank);
			rankLabel.setBounds(250, 130, 200, 50);
			this.add(rankLabel);
			rankLabel.setHorizontalAlignment(JLabel.CENTER);
			rankLabel.setFont(new Font("MS ゴシック", Font.BOLD, 30));
			resultLabel.setForeground(Color.red);
			endcaseLabel.setFont(new Font("MS ゴシック", Font.BOLD, 20));
			resultLabel.setFont(new Font("MS ゴシック", Font.BOLD, 80));
			end = endcase;
			endcaseLabel.setBounds(250, 80, 200, 50);
			resultLabel.setBounds(250, 0, 200, 100);
			resultLabel.setHorizontalAlignment(JLabel.CENTER);
			endcaseLabel.setHorizontalAlignment(JLabel.CENTER);
			this.add(resultLabel);
			enemyResultLabel.setFont(new Font("MS ゴシック", Font.BOLD, 30));
			enemyResultLabel.setBounds(100, 250, 500, 50);
			enemyResultLabel.setText("対戦相手:" + enemyPlayer.id);
			enemyResultLabel.setHorizontalAlignment(JLabel.CENTER);
			this.add(enemyResultLabel);
			backButton.setFont(new Font("MS ゴシック", Font.BOLD, 30));
			backButton.setBounds(300, 370, 100, 50);
			backButton.addActionListener(new Back());
			backButton.setForeground(Color.WHITE);
			backButton.setBackground(new Color(51, 102, 255));
			this.add(backButton);
			if (end == 0) {
				resultLabel.setText("勝利");
				Client.myPlayer.win++;
			} else if (end == 1) {
				resultLabel.setText("敗北");
				Client.myPlayer.lose++;
			} else if (end == 2) {
				resultLabel.setText("勝利");
				this.add(endcaseLabel);
				endcaseLabel.setText("相手が投了しました");
				Client.myPlayer.win++;
			} else if (end == 3) {
				resultLabel.setText("敗北");
				endcaseLabel.setText("投了しました");
				Client.myPlayer.conceed++;
			} else if (end == 4) {
				resultLabel.setText("勝利");
				endcaseLabel.setText("相手が切断しました");
				Client.myPlayer.win++;
			} else if (end == 5) {
				resultLabel.setText("引き分け");
				Client.myPlayer.draw++;
			}
			timer = new Timer();
			rankBar.setValue(Client.myPlayer.rankPoint);
			timer.schedule(new TimerTask() {
				int xaddpoint = getpoint(end);
				int addpoint = getpoint(end);

				int now = Client.myPlayer.rankPoint;
				int xnow = Client.myPlayer.rankPoint;
				int rankbox = Client.myPlayer.playerRank;
				boolean sendflag = true;

				public void run() {

					if (sendflag) {
						while (xaddpoint != 0) {
							if (xaddpoint <= 0
									&& !(Client.myPlayer.rankPoint == 0 && Client.myPlayer.playerRank == 0)) {
								if (xnow == 0) {
									xnow = 100;
									Client.myPlayer.playerRank--;
								}
								xnow--;
								xaddpoint++;
							} else if (xaddpoint > 0
									&& !(Client.myPlayer.rankPoint == 10 && Client.myPlayer.playerRank == 100)) {
								xnow++;
								if (xnow == 100) {
									xnow = 0;
									Client.myPlayer.playerRank++;

								}

								rankBar.setValue(xnow);
								xaddpoint--;

							}

							Client.myPlayer.rankPoint = xnow;
							try {
								OthelloClient.send("updatePlayer", Client.myPlayer);
								ArrayList<String> gameRecordPackage = new ArrayList<String>();
								gameRecordPackage.add(Client.myPlayer.id);
								gameRecordPackage.add(enemyPlayer.id);
								gameRecordPackage.add(String.valueOf(endcase));
								OthelloClient.send("MakeGameRecord", gameRecordPackage);
								// a対局記録作る
							} catch (IOException e) {
								// TODO 自動生成された catch ブロック
								e.printStackTrace();
							}

							sendflag = false;
						}

					}

					if (addpoint <= 0 && !(rankbox == 0 && now == 0)) {
						if (addpoint != 0) {

							if (now == 0) {
								now = 100;
								rankbox--;
								rankLabel.setText("ランク" + rankbox);
								rankChangeLabel.setText("ランクダウン");
							}
							now--;
							rankBar.setValue(now);
							addpoint++;

						}
					} else if (addpoint > 0 && !(rankbox == 10 && now == 100)) {
						if (addpoint != 0) {
							now++;
							if (now == 100) {
								now = 0;
								rankBar.setValue(now);
								rankbox++;
								rankLabel.setText("ランク" + rankbox);
								rankChangeLabel.setText("ランクアップ!");
							}
							rankBar.setValue(now);
							addpoint--;

						}
					}

				}
			}, 0, 10);

		}

		int getpoint(int endset) {
			int i;
			if (endset == 0 || endset == 2 || endset == 4) {
				i = 80 - (Client.myPlayer.playerRank * 5) + (enemyPlayer.playerRank - Client.myPlayer.playerRank) * 3;
			} else if (endset == 1 || endset == 3) {
				i = -1 * (20 + (Client.myPlayer.playerRank * 5)
						+ (Client.myPlayer.playerRank - enemyPlayer.playerRank) * 3);
			} else {
				i = 0;
			}
			return i;
		}
	}
}
