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
 * NBCIDX Member.Put API - TC5013_MemberPut_Status. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC5013_MemberPut_Status {

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
	public void memberPut_status() throws Exception {
		
		// Get the 'username' for Random Member
		String sApiUUID = TC01_Create_RandomMemberInfo.sfinalUUID;
		String username = TC01_Create_RandomMemberInfo.randomMemberUserName;
				
		Reporter.log("1) Updating status for member "+username+ " to 'pending' using member.put API and 'status' field");
		
		//Generate the JSON Body for POST 
		String jsonBody ="{'_id':'"+sApiUUID+"','status':'pending'}";
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		//Send member.put POST Request 
		JsonObject response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);	
		if(response ==null)
			fail("Error/Null Response from API call");

		//Get the DB Response 
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating Status of Member in MongoDB");
		Reporter.log("");
		
		// Find and display
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("metadata.username.value", username);
				
		BasicDBObject fields = new BasicDBObject();
		fields.put("_id", 1);
		fields.put("metadata.status.value", 1);
		fields.put("metadata.username.value", 1);	
		fields.put("metadata.password.value", 1);	
				
		DBObject DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
		
		JsonObject DBResponse =  al.JsonObjectFromMongoDBObject(DbObj);
		ArrayList<HashMap<String, String>> statusArrlst = api.convertJsonArrayToHashMap(api.getJsonArray(api.getAsJsonObject(DBResponse, "metadata"),"status"));
		Reporter.log("Latest status of this member is = " +statusArrlst.get(0).toString());
		boolean bMemberStatus = statusArrlst.get(0).toString().contains("pending");
		Assert.assertEquals(bMemberStatus, true);
		Reporter.log("Passed : Status of Member = " +username+ " in MongoDB is 'pending'.");
		Reporter.log("-- X --");
		Reporter.log("");
		
		Thread.sleep(4000);
		Reporter.log("2) Updating status for member "+username+ " to 'deleted' using member.put API and 'status' field");
		
		jsonBody = "";
		jsonBody ="{'_id':'"+sApiUUID+"','status':'deleted'}";
				
		//Send member.put POST Request 
		response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
		
		//Get the DB Response 
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating Username updated in MongoDB");
		Reporter.log("");
				
		DbObj = null;
		DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
		
		DBResponse =  al.JsonObjectFromMongoDBObject(DbObj);
		statusArrlst = api.convertJsonArrayToHashMap(api.getJsonArray(api.getAsJsonObject(DBResponse, "metadata"),"status"));
		Reporter.log("Latest status of this member is = " +statusArrlst.get(0).toString());
		bMemberStatus = statusArrlst.get(0).toString().contains("deleted");
		Assert.assertEquals(bMemberStatus, true);
		Reporter.log("Passed : Status of Member = " +username+ " in MongoDB is 'deleted'.");
		Reporter.log("-- X --");
		Reporter.log("");
		
		Thread.sleep(4000);
		Reporter.log("3) Updating status for member "+username+ " to 'banned' using member.put API and 'status' field");
		
		jsonBody = "";
		jsonBody ="{'_id':'"+sApiUUID+"','status':'banned'}";
				
		//Send member.put POST Request 
		response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
		
		//Get the DB Response 
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating Username updated in MongoDB");
		Reporter.log("");
				
		DbObj = null;
		DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
		
		DBResponse =  al.JsonObjectFromMongoDBObject(DbObj);
		statusArrlst = api.convertJsonArrayToHashMap(api.getJsonArray(api.getAsJsonObject(DBResponse, "metadata"),"status"));
		Reporter.log("Latest status of this member is = " +statusArrlst.get(0).toString());
		bMemberStatus = statusArrlst.get(0).toString().contains("banned");
		Assert.assertEquals(bMemberStatus, true);
		Reporter.log("Passed : Status of Member = " +username+ " in MongoDB is 'banned'.");
		Reporter.log("-- X --");
		Reporter.log("");
		
		Thread.sleep(4000);
		Reporter.log("4) Updating status for member "+username+ " to 'active' using member.put API and 'status' field");
		
		jsonBody = "";
		jsonBody ="{'_id':'"+sApiUUID+"','status':'active'}";
				
		//Send member.put POST Request 
		response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
		
		//Get the DB Response 
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating Username updated in MongoDB");
		Reporter.log("");
				
		DbObj = null;
		DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");

		DBResponse =  al.JsonObjectFromMongoDBObject(DbObj);
		statusArrlst = api.convertJsonArrayToHashMap(api.getJsonArray(api.getAsJsonObject(DBResponse, "metadata"),"status"));
		Reporter.log("Latest status of this member is = " +statusArrlst.get(0).toString());
		bMemberStatus = statusArrlst.get(0).toString().contains("active");
		Assert.assertEquals(bMemberStatus, true);
		Reporter.log("Passed : Status of Member = " +username+ " in MongoDB is 'active'.");
		Reporter.log("-- X --");
		Reporter.log("");
		
		Thread.sleep(4000);
		Reporter.log("5) Updating status for member "+username+ " to 'invalid string value' using member.put API and 'status' field");
		
		jsonBody = "";
		jsonBody ="{'_id':'"+sApiUUID+"','status':'xyz'}";
		String myJsonBodyAfterFormat=al.convertIntoJsonFormat(jsonBody);
		Reporter.log("JSON Body for POST = " +myJsonBodyAfterFormat);

		//Send member.put POST Request 
		int responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 400)
			Reporter.log("Passed : member.put is not accepting invalid string value for 'status' field");
		else
			fail("Failed with Response Code = "+responseCode);
					
	}
}
