package com.nbcuni.test.nbcidx.member.link_noverify.brands;

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

/********************************************************************************************
 * NBCIDX Member.Link_noverify API - TC5346_MemberLinkNoVerify_Brand_INTERNALb92e. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Sep 10, 2014
 ********************************************************************************************/

public class TC5346_MemberLinkNoVerify_Brand_INTERNALb92e {
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private DB mydb;
	private Proxy proxy=null;
	
	private String username = "";
	private String pw = "";
	private String email = "";
	private String uuid;
	
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

	@Test(groups = {"QA_Internal"})
	public void memberLinkNoVerify_INTERNALb92e() throws Exception {
		
		Reporter.log("Validating member.link_noverify with brand = INTERNAL b92e");
		Reporter.log(" ");
		
		// 1) Creating new member using member.put API and 'username' field
		String timeStamp = al.getCurrentTimestamp();
		username = "test_"+timeStamp;
		pw = "test_"+timeStamp;
		email = "test_"+timeStamp+"@gmail.com";
		Reporter.log("1) Creating new member using member.put with below information =>");
		Reporter.log(" ");
		Reporter.log("Member Name : " + username);
		Reporter.log("Email-Id : " + email);
		Reporter.log("Password : " + pw);
		
		//Generate the JSON Body for POST 
		String jsonBody ="{'username':'"+username+"','password':'"+pw+"','status': 'active','phone': [{'mobile': false,'number': '2128502384','primary': true}],'address': [{'city': 'Jersey City','country': 'US','address2': null,'primary': true,'state': 'NJ','address1': null,'postalcode': '07310','type': null}],'brand_data': {'email_optin': 'true','custom_data_1': 'custom data'},'gender': 'f','birthdate': {'month': 5,'day': 22,'year': 1947},'email': [{'verified': true,'primary': true,'address': '"+email+"'}]}";
		
		// Get b92e Brand Id
		String internalBrandId = "4b2526c719264257aec34453d568b92e";
		
		//Send member.put POST Request 
		JsonObject response = ma.memberPUTResponse(api, al, jsonBody, internalBrandId);
		if(response == null)
		{
			fail("Error/Null Response from API call");
		}		
		// Fetch the UUID from 1st POST 
		JsonElement id = response.get("_id");
		String sUUID = id.toString();
		uuid = sUUID.substring(1, sUUID.length()-1);
				
		//Get the DB Response 		
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating Member present in MongoDB");
		Reporter.log("");
		
		// Find and display
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("_id", uuid);
				
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
		Assert.assertEquals(uuid, dbUUID);
		Reporter.log("Passed : Member = " +username+ " present in Mongo Database with UUID = " +dbUUID);
		Reporter.log("-- X --");
		Reporter.log("");		
			
		// 2) member.link_noverify
		timeStamp = al.getCurrentTimestamp();
		String providerID = "test_provider"+timeStamp;
		
		Reporter.log("");
		Reporter.log("2) Calling member.link_noverify with uuid, provider = facebook and providerID = "+providerID);
		Reporter.log("");
		
		// Generate Post body to link with facebook.
		String postBody ="id="+uuid+"&provider=facebook&provider_id="+providerID;
		int mylink = ma.memberLINKNoVerifyResponseCode(api, al, postBody, internalBrandId);	
		if (mylink ==200)
			Reporter.log("Linking uuid with facebook is successful");
		else
        	fail("Linking is Not successful");
		Reporter.log("-- X --");
		Reporter.log("");
		
		Reporter.log("3) Calling member.link_noverify with emailid, provider = facebook and providerID = "+providerID);
		Reporter.log("");
		
		// Generate Post body to link with facebook.
		postBody ="id="+email+"&provider=facebook&provider_id="+providerID;
		mylink = ma.memberLINKNoVerifyResponseCode(api, al, postBody, internalBrandId);	
		if (mylink ==200)
			Reporter.log("Linking emailid with facebook is successful");
		else
        	fail("Linking is Not successful");
		Reporter.log("-- X --");
		Reporter.log("");
		
		Reporter.log("4) Calling member.link_noverify with username, provider = facebook and providerID = "+providerID);
		Reporter.log("");
		
		// Generate Post body to link with facebook.
		postBody ="id="+username+"&provider=facebook&provider_id="+providerID;
		mylink = ma.memberLINKNoVerifyResponseCode(api, al, postBody, internalBrandId);	
		if (mylink ==200)
			Reporter.log("Linking username with facebook is successful");
		else
        	fail("Linking is Not successful");
		Reporter.log("-- X --");
		Reporter.log("");
		
		Reporter.log("5) Removing member from brand = INTERNAL b92e");
		//Generate Post Body.
		postBody = "id="+uuid;

		//Send member.remove POST Request 
		int removeReturnCode = ma.memberREMOVEResponseCode(api, al, postBody, internalBrandId, username, uuid, mydb);
		if(removeReturnCode != 204)
			fail("Failed : HTTP error code : "+ removeReturnCode);			
	}
}
