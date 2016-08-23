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

		 		outSider[z]=0; //���� �� �� ���
		 		insider[z] = 0; //���� �� ������ ���� ���
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
	
			customerNum.write("����" + ",");
			customerNum.write("��" + ",");
			customerNum.write("��" + ",");
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
			 	
			 	//���� �����͸� �ϳ��� �о����
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
						String ar = nextLine[2]; //���� ����
						String time = nextLine[3]; //���� �ð�(epoch time ����)
						String date = nextLine[4]; //����  �ð�(human readable)
						
					
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
						int currentTime = Integer.parseInt(arr2[0]); //���� ��(hour) 
						
			
						
						//���� ���� ��ġ�� out�̰� ���� ���� ������ hashmap�� ���ٸ� �� ������ �����ϰ�  ������ ���� ����
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
							
							
							//���� ��ġ�� out�� ���� ���� �����Ͱ� �����ߴٸ� �� ���� �ϳ� ������Ŵ
							
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
							
							//���� ���� ��ġ�� out�� �ƴ϶�� �ϴ� �ش� �����͸� ����

				
								
							mapArea.get(userID).add(ar);
							//System.out.println(ar);
							mapTime.get(userID).add(arr[0]);
							mapDate.get(userID).add(date);
			
						}
				
					}
					
		
						
						

					i++;
					for(int q = 0; q < 24; q++){
						//���� ���� �ð�
						if(q<10 | q>21)
							continue;
							
						customerNum.write(f2+",");
						customerNum.write(q+ ",");
						customerNum.write(Integer.toString(outSider[q]) + ","); //���� ��
						customerNum.write(Integer.toString(insider[q])+ ","); // �� �� 
						customerNum.write(Double.toString(insider[q]/(outSider[q] * 1.0))); // ���� ��� �� ��
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
