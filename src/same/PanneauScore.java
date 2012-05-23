package same;

import java.awt.*;

import javax.swing.*;

/**
 * Panneau indicatif de la partie en cours
 * 
 * @author Axel Schumacher
 *
 */
public class PanneauScore extends JPanel 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Score de la partie
	 */
	private JLabel scoreTotal;
	/**
	 * Score du groupe sélectionné
	 */
	private JLabel scoreSelection;
	/**
	 * Nombre de pions restant
	 */
	private JLabel nbPions;
	
	/**
	 * Constructeur
	 */
	public PanneauScore()
	{
		//Initialisation et placement des composants
		setBorder(BorderFactory.createLineBorder(Color.black, 2));
		setBackground(Color.white);
		scoreTotal = new JLabel();
		scoreSelection = new JLabel();
		nbPions = new JLabel();
		FlowLayout layout = new FlowLayout();
		layout.setHgap(30);
		setLayout(layout);
		add(nbPions, "West");
		add(scoreSelection, "Center");
		add(scoreTotal, "East");
		MAJNbPions(0);
		MAJScoreTotal(0, 0);
		MAJScoreSelection(0);
	}
	
	/**
	 * Change le nombre de pions
	 * 
	 * @param nb
	 * 	Nouveau nombre de pions
	 */
	public void MAJNbPions(int nb)
	{
		nbPions.setText("Nombre de Pions restant : "+nb);
	}
	
	/**
	 * Change le score total
	 * 
	 * @param score
	 * 	Score
	 * @param scoreABattre
	 * 	Score à battre
	 */
	public void MAJScoreTotal(int score, int scoreABattre)
	{
		scoreTotal.setText("Score total : "+score+"/"+scoreABattre);
	}
	
	/**
	 * Change le score du groupe
	 * 
	 * @param score
	 * 	Score du groupe
	 */
	public void MAJScoreSelection(int score)
	{
		scoreSelection.setText("Score du groupe : "+score);
	}
}
