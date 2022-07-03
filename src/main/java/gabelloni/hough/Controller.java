package gabelloni.hough;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nu.pattern.OpenCV;

public class Controller {

	@FXML
	private GridPane root;
	@FXML
	private ImageView imgFinal;
	@FXML
	private ImageView imgOriginal;
	@FXML
	private Button botonCargar;
	@FXML
	private TextField cantLineas;
	@FXML
	private Button botonLinea;
	@FXML
	private Button botonCirculo;
	@FXML
	private ImageView imgCanny;
	private BufferedImage imagen;
	private BufferedImage imagenOriginal;
	private static Integer RADIO = 34;

	@FXML
	void initialize() {

	}

	@FXML
	protected void botonCargarImagen(ActionEvent event) throws IOException {
		Stage stage = (Stage) root.getScene().getWindow();

		FileChooser chooser = new FileChooser();
		chooser.setTitle("seleccionar imagen");

		File defaultDirectory = new File(".");
		chooser.setInitialDirectory(defaultDirectory);

		File archivo = chooser.showOpenDialog(stage);
		imagenOriginal = ImageIO.read(archivo);
		imgOriginal.maxWidth(250);
		imgOriginal.setImage(SwingFXUtils.toFXImage(imagenOriginal, null));

		OpenCV.loadLocally();
		
		System.out.println(archivo.getPath());
		Mat src = Imgcodecs.imread(archivo.getPath());
		Mat gris = new Mat();
		Imgproc.cvtColor(src, gris, Imgproc.COLOR_RGBA2GRAY);

		Mat bordes = new Mat();
		Imgproc.Canny(gris, bordes, 30, 30 * 3, 3, false);
		Mat cannyColor = new Mat();
		Imgproc.cvtColor(bordes, cannyColor, Imgproc.COLOR_GRAY2BGR);

		imagen = (BufferedImage) HighGui.toBufferedImage(cannyColor);
		imgCanny.setImage(SwingFXUtils.toFXImage(imagen, null));

	}

	@FXML
	protected void botonBuscarLineas(ActionEvent event) throws IOException {

		TransformadaHoughLineas transformadaLineas = new TransformadaHoughLineas(imagen);

		Integer cantidad = Integer.parseInt(cantLineas.getText());
		Vector<Linea> lines = transformadaLineas.getLineas(cantidad);

		dibujar(lines);

		Image imagenFinal = SwingFXUtils.toFXImage(imagenOriginal, null);
		imgFinal.setImage(imagenFinal);

	}

	@FXML
	protected void botonBuscarCirculo(ActionEvent event) throws IOException {
		
		TransformadaHoughCirculo transformadaHoughCirculo = new TransformadaHoughCirculo(imagen, RADIO);

		Integer cantidad = Integer.parseInt(cantLineas.getText());
		Vector<Circulo> circulos = transformadaHoughCirculo.getCirculos(cantidad);

		dibujarCirculo(circulos);

		Image imagenFinal = SwingFXUtils.toFXImage(imagenOriginal, null);
		imgFinal.setImage(imagenFinal);
	}

	private void dibujarCirculo(Vector<Circulo> circulos) {

		for (Circulo circulo : circulos) {
			Graphics2D g2 = imagenOriginal.createGraphics();
			g2.setColor(Color.GREEN);
			g2.drawOval(circulo.x0 - RADIO, circulo.y0 - RADIO, RADIO * 2, RADIO * 2);
			g2.dispose();
		}
	}

	private void dibujar(Vector<Linea> lines) {

		for (Linea linea : lines) {
			Graphics2D g2 = imagenOriginal.createGraphics();
			g2.setColor(Color.GREEN);
			BasicStroke stroke = new BasicStroke(4);
			g2.setStroke(stroke);
			g2.drawLine(linea.inicioX, linea.inicioY, linea.finX, linea.finY);
			g2.dispose();
		}
	}

}
