package perso.youri;
import java.io.*;
import java.util.*;

public class Repertoire implements Serializable
{
	private static Repertoire instance = null;
	private static Repertoire instanceData = null;
	private ArrayList<Repertoire> rep;
	private String nomRep;
	private String direction;
	
	//Le constructeur qui crée le repertoire
	private Repertoire (String dir, String nomRep) {
		direction = dir + "/" + nomRep;
		System.out.println(direction);
		rep = new ArrayList<Repertoire>();
		this.nomRep = nomRep;
		new File (direction).mkdir();
	}
	
	public Repertoire chercherRepertoire(String nom) {
		ArrayList<Repertoire> al = chercherRepertoire2(nom, new ArrayList<Repertoire>());
		for(Repertoire r : al) {
			if(r!= null && r.getNom().equals(nom))
				return r;
		}
		return null;
	}
	
	private ArrayList<Repertoire> chercherRepertoire2(String nom, ArrayList<Repertoire> al)	{
		Repertoire r2=null;
		if(this.nomRep.equals(nom)) {
			al.add(this);
			return al;
		}
		for (Repertoire r : this.rep)
			al.add(r.chercherRepertoire(nom));

		return al;
	}
	
	//Permet de ne créer qu'une seule instance du dossier principal
	public final static Repertoire getInstance(String dir) {
		if (null == instance)
			instance = new Repertoire(dir, "workspace");

		return instance;
	}
	
	// Même procédé mais pour le dossier data
	public final static Repertoire getInstanceD(String dir) {
		if (null == instanceData)
			instanceData = new Repertoire(dir, "data");

		return instanceData;
	}
	
	//Permet d'ajouter un repertoire à l'interieur
	//du repertoire ou la methode est appelée
	public void ajouterRepertoire(String nom) {
		System.out.println(direction);
		Repertoire rep2 = new Repertoire(this.direction, nom);
		rep.add(rep2);
	}
	
	
	//Methode qui permet de supprimer
	//un repertoire et tout ce qu'il contient
	public void supprimerRepertoire(String nom) {
		boolean test = false;
		System.out.println(rep);

		for (int i=0; i<rep.size(); i++) {
			if(rep.get(i).getNom().equals(nom))	{	
				rep.get(i).effacerContenuRepertoire();
				rep.remove(i);
				test = true;
			}
		}
		if(test)
			new File (direction + "/" + nom).delete();
	}
	
	//Methode recursive qui efface toutes les instances
	//de pages et de repertoire. Elle est appelee par
	//la classe supprimerRepertoire
	private void effacerContenuRepertoire()	{
		for(Repertoire r : rep)	{
			r.effacerContenuRepertoire();
			rep.remove(r);
		}
	}
	
	/***************/
	/** GET / SET **/
	/***************/
	//Retourne l'ArrayList de repertoire
	public ArrayList<Repertoire> getRepertoires() {return this.rep;}
	
	public Repertoire getRepertoire(String nom)	{
		Repertoire tmp = null;
		for(Repertoire r : this.rep) {
			if(r.getNom().equals(nom))
				tmp = r;
		}
		return tmp;
	}
	
	//Retourne le nom du repertoire
	public String getNom() 	{return this.nomRep;}
	
	//Methode recursive qui renvoie le nom du repertoire
	//et tous les sous-repertoires qu'il contient
	public String toString() {
		String s = this.nomRep + ", ";
		s+="\n";
		for (Repertoire r : rep) {
			s += r.toString();
		}
		return s;
	}
}