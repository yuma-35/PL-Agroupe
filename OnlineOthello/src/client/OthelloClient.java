package client;

import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import client.displays.Disp;
import model.Client;
import model.Player;

public class OthelloClient {
	IPin ipbox;
boolean conection=false;
	static public Socket socket1;
	static public Socket socket2;
	public String server = "localhost";
	public int port = 4231;

	public static void main(String args[]) {
		// Macで表示が崩れないようにする設定
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		new OthelloClient();
	}

	public OthelloClient() {
	
		do {
			ipbox = new IPin(Disp.disp, ModalityType.APPLICATION_MODAL);
			ipbox.setLocation(440, 220);
			ipbox.setVisible(true);
		try {
			// ソケットを2つ作成
			// サーバ受信用のソケット
			socket1 = new Socket(server, port);
			// クライアント受信用のソケット
			socket2 = new Socket(server, port);

			RecieveThread recieveThread = new RecieveThread();
			recieveThread.start();
			conection=true;
			
			Disp.disp = new Disp("オセロ");

		} catch (Exception e) {
			JOptionPane.showMessageDialog(Disp.disp, "サーバーに接続できません。アドレスを確認してください。");	
		}
		}while(!conection);
	}

	static public void send(String operation, Object object) throws IOException {
		OutputStream os = socket1.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		// オペレーション書き込み
		oos.writeObject(operation);
		// 送信データ書き込み
		oos.writeObject(object);
	}
	static public int receiveMSG() throws IOException, ClassNotFoundException {
		int i;
		InputStream is = OthelloClient.socket1.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(is);
		i=(int)ois.readObject();
	
		return i;
	}
	static public Player getEnemy(String iDString) throws IOException, ClassNotFoundException {
		Player enePlayer;
		ArrayList<String> pk = new ArrayList<String>();
		pk.add(Client.myPlayer.id);
		pk.add(iDString);
		OthelloClient.send("getProfile", pk);
		InputStream is2 = OthelloClient.socket1.getInputStream();
		ObjectInputStream ois2 = new ObjectInputStream(is2);
		String message=(String) ois2.readObject();
		
	
		enePlayer=(Player) ois2.readObject();
		return enePlayer;
	}
	
	class IPin extends JDialog implements WindowListener {
		Disp disp;
		JLabel nameLabel=new JLabel();
		JButton setButton=new JButton("決定");		
		JTextField ipField=new JTextField("localhost");
		public IPin(Disp disp, ModalityType mt) {
			// TODO 自動生成されたコンストラクター・スタブ
			super(disp, mt);
			this.disp = disp;
			this.setSize(400, 250);
			this.addWindowListener(this);
			this.setTitle("IPアドレス指定");
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setLayout(null);
			nameLabel.setText("IPアドレスを入力");
			nameLabel.setHorizontalAlignment(JLabel.CENTER);
			nameLabel.setBounds(0, 30, 400, 50);
			nameLabel.setFont(new Font("MS ゴシック", Font.BOLD, 20));
			this.add(nameLabel);
			ipField.setBounds(100,100, 200, 30);
			this.add(ipField);
		
			setButton.addActionListener(new Set());
		setButton.setBounds(160, 150, 80, 40);
		this.add(setButton);
		}
		class Set implements ActionListener {
			public void actionPerformed(ActionEvent e) {
			server=ipField.getText();
			setVisible(false);
			}
			}
		@Override
		public void windowActivated(WindowEvent arg0) {
			// TODO 自動生成されたメソッド・スタブ
			
		}
		@Override
		public void windowClosed(WindowEvent arg0) {
			// TODO 自動生成されたメソッド・スタブ
			System.exit(0);
		}
		@Override
		public void windowClosing(WindowEvent arg0) {
			// TODO 自動生成されたメソッド・スタブ
			
		}
		@Override
		public void windowDeactivated(WindowEvent arg0) {
			// TODO 自動生成されたメソッド・スタブ
			
		}
		@Override
		public void windowDeiconified(WindowEvent arg0) {
			// TODO 自動生成されたメソッド・スタブ
			
		}
		@Override
		public void windowIconified(WindowEvent arg0) {
			// TODO 自動生成されたメソッド・スタブ
			
		}
		@Override
		public void windowOpened(WindowEvent arg0) {
			// TODO 自動生成されたメソッド・スタブ
			
		}
	
	}
	
	
}


