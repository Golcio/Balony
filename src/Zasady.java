import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Zasady extends JFrame implements ActionListener
{
	private JTextArea tZasady;
	private JButton bPowrot;
	private BufferedReader buffReader;
	
	public Zasady()
	{
		setTitle("Zasady Gry");
		setSize(400,460);
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
		
		tZasady = new JTextArea();
		JScrollPane suwak = new JScrollPane(tZasady);
		tZasady.setEditable(false);
		add(suwak, BorderLayout.CENTER);

		try {
			File file = new File("zasady.txt");
			FileReader fileReader = new FileReader(file);
			buffReader = new BufferedReader(fileReader);
			tZasady.read(buffReader, null);
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		bPowrot = new JButton("Powrót");
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
