package gabelloni.hough;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Vector;

public class TransformadaHoughLineas {
	final int thetaMaxima = 180;
	final double radianes = Math.PI / thetaMaxima;
	protected Integer ancho, alto;
	protected int[][] matrizAcumuladora;
	protected int rhoMaximo;
	protected int cantidadDePuntos;
	protected BufferedImage imagen;

	public TransformadaHoughLineas(BufferedImage imagen) {
		this.imagen = imagen;
		this.ancho = imagen.getWidth();
		this.alto = imagen.getHeight();
		cantidadDePuntos = 0;
	}

	public void agregarPuntosLinea(BufferedImage imagen) {
		for (int x = 0; x < imagen.getWidth(); x++) {
			for (int y = 0; y < imagen.getHeight(); y++) {
				if ((imagen.getRGB(x, y) & 0x000000ff) != 0) {
					agregarPuntoLinea(x, y);
				}
			}
		}
	}

	public void agregarPuntoLinea(Integer x, Integer y) {
		for (Integer theta = 0; theta < thetaMaxima; theta++) {
			Integer rho = (int) (((x) * Math.cos(theta * radianes)) + ((y) * Math.sin(theta * radianes)));
			if (rho < 0 || rho >= rhoMaximo) { //si se pasa de la matriz, no lo agrego.
				continue;
			}
			matrizAcumuladora[theta][rho]++;
		}
		cantidadDePuntos++;
	}

	public Vector<Linea> getLineas(Integer cantidad) {
		rhoMaximo = (int) Math.ceil(Math.sqrt(alto * alto + ancho * ancho));
		matrizAcumuladora = new int[thetaMaxima][rhoMaximo];

		agregarPuntosLinea(imagen);
		
		Vector<Linea> lineas = new Vector<Linea>(200);

		if (cantidadDePuntos == 0) {
			return lineas;
		}
		for (int t = 0; t < thetaMaxima; t++) {
			for (int r = 0; r < rhoMaximo; r++) {
				if (matrizAcumuladora[t][r] > 0) {
					double theta = t * radianes;
					lineas.add(new Linea(theta, r, ancho, alto, matrizAcumuladora[t][r]));
				}
			}
		}
		Collections.sort(lineas, Collections.reverseOrder());
		lineas.setSize(cantidad);
		return lineas;
	}
	
}
