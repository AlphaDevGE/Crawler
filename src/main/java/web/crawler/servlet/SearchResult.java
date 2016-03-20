package web.crawler.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.lucene.queryParser.ParseException;

import web.crawler.controller.Searching;
import web.crawler.db.dao.DocDao;
import web.crawler.db.dao.IndexDao;
import web.crawler.db.dao.UserDao;
import web.crawler.db.model.Doc;
import web.crawler.db.model.ResultBean;
import web.crawler.db.model.User;

/**
 * Servlet implementation class SearchResult
 */
public class SearchResult extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	IndexDao indexDao = new IndexDao();
	Map<String, Doc> docMap;
	private UserDao userDao = new UserDao();
	private String cookieName = "AlphaDev";
	private String termsSearched = "termsSearched";
	
	public void init(){
		
		docMap = new HashMap<String, Doc>();
		
		List<Doc> docs = new DocDao().getAllDocs();
		
		System.out.println("cearting Map of Docs....");
		for(Doc d : docs){
			docMap.put(d.getLocation(), d);
		}
		System.out.println("DocMap created...");
		
		getServletContext().setAttribute("docMap", docMap);
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchResult() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Search result GET");
		
		HttpSession session = request.getSession(); 
		String term = request.getParameter("search").toLowerCase();
		String[] splitTerm = term.split(" ");
		Searching searching = new Searching(); 
		List<ResultBean> items = new ArrayList<ResultBean>();
		docMap = (Map<String, Doc>) getServletContext().getAttribute("docMap");
		
		
		// start: Cookies part  
		boolean cookieFound = false;
		User user = null;
		Cookie cookie = new Cookie("null","null");
		String ipAddress = "testing IP Add";
		String geoLocation;
		String zipCode;
		Cookie[] cookies = request.getCookies();
		Map<String, Integer> sessionTermsSearched = (Map<String, Integer>) session.getAttribute( termsSearched );

		//get the GEO location and zipCode h
		geoLocation= "0000";
		zipCode = "0000";
		
		
		
		// Option1: if cookie exist
		for(Cookie c : cookies)
		{
			if(c.getName().equals(cookieName))
			{
				System.out.println("Cookie found ... ");
				cookieFound = true;
				String userId =c.getValue();
				System.out.println("User ID: " + userId);
				//find the user in DB
				user = userDao.getUserById(userId);
				if(user == null)
				{
					//we have cookie but not in DB. So create new User and save
					System.out.println("User Not found in DB: problem in the User ID in Cookies of DB");
					user = new User(geoLocation, zipCode, ipAddress, new Date(), new HashMap<String, Integer>());
					userDao.saveUser(user);
				}
				break;
			}
		}
		
		//Option2: if cookie does not exist 
		if(!cookieFound)
		{
			//create user and save it in cookie
			System.out.println("Creating New User...");
			user = new User();
			userDao.saveUser(user);
			System.out.println("User ID: " + user.getId());
		}
		//In any 2 options generate cookie for 2 years 
		cookie = new Cookie(cookieName, user.getId());
		cookie.setMaxAge(60 * 60 * 24 * 30 * 12 * 2); // 2year
		response.addCookie(cookie);
		System.out.println("cookie Extendet/Generated for 2 years");
		
		user.setIpAddress(ipAddress);
		userDao.saveUser(user);
		user = userDao.getUserById(user.getId());
				
		//set the previous terms search to session in cluding the new one
		int count = 0;
		if(sessionTermsSearched == null)
			System.out.println("termsSearched not found in session...");
		else
		{
			//check if term exists in DB
			if(user.getTermsSearched() == null)
				user.setTermsSearched(new HashMap<String, Integer>());
			if(user.getTermsSearched().get(term) != null)
				count = user.getTermsSearched().get(term);
			user.getTermsSearched().put(term, ++count);
		}
		
		userDao.saveUser(user);
		
		System.out.println("termsSearched saved in session ... ");
		session.setAttribute(termsSearched, user.getTermsSearched());	
		
		//end: Cookies part 
		
		if(splitTerm.length>1)
		{
			try {
				items = Searching.searchIndexWithQueryParser(term);
//				System.out.println("Boolean found:"+items.size());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			//remove the space from the term
			term = term.replaceAll("\\s+", "");
			items = Searching.singleTermSearch(term, docMap);
		}
		
		session.setAttribute("items", items);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
		dispatcher.forward(request, response);
	}
		
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
