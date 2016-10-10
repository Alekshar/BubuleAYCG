package perso.gatien;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

//Ajouter une meilleure v�rification des distances
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
		
		//Cr�ation des arr�tes
		for(double seuilDistance =0.2;seuilDistance<1.3;seuilDistance+=0.2){
			algo(bulles,seuilDistance,0.1,20.0,true);
			algo(bulles,seuilDistance,0.1,20.0,false);
			algo(bulles,seuilDistance,0.2,20.0,true);			
			algo(bulles,seuilDistance,0.2,20.0,false);				
			algo(bulles,seuilDistance,0.3,20.0,true);
			algo(bulles,seuilDistance,0.3,20.0,false);
			algo(bulles,seuilDistance,0.3,25.0,true);
			algo(bulles,seuilDistance,0.3,25.0,false);
			algo(bulles,seuilDistance,0.3,30.0,true);
		}
		
		removeEdge(bulles);
		addEdgeSameSerie(bulles);
		
		bulles.addAttribute("ui.stylesheet", "url(style/stylesheet);");
		bulles.addAttribute("ui.quality");
		bulles.addAttribute("ui.antialias");
		bulles.display(false);
		
	}
	
	//Ajoutes les arrêtes des séries après analyse de l'algo
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
	//Supprime toutes les arêtes d'un graphe
	public static void removeEdge(Graph graph){
		while (graph.getEdgeCount() > 0)
		    graph.removeEdge(0);
	}
	
	//Génère les arêtes d'un graphe dont la distance est inférieure à distanceLimit
	public static Graph generateEdges(Graph graph, double distanceLimit, boolean troisD) {
		for (Node n1 : graph) {
			Point3D p1 = new Point3D(n1);
			for (Node n2 : graph) {
				if(n2.getId()!=n1.getId()){
					Point3D p2 = new Point3D(n2);
					double distanceP1P2 = p1.distance(p2,troisD);
					if (distanceP1P2 < distanceLimit) {
						try {
							graph.addEdge(n1.getId() + n2.getId(), n1, n2);
						} catch (Exception e) {
							// Edge already exists
						}
					}
				}
			}
		}
		return graph;
	}
	
	/** Paramètres
		* @param graph 			: Graphe représentant le fichier de point dont les arrêtes ont été préalablement crée
		* @param margeErreur 	: Marge d'erreur permise entre 2 distances d'une même série
		* @param angleMax 		: Angle maximum que peut prendre une série de 3 points
		* @param troisD 		: boolean spécifiant si les distances sont gérés en troisD ou non
	**/
	//TODO voir ce qu'il se passe avec une gestion d'angle en troisD lorsque le paramètre troisD est à true.
	//TODO ajouter la création d'arrête dans l'algo et le paramètre de seuil distance dans celui ci.
	public static void algo(Graph graph, Double seuilDistance, Double margeErreur,Double angleMax, boolean troisD){
		generateEdges(graph, seuilDistance, troisD);
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
									
									//Verification des s�ries de 3
									if(p1.verificationDistance( p2, p3, margeErreur,1,troisD)&&(ProcessUtils.getAngle(p1, p2, p3)<angleMax)){
										for(Edge e3:n3){
											Node n4 = e3.getOpposite(n3);
											if(!n1.hasAttribute("serie")&& !n2.hasAttribute("serie") && !n3.hasAttribute("serie")&&!n4.hasAttribute("serie")){
												if(n4.getId()!=n2.getId()){
													Point3D p4 = new Point3D(n4);
													//Verification des s�ries de 4
													if(p2.verificationDistance(p3, p4, margeErreur,2,troisD)&&(ProcessUtils.getAngle(p2, p3, p4)<angleMax)){
														for(Edge e4:n4){
															Node n5 = e4.getOpposite(n4);
															if(!n1.hasAttribute("serie")&& !n2.hasAttribute("serie") && !n3.hasAttribute("serie")&&!n4.hasAttribute("serie")&&!n5.hasAttribute("serie")){
																if(n5.getId()!=n3.getId()){
																	Point3D p5 = new Point3D(n5);
																	//Verification des s�ries de 5
																	if(p3.verificationDistance(p4, p5, margeErreur,0.5,troisD)&&(ProcessUtils.getAngle(p3, p4, p5)<angleMax)){																																									
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
																			
																			cptSerie5++;
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
}
