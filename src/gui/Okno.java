package gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import splosno.Igralec;
import splosno.Vodja;
import splosno.VrstaIgralca;

@SuppressWarnings("serial")
public class Okno extends JFrame implements ActionListener {

	private Platno platno;
	
	//Statusna vrstica v spodnjem delu okna

	private JLabel status;

	private JMenuItem igraClovekRacunalnik;
	private JMenuItem igraRacunalnikClovek;
	private JMenuItem igraClovekClovek;
	private JMenuItem igraRacunalnikRacunalnik;

	public Okno() {
		
		
		this.setTitle("Backgammon");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		
		JMenuBar menu_bar = new JMenuBar();
		this.setJMenuBar(menu_bar);
		
		JMenu igra_menu = new JMenu("Nova igra");
		menu_bar.add(igra_menu);

		igraClovekRacunalnik = dodajMenuItem(igra_menu, "Človek – računalnik");
		igraRacunalnikClovek = dodajMenuItem(igra_menu, "Računalnik – človek");
		igraClovekClovek = dodajMenuItem(igra_menu, "Človek – človek");
		igraRacunalnikRacunalnik = dodajMenuItem(igra_menu, "Računalnik – računalnik");
		
		
		platno = new Platno();
		
		GridBagConstraints polje_layout = new GridBagConstraints();
		polje_layout.gridx = 0;
		polje_layout.gridy = 0;
		polje_layout.fill = GridBagConstraints.BOTH;
		//polje_layout.anchor = GridBagConstraints.CENTER;
		polje_layout.weightx = 1.0;
		polje_layout.weighty = 1.0;
		getContentPane().add(platno, polje_layout);
		// statusna vrstica za sporočila
		status = new JLabel();
		status.setFont(new Font(status.getFont().getName(),
							    status.getFont().getStyle(),
							    20));
		GridBagConstraints status_layout = new GridBagConstraints();
		status_layout.gridx = 0;
		status_layout.gridy = 1;
		status_layout.anchor = GridBagConstraints.CENTER;
		getContentPane().add(status, status_layout);
		
		status.setText("Izberite igro!");

	}
	
	public JMenuItem dodajMenuItem(JMenu menu, String naslov) {
		JMenuItem menuitem = new JMenuItem(naslov);
		menu.add(menuitem);
		menuitem.addActionListener(this);
		return menuitem;
	}

	// Odzivi ob izbirah različnih opcij v menuju
		@Override
		public void actionPerformed(ActionEvent e) {
		// PODMENU - NOVA IGRA
			// Nastavi izbiro igralcev
			if (e.getSource() == igraClovekRacunalnik) {
				Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
				Vodja.vrstaIgralca.put(Igralec.Crna, VrstaIgralca.C);
				Vodja.vrstaIgralca.put(Igralec.Bela, VrstaIgralca.R);
				Vodja.igramoNovoIgro();
			}
			else if (e.getSource() == igraRacunalnikClovek) {
				Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
				Vodja.vrstaIgralca.put(Igralec.Crna, VrstaIgralca.R);
				Vodja.vrstaIgralca.put(Igralec.Bela, VrstaIgralca.C);
				Vodja.igramoNovoIgro();
			}
			else if (e.getSource() == igraClovekClovek) {
				Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
				Vodja.vrstaIgralca.put(Igralec.Crna, VrstaIgralca.C);
				Vodja.vrstaIgralca.put(Igralec.Bela, VrstaIgralca.C);
				Vodja.igramoNovoIgro();
			}
			else if (e.getSource() == igraRacunalnikRacunalnik) {
				Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
				Vodja.vrstaIgralca.put(Igralec.Crna, VrstaIgralca.R);
				Vodja.vrstaIgralca.put(Igralec.Bela, VrstaIgralca.R);
				Vodja.igramoNovoIgro();
			}
	}
		
	// Osvezi GUI
	public void osveziGUI() {
		// Izpis v statusni vrstici
		if (Vodja.igra == null) {
			status.setText("Igra ni v teku.");
		}
		else {
			switch(Vodja.igra.stanje()) {
			case NEODLOCENO: status.setText("Neodločeno!"); break;
			case V_TEKU: 
				status.setText("Na potezi je " + Vodja.igra.naPotezi()); 
				break;
			case ZMAGA_CRNA: 
				status.setText("Zmagal je Črni");
				
				break;
			case ZMAGA_BELA: 
				status.setText("Zmagal je Beli");
				break;
			}
		}
		platno.repaint();
	}
}
