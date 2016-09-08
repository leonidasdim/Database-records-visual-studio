package my_reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

public class my_wifi{
	
	
	
	
	
	private ArrayList<wifi> wifiList; 
	
	public my_wifi(){
		try {
			wifiList= new ArrayList<wifi>();
			FileReader fr;
			fr = new FileReader("./wifi.csv");
	      BufferedReader br = new BufferedReader(fr);
	      String stringRead = br.readLine();
	      ArrayList<String> macs = new ArrayList<String>();
	      stringRead = br.readLine();
	      while( stringRead != null )
	      {	      
	    	  String[] elements = stringRead.split("\t");

	    	    String id = elements[0];
	    	    String user = elements[1];
	    	    String ssid = elements[2];
	    	    String mac = elements[3];
	    	    String rssi = elements[4];
	    	    String frenqury = elements[5];
	    	    String latitude = elements[6];
	    	    String longtitude = elements[7];
	    	    String timestamp = elements[8];
	    	    
	    	    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
	    	    Date date = null;
	            try {
					date = dateformat.parse(timestamp);
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	    wifi wifi = new wifi(id, user, ssid,mac,Integer.parseInt(rssi),Integer.parseInt(frenqury),Double.parseDouble(latitude),Double.parseDouble(longtitude), date);
	    	    if(wifi.getLatitude()>100)
	    	    	System.out.println("1 "+wifi.getId() +" " +wifi.getUser()+" " +wifi.getSsid()+" " +wifi.getMac()+" " +wifi.getRssi()+" " +wifi.getFrenqury()+" " +wifi.getLatitude()+" " +wifi.getLongtitude()+" " +wifi.getDat() );
	    	    wifiList.add(wifi);  // auti tin lista tin exw gia eukoloteri anazitisi gia parakatw
	    	    macs.add(mac);
	        stringRead = br.readLine();
	      }
	      br.close( );
	      
	      /* Parakatw exw san odigo lista macs. ekei anazitw an uparxoun ta stoixeia pou 8elw
	       *  (8a mporousa na exw kanei override tin search tis listas alla to skeftika meta)
	       * gia opoies mac brw polles fores bazw se alli lista (stin list) tous diktes tous stin wifiList 
	       * gia na min ksanasxoli8w meta me mac pou exw idi ftiaksei (opote kai 8a tis xalaga) , tis krataw
	       * stin lista found
	       * 
	       *  opote telika exw 4 listes.....
	      */
	      
	      int temp_rssi=0;
	      ArrayList<String> found = new ArrayList<String>();
	      for(int i = 0; i < wifiList.size(); i ++)
	    	  {
	    	  String bssid = macs.get(i);
	    	  int fl = macs.lastIndexOf(bssid);   // briskw tin teleutaia mac
	    	  wifi wifi = wifiList.get(i);			// auto einai metabliti - deiktis den peirazei poli an to peiraksw sto telos
	    	
	    	  if(( fl>i ) && (!found.contains(wifi.getMac()))){		// elegxei an exw ksanaasxoli8ei me tin mac auti
	    		  ArrayList<Integer> list = new ArrayList<Integer>();
	    		  Double w = Math.pow(10, -3) * Math.pow(wifi.getRssi(), 10);
	    		  Double lat = (wifi.getLatitude()* Math.PI/180 )* w ;
		    	  Double lon = (wifi.getLongtitude()* Math.PI/180 )*w;
		    	  temp_rssi +=wifi.getRssi();
		    	  for(int j = i ; j < fl ; j ++){
			    		 wifi wifi_in = wifiList.get(j);
			    		 if(bssid.matches(wifi_in.getMac())){
				   			 Double w_temp = Math.pow(10, -3) * Math.pow(wifi_in.getRssi(), 10);
				   			 w = w+w_temp;
				   			 lat = lat + (wifi_in.getLatitude()* Math.PI/180 )* w_temp ;
					    	 lon = lon + (wifi_in.getLongtitude()* Math.PI/180 )*w_temp;
					    	 temp_rssi +=wifi_in.getRssi();
				   			list.add(j); 
			    			 
			    		 }
			      }
			      found.add(wifi.getMac()); // pros8etw tin mac se mia lista me tis mac pou exw idi elenksei gia melontiki anafora
			      if(!list.isEmpty())
			      {
			    	// prwta ftiaxnw to proto wifi , to opoio den exei mpei pote stin lista wifiList
			    	lat = lat/w;
			    	lon = lon /w;
			    	lat = lat * 180 / Math.PI;
			    	lon = lon * 180 / Math.PI;
			    	temp_rssi = temp_rssi/(list.size()+1); // to +1 einai gia to stoixeio pou den mpike pote
			    	wifi.setLatitude(lat);
			    	wifi.setLongtitude(lon);
			    	wifi.setRssi_min(temp_rssi);
			    	if(wifi.getLatitude()>100)
		    	    	System.out.println("2\n "+wifi.getId() +" " +wifi.getUser()+" " +wifi.getSsid()+" " +wifi.getMac()+" " +wifi.getRssi()+" " +wifi.getFrenqury()+" " +wifi.getLatitude()+" " +wifi.getLongtitude()+" " +wifi.getDat() );
			    	wifiList.set(i, wifi);
			    	
			    	for(int j = 0 ; j < list.size(); j++ ){
			    			  int index = (int) list.get(j);
			    			 
			    			  wifi = wifiList.get(index);
			    			  wifi.setLatitude(lat);
				    		  wifi.setLongtitude(lon);
				    		  wifi.setRssi_min(temp_rssi);
				    		  if(wifi.getLatitude()>100.00)
					    	    	System.out.println("3\n\n "+wifi.getId() +" " +wifi.getUser()+" " +wifi.getSsid()+" " +wifi.getMac()+" " +wifi.getRssi()+" " +wifi.getFrenqury()+" " +wifi.getLatitude()+" " +wifi.getLongtitude()+" " +wifi.getDat() );
				    		  wifiList.set(index, wifi);
			    		  }
			    	  }
		      }
	      }
	      
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
}

	public ArrayList<wifi> getList() {
		return wifiList;
	}
}
