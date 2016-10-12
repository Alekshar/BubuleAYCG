package perso.youri;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.text.View;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkImages;
import org.graphstream.stream.file.FileSinkImages.LayoutPolicy;
import org.graphstream.stream.file.FileSinkImages.OutputType;
import org.graphstream.stream.file.FileSinkImages.Resolution;
import org.graphstream.stream.file.FileSinkImages.Resolutions;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

public class IHM extends JFrame implements ActionListener{

	/****************/
	/** Attributes **/
	/****************/
	private final static int SCREEN_W = 1000;
	private final static int SCREEN_H = 800;
	// IHM
	private JPanel generalPan, northPan, topNorthPan, bottomNorthPan, centerPan, eastPan;
	private JButton fichier, analyserAfficher, effacer, image, trajectoire, sauvegarder, convertir;
	private JLabel infoConvertion;
	private JTextField jtf;
	private String pathFileSelected, nameFileSelected, fileTypeSelected;
	// Fichier / Dossier
	private FileDialog open;
	// Viewer
	private Graph graph;
	private Viewer viewer;
    private ViewPanel view;
    // Loader
    private Loader loader;
    // Graph loaded
    private Graph graphLoaded;
	
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
		
		/************************************************/
		generalPan = new JPanel();
		generalPan.setLayout(new BorderLayout());
		
		// Panneau du haut
		northPan = new JPanel();
		northPan.setLayout(new BorderLayout());
		northPan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),"Choix du fichier txt"));
		
		topNorthPan = new JPanel();
		bottomNorthPan = new JPanel();

		fichier = new JButton("Fichier");
		fichier.addActionListener(this);
		jtf = new JTextField(50);
		analyserAfficher = new JButton("Analyser et Afficher");
		analyserAfficher.addActionListener(this);
		infoConvertion = new JLabel(" ");
		
		topNorthPan.add(fichier);
		topNorthPan.add(jtf);
		topNorthPan.add(analyserAfficher);
		bottomNorthPan.add(infoConvertion);
		
		northPan.add(topNorthPan, BorderLayout.NORTH);
		northPan.add(bottomNorthPan, BorderLayout.SOUTH);
		generalPan.add(northPan, BorderLayout.NORTH);
		
		// Panneau central
		centerPan = new JPanel();
		centerPan.setLayout(new BorderLayout());
		centerPan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),"Affichage"));
		//centerPan.add(view, BorderLayout.CENTER);
		generalPan.add(centerPan, BorderLayout.CENTER);
		
		// Panneau de droite
		eastPan = new JPanel();
		eastPan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),"Options"));
		eastPan.setLayout(new GridLayout(5, 1));
		trajectoire = new JButton("Trajectoire");
		trajectoire.addActionListener(this);
		image = new JButton("Screenshot");
		image.addActionListener(this);
		sauvegarder = new JButton("Sauvegarder");
		sauvegarder.addActionListener(this);
		convertir = new JButton("Convertir");
		convertir.addActionListener(this);
		effacer = new JButton("Effacer");
		effacer.addActionListener(this);
		
		eastPan.add(trajectoire);
		eastPan.add(image);
		eastPan.add(sauvegarder);
		eastPan.add(convertir);
		eastPan.add(effacer);
		generalPan.add(eastPan, BorderLayout.EAST);
		
		// Panneau de gauche
		
		// Frame
		/*this.add(northPan, BorderLayout.NORTH);
		this.add(centerPan, BorderLayout.CENTER);
		this.add(eastPan, BorderLayout.EAST);*/
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
				infoConvertion.setText(" ");
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
		}
		
		// Action bouton analyserAfficher
		if(e.getSource() == analyserAfficher) {
			
			// Si le type du fichier est bon
			if(fileTypeSelected.equals(".txt")) {
				graphLoaded = new Loader(pathFileSelected).loadGraph("graph");
				
				graphCode(graphLoaded);
				
				infoConvertion.setForeground(Color.BLACK);
				infoConvertion.setText("Le fichier a bien été analysé.");
			}
			else {
				infoConvertion.setForeground(Color.RED);
				infoConvertion.setText("Erreur : Le type du fichier n'est pas un .txt !");
			}
		}
		/***********************/
		/* Boutons des Options */
		/***********************/
		if(e.getSource() == trajectoire) {
			
		}
		
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
			
			}
		}
		
		if(e.getSource() == sauvegarder) {
			
		}

		if(e.getSource() == convertir) {
	
		}

		// Efface l'affichage du graph
		if(e.getSource() == effacer) {
			if(viewer != null) {
				viewer.close();
				centerPan.repaint();
			}
		}		
	}
	
	/**************************************/
	/** Méthode de convertion txt to dgs **/
	/**************************************/
	public static void txtToGsd(String pathFileName, String fileName) throws IOException{
		Graph graphconverter = new SingleGraph("");
		
        String basicFile = pathFileName;
        String line = "";
        String cvsSplitBy = "   ";

        try (BufferedReader br = new BufferedReader(new FileReader(basicFile))) {
        	int counter = 1;
            while ((line = br.readLine()) != null) {
                String[] buble = line.split(cvsSplitBy);

                double x = Double.valueOf(buble[1]).doubleValue();
                double y = Double.valueOf(buble[2]).doubleValue();
                double z = Double.valueOf(buble[3]).doubleValue();
                
                Node node = graphconverter.addNode("B"+counter);
                node.setAttribute("xyz", x, y, z);
                counter++;
            }

        } catch (IOException e) {e.printStackTrace();}
        
        //graphconverter.write(getSworkspace()+"/workspace/data/"+fileName+".dgs");
	}
	
	/*******************/
	/** Code du graph **/
	/*******************/
	public void graphCode(Graph graph) {
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
		
		/* Autre moyen
		 * viewer = graph.displaey();
		 * view = viewer.getDefaultView();
		 * */
		//viewer.enableAutoLayout();
		//view.setEnabled(true);
		this.centerPan.add(view, BorderLayout.CENTER);
		
	}
	
	/***************/
	/** GET / SET **/
	/***************/
	public Graph getGraphLoaded(){return this.graphLoaded;}
	
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
