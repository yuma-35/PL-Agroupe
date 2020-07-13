package model;

import java.io.Serializable;

public class Player implements Serializable{
	public String id;
	public String password;
	public int win;
	public int lose;
	public int draw;
	public int conceed;
	public String iconImage;
	public int playerRank;
	public int rankPoint;
	public String comment;
	public String question;
	public String answer;
	public int status;
	public int frflag;
	public Match frMatch;//フレンド関連用


	public String getId() {
		return id;
	}

	public int getWin() {
		return win;
	}

	public int getLose() {
		return lose;
	}

	public int getDraw() {
		return draw;
	}

	public int getConceed() {
		return conceed;
	}

}
