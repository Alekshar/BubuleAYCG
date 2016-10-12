package perso.youri;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

public class Loader {
	private String filePath;

	public Loader(String filePath){
		this.filePath = filePath;
	}
	
	public Graph loadGraph(String id){
		Graph graph = new SingleGraph(id);

        String basicFile = filePath;
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
		
		return graph;
	}
	
	public void saveGraph(String savePath) throws IOException{
		Graph graph = this.loadGraph("graph");
		graph.write(savePath);
	}
	
	public static void convert(String sourcePath, String destPath) throws IOException{
		new Loader(sourcePath).saveGraph(destPath);
	}
}
