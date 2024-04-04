package com.geojason;

import java.io.ObjectOutputStream.PutField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TerritoryConstantNew {

	public static List<String> getTerritory(){
		
		List<String> terriList = new ArrayList<String>();
		

		terriList.add("8142");
		terriList.add("8140");
		terriList.add("8139");
		
		terriList.add("8143");
		terriList.add("8138");
		terriList.add("8141");
		
		terriList.add("8137");
		terriList.add("8136");
		terriList.add("8135");
		
		
		return terriList;
	}
	
	public static String getTerritoryNameMapping(String id){
		HashMap<String,String> tNameMap = new HashMap<String,String>();
		String res="";
//		tNameMap.put("7137","US106T Chelsea");
//		tNameMap.put("7138","US106T Chelsea North");
//		tNameMap.put("7139","US106T Downtown");
//		tNameMap.put("7140","US106T Downtown East");
//		tNameMap.put("7141","US106T Downtown North");
//		tNameMap.put("7142","US106T Downtown West");
//		tNameMap.put("7143","US106T East 23rd");
//		tNameMap.put("7144","US106T East Harlem");
//		tNameMap.put("7145","US106T East Village");
//		tNameMap.put("7146","US106T Greenwich Village");
//		tNameMap.put("7147","US106T Lower East Side");
//		tNameMap.put("7148","US106T Lower East Side North");
//		tNameMap.put("7149","US106T Midtown East");
//		tNameMap.put("7150","US106T Midtown East North");
//		tNameMap.put("7151","US106T Midtown East South");
//		tNameMap.put("7152","US106T Midtown West");
//		tNameMap.put("7153","US106T Midtown West Norh");
//		tNameMap.put("7154","US106T Midtown West South");
//		tNameMap.put("7155","US106T Morningside Heights");
//		tNameMap.put("7156","US106T Soho");
//		tNameMap.put("7157","US106T Tribeca");
//		tNameMap.put("7158","US106T Upper East Side to 80");
//		tNameMap.put("7159","US106T Upper East Side to 97");
//		tNameMap.put("7160","US106T Upper West Side to 76");
//		tNameMap.put("7161","US106T Upper West Side to 91");

		tNameMap.put("8137","US113D6 Harrisburg PA");
		tNameMap.put("8138","US113C Philly PA");
		tNameMap.put("8139","US113C Delmarva DE/MD");
		tNameMap.put("8140","US113C Chester PA");
		tNameMap.put("8141","US113C Reading PA");
		tNameMap.put("8142","US113C Allentown PA");
		tNameMap.put("8143","US113C Dover DE");		
		tNameMap.put("8136","US133M4 Florida Keys");
		tNameMap.put("8135","US133K1 Ft Lauderdale FL");				




		if(tNameMap.get(id)!=null){
			res = tNameMap.get(id);
		}
		return res;
	}
	
	public static ArrayList<String> oldTerritory(){
	// boolean res=false;
	  ArrayList<String> oldTerri = new ArrayList<String>();
		  
	  
	 return oldTerri;
	 }
	
}
