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
 * NBCIDX Member.Put API - TC5019_MemberPut_Lastname. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC5019_MemberPut_Lastname {

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
	public void memberPut_lastname() throws Exception {
		
		// Get the 'username' for Random Member
		String sApiUUID = TC01_Create_RandomMemberInfo.sfinalUUID;
		String username = TC01_Create_RandomMemberInfo.randomMemberUserName;
		String lastname = "(_E-ns/te`in}";
				
		Reporter.log("1) Creating Lastname field for member "+username+ " with value = "+lastname+" using member.put API and 'lastname' field");
		
		//Generate the JSON Body for POST 
		String jsonBody ="{'_id':'"+sApiUUID+"','lastname':'"+lastname+"'}";
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		//Send member.put POST Request 
		JsonObject response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
		
		//Get the DB Response 
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating Lastname of Member in MongoDB");
		Reporter.log("");
		
		// Find and display
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("_id", sApiUUID);
						
		BasicDBObject fields = new BasicDBObject();
		fields.put("_id", 1);
		fields.put("metadata.status.value", 1);
		fields.put("metadata.username.value", 1);
		fields.put("metadata.firstname.value", 1);
		fields.put("metadata.middlename.value", 1);
		fields.put("metadata.lastname.value", 1);
		fields.put("metadata.password.value", 1);
		fields.put("metadata.email.value.address", 1);
		fields.put("metadata.gender.value", 1);
						
		DBObject DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
		
		JsonObject DBResponse =  al.JsonObjectFromMongoDBObject(DbObj);
		ArrayList<HashMap<String, String>> lastnameArrlst = api.convertJsonArrayToHashMap(api.getJsonArray(api.getAsJsonObject(DBResponse, "metadata"),"lastname"));
		Reporter.log("Latest value for Lastname of this member is = " +lastnameArrlst.get(0).toString());
	
		boolean bMemberLastname = lastnameArrlst.get(0).toString().contains(lastname);
		Assert.assertEquals(bMemberLastname, true);
		Reporter.log("Passed : Lastname field created for Member = " +username+ " in MongoDB with value Lastname = "+lastname);
		Reporter.log("-- X --");		
		Reporter.log("");
		
		lastname = TC01_Create_RandomMemberInfo.randomMemberLastName;
		Reporter.log("2) Updating Lastname for member "+username+ " with value = "+lastname+" using member.put API and 'lastname' field");
		
		jsonBody = "";
		jsonBody ="{'_id':'"+sApiUUID+"','lastname':'"+lastname+"'}";
				
		//Send member.put POST Request 
		response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
		
		//Get the DB Response
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating Lastname of Member in MongoDB");
		Reporter.log("");
						
		DbObj = null;
		DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
		
		DBResponse =  al.JsonObjectFromMongoDBObject(DbObj);
		lastnameArrlst = api.convertJsonArrayToHashMap(api.getJsonArray(api.getAsJsonObject(DBResponse, "metadata"),"lastname"));
		Reporter.log("Latest value for Lastname of this member is = " +lastnameArrlst.get(0).toString());
	
		bMemberLastname = lastnameArrlst.get(0).toString().contains(lastname);
		Assert.assertEquals(bMemberLastname, true);
		Reporter.log("Passed : Lastname field updated for Member = " +username+ " in MongoDB with value Lastname = "+lastname);
		Reporter.log("-- X --");				
		Reporter.log("");

		Reporter.log("3) Updating Lastname for member "+username+ " to 'invalid value' using member.put API and 'lastname' field");
		jsonBody = "";
		jsonBody ="{'_id':'"+sApiUUID+"','lastname':@%34}";
	
		//Send member.put POST Request 
		int responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 400)
			Reporter.log("Passed : member.put is not accepting invalid value for 'lastname' field");
		else
			fail("Failed with Response Code = "+responseCode);
					
	}
}
