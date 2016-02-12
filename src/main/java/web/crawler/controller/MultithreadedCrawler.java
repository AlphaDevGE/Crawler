package web.crawler.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.txt.TXTParser;
import org.apache.tika.sax.BodyContentHandler;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.xml.sax.SAXException;

import com.google.common.io.Files;

import ch.qos.logback.core.pattern.parser.Parser;

import java.util.Date;
import java.util.regex.Pattern;



















import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;




public class MultithreadedCrawler extends WebCrawler{
	
		  

	




	
	public String emailSha(String url) {
		try {
			MessageDigest md=MessageDigest.getInstance("SHA-256");
			md.update(url.getBytes());
			byte[] arr=md.digest();
			StringBuffer hexString=new StringBuffer();
			for (Byte byt:arr) {
	    	   hexString.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1)).toString();
	    	}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
				return null;
		}
		}
	
	@Override
     public void visit(Page page) {
		
	    String  URL= page.getWebURL().getURL();
    	String hashValue =emailSha(URL);
    	System.out.println("Hashed Value is: "+hashValue);
    	String path=page.getWebURL().getPath();
    	int docId=page.getWebURL().getDocid();
    	int statusCode=page.getStatusCode();
    	String language=page.getLanguage();
    	String charset=page.getContentCharset();
    	String contentType=page.getContentType();
    	String contentEncoding=page.getContentEncoding();
    	Header[] headers=page.getFetchResponseHeaders();
    	String parentUrl=page.getWebURL().getParentUrl();
    	String filePath=null;
    	
    	
    	
    
    			
    	//	This is for crawling data
    	if (page.getParseData() instanceof HtmlParseData) {
    		HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
    		
    		String text = htmlParseData.getText();
    		String html = htmlParseData.getHtml();
    		String title=htmlParseData.getTitle();
    		Map<String,String> metaTags=htmlParseData.getMetaTags();
    		Set<WebURL> outgoingURLS=htmlParseData.getOutgoingUrls();
    		
    		//Tika extracting metadata.
    		
    		 BodyContentHandler handler = new BodyContentHandler();
    	      Metadata metadata = new Metadata();
    	      FileInputStream inputstream = null;
			try {
				inputstream = new FileInputStream(new File(html));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	      ParseContext pcontext=new ParseContext();
    	      
    	      //Text document parser
    	      TXTParser  TexTParser = new TXTParser();
    	      try {
				TexTParser.parse(inputstream, handler, metadata,pcontext);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SAXException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (TikaException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	      System.out.println("Contents of the document:" + handler.toString());
    	      System.out.println("Metadata of the document:");
    	      String[] metadataNames = metadata.names();
    	      
    	      for(String name : metadataNames) {
    	         System.out.println(name + " : " + metadata.get(name));
    	      }
    		
    		
    		// to Make only one file per Url
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
    		// To Make separate file for each url
    		try{
    			File file = new File("D:/webcrawler/separateFiles/"+hashValue+".txt");
    			filePath=file.getAbsolutePath();
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
            	ob.put("hashValue",hashValue);
            	ob.put("docId", docId);
            	ob.put("location",filePath);
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
            	ob.put("Number of outgoing urls", outgoingURLS.size());
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
    
    	


