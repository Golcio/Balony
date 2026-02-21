package balony;
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
import java.util.Properties;
import java.util.Random;
import java.util.ArrayList;

/**
 * Created by user on 2017-06-11.
 */
public class Plansza extends JFrame implements ActionListener/* , Runnable */ {

	private JButton WyjdzButton;
	private JPanel contentPane;

	private static final Image IMG_ZOLTY = new ImageIcon("zolty.png").getImage();
	private static final Image IMG_CZERWONY = new ImageIcon("czerwony.png").getImage();
	private static final Image IMG_ZIELONY = new ImageIcon("zielony.png").getImage();
	private static final Image IMG_NIEBIESKI = new ImageIcon("niebieski.png").getImage();
	private static final Image IMG_CZARNY = new ImageIcon("czarny.png").getImage();
	private static final Image IMG_ROZOWY = new ImageIcon("rozowy.png").getImage();
	private static final Image IMG_BOMBA = new ImageIcon("bomba.png").getImage();

	private int WYSOKOSC, SZEROKOSC;
	private ArrayList<Polozenie> polozenia = new ArrayList<>();
	private ArrayList<Balon> balonyNaPlanszy = new ArrayList<>();
	private ArrayList<Balon> pociski = new ArrayList<>();
	private Properties pola = new Properties();

	private double proporcjaX;
	private double proporcjaY;

	private double droga;
	private Balon pocisk;
	private Balon balonik;

	private double PRZESUNIECIE = 15;
	private double PRZESUNIECIEX;
	private double PRZESUNIECIEY;

	private int przesuniecieWPoziomie;
	private int przesuniecieWPionie;
	private int czas = 15;
	private Timer tm;
	private boolean stoper = false;
	private boolean active = true;
	private boolean klik = true;

	private double ilebalonow = 0;
	private int licznikspadania = 0;
	private int punkty = 0;
	private JLabel lPunkty;
	private JLabel lPunktyKarne;
	private int trudnosc;
	private int poIluNowyRzad;
	private double mnoznik;

	/**
	 * glowne okno gry na ktorym jest rozgrywka
	 * 
	 * @param plikStartowy plik z wczytywaną mapą
	 * @param trudnosc     poziom trudności mapy
	 * @throws java.io.IOException wyjątek
	 */
	public Plansza(File plikStartowy, int trudnosc) throws IOException {

		this.trudnosc = trudnosc;

		Wczytaj(plikStartowy);
		setTitle("Gra Balony");
		setLocationRelativeTo(null);
		pocisk = new Balon(getKolor(99), new Polozenie((getWidth() / 2), getHeight() - 90));
		pociski.add(pocisk);
		pocisk = new Balon(getKolor(99), new Polozenie(getWidth() - 90, getHeight() - 90));
		pociski.add(pocisk);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		contentPane = new JPanel();

		class TimeListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (active) {
					if (!stoper) {
						modyfikacjaPolozenia();
						repaint();
					}
					if (CzyKoniec()) {
						tm.stop();
						dispose();
						if ("Pierwszy.txt".equals(plikStartowy.getPath())) {
							KoniecGry przegryw = new KoniecGry(punkty, new File("wyniki1.txt"));
							przegryw.setVisible(true);
						} else if ("Drugi.txt".equals(plikStartowy.getPath())) {
							KoniecGry przegryw = new KoniecGry(punkty, new File("wyniki2.txt"));
							przegryw.setVisible(true);
						} else if ("Trzeci.txt".equals(plikStartowy.getPath())) {
							KoniecGry przegryw = new KoniecGry(punkty, new File("wyniki3.txt"));
							przegryw.setVisible(true);
						}
					}
				}

			}
		}

		ActionListener listener = new TimeListener();
		tm = new Timer(czas, listener);
		tm.start();

		MouseListenerPlansza();

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		Box verticalBox = Box.createHorizontalBox();
		contentPane.add(verticalBox, BorderLayout.SOUTH);

		JToggleButton PauzaToggleButton = new JToggleButton("Pauza");
		PauzaToggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (PauzaToggleButton.isSelected()) {
					active = false;
					klik = false;
				} else {
					active = true;
					klik = true;
				}
			}
		});

		JSeparator separator_1 = new JSeparator();
		verticalBox.add(separator_1);
		verticalBox.add(PauzaToggleButton);

		JSeparator separator = new JSeparator();
		verticalBox.add(separator);

		WyjdzButton = new JButton("Wyjdz");

		/**
		 * Metoda obsďż˝ugujaca zdarzenie wcisniecia przycisku.
		 *
		 * @param e przycisniecie przycisku
		 */
		WyjdzButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int x = JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz wyjść˝?", "Hola hola!",
						JOptionPane.YES_NO_OPTION);
				if (x == JOptionPane.YES_OPTION) {
					tm.stop();
					dispose();
					MenuGlowne okienko = new MenuGlowne();
					okienko.setVisible(true);
				} else if (x == JOptionPane.NO_OPTION) {
					JOptionPane.showMessageDialog(null, "Dobra decyzja!");
				} else if (x == JOptionPane.CLOSED_OPTION) {

					JOptionPane.showMessageDialog(null, "Panie, co to za iksowanie?!", "Nieładnie!",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		verticalBox.add(WyjdzButton);

		JSeparator separator_2 = new JSeparator();
		verticalBox.add(separator_2);

		lPunkty = new JLabel("Punkty: " + punkty);
		lPunkty.setFont(new Font("SanSerif", Font.BOLD, 15));
		verticalBox.add(lPunkty);

		JSeparator separator_3 = new JSeparator();
		verticalBox.add(separator_3);

		lPunktyKarne = new JLabel("Punkty Karne: " + licznikspadania);
		lPunktyKarne.setFont(new Font("SanSerif", Font.BOLD, 15));
		verticalBox.add(lPunktyKarne);

		/**
		 * Metoda obsďż˝ugujaca zdarzenie wcisniecia przycisku X.
		 *
		 * @param e
		 *          przycisniecie przycisku
		 */
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int x = JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz wyjść˝?", "Hola hola!",
						JOptionPane.YES_NO_OPTION);
				if (x == JOptionPane.YES_OPTION) {
					tm.stop();
					dispose();
					MenuGlowne okienko = new MenuGlowne();
					okienko.setVisible(true);
				} else if (x == JOptionPane.NO_OPTION) {
					JOptionPane.showMessageDialog(null, "Dobra decyzja!");
				} else if (x == JOptionPane.CLOSED_OPTION) {

					JOptionPane.showMessageDialog(null, "Panie, co to za iksowanie?!", "Nieładnie!",
							JOptionPane.WARNING_MESSAGE);
				}
			}

		});
	}

	/**
	 * sĹ‚uchacz zdarzeĹ„ od myszy pozwalajacy na zbieranie informacji o
	 * tym gdzie kliknieto na lansze gry a tym samym sterowanie lotem pocisku
	 */
	public void MouseListenerPlansza() {
		contentPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (klik) {
					super.mouseClicked(e);

					Polozenie gdzieKliknieto = new Polozenie(e.getX() + 30, e.getY() + 60);
					Polozenie polozenieWyrzutni = new Polozenie(pociski.get(0).getAktualnePolozenia().getWsplX(),
							pociski.get(0).getAktualnePolozenia().getWsplY());
					przesuniecieWPoziomie = gdzieKliknieto.getWsplX() - polozenieWyrzutni.getWsplX();
					przesuniecieWPionie = gdzieKliknieto.getWsplY() - polozenieWyrzutni.getWsplY();
					droga = Math.sqrt(
							przesuniecieWPionie * przesuniecieWPionie + przesuniecieWPoziomie * przesuniecieWPoziomie);
					proporcjaX = przesuniecieWPoziomie / droga;
					proporcjaY = przesuniecieWPionie / droga;
					PRZESUNIECIEX = Math.abs(PRZESUNIECIE * proporcjaX);
					PRZESUNIECIEY = Math.abs(PRZESUNIECIE * proporcjaY);

					stoper = false;
					klik = false;
				}
			}
		});
	}

	/**
	 * Metoda wczytuje wymiary planszy oraz informacje o balonach z pliku
	 * konfiguracyjnego
	 *
	 * @param plikStartowy
	 *                     bieďż˝ďż˝ca linijka
	 * @throws IOException
	 *                     jeďż˝eli nie uda siďż˝ otworzyc pliku
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
	 *                  planszy w ilosci rzedďż˝w balonďż˝w
	 * @param SZEROKOSC
	 *                  planszy w ilosci rzedďż˝w balonďż˝w
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
	 * Metoda wczytuje poďż˝ozenie balonďż˝w z pliku kofiguracyjnego.
	 *
	 * @param line
	 *             bieďż˝ďż˝ca linijka
	 * @param br
	 *             bufor czytnika
	 * @return bierzaca linie
	 * @throws IOException
	 *                     jeďż˝eli nie uda siďż˝ odczytaďż˝ kolejnej linijki
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
	 * Metoda wczytuje z odczytanej lini pliku poďż˝ozenie i kolor balona a
	 * obiektu klasy Plansza.
	 *
	 * @param line
	 *             bieďż˝ďż˝ca linijka pliku nad ktora pracuje metoda
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
	 *                 kod koloru
	 * @return zwracany obiekt Kolor
	 */

	private Kolor getKolor(int kolorInt) {
		Kolor kolor;
		Random rand = new Random();
		if (kolorInt == 99) {
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
			case 5:
				kolor = Kolor.BOMBA;
				break;
			default:
				kolor = Kolor.brak;

		}
		return kolor;
	}

	/**
	 * modyfikuje poĹ‚ozenie balonu-pocisku
	 */

	private void modyfikacjaPolozenia() {

		Polozenie nowePolozenie = pociski.get(0).getAktualnePolozenia();

		if (pociski.get(0).getAktualnePolozenia().getWsplX() >= 90
				&& pociski.get(0).getAktualnePolozenia().getWsplX() <= (getWidth() - 90)) {
			if (przesuniecieWPoziomie < 0) {
				nowePolozenie.setWsplX((int) (pociski.get(0).getAktualnePolozenia().getWsplX() - PRZESUNIECIEX));

			}
			if (przesuniecieWPoziomie > 0) {
				nowePolozenie.setWsplX((int) (pociski.get(0).getAktualnePolozenia().getWsplX() + PRZESUNIECIEX));

			}
		}
		if (pociski.get(0).getAktualnePolozenia().getWsplX() <= 90) {
			nowePolozenie.setWsplX(90);
			PRZESUNIECIEX = -1 * PRZESUNIECIEX;
		}

		if (pociski.get(0).getAktualnePolozenia().getWsplX() >= getWidth() - 90) {
			nowePolozenie.setWsplX(getWidth() - 90);
			PRZESUNIECIEX = -1 * PRZESUNIECIEX;
		}

		if (pociski.get(0).getAktualnePolozenia().getWsplY() >= 90
				&& pociski.get(0).getAktualnePolozenia().getWsplY() <= getHeight() - 90) {
			if (przesuniecieWPionie < 0) {
				nowePolozenie.setWsplY((int) (pociski.get(0).getAktualnePolozenia().getWsplY() - PRZESUNIECIEY));
			}
			if (przesuniecieWPionie > 0) {
				nowePolozenie.setWsplY((int) (pociski.get(0).getAktualnePolozenia().getWsplY() + PRZESUNIECIEY));
			}

			if (pociski.get(0).getAktualnePolozenia().getWsplY() >= getHeight() - 90) {
				nowePolozenie.setWsplY(getHeight() - 90);
				PRZESUNIECIEY = -1 * PRZESUNIECIEY;
			}

		}

		boolean czyMoznadalej = CzyDrogaWolna(nowePolozenie, balonyNaPlanszy);

		if (czyMoznadalej) {
			pociski.get(0).setAktualnePolozenia(nowePolozenie);
		} else {
			if (balonik.getAktualnePolozenia().getWsplY() - nowePolozenie.getWsplY() <= -30
					&& balonik.getAktualnePolozenia().getWsplY() - nowePolozenie.getWsplY() >= -60
					&& balonik.getAktualnePolozenia().getWsplX() - nowePolozenie.getWsplX() <= 43
					&& balonik.getAktualnePolozenia().getWsplX() - nowePolozenie.getWsplX() >= -43) {
				pociski.get(0).setAktualnePolozenia(new Polozenie(balonik.getAktualnePolozenia().getWsplX(),
						balonik.getAktualnePolozenia().getWsplY() + 60));
			} else if (balonik.getAktualnePolozenia().getWsplY() - nowePolozenie.getWsplY() >= 20
					&& balonik.getAktualnePolozenia().getWsplY() - nowePolozenie.getWsplY() <= 60
					&& balonik.getAktualnePolozenia().getWsplX() - nowePolozenie.getWsplX() <= 43
					&& balonik.getAktualnePolozenia().getWsplX() - nowePolozenie.getWsplX() >= -43) {
				pociski.get(0).setAktualnePolozenia(new Polozenie(balonik.getAktualnePolozenia().getWsplX(),
						balonik.getAktualnePolozenia().getWsplY() - 60));
			} else if (balonik.getAktualnePolozenia().getWsplY() - nowePolozenie.getWsplY() >= -43
					&& balonik.getAktualnePolozenia().getWsplY() - nowePolozenie.getWsplY() <= 43
					&& balonik.getAktualnePolozenia().getWsplX() - nowePolozenie.getWsplX() >= -60
					&& balonik.getAktualnePolozenia().getWsplX() - nowePolozenie.getWsplX() <= -20) {
				pociski.get(0).setAktualnePolozenia(new Polozenie(balonik.getAktualnePolozenia().getWsplX() + 60,
						balonik.getAktualnePolozenia().getWsplY()));
			} else if (balonik.getAktualnePolozenia().getWsplY() - nowePolozenie.getWsplY() >= -43
					&& balonik.getAktualnePolozenia().getWsplY() - nowePolozenie.getWsplY() <= 43
					&& balonik.getAktualnePolozenia().getWsplX() - nowePolozenie.getWsplX() <= 60
					&& balonik.getAktualnePolozenia().getWsplX() - nowePolozenie.getWsplX() >= 30) {
				pociski.get(0).setAktualnePolozenia(new Polozenie(balonik.getAktualnePolozenia().getWsplX() - 60,
						balonik.getAktualnePolozenia().getWsplY()));
			} else {
				{
					System.out.println("pozostałe");
					System.out.println("Y: " + (balonik.getAktualnePolozenia().getWsplY() - nowePolozenie.getWsplY()));
					System.out.println("X: " + (balonik.getAktualnePolozenia().getWsplX() - nowePolozenie.getWsplX()));

				}
			}

			if (nowePolozenie.getWsplY() <= 90) {
				nowePolozenie.setWsplY(90);
				for (int x = 1; x <= SZEROKOSC - 1; x++) {
					if (nowePolozenie.getWsplX() - (x * 60 + 30) >= -30
							&& nowePolozenie.getWsplX() - (x * 60 + 30) < 30) {
						nowePolozenie.setWsplX(x * 60 + 30);
					}
				}
			}
			stoper = true;

			ZnikanieBalonow(pociski.get(0));
			Usuwanie();
			Bomba();

			if (trudnosc == 1) {
				poIluNowyRzad = 4;
				mnoznik = 1;
			} else if (trudnosc == 2) {
				poIluNowyRzad = 3;
				mnoznik = 1.25;
			} else if (trudnosc == 3) {
				poIluNowyRzad = 2;
				mnoznik = 1.5;
			}

			if (ilebalonow == 2) {
				punkty += 10 * (ilebalonow + 1) * mnoznik;
			} else if (ilebalonow >= 3) {
				punkty += 10 * (ilebalonow + 1) * (1 + ((ilebalonow + 1) / 10)) * mnoznik;
			}
			lPunkty.setText("Punkty: " + punkty);

			if (ilebalonow >= 4) {
				pociski.get(pociski.size() - 1).setKolor(getKolor(5));
			}

			ilebalonow = 0;
			if (pociski.get(0).isCzyIstnieje() == true) {
				balonyNaPlanszy.add(pociski.get(0));
				licznikspadania++;
				lPunktyKarne.setText("Punkty Karne: " + licznikspadania);

			}

			if (licznikspadania == poIluNowyRzad) {
				for (int x = balonyNaPlanszy.size() - 1; x >= 0; x--) {
					balonyNaPlanszy.get(x).setAktualnePolozenia(
							new Polozenie(balonyNaPlanszy.get(x).getAktualnePolozenia().getWsplX(),
									balonyNaPlanszy.get(x).getAktualnePolozenia().getWsplY() + 60));
				}
				for (int x = 1; x <= SZEROKOSC - 2; x++) {
					Balon pomocniczy = new Balon(getKolor(99), new Polozenie(x * 60 + 30, 90));
					balonyNaPlanszy.add(pomocniczy);
				}
				licznikspadania = 0;
				lPunktyKarne.setText("Punkty Karne: " + licznikspadania);
			}

			pociski.remove(pociski.get(0));
			pociski.get(0).setAktualnePolozenia(new Polozenie((getWidth() / 2), getHeight() - 90));
			Random rand = new Random();
			int los = rand.nextInt(10) + 1;
			if (los == 5) {
				int loslos = rand.nextInt(3) + 1;
				if (loslos == 2) {
					pocisk = new Balon(getKolor(88), new Polozenie(getWidth() - 90, getHeight() - 90));
				} else {
					pocisk = new Balon(getKolor(69), new Polozenie(getWidth() - 90, getHeight() - 90));
				}
			} else {
				pocisk = new Balon(getKolor(99), new Polozenie(getWidth() - 90, getHeight() - 90));
			}
			pociski.add(pocisk);
		}

	}

	/**
	 * Metoda sprawdzajaca czy oblicone nowe polozenie pocisku nie jest zajete
	 *
	 * @param nowePolozenie
	 *                        oblicone w modyfikacjaPolozenia()
	 * @param balonyNaPlanszy
	 *                        balony znajdujace sie aktualnie na planszy
	 * @return zwracana prawda albo fałsz
	 */
	private boolean CzyDrogaWolna(Polozenie nowePolozenie, ArrayList<Balon> balonyNaPlanszy) {

		if (nowePolozenie.getWsplY() <= 90) {
			klik = true;
			return false;
		}

		for (Balon b : balonyNaPlanszy) {
			double odleglosc = Math.sqrt((b.getAktualnePolozenia().getWsplX() - nowePolozenie.getWsplX())
					* (b.getAktualnePolozenia().getWsplX() - nowePolozenie.getWsplX())
					+ (b.getAktualnePolozenia().getWsplY() - nowePolozenie.getWsplY())
							* (b.getAktualnePolozenia().getWsplY() - nowePolozenie.getWsplY()));
			if (odleglosc <= 50) {
				klik = true;
				balonik = b;
				return false;

			}
		}
		return true;

	}

	/**
	 * metoda odpowiedzialna za oznaczenie balonów do usunięcia gdy balon pocisk
	 * zatrzyma sie przy skupisku wiecej niz 3 balonow swojego koloru
	 * 
	 * @param balon
	 *              balon-pocisk
	 */
	private void ZnikanieBalonow(Balon balon) {
		for (int x = balonyNaPlanszy.size() - 1; x >= 0; x--) {
			try {
				double odleglosc = Math.sqrt((balonyNaPlanszy.get(x).getAktualnePolozenia().getWsplX()
						- balon.getAktualnePolozenia().getWsplX())
						* (balonyNaPlanszy.get(x).getAktualnePolozenia().getWsplX()
								- balon.getAktualnePolozenia().getWsplX())
						+ (balonyNaPlanszy.get(x).getAktualnePolozenia().getWsplY()
								- balon.getAktualnePolozenia().getWsplY())
								* (balonyNaPlanszy.get(x).getAktualnePolozenia().getWsplY()
										- balon.getAktualnePolozenia().getWsplY()));

				if (odleglosc == 60 && balonyNaPlanszy.get(x).getKolor() == balon.getKolor()
						&& balon.getKolor() != getKolor(69) && balon.getKolor() != getKolor(5)) {
					if (balonyNaPlanszy.get(x).isCzyIstnieje() == true) {
						balon.setCzyIstnieje(false);
						balonyNaPlanszy.get(x).setCzyIstnieje(false);
						ilebalonow++;
						ZnikanieBalonow(balonyNaPlanszy.get(x));
					}
				}

				if (odleglosc == 60 && balon.getKolor() == getKolor(69)) {
					if (balonyNaPlanszy.get(x).isCzyIstnieje() == true) {
						balon.setCzyIstnieje(false);
						balonyNaPlanszy.get(x).setCzyIstnieje(false);
						ilebalonow++;
						ZnikanieBalonow(balonyNaPlanszy.get(x));
					}
				}

			} catch (ArrayIndexOutOfBoundsException e) {
				// break;
			}
		}

	}

	/**
	 * metoda odpowiedzialna za usuwanie oznaczonych przez funkcję ZnikanieBalonow
	 * balonów
	 */
	private void Usuwanie() {
		for (int x = balonyNaPlanszy.size() - 1; x >= 0; x--) {
			if (balonyNaPlanszy.get(x).isCzyIstnieje() == false) {
				if (ilebalonow >= 2) {
					balonyNaPlanszy.remove(x);
				} else {
					balonyNaPlanszy.get(x).setCzyIstnieje(true);
					pociski.get(0).setCzyIstnieje(true);
				}
			}

		}
	}

	/**
	 * Metoda implemetujaca specjalny balon-bombe ktory niszczy balony w okolicy gdy
	 * sie kolo nich zarzyma
	 *
	 *
	 */
	private void Bomba() {
		for (int x = balonyNaPlanszy.size() - 1; x >= 0; x--) {
			double odleglosc = Math.sqrt((balonyNaPlanszy.get(x).getAktualnePolozenia().getWsplX()
					- pociski.get(0).getAktualnePolozenia().getWsplX())
					* (balonyNaPlanszy.get(x).getAktualnePolozenia().getWsplX()
							- pociski.get(0).getAktualnePolozenia().getWsplX())
					+ (balonyNaPlanszy.get(x).getAktualnePolozenia().getWsplY()
							- pociski.get(0).getAktualnePolozenia().getWsplY())
							* (balonyNaPlanszy.get(x).getAktualnePolozenia().getWsplY()
									- pociski.get(0).getAktualnePolozenia().getWsplY()));

			if (odleglosc <= 85 && odleglosc >= 60 && pociski.get(0).getKolor() == getKolor(5)) {
				balonyNaPlanszy.remove(x);
				pociski.get(0).setCzyIstnieje(false);
				ilebalonow++;
			}
		}
	}

	/**
	 * sprawdzajaca czy balony nie sa juz ponizej dopuszczalnej granicy dolu planszy
	 * 
	 * @return zwraca prawdę albo fałsz
	 */
	private boolean CzyKoniec() {
		for (int x = balonyNaPlanszy.size() - 1; x >= 0; x--) {
			if (balonyNaPlanszy.get(x).getAktualnePolozenia().getWsplY() >= getHeight() - 90) {
				return true;
			}
		}
		return false;
	}

	/**
	 * maluje komponent planszy gry
	 *
	 * @param g
	 *          kontekst graficzny
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
		g.drawLine(60, WYSOKOSC * 60 - 120, SZEROKOSC * 60 - 60, WYSOKOSC * 60 - 120);
		for (Balon b : balonyNaPlanszy) {
			switch (b.getKolor()) {
				case ZOLTY:
					g.setColor(Color.YELLOW);
					b.setObrazekBalonu(IMG_ZOLTY);
					break;
				case CZERWONY:
					g.setColor(Color.RED);
					b.setObrazekBalonu(IMG_CZERWONY);
					break;
				case ZIELONY:
					g.setColor(Color.GREEN);
					b.setObrazekBalonu(IMG_ZIELONY);
					break;
				case NIEBIESKI:
					g.setColor(Color.BLUE);
					b.setObrazekBalonu(IMG_NIEBIESKI);
					break;
				case CZARNY:
					g.setColor(Color.BLACK);
					b.setObrazekBalonu(IMG_CZARNY);
					break;
				case TECZOWY:
					g.setColor(Color.PINK);
					b.setObrazekBalonu(IMG_ROZOWY);
					break;
				case BOMBA:
					g.setColor(Color.DARK_GRAY);
					b.setObrazekBalonu(IMG_BOMBA);
					break;
				default:
					g.setColor(Color.WHITE);

			}
			/* if (g.getColor() != Color.WHITE) { */
			// g.fillOval(p.getWsplX() * 60, p.getWsplY() * 60, 60, 60);
			g.drawImage(b.getObrazekBalonu(), b.getAktualnePolozenia().getWsplX() - 30,
					b.getAktualnePolozenia().getWsplY() - 30, null);
			// }
		}

		// g.setColor(Color.black);

		// g.fillOval(polozenieNaboju.getWsplX() * 1, polozenieNaboju.getWsplY()
		// * 1, 60, 60);

		for (Balon b : pociski) {
			switch (b.getKolor()) {
				case ZOLTY:
					g.setColor(Color.YELLOW);
					b.setObrazekBalonu(IMG_ZOLTY);
					break;
				case CZERWONY:
					g.setColor(Color.RED);
					b.setObrazekBalonu(IMG_CZERWONY);
					break;
				case ZIELONY:
					g.setColor(Color.GREEN);
					b.setObrazekBalonu(IMG_ZIELONY);
					break;
				case NIEBIESKI:
					g.setColor(Color.BLUE);
					b.setObrazekBalonu(IMG_NIEBIESKI);
					break;
				case CZARNY:
					g.setColor(Color.BLACK);
					b.setObrazekBalonu(IMG_CZARNY);
					break;
				case TECZOWY:
					g.setColor(Color.PINK);
					b.setObrazekBalonu(IMG_ROZOWY);
					break;
				case BOMBA:
					g.setColor(Color.DARK_GRAY);
					b.setObrazekBalonu(IMG_BOMBA);
					break;
				default:
					g.setColor(Color.WHITE);

			}
			if (g.getColor() != Color.WHITE) {
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
	}

}
