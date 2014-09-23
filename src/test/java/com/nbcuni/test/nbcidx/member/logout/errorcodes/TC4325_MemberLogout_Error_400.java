package com.nbcuni.test.nbcidx.member.logout.errorcodes;

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
 * NBCIDX Member.Logout API - TC4325_MemberLogout_Error_400. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Aug 18, 2014
 **************************************************************************/

public class TC4325_MemberLogout_Error_400 {

	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private DB mydb;
	private Proxy proxy=null;
	
	public static String randomMemberName = "";
	public static String randomPassword = "";
	public static String randomEmail= "";
	public static String uuid="";

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
			ma = new MemberAPIs();
			} catch (Exception e) {
				fail(e.toString());
				}
	}
	
	/**
     * Instantiate the TestNG After Class Method.
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
	public void memberLogout_error400() throws Exception {

		String timeStamp = al.getCurrentTimestamp();
		randomMemberName = "test_"+timeStamp;
		randomPassword = timeStamp;
		randomEmail = "test"+timeStamp+"@gmail.com";
		
		Reporter.log(" ");
		Reporter.log("/************************ Creating Random Member ********************************/");
		Reporter.log(" ");
		Reporter.log("1) Random Member Created with below information =>");
		Reporter.log(" ");
		Reporter.log("Member Name : " + randomMemberName);
		Reporter.log("Password : " + randomPassword);
		Reporter.log("Email-Id : " + randomEmail);
		
		//Generate the JSON Body for POST 
		String jsonBody ="{'username':'"+randomMemberName+"','password':'"+randomPassword+"','email': { 'address': '"+randomEmail+"', 'verified': true } }";
		
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
		uuid=sApiUUID;
		
		//Get the DB Response
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating Member present in MongoDB");
		Reporter.log("");
		
		// Find and display
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("metadata.username.value", randomMemberName);
				
		BasicDBObject fields = new BasicDBObject();
		fields.put("_id", 1);
		fields.put("metadata.username.value", 1);	
		
		DBObject DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");

		String dbUUID =  DbObj.get("_id").toString();
		String dbresponse = DbObj.toString();				

		boolean bMembername = dbresponse.contains(randomMemberName);
		Assert.assertEquals(bMembername, true);
		Assert.assertEquals(sApiUUID, dbUUID);
		Reporter.log("Passed : Member = " +randomMemberName+ " present in Mongo Database with UUID = " +dbUUID);
				
		Reporter.log("");
		Reporter.log("2) Member.logout with multiple brand_ids : ");
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.LOGOUT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		String myJsonBody ="id="+uuid;
		
		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		//Generate the API call for member.put.
		String apicall = al.getApiURL()+"/member/logout?API_KEY="+al.getDefaultApiKey()+"&BRAND_ID=*";
		
		//Send member.put POST Request. 
		int mylogout = al.postHTTPReponseCode(apicall, myJsonBody, myContentType);
		if(mylogout == 400)
			Reporter.log("Member.logout Response code ="+mylogout);
		else
			fail("Member.logout Response code ="+mylogout);
		Reporter.log("-- X --");
		Reporter.log("");
		
		Reporter.log("3) Member.logout with blank 'id' : ");
		myJsonBody ="id=";
		mylogout = ma.memberLOGOUTResponseCode(api, al, myJsonBody, surfBrandId);
		if(mylogout == 400)
			Reporter.log("Member.logout Response code ="+mylogout);
		else
			fail("Member.logout Response code ="+mylogout);
		Reporter.log("-- X --");
		Reporter.log("");
		
		Reporter.log("4) Member.logout with wrong parameter 'i' : ");
		myJsonBody ="i=";
		mylogout = ma.memberLOGOUTResponseCode(api, al, myJsonBody, surfBrandId);
		if(mylogout == 400)
			Reporter.log("Member.logout Response code ="+mylogout);
		else
			fail("Member.logout Response code ="+mylogout);
		Reporter.log("-- X --");
		Reporter.log("");
		
		Reporter.log("5) Member.logout with params 'gigya_api_key' & 'site_id' : ");
		myJsonBody ="id="+uuid+"&maintain_social_login=true&gigya_api_key=456&site_id=123";
		mylogout = ma.memberLOGOUTResponseCode(api, al, myJsonBody, surfBrandId);
		if(mylogout == 400)
			Reporter.log("Member.logout Response code ="+mylogout);
		else
			fail("Member.logout Response code ="+mylogout);
		Reporter.log("-- X --");
		Reporter.log("");	
	}
}
