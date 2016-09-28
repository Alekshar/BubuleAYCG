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

		int cptSerie3 =0;
		int cptSerie4 =0;
		int cptSerie5 =0;
		//Algo
		for(Node n1:bulles){
			Point3D p1 = new Point3D(n1);
			for(Edge e1:n1){
				if(!n1.hasAttribute("serie")){
					Node n2 = e1.getOpposite(n1);
					if(!n1.hasAttribute("serie")&& !n2.hasAttribute("serie")){
						Point3D p2 = new Point3D(n2);
						
						for(Edge e2:n2){
							Node n3 = e2.getOpposite(n2);
							if(!n1.hasAttribute("serie")&& !n2.hasAttribute("serie") && !n3.hasAttribute("serie")){
								//On test si le voisin de n2 n'est pas tout simplement n1
								if(n3.getId()!=n1.getId()){
									Point3D p3 = new Point3D(n3);
									
									//Verification des séries de 3
									//TODO Ajouter la vérification d'angle
									if(p1.verificationDistance( p2, p3, 0.1)){
										cptSerie3++;
										for(Edge e3:n3){
											Node n4 = e3.getOpposite(n3);
											if(!n1.hasAttribute("serie")&& !n2.hasAttribute("serie") && !n3.hasAttribute("serie")&&!n4.hasAttribute("serie")){
												if(n4.getId()!=n2.getId()){
													Point3D p4 = new Point3D(n4);
													//Verification des séries de 4
													//TODO Ajouter vérification d'angle
													if(p2.verificationDistance(p3, p4, 0.1,true)){
														cptSerie4++;
														for(Edge e4:n4){
															Node n5 = e4.getOpposite(n4);
															if(!n1.hasAttribute("serie")&& !n2.hasAttribute("serie") && !n3.hasAttribute("serie")&&!n4.hasAttribute("serie")&&!n5.hasAttribute("serie")){
																if(n5.getId()!=n3.getId()){
																	Point3D p5 = new Point3D(n5);
																	//Verification des séries de 5
																	//TODO Ajouter vérification d'angle
																	if(p5.verificationDistance(p4, p3, 0.1)){
																		cptSerie5++;
																		n1.setAttribute("serie", cptSerie5);	
																		n2.setAttribute("serie", cptSerie5);
																		n3.setAttribute("serie", cptSerie5);
																		n4.setAttribute("serie", cptSerie5);
																		n5.setAttribute("serie", cptSerie5);
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		int cptNode = 0;
		for(Node n:bulles){
			System.out.println(n.hasAttribute("serie")? n.getAttribute("serie") : "null");
			cptNode++;
		}
		System.out.println(cptSerie3+" "+cptSerie4+" "+cptSerie5);
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
