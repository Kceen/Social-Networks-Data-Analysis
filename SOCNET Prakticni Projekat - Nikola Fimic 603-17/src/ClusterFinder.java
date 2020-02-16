import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections15.Transformer;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class ClusterFinder<V, E> {
	Set<E> edgesToDeleteIfNotClusterable;
	Collection<Set<V>> NOTCoalitionClusterCollection; 
	
	public Set<E> getEdgesToDeleteIfNotClusterable() {
		return edgesToDeleteIfNotClusterable;
	}

	public Collection<Set<V>> getNOTCoalitionClusterCollection() {
		return NOTCoalitionClusterCollection;
	}

	public Set<Set<V>> findClusters(UndirectedSparseGraph<V, E> graph, Transformer<E, Boolean> transformer) {
		
		// REMOVE EDGES WITH NEGATIVE RELATION
		Collection<E> edges = graph.getEdges();
		Iterator<E> edgeIterator = edges.iterator();
		List<E> toRemove = new ArrayList<>();
		
		while (edgeIterator.hasNext()) {
			E edge = edgeIterator.next();
			if (transformer.transform(edge) == false) {
				toRemove.add(edge);
			}

		}
		
		for (E edge : toRemove) {
			graph.removeEdge(edge);
		}
		//////////////////////////////////////////////////////////////////////////////////

		
		//IDENTIFY COMPONENTS USING DFS
		DFSComponents<V, E> dfsComponents = new DFSComponents<>(graph);
		Set<Set<V>> components = dfsComponents.identifyComponents();
		
		return components;
		//////////////////////////////////////////////////////////////////////////////////
	}
	
	public boolean isClusterable(UndirectedSparseGraph<V, E> backupGraph, UndirectedSparseGraph<V, E> graph, Set<Set<V>> clusters, Transformer<E, Boolean> transformer) {
		boolean isClusterableBoolean = true;
		edgesToDeleteIfNotClusterable = new HashSet<>();
		NOTCoalitionClusterCollection = new HashSet<>();
		
		//ITERATE THROUGH EACH CLUSTER
		Iterator<Set<V>> clusterIterator = clusters.iterator();
		while (clusterIterator.hasNext()) {
			Set<V> cluster = clusterIterator.next();
			Iterator<V> verticesInClusterIterator = cluster.iterator();
			
			//ITERATE THROUGH VERTICES OF EACH INDIVUDUAL CLUSTER
			while (verticesInClusterIterator.hasNext()) {
				V checkingVertice = verticesInClusterIterator.next();
				Iterator<V> verticesInClusterIterator2 = cluster.iterator();
				//AGAIN ITERATE THROUGH VERTICES OF CURRENT CLUSTER AND CHECK IF THERE IS A NEGATIVE EDGE BETWEEN ANY TWO VERTICES IN CURRENT CLUSTER
				while (verticesInClusterIterator2.hasNext()) {
					V verticeToCheck = verticesInClusterIterator2.next();
					if (backupGraph.findEdge(checkingVertice, verticeToCheck) != null
							&& transformer.transform(backupGraph.findEdge(checkingVertice, verticeToCheck)) == false) {
						edgesToDeleteIfNotClusterable.add(backupGraph.findEdge(checkingVertice, verticeToCheck));
						NOTCoalitionClusterCollection.add(cluster);
						isClusterableBoolean = false;
					}
				}

			}

		}
		return isClusterableBoolean;
	}

	public Collection<UndirectedSparseGraph<V, E>> clustersAsGraphs(UndirectedSparseGraph<V, E> graph, Set<Set<V>> clusters) {
		Collection<UndirectedSparseGraph<V, E>> clustersAsGraphsCollection = new ArrayList<>();
		Iterator<Set<V>> clustersIterator = clusters.iterator();
		
		while (clustersIterator.hasNext()) {
			Set<V> cluster = clustersIterator.next();
			
			UndirectedSparseGraph<V, E> clusterGraph = clusterAsGraph(graph, cluster);
			clustersAsGraphsCollection.add(clusterGraph);
		}
		return clustersAsGraphsCollection;
		
	}
	
	private UndirectedSparseGraph<V, E> clusterAsGraph(UndirectedSparseGraph<V, E> graph, Set<V> cluster) {
		UndirectedSparseGraph<V, E> clusterGraph = new UndirectedSparseGraph<>();
		
		//ITERATE THROUGH VERTICES OF CLUSTER
		Iterator<V> singleClusterIterator = cluster.iterator();
		while (singleClusterIterator.hasNext()) {
			//FOR EACH VERTICE, FIND INCIDENT EDGES 
			V vertice = singleClusterIterator.next();
			Collection<E> incidentEdges = graph.getIncidentEdges(vertice);
			Iterator<E> incidentEdgesIterator = incidentEdges.iterator();
			//FOR EACH INCIDENT EDGE, FIND VERTICES WHICH IT CONNECTS AND PUT IT IN THE NEW GRAPH
			while (incidentEdgesIterator.hasNext()) {
				E edge = incidentEdgesIterator.next();
				Collection<V> incidentVertices = graph.getIncidentVertices(edge);
				
				clusterGraph.addEdge(edge, incidentVertices);
			}
			
		}
		return clusterGraph;
	}


}
