import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
/**
 * okno wyswietlajace liste najlepszych wynikow
 * @filed wyniki plik tekstowy  wynikami
 */
public class ListaWynikow extends JFrame implements ActionListener
{
	private JTextArea tWyniki;
	private JButton bPowrot;
	private Scanner wczytaj;
	private File wyniki;
	/**
     * Konstruktor tworzacy okno na podstawie pliku z wynikami i wypisujacy je w polu tekstowym  .
     *
     * @param wyniki  plik z wynikami
     *
     */
	public ListaWynikow(File wyniki)
	{
		this.wyniki = wyniki;
		setTitle("Lista Wynik�w");
		setSize(250,250);
		setVisible(true);
		setLocationRelativeTo(null);
		setResizable(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
		setLayout(new BorderLayout());
		
		tWyniki = new JTextArea();
		JScrollPane suwak = new JScrollPane(tWyniki);
		tWyniki.setEditable(false);
		add(suwak, BorderLayout.CENTER);
		
		try 
		{
			wczytaj = new Scanner(wyniki);
			while (wczytaj.hasNext())
				tWyniki.append(wczytaj.nextLine() + "\n");
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		tWyniki.setFont(new Font("ArialBlack", Font.BOLD,20));
		
		
		bPowrot = new JButton("Powr�t");
		add(bPowrot, BorderLayout.PAGE_END);
		bPowrot.addActionListener(this);
		
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object zrodlo = e.getSource();
		if(zrodlo==bPowrot)
		{
			dispose();
			MenuGlowne okienko = new MenuGlowne();
		}
	}

	public static void main(String[] args)
	{
		
	}
}
