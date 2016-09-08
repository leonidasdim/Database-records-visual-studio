package Optikopoihsh;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

import com.sun.org.apache.xerces.internal.impl.dv.xs.DayDV;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import my_reader.Battery;
import my_reader.GPS;
import my_reader.baseStation;
import my_reader.my_BaseStation;
import my_reader.my_GPS;
import my_reader.my_battery;
import my_reader.my_wifi;
import my_reader.wifi;

public class er2 {			/// gia tpo 2 . 1  -  2 . 2
	

	
	public int createfile2( String user , Date Start, Date End ,my_GPS wi) {
		ArrayList <GPS> list = null;
		list = GPSsearch(user, Start, End, wi);
		if (list.size() == 0){
//			System.out.println("List = null");
			return-1;
		}
		
		PrintWriter writer;
		try {
			double max_lat = 0 , max_lon = 0, min_lat = 0, min_lon = 0;
			String temp =  System.getProperty("user.dir");
			String temp1  = temp.substring(0, temp.length()-18);
			String dammy = temp+"/my_ask.html";
			writer = new PrintWriter(dammy, "UTF-8");
			writer.println( " <!DOCTYPE html> \n"
					+" <html> \n"
					+"  <head> \n"
					+"    <style type=\"text/css\"> \n"
					+"      html, body, #map-canvas { height: 100%; margin: 0; padding: 0;} \n"
					+"    </style> \n"
					+"    <script type=\"text/javascript\" \n"
					+"      src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyCCMxcqD5JnQlGAMhb5lQRhOdDZJAv9iKc\"> \n"
					+"    </script> \n"
					+"    <script type=\"text/javascript\"> \n"
					+"      function initialize() { \n"
					
					        
					+"        var flightPlanCoordinates = [ " );

			
			
			for(int i =0 ; i < list.size() -1; i++)
			{
				GPS temp_wi = list.get(i);
//				System.out.println(temp_wi.getId() +" " +temp_wi.getUser()+" " +temp_wi.getLatitude()+" " +temp_wi.getLongtitude()+" " +temp_wi.getDat() );
				writer.println( "		 new google.maps.LatLng("+ temp_wi.getLatitude()+", "+temp_wi.getLongtitude()+"), ");
				if(i == 0 ){
					max_lat = temp_wi.getLatitude();
					min_lat = temp_wi.getLatitude();
					max_lon = temp_wi.getLongtitude();
					min_lon = temp_wi.getLongtitude();
				}
				else{
					
					if(max_lat < temp_wi.getLatitude())
						max_lat = temp_wi.getLatitude();
					else if(min_lat > temp_wi.getLatitude())
						min_lat = temp_wi.getLatitude();
					else if(max_lon < temp_wi.getLongtitude())
						max_lon = temp_wi.getLongtitude();
					else if(min_lon > temp_wi.getLongtitude())
						min_lon = temp_wi.getLongtitude();
				}
			}
			GPS temp_wi = list.get(list.size()-1);
			writer.println( "		 new google.maps.LatLng("+ temp_wi.getLatitude()+", "+temp_wi.getLongtitude()+") \n		]; ");
			if(list.size() == 1 ){
				max_lat = temp_wi.getLatitude();
				min_lat = temp_wi.getLatitude();
				max_lon = temp_wi.getLongtitude();
				min_lon = temp_wi.getLongtitude();
			}
			else{
				
				if(max_lat < temp_wi.getLatitude())
					max_lat = temp_wi.getLatitude();
				else if(min_lat > temp_wi.getLatitude())
					min_lat = temp_wi.getLatitude();
				else if(max_lon < temp_wi.getLongtitude())
					max_lon = temp_wi.getLongtitude();
				else if(min_lon > temp_wi.getLongtitude())
					min_lon = temp_wi.getLongtitude();
			}
				
			
			writer.println("    	//location of marker    \n"
					+"    	var scanAP = new google.maps.LatLng("+(max_lat+min_lat)/2+", "+(max_lon+min_lon)/2+"); \n" 
					    	  
					+"        var mapOptions = { \n"
					+"          center: { lat: "+(max_lat+min_lat)/2+", lng:"+(max_lon+min_lon)/2+"}, \n"
					+"          zoom: 8 \n"
					+"        }; \n"
					        
					+"        var map = new google.maps.Map(document.getElementById('map-canvas'), \n"
					+"            mapOptions); \n"
			);
			
			
			
			writer.println( "		var flightPath = new google.maps.Polyline({ \n"
					+"			path: flightPlanCoordinates, \n"
					+"			geodesic: true, \n"
					+"			strokeColor: '#FF0000', \n"
					+"			strokeOpacity: 1.0, \n"
					+"			strokeWeight: 2 \n"
					+"		}); \n"
					                                   
					+"		flightPath.setMap(map); \n"
					                               
					+"      } \n"
					+"      google.maps.event.addDomListener(window, 'load', initialize); \n"
					+"    </script> \n"
					+"  </head> \n"
					+"  <body> \n"
					+"<div id=\"map-canvas\"></div> \n"
					+"</body> \n"
					+"</html> " );
			writer.close();

		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String temp =  System.getProperty("user.dir");
//		System.out.println(temp);
		String temp1  = temp.substring(0, temp.length()-18);
		String dammy = temp+"/my_ask.html";

		 String url = "file:///" +dammy;

	        if (Desktop.isDesktopSupported()) {
	            // Windows
	            try {
					Desktop.getDesktop().browse(new URI(url));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        } else {
	            // Ubuntu
	            Runtime runtime = Runtime.getRuntime();
	            try {
					runtime.exec("/usr/bin/firefox -new-window " + url);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }

		return 0;
	}

	
	
	
	
	public int createfile1_4( String user , Date Start, Date End ,my_wifi wi , my_BaseStation base) {
		ArrayList  list = null;
		if (wi != null)
			list = katagegramenaAccesPoints(user, Start, End, wi);
		else 
			list = celsSearch(user , Start, End, base);
		if (list.size() == 0){
//			System.out.println("List = null");
			return-1;
		}
		PrintWriter writer;
		try {
			String temp =  System.getProperty("user.dir");
			String dammy = temp+"/my_ask.html";
			double max_lat = 0 , max_lon = 0, min_lat = 0, min_lon = 0;
			writer = new PrintWriter(dammy, "UTF-8");
			writer.println( "<!DOCTYPE html> \n"+ 
							"	<html> \n"+
							"	<head> \n"+
							"	  <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" />\n"+ 
							"	  <title>Axaopoulos-Dimakis</title> \n"+
							"	  <script src=\"http://maps.google.com/maps/api/js?sensor=false\"\n"+ 
							"	          type=\"text/javascript\"></script>\n"+
							"	</head> \n"+
							"	<body> \n"+
							"	  <div id=\"map\" style=\"width: 800px; height: 500px;\"></div> \n\n"+
						
							"	  <script type=\"text/javascript\"> \n"+
							"	    var locations = [ ");
	
							for(int i =0 ; i < list.size() -1; i++)
							{
								if(wi != null){
									wifi temp_wi = (wifi) list.get(i);
									int rssi  = temp_wi.getRssi();
									if(temp_wi.getRssi_min() != 0)
										rssi = temp_wi.getRssi_min();
									writer.println("	      ['rssi = "+rssi +", frequency = " +temp_wi.getFrenqury()+"', " +temp_wi.getLatitude()+", " +temp_wi.getLongtitude()+", " +(i+1) +"],");
									if(i == 0 ){
										max_lat = temp_wi.getLatitude();
										min_lat = temp_wi.getLatitude();
										max_lon = temp_wi.getLongtitude();
										min_lon = temp_wi.getLongtitude();
									}
									else{
										
										if(max_lat < temp_wi.getLatitude())
											max_lat = temp_wi.getLatitude();
										else if(min_lat > temp_wi.getLatitude())
											min_lat = temp_wi.getLatitude();
										else if(max_lon < temp_wi.getLongtitude())
											max_lon = temp_wi.getLongtitude();
										else if(min_lon > temp_wi.getLongtitude())
											min_lon = temp_wi.getLongtitude();
									}
								}
								else
								{
									baseStation temp_wi = (baseStation) list.get(i);
									writer.println("	      ['operator = "+temp_wi.getOperator() +", cid = " +temp_wi.getCid()+"', " +temp_wi.getLatitude()+", " +temp_wi.getLongtitude()+", " +(i+1) +"],");
									if(i == 0 ){
										max_lat = temp_wi.getLatitude();
										min_lat = temp_wi.getLatitude();
										max_lon = temp_wi.getLongtitude();
										min_lon = temp_wi.getLongtitude();
									}
									else{
										
										if(max_lat < temp_wi.getLatitude())
											max_lat = temp_wi.getLatitude();
										else if(min_lat > temp_wi.getLatitude())
											min_lat = temp_wi.getLatitude();
										else if(max_lon < temp_wi.getLongtitude())
											max_lon = temp_wi.getLongtitude();
										else if(min_lon > temp_wi.getLongtitude())
											min_lon = temp_wi.getLongtitude();
									}
								}
							}
							
							if(wi != null){
								wifi temp_wi = (wifi) list.get(list.size()-1);
								int rssi  = temp_wi.getRssi();
								if(temp_wi.getRssi_min() != 0)
									rssi = temp_wi.getRssi_min();
								writer.println("	      ['rssi = "+rssi +", frequency = " +temp_wi.getFrenqury()+"', " +temp_wi.getLatitude()+", " +temp_wi.getLongtitude()+", " +list.size() +"]");					
								if(list.size() == 1 ){
									max_lat = temp_wi.getLatitude();
									min_lat = temp_wi.getLatitude();
									max_lon = temp_wi.getLongtitude();
									min_lon = temp_wi.getLongtitude();
								}
								else{
									
									if(max_lat < temp_wi.getLatitude())
										max_lat = temp_wi.getLatitude();
									else if(min_lat > temp_wi.getLatitude())
										min_lat = temp_wi.getLatitude();
									else if(max_lon < temp_wi.getLongtitude())
										max_lon = temp_wi.getLongtitude();
									else if(min_lon > temp_wi.getLongtitude())
										min_lon = temp_wi.getLongtitude();
								}
							}
							else
							{
								baseStation temp_wi = (baseStation) list.get(list.size()-1);
								writer.println("	      ['operator = "+temp_wi.getOperator() +", cid = " +temp_wi.getCid()+"', " +temp_wi.getLatitude()+", " +temp_wi.getLongtitude()+", " +list.size() +"]");
								if(list.size() == 1 ){
									max_lat = temp_wi.getLatitude();
									min_lat = temp_wi.getLatitude();
									max_lon = temp_wi.getLongtitude();
									min_lon = temp_wi.getLongtitude();
								}
								else{
									
									if(max_lat < temp_wi.getLatitude())
										max_lat = temp_wi.getLatitude();
									else if(min_lat > temp_wi.getLatitude())
										min_lat = temp_wi.getLatitude();
									else if(max_lon < temp_wi.getLongtitude())
										max_lon = temp_wi.getLongtitude();
									else if(min_lon > temp_wi.getLongtitude())
										min_lon = temp_wi.getLongtitude();
								}
							}
							
							writer.println("	    ]; \n"+
							"	    var map = new google.maps.Map(document.getElementById('map'), { \n"+
							"	      zoom: 8, \n"+							
							
							"	      center: new google.maps.LatLng("+(max_lat+min_lat)/2+", "+(max_lon+min_lon)/2+"), \n"+
							"	      mapTypeId: google.maps.MapTypeId.ROADMAP \n"+
							"	    }); \n\n"+
						
							"	    var infowindow = new google.maps.InfoWindow(); \n\n"+
						
							"	    var marker, i; \n\n"+
						
							"	    for (i = 0; i < locations.length; i++) { \n"+  
							"	      marker = new google.maps.Marker({ \n"+
							"	        position: new google.maps.LatLng(locations[i][1], locations[i][2]), \n"+
							"	        map: map \n"+
							"	      }); \n"+
						
							"	      google.maps.event.addListener(marker, 'click', (function(marker, i) { \n"+
							"	        return function() { \n"+
							"	          infowindow.setContent(locations[i][0]); \n"+
							"	          infowindow.open(map, marker); \n"+
							"	        } \n"+
							"	      })(marker, i)); \n"+
							"	    } \n"+
							"	  </script> \n"+
							"	</body> \n"+
							"	</html> " );
			writer.close();

		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String temp =  System.getProperty("user.dir");
//		System.out.println(temp);
		String temp1  = temp.substring(0, temp.length()-18);
		String dammy = temp+"/my_ask.html";

		 String url = "file:///" +dammy;

	        if (Desktop.isDesktopSupported()) {
	            // Windows
	            try {
					Desktop.getDesktop().browse(new URI(url));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        } else {
	            // Ubuntu
	            Runtime runtime = Runtime.getRuntime();
	            try {
					runtime.exec("/usr/bin/firefox -new-window " + url);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
		return 0;
	}
	
	
	
	
	
	
	public int createfile_3( String user , Date Start, Date End ,my_battery battery ){
		ArrayList <Battery> list =null;
		
		list = batterySearch(user, Start, End, battery);
		
		for(int i =0 ; i < list.size() -1; i++)	{
			if(i!=0){
			if(list.get(i).getDat().getHours()==list.get(i-1).getDat().getHours())
				list.remove(i);
		
			}
		}
		System.out.println(list.size());


		PrintWriter writer;
		try {
			String temp =  System.getProperty("user.dir");
			String temp1  = temp.substring(0, temp.length()-18);
			String dammy = temp+"/my_ask.html";
			String dammy2 = temp+"/" +"Chart.js";
			writer = new PrintWriter(dammy, "UTF-8");
			writer.println("<!doctype html> \n"+
							"<html> \n" +
							"<head> \n"+
							"<title>Batery chart of "+ user+"</title> \n"+		                    
							" <script src= \""+dammy2+"\"></script>  \n " +
							"</head> \n"+
							"<body> \n" +
							"<div style=\"width:70%\"> \n"+
							"<div> \n"+
							"<canvas id= \"canvas\" height=\"450\" width=\"600\"></canvas> \n" +
							"</div> \n"+
							"</div> \n"+
							"<script> \n" +
							"var lineChartData = { \n" +
							"labels : [ ");
			int k=Math.round(list.size()/10);
			
			if(list.size()<=10){
			for(int i =0 ; i < list.size() -1; i++)	{
			writer.println("\""+list.get(i).getDat()+"\"" +"," );
			}
			}else{
				for(int i =0 ; i < list.size() -1; i=i+k)	{
					writer.println("\""+list.get(i).getDat()+"\"" +"," );
					}
				writer.println("\""+End+"\"" +",");
			}
		
			writer.println(
							//  \"MAR \" , \"APR\" , \"MAY\""
							"  ], \n   " +
							"datasets : [ \n" +
							"{ \n" +
							"label: \"My First dataset\" ,\n" +
							"fillColor : \"rgba(220,220,220,0.2)\", \n" +
							"strokeColor : \"rgba(220,220,220,1)\", \n" +
							"pointColor : \"rgba(220,220,220,1)\", \n" +
							"pointStrokeColor : \"#fff\", \n" +
							"pointHighlightFill : \"#fff\", \n" +
							"pointHighlightStroke : \"rgba(220,220,220,1)\", \n"+
							"data : [ ");
			
			for(int i =0 ; i < list.size() -1; i++)	{
				Battery temp_wi = list.get(i);
				//System.out.println(temp_wi.getDat());
				String s =Integer.toString(temp_wi.getLevel());
				writer.println(s +",");
				
			}
			writer.println("] \n" +
							"} \n" +
							"] \n" +
							"} \n" +
							"window.onload = function(){ \n" +	
							"var ctx = document.getElementById(\"canvas\").getContext(\"2d\"); \n" +
							"window.myLine = new Chart(ctx).Line(lineChartData, { \n" +
							"responsive: true \n"+
							"}); \n" +
							"} \n"+
							"</script> \n" +
							"</body> \n" +
							"</html> \n"
							);
			writer.close();
		
	}
	catch (FileNotFoundException | UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		String temp =  System.getProperty("user.dir");
//		System.out.println(temp);
		String temp1  = temp.substring(0, temp.length()-18);
		String dammy = temp+"/my_ask.html";

		 String url = "file:///" +dammy;

	        if (Desktop.isDesktopSupported()) {
	            // Windows
	            try {
					Desktop.getDesktop().browse(new URI(url));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        } else {
	            // Ubuntu
	            Runtime runtime = Runtime.getRuntime();
	            try {
					runtime.exec("/usr/bin/firefox -new-window " + url);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
		
		return 0;
	}
	
	
	
	
	

	
	
	

	
	
	public ArrayList <wifi> katagegramenaAccesPoints(String user , Date dateSt, Date dateFn, my_wifi my_wi){				
								
		ArrayList <wifi> in_wifi = my_wi.getList();	    	    
		ArrayList<wifi> list = new ArrayList <wifi>();
			for(int i = 0; i < in_wifi.size(); i++){
					wifi wi = (wifi)in_wifi.get(i);
					Date date= wi.getDat();
					if(user.matches (wi.getUser()) && (dateSt.before(date) && dateFn.after(date)))
						list.add(wi);
			}
		return list;
	}

	
	public ArrayList <GPS> GPSsearch(String user , Date dateSt, Date dateFn, my_GPS my_wi){				
			ArrayList <GPS> in_wifi = my_wi.getList();
			ArrayList<GPS> list = new ArrayList <GPS>();
				for(int i = 0; i < in_wifi.size(); i++){
						GPS wi = (GPS)in_wifi.get(i);
						Date date= wi.getDat();
						if(user.matches (wi.getUser()) && (dateSt.before(date) && dateFn.after(date)))
							list.add(wi);
				}
			return list;
			
			
		}
			
			
			
			
	public ArrayList <Battery> batterySearch(String user , Date dateSt, Date dateFn, my_battery battery){
		ArrayList<Battery> in_battery = battery.getList();
		ArrayList<Battery> list = new ArrayList <Battery>();
		for(int i = 0; i < in_battery.size(); i++){
				Battery wi = (Battery)in_battery.get(i);
				if(user.matches (wi.getUser()) && (dateSt.before(wi.getDat()) && dateFn.after(wi.getDat())))
					list.add(wi);
		}
		return list;			
	}
			
			
			
			
public ArrayList<baseStation> celsSearch(String user , Date dateSt, Date dateFn, my_BaseStation base){
				
				ArrayList<baseStation> baseStationList = base.getList();
				
	    	    ArrayList<baseStation> list = new ArrayList<baseStation>();
				
					for(int i = 0; i < baseStationList.size(); i++){
							baseStation wi = (baseStation)baseStationList.get(i);
							if(user.matches (wi.getUser()) && (dateSt.before(wi.getDat()) && dateFn.after(wi.getDat())))
								if(wi.getLatitude() != 0.0)
									list.add(wi);
					}
				return list;
				
			}




}
