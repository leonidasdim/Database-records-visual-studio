package my_reader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class my_GPS {
	private ArrayList<GPS> GpsList;
	
	public my_GPS(){
		
		try {
			GpsList = new ArrayList<GPS>();
			FileReader fr;
			fr = new FileReader("./gps.csv");
	      BufferedReader br = new BufferedReader(fr);
	      String stringRead = br.readLine();
	      
	      stringRead = br.readLine();
	      while( stringRead != null )
	      {	      
	    	  String[] elements = stringRead.split("\t");

	    	    String id = elements[0];
	    	    String email = elements[1];
	    	    String latitude = elements[2];
	    	    String longtitude = elements[3];
	    	    String timestamp = elements[4];
	    	    
	    	    
	    	    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
	    	    Date date = null;
	            try {
					date = dateformat.parse(timestamp);
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        
	    	    
	    	    GPS gps = new GPS(id, email, Double.parseDouble(latitude),Double.parseDouble(longtitude),date);
	    	    GpsList.add(gps);
	    	    
	        stringRead = br.readLine();
	       
	      }
	      br.close( );
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
	
	public ArrayList<GPS> getList() {
	       return GpsList;
	   }
	
		
	
}
