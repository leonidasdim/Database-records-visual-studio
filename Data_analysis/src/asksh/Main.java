package asksh;


import java.io.File;
import java.net.URL;


import my_reader.my_GPS;
import my_reader.my_wifi;
import my_reader.my_battery;
import my_reader.my_BaseStation;
//import grafika.MyBrowser;
import grafika.grafika1;


public class Main  {
	public static void main(String[] args){

		my_battery battery = new my_reader.my_battery();
		my_reader.my_GPS gps ;
		my_reader.my_wifi wifi ;
		gps = new my_reader.my_GPS();
		wifi =new my_reader.my_wifi();
		my_BaseStation base= new my_reader.my_BaseStation();
		grafika.grafika1 my_graph =new grafika.grafika1();
		grafika.controll my_graph2 =new grafika.controll(my_graph , battery ,gps ,wifi , base);
		my_graph.getGraphics();

	}
	 
}
