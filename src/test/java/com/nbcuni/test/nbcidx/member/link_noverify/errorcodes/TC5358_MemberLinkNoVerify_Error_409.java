package com.nbcuni.test.nbcidx.member.link_noverify.errorcodes;

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

/************************************************************************************
 * NBCIDX Member.Link_noverify API - TC5358_MemberLinkNoVerify_Error_409. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Sep 10, 2014
 ************************************************************************************/

public class TC5358_MemberLinkNoVerify_Error_409 {
	
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private DB mydb;
	private Proxy proxy=null;
	
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
	public void memberLinkNoVerify_error409() throws Exception {
		
		// 1) Creating 1st member using member.put API and 'username' field
		String timeStamp = al.getCurrentTimestamp();
		String username = "test_"+timeStamp;
		String pw = "test_"+timeStamp;
		String email = "test_"+timeStamp+"@gmail.com";
		Reporter.log("1) Creating 1st member using member.put with below information =>");
		Reporter.log(" ");
		Reporter.log("Member Name : " + username);
		Reporter.log("Email-Id : " + email);
		Reporter.log("Password : " + pw);
		
		//Generate the JSON Body for POST 
		String jsonBody ="{'username':'"+username+"','password':'"+pw+"','status': 'active','phone': [{'mobile': false,'number': '2127772374','primary': true}],'address': [{'city': 'Jersey City','country': 'US','address2': null,'primary': true,'state': 'NJ','address1': null,'postalcode': '07310','type': null}],'brand_data': {'email_optin': 'true','custom_data_1': 'custom data'},'gender': 'f','birthdate': {'month': 5,'day': 22,'year': 1947},'email': [{'verified': true,'primary': true,'address': '"+email+"'}]}";
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		//Send member.put POST Request 
		JsonObject response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response == null)
		{
			fail("Error/Null Response from API call");
		}		
		// Fetch the UUID from 1st POST 
		JsonElement id = response.get("_id");
		String sUUID = id.toString();
		String uuidOne = sUUID.substring(1, sUUID.length()-1);
				
		//Get the DB Response 		
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating Member present in MongoDB");
		Reporter.log("");
		
		// Find and display
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("_id", uuidOne);
				
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
		Assert.assertEquals(uuidOne, dbUUID);
		Reporter.log("Passed : Member = " +username+ " present in Mongo Database with UUID = " +dbUUID);
		Reporter.log("-- X --");
		Reporter.log("");		
			
		// 2) member.link_noverify
		timeStamp = al.getCurrentTimestamp();
		String providerID = "test_provider"+timeStamp;
		
		Reporter.log("");
		Reporter.log("2) Calling member.link_noverify for 1st member, provider=facebook and providerID="+providerID);
		Reporter.log("");
		
		// Generate Post body to link with facebook.
		String postBody ="id="+uuidOne+"&provider=facebook&provider_id="+providerID;
		int mylink = ma.memberLINKNoVerifyResponseCode(api, al, postBody, surfBrandId);	
		if (mylink ==200)
			Reporter.log("Linking uuid with facebook is successful");
		else
        	fail("Linking is Not successful");
		Reporter.log("-- X --");
		Reporter.log("");
		
		// 3) Creating 2nd member using member.put API and 'username' field
		timeStamp = al.getCurrentTimestamp();
		username = "test_"+timeStamp;
		pw = "test_"+timeStamp;
		email = "test_"+timeStamp+"@gmail.com";
		
		Reporter.log("3) Creating 2nd member using member.put with below information =>");
		Reporter.log(" ");
		Reporter.log("Member Name : " + username);
		Reporter.log("Email-Id : " + email);
		Reporter.log("Password : " + pw);
		
		//Generate the JSON Body for POST 
		jsonBody ="{'username':'"+username+"','password':'"+pw+"','status': 'active','phone': [{'mobile': false,'number': '2128512374','primary': true}],'address': [{'city': 'Jersey City','country': 'US','address2': null,'primary': true,'state': 'NJ','address1': null,'postalcode': '07310','type': null}],'brand_data': {'email_optin': 'true','custom_data_1': 'custom data'},'gender': 'f','birthdate': {'month': 5,'day': 22,'year': 1947},'email': [{'verified': true,'primary': true,'address': '"+email+"'}]}";
		
		//Send member.put POST Request 
		response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response == null)
		{
			fail("Error/Null Response from API call");
		}		
		// Fetch the UUID from 2nd POST 
		id = response.get("_id");
		sUUID = id.toString();
		String uuidTwo = sUUID.substring(1, sUUID.length()-1);
		
		//Get the DB Response 		
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating Member present in MongoDB");
		Reporter.log("");
		
		// Find and display
		searchQuery = new BasicDBObject();
		searchQuery.put("_id", uuidTwo);
				
		DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");

		String dbUUIDTwo =  DbObj.get("_id").toString();
		dbresponse = DbObj.toString();				
		bMembername = dbresponse.contains(username);
		Assert.assertEquals(bMembername, true);
		Assert.assertEquals(uuidTwo, dbUUIDTwo);
		Reporter.log("Passed : Member = " +username+ " present in Mongo Database with UUID = " +dbUUID);
		Reporter.log("-- X --");
		Reporter.log("");		
				
		Reporter.log("4) Calling member.link_noverify for 2nd member with same provider=facebook and providerID="+providerID);
		Reporter.log("");
		
		// Generate Post body to link with facebook.
		postBody ="id="+uuidTwo+"&provider=facebook&provider_id="+providerID;
		mylink = ma.memberLINKNoVerifyResponseCode(api, al, postBody, surfBrandId);	
		if (mylink ==409)
			Reporter.log("Passed : member.link_noverify error code : "+mylink);
		else
        	fail("Failed : error code = "+mylink);
		Reporter.log("-- X --");
		Reporter.log("");	
	}	
}
