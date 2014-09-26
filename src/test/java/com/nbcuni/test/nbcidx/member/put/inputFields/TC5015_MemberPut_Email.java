package com.nbcuni.test.nbcidx.member.put.inputFields;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.nbcidx.MemberAPIs;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/**************************************************************************
 * NBCIDX Member.Put API - TC5015_MemberPut_Email. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC5015_MemberPut_Email {

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
	public void memberPut_email() throws Exception {
		
		// Get the 'username' for Random Member
		String sApiUUID = TC01_Create_RandomMemberInfo.sfinalUUID;
		String username = TC01_Create_RandomMemberInfo.randomMemberUserName;
		String email = TC01_Create_RandomMemberInfo.randomEmail;
				
		Reporter.log("1) Creating Email-Id for member "+username+ " using member.put API and 'email' field");
		
		//Generate the JSON Body for POST 
		String jsonBody ="{'_id':'"+sApiUUID+"','email':'"+email+"'}";
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		//Send member.put POST Request 
		JsonObject response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
		
		//Get the DB Response
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating Email of Member in MongoDB");
		Reporter.log("");
		
		// Find and display
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("_id", sApiUUID);
		searchQuery.put("metadata.email.value.address", email);
				
		BasicDBObject fields = new BasicDBObject();
		fields.put("_id", 1);
		fields.put("metadata.status.value", 1);
		fields.put("metadata.username.value", 1);
		fields.put("metadata.password.value", 1);
		fields.put("metadata.email.value.address", 1);
				
		DBObject DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
		
		JsonObject DBResponse =  al.JsonObjectFromMongoDBObject(DbObj);
		ArrayList<HashMap<String, String>> emailArrlst = api.convertJsonArrayToHashMap(api.getJsonArray(api.getAsJsonObject(DBResponse, "metadata"),"email"));
		Reporter.log("Latest Email-Id of this member is = " +emailArrlst.get(0).toString());
	
		boolean bMemberEmail = emailArrlst.get(0).toString().contains(email);
		Assert.assertEquals(bMemberEmail, true);
		Reporter.log("Passed : Email-Id created for Member = " +username+ " in MongoDB with value email = "+email);
		Reporter.log("-- X --");
		Reporter.log("");

		Reporter.log("2) Updating Email-Id for member "+username+ " using member.put API and 'email' field");
		email = username+"@gmail.com";
		jsonBody = "";
		jsonBody ="{'_id':'"+sApiUUID+"','email':'"+email+"'}";
				
		//Send member.put POST Request 
		response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
		
		//Get the DB Response 
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating Email-Id updated in MongoDB");
		Reporter.log("");
				
		searchQuery = new BasicDBObject();
		searchQuery.put("_id", sApiUUID);
		searchQuery.put("metadata.email.value.address", email);
		
		DbObj = null;
		DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
		
		DBResponse =  al.JsonObjectFromMongoDBObject(DbObj);
		emailArrlst = api.convertJsonArrayToHashMap(api.getJsonArray(api.getAsJsonObject(DBResponse, "metadata"),"email"));
		Reporter.log("Latest Email-Id of this member is = " +emailArrlst.get(0).toString());
		bMemberEmail = emailArrlst.get(0).toString().contains(email);
		Assert.assertEquals(bMemberEmail, true);
		Reporter.log("Passed : Email-Id updated for Member = " +username+ " in MongoDB with value email = "+email);
		Reporter.log("-- X --");		
		Reporter.log("");

		Reporter.log("3) Updating Email-Id for member "+username+ " for 'verified' and 'primary' fields using member.put API and 'email' field");
		
		jsonBody = "";
		email = username+"_One@gmail.com";
		jsonBody = "{'_id':'"+sApiUUID+"','email': [{'verified': true,'primary': true,'address': '"+email+"'}]}";
		//jsonBody ="{'_id':'"+sApiUUID+"','status':'banned'}";
				
		//Send member.put POST Request 
		response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
		
		//Get the DB Response 
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating Email-Id updated in MongoDB");
		Reporter.log("");
		
		searchQuery = new BasicDBObject();
		searchQuery.put("_id", sApiUUID);
		searchQuery.put("metadata.email.value.address", email);
				
		DbObj = null;
		DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
		
		DBResponse =  al.JsonObjectFromMongoDBObject(DbObj);
		emailArrlst = api.convertJsonArrayToHashMap(api.getJsonArray(api.getAsJsonObject(DBResponse, "metadata"),"email"));
		Reporter.log("Latest Email-Id of this member is = " +emailArrlst.get(0).toString());
		bMemberEmail = emailArrlst.get(0).toString().contains(email);
		Assert.assertEquals(bMemberEmail, true);
		Reporter.log("Passed : Email-Id updated for Member = " +username+ " in MongoDB with value email = "+email +", 'verified': true and 'primary': true");
		Reporter.log("-- X --");	
		Reporter.log("");

		Reporter.log("4) Updating Email-Id for member "+username+ " with 2 Email-Ids and for 'verified' and 'primary' fields using member.put API and 'email' field");
		
		jsonBody = "";
		email = username+"_Three@gmail.com";
		String emailSec = username+"_Two@gmail.com";
		jsonBody = "{'_id':'"+sApiUUID+"','email': [{'verified': true,'primary': true,'address': '"+email+"'},{'verified': false,'primary': false,'address': '"+emailSec+"'}]}";
			            				
		//Send member.put POST Request 
		response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
		
		//Get the DB Response 
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating Email-Id updated in MongoDB");
		Reporter.log("");
				
		searchQuery = new BasicDBObject();
		searchQuery.put("_id", sApiUUID);
		searchQuery.put("metadata.email.value.address", email);
		
		DbObj = null;
		DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
		
		DBResponse =  al.JsonObjectFromMongoDBObject(DbObj);
		emailArrlst = api.convertJsonArrayToHashMap(api.getJsonArray(api.getAsJsonObject(DBResponse, "metadata"),"email"));
		//Reporter.log("Latest status of this member is = " +emailArrlst.get(0).toString());
		bMemberEmail = emailArrlst.get(0).toString().contains(email);
		Assert.assertEquals(bMemberEmail, true);
		Reporter.log("Passed : Email-Id updated for Member = "+username+" in MongoDB with 2 values Email-Id1 = "+email+", Email-Id2 = "+emailSec);
		Reporter.log("-- X --");		
		Reporter.log("");

		Reporter.log("5) Updating Email-Id for member "+username+ " to 'invalid format of Email-Id' using member.put API and 'email' field");
		Reporter.log("");
		
		emailSec = "xyz@gmail";
		jsonBody = "";
		jsonBody ="{'_id':'"+sApiUUID+"','email':'"+emailSec+"'}";
	
		//Send member.put POST Request 
		int responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 400)
			Reporter.log("Passed : member.put is not accepting invalid format for 'email' field");
		else
			fail("Failed with Response Code = "+responseCode);
		Reporter.log("-- X --");			
		Reporter.log("");

		Reporter.log("6) Updating Email-Id for member "+username+ " to 'invalid format of Email-Id without @ ' using member.put API and 'email' field");
		Reporter.log("");
		
		emailSec = "xyzgmail.com";
		jsonBody = "";
		jsonBody ="{'_id':'"+sApiUUID+"','email':'"+emailSec+"'}";
				
		//Send member.put POST Request 
		responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 400)
			Reporter.log("Passed : member.put is not accepting invalid format for 'email' field");
		else
			fail("Failed with Response Code = "+responseCode);
		Reporter.log("-- X --");

		TC01_Create_RandomMemberInfo.randomEmail=email;
		Reporter.log("-- X --");			
		Reporter.log("");
		
	}
}
