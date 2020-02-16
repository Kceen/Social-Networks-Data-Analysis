import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class RandomNetworkGenerator {
	int numberOfVertices;
	int numberOfNamesForVertices = 1400;
	
	public RandomNetworkGenerator(int numberOfVertices) throws Exception {
		if(this.numberOfNamesForVertices >= numberOfVertices) {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("NETWORK EXAMPLES/RANDOM mreza.txt")));
			BufferedWriter writerForNodes = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("ZA SPREADSHEET/nodes.txt")));
			BufferedWriter writerForEdges1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("ZA SPREADSHEET/edge1.txt")));
			BufferedWriter writerForEdges2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("ZA SPREADSHEET/edge2.txt")));
			BufferedWriter writerForRelation = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("ZA SPREADSHEET/relation.txt")));
			
			
			Random random = new Random();
			this.numberOfVertices = numberOfVertices;
			Set<Integer> nodesSet = new HashSet<>();
			
			while(nodesSet.size() < numberOfVertices) {
				int number1 = random.nextInt(numberOfNamesForVertices);
				int number2 = random.nextInt(numberOfNamesForVertices);
				
				if(number1 == number2) {
					while(number1 == number2) {
						number2 = random.nextInt(numberOfNamesForVertices);
					}
				}
				
				nodesSet.add(number1);
				nodesSet.add(number2);
				
				int relationInt = random.nextInt(10);
				boolean relationBoolean = relationInt < 8 ? true : false;
				String relationString = relationBoolean == true ? "+" : "-";
				
				writer.write(number1 + " " + number2 + " " + relationString + System.lineSeparator());
				writerForEdges1.write(number1 + System.lineSeparator());
				writerForEdges2.write(number2 + System.lineSeparator());
				
				if(relationBoolean == true) {
					writerForRelation.write("1" + System.lineSeparator());
				}
				else {
					writerForRelation.write("0.1" + System.lineSeparator());
				}
				
				
				 
			}
			
			Iterator<Integer> nodesIterator = nodesSet.iterator();
			
			while (nodesIterator.hasNext()) {
				Integer node = nodesIterator.next();
				writerForNodes.write(node + System.lineSeparator());
				
				
			}
			
			
			writer.close();
			writerForEdges1.close();
			writerForEdges2.close();
			writerForRelation.close();
			writerForNodes.close();
		}
		else {
			System.out.println("Random Network Generation NOT Possible");
			System.out.println("Number of names for Vertices needs to be increased");
		}
	}
		

}
