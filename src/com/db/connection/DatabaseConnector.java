package com.db.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;


public class DatabaseConnector {

private static ResourceBundle props = ResourceBundle.getBundle("dataloader", Locale.getDefault());
private static Logger logger = Logger.getLogger(DatabaseConnector.class.getName());
private Connection conn=null;
private static DatabaseConnector databaseConnector=null;

private DatabaseConnector(){
   	
}

public static DatabaseConnector getInstance(){

	if(databaseConnector!=null){
		return databaseConnector;
	}else{
		return new DatabaseConnector();
	}
}

public Connection getConnection(){
  
  String driverClass = props.getString("driverClassName");
  String dbUrl = props.getString("url");
  String user_id = props.getString("username");
  String password = props.getString("password");
  System.out.println("Connecting to database...");
  logger.info("Connecting to database...");
  
  try{
   if(conn==null){
   Class.forName(driverClass);
   conn = DriverManager.getConnection(dbUrl,user_id,password);
   System.out.println("Connection Established!");
  }
  }catch(Exception e){
	  e.printStackTrace();
  }
  return conn;
}

public void closeConnection(){
	  
	  if(conn!=null){
		  try{
			conn.close();  
			System.out.println("Connection closed");
		  }catch(Exception e){
			  e.printStackTrace();
		  }
	  }
	}

}
