package web.crawler.db.model;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.htrace.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import web.crawler.constant.DBTable;

/**
 * 
 * 
 * @author AlphaDev, Mohammad Yazdani 
 * @email m.yazdani2010@gmail.com
 * 
 */

@Document(collection = DBTable.USER)
public class User {	
	@Id
	@JsonProperty
	private String id;
	private String geoLocation;
	private String zipCode;
	private String ipAddress;
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date lastSearch = new Date();
	private Map<String, Integer> termsSearched = new HashMap<String, Integer>();

	public User() {super();}

	public User(String geoLocation, String zipCode, String ipAddress, Date lastSearch, Map<String, Integer> termsSearched) {
		super();
		this.geoLocation = geoLocation;
		this.zipCode = zipCode;
		this.ipAddress = ipAddress;
		this.lastSearch = lastSearch;
		this.termsSearched = termsSearched;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(String geoLocation) {
		this.geoLocation = geoLocation;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Date getLastSearch() {
		return lastSearch;
	}

	public void setLastSearch(Date lastSearch) {
		this.lastSearch = lastSearch;
	}

	public Map<String, Integer> getTermsSearched() {
		return termsSearched;
	}

	public void setTermsSearched(Map<String, Integer> termsSearched) {
		this.termsSearched = termsSearched;
	}

	public String toString() {
		String str = String.format(
                "'Doc':{\n"
              + "		'id': %s,\n"
              + "		'geoLocation': '%s',\n"
              + "		'zipCode': '%s',\n"
              + "		'ipAddress': '%s',\n"
              + "		'lastSearch': '%s',\n"
              + "		'termsSearched': '%s',\n"
              + "		},\n",
                id, geoLocation, zipCode, ipAddress, lastSearch, termsSearched
                );
		return str;
	}
	
}