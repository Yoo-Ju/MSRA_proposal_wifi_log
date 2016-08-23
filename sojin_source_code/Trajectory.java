package colon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.*;

import au.com.bytecode.opencsv.CSVReader;


public class Trajectory {

	public static void main( String[] args ) throws IOException, ParseException
	    {
	    	int num = 0;
	    	int total = 0;

	    	BufferedWriter out = null;
	    	BufferedWriter customerTrajectory = null;
	    	BufferedWriter textout = null;
	    //	BufferedWriter userName = null;

			String filePath;
			//String filename = "epoche.txt";

			BufferedReader in;

			int i = 0;
			
			int currentTime = 0;
			String gap = "0";
			int size = 0;
			long epoch;
			
			
			//input file list
			in = new BufferedReader(new FileReader("CM_list_Winter.txt"));
			
			 try {
					customerTrajectory = new BufferedWriter(new FileWriter("CM_CustomerTrjactoryFile_0527_Winter.csv"));
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			
			   try {
					out = new BufferedWriter(new FileWriter("CM_TotalResult_0527_Winter.csv"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			
			   try {
					textout = new BufferedWriter(new FileWriter("CM_Final_sequence_0527_Winter.txt"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			
			
			while((filePath = in.readLine()) != null){
		
				Map<String, List<String>> mapArea = new LinkedHashMap<String, List<String>>();
				Map<String, List<String>> mapTime = new LinkedHashMap<String, List<String>>();
				Map<String, List<String>> mapDate = new LinkedHashMap<String, List<String>>();
				Map<String, List<String>> mapGap = new LinkedHashMap<String, List<String>>();
				
				
		
				try {

					CSVReader reader = new CSVReader(new FileReader(filePath));

				    String[] nextLine;
				    
			 
				       

					while ((nextLine = reader.readNext()) != null){
						String userID = nextLine[0];
						String ar = nextLine[2];
						String time = nextLine[3];
						String date = nextLine[4];
						String deny = nextLine[7];
						

						
						if(userID.compareTo("Device_ID") == 0)
							continue;
						
						//���� �����̶�� �ش� �α״� ��ŵ
						if(deny.compareTo("true") == 0){
		//					System.out.println(deny);
							continue;
						}
		//				System.out.println(deny);	
						
						String[] arr = date.split(" ");
						String [] arr2 = arr[1].split(":");
						
						//�ش� ������ ���� �ð��� ���캽
						if(10 > Integer.parseInt(arr2[0]) | Integer.parseInt(arr2[0]) >= 21)
							continue;


					
						//���� ID�� ������ ������ �ʾҴٸ� ���ο� list�� ����
						if(!mapArea.containsKey(userID)) {
							mapArea.put(userID, new LinkedList<String>());
						}


						if(!mapTime.containsKey(userID)) 
							mapTime.put(userID, new LinkedList<String>());
							

						if(!mapDate.containsKey(userID)) 
							mapDate.put(userID, new LinkedList<String>());
						
						//���� ������ ó�� ������ ���̶�� dwell_time�� 0���� �ΰ� ���� ������ �����Ͽ��ٸ� ���� Ÿ�ӽ������� �ð� ���� ���Ͽ� dwell_time�� ����
						if(!mapGap.containsKey(userID)) {
							mapGap.put(userID, new LinkedList<String>());
							gap = "0";
						}
						else{
							size = mapTime.get(userID).size();
							epoch = Long.parseLong(mapTime.get(userID).get(size-1));
						
							gap = Double.toString((epoch - Long.parseLong(time)) / 1000.0);
							
							//���� �ð��뿡 �αװ� �̹� �����Ѵٸ� ���� �α״� ��ŵ
							if(Double.parseDouble(gap) == 0)
								continue;
						
						};

					
						int IndexSize = mapGap.get(userID).size();
						
						
				
						if(IndexSize == 1 & ar.compareTo("out") == 0){
							String lastArea = mapArea.get(userID).get(IndexSize-1);
							
							if(lastArea.compareTo("out")==0) {
							
							mapArea.remove(userID);
							mapTime.remove(userID);
							mapDate.remove(userID);
							mapGap.remove(userID);
					//		mapTotal.remove(userID);
							
							
							mapArea.put(userID, new LinkedList<String>());
							mapTime.put(userID, new LinkedList<String>());
							mapDate.put(userID, new LinkedList<String>());
							mapGap.put(userID, new LinkedList<String>());
												
							mapArea.get(userID).add(ar);
							mapTime.get(userID).add(time);
							mapDate.get(userID).add(date);
							mapGap.get(userID).add(gap);
						//	mapTotal.put(userID, Double.parseDouble(gap));
							
							continue;
							}
							
						}
						
						//���� �����Ϳ��� �ð� ���� 1�ð� �̸��̸� ���� Ʈ�����丮�� ���
						if(IndexSize >= 1 & Double.parseDouble(gap) < 3600.0){
							
							mapArea.get(userID).add(ar);
							mapTime.get(userID).add(time);
							mapDate.get(userID).add(date);
							mapGap.get(userID).add(gap);
			
							
						}
						
						//���� log data���� �ð� ���� 1�ð� �̻��̸� ���Ӱ� ���忡 ������ ������ ���
						else if(IndexSize > 1 & Double.parseDouble(gap) >= 3600.0  ){
				 
								String key = userID;
								double totalTime = 0.0;
								int simbolNum = 0;
								long averageTime = 0;
								String currentArea = null;
								                                               
						//		customerTrajectory.write(key + ",");
								out.write(key + ",");
								Iterator<String> itA = mapArea.get(key).iterator();
		
								customerTrajectory.write(key + ",");
						//		userName.write(key+",");
								int totalNum = 0;
								int stayPoint = 0;
								while(itA.hasNext()){
								//	out.write(itA.next()  + ",");
									currentArea = itA.next();
									customerTrajectory.write(currentArea  + ",");
									simbolNum++;
									if(currentArea.compareTo("out") != 0){
										totalNum++;
										if(Double.parseDouble(gap) >= 10)
											stayPoint++;
									}
								}
								out.write(Integer.toString(stayPoint) + ",");
								//out.newLine();
								customerTrajectory.newLine();
								customerTrajectory.write(",");
							//	out.write(key + ",");
								Iterator<String> itDate = mapDate.get(key).iterator();
								Iterator<String> itTime = mapTime.get(key).iterator();
								while(itDate.hasNext()){
								//	out.write(itDate.next()  + ",");
									customerTrajectory.write(itDate.next()  + ",");
									averageTime += Long.parseLong(itTime.next());
								}
								if(totalNum == 0)
									averageTime = 0;
								else
									averageTime = averageTime/simbolNum;
								String Averagedate = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date (averageTime));
								
								
								Iterator<String> itArea = mapArea.get(key).iterator();
								int c = 0;
								c = (mapArea.get(key).size() -1);
								while(c >= 0){
									textout.write(mapArea.get(key).get(c) + " -1 ");
									c--;
								}
								textout.write("-2");
								textout.newLine();
								
								customerTrajectory.newLine();
								customerTrajectory.write(",");
							//	out.newLine();
							//	out.write(key + ",");
								Iterator<String> itGap = mapGap.get(key).iterator();
								int gapX = 0;
								while(itGap.hasNext()){
									if(gapX == 0){
										String listGap = itGap.next();
										gapX++;
									}
								//	out.write(itGap.next() + ",");
									String listGap = itGap.next();
									customerTrajectory.write(listGap + ",");
									totalTime += Double.parseDouble(listGap);
								}
							
								customerTrajectory.write("0");
								
								out.write(Double.toString(totalTime) + ",");
								out.write(Averagedate + ",");
								out.write(Long.toString(averageTime/1000));
								out.newLine();
								
								customerTrajectory.newLine();
							
							gap = "0";
							
							mapArea.remove(key);
							mapTime.remove(key);
							mapDate.remove(key);
							mapGap.remove(key);
							
							//���� �����ͺ��� ���ο� linkedList ����
							mapArea.put(userID, new LinkedList<String>());
							mapTime.put(userID, new LinkedList<String>());
							mapDate.put(userID, new LinkedList<String>());
							mapGap.put(userID, new LinkedList<String>());
							
							mapArea.get(userID).add(ar);
							mapTime.get(userID).add(time);
							mapDate.get(userID).add(date);
							mapGap.get(userID).add(gap);
			
							
						}
						else {
							mapArea.get(userID).add(ar);
							mapTime.get(userID).add(time);
							mapDate.get(userID).add(date);
							mapGap.get(userID).add(gap);
							
						}
						


					}
					
		
						
						
					//LiknekdHashMap�� ����� �����͸�  csv�������·� write
					
					Iterator<String> keys = mapArea.keySet().iterator();
			
					
					
					
					while (keys.hasNext()) {
						
						
						
						double totalTime = 0.0;
						int simbolNum = 0;
						long averageTime = 0;
						String currentArea = null;
						int totalNum = 0;
						int stayPoint = 0;
						String key = keys.next();
					/*	int nume = mapArea.get(key).size();
						if(nume<=1)
							continue;*/
						
						int elementSize = mapArea.get(key).size();
						
						if(elementSize>3){
						
						
						out.write(key + ",");
						Iterator<String> itA = mapArea.get(key).iterator();
						customerTrajectory.write(key + ",");
				//		userName.write(key+",");
						while(itA.hasNext()){
						//	out.write(itA.next()  + ",");
							currentArea = itA.next();
							customerTrajectory.write(currentArea  + ",");
							simbolNum++;
							if(currentArea.compareTo("out") != 0){
								totalNum++;	
								if(Double.parseDouble(gap) >= 10)
									stayPoint++;
								
							}
						}
						out.write(Integer.toString(stayPoint) + ",");
						//out.newLine();
						customerTrajectory.newLine();
						customerTrajectory.write(",");
					//	out.write(key + ",");
						Iterator<String> itDate = mapDate.get(key).iterator();
						Iterator<String> itTime = mapTime.get(key).iterator();
						while(itDate.hasNext()){
						//	out.write(itDate.next()  + ",");
							customerTrajectory.write(itDate.next()  + ",");
							averageTime += Long.parseLong(itTime.next());
						}
						if(totalNum == 0)
							averageTime = 0;
						else
							averageTime = averageTime/simbolNum;
						
						String Averagedate = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date (averageTime));

					//	customerTrajectory.write(Averagedate);
						
						customerTrajectory.newLine();
						customerTrajectory.write(",");
				
						Iterator<String> itGap = mapGap.get(key).iterator();
						int gapX = 0;
						while(itGap.hasNext()){
							if(gapX == 0){
								String listGap = itGap.next();
								gapX++;
							}
						//	out.write(itGap.next() + ",");
							String listGap = itGap.next();
							customerTrajectory.write(listGap + ",");
							totalTime += Double.parseDouble(listGap);
						}
						customerTrajectory.write("0,");
						int c = 0;
						c = (mapArea.get(key).size() -1);
						while(c >= 0){
							textout.write(mapArea.get(key).get(c) + " -1 ");
							c--;
						}
						textout.write("-2");
						textout.newLine();
					
						out.write(Double.toString(totalTime) + ",");
						out.write(Averagedate + ",");
						out.write(Long.toString(averageTime/1000));
						out.newLine();
						
			
						customerTrajectory.newLine();
					
					}
					
					}
			
						
					
					
					i++;
					System.out.println(i);
			
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				  
				
			}
			textout.close();
			out.close();
			customerTrajectory.close();
			//userName.close();
			System.out.println("END");

	    }

}
