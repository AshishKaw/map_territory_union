package com.dataloader.util;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.xml.serialize.OutputFormat;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import oracle.sql.CLOB;

public class DataloaderUtil {

	public static CLOB stringtoClob(String stringData, Connection conn) 
	{
	        CLOB clob = null;
	        try {
	            clob = CLOB.createTemporary(conn, false, CLOB.DURATION_SESSION);
	            clob.setString(1, stringData);
	        } catch (SQLException sqlException) {
	        	sqlException.printStackTrace();
	        }
	 
	        return clob;
	}
	
	public static String ConvertKmlToJson(String kml){
		String res="";
		
		try{
		 String typeGem = "";	
		 DocumentBuilderFactory factory =
		 DocumentBuilderFactory.newInstance();
		 DocumentBuilder builder = factory.newDocumentBuilder();
	     StringBuilder xmlStringBuilder = new StringBuilder();
	     xmlStringBuilder.append(kml);
	     ByteArrayInputStream input =  new ByteArrayInputStream(
			   xmlStringBuilder.toString().getBytes("UTF-8"));
		 Document doc = builder.parse(input);
		 doc.getDocumentElement().normalize();
		 
		 JSONObject prop = new JSONObject();
		 
		 Element pElement = (Element) doc.getDocumentElement();
		 String prop_name = pElement.getElementsByTagName("zipCode").item(0).getTextContent();
//		 String prop_county_id = pElement.getElementsByTagName("countyID").item(0).getTextContent();
//		 String prop_state = pElement.getElementsByTagName("state").item(0).getTextContent();
//		 String prop_value = pElement.getElementsByTagName("value").item(0).getTextContent();
//		 String prop_geoID = pElement.getElementsByTagName("geo_id").item(0).getTextContent();
//		 String prop_geoID2 = pElement.getElementsByTagName("geo_id2").item(0).getTextContent();
//		 String prop_GeographicName = pElement.getElementsByTagName("GeographicName").item(0).getTextContent();
//		 String prop_FIPScountyNum = pElement.getElementsByTagName("countyNum").item(0).getTextContent();
//		 String prop_FIPSstateNum = pElement.getElementsByTagName("stateNum").item(0).getTextContent();
//		 String prop_FIPSformula = pElement.getElementsByTagName("FIPSformula").item(0).getTextContent();
		 prop.put("name", prop_name);
//		 prop.put("county_id", prop_county_id);
//		 prop.put("state", prop_state);
//		 prop.put("value", prop_value);
//		 prop.put("geo_id", prop_geoID);
//		 prop.put("geo_id2", prop_geoID2);
//		 prop.put("GeographicName", prop_GeographicName);
//		 prop.put("FIPScountyNum", prop_FIPScountyNum);
//		 prop.put("FIPSstateNum", prop_FIPSstateNum);
//		 prop.put("FIPSformula", prop_FIPSformula);
		 
		 NodeList mgList = doc.getElementsByTagName("MultiGeometry");
		 JSONArray jsnCrdAry = new JSONArray();
		 if(mgList.getLength()>0){
		 typeGem="MultiPolygon";		 
	     NodeList pList = doc.getElementsByTagName("Polygon");	
		 for (int temp = 0; temp < pList.getLength(); temp++) {
			 JSONArray multiPolyAry = new JSONArray();
				Node nNode = pList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					JSONArray crdAry = new JSONArray();
					Element eElement = (Element) nNode;
					String cordinates = eElement.getElementsByTagName("coordinates").item(0).getTextContent(); 
					String[] cordiArry = cordinates.split(" ");
					for(String crd: cordiArry){
						JSONArray crdAr = new JSONArray();
						String[] crdLatLong = crd.split(",");
						double lat = Double.parseDouble(crdLatLong[0]);
						double lng = Double.parseDouble(crdLatLong[1]);
						crdAr.put(lat);
						crdAr.put(lng);
						crdAry.put(crdAr);
					}
					multiPolyAry.put(crdAry);
				}
				jsnCrdAry.put(multiPolyAry);
			}
		   
		 }else{
			 NodeList pList = doc.getElementsByTagName("Polygon");
			 typeGem="Polygon";
			 for (int temp = 0; temp < pList.getLength(); temp++) {
					Node nNode = pList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						JSONArray crdAry = new JSONArray();
						Element eElement = (Element) nNode;
						String cordinates = eElement.getElementsByTagName("coordinates").item(0).getTextContent(); 
						String[] cordiArry = cordinates.split(" ");
						for(String crd: cordiArry){
							JSONArray crdAr = new JSONArray();
							String[] crdLatLong = crd.split(",");
							double lat = Double.parseDouble(crdLatLong[0]);
							double lng = Double.parseDouble(crdLatLong[1]);
							crdAr.put(lat);
							crdAr.put(lng);
							crdAry.put(crdAr);
						}
						jsnCrdAry.put(crdAry);
					}
				}
		 }
		 
		 JSONObject finaljObj = new JSONObject();
		 finaljObj.put("properties", prop);
		 finaljObj.put("type", typeGem);
		 finaljObj.put("coordinates", jsnCrdAry);
		 res = finaljObj.toString();
	     System.out.println(res);
		 
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return res;
	}
	
	
	public static String nodeToString(Node node) {
	    StringWriter sw = new StringWriter();
	    try {
	      Transformer t = TransformerFactory.newInstance().newTransformer();
	      t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	      t.setOutputProperty(OutputKeys.INDENT, "yes");
	      t.transform(new DOMSource(node), new StreamResult(sw));
	    } catch (TransformerException te) {
	      System.out.println("nodeToString Transformer Exception");
	    }
	    return sw.toString();
	  }
	
	public static String getJson(String kml){
		String res="";
		
		try{
		 String typeGem = "";	
		 DocumentBuilderFactory factory =
		 DocumentBuilderFactory.newInstance();
		 DocumentBuilder builder = factory.newDocumentBuilder();
	     StringBuilder xmlStringBuilder = new StringBuilder();
	     xmlStringBuilder.append(kml);
	     ByteArrayInputStream input =  new ByteArrayInputStream(
			   xmlStringBuilder.toString().getBytes("UTF-8"));
		 Document doc = builder.parse(input);
		 doc.getDocumentElement().normalize();
		 
		 JSONObject prop = new JSONObject();
		 
		 Element pElement = (Element)doc.getDocumentElement();
		 //Element pElementSh =  (Element)doc.getElementsByTagName("SchemaData");
		 String prop_name = pElement.getElementsByTagName("SimpleData").item(0).getTextContent();
		 prop.put("name", prop_name);
		 NodeList mgList = doc.getElementsByTagName("MultiGeometry");
		 JSONArray jsnCrdAry = new JSONArray();
		 if(mgList.getLength()>0){
		 typeGem="MultiPolygon";		 
	     NodeList pList = doc.getElementsByTagName("Polygon");	
		 for (int temp = 0; temp < pList.getLength(); temp++) {
			 JSONArray multiPolyAry = new JSONArray();
				Node nNode = pList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					JSONArray crdAry = new JSONArray();
					Element eElement = (Element) nNode;
					String cordinates = eElement.getElementsByTagName("coordinates").item(0).getTextContent(); 
					String[] cordiArry = cordinates.split(" ");
					for(String crd: cordiArry){
						JSONArray crdAr = new JSONArray();
						String[] crdLatLong = crd.split(",");
						double lat = Double.parseDouble(crdLatLong[0]);
						double lng = Double.parseDouble(crdLatLong[1]);
						crdAr.put(lat);
						crdAr.put(lng);
						crdAry.put(crdAr);
					}
					multiPolyAry.put(crdAry);
				}
				jsnCrdAry.put(multiPolyAry);
			}
		   
		 }else{
			 NodeList pList = doc.getElementsByTagName("Polygon");
			 typeGem="Polygon";
			 for (int temp = 0; temp < pList.getLength(); temp++) {
					Node nNode = pList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						JSONArray crdAry = new JSONArray();
						Element eElement = (Element) nNode;
						String cordinates = eElement.getElementsByTagName("coordinates").item(0).getTextContent(); 
						String[] cordiArry = cordinates.split(" ");
						for(String crd: cordiArry){
							JSONArray crdAr = new JSONArray();
							String[] crdLatLong = crd.split(",");
							double lat = Double.parseDouble(crdLatLong[0]);
							double lng = Double.parseDouble(crdLatLong[1]);
							crdAr.put(lat);
							crdAr.put(lng);
							crdAry.put(crdAr);
						}
						jsnCrdAry.put(crdAry);
					}
				}
		 }
		 
		 JSONObject finaljObj = new JSONObject();
		 finaljObj.put("properties", prop);
		 finaljObj.put("type", typeGem);
		 finaljObj.put("coordinates", jsnCrdAry);
		 res = finaljObj.toString();
	     //System.out.println(res);
		 
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return res;
	}

	public static String getPolygonCordinates(JSONArray coordinateAry){
		String res = "";
		     try{
		     StringBuilder cordinates = new StringBuilder();
		     StringBuilder poly = new StringBuilder();
			 for(int i=0;i<coordinateAry.length();i++){
				 JSONArray coordinates =  coordinateAry.getJSONArray(i);
				 double lati = coordinates.getDouble(0);
				 double longi = coordinates.getDouble(1);
				 String sLati = Double.toString(lati);
				 String sLongi = Double.toString(longi);
				 String sLatLng = sLati+","+sLongi;
				 cordinates.append(sLatLng);
				 cordinates.append(" ");
			 }
			 poly.append("<Polygon><outerBoundaryIs><LinearRing><coordinates>");
			 poly.append(cordinates.toString().trim());
			 poly.append("</coordinates></LinearRing></outerBoundaryIs></Polygon>");
			
			 
			 res = poly.toString();
			 System.out.println("poly::"+res);
			 
		     }catch(Exception e){
		    	 e.printStackTrace();
		     }
			// doc.getDocumentElement().normalize();
		return res;
	}
	
	public static String ConvertJsonToKml(JSONObject jObj){
		String res="";
		String geometry="";
		String coordinatesStr="";
		StringBuilder kmlXml = new StringBuilder();
		try{
		 String gmType = jObj.getString("type");	
		 JSONObject objProp = jObj.getJSONObject("properties");
		 JSONArray cordinatesArry = jObj.getJSONArray("coordinates");
		 ArrayList<String> polyCordinateList = new ArrayList<String>();
		 if(gmType.equalsIgnoreCase("MultiPolygon")){
			for(int i=0; i<cordinatesArry.length();i++){
				polyCordinateList.add(getPolygonCordinates(cordinatesArry.getJSONArray(i).getJSONArray(0)));
				for(String val:polyCordinateList){
					 coordinatesStr+=val;
				}
				geometry = "<MultiGeometry>"+coordinatesStr+"</MultiGeometry>";
			}
		 }else if(gmType.equalsIgnoreCase("Polygon")){ 
			 polyCordinateList.add(getPolygonCordinates(cordinatesArry.getJSONArray(0)));
			 geometry=polyCordinateList.get(0);
		 }
		 
		 kmlXml.append("<?xml version='1.0' encoding='UTF-8'?>");
		 kmlXml.append("<Placemark>");
	     Iterator itr = objProp.keys();
		
	     while (itr.hasNext()){
	    	 String key = (String)itr.next();
	    	 kmlXml.append("<"+key+">"+objProp.getString(key)+"</"+key+">");
	     }
	     
	     kmlXml.append(geometry);
	     kmlXml.append("</Placemark>");
	     res = kmlXml.toString();
	     System.out.println("KML:"+res);
		 
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return res;
	}
	
	public static HashMap<String,String> processFileOne(String fileName1){
		Random random = new Random();
	    String currLine= "";
		File file1= new File(fileName1);
		
		String oData = "";
		
		HashMap<String,String> terrMap = new HashMap<String,String>();
		
	    
		try {
		    POIFSFileSystem fs1 = new POIFSFileSystem(new FileInputStream(file1));
		    
		    HSSFWorkbook wb1 = new HSSFWorkbook(fs1);
		    
		    HSSFSheet sheet1 = wb1.getSheetAt(0);
		   
		    HSSFRow row1;
		    
		    HSSFCell cell1;
		   
		    int rows1; // No of rows
		    
		    rows1 = sheet1.getPhysicalNumberOfRows();
		    
		    
		    for(int r = 1; r < rows1; r++) {
		        row1 = sheet1.getRow(r);
		        currLine = "";
		       // System.out.println("cell:"+row.getCell(0));
		        if(row1 != null && row1.getCell(0)!=null && !"".equals(row1.getCell(0).getStringCellValue().trim())) {
		        	try{
		                cell1 = row1.getCell(0);
		                String name =cell1.getStringCellValue();
		                cell1 = row1.getCell(1);
		                String zip =cell1.getStringCellValue();
		                terrMap.put(zip,name);
		               
		        }catch(Exception e){
		        	e.printStackTrace();
		        	currLine=currLine + "|*****Unable to read row****| ERROR:"+e.getMessage();;
		        }
			      
		       }
 
		   }
		  
		} catch(Exception ioe) {
		    ioe.printStackTrace();
		}
		
		return terrMap;
	}

	public static boolean writeFile(String content){

		File file = null;
		FileWriter fw =null;
		BufferedWriter bw = null;
		try{
	     	file = new File("E:\\LOCAL_Territory_tool\\files\\notFoundZips.txt");
			fw = new FileWriter(file.getAbsoluteFile(),true);
			bw = new BufferedWriter(fw);
			bw.write(content+"\n");
		}catch(Exception e){
			System.out.println("Error in ResponseService:writeFilenames()"+e);
			return false;
		}finally{
			try {
				if(bw!=null)
					bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Error in closing the buffered reader in ResponseService:writeFilenames()"+e);
				return false;
			}
		}
		return true;

	}
}
