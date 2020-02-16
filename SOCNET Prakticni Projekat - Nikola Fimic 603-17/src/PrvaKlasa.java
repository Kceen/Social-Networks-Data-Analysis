import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Set;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.algorithms.shortestpath.UnweightedShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class PrvaKlasa {

	public static void main(String[] args) throws Exception {
		//BufferedReader reader = new BufferedReader(new FileReader("mreza2.txt"));
		//String line = reader.readLine();
		UndirectedGraph<String, String> g = new UndirectedSparseGraph<>();
		DirectedSparseGraph<String, String> g2 = new DirectedSparseGraph<>();
		int i = 0;
		
		/*
		while(line != null) {
			if(line.equals("-")) {
				line = reader.readLine();
			}
			String[] splitLine = line.split(" ");
			g.addEdge(i + "", splitLine[0], splitLine[1]);
			
			i++;
			line = reader.readLine();
		}
		*/
		
		System.out.println(g2.addEdge("1", "1", "2"));
		System.out.println(g2.addEdge("2", "2", "1"));
		
		System.out.println(g.addEdge("1", "1", "2"));
		System.out.println(g.addEdge("2", "2", "1"));
		
		System.out.println(g);
		System.out.println(g2);
		//reader.close();
	
	}

}
