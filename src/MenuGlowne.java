import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class MenuGlowne extends JFrame implements ActionListener
{
	private JMenuBar menuBar;
	private JMenu menuGra;
	private JMenuItem mRozpocznijGre, mPoziomTrudnosci, mListaWynikow, mWyjdz, menuZasadyGry;
	private JLabel lNazwa;
	
	public MenuGlowne()
	{
		setTitle("Balony");
		setSize(300,300);
		setVisible(true);
		setLocationRelativeTo(null);
		setResizable(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() 
		{

			@Override
			public void windowClosing(WindowEvent e) {
				int x = JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz wyj��?", "Hola hola!", JOptionPane.YES_NO_OPTION);
				if(x == JOptionPane.YES_OPTION)
				{
				e.getWindow().dispose();//System.exit(0);
				}
				else if(x == JOptionPane.NO_OPTION)
				{
					JOptionPane.showMessageDialog(null, "Dobra decyzja!");
				}
				else if(x == JOptionPane.CLOSED_OPTION)
				{
					JOptionPane.showMessageDialog(null, "Panie, co to za iksowanie?!","Nie�adnie!", JOptionPane.WARNING_MESSAGE);
				}
				}
			
		});
		setLayout(new BorderLayout());
		
		menuBar = new JMenuBar();
		menuGra = new JMenu("Gra");
		menuZasadyGry = new JMenuItem("Zasady Gry");
		
		mRozpocznijGre = new JMenuItem("Rozpocznij Gr�");
		mRozpocznijGre.addActionListener(this);
		
		mPoziomTrudnosci = new JMenuItem("Poziom Trudno�ci");
		mListaWynikow = new JMenuItem("Lista Wynik�w");
		mWyjdz = new JMenuItem("Wyj�cie");
		
		menuGra.add(mRozpocznijGre);
		menuGra.add(mPoziomTrudnosci);
		menuGra.add(mListaWynikow);
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
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object z = e.getSource();
		if(z==mWyjdz)
		{
			int odp = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz wyj��?", "Hola hola!", JOptionPane.YES_NO_OPTION);
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
				JOptionPane.showMessageDialog(this, "Panie, co to za iksowanie?!","Nie�adnie!", JOptionPane.WARNING_MESSAGE);
			}
		}
		if(z==menuZasadyGry)
		{
			dispose();
			Zasady zasady = new Zasady();
		}
	
		
		if(z==mRozpocznijGre)
		{
			Plansza plansza = new Plansza();
		}
		
	}
	
	public static void main(String[] args)
	{
		MenuGlowne okienko = new MenuGlowne();
	}
	
	
}
