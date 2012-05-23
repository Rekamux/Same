package same;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Score d'un joueur
 * 
 * @author Axel Schumacher
 *
 */
class Score implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**
	 * Nom du joueur
	 */
	private String nom;
	/**
	 * Score du joueur
	 */
	private int score;
	/**
	 * TimeStamp de la partie
	 */
	private long timeStamp;
	
	/**
	 * Constructeur
	 * @param nom nom du joueur
	 * @param score score du joueur
	 * @param timeStamp timeStamp de la partie
	 */
	public Score(String nom, int score, long timeStamp) 
	{
		this.nom = nom;
		this.score = score;
		this.timeStamp = timeStamp;
	}

	/**
	 * Retourne le nom du joueur
	 * @return
	 * 	Nom du joueur
	 */
	public String getNom() 
	{
		return nom;
	}

	/**
	 * Retourne le score
	 * 
	 * @return
	 * 	Le score
	 */
	public int getScore() 
	{
		return score;
	}

	/**
	 * Retourne le timestamp de la partie
	 * 
	 * @return
	 * 	Le TimeStamp
	 */
	public long getTimeStamp()
	{
		return timeStamp;
	}
}

/**
 * Contient une liste de scores
 * 
 * @author Axel Schumacher
 *
 */
class Scores extends LinkedList<Score> implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Nombre maximum de scores
	 */
	private int nbMax = 15;
	/**
	 * Si la liste est pleine
	 */
	private boolean estPleine = false;
	
	/**
	 * Constructeur
	 */
	public Scores()
	{
		super();
	}
	
	/**
	 * Ajoute un score
	 * 
	 * @param nom
	 * 	Nom du joueur
	 * @param score
	 * 	Score du joueur
	 * @param timeStamp
	 * 	TimeStamp de la partie
	 */
	public void ajouterScore(String nom, int score, long timeStamp)
	{
		boolean trouve = false;
		if (nom.length()>10)
			nom = nom.substring(0, 10);
		Score ajout = new Score(nom, score, timeStamp);
		int i;
		for (i=0; i<size() && !trouve; i++)
			if (get(i).getScore()<score)
				trouve = true;
		if (trouve)
		{
			add(i-1, ajout);
			if (estPleine)
			{
				Score dernier = get(size()-1);
				long stampDernier = dernier.getTimeStamp();
				if (!appartient(stampDernier))
				{
					File supprF = new File(Fenetre.cheminImage(stampDernier));
					supprF.delete();
				}
				removeLast();
			}
		}
		else if (!estPleine)
			addLast(ajout);
		estPleine = (size()==nbMax);
	}

	/**
	 * Retourne si la liste est pleine
	 * @return
	 * 	Si la liste est pleine
	 */
	public boolean isEstPleine() 
	{
		return estPleine;
	}

	public boolean appartient(long stamp) 
	{
		for (int i=0; i<size(); i++)
			if (get(i).getTimeStamp() == stamp)
				return true;
		return false;
	}
}

/**
 * Panneau montrant les meilleurs scores
 * 
 * @author Axel Schumacher
 *
 */
public class PanneauMeilleursScores extends JPanel 
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Scores montrés
	 */
	protected Scores scores = new Scores();
	/**
	 * Fenêtre principale
	 */
	protected Fenetre fenetre;
	
	/**
	 * Génère un panneau fils de la fenêtre
	 * 
	 * @param fenetre
	 * 	Fenêtre mère
	 */
	public PanneauMeilleursScores(Fenetre fenetre)
	{
		//Initialisation
		this.fenetre = fenetre;
		GridLayout layout = new GridLayout(16, 1);
		setLayout(layout);
		Dimension dimension = new Dimension(150, 200);
		setPreferredSize(dimension);
		setBorder(BorderFactory.createLineBorder(Color.black, 2));
		setBackground(Color.white);
		MAJ();
	}

	/**
	 * Ajoute un score
	 *  
	 * @param nom
	 * 	Nom du joueur
	 * @param score
	 * 	Score du joueur
	 * @param timeStamp
	 * 	TimeStamp de la partie
	 */
	public void ajouterScore(String nom, int score, long timeStamp)
	{
		scores.ajouterScore(nom, score, timeStamp);
		fenetre.sauverScores();
		MAJ();
	}

	/**
	 * Montre tous les scores de la liste
	 */
	public void MAJ()
	{
		removeAll();
		//Création du titre
		JLabel titre = new JLabel("Meilleurs Scores");
		titre.setFont(new Font("Arial", Font.BOLD, 16));
		add(titre);
		for (int i=0; i<scores.size(); i++)
		{
			//Récupération des variables
			Score s = scores.get(i);
			final String nom = s.getNom();
			final int pts = s.getScore();
			final long stamp = s.getTimeStamp();
			//Initialisation de la ligne à rajouter
			JPanel ligne = new JPanel();
			//Création du layout de la ligne
			BorderLayout layout= new BorderLayout();
			layout.setHgap(1);
			//Propriétés de la ligne
			ligne.setLayout(layout);
			ligne.setBackground(Color.white);
			//Label de résultats
			JLabel resultat = new JLabel(pts+" "+nom);
			resultat.setFont(new Font("Arial", Font.BOLD, 14));
			//Bouton pour battre le score
			JButton battre = new JButton("Battez moi!");
			battre.setMargin(new Insets(0, 0, 0, 0));
			battre.setForeground(Grille.getCouleurTexteTimeStamp(stamp));
			battre.setBackground(Grille.getCouleurTimeStamp(stamp));
			battre.setFocusable(false);
			battre.addActionListener
			(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0) 
					{	
						String texte = "<html>" +
										"<img src=\"file:" +
										Fenetre.cheminImage(stamp) +
										"\" " +
										"width=\"150\" " +
										"height=\"100\" ></img>" +
										"<br>" +
										"Voulez-vous tenter de battre les "+pts+" points de "+nom+" ?" +
										"</html>";
						int choix = JOptionPane.showConfirmDialog
						(
								null, 
								texte, 
								"Nouvelle partie", 
								JOptionPane.YES_NO_OPTION, 
								JOptionPane.WARNING_MESSAGE
						);
						if(choix == JOptionPane.OK_OPTION)
							fenetre.demarrerPartie(stamp, pts);
					}
				}
			);
			//ToolTip de battre
			String toolTip = "<html>" +
					"<img src=\"file:" +
					Fenetre.cheminImage(stamp) +
					"\" " +
					"width=\"150\" " +
					"height=\"100\"></img>" +
					"</html>";
			battre.setToolTipText(toolTip);
			//Ajout de la ligne
			ligne.add(resultat, "West");
			ligne.add(battre, "East");
			Dimension dim = new Dimension(150, 50);
			ligne.setPreferredSize(dim);
			add(ligne);
		}
	}

	/**
	 * Retourne vrai si le score est valable
	 * 
	 * @param score
	 * 	Score à tester
	 * @return
	 * 	true si le score est suffisament élevé pour entrer dans les meilleurs scores
	 * 	false sinon
	 */
	public boolean valable(int score)
	{
		if (!scores.isEstPleine())
			return true;
		if (scores.get(scores.size()-1).getScore()<score)
			return true;
		return false;
	}

	/**
	 * Retourne la liste de scores
	 * @return
	 * 	La liste de scores
	 */
	public Scores getScores() 
	{
		return scores;
	}

	/**
	 * Change la liste de scores
	 * 
	 * @param scores
	 * 	Nouvelle liste de scores
	 */
	public void setScores(Scores scores) 
	{
		this.scores = scores;
		MAJ();
	}
	
	/**
	 * Indique si le stamp appartient à la lsite de scores
	 */
	public boolean appartient(long stamp)
	{
		return scores.appartient(stamp);
	}
}
