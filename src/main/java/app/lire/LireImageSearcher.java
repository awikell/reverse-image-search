package app.lire;

import app.model.ImageResult;
import com.sun.jersey.core.util.Base64;
import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.ImageSearchHits;
import net.semanticmetadata.lire.ImageSearcher;
import net.semanticmetadata.lire.imageanalysis.*;
import net.semanticmetadata.lire.impl.GenericFastImageSearcher;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class LireImageSearcher {

	private static final int NR_OF_RESULTS = 9;

	private String indexLocation;

	public LireImageSearcher(String indexLocation) {
		this.indexLocation = indexLocation;
	}

	public List<ImageResult> searchForSimilarImageByData(String imageData, String searchType) throws IOException {
		BufferedImage img = createImageFromImageData(imageData);
		return findSimilarImages(img, searchType);
	}

	private List<ImageResult> findSimilarImages(BufferedImage img, String searchType) throws IOException {
		IndexReader ir = DirectoryReader.open(FSDirectory.open(new File(indexLocation)));
		ImageSearcher searcher = getImageSearcherForType(searchType);

		// searching with a image file ...
		ImageSearchHits hits = searcher.search(img, ir);
		List<ImageResult> resultList = new ArrayList<ImageResult>();
		for (int i = 0; i < hits.length(); i++) {
			String fileName = hits.doc(i).getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
			resultList.add(new ImageResult(fileName, hits.score(i)));
			System.out.println(hits.score(i) + ": \t" + fileName);
		}
		return resultList;
	}

	private ImageSearcher getImageSearcherForType(String searchType) {
		switch (searchType) {
			case "AutoColorCorrelogram": return new GenericFastImageSearcher(NR_OF_RESULTS, AutoColorCorrelogram.class);
			case "CEDD": return new GenericFastImageSearcher(NR_OF_RESULTS, CEDD.class);
			case "FCTH": return new GenericFastImageSearcher(NR_OF_RESULTS, FCTH.class);
			case "ColorLayout": return new GenericFastImageSearcher(NR_OF_RESULTS, ColorLayout.class);
			case "JCD": return new GenericFastImageSearcher(NR_OF_RESULTS, JCD.class);
			default: return new GenericFastImageSearcher(NR_OF_RESULTS, AutoColorCorrelogram.class);
		}
	}

	private BufferedImage createImageFromImageData(String imageData) throws IOException {
		String imageDataBytes = imageData.substring(imageData.indexOf(",")+1);
		byte[] image = Base64.decode(imageDataBytes);
		ByteArrayInputStream bais = new ByteArrayInputStream(image);
		BufferedImage img = ImageIO.read(bais);
		bais.close();
		return img;
	}
}
