package perso.gatien;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

public class Bubulle {

	public static void main(String[] args) {
		Graph bulles = new SingleGraph("Bulles");
		
		try{
			bulles.read("dgs/norma_N5_tau4_dt2_delai820_000001.dgs");
		}
		catch(Exception e){
			System.out.print("erreur");
		}
		
		double seuilDistance = 2;
		
		//Création des arrêtes
		bulles = ProcessUtils.generateEdges(bulles, seuilDistance);
		
		int test =0;
		int cptSerie5 =0;
		//Algo
		for(Node n1:bulles){
			Point3D p1 = new Point3D(n1);
			
			if(!n1.hasAttribute("serie")){
				//System.out.println("(1)"+n1.getId()+"\t");
				//n1.addAttribute("ui.label", n1.getId());
				for(Edge e1:n1){
					Node n2 = e1.getOpposite(n1);
					if(!n1.hasAttribute("serie")&& !n2.hasAttribute("serie")){
						//System.out.println("(2)"+n2.getId()+"\t");
						Point3D p2 = new Point3D(n2);
						
						for(Edge e2:n2){
							Node n3 = e2.getOpposite(n2);
							if(!n1.hasAttribute("serie")&& !n2.hasAttribute("serie") && !n3.hasAttribute("serie")){
								//On test si le voisin de n2 n'est pas tout simplement n1
								if(n3.getId()!=n1.getId()){
									Point3D p3 = new Point3D(n3);
									
									//Verification des séries de 3
									if(p1.verificationDistance( p2, p3, 0.1,1)&&(ProcessUtils.getAngle(p1, p2, p3)<10)){
										//System.out.println("(3)"+n3.getId()+"\t"+ProcessUtils.getAngle(p1, p2, p3));
										for(Edge e3:n3){
											Node n4 = e3.getOpposite(n3);
											if(!n1.hasAttribute("serie")&& !n2.hasAttribute("serie") && !n3.hasAttribute("serie")&&!n4.hasAttribute("serie")){
												if(n4.getId()!=n2.getId()){
													Point3D p4 = new Point3D(n4);
													//Verification des séries de 4
													//TODO Ajouter vérification d'angle
													if(p2.verificationDistance(p3, p4, 0.1,2)&&(ProcessUtils.getAngle(p2, p3, p4)<10)){
														//System.out.println("(4)"+n4.getId()+"\t"+ProcessUtils.getAngle(p2, p3, p4));
														for(Edge e4:n4){
															Node n5 = e4.getOpposite(n4);
															if(!n1.hasAttribute("serie")&& !n2.hasAttribute("serie") && !n3.hasAttribute("serie")&&!n4.hasAttribute("serie")&&!n5.hasAttribute("serie")){
																if(n5.getId()!=n3.getId()){
																	Point3D p5 = new Point3D(n5);
																	//Verification des séries de 5
																	//TODO Ajouter vérification d'angle
																	//System.out.println(ProcessUtils.getAngle(p3, p4, p5));
																	if(p3.verificationDistance(p4, p5, 0.1,0.5)&&(ProcessUtils.getAngle(p3, p4, p5)<10)){
																		//System.out.println("(5)"+n5.getId()+"\t"+ProcessUtils.getAngle(p3, p4, p5));
																		cptSerie5++;
																		n1.setAttribute("serie", cptSerie5);
																		n1.setAttribute("indexSerie", 1);
																		n2.setAttribute("serie", cptSerie5);
																		n2.setAttribute("indexSerie", 2);
																		n3.setAttribute("serie", cptSerie5);
																		n3.setAttribute("indexSerie", 3);
																		n4.setAttribute("serie", cptSerie5);
																		n4.setAttribute("indexSerie", 4);
																		n5.setAttribute("serie", cptSerie5);
																		n5.setAttribute("indexSerie", 5);
																		n1.addAttribute("ui.label", cptSerie5+"("+1+")");
																		n2.addAttribute("ui.label", cptSerie5+"("+2+")");
																		n3.addAttribute("ui.label", cptSerie5+"("+3+")");
																		n4.addAttribute("ui.label", cptSerie5+"("+4+")");
																		n5.addAttribute("ui.label", cptSerie5+"("+5+")");
																		
																		/*System.out.print(p1.distance(p2)+" ");
																		System.out.print(p2.distance(p3)+" ");
																		System.out.print(p3.distance(p4)+" ");
																		System.out.println(p4.distance(p5)+" ");*/
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
		
		//TODO Suppression des arrêtes de noeud n'appartenant pas à la même série
		removeEdge(bulles);
		addEdgeSameSerie(bulles);
		
		bulles.addAttribute("ui.stylesheet", "url(style/stylesheet);");
		bulles.addAttribute("ui.quality");
		bulles.addAttribute("ui.antialias");
		bulles.display(false);
		System.out.println(test);
		System.out.println(cptSerie5);
	}
	public static void addEdgeSameSerie(Graph graph){
		for(Node n1:graph){
			for(Node n2:graph){
				if(n1.hasAttribute("serie")&&n2.hasAttribute("serie")){
					if(n1.getId()!=n2.getId()&&n1.getAttribute("serie").equals(n2.getAttribute("serie"))){
						if(n1.getNumber("indexSerie")-n2.getNumber("indexSerie")==1){
							if(!n1.hasEdgeBetween(n2)){
								graph.addEdge(n1.getId()+n2.getId(), n1, n2);
							}
						}
					}
				}
			}
		}
	}
	public static void removeEdge(Graph graph){
		while (graph.getEdgeCount() > 0)
		    graph.removeEdge(0);
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
