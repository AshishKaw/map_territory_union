package com.geojason;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONObject; 

import com.union.PolygonUtilities;

public class Compare {

 public static void main(String args[]){
	 
//	 String inputfilesStr = "E://NCR_Territory_tool//outputfiles//points";
//	 String outStr = "E://NCR_Territory_tool//final_out//us_AllPoints.geojson";
//	 String pointOldStr =  "E://NCR_Territory_tool//old//points.json";
// 	 File folder = new File(inputfilesStr);
// 	 File[] listOfFiles = folder.listFiles();
//	 
//	 JSONObject finalTerritory = new JSONObject();
//	 JSONObject newPointTerritory = new JSONObject();
//	 JSONObject newTerritory = new JSONObject();
//	 JSONObject finalobj = new JSONObject();
//	 
//	 
//	 try{
//		String pointfileContent = readFile(pointOldStr);
//		 JSONObject oldPointTerritory = new JSONObject(pointfileContent);
//		 JSONArray pointArray=oldPointTerritory.getJSONArray("features"); 
//		 int count=0;
//		 JSONArray finalobjArr = new JSONArray();
//		 for (final File fileEntry : listOfFiles) {
//		    	String fileContent = readFile(inputfilesStr+"//"+fileEntry.getName());
//		    	JSONObject obj = new JSONObject(fileContent);
//		    	for(int i =0;i<pointArray.length();i++){
//		    		String oldTerri = pointArray.getJSONObject(i).getJSONObject("properties").getString("title").trim();
//		    		String newTerri = obj.getJSONObject("properties").getString("title").trim();
//		    		if(newTerri.startsWith(oldTerri) && !newTerri.contains("Phoenix")){
//		    			JSONObject mainObj = new JSONObject();
//		    			mainObj.put("properties",obj.getJSONObject("properties"));
//		    			mainObj.put("type","Feature");
//		    			mainObj.put("geometry",pointArray.getJSONObject(i).getJSONObject("geometry"));
//		    			count++;
//		    			finalobjArr.put(mainObj);
//		    			System.out.println(count+"New:"+newTerri+" | old:"+oldTerri);
//		    			
//		    		}
//		    	}
//		    		
//		 }
//		 
//		 finalobj.put("features", finalobjArr);
//		 finalobj.put("type", "FeatureCollection");
//		 writeContent(outStr, finalobj.toString());
//		 System.out.println("Total points matched:"+count);
//	 }catch(Exception e){
//		 e.printStackTrace();
//	 }
	 
//	 
//	 String inputfilesStr = "E://NCR_Territory_tool//outputfiles//points";
//	 String outStr = "E://NCR_Territory_tool//final_out//us_All_geometry.geojson";
//	 String pointOldStr =  "E://NCR_Territory_tool//old//points.json";
//	 String oldTerritories = "E://NCR_Territory_tool//old//territories.json";
//	 String newTerritories = "E://NCR_Territory_tool//final_out//AllUS_final.json";
// 	 File folder = new File(inputfilesStr);
// 	 File[] listOfFiles = folder.listFiles();
//	 
//	 JSONObject finalTerritory = new JSONObject();
//	 JSONArray finalTerritoryArr = new JSONArray();
//	 JSONObject newPointTerritory = new JSONObject();
//	 JSONObject newTerritory = new JSONObject();
//	 JSONObject finalobj = new JSONObject();
	 
//	 
//	 try{
//		 String trrfileContent = readFile(oldTerritories);
//		 String trrNewfileContent = readFile(newTerritories);
//		 JSONObject oldtrrTerritory = new JSONObject(trrfileContent);
//		 JSONArray terrOldArray=oldtrrTerritory.getJSONArray("features"); 
//		 JSONObject newtrrTerritory = new JSONObject(trrNewfileContent);
//		 JSONArray terrNewArray=newtrrTerritory.getJSONArray("features"); 
//		 int count=0;
//		 //JSONArray finalobjArr = new JSONArray();
//		 for (int k =0;k<terrNewArray.length();k++) {
//			    boolean ismatch=false;
//		    	JSONObject obj = terrNewArray.getJSONObject(k);
//		    	String newTerri = obj.getJSONObject("properties").getString("title").trim();
//		    	for(int i =0;i<terrOldArray.length();i++){
//		    		String oldTerri = terrOldArray.getJSONObject(i).getJSONObject("properties").getString("title").trim();
//		    		if(newTerri.equalsIgnoreCase(oldTerri) && !newTerri.contains("Phoenix")){
//		    			JSONObject mainObj = new JSONObject();
//		    			mainObj.put("properties",obj.getJSONObject("properties"));
//		    			mainObj.put("type",obj.getString("type"));
//		    			mainObj.put("geometry", terrOldArray.getJSONObject(i).getJSONObject("geometry"));
//		    			finalTerritoryArr.put(mainObj);
//		    			count++;
//		    			System.out.println(count+"New:"+newTerri+" | old:"+oldTerri);
//		    			ismatch=true;
//		    		}
//		    	}
//		    	if(!ismatch){
//		    	  finalTerritoryArr.put(terrNewArray.getJSONObject(k));
//		    	}
//		 }
//		 
//		 finalTerritory.put("features", finalTerritoryArr);
//		 finalTerritory.put("type", "FeatureCollection");
//		 writeContent(outStr, finalTerritory.toString());
//		 System.out.println("Total points matched:"+count);
//	 }catch(Exception e){
//		 e.printStackTrace();
//	 }
	 
	 
	 
//	 try{
//		 readCSVFile();
//		 String newTerritories = "E://NCR_Territory_tool//final_out//us_All_geometry.geojson";
//		 String trrNewfileContent = readFile(newTerritories);
//		 String outStr = "E://NCR_Territory_tool//final_out//us_All_geometry_no_dup.geojson";
//		 JSONObject newtrrTerritory = new JSONObject(trrNewfileContent);
//		 JSONArray terrNewArray=newtrrTerritory.getJSONArray("features"); 
//		 int count=0;
//		 //JSONArray finalobjArr = new JSONArray();
//		 for (int k =0;k<terrNewArray.length();k++) {
//		    	JSONObject obj = terrNewArray.getJSONObject(k);
//		    	String newTerri = obj.getJSONObject("properties").getString("title").trim();
//		    	String newTerriId = obj.getJSONObject("properties").getString("id").trim();
//		    		if(idSet.get(newTerriId)!=null){
//		    			JSONObject mainObj = new JSONObject();
//		    			JSONObject propObj = new JSONObject();
//		    			
//		    			propObj.put("title",idSet.get(newTerriId));
//		    			propObj.put("id",newTerriId);
//		    			
//		    			propObj.put("stroke",obj.getJSONObject("properties").getString("stroke").trim());
//		    			propObj.put("fill",obj.getJSONObject("properties").getString("fill").trim());
//		    			propObj.put("fill-opacity",obj.getJSONObject("properties").getString("fill-opacity").trim());
//		    			propObj.put("stroke-opacity",obj.getJSONObject("properties").getString("stroke-opacity").trim());
//		    			propObj.put("stroke-width",obj.getJSONObject("properties").getString("stroke-width").trim());
//		    			
//		    			mainObj.put("properties",propObj);
//		    			mainObj.put("type",obj.getString("type"));
//		    			mainObj.put("geometry", obj.getJSONObject("geometry"));
//		    			finalTerritoryArr.put(mainObj);
//		    			count++;
//		    			System.out.println(count+"New:"+newTerri);
//		    		}else if(!delSet.contains(newTerriId)){
//				    	  finalTerritoryArr.put(terrNewArray.getJSONObject(k));
//				    }
//		    	}
//		 
//		 finalTerritory.put("features", finalTerritoryArr);
//		 finalTerritory.put("type", "FeatureCollection");
//		 writeContent(outStr, finalTerritory.toString());
//		 System.out.println("Total points matched:"+count);
//	 }catch(Exception e){
//		 e.printStackTrace();
//	 }
//	 
// }

	 
	 String inputfilesStr = "E://NCR_Territory_tool//final_out_new//US113C_final.json";
	 String outStr = "E://NCR_Territory_tool//final_out_new//US113C_USBoundries.json";
 	 File folder = new File(inputfilesStr);

	 JSONObject finalobj = new JSONObject();
	 
	 
	 try{
		 
		 int count=0;
		 String id_blank="";
		 String fileContent = readFile(inputfilesStr);
		 JSONObject obj = new JSONObject(fileContent);
		 JSONArray objArray=obj.getJSONArray("features"); 
		 JSONArray finalobjArr = new JSONArray();
		 for (int k =0;k<objArray.length();k++) {
		    	

		    		JSONObject mainObj = new JSONObject();
	    			mainObj.put("properties",objArray.getJSONObject(k).getJSONObject("properties"));
	    			mainObj.put("type",objArray.getJSONObject(k).getString("type"));
	    			mainObj.put("geometry",objArray.getJSONObject(k).getJSONObject("geometry"));
	    			//finalobjArr.put(mainObj);
	    			
	    			String typeOfPoly = objArray.getJSONObject(k).getJSONObject("geometry").getString("type");
	    			JSONArray polyAry = objArray.getJSONObject(k).getJSONObject("geometry").getJSONArray("coordinates");
	    			HashMap<String,Object> polyAttr = PolygonUtilities.getPolygonAttributes(typeOfPoly, polyAry);
	    			
	    			
	    			JSONObject gemObj = new JSONObject();
	    			JSONArray cordiAry = new JSONArray();	    			
	    			cordiAry.put((Double)polyAttr.get("centroidY"));
	    			cordiAry.put((Double)polyAttr.get("centroidX"));
	    			gemObj.put("coordinates",cordiAry);
	    			gemObj.put("type", "Point");
	    			
	    			JSONObject propObj = new JSONObject();
	    			try{
	    			propObj.put("id", objArray.getJSONObject(k).getJSONObject("properties").get("id"));
	    			}catch(Exception e){
	    				e.printStackTrace();
	    				propObj.put("id", "");
	    				id_blank+=objArray.getJSONObject(k).getJSONObject("properties").get("title")+";";
	    			}
	    			propObj.put("title", objArray.getJSONObject(k).getJSONObject("properties").get("title"));
	    			propObj.put("displayLevel", polyAttr.get("displayLevel"));
	    			propObj.put("area", polyAttr.get("area"));
	    			
	    			JSONObject pointObj = new JSONObject();
	    			pointObj.put("properties",propObj);
	    			pointObj.put("type","Feature");
	    			pointObj.put("geometry",gemObj);
//	    			String titleStr=((String)objArray.getJSONObject(k).getJSONObject("properties").get("title"));
//	    			if(titleStr.startsWith("US106T")){
	    			finalobjArr.put(mainObj);
	    			finalobjArr.put(pointObj);
	    			count++;
	    			//}
		    	}
		    		
		 
		 finalobj.put("features", finalobjArr);
		 finalobj.put("type", "FeatureCollection");
		 writeContent(outStr, finalobj.toString());
		 System.out.println("Total points matched:"+count);
		 System.out.println("Blank Id:\n"+id_blank);
	 }catch(Exception e){
		 e.printStackTrace();
	 }

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
 public static HashMap<String,String> idSet ;
 public static HashSet<String> delSet ;
 public static String readCSVFile(){
	   
		String content="";
		try{
		File file = new File("E:\\NCR_Territory_tool\\files\\duplicate_terri.csv");
		FileReader fr1 = new FileReader(file);   
		BufferedReader br = new BufferedReader(fr1);  
		String s = null;  
		idSet = new HashMap<String,String>();
		HashSet<String> zpSet = new HashSet<String>();
		HashSet<String> trrSet = new HashSet<String>();
		delSet = new HashSet<String>();
		HashMap<String,String> idMap = new HashMap<String,String>();

		while((s = br.readLine())!=null) { 
			String[] rowArr = s.split(","); 
			if(!zpSet.contains(rowArr[2])){
				String trrName=rowArr[1];
				if(rowArr[1].length()>24){
					trrName=rowArr[1].substring(0,24).trim();
				}
				if(rowArr[1].contains("Fin")){
					trrName+=" FIN";
				}else if(rowArr[1].contains("Ret") && !rowArr[1].contains("Alpharetta")){
					trrName+=" RET";
				}else if(rowArr[1].contains("Ins") && !rowArr[1].contains("Peninsula")){
					trrName+=" INS";
				}else if(rowArr[1].contains("Ins") && rowArr[1].contains("Peninsula")){
					trrName+=" INS";
				}
				
				idSet.put(rowArr[0],trrName);
				zpSet.add(rowArr[2]);
				trrSet.add(rowArr[1]);
				idMap.put(rowArr[2], rowArr[0]);
			}else{
				String terr = idMap.get(rowArr[2]);
				String name = idSet.get(terr);
				String trrName="";
				if(rowArr[1].contains("Fin")){
					trrName+=" FIN";
				}else if(rowArr[1].contains("Ret") && !rowArr[1].contains("Alpharetta")){
					trrName+=" RET";
				}else if(rowArr[1].contains("Ins") && !rowArr[1].contains("Peninsula")){
					trrName+=" INS";
				}else if(rowArr[1].contains("Ins") && rowArr[1].contains("Peninsula")){
					trrName+=" INS";
				}
				idSet.put(terr, name+trrName);
				delSet.add(rowArr[0]);
			}
		}
		
		System.out.println(idSet);
		System.out.println(delSet);
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		   
		return content;   
}
 
 public static void writeContent(String filename, String content){

		File file = null;
		FileWriter fw =null;
		BufferedWriter bw = null;
		try{
			
			//writeContent("", "");
			file = new File(filename);
			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(content);
		}catch(IOException e){
			System.out.println("Error at UpdateService:writeId()"+e);
		}finally{
			try {
				if(bw!=null)
					bw.close();
			} catch (IOException e) {
				System.out.println("Error at UpdateService:writeId()"+e);
			}
		}

	}
}
