package same;

import java.util.ArrayList;

/**
 * Une simple structure de pile
 * 
 * @author Axel Schumacher
 *
 * @param <A>
 * 	Classe des �l�ments empil�s
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
	 * Empile l'�l�ment a
	 * 
	 * @param a
	 * 	El�ment � empiler
	 */
	public void empiler(A a)
	{
		if (a == null)
			System.err.println("On essaie d'empiler une situation nulle !");
		pile.add(a);
	}
	
	/**
	 * D�pile le premier �l�ment et le renvoie
	 * 
	 * @return
	 * 	L'�l�ment d�pil�
	 */
	public A depiler()
	{
		int index = pile.size()-1;
		if (index < 0)
			System.err.println("On essaie de d�piler une pile vide !");
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
