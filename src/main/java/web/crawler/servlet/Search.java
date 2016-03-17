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

import web.crawler.db.dao.IndexDao;
import web.crawler.db.dao.UrlDao;
import web.crawler.db.model.Index;
import web.crawler.db.model.Url;


@WebServlet("/index.html")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private IndexDao indexDao = new IndexDao();

    public Search() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		System.out.println("Search Servlet: GET");
		
    	RequestDispatcher dis = request.getRequestDispatcher("/index.jsp");
//    	dis.forward(request, response);
    	
		RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
		dispatcher.forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		int numberOfTermResult = 7;
		//System.out.println("Search Servlet: POST");
		
		String term = request.getParameter("term");
		String[] splitTerm = term.split(" ");
		//System.out.println("splitterm size: " + splitTerm.length);
		
		String sugessionResult = "";
		for(int i=0; i<splitTerm.length-1; i++)
			sugessionResult=sugessionResult + splitTerm[i] + " ";
		
		//System.out.println("sugessionResult: " + sugessionResult);
			
		term = splitTerm[splitTerm.length-1];
		
		List<Index> indexes = indexDao.getIndexBySimilarTerm(term);
		
		for(Index i: indexes)
		{
		//	System.out.println(i.getTerm());
		}
		//System.out.println(indexes.size() + " URLs found...");
		
		
		List<String> strList = new ArrayList<String>();

		int count = 0;
		for(Index i : indexes)
		{
			strList.add( sugessionResult + i.getTerm() );
			count++;
			if(count >= numberOfTermResult )
				break;
		}
		String searchList = new JSONArray(strList).toString();
		//System.out.println(searchList);
		response.getWriter().write(searchList);
		
	}
}
