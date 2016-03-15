package web.crawler.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import web.crawler.db.dao.IndexDao;
import web.crawler.db.model.Index;
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
		int numberOfTermResult = 7;
		System.out.println("Search Servlet: POST");
		
		String term = request.getParameter("term");
		String[] splitTerm = term.split(" ");
		System.out.println("splitterm size: " + splitTerm.length);
		
		String sugessionResult = "";
		for(int i=0; i<splitTerm.length-1; i++)
			sugessionResult=sugessionResult + splitTerm[i] + " ";
		
		System.out.println("sugessionResult: " + sugessionResult);
			
		term = splitTerm[splitTerm.length-1];
		
		List<Index> indexes = indexDao.getIndexBySimilarTerm(term);
		List<ResultBean> items = new ArrayList<ResultBean>();
		
		for(Index i: indexes)
		{
			System.out.println(i.getTerm());
		}
		System.out.println(indexes.size() + " URLs found...");
		
		
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
		System.out.println(searchList);
		
		getServletContext().setAttribute("items", items);
		
		response.sendRedirect("Admin");
		response.getWriter().write(searchList);
		
	}
		
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
