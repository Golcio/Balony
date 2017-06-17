import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class KoniecGry extends JFrame implements ActionListener
{
	private boolean wygrana;
	private JLabel wiadomosc;
	private JButton okej;
	
	public KoniecGry(boolean wygrana)
	{
		if(wygrana == true)
		{
			setTitle("Wygrana!");
		}
		else
		{
			setTitle("Przegrana!");
		}
		setSize(400,200);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
		
		addWindowListener(new WindowAdapter() 
		{

			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				MenuGlowne okienko = new MenuGlowne();
				okienko.setVisible(true);
				}
			
		});
		
		wiadomosc = new JLabel("Przegra³eœ!");
		wiadomosc.setFont(new Font("SanSerif",Font.BOLD,25));
		add(wiadomosc);
		
		okej = new JButton("OK");
		okej.setSize(80, 50);
		add(okej);
		okej.addActionListener(this);
		
		

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object zrodlo = e.getSource();
		if(zrodlo==okej)
		{
			dispose();
			MenuGlowne okienko = new MenuGlowne();
		}
		
	}

}
	
