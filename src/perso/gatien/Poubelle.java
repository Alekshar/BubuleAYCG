package perso.gatien;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class Poubelle {
	//A améliorer, trouver une fonction qui vire les séries ayant des trajectoires pas naturelles.
		public static boolean testVariation(Double p1, Double p2, Double p3, Double p4, Double p5){
			//X diminue
			if(p1>p2){
				//X diminue
				if(p2>p3){
					//X diminue
					if(p3>p4){
						//Peu importe si il augmente ou diminue, c'est ok
						return true;
					}
					//X augmente
					else{
						//X diminue
						if(p4>p5){
							return false;
						}else{
							return true;
						}
					}
				}
				//X augmente
				else{
					if(p3>p4){	
						return false;
					}
					//X augmente
					else{
						//X diminue
						if(p4>p5){
							return false;
						}
						//X augmente
						else{
							return true;
						}
					}
				}
			}
			//X augmente
			else{
				//X diminue
				if(p2>p3){
					//X diminue
					if(p3>p4){	
						//X diminue
						if(p4>p5){
							return true;
						}
						//X augmente
						else{
							return false;
						}
					}else{
						return false;
					}
				}
				//X augmente
				else{
					//X diminue
					if(p3>p4){	
						//X diminue
						if(p4>p5){
							return true;
						}
						//X augmente
						else{
							return false;
						}
					}
					//X augmente
					else{
						return true;
					}
				}
			}
		}
		
		//Verifie toutes les distances par rapport à la distance p1/p2 (Utile ?)
		public static boolean verificationDistanceFinale(Point3D p1, Point3D p2,Point3D p3,Point3D p4,Point3D p5, double margeErreur, boolean troisD){
			if(comparerDistance(p1.distance(p2, troisD),p2.distance(p3, troisD),margeErreur,1)
				||comparerDistance(p1.distance(p2, troisD),p3.distance(p4, troisD),margeErreur,2)
				||comparerDistance(p1.distance(p2, troisD),p4.distance(p5, troisD),margeErreur,1)
				){
				return true;
			}
			else{
				return false;
			}
			
		}
		public static boolean comparerDistance(double d1, double d2, double margeErreur, double ratio){
			if(Math.abs(d1*ratio-d2) < d1*margeErreur){
				return true;
			}
			return false;
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
					
					sommeDistance += pA.distance(pB,false);
					cpt++;
				}
			}
			return sommeDistance/cpt;
		}
}
