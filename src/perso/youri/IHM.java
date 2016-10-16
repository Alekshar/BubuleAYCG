package perso.youri;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.graphstream.graph.Graph;
import org.graphstream.stream.file.FileSinkImages;
import org.graphstream.stream.file.FileSinkImages.OutputType;
import org.graphstream.stream.file.FileSinkImages.Resolutions;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

import org.json.JSONException;

public class IHM extends JFrame implements ActionListener, ChangeListener{

	/****************/
	/** Attributes **/
	/****************/
	private final static int SCREEN_W = 1000;
	private final static int SCREEN_H = 800;
	// IHM
	private JPanel generalPan, northPan, topNorthPan, bottomNorthPan, bottomNorthPanNorth, bottomNorthPanSouth, centerPan, eastPan;
	private JButton fichier, afficher, analyser, effacer, image, trajectoire, help;
	private JLabel infoBtnFiles, infoSlider;
	private JTextField jtf;
	private String pathFileSelected, nameFileSelected, fileTypeSelected;
	// Fichier / Dossier
	private FileDialog open;
	// Viewer
	private Viewer viewer;
    private ViewPanel view;
    private static boolean isAfficher = false;
    private static boolean isAnalyser = false;
    // Graph loaded
    private Graph graphLoaded;
    // Algo
    private Bubulle algo;
    // Slider
    JSlider slider;
    private static double sliderPercent = 1.;
    private static double sliderTmpCpt = 0;

	/******************/
	/** Constructeur **/
	/******************/
	public IHM() {
		super("Etude d'une dynamique tourbillonnaire");
		setPreferredSize(new Dimension(SCREEN_W,SCREEN_H));
		//setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setLayout(new BorderLayout());
		
		// Positionne la fenêtre au milieu de l'écran
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((dim.width/2 - this.getWidth())/2, (dim.height/4 - this.getHeight())/2);
		
		// Initialisation des strings
		pathFileSelected = ""; nameFileSelected = ""; fileTypeSelected = "";
		/************************************************/
		generalPan = new JPanel();
		generalPan.setLayout(new BorderLayout());
		
		// Panneau du haut
		northPan = new JPanel();
		northPan.setLayout(new BorderLayout());

		topNorthPan = new JPanel();
		topNorthPan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),"Choix du fichier txt"));
		bottomNorthPan = new JPanel();
		bottomNorthPan.setLayout(new BorderLayout());
		bottomNorthPan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),"Informations"));

		fichier = new JButton("Fichier");
		fichier.addActionListener(this);
		jtf = new JTextField(50);
		jtf.setEnabled(false);
		afficher = new JButton("Afficher");
		afficher.addActionListener(this);
		analyser = new JButton("Analyser");
		analyser.addActionListener(this);
		infoBtnFiles = new JLabel(" ");
		infoSlider = new JLabel(" ");
		
		topNorthPan.add(fichier);
		topNorthPan.add(jtf);
		topNorthPan.add(afficher);
		topNorthPan.add(analyser);
		bottomNorthPanNorth = new JPanel();
		bottomNorthPanSouth = new JPanel();
		bottomNorthPanNorth.add(infoBtnFiles);
		bottomNorthPanSouth.add(infoSlider);
		bottomNorthPan.add(bottomNorthPanNorth,BorderLayout.NORTH);
		bottomNorthPan.add(bottomNorthPanSouth,BorderLayout.SOUTH);
		
		northPan.add(topNorthPan, BorderLayout.NORTH);
		northPan.add(bottomNorthPan, BorderLayout.SOUTH);
		generalPan.add(northPan, BorderLayout.NORTH);
		
		// Panneau central
		centerPan = new JPanel();
		centerPan.setLayout(new BorderLayout());
		centerPan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),"Affichage"));	
		generalPan.add(centerPan, BorderLayout.CENTER);
		
		// Panneau de droite
		eastPan = new JPanel();
		eastPan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),"Options"));
		eastPan.setLayout(new GridLayout(5, 1));
		trajectoire = new JButton("Trajectoire");
		trajectoire.addActionListener(this);
		image = new JButton("Screenshot");
		image.addActionListener(this);
		effacer = new JButton("Effacer");
		effacer.addActionListener(this);
		help = new JButton("Aide");
		help.addActionListener(this);
		
		eastPan.add(trajectoire);
		eastPan.add(image);
		eastPan.add(effacer);
		eastPan.add(help);
		generalPan.add(eastPan, BorderLayout.EAST);
		
		// Frame
		this.add(generalPan);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {System.exit(0);}
		});
		/************************************************/
		pack();
		setVisible(true);
		//setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/*************************/
	/** Actions des boutons **/
	/*************************/
	@Override
	public void actionPerformed(ActionEvent e) {
		// Action bouton Fichier
		if(e.getSource() == fichier) {
			if(jtf.getText() != "") {
				jtf.setText("");
				infoBtnFiles.setText(" ");
				infoSlider.setText("");
			}
			
			open = new FileDialog(this);
			open.setVisible(true);
			pathFileSelected = open.getDirectory()+open.getFile();
			
			// Obtention du nom du fichier
			int lastIndex = 0;
			
			// Lien Windows
			lastIndex =  pathFileSelected.lastIndexOf("\\");
			nameFileSelected = pathFileSelected.substring(lastIndex+1, pathFileSelected.length()-4);
			fileTypeSelected = pathFileSelected.substring(pathFileSelected.length()-4, pathFileSelected.length());
			
			// Lien Linux
			//lastIndex =  pathFileSelected.lastIndexOf("/");
			//nameFileSelected = pathFileSelected.substring(lastIndex+1, pathFileSelected.length()-4);
			
			// Si on ouvre la fenêtre de recherche de fichier
			// mais que finalement on ne choisit rien
			// on supprime le message d'erreur
			if(pathFileSelected.equals("nullnull"))
				jtf.setText("");
			else
				jtf.setText(nameFileSelected+fileTypeSelected);
			
			this.isAfficher = false;
			this.isAnalyser = false;
		}
		
		// Action bouton Afficher
		if(e.getSource() == afficher) {
			if(!jtf.getText().equals("") && !isAfficher) {
				if(!fileTypeSelected.equals("null")) {
					// Si le type du fichier est bon
					if(fileTypeSelected.equals(".txt")) {
						graphLoaded = new Loader(pathFileSelected).loadGraph("graph");
						displayGraph(graphLoaded);
						createSlider();
						resetSlider();
						isAfficher = true;
						
						infoBtnFiles.setForeground(Color.BLACK);
						infoBtnFiles.setText("Affichage du graph lié au fichier choisi.");
						infoSlider.setText("Vous pouvez zoomer sur le graph grâce au slider sur le côté. Pour vous déplacer, cliquez sur le graph puis utilisez les flèches du clavier.");
					}
					else {
						infoBtnFiles.setForeground(Color.RED);
						infoBtnFiles.setText("Erreur : Le type du fichier n'est pas un .txt !");
					}
					isAnalyser = false;
				}
			}
		}
		
		// Action bouton analyser
		if(e.getSource() == analyser) {
			if(!jtf.getText().equals("") && !isAnalyser) {
				if(!fileTypeSelected.equals("null")) {
					// Si le type du fichier est bon
					if(fileTypeSelected.equals(".txt")) {
						graphLoaded = new Loader(pathFileSelected).loadGraph("graph");
						
						try {
							algo = new Bubulle(graphLoaded);
						} catch (JSONException e1) {e1.printStackTrace();}
						  catch (IOException e1) {e1.printStackTrace();}
										
						displayGraph(algo.getGraph());
						createSlider();
						resetSlider();
						isAnalyser = true;
	
						infoBtnFiles.setForeground(Color.BLACK);
						infoBtnFiles.setText("Le fichier a bien été analysé.");
						infoSlider.setText("Vous pouvez zoomer sur le graph grâce au slider sur le côté. Pour vous déplacer, cliquez sur le graph puis utilisez les flèches du clavier.");
					}
					else {
						infoBtnFiles.setForeground(Color.RED);
						infoBtnFiles.setText("Erreur : Le type du fichier n'est pas un .txt !");
					}
					isAfficher = false;
				}
			}
		}
		/***********************/
		/* Boutons des Options */
		/***********************/
		// Crée un fichier contenant les trajectoires trouvées
		if(e.getSource() == trajectoire) {			
			// Ouvrir une boite de dialogue
			if(viewer != null) {
				open = new FileDialog(this, "Sauvegardez les trajectoires dans un fichier txt", FileDialog.SAVE);
				open.setVisible(true);
				pathFileSelected = open.getDirectory()+open.getFile();
				fileTypeSelected = pathFileSelected.substring(pathFileSelected.length()-4, pathFileSelected.length()-3);
				
				if(!fileTypeSelected.equals("."))
					pathFileSelected = open.getDirectory()+open.getFile()+".txt";
				
				algo.getTrajectoire(pathFileSelected);
				infoBtnFiles.setText("Le fichier txt des trajectoires a bien été crée.");
			}			
		}
		
		// Prend un screenshot de l'affichage du graph
		if(e.getSource() == image) {
			if(viewer != null){
				open = new FileDialog(this, "Sauvegardez votre image", FileDialog.SAVE);
				open.setVisible(true);
				
				pathFileSelected = open.getDirectory()+open.getFile();
				fileTypeSelected = pathFileSelected.substring(pathFileSelected.length()-4, pathFileSelected.length()-3);

				if(!fileTypeSelected.equals("."))
					pathFileSelected = open.getDirectory()+open.getFile()+".png";
					
				/*Autre méthode sans utiliser le FileSinkImages
				 * this.getGraphLoaded().addAttribute("ui.screenshot", pathFileSelected);
				*/
				
				FileSinkImages pic = new FileSinkImages(OutputType.PNG, Resolutions.HD720);
				try {
					pic.writeAll(getGraphLoaded(), pathFileSelected);
				} catch (IOException e1) {e1.printStackTrace();}
				
				infoBtnFiles.setText("L'image du graph a bien été créée.");
			}
		}

		// Efface l'affichage du graph
		if(e.getSource() == effacer) {
			if(viewer != null) {
				viewer.close();
				centerPan.repaint();
				jtf.setText("");
				infoBtnFiles.setText("L'affichage a bien été effacé.");
				infoSlider.setText("");
				this.view = null;
				this.viewer = null;
				this.isAfficher = false;
				this.isAnalyser = false;
			}
		}
		
		// Bouton Aide
		if(e.getSource() == help) {
			JFrame help = new JFrame("Aide");
			JPanel helpPan = new JPanel(new FlowLayout());

			help.setPreferredSize(new Dimension(800,575));
			help.setLayout(new BorderLayout());
			
			StringBuilder strB = new StringBuilder();
			String strSave = null;
			
			try (BufferedReader br = new BufferedReader(new FileReader("data/README.txt"))) {
	        	String strLine;
	        	
	        	while((strLine = br.readLine()) != null) {
	        		strB.append(strLine+"\n");
	        	}
	        	br.close();	
	        } catch (IOException e1) {e1.printStackTrace();
			
				strSave = "Bonjour et bienvenue dans l'Aide !\n\n"+

				"Utilisation :\n"+
					"\t- Sélectionnez un fichier .txt grâce au bouton \"Fichier\".\n"+
					"\t- Apparition du nom du fichier sélectionné.\n"+
					"\t- Affichez ce fichier grâce au bouton \"Afficher\".\n"+
					"\t- Analysez ce fichier grâce au bouton \"Analyser\".\n\n"+
				
				"Informations :\n"+
					"\tDes informations concernant vos actions seront affichées dans la partie \"Informations\".\n\n"+
				
				"Affichage :\n"+
					"\tPermet l'affichage du graph une fois analysé.\n"+
					"\tUtilisez le slider sur le côté pour Zoomer.\n"+
					"\tPour vous déplacer dans la graph, cliquez dessus puis utilisez les flèches du clavier.\n\n"+
				
				"Options :\n"+
					"\tBouton Trajectoire :\n"+
						"\t\tPermet de générer un fichier .txt du résultat des trajectoires trouvées.\n"+
						"\t\tOuvrir ensuite ce fichier avec un éditeur de texte autre que le bloc note\n"+
						"\t\tafin d'obtenir une bonne indentation.\n"+
					"\tBouton Screenshot :\n"+
						"\t\tPermet de générer une image png (par défaut) du graph résultat.\n"+
					"\tBouton Effacer :\n"+
						"\t\tPermet d'effacer l'affichage du graph.\n"+
					"\tBouton Aide :\n"+
						"\t\tPermet d'ouvrir une fenêtre d'aide. Ce contenu est disponible également\n"+
						"\t\tdans un fichier README.txt.\n"+					
														"\t\t\t\t\t\t\t\t\t\tBy youyou.";
	        }
			
			JTextArea infos = new JTextArea();
			// Si le fichier README n'est pas trouvable, l'aide va s'afficher quand même
			if(strSave != null)
				infos.setText(strSave.toString());
			else
				infos.setText(strB.toString());
			infos.setEditable(false);
			infos.setBackground(null);
			
			helpPan.add(infos);
			help.add(helpPan);
			help.pack();
			help.setVisible(true);
		}
	}
	
	/*****************/
	/* Action Slider */
	/*****************/
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		int cpt = (int)source.getValue();
		
		if(cpt == 0) {
			resetSlider();
		}
		// Zoom
		if(cpt > 0 && cpt > sliderTmpCpt && sliderPercent >= 0) {
			view.getCamera().setViewPercent(sliderPercent-0.2);
			sliderPercent -= 0.2;
		}
		// Dézoom
		if(cpt > 0 && cpt < sliderTmpCpt && sliderPercent <= 1) {
			view.getCamera().setViewPercent(sliderPercent+0.2);
			sliderPercent += 0.2;
		}			
		sliderTmpCpt = cpt;
		System.out.println("cpt : " + cpt);
		System.out.println("sliderTmp : " + sliderTmpCpt);
		System.out.println("pourcent : " + sliderPercent);
	}
	
	/************************/
	/** Affichage du graph **/
	/************************/
	public void displayGraph(Graph graph) {
		// Permet de générer le graph dans l'IHM directement
		if(view != null) {
			viewer.close();
			viewer = new Viewer(graph,Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
			view = (ViewPanel) viewer.addDefaultView(false);
			centerPan.repaint();
		}
		else {
			viewer = new Viewer(graph,Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
			view = (ViewPanel) viewer.addDefaultView(false);
		}		
		this.centerPan.add(view, BorderLayout.CENTER);
	}
	
	/***************/
	/** GET / SET **/
	/***************/
	public Graph getGraphLoaded(){return this.graphLoaded;}
	
	/*************/
	/* Fonctions */
	/*************/
	public void createSlider() {
		slider = new JSlider(JSlider.VERTICAL,0,5,0);
		slider.addChangeListener(this);
		slider.setMajorTickSpacing(5);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(false);
		slider.setPaintLabels(true);
		slider.setLabelTable(slider.createStandardLabels(1));
		
		centerPan.add(slider, BorderLayout.EAST);
	}
	public void resetSlider() {
		view.getCamera().resetView();
		sliderPercent = 1;
		sliderTmpCpt = 0;
		slider = null;
	}
	
	
	/*************************************/
	/** Lancement du programme sécurisé **/
	/*************************************/
	public static void createAndShowGUI() {
		
		// Permet d'utiliser l'apparence du système en général
		try { 
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { e.printStackTrace();}
		
		new IHM(); 
	}
	
	public static void main(String...args){
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() { createAndShowGUI(); }
		});
	}
}
