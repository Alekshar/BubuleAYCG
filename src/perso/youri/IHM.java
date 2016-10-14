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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.text.View;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkDGS;
import org.graphstream.stream.file.FileSinkImages;
import org.graphstream.stream.file.FileSinkImages.OutputType;
import org.graphstream.stream.file.FileSinkImages.Resolutions;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.json.JSONException;

public class IHM extends JFrame implements ActionListener{

	/****************/
	/** Attributes **/
	/****************/
	private final static int SCREEN_W = 1000;
	private final static int SCREEN_H = 800;
	// IHM
	private JPanel generalPan, northPan, topNorthPan, bottomNorthPan, centerPan, eastPan;
	private JButton fichier, analyserAfficher, effacer, image, trajectoire, help;
	private JLabel infoAffAn;
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
    // Algo
    private Bubulle algoGatien;
	
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
		
		topNorthPan = new JPanel();
		topNorthPan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),"Choix du fichier txt"));
		bottomNorthPan = new JPanel();
		bottomNorthPan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),"Informations"));

		fichier = new JButton("Fichier");
		fichier.addActionListener(this);
		jtf = new JTextField(50);
		analyserAfficher = new JButton("Analyser et Afficher");
		analyserAfficher.addActionListener(this);
		infoAffAn = new JLabel(" ");
		
		topNorthPan.add(fichier);
		topNorthPan.add(jtf);
		topNorthPan.add(analyserAfficher);
		bottomNorthPan.add(infoAffAn);
		
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
				infoAffAn.setText(" ");
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

			//graphLoaded = new Loader(pathFileSelected).loadGraph("graph");
			//displayGraph(graphLoaded);
		}
		
		// Action bouton analyserAfficher
		if(e.getSource() == analyserAfficher) {
			// Si le type du fichier est bon
			if(fileTypeSelected.equals(".txt")) {
				graphLoaded = new Loader(pathFileSelected).loadGraph("graph");
				try {
					algoGatien = new Bubulle(graphLoaded);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
				displayGraph(algoGatien.getGraph());
				
				infoAffAn.setForeground(Color.BLACK);
				infoAffAn.setText("Le fichier a bien été analysé.");
			}
			else {
				infoAffAn.setForeground(Color.RED);
				infoAffAn.setText("Erreur : Le type du fichier n'est pas un .txt !");
			}
		}
		/***********************/
		/* Boutons des Options */
		/***********************/
		if(e.getSource() == trajectoire) {
			
			// Ouvrir une boite de dialogue
			if(viewer != null) {
				open = new FileDialog(this, "Sauvegardez les trajectoires dans un fichier txt", FileDialog.SAVE);
				open.setVisible(true);
				pathFileSelected = open.getDirectory()+open.getFile();
				fileTypeSelected = pathFileSelected.substring(pathFileSelected.length()-4, pathFileSelected.length()-3);
				
				if(!fileTypeSelected.equals("."))
					pathFileSelected = open.getDirectory()+open.getFile()+".txt";
				
				algoGatien.getTrajectoire(pathFileSelected);
				
			}			
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

		// Efface l'affichage du graph
		if(e.getSource() == effacer) {
			if(viewer != null) {
				viewer.close();
				centerPan.repaint();
				jtf.setText("");
				infoAffAn.setText("L'affichage a bien été effacé.");
				this.view = null;
			}
		}
		
		// Bouton Aide
		if(e.getSource() == help) {
			JFrame help = new JFrame("Aide");
			JPanel helpPan = new JPanel(new FlowLayout());

			help.setPreferredSize(new Dimension(800,500));
			help.setLayout(new BorderLayout());
			
			StringBuilder strB = new StringBuilder();
			try (BufferedReader br = new BufferedReader(new FileReader("data/README.txt"))) {
	        	String strLine;
	        	
	        	while((strLine = br.readLine()) != null) {
	        		strB.append(strLine+"\n");
	        	}
	        	br.close();	
	        } catch (IOException e1) {e1.printStackTrace();}
			
			JTextArea infos = new JTextArea();
			infos.setText(strB.toString());
			infos.setEditable(false);
			infos.setBackground(null);
			
			helpPan.add(infos);
			help.add(helpPan);
			help.pack();
			help.setVisible(true);
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
                double col4 = Double.valueOf(buble[4]).doubleValue(); //modifier nom
                double col5 = Double.valueOf(buble[5]).doubleValue(); //modifier nom
                
                Node node = graphconverter.addNode("B"+counter);
                node.setAttribute("xyz", x, y, z);
                node.setAttribute("col4", col4);
                node.setAttribute("col5", col5);
                counter++;
            }

        } catch (IOException e) {e.printStackTrace();}
        
        //graphconverter.write(getSworkspace()+"/workspace/data/"+fileName+".dgs");
	}
	
	/*******************/
	/** Code du graph **/
	/*******************/
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
		
		/* Autre moyen
		 * viewer = graph.display();
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
