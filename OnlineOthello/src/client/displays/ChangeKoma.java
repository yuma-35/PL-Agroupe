package client.displays;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class ChangeKoma extends JPanel {
	String str = "コマの着せ替え";

	//ファイル名を取得
	File Bfile = new File("image/コマ/Black");
	File Bfiles[] = Bfile.listFiles();
	File Wfile = new File("image/コマ/White");
	File Wfiles[] = Wfile.listFiles();

	//変更を保存するファイル
	File fb= new File("save-data/black.txt");
	File fw= new File("save-data/white.txt");

	//JRadioButton
	 JRadioButton radio1;
	 JRadioButton radio2;
	 JRadioButton radio3;
	 JRadioButton radio4;
	 JRadioButton radio5;
	 JRadioButton radio6;
	 JRadioButton radio7;
	 JRadioButton radio11;
	 JRadioButton radio22;
	 JRadioButton radio33;
	 JRadioButton radio44;
	 JRadioButton radio55;
	 JRadioButton radio66;
	 JRadioButton radio77;

	//ボタン
		JButton toSounds = new JButton("音量調整");
		JButton toMain = new JButton("決定");

		Color color = new Color(51,102,255);

	//ラベル
		JLabel name = new JLabel();
		JLabel black = new JLabel("黒");
		JLabel white = new JLabel("白");

		JPanel p = new JPanel();
		JPanel q = new JPanel();
		int j = 0;

	//位置
		static int Bheight = 260;
		static int Wheight = 450;



	ChangeKoma(){
		p.setLayout(null);
		q.setLayout(null);
		//画面サイズは固定
		 setSize(1000, 600);
		 this.setLayout(null);

		//タイトル、ラベル
		 name.setFont(new Font("MS ゴシック", Font.BOLD, 20));
		 name.setText(str);
		 name.setForeground(Color.WHITE);
		 name.setBounds(200,70,300,100);
		 this.add(name);
		 black.setFont(new Font("MS ゴシック", Font.BOLD, 20));
		 black.setForeground(Color.WHITE);
		 black.setBounds(130,120,100,100);
		 this.add(black);
		 white.setFont(new Font("MS ゴシック", Font.BOLD, 20));
		 white.setForeground(Color.WHITE);
		 white.setBounds(130,320,100,100);
		 this.add(white);

		 //ボタン
		 ButtonTest(Bheight,Bfiles,Wheight,Wfiles);

		//画像
         for (int i = 0; i < Bfiles.length ; i++) {
        	 ImageTest(Bheight/3,Bfiles[i].getPath(),p);
        	 j = j + 100;
         }

         p.setBackground(Color.LIGHT_GRAY);
         p.setBounds(110,50, 800, 200);
         this.add(p);

         j = 0;//画像x座標のリセット

         for (int i = 0; i < Wfiles.length ; i++) {
        	ImageTest(Wheight/2,Wfiles[i].getPath(),q);
 		    j = j + 100;
         }

         q.setBackground(Color.LIGHT_GRAY);
		 q.setBounds(110,100, 800, 400);
         this.add(q);

	}

	//ボタン
		public void ButtonTest(int i,File files[],int k, File files2[]) {
			//音量調整ボタン
			 toSounds.setFont(new Font("MS ゴシック", Font.BOLD, 10));
		     toSounds.setForeground(Color.WHITE);
		     toSounds.setBackground(color);
			 toSounds.setBounds(870, 5, 90, 25);
			 toSounds.addActionListener(new toStartS());
			 this.add(toSounds);

			//決定ボタン
			 toMain.setFont(new Font("MS ゴシック", Font.BOLD, 10));
			 toMain.setForeground(Color.WHITE);
		     toMain.setBackground(color);
			 toMain.setBounds(487, 510, 90, 25);
			 toMain.addActionListener(new toStartM());
			 this.add(toMain);

			 //選択ボタン
	        radio1 = new JRadioButton("", true);
	        radio2 = new JRadioButton("", false);
	        radio3 = new JRadioButton("", false);
	        radio4 = new JRadioButton("", false);
	        radio5 = new JRadioButton("", false);
	        radio6 = new JRadioButton("", false);
	        radio7 = new JRadioButton("", false);
	        radio1.setBackground(Color.LIGHT_GRAY);
	        radio2.setBackground(Color.LIGHT_GRAY);
	        radio3.setBackground(Color.LIGHT_GRAY);
	        radio4.setBackground(Color.LIGHT_GRAY);
	        radio5.setBackground(Color.LIGHT_GRAY);
	        radio6.setBackground(Color.LIGHT_GRAY);
	        radio7.setBackground(Color.LIGHT_GRAY);
	        ButtonGroup group = new ButtonGroup();
	        group.add(radio1);
	        group.add(radio2);
	        group.add(radio3);
	        group.add(radio4);
	        group.add(radio5);
	        group.add(radio6);
	        group.add(radio7);
	        radio1.setBounds(200, i, 100, 25);
	        radio2.setBounds(300, i, 100, 25);
	        radio3.setBounds(400, i, 100, 25);
	        radio4.setBounds(500, i, 100, 25);
	        radio5.setBounds(600, i, 100, 25);
	        radio6.setBounds(700, i, 100, 25);
	        radio7.setBounds(800, i, 100, 25);
	        this.add(radio1);
	        this.add(radio2);
	        this.add(radio3);
	        this.add(radio4);
	        this.add(radio5);
	        this.add(radio6);
	        this.add(radio7);

	        radio11 = new JRadioButton("", true);
	        radio22 = new JRadioButton("", false);
	        radio33 = new JRadioButton("", false);
	        radio44 = new JRadioButton("", false);
	        radio55 = new JRadioButton("", false);
	        radio66 = new JRadioButton("", false);
	        radio77 = new JRadioButton("", false);
	        radio11.setBackground(Color.LIGHT_GRAY);
	        radio22.setBackground(Color.LIGHT_GRAY);
	        radio33.setBackground(Color.LIGHT_GRAY);
	        radio44.setBackground(Color.LIGHT_GRAY);
	        radio55.setBackground(Color.LIGHT_GRAY);
	        radio66.setBackground(Color.LIGHT_GRAY);
	        radio77.setBackground(Color.LIGHT_GRAY);
	        ButtonGroup group2 = new ButtonGroup();
	        group2.add(radio11);
	        group2.add(radio22);
	        group2.add(radio33);
	        group2.add(radio44);
	        group2.add(radio55);
	        group2.add(radio66);
	        group2.add(radio77);
	        radio11.setBounds(200, k, 100, 25);
	        radio22.setBounds(300, k, 100, 25);
	        radio33.setBounds(400, k, 100, 25);
	        radio44.setBounds(500, k, 100, 25);
	        radio55.setBounds(600, k, 100, 25);
	        radio66.setBounds(700, k, 100, 25);
	        radio77.setBounds(800, k, 100, 25);
	        this.add(radio11);
	        this.add(radio22);
	        this.add(radio33);
	        this.add(radio44);
	        this.add(radio55);
	        this.add(radio66);
	        this.add(radio77);

		}

		//画像
		public void ImageTest(int i,String str,JPanel r) {
		    ImageIcon icon = new ImageIcon(str);

			//JLabelにアイコンを設定
		    JLabel l = new JLabel(icon);

		    l.setBounds(50 + j, i , 100, 100);
		    r.add(l);
		}

		//音量調整ボタン
		public class toStartS implements ActionListener{
	     	public  void actionPerformed(ActionEvent e) {
	     		Account.subWindow = new Music(Disp.disp,ModalityType.MODELESS);
	     		Account.subWindow.setLocation(440, 220);
	     		Account.subWindow.setVisible(true);
	     	}
	     }
		//決定ボタン
		public class toStartM implements ActionListener{
	     	public  void actionPerformed(ActionEvent e) {
	     		//黒
	     		boolean status1 = radio1.isSelected();
	            boolean status2 = radio2.isSelected();
	            boolean status3 = radio3.isSelected();
	            boolean status4 = radio4.isSelected();
	            boolean status5 = radio5.isSelected();
	            boolean status6 = radio6.isSelected();
	            boolean status7 = radio7.isSelected();


	            //ファイルのパス名
	            String str = null;

	            if(status1 == true) {
	            	str = Bfiles[0].getPath();
	            }else if(status2 == true) {
	            	str = Bfiles[1].getPath();
	            }else if(status3 == true) {
	            	str = Bfiles[2].getPath();
	            }else if(status4 == true) {
	            	str = Bfiles[3].getPath();
	            }else if(status5 == true) {
	            	str = Bfiles[4].getPath();
	            }else if(status6 == true) {
	            	str = Bfiles[5].getPath();
	            }else if(status7 == true) {
	            	str = Bfiles[6].getPath();
	            }

	          //ファイルに書き込む
	 			try{
	 				BufferedWriter bw = new BufferedWriter(new FileWriter(fb));
	 				bw.write(str);
	 				bw.close();
	 			} catch(IOException e1){
	     			  System.out.println(e1);
	     			 JOptionPane.showMessageDialog(Disp.disp, "エラーが発生しました");
	     		}

	 			//白
	     		boolean status11 = radio11.isSelected();
	            boolean status22 = radio22.isSelected();
	            boolean status33 = radio33.isSelected();
	            boolean status44 = radio44.isSelected();
	            boolean status55 = radio55.isSelected();
	            boolean status66 = radio66.isSelected();
	            boolean status77 = radio77.isSelected();

	            //ファイルのパス名
	            String str1 = null;

	            if(status11 == true) {
	            	str1 = Wfiles[0].getPath();
	            }else if(status22 == true) {
	            	str1 = Wfiles[1].getPath();
	            }else if(status33 == true) {
	            	str1 = Wfiles[2].getPath();
	            }else if(status44 == true) {
	            	str1 = Wfiles[3].getPath();
	            }else if(status55 == true) {
	            	str1 = Wfiles[4].getPath();
	            }else if(status66 == true) {
	            	str1 = Wfiles[5].getPath();
	            }else if(status77 == true) {
	            	str1 = Wfiles[6].getPath();
	            }

	          //ファイルに書き込む
	 			try{
	 				BufferedWriter bw = new BufferedWriter(new FileWriter(fw));
	 				bw.write(str1);
	 				bw.close();
	 			} catch(IOException e1){
	     			  System.out.println(e1);
	     			  JOptionPane.showMessageDialog(Disp.disp, "エラーが発生しました");
	     		}

 	     		Disp.ChangeDisp(Disp.account);
	     	}
	     }

		//テキストエリアの中身を返す
		public String getBlack() {
				String str2 = null;
				try{
					BufferedReader br = new BufferedReader(new FileReader(fb));

					String str;
				    while((str = br.readLine()) != null){
			        	str2 = str;
			        }

					br.close();

				} catch(IOException e1){
		 			  System.out.println(e1);
		 			 JOptionPane.showMessageDialog(Disp.disp, "エラーが発生しました");
		 		}
				return str2;

		}

		public String getWhite() {
			String str2 = null;
			try{
				BufferedReader br = new BufferedReader(new FileReader(fw));

				String str;
			    while((str = br.readLine()) != null){
		        	str2 = str;
		        }

				br.close();

			} catch(IOException e1){
	 			  System.out.println(e1);
	 			 JOptionPane.showMessageDialog(Disp.disp, "エラーが発生しました");
	 		}
			return str2;

	}



}
