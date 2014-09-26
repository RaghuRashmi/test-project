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
 * NBCIDX Member.Put API - TC5027_MemberPut_Avatar. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC5027_MemberPut_Avatar {

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
	public void memberPut_avatar() throws Exception {
		
		// Get the 'username' for Random Member
		String sApiUUID = TC5023_MemberPut_Prefix.sfinalUUID;
		String username = TC5023_MemberPut_Prefix.username;
						
		Reporter.log("1) Creating Avatar for member "+username+ " using member.put API and 'avatar' field");
		
		//Generate the JSON Body for POST 
		String jsonBody ="{'_id':'"+sApiUUID+"','avatar':'http://mysite.verizon.net/mbsafdar/ej/radioman.jpg'}";
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		//Send member.put POST Request 
		JsonObject response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);	
		if(response ==null)
			fail("Error/Null Response from API call");
					
		//Get the DB Response.
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating Suffix of Member in MongoDB");
		Reporter.log("");
				
		// Find and display
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("_id", sApiUUID);
								
		BasicDBObject fields = new BasicDBObject();
		fields.put("_id", 1);
		fields.put("metadata.username.value", 1);
		fields.put("metadata.firstname.value", 1);
		fields.put("metadata.middlename.value", 1);
		fields.put("metadata.lastname.value", 1);
		fields.put("metadata.email.value.address", 1);
		fields.put("metadata.prefix.value", 1);
		fields.put("metadata.suffix.value", 1);
		fields.put("metadata.avatar.value", 1);
						
		DBObject DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
		Reporter.log("-- X --");					
		Reporter.log("");

		Reporter.log("2) Updating Avatar for member "+username+ " using member.put API and 'avatar' field");
				
		jsonBody = "";
		jsonBody ="{'_id':'"+sApiUUID+"','avatar': 'http://www.nbc.com/'}";
						
		//Send member.put POST Request 
		response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
				
		//Get the DB Response 
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating Avatar updated in MongoDB");
		Reporter.log("");
				
		DbObj = null;
		DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");			
		Reporter.log("-- X --");
		Reporter.log("");
	}
}
