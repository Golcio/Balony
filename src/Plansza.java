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
import java.util.Vector;

/**
 * Created by user on 2017-06-11.
 */
public class Plansza extends JFrame implements ActionListener, Runnable {

    private JToggleButton WyjdzToggleButton;
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


    double PRZESUNIECIE = 10;
    double PRZESUNIECIEX;
    double PRZESUNIECIEY;

    int przesuniecieWPoziomie;
    int przesuniecieWPionie;
    int czas = 5;
    private Timer tm = new Timer(czas, this);
    boolean stoper;
    boolean active;
    private Thread th;

    /**
     * Create the frame.
     */
    public Plansza(File plikStartowy) throws IOException {

        Wczytaj(plikStartowy);
        setTitle("Gra Balony");
        pocisk = new Balon(getKolor(99), new Polozenie((getWidth() / 2) - 30, getHeight() - 120));
        pociski.add(pocisk);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, getWidth(), getHeight());
        contentPane = new JPanel();
        
        

        MouseListenerPlansza();
        

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        Box verticalBox = Box.createVerticalBox();
        contentPane.add(verticalBox, BorderLayout.EAST);

        JToggleButton PauzaToggleButton = new JToggleButton("Pauza");
        PauzaToggleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                active=false;
                System.out.println("Pauza dziala");
                try {
                    if (th.getState() == Thread.State.RUNNABLE) {
                        try {
                            th.join();

                        } catch (NullPointerException e1) {
                            System.out.println("NullPointerException - przy naciśnięciu Pauza");
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }


                    } else th.interrupt();


                } catch (NullPointerException e1) {
                    // nic nie rób
                }


            }
        });

        JSeparator separator_1 = new JSeparator();
        verticalBox.add(separator_1);
        verticalBox.add(PauzaToggleButton);

        JSeparator separator = new JSeparator();
        verticalBox.add(separator);

        JToggleButton WyjdzToggleButton = new JToggleButton("Wyjdz");

        WyjdzToggleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Wyjdz dziala");
            }
        });

        verticalBox.add(WyjdzToggleButton);

        JSeparator separator_2 = new JSeparator();
        verticalBox.add(separator_2);
        
        this.addWindowListener(new WindowAdapter()
		{

			@Override
			public void windowClosing(WindowEvent e) 
			{
				dispose();
				MenuGlowne okienko = new MenuGlowne();
				okienko.setVisible(true);
			}
			
		});
    }

    public void MouseListenerPlansza() {
        contentPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println(e.getPoint());
                th = new Thread(Plansza.this::run);
                active = true;
                th.start();

                Polozenie gdzieKliknieto = new Polozenie(e.getX(), e.getY());
                Polozenie polozenieWyrzutni = new Polozenie(pociski.lastElement().getAktualnePolozenia().getWsplX(), pociski.lastElement().getAktualnePolozenia().getWsplY());
                przesuniecieWPoziomie = gdzieKliknieto.getWsplX() - polozenieWyrzutni.getWsplX();
                przesuniecieWPionie = gdzieKliknieto.getWsplY() - polozenieWyrzutni.getWsplY();
                droga = Math.sqrt(przesuniecieWPionie * przesuniecieWPionie + przesuniecieWPoziomie * przesuniecieWPoziomie);
                // System.out.println("droga przesuniecieX przesuniecieY " + droga + przesuniecieWPoziomie + przesuniecieWPionie);
                proporcjaX = przesuniecieWPoziomie / droga;
                proporcjaY = przesuniecieWPionie / droga;
                PRZESUNIECIEX = Math.abs(PRZESUNIECIE * proporcjaX);
                PRZESUNIECIEY = Math.abs(PRZESUNIECIE * proporcjaY);
            }
        });
    }


    /**
     * Metoda wczytuje wymiary planszy oraz informacje o balonach z pliku konfiguracyjnego
     *
     * @param plikStartowy bie��ca linijka
     * @throws IOException je�eli nie uda si� otworzyc pliku
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
     * @param WYSOKOSC  planszy w ilosci rzed�w balon�w
     * @param SZEROKOSC planszy w ilosci rzed�w balon�w
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
     * Metoda wczytuje po�ozenie balon�w  z pliku kofiguracyjnego.
     *
     * @param line bie��ca linijka
     * @param br   bufor czytnika
     * @return bierzaca linie
     * @throws IOException je�eli nie uda si� odczyta� kolejnej linijki
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
     * Metoda wczytuje z odczytanej lini pliku po�ozenie i kolor balona a obiektu klasy Plansza.
     *
     * @param line bie��ca linijka pliku nad ktora pracuje metoda
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
     * @param kolorInt kod koloru
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
     * Metoda obs�ugujaca zdarzenie  wcisniecia przycisku.
     *
     * @param e przycisniecie przycisku
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        Object z = e.getSource();
        if (z == this.WyjdzToggleButton) {
            int odp = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz wyj��?", "Hola hola!", JOptionPane.YES_NO_OPTION);
            if (odp == JOptionPane.YES_OPTION) {
                dispose();
                MenuGlowne okienko = new MenuGlowne();
            } else if (odp == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog(this, "Dobra decyzja!", "Brawo!", JOptionPane.INFORMATION_MESSAGE);
            } else if (odp == JOptionPane.CLOSED_OPTION) {
                JOptionPane.showMessageDialog(this, "Panie, co to za iksowanie?!", "Nie�adnie!", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * modyfikuje połozenie balonu-pocisku
     */

    private void modyfikacjaPolozenia() {

        Polozenie nowePolozenie = pociski.lastElement().getAktualnePolozenia();


        if (pociski.lastElement().getAktualnePolozenia().getWsplX() >= 60 && pociski.lastElement().getAktualnePolozenia().getWsplX() <= (getWidth() - 120)) {
            if (przesuniecieWPoziomie < 0) {
                nowePolozenie.setWsplX((int) (pociski.lastElement().getAktualnePolozenia().getWsplX() - PRZESUNIECIEX));

            }
            if (przesuniecieWPoziomie > 0) {
                nowePolozenie.setWsplX((int) (pociski.lastElement().getAktualnePolozenia().getWsplX() + PRZESUNIECIEX));

            }
        }
        if (pociski.lastElement().getAktualnePolozenia().getWsplX() <= 60) {
            nowePolozenie.setWsplX(60);
            PRZESUNIECIEX = -1 * PRZESUNIECIEX;
        }


        if (pociski.lastElement().getAktualnePolozenia().getWsplX() >= getWidth() - 120) {
            nowePolozenie.setWsplX(getWidth() - 120);
            PRZESUNIECIEX = -1 * PRZESUNIECIEX;
        }


        if (pociski.lastElement().getAktualnePolozenia().getWsplY() >= 60 && pociski.lastElement().getAktualnePolozenia().getWsplY() <= getHeight() - 60) {
            if (przesuniecieWPionie < 0) {
                nowePolozenie.setWsplY((int) (pociski.lastElement().getAktualnePolozenia().getWsplY() - PRZESUNIECIEY));
            }
            if (przesuniecieWPionie > 0) {
                nowePolozenie.setWsplY((int) (pociski.lastElement().getAktualnePolozenia().getWsplY() + PRZESUNIECIEY));
            }
            if (pociski.lastElement().getAktualnePolozenia().getWsplY() <= 60) {
                nowePolozenie.setWsplY(60);
                PRZESUNIECIEY = -1 * PRZESUNIECIEY;
            }

            if (pociski.lastElement().getAktualnePolozenia().getWsplY() >= getHeight() - 60) {
                nowePolozenie.setWsplY(getHeight() - 60);
                PRZESUNIECIEY = -1 * PRZESUNIECIEY;
            }

        }


        boolean czyMoznadalej = CzyDrogaWolna(nowePolozenie, balonyNaPlanszy);


        if (czyMoznadalej) {
            pociski.lastElement().setAktualnePolozenia(nowePolozenie);
        } else {
        	pociski.lastElement().setAktualnePolozenia(new Polozenie(balonik.getAktualnePolozenia().getWsplX()*60, balonik.getAktualnePolozenia().getWsplY()*60 + 60));
        	/*pocisk = new Balon(new Polozenie((getWidth() / 2) - 30, getHeight() - 120));*/
            stoper = true;
        }


    }

    private boolean CzyDrogaWolna(Polozenie nowePolozenie, Vector<Balon> balonyNaPlanszy) {


        for (Balon b : balonyNaPlanszy) {

            if (Math.abs(b.getAktualnePolozenia().getWsplX() * 60 - nowePolozenie.getWsplX()) <= 60) {
                if (Math.abs(b.getAktualnePolozenia().getWsplY() * 60 - nowePolozenie.getWsplY()) <= 60) {
                	balonik = b;
                    return false;
                }
            }
        }
        return true;
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
     * @param g kontekst graficzny
     */


    public void paintComponent(Graphics g) {
        super.paintComponents(g);

        // g.setColor(Color.WHITE);
        //g.fillRect(0, 0, SZEROKOSC * 60, WYSOKOSC * 60);
        // g.setColor(Color.GRAY);
        //g.fillRect(0, 0, SZEROKOSC * 60, 60);
        //g.fillRect(0, 0, 60, WYSOKOSC * 60);
        //g.fillRect(SZEROKOSC * 60 - 60, 0, 60, WYSOKOSC * 60);
        // g.fillRect(0, WYSOKOSC * 60 - 60, SZEROKOSC * 60, 60);
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
            if (g.getColor() != Color.WHITE) {
                //g.fillOval(p.getWsplX() * 60, p.getWsplY() * 60, 60, 60);
                g.drawImage(b.getObrazekBalonu(), b.getAktualnePolozenia().getWsplX() * 60, b.getAktualnePolozenia().getWsplY() * 60, null);
            }
        }

        //g.setColor(Color.black);

        //g.fillOval(polozenieNaboju.getWsplX() * 1, polozenieNaboju.getWsplY() * 1, 60, 60);
        
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
                //g.fillOval(p.getWsplX() * 60, p.getWsplY() * 60, 60, 60);
                g.drawImage(b.getObrazekBalonu(), b.getAktualnePolozenia().getWsplX(), b.getAktualnePolozenia().getWsplY(), null);
            }
        }
        /*img = new ImageIcon("czarny.png").getImage();

        g.drawImage(img, pocisk.getAktualnePolozenia().getWsplX(), pocisk.getAktualnePolozenia().getWsplY(), null);*/


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

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        while (true) {
            if (stoper) {
                break;
            }
            if (active) {
                modyfikacjaPolozenia();
                repaint();
                Sleeeep(25);
            }

        }

    }
}


