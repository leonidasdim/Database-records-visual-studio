package grafika;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

import Optikopoihsh.er3;
import Optikopoihsh.er2;
import my_reader.my_BaseStation;
import my_reader.my_GPS;
import my_reader.my_battery;
import my_reader.my_wifi;

public class controll {
	private grafika1 graphics;
	 
	private my_battery battery ;
	private my_GPS gps ;
	private my_wifi wifi ;
	private my_BaseStation base;

	
	public controll(grafika1 graphics , my_battery battery , my_GPS gps , my_wifi wifi , my_BaseStation base ){
	
	this.graphics=graphics;
	
	
	this.graphics.Button1(new controler1());
	this.graphics.Button2(new controler2());
	this.graphics.Button3(new controler3());
	this.graphics.Button4(new controler4());
	this.graphics.Stay_points(new controler5());
	this.graphics.DBSCAN(new controler6());
	this.graphics.Battery2(new controler7());
	this.graphics.Telecoms(new controler8());
	this.graphics.Button5(new controler9());
	this.graphics.Button6(new controler10());
	
	
	this.battery = battery;
	this.gps = gps;
	this.wifi = wifi;
	this.base = base;
	}
	public class controler implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		graphics.displayErrorMessage(graphics.userDataset());
	
	
	
}	
	
	
	}
	
	

	public class controler1 implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
//				graphics.displayErrorMessage("Koumpi AP");
			String hmera=(String) graphics.list2.getSelectedValue();
	//		System.out.println(hmera);
			String hmera2=(String) graphics.list1.getSelectedValue();
	//	 	System.out.println(hmera2);
	
			String st = " Epilegmeni imerominia= " + graphics.DateSt() ;//+ " " + graphics.DateEnd() + " " + graphics.userDataset() +;
	//		System.out.println(st);
			SimpleDateFormat dateformat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
			Date Start = null;
			Date End = null ;
			try {
				Start = dateformat.parse( hmera +" 00:00:00");
				End = dateformat.parse(hmera2 + " 23:59:59");
	//			System.out.println(Start);
	//			System.out.println(End);
				
			} catch (java.text.ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(Start.before(End)){
				Optikopoihsh.er2 map =  new Optikopoihsh.er2();
				if(map.createfile1_4(graphics.userDataset() , Start , End, wifi, null) ==-1)//graphics.DateSt() , graphics.DateEnd());
					
					graphics.displayErrorMessage("No places found");
			
			}
			else
				graphics.displayErrorMessage("Date Start mast be before date End");
		}	
		
	}

	
	public class controler2 implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
//		graphics.displayErrorMessage("koumpi 2");
		String hmera=(String) graphics.list2.getSelectedValue();
//		System.out.println(hmera);
		String hmera2=(String) graphics.list1.getSelectedValue();
// 	    System.out.println(hmera2);
			 
		
		String st = " Epilegmeni imerominia= " + graphics.DateSt() ;//+ " " + graphics.DateEnd() + " " + graphics.userDataset() +;
//		System.out.println(st);
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		Date Start = null;
		Date End = null ;
		try {
			Start = dateformat.parse( hmera +" 00:00:00");
			End = dateformat.parse(hmera2 + " 23:59:59");
//			System.out.println(Start);
//			System.out.println(End);	
		} catch (java.text.ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(Start.before(End)){
			Optikopoihsh.er2 map =  new Optikopoihsh.er2();
			if(map.createfile2(graphics.userDataset() , Start , End, gps) ==-1)//graphics.DateSt() , graphics.DateEnd());
				graphics.displayErrorMessage("No places found");
			
	
		}
		else
			graphics.displayErrorMessage("Date Start mast be before date End");
	}	
	
}
	
public class controler3 implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String hmera=(String) graphics.list2.getSelectedValue();
//		System.out.println(hmera);
		String hmera2=(String) graphics.list1.getSelectedValue();
//	 	System.out.println(hmera2);
	 	String st = " Epilegmeni imerominia= " + graphics.DateSt() ;//+ " " + graphics.DateEnd() + " " + graphics.userDataset() +;
//		System.out.println(st);
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		Date Start = null;
		Date End = null ;
			
		try {
			Start = dateformat.parse( hmera +" 00:00:00");
			End = dateformat.parse(hmera2 + " 23:59:59");
//			System.out.println(Start);
//			System.out.println(End);					
		} catch (java.text.ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(Start.before(End)){
			Optikopoihsh.er2 map =  new Optikopoihsh.er2();
			if(map.createfile_3(graphics.userDataset() , Start , End, battery) ==-1)//graphics.DateSt() , graphics.DateEnd());	
					graphics.displayErrorMessage("No battery data found");
		}
		else
			graphics.displayErrorMessage("Date Start mast be before date End");
	}		
}
	
	
	public class controler4 implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
//		graphics.displayErrorMessage("koumpi 4");
		String hmera=(String) graphics.list2.getSelectedValue();
//		System.out.println(hmera);
		String hmera2=(String) graphics.list1.getSelectedValue();
	 	String st = " Epilegmeni imerominia= " + graphics.DateSt() ;//+ " " + graphics.DateEnd() + " " + graphics.userDataset() +;
//		System.out.println(st);
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		Date Start = null;
		Date End = null ;
		try {
			Start = dateformat.parse( hmera +" 00:00:00");
			End = dateformat.parse(hmera2 + " 23:59:59");
//			System.out.println(Start);
//			System.out.println(End);			
		} catch (java.text.ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(Start.before(End)){
			Optikopoihsh.er2 map =  new Optikopoihsh.er2();
			if(map.createfile1_4(graphics.userDataset() , Start , End, null,  base) ==-1)//graphics.DateSt() , graphics.DateEnd());	
				graphics.displayErrorMessage("No places found");
		
		}
		else
			graphics.displayErrorMessage("Date Start mast be before date End");
	}	

	
}	

	public class controler5 implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub			
			String hmera=(String) graphics.list3.getSelectedValue();
//			System.out.println(hmera);
			String hmera2=(String) graphics.list4.getSelectedValue();
//	 	    System.out.println(hmera2);
	 	    String dmaxx =graphics.Dmax();
			String Tmaxx =graphics.Tmax();
			String Tminx =graphics.Tmin();
			if(dmaxx.equals("")||Tmaxx.equals("00:00:00")||Tminx.equals("00:00:00"))
				graphics.displayErrorMessage("Βαλε σωστες μεταβληλες");
			else{
				String[] time = Tmaxx.split(":");
				String[] time2 = Tminx.split(":");
				int tMin=Integer.parseInt(time2[0])*3600 +Integer.parseInt(time2[1])*60 +Integer.parseInt(time2[2]) ;			
				int tMax=Integer.parseInt(time[0])*3600 +Integer.parseInt(time[1])*60 +Integer.parseInt(time[2]) ;
//				System.out.println(tMin);
//				System.out.println(tMax);				
				double d=Double.parseDouble(dmaxx);
//				System.out.println(d);
				String st = " Epilegmeni imerominia= " + graphics.DateSt2() ;//+ " " + graphics.DateEnd() + " " + graphics.userDataset() +;
//				System.out.println(st);
				SimpleDateFormat dateformat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
				Date Start = null;
				Date End = null ;
				try {
					Start = dateformat.parse( hmera +" 00:00:00");
					End = dateformat.parse(hmera2 + " 23:59:59");
//					System.out.println(Start);
//					System.out.println(End);	
				} catch (java.text.ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(Start.before(End)){					
					er3 er = new er3();
//					System.out.println(graphics.userDataset2());
					if(er.EstimateStayPoint(graphics.userDataset2(),tMin, tMax, d, gps, Start, End)){
						String temp =  System.getProperty("user.dir");
//						System.out.println(temp);
						String temp1  = temp.substring(0, temp.length()-18);
						String dammy = temp+"/my_ask.html";
						String url = "file:///" +dammy;
				        if (Desktop.isDesktopSupported()) {
				            // Windows
				            try {
								Desktop.getDesktop().browse(new URI(url));
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (URISyntaxException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
				        } else {
				            // Ubuntu
				            Runtime runtime = Runtime.getRuntime();
				            try {
								runtime.exec("/usr/bin/firefox -new-window " + url);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
				        }	
					}
					else{
						graphics.displayErrorMessage("No cendroid points found");
					}
				}	
				else
					graphics.displayErrorMessage("Date Start mast be before date End");
			}
		}
	}
	
	public class controler6 implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String dmaxx =graphics.Dmax2();
			String Tmaxx =graphics.Tmax2();
			String Tminx =graphics.Tmin2();
			String epsx =graphics.eps();
			String Minptsx=graphics.Minpts();
			if(dmaxx.equals("")||Tmaxx.equals("00:00:00")||Tminx.equals("00:00:00")||epsx.equals("")|| Minptsx.equals(""))
				graphics.displayErrorMessage("Βαλε σωστες μεταβληλες");
			else{
				String[] time = Tmaxx.split(":");
				String[] time2 = Tminx.split(":");
				int Tmin=Integer.parseInt(time2[0])*3600 +Integer.parseInt(time2[1])*60 +Integer.parseInt(time2[2]) ;
				int Tmax=Integer.parseInt(time[0])*3600 +Integer.parseInt(time[1])*60 +Integer.parseInt(time[2]) ;
				double d=Double.parseDouble(dmaxx);
//				System.out.println(Tmax);
//				System.out.println(Tmin);
				double eps = Double.parseDouble(epsx);
				int MinPts=Integer.parseInt(Minptsx);
				String hmera=(String) graphics.list5.getSelectedValue();
//				System.out.println(hmera);		 
				String hmera2=(String) graphics.list6.getSelectedValue();
//		 	    System.out.println(hmera2);
		 	    SimpleDateFormat dateformat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		 	    Date Start = null;
				Date End = null ;
				try {
					Start = dateformat.parse( hmera +" 00:00:00");
					End = dateformat.parse(hmera2 + " 23:59:59");
	//				System.out.println(Start);
	//				System.out.println(End);					
				} catch (java.text.ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(Start.before(End)){
			 	    er3 er = new er3();
			 	   // if(!er.er_3_2(eps, MinPts, Tmin, Tmax, d, gps, Start, End, battery )){
			 	   if(!er.er_3_2(eps, MinPts, Tmin, Tmax, d, gps, Start, End, battery , wifi , false)){
			 	    	graphics.displayErrorMessage("Δεν βρέθηκαν cendroids");
			 	    	return ;
			 	    }
				}
			}
			String temp =  System.getProperty("user.dir");
//			System.out.println(temp);
			String temp1  = temp.substring(0, temp.length()-18);
			String dammy = temp+"/my_ask.html";

			String url = "file:///" +dammy;

	        if (Desktop.isDesktopSupported()) {
	            // Windows
	            try {
					Desktop.getDesktop().browse(new URI(url));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        } else {
	            // Ubuntu
	            Runtime runtime = Runtime.getRuntime();
	            try {
					runtime.exec("/usr/bin/firefox -new-window " + url);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        }
		}	
	}

	
	public class controler7 implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Optikopoihsh.er4 map =  new Optikopoihsh.er4();
			map.createfile_1(battery);
		}

	}
	
	
	public class controler8 implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Optikopoihsh.er4 map =  new Optikopoihsh.er4();
			map.createfile_3(base);

		}

	}
	
	
	
	public class controler9 implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

	
	
			String hmera=(String) graphics.list2.getSelectedValue();
//			System.out.println(hmera);
			String hmera2=(String) graphics.list1.getSelectedValue();
//	 	    System.out.println(hmera2);
			
			String st = " Epilegmeni imerominia= " + graphics.DateSt() ;//+ " " + graphics.DateEnd() + " " + graphics.userDataset() +;
//			System.out.println(st);
			SimpleDateFormat dateformat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
			Date Start = null;
			Date End = null ;
			try {
				Start = dateformat.parse( hmera +" 00:00:00");
				End = dateformat.parse(hmera2 + " 23:59:59");
	//			System.out.println(Start);
	//			System.out.println(End);	
			} catch (java.text.ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(Start.before(End)){
				Optikopoihsh.er4 map =  new Optikopoihsh.er4();
				if(map.createfile_2(graphics.userDataset() , Start , End, gps, wifi ) ==-1)//graphics.DateSt() , graphics.DateEnd());
					graphics.displayErrorMessage("No root or places found");
			}
			else
				graphics.displayErrorMessage("Date Start mast be before date End");		
		}
	}
	public class controler10 implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			// TODO Auto-generated method stub
			String dmaxx =graphics.Dmax2();
			String Tmaxx =graphics.Tmax2();
			String Tminx =graphics.Tmin2();
			String epsx =graphics.eps();
			String Minptsx=graphics.Minpts();
			if(dmaxx.equals("")||Tmaxx.equals("00:00:00")||Tminx.equals("00:00:00")||epsx.equals("")|| Minptsx.equals(""))
				graphics.displayErrorMessage("Βαλε σωστες μεταβληλες");
			else{
				String[] time = Tmaxx.split(":");
				String[] time2 = Tminx.split(":");
				int Tmin=Integer.parseInt(time2[0])*3600 +Integer.parseInt(time2[1])*60 +Integer.parseInt(time2[2]) ;
				int Tmax=Integer.parseInt(time[0])*3600 +Integer.parseInt(time[1])*60 +Integer.parseInt(time[2]) ;
				double d=Double.parseDouble(dmaxx);
//				System.out.println(Tmax);
//				System.out.println(Tmin);
				double eps = Double.parseDouble(epsx);
				int MinPts=Integer.parseInt(Minptsx);
				String hmera=(String) graphics.list5.getSelectedValue();
//				System.out.println(hmera);		 
				String hmera2=(String) graphics.list6.getSelectedValue();
//		 	    System.out.println(hmera2);
		 	    SimpleDateFormat dateformat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		 	    Date Start = null;
				Date End = null ;
				try {
					Start = dateformat.parse( hmera +" 00:00:00");
					End = dateformat.parse(hmera2 + " 23:59:59");
	//				System.out.println(Start);
	//				System.out.println(End);					
				} catch (java.text.ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(Start.before(End)){
			 	    er3 er = new er3();
			 	   // if(!er.er_3_2(eps, MinPts, Tmin, Tmax, d, gps, Start, End, battery )){
			 	   if(!er.er_3_2(eps, MinPts, Tmin, Tmax, d, gps, Start, End, battery , wifi , true)){
			 	    	graphics.displayErrorMessage("Δεν βρέθηκαν cendroids");
			 	    	return ;
			 	    }
				}
			}
			String temp =  System.getProperty("user.dir");
//			System.out.println(temp);
			String temp1  = temp.substring(0, temp.length()-18);
			String dammy = temp+"/my_ask.html";

			String url = "file:///" +dammy;

	        if (Desktop.isDesktopSupported()) {
	            // Windows
	            try {
					Desktop.getDesktop().browse(new URI(url));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        } else {
	            // Ubuntu
	            Runtime runtime = Runtime.getRuntime();
	            try {
					runtime.exec("/usr/bin/firefox -new-window " + url);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        }
		
			
			
			
		}
		}
	
		}

