package app.lire;

import net.semanticmetadata.lire.imageanalysis.*;
import net.semanticmetadata.lire.impl.ChainedDocumentBuilder;
import net.semanticmetadata.lire.impl.GenericDocumentBuilder;
import net.semanticmetadata.lire.utils.FileUtils;
import net.semanticmetadata.lire.utils.LuceneUtils;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class LireImageIndexer {

	private String indexLocation;

	public LireImageIndexer(String indexLocation) {
		this.indexLocation = indexLocation;
	}

	public void indexImagesInFolder(String folderPath) throws IOException {
		File indexFolder = new File(folderPath);
		if (! (indexFolder.exists() && indexFolder.isDirectory())) {
			throw new IOException("Invalid path: " +folderPath);
		}

		ChainedDocumentBuilder documentBuilder = createDocumentBuilder();
		indexImages(indexFolder, documentBuilder);
	}

	private ChainedDocumentBuilder createDocumentBuilder() {
		ChainedDocumentBuilder documentBuilder = new ChainedDocumentBuilder();
		documentBuilder.addBuilder(new GenericDocumentBuilder(CEDD.class));
		documentBuilder.addBuilder(new GenericDocumentBuilder(FCTH.class));
		documentBuilder.addBuilder(new GenericDocumentBuilder(AutoColorCorrelogram.class));
		documentBuilder.addBuilder(new GenericDocumentBuilder(JCD.class));
		documentBuilder.addBuilder(new GenericDocumentBuilder(ColorLayout.class));
		return documentBuilder;
	}

	private void indexImages(File indexFolder, ChainedDocumentBuilder documentBuilder) throws IOException {
		ArrayList<String> images = FileUtils.getAllImages(indexFolder, true);
		// Creating an Lucene IndexWriter
		IndexWriterConfig conf = new IndexWriterConfig(LuceneUtils.LUCENE_VERSION,
				new WhitespaceAnalyzer());
		IndexWriter iw = new IndexWriter(FSDirectory.open(new File(indexLocation)), conf);
		// Iterating through images building the low level features
		for (Iterator<String> it = images.iterator(); it.hasNext(); ) {
			String imageFilePath = it.next();
			try {
				BufferedImage img = ImageIO.read(new FileInputStream(imageFilePath));
				Document document = documentBuilder.createDocument(img, imageFilePath);
				iw.addDocument(document);
			} catch (Exception e) {
				System.err.println("Error reading image or indexing it.");
				e.printStackTrace();
			}
		}
		iw.close();
	}
}
