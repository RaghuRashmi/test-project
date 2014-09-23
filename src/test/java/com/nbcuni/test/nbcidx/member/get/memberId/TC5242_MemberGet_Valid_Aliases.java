package com.nbcuni.test.nbcidx.member.get.memberId;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;
import java.util.UUID;

import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.nbcidx.MemberAPIs;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/**************************************************************************
 * NBCIDX Member.Get API - TC5242_MemberGet_Valid_Aliases. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC5242_MemberGet_Valid_Aliases {
	
	private CustomWebDriver cs;
	private static AppLib al;
	private API api;
	private DB mydb;
	private Proxy proxy=null;
	private MemberAPIs ma;
	
	
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
			proxy = al.getHttpProxy();
			mydb = al.getMongoDbConnection();
			api = new API();
			api.setProxy(proxy);
			ma = new MemberAPIs();
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
  public void memberGet_aliases() throws Exception {
	  	
	  	//Generate random UUID. Random UUID is the reference to fetch any existent member's UUID from MongoDB.
	  	UUID uid = UUID.randomUUID();
						
		String strUUID = al.convertUUID(uid);
		Reporter.log("1) Creating Random UUID value to use as a reference for MongoDB :"+strUUID);
		Reporter.log("");
		
		//Fetch the member from MongoDB
		BasicDBObject fields = new BasicDBObject();
		fields.put("_id", 1);
		fields.put("aliases", new BasicDBObject("$slice", new int[] { 0, 1 }));
						
		//Check if any UUID present in MongoDB which is greater than random UUID. If present then fetch the corresponding aliases value.
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("_id", new BasicDBObject("$gte", strUUID));
		searchQuery.put("aliases.0", new BasicDBObject("$exists", true));
			
		DBObject myDbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FINDONE);	
		if(myDbObj==null)
		{
			//If null then check if any UUID present in MongoDB which is less than random UUID. If present then fetch the corresponding email and username.
			searchQuery = new BasicDBObject();
			searchQuery.put("_id", new BasicDBObject("$lte", strUUID));
			searchQuery.put("aliases.0", new BasicDBObject("$exists", true));
								
			myDbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FINDONE);
			if(myDbObj==null)
				fail("MongoDB Cursor is empty. No Data found from Database");
		}
		
		BasicDBList aliases = (BasicDBList) myDbObj.get("aliases");
		DBObject obj = (DBObject) aliases.get(0);
		String sAliases = obj.get("value").toString();
		Reporter.log("2) Fetched corresponding aliases value = "+sAliases);
		Reporter.log(" ");	
		
		Reporter.log("3) Validating member.get API Response with id=<valid Aliases>");
		String params = "id="+sAliases;
		
		JsonObject response = ma.memberGetResponse(api, al, params, "*");		
		if(response ==null)
			fail("Error/Null Response from API call");
		
		String responseString = response.toString();
		Reporter.log("RESPONSE BODY: " +responseString);
		Reporter.log("-- X --");
		Reporter.log("");			
	}   
}

