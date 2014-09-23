package com.nbcuni.test.nbcidx.member.get.errorcodes;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;
import java.util.UUID;

import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/**************************************************************************
 * NBCIDX Member.Get API - TC01_FindMemberFromDB. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC01_FindMemberFromDB {
	
	private CustomWebDriver cs;
	private static AppLib al;
	private API api;
	private DB mydb;
	private Proxy proxy=null;
	
	static String finalUUID;
	static String finalUsername;
	static String finalEmail;
	static String finalBrands;
	static String sUUID;
	static String sUsername;
	static String sEmail;
	static String sBrands;
		
	/**
	 * Instantiate the TestNG Before Class Method.
	 * 
	 * @param sEnv - environment
	 * @throws Exception - error
	 */
	@BeforeClass(alwaysRun = true)
	@Parameters("Environment")
	public void startEnvironment(String sEnv) {
		try {
			cs = null;
			al = new AppLib(cs);
			al.setEnvironmentInfo(sEnv);
			mydb = al.getMongoDbConnection();
			proxy = al.getHttpProxy();
			api = new API();
			api.setProxy(proxy);
			} catch (Exception e) {
				fail(e.toString());
				}
	}
	
	/**
     * Instantiate the TestNG After Class Method.
     * 
     * @throws Exception - error
     */
    @AfterClass(alwaysRun = true)
    public void stopEnvironment() {
    	try {
        al.closeMongoDbConnection();
    	} catch (Exception e) {
			fail(e.toString());
			}
    }

	/**
     * Instantiate the TestNG Test Method.
     * 
     * @throws Exception - error
     */
  @Test(groups = {"full"})  
  public void findMemberFromDB() throws Exception {
	  	
	  	//Generate random UUID. Random UUID is the reference to fetch any existent member's UUID from MongoDB.
	  	UUID uid = UUID.randomUUID();
						
		String strUUID = al.convertUUID(uid);
		Reporter.log("1) Creating Random UUID value to use as a reference for MongoDB :"+strUUID);
		Reporter.log("");
		
		// Fetch the member from MongoDB 
		BasicDBObject fields = new BasicDBObject();
		fields.put("_id", 1);
		fields.put("brands", 1);
		fields.put("metadata.username.value", 1);	
		fields.put("metadata.email.value.address", 1);
						
		//Check if any UUID present in MongoDB which is greater than random UUID. If present then fetch the corresponding email and username.
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("_id", new BasicDBObject("$gte", strUUID));
		searchQuery.put("metadata.username.value", new BasicDBObject("$exists", true));
		searchQuery.put("metadata.email.value.address", new BasicDBObject("$exists", true));
					
		DBObject myDbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FINDONE);	
		if(myDbObj==null)
		{
			//If null then check if any UUID present in MongoDB which is less than random UUID. If present then fetch the corresponding email and username.
			searchQuery = new BasicDBObject();
			searchQuery.put("_id", new BasicDBObject("$lte", strUUID));
			searchQuery.put("metadata.username.value", new BasicDBObject("$exists", true));
			searchQuery.put("metadata.email.value.address", new BasicDBObject("$exists", true));
								
			myDbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FINDONE);
			if(myDbObj==null)
				fail("MongoDB Cursor is empty. No Data found from Database");
			 				
		}
		JsonObject DBResponse =  al.JsonObjectFromMongoDBObject(myDbObj);
		// We got the UUID from MongoDB 
		JsonElement UUID = DBResponse.get("_id");
		sUUID= UUID.toString();
		finalUUID=sUUID.substring(1, sUUID.length()-1);
		Reporter.log("2) Fetched UUID of an existent member from MongoDB = "+finalUUID);
		Reporter.log("");
		
		//Fetch member's brand
		JsonElement brand = api.getJsonArray(DBResponse, "brands").get(0);
		sBrands= brand.toString();
		finalBrands=sBrands.substring(1, sBrands.length()-1);
		Reporter.log("Fetched Brand = "+finalBrands);
		
		JsonObject metadata = api.getAsJsonObject(DBResponse, "metadata");
		
		//Fetch member's username
		JsonElement username = api.getJsonArray(metadata, "username").get(0).getAsJsonObject().get("value");
		sUsername = username.toString();
		finalUsername=sUsername.substring(1, sUsername.length()-1);
		Reporter.log("Fetched username = " + finalUsername);
		
		//Fetch member's email
		JsonElement email = api.getJsonArray(metadata, "email").get(0).getAsJsonObject().get("value").getAsJsonArray().get(0).getAsJsonObject().get("address");
		sEmail = email.toString();
		finalEmail=sEmail.substring(1, sEmail.length()-1);
		Reporter.log("Fetched email id = " +finalEmail );
	}
}

