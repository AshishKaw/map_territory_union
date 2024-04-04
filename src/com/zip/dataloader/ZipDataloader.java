package com.zip.dataloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dataloader.beans.GeometryBean;
import com.dataloader.util.CSVReadWrite;
import com.dataloader.util.DataloaderUtil;
import com.dataloader.util.QueryConstant;
import com.db.connection.DatabaseConnector;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

public class ZipDataloader {
	
	
private static ResourceBundle props = ResourceBundle.getBundle("dataloader", Locale.getDefault());
private static Logger logger = Logger.getLogger(ZipDataloader.class.getName());

private ArrayList<String> existingZipList = new ArrayList<String>();
private ArrayList<String> existingCountyList = new ArrayList<String>();
public static void main(String agrs[]){
	System.out.println("******Data Loader Started");
	try{
	//JSONObject jOb = new JSONObject(props.getString("json_test"));
	//DataloaderUtil.ConvertJsonToKml(jOb);
//		ZipDataloader zipDataloader = new ZipDataloader();
//	if(!props.getString("updateKml").equalsIgnoreCase("false")){
//		zipDataloader.updateKml();
//	}	
		Connection conn = DatabaseConnector.getInstance().getConnection();
		//readFile(conn);
		readFileJson(conn);
		//formatKmlJson(conn);
		//zipDataloader.updateTerritories("E:\\LOCAL_Territory_tool\\files\\File_without_range_new\\File_without_range_new.xls");
	   
	}catch(Exception e){
		e.printStackTrace();
	}finally{
    	DatabaseConnector.getInstance().closeConnection();
    }
	//ZipDataloader zipDataloader = new ZipDataloader();
	//zipDataloader.updateZipCodeCountyMapping();
	//zipDataloader.updateCountyGeometry();
}


public void updateKml(){
    try{
	Connection conn = DatabaseConnector.getInstance().getConnection();
	String updateKml = props.getString("updateKml");
	if(updateKml.equalsIgnoreCase("zip")){
	  updateZipKml(conn);
	}
    }catch(Exception e){
    	e.printStackTrace();
    }finally{
    	DatabaseConnector.getInstance().closeConnection();
    }
	
}

public void updateZipCodeCountyMapping(){
    try{
	Connection conn = DatabaseConnector.getInstance().getConnection();
	String operationType = props.getString("operationType");
	String operation = props.getString("operation");
	String loadGeometryFromWeb = props.getString("loadGeometryFromWeb");
	if(operationType.indexOf("ZPinsert")>-1 && operationType.indexOf("ZPupdate")>-1){
	   existingZipList = getOldZipCode(conn);
	}
	int update_count=0;
	int insert_count=0;
	List<GeometryBean> dataRowsList = getRowsFromCSVFile();
	if(operation.indexOf("ZPupdateGeometryFromWeb")>-1){
		updateZipGeometry(conn);
     }else if(operation.indexOf("ZPupdateGeometryFromFile")>-1){
	for(GeometryBean gbean:dataRowsList){
		if(loadGeometryFromWeb.equalsIgnoreCase("ZPtrue")){
            String zipGeometry = getZipGeometry(gbean.getZip_code());
            gbean.setGeometry(zipGeometry);
		 }
	   if(operationType.indexOf("ZPupdate")>-1 && operationType.indexOf("ZPinsert")>-1){
	    	if(existingZipList.contains(gbean.getZip_code())){
	    		int ucnt = updateZipRow(gbean, conn);
	    		if(ucnt==1)
	    		  update_count++;
	    	}else{
				int icnt =insertZipRow(gbean, conn);
				if(icnt==1)
					insert_count++;
	    	}
	    	
	     }else if(operationType.indexOf("ZPupdate")>-1){
		    int ucnt = updateZipRow(gbean, conn);
		    if(ucnt==1)
		       update_count++;
         }else if(operationType.indexOf("ZPinsert")>-1){
	    	int icnt =insertZipRow(gbean, conn);
			if(icnt==1)
				insert_count++;
       }
	 }
    }
	System.out.println("Rows Updated:"+update_count);
	System.out.println("Rows Inserted:"+insert_count);
	
    }catch(Exception e){
    	e.printStackTrace();
    }finally{
    	DatabaseConnector.getInstance().closeConnection();
    }
	
}

public void updateCountyGeometry(){
    try{
	Connection conn = DatabaseConnector.getInstance().getConnection();
	String operationType = props.getString("operationType");
	String operation = props.getString("operation");
	String loadGeometryFromWeb = props.getString("loadGeometryFromWeb");
	if(operationType.indexOf("CNinsert")>-1 && operationType.indexOf("CNupdate")>-1){
	   existingCountyList = getOldCountyID(conn);
	}
	int update_count=0;
	int insert_count=0;
	List<GeometryBean> dataRowsList = getRowsFromCSVFile();
	if(operation.indexOf("CNupdateGeometryFromWeb")>-1){
		updateZipGeometry(conn);
     }else if(operation.indexOf("CNupdateGeometryFromFile")>-1){
	for(GeometryBean gbean:dataRowsList){
		if(loadGeometryFromWeb.equalsIgnoreCase("CNtrue")){
            String countyKmlGeometry = getCountyKmlGeometry(gbean);
            gbean.setKml(countyKmlGeometry);
            String countyJsonGeometry = DataloaderUtil.ConvertKmlToJson(countyKmlGeometry);
            gbean.setGeometry(countyJsonGeometry);
		 }
	   if(operationType.indexOf("CNupdate")>-1 && operationType.indexOf("CNinsert")>-1){
	    	if(existingCountyList.contains(gbean.getCounty_id())){
//	    		int ucnt = updateCountyRow(gbean, conn);
//	    		if(ucnt==1)
	    		  update_count++;
	    		  System.out.println(update_count);
	    	}else{
				int icnt =insertCountyRow(gbean, conn);
				if(icnt==1)
					insert_count++;
	    	}
	    	
	     }else if(operationType.indexOf("CNupdate")>-1){
		    int ucnt = updateCountyRow(gbean, conn);
		    if(ucnt==1)
		       update_count++;
         }else if(operationType.indexOf("CNinsert")>-1){
	    	int icnt =insertCountyRow(gbean, conn);
			if(icnt==1)
				insert_count++;
       }
	 }
    }
	System.out.println("Rows Updated:"+update_count);
	System.out.println("Rows Inserted:"+insert_count);
	
    }catch(Exception e){
    	e.printStackTrace();
    }finally{
    	DatabaseConnector.getInstance().closeConnection();
    }
	
}

private ArrayList<String> getOldZipCode(Connection conn){
	ArrayList<String> existingZipList = new ArrayList<String>();
	ResultSet rs=null;
	Statement stmt=null;
	try{
	stmt = conn.createStatement();
	rs = stmt.executeQuery(QueryConstant.selectQueryZipCountyMap);
	while( rs.next()){
		existingZipList.add(rs.getString("zip_code"));
	}
	
    }catch(Exception e){
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
	return existingZipList;
}

private static HashMap<String,String> getZipCode(Connection conn){
	HashMap<String,String> noGemZipMap = new HashMap<String,String>();
	ResultSet rs=null;
	Statement stmt=null;
	try{
	stmt = conn.createStatement();
	rs = stmt.executeQuery(QueryConstant.selectQueryZipCountyMap);
	while( rs.next()){
		noGemZipMap.put(rs.getString("zip_code"),rs.getString("zip_code"));
	}
	
    }catch(Exception e){
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
	return noGemZipMap;
}

private ArrayList<String> getOldCountyID(Connection conn){
	ArrayList<String> existingCountyList = new ArrayList<String>();
	ResultSet rs=null;
	Statement stmt=null;
	try{
	stmt = conn.createStatement();
	rs = stmt.executeQuery(QueryConstant.selectQueryCountyTbl);
	while( rs.next()){
		String county_id=rs.getString("county_id");
		existingCountyList.add(county_id);
		System.out.println(county_id);
	}
	
    }catch(Exception e){
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
	return existingCountyList;
}

private int updateZipGeometry(Connection conn){
	ResultSet rs=null;
	Statement stmt=null;
	GeometryBean geometryBean = new GeometryBean();
	int updateCount=0;
	try{
	stmt = conn.createStatement();
	rs = stmt.executeQuery("select zip_code from LOCALX.LOCAL_IM_ES_ZIP_GEOMETRY where last_updated is null");
	while( rs.next()){
		geometryBean.setZip_code(rs.getString("zip_code"));
		String zipGeometry = getZipGeometry(geometryBean.getZip_code());
		geometryBean.setGeometry(zipGeometry);
		updateCount+= updateZipRow(geometryBean, conn);
	}
	
    }catch(Exception e){
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
	return updateCount;
}

private int updateZipKml(Connection conn){
	ResultSet rs=null;
	Statement stmt=null;
	GeometryBean geometryBean = new GeometryBean();
	int updateCount=0;
	try{
	stmt = conn.createStatement();
	rs = stmt.executeQuery("select zip_code,geometry from LOCALx.LOCAL_im_es_zip_geometry where zip_code in ('86029','79085')");
	while( rs.next()){
		String geometryClb = rs.getString(("geometry"));
		String zip_code = rs.getString(("zip_code"));
		//JSONObject jObj = new JSONObject(geometryClb);
		String jsonStr = DataloaderUtil.getJson(geometryClb);
		geometryBean.setKml(geometryClb);
		geometryBean.setGeometry(jsonStr);
		geometryBean.setZip_code(zip_code);
		updateCount+= updateZipRow(geometryBean, conn);
	}
	System.out.println("total updated :"+updateCount);
    }catch(Exception e){
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
	return updateCount;
}

private int updateTerritories(String fileName){
	int count=0;
	int res=0;
	Statement stmt=null;
	ResultSet rs=null;
	try{
	HashMap<String,String> territoryZipMap = DataloaderUtil.processFileOne(fileName);
	Connection conn = DatabaseConnector.getInstance().getConnection();
	HashMap<String,String> zMap = new HashMap<String,String>();
	stmt = conn.createStatement();
	rs = stmt.executeQuery("select zip_code from LOCALX.LOCAL_IM_ES_ZIP_GEOMETRY where territory is null");
	
	while(rs.next()){
		GeometryBean gBean = new GeometryBean();
		String zip = rs.getString("zip_code");
		zMap.put(zip,zip);
		gBean.setZip_code(zip);
		String territory = territoryZipMap.get(zip);
		if(territory!=null && !"".equalsIgnoreCase(territory)){
			gBean.setTerritory_desc(territory);
			gBean.setTerritory(territory.substring(0, 7));
			res = updateZipRow(gBean,conn);
		}
//		if(territory==null){
//			System.out.println(zip);
//			count++;
//		}
		if(res!=0){
			count++;
		}
		System.out.println(count);
	}
//	Set<String> zfSet = territoryZipMap.keySet();
//	ArrayList<String> zfList= new ArrayList<String>(zfSet);
//	for(String s:zfList){
//		if(zMap.get(s)==null){
//			System.out.println(s);
//			DataloaderUtil.writeFile(s);
//			count++;
//		}
//		
//	}
//	System.out.println(count);
	}catch(Exception e){
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
    	DatabaseConnector.getInstance().closeConnection();
    }
	return count;
	
}

private static int updateZipRow(GeometryBean geometryBean,Connection conn){
	int result =0;
	PreparedStatement pStmt=null;
    String updateSql = "update LOCALx.LOCAL_im_es_zip_geometry set  ";
	try{
	
	if(geometryBean.getCounty()!=null && !"".equals(geometryBean.getCounty())){
		updateSql+=" county = '"+geometryBean.getCounty()+"',";
	}
	if(geometryBean.getState()!=null && !"".equals(geometryBean.getState())){
		updateSql+=" state = '"+geometryBean.getState()+"',";
	}
	if(geometryBean.getCity()!=null && !"".equals(geometryBean.getCity())){
		updateSql+=" city = '"+geometryBean.getCity()+"',";
	}
	if(geometryBean.getLattitude()!=null && !"".equals(geometryBean.getLattitude())){
		updateSql+=" lattitude = '"+geometryBean.getLattitude()+"',";
	}
	if(geometryBean.getLongitude()!=null && !"".equals(geometryBean.getLongitude())){
		updateSql+=" longitude = '"+geometryBean.getLongitude()+"',";
	}
	if(geometryBean.getCountry_desc()!=null && !"".equals(geometryBean.getCountry_desc())){
		updateSql+=" country_desc = '"+geometryBean.getCountry_desc()+"',";
	}
	if(geometryBean.getCountry_code()!=null && !"".equals(geometryBean.getCountry_code())){
		updateSql+=" country_code = '"+geometryBean.getCountry_code()+"',";
	}
	if(geometryBean.getTerritory()!=null && !"".equals(geometryBean.getTerritory())){
		updateSql+=" territory = '"+geometryBean.getTerritory()+"',";
	}
	if(geometryBean.getTerritory_desc()!=null && !"".equals(geometryBean.getTerritory_desc())){
		updateSql+=" territory_desc = '"+geometryBean.getTerritory_desc()+"',";
	}
	if(geometryBean.getCounty()!=null && !"".equals(geometryBean.getCounty()) && geometryBean.getState()!=null && !"".equals(geometryBean.getState())){
		updateSql+=" county_id = '"+geometryBean.getState().toUpperCase()+"-"+geometryBean.getCounty().replaceAll(" ", "").toUpperCase()+"',";
	}
	if(geometryBean.getGeometry()!=null && !"".equals(geometryBean.getGeometry())){
		updateSql+=" geometry = ?,";
	}
	if(geometryBean.getKml()!=null && !"".equals(geometryBean.getKml())){
		updateSql+=" kml = ?,";
	}
	//updateSql = updateSql.substring(0,(updateSql.length()-1));
	updateSql+="  last_updated = sysdate  where zip_code = ? and geometry is null ";
	pStmt = conn.prepareStatement(updateSql);
	if(geometryBean.getGeometry()!=null && !"".equals(geometryBean.getGeometry()) && geometryBean.getKml()!=null && !"".equals(geometryBean.getKml())){
	 pStmt.setClob(1,DataloaderUtil.stringtoClob(geometryBean.getGeometry(), conn));
	 pStmt.setClob(2,DataloaderUtil.stringtoClob(geometryBean.getKml(), conn));
	 pStmt.setString(3,geometryBean.getZip_code());
	}else if(geometryBean.getGeometry()!=null && !"".equals(geometryBean.getGeometry())){
		 pStmt.setClob(1,DataloaderUtil.stringtoClob(geometryBean.getGeometry(), conn));
		 pStmt.setString(2,geometryBean.getZip_code());
	}else if(geometryBean.getKml()!=null && !"".equals(geometryBean.getKml())){
		 pStmt.setClob(1,DataloaderUtil.stringtoClob(geometryBean.getKml(), conn));
		 pStmt.setString(2,geometryBean.getZip_code());
	}else{
	 pStmt.setString(1,geometryBean.getZip_code());
	}
	result = pStmt.executeUpdate();
	logger.info(result+" "+geometryBean.getZip_code()+" Row Updated");
	
    }catch(Exception e){
    	e.printStackTrace();
    }finally{
    	if(pStmt!=null){
    		try{
    		  pStmt.close();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}
    }
	return result;
}

private int updateCountyRow(GeometryBean geometryBean,Connection conn){
	int result =0;
	PreparedStatement pStmt=null;
    String updateSql = "update LOCALx.LOCAL_im_es_county_geometry set  ";
	try{
	
	if(geometryBean.getCounty()!=null && !"".equals(geometryBean.getCounty())){
		updateSql+=" county = '"+geometryBean.getCounty()+"',";
	}
	if(geometryBean.getState()!=null && !"".equals(geometryBean.getState())){
		updateSql+=" state = '"+geometryBean.getState()+"',";
	}
	if(geometryBean.getCity()!=null && !"".equals(geometryBean.getCity())){
		updateSql+=" city = '"+geometryBean.getCity()+"',";
	}
	if(geometryBean.getCountry_desc()!=null && !"".equals(geometryBean.getCountry_desc())){
		updateSql+=" country_desc = '"+geometryBean.getCountry_desc()+"',";
	}
	if(geometryBean.getCountry_code()!=null && !"".equals(geometryBean.getCountry_code())){
		updateSql+=" country_code = '"+geometryBean.getCountry_code()+"',";
	}
	if(geometryBean.getTerritory()!=null && !"".equals(geometryBean.getTerritory())){
		updateSql+=" territory = '"+geometryBean.getTerritory()+"',";
	}
	if(geometryBean.getTerritory_desc()!=null && !"".equals(geometryBean.getTerritory_desc())){
		updateSql+=" territory_desc = '"+geometryBean.getTerritory_desc()+"',";
	}
	if(geometryBean.getGeometry()!=null && !"".equals(geometryBean.getGeometry())){
		updateSql+=" geometry = ?,";
	}
	if(geometryBean.getKml()!=null && !"".equals(geometryBean.getKml())){
		updateSql+=" kml = ?,";
	}
	//updateSql = updateSql.substring(0,(updateSql.length()-1));
	updateSql+=" last_updated = sysdate  where county_id = ? ";
	pStmt = conn.prepareStatement(updateSql);
	if(geometryBean.getGeometry()!=null && !"".equals(geometryBean.getGeometry()) && geometryBean.getKml()!=null && !"".equals(geometryBean.getKml())){
	 pStmt.setClob(1,DataloaderUtil.stringtoClob(geometryBean.getGeometry(), conn));
	 pStmt.setClob(2,DataloaderUtil.stringtoClob(geometryBean.getKml(), conn));
	 pStmt.setString(3,geometryBean.getCounty_id());
	}if(geometryBean.getGeometry()!=null && !"".equals(geometryBean.getGeometry())){
		 pStmt.setClob(1,DataloaderUtil.stringtoClob(geometryBean.getGeometry(), conn));
		 pStmt.setString(2,geometryBean.getCounty_id());
	}if(geometryBean.getKml()!=null && !"".equals(geometryBean.getKml())){
		 pStmt.setClob(1,DataloaderUtil.stringtoClob(geometryBean.getKml(), conn));
		 pStmt.setString(2,geometryBean.getCounty_id());
	}else{
	 pStmt.setString(1,geometryBean.getCounty_id());
	}
	result = pStmt.executeUpdate();
	logger.info(result+" "+geometryBean.getCounty_id()+" Row Updated");
	
    }catch(Exception e){
    	e.printStackTrace();
    }finally{
    	if(pStmt!=null){
    		try{
    		  pStmt.close();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}
    }
	return result;
}

private int insertZipRow(GeometryBean geometryBean,Connection conn){
	int result =0;
	PreparedStatement pStmt=null;
	try{
	pStmt = conn.prepareStatement(QueryConstant.insertQueryZipCountyMap);
	pStmt.setString(1,geometryBean.getZip_code());
	pStmt.setString(2, geometryBean.getCounty());
	if(geometryBean.getCounty()!=null && !"".equals(geometryBean.getCounty()) && geometryBean.getState()!=null && !"".equals(geometryBean.getState())){
	   pStmt.setString(3, geometryBean.getState().toUpperCase()+"-"+geometryBean.getCounty().replaceAll(" ", "").toUpperCase());
	}else{
	   pStmt.setString(3, "");	
	}
	pStmt.setString(4, geometryBean.getState());
	pStmt.setString(5, geometryBean.getCity());
	pStmt.setString(6, geometryBean.getLattitude());
	pStmt.setString(7, geometryBean.getLongitude());
	pStmt.setString(8, geometryBean.getCountry_desc());
	pStmt.setString(9, geometryBean.getCountry_code());
	pStmt.setString(10, geometryBean.getTerritory());
	pStmt.setString(11, geometryBean.getTerritory_desc());
	pStmt.setClob(12,DataloaderUtil.stringtoClob(geometryBean.getGeometry(), conn));
	result = pStmt.executeUpdate();
	//pStmt.addBatch();
	System.out.println(result+" New row inserted.");
    }catch(Exception e){
    	e.printStackTrace();
    }finally{
    	if(pStmt!=null){
    		try{
    		  pStmt.close();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}
    }
	return result;
}

private int insertCountyRow(GeometryBean geometryBean,Connection conn){
	int result =0;
	PreparedStatement pStmt=null;
	try{
	pStmt = conn.prepareStatement(QueryConstant.insertQueryCountyTbl);
	pStmt.setString(1,geometryBean.getCounty_id());
	pStmt.setString(2, geometryBean.getCounty());
	pStmt.setString(3, geometryBean.getState());
	pStmt.setString(4, geometryBean.getCity());
	pStmt.setString(5, "UNITED STATES"/*geometryBean.getCountry_desc()*/);
	pStmt.setString(6, "US"/*geometryBean.getCountry_code()*/);
	pStmt.setString(7, geometryBean.getTerritory());
	pStmt.setString(8, geometryBean.getTerritory_desc());
	pStmt.setClob(9,DataloaderUtil.stringtoClob(geometryBean.getGeometry(), conn));
	pStmt.setClob(10,DataloaderUtil.stringtoClob(geometryBean.getKml(), conn));
	result = pStmt.executeUpdate();
	//pStmt.addBatch();
	System.out.println(result+" New row inserted.");
    }catch(Exception e){
    	e.printStackTrace();
    }finally{
    	if(pStmt!=null){
    		try{
    		  pStmt.close();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}
    }
	return result;
}


public List<GeometryBean> getRowsFromCSVFile(){

	List<GeometryBean> dataRowsList = new ArrayList<GeometryBean>();
	dataRowsList = CSVReadWrite.readCSV(props.getString("csv_zip_county_map_file"));
	return dataRowsList;
}

public List<GeometryBean> getRowsFromWebFile(){
	List<GeometryBean> dataRowsList = new ArrayList<GeometryBean>();
	return dataRowsList; 
}
private static int Mashape_Key_Count = 0;
public static String getZipGeometry(String zipCode) {
    String responseString="";
	String Mashape_Key=props.getString("Mashape_Key"+Integer.toString(Mashape_Key_Count));
	String Mashape_URL=props.getString("Mashape_url");
	logger.info("Calling external webservise for "+zipCode);
	try{
		HttpResponse<JsonNode> response = Unirest.get(Mashape_URL+zipCode)
		.header("X-Mashape-Key", Mashape_Key) 
		.header("Accept", "application/json")
		.asJson();
        if(response.getStatus()!=200){
        	Mashape_Key_Count++;
        	if(Mashape_Key_Count==3){
        		Mashape_Key_Count=0;
        	}
        	System.out.println("Error: "+response.getStatus());
        }
		logger.info("-----Status-----:"+response.getStatus()+"-----");
		logger.info(response.getBody());
		logger.info("----------------End of body----------------");
        

		responseString = formatJson(response.getBody().toString(),zipCode);
		//parseJson(builder.toString());
	}catch(Exception e){
		System.out.println("Error at UpdateService:callWebservice()"+e);
		e.printStackTrace();
	}
	return responseString;
}

public static String getCountyKmlGeometry(GeometryBean geometryBean) {
    String properties ="";
    String responseString="";
	logger.info("getting kml for County "+geometryBean.getCounty_id());
	try{
		properties = "<name>"+geometryBean.getCounty()+"</name><countyID>"+geometryBean.getCounty_id().toUpperCase()+"</countyID><state>"+geometryBean.getState()+"</state><value>"+geometryBean.getValue()+"</value><geo_id>"+geometryBean.getGEO_ID()+
        "</geo_id><geo_id2>"+geometryBean.getGEO_ID2()+"</geo_id2><GeographicName>"+geometryBean.getGeographicName()+"</GeographicName><stateNum>"+geometryBean.getSTATE_NUM()+
        "</stateNum><countyNum>"+geometryBean.getCOUNTY_NUM()+"</countyNum><FIPSformula>"+geometryBean.getFIPSformula()+"</FIPSformula>";
		responseString = "<?xml version='1.0' encoding='UTF-8'?><Placemark>"+properties+geometryBean.getGeometry()+"</Placemark>";
	}catch(Exception e){
		System.out.println("Error at getCountyKmlGeometry()"+e);
		e.printStackTrace();
	}
	return responseString;
}


public static void readFile(Connection conn){
	
	File folder = new File("E:/generateGeoJson/inputfiles_allUs");
	File[] listOfFiles = folder.listFiles();
    HashMap<String,String> zipMap = getZipCode(conn);
    System.out.println("no gem zip list fetched!!");
	for (int i = 0; i < listOfFiles.length; i++) {
	  File file = listOfFiles[i];
	  System.out.println("reading file:"+file.getName());
	  if (file.isFile() && file.getName().endsWith(".geojson")) {
	    String content = getFileCont(file);
	    formatJsonLoop(content,conn,zipMap);
	  } 
	}
}

public static void readFileJson(Connection conn){
	
	File file = new File("C:/Users/ak185258/Downloads/2010_q2_ScanUS_ZIP/allUs.geojson");
    HashMap<String,String> zipMap = getZipCode(conn);
    
    System.out.println("reading file:"+file.getName());
	String content = getFileCont(file);
	formatJsonLoop(content,conn,zipMap);
}

public static String getFileCont(File file){
	String res="";
	try 
	{
		BufferedReader br = new BufferedReader(new FileReader(file));
		String sCurrentLine;
		System.out.println("reading file:---");
		while ((sCurrentLine = br.readLine()) != null) {
			res+=sCurrentLine;
		}
		System.out.println("file reading finished:---");
	} catch (IOException e) {
		e.printStackTrace();
	} 
	return res;
}

public static String formatJsonLoop(String jsonString,Connection conn, HashMap<String,String> zipMap) {
	    String resJson = "";
	    int count=0;
	    try{
		JSONObject jObjProp = new JSONObject(jsonString);
		JSONArray jsnArry = jObjProp.getJSONArray("features");
		System.out.println("Length:"+jsnArry.length());
		if(jsnArry.length()>0){
	    for(int i=0;i<jsnArry.length();i++){	
	    	System.out.println("i:"+i);
			JSONObject jsonProperties = jsnArry.getJSONObject(i).getJSONObject("properties");
			String zip = jsonProperties.getString("zipCode");
			if(zip.trim().length()==3){
				zip="00"+zip;
			}else if(zip.trim().length()==4){
				zip="0"+zip;
			}
			System.out.println(zip);
			if(zipMap.get(zip)!=null){
			JSONObject geometryObj = jsnArry.getJSONObject(i).getJSONObject("geometry");
			String type = geometryObj.getString("type");
			JSONArray cordinates = geometryObj.getJSONArray("coordinates");
			JSONObject finaljObj = new JSONObject();
			finaljObj.put("properties", jsonProperties);
			finaljObj.put("type", type);
			finaljObj.put("coordinates", cordinates);
	        resJson = finaljObj.toString();
	        GeometryBean gBean = new GeometryBean();
	        gBean.setZip_code(zip);
	        gBean.setGeometry(resJson);
	        String kmlStr = DataloaderUtil.ConvertJsonToKml(finaljObj);
	        gBean.setKml(kmlStr);
	        int result=updateZipRow(gBean,conn);
	        if(result>0){
	        	count++;
	        	System.out.println(count);
	        }
			}
	    }
		}else{
			System.out.println("******No Geometry Found for *********");
			logger.info("******No Geometry Found for *********");
		}
	//		Thread.sleep(1000);
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	   System.out.println("Total Updates:"+count);
		return resJson;
	}

public static String formatKmlJson(Connection conn) {
    String resJson = "";
    int count=0;
    int mb = 1024*1024;
    Runtime runtime = Runtime.getRuntime();
    System.out.println("Used Memory:" + (runtime.totalMemory() - runtime.freeMemory()) / mb);
    System.out.println("Total Memory:" + runtime.totalMemory() / mb);
    
    System.out.println("Max Memory:" + runtime.maxMemory() / mb);
    try{
    	HashMap<String,String> zipMap = getZipCode(conn);//new HashMap<String,String>();
    	System.out.println("reading doc...0");
    	File fXmlFile = new File("C:/Users/ak185258/Downloads/cb_2014_us_zcta510_500k (2)/cb_2014_us_zcta510_500k.kml");
    	System.out.println("reading doc...1");
    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    	Document doc = dBuilder.parse(fXmlFile);
    	doc.getDocumentElement().normalize();
    	System.out.println("Doc loaded");
    	NodeList nList = doc.getElementsByTagName("Placemark");
    	
    	for (int temp = 0; temp < nList.getLength(); temp++) {
    		Node nNode = nList.item(temp);
	    	String xml = DataloaderUtil.nodeToString(nNode);
	    	//System.out.println("kml:\n"+xml);
	    	String jsonStr =DataloaderUtil.getJson(xml);
    		JSONObject geometryObj = new JSONObject(jsonStr);
    		JSONObject jsonProp = geometryObj.getJSONObject("properties");
    		String zip  = jsonProp.getString("name");
    		if(zipMap.get(zip)!=null){
    			System.out.println(zip);
	            GeometryBean gBean = new GeometryBean();
	            gBean.setZip_code(zip);
	            gBean.setGeometry(jsonStr);
	            String kmlStr = DataloaderUtil.ConvertJsonToKml(geometryObj);
	            gBean.setKml(kmlStr);
	            int result=updateZipRow(gBean,conn);
	            if(result>0){
	            	count++;
	            }
	         }
	    	System.out.println(count);
    	}

	}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	}
        
	
	return resJson;
}

public static String formatJson(String jsonString,String zipCode) {
    String resJson = "";
    try{
	JSONObject jObjProp = new JSONObject(jsonString);
	JSONArray jsnArry = jObjProp.getJSONArray("features");
	if(jsnArry.length()>0){
	JSONObject jsonProperties = jsnArry.getJSONObject(0).getJSONObject("properties");
	JSONObject geometryObj = jsnArry.getJSONObject(0).getJSONObject("geometry");
	String type = geometryObj.getString("type");
	JSONArray cordinates = geometryObj.getJSONArray("coordinates");
	
	JSONObject finaljObj = new JSONObject();
	finaljObj.put("properties", jsonProperties);
	finaljObj.put("type", type);
	finaljObj.put("coordinates", cordinates);
    resJson = finaljObj.toString();
	}else{
		System.out.println("******No Geometry Found for "+zipCode+"*********");
		logger.info("******No Geometry Found for "+zipCode+"*********");
	}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	return resJson;
 }
}
