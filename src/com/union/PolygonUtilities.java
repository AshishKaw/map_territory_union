package com.union;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class PolygonUtilities {

	/**
	 * Function to calculate the area of a polygon, according to the algorithm
	 * 
	 * @param polyPoints
	 *            array of points in the polygon
	 * @return area of the polygon defined by pgPoints
	 */
	public static double area(Point2D[] polyPoints) {
		int i, j, n = polyPoints.length;
		double area = 0;

		for (i = 0; i < n; i++) {
			j = (i + 1) % n;
			area += polyPoints[i].getX() * polyPoints[j].getY();
			area -= polyPoints[j].getX() * polyPoints[i].getY();
		}
		area /= 2.0;
		return (area);
	}

	/**
	 * Function to calculate the center of mass for a given polygon, according
	 * ot the algorithm defined at
	 * 
	 * @param polyPoints
	 *            array of points in the polygon
	 * @return point that is the center of mass
	 */
	public static Point2D centerOfMass(Point2D[] polyPoints) {
		double cx = 0, cy = 0;
		double area = area(polyPoints);
		// could change this to Point2D.Float if you want to use less memory
		Point2D res = new Point2D.Double();
		int i, j, n = polyPoints.length;

		double factor = 0;
		for (i = 0; i < n; i++) {
			j = (i + 1) % n;
			factor = (polyPoints[i].getX() * polyPoints[j].getY()
					- polyPoints[j].getX() * polyPoints[i].getY());
			cx += (polyPoints[i].getX() + polyPoints[j].getX()) * factor;
			cy += (polyPoints[i].getY() + polyPoints[j].getY()) * factor;
		}
		area *= 6.0f;
		factor = 1 / area;
		cx *= factor;
		cy *= factor;
		res.setLocation(cx, cy);
		return res;
	}
	
	 public static HashMap<String,Object>getPolygonAttributes(String type,JSONArray ptArray) 
	    {
		 HashMap<String,Object> attributeMap = new HashMap<String,Object>();
	    try{
	    	
	    	
			double totalArea=0;
			double centrX=0;
			double centrY=0;
			String displayLevel="3";
			 
		        System.out.println("Centroid Algorithm Test\n");
		       
		        if(type.equalsIgnoreCase("MultiPolygon")){
		        	Point2D[] points=new Point2D[0];
		        	double area=0;
		        	double lastArea=0;
		        	for (int i = 0; i < ptArray.length(); i++)
		            {
		        		JSONArray multiArray = ptArray.getJSONArray(i);
		        		JSONArray innerArray = multiArray.getJSONArray(0);
		        		int length = innerArray.length();
		        		points = new Point2D[length];
		        		for (int k = 0; k < innerArray.length(); k++){
		        			JSONArray cordiArray = 	innerArray.getJSONArray(k);
		        			Point2D p = new Point();
		        			p.setLocation(cordiArray.getDouble(1),cordiArray.getDouble(0));
		        			points[k]=p;
		        		}
		        		area = area(points)*1000;
		        		if(area>lastArea){
		        			lastArea = area;
		        			totalArea+=area;
		        			Point2D cordi = centerOfMass(points);
		        			centrX= cordi.getX();
		        			centrY= cordi.getY();
		        		}
		            } 
        			
		        }else if(type.equalsIgnoreCase("Polygon")){		        	   
		        		JSONArray innerArray = ptArray.getJSONArray(0);
		        		int length = innerArray.length();
		        		Point2D[] points = new Point2D[length];
		        		for (int k = 0; k < innerArray.length(); k++){
		        			JSONArray cordiArray = 	innerArray.getJSONArray(k);
		        			Point2D p = new Point();
		        			p.setLocation(cordiArray.getDouble(1),cordiArray.getDouble(0));
		        			points[k]=p;
		        		}
		        		totalArea= area(points)*1000;
	        			Point2D cordi = centerOfMass(points);
	        			centrX= cordi.getX();
	        			centrY= cordi.getY();
		        }
		        
		        if(totalArea<100000 && totalArea>8000){
		        	displayLevel="0";
	            }else if(totalArea<8000 && totalArea>1000){
	            	displayLevel="1";
	            }else if(totalArea<1000 && totalArea>100){
		        	displayLevel="2";
		        }else if(totalArea<100 && totalArea>8){
		        	displayLevel="3";
		        }else if(totalArea<8 && totalArea>0){
		        	displayLevel="4";
		        }
		        	
		        System.out.println("DisplayLevel:"+displayLevel);
		        System.out.println("Total Area:"+totalArea);
		        System.out.println("Centroid:"+centrX+","+centrY);
		        
		        attributeMap.put("displayLevel",displayLevel);
		        attributeMap.put("area",totalArea);
		        attributeMap.put("centroidX",centrX);
		        attributeMap.put("centroidY",centrY);
		        
		        
	    }catch(Exception e){
	    	 e.printStackTrace();
	     }
	    return attributeMap;
	  }
	 
	 public static void main(String args[]){
		 String data = "{\"type\":\"Polygon\",\"coordinates\":[[[-73.9736328125,40.71861267089844],[-73.99260711669922,40.72413635253906],[-73.990966796875,40.74542236328125],[-73.97156524658203,40.74299240112305],[-73.9736328125,40.71861267089844]]]}";
	    	//String data = "{\"type\":\"MultiPolygon\",\"coordinates\":[[[[-72.03682708740234,41.24984359741211],[-72.02049255371094,41.2758674621582],[-71.9942855834961,41.27094650268555],[-71.99297332763672,41.27955627441406],[-71.96749114990234,41.27318572998047],[-72.0024642944336,41.25286865234375],[-72.01712036132812,41.25542449951172],[-72.03682708740234,41.24984359741211]]],[[[-73.04244995117188,40.785335540771484],[-73.0220947265625,40.793357849121094],[-73.02110290527344,40.83014678955078],[-73.00869750976562,40.828739166259766],[-73.00785827636719,40.848846435546875],[-73.02354431152344,40.86779022216797],[-73.03514862060547,40.88850402832031],[-73.03606414794922,40.896244049072266],[-73.00955963134766,40.904090881347656],[-73.0109634399414,40.9152946472168],[-73.03398132324219,40.92832946777344],[-73.04497528076172,40.952754974365234],[-73.04473876953125,40.964195251464844],[-72.99593353271484,40.96649932861328],[-72.9548568725586,40.96611785888672],[-72.91383361816406,40.962467193603516],[-72.88825225830078,40.96296310424805],[-72.8331298828125,40.970115661621094],[-72.77410125732422,40.96531295776367],[-72.74565887451172,40.96844482421875],[-72.7083740234375,40.97775650024414],[-72.67316436767578,40.97867202758789],[-72.63555908203125,40.981956481933594],[-72.5853271484375,40.99758529663086],[-72.51798248291016,41.04011917114258],[-72.47730255126953,41.05221176147461],[-72.4592514038086,41.06636047363281],[-72.44524383544922,41.086116790771484],[-72.4179458618164,41.087955474853516],[-72.39700317382812,41.09630584716797],[-72.38255310058594,41.114532470703125],[-72.37012481689453,41.1197395324707],[-72.3541259765625,41.13995361328125],[-72.3216323852539,41.13871765136719],[-72.31277465820312,41.14800262451172],[-72.2787857055664,41.158721923828125],[-72.26809692382812,41.154144287109375],[-72.24534606933594,41.161216735839844],[-72.23821258544922,41.159488677978516],[-72.25474548339844,41.13398742675781],[-72.27873992919922,41.122222900390625],[-72.3128433227539,41.14032745361328],[-72.32662963867188,41.13216018676758],[-72.33827209472656,41.114906311035156],[-72.33517456054688,41.10691833496094],[-72.32099914550781,41.100582122802734],[-72.3172378540039,41.08865737915039],[-72.30181121826172,41.08237075805664],[-72.2803726196289,41.08040237426758],[-72.28309631347656,41.067874908447266],[-72.2736587524414,41.05153274536133],[-72.26051330566406,41.04206466674805],[-72.24124908447266,41.044769287109375],[-72.21747589111328,41.040611267089844],[-72.20921325683594,41.034454345703125],[-72.19055938720703,41.03257751464844],[-72.16289520263672,41.05318832397461],[-72.15385437011719,41.05186080932617],[-72.13729858398438,41.0396842956543],[-72.13740539550781,41.02390670776367],[-72.12673950195312,41.015628814697266],[-72.11637115478516,40.99979782104492],[-72.095458984375,40.99134826660156],[-72.08303833007812,40.99645233154297],[-72.076171875,41.00909423828125],[-72.06291198730469,41.01131820678711],[-72.05542755126953,41.02225875854492],[-72.02549743652344,41.02051544189453],[-71.99925994873047,41.039669036865234],[-71.99140930175781,41.04880905151367],[-71.97222900390625,41.04084014892578],[-71.9572525024414,41.04827880859375],[-71.9595947265625,41.07123565673828],[-71.91938781738281,41.08051681518555],[-71.90432739257812,41.08599090576172],[-71.89549255371094,41.0773811340332],[-71.86956024169922,41.07504653930664],[-71.85621643066406,41.07059860229492],[-71.88050079345703,41.048797607421875],[-71.95881652832031,41.02672576904297],[-72.02935791015625,40.999908447265625],[-72.11444854736328,40.972084045410156],[-72.2292251586914,40.929988861083984],[-72.33899688720703,40.887481689453125],[-72.3958511352539,40.866661071777344],[-72.4699935913086,40.84273910522461],[-72.57430267333984,40.813011169433594],[-72.66307830810547,40.788551330566406],[-72.74520874023438,40.76708984375],[-72.72986602783203,40.77933120727539],[-72.7232666015625,40.77545928955078],[-72.6679916381836,40.78984451293945],[-72.66197204589844,40.80329895019531],[-72.69361877441406,40.79707336425781],[-72.71426391601562,40.801002502441406],[-72.72437286376953,40.80924987792969],[-72.7326431274414,40.79960250854492],[-72.75384521484375,40.80229187011719],[-72.75228118896484,40.790897369384766],[-72.76332092285156,40.78203582763672],[-72.79667663574219,40.78584289550781],[-72.81220245361328,40.7768669128418],[-72.81156158447266,40.75779342651367],[-72.83535766601562,40.75275421142578],[-72.84586334228516,40.74565124511719],[-72.8768310546875,40.73600387573242],[-72.88556671142578,40.74305725097656],[-72.88858795166016,40.758113861083984],[-72.91801452636719,40.76022720336914],[-72.92887878417969,40.757266998291016],[-72.9441909790039,40.74494552612305],[-72.96630096435547,40.7430419921875],[-72.98031616210938,40.74878692626953],[-73.01663970947266,40.74810028076172],[-73.02447509765625,40.75206756591797],[-73.0351333618164,40.76611328125],[-73.04570770263672,40.766517639160156],[-73.04244995117188,40.785335540771484]],[[-72.58863830566406,40.93153762817383],[-72.6003189086914,40.93430709838867],[-72.6152114868164,40.92881774902344],[-72.61175537109375,40.91189193725586],[-72.57138061523438,40.90717315673828],[-72.56394958496094,40.91523361206055],[-72.5515365600586,40.91447067260742],[-72.52222442626953,40.9014778137207],[-72.50191497802734,40.895347595214844],[-72.4728775024414,40.900001525878906],[-72.44791412353516,40.92295455932617],[-72.44300842285156,40.94551086425781],[-72.40644073486328,40.94989013671875],[-72.37451934814453,40.98848342895508],[-72.37097930908203,40.99625778198242],[-72.34967041015625,40.99800491333008],[-72.33251953125,40.99504470825195],[-72.3167724609375,41.010772705078125],[-72.32960510253906,41.02424240112305],[-72.3331298828125,41.03618621826172],[-72.37019348144531,41.02387619018555],[-72.39387512207031,41.07246017456055],[-72.40010070800781,41.0755729675293],[-72.41453552246094,41.05872344970703],[-72.38672637939453,41.04560470581055],[-72.39022827148438,41.029876708984375],[-72.4361343383789,41.03565216064453],[-72.43557739257812,41.018882751464844],[-72.44752502441406,41.018802642822266],[-72.4348373413086,40.98880386352539],[-72.46892547607422,41.00321578979492],[-72.47433471679688,40.988983154296875],[-72.49063873291016,40.99101257324219],[-72.52310180664062,40.98078536987305],[-72.53520202636719,40.974212646484375],[-72.54792785644531,40.95549011230469],[-72.57569122314453,40.93354034423828],[-72.58863830566406,40.93153762817383]],[[-72.57164764404297,40.821170806884766],[-72.55559539794922,40.82075500488281],[-72.53395080566406,40.82645034790039],[-72.49662017822266,40.84096145629883],[-72.48827362060547,40.84086608886719],[-72.44358825683594,40.85526657104492],[-72.43194580078125,40.856895446777344],[-72.44158935546875,40.87510681152344],[-72.46521759033203,40.88125228881836],[-72.49307250976562,40.883121490478516],[-72.4906997680664,40.87147521972656],[-72.50457763671875,40.84880447387695],[-72.53314208984375,40.84961700439453],[-72.54246520996094,40.856849670410156],[-72.55091094970703,40.8400993347168],[-72.56848907470703,40.842525482177734],[-72.57978820800781,40.82925033569336],[-72.57164764404297,40.821170806884766]]],[[[-73.04010009765625,40.671024322509766],[-72.97828674316406,40.69739532470703],[-72.97196960449219,40.69717025756836],[-72.92094421386719,40.72180938720703],[-72.89777374267578,40.72355270385742],[-72.8924789428711,40.73162078857422],[-72.87178802490234,40.73246765136719],[-72.8218765258789,40.74965286254883],[-72.80713653564453,40.75150680541992],[-72.79378509521484,40.76310729980469],[-72.76815032958984,40.7615852355957],[-72.86316680908203,40.73296356201172],[-72.9232177734375,40.71328353881836],[-73.01254272460938,40.679649353027344],[-73.04010009765625,40.671024322509766]]],[[[-72.14292907714844,41.09781265258789],[-72.12834930419922,41.108131408691406],[-72.11557006835938,41.1106071472168],[-72.07951354980469,41.100677490234375],[-72.08676147460938,41.08571243286133],[-72.08184051513672,41.07173538208008],[-72.08697509765625,41.058292388916016],[-72.09719848632812,41.05488586425781],[-72.09713745117188,41.075843811035156],[-72.1031494140625,41.086483001708984],[-72.14292907714844,41.09781265258789]]],[[[-72.21099853515625,41.17894744873047],[-72.18898010253906,41.18901062011719],[-72.18789672851562,41.18061447143555],[-72.19860076904297,41.16495132446289],[-72.21099853515625,41.17894744873047]]]]}"; 
			try{
		    JSONObject obj = new JSONObject(data);
			JSONArray ptArray = obj.getJSONArray("coordinates");
			String type="Polygon";
			//String type="MultiPolygon";
		  getPolygonAttributes(type, ptArray); 
		} catch(Exception e){
			 e.printStackTrace();
		 }
		 
	 }

}
