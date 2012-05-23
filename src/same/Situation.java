package same;

/**
 * Situation donnée dans le jeu
 * 
 * @author Axel Schumacher
 *
 */
public class Situation 
{
	/**
	 * Matrice de pions
	 */
	private Pion[][] matrice;
	/**
	 * Nombre de pions restant
	 */
	private int nbPionsRestant;
	/**
	 * Score actuel de la partie
	 */
	private int score;
	
	/**
	 * Constructeur
	 * 
	 * @param matrice
	 * 	Matrice actuelle
	 * @param nbPionsRestant
	 * 	Nombre de pions restant
	 * @param score
	 * 	Score de la partie
	 */
	public Situation(Pion[][] matrice, int nbPionsRestant, int score) 
	{
		this.matrice = matrice;
		this.nbPionsRestant = nbPionsRestant;
		this.score = score;
	}

	/**
	 * Retourne la matrice de pions
	 * 
	 * @return
	 * 	La matrice de pions
	 */
	public Pion[][] getMatrice() 
	{
		return matrice;
	}

	/**
	 * Retourne le nombre de pions restant
	 * 
	 * @return
	 * 	Le nombre de pions restant
	 */
	public int getNbPionsRestant() 
	{
		return nbPionsRestant;
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
}
