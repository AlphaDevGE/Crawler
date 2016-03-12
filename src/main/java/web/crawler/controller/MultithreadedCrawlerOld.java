package web.crawler.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import web.crawler.db.dao.UrlDao;
import web.crawler.db.model.Url;

public class MultithreadedCrawlerOld extends WebCrawler {

	private UrlDao urlDao = new UrlDao();
	boolean flag = true;
	
	public static List<String> types = new ArrayList<String>();
	public static List<String> images = new ArrayList<String>();
	

	public String emailSha(String url) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(url.getBytes());
			byte[] arr = md.digest();
			StringBuffer hexString = new StringBuffer();
			for (Byte byt : arr) {
				hexString
						.append(Integer.toString((byt & 0xff) + 0x100, 16)
								.substring(1)).toString();
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
	
		if(urlDao.getUrlByUrl(url.getURL()) == null)
			return true;
		else
		{
			System.out.println("Url " +url.getURL() + " has been visited before! check in DB" );
			flag = false;
			return true;
		}
	}

	@Override
	public void visit(Page page) {

		String URL = page.getWebURL().getURL();
		String hashValue = emailSha(URL);
		String path = page.getWebURL().getPath();
		int docId = page.getWebURL().getDocid();
		String parentUrl = page.getWebURL().getParentUrl();
		String filePath = null;
		types.add(page.getContentType());
		File file = null;
		if(page.getParseData() instanceof BinaryParseData){
			System.out.println("IMMMMMMMMMMMMMMMMMMMMMMMMMAAAAAAAAAAAAAAAGEEEEEEEEEEEE");
			
			 String url = page.getWebURL().getURL();
			 String extension = url.substring(url.lastIndexOf('.'));
			System.out.println("Parsed data is"+page.getContentData().toString());	    // store image
			    try {
					file = new File("D:/webcrawler/separateFiles/" +hashValue
							+extension);
					filePath = file.getAbsolutePath();
					FileOutputStream fileWriter = new FileOutputStream(file, true);
				
					fileWriter.write(page.getContentData());
					fileWriter.flush();
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

		}
		// This is for crawling data
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String html = htmlParseData.getHtml();
			String title = htmlParseData.getTitle();
			Set<WebURL> outgoingURLS = htmlParseData.getOutgoingUrls();
			
			System.out.println("outgoingURLS size:" + outgoingURLS.size());
			// to Make only one file per Url
		/*	try {
				File file = new File("D:/webcrawler/spider.txt");
				FileWriter fileWriter = new FileWriter(file, true);
				fileWriter.write("URL : " + URL + "\r\n" + "\r\n");
				fileWriter.write("Headers" + "\r\n");
				for (Header header : headers) {
					fileWriter.write(header.getName() + ": "
							+ header.getValue() + "\r\n");
				}
				fileWriter.write("\r\n" + "HTML " + "\r\n" + html);
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			// To Make separate file for each url
			try {
				file = new File("D:/webcrawler/separateFiles/" + hashValue
						+ ".txt");
				filePath = file.getAbsolutePath();
				FileWriter fileWriter = new FileWriter(file, true);
				//fileWriter.write("URL : " + URL + "\r\n" + "\r\n");
				fileWriter.write( html);
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			//IF extract is needed
			if (Controller.shouldExtract && flag) {
				// Write this to database

				// This is to extract from local storage and using tika for
				// extraction
				File directory = new File("D:/webcrawler/separateFiles");
				File[] fList = directory.listFiles();
				
				// clean database coollection

				for (File f : fList) {
					System.out.println("Hello" + f.getName());
					System.out.println("Path :" + f.getAbsolutePath());
					if (f.getName().equals(hashValue + ".txt")) {
						InputStream input = null;
						try {
							input = new FileInputStream(new File(
									f.getAbsolutePath()));
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						BodyContentHandler handler = new BodyContentHandler();
						Metadata metadata = new Metadata();
						AutoDetectParser autoDetectParser = new AutoDetectParser();
						try {
							autoDetectParser.parse(input, handler, metadata);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SAXException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (TikaException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("Contents of the document:"
								+ handler.toString());
						String headerStr = handler.toString();
						System.out.println("Metadata of the document:");
						String[] metadataNames = metadata.names();
						
						
						for (String name : metadataNames) {
							System.out.println(name + " : "
									+ metadata.get(name));
							
							
						}
						
						
						String metadataStr = "";
						for(String meta : metadataNames)
						{
							metadataStr = metadataStr + meta + metadata.get(meta)+ "\n";
						}
						images.add(metadataStr);
						
						Set<String> urlStrSet = new HashSet<String>();
						for(WebURL weburl : outgoingURLS)
						{
							urlStrSet.add(weburl.getURL());
						}
						//save into database
//						String location = "D:/webcrawler/separateFiles/" + hashValue + ".txt";
						String location = file.getAbsolutePath();
						
						Url url = new Url(URL, new Date(), hashValue, location, metadataStr, 
								headerStr, title, urlStrSet, parentUrl, headerStr);
						
						urlDao.saveUrl(url);

						
					}

				}
				
				
				
			}

		}
	}
	

}
