package web.crawler.controller;

import web.crawler.db.dao.DocDao;

public class TestDB {

	public static void main(String[] args) {
		DocDao dao = new DocDao();
		
		System.out.println("Doc by id: \n" + dao.getDocById("56eb44014153b9095a997565"));
		
	}

}
