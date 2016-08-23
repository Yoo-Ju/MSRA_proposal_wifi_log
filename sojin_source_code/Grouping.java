package colon;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;

import au.com.bytecode.opencsv.CSVReader;

public class Grouping {
	
	public static void main(String[] args) throws IOException, ParseException{
		
		CSVReader reader = new CSVReader(new FileReader("CM_grouping_0604.csv"));

	    String[] contents;
	    
	    BufferedWriter out = new BufferedWriter(new FileWriter("CM_grouping_Two_0604.csv"));
	    
	    long ex = 0;
	    String[] exContents = null;
	    
	    int length = 0;
	    int flag = 0;
	    int count = 0;
	    exContents = reader.readNext();
	    ex = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(exContents[4]).getTime() / 1000;
	    
		while((contents = reader.readNext()) != null){
	
			length = 0;
			
			for(; (length < 57 & length < contents.length); length++){
			//	System.out.println(contents[length]);
			//	System.out.println(length);
				if(contents[length].compareTo("") != 0)
					length++;
				else
					break;
			}
			
			if(length < 10)
				continue;
			
			long current = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(contents[4]).getTime() / 1000;
			String date = null;
			if(current - ex <= 60) {
				
			
					count++;
					out.write(exContents[0]+ " ,");
					
					for(int i = 1; i < exContents.length -1; i++) {
						
						out.write(exContents[i++]+ " ,");
						out.write(exContents[i] + ",");
			
					}
					
					out.newLine();

				
				out.write(contents[0]+ " ,");
				
				for(int i = 1; i < contents.length - 1; i++){
					
					out.write(contents[i++]+ " ,");
					out.write(contents[i] + ",");
	
				}
				
				out.newLine();

			}
			
			ex = current;
			exContents = contents.clone();
			System.out.println(count);
			
						
			
		}
		
		out.close();
		
		
	}

}
