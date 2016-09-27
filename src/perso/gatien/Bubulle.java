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
		for(Node p1:bulles){
			Object[] attrP1 = p1.getAttribute("xyz");
			CoordonneesXYZ cP1 = new CoordonneesXYZ((Double)attrP1[0], (Double)attrP1[1], (Double)attrP1[2]);
			
			for(Node p2:bulles){
				Object[] attrP2 = p2.getAttribute("xyz");
				CoordonneesXYZ cP2 = new CoordonneesXYZ((Double)attrP2[0], (Double)attrP2[1], (Double)attrP2[2]);
				
				double distanceP1P2 = distanceXYZ(cP1,cP2);
				
				if(distanceP1P2 < seuilDistance){
					try{
						bulles.addEdge(p1.getId()+p2.getId(), p1, p2);
					}catch(Exception e){
						//L'arrête existe déjà
					}
					
				}
			}
		}
		bulles.display(false);
		
		//Algo
		for(Node p1:bulles){
			Object[] attrP1 = p1.getAttribute("xyz");
			CoordonneesXYZ cP1 = new CoordonneesXYZ((Double)attrP1[0], (Double)attrP1[1], (Double)attrP1[2]);
			for(Edge e1:p1){
				Node p2 = e1.getOpposite(p1);
				Object[] attrP2 = p2.getAttribute("xyz");
				CoordonneesXYZ cP2 = new CoordonneesXYZ((Double)attrP2[0], (Double)attrP2[1], (Double)attrP2[2]);
				
				for(Edge e2:p2){
					Node p3 = e2.getOpposite(p2);
					Object[] attrP3 = p2.getAttribute("xyz");
					CoordonneesXYZ cP3 = new CoordonneesXYZ((Double)attrP3[0], (Double)attrP3[1], (Double)attrP3[2]);
					//if()
				}
			}
		}
	}
	
	public static double moyenneDesDistance(Graph graph){
		
		double cpt =0.0;
		double sommeDistance=0.0;
		for(Node p0:graph){
			Object[] attributesP0 = p0.getAttribute("xyz");
			CoordonneesXYZ cP0 = new CoordonneesXYZ((Double)attributesP0[0], (Double)attributesP0[1], (Double)attributesP0[2]);

			for(Node p1:graph){
				Object[] attributesP1 = p1.getAttribute("xyz");
				CoordonneesXYZ cP1 = new CoordonneesXYZ((Double)attributesP1[0], (Double)attributesP1[1], (Double)attributesP1[2]);
				
				sommeDistance += distanceXYZ(cP0,cP1);
				cpt++;
			}
		}
		return sommeDistance/cpt;
	}
	
	public static double distanceXYZ(CoordonneesXYZ c0, CoordonneesXYZ c1){
		return Math.sqrt( Math.pow(c1.getX()-c0.getX(),2)+Math.pow(c1.getY()-c0.getY(),2)+Math.pow(c1.getZ()-c0.getZ(),2));
	}
	
	public static boolean verificationDistance(CoordonneesXYZ c1, CoordonneesXYZ c2, CoordonneesXYZ c3, double margeErreur){
		return false;	
	}
	
}
