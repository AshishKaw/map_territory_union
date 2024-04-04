package com.union;

import java.awt.geom.Point2D;

public class Point extends Point2D{
	 double x, y;
	 
	 public Point(){
		 
	 }
   
   @Override
   public void setLocation(double x, double y){
   	 this.x=x;
		 this.y=y;
   }

	@Override
	public double getX() {
		// TODO Auto-generated method stub
		return this.x;
	}

	@Override
	public double getY() {
		// TODO Auto-generated method stub
		return this.y;
	}
}
