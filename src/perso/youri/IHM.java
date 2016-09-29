package perso.youri;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
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
	private PanelArbre panelArb;
	private JButton fichier, convertir;
	private JLabel infoConvertion;
	private JTextField jtf;
	private String pathFileSelected, nameFileSelected, fileTypeSelected;
	// Fichier / Dossier
	private FileDialog open;
	// Viewer
	private Graph graph;
	private Viewer viewer;
    private ViewPanel view;
	// Workspace
	private JFileChooser workspace;
	private String Sworkspace;
	
	/******************/
	/** Constructeur **/
	/******************/
	public IHM() {
		super("Etude d'une dynamique tourbillonnaire");
		setPreferredSize(new Dimension(SCREEN_W,SCREEN_H));
		//setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setLayout(new BorderLayout());
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/4 - this.getWidth()/4, dim.height/6 - this.getHeight()/6);
		
		/*
		 * Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			f.setLocation(dim.width/2 - f.getWidth()/2, dim.height/2 - f.getHeight()/2);
		 *  f pour frame
		 * */
		/************************************************/
		/*
		 * Choix du dossier JFileChooser (directement racine)
		 * Changement du titre du JFileChooser
		 * Choix du repertoire ou save workspace
		 * setVisible de la frame
		 * Recuperation selection sWorkspace 
		 */
		workspace = new JFileChooser(new File("~/"));
		workspace.setDialogTitle("Choisissez votre dossier de travail :");
		workspace.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		workspace.showOpenDialog(this);
		Sworkspace = workspace.getSelectedFile().toString(); 
		
		// Si notre dossier ou l'on save le workspace est choisi alors ...
		if(Sworkspace != null ){
			// Création du dossier workspace
			Creer.CreerDir(Sworkspace);
			
			/************************************************/
			generalPan = new JPanel();
			generalPan.setLayout(new BorderLayout());
			
			// Panneau du haut
			northPan = new JPanel();
			northPan.setLayout(new BorderLayout());
			northPan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),"Choix de fichiers txt à convertir"));
			
			topNorthPan = new JPanel();
			bottomNorthPan = new JPanel();

			fichier = new JButton("Fichier");
			fichier.addActionListener(this);
			jtf = new JTextField(50);
			convertir = new JButton("Convertir");
			convertir.addActionListener(this);
			infoConvertion = new JLabel(" ");
			
			topNorthPan.add(fichier);
			topNorthPan.add(jtf);
			topNorthPan.add(convertir);
			bottomNorthPan.add(infoConvertion);
			
			northPan.add(topNorthPan, BorderLayout.NORTH);
			northPan.add(bottomNorthPan, BorderLayout.SOUTH);
			generalPan.add(northPan, BorderLayout.NORTH);
			
			// Panneau central
			graphCode();
			centerPan = new JPanel();
			centerPan.setLayout(new BorderLayout());
			centerPan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),"Affichage"));
			centerPan.add(view, BorderLayout.CENTER);
			generalPan.add(centerPan, BorderLayout.CENTER);
			
			// Panneau de droite
			eastPan = new JPanel();
			eastPan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),"Options"));
			generalPan.add(eastPan, BorderLayout.EAST);
			
			// Panneau de gauche
			panelArb = new PanelArbre(this);
			generalPan.add(panelArb, BorderLayout.WEST);
			
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
			
			//graphCode();
		}
	}
	
	/***************/
	/** GET / SET **/
	/***************/
	public String getSworkspace(){ return Sworkspace; }
	public Repertoire getWorkspace(){ return panelArb.getWorkspace();}
	public PanelArbre getPanelArb(){ return panelArb;} 
	
	/*************************/
	/** Actions des boutons **/
	/*************************/
	@Override
	public void actionPerformed(ActionEvent e) {
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
			
			jtf.setText(nameFileSelected+fileTypeSelected);
		}
		
		if(e.getSource() == convertir) {
			
			// Si le type du fichier est bon
			if(fileTypeSelected.equals(".txt")) {
				try {
					txtToGsd(pathFileSelected,nameFileSelected);
				} catch (IOException e1) {e1.printStackTrace();}
				
				infoConvertion.setForeground(Color.BLACK);
				infoConvertion.setText("Le fichier a bien été converti.");
			}
			else {
				infoConvertion.setForeground(Color.RED);
				infoConvertion.setText("Erreur : Le type du fichier n'est pas un .txt !");
			}
			/*open = new FileDialog(this);
			open.setVisible(true);
			nameFileSelected = open.getDirectory()+open.getFile();
			System.out.println(nameFileSelected);
			jtf.setText(nameFileSelected);*/
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
        
        graphconverter.write("data/"+fileName+".dgs");
	}
	
	/*******************/
	/** Code du graph **/
	/*******************/
	public void graphCode() {
		graph = new SingleGraph("Tutorial 1");
		
		// Permet de générer le graph dans l'IHM directement
		viewer = new Viewer(graph,Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		viewer.enableAutoLayout();
		view = (ViewPanel) viewer.addDefaultView(false);
		//view.setEnabled(true);
		
		graph.addNode("A" );
		graph.addNode("B" );
		graph.addNode("C" );
		graph.addEdge("AB", "A", "B");
		graph.addEdge("BC", "B", "C");
		graph.addEdge("CA", "C", "A");

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
