package perso.gatien;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

public class Bubulle {

	public static void main(String[] args) {
		Graph bulles = new SingleGraph("Bulles");
		
		try{
			bulles.read("norma_N5_tau4_dt2_delai820_000000.dgs");
		}
		catch(Exception e){
			System.out.print("erreur");
		}
		
		double seuilDistance = moyenneDesDistance(bulles)*20/100;
		
		//Création des arrêtes
		for(Node n1:bulles){
			Object[] attrN1 = n1.getAttribute("xyz");
			Point3D p1 = new Point3D((Double)attrN1[0], (Double)attrN1[1], (Double)attrN1[2]);
			
			for(Node n2:bulles){
				Object[] attrN2 = n2.getAttribute("xyz");
				Point3D p2 = new Point3D((Double)attrN2[0], (Double)attrN2[1], (Double)attrN2[2]);
				
				double distanceP1P2 = p1.distance(p2);
				
				if(distanceP1P2 < seuilDistance){
					try{
						bulles.addEdge(n1.getId()+n2.getId(), n1, n2);
					}catch(Exception e){
						//L'arrête existe déjà
					}
					
				}
			}
		}
		bulles.display(false);
		
		//Algo
		for(Node n1:bulles){
			Object[] attrN1 = n1.getAttribute("xyz");
			Point3D p1 = new Point3D((Double)attrN1[0], (Double)attrN1[1], (Double)attrN1[2]);
			for(Edge e1:n1){
				Node n2 = e1.getOpposite(n1);
				Object[] attrN2 = n2.getAttribute("xyz");
				Point3D p2 = new Point3D((Double)attrN2[0], (Double)attrN2[1], (Double)attrN2[2]);
				
				for(Edge e2:n2){
					Node n3 = e2.getOpposite(n2);
					Object[] attrN3 = n2.getAttribute("xyz");
					Point3D p3 = new Point3D((Double)attrN3[0], (Double)attrN3[1], (Double)attrN3[2]);
					//if()
				}
			}
		}
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
