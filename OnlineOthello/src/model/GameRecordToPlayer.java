package model;

import java.io.Serializable;

public class GameRecordToPlayer implements Serializable{
	public String opponentId;
	public int win = 0;
	public int lose =0;
	public int draw = 0;
	public int conceed = 0;

	public String getOpponentId() {
		return opponentId;
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
