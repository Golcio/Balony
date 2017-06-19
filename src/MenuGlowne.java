import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingConstants;
import javax.swing.JButton;

public class MenuGlowne extends JFrame implements ActionListener
{
	private JMenuBar menuBar;
	private JMenu menuGra, mPoziomTrudnosci, mRozpocznijGre, mListaWynikow;
	private JMenuItem mWyjdz, menuZasadyGry, bPoziomPierwszy, bPoziomDrugi, bPoziomTrzeci, bWynik1, bWynik2, bWynik3;
	private JRadioButtonMenuItem mLatwy, mSredni, mTrudny;
	private ButtonGroup bgPoziom;
	private JLabel lNazwa;
	private int trudnosc=1;
	
	public MenuGlowne()
	{
		setTitle("Balony");
		setSize(700,500);
		setLocationRelativeTo(null);
		setResizable(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() 
		{

			@Override
			public void windowClosing(WindowEvent e) {
				int x = JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz wyjœæ?", "Hola hola!", JOptionPane.YES_NO_OPTION);
				if(x == JOptionPane.YES_OPTION)
				{
				e.getWindow().dispose();
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
		getContentPane().setLayout(new BorderLayout());
		
		menuBar = new JMenuBar();
		menuGra = new JMenu("Gra");
		menuZasadyGry = new JMenuItem("Zasady Gry");
		
		mRozpocznijGre = new JMenu("Rozpocznij Grê");
		menuGra.add(mRozpocznijGre);
		bPoziomPierwszy = new JMenuItem("Poziom Pierwszy");
		mRozpocznijGre.add(bPoziomPierwszy);
		bPoziomDrugi = new JMenuItem("Poziom Drugi");
		mRozpocznijGre.add(bPoziomDrugi);
		bPoziomTrzeci = new JMenuItem("Poziom Trzeci");
		mRozpocznijGre.add(bPoziomTrzeci);
		bPoziomPierwszy.addActionListener(this);
		bPoziomDrugi.addActionListener(this);
		bPoziomTrzeci.addActionListener(this);
		
		
		mPoziomTrudnosci = new JMenu("Poziom Trudnoœci");
		bgPoziom = new ButtonGroup();
		mLatwy = new JRadioButtonMenuItem("£atwy");
		mSredni = new JRadioButtonMenuItem("Œredni");
		mTrudny = new JRadioButtonMenuItem("Trudny");
		bgPoziom.add(mLatwy);
		bgPoziom.add(mSredni);
		bgPoziom.add(mTrudny);
		mPoziomTrudnosci.add(mLatwy);
		mPoziomTrudnosci.add(mSredni);
		mPoziomTrudnosci.add(mTrudny);
		mLatwy.addActionListener(this);
		mSredni.addActionListener(this);
		mTrudny.addActionListener(this);
		mLatwy.setSelected(true);
		
		mListaWynikow = new JMenu("Lista Wyników");
		bWynik1 = new JMenuItem("Pierwsza Plansza");
		bWynik2 = new JMenuItem("Druga Plansza");
		bWynik3 = new JMenuItem("Trzecia Plansza");
		
		
		mWyjdz = new JMenuItem("Wyjœcie");
		
		
		menuGra.add(mPoziomTrudnosci);
		menuGra.add(mListaWynikow);
		mListaWynikow.add(bWynik1);
		mListaWynikow.add(bWynik2);
		mListaWynikow.add(bWynik3);
		mListaWynikow.addActionListener(this);
		bWynik1.addActionListener(this);
		bWynik2.addActionListener(this);
		bWynik3.addActionListener(this);
		
		menuGra.addSeparator();
		menuGra.add(mWyjdz);
		
		mWyjdz.addActionListener(this);
		
		
		setJMenuBar(menuBar);
		menuBar.add(menuGra);
		
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(menuZasadyGry);
		menuZasadyGry.addActionListener(this);
		
		lNazwa = new JLabel("Witaj w grze Balony!", SwingConstants.CENTER);
		lNazwa.setBounds(20,20,300,200);
		lNazwa.setFont(new Font("SanSerif",Font.BOLD,25));
		getContentPane().add(lNazwa);
		setVisible(true);
	}
	/**
	 * obs³uga zdarzen dla glownego okna
	 * @param e zdarzenie
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
	
		Object z = e.getSource();
		if(z==mWyjdz)
		{
			int odp = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz wyjœæ?", "Hola hola!", JOptionPane.YES_NO_OPTION);
			if(odp == JOptionPane.YES_OPTION)
			{
			System.exit(0);
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
		
		else if(z==menuZasadyGry)
		{
			dispose();
			Zasady zasady = new Zasady();
		}
	
		
		else if(z==bPoziomPierwszy)
		{
			
			File plikKofiguracyjny = new File("Pierwszy.txt");


			try {
				dispose();
				Plansza plansza = new Plansza(plikKofiguracyjny, trudnosc);
				plansza.setVisible(true);
			}
			catch (IOException error)
			{
				System.out.println("ERROR: IOException");
			}

		}
		
		else if(z==bPoziomDrugi)
		{
			
			File plikKofiguracyjny = new File("Drugi.txt");


			try {
				dispose();
				Plansza plansza = new Plansza(plikKofiguracyjny, trudnosc);
				plansza.setVisible(true);
			}
			catch (IOException error)
			{
				System.out.println("ERROR: IOException");
			}

		}
		
		else if(z==bPoziomTrzeci)
		{
			
			File plikKofiguracyjny = new File("Trzeci.txt");


			try {
				dispose();
				Plansza plansza = new Plansza(plikKofiguracyjny, trudnosc);
				plansza.setVisible(true);
			}
			catch (IOException error)
			{
				System.out.println("ERROR: IOException");
			}

		}
		
		else if(z==bWynik1)
		{
			dispose();
			ListaWynikow wyniki = new ListaWynikow(new File("wyniki1.txt"));
		}
		else if(z==bWynik2)
		{
			dispose();
			ListaWynikow wyniki = new ListaWynikow(new File("wyniki2.txt"));
		}
		else if(z==bWynik3)
		{
			dispose();
			ListaWynikow wyniki = new ListaWynikow(new File("wyniki3.txt"));
		}
		
		
		if(mLatwy.isSelected())
		{
			trudnosc=1;
		}
		else if(mSredni.isSelected())
		{
			trudnosc=2;
		}
		else if(mTrudny.isSelected())
		{			
			trudnosc=3;
		}
		
	}
	
	public static void main(String[] args)
	{
		MenuGlowne okienko = new MenuGlowne();
	}
	
	
}
