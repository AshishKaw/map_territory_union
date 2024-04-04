package com.dataloader.util;

public class QueryConstant {

	public static final String insertQueryZipCountyMap = "insert into im_es_zip_geometry (zip_code,county,county_id,state,city,lattitude,longitude,country_desc,country_code,territory,territory_desc,geometry,last_updated) values(?,?,?,?,?,?,?,?,?,?,?,?,sysdate)";
	public static final String updateQueryZipCountyMap = "";
	public static final String selectQueryZipCountyMap = "select zip_code from im_es_zip_geometry where geometry is null and territory is not null";
	
	// county
	
	public static final String insertQueryCountyTbl = "insert into im_es_county_geometry (county_id,county,state,city,country_desc,country_code,territory,territory_desc,geometry,kml,last_updated) values(?,?,?,?,?,?,?,?,?,?,sysdate)";
	public static final String updateQueryCountyTbl = "";
	public static final String selectQueryCountyTbl = "select county_id from im_es_county_geometry";
	
}