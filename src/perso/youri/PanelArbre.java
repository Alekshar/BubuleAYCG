package perso.youri;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class PanelArbre extends JPanel implements MouseListener {

	private IHM frame;
	private Repertoire workspace;
	
	// Arbre
	private JTree tree;
    private DefaultTreeModel model;
    private String dernierClic;
    private DefaultMutableTreeNode root = new DefaultMutableTreeNode();
   	private HashMap<Repertoire, DefaultMutableTreeNode> hm;
   	private TreePath path;
    //private ArrayList<PageHTML> alPHTML;
    //private PopUp menu;
    
    /******************/
    /** Constructeur **/
    /******************/
	public PanelArbre(IHM frame) {
		this.frame = frame;
		this.setPreferredSize(new Dimension ( 150, this.getHeight()));
		this.addMouseListener(this);
		this.setLayout(new BorderLayout());
		
		this.hm = new HashMap<Repertoire, DefaultMutableTreeNode> ();
		
		// Construction de l'arbre
		constructionArbre();
		
		// Ajout arbre sur le ContentPane de notre frame à l'aide d'un scroll
		this.add(new JScrollPane(tree));
	}
	
	private void lireFichier() {
		try	{
		    ObjectInputStream ois = new ObjectInputStream (new FileInputStream (frame.getSworkspace()+"/workspace/.data.txt"));
		    workspace = (Repertoire) ois.readObject();
		    ois.close();
		}
		catch (ClassNotFoundException exception) {
		    System.out.println ( "Impossible de lire l'objet : " + exception.getMessage());
		}
		catch (IOException exception) {
		    System.out.println ("Erreur lors de l'écriture : " + exception.getMessage());
		}
    }
	
	public void constructionArbre() {
		//On récupère l'objet repertoire workspace contenu dans le fichier .data.txt
        lireFichier();		
		
		// Création d'une racine
		root = new DefaultMutableTreeNode(workspace.getNom());
        this.model = new DefaultTreeModel(root);
        initialisationHashMap(workspace, root);
        constructionRacine(workspace);
        
        //On crée, avec notre hiérarchie, un arbre
		System.out.println("root : " + root);
		tree = new JTree(model);
	}
	
	public void initialisationHashMap(Repertoire r, DefaultMutableTreeNode dmtn) {	
		hm.put(r, dmtn);
		if(r.getRepertoires().size()>0)	{
			for (Repertoire rep : r.getRepertoires()) {
				initialisationHashMap(rep, new DefaultMutableTreeNode(rep.getNom()));
			}
		}
	}
	
	private void constructionRacine(Repertoire r) {
		/*for (PageHTML p : r.getPagesHTML())	{	
			alPHTML.add(p);
			model.insertNodeInto(new DefaultMutableTreeNode(p.getNomPage()), hm.get(r), 0);
		}*/
		for(Repertoire rep : r.getRepertoires()){			
			model.insertNodeInto(hm.get(rep),hm.get(r),0);
			constructionRacine(rep);
		}
	}

	/***************/
	/** GET / SET **/
	/***************/
	public Repertoire getWorkspace() {return workspace;}
	public void setWorkspace(Repertoire workspace) {this.workspace = workspace;}
	public TreePath getPath(){return path;}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
}
