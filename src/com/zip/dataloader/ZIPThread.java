package com.zip.dataloader;

import java.sql.Connection;

import com.geojason.GenerateGeoJson;

public class ZIPThread extends Thread{

	
    public ZIPThread(){
    	System.out.println("cnstructer");
	}

    public void start(){
    	//super.start();
    	//System.out.println("Start");
    }
	
	@Override
	public void run(){
		System.out.println("Run");
		
	}
	
	public static void main(String args[]){
		Thread t = new ZIPThread();
		t.start();
	}

}
