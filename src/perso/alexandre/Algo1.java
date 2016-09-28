package perso.alexandre;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import project.Loader;
import project.Point3D;
import project.ProcessUtils;

public class Algo1 {
	
	private static final double DISTANCE_RATIO = 0.1;
	private static final double ANGLE_LIMIT = 20;
	private static List<List<Node>> series = new ArrayList<List<Node>>();

	public static void main(String args[]){
		Graph graph = new Loader("data/sampletest.txt").loadGraph("graph");
		
		//1st step : link nodes by edges under a certain distance
		ProcessUtils.generateEdges(graph, 2);
		graph.display(false);
		
		//2nd step : list all correct series (matching both angle and distance constraints)
		for(Node node : graph){
			processStartNode(node);
		}
		ResultDisplay.display(graph, series);
//		for(List<Node> serie : series){
//			for(Node node : serie){
//				System.out.print(node+" -> ");
//			}
//			System.out.println();
//		}
//		System.out.println(series.size()+" end");
		
		//3rd step : solve conflicts by deleting less impacting series (defined by increase of points out of series)
		
		//4th display and save graph result
		
		//5th save txt file with series points ids
	}

	private static void processStartNode(Node node) {
		for(Edge edge : node){
			Node node2 = edge.getOpposite(node);
			List<Node> list = new ArrayList<Node>();
			list.add(node);
			list.add(node2);
			processNode(list);
		}
	}

	private static void processNode(List<Node> list) {
		int nodeCount = list.size();
		Point3D p1 = new Point3D(list.get(nodeCount-2));
		Node node2 = list.get(nodeCount-1);//last node
		Point3D p2 = new Point3D(node2 );
		
		int coefficient = (nodeCount == 3) ? 2 : 1; //double values for P3->P4 match
		
		for(Edge edge : node2){
			Node node3 = edge.getOpposite(node2);
			if(list.contains(node3)){
				continue;
			}
			Point3D p3 = new Point3D(node3);
			if(p1.checkDistance(p2, p3, DISTANCE_RATIO, coefficient)
					&& ProcessUtils.checkAngle(p1, p2, p3, ANGLE_LIMIT)){
				List<Node> sublist = new ArrayList<Node>(list);
				sublist.add(node3);
				if(sublist.size() == 5){
					//end of search
					series.add(sublist);
					return;
				}
				processNode(sublist);
			}
		}
	}

}
