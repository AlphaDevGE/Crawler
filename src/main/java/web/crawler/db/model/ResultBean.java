package web.crawler.db.model;

public class ResultBean {
	private String description;
	private String location;
	private double tdIdf;
	private double pageRanking;
	private double score;
	
	public ResultBean(){super();}
	
	public ResultBean(String description, String location, double tdIdf, double pageRanking, double score) {
		super();
		this.description = description;
		this.location = location;
		this.tdIdf = tdIdf;
		this.pageRanking = pageRanking;
		this.score = score;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getTdIdf() {
		return tdIdf;
	}

	public void setTdIdf(double tdIdf) {
		this.tdIdf = tdIdf;
	}

	public double getPageRanking() {
		return pageRanking;
	}

	public void setPageRanking(double pageRanking) {
		this.pageRanking = pageRanking;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
}