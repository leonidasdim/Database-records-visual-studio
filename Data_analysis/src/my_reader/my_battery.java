package my_reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class my_battery {
	private ArrayList<Battery> batteryList; 
	private ArrayList <String> users;
	private ArrayList <Date> dates;
	
	public my_battery(){
		
		try {
			batteryList  = new ArrayList<Battery>();
			users =  new ArrayList<String>();
			dates = new ArrayList<Date>();
			FileReader fr;
			fr = new FileReader("./battery.csv");
	      BufferedReader br = new BufferedReader(fr);
	      String stringRead = br.readLine();
	      
	      stringRead = br.readLine();
	      while( stringRead != null )
	      {	      
	    	  String[] elements = stringRead.split("\t");

	    	    String id = elements[0];
	    	    String user = elements[1];
	    	    String level = elements[2];
	    	    String plugged = elements[3];
	    	    String temperature = elements[4];
	    	    String voltage = elements[5];
	    	    String timestamp = elements[6];

	    	    
	    	    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
	    	    Date date = null;
	            try {
					date = dateformat.parse(timestamp);
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        
	    	    Battery bat = new Battery (id,user,Integer.parseInt(level),Integer.parseInt(plugged),Integer.parseInt(temperature),Integer.parseInt(voltage),date);
	    	    batteryList.add(bat);
	    	    if(!users.contains(user))
	    	    	users.add(user);
	    	    dates.add(date);
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
	 public ArrayList<Battery> getList() {
	       return batteryList;
	   }
		public ArrayList<String> getUsers() {
			return users;
		}
		
		public ArrayList<Date> getDates() {
			return dates;
		}
}