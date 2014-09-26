package com.nbcuni.test.nbcidx.member.put.inputFields;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;
import org.testng.Assert;
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
import com.nbcuni.test.nbcidx.MemberAPIs;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/**************************************************************************
 * NBCIDX Member.Put API - TC5009_MemberPut_Username. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC5009_MemberPut_Username {

	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private DB mydb;
	private Proxy proxy=null;
	
	JsonObject response=null;
	int responseCode;
	
	/**
	 * Instantiate the TestNG Before Class Method.
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
     */
	@Test(groups = {"full"})
	public void memberPut_username() throws Exception {
		
		// Get the 'username' for Random Member
		String username = TC01_Create_RandomMemberInfo.randomMemberUserName;
		
		Reporter.log("1) Creating new member using member.put API and 'username' field");
		
		//Generate the JSON Body for POST 
		String jsonBody ="{'username':'"+username+"'}";
				
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		//Send member.put POST Request 
		JsonObject response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
				
		// Fetch the UUID from 1st POST 
		JsonElement id = response.get("_id");
		String sUUID = id.toString();
		String sApiUUID = sUUID.substring(1, sUUID.length()-1);
		TC01_Create_RandomMemberInfo.sfinalUUID=sApiUUID;
		
		//Get the DB Response 
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating Member present in MongoDB");
		Reporter.log("");
		
		// Find and display
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("metadata.username.value", username);
				
		BasicDBObject fields = new BasicDBObject();
		fields.put("_id", 1);
		fields.put("metadata.username.value", 1);	
		
		DBObject DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");

		String dbUUID =  DbObj.get("_id").toString();
		String dbresponse = DbObj.toString();				
		boolean bMembername = dbresponse.contains(username);
		Assert.assertEquals(bMembername, true);
		Assert.assertEquals(sApiUUID, dbUUID);
		Reporter.log("Passed : Member = " +username+ " present in Mongo Database with UUID = " +dbUUID);
		Reporter.log("-- X --");
		Reporter.log("");		

		Reporter.log("2) Updating same member using member.put API for 'username' field");
		
		jsonBody = "";
		username = username+"_Updated";
		jsonBody = "{'_id':'"+sApiUUID+"','username':'"+username+"'}";
				
		//Send member.put POST Request 
		response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
		
		//Get the DB Response 		
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating Username updated in MongoDB");
		Reporter.log("");
		
		// Find and display
		searchQuery = new BasicDBObject();
		searchQuery.put("_id", sApiUUID);
				
		DbObj = null;
		DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");

		dbresponse = DbObj.toString();				
		bMembername = dbresponse.contains(username);
		Assert.assertEquals(bMembername, true);
		Reporter.log("Passed : Username of member with UUID = " +dbUUID+ " got updated with = " +username+ " in MongoDB.");
		Reporter.log("-- X --");
		Reporter.log(" ");

		Reporter.log("3) Updating 'Username' for member "+username+ " to 'Integer value' using member.put API and 'username' field");
		Reporter.log("");
		
		jsonBody = "";
		jsonBody = "{'_id':'"+sApiUUID+"','username':12345}";
				
		//Send member.put POST Request 
		int responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 400)
			Reporter.log("Passed : member.put is not accepting Integer value for 'username' field");
		else
			fail("Failed with Response Code = "+responseCode);
		
		//Update the global variable with updated 'username'
		TC01_Create_RandomMemberInfo.randomMemberUserName=username;
		Reporter.log("-- X --");
	}
}
