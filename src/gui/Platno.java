package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import splosno.Vodja;
import splosno.Polje;

@SuppressWarnings("serial")
public class Platno extends JPanel {
	
	public Platno() {
		setBackground(Color.PINK);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(700, 400);
	}

	
	// Relativna širina črte
	private final static double LINE_WIDTH = 0.05;
	
	// Širina enega kvadratka
	private double skatlice() {
		return getWidth() / 10;
	}
	
	private double rob() {
		return getWidth() / 28;
	}
	
	private double spice() {
		return (getWidth() - skatlice() - 4 * rob()) / 12;
	}
	
	
	
	
	// Relativni prostor okoli X in O
	private final static double PADDING = 0.18;
	
	
	private void paintBela(Graphics2D g2, int polje, int stZetonov) {
		double y;
		double x = 0;
		double spice = spice();
		double premer = spice/3;
		double rob = rob();
		double skatlice = skatlice();
		if (polje < 12) { 
			y = getHeight() - rob - premer - (0.5 * LINE_WIDTH + PADDING);
		}
		else {
			y = rob + 0.5 * LINE_WIDTH + PADDING;
		}
		if (polje == 25) {
			x = getWidth() - rob - skatlice/2 - premer/2 - (0.5 * LINE_WIDTH + PADDING);
		}
		else if (polje == 26) {
			x = 2 *rob + 6 * spice - premer/2 - (0.5 * LINE_WIDTH + PADDING);
			y = 3 * getHeight() / 4 - premer/2;
		}
		else if (polje > 6 && polje < 13) {
			x = rob + (12 - polje) * spice + spice/2 - premer/2 - (0.5 * LINE_WIDTH + PADDING); 
		}
		else if (polje > 12 && polje < 19) {
			x = rob + (polje - 13) * spice + spice/2 - premer/2 - (0.5 * LINE_WIDTH + PADDING); 
		}
		else if (polje < 7 && polje > 0) {
			x = 3 * rob + (12 - polje) * spice + spice/2 - premer/2 - (0.5 * LINE_WIDTH + PADDING); 
		}
		else if (polje > 18 && polje < 25) {
			x = 3 * rob + (polje - 13) * spice + spice/2 - premer/2 - (0.5 * LINE_WIDTH + PADDING);
		}
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke((float) (spice * LINE_WIDTH)));
		g2.fillOval((int)x, (int)y, (int)premer , (int)premer);
		g2.setColor(Color.BLACK);
		g2.drawString(Integer.toString(stZetonov), (int)(x + premer/2), (int)(y + premer/2));
	}
	
	private void paintCrna(Graphics2D g2, int polje, int stZetonov) {
		double y;
		double x = 0;
		double spice = spice();
		double premer = spice/3;
		double rob = rob();
		double skatlice = skatlice();
		if (polje < 12) { 
			y = getHeight() - rob - premer - (0.5 * LINE_WIDTH + PADDING);
		}
		else {
			y = rob + 0.5 * LINE_WIDTH + PADDING;
		}
		if (polje == 0) {
			x = getWidth() - rob - skatlice/2 - premer/2 - (0.5 * LINE_WIDTH + PADDING);
		}
		else if (polje == 27) {
			x = 2 *rob + 6 * spice - premer/2 - (0.5 * LINE_WIDTH + PADDING);
			y = getHeight() / 4 - premer/2;
		}
		else if (polje > 6 && polje < 13) {
			x = rob + (12 - polje) * spice + spice/2 - premer/2 - (0.5 * LINE_WIDTH + PADDING); 
		}
		else if (polje > 12 && polje < 19) {
			x = rob + (polje - 13) * spice + spice/2 - premer/2 - (0.5 * LINE_WIDTH + PADDING); 
		}
		else if (polje < 7 && polje > 0) {
			x = 3 * rob + (12 - polje) * spice + spice/2 - premer/2 - (0.5 * LINE_WIDTH + PADDING); 
		}
		else if (polje > 18 && polje < 25) {
			x = 3 * rob + (polje - 13) * spice + spice/2 - premer/2 - (0.5 * LINE_WIDTH + PADDING);
		}
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke((float) (spice * LINE_WIDTH)));
		g2.fillOval((int)x, (int)y, (int)premer , (int)premer);
		g2.setColor(Color.WHITE);
		g2.drawString(Integer.toString(stZetonov), (int)(x + premer/2), (int)(y + premer/2));
	}
	
	public void paintKocke(Graphics2D g2, int prva, int druga) {
		double spice = spice();
		double rob = rob();
		double premer =  2 * spice / 3;
		double y = getHeight() / 2 - premer / 2;
		double x1 = 3 * rob + 7 * spice;
		double x2 = x1 + 2 * premer;
		g2.setColor(Color.RED);
		g2.drawRoundRect((int)x1, (int)y, (int)premer, (int)premer, (int)premer/4, (int)premer/4);
		g2.drawRoundRect((int)x2, (int)y, (int)premer, (int)premer, (int)premer/4, (int)premer/4);
		paintPike(g2, prva, x1, y, premer);
		paintPike(g2, druga, x2, y, premer);
		if (prva == druga) {
			g2.setColor(Color.RED);
			g2.drawRoundRect((int)(x2 + 2*premer), (int)y, (int)premer, (int)premer, (int)premer/4, (int)premer/4);
			g2.drawRoundRect((int)(x2 + 4*premer), (int)y, (int)premer, (int)premer, (int)premer/4, (int)premer/4);
			paintPike(g2, prva, x2 + 2*premer, y, premer);
			paintPike(g2, prva, x2 + 4*premer, y, premer);
		}
	}
	
	public void paintPike(Graphics2D g2, int stPik, double x, double y, double premer) {
		double polmerPike = premer / 10;
		g2.setColor(Color.WHITE);
		switch (stPik) {
		case 1: g2.drawOval((int)(x + premer/2 - polmerPike), (int)(y + premer/2 - polmerPike), (int)polmerPike * 2, (int)polmerPike * 2);
		case 2: 
			g2.drawOval((int)(x + polmerPike), (int)(y + polmerPike), (int)polmerPike * 2, (int)polmerPike * 2);
			g2.drawOval((int)(x + premer - 3 * polmerPike), (int)(y + premer - 3 * polmerPike), (int)polmerPike * 2, (int)polmerPike * 2);
		case 3:
			g2.drawOval((int)(x + polmerPike), (int)(y + polmerPike), (int)polmerPike * 2, (int)polmerPike * 2);
			g2.drawOval((int)(x + 4 * polmerPike), (int)(y + 4 * polmerPike), (int)polmerPike * 2, (int)polmerPike * 2);
			g2.drawOval((int)(x + premer - 3 * polmerPike), (int)(y + premer - 3 * polmerPike), (int)polmerPike * 2, (int)polmerPike * 2);
		case 4:
			g2.drawOval((int)(x + polmerPike), (int)(y + polmerPike), (int)polmerPike * 2, (int)polmerPike * 2);
			g2.drawOval((int)(x + premer - 3 * polmerPike), (int)(y + polmerPike), (int)polmerPike * 2, (int)polmerPike * 2);
			g2.drawOval((int)(x + polmerPike), (int)(y + premer - 3 * polmerPike), (int)polmerPike * 2, (int)polmerPike * 2);
			g2.drawOval((int)(x + premer - 3 * polmerPike), (int)(y + premer - 3 * polmerPike), (int)polmerPike * 2, (int)polmerPike * 2);
		case 5:
			g2.drawOval((int)(x + polmerPike), (int)(y + polmerPike), (int)polmerPike * 2, (int)polmerPike * 2);
			g2.drawOval((int)(x + premer - 3 * polmerPike), (int)(y + polmerPike), (int)polmerPike * 2, (int)polmerPike * 2);
			g2.drawOval((int)(x + polmerPike), (int)(y + premer - 3 * polmerPike), (int)polmerPike * 2, (int)polmerPike * 2);
			g2.drawOval((int)(x + premer - 3 * polmerPike), (int)(y + premer - 3 * polmerPike), (int)polmerPike * 2, (int)polmerPike * 2);
			g2.drawOval((int)(x + 4 * polmerPike), (int)(y + 4 * polmerPike), (int)polmerPike * 2, (int)polmerPike * 2);
		case 6:

			g2.drawOval((int)(x + polmerPike), (int)(y + polmerPike), (int)polmerPike * 2, (int)polmerPike * 2);
			g2.drawOval((int)(x + premer - 3 * polmerPike), (int)(y + polmerPike), (int)polmerPike * 2, (int)polmerPike * 2);
			g2.drawOval((int)(x + polmerPike), (int)(y + premer - 3 * polmerPike), (int)polmerPike * 2, (int)polmerPike * 2);
			g2.drawOval((int)(x + premer - 3 * polmerPike), (int)(y + premer - 3 * polmerPike), (int)polmerPike * 2, (int)polmerPike * 2);
			g2.drawOval((int)(x + polmerPike), (int)(y + 4 * polmerPike), (int)polmerPike * 2, (int)polmerPike * 2);
			g2.drawOval((int)(x + premer - 3 * polmerPike), (int)(y + 4 * polmerPike), (int)polmerPike * 2, (int)polmerPike * 2);			
		}
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		double skatlice = skatlice();
		double rob = rob();
		double spice = spice();
		
		g2.setColor(Color.WHITE);
		int sirina =(int)((getWidth() - skatlice - 4 * rob) /2);
		g2.fillRect((int)rob, (int)rob, (int)sirina, (int)(getHeight() - 2* rob));
		g2.fillRect((int)(3*rob + sirina), (int)rob, (int)sirina, (int)(getHeight() - 2* rob));
		
		g2.setColor(Color.GREEN);
		g2.fillRect((int)(getWidth() - skatlice), (int)rob, (int)(skatlice - rob), (int)(getHeight() / 2 - 2 * rob));
		g2.fillRect((int)(getWidth() - skatlice), (int)(rob + getHeight() /2 ), (int)(skatlice - rob), (int)((getHeight() - 4 * rob) /2 ));
		
		boolean modra = false;
		for (int i = 0;i < 12;i++) {
			double zamik;
			if (i < 6) {zamik = 0;} else {zamik = 2 * rob;}
			if (modra) {g2.setColor(Color.BLUE);} else {g2.setColor(Color.GREEN);}
			g2.fillPolygon(new int[] {(int)(rob + i * spice + zamik), (int)(rob + (i + 1) * spice + zamik), (int)(rob + i * spice + spice/2 + zamik)}, 
					new int[] {(int)rob, (int)rob, (int)(getHeight()/2 - rob)}, 3);
			if (modra) {g2.setColor(Color.BLUE);} else {g2.setColor(Color.GREEN);}
			modra = ! modra;
			g2.fillPolygon(new int[] {(int)(rob + i * spice + zamik), (int)(rob + (i + 1) * spice + zamik), (int)(rob + i * spice + spice/2 + zamik)}, 
					new int[] {(int)(getHeight() -rob), (int)(getHeight() -rob), (int)(getHeight()/2 + rob)}, 3);
		}
			
		//zetoni
		Polje[] plosca;
		if (Vodja.igra != null) {
			plosca = Vodja.igra.getPlosca();
			for (int i = 0; i < 28; i++) {
				switch(plosca[i]) {
				case Bela: paintBela(g2, i, plosca[i].steviloZetonov); break;
				case Crna: paintCrna(g2, i, plosca[i].steviloZetonov); break;
				default: break;
				}
			}
		}
	}
}










