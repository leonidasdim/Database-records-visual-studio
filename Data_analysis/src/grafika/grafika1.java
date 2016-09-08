package grafika;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import my_reader.Battery;
import my_reader.GPS;
import my_reader.my_battery;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;


public class grafika1 extends JFrame {

	

	ArrayList<String> list = new ArrayList<String>();
	
	
	JTabbedPane tabbedPane;	
	JPanel		panel1 ;
	private JTextField text,text2,Dmax,Tmin,Tmax,Dmax2,Tmin2,Tmax2,eps,Minpts;
	public JList list2 ;
	private JScrollPane listScroller;
	public JList list1,list3,list4,list5,list6;
	private JScrollPane listScroller2,listScroller3,listScroller4,listScroller5,listScroller6;
	private JButton b1;
	private JButton b2;
	private JButton b3;
	private JButton b4;
	private JButton b5;
	private JButton Stay_points,DBSCAN,Telecoms,Battery2;
	private JButton b6 ;
	JPanel		panel2;
	JPanel		panel3;
	JPanel		panel4;

	public grafika1()
	{
		
		
	
		super( "Tabbed Pane Application" );
		setSize( 500,300 );
		JPanel topPanel = new JPanel();
		topPanel.setLayout( new BorderLayout() );
		getContentPane().add( topPanel );
		createPage1();
		createPage3();
		createPage4();

		createPage2();
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab( "optical data", panel1 );
		
	tabbedPane.addTab( "data analyst", panel2 );
	tabbedPane.addTab( "data analyst2", panel3 );
	tabbedPane.addTab( "conclusions", panel4 );
		topPanel.add( tabbedPane, BorderLayout.CENTER );
		this.setVisible( true );
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
	}
	
	
	

	public void createPage1()
	{
		
		for(int i=3;i<=6;i++)
			for(int j=0;j<=31;j++){
				
				if((i==3) && (j>=27)){
					
					String data=Integer.toString(j)+"-"+Integer.toString(i)+"-"+"2015";
					list.add(data);
				}else if (i==4 && j<=30 && j!=0){
					
					String data=Integer.toString(j)+"-"+Integer.toString(i)+"-"+"2015";
					list.add(data);
				}else if(i==5 && j<=6 && j!=0){
					
					String data=Integer.toString(j)+"-"+Integer.toString(i)+"-"+"2015";
					list.add(data);
				}
				
		
			}
		
		
		
		String []dsf = new String[list.size()];
		list.toArray(dsf);
		
		
		 panel1 =new JPanel();
		 panel1.setLayout(null);
		JLabel label =new JLabel("enter user dataset :");
		 text=new JTextField();
		label.setSize(200,30);
	label.setLocation(0,0);
	text.setSize(150,20);
	text.setLocation(230,0);
		panel1.add(label);
		panel1.add(text);
		
		JLabel label2 =new JLabel("check time  :");
		label2.setSize(150,30);
		label2.setLocation(0,50);
		panel1.add(label2);
		
		 list2 = new JList(dsf); //data has type Object[]
		list2.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	//list2.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list2.setVisibleRowCount(-1);
	        Object listModel;
			//create the list
	         listScroller = new JScrollPane(list2);
	    //    listScroller.setPreferredSize(new Dimension(100, 100));
	        listScroller.setSize(100, 100);
	        listScroller.setLocation(170, 50);
	        JLabel label3 =new JLabel("Strat: :");
	    	label3.setSize(60,30);
	    	label3.setLocation(100,50);
	    	panel1.add(label3);
		panel1.add(listScroller);
		
		   JLabel label4 =new JLabel("End :");
	    	label4.setSize(60,30);
	    	label4.setLocation(280,50);
		panel1.add(label4);
		
		 list1= new JList(dsf); //data has type Object[]
		list1.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	//list2.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list1.setVisibleRowCount(-1);
	        //Object listModel;
			//create the list
	         listScroller2 = new JScrollPane(list1);
	    //    listScroller.setPreferredSize(new Dimension(100, 100));
	        listScroller2.setSize(100, 100);
	        listScroller2.setLocation(350, 50);
		
	    	panel1.add(listScroller2);
	    	 b1=new JButton("A.P");
	    	 b2=new JButton("Gps");
	    	 b3=new JButton("Battery");
	    	 b4=new JButton("Cells");
	    	 b5=new JButton("4.2 ");
	    	b1.setSize(100,30);
	    	b1.setLocation(20,200 );
	    	panel1.add(b1);
	    	b2.setSize(100,30);
	    	b2.setLocation(140,180 );
	    	panel1.add(b2);
	    	b3.setSize(100,30);
	    	b3.setLocation(260,200 );
	    	panel1.add(b3);
	    	b4.setSize(100,30);
	    	b4.setLocation(380,200 );
	    	panel1.add(b4);
	    	b5.setSize(100,30);
	    	b5.setLocation(140,215 );
	    	panel1.add(b5);
	    	
	}
	
	
	public void createPage2()
	{
	
		
		
		
		
		
		String []dsf = new String[list.size()];
		list.toArray(dsf);
		
		
		 panel2 =new JPanel();
		 panel2.setLayout(null);
		JLabel label =new JLabel("enter user dataset :");
		 text2=new JTextField();
		label.setSize(200,30);
	label.setLocation(0,0);
	text2.setSize(150,20);
	text2.setLocation(230,0);
		panel2.add(label);
		panel2.add(text2);
		
		JLabel label2 =new JLabel("check time  :");
		label2.setSize(150,30);
		label2.setLocation(0,50);
		panel2.add(label2);
		
		 list3 = new JList(dsf); //data has type Object[]
		list3.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	//list2.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list3.setVisibleRowCount(-1);
	        Object listModel;
			//create the list
	         listScroller3 = new JScrollPane(list3);
	    //    listScroller.setPreferredSize(new Dimension(100, 100));
	        listScroller3.setSize(100, 100);
	        listScroller3.setLocation(170, 50);
	        JLabel label3 =new JLabel("Strat: :");
	    	label3.setSize(60,30);
	    	label3.setLocation(100,50);
	    	panel2.add(label3);
		panel2.add(listScroller3);
		
		   JLabel label4 =new JLabel("End :");
	    	label4.setSize(60,30);
	    	label4.setLocation(280,50);
		panel2.add(label4);
		
		 list4= new JList(dsf); //data has type Object[]
		list4.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	//list2.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list4.setVisibleRowCount(-1);
	        //Object listModel;
			//create the list
	         listScroller4 = new JScrollPane(list4);
	    //    listScroller.setPreferredSize(new Dimension(100, 100));
	        listScroller4.setSize(100, 100);
	        listScroller4.setLocation(350, 50);
		
	    	panel2.add(listScroller4);
	    	Dmax=new JTextField();
	    	Dmax.setSize(80,20);
	    	Dmax.setLocation(20,170);
	    	panel2.add(Dmax);
	    	JLabel label5 =new JLabel(": Dmax");
	    	label5.setLocation(110,170);
	    	label5.setSize(80,20);
	    	panel2.add(label5);
	    	JLabel label6 =new JLabel("Tmin");
	    	Tmin=new JTextField("00:00:00");
	    	Tmin.setSize(80,20);
	    	Tmin.setLocation(170,170);
	    	panel2.add(Tmin);
	    	label6.setSize(80,20);
	    	label6.setLocation(250,170);
	    	panel2.add(label6);
	    	Tmax=new JTextField("00:00:00");
	    	Tmax.setSize(80,20);
	    	Tmax.setLocation(300,170);
	    	panel2.add(Tmax);
	    	JLabel label7 =new JLabel("Tmax");
	    	label7.setSize(80,20);
	    	label7.setLocation(400,170);
	    	panel2.add(label7);
	    	Stay_points=new JButton("Stay points");
	    	Stay_points.setSize(150,30);
	    	Stay_points.setLocation(200, 210);
	    	panel2.add(Stay_points);
		
		
		
	}
	
	
	
	public void createPage3(){
		
		
		String []dsf = new String[list.size()];
		list.toArray(dsf);
		
		
		 panel3 =new JPanel();
		 panel3.setLayout(null);
		 JLabel label2 =new JLabel("check time  :");
			label2.setSize(150,30);
			label2.setLocation(0,0);
			panel3.add(label2);
			
			list5 = new JList(dsf); //data has type Object[]
			list5.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		//list2.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			list5.setVisibleRowCount(-1);
		        Object listModel;
				//create the list
		         listScroller5 = new JScrollPane(list5);
		    //    listScroller.setPreferredSize(new Dimension(100, 100));
		        listScroller5.setSize(100, 100);
		        listScroller5.setLocation(170, 0);
		        JLabel label3 =new JLabel("Strat: :");
		    	label3.setSize(60,30);
		    	label3.setLocation(100,0);
		    	panel3.add(label3);
			panel3.add(listScroller5);
			
			   JLabel label4 =new JLabel("End :");
		    	label4.setSize(60,30);
		    	label4.setLocation(280,0);
			panel3.add(label4);
			
			 list6= new JList(dsf); //data has type Object[]
			list6.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		//list2.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			list6.setVisibleRowCount(-1);
		        //Object listModel;
				//create the list
		         listScroller6 = new JScrollPane(list6);
		    //    listScroller.setPreferredSize(new Dimension(100, 100));
		        listScroller6.setSize(100, 100);
		        listScroller6.setLocation(350, 0);
		        panel3.add(listScroller6);
		        Dmax2=new JTextField();
		    	Dmax2.setSize(80,20);
		    	Dmax2.setLocation(20,120);
		    	panel3.add(Dmax2);
		    	JLabel label5 =new JLabel(": Dmax");
		    	label5.setLocation(110,120);
		    	label5.setSize(80,20);
		    	panel3.add(label5);
		    	JLabel label6 =new JLabel("Tmin");
		    	Tmin2=new JTextField("00:00:00");
		    	Tmin2.setSize(80,20);
		    	Tmin2.setLocation(170,120);
		    	panel3.add(Tmin2);
		    	label6.setSize(80,20);
		    	label6.setLocation(250,120);
		    	panel3.add(label6);
		    	Tmax2=new JTextField("00:00:00");
		    	Tmax2.setSize(80,20);
		    	Tmax2.setLocation(300,120);
		    	panel3.add(Tmax2);
		    	JLabel label7 =new JLabel("Tmax");
		    	label7.setSize(80,20);
		    	label7.setLocation(400,120);
		    	panel3.add(label7);
		    	 eps=new JTextField("000.0");
		    	JLabel label8 =new JLabel(" :eps");
		        eps.setSize(80,20);
		        eps.setLocation(20,150);
		    	panel3.add(eps);
		    	label8.setSize(80,20);
		    	 label8.setLocation(110,150);
		    	 panel3.add(label8);
		    	 Minpts=new JTextField();
		    	Minpts.setSize(80,20);
		    	Minpts.setLocation(170,150);
		    	panel3.add(Minpts);
		    	JLabel label9 =new JLabel(" :Minpts");
		    	label9.setSize(80,20);
		    	 label9.setLocation(250,150);
		    	 panel3.add(label9);
		    	 DBSCAN=new JButton("DBSCAN");
		    		DBSCAN.setSize(100,30);
		    		DBSCAN.setLocation(150, 210);
		    		panel3.add(DBSCAN);
		    		 b6=new JButton("4.4");
			    		b6.setSize(100,30);
			    		b6.setLocation(270, 210);
			    		panel3.add(b6);
	}	
	
	
	public void createPage4()
	{
		 panel4 =new JPanel();
		 panel4.setLayout(null);
		
		Battery2=new JButton("Battery");
		Battery2.setSize(100,30);
		Battery2.setLocation(50,100);
		panel4.add(Battery2);
		Telecoms=new JButton("Telecoms");
		Telecoms.setSize(100,30);
		Telecoms.setLocation(200,100);
		panel4.add(Telecoms);
		
	}
	
	
	
	
	
	
	
	
	public String userDataset2(){
		return text2.getText();
	}
	public String userDataset(){
		return text.getText();
	}
	
	public String DateSt(){
		return  listScroller.toString();
	}
	
	public String DateEnd(){
		return  listScroller2.toString();
	}
	public String DateSt2(){
		return  listScroller3.toString();
	}
	
	public String DateEnd2(){
		return  listScroller4.toString();
	}
	
	public String Dmax(){
		return Dmax.getText();
	}
	public String Tmin(){
		return Tmin.getText();
	}
	public String Tmax(){
		return Tmax.getText();
	}
	public String Dmax2(){
		return Dmax2.getText();
	}
	public String Tmin2(){
		return Tmin2.getText();
	}
	public String Tmax2(){
		return Tmax2.getText();
	}
	public String eps(){
		return eps.getText();
	}
	public String Minpts(){
		return Minpts.getText();
	}
	void Button1 (ActionListener AP){
		b1.addActionListener(AP);
	}
	void Button2 (ActionListener AP){
		b2.addActionListener(AP);
	}
	void Button3 (ActionListener AP){
		b3.addActionListener(AP);
	}
	void Button4 (ActionListener AP){
		b4.addActionListener(AP);
	}
	void Stay_points (ActionListener AP){
		Stay_points.addActionListener(AP);
	}
	void DBSCAN (ActionListener AP){
		DBSCAN.addActionListener(AP);
	}
	void Battery2 (ActionListener AP){
		Battery2.addActionListener(AP);
	}
	void Telecoms (ActionListener AP){
		Telecoms.addActionListener(AP);
	}
	void Button5 (ActionListener AP){
		b5.addActionListener(AP);
	}
	void Button6 (ActionListener AP){
		b6.addActionListener(AP);
	}
	void displayErrorMessage(String errorMessage){
		JOptionPane.showMessageDialog(this, errorMessage);
	}
			
			
			
			
	/*public JPanel createPanelNORTH(){
		JPanel panel3 =new JPanel();
		JButton b1 = new JButton("SERCH");
		b1.setPreferredSize(new Dimension(100,30));
		panel3.add(b1);
		
		return panel3;
		
	}
	
	public JPanel createSouthPanel(){
		
		JPanel panel1 =new JPanel();
		JTextField t1 = new JTextField();
		GridLayout g1 = new  GridLayout(1,2,10,10 );
		t1.setPreferredSize(new Dimension(150,20));
		panel1.setLayout(g1);
		panel1.add( new JLabel( "name for database" ) );
		panel1.add(t1);
		
		//panel1.add( t1);

		
		return panel1;
	}
	public JPanel createPanelCenter2(){
		String []dsf = new String[list.size()];
		list.toArray(dsf);
		
		JPanel panel2 =new JPanel();
		
		JList list2 = new JList(dsf); //data has type Object[]
		list2.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	//	list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list2.setVisibleRowCount(-1);
	        Object listModel;
			//create the list
	        JScrollPane listScroller = new JScrollPane(list2);
	        listScroller.setPreferredSize(new Dimension(100, 100));
	       
	        
		
		panel2.add(listScroller);
		
		
		
		return panel2;

	}*/
/*	public JPanel createSouthPanel2(){
		
		String []dsf = new String[list.size()];
		list.toArray(dsf);
		
		JPanel panel2 =new JPanel();
		JList list = new JList(dsf); //data has type Object[]
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	//	list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		 JScrollPane listScroller2 = new JScrollPane(list);
	        listScroller2.setPreferredSize(new Dimension(100, 100));
	        listScroller2.setLocation(2000,23);
		panel2.add(listScroller2);
		
		return panel2;
	}
	public JPanel createPanelCenter3(){
		JPanel panel3 =new JPanel();
		JLabel label =new JLabel("valte to xroniko perithorio");
		label.setSize(150,30);
		panel3.add(label);
		
		
		return panel3;
	}
	public JPanel createPanelCenter(){
		

		
		for(int i =0 ; i < list.size(); i ++)
		 System.out.println(list.get(i));
		JPanel panel2 =new JPanel();
		
	
		panel2.setLayout(new BorderLayout());
		panel2.add(createPanelCenter3(), BorderLayout.NORTH );
		panel2.add(createSouthPanel2(), BorderLayout.WEST );
		panel2.add(createPanelCenter2(), BorderLayout.CENTER );
		
		return panel2;
	}
	

	public void createPage1()
	{
		
		
		
		panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		
		panel1.add(createSouthPanel(), BorderLayout.NORTH );
		panel1.add(createPanelCenter(), BorderLayout.CENTER );
		panel1.add(createPanelNORTH(), BorderLayout.SOUTH );
		
	}
*/
	
	
	
	
	
	
	
	

}
