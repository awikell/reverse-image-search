package app;

import app.lire.LireImageIndexer;
import app.lire.LireImageSearcher;
import app.model.ImageRequest;
import app.model.ImageResult;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@EnableAutoConfiguration
public class AppController {

	private String indexLocation = "index_2"; //Store index in a file system folder "/index"
	LireImageSearcher imageSearcher = new LireImageSearcher(indexLocation);
	LireImageIndexer imageIndexer = new LireImageIndexer(indexLocation);


	@RequestMapping("/")
	public String home() {
		return "index";
	}

	@RequestMapping(value = "/searchByImageData", method = RequestMethod.POST)
	public @ResponseBody List<ImageResult> getImagesByOtherImage(@RequestBody ImageRequest imageRequest) throws IOException {
		return imageSearcher.searchForSimilarImageByData(imageRequest.getImageData(), imageRequest.getSearchType());

	}

	@RequestMapping("/index")
	public void home(@RequestParam("imgFolder") String imgFolder) throws IOException {
		imageIndexer.indexImagesInFolder(imgFolder);
	}
}