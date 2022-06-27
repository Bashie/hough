package gabelloni.hough;

public class Linea implements Comparable<Linea> {
	protected double theta;
	protected double rho;
	protected int peso;
	public Integer inicioX;
    public Integer inicioY;
    public Integer finX;
    public Integer finY;
    
	public Linea(Double theta, Integer rho, Integer ancho, Integer alto, Integer peso) {
		this.theta = theta;
		this.rho = rho;
		this.peso = peso;

		double senoTetha = Math.sin(theta);
		double cosenoTetha = Math.cos(theta);

		if (theta < Math.PI * 0.25 || theta > Math.PI * 0.75) {
			int x1 = 0, y1 = 0;
			int x2 = 0, y2 = alto - 1;

			x1 = (int) ((((rho) - ((y1) * senoTetha)) / cosenoTetha));
			x2 = (int) ((((rho) - ((y2) * senoTetha)) / cosenoTetha));

			inicioX = x1;
			inicioY = y1;
			finX = x2;
			finY = y2;
		} else {
			int x1 = 0, y1 = 0;
			int x2 = ancho - 1, y2 = 0;

			y1 = (int) ((((rho) - ((x1) * cosenoTetha)) / senoTetha));
			y2 = (int) ((((rho) - ((x2) * cosenoTetha)) / senoTetha));

			inicioX = x1;
			inicioY = y1;
			finX = x2;
			finY = y2;
		}
	}

	public int compareTo(Linea o) {
		return (this.peso - o.peso);
	}

	@Override
	public String toString() {
		return "Linea [theta=" + theta + ", r=" + rho + ", score=" + peso + ", inicioX=" + inicioX + ", inicioY=" + inicioY + ", finX=" + finX + ", finY=" + finY + "]";
	}

}
