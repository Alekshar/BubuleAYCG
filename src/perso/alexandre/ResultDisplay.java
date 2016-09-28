package perso.alexandre;

import java.util.List;

import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

public class ResultDisplay {
	public static void display(Graph graph, List<List<Node>> series){
		displayXY(graph, series);
		displayXZ(graph, series);
		displayYZ(graph, series);
	}

	private static void displayXY(Graph base, List<List<Node>> series) {
		Graph graph = new SingleGraph("xy_display");
		for(Node node : base){
			Node newNode = graph.addNode(node.getId());
			newNode.setAttribute("xyz", node.getAttribute("xyz"));
		}
		listEdges(graph, series);
		graph.display(false);
	}

	private static void displayXZ(Graph base, List<List<Node>> series) {
		Graph graph = new SingleGraph("xz_display");
		for(Node node : base){
			Node newNode = graph.addNode(node.getId());
			Object[] attributes = (Object[]) node.getAttribute("xyz");
			newNode.setAttribute("x", attributes[0]); //x
			newNode.setAttribute("y", attributes[2]); //z
		}
		listEdges(graph, series);
		graph.display(false);
	}


	private static void displayYZ(Graph base, List<List<Node>> series) {
		Graph graph = new SingleGraph("yz_display");
		for(Node node : base){
			Node newNode = graph.addNode(node.getId());
			Object[] attributes = (Object[]) node.getAttribute("xyz");
			newNode.setAttribute("x", attributes[1]); //y
			newNode.setAttribute("y", attributes[2]); //z
		}
		listEdges(graph, series);
		graph.display(false);
	}

	private static void listEdges(Graph graph, List<List<Node>> series) {
		for(List<Node> serie : series){
			Node lastNode = null;
			for(Node node : serie){
				Node targetNode = graph.getNode(node.getId());
				if(lastNode != null){
					try {
						graph.addEdge(lastNode.getId()+node.getId(), lastNode, targetNode);
					} catch (IdAlreadyInUseException e) {
					} catch (EdgeRejectedException e) {
					}
				}
				lastNode = targetNode;
			}
		}		
	}

}
