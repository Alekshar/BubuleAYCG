package project;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import perso.gatien.Point3D;

public final class ProcessUtils {
	private ProcessUtils() {
		// means only static methods
	}

	public static Graph fillEdges(Graph graph, double distanceLimit) {
		for (Node p1 : graph) {
			Point3D cP1 = new Point3D(p1);
			for (Node p2 : graph) {
				Point3D cP2 = new Point3D(p2);
				double distanceP1P2 = cP1.distance(cP2);
				if (distanceP1P2 < distanceLimit) {
					try {
						graph.addEdge(p1.getId() + p2.getId(), p1, p2);
					} catch (Exception e) {
						// Edge already exists
					}
				}
			}
		}
		return graph;
	}

}
