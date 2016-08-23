package org.parser.Pars;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Craw {
	public static void main(String[] args) throws FileNotFoundException{
	int j = 0;

	String filename = null;
	String line;
	BufferedReader inn;
	JSONParser parser = new JSONParser();
	
	BigInteger a = 	new BigInteger("9223372036854775807");
	int b = 0x1000; 
	BigInteger ak = new BigInteger(Integer.toString(b));
	BigInteger result = null;
	String s = null;
	String add = "http://walkinsights.com/api/v1/shops/781/sessions?since="; //API의 주소 앞 부분
	String lastPart = "&limit=1000&user_token=77eFJPB37UkgYyLjWZrx&amp;user_email=jeeyeachung@kolon.com"; //API의 주소 뒷 부분

  	BigInteger c = null;
	FileOutputStream fos = null;
	BigInteger timelimit = new BigInteger("1440760961000"); //가장 마지막으로 읽어들일 TS 값(2015년 8월 28일 저녁 8시로 되어있음)
	
	  try {
		  
		  while (true) {
			  
			  
			   String urlStr = null;
			   System.out.println(j);
			  
			   filename = "782" + j + ".json";
			
			  
			  	Object obj = parser.parse(new FileReader(filename));
				 
			//  	System.out.println(filename);
				JSONObject jsonObject = (JSONObject) obj;
		
	
				// loop array
				JSONArray arr = (JSONArray) jsonObject.get("sessions");
				
				Object o2 = arr.get(arr.size()-1);
				JSONObject jo2 =  (JSONObject) o2;
				String epoch = jo2.get("ts").toString();
				
				c = new BigInteger(epoch);
				//System.out.println(c);
				BigInteger multiplyed = c.multiply(ak);
			//	System.out.println(multiplyed);
				result = a.subtract(multiplyed);
			//	System.out.println(result);
				s = result.toString(16);
				
		//		System.out.println(s);

				urlStr = add.concat(s);
				urlStr = urlStr.concat(lastPart);
				

				//현재 타임스탬프가 time limit보다 작다면 크롤링을 멈춤
				if(c.compareTo(timelimit) <= 0)
					break;
				
			   URL url = new URL(urlStr);
		   
			   System.out.println(url);
			   InputStream in = url.openStream();
			   j++;
			   fos = new FileOutputStream("CM_ordered_data_" + j + ".json");
			  
			   byte[] buffer = new byte[512];
			   int readcount = 0;

			   
			   while((readcount = in.read(buffer))!=-1){
				   fos.write(buffer, 0, readcount);
			   }
			   
	
			  }
		  
		  }catch (Exception e) {
				   System.out.println(e);
				  } finally{
				   try {
				    if(fos!=null) fos.close();
				   } catch (Exception e2) {}
				  }
	  
	  System.out.println("끝");
	  }


}
