package com.geojason;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.db.connection.DatabaseConnector;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;
import com.union.GeometryUnion;
//import com.zip.dataloader.ZIPThread;

public class GenerateGeoJson {
//
//	 static File batchfolder=null;
//	 static File inputfiles=null;
//	 static File outPutFiles=null;
	 static String url = "https://vanitysoft-boundaries-io-v1.p.mashape.com/reaperfire/rest/v1/public/boundary";
	 static String batchfolderStr = "E://LOCAL_Territory_tool//files";
	 static String inputfilesStr = "E://LOCAL_Territory_tool//inputfilesNew";
	 static String inputfilesStr1 = "E://LOCAL_Territory_tool//inputfilesNew1";
	 static String outPutFilesStr = "E://LOCAL_Territory_tool//outputfiles_";
	 static String outPutFilesStr2 = "E://LOCAL_Territory_tool//final_out";
	 static String final_outPutFilesStr = "E://LOCAL_Territory_tool//final_outputFiles";
	 static String final_outPutFilesStr_ ="E://LOCAL_Territory_tool//final_outputFiles_";
	 static String final_outPutFiles106TStr_ ="E://LOCAL_Territory_tool//final_out113C";
	 static String final_outPutFiles113CStr ="E://LOCAL_Territory_tool//input_out113C";
	 public static void main(String[] args)  {
	String responseStr="";
    int count=0;
    try{
    	File folder = new File(final_outPutFiles106TStr_);
    	File[] listOfFiles = folder.listFiles();
    	
    GenerateGeoJson gJson = new GenerateGeoJson();
    Connection conn = DatabaseConnector.getInstance().getConnection();
    GeometryUnion dissolveGem = new GeometryUnion();
    JSONObject jObjProp = new JSONObject();
	JSONObject finaljObj = new JSONObject();
	for(String item : TerritoryConstantNew.getTerritory()){	
		count++;
		boolean exising=false;
		for (int i = 0; i < listOfFiles.length; i++) {
	    	 if(listOfFiles[i].getName().startsWith(item)){
	    		 exising=true;
	    		 break;
	    	 }
		}	
			boolean error=false;
		    if(!exising){
	    		 System.out.println(count);
	    			String resultZips = getZips(item);
	    			if(resultZips!=null && !"".equalsIgnoreCase(resultZips)){
//	    				zipThread=new ZIPThread(conn,resultZips,item,gJson);
//	    				zipThread.start();
	    				//listZipThread.add(zipThread);
	    		    String jsonStr = gJson.getGeometry(resultZips,conn);	
	    		    try{
	    		    responseStr = dissolveGem.dissolveGeometry(jsonStr);
	    		    }catch(Exception e){
	    		    	e.printStackTrace();
	    		    	gJson.writeContent(final_outPutFiles113CStr+"/"+item+".geojson", jsonStr);
	    		    	responseStr =dissolveBordersOfZip(final_outPutFiles113CStr+"/"+item+".geojson");
	    		    	System.out.println("Not merged - "+item);
	    		    	error=true;
	    		    }
	    		    
	                JSONObject contentObj = new JSONObject(responseStr);
	                jObjProp.put("title", TerritoryConstantNew.getTerritoryNameMapping(item));
	       			jObjProp.put("fill", TerritoryColorConstantNew.getTerritoryColors(item));
	       			jObjProp.put("id", item);
	     			jObjProp.put("fill-opacity", "0.3");
	     			jObjProp.put("stroke-opacity", "1");
	     			jObjProp.put("stroke-width", "2");
	     			jObjProp.put("stroke", "#6E6E6E");
	     			
	                // jObj = new JSONObject(content);
	     			
	                 finaljObj.put("properties",jObjProp);
	                 finaljObj.put("type","Feature");
	                 JSONObject gemObj = new JSONObject();
	                 if(!error){
	                  gemObj.put("type",contentObj.getString("type"));
	                  gemObj.put("coordinates",contentObj.getJSONArray("coordinates"));
	                 }else{
	                  gemObj.put("type",contentObj.getJSONArray("geometries").getJSONObject(0).getString("type"));
		              gemObj.put("coordinates",contentObj.getJSONArray("geometries").getJSONObject(0).getJSONArray("coordinates")); 
	                 }
	                 finaljObj.put("geometry", gemObj);
	    		     gJson.writeContent(final_outPutFiles106TStr_+"/"+item+".geojson",finaljObj.toString()); 
	    	 }else{
	 			//System.out.println("Invalid Result---"+resultZips);	
	 		}	
		   }
 
   }
// un-comment for merging	
//	mergeContent();
//	mergeToOneJson(files);
    }catch(Exception e){
    	e.printStackTrace();
    }finally{
    	DatabaseConnector.getInstance().closeConnection();
    }
 }

	public static void mergeContent() throws JSONException{
	      int count=0;
	      String files="";
	      final File inputfiles = new File(final_outPutFiles106TStr_);
	      GenerateGeoJson gJson = new GenerateGeoJson();
	      JSONArray featureArry = new JSONArray();
	      JSONObject finalMergeObj = new JSONObject();
	      for (final File fileEntry : inputfiles.listFiles()) {
	      	files+=" "+fileEntry.getName();
	      	String fileContent = readFile(final_outPutFiles106TStr_+"//"+fileEntry.getName());
	      	
//	      	JSONObject finalObj = new JSONObject();
//	      	JSONObject finalGemObj = new JSONObject();
//	      	JSONObject tempObj = new JSONObject(fileContent);
//	      	
//	      	finalGemObj.put("type",tempObj.getString("type"));
//	      	finalGemObj.put("coordinates",tempObj.getJSONArray("coordinates"));
//	      	
//	      	finalObj.put("properties",tempObj.getJSONObject("properties"));
//	      	finalObj.put("type", "Feature");
//	      	finalObj.put("geometry", finalGemObj);
//	          
//	      	files+= " "+fileEntry.getName();
	      	JSONObject tempObj = new JSONObject(fileContent);
	      	featureArry.put(tempObj);
	      	count++;
	      	System.out.println(count+" - "+fileEntry.getName()+" added!!");
//	        gJson.writeContent(final_outPutFilesStr_+"/"+fileEntry.getName(),finalObj.toString()); 
	      } 	
	           finalMergeObj.put("type", "FeatureCollection");
	           finalMergeObj.put("features",featureArry);
	           int mb = 1024*1024;
	           Runtime runtime = Runtime.getRuntime();
	           System.out.println("Used Memory:" + (runtime.totalMemory() - runtime.freeMemory()) / mb);
	           System.out.println("Total Memory:" + runtime.totalMemory() / mb);
	           System.out.println("Max Memory:" + runtime.maxMemory() / mb);
	           System.out.println("*****Writing final output*********");
	      	gJson.writeContent("E://LOCAL_Territory_tool//final_out_new//US113C_final.json",finalMergeObj.toString()); 
	}
	 
   public static String dissolveBordersOfZip() {
	   System.out.println("----Dissolving innner polygon borders-----");
		final File batchfolder = new File(batchfolderStr);
		final File inputfiles = new File(inputfilesStr);
		final File outPutFiles = new File(outPutFilesStr);
		String files ="";
		JSONObject jObj = null;
		JSONObject jObjProp = new JSONObject();
		JSONObject finaljObj = new JSONObject();
		 GenerateGeoJson gJson = new GenerateGeoJson();
		
		for (final File fileEntry : inputfiles.listFiles()) {
	       //fileEntry.isDirectory())    
			files+=" "+fileEntry.getName();
	        System.out.println(fileEntry.getName());
	        try {
	        	String cndStr = "cd \"C:\\Users\\ak185258\\AppData\\Roaming\\npm\\node_modules\\mapshaper\\bin\" && mapshaper -i E:\\LOCAL_Territory_tool\\inputfiles\\"+fileEntry.getName()+" -dissolve -o "+outPutFilesStr+"/"+fileEntry.getName();
	        	ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", cndStr);//-simplify 4%
                builder.redirectErrorStream(true);
                Process p = builder.start();
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while (true) {
                    line = r.readLine();
                    if (line == null) { break; }
                    System.out.println(line);
                }
                String territory = fileEntry.getName().substring(0,fileEntry.getName().indexOf("."));
                String content = readFile(outPutFilesStr+"/"+fileEntry.getName());
                JSONObject contentObj = new JSONObject(content);
                jObjProp.put("title", TerritoryConstant.getTerritoryNameMapping(territory));
      			jObjProp.put("fill", TerritoryColorConstant.getTerritoryColors(territory));
      			jObjProp.put("id", territory);
    			jObjProp.put("fill-opacity", "0.3");
    			jObjProp.put("stroke-opacity", "1");
    			jObjProp.put("stroke-width", "2");
    			jObjProp.put("stroke", "#6E6E6E");
    			
               // jObj = new JSONObject(content);
    			
                finaljObj.put("properties",jObjProp);
                finaljObj.put("type",contentObj.getJSONArray("geometries").getJSONObject(0).getString("type"));
                finaljObj.put("coordinates",contentObj.getJSONArray("geometries").getJSONObject(0).getJSONArray("coordinates"));
                
                System.out.println(finaljObj.toString());
                gJson.writeContent(outPutFilesStr+"/"+fileEntry.getName(),finaljObj.toString());
    			
				Thread.sleep(1000);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	    }
		
		return files;
	}
   
   public static String dissolveBordersOfZip(String fileNameStr) {
	   System.out.println("----Dissolving innner polygon borders-----");
		final File inpfile = new File(fileNameStr);
		String resPonseString ="";
		JSONObject finaljObj = new JSONObject();
		
	       //fileEntry.isDirectory())    
	        try {
	        	String cndStr = "cd \"C:\\Users\\ak185258\\AppData\\Roaming\\npm\\node_modules\\mapshaper\\bin\" && mapshaper -i "+fileNameStr+" -dissolve -o "+fileNameStr+"_tmp";
	        	ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", cndStr);//-simplify 4%
                builder.redirectErrorStream(true);
                Process p = builder.start();
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while (true) {
                    line = r.readLine();
                    if (line == null) { break; }
                    System.out.println(line);
                }
                
                System.out.println(finaljObj.toString());
                resPonseString = readFile(fileNameStr+"_tmp");
                //File tmpFile = new File(fileNameStr+"_tmp");
                //tmpFile.delete();
    			
				Thread.sleep(1000);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
		
		return resPonseString;
	}
   
   
   public static void mergeToOneJson(String files) {
	   System.out.println("----Merging all GeoJson-----");
		final File batchfolder = new File(batchfolderStr);
		final File inputfiles = new File(inputfilesStr);
		final File outPutFiles = new File(outPutFilesStr);
		
		
	       //fileEntry.isDirectory())     
	        try {
	        	ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd \"C:\\Users\\ak185258\\AppData\\Roaming\\npm\\node_modules\" && geojson-merge "+files+" > "+outPutFilesStr2+"/AllUS_territory.geojson");
                builder.redirectErrorStream(true);
                Process p = builder.start();
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while (true) {
                    line = r.readLine();
                    if (line == null) { break; }
                    System.out.println(line);
                }
				Thread.sleep(1000);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	}
	
	public static String callWebservice(String zipCodes) {
		// TODO Auto-generated method stub
		//logger.info("Checking New Messages on Service...");
		String responseStr="";
		try{
			HttpResponse<JsonNode> response = Unirest.get("https://vanitysoft-boundaries-io-v1.p.mashape.com/reaperfire/rest/v1/public/boundary?and=false&includepostal=false&limit=999&zipcode="+zipCodes)
			//.header("X-Mashape-Key", "h5Vb6s5fBAmshRf7loH5KOTyBKkTp157IkZjsnPIR6TzF7vraD")
			.header("X-Mashape-Key", "4mFzIwLcchmsh2BKcsqDOxMdRTCnp1AD2Q0jsn4x35Yh6KUEZx") // ranjan
			//.header("X-Mashape-Key", "yJzQi8hOe1mshElau5Kl1B98Zx0qp1eaUD4jsnJO7ybsDiVBP6") // sanjiv
			//.header("X-Mashape-Key", "omkqAIRVKwmshyrjae5btP1I69hzp1oXMc2jsnzfTXyknfZE0o") // sandeep
			.header("Accept", "application/json")
			.asJson();
//			
//			HttpResponse<JsonNode> response = Unirest.get("https://vanitysoft-boundaries-io-v1.p.mashape.com/reaperfire/rest/v1/public/boundary?and=false&includepostal=false&limit=30&state=DC&zipcode=20002%2C20037%2C20005")
//			.header("X-Mashape-Key", "h5Vb6s5fBAmshRf7loH5KOTyBKkTp157IkZjsnPIR6TzF7vraD")
//			.header("Accept", "application/json")
//			.asJson();

			System.out.println("statusLine:"+response.getStatus());
	        //System.out.println("statusLine:"+response.getBody());
	        
	        responseStr=response.getBody().toString();

			//parseJson(builder.toString());
		}catch(Exception e){
			System.out.println("Error at UpdateService:callWebservice()"+e);
			e.printStackTrace();
		}
		return responseStr;
	}

	public String getGeometry(String zipCodes, Connection conn) {
		// TODO Auto-generated method stub
		//logger.info("Checking New Messages on Service...");
		String gemometry="";
		Statement stmt = null;
		ResultSet rs = null;
		try{
			 JSONObject resObj = new JSONObject();
			 JSONArray resArray = new JSONArray();
			 String[] zipArr = zipCodes.split(",");
			 String quotedString="";
			 int count=0;
		        	 for(String zp:zipArr){
		        		 if(zp!=null){
		        			 if(zp.trim().length()==3){
		        					zp="00"+zp;
		        				}else if(zp.trim().length()==4){
		        					zp="0"+zp;
		        				}
		        			 quotedString+=",'"+zp+"'"; 
		        		 }
		        	 }
		        	 quotedString=quotedString.substring(1);
		        	 stmt=conn.createStatement();
		        	 String query="select geometry,zip_code from LOCALx.LOCAL_im_es_zip_geometry where zip_code in ("+quotedString+") and geometry is not null";
		        	 System.out.println(query);
		        	 rs = stmt.executeQuery(query);
		        	
		        	 while(rs.next()){
		        		 String line="";
			        	 StringBuffer strOut = new StringBuffer();
		        		 Clob clb = rs.getClob("geometry");
		        		 System.out.println(rs.getString("zip_code"));
		        		 if(clb!=null){
		        		 BufferedReader br = new BufferedReader(rs.getClob("geometry").getCharacterStream());
		        		 while ((line=br.readLine())!=null) {
		        		     strOut.append(line);
		        		    }
		        		// JSONObject finalTObj = new JSONObject();
		        		 JSONObject tObj = new JSONObject(strOut.toString());
//		        		 finalTObj.put("type","Feature");
//		        		 finalTObj.put("properties", tObj.getJSONObject("properties"));
//		        		 JSONObject tmpObj = new JSONObject();
//		        		 tmpObj.put("type", tObj.getString("type"));
//		        		 tmpObj.put("coordinates", tObj.getJSONArray("coordinates"));
//		        		 finalTObj.put("geometry",tmpObj);
	        			 resArray.put(tObj);
	        			 count++;	  
		        	 }
		        	}
			 resObj.put("geometries", resArray);
			 resObj.put("type", "GeometryCollection");//dissolveGeometry
			 gemometry = resObj.toString();
		}catch(Exception e){
			System.out.println("Error at UpdateService:callWebservice()"+e);
			e.printStackTrace();
		}finally{
			if(rs!=null){
	    		try{
	    		rs.close();
	    		}catch(Exception e){
	    			e.printStackTrace();
	    		}
	    	}
	    	if(stmt!=null){
	    		try{
	    		  stmt.close();
	    		}catch(Exception e){
	    			e.printStackTrace();
	    		}
	    	}
	}
		return gemometry;		
}
	
	
   public static String getZips(String territory){
	  
	 String result="";  
	 try{  
	   
	 File reqfile = new File(batchfolderStr+"//US113C.xls");
	 FileInputStream file = new FileInputStream(reqfile);
     HSSFWorkbook workbook = new HSSFWorkbook(file);
	 HSSFSheet details_sheet = workbook.getSheet("zips");	 
	 Iterator<Row> rowIterator = details_sheet.iterator();
	 String value="";
	 int count=0;
	 while (rowIterator.hasNext())
     {
         Row row = rowIterator.next();
         String zipVal="";
         String teriVal="";
         if(row.getCell(0)!=null && row.getCell(2)!=null && row.getCell(2)!=null){
         int terriCelltype = row.getCell(0).getCellType();
         if(terriCelltype==0){
           double terDbl = row.getCell(0).getNumericCellValue();
           teriVal = Double.toString(terDbl).replace(".0", "");
         }else if(terriCelltype==1){
           teriVal= row.getCell(0).getStringCellValue(); 
         }
         
         String teriName = row.getCell(1).getStringCellValue();
         
         int zipCelltype = row.getCell(2).getCellType();
         if(zipCelltype==0){
         double zipDbl = row.getCell(2).getNumericCellValue();
         zipVal = Double.toString(zipDbl).replace(".0", "");;
         }else if(zipCelltype==1){
        	 zipVal= row.getCell(2).getStringCellValue(); 
         }
         if(teriVal!=null && !"".equals(teriVal.trim()) && zipVal!=null && !"".equals(zipVal.trim()) && territory.trim().equalsIgnoreCase(teriVal.trim())){
               value+=","+zipVal.trim();  
               count++;
          }
	 
       }
         	
	 if(value.length()>1)
     result = value.substring(1);
     }
	// System.out.println(territory+"::"+value);  
	 System.out.println(count+"================Length ["+count+"] Reload Territory - "+territory);   
   }catch(Exception e){  
	  System.out.println("Error#########################");
	  e.printStackTrace(); 
   }
  return result;
 }
   
   public void writeContent(String filename, String content){

		File file = null;
		FileWriter fw =null;
		BufferedWriter bw = null;
		try{
			
			
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