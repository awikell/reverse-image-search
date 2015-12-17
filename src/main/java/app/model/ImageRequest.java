package app.model;

public class ImageRequest {

	private String imageData;
	private String searchType;

	public ImageRequest() {
	}

	public ImageRequest(String imageData, String searchType) {
		this.imageData = imageData;
		this.searchType = searchType;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getImageData() {
		return imageData;
	}

	public void setImageData(String imageData) {
		this.imageData = imageData;
	}
}
