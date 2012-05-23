package same;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Contient une grille de pion et l'affiche
 * 
 * @author Axel Schumacher
 */
public class Panneau extends JPanel
{
	private static final long serialVersionUID = 1L;

	/**
	 * Fenetre contenant le panneau
	 */
	private Fenetre fenetre;
	/**
	 * Grille affichée par le panneau
	 */
	private Grille grille;
	/**
	 * Groupe de pions séléctionné par l'utilisateur
	 */
	private ArrayList<Pion> groupeSelectionne = null;
	
	/**
	 * Situation actuelle
	 */
	private Situation situation;
	/**
	 * Historique des situations précédentes
	 */
	private Pile<Situation> situationsPassees;
	/**
	 * Historique des situations suivantes 
	 */
	private Pile<Situation> situationsFutures;
	
	/**
	 * Génère un panneau avec ces paramètres
	 * 
	 * @param nbColonnes
	 * 	Nombre de colonnes de la grille
	 * @param nbLignes
	 * 	Nombre de lignes de la grille
	 * @param nbCouleurs
	 * 	Nombre de couleurs de la grille
	 * @param fenetre
	 * 	Fenêtre principale
	 */
	public Panneau(int nbColonnes, int nbLignes, int nbCouleurs, Fenetre fenetre)
	{
		this.fenetre = fenetre;
		
		grille = new Grille(nbColonnes, nbLignes, nbCouleurs, fenetre);
		
		Dimension dimension = new Dimension(grille.getNbColonnes()*Pion.getTaille(), grille.getNbLignes()*Pion.getTaille());
		setPreferredSize(dimension);
		setMinimumSize(dimension);
		
		this.addMouseListener(new EcouteurSourisCliquee());
		this.addMouseMotionListener(new EcouteurSourisBougee());

		//Initialiser les gestionnaires de situation
		situation = new Situation(grille.getMatrice(), grille.getNbPions(), grille.getScore());
		situationsPassees = new Pile<Situation>();
		situationsFutures = new Pile<Situation>();
	}
	
	/**
	 * Surcharge de la méthode de dessin pour pouvoir
	 * dessiner la grille 
	 */
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		grille.dessine(g, groupeSelectionne);
	}
	
	/**
	 * Ecouteur d'un mouvement de souris
	 * Sélectionne le groupe sur lequel la souris se trouve
	 */
	class EcouteurSourisBougee extends MouseMotionAdapter
	{
		public void mouseMoved(MouseEvent e)
		{
			selectionnerGroupe(e);
		}
	}
	
	/**
	 * Ecouteur d'un clic de souris ou d'une sortie de souris
	 * Supprime le groupe sélectionné lors du clic
	 * Désélectionne le groupe lors de la sortie
	 */
	class EcouteurSourisCliquee extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			if (groupeSelectionne != null)
			{
				changerSituation();
				selectionnerGroupe(e);
				finDePartie();
			}
		}
		
		public void mouseExited(MouseEvent e)
		{
			groupeSelectionne = null;
			fenetre.getPanneauScore().MAJScoreSelection(0);
			repaint();
		}
	}
	
	/**
	 * Sélectionne le groupe sur lequel se trouve la souris
	 * 
	 * @param e
	 * 	Evênement souris concerné
	 */
	private void selectionnerGroupe(MouseEvent e) 
	{
		int x = e.getX();
		int y = e.getY();
		ArrayList<Pion> groupeSurvole = grille.voisins(grille.quelleCase(x,y));
		if (groupeSurvole != null && (groupeSelectionne == null || (Grille.estUnGroupe(groupeSurvole) && !groupeSelectionne.contains(groupeSurvole.get(0)))))
		{
			groupeSelectionne = groupeSurvole;
			fenetre.getPanneauScore().MAJScoreSelection((groupeSelectionne.size()-2)*(groupeSelectionne.size()-2));
			repaint();
		}
		else if (groupeSurvole == null)
		{
			groupeSelectionne = null;
			fenetre.getPanneauScore().MAJScoreSelection(0);
			repaint();
		}
	}

	/**
	 * Test si la partie est terminée et agit en conséquence
	 */
	private void finDePartie() 
	{
		long stamp = grille.getTimeStamp();
		int scoreABattre = 0;
		//Si la partie est terminée
		if (!grille.existeUnGroupe())
		{
			//Si on  peut entrer dans les scores
			if (fenetre.getOptions().getTaille() == 1 && fenetre.getOptions().getNbCouleurs() == 3)
			{
				grille.calculeScore(grille.getNbPions());
				if (grille.getNbPions() == 0)
					grille.calculeScore(1000);
				int score = grille.getScore();
				scoreABattre = grille.getScoreABattre();
				String texte = new String();
				String titre = new String();
				//Si le score est suffisament haut
				if (fenetre.getPanneauMeilleursScores().valable(score))
				{
					//Si on a battu le score à battre
					if (score > scoreABattre && scoreABattre != 0)
					{
						texte = "Félicitations ! Vous avez battu le score de "+scoreABattre+" précédemment inscrit\n" +
								"avec "+score+" points !\n" +
								"Entrez votre nom pour le sauvegarder et pouvoir le défier plus tard!";
						titre = "Record battu !";
						scoreABattre = score;
					}
					//Si on n'a pas battu le score à battre
					else if (scoreABattre != 0)
					{
						texte = "Félicitations ! Vous avez terminé cette partie !\n" +
								"Vous n'avez pas battu le score de "+scoreABattre+" points mais vous êtes cependant dans les 15 meilleurs scores ! \n" +
								"Votre score est : "+score+".\n" +
								"Entrez votre nom pour le sauvegarder !\n";
						titre = "Un des meilleurs scores !";
					}
					//Si on créé un nouveau score à battre
					else
					{
						texte = "Félicitations ! Vous avez terminé cette partie !\n" +
								"Votre score est : "+score+".\n" +
								"Entrez votre nom pour le sauvegarder et pouvoir le défier plus tard !\n";
						titre = "Meilleur score !";
						scoreABattre = score;
					}
					//On saisit le nom du joueur
					String nom = (String) JOptionPane.showInputDialog
					(
						null, 
						texte,
						titre, 
						JOptionPane.QUESTION_MESSAGE,
						null, 
						null, 
						(String)(fenetre.getOptions().getNom())
					);
					if (nom != null && nom.length()!=0)
					{
						fenetre.getPanneauMeilleursScores().ajouterScore(nom, score, stamp);
						fenetre.getOptions().setNom(nom);
						fenetre.getBONom().setText(nom);
						fenetre.sauverOptions();
					}
				}	
				//Sinon on se permet quelques sacarsmes
				else
					JOptionPane.showMessageDialog(null, "Dommage !\nVous ne pouvez pas entrer dans les meilleurs scores !\nRetentez votre chance !");
			}
			//Sinon on indique la marche à suivre pour pouvoir enregistrer son score
			else
				JOptionPane.showMessageDialog(null, "Félicitations ! Vous avez terminé cette partie ! Jouez en taille moyenne avec 3 couleurs pour pouvoir sauvegarder vos scores !");
			//Ensuite on relance cette partie avec le (nouveau?) score à battre
			fenetre.demarrerPartie(stamp, scoreABattre);
		}
	}

	/**
	 * Empile la situation précédente dans l'historique
	 * et met à jour la situation présente
	 */
	private void changerSituation()
	{
		//On empile
		situationsPassees.empiler(situation);
		//On supprime le groupe
		grille.supprimerGroupe(groupeSelectionne);
		repaint();
		groupeSelectionne = null;
		//On créé une situation représentant l'actuelle
		situation = new Situation(grille.getMatrice(), grille.getNbPions(), grille.getScore());
		//On vide la pile de situations futures
		if (!situationsFutures.estVide())
			situationsFutures = new Pile<Situation>();
		//On met à jour les boutons d'historique
		fenetre.MAJBoutons();
		
	}

	/**
	 * Indique si la pile de situation passées (précédentes) est vide
	 * 
	 * @return Si l'on peut revenir en arrière
	 */
	public boolean ilYASituationPassee()
	{
		return !situationsPassees.estVide();
	}
	
	/**
	 * Charge la dernière situation passée
	 */
	public void chargerSituationPassee()
	{
		//On dépile la dernière situation passée
		Situation derniereSituation = situationsPassees.depiler();
		//On empile la situation actuelle comme situation future
		situationsFutures.empiler(situation);
		situation = derniereSituation;
		//Et on met à jour la situation
		grille.chargerSituation(situation);
		repaint();
	}
	
	/**
	 * Indique si la pile de situations futures (suivante) est vide
	 * 
	 * @return Si l'on peut passer à la situation suivante
	 */
	public boolean ilYASituationFuture()
	{
		return !situationsFutures.estVide();
	}
	
	/**
	 * Charge la prochaine situation future
	 */
	public void chargerSituationFuture()
	{
		//On dépile la prochaine situation
		Situation prochaineSituation = situationsFutures.depiler();
		//On empile la situation actuelle dans le passé
		situationsPassees.empiler(situation);
		//On met à jour la situation
		situation = prochaineSituation;
		grille.chargerSituation(situation);
		repaint();
	}

	/**
	 * Retourne la grille
	 * 
	 * @return La grille de pions
	 */
	public Grille getGrille() 
	{
		return grille;
	}

	/**
	 * Change la pile de situations passées
	 * 
	 * @param situationsPassees
	 * 	Nouvelle pile de situations passées
	 */
	public void setSituationsPassees(Pile<Situation> situationsPassees) 
	{
		this.situationsPassees = situationsPassees;
	}

	/**
	 * Change la pile de situations futures
	 * 
	 * @param situationsFutures
	 * 	Nouvelle pile de situations futures
	 */
	public void setSituationsFutures(Pile<Situation> situationsFutures) 
	{
		this.situationsFutures = situationsFutures;
	}

	/**
	 * Change la situation actuelle
	 * 
	 * @param situation
	 * 	Nouvelle situation
	 */
	public void setSituation(Situation situation) 
	{
		this.situation = situation;
	}

	/**
	 * Retourne la pile de situation passées
	 * 
	 * @return
	 * 	La pile de situations passées
	 */
	public Pile<Situation> getSituationsPassees()
	{
		return situationsPassees;
	}

	/**
	 * Retourne la pile de situations futures
	 * 
	 * @return
	 * 	La pile de situations futures
	 */
	public Pile<Situation> getSituationsFutures()
	{
		return situationsFutures;
	}
}
