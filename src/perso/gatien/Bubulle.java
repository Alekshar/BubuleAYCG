package perso.gatien;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

public class Bubulle {

	public static void main(String[] args) {
		Graph bulles = new SingleGraph("Bulles");
		
		try{
			bulles.read("dgs/norma_N5_tau4_dt2_delai820_000000.dgs");
		}
		catch(Exception e){
			System.out.print("erreur");
		}
		
		double seuilDistance = moyenneDesDistance(bulles)*20/100;
		
		//Création des arrêtes
		bulles = ProcessUtils.generateEdges(bulles, seuilDistance);

		int cpt =0;
		//Algo
		for(Node n1:bulles){
			Point3D p1 = new Point3D(n1);
			for(Edge e1:n1){
				Node n2 = e1.getOpposite(n1);
				Point3D p2 = new Point3D(n2);
				
				for(Edge e2:n2){
					Node n3 = e2.getOpposite(n2);
					//On test si le voisin de n2 n'est pas tout simplement n1
					if(n3.getId()!=n1.getId()){
						Point3D p3 = new Point3D(n3);
						
						//rajouter la vérification d'angle
						if(p1.verificationDistance( p2, p3, 0.1)){
							cpt++;
						}
					}else{
						
					}
					
				}
			}
		}
		System.out.println(cpt);
	}
	
	public static double moyenneDesDistance(Graph graph){
		
		double cpt =0.0;
		double sommeDistance=0.0;
		for(Node n0:graph){
			Object[] attrN0 = n0.getAttribute("xyz");
			Point3D pA = new Point3D((Double)attrN0[0], (Double)attrN0[1], (Double)attrN0[2]);

			for(Node n1:graph){
				Object[] attrN1 = n1.getAttribute("xyz");
				Point3D pB = new Point3D((Double)attrN1[0], (Double)attrN1[1], (Double)attrN1[2]);
				
				sommeDistance += pA.distance(pB);
				cpt++;
			}
		}
		return sommeDistance/cpt;
	}
	
	public static boolean verificationDistance(Point3D c1, Point3D c2, Point3D c3, double margeErreur){
		return false;	
	}
	
}
