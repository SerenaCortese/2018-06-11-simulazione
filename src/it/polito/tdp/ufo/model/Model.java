package it.polito.tdp.ufo.model;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.ufo.db.SightingsDAO;

public class Model {
	
	private SightingsDAO dao;
	
	private Graph<String, DefaultEdge> graph;
	
	private List<String> stati;
	
	private List<String> sequenzaOttima;
	
	public Model() {
		dao = new SightingsDAO();	
	}

	public List<AnnoAvvistamenti> getYears() {
		List<AnnoAvvistamenti> anniAvvistamenti = dao.getYearsSightings();
		return anniAvvistamenti;
	}
	
	public void createGraph(Year anno) {
		
		graph = new SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
		
		stati = dao.getStates(anno);
		
		Graphs.addAllVertices(this.graph, stati);
		
		System.out.println("# vertici : "+ graph.vertexSet().size());
		
//		for(String s1 : stati) {
//			for(String s2: stati) {
//				if(!s1.equals(s2)) {
//					if(dao.esisteArco(s1,s2,anno)) {
//						graph.addEdge(s1, s2);
//					}
//				}
//			}
//		}
		
		//METODO PIU' VELOCE!
		List<Arco> archi = dao.getArchi(anno);
		for(Arco a : archi) {
			
			graph.addEdge(a.getState1(), a.getState2());
		}
		
		System.out.println("# archi : "+ graph.edgeSet().size());
	}

	public List<String> getStates() {
		return stati;
	}

	public List<String> getPrecedentiDi(String stato) {
		return Graphs.predecessorListOf(this.graph, stato);
	}

	public List<String> getSuccessiviDi(String stato) {
		return Graphs.successorListOf(this.graph, stato);
	}

	public Set<String> getRaggiungiliDa(String stato) {
		
		Set<String> visited = new HashSet<String>();
		
		GraphIterator<String, DefaultEdge> bfv = new BreadthFirstIterator<String, DefaultEdge>(graph,stato); 
		bfv.next(); 
		while (bfv.hasNext()) { 
			visited.add(bfv.next());
		}
		return visited;
	}

	public List<String> getCamminoPiuLungo(String stato) {
		this.sequenzaOttima = new ArrayList<String>();
		List<String> parziale = new ArrayList<String>();
		parziale.add(stato);
		ricorsione(parziale);
		return sequenzaOttima;
	}
	
	private void ricorsione(List<String> parziale) {
		
		//condizione di terminazione
		if(parziale.size()>sequenzaOttima.size()) {
			System.out.println(parziale.toString()+"\n");
			sequenzaOttima = new ArrayList<>(parziale);
		}
		
		List<String> successori = this.getSuccessiviDi(parziale.get(parziale.size()-1)); 
		for(String prova : successori) {
			if(!parziale.contains(prova)) {
				parziale.add(prova);
				ricorsione(parziale);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}
	

}
