package colon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

public class CustomerCounting {
	
	
	 public static void main( String[] args ) throws IOException, ParseException
	    {
		 	int[] customer = new int[24];
		 	int[] outSider = new int[24];
		 	int[] insider = new int[24];
		 	for(int z = 0; z<24; z++){

		 		outSider[z]=0; //행인 및 고객 계수
		 		insider[z] = 0; //매장 내 입장한 고객만 계수
		 	}
		 	
		 	int total = 0;
		 	BufferedWriter customerNum= null;
		 	
			 try {
				 customerNum = new BufferedWriter(new FileWriter("customerNum_CM.csv"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 

			 
			customerNum.write("Day" + ",");
			customerNum.write("Time" + ",");
	
			customerNum.write("행인" + ",");
			customerNum.write("고객" + ",");
			customerNum.write("비" + ",");
			customerNum.newLine();
			
	    	String standard_hour = "0";

			String filePath;


			BufferedReader in;

			int i = 0;

			int week_time = 0;
			int CurrentDate = 29;

		//	String gap = "0";

			long epoch;
			
			in = new BufferedReader(new FileReader("list_CM.txt"));
			
			
			
			while((filePath = in.readLine()) != null){
				System.out.println(filePath);
				String[] fileName = filePath.split("_");
				//System.out.println(fileName[2]);
				String f2= fileName[3].substring(0, 8);
			//	System.out.println(f2);
				
				Map<String, List<String>> mapArea = new LinkedHashMap<String, List<String>>();
				Map<String, List<String>> mapTime = new LinkedHashMap<String, List<String>>();
				Map<String, List<String>> mapDate = new LinkedHashMap<String, List<String>>();
		
				Map<String, List<String>> mapPedestrian = new LinkedHashMap<String, List<String>>();
				Map<String, List<String>> mapCustomer = new LinkedHashMap<String, List<String>>();

				
			 	for(int z = 0; z<24; z++){
			 		
			 		outSider[z]=0;
			 		insider[z] = 0;
			 	}
			 	
			 	//고객의 데이터를 하나씩 읽어들임
				try {

					CSVReader reader = new CSVReader(new FileReader(filePath));

				    String[] nextLine;
				    
				    
				       

					while ((nextLine = reader.readNext()) != null){
						String userID = nextLine[0];
						String deny = nextLine[7];
						
						
						if(deny.compareTo("true") == 0){
							continue;
						}
						
						if(userID.compareTo("Device_ID") == 0)
							continue;
						
						total++;
						String ar = nextLine[2]; //현재 구역
						String time = nextLine[3]; //현재 시간(epoch time 형태)
						String date = nextLine[4]; //현재  시간(human readable)
						
					
						int hour = 0;
						

			
						if(!mapArea.containsKey(userID)) {
							mapArea.put(userID, new LinkedList<String>());
						}


						if(!mapTime.containsKey(userID)) 
							mapTime.put(userID, new LinkedList<String>());
							

						if(!mapDate.containsKey(userID)) 
							mapDate.put(userID, new LinkedList<String>());
						
						if(!mapPedestrian.containsKey(userID)) {								
							mapPedestrian.put(userID, new LinkedList<String>());

						}
						
						

						String[] arr = date.split(" ");
				//		String [] arr2 = arr[0].split("/");
						String [] arr2 = arr[1].split(":");
						int currentTime = Integer.parseInt(arr2[0]); //현재 시(hour) 
						
			
						
						//고객의 현재 위치가 out이고 아직 고객의 정보가 hashmap에 없다면 고객 정보를 저장하고  행인의 수를 증가
						if(!mapPedestrian.get(userID).contains(arr[0]) & ar.compareTo("out") == 0 ){
								
							mapPedestrian.get(userID).add(arr[0]);
							outSider[currentTime] ++;
						}
						int size = mapArea.get(userID).size();
						

						String key = userID;
						
						
						if(ar.compareTo("out") == 0 & (size == 0 )){
						
						
							mapArea.remove(key);
							mapTime.remove(key);
							mapDate.remove(key);
						//	mapGap.remove(key);
							
	
							
						}
						else if(ar.compareTo("out") == 0 & (size > 1)){
					//		System.out.println(size);
					//		inSider[h] ++;
							
							String first_element = mapDate.get(userID).get(0);
							String fe[] = first_element.split(" ");
							
						
					//		System.out.println(first_element);
			
							
							if(arr[1].compareTo(fe[1]) == 0){
								System.out.println(first_element);
								System.out.println(date);
								continue;
							}
								

							if(!mapCustomer.containsKey(userID)) {								
								mapCustomer.put(userID, new LinkedList<String>());
								
							}
							
							
							//현재 위치가 out인 고객이 이전 데이터가 존재했다면 고객 수를 하나 증가시킴
							
							if(!mapCustomer.get(userID).contains(arr[0])){
								
								int lastIndex = mapTime.get(userID).lastIndexOf(arr[0]);
							//	System.out.println(lastIndex);
								mapCustomer.get(userID).add(arr[0]);
								insider[currentTime] ++;

	
							}
							
						

							mapArea.get(userID).add(ar);
							mapTime.get(userID).add(arr[0]);
							mapDate.get(userID).add(date);

							
							mapArea.remove(key);
							mapTime.remove(key);
							mapDate.remove(key);
						}
						else {
							
							//고객의 현재 위치가 out이 아니라면 일단 해당 데이터를 저장

				
								
							mapArea.get(userID).add(ar);
							//System.out.println(ar);
							mapTime.get(userID).add(arr[0]);
							mapDate.get(userID).add(date);
			
						}
				
					}
					
		
						
						

					i++;
					for(int q = 0; q < 24; q++){
						//매장 오픈 시간
						if(q<10 | q>21)
							continue;
							
						customerNum.write(f2+",");
						customerNum.write(q+ ",");
						customerNum.write(Integer.toString(outSider[q]) + ","); //행인 수
						customerNum.write(Integer.toString(insider[q])+ ","); // 고객 수 
						customerNum.write(Double.toString(insider[q]/(outSider[q] * 1.0))); // 행인 대비 고객 비
						customerNum.newLine();
					}
					
					
		
				
			
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		
			}
			
			
			System.out.println("Total : " + total);
			
			System.out.println("Total customer during the time period");
			customerNum.close();


	    }

}
