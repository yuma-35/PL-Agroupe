package enums;

public enum LogInStatus {
	OFFLINE(0, "オフライン"), ONLINE(1, "オンライン"), WAITING(2, "対戦待ち"), PLAYING(3, "対局中");

	public int code;
	public String display;

	private LogInStatus(int code, String display) {
		this.code = code;
		this.display = display;
	}
}