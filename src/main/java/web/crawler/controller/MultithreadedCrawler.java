package web.crawler.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import web.crawler.constant.Paths;
import web.crawler.db.dao.DocDao;
import web.crawler.db.model.Doc;

public class MultithreadedCrawler extends WebCrawler {

	private DocDao docDao = new DocDao();
	private boolean flag = true;
	public static List<Doc> crawledDocs = new ArrayList<Doc>();

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
		
		if (docDao.getDocByUrl(url.getURL()) == null) {
			return true;
		} else {
			System.out.println("Doc " + url.getURL()
					+ " has been visited before! check in DB");
			flag = false;
			return true;
		}
	}

	@Override
	public void visit(Page page) {

		String url = page.getWebURL().getURL();
		String urlHash = emailSha(url);
		String path = page.getWebURL().getPath();
		int docId = page.getWebURL().getDocid();
		String parentUrl = page.getWebURL().getParentUrl();
		String filePath = null;

		File file = null;

		if (page.getParseData() instanceof BinaryParseData) {
			System.out
					.println("IMMMMMMMMMMMMMMMMMMMMMMMMMAAAAAAAAAAAAAAAGEEEEEEEEEEEE");

			// String url = page.getWebURL().getURL();
			String extension = url.substring(url.lastIndexOf('.'));
			System.out.println("Parsed data is"
					+ page.getContentData().toString()); // store image
			try {
				file = new File(Paths.PATH_TO_STORE_CRAWLED_DATA + urlHash);
				filePath = file.getAbsolutePath();
				FileOutputStream fileWriter = new FileOutputStream(file, true);
				fileWriter.write(page.getContentData());
				fileWriter.flush();
				fileWriter.close();
				Doc doc = new Doc();
				doc.setUrl(url);
				doc.setVisitedDate(new Date());
				doc.setHash(urlHash);
				doc.setPath(path);
				doc.setLocation(filePath);
				doc.setParentStr(parentUrl);
				doc.setTypeOfFile(extension);
				crawledDocs.add(doc);
				docDao.saveDocsList(crawledDocs);
				crawledDocs.clear();

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

			// To Make separate file for each url
			try {
				file = new File(Paths.PATH_TO_STORE_CRAWLED_DATA + urlHash);
				filePath = file.getAbsolutePath();
				FileWriter fileWriter = new FileWriter(file, true);
				// fileWriter.write("URL : " + URL + "\r\n" + "\r\n");
				fileWriter.write(html);
				fileWriter.flush();
				fileWriter.close();

				// pushing data into the db which we can't get during Extraction
				// url,visitedDate,hash,Location,parentstr,type
				Doc doc = new Doc();
				doc.setUrl(url);
				doc.setVisitedDate(new Date());
				doc.setHash(urlHash);
				doc.setPath(path);
				doc.setLocation(filePath);
				doc.setParentStr(parentUrl);
				doc.setTypeOfFile("html");
				crawledDocs.add(doc);
				if (outgoingURLS.size() == 0 || crawledDocs.size() == 500) {
					docDao.saveDocsList(crawledDocs);
					crawledDocs.clear();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			// Because we are dealing with we need to do this way
			if (Controller.shouldExtract && flag) {
				// call the extractor class here
			}

		}
	}

}
