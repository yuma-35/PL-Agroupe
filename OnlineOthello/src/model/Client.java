package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.displays.Disp;

public class Client {
	public static Player myPlayer = new Player();


	static public class toSoundB implements ActionListener {
		public void actionPerformed(ActionEvent e) {

		}
	}

	public void logout(int situation) {//situationに応じたメッセージ
		myPlayer = null;
		Disp.ChangeDisp(Disp.start);
		//if(situation==){
		//ex 通信がきれました
		//}
	}
}
