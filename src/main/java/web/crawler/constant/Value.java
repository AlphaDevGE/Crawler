package web.crawler.constant;

public class Value {
	public static final double LINK_ANALYSIS_WEIGHT_50= 0.5;
	public static final double TF_IDF_WEIGHT_50 = 0.5;
	public static final double LINK_ANALYSIS_WEIGHT_30= 0.3;
	public static final double TF_IDF_WEIGHT_70 = 0.7;
	public static final double LINK_ANALYSIS_WEIGHT_70= 0.7;
	public static final double TF_IDF_WEIGHT_30 = 0.3;
	
	public static final double TITLE_RANKING_WEIGHT = 1.1; //10% increment
	public static final double LAMBDA = 0.85;
	//SCALER is a value that used in page ranking to keep the page ranking for all the pages ranging from 0 to 1
	public static final double SCALER = 0.75;
	public static final double SCORE_SCALER = 0.95;
	
}
