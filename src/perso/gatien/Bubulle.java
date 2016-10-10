package perso.gatien;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

//Ajouter une meilleure vérification des distances
public class Bubulle {
	static int cptSerie5 =0;
	public static void main(String[] args) {
		Graph bulles = new SingleGraph("Bulles");
		
		try{
			bulles.read("dgs/norma_N5_tau4_dt2_delai820_000001.dgs");
		}
		catch(Exception e){
			System.out.print("erreur");
		}
		
		//Création des arrêtes
		for(double seuilDistance =0.4;seuilDistance<1.1;seuilDistance+=0.2){
			bulles = ProcessUtils.generateEdges(bulles, seuilDistance);
			algo(bulles,0.1,20.0,true);
			algo(bulles,0.2,20.0,true);
			algo(bulles,0.1,20.0,false);
			algo(bulles,0.2,20.0,false);
			algo(bulles,0.3,20.0,true);
			algo(bulles,0.3,20.0,false);
			algo(bulles,0.3,25.0,true);
			algo(bulles,0.3,25.0,false);
			algo(bulles,0.3,30.0,true);
			algo(bulles,0.3,30.0,false);
		}
		
		
		//TODO Suppression des arrêtes de noeud n'appartenant pas à la même série
		removeEdge(bulles);
		addEdgeSameSerie(bulles);
		
		bulles.addAttribute("ui.stylesheet", "url(style/stylesheet);");
		bulles.addAttribute("ui.quality");
		bulles.addAttribute("ui.antialias");
		bulles.display(false);
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
	
	//Refaire les vérifications de distance pour qu'elles se fassent en fonction de D1
	public static void algo(Graph graph,Double margeErreur,Double margeAngle, boolean troisD){
		
		for(Node n1:graph){
			Point3D p1 = new Point3D(n1);
			
			if(!n1.hasAttribute("serie")){
				for(Edge e1:n1){
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
									if(p1.verificationDistance( p2, p3, margeErreur,1,troisD)&&(ProcessUtils.getAngle(p1, p2, p3)<margeAngle)){
										for(Edge e3:n3){
											Node n4 = e3.getOpposite(n3);
											if(!n1.hasAttribute("serie")&& !n2.hasAttribute("serie") && !n3.hasAttribute("serie")&&!n4.hasAttribute("serie")){
												if(n4.getId()!=n2.getId()){
													Point3D p4 = new Point3D(n4);
													//Verification des séries de 4
													if(p2.verificationDistance(p3, p4, margeErreur,2,troisD)&&(ProcessUtils.getAngle(p2, p3, p4)<margeAngle)){
														for(Edge e4:n4){
															Node n5 = e4.getOpposite(n4);
															if(!n1.hasAttribute("serie")&& !n2.hasAttribute("serie") && !n3.hasAttribute("serie")&&!n4.hasAttribute("serie")&&!n5.hasAttribute("serie")){
																if(n5.getId()!=n3.getId()){
																	Point3D p5 = new Point3D(n5);
																	//Verification des séries de 5
																	if(p3.verificationDistance(p4, p5, margeErreur,0.5,troisD)&&(ProcessUtils.getAngle(p3, p4, p5)<margeAngle)){
																		if(testVariation(p1.getX(),p2.getX(),p3.getX(),p4.getX(),p5.getX())&&
																				testVariation(p1.getY(),p2.getY(),p3.getY(),p4.getY(),p5.getY())&&
																				verificationDistanceFinale(p1,p2,p3,p4,p5,margeErreur,troisD)){
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
		}
		System.out.println(cptSerie5);
	}
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
		if(Math.abs(d1*ratio)-d2 < Point3D.seuilErreur(d1, margeErreur)){
			return true;
		}
		return false;
	}
	
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
