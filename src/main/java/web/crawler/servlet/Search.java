package web.crawler.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;

import web.crawler.db.dao.IndexDao;
import web.crawler.db.dao.UserDao;
import web.crawler.db.model.Index;
import web.crawler.db.model.User;

@WebServlet("/index.html")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private IndexDao indexDao = new IndexDao();

	public Search() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Search Servlet: GET");
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
		dispatcher.forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int numberOfTermResult = 7;
		// System.out.println("Search Servlet: POST");

		String term = request.getParameter("term");
		String[] splitTerm = term.split(" ");
		// System.out.println("splitterm size: " + splitTerm.length);

		String sugessionResult = "";
		for (int i = 0; i < splitTerm.length - 1; i++)
			sugessionResult = sugessionResult + splitTerm[i] + " ";

		// System.out.println("sugessionResult: " + sugessionResult);

		term = splitTerm[splitTerm.length - 1];

		List<Index> indexes = indexDao.getIndexBySimilarTerm(term);

		List<String> strList = new ArrayList<String>();

		int count = 0;
		for (Index i : indexes) {
			strList.add(sugessionResult + i.getTerm());
			count++;
			if (count >= numberOfTermResult)
				break;
		}
		String searchList = new JSONArray(strList).toString();
		// System.out.println(searchList);
		response.getWriter().write(searchList);
	}
}
