package Optikopoihsh;

import java.util.ArrayList;

//o kodikas par8ike apo to	 https://en.wikipedia.org/wiki/DBSCAN
public class bdscan {
//	private double MinPts;
//	private double e ;

	public ArrayList <ArrayList<StayPoints>>  DBSCAN(ArrayList <StayPoints> list, double eps, int MinPts) {
	  ArrayList < ArrayList < StayPoints > > clusters = new ArrayList <ArrayList<StayPoints>> ();
	  System.out.println("Stay Points Found "+list.size()); 
	  for (int i = 0; i < list.size(); i++) { 											// each point P in dataset D {
		      if(!(list.get(i).isVisited())){											// if P is visited
		    	  																		// continue next point
	    	  list.get(i).setVisited(true);												// mark P as visited
		      ArrayList <StayPoints> NeighborPts = regionQuery(i, list, eps);
		      if (NeighborPts.size() >= MinPts){										// if sizeof(NeighborPts) < MinPts
		    	  																		// mark P as NOISE , opote to afhnw 0
		    	  																		// else {
		         
		         clusters.add( expandCluster(i, list, NeighborPts,  eps, MinPts)); 
		      }
	      }
	   }
	   return clusters;
	}

	ArrayList<StayPoints> expandCluster(int index, ArrayList <StayPoints> list, ArrayList<StayPoints> NeighborPts,  double eps, int MinPts) {
	   ArrayList <StayPoints> clusteraki = new ArrayList <StayPoints> (); 
	   clusteraki.add(list.get(index));													// add P to cluster C
	   for(int i = 0; i < NeighborPts.size(); i ++){									// for each point P' in NeighborPts { 
		   if(! NeighborPts.get(i).isVisited() ){										// if P' is not visited {
			   NeighborPts.get(i).setVisited(true);										// mark P' as visited
			   int pros = list.indexOf(NeighborPts.get(i));					// briskw tin 8esi tou stin basiki lista
			   ArrayList <StayPoints> temp_Neighbours = regionQuery(pros, list, eps );// NeighborPts' = regionQuery(P', eps) , ara briskei tous geitones...
			   if(temp_Neighbours.size()>= MinPts) {									// if sizeof(NeighborPts') >= MinPts
				   for(int j = 0 ; j < temp_Neighbours.size() ; j ++){							// NeighborPts = NeighborPts joined with NeighborPts'
					   if(NeighborPts.contains(temp_Neighbours.get(j))){
						   NeighborPts.add(temp_Neighbours.get(j));
					   }
				   }
	        }
	      }
	      if(!NeighborPts.get(i).isCluster_member()) {									 // if P' is not yet member of any cluster
	    	  clusteraki.add(NeighborPts.get(i));										 // add P' to cluster C
	    	  NeighborPts.get(i).setCluster_member(true);								 // kanto na anikei se cluster
	      }
	  }
	   return clusteraki;
	}

	ArrayList <StayPoints> regionQuery(int index , ArrayList <StayPoints> list, double eps){
		ArrayList <StayPoints> neighbours = new ArrayList <StayPoints> ();				/// return all points within P's eps-neighborhood (including P)
		neighbours.add(list.get(index));
		er3 temp = new er3();
		for(int i = 0; i < list.size() ; i ++){
			if(temp.distance(list.get(index).getLatitude(), list.get(i).getLatitude(), list.get(index).getLongtitude(), list.get(i).getLongtitude()) <=eps)
				neighbours.add(list.get(i));
		}
		
		
		return neighbours;
	}
	

}
