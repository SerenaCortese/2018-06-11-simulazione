package it.polito.tdp.ufo.model;

import java.time.Year;

public class AnnoAvvistamenti {
	
	private Year year;
	private int avvistamenti;
	
	public AnnoAvvistamenti(Year year, int avvistamenti) {
		super();
		this.year = year;
		this.avvistamenti = avvistamenti;
	}

	public Year getYear() {
		return year;
	}

	public void setYear(Year year) {
		this.year = year;
	}

	public int getAvvistamenti() {
		return avvistamenti;
	}
	
	public void setAvvistamenti(int avvistamenti) {
		this.avvistamenti = avvistamenti;
	}

	@Override
	public String toString() {
		return year + " (" + avvistamenti + ")";
	}

	
	
	
	

}
