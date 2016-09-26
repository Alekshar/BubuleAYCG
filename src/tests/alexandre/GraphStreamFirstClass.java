package tests.alexandre;

import java.io.IOException;

import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.stream.GraphParseException;

public class GraphStreamFirstClass {
	
	public static void main(String args[]) throws IOException, ElementNotFoundException, GraphParseException{
		Graph graph = new MultiGraph("test");
		
		graph.read("data/norma_N5_tau4_dt2_delai820_000001.txt.dgs");
		
		graph.display(false);
	}

}
