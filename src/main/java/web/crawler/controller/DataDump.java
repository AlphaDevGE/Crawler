package web.crawler.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import web.crawler.db.dao.UrlDao;
import web.crawler.db.model.Url;

public class DataDump {
	
	public static void main(String[] args) {
		UrlDao urlDao = new UrlDao();
		 

		List<Url> urls = urlDao.getAllUrls();

		List<JSONObject> joList = new ArrayList<JSONObject>();

		int count = 1;
		for(Url u: urls)
		{
			JSONObject jo = new JSONObject();
			jo.put("url", u.getUrl());
			jo.put("Outgoing URLs", u.getOutgoingUrls());
			jo.put("Metadata", u.getMetadata());
			jo.put("Path", u.getLocation());
//			System.out.println(jo.toJSONString());
			joList.add(jo);
		}
		System.out.println(joList);
		System.out.println(urls.size() + "objects found");

	}

}