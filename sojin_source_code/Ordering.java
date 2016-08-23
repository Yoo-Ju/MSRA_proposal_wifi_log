package colon;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

public class Ordering {

	public static void main(String[] args) throws IOException{
		
		
		CSVReader reader = new CSVReader(new FileReader("CM_CustomerTrjactoryFile_0604.csv"));

	    String[] contents;
	    
	    BufferedWriter out = new BufferedWriter(new FileWriter("CM_grouping_0604.csv"));
	    
		while((contents = reader.readNext()) != null){
			
			//궤적 길이가 5이상인 사람들은 pass
			if(contents.length < 12)
				continue;
			
			int i = 1;
			
			//out, in 순으로 로그가 찍힌 사람들만 고려
			while(i+2 < contents.length){
				if(contents[i].compareTo("out") == 0 & contents[i+2].compareTo("in") == 0) {
					out.write(contents[0] + ",");
					System.out.println(contents[0] + ",");
					for(; i<contents.length;i++){
						out.write(contents[i] + ",");
						if(i+3<contents.length){
							if(contents[i+1].compareTo("out") == 0 & contents[i+3].compareTo("in") == 0){
								out.newLine();
								out.write(contents[0] + ",");
							}
						}
							
					}
					
					out.newLine();
			
			
				}
				else 
					i = i+2;
				
			}
			
			
			
		}
		
		out.close();
		reader.close();
		
		
	}
}
