public class Edge {
	
	boolean positiveRelation;
	Vertice sourceVertice;
	Vertice targetVertice;
	
	public Edge(boolean positiveRelation, Vertice sourceVertice, Vertice targetVertice) {
		this.positiveRelation = positiveRelation;
		this.sourceVertice = sourceVertice;
		this.targetVertice = targetVertice;
	}

	public boolean getPositiveRelation() {
		return positiveRelation;
	}
	
	public Vertice getSourceVertice() {
		return sourceVertice;
	}

	public void setSourceVertice(Vertice sourceVertice) {
		this.sourceVertice = sourceVertice;
	}

	public Vertice getTargetVertice() {
		return targetVertice;
	}

	public void setTargetVertice(Vertice targetVertice) {
		this.targetVertice = targetVertice;
	}

	public void setPositiveRelation(boolean positiveRelation) {
		this.positiveRelation = positiveRelation;
	}

	@Override
	public String toString() {
		String relation = positiveRelation == true ? "+" : "-";
		//return relation + "[" + sourceVertice + "," + targetVertice + "]"; 
		return relation;
	}
	
	public String toStringWithVertices() {
		String relation = positiveRelation == true ? "+" : "-";
		return relation + "[" + sourceVertice + "," + targetVertice + "]"; 
	}

}
