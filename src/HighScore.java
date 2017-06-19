
public class HighScore {
	private String nick;
	private int punkty;
	
	public HighScore(String nick, int punkty)
	{
		this.nick = nick;
		this.punkty = punkty;
	}
	
	public String getNick() {
		return nick;
	}

	public int getPunkty() {
		return punkty;
	}
	
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	public void setPunkty(int punkty) {
		this.punkty = punkty;
	}
}
