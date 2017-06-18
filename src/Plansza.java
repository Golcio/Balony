import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import java.util.Vector;

/**
 * Created by user on 2017-06-11.
 */
public class Plansza extends JFrame implements ActionListener/* , Runnable */ {

	private JButton WyjdzButton;
	private JToggleButton PauzaToggleButton;
	private JPanel contentPane;
	private Image img;

	private int WYSOKOSC, SZEROKOSC;
	private Vector<Polozenie> polozenia = new Vector<>();
	private Vector<Balon> balonyNaPlanszy = new Vector<>();
	private Vector<Balon> pociski = new Vector<>();
	private Properties pola = new Properties();

	double proporcjaX;
	double proporcjaY;

	double droga;
	Balon pocisk;
	Balon balonik;

	double PRZESUNIECIE = 15;
	double PRZESUNIECIEX;
	double PRZESUNIECIEY;

	int przesuniecieWPoziomie;
	int przesuniecieWPionie;
	int czas = 15;
	private Timer tm;
	boolean stoper = false;
	boolean active = true;
	boolean klik = true;
	
	double ilebalonow=0;
	int licznikspadania=0;
	int punkty=0;
	private JLabel lPunkty;
	private JLabel lPunktyKarne;
	/* private Thread th; */

	/**
	 * Create the frame.
	 */
	public Plansza(File plikStartowy) throws IOException {

		Wczytaj(plikStartowy);
		setTitle("Gra Balony");
		setLocationRelativeTo(null);
		pocisk = new Balon(getKolor(99), new Polozenie((getWidth() / 2), getHeight() - 90));
		pociski.add(pocisk);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		//setBounds(100, 100, getWidth(), getHeight());
		contentPane = new JPanel();

		class TimeListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(active)
				{
				if (!stoper) {
					modyfikacjaPolozenia();
					repaint();
				}
					if(CzyPrzegrana())
					{
						tm.stop();
						dispose();
						KoniecGry przegryw = new KoniecGry(false, punkty);
						przegryw.setVisible(true);
					}
				}

			}
		}

		ActionListener listener = new TimeListener();
		tm = new Timer(czas, listener);
		tm.start();

		/*
		 * th = new Thread(this); th.start();
		 */

		MouseListenerPlansza();

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		Box verticalBox = Box.createHorizontalBox();
		contentPane.add(verticalBox, BorderLayout.SOUTH);

		
		 JToggleButton PauzaToggleButton = new JToggleButton("Pauza");
		 PauzaToggleButton.addActionListener(new ActionListener() { 
		 public void actionPerformed(ActionEvent e) { 
			 if(PauzaToggleButton.isSelected())
			 {
				 active=false;
				 klik=false;
			 }
			 else
			 {
				 active = true;
				 klik=true;
			 }
		 } });
		 

		JSeparator separator_1 = new JSeparator();
		verticalBox.add(separator_1);
		verticalBox.add(PauzaToggleButton);

		JSeparator separator = new JSeparator();
		verticalBox.add(separator);

		WyjdzButton = new JButton("Wyjdz");

		
		
		/**
		 * Metoda obsï¿½ugujaca zdarzenie wcisniecia przycisku.
		 *
		 * @param e
		 *            przycisniecie przycisku
		 */
		WyjdzButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int x = JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz wyjœæ?", "Hola hola!", JOptionPane.YES_NO_OPTION);
				if(x == JOptionPane.YES_OPTION)
				{
				tm.stop();
				dispose();
				MenuGlowne okienko = new MenuGlowne();
				okienko.setVisible(true);
				}
				else if(x == JOptionPane.NO_OPTION)
				{
					JOptionPane.showMessageDialog(null, "Dobra decyzja!");
				}
				else if(x == JOptionPane.CLOSED_OPTION)
				{
					
					JOptionPane.showMessageDialog(null, "Panie, co to za iksowanie?!","Nie³adnie!", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		verticalBox.add(WyjdzButton);

		JSeparator separator_2 = new JSeparator();
		verticalBox.add(separator_2);
		
		lPunkty = new JLabel("Punkty: " + punkty);
		lPunkty.setFont(new Font("SanSerif",Font.BOLD,15));
		verticalBox.add(lPunkty);
		
		JSeparator separator_3 = new JSeparator();
		verticalBox.add(separator_3);
		
		lPunktyKarne = new JLabel("Punkty Karne: " + licznikspadania);
		lPunktyKarne.setFont(new Font("SanSerif",Font.BOLD,15));
		verticalBox.add(lPunktyKarne);

		/**
		 * Metoda obsï¿½ugujaca zdarzenie wcisniecia przycisku X.
		 *
		 * @param e
		 *            przycisniecie przycisku
		 */
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int x = JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz wyjœæ?", "Hola hola!", JOptionPane.YES_NO_OPTION);
				if(x == JOptionPane.YES_OPTION)
				{
				tm.stop();
				dispose();
				MenuGlowne okienko = new MenuGlowne();
				okienko.setVisible(true);
				}
				else if(x == JOptionPane.NO_OPTION)
				{
					JOptionPane.showMessageDialog(null, "Dobra decyzja!");
				}
				else if(x == JOptionPane.CLOSED_OPTION)
				{
					
					JOptionPane.showMessageDialog(null, "Panie, co to za iksowanie?!","Nie³adnie!", JOptionPane.WARNING_MESSAGE);
				}
			}

		});
	}

	public void MouseListenerPlansza() {
		contentPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(klik)
				{
				super.mouseClicked(e);
				System.out.println(e.getPoint());

				Polozenie gdzieKliknieto = new Polozenie(e.getX() + 30, e.getY() + 60);
				Polozenie polozenieWyrzutni = new Polozenie(pociski.lastElement().getAktualnePolozenia().getWsplX(),
						pociski.lastElement().getAktualnePolozenia().getWsplY());
				przesuniecieWPoziomie = gdzieKliknieto.getWsplX() - polozenieWyrzutni.getWsplX();
				przesuniecieWPionie = gdzieKliknieto.getWsplY() - polozenieWyrzutni.getWsplY();
				droga = Math.sqrt(
						przesuniecieWPionie * przesuniecieWPionie + przesuniecieWPoziomie * przesuniecieWPoziomie);
				// System.out.println("droga przesuniecieX przesuniecieY " +
				// droga + przesuniecieWPoziomie + przesuniecieWPionie);
				proporcjaX = przesuniecieWPoziomie / droga;
				proporcjaY = przesuniecieWPionie / droga;
				PRZESUNIECIEX = Math.abs(PRZESUNIECIE * proporcjaX);
				PRZESUNIECIEY = Math.abs(PRZESUNIECIE * proporcjaY);

				stoper = false;
				klik=false;
				}
			}
		});
	}

	/**
	 * Metoda wczytuje wymiary planszy oraz informacje o balonach z pliku
	 * konfiguracyjnego
	 *
	 * @param plikStartowy
	 *            bieï¿½ï¿½ca linijka
	 * @throws IOException
	 *             jeï¿½eli nie uda siï¿½ otworzyc pliku
	 */
	private void Wczytaj(File plikStartowy) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(plikStartowy))) {
			String line = br.readLine();
			while (line != null) {
				if (line.contains("WYMIARY")) {
					String[] wymiaryString = line.split("\\s+");
					SZEROKOSC = Integer.parseInt(wymiaryString[1]);
					WYSOKOSC = Integer.parseInt(wymiaryString[2]);

					setSize(SZEROKOSC * 60, WYSOKOSC * 60);
					StworzPustaPlansze(SZEROKOSC, WYSOKOSC);
					line = br.readLine();
				} else {
					try {
						line = WczytajBalony(br, line);
					} catch (NullPointerException e) {
						break;
					}
				}

			}

		}

	}

	/**
	 * Metoda tworzy pusta plansze o podanych wymiarach.
	 *
	 * @param WYSOKOSC
	 *            planszy w ilosci rzedï¿½w balonï¿½w
	 * @param SZEROKOSC
	 *            planszy w ilosci rzedï¿½w balonï¿½w
	 */

	private void StworzPustaPlansze(int WYSOKOSC, int SZEROKOSC) {
		for (int i = 0; i < WYSOKOSC; i++) {
			for (int j = 0; j < SZEROKOSC; j++) {

				Polozenie wspolrzedne = new Polozenie(j, i);
				polozenia.add(wspolrzedne);
				pola.put(wspolrzedne, new Balon());
			}
		}
	}

	/**
	 * Metoda wczytuje poï¿½ozenie balonï¿½w z pliku kofiguracyjnego.
	 *
	 * @param line
	 *            bieï¿½ï¿½ca linijka
	 * @param br
	 *            bufor czytnika
	 * @return bierzaca linie
	 * @throws IOException
	 *             jeï¿½eli nie uda siï¿½ odczytaï¿½ kolejnej linijki
	 */
	private String WczytajBalony(BufferedReader br, String line) throws IOException {

		if (line.contains("#"))
			line = br.readLine();
		else {
			WczytajPole(line);
			line = br.readLine();
		}

		return line;
	}

	/**
	 * Metoda wczytuje z odczytanej lini pliku poï¿½ozenie i kolor balona a
	 * obiektu klasy Plansza.
	 *
	 * @param line
	 *            bieï¿½ï¿½ca linijka pliku nad ktora pracuje metoda
	 */

	private void WczytajPole(String line) {

		String[] balonString = line.split("\\s+");
		try {
			int wsplX = Integer.parseInt(balonString[0]);
			int wsplY = Integer.parseInt(balonString[1]);
			int kolorInt = Integer.parseInt(balonString[2]);
			Kolor kolor;
			Polozenie wspolrzedneBalona = new Polozenie(wsplX, wsplY);
			kolor = getKolor(kolorInt);
			Balon balon = new Balon(kolor, wspolrzedneBalona);
			balonyNaPlanszy.add(balon);
			for (Polozenie p : polozenia) {
				if (p.equals(wspolrzedneBalona))
					wspolrzedneBalona = p;
				pola.replace(wspolrzedneBalona, balon);
			}

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("ERROR = ArrayIndexOutOfBoundsException in Wczytaj Pole");
		}
	}

	/**
	 * Metoda zwraca kolor na podstawie dostarczonego kodu numerycznego.
	 *
	 * @param kolorInt
	 *            kod koloru
	 * @return zwracany obiekt Kolor
	 */

	private Kolor getKolor(int kolorInt) {
		Kolor kolor;
		if (kolorInt == 99) {
			Random rand = new Random();
			kolorInt = rand.nextInt(4) + 1;

		}
		switch (kolorInt) {
		case 1:
			kolor = Kolor.ZIELONY;
			break;
		case 2:
			kolor = Kolor.CZERWONY;
			break;
		case 3:
			kolor = Kolor.NIEBIESKI;
			break;
		case 4:
			kolor = Kolor.ZOLTY;
			break;
		case 88:
			kolor = Kolor.CZARNY;
			break;
		case 69:
			kolor = Kolor.TECZOWY;
			break;
		default:
			kolor = Kolor.brak;

		}
		return kolor;
	}


	/**
	 * modyfikuje poÅ‚ozenie balonu-pocisku
	 */

	private void modyfikacjaPolozenia() {

		Polozenie nowePolozenie = pociski.lastElement().getAktualnePolozenia();

		if (pociski.lastElement().getAktualnePolozenia().getWsplX() >= 90
				&& pociski.lastElement().getAktualnePolozenia().getWsplX() <= (getWidth() - 90)) {
			if (przesuniecieWPoziomie < 0) {
				nowePolozenie.setWsplX((int) (pociski.lastElement().getAktualnePolozenia().getWsplX() - PRZESUNIECIEX));

			}
			if (przesuniecieWPoziomie > 0) {
				nowePolozenie.setWsplX((int) (pociski.lastElement().getAktualnePolozenia().getWsplX() + PRZESUNIECIEX));

			}
		}
		if (pociski.lastElement().getAktualnePolozenia().getWsplX() <= 90) {
			nowePolozenie.setWsplX(90);
			PRZESUNIECIEX = -1 * PRZESUNIECIEX;
		}

		if (pociski.lastElement().getAktualnePolozenia().getWsplX() >= getWidth() - 90) {
			nowePolozenie.setWsplX(getWidth() - 90);
			PRZESUNIECIEX = -1 * PRZESUNIECIEX;
		}

		if (pociski.lastElement().getAktualnePolozenia().getWsplY() >= 90
				&& pociski.lastElement().getAktualnePolozenia().getWsplY() <= getHeight() - 90) {
			if (przesuniecieWPionie < 0) {
				nowePolozenie.setWsplY((int) (pociski.lastElement().getAktualnePolozenia().getWsplY() - PRZESUNIECIEY));
			}
			if (przesuniecieWPionie > 0) {
				nowePolozenie.setWsplY((int) (pociski.lastElement().getAktualnePolozenia().getWsplY() + PRZESUNIECIEY));
			}

			if (pociski.lastElement().getAktualnePolozenia().getWsplY() >= getHeight() - 90) {
				nowePolozenie.setWsplY(getHeight() - 90);
				PRZESUNIECIEY = -1 * PRZESUNIECIEY;
			}

		}
		
		boolean czyMoznadalej = CzyDrogaWolna(nowePolozenie, balonyNaPlanszy);

		if (czyMoznadalej) 
		{
			pociski.lastElement().setAktualnePolozenia(nowePolozenie);
		} 
		else 
		{
			if (balonik.getAktualnePolozenia().getWsplY() - nowePolozenie.getWsplY() <= - 30 
					&& balonik.getAktualnePolozenia().getWsplY() - nowePolozenie.getWsplY() >= - 60
					&& balonik.getAktualnePolozenia().getWsplX() - nowePolozenie.getWsplX() <= 43 
					&& balonik.getAktualnePolozenia().getWsplX() - nowePolozenie.getWsplX() >= -43) 
			{
				pociski.lastElement().setAktualnePolozenia(new Polozenie(balonik.getAktualnePolozenia().getWsplX(),
						balonik.getAktualnePolozenia().getWsplY() + 60));
						System.out.println("dó³");
			} 
			else if (balonik.getAktualnePolozenia().getWsplY() - nowePolozenie.getWsplY() >= 30 
					&& balonik.getAktualnePolozenia().getWsplY() - nowePolozenie.getWsplY() <= 60
					&& balonik.getAktualnePolozenia().getWsplX() - nowePolozenie.getWsplX() <= 43 
					&& balonik.getAktualnePolozenia().getWsplX() - nowePolozenie.getWsplX() >= -43) 
			{
				pociski.lastElement().setAktualnePolozenia(new Polozenie(balonik.getAktualnePolozenia().getWsplX(),
						balonik.getAktualnePolozenia().getWsplY() - 60));
						System.out.println("góra");
			} 
			else if (balonik.getAktualnePolozenia().getWsplY() - nowePolozenie.getWsplY() >= - 43
					&& balonik.getAktualnePolozenia().getWsplY() - nowePolozenie.getWsplY() <= 43
					&& balonik.getAktualnePolozenia().getWsplX() - nowePolozenie.getWsplX() >= -60 
					&& balonik.getAktualnePolozenia().getWsplX() - nowePolozenie.getWsplX() <= - 30) 
			{
				pociski.lastElement().setAktualnePolozenia(new Polozenie(balonik.getAktualnePolozenia().getWsplX() + 60,
						balonik.getAktualnePolozenia().getWsplY()));
						System.out.println("prawo");
			} 
			else if (balonik.getAktualnePolozenia().getWsplY() - nowePolozenie.getWsplY() >= - 30 
					&& balonik.getAktualnePolozenia().getWsplY() - nowePolozenie.getWsplY() <= 30
					&& balonik.getAktualnePolozenia().getWsplX() - nowePolozenie.getWsplX() <= 60 
					&& balonik.getAktualnePolozenia().getWsplX() - nowePolozenie.getWsplX() >= 30) 
			{
				pociski.lastElement().setAktualnePolozenia(new Polozenie(balonik.getAktualnePolozenia().getWsplX() - 60,
						balonik.getAktualnePolozenia().getWsplY()));
						System.out.println("lewo");
			}
			else {
				{
					System.out.println("pozosta³e");
					System.out.println("Y: " + (balonik.getAktualnePolozenia().getWsplY() - nowePolozenie.getWsplY()));
					System.out.println("X: " + (balonik.getAktualnePolozenia().getWsplX() - nowePolozenie.getWsplX()));
					
					
					
				}
			}
			
			if(nowePolozenie.getWsplY() <= 90)
			{
				nowePolozenie.setWsplY(90);
				for(int x = 1; x<= SZEROKOSC-1; x++)
				{
					if(nowePolozenie.getWsplX() - (x*60 + 30) >= -30 && nowePolozenie.getWsplX() - (x*60 + 30) < 30)
					{
						nowePolozenie.setWsplX(x*60 + 30);
					}
				}
			}
			stoper = true;
			
			ZnikanieBalonów(pociski.lastElement());
			System.out.println("ile: " + ilebalonow);
			if(ilebalonow==2)
			{
				System.out.println("if");
				punkty+=10*(ilebalonow+1);
			}
			else if(ilebalonow>=3)
			{
				System.out.println("else");
				punkty+=10*(ilebalonow+1)*(1 + ((ilebalonow+1)/10));
			}
			lPunkty.setText("Punkty: " + punkty);
			ilebalonow=0;
			if(pociski.lastElement().isCzyIstnieje()==true)
			{
			balonyNaPlanszy.add(pociski.lastElement());
			licznikspadania++;
			lPunktyKarne.setText("Punkty Karne: " + licznikspadania);
			System.out.println("ile: " + ilebalonow);
			
			}
			
			if(licznikspadania==3)
			{
				for(int x = balonyNaPlanszy.size()-1; x>= 0; x--)
				{
					balonyNaPlanszy.get(x).setAktualnePolozenia(new Polozenie(balonyNaPlanszy.get(x).getAktualnePolozenia().getWsplX(), balonyNaPlanszy.get(x).getAktualnePolozenia().getWsplY()+60));
				}
				for(int x = 1; x<= SZEROKOSC - 2; x++)
				{
					Balon pomocniczy = new Balon(getKolor(99), new Polozenie(x*60 + 30, 90));
					balonyNaPlanszy.add(pomocniczy);
				}
				licznikspadania=0;
				lPunktyKarne.setText("Punkty Karne: " + licznikspadania);
			}
			
			pociski.clear();
			pocisk = new Balon(getKolor(99), new Polozenie((getWidth() / 2), getHeight() - 90));
			pociski.add(pocisk);
		}

	}

	private boolean CzyDrogaWolna(Polozenie nowePolozenie, Vector<Balon> balonyNaPlanszy) 
	{
		
		if(nowePolozenie.getWsplY() <= 90)
		{
			return false;
		}

		for (Balon b : balonyNaPlanszy) 
		{
			double odleglosc = Math.sqrt((b.getAktualnePolozenia().getWsplX() - nowePolozenie.getWsplX())
					* (b.getAktualnePolozenia().getWsplX() - nowePolozenie.getWsplX())
					+ (b.getAktualnePolozenia().getWsplY() - nowePolozenie.getWsplY())
							* (b.getAktualnePolozenia().getWsplY() - nowePolozenie.getWsplY()));
			if (odleglosc <= 50)
			{
				klik=true;
				balonik = b;
				return false;

			}
		}
		return true;

	}
	
	
	private void ZnikanieBalonów(Balon balon) 
	{
		for (int x = balonyNaPlanszy.size() - 1; x >= 0; x--) 
		{
			try 
			{
				double odleglosc = Math.sqrt((balonyNaPlanszy.get(x).getAktualnePolozenia().getWsplX()
						- balon.getAktualnePolozenia().getWsplX())
						* (balonyNaPlanszy.get(x).getAktualnePolozenia().getWsplX() - balon.getAktualnePolozenia().getWsplX())
						+ (balonyNaPlanszy.get(x).getAktualnePolozenia().getWsplY() - balon.getAktualnePolozenia().getWsplY())
								* (balonyNaPlanszy.get(x).getAktualnePolozenia().getWsplY() - balon.getAktualnePolozenia().getWsplY()));

				if (odleglosc == 60 && balonyNaPlanszy.get(x).getKolor() == balon.getKolor()) 
				{
					if(balonyNaPlanszy.elementAt(x).isCzyIstnieje()==true)
					{
					balon.setCzyIstnieje(false);
					balonyNaPlanszy.elementAt(x).setCzyIstnieje(false);
					ilebalonow++;
					ZnikanieBalonów(balonyNaPlanszy.elementAt(x));
					}
				}
			} 
			catch (ArrayIndexOutOfBoundsException e) 
			{
				//break;
			}
		}
		
		for (int x = balonyNaPlanszy.size() - 1; x >= 0; x--)
		{
			if(balonyNaPlanszy.elementAt(x).isCzyIstnieje()==false)
			{
				if(ilebalonow>=2)
				{
				balonyNaPlanszy.remove(x);
				}
				else
				{
					balonyNaPlanszy.elementAt(x).setCzyIstnieje(true);
					pociski.lastElement().setCzyIstnieje(true);
				}
			}
			
		}
		
	}
	

	private boolean CzyPrzegrana()
	{
		for(int x = balonyNaPlanszy.size() - 1; x>=0; x--)
		{
			if(balonyNaPlanszy.get(x).getAktualnePolozenia().getWsplY() >= getHeight() - 90)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * usypia watek na n ms
	 */

	private void Sleeeep(int n) {
		try {
			Thread.sleep(n);
		} catch (InterruptedException e1) {
			System.out.println("InterruptedException");
		}
	}

	/**
	 * maluje komponent planszy gry
	 *
	 * @param g
	 *            kontekst graficzny
	 */

	public void paintComponent(Graphics g) {
		super.paintComponents(g);

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, SZEROKOSC * 60, WYSOKOSC * 60 - 45);
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, SZEROKOSC * 60, 60);
		g.fillRect(0, 0, 60, WYSOKOSC * 60 - 45);
		g.fillRect(SZEROKOSC * 60 - 60, 0, 60, WYSOKOSC * 60 - 45);
		g.fillRect(0, WYSOKOSC * 60 - 60, SZEROKOSC * 60, 15);
		g.setColor(Color.RED);
		g.drawLine(60, WYSOKOSC*60 - 120, SZEROKOSC*60 - 60, WYSOKOSC*60 - 120);
		for (Balon b : balonyNaPlanszy) {
			switch (b.getKolor()) {
			case ZOLTY:
				g.setColor(Color.YELLOW);
				b.setObrazekBalonu(img = new ImageIcon("zolty.png").getImage());
				break;
			case CZERWONY:
				g.setColor(Color.RED);
				b.setObrazekBalonu(img = new ImageIcon("czerwony.png").getImage());
				break;
			case ZIELONY:
				g.setColor(Color.GREEN);
				b.setObrazekBalonu(img = new ImageIcon("zielony.png").getImage());
				break;
			case NIEBIESKI:
				g.setColor(Color.BLUE);
				b.setObrazekBalonu(img = new ImageIcon("niebieski.png").getImage());
				break;
			case CZARNY:
				g.setColor(Color.BLACK);
				b.setObrazekBalonu(img = new ImageIcon("czarny.png").getImage());
				break;
			case TECZOWY:
				g.setColor(Color.PINK);
				b.setObrazekBalonu(img = new ImageIcon("rozowy.png").getImage());
				break;
			default:
				g.setColor(Color.WHITE);

			}
			/*if (g.getColor() != Color.WHITE) {*/
				// g.fillOval(p.getWsplX() * 60, p.getWsplY() * 60, 60, 60);
				g.drawImage(b.getObrazekBalonu(), b.getAktualnePolozenia().getWsplX() - 30,
						b.getAktualnePolozenia().getWsplY() - 30, null);
			//}
		}

		// g.setColor(Color.black);

		// g.fillOval(polozenieNaboju.getWsplX() * 1, polozenieNaboju.getWsplY()
		// * 1, 60, 60);

		for (Balon b : pociski) {
			switch (b.getKolor()) {
			case ZOLTY:
				g.setColor(Color.YELLOW);
				b.setObrazekBalonu(img = new ImageIcon("zolty.png").getImage());
				break;
			case CZERWONY:
				g.setColor(Color.RED);
				b.setObrazekBalonu(img = new ImageIcon("czerwony.png").getImage());
				break;
			case ZIELONY:
				g.setColor(Color.GREEN);
				b.setObrazekBalonu(img = new ImageIcon("zielony.png").getImage());
				break;
			case NIEBIESKI:
				g.setColor(Color.BLUE);
				b.setObrazekBalonu(img = new ImageIcon("niebieski.png").getImage());
				break;
			case CZARNY:
				g.setColor(Color.BLACK);
				b.setObrazekBalonu(img = new ImageIcon("czarny.png").getImage());
				break;
			case TECZOWY:
				g.setColor(Color.PINK);
				b.setObrazekBalonu(img = new ImageIcon("rozowy.png").getImage());
				break;
			default:
				g.setColor(Color.WHITE);

			}
			if (g.getColor() != Color.WHITE) {
				// g.fillOval(p.getWsplX() * 60, p.getWsplY() * 60, 60, 60);
				g.drawImage(b.getObrazekBalonu(), b.getAktualnePolozenia().getWsplX() - 30,
						b.getAktualnePolozenia().getWsplY() - 30, null);
			}
		}

		g.dispose();
		setFocusable(true);

	}

	public void paint(Graphics g) {
		BufferedImage dbImage = new BufferedImage(SZEROKOSC * 60, WYSOKOSC * 60, BufferedImage.TYPE_INT_ARGB);
		Graphics dbg = dbImage.getGraphics();
		paintComponent(dbg);

		BufferedImage scaled = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D gg = scaled.createGraphics();
		gg.drawImage(dbImage, 0, 0, getWidth(), getHeight(), null);
		g.drawImage(scaled, 0, 0, this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
