//13:

package client.displays;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class BattleRequest extends JDialog {

	//メインフレーム
	Disp disp;

	public BattleRequest(Disp disp, ModalityType mt) {
		super(disp, mt);

		this.disp = disp;

		this.setSize(600, 300);
		this.setLayout(null);
		this.getContentPane().setBackground(Color.LIGHT_GRAY);


		JLabel label = new JLabel("フレンドから対戦申し込みが届きました");
		label.setFont(new Font("MS ゴシック", Font.BOLD, 15));
		label.setForeground(Color.WHITE);
		label.setBounds(160, 50, 300, 20);
		this.add(label);

		JButton no = new JButton("拒否");
		no.setFont(new Font("MS ゴシック", Font.BOLD, 10));
		no.setBounds(270, 250, 60, 30);
		no.setForeground(Color.WHITE);
		no.setBackground(new Color(51, 102, 255));
		no.addActionListener(new toRelease());
		this.add(no);

	}

	//拒否ボタンが押されたときの処理
	public class toRelease implements ActionListener {
		public void actionPerformed(ActionEvent e) {

		dispose();
		}
	}

}
