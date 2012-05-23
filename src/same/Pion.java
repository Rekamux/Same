
package same;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Un pion est constitué d'une couleur et d'une position géographique.
 * 
 * @author Axel Schumacher
 *
 */
public class Pion 
{
	/**
	 * Taille en pixels d'un pion
	 */
	static private int taille = 45;
	/**
	 * Liste des couleurs
	 */
	static protected Color[] couleurs = 
	{
		Color.red,
		Color.green,
		Color.blue,
		Color.yellow,
		Color.magenta,
		Color.pink
	};
	/**
	 * Liste des dégradés de chaque couleur
	 */
	static private Color[][] couleursCercles = new Color[Pion.couleurs.length][];
	/**
	 * Couleur du pion
	 */
	private Color couleur;
	/**
	 * Numéro de cette couleur dans le tableau des couleurs
	 */
	private int numCouleur;
	/**
	 * Abscisse du pion dans le panneau
	 */
	private int abscisse;
	/**
	 * Ordonnée du pion dans le panneau
	 */
	private int ordonnee;
	/**
	 * I ème ligne dans la matrice
	 */
	private int ligne;
	/**
	 * J ème colonne dans la matrice
	 */
	private int colonne;
	
	/**
	 * Initialisation des couleurs du cercle
	 * Ceci pour obtenir un dégradé
	 */
	{
		for (int i=0; i<Pion.couleurs.length; i++)
		{
			couleursCercles[i] = new Color[4];
			couleursCercles[i][0] = Pion.couleurs[i];
		
			//Récupération des couleurs composantes
			int rouge = couleursCercles[i][0].getRed();
			int vert = couleursCercles[i][0].getGreen();
			int bleu = couleursCercles[i][0].getBlue();
		
			//Création des dégradés
			couleursCercles[i][1] = new Color(rouge+(255-rouge)/3, vert+(255-vert)/3, bleu+(255-bleu)/3);
			couleursCercles[i][2] = new Color(rouge+2*(255-rouge)/3, vert+2*(255-vert)/3, bleu+2*(255-bleu)/3);
			couleursCercles[i][3] = new Color(rouge+5*(255-rouge)/6, vert+5*(255-vert)/6, bleu+5*(255-bleu)/6);
		}
	}
	
	/**
	 * Génère un pion
	 * 
	 * @param couleur 
	 * 	Indice dans le tableau des couleurs
	 * @param ligne 
	 * 	Ligne dans la matrice
	 * @param colonne
	 * 	Colonne dans la matrice
	 */
	public Pion(int couleur, int ligne, int colonne)
	{
		this.couleur = couleurs[couleur];
		this.numCouleur = couleur;
		setLigne(ligne);
		setColonne(colonne);
	}
	
	/**
	 * Génère un pion
	 * 
	 * @param couleur 
	 * 	Couleur qui doit se trouver dans le tableau
	 * @param ligne 
	 * 	Ligne dans la matrice
	 * @param colonne 
	 * 	Colonne dans la matrice
	 */
	public Pion(Color couleur, int ligne, int colonne)
	{
		this.couleur = couleur;
		setLigne(ligne);
		setColonne(colonne);
		numCouleur = getNumCouleur(couleur);
	}

	/**
	 * Dessine le pion avec g
	 * 
	 * @param g
	 * 	Graphics en cours d'utilisation
	 * 	Attention ne le libère pas à la fin!
	 */
	public void dessine(Graphics g)
	{
		Color[] couls = couleursCercles[numCouleur];
		g.setColor(couls[0]);
		g.fillOval(abscisse, ordonnee, taille, taille);
		g.setColor(couls[1]);
		g.fillOval(abscisse+2*taille/20, ordonnee+2*taille/20, 4*taille/5, 4*taille/5);
		g.setColor(couls[2]);
		g.fillOval(abscisse+3*taille/20, ordonnee+3*taille/20, 3*taille/5, 3*taille/5);
		g.setColor(couls[3]);
		g.fillOval(abscisse+4*taille/20, ordonnee+4*taille/20, 2*taille/5, 2*taille/5);
	}

	/**
	 * Dessine le pion selon l'indice d'animation
	 * 
	 * @param g
	 * 	Graphics en cours d'utilisation
	 * 	Attention ne le libère pas à la fin!
	 * @param p
	 * 	Indice d'avancement dans l'animation
	 */
	public void dessine(Graphics g, int p)
	{
		dessine(g);
		g.setColor(Color.black);
		int abscisseRectangle;
		int largeurCercle;
		int angleDepart;
		if (p == 0)
			return;
		if (p>=10)
		{
			largeurCercle = (p-10)*taille/10;
			abscisseRectangle = abscisse+(taille-largeurCercle)/2;
			angleDepart = 270;
		}
		else
		{
			largeurCercle = (10-p)*taille/10;
			abscisseRectangle = abscisse+(taille-largeurCercle)/2;
			angleDepart = 90;
		}
		g.drawArc
		(
			abscisseRectangle,
			ordonnee,
			largeurCercle,
			taille-1,
			angleDepart,
			180
		);
	}

	/**
	 * Un court descriptif des caractéristiques du pion
	 */
	public String toString()
	{
		String r = "Je suis le pion positionné en "+abscisse+"x"+ordonnee+"\n";
		r += "Je suis en ligne "+ligne+" et en colonne "+colonne+" et ma couleur est le "+couleur;
		return r;
	}
	
	//////////////////////////////////////
	/*	ACCESSEURS	ET MODIFICATEURS	*/
	//////////////////////////////////////
	
	
	/**
	 * Retourne la couleur du pion
	 * 
	 * @return
	 * 	La couleur du pion
	 */	
	public Color getCouleur()
	{
		return couleur;
	}
	
	/**
	 * Modifie la ligne du pion dans la matrice
	 * 
	 * @param ligne
	 * 	Nouvelle ligne
	 */
	public void setLigne(int ligne)
	{
		ordonnee = ligne*taille;
		this.ligne = ligne;
	}
	
	/**
	 * Retourne la ligne du pion
	 * 
	 * @return
	 * 	La ligne du pion
	 */
	public int getLigne()
	{
		return ligne;
	}
	
	/**
	 * Modifie la colonne du pion dans la matrice
	 * 
	 * @param colonne
	 * 	Nouvelle colonne
	 */
	public void setColonne(int colonne)
	{
		abscisse = colonne*taille;
		this.colonne = colonne;
	}
	
	/**
	 * Retourne la colonne du pion
	 * 
	 * @return
	 * 	La colonne du pion
	 */
	public int getColonne()
	{
		return colonne;
	}
	
	/**
	 * Retourne la taille de tous les pions
	 * 
	 * @return
	 * 	La taille de tous les pions
	 */
	public static int getTaille() 
	{
		return taille;
	}
	
	/**
	 * Retourne l'indice de la couleur du pion
	 * 
	 * @param couleur
	 * 	Couleur dont on cherche l'indice
	 * @return
	 * 	-1 si la couleur n'est pas dans le tableau
	 * 	son indice sinon
	 */
	static private int getNumCouleur(Color couleur)
	{
	
		int i=0;
		boolean trouve = false;
		do
			if (couleur.equals(couleurs[i]))
				trouve = true;
			else
				i++;
		while (i<couleurs.length && !trouve);
		if (trouve)
			return i;
		else
			return -1;
	}
}
