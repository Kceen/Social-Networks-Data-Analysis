public class Vertice<T> {
	
	T info;
	
	public Vertice() {}
	
	public Vertice(T info) {
		this.info = info;
	}

	public T getInfo() {
		return info;
	}

	public void setInfo(T vertice) {
		this.info = vertice;
	}

	@Override
	public String toString() {
		return info + "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((info == null) ? 0 : info.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertice other = (Vertice) obj;
		if (info == null) {
			if (other.info != null)
				return false;
		} else if (!info.equals(other.info))
			return false;
		return true;
	}
	
}
