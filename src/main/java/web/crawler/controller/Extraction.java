package web.crawler.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import web.crawler.db.dao.DocDao;
import web.crawler.db.dao.UrlDao;
import web.crawler.db.model.Doc;
import web.crawler.db.model.Url;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class Extraction {
	

	//private static List<Doc> docList = new ArrayList<Doc>();
	
	static String emailSha(String url) {
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
	
	static List<Doc> docsList=new ArrayList<Doc>();

	static void walk(String path) throws IOException {
		
		File root = new File(path);
		File[] list = root.listFiles();

		if (list == null)
			return;
		int count = 0;
		for (File f : list) {
			System.out.println(count++);

			if (f.isDirectory()) {
				String absPath = f.getAbsolutePath();
				walk(f.getAbsolutePath());
			} else {
				// Parse the file data and put it in the database.
				FileInputStream input = null;
				try {
					input = new FileInputStream(new File(f.getAbsolutePath()));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String location = f.getAbsolutePath();

				Document doc = Jsoup.parse(f, "UTF-8");
				Elements links = doc.select("a");

				// System.out.println("for file "+f.getAbsolutePath()+"/"+f.getName()+" Links Are");
				Set<String> urlStrSet = new HashSet<String>();
				for (Element link : links) {
					String outgoingLink = link.attr("href");
					String[] breakSlashes = outgoingLink.split("/");
					String outUrl = breakSlashes[breakSlashes.length - 1];
					urlStrSet.add(outUrl);
				}
				String title = doc.title();

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
				String[] metadataNames = metadata.names();
				String metadataStr = "";
				for (String meta : metadataNames) {
					metadataStr = metadataStr + meta + metadata.get(meta)
							+ "\n";
				}
				String parentUrl = null;
				String headerStr =null;

				Doc docDb = new Doc(f.getName(), new Date(),
						emailSha(f.getName()), location, metadataStr, null,
						title, location, urlStrSet, parentUrl);
				docsList.add(docDb);
				//docDao.saveDoc(docDb);
			}
			/*DocsDao docsDao=new DocsDao();
			docsDao.saveDocs(docList);*/
		}
		
		
	}

	public static void main(String sr[]) throws IOException {
		// Read data from the files
		DocDao docDao=new DocDao();
		walk("D:/en");
		docDao.saveDocsList(docsList);
		
	}

}
