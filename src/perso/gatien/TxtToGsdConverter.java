package perso.gatien;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

public class TxtToGsdConverter {

	public static void main(String args[]) throws IOException{
		//Si on finit assez vite la partie algo, 
		//faire une interface qui propose de choisir un fichier texte
		// (Youri travaille dessus normalement)
		for(int i = 0; i < 10; i++){
			txtToGsd("data/","norma_N5_tau4_dt2_delai820_00000"+i);
		}
	}
	public static void txtToGsd(String path, String filename) throws IOException{
		Graph graph = new SingleGraph("test");
		
        String basicFile = path+filename+".txt";
        String line = "";
        String cvsSplitBy = "   ";

        try (BufferedReader br = new BufferedReader(new FileReader(basicFile))) {
        	int counter = 1;
            while ((line = br.readLine()) != null) {
                String[] buble = line.split(cvsSplitBy);

                double x = Double.valueOf(buble[1]).doubleValue();
                double y = Double.valueOf(buble[2]).doubleValue();
                double z = Double.valueOf(buble[3]).doubleValue();
                
                Node node = graph.addNode("B"+counter);
                node.setAttribute("xyz", x, y, z);
                counter++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        graph.write("dgs/"+filename+".dgs");
	}
}