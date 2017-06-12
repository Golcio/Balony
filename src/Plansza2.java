import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
public class Plansza2 extends JFrame implements ActionListener, Runnable
{

    private JPanel contentPane;
    private ImageIcon img;
    private Image balonik;
    private int WYSOKOSC, SZEROKOSC;
    int czas = 5;
    private Timer tm = new Timer(czas, this);
    double proporcjaX;
    double proporcjaY;
    double droga;
    private Polozenie polozenieNaboju;
    private Properties pola = new Properties();
    double PRZESUNIECIE = 10;
    double PRZESUNIECIEX;
    double PRZESUNIECIEY;
    private Vector<Polozenie> polozenia = new Vector<>();
    int przesuniecieWPoziomie;
    int przesuniecieWPionie;

    /**
     * Create the frame.
     */
    public Plansza2(File plikStartowy) throws IOException
    {
        Wczytaj(plikStartowy);
        setTitle("Gra Balony");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, getWidth(), getHeight());
        contentPane = new JPanel();
        contentPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println(e.getPoint());
            }
        });
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        Box verticalBox = Box.createVerticalBox();
        contentPane.add(verticalBox, BorderLayout.EAST);

        JToggleButton PauzaToggleButton = new JToggleButton("Pauza");
        PauzaToggleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
            }
        });

        verticalBox.add(WyjdzToggleButton);

        JSeparator separator_2 = new JSeparator();
        verticalBox.add(separator_2);
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
            Balon balon = new Balon(kolor);
            for (Polozenie p : polozenia) {
                if (p.equals(wspolrzedneBalona))
                    wspolrzedneBalona = p;
                pola.replace(wspolrzedneBalona, balon);
            }


        } catch (ArrayIndexOutOfBoundsException e) {
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

    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

