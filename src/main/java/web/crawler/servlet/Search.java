package web.crawler.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import web.crawler.db.dao.UrlDao;
import web.crawler.db.model.Url;


@WebServlet("/index.html")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private UrlDao urlDao = new UrlDao();

    public Search() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		System.out.println("Search Servlet: GET");
		
    	RequestDispatcher dis = request.getRequestDispatcher("/index.jsp");
    	dis.forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		int numberOfTermResult = 7;
		System.out.println("Search Servlet: POST");
		
		String term = request.getParameter("term");

		
		List<Url> urls = urlDao.getUrlByTerm(term);
		
		for(Url u: urls)
		{
			System.out.println(u.getUrl());
		}
		System.out.println(urls.size() + " URLs found...");
		
		
		List<String> strList = new ArrayList<String>();

		int count = 0;
		for(Url u : urls)
		{
			strList.add(u.getUrl().replaceAll("/", "") );
			count++;
			if(count >= numberOfTermResult )
				break;
		}
		String searchList = new JSONArray(strList).toString();
		System.out.println(searchList);
		response.getWriter().write(searchList);
		
	}
}