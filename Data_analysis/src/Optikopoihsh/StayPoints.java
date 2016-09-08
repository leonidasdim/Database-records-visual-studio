package Optikopoihsh;

import java.util.Date;


public class StayPoints  {
	
	double latitude;
	double longtitude;
	Date Tstart;
	Date Tend;
	boolean visited;
	boolean cluster_member ;
	int neighbours ;
	

	public StayPoints(double latitude, double longtitude, Date tstart, Date tend) {
		super();
		this.latitude = latitude;
		this.longtitude = longtitude;
		Tstart = tstart;
		Tend = tend;
		visited = false;
		neighbours  =0;
		cluster_member = false;
	}

	
	/// auto genetated getters and setters
	public boolean isCluster_member() {
		return cluster_member;
	}


	public void setCluster_member(boolean cluster_member) {
		this.cluster_member = cluster_member;
	}

	
	public int getNeighbours() {
		return neighbours;
	}
	
	public void setNeighbours(int neighbours) {
		this.neighbours = neighbours;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	public Date getTstart() {
		return Tstart;
	}
	public void setTstart(Date tstart) {
		Tstart = tstart;
	}
	public Date getTend() {
		return Tend;
	}
	public void setTend(Date tend) {
		Tend = tend;
	}
	
	

}
