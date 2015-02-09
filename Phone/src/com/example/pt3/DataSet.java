package com.example.pt3;

public class DataSet {

	
	DataSet dataset = null;
	
	public DataSet getInstance(){
		
		return (dataset==null)? new DataSet():dataset;
		
	}
	
	private DataSet(){
		
	}
}
