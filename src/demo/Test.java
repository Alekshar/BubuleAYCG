package demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Graph graph = new SingleGraph("Le Havre");
		
		try{
			graph.read("lh_tram.dgs");
		}
		catch(Exception e){
			System.out.print("erreur");
		}
		
		analyseAvecTram(graph);
	}
	
	//Retourne le temps en heure pour effectuer une distance à pied
	public static double tpsTrajetPiedHeure(double metre){
		double vitessePieton = 4.5; //en km/h
		return (metre/1000)/vitessePieton;	// /1000 = conversion metre vers kilomettre
	}
	
	//Retourne le temps en heure pour effectuer une distance en voiture
	public static double tpsTrajetVoitureHeure(double metre){
		double vitesseVoiture = 15.0; //en km/h
		double tempsFixe = 15.0/60; //conversion en heure
		return ( (metre/1000)/ vitesseVoiture) + tempsFixe ;
	}
	
	//Retourne le temps en heure pour effectuer une distance en tram
	public static double tpsTrajetTramHeure(double metre){
		double vitesseVoiture = 40.0; //en km/h
		return ( (metre/1000)/ vitesseVoiture) ;
	}

	public static void analyseSansTram(Graph g){
		Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, null, "length");
		
		double sommeTpsVoiture = 0;
		double sommeTpsPieton = 0;
		double nbTrajet = 0;
		double nbTrajetVoiture = 0;
		double nbTrajetPieton = 0;
		
		dijkstra.init(g);
		for(Node depart: g){
			dijkstra.setSource(depart);
			dijkstra.compute();	
			
			for(Node arrive: g){
				
				double nbMetre = dijkstra.getPathLength(arrive);
				
				if(nbMetre <= 1){
					//On ne fait rien, cela correspond à un trajet ou l'on reste sur palce
				}
				else{
					if(tpsTrajetPiedHeure(nbMetre) > tpsTrajetVoitureHeure(nbMetre)){
						sommeTpsVoiture += tpsTrajetVoitureHeure(nbMetre);
						nbTrajetVoiture++;
					}else{
						sommeTpsPieton += tpsTrajetPiedHeure(nbMetre);
						nbTrajetPieton++;
					}
				}
			}
		}
		
		nbTrajet = nbTrajetVoiture+nbTrajetPieton;
		double tempsMoyenPietonHeure = sommeTpsPieton/nbTrajetPieton;
		double tempsMoyenVoitureHeure = sommeTpsVoiture/nbTrajetVoiture;
		double pourcentTrajetPieton = (nbTrajetPieton/nbTrajet)*100;
		double pourcentTrajetVoiture = (nbTrajetVoiture/nbTrajet)*100;
		
		System.out.println("Temps moyen pieton : "+affichageHM(tempsMoyenPietonHeure));
		System.out.println("Temps moyen Voiture : "+affichageHM(tempsMoyenVoitureHeure));	
		System.out.println("Pourcentage trajet pieton : "+pourcentTrajetPieton+"%");
		System.out.println("Pourcentage trajet voiture : "+pourcentTrajetVoiture+"%");
	}
	
	public static void analyseAvecTram(Graph g){
		
		System.out.println("--- Construction du sous graphe ---");
	/*** DEBUT : Construction du sous graphe des trams ***/
		Graph tram = new SingleGraph("Tram");
		
		try{
			tram.read("lh_tram.dgs");
		}
		catch(Exception e){
			System.out.print("erreur");
		}
		
		ArrayList<Node> alNodeSupprimer = new ArrayList<Node>();
		
		for(Node n:tram){

			boolean noeudDuTram = false;
			for(Edge e:n){
				if(e.hasAttribute("tramA")||e.hasAttribute("tramB")){
					noeudDuTram = true;
				}
			}
			
			if(!noeudDuTram){
				alNodeSupprimer.add(n);
			}
		}

		for(Node n:alNodeSupprimer){
			tram.removeNode(n.getId());
		}
		
	/*** FIN : Construction du sous graphe des trams ***/
		System.out.println("--- Construction de la liste d'arrêt de tram ---");
		ArrayList<Node> listeArretsTram = new ArrayList<Node>();
		for(Node n: g){
			if(n.hasAttribute("tramA")||n.hasAttribute("tramB")){
				listeArretsTram.add(n);
			}
		}
		Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, null, "length");
		dijkstra.init(g);
		
		Dijkstra dijkstraTram = new Dijkstra(Dijkstra.Element.EDGE, null, "length");
		dijkstraTram.init(tram);
		
		System.out.println("--- Initialisation des variables à 0 ---");
		double nbTrajet = 0;
		
		double[] tabSommeTpsVoiture = {0,0,0,0,0,0,0};
		double[] tabSommeTpsPieton = {0,0,0,0,0,0,0};
		double[] tabS = {0,0,0,0,0,0,0};
		double[] tabNbTrajetVoiture = {0,0,0,0,0,0,0};
		double[] tabNbTrajetPieton = {0,0,0,0,0,0,0};
		double[] tabNbTrajetTram = {0,0,0,0,0,0,0};
		double[] tabTempsEntreTram = {(double)3/60, (double)4/60,(double)5/60,(double)8/60,(double)10/60,(double)15/60,(double)20/60};
		
		Map<String, double[]> map = new HashMap<String, double[]>();
		map.put("sommeTpsVoiture", tabSommeTpsVoiture);
		map.put("sommeTpsPieton", tabSommeTpsPieton);
		map.put("sommeTpsTram", tabS);
		map.put("nbTrajetVoiture", tabNbTrajetVoiture);
		map.put("nbTrajetPieton", tabNbTrajetPieton);
		map.put("nbTrajetTram", tabNbTrajetTram);
		map.put("tempsEntreTram", tabTempsEntreTram);
		
		
		//Pour tout les points
		for(Node depart: g){
			//On lance dijkstra
			dijkstra.setSource(depart);
			dijkstra.compute();	
			
			//On récupère le plus proche arrêt de tram du noeud de départ
			Node plusProcheArrêtDepart = depart;
			double distancePlusProcheArretDepart = -1;	//Va contenir la distance en metre du point de depart à l'arret de tram
			for(Node arretTram: listeArretsTram){
				if(distancePlusProcheArretDepart == -1 ){
					distancePlusProcheArretDepart = dijkstra.getPathLength(arretTram);
					plusProcheArrêtDepart = arretTram;
				}
				if(distancePlusProcheArretDepart > dijkstra.getPathLength(arretTram)){
					distancePlusProcheArretDepart = dijkstra.getPathLength(arretTram);
					plusProcheArrêtDepart = arretTram;
				}
			}
			
			//Dijkstra dans les trams
			dijkstraTram.setSource( tram.getNode( plusProcheArrêtDepart.getId() ) );
			dijkstraTram.compute();
			
			//Pour tout les noeuds d'arrivé
			for(Node arrive: g){
				double nbMetreSansTram = dijkstra.getPathLength(arrive);
				
			/*** DEBUT : Calcul pour tram ***/
				dijkstra.setSource(arrive);	
				
				//On trouve l'arrêt de tram le plus proche et on calcul la distance
				Node plusProcheArrêtArrive = arrive;
				double distancePlusProcheArretArrive = -1;
				for(Node arretTram: listeArretsTram){
					if(distancePlusProcheArretArrive == -1 ){
						distancePlusProcheArretArrive = dijkstra.getPathLength(arretTram);
						plusProcheArrêtArrive = arretTram;
					}
					if(distancePlusProcheArretArrive > dijkstra.getPathLength(arretTram)){
						distancePlusProcheArretArrive = dijkstra.getPathLength(arretTram);
						plusProcheArrêtArrive = arretTram;
					}
				}
				
				double distanceTrajetTram = dijkstraTram.getPathLength(tram.getNode( plusProcheArrêtArrive.getId() ));
				
				for(int i = 0; i < 7 ; i++){
					//Calcul temps d'attente du tram
						double tpsFixe = 0;
						
						//Si on est pas sur la même ligne que l'arrêt d'arrive
						if(plusProcheArrêtDepart.hasAttribute("tramA")&& plusProcheArrêtArrive.hasAttribute("tramB")
								|| plusProcheArrêtDepart.hasAttribute("tramB")&& plusProcheArrêtArrive.hasAttribute("tramA")){
							tpsFixe = (map.get("tempsEntreTram")[i]/2)*2;
						}
						else{
							tpsFixe = map.get("tempsEntreTram")[i]/2;
						}	
					//
					
					double tpsTramHeure = tpsTrajetTramHeure(distanceTrajetTram);
					double tpsPiedHeure = tpsTrajetPiedHeure(distancePlusProcheArretDepart + distancePlusProcheArretArrive);
					double tpsTramTotalHeure = tpsTramHeure+tpsPiedHeure+tpsFixe;
					
				/*** FIN : Calcul pour tram ***/
				
				/*** DEBUT : Choix tram,piéton,voiture ***/
					
					if(nbMetreSansTram <= 1){
						//On ne fait rien, cela correspond à un trajet ou l'on reste sur place
					}
					else{
						/*if(tpsTrajetPiedHeure(nbMetreSansTram) >= tpsTrajetVoitureHeure(nbMetreSansTram)){
							if(tpsTramTotalHeure >= tpsTrajetVoitureHeure(nbMetreSansTram)){
								map.get("sommeTpsVoiture")[i] += tpsTrajetVoitureHeure(nbMetreSansTram);
								map.get("nbTrajetVoiture")[i]++;
							}else{
								map.get("sommeTpsTram")[i] += tpsTramTotalHeure;
								map.get("nbTrajetTram")[i]++;
								depart.addAttribute("ui.class", "tram"+i);
							}
						}else{
							if(tpsTramTotalHeure >= tpsTrajetPiedHeure(nbMetreSansTram)){
								map.get("sommeTpsPieton")[i] += tpsTrajetPiedHeure(nbMetreSansTram);
								map.get("nbTrajetPieton")[i]++;
							}else{
								map.get("sommeTpsTram")[i] += tpsTramTotalHeure;
								map.get("nbTrajetTram")[i]++;
								depart.addAttribute("ui.class", "tram"+i);
							}
						}*/
						
						if(tpsTrajetPiedHeure(nbMetreSansTram) > (double)20/60){
							if(tpsPiedHeure > (double)20/60){
								map.get("sommeTpsVoiture")[i] += tpsTrajetVoitureHeure(nbMetreSansTram);
								map.get("nbTrajetVoiture")[i]++;
							}
							else{
								if(tpsTramTotalHeure >= tpsTrajetVoitureHeure(nbMetreSansTram)){
									map.get("sommeTpsVoiture")[i] += tpsTrajetVoitureHeure(nbMetreSansTram);
									map.get("nbTrajetVoiture")[i]++;
								}
								else{
									map.get("sommeTpsTram")[i] += tpsTramTotalHeure;
									map.get("nbTrajetTram")[i]++;
									depart.addAttribute("ui.class", "tram"+i);
								}	
							}
						}else{
							if(tpsTramTotalHeure >= tpsTrajetPiedHeure(nbMetreSansTram)){
								map.get("sommeTpsPieton")[i] += tpsTrajetPiedHeure(nbMetreSansTram);
								map.get("nbTrajetPieton")[i]++;
							}else{
								map.get("sommeTpsTram")[i] += tpsTramTotalHeure;
								map.get("nbTrajetTram")[i]++;
								depart.addAttribute("ui.class", "tram"+i);
							}
						}
					}
				}
			/*** FIN : Choix tram,piéton,voiture ***/
			}
		}
		g.setAttribute("ui.quality");
		g.setAttribute("ui.antialias");
		
		for(Node n : g){
			if(n.hasAttribute("tramA")&&n.hasAttribute("tramB")){
				n.addAttribute("ui.class", "tramAB");
			}else if(n.hasAttribute("tramA")){
				n.addAttribute("ui.class", "tramA");
			}
			else if(n.hasAttribute("tramB")){
				n.addAttribute("ui.class", "tramB");
			}
			
			for(Edge e:n){
				if(e.hasAttribute("tramA")&&e.hasAttribute("tramB")){
					e.addAttribute("ui.class", "tramAB");
				}else if(e.hasAttribute("tramA")){
					e.addAttribute("ui.class", "tramA");
				}
				else if(e.hasAttribute("tramB")){
					e.addAttribute("ui.class", "tramB");
				}
			}
		}
			
		for(int i = 0 ; i < 7 ; i++){
			nbTrajet = map.get("nbTrajetVoiture")[i]+map.get("nbTrajetPieton")[i]+map.get("nbTrajetTram")[i];
			double tempsMoyenPietonHeure = map.get("sommeTpsPieton")[i]/map.get("nbTrajetPieton")[i];
			double tempsMoyenVoitureHeure = map.get("sommeTpsVoiture")[i]/map.get("nbTrajetVoiture")[i];
			double tempsMoyenTramHeure = map.get("sommeTpsTram")[i]/map.get("nbTrajetTram")[i];
			double pourcentTrajetPieton = (map.get("nbTrajetPieton")[i]/nbTrajet)*100;
			double pourcentTrajetVoiture = (map.get("nbTrajetVoiture")[i]/nbTrajet)*100;
			double pourcentTrajetTram = (map.get("nbTrajetTram")[i]/nbTrajet)*100;
			System.out.println("<<<----------- "+i+" ------------");
			System.out.println("Temps entre chaque tram : "+affichageHM(map.get("tempsEntreTram")[i]));
			System.out.println("Temps moyen pieton : "+affichageHM(tempsMoyenPietonHeure));
			System.out.println("Temps moyen Voiture : "+affichageHM(tempsMoyenVoitureHeure));	
			System.out.println("Temps moyen Tram : "+affichageHM(tempsMoyenTramHeure));	
			System.out.println("Pourcentage trajet pieton : "+pourcentTrajetPieton+"%");
			System.out.println("Pourcentage trajet voiture : "+pourcentTrajetVoiture+"%");
			System.out.println("Pourcentage trajet tram : "+pourcentTrajetTram+"%");
			System.out.println("-------------------------->>>");
			
			String styleSheet="node{ size: 4px; z-index: 0;}"+
					"node.tram"+i+" {" +
	                "fill-color: #1F9C96;" +
	                "}"+
	                "node.tramA{ size: 8px; fill-color: red; z-index: 1;}"+
	                "node.tramB{ size: 8px; fill-color: green; z-index: 1;}"+
	                "node.tramAB{ size: 8px; fill-color: blue; z-index: 1;}"+
	                "edge{shape: line;z-index: 0;}"+
	                "edge.tramA{ fill-color: red; shape: line;z-index: 1;}"+
	                "edge.tramB{ fill-color: green; shape: line;z-index: 1;}"+
	                "edge.tramAB{ fill-color: blue; shape: line;z-index: 1;}";
			
			g.addAttribute("stylesheet", styleSheet);
			g.display(false);
		}
		
	}
	public static String affichageHM(double heure){
		int h = (int) heure;
		int m = (int) ((heure - h)*60);

		return m<10 ? h+"h0"+m+"min": h+"h"+m+"min";
	}
}