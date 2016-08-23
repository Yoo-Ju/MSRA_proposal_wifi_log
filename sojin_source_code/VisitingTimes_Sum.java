package colon;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

public class VisitingTimes_Sum {
	public static void main(String[] args) throws IOException{
		
		BufferedWriter out =  new BufferedWriter(new FileWriter("CM_비교2.csv"));
		
		//요일,시간별 고객 수
		int[][] low = new int[7][24];
		int[][] high = new int[7][24];
		int[][] first  = new int[7][24];

		//요일,시간별 고객 체류 시간 
		double[][] first_duration = new double[7][24];
		double[][] low_duration = new double[7][24];
		double[][] high_duration = new double[7][24];
		
		//요일, 시간별 고객 stay point 개수
		int[][] first_tra = new int[7][24];
		int[][] low_tra = new int[7][24];
		int[][] high_tra = new int[7][24];
		
		//재방문 기간
		int[][] low_period = new int[7][24];
		int[][] high_period = new int[7][24];
		
		//초기화
		for(int i = 0; i< 7; i++){
			for(int j = 0; j<24; j++){
				low[i][j]=0;
				high[i][j]=0;
				first[i][j]=0;
				
				first_duration[i][j]=0;
				low_duration[i][j] = 0;
				high_duration[i][j] = 0;
				
				first_tra[i][j]=0;
				low_tra[i][j]=0;
				high_tra[i][j]=0;

				low_period[i][j]=0;
				high_period[i][j]=0;
			}
			
			
		}
		
		//1~2번, 3~4번, 5번 방문한 사람의 csv파일을 각각 읽어들임
		CSVReader reader_first = new CSVReader(new FileReader("TotalResult_firstVisit_CM.csv"));
		CSVReader reader_revisit = new CSVReader(new FileReader("TotalResult_rivisit_CM.csv"));
		CSVReader reader_5visit = new CSVReader(new FileReader("TotalResult_5visit_CM.csv"));

	    String[] contents;
	    
	    int standard = 1;
	    int current_day = 1;
	    
	    //1~2번 매장 방문한 고객
	    while((contents = reader_first.readNext()) != null){
	    	
	    	String date = contents[3];
	    	int count = Integer.parseInt(contents[1]);
	    	double duration = Double.parseDouble(contents[2]);
	    	
	    	//매장 내 3분이내로 머무르고 2시간 이상 머무른 사람은 제외
	    	if(duration > 7200 | duration <180)
	    		continue;
	    	
			String[] arr = date.split(" ");
			String[] arr1 = arr[0].split("-");
			String [] arr2 = arr[1].split(":");
			
	//		System.out.println(arr1);
	//		System.out.println(duration);
			
			//현재 날짜와 시간을 저장
			int day = Integer.parseInt(arr1[2]);
			int time = Integer.parseInt(arr2[0]);
			
			
			if(current_day != day){
				//이전 날짜로부터 며칠이나 지났는지 체크
				if( day != 1 & (day <= 31) & day-current_day > 1)
					standard += (day-current_day - 1);
				
				current_day = day;
				standard++;
				standard = standard % 7; //요일 변경
				
				System.out.println(date);
				System.out.println(standard);
			}

			//요일별 고객 수, stay point 개수, 머뭄른 시간 증가 
			first[standard][time]++;
			first_tra[standard][time]  += count;
			first_duration[standard][time] += duration;
	    	

	    }
	    
	    
	    System.out.println("************************");
	    standard = 1;
	    current_day = 1;
	    
	    //3~4번 방문한 고객 대상으로 내용은 위와 동일한 기능 수행
	    while((contents = reader_revisit.readNext()) != null){
	    	
	    	String date = contents[3];
	    	int count = Integer.parseInt(contents[1]);
	    	int period = Integer.parseInt(contents[6]); //고객이 지난 방문으로부터 며칠 뒤 재방문했는지 나타냄
	    	int revisit = Integer.parseInt(contents[5]);
	    	double duration = Double.parseDouble(contents[2]);
	    	
			String[] arr = date.split(" ");
			String[] arr1 = arr[0].split("-");
			String [] arr2 = arr[1].split(":");
			
	//		System.out.println(duration);
			int day = Integer.parseInt(arr1[2]);
			int time = Integer.parseInt(arr2[0]);
			
			if(duration > 7200 | duration <180)
	    		continue;
			
			if(current_day != day){
				if(day != 1 & (day <= 31) & day-current_day > 1)
					standard += (day-current_day - 1);
				
				current_day = day;
				standard++;
				standard = standard % 7;
				
				System.out.println(date);
				System.out.println(standard);
			}
			
			low[standard][time]++;
			low_duration[standard][time] += duration;
			low_tra[standard][time] += count;
			low_period[standard][time] += period;
	
	    }
	    
	    System.out.println("************************");
	    standard = 1;
	    current_day = 1;
	    
	    //5번 이상 방문한 고객
	    while((contents = reader_5visit.readNext()) != null){
	    	
	    	String date = contents[3];
	    	int count = Integer.parseInt(contents[1]);
	    	int period = Integer.parseInt(contents[6]);
	    	int revisit = Integer.parseInt(contents[5]);
	    	double duration = Double.parseDouble(contents[2]);
	    	
	    	
	    	if(duration > 7200 | duration <180)
	    		continue;
	    	
	//    	System.out.println(date);
	    	
			String[] arr = date.split(" ");
			String[] arr1 = arr[0].split("-");
			String [] arr2 = arr[1].split(":");
			
	//		System.out.println(duration);
			int day = Integer.parseInt(arr1[2]);
			int time = Integer.parseInt(arr2[0]);
			
			
			if(current_day != day){
				if(day != 1 & (day <= 31) & day-current_day > 1)
					standard += (day-current_day - 1);
				
				current_day = day;
				standard++;
				standard = standard % 7;
				
				System.out.println(date);
				System.out.println(standard);
			}
			
			high[standard][time]++;
			high_duration[standard][time] += duration;
			high_tra[standard][time] += count;
			high_period[standard][time] += period;
	
	    }
	    
	    
	    
	    //각 방문 횟수별 고객의 수, stay point 개수, 체류 시간 프린트 
		for(int i = 0; i< 7; i++){
			for(int j = 0; j<24; j++){
				out.write(Integer.toString(first[i][j])+ ",");				
				
			}
			out.newLine();
		}
		out.newLine();
		

		for(int i = 0; i< 7; i++){
			for(int j = 0; j<24; j++){
				out.write(first_tra[i][j] / (first[i][j] * 1.0) + ",");				
				
			}
			out.newLine();
		}
		out.newLine();
		

		for(int i = 0; i< 7; i++){
			for(int j = 0; j<24; j++){
				out.write(first_duration[i][j] / (first[i][j] * 1.0)+ ",");				
				
			}
			out.newLine();
		}
		out.newLine();
		
		out.write("3~4 times");
		
    	out.newLine();
			for(int i = 0; i< 7; i++){
				for(int j = 0; j<24; j++){
					out.write(Integer.toString(low[i][j])+ ",");				
					
				}
				out.newLine();
			}
			out.newLine();
			
			for(int i = 0; i< 7; i++){
				for(int j = 0; j<24; j++){
					out.write(low_tra[i][j] / (low[i][j] * 1.0)+ ",");				
					
				}
				out.newLine();
			}
	    	out.newLine();
	    
			for(int i = 0; i< 7; i++){
				for(int j = 0; j<24; j++){
					out.write(low_duration[i][j] / (low[i][j] * 1.0)+ ",");				
					
				}
				out.newLine();
			}
	    	out.newLine();
	    
			for(int i = 0; i< 7; i++){
				for(int j = 0; j<24; j++){
					out.write(low_period[i][j] / (low[i][j] * 1.0)+ ",");				
					
				}
				out.newLine();
			}
	    	out.newLine();
	    
	    	out.write("more than 5 times ");
	    	out.newLine();
	    	
			for(int i = 0; i < 7; i++){
				for(int j = 0; j<24; j++){
					out.write(high[i][j]+ ",");				
				}
				out.newLine();
			}
			out.newLine();
			
			for(int i = 0; i< 7; i++){
				for(int j = 0; j<24; j++){
					out.write(high_tra[i][j] / (high[i][j] * 1.0)+ ",");				
					
				}
				out.newLine();
			}
	    	out.newLine();
	    
			for(int i = 0; i< 7; i++){
				for(int j = 0; j<24; j++){
					out.write(high_duration[i][j] / (high[i][j] * 1.0)+ ",");				
					
				}
				out.newLine();
			}
	    	out.newLine();
	    
			for(int i = 0; i< 7; i++){
				for(int j = 0; j<24; j++){
					out.write(high_period[i][j] / (high[i][j] * 1.0)+ ",");				
					
				}
				out.newLine();
			}
	    	out.newLine();
			
			out.close();
	    	
	}


}
