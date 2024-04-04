package com.union;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.operation.union.CascadedPolygonUnion;
import com.vividsolutions.jts.operation.union.UnionInteracting;
import com.vividsolutions.jts.operation.union.UnaryUnionOp;


public class GeometryUnion {
	
	public String dissolveGeometry(String content) throws Exception{
		GeometryFactory gemtryFtry = new GeometryFactory();
		GeoJsonReader gRdr = new GeoJsonReader();
		Collection<Geometry> gmList = new ArrayList<Geometry>();
		System.out.println(content);
//		String content2 = readFile("E:\\test2.json");
		JSONObject contentObj = new JSONObject(content);
		JSONArray polyArray = contentObj.getJSONArray("geometries");
		for(int i =0; i<polyArray.length();i++){
			Geometry gm = gRdr.read(polyArray.getJSONObject(i).toString());	
			gmList.add(gm);
			//System.out.println(gm.toString());
		}
		
//		System.out.println(ab.length());
//		JSONObject contentObj2 = ab.getJSONObject(0);
//		
//		JSONObject jObject3 = new JSONObject(contentObj.getJSONArray("features").get(0));
//		contentObj2.put("type", "GeometryCollection");
//		System.out.println(contentObj2);
//		jObject3.put("geometries", contentObj.getJSONArray("features"));
//		System.out.println(jObject3);
		
//		Geometry gm = gRdr.read(content);
//		gmList.add(gm);
//		System.out.println(gm.toString());
	//	Geometry gm2 = gRdr.read(content2);
	//	gmList.add(gm2);
//		System.out.println("------gm2---------\n");
//		System.out.println(gm2.toString());
		GeometryUnion cuObj = new GeometryUnion();
//		
//		BoundaryOp bnOp = new BoundaryOp(gm);
//		Geometry bnGm = bnOp.getBoundary();
//		
//		System.out.println("\n\n----Boundry----\n"+bnGm.toString());
//		
		Geometry gmUnion  = cuObj.computeUnion(gmList);
		System.out.println("\n\n----Union----\n"+gmUnion.toString());
		
		GeoJsonWriter jsonWriter = new GeoJsonWriter();
		
		String resultJson = jsonWriter.write(gmUnion);
		System.out.println("\n\n----Json Result----\n"+resultJson);
		
//		GeometryFactory gt = new GeometryFactory();
//		Coordinate coord = new Coordinate( 1, 1 );
//		Coordinate[] crdntes;
//		crdntes[0] = coord;
//		LinearRing lr = gemtryFtry.createLinearRing(crdntes);
//		//ply.addPoint(1.2, 2.1);
		return resultJson;
	}
	
	public Geometry computeUnion (Collection<Geometry> geom) {
		Geometry resGeomatry =null;
	if (geom!=null && geom.size() > 0) {
		try{
		 resGeomatry  = CascadedPolygonUnion.union(geom);
		}catch(Exception e){
			try{
			resGeomatry =UnaryUnionOp.union(geom);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
 }
	return resGeomatry;
	}
	
	public static String readFile(String fileName){
		   
		String content="";
		try{
		File file = new File(fileName);
		FileReader fr1 = new FileReader(file);   
		BufferedReader br = new BufferedReader(fr1);  
		String s = null;  

		while((s = br.readLine())!=null) { 
			//System.out.println(s);  
			content+=s;
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		   
		return content;   
	   }
	
}
