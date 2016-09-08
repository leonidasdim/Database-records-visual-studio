package my_reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class my_BaseStation {
	private ArrayList<baseStation> baseStationList; 
	
	
	public my_BaseStation(){
		
		try {
			baseStationList  = new ArrayList<baseStation>();
			
			FileReader fr;
			fr = new FileReader("./base_station.csv");
	      BufferedReader br = new BufferedReader(fr);
	      String stringRead = br.readLine();
	      
	      stringRead = br.readLine();
	      while( stringRead != null )
	      {	      
	    	  String[] elements = stringRead.split("\t");		// xorismos ths gramis se kommatia

	    	    String id = elements[0];
	    	    String user = elements[1];
	    	    String operator = elements[2];
	    	    String mcc = elements[3];
	    	    String mnc = elements[4];
	    	    String cid = elements[5];
	    	    String lac = elements[6];
	    	    String latitude = elements[7];
	    	    String longtitude = elements[8];
	    	    String timestamp = elements[9];
	    	    
	    	    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
	    	    Date date = null;
	            try {
					date = dateformat.parse(timestamp);
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
         
	    	    if(latitude.contains("No Latitude")||latitude.contains("No latitude yet")){
	    	    	latitude = "0.0";
	    	    	longtitude = "0.0";
	    	    }
	    	    
	    	    baseStation bat = new baseStation(id,user,operator,Integer.parseInt(mcc),Integer.parseInt(mnc),Integer.parseInt(cid),Integer.parseInt(lac),Double.parseDouble(latitude), Double.parseDouble(longtitude),date);
	    	    baseStationList.add(bat);

	    	    
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
	
	public ArrayList<baseStation> getList() {
		return baseStationList;
	}
	

}
