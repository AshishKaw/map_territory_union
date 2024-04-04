package com.geojason;

import java.util.HashMap;

public class TerritoryColorConstantNew {

	public static String getTerritoryColors(String territory){
	 String color ="white";
	 
	 final HashMap<String,String> tNameMap = new HashMap<String,String>();	
		
 
//	tNameMap.put("6394","#FF69B4");
//	tNameMap.put("4452","#DDA0DD");
//	tNameMap.put("4451","#CD6839");
//	tNameMap.put("4450","#FF00FF");
//	tNameMap.put("6393","#8B3A62");
//	tNameMap.put("6392","#1C86EE");
//	tNameMap.put("6680","#D15FEE");
//	tNameMap.put("6679","#B452CD");
//	tNameMap.put("6678","#9400D3");
//	tNameMap.put("6677","#EE82EE");
//	tNameMap.put("6164","#B23AEE");
//	tNameMap.put("6163","#EEC900");
//	tNameMap.put("6161","#FFD700");
//	tNameMap.put("6162","#E3CF57");
//	 
//	 tNameMap.put("7137","#E3CF57");
//	 tNameMap.put("7138","#FFD700");
//	 tNameMap.put("7139","#B23AEE");
//	 tNameMap.put("7140","#9400D3");
//	 tNameMap.put("7141","#FF00FF");
//	 tNameMap.put("7142","#FF69B4");
//	 tNameMap.put("7143","#EE82EE");
//	 tNameMap.put("7144","#FF00EE");
//	 tNameMap.put("7145","#DDA0DD");
//	 tNameMap.put("7146","#FF69B4");
//	 tNameMap.put("7147","#CD6839");
//	 tNameMap.put("7148","#D15FEE");
//	 tNameMap.put("7149","#D45CEE");
//	 tNameMap.put("7150","#B23AEE");
//	 tNameMap.put("7151","#B452CD");
//	 tNameMap.put("7152","#FF69B4");
//	 tNameMap.put("7153","#DA81F5");
//	 tNameMap.put("7154","#58FAAC");
//	 tNameMap.put("7155","#9A2EFE");
//	 tNameMap.put("7156","#61380B");
//	 tNameMap.put("7157","#F781BE");
//	 tNameMap.put("7158","#F6CED8");
//	 tNameMap.put("7159","#0B3861");
//	 tNameMap.put("7160","#243B0B");
//	 tNameMap.put("7161","#82FA58");

	 tNameMap.put("8137","#2E2EFE");
	 tNameMap.put("8138","#2E2EFE");
	 tNameMap.put("8139","#8A084B");
	 tNameMap.put("8140","#2A1B0A");
	 tNameMap.put("8141","#088A29");
	 tNameMap.put("8142","#F5A9D0");
	 tNameMap.put("8143","#DF013A");	 
	 tNameMap.put("8136","#084B8A");
	 tNameMap.put("8135","#8A084B");

	 
	 color = tNameMap.get(territory);
	 
	 if(color==null){
		 
		 color = "#fffff";
	 }
	 return color;	
	}
	
		
}
