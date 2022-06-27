package gabelloni.hough;

public class Circulo implements Comparable<Circulo> {
	protected Integer x0;
	protected Integer y0;
	protected int peso;
    
	public Circulo(Integer x0, Integer y0, Integer peso) {
		this.x0 = x0;
		this.y0 = y0;
		this.peso = peso;
	}

	public int compareTo(Circulo o) {
		return (this.peso - o.peso);
	}

	@Override
	public String toString() {
		return "Circulo [x0=" + x0 + ", y0=" + y0 + ", peso=" + peso + "]";
	}


}
