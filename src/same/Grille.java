package same;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Gère une grille de pion(matrice) et tous ses attributs
 * 
 * @author Axel Schumacher
 *
 */
public class Grille 
{
	/**
	 * Nombre de lignes de la grille
	 */
	private int nbLignes;
	/**
	 * Nombre de colonnes de la grille
	 */
	private int nbColonnes;
	/**
	 * Matrice de pions de la grille (ligne*colonne)
	 */
	private Pion matrice[][];
	/**
	 * TimeStamp de la partie en cours
	 */
	private long timeStamp;
	/**
	 * Nombre de couleurs de la grille
	 */
	private int nbCouleurs;
	/**
	 * Score actuel de la partie
	 */
	private int score = 0;
	/**
	 * Nombre de pions restant dans la grille
	 */
	private int nbPions;
	/**
	 * Fenetre contenant la grille (utile pour interagir avec panneau par exemple)
	 */
	private Fenetre fenetre;
	/**
	 * Score à battre de la partie en cours
	 */
	private int scoreABattre = 0;
	/**
	 * Timer gérant l'animation des boules séléctionnées
	 */
	private Timer timerAnimation = new Timer();
	/**
	 * Indicateur de position dans l'animation
	 */
	private int positionDansAnimation = 0;

	/**
	 * Génère une grille avec ces caractéristiques
	 * 
	 * @param nbLignes
	 * 	Nombre de lignes de la matrice
	 * @param nbColonnes
	 * 	Nombre de colonnes de la matrice
	 * @param nbCouleurs
	 * 	Nombre de couleurs différentes
	 * @param fenetre
	 * 	Fenetre principale
	 */
	public Grille(int nbLignes, int nbColonnes, int nbCouleurs, Fenetre fenetre)
	{
		this.fenetre = fenetre;
		
		//Vérification de la validité des lignes
		if (nbLignes>0)
			this.nbLignes = nbLignes;
		else
			this.nbLignes = 10;
		
		//Vérification de la validité des colonnes
		if (nbColonnes>0)
			this.nbColonnes = nbColonnes;
		else
			this.nbColonnes = 15;
		
		//Vérification de la validité des couleurs
		if (nbCouleurs>0 && nbCouleurs<=6)
			this.nbCouleurs = nbCouleurs;
		else
			this.nbCouleurs = 3;
		
		//Initialisation de la matrice
		matrice = new Pion[this.nbLignes][];
		for (int i=0; i<matrice.length; i++)
		{
			matrice[i] = new Pion[this.nbColonnes];
			for (int j=0; j<matrice[i].length; j++)
				matrice[i][j] = null;
		}
		
		//Remplissage de la grille aléatoirement
		timeStamp = System.currentTimeMillis();
		alea();
		nbPions = this.nbLignes*this.nbColonnes;
		
		//Mise a jour du nombre de pions
		fenetre.getPanneauScore().MAJNbPions(nbPions);
	}
	
	//////////////
	/* METHODES	*/
	//////////////
	
	/**
	 * Dessine la grille en appellant la méthode dessine de chaque pion
	 * 
	 * @param g
	 * 	Graphics à utiliser
	 * @param groupe
	 * 	Groupe de pions à animer 
	 */
	public void dessine(Graphics g, ArrayList<Pion> groupe)
	{
		for (int j=0; j<matrice.length; j++)
			for (int k=matrice[j].length-1; k>=0; k--)
				if (matrice[j][k] != null)
					matrice[j][k].dessine(g);

		//Lancement de l'animation du groupe si celui-ci n'est pas nul
		timerAnimation.cancel();
		timerAnimation = null;
		timerAnimation = new Timer();
		if (groupe != null && estUnGroupe(groupe))
			timerAnimation.schedule(new JouerAnimation(groupe), 0, 50);
	}

	/**
	 * Tâche du timer qui va lancer l'animation des boules sélectionnées
	 * 
	 */
	class JouerAnimation extends TimerTask
	{
		private ArrayList<Pion> groupe;
		public JouerAnimation(ArrayList<Pion> groupe)
		{
			this.groupe = groupe;
		}
		public void run() 
		{
			animerGroupe(groupe);
		}
	}
	
	/**
	 * Animation des boules sélectionnées
	 * 
	 * @param groupe
	 */
	public void animerGroupe(ArrayList<Pion> groupe)
	{
		Graphics g = fenetre.getPanneau().getGraphics();
		positionDansAnimation ++;
		positionDansAnimation = positionDansAnimation % 20;
		for (int i=0; i<groupe.size(); i++)
			groupe.get(i).dessine(g, positionDansAnimation);
		g.dispose();
	}
	
	/**
	 * Charge une situation donnée dans la grille
	 * 
	 * @param situation
	 * 	La situation à charger
	 */
	public void chargerSituation(Situation situation)
	{
		if (!chargerMatrice(situation.getMatrice()))
			System.err.println("Matrice non compatible lors du chargement de situation !");
		score = situation.getScore();
		nbPions = situation.getNbPionsRestant();
		fenetre.getPanneauScore().MAJScoreTotal(score, scoreABattre);
		fenetre.getPanneauScore().MAJNbPions(nbPions);
	}
	
	/**
	 * Charge une matrice donnée de pions
	 * 
	 * @param matrice
	 * 	La matrice à charger
	 * @return
	 * 	true si la matrice est compatible
	 * 	false sinon
	 */
	public boolean chargerMatrice(Pion[][] matrice)
	{
		int largeur = matrice.length;
		if (largeur != nbLignes)
			return false;
		int hauteur = matrice[0].length;
		if (hauteur != nbColonnes)
			return false;
		for (int i=0; i<nbLignes; i++)
			for (int j=0; j<nbColonnes; j++)
				if (matrice[i][j] != null)
					this.matrice[i][j] = new Pion
					(
						matrice[i][j].getCouleur(),
						matrice[i][j].getLigne(),
						matrice[i][j].getColonne()
					);
				else
					this.matrice[i][j] = null;
		return true;
	}

	/**
	 * Remplissage de la grille aléatoirement en fonction du timeStamp
	 **/
	public void alea()
	{
		Random R = new Random(timeStamp);
		int tailleTab = nbLignes * nbColonnes;
		int tab[] = new int[tailleTab];
		int tailleDecoupage = tailleTab / nbCouleurs;
		int couleur = 0;
		//Remplissage du tableau équitablement
		for (int i=0; i<tailleTab; i++)
		{
			if (i > (couleur+1)*tailleDecoupage && couleur+1<nbCouleurs)
			{
				couleur++;
			}
			tab[i] = couleur;
		}
		//Mélange du tableau
		for (int i=0; i<tailleTab; i++)
		{
			int indice = R.nextInt(tailleTab);
			int aux = tab[i];
			tab[i] = tab[indice];
			tab[indice] = aux;
		}
		//Distribution dans la grille du tableau
		for (int i=0; i<matrice.length; i++)
			for (int j=0; j<matrice[i].length; j++)
				matrice[i][j] = new Pion(tab[i*matrice.length + j], i, j);
	}
	
	/**
	 * Récupérer le Pion de la grille dans les coordonnées x et y
	 **/
	public Pion quelleCase(int x, int y)
	{
		Panneau panneau = fenetre.getPanneau();
		if (x<panneau.getWidth() && y<panneau.getHeight() && x>=0 && y>=0)
		{
			x = x/Pion.getTaille();
			y = y/Pion.getTaille();
			if (y>=matrice.length || x>=matrice[y].length)
				return null;
			else
				return matrice[y][x];
		}
		else
			return null;
	}
	
	/**
	 * Récupérer le Pion de la grille à la ligne lig et à la colonne col
	 **/
	public Pion quelPion(int lig, int col)
	{
		if (lig>=0 && lig<matrice.length && col>=0 && col<matrice[lig].length)
			return matrice[lig][col];
		else
			return null;
	}
	
	/**
	 * Récupère une liste des voisins d'un pion (y compris lui même)
	 **/
	public ArrayList<Pion> voisins(Pion pion)
	{
		if (pion == null)
				return null;
		ArrayList<Pion> retour = new ArrayList<Pion>();
		ajouterLesVoisins(pion, retour);
		return retour;
	}
	
	/**
	 * Rajoute les voisins du pion récursivement dans la liste des voisins
	 * 
	 * @param pion
	 * @param voisins
	 */
	private void ajouterLesVoisins(Pion pion, ArrayList<Pion> voisins)
	{
		//Si on a déja étudié ce pion
		if (voisins.contains(pion))
			return;
		voisins.add(pion);
		//Récupérons ses coordonnées
		int l = pion.getLigne();
		int c = pion.getColonne();
		Color couleurPion = pion.getCouleur();
		//Si il y a quelqu'un au dessus
		if (l>0)
		{
			Pion voisin = matrice[l-1][c];
			if (voisin != null && couleurPion.equals(voisin.getCouleur()))
				ajouterLesVoisins(voisin, voisins);
		}
		//Si il y a quelqu'un en dessous
		if (l<nbLignes-1)
		{
			Pion voisin = matrice[l+1][c];
			if (voisin != null && couleurPion.equals(voisin.getCouleur()))
				ajouterLesVoisins(voisin, voisins);
		}
		//Si il y a quelqu'un à gauche
		if (c>0)
		{
			Pion voisin = matrice[l][c-1];
			if (voisin != null && couleurPion.equals(voisin.getCouleur()))
				ajouterLesVoisins(voisin, voisins);
		}
		//Si il y a quelqu'un à droite
		if (c<nbColonnes-1)
		{
			Pion voisin = matrice[l][c+1];
			if (voisin != null && couleurPion.equals(voisin.getCouleur()))
				ajouterLesVoisins(voisin, voisins);
		}
	}
	
	/**
	 * Supprime le groupe de voisins de pion
	 **/
	public void supprimerGroupe(ArrayList<Pion> groupe)
	{
		if (estUnGroupe(groupe))
		{
			for (int i=0; i<groupe.size(); i++)
			{
				int l = groupe.get(i).getLigne();
				int c = groupe.get(i).getColonne();
				matrice[l][c] = null;
			}
			timerAnimation.cancel();
			tasserHorizontal();
			calculeScore((groupe.size()-2)*(groupe.size()-2));
			nbPions -= groupe.size();
			fenetre.getPanneauScore().MAJNbPions(nbPions);
		}
	}
	
	/**
	 * Vérifie si le tableau de pions est un groupe(au moins 2 éléments)
	 **/
	public static boolean estUnGroupe(ArrayList<Pion> pions)
	{
		if (pions == null)
			return false;
		return (pions.size() >= 2);
	}
	
	/**
	 * Tasse une colonne verticalement
	 **/
	private void tasserColonne(int i)
	{
		//Vérification de la validité de i
		if (i>=0 && i<nbColonnes)
		{
			boolean yaDesTrous = true;
			for (int j=nbLignes-1; j>=0 && yaDesTrous; j--)
			{
				if (matrice[j][i] == null)
				{
					boolean trouve = false;
					for (int k=j-1; k>=0 && !trouve; k--)
						if (matrice[k][i] != null)
						{
							matrice[j][i] = matrice[k][i];
							matrice[j][i].setLigne(j);
							matrice[k][i] = null;
							trouve = true;
						}
					if (!trouve)
						yaDesTrous = false;
				}
			}
		}
		else
			System.err.println("Demande d'une colonne inexistante : "+i);
	}
	
	/**
	 * Tasse toutes les colonnes verticalement
	 **/
	private void tasserVertical()
	{
		for (int i=0; i<nbColonnes; i++)
			tasserColonne(i);
	}
	
	/**
	 * Tasse toutes les colonnes vers la gauche en les tassant
	 * verticalement au préalable
	 */
	private void tasserHorizontal()
	{
		//On tasse chaque colonne vers le bas
		tasserVertical();
		for (int i=0; i<nbColonnes; i++)
		{
			//On regarde le pion le plus bas de la colonne lue
			Pion premierColonneI = matrice[nbLignes-1][i];
			//Si il est null, on estime que la colonne est vide
			if (premierColonneI == null)
			{
				//On va donc chercher une colonne non vide plus à droite
				boolean trouve = false;
				for (int j=i+1; j<nbColonnes && !trouve; j++)
				{
					Pion premierColonneJ = matrice[nbLignes-1][j];
					if (premierColonneJ != null)
					{
						//Si on a trouve une colonne non vide après la vide
						trouve = true;
						//On va juste copier les occurences non nulles de la colonne
						boolean termine = false;
						for (int k=nbLignes-1; k>=0 && !termine; k--)
							if (matrice[k][j] != null)
							{
								matrice[k][j].setColonne(i);
								matrice[k][i] = matrice[k][j];
								matrice[k][j] = null;
							}
							else
								termine = true;
					}
				}
			}
		}
	}
	
	/**
	 * Calcule le score et l'envoie au panneauScore afin qu'il l'affiche
	 */
	public void calculeScore(int ajout)
	{
		score += ajout;
		fenetre.getPanneauScore().MAJScoreTotal(score, scoreABattre);
	}
	
	/**
	 * Vérifie si il existe un groupe dans le jeu (pour terminer la partie
	 */
	public boolean existeUnGroupe()
	{
		//Si il n'y a plus de pions il n'y a pas de groupe
		if (nbPions == 0)
			return false;
		//On se déplace de gauche à droite en cherchant des colonnes non vides
		boolean trouveColonne = true;
		for (int i=0; i<nbColonnes && trouveColonne; i++)
			if (matrice[nbLignes-1][i] == null)
				//A la première colonne vide on arrête
				trouveColonne = false;
			else
			{
				//Si on trouve une colonne non vide,
				//on regarde de bas en haut si il exite au moins un pion
				//appartenant à un groupe
				boolean trouvePion = true;
				for (int j=nbLignes-1; j>=0 && trouvePion; j--)
				{
					Pion pion = matrice[j][i];
					if (pion == null)
						trouvePion = false;
					//Si on en trouve ne serais-ce qu'un seul on a gagné
					else if (estUnGroupe(voisins(pion)))
						return true;
				}
			}
		return false;
	}
	
	/**
	 * Démarre une partie avec le TimeStamp donnée en paramètre
	 * 
	 * @param stamp
	 */
	public void demarrer(long stamp)
	{
		timeStamp = stamp;
		alea();
		score = 0;
		nbPions = this.nbLignes*this.nbColonnes;
		setScoreABattre(0);
		fenetre.getPanneau().repaint();
	}
	
	/**
	 * Démarre une partie avec le TimeStamp et le score à battre
	 * 
	 * @param stamp
	 * @param ancienScore
	 */
	public void demarrer(long stamp, int ancienScore)
	{
		demarrer(stamp);
		setScoreABattre(ancienScore);
	}
	
	//////////////////////////////////
	/* ACCESSEURS ET MODIFICATEURS	*/
	//////////////////////////////////

	/**
	 * Retourne le nombre de pions restant
	 */
	public int getNbPions() 
	{
		return nbPions;
	}

	/**
	 * Retourne le TimeStamp de la partie en cours
	 * 
	 * @return
	 * 	Le TimeStamp de la partie
	 */
	public long getTimeStamp() 
	{
		return timeStamp;
	}
	
	/**
	 * Récupère la couleur caractéristique du TimeStamp donné en paramètre
	 * 
	 * @param stamp
	 * @return
	 * 	La couleur associée au TimeStamp
	 */
	static public Color getCouleurTimeStamp(long stamp)
	{
		Random R = new Random(stamp);
		int rouge = R.nextInt(256);
		int vert = R.nextInt(256);
		int bleu = R.nextInt(256);
		return new Color(rouge, vert, bleu);
	}
	
	/**
	 * Récupère la couleur du texte la plus visible sur 
	 * la couleur caractéristique du TimeStamp
	 * 
	 * @param stamp
	 * @return
	 * 	La couleur du texte associé au TimeStamp
	 */
	static public Color getCouleurTexteTimeStamp(long stamp)
	{
		Random R = new Random(stamp);
		int rouge = R.nextInt(256);
		int vert = R.nextInt(256);
		int bleu = R.nextInt(256);
		int gamme = 255 - (rouge+vert+bleu)/3;
		if (gamme < 175)
			gamme = 0;
		else
			gamme = 255;
		return new Color(gamme, gamme, gamme);
	}

	/**
	 * Renvoie le score actuel
	 * 
	 * @return
	 * 	Le Score actuel
	 */
	public int getScore() 
	{
		return score;
	}

	/**
	 * Change le score à battre
	 * 
	 * @param scoreABattre
	 * 	Nouveau score à battre
	 */
	public void setScoreABattre(int scoreABattre) 
	{
		this.scoreABattre = scoreABattre;
	}

	/**
	 * Renvoie le score à battre
	 * 
	 * @return
	 * 	Le Score à battre
	 */
	public int getScoreABattre() 
	{
		return scoreABattre;
	}
	
	/**
	 * Retourne une copie en profondeur de la matrice
	 */
	public Pion[][] getMatrice() 
	{
		Pion[][] retour = new Pion[nbLignes][];
		for (int i=0; i<nbLignes; i++)
		{
			retour[i] = new Pion[nbColonnes];
			for (int j=0; j<nbColonnes; j++)
				if (matrice[i][j] != null)
					retour[i][j] = new Pion
					(
							matrice[i][j].getCouleur(),
							matrice[i][j].getLigne(),
							matrice[i][j].getColonne()
					);
				else
					retour[i][j] = null;
		}
		return retour;
	}

	/**
	 * Renvoie le nombre de lignes de la matrice
	 * 
	 * @return
	 * 	Le nombre de lignes de la matrice
	 */
	public int getNbLignes()
	{
		return nbLignes;
	}

	/**
	 * Renvoie le nombre de colonnes de la matrice
	 * 
	 * @return
	 * 	Le nombre de colonnes de la matrice
	 */
	public int getNbColonnes()
	{
		return nbColonnes;
	}
}
