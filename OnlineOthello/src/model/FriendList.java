package model;

import java.util.ArrayList;

public class FriendList {

	String search_id; //検索ID

	//フレンドリスト
	ArrayList<Friend> list = new ArrayList<Friend>();

	//コンストラクタ
	public FriendList() {

	}

	//フレンドリストを取得
	public void get_friendlist() {
		//サーバから最新のフレンドリストを受信
	}

	//フレンド検索IDを取得
	public void search(String id) {
		search_id = id; //IDをテキストフィールドから取得
		//サーバにそのIDを送信
		//サーバから該当するIDのアカウント情報を取得
	}

	//フレンド申請する
	public void request(String id) {
		search_id = id;

	}

	//フレンド解除する
	public void remove(String id) {
		//friend.remove();
	}

	//フレンド申請情報を取得
	public void req_fri() {

	}

	//フレンド申請を許可したとき
	public void yes() {
		Friend newfriend = new Friend();
		list.add(newfriend);
	}

	//フレンド申請を拒否したとき
	public void no() {
		//拒否情報をサーバに送信
	}

	//フレンドへ対戦要求
	public void battle() {

	}

	//フレンドからの対戦要求が来た時にサブモーダルを表示するっていう判断をするためのメソッドも入れるかも？

}
