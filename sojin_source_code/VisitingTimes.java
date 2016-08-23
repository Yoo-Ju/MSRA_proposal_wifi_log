package colon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import au.com.bytecode.opencsv.CSVReader;


public class VisitingTimes {
	public static void main( String[] args ) throws IOException, ParseException
    {
    	int num = 0;
    	int total = 0;

    	BufferedWriter out_firstVisit = null;
    	BufferedWriter customerTrajectory_firstVisit = null;
    	BufferedWriter textout_firstVisit = null;
    	
    	BufferedWriter out_revisit = null;
    	BufferedWriter customerTrajectory_revisit = null;
    	BufferedWriter textout_revisit = null;
    	BufferedWriter userName_revisit = null;

    	
    	BufferedWriter out_5visit = null;
    	BufferedWriter customerTrajectory_5visit = null;
    	BufferedWriter textout_5visit = null;
      	BufferedWriter userName_5visit = null;
    	
    	
    	
		String filePath;
		//String filename = "epoche.txt";

		BufferedReader in;

		int i = 0;
		
		int currentTime = 0;
		String gap = "0";
		int size = 0;
		long epoch;
		
		in = new BufferedReader(new FileReader("list_CM.txt"));
		
		//1~2번 방문한 고객의 trajectory 및 stay point 개수
		 try {
				customerTrajectory_firstVisit = new BufferedWriter(new FileWriter("CustomerTrjactoryFile_firstVisit_CM.csv"));
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		
		   try {
				out_firstVisit = new BufferedWriter(new FileWriter("TotalResult_firstVisit_CM.csv"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		
		   try {
				textout_firstVisit = new BufferedWriter(new FileWriter("Final_sequence_firstVisit_CM.txt"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		   
		 //3~4번 방문한 고객의 trajectory 및 stay point 개수
			 try {
					customerTrajectory_revisit = new BufferedWriter(new FileWriter("CustomerTrjactoryFile_rivisit_CM.csv"));
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			
			   try {
					out_revisit = new BufferedWriter(new FileWriter("TotalResult_rivisit_CM.csv"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			
			   try {
					textout_revisit = new BufferedWriter(new FileWriter("Final_sequence_rivisit_CM.txt"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			   
			 //5번 이상 방문한 고객의 trajectory 및 stay point 개수   
			   try {
					customerTrajectory_5visit = new BufferedWriter(new FileWriter("CustomerTrjactoryFile_5visit_CM.csv"));
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			
			   try {
					out_5visit = new BufferedWriter(new FileWriter("TotalResult_5visit_CM.csv"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			
			   try {
					textout_5visit = new BufferedWriter(new FileWriter("Final_sequence_5visit_CM.txt"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
		
			try {
					userName_revisit = new BufferedWriter(new FileWriter("revisit_customer_CM.txt"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
			} 
		
			try {
				userName_5visit = new BufferedWriter(new FileWriter("regular_customer_CM.txt"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
		} 
			
			
		while((filePath = in.readLine()) != null){
			
			//1~2번 방문한 고객 hashmap
	
			Map<String, List<String>> mapArea_firstVisit = new LinkedHashMap<String, List<String>>();
			Map<String, List<String>> mapTime_firstVisit = new LinkedHashMap<String, List<String>>();
			Map<String, List<String>> mapDate_firstVisit = new LinkedHashMap<String, List<String>>();
			Map<String, List<String>> mapGap_firstVisit = new LinkedHashMap<String, List<String>>();
			Map<String, String> mapCount_firstVisit = new LinkedHashMap<String, String>();
			Map<String, String> mapDuration_firstVisit = new LinkedHashMap<String, String>();

			//3~4번 방문한 고객 hashmap
			Map<String, List<String>> mapArea_revisit = new LinkedHashMap<String, List<String>>();
			Map<String, List<String>> mapTime_revisit = new LinkedHashMap<String, List<String>>();
			Map<String, List<String>> mapDate_revisit = new LinkedHashMap<String, List<String>>();
			Map<String, List<String>> mapGap_revisit = new LinkedHashMap<String, List<String>>();
			Map<String, String> mapCount_revisit = new LinkedHashMap<String, String>();
			Map<String, String> mapDuration_revisit = new LinkedHashMap<String, String>();
			
			//5번 이상 방문한 고객 hashmap
			Map<String, List<String>> mapArea_5visit = new LinkedHashMap<String, List<String>>();
			Map<String, List<String>> mapTime_5visit = new LinkedHashMap<String, List<String>>();
			Map<String, List<String>> mapDate_5visit = new LinkedHashMap<String, List<String>>();
			Map<String, List<String>> mapGap_5visit = new LinkedHashMap<String, List<String>>();
			Map<String, String> mapCount_5visit = new LinkedHashMap<String, String>();
			Map<String, String> mapDuration_5visit = new LinkedHashMap<String, String>();
			
				

			try {

				CSVReader reader = new CSVReader(new FileReader(filePath));

			    String[] nextLine;
			    
		 
			       

				while ((nextLine = reader.readNext()) != null){
					String userID = nextLine[0];
					String ar = nextLine[2];
					String time = nextLine[3];
					
				
					String date = nextLine[4];
					String revisit_count = nextLine[5];
					String revisit_duration = nextLine[6];
					String deny = nextLine[7];
					
			
					
					if(userID.compareTo("Device_ID") == 0)
						continue;
					
					if(deny.compareTo("true") == 0){
	//					System.out.println(deny);
						continue;
					}
					

					
					String[] arr = date.split(" ");
					String [] arr2 = arr[1].split(":");
					
					if(10 > Integer.parseInt(arr2[0]) | Integer.parseInt(arr2[0]) >= 21)
						continue;

					//1~2번 방문한 고객 정보
					
					if(Integer.parseInt(revisit_count) <= 1) {
						if(!mapArea_firstVisit.containsKey(userID)) {
							mapArea_firstVisit.put(userID, new LinkedList<String>());
						}


						if(!mapTime_firstVisit.containsKey(userID)) 
							mapTime_firstVisit.put(userID, new LinkedList<String>());
							

						if(!mapDate_firstVisit.containsKey(userID)) 
							mapDate_firstVisit.put(userID, new LinkedList<String>());
						
						if(!mapGap_firstVisit.containsKey(userID)) {
							mapGap_firstVisit.put(userID, new LinkedList<String>());
							gap = "0";
						}
						else{
							size = mapTime_firstVisit.get(userID).size();
				//			System.out.println(Long.parseLong(time));
							epoch = Long.parseLong(mapTime_firstVisit.get(userID).get(size-1));
						
							gap = Double.toString((epoch - Long.parseLong(time)) / 1000.0);
					
							if(Double.parseDouble(gap) == 0)
								continue;
							
							//System.out.println(gap);
						};
						
						if(!mapCount_firstVisit.containsKey(userID)) {
							mapCount_firstVisit.put(userID, revisit_count);
						}
						if(!mapDuration_firstVisit.containsKey(userID)) {
							mapDuration_firstVisit.put(userID, revisit_duration);
						}
						
						int IndexSize = mapGap_firstVisit.get(userID).size();
						
						//이전 구역이 out인 데이터가 1개 들어있다면 그 정보는 불필요하기 때문에 삭제
						
							if(IndexSize == 1 & ar.compareTo("out") == 0){
								String lastArea = mapArea_firstVisit.get(userID).get(IndexSize-1);
								
								if(lastArea.compareTo("out")==0) {
								
								mapArea_firstVisit.remove(userID);
								mapTime_firstVisit.remove(userID);
								mapDate_firstVisit.remove(userID);
								mapGap_firstVisit.remove(userID);
						//		mapTotal.remove(userID);
								
								
								mapArea_firstVisit.put(userID, new LinkedList<String>());
								mapTime_firstVisit.put(userID, new LinkedList<String>());
								mapDate_firstVisit.put(userID, new LinkedList<String>());
								mapGap_firstVisit.put(userID, new LinkedList<String>());
													
								mapArea_firstVisit.get(userID).add(ar);
								mapTime_firstVisit.get(userID).add(time);
								mapDate_firstVisit.get(userID).add(date);
								mapGap_firstVisit.get(userID).add(gap);
						//		mapTotal.put(userID, Double.parseDouble(gap));
								
								continue;
								}
								
							}
							
							if(IndexSize >= 1 & Double.parseDouble(gap) < 3600.0){
								
								mapArea_firstVisit.get(userID).add(ar);
								mapTime_firstVisit.get(userID).add(time);
								mapDate_firstVisit.get(userID).add(date);
								mapGap_firstVisit.get(userID).add(gap);
								
					
								
							}

							//이전 로그와의 시간 차가 1시간 이상이면 새로운 트라젝토리로 취급
							
							else if(IndexSize > 1 & Double.parseDouble(gap) >= 3600.0  ){
					 
									String key = userID;
									double totalTime = 0.0;
									int simbolNum = 0;
									long averageTime = 0;
									String currentArea = null;
									double firstFloor_time = 0;
									double firstFloor_total = 0;
									double firstFloor_left = 0;
									double firstFloor_right = 0;
									double firstFloor_inner = 0;
									
									double secondFloor_time = 0;
									double secondFloor_left = 0;
									double secondFloor_right = 0;
									double secondFloor_inner = 0;
									double secondFloor_total = 0;
									double thirdFloor = 0;
									                                               
							//		customerTrajectory.write(key + ",");
									out_firstVisit.write(key + ",");
									
									int numberofTra =  (mapArea_firstVisit.get(key).size() -1 );
									Iterator<String> itA = mapArea_firstVisit.get(key).iterator();
			
									customerTrajectory_firstVisit.write(key + ",");
							//		userName.write(key+",");
									int totalNum = 0;
									int n = 0;
									
									while(itA.hasNext()){
									//	out.write(itA.next()  + ",");
										currentArea = itA.next();
										
										customerTrajectory_firstVisit.write(currentArea  + ",");
										simbolNum++;
								
										
										//층별 체류 시간 계산
										
										if(currentArea.compareTo("1f") == 0) {
											firstFloor_time += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
											firstFloor_total += Double.parseDouble(mapGap_firstVisit.get(key).get(n));;
										}
										else if(currentArea.compareTo("1f-right-1") == 0){
											firstFloor_right += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
											firstFloor_total += Double.parseDouble(mapGap_firstVisit.get(key).get(n));;}
										else if(currentArea.compareTo("1f-right-2") == 0){
											firstFloor_left += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
											firstFloor_total += Double.parseDouble(mapGap_firstVisit.get(key).get(n));;
										}
										else if(currentArea.compareTo("1f-left-1") == 0) {
											firstFloor_inner += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
											firstFloor_total += Double.parseDouble(mapGap_firstVisit.get(key).get(n));; }
										else if(currentArea.compareTo("1f-left-2") == 0) {
											secondFloor_left += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
											secondFloor_total += Double.parseDouble(mapGap_firstVisit.get(key).get(n));;
										}
										else if(currentArea.compareTo("b1-left-3") == 0) {
											secondFloor_right += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
											secondFloor_total += Double.parseDouble(mapGap_firstVisit.get(key).get(n));;
										}
										else if(currentArea.compareTo("b1-left-2") == 0) {
											secondFloor_inner += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
											secondFloor_total += Double.parseDouble(mapGap_firstVisit.get(key).get(n));;
										}
										else if(currentArea.compareTo("b1-left-1") == 0) {
											secondFloor_time += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
											secondFloor_total += Double.parseDouble(mapGap_firstVisit.get(key).get(n));;
										}
										else if(currentArea.compareTo("b1-center-1") == 0) {
											secondFloor_right += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
											secondFloor_total += Double.parseDouble(mapGap_firstVisit.get(key).get(n));;
										}
										else if(currentArea.compareTo("b1-center-2") == 0) {
											secondFloor_inner += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
											secondFloor_total += Double.parseDouble(mapGap_firstVisit.get(key).get(n));;
										}
										else if(currentArea.compareTo("b1-right") == 0) {
											secondFloor_time += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
											secondFloor_total += Double.parseDouble(mapGap_firstVisit.get(key).get(n));;
										}
										else if(currentArea.compareTo("b1") == 0)
											thirdFloor += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
										
										n++;
									}
									
									//out.newLine();
									customerTrajectory_firstVisit.newLine();
									customerTrajectory_firstVisit.write(",");
								//	out.write(key + ",");
									Iterator<String> itDate = mapDate_firstVisit.get(key).iterator();
									Iterator<String> itTime = mapTime_firstVisit.get(key).iterator();
									while(itDate.hasNext()){
									//	out.write(itDate.next()  + ",");
										customerTrajectory_firstVisit.write(itDate.next()  + ",");
										averageTime += Long.parseLong(itTime.next());

									}
							
									if(simbolNum == 0)
										averageTime = 0;
									else
										averageTime = averageTime/simbolNum;
									String Averagedate = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date (averageTime));
									
									
									Iterator<String> itArea = mapArea_firstVisit.get(key).iterator();
									int c = 0;
									c = (mapArea_firstVisit.get(key).size() -1);
									while(c >= 0){
										textout_firstVisit.write(mapArea_firstVisit.get(key).get(c) + " -1 ");
										c--;
									}
									textout_firstVisit.write("-2");
									textout_firstVisit.newLine();
									
									customerTrajectory_firstVisit.newLine();
									customerTrajectory_firstVisit.write(",");
								//	out.newLine();
								//	out.write(key + ",");
									Iterator<String> itGap = mapGap_firstVisit.get(key).iterator();
									while(itGap.hasNext()){
										String listGap = itGap.next();
										customerTrajectory_firstVisit.write(listGap + ",");
										totalTime += Double.parseDouble(listGap);
										
										if(Double.parseDouble(listGap) >= 10)
											totalNum++;
									}
									customerTrajectory_firstVisit.newLine();
									
									out_firstVisit.write(Integer.toString(totalNum) + ",");
									out_firstVisit.write(Double.toString(totalTime) + ",");
									out_firstVisit.write(Averagedate + ",");
									out_firstVisit.write(Long.toString(averageTime/1000) + ",");
									out_firstVisit.write(mapCount_firstVisit.get(key)+ ",");
									out_firstVisit.write(mapDuration_firstVisit.get(key) + ",");
									out_firstVisit.write(Double.toString(firstFloor_time) + ",");
									out_firstVisit.write(Double.toString(firstFloor_left)  + ",");
									out_firstVisit.write(Double.toString(firstFloor_right)  + ",");
									out_firstVisit.write(Double.toString(firstFloor_inner)  + ",");
									out_firstVisit.write(Double.toString(secondFloor_time)  + ",");
									out_firstVisit.write(Double.toString(secondFloor_left)  + ",");
									out_firstVisit.write(Double.toString(secondFloor_right)  + ",");
									out_firstVisit.write(Double.toString(secondFloor_inner) + ",");
									out_firstVisit.write(Double.toString(firstFloor_total) + ",");
									out_firstVisit.write(Double.toString(secondFloor_total) + ",");
									out_firstVisit.write(Double.toString(thirdFloor));
									out_firstVisit.newLine();
									
				
								gap = "0";
								
								mapArea_firstVisit.remove(key);
								mapTime_firstVisit.remove(key);
								mapDate_firstVisit.remove(key);
								mapGap_firstVisit.remove(key);
								
								
								mapArea_firstVisit.put(userID, new LinkedList<String>());
								mapTime_firstVisit.put(userID, new LinkedList<String>());
								mapDate_firstVisit.put(userID, new LinkedList<String>());
								mapGap_firstVisit.put(userID, new LinkedList<String>());
								
								mapArea_firstVisit.get(userID).add(ar);
								mapTime_firstVisit.get(userID).add(time);
								mapDate_firstVisit.get(userID).add(date);
								mapGap_firstVisit.get(userID).add(gap);
				
								
							}
							else {
								mapArea_firstVisit.get(userID).add(ar);
								mapTime_firstVisit.get(userID).add(time);
								mapDate_firstVisit.get(userID).add(date);
								mapGap_firstVisit.get(userID).add(gap);
								
							}
							
						
						
					}
					
					//3~4번 방문한 고객 정보 
					
					else if(2 <= Integer.parseInt(revisit_count) & Integer.parseInt(revisit_count) <= 3 ) {
						if(!mapArea_revisit.containsKey(userID)) {
							mapArea_revisit.put(userID, new LinkedList<String>());
						}


						if(!mapTime_revisit.containsKey(userID)) 
							mapTime_revisit.put(userID, new LinkedList<String>());
							

						if(!mapDate_revisit.containsKey(userID)) 
							mapDate_revisit.put(userID, new LinkedList<String>());
						
						if(!mapGap_revisit.containsKey(userID)) {
							mapGap_revisit.put(userID, new LinkedList<String>());
							gap = "0";
						}
						else{
							size = mapTime_revisit.get(userID).size();
							epoch = Long.parseLong(mapTime_revisit.get(userID).get(size-1));
						
							gap = Double.toString((epoch - Long.parseLong(time)) / 1000.0);
					
							
							if(Double.parseDouble(gap) == 0)
								continue;
							
							//System.out.println(gap);
						};
						
						
						if(!mapCount_revisit.containsKey(userID)) {
							mapCount_revisit.put(userID, revisit_count);
						}
						if(!mapDuration_revisit.containsKey(userID)) {
							mapDuration_revisit.put(userID, revisit_duration);
						}
						
						int IndexSize = mapGap_revisit.get(userID).size();
						
						
				
							if(IndexSize == 1 & ar.compareTo("out") == 0){
								String lastArea = mapArea_revisit.get(userID).get(IndexSize-1);
								
								if(lastArea.compareTo("out")==0) {
								
								mapArea_revisit.remove(userID);
								mapTime_revisit.remove(userID);
								mapDate_revisit.remove(userID);
								mapGap_revisit.remove(userID);
						//		mapTotal.remove(userID);
								
								
								mapArea_revisit.put(userID, new LinkedList<String>());
								mapTime_revisit.put(userID, new LinkedList<String>());
								mapDate_revisit.put(userID, new LinkedList<String>());
								mapGap_revisit.put(userID, new LinkedList<String>());
													
								mapArea_revisit.get(userID).add(ar);
								mapTime_revisit.get(userID).add(time);
								mapDate_revisit.get(userID).add(date);
								mapGap_revisit.get(userID).add(gap);
						//		mapTotal.put(userID, Double.parseDouble(gap));
								
								continue;
								}
								
							}
							
							if(IndexSize >= 1 & Double.parseDouble(gap) < 3600.0){
								
								mapArea_revisit.get(userID).add(ar);
								mapTime_revisit.get(userID).add(time);
								mapDate_revisit.get(userID).add(date);
								mapGap_revisit.get(userID).add(gap);
				
								
								
							}

							else if(IndexSize > 1 & Double.parseDouble(gap) >= 3600.0  ){
					 
									String key = userID;
									double totalTime = 0.0;
									int simbolNum = 0;
									long averageTime = 0;
									String currentArea = null;
									
									double firstFloor_time = 0;
									double firstFloor_total = 0;
									double firstFloor_left = 0;
									double firstFloor_right = 0;
									double firstFloor_inner = 0;
									
									double secondFloor_time = 0;
									double secondFloor_left = 0;
									double secondFloor_right = 0;
									double secondFloor_inner = 0;
									double secondFloor_total = 0;
									double thirdFloor = 0;
									                                               
							//		customerTrajectory.write(key + ",");
									out_revisit.write(key + ",");
									Iterator<String> itA = mapArea_revisit.get(key).iterator();
									userName_revisit.write(key);
									userName_revisit.newLine();
									customerTrajectory_revisit.write(key + ",");
							//		userName.write(key+",");
									int totalNum = 0;
									int n = 0;
									while(itA.hasNext()){
									//	out.write(itA.next()  + ",");
										currentArea = itA.next();
										customerTrajectory_revisit.write(currentArea  + ",");
										simbolNum++;
									
										
										if(currentArea.compareTo("1f") == 0) {
											firstFloor_time += Double.parseDouble(mapGap_revisit.get(key).get(n));
											firstFloor_total += Double.parseDouble(mapGap_revisit.get(key).get(n));
										}
										else if(currentArea.compareTo("1f-right") == 0){
											firstFloor_right += Double.parseDouble(mapGap_revisit.get(key).get(n));
											firstFloor_total += Double.parseDouble(mapGap_revisit.get(key).get(n));}
										else if(currentArea.compareTo("1f-left") == 0){
											firstFloor_left += Double.parseDouble(mapGap_revisit.get(key).get(n));
											firstFloor_total += Double.parseDouble(mapGap_revisit.get(key).get(n));
										}
										else if(currentArea.compareTo("1f-inner") == 0) {
											firstFloor_inner += Double.parseDouble(mapGap_revisit.get(key).get(n));
											firstFloor_total += Double.parseDouble(mapGap_revisit.get(key).get(n)); }
										else if(currentArea.compareTo("2f-left") == 0) {
											secondFloor_left += Double.parseDouble(mapGap_revisit.get(key).get(n));
											secondFloor_total += Double.parseDouble(mapGap_revisit.get(key).get(n));
										}
										else if(currentArea.compareTo("2f-right") == 0) {
											secondFloor_right += Double.parseDouble(mapGap_revisit.get(key).get(n));
											secondFloor_total += Double.parseDouble(mapGap_revisit.get(key).get(n));
										}
										else if(currentArea.compareTo("2f-inner") == 0) {
											secondFloor_inner += Double.parseDouble(mapGap_revisit.get(key).get(n));
											secondFloor_total += Double.parseDouble(mapGap_revisit.get(key).get(n));
										}
										else if(currentArea.compareTo("2f") == 0) {
											secondFloor_time += Double.parseDouble(mapGap_revisit.get(key).get(n));
											secondFloor_total += Double.parseDouble(mapGap_revisit.get(key).get(n));
										}
										else if(currentArea.compareTo("3f") == 0)
											thirdFloor += Double.parseDouble(mapGap_revisit.get(key).get(n));
										
										n++;
									}
									
									//out.newLine();
									customerTrajectory_revisit.newLine();
									customerTrajectory_revisit.write(",");
								//	out.write(key + ",");
									Iterator<String> itDate = mapDate_revisit.get(key).iterator();
									Iterator<String> itTime = mapTime_revisit.get(key).iterator();
									while(itDate.hasNext()){
									//	out.write(itDate.next()  + ",");
										customerTrajectory_revisit.write(itDate.next()  + ",");
										averageTime += Long.parseLong(itTime.next());
									}
									
									if(simbolNum == 0)
										averageTime = 0;
									else
										averageTime = averageTime/simbolNum;
									String Averagedate = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date (averageTime));
									
									
									Iterator<String> itArea = mapArea_revisit.get(key).iterator();
									int c = 0;
									c = (mapArea_revisit.get(key).size() -1);
									while(c >= 0){
										textout_revisit.write(mapArea_revisit.get(key).get(c) + " -1 ");
										c--;
									}
									textout_revisit.write("-2");
									textout_revisit.newLine();
									
									customerTrajectory_revisit.newLine();
									customerTrajectory_revisit.write(",");
								//	out.newLine();
								//	out.write(key + ",");
									Iterator<String> itGap = mapGap_revisit.get(key).iterator();
									while(itGap.hasNext()){
										String listGap = itGap.next();
										customerTrajectory_revisit.write(listGap + ",");
										totalTime += Double.parseDouble(listGap);
										

										if(Double.parseDouble(listGap) >= 10)
											totalNum++;
									}
				
									out_revisit.write(Double.toString(totalNum) + ",");
									out_revisit.write(Double.toString(totalTime) + ",");
									out_revisit.write(Averagedate + ",");
									out_revisit.write(Long.toString(averageTime/1000) +",");
									out_revisit.write(mapCount_revisit.get(key)+ ",");
									out_revisit.write(mapDuration_revisit.get(key) + ",");
									out_revisit.write(Double.toString(firstFloor_time) + ",");
									out_revisit.write(Double.toString(firstFloor_left)  + ",");
									out_revisit.write(Double.toString(firstFloor_right)  + ",");
									out_revisit.write(Double.toString(firstFloor_inner)  + ",");
									out_revisit.write(Double.toString(secondFloor_time)  + ",");
									out_revisit.write(Double.toString(secondFloor_left)  + ",");
									out_revisit.write(Double.toString(secondFloor_right)  + ",");
									out_revisit.write(Double.toString(secondFloor_inner) + ",");
									out_revisit.write(Double.toString(firstFloor_total) + ",");
									out_revisit.write(Double.toString(secondFloor_total) + ",");
									out_revisit.write(Double.toString(thirdFloor));
									out_revisit.newLine();
									
									customerTrajectory_revisit.newLine();
								
								gap = "0";
								
								mapArea_revisit.remove(key);
								mapTime_revisit.remove(key);
								mapDate_revisit.remove(key);
								mapGap_revisit.remove(key);
								
								
								mapArea_revisit.put(userID, new LinkedList<String>());
								mapTime_revisit.put(userID, new LinkedList<String>());
								mapDate_revisit.put(userID, new LinkedList<String>());
								mapGap_revisit.put(userID, new LinkedList<String>());
								
								mapArea_revisit.get(userID).add(ar);
								mapTime_revisit.get(userID).add(time);
								mapDate_revisit.get(userID).add(date);
								mapGap_revisit.get(userID).add(gap);
				
								
							}
							else {
								mapArea_revisit.get(userID).add(ar);
								mapTime_revisit.get(userID).add(time);
								mapDate_revisit.get(userID).add(date);
								mapGap_revisit.get(userID).add(gap);
								
							}
							
						
						
						
					}
					
					//5번 이상 방문한 고객 정보 
					
					else if(Integer.parseInt(revisit_count) > 3 ){
						if(!mapArea_5visit.containsKey(userID)) {
							mapArea_5visit.put(userID, new LinkedList<String>());
						}


						if(!mapTime_5visit.containsKey(userID)) 
							mapTime_5visit.put(userID, new LinkedList<String>());
							

						if(!mapDate_5visit.containsKey(userID)) 
							mapDate_5visit.put(userID, new LinkedList<String>());
						
						if(!mapGap_5visit.containsKey(userID)) {
							mapGap_5visit.put(userID, new LinkedList<String>());
							gap = "0";
						}
						else{
							size = mapTime_5visit.get(userID).size();
				//			System.out.println(Long.parseLong(time));
							epoch = Long.parseLong(mapTime_5visit.get(userID).get(size-1));
						
							gap = Double.toString((epoch - Long.parseLong(time)) / 1000.0);
							
							if(Double.parseDouble(gap) == 0)
								continue;
							//System.out.println(gap);
						};
						
						if(!mapCount_5visit.containsKey(userID)) {
							mapCount_5visit.put(userID, revisit_count);
						}
						if(!mapDuration_5visit.containsKey(userID)) {
							mapDuration_5visit.put(userID, revisit_duration);
						}
						
						int IndexSize = mapGap_5visit.get(userID).size();
						
						

							if(IndexSize == 1 & ar.compareTo("out") == 0){
								String lastArea = mapArea_5visit.get(userID).get(IndexSize-1);
								
								if(lastArea.compareTo("out")==0) {
								
								mapArea_5visit.remove(userID);
								mapTime_5visit.remove(userID);
								mapDate_5visit.remove(userID);
								mapGap_5visit.remove(userID);
						//		mapTotal.remove(userID);
								
								
								mapArea_5visit.put(userID, new LinkedList<String>());
								mapTime_5visit.put(userID, new LinkedList<String>());
								mapDate_5visit.put(userID, new LinkedList<String>());
								mapGap_5visit.put(userID, new LinkedList<String>());
													
								mapArea_5visit.get(userID).add(ar);
								mapTime_5visit.get(userID).add(time);
								mapDate_5visit.get(userID).add(date);
								mapGap_5visit.get(userID).add(gap);
						//		mapTotal.put(userID, Double.parseDouble(gap));
								
								continue;
								}
								
							}
							
							if(IndexSize >= 1 & Double.parseDouble(gap) < 3600.0){
								
								mapArea_5visit.get(userID).add(ar);
								mapTime_5visit.get(userID).add(time);
								mapDate_5visit.get(userID).add(date);
								mapGap_5visit.get(userID).add(gap);
								
		
							}

							else if(IndexSize > 1 & Double.parseDouble(gap) >= 3600.0  ){
					 
									String key = userID;
									double totalTime = 0.0;
									int simbolNum = 0;
									long averageTime = 0;
									String currentArea = null;
									
									double firstFloor_time = 0;
									double firstFloor_total = 0;
									double firstFloor_left = 0;
									double firstFloor_right = 0;
									double firstFloor_inner = 0;
									
									double secondFloor_time = 0;
									double secondFloor_left = 0;
									double secondFloor_right = 0;
									double secondFloor_inner = 0;
									double secondFloor_total = 0;
									double thirdFloor = 0;
									                                               
							//		customerTrajectory.write(key + ",");
									out_5visit.write(key + ",");
									
									int numberofTra =  (mapArea_5visit.get(key).size() -1 );
									Iterator<String> itA = mapArea_5visit.get(key).iterator();
			
									customerTrajectory_5visit.write(key + ",");
									userName_5visit.write(key);
									userName_5visit.newLine();
									int n = 0;
									int totalNum = 0;
									while(itA.hasNext()){
									//	out.write(itA.next()  + ",");
										currentArea = itA.next();
										customerTrajectory_5visit.write(currentArea  + ",");
										simbolNum++;
								
										if(currentArea.compareTo("1f") == 0) {
											firstFloor_time += Double.parseDouble(mapGap_5visit.get(key).get(n));
											firstFloor_total += Double.parseDouble(mapGap_5visit.get(key).get(n));
										}
										else if(currentArea.compareTo("1f-right") == 0){
											firstFloor_right += Double.parseDouble(mapGap_5visit.get(key).get(n));
											firstFloor_total += Double.parseDouble(mapGap_5visit.get(key).get(n));}
										else if(currentArea.compareTo("1f-left") == 0){
											firstFloor_left += Double.parseDouble(mapGap_5visit.get(key).get(n));
											firstFloor_total += Double.parseDouble(mapGap_5visit.get(key).get(n));
										}
										else if(currentArea.compareTo("1f-inner") == 0) {
											firstFloor_inner += Double.parseDouble(mapGap_5visit.get(key).get(n));
											firstFloor_total += Double.parseDouble(mapGap_5visit.get(key).get(n)); }
										else if(currentArea.compareTo("2f-left") == 0) {
											secondFloor_left += Double.parseDouble(mapGap_5visit.get(key).get(n));
											secondFloor_total += Double.parseDouble(mapGap_5visit.get(key).get(n));
										}
										else if(currentArea.compareTo("2f-right") == 0) {
											secondFloor_right += Double.parseDouble(mapGap_5visit.get(key).get(n));
											secondFloor_total += Double.parseDouble(mapGap_5visit.get(key).get(n));
										}
										else if(currentArea.compareTo("2f-inner") == 0) {
											secondFloor_inner += Double.parseDouble(mapGap_5visit.get(key).get(n));
											secondFloor_total += Double.parseDouble(mapGap_5visit.get(key).get(n));
										}
										else if(currentArea.compareTo("2f") == 0) {
											secondFloor_time += Double.parseDouble(mapGap_5visit.get(key).get(n));
											secondFloor_total += Double.parseDouble(mapGap_5visit.get(key).get(n));
										}
										else if(currentArea.compareTo("3f") == 0)
											thirdFloor += Double.parseDouble(mapGap_5visit.get(key).get(n));
										
										n++;
									}
									
									//out.newLine();
									customerTrajectory_5visit.newLine();
									customerTrajectory_5visit.write(",");
								//	out.write(key + ",");
									Iterator<String> itDate = mapDate_5visit.get(key).iterator();
									Iterator<String> itTime = mapTime_5visit.get(key).iterator();
									while(itDate.hasNext()){
									//	out.write(itDate.next()  + ",");
										customerTrajectory_5visit.write(itDate.next()  + ",");
										averageTime += Long.parseLong(itTime.next());
									}
							
									if(simbolNum == 0)
										averageTime = 0;
									else
										averageTime = averageTime/simbolNum;
									String Averagedate = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date (averageTime));
									
									
									Iterator<String> itArea = mapArea_5visit.get(key).iterator();
									int c = 0;
									c = (mapArea_5visit.get(key).size() -1);
									while(c >= 0){
										textout_5visit.write(mapArea_5visit.get(key).get(c) + " -1 ");
										c--;
									}
									textout_5visit.write("-2");
									textout_5visit.newLine();
									
									customerTrajectory_5visit.newLine();
									customerTrajectory_5visit.write(",");
								//	out.newLine();
								//	out.write(key + ",");
									Iterator<String> itGap = mapGap_5visit.get(key).iterator();
									while(itGap.hasNext()){
										String listGap = itGap.next();
										customerTrajectory_5visit.write(listGap + ",");
										totalTime += Double.parseDouble(listGap);
										
										if(Double.parseDouble(listGap) >= 10)
											totalNum++;
									}
									customerTrajectory_5visit.newLine();
									
									out_5visit.write(Integer.toString(totalNum) + ",");
									out_5visit.write(Double.toString(totalTime) + ",");
									out_5visit.write(Averagedate + ",");
									out_5visit.write(Long.toString(averageTime/1000) + ",");
									out_5visit.write(mapCount_5visit.get(key)+ ",");
									out_5visit.write(mapDuration_5visit.get(key)+ ",");
									out_5visit.write(Double.toString(firstFloor_time) + ",");
									out_5visit.write(Double.toString(firstFloor_left)  + ",");
									out_5visit.write(Double.toString(firstFloor_right)  + ",");
									out_5visit.write(Double.toString(firstFloor_inner)  + ",");
									out_5visit.write(Double.toString(secondFloor_time)  + ",");
									out_5visit.write(Double.toString(secondFloor_left)  + ",");
									out_5visit.write(Double.toString(secondFloor_right)  + ",");
									out_5visit.write(Double.toString(secondFloor_inner) + ",");
									out_5visit.write(Double.toString(firstFloor_total) + ",");
									out_5visit.write(Double.toString(secondFloor_total) + ",");
									out_5visit.write(Double.toString(thirdFloor));
									out_5visit.newLine();
									
				
								gap = "0";
								
								mapArea_5visit.remove(key);
								mapTime_5visit.remove(key);
								mapDate_5visit.remove(key);
								mapGap_5visit.remove(key);
								
								
								mapArea_5visit.put(userID, new LinkedList<String>());
								mapTime_5visit.put(userID, new LinkedList<String>());
								mapDate_5visit.put(userID, new LinkedList<String>());
								mapGap_5visit.put(userID, new LinkedList<String>());
								
								mapArea_5visit.get(userID).add(ar);
								mapTime_5visit.get(userID).add(time);
								mapDate_5visit.get(userID).add(date);
								mapGap_5visit.get(userID).add(gap);
				
								
							}
							else {
								mapArea_5visit.get(userID).add(ar);
								mapTime_5visit.get(userID).add(time);
								mapDate_5visit.get(userID).add(date);
								mapGap_5visit.get(userID).add(gap);
								
							}
							
						
						
					}

	
			


				}
				
	
					
					
				
				Iterator<String> keys = mapArea_firstVisit.keySet().iterator();
		
				while (keys.hasNext()) {
					

				
					
					
					double totalTime = 0.0;
					int simbolNum = 0;
					long averageTime = 0;
					String currentArea = null;
					int totalNum = 0;
					String key = keys.next();
				/*	int nume = mapArea.get(key).size();
					if(nume<=1)
						continue;*/
					
					int elementSize = mapArea_firstVisit.get(key).size();
					
					if(elementSize>2){
					
					int n = 0;
					double firstFloor_time = 0;
					double firstFloor_total = 0;
					double firstFloor_left = 0;
					double firstFloor_right = 0;
					double firstFloor_inner = 0;
					
					double secondFloor_time = 0;
					double secondFloor_left = 0;
					double secondFloor_right = 0;
					double secondFloor_inner = 0;
					double secondFloor_total = 0;
					double thirdFloor = 0;
					//System.out.println(key);
				//	customerTrajectory.write(key + ",");
					out_firstVisit.write(key + ",");
					Iterator<String> itA = mapArea_firstVisit.get(key).iterator();
					customerTrajectory_firstVisit.write(key + ",");
			//		userName.write(key+",");
					while(itA.hasNext()){
					//	out.write(itA.next()  + ",");
						currentArea = itA.next();
						customerTrajectory_firstVisit.write(currentArea  + ",");
						simbolNum++;
				
						
						if(currentArea.compareTo("1f") == 0) {
							firstFloor_time += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
							firstFloor_total += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
						}
						else if(currentArea.compareTo("1f-right") == 0){
							firstFloor_right += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
							firstFloor_total += Double.parseDouble(mapGap_firstVisit.get(key).get(n));}
						else if(currentArea.compareTo("1f-left") == 0){
							firstFloor_left += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
							firstFloor_total += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
						}
						else if(currentArea.compareTo("1f-inner") == 0) {
							firstFloor_inner += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
							firstFloor_total += Double.parseDouble(mapGap_firstVisit.get(key).get(n)); }
						else if(currentArea.compareTo("2f-left") == 0) {
							secondFloor_left += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
							secondFloor_total += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
						}
						else if(currentArea.compareTo("2f-right") == 0) {
							secondFloor_right += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
							secondFloor_total += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
						}
						else if(currentArea.compareTo("2f-inner") == 0) {
							secondFloor_inner += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
							secondFloor_total += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
						}
						else if(currentArea.compareTo("2f") == 0) {
							secondFloor_time += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
							secondFloor_total += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
						}
						else if(currentArea.compareTo("3f") == 0)
							thirdFloor += Double.parseDouble(mapGap_firstVisit.get(key).get(n));
						
						n++;
					}
					
					//out.newLine();
					customerTrajectory_firstVisit.newLine();
					customerTrajectory_firstVisit.write(",");
				//	out.write(key + ",");
					Iterator<String> itDate = mapDate_firstVisit.get(key).iterator();
					Iterator<String> itTime = mapTime_firstVisit.get(key).iterator();
					while(itDate.hasNext()){
					//	out.write(itDate.next()  + ",");
						customerTrajectory_firstVisit.write(itDate.next()  + ",");
						averageTime += Long.parseLong(itTime.next());
					}
					if(simbolNum == 0)
						averageTime = 0;
					else
						averageTime = averageTime/simbolNum;
					
					String Averagedate = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date (averageTime));

				//	customerTrajectory.write(Averagedate);
					
					customerTrajectory_firstVisit.newLine();
					customerTrajectory_firstVisit.write(",");
				//	out.newLine();
				//	out.write(key + ",");
					Iterator<String> itGap = mapGap_firstVisit.get(key).iterator();
					while(itGap.hasNext()){
						String listGap = itGap.next();
						customerTrajectory_firstVisit.write(listGap + ",");
						totalTime += Double.parseDouble(listGap);
						
						if(Double.parseDouble(listGap) >= 10)
							totalNum++;
					}
	
					
					int c = 0;
					c = (mapArea_firstVisit.get(key).size() -1);
					while(c >= 0){
						textout_firstVisit.write(mapArea_firstVisit.get(key).get(c) + " -1 ");
						c--;
					}
					textout_firstVisit.write("-2");
					textout_firstVisit.newLine();
				
					out_firstVisit.write(Integer.toString(totalNum) + ",");
					out_firstVisit.write(Double.toString(totalTime) + ",");
					out_firstVisit.write(Averagedate + ",");
					out_firstVisit.write(Long.toString(averageTime/1000) + ",");
					out_firstVisit.write(mapCount_firstVisit.get(key)+ ",");
					out_firstVisit.write(mapDuration_firstVisit.get(key) + ",");
					out_firstVisit.write(Double.toString(firstFloor_time) + ",");
					out_firstVisit.write(Double.toString(firstFloor_left)  + ",");
					out_firstVisit.write(Double.toString(firstFloor_right)  + ",");
					out_firstVisit.write(Double.toString(firstFloor_inner)  + ",");
					out_firstVisit.write(Double.toString(secondFloor_time)  + ",");
					out_firstVisit.write(Double.toString(secondFloor_left)  + ",");
					out_firstVisit.write(Double.toString(secondFloor_right)  + ",");
					out_firstVisit.write(Double.toString(secondFloor_inner) + ",");
					out_firstVisit.write(Double.toString(firstFloor_total) + ",");
					out_firstVisit.write(Double.toString(secondFloor_total) + ",");
					out_firstVisit.write(Double.toString(thirdFloor));
					out_firstVisit.newLine();
					
				//	customerTrajectory.write(Double.toString(totalTime));
				//	out.newLine();
					customerTrajectory_firstVisit.newLine();
				
				}
					
					
				
				}

				
				keys = mapArea_revisit.keySet().iterator();
				
				while (keys.hasNext()) {
					

					double firstFloor_time = 0;
					double firstFloor_total = 0;
					double firstFloor_left = 0;
					double firstFloor_right = 0;
					double firstFloor_inner = 0;
					
					double secondFloor_time = 0;
					double secondFloor_left = 0;
					double secondFloor_right = 0;
					double secondFloor_inner = 0;
					double secondFloor_total = 0;
					double thirdFloor = 0;
					
					
					double totalTime = 0.0;
					int simbolNum = 0;
					long averageTime = 0;
					String currentArea = null;
					int totalNum = 0;
					String key = keys.next();
				/*	int nume = mapArea.get(key).size();
					if(nume<=1)
						continue;*/
					
					int elementSize = mapArea_revisit.get(key).size();
					
					if(elementSize>2){
					
					userName_revisit.write(key);
					userName_revisit.newLine();
					//System.out.println(key);
				//	customerTrajectory.write(key + ",");
					out_revisit.write(key + ",");
					int n = 0;
					Iterator<String> itA = mapArea_revisit.get(key).iterator();
					customerTrajectory_revisit.write(key + ",");
			//		userName.write(key+",");
					while(itA.hasNext()){
					//	out.write(itA.next()  + ",");
						currentArea = itA.next();
						customerTrajectory_revisit.write(currentArea  + ",");
						simbolNum++;
	
						
						if(currentArea.compareTo("1f") == 0) {
							firstFloor_time += Double.parseDouble(mapGap_revisit.get(key).get(n));
							firstFloor_total += Double.parseDouble(mapGap_revisit.get(key).get(n));
						}
						else if(currentArea.compareTo("1f-right") == 0){
							firstFloor_right += Double.parseDouble(mapGap_revisit.get(key).get(n));
							firstFloor_total += Double.parseDouble(mapGap_revisit.get(key).get(n));}
						else if(currentArea.compareTo("1f-left") == 0){
							firstFloor_left += Double.parseDouble(mapGap_revisit.get(key).get(n));
							firstFloor_total += Double.parseDouble(mapGap_revisit.get(key).get(n));
						}
						else if(currentArea.compareTo("1f-inner") == 0) {
							firstFloor_inner += Double.parseDouble(mapGap_revisit.get(key).get(n));
							firstFloor_total += Double.parseDouble(mapGap_revisit.get(key).get(n)); }
						else if(currentArea.compareTo("2f-left") == 0) {
							secondFloor_left += Double.parseDouble(mapGap_revisit.get(key).get(n));
							secondFloor_total += Double.parseDouble(mapGap_revisit.get(key).get(n));
						}
						else if(currentArea.compareTo("2f-right") == 0) {
							secondFloor_right += Double.parseDouble(mapGap_revisit.get(key).get(n));
							secondFloor_total += Double.parseDouble(mapGap_revisit.get(key).get(n));
						}
						else if(currentArea.compareTo("2f-inner") == 0) {
							secondFloor_inner += Double.parseDouble(mapGap_revisit.get(key).get(n));
							secondFloor_total += Double.parseDouble(mapGap_revisit.get(key).get(n));
						}
						else if(currentArea.compareTo("2f") == 0) {
							secondFloor_time += Double.parseDouble(mapGap_revisit.get(key).get(n));
							secondFloor_total += Double.parseDouble(mapGap_revisit.get(key).get(n));
						}
						else if(currentArea.compareTo("3f") == 0)
							thirdFloor += Double.parseDouble(mapGap_revisit.get(key).get(n));
						
						n++;
					}
		
					//out.newLine();
					customerTrajectory_revisit.newLine();
					customerTrajectory_revisit.write(",");
				//	out.write(key + ",");
					Iterator<String> itDate = mapDate_revisit.get(key).iterator();
					Iterator<String> itTime = mapTime_revisit.get(key).iterator();
					while(itDate.hasNext()){
					//	out.write(itDate.next()  + ",");
						customerTrajectory_revisit.write(itDate.next()  + ",");
						averageTime += Long.parseLong(itTime.next());
					}
					if(simbolNum == 0)
						averageTime = 0;
					else
						averageTime = averageTime/simbolNum;
					
					String Averagedate = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date (averageTime));

				//	customerTrajectory.write(Averagedate);
					
					customerTrajectory_revisit.newLine();
					customerTrajectory_revisit.write(",");
				//	out.newLine();
				//	out.write(key + ",");
					Iterator<String> itGap = mapGap_revisit.get(key).iterator();
			
					while(itGap.hasNext()){
						String listGap = itGap.next();
						customerTrajectory_revisit.write(listGap + ",");
						totalTime += Double.parseDouble(listGap);
						
						if(Double.parseDouble(listGap) >= 10)
							totalNum++;
					}
		
					int c = 0;
					c = (mapArea_revisit.get(key).size() -1);
					while(c >= 0){
						textout_revisit.write(mapArea_revisit.get(key).get(c) + " -1 ");
						c--;
					}
					textout_revisit.write("-2");
					textout_revisit.newLine();
				
					out_revisit.write(Integer.toString(totalNum) + ",");
					out_revisit.write(Double.toString(totalTime) + ",");
					out_revisit.write(Averagedate + ",");
					out_revisit.write(Long.toString(averageTime/1000)+ ",");
					out_revisit.write(mapCount_revisit.get(key)+ ",");
					out_revisit.write(mapDuration_revisit.get(key) + ",");
					out_revisit.write(Double.toString(firstFloor_time) + ",");
					out_revisit.write(Double.toString(firstFloor_left)  + ",");
					out_revisit.write(Double.toString(firstFloor_right)  + ",");
					out_revisit.write(Double.toString(firstFloor_inner)  + ",");
					out_revisit.write(Double.toString(secondFloor_time)  + ",");
					out_revisit.write(Double.toString(secondFloor_left)  + ",");
					out_revisit.write(Double.toString(secondFloor_right)  + ",");
					out_revisit.write(Double.toString(secondFloor_inner) + ",");
					out_revisit.write(Double.toString(firstFloor_total) + ",");
					out_revisit.write(Double.toString(secondFloor_total) + ",");
					out_revisit.write(Double.toString(thirdFloor));
					out_revisit.newLine();
					
				//	customerTrajectory.write(Double.toString(totalTime));
				//	out.newLine();
					customerTrajectory_revisit.newLine();
				
				}
				
				}
				
				
				keys = mapArea_5visit.keySet().iterator();
				
				while (keys.hasNext()) {
					

					double firstFloor_time = 0;
					double firstFloor_total = 0;
					double firstFloor_left = 0;
					double firstFloor_right = 0;
					double firstFloor_inner = 0;
					
					double secondFloor_time = 0;
					double secondFloor_left = 0;
					double secondFloor_right = 0;
					double secondFloor_inner = 0;
					double secondFloor_total = 0;
					double thirdFloor = 0;
					
					
					double totalTime = 0.0;
					int simbolNum = 0;
					long averageTime = 0;
					String currentArea = null;
					int totalNum = 0;
					String key = keys.next();
				/*	int nume = mapArea.get(key).size();
					if(nume<=1)
						continue;*/
					int n = 0;
					int elementSize = mapArea_5visit.get(key).size();
					
					if(elementSize>2){
					
					
					//System.out.println(key);
				//	customerTrajectory.write(key + ",");
					out_5visit.write(key + ",");
					Iterator<String> itA = mapArea_5visit.get(key).iterator();
					customerTrajectory_5visit.write(key + ",");
					userName_5visit.write(key);
					userName_5visit.newLine();
					while(itA.hasNext()){
					//	out.write(itA.next()  + ",");
						currentArea = itA.next();
						customerTrajectory_5visit.write(currentArea  + ",");
						simbolNum++;
		
						
						
						if(currentArea.compareTo("1f") == 0) {
							firstFloor_time += Double.parseDouble(mapGap_5visit.get(key).get(n));
							firstFloor_total += Double.parseDouble(mapGap_5visit.get(key).get(n));
						}
						else if(currentArea.compareTo("1f-right") == 0){
							firstFloor_right += Double.parseDouble(mapGap_5visit.get(key).get(n));
							firstFloor_total += Double.parseDouble(mapGap_5visit.get(key).get(n));}
						else if(currentArea.compareTo("1f-left") == 0){
							firstFloor_left += Double.parseDouble(mapGap_5visit.get(key).get(n));
							firstFloor_total += Double.parseDouble(mapGap_5visit.get(key).get(n));
						}
						else if(currentArea.compareTo("1f-inner") == 0) {
							firstFloor_inner += Double.parseDouble(mapGap_5visit.get(key).get(n));
							firstFloor_total += Double.parseDouble(mapGap_5visit.get(key).get(n)); }
						else if(currentArea.compareTo("2f-left") == 0) {
							secondFloor_left += Double.parseDouble(mapGap_5visit.get(key).get(n));
							secondFloor_total +=Double.parseDouble(mapGap_5visit.get(key).get(n));
						}
						else if(currentArea.compareTo("2f-right") == 0) {
							secondFloor_right += Double.parseDouble(mapGap_5visit.get(key).get(n));
							secondFloor_total +=Double.parseDouble(mapGap_5visit.get(key).get(n));
						}
						else if(currentArea.compareTo("2f-inner") == 0) {
							secondFloor_inner += Double.parseDouble(mapGap_5visit.get(key).get(n));
							secondFloor_total += Double.parseDouble(mapGap_5visit.get(key).get(n));
						}
						else if(currentArea.compareTo("2f") == 0) {
							secondFloor_time += Double.parseDouble(mapGap_5visit.get(key).get(n));
							secondFloor_total += Double.parseDouble(mapGap_5visit.get(key).get(n));
						}
						else if(currentArea.compareTo("3f") == 0)
							thirdFloor += Double.parseDouble(mapGap_5visit.get(key).get(n));
						
						n++;
					}
	
					//out.newLine();
					customerTrajectory_5visit.newLine();
					customerTrajectory_5visit.write(",");
				//	out.write(key + ",");
					Iterator<String> itDate = mapDate_5visit.get(key).iterator();
					Iterator<String> itTime = mapTime_5visit.get(key).iterator();
					while(itDate.hasNext()){
					//	out.write(itDate.next()  + ",");
						customerTrajectory_5visit.write(itDate.next()  + ",");
						averageTime += Long.parseLong(itTime.next());
					}
					if(simbolNum == 0)
						averageTime = 0;
					else
						averageTime = averageTime/simbolNum;
					
					String Averagedate = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date (averageTime));

				//	customerTrajectory.write(Averagedate);
					
					customerTrajectory_5visit.newLine();
					customerTrajectory_5visit.write(",");
				//	out.newLine();
				//	out.write(key + ",");
					Iterator<String> itGap = mapGap_5visit.get(key).iterator();
			
					while(itGap.hasNext()){
						String listGap = itGap.next();
						customerTrajectory_5visit.write(listGap + ",");
						totalTime += Double.parseDouble(listGap);
						
						if(Double.parseDouble(listGap) >= 10)
							totalNum++;
					}
		
					int c = 0;
					c = (mapArea_5visit.get(key).size() -1);
					while(c >= 0){
						textout_5visit.write(mapArea_5visit.get(key).get(c) + " -1 ");
						c--;
					}
					textout_5visit.write("-2");
					textout_5visit.newLine();
				
					out_5visit.write(Integer.toString(totalNum) + ",");
					out_5visit.write(Double.toString(totalTime) + ",");
					out_5visit.write(Averagedate + ",");
					out_5visit.write(Long.toString(averageTime/1000)+ ",");
					out_5visit.write(mapCount_5visit.get(key)+ ",");
					out_5visit.write(mapDuration_5visit.get(key) + ",");
					out_5visit.write(Double.toString(firstFloor_time) + ",");
					out_5visit.write(Double.toString(firstFloor_left)  + ",");
					out_5visit.write(Double.toString(firstFloor_right)  + ",");
					out_5visit.write(Double.toString(firstFloor_inner)  + ",");
					out_5visit.write(Double.toString(secondFloor_time)  + ",");
					out_5visit.write(Double.toString(secondFloor_left)  + ",");
					out_5visit.write(Double.toString(secondFloor_right)  + ",");
					out_5visit.write(Double.toString(secondFloor_inner) + ",");
					out_5visit.write(Double.toString(firstFloor_total) + ",");
					out_5visit.write(Double.toString(secondFloor_total) + ",");
					out_5visit.write(Double.toString(thirdFloor));
					out_5visit.newLine();
					
				//	customerTrajectory.write(Double.toString(totalTime));
				//	out.newLine();
					customerTrajectory_5visit.newLine();
				
				}
				
				}


		
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			
			
			  
			
		}
		textout_firstVisit.close();
		out_firstVisit.close();
		customerTrajectory_firstVisit.close();
		//userName.close();
		
		textout_revisit.close();
		out_revisit.close();
		customerTrajectory_revisit.close();
		
		textout_5visit.close();
		out_5visit.close();
		customerTrajectory_5visit.close();
		
		
		userName_revisit.close();
		userName_5visit.close();
		System.out.println("END");
				

    }

}
