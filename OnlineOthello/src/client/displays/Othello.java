package client.displays;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

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

import model.Client;
import model.Player;

//turn変わるたびにおける場所検索のメソッド起動
//aラベルに条件付けして自動で変わるようにする

public class Othello extends JPanel implements MouseListener {
	Result rsbox;
	boolean myTurn = true;
	Player enemyPlayer = new Player();
	int bw;// 黒なら0で先手 白なら1で後手 2ならあいてるけど置けない3なら置ける 境界なら8
	int bwE;
	boolean flipFlag = false;// flip用
	boolean enableFlag = false;
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
	StringBuilder chatBuild = new StringBuilder();
	JLabel myLabel = new JLabel("自分");
	JLabel enemyLabel = new JLabel("相手");
	JLabel myIDLabel = new JLabel("yuma123");
	JLabel enemyIDLabel = new JLabel("taro123");
	JLabel blackkoma = new JLabel(blackIcon);
	JLabel whitekoma = new JLabel(whiteIcon);
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
	JLabel pasLabel = new JLabel("");
	JTextArea chatArea = new JTextArea();
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
		pasLabel.setBounds(420, 530, 200, 30);
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

		enemyIDLabel.setBounds(80, 310, 200, 50);
		enemyIDLabel.setFont(a);
		this.add(enemyIDLabel);

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

	public void startOthello(int rule, int setBW) {
		// enemyPlayer=相手のプレイヤクラス;
		// a相手のアイコン表示

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
		// enemyIDLabel.setText(this.enemyPlayer.id);
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
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {

		if (myTurn) {
			int x = 1 + (int) e.getPoint().x / 50;
			int y = 1 + (int) e.getPoint().y / 50;
			action(x, y);
		}
	}

	void Item1() {
		// potentialとboardinformationをいじる
	}

	void Item2() {

	}

	void Item3() {

	}

	void Item4() {

	}

	void Item5() {

	}

	void action(int x, int y) {
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
		} else if (BoardInformation[x][y] == 3) {
			flip(x, y);

		}

		myTurn = false;
		boardRepaint();
		// send(x,y)
	}

	void waitEnemy() {
		boolean pas = false;
		do {
			if (pas) {
				// send(9,9)
			}
			/*
			 *
			 * Point message=new Point(0,0); //read(message)ここで相手の操作を待つ
			 * if(message.x==9&&!pas){ // パスなので何もしない }else if(message.x==9&&pas){
			 * //終局したので自分のコマの数を数えて送る break; }else if(message.x==10){
			 * //相手が投了したという意味なので終局処理に入る }else if(message.x==11) { Item1(); }else
			 * if(message.x==12) { Item2(); }else if(message.x==13) { Item3(); }else
			 * if(message.x==14) { Item4(); }else if(message.x==15) { Item5(); }else{
			 * flip(message.x,message.y); }
			 */
			pas = true;
		} while (boardRepaint());

		myTurn = true;
		boardRepaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(20, 40, 230, 500);
		g2d.fillRect(750, 40, 220, 500);

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
				action(12, 12);
			}
		}
	}

	public class Item3 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (myTurn) {
				myItem3Button.setEnabled(false);
				action(13, 13);
			}
		}
	}

	public class Item4 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (myTurn) {
				myItem4Button.setEnabled(false);
				action(14, 14);

			}
		}
	}

	public class Item5 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (myTurn) {
				myItem5Button.setEnabled(false);
				action(15, 15);

			}
		}
	}

	public class sendchat implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// if(成功)→textareaに文字列追加&入力欄リセット
			// o失敗→テキストエリアにエラーメッセージ
			if (chatIn.getText() != "") {
				chatBuild.append(Client.myPlayer.id + "：" + chatIn.getText() + "\n");
				chatIn.setText("");
				chatArea.setText(chatBuild.toString());
			}
			gameEnd(2);
		}
	}

	public class conceed implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			action(10, 10);

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

	void gameEnd(int endcase) {// 0勝利、1敗北,2相手の投了,3自分の投了,4相手の切断
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
			/*
			 * class square extends JPanel { public square() { this.setBounds(150, 350, 400,
			 * 100);// TODO 自動生成されたコンストラクター・スタブ } public void paintComponent(Graphics g) {
			 * super.paintComponent(g); Graphics2D g2d = (Graphics2D) g; g2d.fillRect(0, 0,
			 * 400, 100); }
			 */
			// square rankmater=new
			rankChangeLabel.setBounds(250, 160, 200, 50);
			rankChangeLabel.setHorizontalAlignment(JLabel.CENTER);
			this.add(rankChangeLabel);
			rankBar.setBounds(150, 200, 400, 50);
			rankBar.setValue(Client.myPlayer.rankPoint);
			this.add(rankBar);
			rankLabel.setText("ランク" + Client.myPlayer.rank);
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
			enemyResultLabel.setBounds(250, 250, 200, 50);
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
			} else if (end == 1) {
				resultLabel.setText("敗北");
			} else if (end == 2) {
				resultLabel.setText("勝利");
				this.add(endcaseLabel);
				endcaseLabel.setText("相手が投了しました");
			} else if (end == 3) {
				resultLabel.setText("敗北");
				endcaseLabel.setText("投了しました");
			} else if (end == 4) {
				resultLabel.setText("勝利");
				endcaseLabel.setText("相手が切断しました");
			}
			timer = new Timer();
			rankBar.setValue(Client.myPlayer.rankPoint);
			timer.schedule(new TimerTask() {
				int xaddpoint = getpoint(end);
				int addpoint = getpoint(end);

				int now = Client.myPlayer.rankPoint;
				int xnow = Client.myPlayer.rankPoint;
				int rankbox = Client.myPlayer.rank;
				boolean sendflag = true;

				public void run() {

					if (sendflag) {
						while (xaddpoint != 0) {
							if (xaddpoint <= 0) {
								if (xnow == 0) {
									xnow = 100;
									Client.myPlayer.rank--;
								}
								xnow--;
								xaddpoint++;
							} else {
								xnow++;
								if (xnow == 100) {
									xnow = 0;
									Client.myPlayer.rank++;

								}

								rankBar.setValue(xnow);
								xaddpoint--;

							}

							Client.myPlayer.rankPoint = xnow;
							//send
							sendflag = false;
						}

					}

					if (addpoint <= 0) {
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
					} else {
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
				i = 80 - (Client.myPlayer.rank * 5) + (enemyPlayer.rank - Client.myPlayer.rank) * 3;
			} else {
				i = -1 * (20 + (Client.myPlayer.rank * 5) + (Client.myPlayer.rank - enemyPlayer.rank) * 3);
			}
			return i;
		}
	}
}
