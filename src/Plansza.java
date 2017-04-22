import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.*;

import javax.swing.*;
import javax.swing.Timer;

/**
 * Klasa okna planszy gry
 *
 */
public class Plansza extends JFrame implements ActionListener
{
	private Timer tm = new Timer(5, this);
	int x = 0, velX = 2;
	private JMenuBar menuBar;
	private JMenuItem wyjdz;
	private JMenuItem pauza;
	private Canvas plansza;
	private Properties pola = new Properties();
	private Vector<Polozenie> polozenia= new Vector<>();
	/**
	 * Konstruktor wczytuj�cy dane planszy gry z pliku konfiguracyjnego.
	 *
	 * @param plikStartowy plik tekstowy (.txt) z parametrami konfiguracyjnymi w ustalonym formacie
	 * @throws IOException je�eli nie b�dzie mo�na nawi�za� po��czenia
	 */

	public Plansza(File plikStartowy) throws IOException
	{
		Wczytaj(plikStartowy);
		setTitle("Balony");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		

		this.addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e) 
			{
				System.exit(0);
				MenuGlowne okienko = new MenuGlowne();
			}
		});

		this.setLayout(new BorderLayout());
		this.menuBar = new JMenuBar();
		this.setJMenuBar(this.menuBar);
		this.wyjdz = new JMenuItem("wyjdz");
		this.pauza = new JMenuItem("pauza");
		this.plansza = new Canvas();
		plansza.setSize(this.getSize());
		plansza.setBackground(Color.WHITE);
		plansza.setVisible(true);
		plansza.setFocusable(false);
		this.add(plansza, BorderLayout.CENTER);
		this.menuBar.add(this.wyjdz);
		this.menuBar.add(Box.createHorizontalGlue());
		this.menuBar.add(this.pauza);
		setVisible(true);
		this.menuBar.setVisible(true);
		this.wyjdz.setMaximumSize(this.getMaximumSize());
		this.pauza.setMaximumSize(this.getMaximumSize());
		plansza.createBufferStrategy(2);
		plansza.setMaximumSize(this.getMaximumSize());
		
		



		boolean running = true;

		BufferStrategy bufferStrategy;
		Graphics graphics;

		while (running) {
			bufferStrategy = plansza.getBufferStrategy();
			graphics = bufferStrategy.getDrawGraphics();
			graphics.clearRect(0, 0, this.getWidth(), this.getHeight());

			for (Polozenie p :polozenia)
			{
				Balon balon = (Balon)pola.get(p);
				switch (balon.getKolor())
				{
					case ZOLTY:graphics.setColor(Color.YELLOW); break;
					case CZERWONY:graphics.setColor(Color.RED); break;
					case ZIELONY:graphics.setColor(Color.GREEN); break;
					case NIEBIESKI:graphics.setColor(Color.BLUE); break;
					case CZARNY:graphics.setColor(Color.BLACK); break;
					case TECZOWY:graphics.setColor(Color.PINK); break;
					default:graphics.setColor(Color.WHITE);

				}

				graphics.fillOval(p.getWsplX()*30,p.getWsplY()*30,30,30);

			}
			Rectangle r = getBounds();
			graphics.setColor(Color.BLUE);
			graphics.fillOval(x, 0, 30, 30 );
			tm.start();


			bufferStrategy.show();
			graphics.dispose();
		}




	}

	/**
	 * Metoda wczytuje wymiary planszy oraz informacje o balonach z pliku konfiguracyjnego
	 *
	 * @param plikStartowy bie��ca linijka
	 * @throws IOException je�eli nie uda si� otworzyc pliku
	 */
	private void Wczytaj(File plikStartowy) throws IOException {
		int WYSOKOSC =3; int SZEROKOSC = 3 ;
		try(BufferedReader br = new BufferedReader(new FileReader(plikStartowy)))
		{
			String line =br.readLine();
			while (line != null)
			{
			if (line.contains("WYMIARY"))
			{
				String[] wymiaryString = line.split("\\s+");
				 WYSOKOSC =Integer.parseInt(wymiaryString[1]);
				SZEROKOSC =Integer.parseInt(wymiaryString[2]);
				
				setSize(SZEROKOSC*30 + 80, WYSOKOSC*30 + 80);
				StworzPustaPlansze(WYSOKOSC, SZEROKOSC);
				line =br.readLine();
			}
			else
			{
				try {
					line = WczytajBalony(br, line);
				}
				catch (NullPointerException e)
				{
					break;
				}
			}
			
			}
			
		}
	
	}

	/**
	 * Metoda wczytuje po�ozenie balon�w  z pliku kofiguracyjnego.
	 *
	 * @param line bie��ca linijka
	 * @param br   bufor czytnika
	 * @return bierzaca linie
	 * @throws IOException je�eli nie uda si� odczyta� kolejnej linijki
	 */
	private String WczytajBalony(BufferedReader br, String line) throws IOException {
        
		if(line.contains("#"))
        line = br.readLine();
        else
		{
			WczytajPole(line);
			line = br.readLine();
		}

		return line;
	}
	/**
	 * Metoda tworzy pusta plansze o podanych wymiarach.
	 *
	 * @param WYSOKOSC planszy w ilosci rzed�w balon�w
	 * @param SZEROKOSC   planszy w ilosci rzed�w balon�w
	 */

	private void StworzPustaPlansze(int WYSOKOSC, int SZEROKOSC) {
		for (int i=1;i<WYSOKOSC + 1;i++)
		{
			for (int j=1;j<SZEROKOSC + 1;j++)
			{

				Polozenie wspolrzedne = new Polozenie(j,i);
				polozenia.add(wspolrzedne);
				pola.put(wspolrzedne,new Balon());
			}
		}
	}
	/**
	 * Metoda wczytuje z odczytanej lini pliku po�ozenie i kolor balona a obiektu klasy Plansza.
	 *
	 * @param line bie��ca linijka pliku nad ktora pracuje metoda
	 *
	 */


	private void WczytajPole(String line)
	{

			String[] balonString = line.split("\\s+");
			try {
				int wsplX = Integer.parseInt(balonString[0]);
				int wsplY = Integer.parseInt(balonString[1]);
				int kolorInt = Integer.parseInt(balonString[2]);
				Kolor kolor;
				Polozenie wspolrzedneBalona = new Polozenie(wsplX, wsplY);
				kolor = getKolor(kolorInt);
				Balon balon = new Balon(kolor);
				for (Polozenie p : polozenia) {
					if (p.equals(wspolrzedneBalona))
						wspolrzedneBalona = p;
					pola.replace(wspolrzedneBalona, balon);
			}


			}catch (ArrayIndexOutOfBoundsException e)
			{}
	}
	
	/**
	 * Metoda zwraca kolor na podstawie dostarczonego kodu numerycznego.
	 *
	 * @param kolorInt kod koloru
	 * @return zwracany obiekt Kolor
	 *
	 */

	private Kolor getKolor(int kolorInt) {
		Kolor kolor;
		switch (kolorInt)
        {
            case 1: kolor=Kolor.ZIELONY;
            break;
            case 2: kolor=Kolor.CZERWONY;
                break;
            case 3: kolor=Kolor.NIEBIESKI;
                break;
            case 4: kolor=Kolor.ZOLTY;
                break;
            case 88: kolor=Kolor.CZARNY;
                break;
            case 69: kolor=Kolor.TECZOWY;
                break;
            default: kolor=Kolor.brak;

        }
		return kolor;
	}

	/**
	 * Metoda obs�ugujaca zdarzenie  wcisniecia przycisku.
	 *@param e przycisniecie przycisku
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		x = x + velX;

		/*Object z = e.getSource();
		if(z == this.wyjdz) {
			int plikKofiguracyjny = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz wyj��?", "Hola hola!", 0);
			if(plikKofiguracyjny == 0) {
				System.exit(0);
			} else if(plikKofiguracyjny == 1) {
				JOptionPane.showMessageDialog(this, "Dobra decyzja!", "Brawo!", 1);
			} else if(plikKofiguracyjny == -1) {
				JOptionPane.showMessageDialog(this, "Panie, co to za iksowanie?!", "Nie�adnie!", 2);
			}
		}*/
		
	}

	public static void main(String[] args)
	{
		
	}
}
