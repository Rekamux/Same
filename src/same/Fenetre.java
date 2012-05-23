package same;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

/**
 * Fenêtre principale du jeu
 * 
 * @author Axel Schumacher
 *
 */
public class Fenetre extends JFrame
{
	private static final long serialVersionUID = 1L;

	/**
	 * Panneau indicatif en bas de la fenêtre
	 */
	private PanneauScore panneauScore = new PanneauScore();
	/**
	 * Options du jeu
	 */
	private Options options = new Options();
	/**
	 * Panneau des meilleurs scores
	 */
	private PanneauMeilleursScores panneauMeilleursScores = new PanneauMeilleursScores(this);
	/**
	 * Panneau du jeu
	 */
	private Panneau panneau = new Panneau(10, 15, 5, this);

	/**
	 * Panneau déroulant du panneau
	 */
	private JScrollPane scrollPanneau = new JScrollPane(panneau);
	/**
	 * Panneau déroulant des meilleurs scores
	 */
	private JScrollPane scrollPMS = new JScrollPane(panneauMeilleursScores);
	/**
	 * Séparation entre le panneau du jeu et le panneau des meilleurs scores
	 */
	private JSplitPane separationPanPMS = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPanneau, scrollPMS);
	
	/**
	 * Barre de menu du jeu
	 */
	private JMenuBar barreMenu = new JMenuBar();
	
	/**
	 * Menu principal
	 */
	private JMenu partie = new JMenu("Partie");
	/**
	 * Elément de menu de nouvelle partie
	 */
	private JMenuItem nouvellePartie = new JMenuItem("Nouvelle partie");
	/**
	 * Elément de menu pour recommencer la même partie
	 */
	private JMenuItem recommencer = new JMenuItem("Recommencer cette partie");
	/**
	 * Elément de menu pour sauvegarder la partie
	 */
	private JMenuItem sauvegarder = new JMenuItem("Sauvegarder cette partie");
	/**
	 * Elément de menu pour charger une partie sauvegardée
	 */
	private JMenuItem charger = new JMenuItem("Charger une partie");
	/**
	 * Element de menu pour quitter le programme
	 */
	private JMenuItem quitter = new JMenuItem("Quitter");
	
	/**
	 * Menu des options
	 */
	private JMenu menuOptions = new JMenu("Options");
	/**
	 * Bouton d'options pour autoriser l'historique
	 */
	private JCheckBoxMenuItem autoriserHistorique = new JCheckBoxMenuItem("Autoriser l'historique");
	/**
	 * Menu pour chosir le nombre de couleurs
	 */
	private JMenu menuOptionsNbCouleurs = new JMenu("Nombre de couleurs");
	/**
	 * Boutons pour choisir le nombre de couleurs
	 */
	private JRadioButtonMenuItem 
		troisCouleurs = new JRadioButtonMenuItem("3"),
		quatreCouleurs = new JRadioButtonMenuItem("4"),
		cinqCouleurs = new JRadioButtonMenuItem("5"),
		sixCouleurs = new JRadioButtonMenuItem("6");
	/**
	 * Menu pour choisir la taille du jeu
	 */
	private JMenu menuOptionsTaille = new JMenu("Taille du plateau");
	/**
	 * Boutons pour choisir la taille du jeu
	 */
	private JRadioButtonMenuItem
		petiteTaille = new JRadioButtonMenuItem("Petit"),
		moyenneTaille = new JRadioButtonMenuItem("Moyen"),
		grandeTaille = new JRadioButtonMenuItem("Grand");
	/**
	 * Groupe des boutons de choix de couleur
	 */
	private ButtonGroup groupeCouleurs = new ButtonGroup();
	/**
	 * Groupe des boutons de choix de taille
	 */
	private ButtonGroup groupeTailles = new ButtonGroup();
	
	/**
	 * Menu de gestion de l'historique
	 */
	private JMenu menuHistorique = new JMenu("Historique");
	/**
	 * Menu pour aller à la situation suivante
	 */
	private JMenuItem suivant = new JMenuItem("Suivant");
	/**
	 * Menu pour aller à la situation précédente
	 */
	private JMenuItem precedent = new JMenuItem("Précédent");
	
	/**
	 * Menu d'aide
	 */
	private JMenu menuAide = new JMenu("Aide");
	/**
	 * Menu d'aide
	 */
	private JMenuItem aide = new JMenuItem("Aide");
	
	/**
	 * Barre d'outils du jeu
	 */
	private JToolBar barreOutils = new JToolBar();
	/**
	 * Bouton de la barre d'outils pour commencer une nouvelle partie
	 */
	private JButton BONouvellePartie = new JButton();
	/**
	 * Bouton de la barre d'outils pour recommencer la même partie
	 */
	private JButton BORecommencer = new JButton();
	/**
	 * Bouton de la barre d'outils pour charger une partie
	 */
	private JButton BOCharger = new JButton();
	/**
	 * Bouton de la barre d'outils pour montrer les règles du jeu
	 */
	private JButton BOAide = new JButton();
	/**
	 * Bouton de la barre d'outils pour aller à la situation précédente
	 */
	private JButton BOPrecedent = new JButton();
	/**
	 * Bouton de la barre d'outils pour aller à la situation suivante
	 */
	private JButton BOSuivant = new JButton();
	/**
	 * Label de la barre d'outils pour titrer BOLabelNom
	 */
	private JLabel BOLabelNom = new JLabel("Nom du joueur");
	/**
	 * Champ de texte de la barre d'outils pour saisir le nom du joueur
	 */
	private JTextField BONom = new JTextField();
	/**
	 * Bouton de la barre d'outils pour sauver la partie
	 */
	private JButton BOSauver = new JButton("Partie en cours - Cliquez pour sauvegarder");
	
	/**
	 * Fichier de sauvegarde des options
	 */
	private File fichierOptions = new File("options");
	/**
	 * Fichier de sauvegarde des meilleurs scores
	 */
	private File fichierScores = new File("scores");
	
	/**
	 * Génère une fenêtre de jeu
	 */
	public Fenetre()
	{
		//Initialisation des caractéristiques de base
		setTitle("Same");
		setSize(900, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		//Gestion du layout
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		add(separationPanPMS, BorderLayout.CENTER);
		add(panneauScore, BorderLayout.SOUTH);
		add(barreOutils, BorderLayout.NORTH);
		
		//Chargement des fichiers
		chargerScores();
		chargerOptions();
		
		//Initialisations diverses des panneaux
		initialiseBarreMenu();
		initialiseBarreOutils();
		
		//Mise à jour des boutons
		MAJBoutons();
	}

	/**
	 * Ecouteur du bouton nouvelle partie
	 */
	private ActionListener ecouteurNouvellePartie = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0) 
		{
			demarrerPartie(System.currentTimeMillis());
		}
	};
	/**
	 * Ecouteur du bouton recommencer
	 */
	private ActionListener ecouteurRecommencer = new ActionListener()
	{
		public void actionPerformed(ActionEvent e) 
		{
			demarrerPartie(panneau.getGrille().getTimeStamp());
		}
		
	};
	/**
	 * Ecouteur du bouton sauvegarder
	 */
	private ActionListener ecouteurSauvegarder = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			sauverPartie();
		}
			
	};
	/**
	 * Ecouteur du bouton charger
	 */
	private ActionListener ecouteurCharger = new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0)
		{
			chargerPartie();
		}
			
	};
	/**
	 * Ecouteur du bouton aide
	 */
	private ActionListener ecouteurAide = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			montrerAide();
		}
	};
	/**
	 * Ecouteur du bouton quitter
	 */
	private ActionListener ecouteurQuitter = new ActionListener()
	{
		public void actionPerformed(ActionEvent e) 
		{
			System.exit(0);
		}
	};
	/**
	 * Ecouteur des changement effectués dans l'édition du nom de joueur
	 */
	private KeyListener ecouteurChangerNom = new KeyListener()
	{
		public void keyTyped(KeyEvent arg0)
		{
			options.setNom(BONom.getText());
		}
		public void keyPressed(KeyEvent arg0) 
		{
			options.setNom(BONom.getText());
		}
		public void keyReleased(KeyEvent arg0) 
		{
			options.setNom(BONom.getText());
		}
	};
	/**
	 * Ecouteur de l'action suivant dans l'historique
	 */
	private ActionListener ecouteurSuivant = new ActionListener()
	{
		public void actionPerformed(ActionEvent e) 
		{
			if (panneau.ilYASituationFuture())	
				panneau.chargerSituationFuture();
			MAJBoutons();
		}
	};
	/**
	 * Ecouteur de l'action précédent dans l'historique
	 */
	private ActionListener ecouteurPrecedent = new ActionListener()
	{
		public void actionPerformed(ActionEvent e) 
		{
			if (panneau.ilYASituationPassee())
				panneau.chargerSituationPassee();
			MAJBoutons();
		}
	};
	/**
	 * Ecouteur de l'action autoriser historique
	 */
	private ActionListener ecouteurAutoriserHistorique = new ActionListener()
	{
		public void actionPerformed(ActionEvent e) 
		{
			options.setAutoriserHistorique(autoriserHistorique.getState());
			MAJBoutons();
			sauverOptions();
		}
	};
	/**
	 * Ecouteur de l'action changer le nombre de couleurs
	 */
	private ActionListener ecouteurNbCouleurs = new ActionListener()
	{
		public void actionPerformed(ActionEvent e) 
		{
			String texte = ((JRadioButtonMenuItem)e.getSource()).getText();
			if (texte.equals("3"))
				options.setNbCouleurs(3);
			else if (texte.equals("4"))
				options.setNbCouleurs(4);
			else if (texte.equals("5"))
				options.setNbCouleurs(5);
			else if (texte.equals("6"))
				options.setNbCouleurs(6);
			else
				System.err.println("Nombre de couleurs incompréhensible");
			sauverOptions();
			int nbLignes = getNbLignesDeTaille(options.getTaille());
			int nbColonnes = getNbColonnesDeTaille(options.getTaille());
			initialiserPanneau(nbLignes, nbColonnes, options.getNbCouleurs());
		}
	};
	/**
	 * Ecouteur de l'action changer la taille du jeu
	 */
	private ActionListener ecouteurTaille = new ActionListener()
	{
		public void actionPerformed(ActionEvent e) 
		{
			String texte = ((JRadioButtonMenuItem)e.getSource()).getText();
			if (texte.equals("Petit"))
				options.setTaille(0);
			else if (texte.equals("Moyen"))
				options.setTaille(1);
			else if (texte.equals("Grand"))
				options.setTaille(2);
			else
				System.err.println("Taille du plateau incompréhensible");
			MAJTaille();
			sauverOptions();
			int nbLignes = getNbLignesDeTaille(options.getTaille());
			int nbColonnes = getNbColonnesDeTaille(options.getTaille());
			initialiserPanneau(nbLignes, nbColonnes, options.getNbCouleurs());
		}
	};
	
	/**
	 * Initialise la barre des menus
	 */
	private void initialiseBarreMenu()
	{
		setJMenuBar(barreMenu);
		
		nouvellePartie.addActionListener(ecouteurNouvellePartie);
		recommencer.addActionListener(ecouteurRecommencer);
		sauvegarder.addActionListener(ecouteurSauvegarder);
		charger.addActionListener(ecouteurCharger);
		quitter.addActionListener(ecouteurQuitter);
		
		barreMenu.add(partie);
		partie.add(nouvellePartie);
		partie.add(recommencer);
		partie.add(sauvegarder);
		partie.add(charger);
		partie.add(quitter);
		
		autoriserHistorique.addActionListener(ecouteurAutoriserHistorique);
		troisCouleurs.addActionListener(ecouteurNbCouleurs);
		quatreCouleurs.addActionListener(ecouteurNbCouleurs);
		cinqCouleurs.addActionListener(ecouteurNbCouleurs);
		sixCouleurs.addActionListener(ecouteurNbCouleurs);
		petiteTaille.addActionListener(ecouteurTaille);
		moyenneTaille.addActionListener(ecouteurTaille);
		grandeTaille.addActionListener(ecouteurTaille);
		
		groupeCouleurs.add(troisCouleurs);
		groupeCouleurs.add(quatreCouleurs);
		groupeCouleurs.add(cinqCouleurs);
		groupeCouleurs.add(sixCouleurs);
		
		groupeTailles.add(petiteTaille);
		groupeTailles.add(moyenneTaille);
		groupeTailles.add(grandeTaille);
			
		barreMenu.add(menuOptions);
		menuOptions.add(autoriserHistorique);
		menuOptions.add(menuOptionsNbCouleurs);
		menuOptionsNbCouleurs.add(troisCouleurs);
		menuOptionsNbCouleurs.add(quatreCouleurs);
		menuOptionsNbCouleurs.add(cinqCouleurs);
		menuOptionsNbCouleurs.add(sixCouleurs);
		menuOptions.add(menuOptionsTaille);
		menuOptionsTaille.add(petiteTaille);
		menuOptionsTaille.add(moyenneTaille);
		menuOptionsTaille.add(grandeTaille);
		
		precedent.addActionListener(ecouteurPrecedent);
		suivant.addActionListener(ecouteurSuivant);
		
		barreMenu.add(menuHistorique);
		menuHistorique.add(precedent);
		menuHistorique.add(suivant);
		
		barreMenu.add(menuAide);
		menuAide.add(aide);
		aide.addActionListener(ecouteurAide);
	}
	
	/**
	 * Initialise la barre d'outils
	 */
	private void initialiseBarreOutils()
	{
		Color couleurPartie = Grille.getCouleurTimeStamp(panneau.getGrille().getTimeStamp());
		Color couleurTexte = Grille.getCouleurTexteTimeStamp(panneau.getGrille().getTimeStamp());
		ImageIcon icone = new ImageIcon("images/Same.png");
		
		BONouvellePartie.addActionListener(ecouteurNouvellePartie);
		BONouvellePartie.setIcon(icone);
		BONouvellePartie.setBackground(Color.white);
		BONouvellePartie.setFocusable(false);
		BONouvellePartie.setMargin(new Insets(0, 0, 0, 0));
		BONouvellePartie.setToolTipText("Démarrer une nouvelle partie");
		
		BORecommencer.setIcon(icone);
		BORecommencer.setBackground(couleurPartie);
		BORecommencer.addActionListener(ecouteurRecommencer);
		BORecommencer.setFocusable(false);
		BORecommencer.setMargin(new Insets(0, 0, 0, 0));
		BORecommencer.setToolTipText("Recommencer cette partie");
		
		BOCharger.setIcon(new ImageIcon("images/charger.png"));
		BOCharger.setFocusable(false);
		BOCharger.setMargin(new Insets(0, 0, 0, 0));
		BOCharger.addActionListener(ecouteurCharger);
		BOCharger.setToolTipText("Charger une partie sauvegardée");
		
		BOAide.setIcon(new ImageIcon("images/aide.png"));
		BOAide.setFocusable(false);
		BOAide.setMargin(new Insets(0, 0, 0, 0));
		BOAide.addActionListener(ecouteurAide);
		
		BOPrecedent.setIcon(new ImageIcon("images/precedent.png"));
		BOPrecedent.setFocusable(false);
		BOPrecedent.setMargin(new Insets(0, 0, 0, 0));
		BOPrecedent.addActionListener(ecouteurPrecedent);
		BOSuivant.setIcon(new ImageIcon("images/suivant.png"));
		BOSuivant.setFocusable(false);
		BOSuivant.setMargin(new Insets(0, 0, 0, 0));
		BOSuivant.addActionListener(ecouteurSuivant);
		
		BONom.addKeyListener(ecouteurChangerNom);
		BONom.setText(options.getNom());
		BONom.setColumns(10);

		BOSauver.setBackground(couleurPartie);
		BOSauver.setForeground(couleurTexte);
		BOSauver.setFocusable(false);
		BOSauver.addActionListener(ecouteurSauvegarder);

		barreOutils.add(BONouvellePartie);
		barreOutils.add(BORecommencer);
		barreOutils.add(BOCharger);
		barreOutils.add(BOAide);
		barreOutils.addSeparator();
		barreOutils.add(BOPrecedent);
		barreOutils.add(BOSuivant);
		barreOutils.addSeparator();
		barreOutils.add(BOLabelNom);
		barreOutils.add(BONom);
		barreOutils.addSeparator();
		barreOutils.add(BOSauver);
	}
	
	/**
	 * Réinitialise le panneau de jeu en cas de changement d'options
	 * 
	 * @param nbLignes
	 * 	Nombre de lignes de la grille
	 * @param nbColonnes
	 * 	Nombre de colonnes de la grille
	 * @param nbCouleurs
	 * 	Nombre de couleurs de la grille
	 */
	private void initialiserPanneau(int nbLignes, int nbColonnes, int nbCouleurs)
	{
		//Debug
		//Sans cette instruction à première vue inutile,
		//La fenêtre se bloque jusqu'à ce qu'on lui demande
		//une taille différente.
		//////////////////////////
		panneau = new Panneau(1, 1, 1, this);
		//////////////////////////
		
		panneau = new Panneau(nbLignes, nbColonnes, nbCouleurs, this);
		//Réinitialisation de tous le contenants de panneau
		scrollPanneau = new JScrollPane(panneau);
		this.remove(separationPanPMS);
		separationPanPMS = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPanneau, scrollPMS);
		add(separationPanPMS, BorderLayout.CENTER);
		separationPanPMS.setOneTouchExpandable(true);
		//Redémarrage de la partie
		demarrerPartie(System.currentTimeMillis());
		repaint();
	}

	/**
	 * Sauvegarde le time stamp de la partie actuelle dans un fichier
	 */
	private void sauverPartie()
	{
		File fichier = null;
		JFileChooser choix = new JFileChooser();
		choix.setCurrentDirectory(options.getDossierCourant());
		int reponse = choix.showSaveDialog(this);
		if (reponse == JFileChooser.APPROVE_OPTION)
		{
			fichier = choix.getSelectedFile();
			options.setDossierCourant(fichier);
			sauverOptions();
			try
			{
				ObjectOutputStream flux = new ObjectOutputStream(new FileOutputStream(fichier));
				flux.writeObject((Long)(panneau.getGrille().getTimeStamp()));
				flux.close();
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(null, "Impossible d'écrire dans ce fichier.");
			}
		}
	}
	
	/**
	 * Charge une partie précédemment sauvegardée
	 */
	private void chargerPartie()
	{
		File fichier = null;
		JFileChooser choix = new JFileChooser();
		choix.setCurrentDirectory(options.getDossierCourant());
		int reponse = choix.showOpenDialog(this);
		if (reponse == JFileChooser.APPROVE_OPTION)
		{
			fichier = choix.getSelectedFile();
			options.setDossierCourant(fichier);
			sauverOptions();
			try
			{
				ObjectInputStream flux = null;
				flux = new ObjectInputStream(new FileInputStream(fichier));
				Object objet;
				objet = flux.readObject();
				long retour = ((Long)objet).longValue();
				flux.close();
				demarrerPartie(retour);
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(null, "Impossible de charger ce fichier.");
			}
		}
	}
	
	/**
	 * Sauvegarde les options dans le fichier consacré
	 */
	public void sauverOptions()
	{
		ObjectOutputStream flux;
		try 
		{
			flux = new ObjectOutputStream(new FileOutputStream(fichierOptions));
			flux.writeObject(options);
			flux.close();
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "Impossible de sauvegarder les options!");
		}
	}
	
	/**
	 * Charge les options du fichier consacré
	 */
	private void chargerOptions()
	{
		ObjectInputStream flux = null;
		try 
		{
			flux = new ObjectInputStream(new FileInputStream(fichierOptions));
			Object objet;
			objet = flux.readObject();
			options = (Options)objet;
			flux.close();
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "Le fichier 'options' est corrompu. Impossible de le lire.");
		}
		//Et charge le panneau en conséquence
		MAJTaille();
		int taille = options.getTaille();
		int nbLignes = getNbLignesDeTaille(taille);
		int nbColonnes = getNbColonnesDeTaille(taille);
		int nbCouleurs = options.getNbCouleurs();
		initialiserPanneau(nbLignes, nbColonnes, nbCouleurs);
	}

	/**
	 * Sauvegarde les scores dans le fichier consacré
	 */
	public void sauverScores()
	{
		ObjectOutputStream flux;
		try 
		{
			flux = new ObjectOutputStream(new FileOutputStream(fichierScores));
			flux.writeObject(panneauMeilleursScores.getScores());
			flux.close();
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "Impossible de sauvegarder les scores!");
		}
	}
	
	/**
	 * Charge les scores du fichier consacré
	 */
	private void chargerScores()
	{
		ObjectInputStream flux;
		try {
			FileInputStream fluxFichier = new FileInputStream(fichierScores);
			flux = new ObjectInputStream(fluxFichier);
			Object objet;
			objet = flux.readObject();
			panneauMeilleursScores.setScores((Scores)objet);
			flux.close();
		}
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "Le fichier 'scores' est corrompu. Impossible de le lire.");
		}
	}
	
	/**
	 * Démarre la partie avec le stamp
	 * 
	 * @param stamp
	 * 	Time Stamp de la partie
	 */
	private void demarrerPartie(long stamp)
	{
		panneau.getGrille().demarrer(stamp);
		demarrerRestePartie();
	}
	
	/**
	 * Démarre la partie avec le stamp et le meilleur score
	 * 
	 * @param stamp
	 * 	TimeStamp de la partie
	 * @param scoreABattre
	 * 	Score à battre
	 */
	public void demarrerPartie(long stamp, int scoreABattre)
	{
		panneau.getGrille().demarrer(stamp, scoreABattre);
		demarrerRestePartie();
	}
	
	/**
	 * Démarre la partie
	 */
	private void demarrerRestePartie()
	{
		purgerImages();
		
		panneauScore.MAJNbPions(panneau.getGrille().getNbPions());
		panneauScore.MAJScoreTotal(panneau.getGrille().getScore(), panneau.getGrille().getScoreABattre());

		Color couleurPartie = Grille.getCouleurTimeStamp(panneau.getGrille().getTimeStamp());
		Color couleurTexte = Grille.getCouleurTexteTimeStamp(panneau.getGrille().getTimeStamp());
		BOSauver.setBackground(couleurPartie);
		BORecommencer.setBackground(couleurPartie);
		BOSauver.setForeground(couleurTexte);
		
		panneau.setSituationsFutures(new Pile<Situation>());
		panneau.setSituationsPassees(new Pile<Situation>());
		panneau.setSituation(new Situation(panneau.getGrille().getMatrice(), panneau.getGrille().getNbPions(), panneau.getGrille().getScore()));
		
		MAJBoutons();
		
		sauverImage();
	}
	
	/**
	 * Montre les règles du jeu
	 */
	private void montrerAide()
	{
		class Aide extends JDialog
		{
			private static final long serialVersionUID = 1L;
			private JEditorPane texte;
			private JScrollPane scroll;
			public Aide()
			{
				try 
				{
					texte = new JEditorPane("file:html/aide.html");
					texte.setEditable(false);
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
				scroll = new JScrollPane(texte);
				add(scroll);
				setSize(450, 600);
			}
		}
		Aide aide = new Aide();
		aide.setVisible(true);
	}
	
	/**
	 * Mise à jour du nombre de couleurs dans les boutons
	 */
	private void MAJBoutonNbCouleurs() 
	{
		int o = options.getNbCouleurs();
		troisCouleurs.setSelected(o == 3);
		quatreCouleurs.setSelected(o == 4);
		cinqCouleurs.setSelected(o == 5);
		sixCouleurs.setSelected(o == 6);
	}

	/**
	 * Mise à jour de la taille dans les boutons
	 */
	private void MAJBoutonTaille()
	{
		int o = options.getTaille();
		petiteTaille.setSelected(o == 0);
		moyenneTaille.setSelected(o == 1);
		grandeTaille.setSelected(o == 2);
	}

	/**
	 * Mise à jour du bouton autoriser historique
	 */
	private void MAJBoutonAutoriserHistorique() 
	{
		autoriserHistorique.setSelected(options.isAutoriserHistorique());
	}

	/**
	 * Mise à jour du bouton précédent
	 */
	private void MAJBoutonPrecedent()
	{
		if (!options.isAutoriserHistorique())
		{
			precedent.setEnabled(false);
			BOPrecedent.setEnabled(false);
		}
		else
		{
			precedent.setEnabled(!panneau.getSituationsPassees().estVide());
			BOPrecedent.setEnabled(!panneau.getSituationsPassees().estVide());
		}
	}

	/**
	 * Mise à jour du bouton suivant
	 */
	private void MAJBoutonSuivant()
	{
		if (!options.isAutoriserHistorique())
		{
			suivant.setEnabled(false);
			BOSuivant.setEnabled(false);
		}
		else
		{
			suivant.setEnabled(!panneau.getSituationsFutures().estVide());
			BOSuivant.setEnabled(!panneau.getSituationsFutures().estVide());
		}
	}

	/**
	 * Mise à jour de tous les boutons
	 */
	public void MAJBoutons()
	{
		MAJBoutonPrecedent();
		MAJBoutonSuivant();
		MAJBoutonAutoriserHistorique();
		MAJBoutonTaille();
		MAJBoutonNbCouleurs();
	}

	/**
	 * Mise à jour de la taille de la fenêtre
	 */
	private void MAJTaille()
	{	
		int taille = options.getTaille();
		if (taille == 0)
			setSize(700, 600);
		else if (taille == 1)
			setSize(900, 600);
		else if (taille == 2)
		{
			//Mise en plein ecran
			GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			Rectangle bounds = graphicsEnvironment .getMaximumWindowBounds();
			setBounds(bounds);
		}
	}

	/**
	 * Retourne le panneau indicatif des scores
	 * 
	 * @return
	 * 	Le panneau indicatif des scores
	 */
	public PanneauScore getPanneauScore()
	{
		return panneauScore;
	}

	/**
	 * Retourne le panneau des meilleurs scores
	 * 
	 * @return
	 * 	Le panneau des meilleurs scores
	 */
	public PanneauMeilleursScores getPanneauMeilleursScores()
	{
		return panneauMeilleursScores;
	}

	/**
	 * Retourne le panneau de jeu
	 * 
	 * @return
	 * 	Le panneau de jeu
	 */
	public Panneau getPanneau()
	{
		return panneau;
	}
	
	/**
	 * Retourne les options du jeu
	 * 
	 * @return
	 * 	Les options du jeu
	 */
	public Options getOptions() 
	{
		return options;
	}

	/**
	 * Modifie les options du jeu
	 * 
	 * @param options
	 * 	Nouvelles options
	 */
	public void setOptions(Options options) 
	{
		this.options = options;
	}

	/**
	 * Retourne le champ de saisie du nom
	 * @return
	 * 	Le champ de saisie du nom du joueur
	 */
	public JTextField getBONom() 
	{
		return BONom;
	}
	
	/**
	 * Retourne le nombre de lignes associé à l'indicateur de taille
	 * 
	 * @param taille
	 * 	Indicateur de taille
	 * @return
	 * 	Nombre de lignes
	 * 	0 si la taille est incorrecte
	 */
	public int getNbLignesDeTaille(int taille)
	{
		if (taille == 0)
			return 5;
		else if (taille == 1)
			return 10;
		else if (taille == 2)
			return 15;
		else
			return 0;
	}

	/**
	 * Retourne le nombre de colonnes associé à l'indicateur de taille
	 * 
	 * @param taille
	 * 	Indicateur de taille
	 * @return
	 * 	Nombre de colonnes
	 * 	0 si la taille est incorrecte
	 */
	public int getNbColonnesDeTaille(int taille)
	{
		if (taille == 0)
			return 10;
		else if (taille == 1)
			return 15;
		else if (taille == 2)
			return 20;
		else
			return 0;
	}
	
	/**
	 * Sauve l'image montrée par le panneau
	 */
	private void sauverImage()
	{
		//Création du fichier contenant l'image
		File fichier = new File(cheminImage(panneau.getGrille().getTimeStamp()));
		//Récupération de l'image du panneau
		BufferedImage tamponSauvegarde = new BufferedImage
		(
			panneau.getPreferredSize().width,
			panneau.getPreferredSize().height,
			BufferedImage.TYPE_3BYTE_BGR
		);
		Graphics g = tamponSauvegarde.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, panneau.getPreferredSize().width,
		panneau.getPreferredSize().height);
		panneau.paint(g);
		try 
		{
			ImageIO.write(tamponSauvegarde, "JPG", fichier);
		} 
		catch (IOException e) 
		{} 
		tamponSauvegarde = null;
		fichier = null;
	}
	
	/**
	 * Purge les images : supprime toute celles qui ne sont pas dans les meilleurs scores
	 */
	private void purgerImages()
	{
		ArrayList<Long> stampsScores = new ArrayList<Long>();
		Scores scores = panneauMeilleursScores.getScores();
		for (int i=0; i<scores.size(); i++)
			if (!stampsScores.contains((Long)scores.get(i).getTimeStamp()))
					stampsScores.add((Long)scores.get(i).getTimeStamp());
		File dossier = new File("imagesScores");
		File[] liste = dossier.listFiles();
		System.out.println("Fichiers:--------------------");
		for (int i=0;i<liste.length; i++)
			System.out.println(liste[i].getName());
		System.out.println("Scores:--------------------");
		for (int i=0; i<stampsScores.size(); i++)
			System.out.println(stampsScores.get(i));
		for (int i=0; i<liste.length; i++)
		{
			String nom = liste[i].getName();
			String[] partiesNom = nom.split("[.]");
			System.out.println("Un fichier:--------------");
			for (int j=0; j<partiesNom.length; j++)
				System.out.println(partiesNom[j]);
			if (partiesNom.length != 2)
				System.err.println("Fichiers images non reconnus dans imagesScores");
			else
				if (!stampsScores.contains(Long.parseLong(partiesNom[0])))
					if (!liste[i].delete())
						System.err.println("Impossible de supprimer "+liste[i].getName());
		}
	}
	

	/**
	 * Renvoie le chemin vers l'image du panneau du stamp
	 * 
	 * @param stamp
	 * 	Time Stamp
	 * @return
	 * 	Chemin
	 */
	static public String cheminImage(long stamp)
	{
		return "imagesScores/"+stamp+".jpg";
	}
}