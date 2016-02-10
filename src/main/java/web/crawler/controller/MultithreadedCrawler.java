package web.crawler.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MultithreadedCrawler extends WebCrawler{
	
	
	@Override
     public void visit(Page page) {
    	String  URL= page.getWebURL().getURL();
    	String path=page.getWebURL().getPath();
    	int docId=page.getWebURL().getDocid();
    	int statusCode=page.getStatusCode();
    	String language=page.getLanguage();
    	String charset=page.getContentCharset();
    	String contentType=page.getContentType();
    	String contentEncoding=page.getContentEncoding();
    	Header[] headers=page.getFetchResponseHeaders();
    	String parentUrl=page.getWebURL().getParentUrl();
    			
    	//	This is for crawling data
    	if (page.getParseData() instanceof HtmlParseData) {
    		HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
    		String text = htmlParseData.getText();
    		String html = htmlParseData.getHtml();
    		String title=htmlParseData.getTitle();
    		Map<String,String> metaTags=htmlParseData.getMetaTags();
    		Set<WebURL> outgoingURLS=htmlParseData.getOutgoingUrls();
    		
    		
    		try{
    			File file = new File("D:/webcrawler/spider.txt");
    			FileWriter fileWriter = new FileWriter(file,true);
    			fileWriter.write("URL : "+URL+"\r\n"+"\r\n");
    			fileWriter.write("Headers"+"\r\n");
    			for(Header header:headers){
    				fileWriter.write(header.getName()+": "+header.getValue()+"\r\n");
    			}
    			fileWriter.write("\r\n"+"HTML "+"\r\n"+html);
    			fileWriter.flush();
    			fileWriter.close();
    		}catch (IOException e) {
    			e.printStackTrace();
    		}
    	
    		
    		if(Controller.shouldExtract){
    		// Write this to database
    			JSONObject ob=new JSONObject();
            	ob.put("URL", URL);
            	ob.put("docId", docId);
            	ob.put("location","D:/webcrawler/spider.txt");
            	ob.put("Parent Url",parentUrl);
            	ob.put("path", path);
            	ob.put("contentType", contentType);
            	ob.put("Title", title);
            	JSONArray headerInfo=new JSONArray();
            	for(Header header:headers){
            		JSONObject head=new JSONObject();
            		String headName=header.getName();
            		String headValue=header.getValue();
            		head.put(headName,headValue);
            		headerInfo.put(head);
            	}
            	ob.put("headers",headerInfo);
            	ob.put("Metadata", metaTags.entrySet());
            	ob.put("content", text);
            	ob.put("outgoing Urls", outgoingURLS);
            	File file = new File("D:/webcrawler/Metadata.json");
            	try{
    			FileWriter fileWriter = new FileWriter(file,true);
    			fileWriter.write(ob.toJSONString());
    			fileWriter.flush();
    			fileWriter.close();
    			
            	}catch(Exception ex){
            		System.out.println(ex);
            	}
    		}
    		
    	}
	}
}
    
    	
    

