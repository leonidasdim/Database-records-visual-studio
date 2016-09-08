package Optikopoihsh;

import java.awt.Desktop;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;

import my_reader.Battery;
import my_reader.GPS;
import my_reader.baseStation;
import my_reader.my_BaseStation;
import my_reader.my_GPS;
import my_reader.my_battery;
import my_reader.my_wifi;
import my_reader.wifi;

public class er4 {
	
	
	
	public int createfile_1( my_battery battery ){
		int j=0;
		String[] names ;
		names =new String[1000];
		boolean flag=true;
		int count0 = 0,count1=0,count2=0,count3=0,count4 = 0,count5 = 0,count6 = 0,count7 = 0,count8 = 0,count9 = 0,count10 = 0,count11 = 0,count12 = 0,count13 = 0,count14 = 0,count15 = 0,count16 = 0,count17 = 0,count18 = 0,count19 = 0,count20 = 0,count21 = 0,count22 = 0,count23 = 0;
		ArrayList<String>  users;
		ArrayList<Battery> in_battery = battery.getList();
	
		for(int i =0 ; i < in_battery.size(); i++){
			
			Battery tesla =in_battery.get(i);
			int hours =tesla.getDat().getHours();
			int level=tesla.getLevel();
			flag=true;
			if(i!=0 &&in_battery.get(i).getDat().getHours()==in_battery.get(i-1).getDat().getHours()){
				for(int k=0;k<=j;k++){
					
					//System.out.println (in_battery.get(names[k]).getUser()) ;
					if(tesla.getUser().equals(names[k]) ){
						flag=false;
					}
				}
				
			}else {
				j=0; flag=true;  
				names =null;
				names=new String[1000];
            }
			if(flag==true){
			
				if(hours==0&&level<=15){
					names[j]=tesla.getUser();
					count0 ++;
				}
				else if(hours==1&&level<=15){
					names[j]=tesla.getUser();
					count1 ++;
				}
				else if(hours==2&&level<=15){
					names[j]=tesla.getUser();
					count2 ++;
				}
				else if(hours==3&&level<=15){
					names[j]=tesla.getUser();
					count3 ++;
				}
				else if(hours==4&&level<=15){
					names[j]=tesla.getUser();
					count4 ++;
				}
				else if(hours==5&&level<=15){
					names[j]=tesla.getUser();
					count5 ++;
				}
				else if(hours==6&&level<=15){
					names[j]=tesla.getUser();
					count6 ++;
				}
				else if(hours==7&&level<=15){
					names[j]=tesla.getUser();
					count7 ++;
				}
				else if(hours==8&&level<=15){
					names[j]=tesla.getUser();
					count8 ++;
				}
				else if(hours==9&&level<=15){
					names[j]=tesla.getUser();
					count9 ++;
				}
				else if(hours==10&&level<=15){
					names[j]=tesla.getUser();
					count10 ++;
				}
				else if(hours==11&&level<=15){
					names[j]=tesla.getUser();
					count11 ++;
				}
				else if(hours==12&&level<=15){
					names[j]=tesla.getUser();
					count12 ++;
				}			
				else if(hours==13&&level<=15){
					names[j]=tesla.getUser();
					count13 ++;
				}			
				else if(hours==14&&level<=15){
					names[j]=tesla.getUser();
					count14 ++;
				}
				else if(hours==15&&level<=15){
					names[j]=tesla.getUser();
					count15 ++;
				}
				else if(hours==16&&level<=15){
					names[j]=tesla.getUser();
					count16 ++;
				}
				else if(hours==17&&level<=15){
					names[j]=tesla.getUser();					
					count17 ++;
				}			
				else if(hours==18&&level<=15){
					names[j]=tesla.getUser();
					count18 ++;
				}
				else if(hours==19&&level<=15){
					names[j]=tesla.getUser();
					count19 ++;
				}				
				else if(hours==20&&level<=15){
					names[j]=tesla.getUser();
					count20 ++;
				}	
				else if(hours==21&&level<=15){
					names[j]=tesla.getUser();
					count21 ++;
				}
				else if(hours==22&&level<=15){
					names[j]=tesla.getUser();
					count22 ++;
				}
				else if(hours==23&&level<=15){
					names[j]=tesla.getUser();
					count23 ++;
				}
				j++;
				
			}
			
		}		
		
		PrintWriter writer;
		try {
			String temp =  System.getProperty("user.dir");
			String temp1  = temp.substring(0, temp.length()-18);
//			System.out.println(temp1);
			String dammy = temp+"/my_ask.html";
			String dammy2 = temp+"/" +"Chart.js";
			writer = new PrintWriter(dammy, "UTF-8");
			writer.println("<!doctype html> \n"+
					"<html> \n" +
					"<head> \n"+
					"<title>Bar diagram</title> \n"+
					" <script src= \""+dammy2+"\"></script>  \n " +
					"</head> \n"+
					"<body> \n" +
					"<div style=\"width:70%\"> \n"+
					"<div> \n"+
					"<canvas id= \"canvas\" height=\"450\" width=\"600\"></canvas> \n" +
					"</div> \n"+
					"</div> \n"+
					"<script> \n" +
					"      var barChartData = {  \n " +
					"labels : [" +  "\""+"00"+"\"" +","+ "\""+"01"+"\"" +","+"\""+"02"+"\"" +","+"\""+"03"+"\"" +","+"\""+"04"+"\"" +","+"\""+"05"+"\"" +","+"\""+"06"+"\"" +","+"\""+"07"+"\"" +","+"\""+"08"+"\"" +","+"\""+"09"+"\"" +","+"\""+"10"+"\"" +","+"\""+"11"+"\"" +"," + "\""+"12"+"\"" +","+ "\""+"13"+"\"" +","+ "\""+"14"+"\"" +","+ "\""+"15"+"\"" +","+ "\""+"16"+"\"" +","+ "\""+"17"+"\"" +","+ "\""+"18"+"\"" +","+ "\""+"19"+"\"" +","+ "\""+"20"+"\"" +","+ "\""+"21"+"\"" +","+ "\""+"22"+"\"" +","+ "\""+"23"+"\"" +"," +"], \n"+
					 "datasets : [ \n"+
					"{ \n"+
					"fillColor : \"rgba(151,187,205,0.5)\"   ,     \n"+
					"strokeColor : \"rgba(151,187,205,0.8)\",     \n" +
					" highlightFill : \"rgba(151,187,205,0.75)\",       \n     "+
					"      highlightStroke : \"rgba(151,187,205,1)\"       ,  \n  "+
					" data : [    "+Integer.toString(count0)+"," +Integer.toString(count1)+"," +Integer.toString(count2)+"," +Integer.toString(count3)+"," +Integer.toString(count4)+"," +Integer.toString(count5)+"," +Integer.toString(count6)+"," +Integer.toString(count7)+"," +Integer.toString(count8)+"," +Integer.toString(count9)+"," +Integer.toString(count10)+","+Integer.toString(count11)+"," +Integer.toString(count12)+"," +Integer.toString(count13)+"," +Integer.toString(count14)+"," +Integer.toString(count15)+"," +Integer.toString(count16)+"," +Integer.toString(count17)+"," +Integer.toString(count18)+"," +Integer.toString(count19)+"," +Integer.toString(count20)+"," +Integer.toString(count21)+"," +Integer.toString(count22)+"," +Integer.toString(count23)+"," +"\n" +
					"] \n"+
					"} \n"+
					"] \n"+
					"} \n"+
					"window.onload = function(){     \n"+
					"var ctx = document.getElementById(\"canvas\").getContext(\"2d\"); \n"+
					"	window.myBar = new Chart(ctx).Bar(barChartData, {  \n"+
					"		responsive : true \n"+
					"}); \n"+
					"} \n"+
					"</script> \n"+
					"</body> \n"+
					"</html>");
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
	
	
	public int createfile_3( my_BaseStation base ){
		
		ArrayList<operattion> oper ; 
		oper=new ArrayList<operattion>() ;
		
		boolean flag=true,flag2=true;;
		ArrayList<baseStation> list = base.getList();
		
		for(int i =0 ; i < list.size()-1; i++)	{
			flag=true;
			if(i!=0){
				for(int k=0;k<oper.size();k++){
					
					if(list.get(i).getOperator().equals(oper.get(k).getOper())){
						//System.out.println("!!!!!!!!!!!!!!!");
						flag=false;
						break;
						
					}

				}
			}
			if(flag==true){
				operattion op =new operattion();
				op.setOper(list.get(i).getOperator());
				oper.add(op);
				
			}
		}

//		System.out.println(oper.size());
	
		flag2=true;
		int j=1 ;	
		String[][] users;
		users =new String[16][100];
		for(int k=0;k<oper.size();k++)
			users[k][0]=oper.get(k).getOper();
		for(int i =0 ; i < list.size()-1; i++){
			flag2=true;
			for(int k=0;k<oper.size();k++){
				
				if(list.get(i).getOperator().equals(oper.get(k).getOper())){
					for(int c=1;c<100;c++){
						if(list.get(i).getUser().equals(users[k][c])){
							flag2=false;
							break;
							
						}
						
					}
					if(flag2==true){
						int z ;
						z=oper.get(k).getCount() +1;
						oper.get(k).setCount(z);
						users[k][j]=list.get(i).getUser();
						j++;		
					}		
				}
			}
		}
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
					"<title>Bar diagram</title> \n"+
					" <script src= \""+dammy2+"\"></script>  \n " +
					"</head> \n"+
					"<body> \n" +
					"<div style=\"width:70%\"> \n"+
					"<div> \n"+
					"<canvas id= \"canvas\" height=\"450\" width=\"600\"></canvas> \n" +
					"</div> \n"+
					"</div> \n"+
					"<script> \n" +
					"      var barChartData = {  \n " +
					"labels : [");
			
			
			for(int i=0;i<oper.size();i++){
				writer.println("\""+oper.get(i).getOper()+"\"" +"," );
			}
			
			
			
			writer.println( "], \n"+
					 "datasets : [ \n"+
					"{ \n"+
					"fillColor : \"rgba(151,187,205,0.5)\"   ,     \n"+
					"strokeColor : \"rgba(151,187,205,0.8)\",     \n" +
					" highlightFill : \"rgba(151,187,205,0.75)\",       \n     "+
					"      highlightStroke : \"rgba(151,187,205,1)\"       ,  \n  "+
					" data : [    ");
				
			for(int i=0;i<oper.size();i++)
				writer.println(Integer.toString(oper.get(i).getCount())+",");
			
				writer.println(		"] \n"+
					"} \n"+
					"] \n"+
					"} \n"+
					"window.onload = function(){     \n"+
					"var ctx = document.getElementById(\"canvas\").getContext(\"2d\"); \n"+
					"	window.myBar = new Chart(ctx).Bar(barChartData, {  \n"+
					"		responsive : true \n"+
					"}); \n"+
					"} \n"+
					"</script> \n"+
					"</body> \n"+
					"</html>");
					writer.close();
		}
		 catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String temp =  System.getProperty("user.dir");
	//	System.out.println(temp);
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
	

	
	
	
	
	
	public int 	createfile_2(String user , Date Start, Date End, my_GPS gps  ,my_wifi wi ){
		er2 tt = new er2();
		ArrayList <GPS> list_gps = tt.GPSsearch(user, Start, End, gps);
		ArrayList <wifi> list_wifi = tt.katagegramenaAccesPoints(user, Start, End, wi);
		ArrayList <wifi> teliki_w = new <wifi> ArrayList();
		if (list_gps == null || list_wifi == null)
			return -1;
		int j = 0;
		for(int i = 0; i < list_gps.size(); i ++){
			wifi temp = null;
			for(;j < list_wifi.size(); j ++){
				if(list_gps.get(i).getDat().equals(list_wifi.get(j).getDat())){
					
					if(temp == null)
						temp = list_wifi.get(j);
					else {
						// epeidi exw kratimeno kai to sketo rssi 8elei prosoxi edw
						if(temp.getRssi_min()!=0)		
						{
							if(list_wifi.get(j).getRssi_min() != 0 ){				//ean uparxei temp rssi_min kai list rssi_min 
								if(temp.getRssi_min()<list_wifi.get(j).getRssi_min())
									temp = list_wifi.get(j);
							}else if(temp.getRssi_min()<list_wifi.get(j).getRssi())	 //ean uparxei temp rssi_min kai list rssi
								temp = list_wifi.get(j);
						}
						else {
							if(list_wifi.get(j).getRssi_min() != 0 ){ 				//ean uparxei temp rssi kai list rssi_min
								if(temp.getRssi()<list_wifi.get(j).getRssi_min())
									temp = list_wifi.get(j);						//ean uparxei temp rssi kai list rssi````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````````
							}else if(temp.getRssi()<list_wifi.get(j).getRssi())	
								temp = list_wifi.get(j);
						
						}
					}
			//		System.out.println(temp.getUser() +"\t"+temp.getMac());
				}
				else if(list_gps.get(i).getDat().before(list_wifi.get(j).getDat()))
				{ 
					if(j > 1)
						j--;
					break;
				}
				
			}
			if(temp!= null)
				teliki_w.add(temp);
		}
		if(teliki_w .size() >0){
	//		System.out.println("H lista exei mege8os "+teliki_w.size());
			
			ArrayList <String> wifi2=new ArrayList();
		
			for(int k = 0 ; k < teliki_w.size(); k++){
				wifi temp = teliki_w.get(k);
			
				if(!wifi2.contains(temp.getMac()))
					wifi2.add(temp.getMac());
				
				else {
					teliki_w.remove(k);
					k--;
				}
	
			}
	/*		for(int k = 0 ; k < teliki_w.size(); k++){
				System.out.println(teliki_w.get(k).getUser() +"\t"+teliki_w.get(k).getMac()+"\t"+teliki_w.get(k).getSsid());
			}
	*/		
			PrintWriter writer;
			try {
				double max_lat = 0 , max_lon = 0, min_lat = 0, min_lon = 0;
				String temp =  System.getProperty("user.dir");
				String dammy = temp+"/my_ask.html";
				writer = new PrintWriter(dammy, "UTF-8");
				writer.println(  "<!DOCTYPE html> \n"+ 
								 "<html> \n"+
								 " <head> \n"+
								 "   <style type=\"text/css\"> \n"+
								 "     html, body, #map-canvas { height: 100%; margin: 0; padding: 0;} \n"+
								 "   </style> \n"+
								 "   <script type=\"text/javascript\" \n"+ 
								 "     src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyCCMxcqD5JnQlGAMhb5lQRhOdDZJAv9iKc\">  \n"+
								 "   </script>  \n"+
								 "   <script type=\"text/javascript\"> \n"+ 
								 "     function initialize() { \n"+ 
								
								 
									
								 "       var flightPlanCoordinates = [ \n");

				for(int i =0 ; i < list_gps.size() -1; i++)
				{
					GPS temp_wi = list_gps.get(i);
		//			System.out.println(temp_wi.getId() +" " +temp_wi.getUser()+" " +temp_wi.getLatitude()+" " +temp_wi.getLongtitude()+" " +temp_wi.getDat() );
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
				
				GPS temp_wi = list_gps.get(list_gps.size()-1);
				if(list_gps.size() == 1 ){
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
				
				writer.println( "		 new google.maps.LatLng("+ temp_wi.getLatitude()+", "+temp_wi.getLongtitude()+") \n		]; ");
								
				writer.println("    	//location of marker    \n"
						+"    	var scanAP = new google.maps.LatLng("+(max_lat+min_lat)/2+", "+(max_lon+min_lon)/2+"); \n" 
						    	  
						+"        var mapOptions = { \n"
						+"          center: { lat: "+(max_lat+min_lat)/2+", lng:"+(max_lon+min_lon)/2+"}, \n"
						+"          zoom: 8 \n"
						+"        }; \n"
						        
						+"        var map = new google.maps.Map(document.getElementById('map-canvas'), \n"
						+"            mapOptions); \n"
				);
		
				
				
				
				writer.println(	 "		\n"+
								 "		var flightPath = new google.maps.Polyline({  \n"+
								 "			path: flightPlanCoordinates,  \n"+
								 "			geodesic: true,  \n"+
								 "			strokeColor: '#FF0000', \n"+ 
								 "			strokeOpacity: 1.0,  \n"+
								 "			strokeWeight: 2  \n"+
								 "		});  \n"+
								 "		flightPath.setMap(map);  \n"+
								
								 "	var locations = [  ");
				
				for(int i =0 ; i < teliki_w.size()-1; i ++)
					writer.println("	      ['"+i+", rssi = "+teliki_w.get(i).getRssi()+", frequency = "+teliki_w.get(i).getFrenqury()+"', "  +  teliki_w.get(i).getLatitude()+", "+ teliki_w.get(i).getLongtitude()+", "+(i+1)+" ], ");
				
				writer.println("	      ['"+ (teliki_w.size()-1) +",rssi = "+teliki_w.get(teliki_w.size()-1).getRssi()+", frequency = "+teliki_w.get(teliki_w.size()-1).getFrenqury()+"', " + teliki_w.get(teliki_w.size()-1).getLatitude()+", "+ teliki_w.get(teliki_w.size()-1).getLongtitude()+", "+teliki_w.size()+" ] ");
								 
								 
				writer.println(	 "	];  \n\n\n"+
								 	
							"    var infowindow = new google.maps.InfoWindow();  \n" +
								 	
								 "		var marker, i;  \n"+
								
								 "	    	for (i = 0; i < locations.length; i++) {  \n"+
								 "	      		marker = new google.maps.Marker({  \n"+
								 "			position: new google.maps.LatLng(locations[i][1], locations[i][2]),  \n"+
								 "			map: map  \n"+
								 "	      });  \n"+
								 "	      google.maps.event.addListener(marker, 'click', (function(marker, i) {  \n"+
								 "			return function() {  \n"+
								 "		  		infowindow.setContent(locations[i][0]);  \n"+
								 "		  		infowindow.open(map, marker);  \n"+
								 "			}  \n"+
								 "	      })(marker, i));  \n"+
								 "	    } 	 \n"+
								
								 "      }  \n"+
								 "     google.maps.event.addDomListener(window, 'load', initialize);  \n"+
								 "    </script>  \n"+
								 "  </head>  \n"+
								 " <body>  \n"+
								 "<div id=\"map-canvas\"></div>  \n"+
								 "</body>  \n"+
								 "</html> 		 \n"
						);
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
		        return 1;
		}
		else
			return -1;
	}
}
