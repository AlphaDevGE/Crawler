package web.crawler.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.lucene.queryParser.ParseException;

import web.crawler.controller.Searching;
import web.crawler.db.dao.IndexDao;
import web.crawler.db.model.ResultBean;

/**
 * Servlet implementation class SearchResult
 */
public class SearchResult extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	IndexDao indexDao = new IndexDao();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchResult() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Search result GET");
		
		HttpSession session = request.getSession(); 
		String term = request.getParameter("search");
		String[] splitTerm = term.split(" ");
		Searching searching = new Searching(); 

		List<ResultBean> items = new ArrayList<ResultBean>();
		
		if(splitTerm.length>1)
		{
			try {
				items = Searching.searchIndexWithQueryParser(term);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
			items = Searching.singleTermSearch(term);
		
		for(ResultBean r : items)
			System.out.println(r.getLocation());
		
//		getServletContext().setAttribute("items", items);
		session.setAttribute("items", items);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
		dispatcher.forward(request, response);

		
//		response.sendRedirect("index.html");
		
	}
		
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
