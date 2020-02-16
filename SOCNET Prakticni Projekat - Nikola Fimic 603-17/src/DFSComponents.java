import java.util.HashSet;
import java.util.Set;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class DFSComponents<V, E> {
	Set<V> visited;
	Set<Set<V>> components;
	UndirectedSparseGraph<V, E> graph;
	
	
	public DFSComponents(UndirectedSparseGraph<V, E> graph) {
		this.graph = graph;
	}
	
	public Set<Set<V>> identifyComponents() {
		visited = new HashSet<>();
		components = new HashSet<>();
		
		for (V vertice : graph.getVertices()) {
			if(!visited.contains(vertice)) {
				components.add(identifyComponent(vertice));
			}
		}
		return components;
	}
	
	private Set<V> identifyComponent(V vertice) {
		Set<V> component = new HashSet<>();
		component.add(vertice);
		visited.add(vertice);
		
		dfs(vertice, component);
		
		return component;
		
		
		
	}
	
	private void dfs(V current, Set<V> component) {
		for (V neighbour : graph.getNeighbors(current)) {
			if(!visited.contains(neighbour)) {
				component.add(neighbour);
				visited.add(neighbour);
				dfs(neighbour, component);
			}
		}
		
	}
	
}
