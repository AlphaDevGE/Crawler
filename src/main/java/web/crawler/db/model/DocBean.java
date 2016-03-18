package web.crawler.db.model;

public class DocBean {
	
	private String path;
	private Double pageRanking;
	
	public DocBean(){super();}
	
	public DocBean(String path, Double pageRanking) {
		super();
		this.path = path;
		this.pageRanking = pageRanking;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public Double getPageRanking() {
		return pageRanking;
	}
	
	public void setPageRanking(Double pageRanking) {
		this.pageRanking = pageRanking;
	}

}
