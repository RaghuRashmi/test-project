package com.nbcuni.test.nbcidx.member.link.errorcodes;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;

import org.testng.Assert;
import org.testng.Reporter;
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
 * NBCIDX Member.Link API - TC4993_MemberLink_Error_400. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Sep 10, 2014
 **************************************************************************/

public class TC4993_MemberLink_Error_400 {

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
			proxy = al.getHttpProxy();
			api = new API();
			api.setProxy(proxy);
			ma = new MemberAPIs();
			mydb = al.getMongoDbConnection();
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
	public void memberLink_error400() throws Exception {

		// 1) Creating random member
		String timeStamp = al.getCurrentTimestamp();
		randomMemberName = "test_"+timeStamp;
		randomPassword = timeStamp;
		randomEmail = "test"+timeStamp+"@gmail.com";
		
		Reporter.log(" ");
		Reporter.log("/************************ Creating Random Member ********************************/");
		Reporter.log("1) Random Member Created with below information =>");
		Reporter.log(" ");
		Reporter.log("Member Name : " + randomMemberName);
		Reporter.log("Password : " + randomPassword);
		Reporter.log("Email-Id : " + randomEmail);
		Reporter.log("-- X --");
		Reporter.log("");
		
		// 2) member.put
		Reporter.log("2) member.put =>");
		
		//Generate the JSON Body for POST 
		String myJsonBody ="{'username':'"+randomMemberName+"','password':'"+randomPassword+"','email': { 'address': '"+randomEmail+"', 'verified': true } }";
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		//Send member.put POST Request 
		JsonObject response = ma.memberPUTResponse(api, al, myJsonBody, surfBrandId);
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
		Reporter.log("-- X --");
		Reporter.log("");		
		
		Reporter.log("3) Member.link with wrong param 'i'");
		myJsonBody ="i="+uuid+"&provider=facebook&provider_id=1002470365";
		int mylink = ma.memberLINKResponseCode(api, al, myJsonBody, surfBrandId);
		if(mylink == 400)
			Reporter.log("Passed : Member.link Response code ="+mylink);
		else
			fail("Member.link Response code ="+mylink);
		Reporter.log("-- X --");
		Reporter.log("");
		
		
		Reporter.log("4) Member.link with blank 'provider'");
		myJsonBody ="id="+uuid+"&provider=&provider_id=1002470365";
		mylink = ma.memberLINKResponseCode(api, al, myJsonBody, surfBrandId);
		if(mylink == 400)
			Reporter.log("Passed : Member.link Response code ="+mylink);
		else
			fail("Member.link Response code ="+mylink);
		Reporter.log("-- X --");
		Reporter.log("");
		
		
		Reporter.log("5) Member.link with no provider");
		myJsonBody ="id="+uuid+"&=facebook&provider_id=1002470365";
		mylink = ma.memberLINKResponseCode(api, al, myJsonBody, surfBrandId);
		if(mylink == 400)
			Reporter.log("Passed : Member.link Response code ="+mylink);
		else
			fail("Member.link Response code ="+mylink);
		Reporter.log("-- X --");
		Reporter.log("");
		
		Reporter.log("6) Member.link with no params");
		myJsonBody ="";
		mylink = ma.memberLINKResponseCode(api, al, myJsonBody, surfBrandId);
		if(mylink == 400)
			Reporter.log("Passed : Member.link Response code ="+mylink);
		else
			fail("Member.link Response code ="+mylink);
		Reporter.log("-- X --");
		Reporter.log("");
		
		
		Reporter.log("7) Member.link with no provider_id");
		myJsonBody ="id="+uuid+"&provider=facebook&=1002470365";
		mylink = ma.memberLINKResponseCode(api, al, myJsonBody, surfBrandId);
		if(mylink == 400)
			Reporter.log("Passed : Member.link Response code ="+mylink);
		else
			fail("Member.link Response code ="+mylink);
		Reporter.log("-- X --");
		Reporter.log("");
		
		
		Reporter.log("8) Member.link with only uuid");
		myJsonBody ="id="+uuid;
		mylink = ma.memberLINKResponseCode(api, al, myJsonBody, surfBrandId);
		if(mylink == 400)
			Reporter.log("Passed : Member.link Response code ="+mylink);
		else
			fail("Member.link Response code ="+mylink);
		Reporter.log("-- X --");
		Reporter.log("");
		
		
		Reporter.log("9) Member.link with blank gigya_api_key & site_id");
		myJsonBody ="id="+uuid+"&provider=twitter&provider_id=1002470365&gigya_api_key=&site_id=";
		mylink = ma.memberLINKResponseCode(api, al, myJsonBody, surfBrandId);
		if(mylink == 400)
			Reporter.log("Passed : Member.link Response code ="+mylink);
		else
			fail("Member.link Response code ="+mylink);
		Reporter.log("-- X --");
		Reporter.log("");
		
		
		Reporter.log("10) Member.link with blank site_id");
		myJsonBody ="id="+uuid+"&provider=twitter&provider_id=1002470365&gigya_api_key=2_0rRI64fkQK4eeYOvIgaRI3f4C273i5sEZyaDmCSbFiazYXnVeYdK8X5M-cPi2KcQ&site_id=";
		mylink = ma.memberLINKResponseCode(api, al, myJsonBody, surfBrandId);
		if(mylink == 400)
			Reporter.log("Passed : Member.link Response code ="+mylink);
		else
			fail("Member.link Response code ="+mylink);
		Reporter.log("-- X --");
		Reporter.log("");		
	}	
}