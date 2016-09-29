package perso.youri;
import java.io.*;

public class Creer {
	
	public static void CreerDir(String workspace){
		
		String adresseDuFichier = workspace;
		String dirFileName = "/workspace/.data.txt";
		Repertoire rep = Repertoire.getInstance(adresseDuFichier);
		Repertoire repData = Repertoire.getInstanceD(adresseDuFichier+"/workspace");
		
		File f = new File(adresseDuFichier+dirFileName);

		if (f.exists())  
			System.out.println("Fichier existant");
		else{ 
			System.out.println("Fichier not found");
			try	{
				/**
				 * BufferedWriter a besoin d un FileWriter, 
				 * les 2 vont ensemble, on donne comme argument le nom du fichier,
				 * true signifie qu on ajoute dans le fichier,
				 *  (append) on ne marque pas par dessus 
				 */
	
				FileWriter fw = new FileWriter(adresseDuFichier+dirFileName, true);
				Serialiser.serialiser(rep, adresseDuFichier);
				fw.close();
				System.out.println("fichier crée");
			}
			catch(IOException ioe) {
				System.out.print("Erreur : ");
				ioe.printStackTrace();
			}
		}
	}
}
