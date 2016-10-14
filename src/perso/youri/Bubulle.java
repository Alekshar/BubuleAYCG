package perso.youri;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.json.*;

//Ajouter une meilleure vérification des distances
public class Bubulle {
	
	private Graph bulles;
	private int numTrajectoire;
	private ArrayList<String> alTrajectoire;
	
	public Bubulle(Graph graph) throws JSONException, IOException {
		bulles = graph;
		numTrajectoire = 0;
		alTrajectoire = new ArrayList<String>();
		
		JSONObject jobj = new JSONObject(ProcessUtils.fileString("conf/configuration.json"));
		
		for(double seuilDistance =jobj.getJSONObject("seuilDistance").getDouble("min");seuilDistance<jobj.getJSONObject("seuilDistance").getDouble("max");seuilDistance+=jobj.getJSONObject("seuilDistance").getDouble("pas")){
			JSONArray arr = jobj.getJSONArray("tests");
			for(int i=0;i<arr.length();i++){
				algo(bulles,seuilDistance,arr.getJSONObject(i).getDouble("margeErreur"),arr.getJSONObject(i).getDouble("angleMax"),arr.getJSONObject(i).getBoolean("3D"));
			}
		}
		
		removeEdge(bulles);
		addEdgeSameSerie(bulles);
		
		bulles.addAttribute("ui.stylesheet", "url(style/stylesheet);");
		bulles.addAttribute("ui.quality");
		bulles.addAttribute("ui.antialias");		
	}
	
	//Ajoutes les arètes des séries après analyse de l'algo
	public void addEdgeSameSerie(Graph graph){
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
	//Supprime toutes les arètes d'un graphe
	public void removeEdge(Graph graph){
		while (graph.getEdgeCount() > 0)
		    graph.removeEdge(0);
	}
	
	//Génère les arètes d'un graphe dont la distance est inférieure a  distanceLimit
	public Graph generateEdges(Graph graph, double distanceLimit, boolean troisD) {
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
	
	/** ParamÃ¨tres
		* @param graph 			: Graphe reprÃ©sentant le fichier de point dont les arrÃªtes ont Ã©tÃ© prÃ©alablement crÃ©e
		* @param margeErreur 	: Marge d'erreur permise entre 2 distances d'une mÃªme sÃ©rie
		* @param angleMax 		: Angle maximum que peut prendre une sÃ©rie de 3 points
		* @param troisD 		: boolean spÃ©cifiant si les distances sont gÃ©rÃ©s en troisD ou non
	**/
	//TODO voir ce qu'il se passe avec une gestion d'angle en troisD lorsque le paramÃ¨tre troisD est Ã  true.
	//TODO ajouter la crÃ©ation d'arrÃªte dans l'algo et le paramÃ¨tre de seuil distance dans celui ci.
	public void algo(Graph graph, Double seuilDistance, Double margeErreur,Double angleMax, boolean troisD){
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
									
									//Verification des sï¿½ries de 3
									if(p1.verificationDistance( p2, p3, margeErreur,1,troisD)&&(ProcessUtils.getAngle(p1, p2, p3)<angleMax)){
										for(Edge e3:n3){
											Node n4 = e3.getOpposite(n3);
											if(!n1.hasAttribute("serie")&& !n2.hasAttribute("serie") && !n3.hasAttribute("serie")&&!n4.hasAttribute("serie")){
												if(n4.getId()!=n2.getId()){
													Point3D p4 = new Point3D(n4);
													//Verification des sï¿½ries de 4
													if(p2.verificationDistance(p3, p4, margeErreur,2,troisD)&&(ProcessUtils.getAngle(p2, p3, p4)<angleMax)){
														for(Edge e4:n4){
															Node n5 = e4.getOpposite(n4);
															if(!n1.hasAttribute("serie")&& !n2.hasAttribute("serie") && !n3.hasAttribute("serie")&&!n4.hasAttribute("serie")&&!n5.hasAttribute("serie")){
																if(n5.getId()!=n3.getId()){
																	Point3D p5 = new Point3D(n5);
																	//Verification des sï¿½ries de 5
																	if(p3.verificationDistance(p4, p5, margeErreur,0.5,troisD)&&(ProcessUtils.getAngle(p3, p4, p5)<angleMax)){																																									
																			n1.setAttribute("serie", numTrajectoire);
																			n1.setAttribute("indexSerie", 1);
																			n2.setAttribute("serie", numTrajectoire);
																			n2.setAttribute("indexSerie", 2);
																			n3.setAttribute("serie", numTrajectoire);
																			n3.setAttribute("indexSerie", 3);
																			n4.setAttribute("serie", numTrajectoire);
																			n4.setAttribute("indexSerie", 4);
																			n5.setAttribute("serie", numTrajectoire);
																			n5.setAttribute("indexSerie", 5);
																			n1.addAttribute("ui.label", numTrajectoire+"("+1+")");
																			n2.addAttribute("ui.label", numTrajectoire+"("+2+")");
																			n3.addAttribute("ui.label", numTrajectoire+"("+3+")");
																			n4.addAttribute("ui.label", numTrajectoire+"("+4+")");
																			n5.addAttribute("ui.label", numTrajectoire+"("+5+")");
																			
																			numTrajectoire++;
																			alTrajectoire.add(n1.getId().split("B")[1]+" "+n2.getId().split("B")[1]+" "+n3.getId().split("B")[1]+" "+n4.getId().split("B")[1]+" "+n5.getId().split("B")[1]);
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
	
	public void getTrajectoire(String path){
		try {
			FileWriter fw = new FileWriter(path);
			for(String trajectoire: alTrajectoire ){
				fw.write(trajectoire+"\n");
			}
			fw.close();
		} catch (IOException e) {e.printStackTrace();}
		
	}
	/************/
	/* GET/ SET */
	/************/
	public Graph getGraph(){return this.bulles;}
	
	
}
