package com.dataloader.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dataloader.beans.GeometryBean;

public class CSVReadWrite {

public static List<GeometryBean> readCSV(String file){
	List<GeometryBean> geometryBeanList = new ArrayList<GeometryBean>();
	String[] colList= {};
	HashMap<String,Integer> colMap = new HashMap<String,Integer>();
	try{
	FileReader fr1 = new FileReader(file);   
	BufferedReader br = new BufferedReader(fr1);  
	String line = null; 
	int count=0;
	while((line = br.readLine())!=null) { 
		//line = line.replaceAll("\"", "");
		String[] msgAry = line.split("\",\"");
		GeometryBean geometryBean = new GeometryBean();
		if(count==0){
			colList = msgAry;
			for(int i=0;i<colList.length;i++){
			    colMap.put(colList[i], i);
			}
		}else if(count>0){
			try{
			if(colMap.get("zip_code")!=null){
			 String zip = msgAry[colMap.get("zip_code")];
			 if(zip.length()==3){
				 zip = "00"+zip;
			 }else if(zip.length()==4){
				 zip = "0"+zip;
			 }
			 geometryBean.setZip_code(zip);
			}
			if(colMap.get("county")!=null)
			geometryBean.setCounty(msgAry[colMap.get("county")]);
			if(colMap.get("state")!=null)
			geometryBean.setState(msgAry[colMap.get("state")]);
			if(colMap.get("city")!=null)
			geometryBean.setCity(msgAry[colMap.get("city")]);
			if(colMap.get("latitude")!=null)
			geometryBean.setLattitude(msgAry[colMap.get("latitude")]);
			if(colMap.get("longitude")!=null)
			geometryBean.setLongitude(msgAry[colMap.get("longitude")]);
			if(colMap.get("country_desc")!=null)
			geometryBean.setCountry_desc(msgAry[colMap.get("country_desc")]);
			if(colMap.get("country_code")!=null)
			geometryBean.setCountry_code(msgAry[colMap.get("country_code")]);
			if(colMap.get("territory")!=null)
			geometryBean.setTerritory(msgAry[colMap.get("territory")]);
			if(colMap.get("territory_desc")!=null)
			geometryBean.setTerritory_desc(msgAry[colMap.get("territory_desc")]);
			if(colMap.get("geometry")!=null)
			geometryBean.setGeometry(msgAry[colMap.get("geometry")]);

			//------for county data
			if(colMap.get("county_id")!=null)
				geometryBean.setCounty_id(msgAry[colMap.get("county_id")].toUpperCase());

			if(colMap.get("value")!=null)
			geometryBean.setValue(msgAry[colMap.get("value")]);
			if(colMap.get("GEO_ID")!=null)
			geometryBean.setGEO_ID(msgAry[colMap.get("GEO_ID")]);
			if(colMap.get("GEO_ID2")!=null)
			geometryBean.setGEO_ID2(msgAry[colMap.get("GEO_ID2")]);
			if(colMap.get("GeographicName")!=null)
			geometryBean.setGeographicName(msgAry[colMap.get("GeographicName")]);
			if(colMap.get("STATE_NUM")!=null)
			geometryBean.setSTATE_NUM(msgAry[colMap.get("STATE_NUM")]);
			if(colMap.get("COUNTY_NUM")!=null)
			 geometryBean.setCOUNTY_NUM(msgAry[colMap.get("COUNTY_NUM")]);
			if(colMap.get("FIPSformula")!=null)
			 geometryBean.setFIPSformula(msgAry[colMap.get("FIPSformula")]);	
			
			geometryBeanList.add(geometryBean);
			
			}catch(ArrayIndexOutOfBoundsException ex){
				//ex.printStackTrace();
				System.out.println("Exception(ArrayIndexOutOfBoundsException) at count:"+count);
			}
		}
		count++;
	}
  }catch(Exception e){
	  e.printStackTrace();
  }

  return geometryBeanList;	
 }


private static HashMap<String,String> getReqMap(File reqfile){

	HashMap<String,String> reqMap = new HashMap<String,String>();
	try{
		FileReader fr1 = new FileReader(reqfile);   
		BufferedReader br = new BufferedReader(fr1);  
		String line = null; 
		int count=0;

		while((line = br.readLine())!=null) { 
			String[] msgAry = line.split(",");
			if(msgAry!=null && msgAry.length>1)
				reqMap.put(msgAry[0], msgAry[1]);
			count++;
		}
	}catch (Exception ex) {  
		ex.printStackTrace();

	}

	return reqMap;

}

}
