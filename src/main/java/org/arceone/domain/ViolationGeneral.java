package org.arceone.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * For a complete reference see 
 * <a href="http://docs.jboss.org/hibernate/stable/annotations/reference/en/html_single/">
 * Hibernate Annotations Communit Documentations</a>
 */
//@Entity
//@Table(name = "VIOLATION")
public class ViolationGeneral implements Serializable {

	private static final long serialVersionUID = -5527566248002296042L;
	
	public static String VIOLATION_STATUS_NEW = "ACIK";
	public static String VIOLATION_STATUS_RESOLVED = "COZULDU";
	
	//String dominos = "ş";
	public static Map getViolationLevelData() throws Exception {
		Map referenceData = new HashMap();
		Map<String,String> country = new LinkedHashMap<String,String>();
		country.put("1", "1");
		country.put("2", "2");
		country.put("3", "3");
		country.put("4", "4");
		country.put("5", "5");
		referenceData.put("countryList", country);
		return country;
	}
	
	public static Timestamp getSqlDate() {
		java.util.Calendar cal = java.util.Calendar.getInstance();
		java.util.Date utilDate = cal.getTime();
		java.sql.Timestamp sqlDate = new Timestamp(utilDate.getTime());
		return sqlDate;
	}
	
	public static String convertTrToEng(String trString) {
		trString = trString.toLowerCase();
		char chars[] = trString.toCharArray();		
		int i=0;
		while(i<chars.length){
			String c = ""+chars[i];
			if('ö' == chars[i])
				chars[i] ='o';
			else if('ç' == chars[i])
				chars[i] ='c';
			else if('ğ' == chars[i])
				chars[i] ='g';
			else if('ş' == chars[i])
				chars[i] ='s';
			else if('ü' == chars[i])
				chars[i] ='u';
			else if('ı' == chars[i])
				chars[i] ='i';
			i++;
		}
		
		return String.valueOf(chars);
	}
	
	public static String convertTr(String trString) {
		trString = trString.toLowerCase();
		char chars[] = trString.toCharArray();		
		int i=0;
		while(i<chars.length){
			String c = ""+chars[i];
			if('ö' == chars[i])
				chars[i] ='o';
			else if('ç' == chars[i])
				chars[i] ='c';
			else if('ğ' == chars[i])
				chars[i] ='g';
			else if('ş' == chars[i])
				chars[i] ='s';
			else if('ü' == chars[i])
				chars[i] ='u';
			else if('ı' == chars[i])
				chars[i] ='i';
			i++;
		}
		
		return String.valueOf(chars);
	}
	
}
