package same;

import java.io.File;
import java.io.Serializable;

/**
 * Options du jeu
 * 
 * @author Axel Schumacher
 *
 */
public class Options implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Nom du joueur
	 */
	private String nom = new String();
	/**
	 * Dossier courant du gestionnaire de sauvegardes
	 */
	private File dossierCourant = new File(".");
	/**
	 * Autoriser ou non l'historique
	 */
	private boolean autoriserHistorique = true;
	/**
	 * Taille de la fenetre
	 */
	private int taille = 1;
	/**
	 * Nombre de couleurs
	 */
	private int nbCouleurs = 3;

	/**
	 * Constructeur
	 */
	public Options()
	{}

	/**
	 * Change le nom par défaut du joueur
	 * @param nom
	 * 	Nouveau nom
	 */
	public void setNom(String nom) 
	{
		this.nom = nom;
	}

	/**
	 * Récupère le nom du joueur
	 * 
	 * @return
	 * 	Le nom joueur
	 */
	public String getNom() 
	{
		return nom;
	}

	/**
	 * Change le dossier courant
	 * 
	 * @param dossierCourant
	 * 	Nouveau dossier courant
	 */
	public void setDossierCourant(File dossierCourant) 
	{
		this.dossierCourant = dossierCourant;
	}

	/**
	 * Retourne le dossier courant
	 * @return
	 *	Le dossier courant
	 */
	public File getDossierCourant() 
	{
		return dossierCourant;
	}

	/**
	 * Changer l'autorisation d'utilisation d'historique
	 * 
	 * @param autoriserHistorique
	 * 	Nouvelle autorisation
	 */
	public void setAutoriserHistorique(boolean autoriserHistorique) 
	{
		this.autoriserHistorique = autoriserHistorique;
	}

	/**
	 * Retourne si l'historique est autorisé
	 * 
	 * @return
	 * 	Si l'historique est autorisé
	 */
	public boolean isAutoriserHistorique() 
	{
		return autoriserHistorique;
	}
	
	/**
	 * Retourne la taille de la partie
	 * 
	 * @return
	 * 	La taille de la partie
	 */
	public int getTaille() 
	{
		return taille;
	}

	/**
	 * Change la taille de la partie
	 * 
	 * @param taille
	 * 	Nouvelle taille comprise entre 0 et 2 inclus
	 */
	public void setTaille(int taille) 
	{
		this.taille = taille;
	}

	/**
	 * Retourne le nombre de couleurs de la partie
	 * 
	 * @return
	 * 	Le nombre de couleurs
	 */
	public int getNbCouleurs() 
	{
		return nbCouleurs;
	}

	/**
	 * Change le nombre de couleurs
	 * 
	 * @param nbCouleurs
	 * 	Nouveau nombre de couleurs
	 */
	public void setNbCouleurs(int nbCouleurs) 
	{
		this.nbCouleurs = nbCouleurs;
	}
}
