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
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;
import javax.swing.JSplitPane;
import net.miginfocom.swing.MigLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;

public class KoniecGry extends JFrame implements ActionListener
{
	private boolean wygrana;
	private JLabel lWiadomosc;
	private JButton bOkej;
	private int punkty;
	private JLabel lPunkty;
	
	public KoniecGry(boolean wygrana, int punkty)
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
		
		addWindowListener(new WindowAdapter() 
		{

			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				MenuGlowne okienko = new MenuGlowne();
				okienko.setVisible(true);
				}
			
		});
		
		bOkej = new JButton("OK");
		bOkej.setLocation(147, 101);
		bOkej.setSize(100, 60);
		getContentPane().setLayout(null);
		
		lWiadomosc = new JLabel("Przegra³eœ!");
		lWiadomosc.setBounds(129, 24, 135, 33);
		lWiadomosc.setHorizontalAlignment(SwingConstants.CENTER);
		lWiadomosc.setFont(new Font("SanSerif",Font.BOLD,25));
		getContentPane().add(lWiadomosc);
		getContentPane().add(bOkej);
		
		lPunkty = new JLabel("Uzyska³eœ " + punkty + " punktów.");
		lPunkty.setBounds(119, 68, 155, 14);
		getContentPane().add(lPunkty);
		bOkej.addActionListener(this);
		
		
		
		

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object zrodlo = e.getSource();
		if(zrodlo==bOkej)
		{
			dispose();
			MenuGlowne okienko = new MenuGlowne();
		}
		
	}
}
	
