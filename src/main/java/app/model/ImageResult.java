package app.model;

import com.sun.jersey.core.util.Base64;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageResult {
	private String imageData;
	private double score;

	public ImageResult(String path, double score) throws IOException {
		this.score = score;
		this.imageData = createImageDataFromPath(path);
	}

	public String getImageData() {
		return imageData;
	}

	public double getScore() {
		return score;
	}

	private String createImageDataFromPath(String path) throws IOException {
		File f = new File(path);
		BufferedImage img = ImageIO.read(f);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(img, "PNG", out);
		byte[] bytes = Base64.encode(out.toByteArray());
		return "data:image/png;base64," + new String(bytes);
	}
}
