import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections15.Transformer;
import org.omg.CORBA.PUBLIC_MEMBER;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class MainGraph {
	public static final DecimalFormat decimalFormat = new DecimalFormat("####0.0");
	
	public static void main(String[] args) throws Exception {
		long startTime = System.nanoTime();
		String fileName = "reddit relations.txt";
		
		
		UndirectedSparseGraph<Vertice, Edge> graph = new UndirectedSparseGraph<>();
		UndirectedSparseGraph<Vertice, Edge> backupGraph = new UndirectedSparseGraph<>();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("FINISHED LOGS/" + fileName)));
		
		// GENERATING RANDOM NETWORK
		RandomNetworkGenerator randomNetworkGenerator = new RandomNetworkGenerator(1000);
		//////////////////////////////////////////////////////////////////////////////////

		
		// RELATION TRANSFORMER FOR EDGES
		Transformer<Edge, Boolean> edgeRelationTransformer = new Transformer<Edge, Boolean>() {
			public Boolean transform(Edge edge) {
				return edge.getPositiveRelation();
			}
		};
		//////////////////////////////////////////////////////////////////////////////////
		
		
		// READING THE NETWORK FROM FILE
		readRedditNetwork("NETWORK EXAMPLES/" + fileName, graph, backupGraph);
		//////////////////////////////////////////////////////////////////////////////////

		//System.out.println(graph);
		
		// GENERATE SET OF CLUSTERS
		ClusterFinder<Vertice, Edge> clusterFinder = new ClusterFinder<>();
		Set<Set<Vertice>> clusters = clusterFinder.findClusters(graph, edgeRelationTransformer);
		//////////////////////////////////////////////////////////////////////////////////
		
		
		writer.write("Number of NODES in network = " + backupGraph.getVertexCount() + System.lineSeparator());
		writer.write("Number of EDGES in network = " + backupGraph.getEdgeCount() + System.lineSeparator());
		writer.write(System.lineSeparator());
		
		// CHECK IF THE NETWORK IS CLUSTERABLE
		boolean isClusterable = clusterFinder.isClusterable(backupGraph, graph, clusters, edgeRelationTransformer);
		String isClusterableString = isClusterable ? "IS" : "IS NOT";
		writer.write("The network " + isClusterableString + " clusterable" + System.lineSeparator());
		writer.write("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		writer.write(System.lineSeparator());
		System.out.println(isClusterable);
		//////////////////////////////////////////////////////////////////////////////////
		
		
		// IF THE NETWORK IS NOT CLUSTERABLE, FIND THE EDGES THAT NEED TO BE DELETED IN ORDER TO BECOME CLUSTERABLE
		if(!isClusterable) {
			Set<Edge> getEdgesToDeleteIfNotClusterable = clusterFinder.getEdgesToDeleteIfNotClusterable();
			
			writer.write(getEdgesToDeleteIfNotClusterable.size() + " EDGES NEED TO BE DELETED");
			writer.write(System.lineSeparator());
			
			double percentOfEdgesToDelete = ((double) getEdgesToDeleteIfNotClusterable.size() / (double) backupGraph.getEdgeCount()) * 100;
			writer.write(decimalFormat.format(percentOfEdgesToDelete) + "% OF ALL EDGES NEED TO BE DELETED");
			writer.write(System.lineSeparator());
			
			Iterator<Edge> getEdgesToDeleteIfNotClusterableIterator = getEdgesToDeleteIfNotClusterable.iterator();
			writer.write("IN ORDER FOR THE NETWORK TO BECOME CLUSTERABLE, THESE EDGES NEED TO BE DELETED = " + System.lineSeparator());
			
			writer.write(System.lineSeparator());
			
			while (getEdgesToDeleteIfNotClusterableIterator.hasNext()) {
				Edge edgeToDelete = getEdgesToDeleteIfNotClusterableIterator.next();
				writer.write(edgeToDelete.toStringWithVertices() + System.lineSeparator());
			}
			writer.write("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			writer.write(System.lineSeparator());
		}
		//////////////////////////////////////////////////////////////////////////////////
		
		
		// GENERATE GRAPHS FROM CLUSTERS
		Collection<UndirectedSparseGraph<Vertice, Edge>> clustersAsGraphsCollection = clusterFinder.clustersAsGraphs(graph, clusters);
		int i = 1;
		
		for (UndirectedSparseGraph<Vertice, Edge> cluster : clustersAsGraphsCollection) {
			if(cluster.getVertexCount() < 2) {
				continue;
			}
			writer.write("CLUSTER " + i + "(" + cluster.getVertexCount() + " vertices) = " + cluster.toString() + System.lineSeparator());
			writer.write(System.lineSeparator());
			i++;
		}
		writer.write("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		writer.write(System.lineSeparator());
		//////////////////////////////////////////////////////////////////////////////////

		
		// CHECK HOW MANY CLUSTERS ARE NOT COALITIONS
		int numberOfNOTCoalitionClusters = 0;
		Collection<Set<Vertice>> NOTCoalitionClusters = clusterFinder.getNOTCoalitionClusterCollection();
		writer.write("Clusters that ARE NOT coalitions clusters = " + System.lineSeparator());
		writer.write(System.lineSeparator());
		
		for (Set<Vertice> cluster : NOTCoalitionClusters) {
			if(cluster.size() < 2) {
				continue;
			}
			writer.write(cluster.toString() + " - (" + cluster.size() + " vertices)");
			writer.write(System.lineSeparator());
			numberOfNOTCoalitionClusters++;
			
			System.out.println("NOT COALITION = " + cluster);
		}
		writer.write("NUMBER OF CLUSTERS THAT ARE NOT COALITIONS = " + numberOfNOTCoalitionClusters);
		writer.write("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		writer.write(System.lineSeparator());
		//////////////////////////////////////////////////////////////////////////////////

		
		// CHECK HOW MANY CLUSTERS ARE COALITIONS
		clusters.removeAll(NOTCoalitionClusters);
		int numberOfCoalitionClusters = 0;
		writer.write("Clusters that ARE coalitions clusters = " + System.lineSeparator());
		writer.write(System.lineSeparator());
		for (Set<Vertice> cluster : clusters) {
			if(cluster.size() < 2) {
				continue;
			}
			writer.write(cluster.toString() + " - (" + cluster.size() + " vertices)");
			writer.write(System.lineSeparator());
			numberOfCoalitionClusters++;
			
			System.out.println("COALITION = " + cluster);
		}
		writer.write("NUMBER OF CLUSTERS THAT ARE COALITIONS = " + numberOfCoalitionClusters);
		writer.write(System.lineSeparator());
		writer.write(System.lineSeparator());
		//////////////////////////////////////////////////////////////////////////////////

		
		// CALCULATE THE TIME THAT PROGRAM RAN
		long endTime = System.nanoTime();
		double seconds = (double) (endTime - startTime) / 1000000000.0;
		
		writer.write("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		writer.write(System.lineSeparator());
		writer.write("PROGRAM TOOK = " + decimalFormat.format(seconds) + " SECONDS");
		//////////////////////////////////////////////////////////////////////////////////

		writer.write(System.lineSeparator());
		writer.write(System.lineSeparator());
		
		Iterator<Vertice> verticeIterator = graph.getVertices().iterator();
		ArrayList<Integer> verticeDegreeCollection = new ArrayList<>();
		
		while (verticeIterator.hasNext()) {
			Vertice vertice = verticeIterator.next();
			verticeDegreeCollection.add(graph.degree(vertice));
		}
		
		Collections.sort(verticeDegreeCollection);
		
		Iterator<Integer> verticeDegreeCollectionIterator = verticeDegreeCollection.iterator();
		
		while (verticeDegreeCollectionIterator.hasNext()) {
			Integer degree = verticeDegreeCollectionIterator.next();
			writer.write(degree + System.lineSeparator());
		}
		
		
		
		
		
		writer.close();
		
	}
	
	public static void readBitcoinTrustNetwork(String fileName, UndirectedSparseGraph<Vertice, Edge> graph, UndirectedSparseGraph<Vertice, Edge> backupGraph) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		double i = 0;
		int numberOfLines = 35592;
		
		String line = reader.readLine();
		while (line != null) {
			if (line.equals("-")) {
				line = reader.readLine();
				i++;
			}
			String[] splitLine = line.split("	");
			boolean isPositive = Integer.parseInt(splitLine[2]) >= 0 ? true : false;
			Vertice sourceVertice = new Vertice(splitLine[0]);
			Vertice targetVertice = new Vertice(splitLine[1]);
			
			graph.addEdge(new Edge(isPositive, sourceVertice, targetVertice), sourceVertice, targetVertice);
			backupGraph.addEdge(new Edge(isPositive, sourceVertice, targetVertice), sourceVertice, targetVertice);

			line = reader.readLine();
			i++;
			System.out.println("READING... " + decimalFormat.format((i / numberOfLines) * 100) + "%");
		}
		
		
		reader.close();
	}
	
	public static void readRandomNetwork(String fileName, UndirectedSparseGraph<Vertice, Edge> graph, UndirectedSparseGraph<Vertice, Edge> backupGraph) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		double i = 0;
		int numberOfLines = 286561;
		
		String line = reader.readLine();
		while (line != null) {
			if (line.equals("-")) {
				line = reader.readLine();
				i++;
			}
			String[] splitLine = line.split(" ");
			boolean isPositive = splitLine[2].equals("+") ? true : false;
			Vertice sourceVertice = new Vertice(splitLine[0]);
			Vertice targetVertice = new Vertice(splitLine[1]);
			
			graph.addEdge(new Edge(isPositive, sourceVertice, targetVertice), sourceVertice, targetVertice);
			backupGraph.addEdge(new Edge(isPositive, sourceVertice, targetVertice), sourceVertice, targetVertice);

			line = reader.readLine();
			i++;
			System.out.println("READING... " + decimalFormat.format((i / numberOfLines) * 100) + "%");
		}
		
		
		reader.close();
	}
	
	public static void readNetwork(String fileName, UndirectedSparseGraph<Vertice, Edge> graph, UndirectedSparseGraph<Vertice, Edge> backupGraph) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		int i = 0;
		
		String line = reader.readLine();
		while (line != null) {
			String[] splitLine = line.split("	");
			if(splitLine.length != 2) {
				line = reader.readLine();
				continue;
			}
			Vertice sourceVertice = new Vertice(splitLine[0]);
			Vertice targetVertice = new Vertice(splitLine[1]);
			
			graph.addEdge(new Edge(true, sourceVertice, targetVertice), sourceVertice, targetVertice);
			backupGraph.addEdge(new Edge(true, sourceVertice, targetVertice), sourceVertice, targetVertice);
			
			line = reader.readLine();
			System.out.println(i);
			i++;
		}
		
		reader.close();
		
		
	}
	
	public static void readWikiRequstForAdminshipNetwork(String fileName, UndirectedSparseGraph<Vertice, Edge> graph, UndirectedSparseGraph<Vertice, Edge> backupGraph) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		double i = 0;
		int numberOfLines = 1586199;
		
		String line = "";
		while(line != null) {
			line = reader.readLine();
			i++;
			String[] splitLine1 = line.split(":");
			if(splitLine1.length != 2) {
				while(!line.equals("")) {
					line = reader.readLine();
					i++;
				}
				continue;
			}
			Vertice sourceVertice = new Vertice(splitLine1[1]);
			
			line = reader.readLine();
			i++;
			String[] splitLine2 = line.split(":");
			Vertice targetVertice = new Vertice(splitLine2[1]);
			
			line = reader.readLine();
			i++;
			String[] splitLine3 = line.split(":");
			boolean isPositive = splitLine3[1].equals("1") ? true : false;
			
			graph.addEdge(new Edge(isPositive, sourceVertice, targetVertice), sourceVertice, targetVertice);
			backupGraph.addEdge(new Edge(isPositive, sourceVertice, targetVertice), sourceVertice, targetVertice);
			
			while(!line.equals("")) {
				line = reader.readLine();
				if(line == null) {
					break;
				}
				i++;
			}
			
			System.out.println("READING... " + decimalFormat.format((i / numberOfLines) * 100) + "%");
			
		}
		
		reader.close();
		
		
	}

	public static void readRedditNetwork(String fileName, UndirectedSparseGraph<Vertice, Edge> graph, UndirectedSparseGraph<Vertice, Edge> backupGraph) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		double i = 0;
		int numberOfLines = 286561;
		
		String line = reader.readLine();
		while (line != null) {
			if (line.equals("-")) {
				line = reader.readLine();
				i++;
			}
			String[] splitLine = line.split("	");
			boolean isPositive = splitLine[2].equals("1") ? true : false;
			Vertice sourceVertice = new Vertice(splitLine[0]);
			Vertice targetVertice = new Vertice(splitLine[1]);
			
			graph.addEdge(new Edge(isPositive, sourceVertice, targetVertice), sourceVertice, targetVertice);
			backupGraph.addEdge(new Edge(isPositive, sourceVertice, targetVertice), sourceVertice, targetVertice);

			line = reader.readLine();
			i++;
			System.out.println("READING... " + decimalFormat.format((i / numberOfLines) * 100) + "%");
		}
		
		
		reader.close();
	}
	
}
