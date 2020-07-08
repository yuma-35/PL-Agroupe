package model;

import java.io.Serializable;

public class Match implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String playerId;
	public int playerRank;
	public int rule;
	public int t_limit;
	public String password;
	public String comment;
	public int friendOnly;
	
}
