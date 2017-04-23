import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;


/**
 * Klasa okna planszy gry
 *
 */
public class Plansza extends JFrame implements ActionListener
{
	private int WYSOKOSC, SZEROKOSC;
	private Timer tm = new Timer(5, this);
	int x = 1, velX = 3;
	int y = 1, velY = 2;
	private JMenuBar menuBar;
	private JMenuItem wyjdz;
	private JMenuItem pauza;
	private JPanel plansza;
	private Properties pola = new Properties();
	private Vector<Polozenie> polozenia= new Vector<>();
	/**
	 * Konstruktor wczytujï¿½cy dane planszy gry z pliku konfiguracyjnego.
	 *
	 * @param plikStartowy plik tekstowy (.txt) z parametrami konfiguracyjnymi w ustalonym formacie
	 * @throws IOException jeï¿½eli nie bï¿½dzie moï¿½na nawiï¿½zaï¿½ poï¿½ï¿½czenia
	 */

	public Plansza(File plikStartowy) throws IOException
	{
		Wczytaj(plikStartowy);
		setTitle("Balony");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		

		this.addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e) 
			{
				dispose();
				MenuGlowne okienko = new MenuGlowne();
			}
		});

		/*setLayout(new BorderLayout());
		menuBar = new JMenuBar();
		setJMenuBar(this.menuBar);
		wyjdz = new JMenuItem("wyjdz");
		wyjdz.addActionListener(this);
		pauza = new JMenuItem("pauza");
		plansza = new JPanel();
		plansza.setSize(this.getSize());
		plansza.setBackground(Color.WHITE);
		plansza.setVisible(true);
		plansza.setFocusable(false);
		add(plansza, BorderLayout.CENTER);
		menuBar.add(wyjdz);
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(pauza);
		add(menuBar, BorderLayout.PAGE_START);*/
		setVisible(true);
		//menuBar.setVisible(true);
		//plansza.createBufferStrategy(3);
		//plansza.setMaximumSize(this.getMaximumSize());
		

		/*boolean running = true;

		BufferStrategy bufferStrategy;
		Graphics graphics;

		while (running) 
		{
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
			graphics.fillOval(x*30, 0, 30, 30 );
			tm.start();


			bufferStrategy.show();
			graphics.dispose();
		}*/




	}

	public void paintComponent(Graphics g)
	{
		super.paintComponents(g);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, SZEROKOSC*60, WYSOKOSC*60);
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, SZEROKOSC*60, 60);
		g.fillRect(0, 0, 60, WYSOKOSC*60);
		g.fillRect(SZEROKOSC*60-60, 0, 60, WYSOKOSC*60);
		g.fillRect(0, WYSOKOSC*60-60, SZEROKOSC*60, 60);
		for (Polozenie p :polozenia)
		{
			Balon balon = (Balon)pola.get(p);
			switch (balon.getKolor())
			{
				case ZOLTY:g.setColor(Color.YELLOW); break;
				case CZERWONY:g.setColor(Color.RED); break;
				case ZIELONY:g.setColor(Color.GREEN); break;
				case NIEBIESKI:g.setColor(Color.BLUE); break;
				case CZARNY:g.setColor(Color.BLACK); break;
				case TECZOWY:g.setColor(Color.PINK); break;
				default:g.setColor(Color.WHITE);

			}
			if(g.getColor()!=Color.WHITE)
			g.fillOval(p.getWsplX()*60,p.getWsplY()*60,60,60);
		}
			g.setColor(Color.BLUE);
			g.fillOval((int)Math.ceil(SZEROKOSC/2)*60, WYSOKOSC*60 - 120, 60, 60);
		tm.start();
		
		g.dispose();
        setFocusable(true);
		
	}
	
	public void paint(Graphics g)
	{
		BufferedImage dbImage = new BufferedImage(SZEROKOSC*60, WYSOKOSC*60, BufferedImage.TYPE_INT_ARGB);
		Graphics dbg = dbImage.getGraphics();
		paintComponent(dbg);
		
		BufferedImage scaled = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D gg = scaled.createGraphics();
		gg.drawImage(dbImage, 0, 0, getWidth(), getHeight(), null);
		g.drawImage(scaled, 0, 0, this);
	}
	
	/**
	 * Metoda wczytuje wymiary planszy oraz informacje o balonach z pliku konfiguracyjnego
	 *
	 * @param plikStartowy bieï¿½ï¿½ca linijka
	 * @throws IOException jeï¿½eli nie uda siï¿½ otworzyc pliku
	 */
	private void Wczytaj(File plikStartowy) throws IOException {
		try(BufferedReader br = new BufferedReader(new FileReader(plikStartowy)))
		{
			String line =br.readLine();
			while (line != null)
			{
			if (line.contains("WYMIARY"))
			{
				String[] wymiaryString = line.split("\\s+");
				SZEROKOSC =Integer.parseInt(wymiaryString[1]);
				WYSOKOSC =Integer.parseInt(wymiaryString[2]);
				
				setSize(SZEROKOSC*60, WYSOKOSC*60);
				StworzPustaPlansze(SZEROKOSC, WYSOKOSC);
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
	 * Metoda wczytuje poï¿½ozenie balonï¿½w  z pliku kofiguracyjnego.
	 *
	 * @param line bieï¿½ï¿½ca linijka
	 * @param br   bufor czytnika
	 * @return bierzaca linie
	 * @throws IOException jeï¿½eli nie uda siï¿½ odczytaï¿½ kolejnej linijki
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
	 * @param WYSOKOSC planszy w ilosci rzedï¿½w balonï¿½w
	 * @param SZEROKOSC   planszy w ilosci rzedï¿½w balonï¿½w
	 */

	private void StworzPustaPlansze(int WYSOKOSC, int SZEROKOSC) {
		for (int i=0;i<WYSOKOSC;i++)
		{
			for (int j=0;j<SZEROKOSC;j++)
			{

				Polozenie wspolrzedne = new Polozenie(j,i);
				polozenia.add(wspolrzedne);
				pola.put(wspolrzedne,new Balon());
			}
		}
	}
	/**
	 * Metoda wczytuje z odczytanej lini pliku poï¿½ozenie i kolor balona a obiektu klasy Plansza.
	 *
	 * @param line bieï¿½ï¿½ca linijka pliku nad ktora pracuje metoda
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
		if(kolorInt == 99)
		{
			Random rand = new Random();
			kolorInt=rand.nextInt(4)+1;
			System.out.println(kolorInt);
		}
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
	 * Metoda obsï¿½ugujaca zdarzenie  wcisniecia przycisku.
	 *@param e przycisniecie przycisku
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		/*if(x<0 || x>270)
		velX=-velX;
		
		if(y<0 || y>270)
		velY=-velY;
			
		x = x + velX;
		y = y + velY;
		repaint();*/

		Object z = e.getSource();
		if(z == this.wyjdz) 
		{
			int odp = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz wyjœæ?", "Hola hola!", JOptionPane.YES_NO_OPTION);
			if(odp == JOptionPane.YES_OPTION)
			{
			dispose();
			MenuGlowne okienko = new MenuGlowne();
			}
			else if(odp == JOptionPane.NO_OPTION)
			{
				JOptionPane.showMessageDialog(this, "Dobra decyzja!", "Brawo!", JOptionPane.INFORMATION_MESSAGE);
			}
			else if(odp == JOptionPane.CLOSED_OPTION)
			{
				JOptionPane.showMessageDialog(this, "Panie, co to za iksowanie?!","Nie³adnie!", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	

	public static void main(String[] args)
	{
		
	}
}
