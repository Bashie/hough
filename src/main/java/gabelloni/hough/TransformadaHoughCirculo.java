package gabelloni.hough;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Vector;

public class TransformadaHoughCirculo {
	final int thetaMaxima = 180;
	final double radianes = Math.PI / thetaMaxima;
	protected Integer ancho, alto;
	protected int[][] matrizAcumuladora;
	protected int rhoMaximo;
	protected int cantidadDePuntos;
	protected BufferedImage imagen;
	private Integer radio;

	public TransformadaHoughCirculo(BufferedImage imagen, Integer radio) {
		this.imagen = imagen;
		this.ancho = imagen.getWidth();
		this.alto = imagen.getHeight();
		this.radio = radio;
		cantidadDePuntos = 1;
	}

	public Vector<Circulo> getCirculos(Integer cantidad) {
		rhoMaximo = (int) Math.ceil(Math.sqrt(alto * alto + ancho * ancho));
		matrizAcumuladora = new int[ancho][alto];

		agregarPuntosCirculo(imagen);

		Vector<Circulo> circulos = new Vector<Circulo>(200);

		if (cantidadDePuntos == 0) {
			return circulos;
		}
		
		for (int x = 0; x < ancho; x++) {
			for (int y = 0; y < alto; y++) {
				if (matrizAcumuladora[x][y] > 0) {
					circulos.add(new Circulo(x, y, matrizAcumuladora[x][y]));
				}
			}
		}
		
		Collections.sort(circulos, Collections.reverseOrder());
		circulos.setSize(cantidad);
		for (Circulo circulo : circulos) {
			System.out.println(circulo.x0 + " - " + circulo.y0 + " - " + radio);
		}
		return circulos;
	}

	public void agregarPuntosCirculo(BufferedImage imagen) {
		Integer minx = ancho;
		Integer maxx = 0;
		for (int x = 0; x < imagen.getWidth(); x++) {
			for (int y = 0; y < imagen.getHeight(); y++) {
				if ((imagen.getRGB(x, y) & 0x000000ff) != 0) {
					agregarPuntoCirculo(x, y);
					if (minx > x) {
						minx = x;
					}
					if (maxx < x) {
						maxx = x;
					}
				}
			}
		}
		System.out.println("minx: " + minx + " maxx: " + maxx);
		
	}

	public void agregarPuntoCirculo(Integer x0, Integer y0) {
		for (Integer theta = 0; theta < thetaMaxima; theta++) {
			
			Integer x = (int)Math.round(x0 + radio * Math.cos(theta * radianes));
			Integer y = (int)Math.round(y0 + radio * Math.sin(theta * radianes));
			if(!(x < ancho && x >= 0 && y < alto && y >= 0)) {//si se pasa de la matriz, no lo agrego.
				continue;
			}
			matrizAcumuladora[x][y]++;
		}
		cantidadDePuntos++;
	}
}
