package Optikopoihsh;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

import my_reader.GPS;
import my_reader.baseStation;
import my_reader.my_GPS;
import my_reader.my_battery;
import my_reader.my_wifi;
import my_reader.wifi;

public class er3 {

	
	 ArrayList <StayPoints> points = null;
	 boolean createFile = true;
	



public boolean EstimateStayPoint (String user, int Tmin, int Tmax , double Dmax , my_GPS my_gps, Date Start, Date End  ){ 

		int i =1;	
		er2 opt = new er2();
		ArrayList <GPS> gps_list = opt.GPSsearch(user, Start, End, my_gps);
		if(points == null)
		points = new ArrayList<StayPoints>();
		
		while (i<gps_list.size()-1){	//logika edw 8elei -1 alliws atermon.
			ArrayList <GPS> isp = new ArrayList<GPS>();
			GPS t_i = (GPS) gps_list.get(i);
			int j = i+1;
			while(j < gps_list.size() ){ // eixe mikrotero iso
		//		System.out.println(user + "\t"+i );
				GPS t_j = (GPS) gps_list.get(j);
				GPS t_j_1 = (GPS) gps_list.get(j-1);
				if( t_j.getDat().getTime() - t_j_1.getDat().getTime()  > Tmax ){ 
					if(t_i.getDat().getTime() - t_j_1 .getDat().getTime() > Tmin)
						isp.add(t_j_1);
					i = j;
					break;
				}
				else if(distance(t_i.getLatitude() , t_j.getLatitude(), t_i.getLongtitude(), t_j.getLongtitude())> Dmax){ 
				
					if(t_i.getDat().getTime() - t_j_1.getDat().getTime() > Tmin){
						isp.add(t_j_1);
						i = j;
						break;
					}
					i ++;
					break;
				}
				else if(j == gps_list.size()-1){				// Sto eclass to exete if(j == N) , pou pote den 8a isxusei. 
					if( t_i.getDat().getTime() - t_j.getDat().getTime()  > Tmin)
						isp.add(t_j_1);
					i = j ;
					break;
				}
				j ++;
			}
			
			if((isp.size()>0) && (createFile = true)) 
				points.add(EstimateCendroid(isp));
			
		}
		if(points.size()>0){
			createfile(points, user);
			return true;
		}
		else
			return false;
	}



	public boolean er_3_2(double eps, int MinPts , int Tmin, int Tmax , double Dmax , my_GPS my_gps, Date Start, Date End, my_battery batt , my_wifi wifi , boolean boo){
		createFile = false;						// gia na min ftiaxnei oli tin wra to EstimateStayPoint axxrista arxeia 
		ArrayList <String> users = batt.getUsers();
//		System.out.println("prin to 1");
		for(int i = 0; i < users.size(); i ++){
			 EstimateStayPoint (users.get(i), Tmin, Tmax , Dmax , my_gps, Start, End  );	// calculation of all the stay points
		}
//		System.out.println("meta to 1");
		bdscan scan = new bdscan ();
		ArrayList <ArrayList<StayPoints> > clusters = scan.DBSCAN(points, eps,  MinPts);	// entopismos twn clusters
		
		System.out.println("bre8ikan " + clusters.size() + " clusters");
		if(clusters.size()>0){
			createfile_3_2( clusters , wifi , boo) ;
			return true;
		}
		else 
			return false;
	}


	
	
	public double distance ( double lat1 , double lat2 , double lon1 , double lon2){
		double d = 0.0;
		double R = 6371.0;									// mesi aktina ghs
		double dLat = (lat1-lat2)*(Math.PI/180);
		double dLon = (lon1-lon2)*(Math.PI/180);
		double a =  Math.sin(dLat/2)*Math.sin(dLat/2)+Math.cos(lat1*(Math.PI/180))*Math.cos(lat2*(Math.PI/180))*Math.sin(dLon/2)*Math.sin(dLon/2); // einai mia i alli me to na to ebaza tetragono
		double c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		d = R*c;
		return d;
	}

// ousiastika cendroid einai kati san auto pou ipologizetai sto prwto erwtima gia ta wifi me idia mac, mono pou edw den exoume baros logo RSSI.
	private StayPoints EstimateCendroid (ArrayList <GPS> list ){
		
		double lat = 0.0;
		double lon = 0.0;
		
		for(int i = 0; i < list.size() ; i++){
			lat += ((GPS)list.get(i)).getLatitude();
			lon += ((GPS)list.get(i)).getLongtitude();
		}
		lat /= list.size();
		lon /=  list.size();
		Date Tstart = ((GPS)list.get(0)).getDat();
		Date Tend = ((GPS)list.get(list.size()-1)).getDat();
		StayPoints sp = new StayPoints(lat,lon,Tstart,Tend);
		return sp;
	}

	
	
	/* anazhthsh gia ola ta wifi points mesa sta oria tou latitude kai longtitude */
	
	private ArrayList <wifi> search_wifi(ArrayList <wifi> w_list , my_wifi wi , double lat_start, double lat_end, double lon_start , double lon_end , ArrayList <String> str){
		ArrayList <wifi> wifi_list = wi.getList();
		for(int i = 0; i < wifi_list.size(); i++ ){
			wifi temp = wifi_list.get(i);
			if(!str.contains(temp.getMac())){
				if(temp.getLatitude() >= lat_start && temp.getLatitude() <= lat_end && temp.getLongtitude() >= lon_start && temp.getLongtitude() <= lon_end  ){
					w_list.add(temp);
					str.add(temp.getMac());
			
				}
			}
		}
		return w_list;
	}

	public void createfile( ArrayList <StayPoints > stay, String user ) {		
		
		PrintWriter writer;
		try {
			double max_lat = 0 , max_lon = 0, min_lat = 0, min_lon = 0;
			
			String temp =  System.getProperty("user.dir");
			String temp1  = temp.substring(0, temp.length()-18);
			String dammy = temp+"/my_ask.html";
			writer = new PrintWriter(dammy, "UTF-8");
			writer.println( "<!DOCTYPE html> \n"+ 
							"	<html> \n"+
							"	<head> \n"+
							"	  <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" />\n"+ 
							"	  <title>Stay Points of user "+user+"</title> \n"+
							"	  <script src=\"http://maps.google.com/maps/api/js?sensor=false\"\n"+ 
							"	          type=\"text/javascript\"></script>\n"+
							"	</head> \n"+
							"	<body> \n"+
							
							"	  <div id=\"map\" style=\"width: 800px; height: 500px;\"></div> \n\n"+
						
							"	  <script type=\"text/javascript\"> \n"+
							"	    var locations = [ ");
	
							for(int i =0 ; i < stay.size() -1; i++)
							{
									StayPoints st =  stay.get(i);
									writer.println("	      ['Time Start = "+st.getTstart() +", Time End = " +st.getTend()+"', " +st.getLatitude()+", " +st.getLongtitude()+", " +(i+1) +"],");
									if(i == 0 ){
										max_lat = st.getLatitude();
										min_lat = st.getLatitude();
										max_lon = st.getLongtitude();
										min_lon = st.getLongtitude();
									}
									else{
										
										if(max_lat < st.getLatitude())
											max_lat = st.getLatitude();
										else if(min_lat > st.getLatitude())
											min_lat = st.getLatitude();
										else if(max_lon < st.getLongtitude())
											max_lon = st.getLongtitude();
										else if(min_lon > st.getLongtitude())
											min_lon = st.getLongtitude();
									}
							}
							
								StayPoints st =  stay.get(stay.size()-1);
								if(stay.size() == 1 ){
									max_lat = st.getLatitude();
									min_lat = st.getLatitude();
									max_lon = st.getLongtitude();
									min_lon = st.getLongtitude();
								}
								else{
									
									if(max_lat < st.getLatitude())
										max_lat = st.getLatitude();
									else if(min_lat > st.getLatitude())
										min_lat = st.getLatitude();
									else if(max_lon < st.getLongtitude())
										max_lon = st.getLongtitude();
									else if(min_lon > st.getLongtitude())
										min_lon = st.getLongtitude();
								}
								
								writer.println("	      ['Time Start = "+st.getTstart() +", Time End = " +st.getTend()+"', " +st.getLatitude()+", " +st.getLongtitude()+", " +stay.size() +"]");
							
							
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

		} catch (FileNotFoundException  | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ;
	}
	
	
	
	// the html code below was copied by https://developers.google.com/maps/documentation/javascript/examples/rectangle-simple
	public void createfile_3_2( ArrayList <ArrayList<StayPoints> > clusters ,  my_wifi wi , boolean boo) {		
		
		PrintWriter writer;
	
		try {
			ArrayList <wifi> w_list = new ArrayList<wifi>();
			ArrayList <String> str = new ArrayList<String>();
			double minimum_lat = 0 , minimum_lon = 0 , maximum_lat = 0 , maximum_lon = 0 ;
			String temp =  System.getProperty("user.dir");
			String dammy = temp+"/my_ask.html";
			writer = new PrintWriter(dammy, "UTF-8");
			
			writer.println(
							"<!DOCTYPE html> 	\n"+	// the code was copied by https://developers.google.com/maps/documentation/javascript/examples/rectangle-simple \n"+
							"<html> \n"+
							"  <head> \n"+
							"    <meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\"> \n"+
							"    <meta charset=\"utf-8\"> \n"+
							"    <title>Cendroid for all users</title> \n"+
							"    <style> \n"+
							"      html, body, #map-canvas { \n"+
							"        height: 100%; \n"+
							"        margin: 0; \n"+
							"        padding: 0; \n"+
							"      } \n\n"+
							
							"    </style> \n"+
							"    <script src=\"https://maps.googleapis.com/maps/api/js?v=3.exp&signed_in=true\"></script> \n"+
							"    <script> \n\n"+
							"function initialize() { \n"+
							"var myBounds = new Array();"
			);
			
			for(int k = 0; k < clusters.size(); k++){
				ArrayList < StayPoints > stay = clusters.get(k);
				
				double min_lat = stay.get(0).getLatitude();
				double min_lon = stay.get(0).getLongtitude();
				double max_lat = stay.get(0).getLatitude();
				double max_lon = stay.get(0).getLongtitude();  
				for(int i = 1 ; i < stay.size() ; i++){
					if(stay.get(i).getLatitude() > max_lat)
						max_lat = stay.get(i).getLatitude();
					if (min_lat > stay.get(i).getLatitude())
						min_lat = stay.get(i).getLatitude();
					if(stay.get(i).getLongtitude() > max_lon)
						max_lon = stay.get(i).getLongtitude();
					if(stay.get(i).getLongtitude() < min_lon)
						min_lon = stay.get(i).getLongtitude();
				}
				System.out.println("List size "+ stay.size());
				System.out.println("Max lon is " +max_lon);
				System.out.println("Max lat is " +max_lat);
				System.out.println("Min lon is " +min_lon);
				System.out.println("Min lat is " +min_lat);
				if(k ==0){
						maximum_lat = max_lat;
						minimum_lat = min_lat;
						maximum_lon = max_lon;
						minimum_lon = min_lon;
				}else{
					if(maximum_lat < max_lat)
						maximum_lat = max_lat;
					if (minimum_lat > min_lat)
						minimum_lat = min_lat;
					if(maximum_lon < max_lon)
						maximum_lon = max_lon;
					if (minimum_lon > min_lon)
						minimum_lon = min_lon;
				}
				if(boo)
					w_list = search_wifi(w_list , wi , min_lat, max_lat, min_lon , max_lon , str);
				
				writer.println(
								"	   myBounds["+k+"] = new google.maps.LatLngBounds( \n"+
								"      new google.maps.LatLng("+ min_lat +", "+ min_lon +"), \n"+
								"      new google.maps.LatLng("+ max_lat +", "+ max_lon +")); \n"+
								" \n"
				);
			
			}	
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			if ( boo ){
			
				writer.println("	    var locations = [ "  );
				
				for(int i =0 ; i < w_list.size() -1; i++)
				{
						wifi temp_wi = w_list.get(i);
						int rssi  = temp_wi.getRssi();
						if(temp_wi.getRssi_min() != 0)
							rssi = temp_wi.getRssi_min();
						writer.println("	      ['rssi = "+rssi +", frequency = " +temp_wi.getFrenqury()+"', " +temp_wi.getLatitude()+", " +temp_wi.getLongtitude()+", " +(i+1) +"],");
				}
				
			
				wifi temp_wi = w_list.get(w_list.size()-1);
				
				int rssi  = temp_wi.getRssi();
				if(temp_wi.getRssi_min() != 0)
					rssi = temp_wi.getRssi_min();
				writer.println("	      ['rssi = "+rssi +", frequency = " +temp_wi.getFrenqury()+"', " +temp_wi.getLatitude()+", " +temp_wi.getLongtitude()+", " +w_list.size() +"]\n"+ 					
							   "	    ]; \n" );
			}
			writer.println( 
						  "  var map = new google.maps.Map(document.getElementById('map-canvas'), { \n"+
						  "    zoom: 8, \n"+
						  "    center: new google.maps.LatLng( "+ (maximum_lat + minimum_lat)/2 +", "+ (maximum_lon + minimum_lon)/2 +"), \n"+
						  "    mapTypeId: google.maps.MapTypeId.TERRAIN \n"+
						  "  }); \n\n"
						  );
						 
						  
			if(boo){
				writer.println(	"	    var infowindow = new google.maps.InfoWindow(); \n\n"+
			
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
								"		} \n " 
						);
						  
		  }
						  
		writer.println(	  "  addRects(myBounds, map); \n"+
						  "} \n\n"+

					  	  "    function addRects(bounds, map){ \n"+
					      "    for (var i=0; i<bounds.length; ++i) { \n"+
					      "      var overlay = new google.maps.Rectangle({ \n"+
					      "          map: map, \n"+
					      "          bounds: bounds[i], \n"+
					      "          strokeColor: \"red\", \n"+
						  "          strokeWeight: 1, \n"+
						  "        });  \n"+
						  "		}\n" +
						  "	   }\n" +
						  
						  
							
					
							
							
						  
					
						  "google.maps.event.addDomListener(window, 'load', initialize); \n\n"+
							
							 "   </script> \n"+
							 " </head> \n"+
							 " <body> \n"+
							 "   <div id=\"map-canvas\"></div> \n"+
							 " </body> \n"+
							"</html> "
				);
			
			
			writer.close();
			
				
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	
	}
}
