package client.displays;

import java.awt.Dialog.ModalityType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.RecieveThread.FriendBattleRequestDialog;

public class Disp extends JFrame {
	public static Disp disp;
	public static StartDisp start = new StartDisp();
	public static Mainmenu mainmenu = new Mainmenu();
	public static BattleApply battleApply = new BattleApply();
	static OthelloWait othellowait = new OthelloWait();
	public static Othello othello = new Othello();
	static MakeAccount makeaccount = new MakeAccount(); //4:アカウント作成
	static Forget forget = new Forget(); //5:ID入力
	static Question question = new Question(); //6:新パスワード設定
	static FriendRegister friendregister = new FriendRegister(); //20:フレンド登録
	static FriendRequest friendrequest = new FriendRequest(); //21:届いた申請
	static Gamerecords gamerecords = new Gamerecords();
	static Account account = new Account();
	static Comment comment = new Comment();
	static ChangeKoma changekoma = new ChangeKoma();
	static ChangeBoard changeboard = new ChangeBoard();
	static ChangeBack changeback = new ChangeBack();
	static ChangeIcon changeicon = new ChangeIcon();
	static MakeTable maketable = new MakeTable();
	//背景
	static JLabel background = new JLabel();
	//

	public static JPanel nowPanel = start;

	static File f = new File("save-data/back.txt");
	static String path;

	public Disp(String title) {
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBack();
		this.setLocation(200, 100);
		setSize(1000, 600);
		this.setResizable(false);

		//
		background.setBounds(0, 0, 1000, 600);
		this.add(background);
		background.add(nowPanel);
		nowPanel.setOpaque(false);//背景を透明にする
		//
		//a音量
		//	static FloatControl ctrl;

		start.setVisible(true);
		setVisible(true);

		set(forget);
		set(mainmenu);
		set(battleApply);
		//
		set(othello);
		set(othellowait);
		set(makeaccount);
		set(question);
		set(friendregister);
		set(friendrequest);
		set(gamerecords);
		set(account);
		set(comment);
		set(changekoma);
		set(changeboard);
		set(changeback);
		set(changeicon);
		set(maketable);

		Music.bgm();
	}
	//

	static void setBack() {
		//a現在の背景を取得
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));

			String str;
			while ((str = br.readLine()) != null) {
				path = str;
			}

			br.close();

			background.setIcon(new ImageIcon(path));

		} catch (IOException e1) {
			System.out.println(e1);
		}
	}

	//
	void set(JPanel setpane) {
		background.add(setpane);
		setpane.setOpaque(false);//背景を透明にする
		setpane.setVisible(false);
	}
	//

	public static void ChangeDisp(JPanel change) {
		nowPanel.setVisible(false);
		change.setVisible(true);
		nowPanel = change;
	}
	
	

	//BGMを鳴らす
	/*	public static void bgm(){
			AudioInputStream ais = null;
	        try {
	            ais = AudioSystem.getAudioInputStream(new File("BGM,SE\\1年ナンバー(仮).wav"));
	            AudioFormat af = ais.getFormat();
	            DataLine.Info info = new DataLine.Info(Clip.class, af);
	            Clip clip = (Clip)AudioSystem.getLine(info);

	            clip.open(ais);
	            //clip.loop(0);//1回
	            //clip.loop(1);//2回

	            clip.loop(Clip.LOOP_CONTINUOUSLY);//ループ
	            clip.flush();

	            //音量
	            ctrl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
	            ctrl.setValue((float)-5);

	/*	            for ( int i = 100;i >= 0;i-- ) {
	    			Thread.sleep(50);
	    			ctrl.setValue((float)Math.log10((float)i / 100)*20);
	    		}

	           while(clip.isActive()) {
	                Thread.sleep(100);
	    			//ctrl.setValue((float)-0.1);
	            }

	            clip.close();
	        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
	            e.printStackTrace();
	        }finally {
	            try {
	                ais.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }

		}*/
}