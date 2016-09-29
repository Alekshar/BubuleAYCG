package perso.youri;
import java.io.*;

public class Serialiser {
	public static void serialiser(Repertoire rep , String adresse) {
		try {
			String adr = adresse+"/workspace/.data.txt";
			System.out.println("Serialisaion : " + adr);
			FileOutputStream fichier = new FileOutputStream(adr);
			ObjectOutputStream oos = new ObjectOutputStream(fichier);
			oos.writeObject(rep);
			oos.flush();
			oos.close();
		}
		catch (java.io.IOException e) {e.printStackTrace();}
	}
}