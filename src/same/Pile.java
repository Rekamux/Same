package same;

import java.util.ArrayList;

/**
 * Une simple structure de pile
 * 
 * @author Axel Schumacher
 *
 * @param <A>
 * 	Classe des éléments empilés
 */
public class Pile<A>
{
	/**
	 * Pile
	 */
	private ArrayList<A> pile;
	
	/**
	 * Constructeur
	 */
	public Pile()
	{
		pile = new ArrayList<A>();
	}
	
	/**
	 * Empile l'élément a
	 * 
	 * @param a
	 * 	Elément à empiler
	 */
	public void empiler(A a)
	{
		if (a == null)
			System.err.println("On essaie d'empiler une situation nulle !");
		pile.add(a);
	}
	
	/**
	 * Dépile le premier élément et le renvoie
	 * 
	 * @return
	 * 	L'élément dépilé
	 */
	public A depiler()
	{
		int index = pile.size()-1;
		if (index < 0)
			System.err.println("On essaie de dépiler une pile vide !");
		A retour = pile.get(index);
		pile.remove(index);
		return retour;
	}
	
	/**
	 * Retourne si la pile est vide
	 * 
	 * @return
	 * 	Si la pile est vide
	 */
	public boolean estVide()
	{
		return pile.isEmpty();
	}
}
