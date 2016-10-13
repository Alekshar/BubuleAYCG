package perso.youri;


import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public final class ProcessUtils {
	private ProcessUtils() {
		// means only static methods
	}

	/**
	 * generates edges in the graph between every node with a distance lesser than distanceLimit
	 * @param graph
	 * @param distanceLimit 
	 * @return graph instance
	 */
	public static Graph generateEdges(Graph graph, double distanceLimit) {
		for (Node p1 : graph) {
			Point3D cP1 = new Point3D(p1);
			for (Node p2 : graph) {
				if(p2.getId()!=p1.getId()){
					Point3D cP2 = new Point3D(p2);
					double distanceP1P2 = cP1.distance(cP2,false);
					if (distanceP1P2 < distanceLimit) {
						try {
							graph.addEdge(p1.getId() + p2.getId(), p1, p2);
						} catch (Exception e) {
							// Edge already exists
						}
					}
				}
			}
		}
		return graph;
	}

	public static double getAngle(Point3D a, Point3D b, Point3D c){
		Vector2d ab = a.getVector2DTo(b);
		Vector2d bc = b.getVector2DTo(c);
		double radian = ab.angle(bc);
		double angle = Math.toDegrees(radian);
		return angle;
	}

	public static boolean checkAngle(Point3D a, Point3D b, Point3D c, Point3D d, double toleratedAngle){
		return Math.abs(getAngle(a, b, c)-getAngle(b,c,d)) < toleratedAngle;
	}
	
}
