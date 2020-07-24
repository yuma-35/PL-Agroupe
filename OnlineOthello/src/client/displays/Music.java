package client.displays;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Music extends JDialog {
	//メインフレーム
	Disp disp;

	//変更を保存するファイル
	static File fB = new File("Save-data/BGM.txt");
	static File fS = new File("Save-data/SE.txt");

	//ボタン
	JButton toMain = new JButton("戻る");

	Color color = new Color(51, 102, 255);

	//テキスト
	JLabel name = new JLabel("音量調整");
	JLabel name2 = new JLabel("BGM");
	JLabel name3 = new JLabel("SE");

	//JSlider
	//	static JSlider sliderB = new JSlider(-80, 6, (int) getVol(fB));
	//	static JSlider sliderS = new JSlider(-80, 6, (int) getVol(fS));

	static JSlider sliderB = new JSlider(0, 100, (int) getVol(fB));
	static JSlider sliderS = new JSlider(0, 100, (int) getVol(fS));

	//音量
	static FloatControl ctrl;
	static FloatControl ctrl2;



	Music(Disp disp, ModalityType mt) {
		super(disp, mt);
		this.disp = disp;

		//画面サイズ
		//this.setTitle("音量");
		this.getContentPane().setBackground(Color.LIGHT_GRAY);
		this.setSize(520, 250);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLayout(null);
		this.setResizable(false);

		//戻るボタン
		toMain.setFont(new Font("MS ゴシック", Font.BOLD, 10));
		toMain.setForeground(Color.WHITE);
		toMain.setBackground(color);
		toMain.setBounds(220, 170, 80, 25);
		toMain.addActionListener(new toStartM());
		this.add(toMain);

		//テキスト
		name.setFont(new Font("MS ゴシック", Font.BOLD, 20));
		name.setForeground(Color.WHITE);
		name.setBounds(210, 5, 300, 50);
		this.add(name);
		name2.setFont(new Font("MS ゴシック", Font.BOLD, 15));
		name2.setForeground(Color.WHITE);
		name2.setBounds(100, 50, 50, 50);
		this.add(name2);
		name3.setFont(new Font("MS ゴシック", Font.BOLD, 15));
		name3.setForeground(Color.WHITE);
		name3.setBounds(100, 100, 50, 50);
		this.add(name3);

		//JSlider
		sliderB.setBackground(Color.LIGHT_GRAY);
		sliderB.setBounds(150, 50, 250, 50);
		sliderB.addChangeListener(new changeB());
		this.add(sliderB);
		sliderS.setBackground(Color.LIGHT_GRAY);
		sliderS.setBounds(150, 100, 250, 50);
		sliderS.addChangeListener(new changeS());
		this.add(sliderS);

	}

	//slider
	public class changeB implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			//			ctrl.setValue((float) sliderB.getValue());
			ctrl.setValue((float) Math.log10((float) sliderB.getValue() / 100) * 20);

		}
	}

	public class changeS implements ChangeListener {
		public void stateChanged(ChangeEvent e) {

			//			ctrl2.setValue((float) sliderS.getValue());
			ctrl2.setValue((float) Math.log10((float) sliderS.getValue() / 100) * 20);

		}
	}


	//戻るボタン
	public class toStartM implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//ファイルに書き込む
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(fB));
				bw.write(String.valueOf(sliderB.getValue()));
				bw.close();
			} catch (IOException e1) {
				System.out.println(e1);
			}

			try {
				BufferedWriter bw2 = new BufferedWriter(new FileWriter(fS));
				bw2.write(String.valueOf(sliderS.getValue()));
				bw2.close();
			} catch (IOException e2) {
				System.out.println(e2);
			}

			dispose();
		}
	}

	//BGMを鳴らす
	public static void bgm() {
		AudioInputStream ais = null;
		try {
			ais = AudioSystem.getAudioInputStream(new File("BGM,SE/15.wav"));
			AudioFormat af = ais.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, af);
			Clip clip = (Clip) AudioSystem.getLine(info);

			clip.open(ais);


			//			clip.loop(Clip.LOOP_CONTINUOUSLY);//ループ
			//			clip.flush();

			//音量
			ctrl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			//音量取得
			//ctrl.setValue(getVol(fB));
			ctrl.setValue((float) Math.log10((float) getVol(fB) / 100) * 20);


			clip.loop(Clip.LOOP_CONTINUOUSLY);//ループ
			clip.flush();

			while (clip.isActive()) {
				Thread.sleep(100);
			}

		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
			e.printStackTrace();
		} finally {

		}
	}

	//SE読み込み
	public static void se() {
		AudioInputStream ais2 = null;
		try {
			ais2 = AudioSystem.getAudioInputStream(new File("BGM,SE/cursor1_choice.wav"));
			AudioFormat af2 = ais2.getFormat();
			DataLine.Info info2 = new DataLine.Info(Clip.class, af2);
			Clip clip2 = (Clip) AudioSystem.getLine(info2);


			clip2.open(ais2);
			//			clip2.loop(0);//1回
			//			clip2.flush();


			//音量
			ctrl2 = (FloatControl) clip2.getControl(FloatControl.Type.MASTER_GAIN);
			//音量取得
			ctrl2.setValue((float) Math.log10((float) getVol(fS) / 100) * 20);
			//			ctrl2.setValue(getVol(fS));

			clip2.loop(0);//1回
			clip2.flush();

			while (clip2.isActive()) {
				Thread.sleep(100);
			}

		} catch (UnsupportedAudioFileException | IOException
				| LineUnavailableException | InterruptedException e) {
			e.printStackTrace();
		} finally {

		}
	}

	//テキストエリアの中身を返す
	public static float getVol(File f) {
		float vol = 50;
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));

			String str;
			while ((str = br.readLine()) != null) {
				vol = Float.parseFloat(str);
			}

			br.close();

		} catch (IOException e1) {
			System.out.println(e1);
		}

		return vol;
	}

}
