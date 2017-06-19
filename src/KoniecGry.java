import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;

/**
 *Okno pojawiajace sie po zakonceniu gry, przegranej lub wygranej .
 *
 * @field punkty ilosc pktow zdobyta
 *  @filed wyniki wektor zawierajacy najlepsze dotychczasowe wyniki
 *  @filed plik plik konfiguracyjny zawierajacy liste najlepszych wynikow
 */
public class KoniecGry extends JFrame implements ActionListener
{
	private JLabel lWiadomosc;
	private JButton bOkej;
	private int punkty;
	private JLabel lPunkty;
	private JTextField tNick;
	private Vector<HighScore> wyniki = new Vector<>();
	private JLabel lNick;
	private String nick;
	private File plik;
	
	/**
     * Konstruktor tworzacy okno na podstawie pliku konfiguracyjnego i ilosci zdobytych punktow  .
     *
     * @param plik  plik konfiguracyjny 
     * @param punkty ilosc zdobytych w danej rogrywce punktow
     */
	public KoniecGry(int punkty, File plik)
	{
		this.punkty=punkty;
		this.plik = plik;
		
		setTitle("Koniec Gry!");
			
		setSize(255,327);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() 
		{

			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				MenuGlowne okienko = new MenuGlowne();
				okienko.setVisible(true);
				}
			
		});
		
		bOkej = new JButton("OK");
		bOkej.setLocation(74, 212);
		bOkej.setSize(100, 60);
		getContentPane().setLayout(null);
		
		lWiadomosc = new JLabel("Koniec Gry!");
		lWiadomosc.setBounds(24, 24, 200, 33);
		lWiadomosc.setHorizontalAlignment(SwingConstants.CENTER);
		lWiadomosc.setFont(new Font("SanSerif",Font.BOLD,25));
		getContentPane().add(lWiadomosc);
		getContentPane().add(bOkej);
		
		lPunkty = new JLabel("Twój wynik: " + punkty + " punktów.");// wyswietlanie wyniku 
		lPunkty.setBounds(47, 68, 155, 14);
		getContentPane().add(lPunkty);
		
		tNick = new JTextField();
		tNick.setBounds(74, 153, 100, 20);
		getContentPane().add(tNick);
		tNick.setColumns(10);
		tNick.addActionListener(this);
		
		lNick = new JLabel("Podaj swï¿½j nick");
		lNick.setBounds(74, 128, 100, 14);
		getContentPane().add(lNick);
		
		bOkej.addActionListener(this);
		
		
		
		

	}
	/**
	 * metoda wczytajaca z pliku konfiguracyjnego listï¿½ wynikow 
	 * @param plik konfiguracyjny
	 */
	
	private void Wczytaj(File plik) throws IOException
	{
		try (BufferedReader br = new BufferedReader(new FileReader(plik))) {
			String line = br.readLine();
			while (line != null)
			{
				String[] wyniki = line.split("\\s+");
				nick = wyniki[0];
				punkty = Integer.parseInt(wyniki[1]);
				this.wyniki.add(new HighScore(nick, punkty));
				line = br.readLine();
			}
		}
		catch (IOException e) {
			// TODO: handle exception
		}
	}
	/**
	 * metoda zapisujaca wyniki(wraz z naszym nowo dodanym jesli jest wystarcajaco dobry) do pliku konfiguracyjnego 
	 * @param plik konfiguracyjny
	 */
	private void Zapis(File plik) throws IOException
	{
		PrintWriter out = new PrintWriter(plik);
		for(int x=0; x <= wyniki.size() - 1; x++)
		{
			out.println(wyniki.get(x).getNick() + " " + wyniki.get(x).getPunkty());
		}
		out.close();
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		Object zrodlo = e.getSource();
		if(zrodlo==bOkej || zrodlo==tNick)
		{
			String Nick = tNick.getText();
			System.out.println(Nick);
			if(Nick.contains(" "))
			{
				String[] temp = Nick.split("\\s+");
				Nick = String.join("", temp);
			}
			System.out.println(Nick);
			if(Nick.contains("-"))
			{
				String[] temp = Nick.split("-");
				Nick = String.join("", temp);
			}
			wyniki.add(new HighScore(tNick.getText(), punkty));
			try {
				Wczytaj(plik);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for(int x=0;x<=wyniki.size()-1;x++)
			{
				System.out.println(wyniki.get(x).getNick() + wyniki.get(x).getPunkty());
			}
			try {
				Zapis(plik);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			dispose();
			MenuGlowne okienko = new MenuGlowne();
		}
		
	}
}
	
