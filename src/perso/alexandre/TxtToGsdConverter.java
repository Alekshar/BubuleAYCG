package perso.alexandre;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

public class TxtToGsdConverter {

	public static void main(String args[]) throws IOException{
		Graph graph = new MultiGraph("test");

        String basicFile = "data/norma_N5_tau4_dt2_delai820_000001.txt";
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
        
        graph.display(false);
        graph.write(basicFile+".dgs");
	}

}
