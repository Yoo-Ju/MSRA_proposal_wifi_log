package org.parser.Pars;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.*;


public class toCSV {
	
	public static void main(String[] args) {

    	BufferedWriter out = null;

		JSONParser parser = new JSONParser();
		String filename;
		String standard="08/28/2015";
		
		//json파일을 읽어 날짜별로 csv파일 생성
		for(int j = 0; j<2669; j++){
			int i = 0;
			try {
				
					
				  	filename = "CM_ordered_data_" + j + ".json";
				
				  
				  	Object obj = parser.parse(new FileReader(filename));
					 
	
					JSONObject jsonObject = (JSONObject) obj;
			
		
					
					JSONArray arr = (JSONArray) jsonObject.get("sessions");
					
					Object o2 = arr.get(0);
					JSONObject jo2 =  (JSONObject) o2;
				
					long epoch = Long.valueOf(jo2.get("ts").toString());
					String date = " "+ new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (epoch));
					String[] d = date.split(" ");
					
		//			System.out.println(d[1]);
					String dateName = null;
					
					if(standard.compareTo(d[1]) != 0){
						
						if(j != 0)
							out.close();
						
						dateName = d[1];
						standard = d[1];
						String[] dateArr = dateName.split("/");
				
					//	System.out.println(dateArr[2]);
						String name = "CM_ordered_csvData_" + dateArr[2] + dateArr[0] +dateArr[1] +".csv" ;
						
						out = new BufferedWriter(new FileWriter(name));
						out.write("Device_ID,");
						out.write("Dwell_time,");
						out.write("area,");
						out.write("ts,");
						out.write("Human time,");
						out.write("revisit_count,");
						out.write("revisit_period,");
						out.write("Deny");
						out.newLine();
						
						
					}
					
				
				Iterator<String> iterator = arr.iterator();

			
				//json파일을 짤라서 csv파일에 씀
				while(iterator.hasNext()){
					iterator.next();
					
					
					Object o1 = arr.get(i);
					JSONObject jo =  (JSONObject) o1;
					
			//		System.out.println(jo.toString());
				
					
			//		System.out.println(name);
			//		out.write(",");
					
					out.write(jo.get("device_id").toString());
					out.write(",");
					out.write(jo.get("dwell_time").toString());
					out.write(",");
					out.write(jo.get("area").toString());
					out.write(",");
					out.write(jo.get("ts").toString());
					out.write(",");
					long epoch2 = Long.valueOf(jo.get("ts").toString());
					String date2 = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (epoch2));
					out.write(date2);
					
					if(jo.containsKey("revisit_count"))
						out.write(","+jo.get("revisit_count").toString());
					else
						out.write(",0");
					
					if(jo.containsKey("revisit_period"))
						out.write(","+jo.get("revisit_period").toString());
					else
						out.write(",0");
					
					if(jo.containsKey("deny"))
						out.write(","+jo.get("deny").toString());
					else
						out.write(",FALSE");

					out.newLine();
					i++;
				
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (org.json.simple.parser.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println(j);
			
		}
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("끛");
    }

}
