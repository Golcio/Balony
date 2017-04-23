import java.awt.*;
import java.awt.event.*;
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
	int wspolczynnikKierunku;
	Polozenie polozenieNaboju= new Polozenie((int)Math.ceil(SZEROKOSC/2)*60,WYSOKOSC*60 - 120);;
	private JMenuBar menuBar;
	private JMenuItem wyjdz;
	private JMenuItem pauza;
	private JPanel plansza;
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
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		boolean running = false;
/**
 * Ukryty bufor.
 */
		Image bufor = null;

		/**
		 * Kontekst graficzny bufora.
		 */
		Graphics kontekstBufora = null;

		

		this.addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e) 
			{
				dispose();
				MenuGlowne okienko = new MenuGlowne();
			}




			
		});
		this.addMouseListener(new MouseAdapter() {
			/**
			 * {@inheritDoc}
			 *
			 * @param e
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				System.out.println(e.getPoint());
				Polozenie gdzieKliknieto = new Polozenie(e.getX(),e.getY());
				Polozenie polozenieWyrzutni=new Polozenie(polozenieNaboju.getWsplX(),polozenieNaboju.getWsplY()) ;
				int PRZESUNIECIE = 10;

				int przesuniecieWPoziomie = gdzieKliknieto.getWsplX()-polozenieWyrzutni.getWsplX();
				int przesuniecieWPionie = gdzieKliknieto.getWsplX()-polozenieWyrzutni.getWsplX();
				 wspolczynnikKierunku = (int) przesuniecieWPionie/przesuniecieWPoziomie;
				boolean running = true;
				while (running)
				{
					addNotify();
					kontekstBufora.clearRect(0, 0, getWidth(), getHeight());
					int i = 0;

					modyfikacjaPolozenia(przesuniecieWPoziomie, PRZESUNIECIE);
					paintComponent(getGraphics());
					Sleeeep();
					i++;
						if(i>100)
							break;




				}

			}

			public void addNotify() {
				JPanel.super.addNotify();
				bufor = createImage(getPreferredSize().width,getPreferredSize().height);
				kontekstBufora = bufor.getDrawGraphics();
			}
		});


		setVisible(true);
		setResizable(true);
		//setMinimumSize(new Dimension(500,500));
		//setMaximumSize(new Dimension(800,800));





	}

	private void Sleeeep() {
		try {
            Thread.sleep(50);
        }catch (InterruptedException e1)
        {
            System.out.println("InterruptedException");
        }
	}

	private void modyfikacjaPolozenia(int przesuniecieWPoziomie, int PRZESUNIECIE) {
		if (przesuniecieWPoziomie<0)
            polozenieNaboju.setWsplX(polozenieNaboju.getWsplX()-PRZESUNIECIE);
		if (przesuniecieWPoziomie>0)
            polozenieNaboju.setWsplX(polozenieNaboju.getWsplX()-PRZESUNIECIE);

		polozenieNaboju.setWsplY(polozenieNaboju.getWsplY()+wspolczynnikKierunku*PRZESUNIECIE);
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

			g.setColor(Color.black);


		g.fillOval( polozenieNaboju.getWsplX(),polozenieNaboju.getWsplY() , 60, 60);
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
	 * @param plikStartowy bie��ca linijka
	 * @throws IOException je�eli nie uda si� otworzyc pliku
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
		if(kolorInt == 99)
		{
			Random rand = new Random();
			kolorInt=rand.nextInt(4)+1;

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
	 * Metoda obs�ugujaca zdarzenie  wcisniecia przycisku.
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
			int odp = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz wyj��?", "Hola hola!", JOptionPane.YES_NO_OPTION);
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
				JOptionPane.showMessageDialog(this, "Panie, co to za iksowanie?!","Nie�adnie!", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	


}
