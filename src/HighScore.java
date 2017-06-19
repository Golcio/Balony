/**
     * klasa przechowujaca wynik gracza 
     * 
     * @param nick wpisana przez uzytkownika nazwa
     * @param punkty ilosc pkt. jaka zdobyl
     */
public class HighScore {
	private String nick;
	private int punkty;
	
	/**
     * Konstruktor tworzacy obiekt wyniku dla podanenej nawy  .
     *
     * @param nick nadana nazwa urzytkownika
     * @param punkty ilsc zdobytych punktow
     */
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
