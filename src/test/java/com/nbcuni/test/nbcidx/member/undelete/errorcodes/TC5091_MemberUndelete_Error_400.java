package com.nbcuni.test.nbcidx.member.undelete.errorcodes;

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
 * NBCIDX Member.Undelete API - TC5091_MemberUndelete_Error_400. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Sep 12, 2014
 **************************************************************************/

public class TC5091_MemberUndelete_Error_400 {

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
	@Test(groups = {"full1"})
	public void memberUndelete_error400() throws Exception {

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
		
		Reporter.log("3) Member.remove with blank postbody");
		myJsonBody ="";
		int myRemoveCode = ma.memberREMOVEResponseCode(api, al, myJsonBody, surfBrandId, randomMemberName, uuid, mydb);
		if(myRemoveCode != 400)
			fail("Member.remove Response code ="+myRemoveCode);
		Reporter.log("-- X --");
		Reporter.log("");
		
		
		Reporter.log("4) Member.remove with paramater 'i'");
		myJsonBody ="i="+randomMemberName;
		myRemoveCode = ma.memberREMOVEResponseCode(api, al, myJsonBody, surfBrandId, randomMemberName, uuid, mydb);
		if(myRemoveCode != 400)
			fail("Member.remove Response code ="+myRemoveCode);
		Reporter.log("-- X --");
		Reporter.log("");
		
		
		Reporter.log("5) Member.remove with no 'username'");
		myJsonBody ="username="+randomMemberName;
		myRemoveCode = ma.memberREMOVEResponseCode(api, al, myJsonBody, surfBrandId, randomMemberName, uuid, mydb);
		if(myRemoveCode != 400)
			fail("Member.remove Response code ="+myRemoveCode);
		Reporter.log("-- X --");
		Reporter.log("");
		
	}	
}
