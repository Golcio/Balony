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

public class MenuGlowne extends JFrame implements ActionListener
{
	private JMenuBar menuBar;
	private JMenu menuGra, mPoziomTrudnosci;
	private JMenuItem mRozpocznijGre, mListaWynikow, mWyjdz, menuZasadyGry;
	private JRadioButtonMenuItem mLatwy, mSredni, mTrudny;
	private ButtonGroup bgPoziom;
	private JLabel lNazwa;
	
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
		setLayout(new BorderLayout());
		
		menuBar = new JMenuBar();
		menuGra = new JMenu("Gra");
		menuZasadyGry = new JMenuItem("Zasady Gry");
		
		mRozpocznijGre = new JMenuItem("Rozpocznij Grê");
		mRozpocznijGre.addActionListener(this);
		
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
		mLatwy.setSelected(true);
		
		mListaWynikow = new JMenuItem("Lista Wyników");
		mWyjdz = new JMenuItem("Wyjœcie");
		
		menuGra.add(mRozpocznijGre);
		menuGra.add(mPoziomTrudnosci);
		menuGra.add(mListaWynikow);
		mListaWynikow.addActionListener(this);
		
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
		add(lNazwa);
		setVisible(true);
	}

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
	
		
		else if(z==mRozpocznijGre)
		{
			
			File plikKofiguracyjny = new File("plikTestowy.txt");
			//File plikKofiguracyjny = new File("jedenkolor.txt");


			try {
				dispose();
				Plansza plansza = new Plansza(plikKofiguracyjny);
				plansza.setVisible(true);
				/*EventQueue.invokeLater(new Runnable() {
					public void run() {
						plansza.setVisible(true);
					}
				});*/
			}
			catch (IOException error)
			{
				System.out.println("ERROR: IOException");
			}

		}
		
		else if(z==mListaWynikow)
		{
			dispose();
			ListaWynikow wyniki = new ListaWynikow();
		}
		
	}
	
	public static void main(String[] args)
	{
		MenuGlowne okienko = new MenuGlowne();
	}
	
	
}
