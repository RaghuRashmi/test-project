package com.nbcuni.test.nbcidx.member.remove.brands;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;

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
 * NBCIDX Member.Remove API - TC3102_MemberRemove_RemoveByBrand. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Aug 31, 2014
 ***********************************************************************************/

public class TC3102_MemberRemove_RemoveByBrand {

	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private DB mydb;
	private Proxy proxy=null;
	
	private static String surfBrandId;
	private static String idxAPIExplBrandId;
	private String username;
	private String uuidOne;
	private String uuidTwo;
	
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
	public void memberRemove_removeByBrand() throws Exception {
		
		// Get Surf Example Site and IDX Expl Brand Id
		surfBrandId = al.getSurfBrandId();
		idxAPIExplBrandId = al.getAPIExplBrandId();
		
		username = al.getCurrentTimestamp();
		
		Reporter.log("1) Creating member in 2 brands with Aliasing = false");
		boolean emialFlag = false;		
		createMember_inBrands(emialFlag);
		Reporter.log(" ");
		Reporter.log("--X--");
		Reporter.log(" ");
		
		Reporter.log("2) member.get, Getting the SURF member UUID from SURF brand");
		String parameters= "id="+uuidOne;
		int returnCode = ma.memberGetResponseCode(api, al, parameters, surfBrandId);
		if(returnCode != 200)
			fail("Member.get Response code ="+returnCode);
		Reporter.log(" ");
		Reporter.log("--X--");
		Reporter.log(" ");
		
		Reporter.log("3) member.get, Getting the IDX API EXPL member UUID from SURF brand");
		parameters= "id="+uuidTwo;
		returnCode = ma.memberGetResponseCode(api, al, parameters, surfBrandId);
		if(returnCode != 404)
			fail("Member.get Response code ="+returnCode);
		Reporter.log(" ");
		Reporter.log("--X--");
		Reporter.log(" ");
		
		Reporter.log("4) member.remove, Removing the SURF member UUID from IDX API EXPL brand");
		String jsonBody= "id="+uuidOne;
		returnCode = ma.memberREMOVEResponseCode(api, al, jsonBody, idxAPIExplBrandId, username, uuidOne, mydb);
		if(returnCode != 404)
			fail("Member.remove Response code ="+returnCode);
		Reporter.log(" ");
		Reporter.log("--X--");
		Reporter.log(" ");
		
		Reporter.log("5) member.remove, Removing the IDX API EXPL member UUID from SURF brand");
		jsonBody= "id="+uuidTwo;
		returnCode = ma.memberREMOVEResponseCode(api, al, jsonBody, surfBrandId, username, uuidTwo, mydb);
		if(returnCode != 404)
			fail("Member.remove Response code ="+returnCode);
		Reporter.log(" ");
		Reporter.log("--X--");
		Reporter.log(" ");
		
		Reporter.log("6) member.remove, Removing the SURF member UUID from SURF brand");
		jsonBody= "id="+uuidOne;
		returnCode = ma.memberREMOVEResponseCode(api, al, jsonBody, surfBrandId, username, uuidOne, mydb);
		if(returnCode != 204)
			fail("Member.remove Error code ="+returnCode);
		Reporter.log(" ");
		Reporter.log("--X--");
		Reporter.log(" ");
		
		Reporter.log("7) member.get, Checking user still exists in the IDX API EXPL brand");
		parameters= "id="+username;
		returnCode = ma.memberGetResponseCode(api, al, parameters, idxAPIExplBrandId);
		if(returnCode != 200)
			fail("Member.get Response code ="+returnCode);
		Reporter.log(" ");
		Reporter.log("--X--");
		Reporter.log(" ");		
		
		Reporter.log("8) member.remove, Removing the IDX API EXPL member UUID from IDX API EXPL brand");
		jsonBody= "id="+uuidTwo;
		returnCode = ma.memberREMOVEResponseCode(api, al, jsonBody, idxAPIExplBrandId, username, uuidTwo, mydb);
		if(returnCode != 204)
			fail("Member.remove Error code ="+returnCode);
		Reporter.log(" ");
		Reporter.log("--X--");
		Reporter.log(" ");
		
		Reporter.log("9) Creating member in 2 brands with Aliasing = true");
		emialFlag = true;		
		createMember_inBrands(emialFlag);
		Reporter.log(" ");
		Reporter.log("--X--");
		Reporter.log(" ");
		
		Reporter.log("10) member.get, Getting the SURF member UUID from SURF brand");
		parameters= "id="+uuidOne;
		returnCode = ma.memberGetResponseCode(api, al, parameters, surfBrandId);
		if(returnCode != 200)
			fail("Member.get Response code ="+returnCode);
		Reporter.log(" ");
		Reporter.log("--X--");
		Reporter.log(" ");
		
		/*
		Reporter.log("11) member.get, Getting the IDX API EXPL member UUID from SURF brand");
		parameters= "id="+uuidTwo;
		returnCode = ma.memberGetResponseCode(api, al, parameters, surfBrandId);
		if(returnCode != 200)
			fail("Member.get Response code ="+returnCode);
		else
			Reporter.log("Aliasing did work properly with SURF brand");
		Reporter.log(" ");
		Reporter.log("--X--");
		Reporter.log(" ");
		*/
		
		Reporter.log("12) member.get, Getting the SURF member UUID from IDX API EXPL brand");
		parameters= "id="+uuidOne;
		returnCode = ma.memberGetResponseCode(api, al, parameters, idxAPIExplBrandId);
		if(returnCode != 200)
			fail("Member.get Response code ="+returnCode);
		Reporter.log(" ");
		Reporter.log("--X--");
		Reporter.log(" ");
		
		Reporter.log("13) member.get, Getting the IDX API EXPL member UUID from IDX API EXPL brand");
		parameters= "id="+uuidTwo;
		returnCode = ma.memberGetResponseCode(api, al, parameters, idxAPIExplBrandId);
		if(returnCode != 200)
			fail("Member.get Response code ="+returnCode);
		else
			Reporter.log("Aliasing did work properly with IDX API EXPL brand");
		Reporter.log(" ");
		Reporter.log("--X--");
		Reporter.log(" ");
		
		Reporter.log("14) member.remove, Removing the SURF member UUID from SURF brand");
		jsonBody= "id="+uuidOne;
		returnCode = ma.memberREMOVEResponseCode(api, al, jsonBody, surfBrandId, username, uuidOne, mydb);
		if(returnCode != 204)
			fail("Member.remove Response code ="+returnCode);
		Reporter.log(" ");
		Reporter.log("--X--");
		Reporter.log(" ");
		
		Reporter.log("15) member.get, Checking user still exists in the IDX API EXPL brand");
		parameters= "id="+username;
		returnCode = ma.memberGetResponseCode(api, al, parameters, idxAPIExplBrandId);
		if(returnCode != 200)
			fail("Member.get Response code ="+returnCode);
		Reporter.log(" ");
		Reporter.log("--X--");
		Reporter.log(" ");	
		
		Reporter.log("16) member.remove, Removing the IDX API EXPL member UUID from SURF brand");
		jsonBody= "id="+uuidTwo;
		returnCode = ma.memberREMOVEResponseCode(api, al, jsonBody, surfBrandId, username, uuidTwo, mydb);
		if(returnCode != 404)
			fail("Member.remove Response code ="+returnCode);
		Reporter.log(" ");
		Reporter.log("--X--");
		Reporter.log(" ");
		
		Reporter.log("17) member.remove, Removing the username from IDX API EXPL brand");
		jsonBody= "id="+username;
		returnCode = ma.memberREMOVEResponseCode(api, al, jsonBody, idxAPIExplBrandId, username, uuidTwo, mydb);
		if(returnCode != 204)
			fail("Member.remove Error code ="+returnCode);
		Reporter.log(" ");
		Reporter.log("--X--");
		Reporter.log(" ");
		
		Reporter.log("18) Checking the user " +username+ " got completely deleted from MongoDB database for all the brands");
		checkMember_inAllDB(username, uuidOne, uuidTwo);
		Reporter.log("--X--");
		Reporter.log(" ");
		
	}
	
	public void createMember_inBrands(boolean emailVerifiedFlag) throws Exception {
	
		uuidOne=""; uuidTwo="";
		String email=username+"@gmail.com";
		Reporter.log("");
		Reporter.log("A) Creating user " +username+ " in Surf Example Site brand");
		
		//Generate the JSON Body for POST 
		String jsonBody ="{ 'username': '"+username+"', 'status': 'active', 'firstname': 'John', 'lastname': 'Smith', 'phone': [ { 'mobile': false, 'number': '2122122122', 'primary': true } ], 'address': [ { 'city': 'new york', 'country': 'US', 'address2': null, 'primary': true, 'state': 'NY', 'address1': null, 'postalcode': '11201', 'type': null } ], 'brand_data': { 'email_optin': 'true', 'custom_data_1': 'custom data' }, 'gender': 'm', 'birthdate': { 'month': 2, 'day': 2, 'year': 1980 }, 'email': [ { 'verified': "+emailVerifiedFlag+", 'primary': true, 'address': '"+email+"' } ] }";
		
		//Send member.put POST Request 
		JsonObject response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		
		// Fetch the 1st UUID from POST 
		JsonElement id = response.get("_id");
		String sUUID = id.toString();
		uuidOne = sUUID.substring(1, sUUID.length()-1);

		Reporter.log("");
		Reporter.log("B) Creating user " +username+ " in IDX API Explorer brand");
		response = ma.memberPUTResponse(api, al, jsonBody, idxAPIExplBrandId);
				
		// Fetch the 2nd UUID from POST 
		id = response.get("_id");
		sUUID = id.toString();
		uuidTwo = sUUID.substring(1, sUUID.length()-1);
	
	}
	
	public boolean checkMember_inAllDB(String username, String uuidOne, String uuidTwo) throws Exception {
		
		// Get the DB Response
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating Member deleted from MongoDB");
		Reporter.log("");
		
		// Build search query for idx_members collection.
		BasicDBObject idx_members_searchQuery = new BasicDBObject();
		idx_members_searchQuery.put("metadata.username.value", username);
		
		// Build search query for idx_metadata_history collection.
		BasicDBObject idx_metadata_history_searchQuery = new BasicDBObject();
		idx_metadata_history_searchQuery.put("category","Member");
		idx_metadata_history_searchQuery.put("category_id",uuidOne);
			
		BasicDBObject fields = null;

		// Check idx_members collection.
		Reporter.log("Checking MongoDB " +al.IDX_MEMBERS_COLLECTION+ " collection");
		DBObject DbObj = null;
		DbObj = al.getMongoDbResponse(mydb, idx_members_searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			Reporter.log("Passed : Member "+username+ " successfully deleted from MongoDB Collection ="+al.IDX_MEMBERS_COLLECTION);
		else
			fail("Member "+username+" still exist in MongoDB Collection ="+al.IDX_MEMBERS_COLLECTION);
		Reporter.log(" ");

		// Check idx_metadata_history collection for uuidOne.
		Reporter.log("Checking MongoDB " +al.IDX_METADATA_HISTORY_COLLECTION+ " collection for " +uuidOne);
		DbObj=null;
		DbObj = al.getMongoDbResponse(mydb, idx_metadata_history_searchQuery, fields, al.IDX_METADATA_HISTORY_COLLECTION, al.FIND);	
		if(DbObj==null)
			Reporter.log("Passed : Member "+username+ " successfully deleted from MongoDB Collection ="+al.IDX_METADATA_HISTORY_COLLECTION);
		else
			fail("Member "+username+" still exist in MongoDB Collection ="+al.IDX_METADATA_HISTORY_COLLECTION);
		Reporter.log(" ");
		
		// Check idx_metadata_history collection for uuidTwo.
		Reporter.log("Checking MongoDB " +al.IDX_METADATA_HISTORY_COLLECTION+ " collection for " +uuidTwo);
		idx_metadata_history_searchQuery.put("category_id",uuidTwo);
		DbObj=null;
		DbObj = al.getMongoDbResponse(mydb, idx_metadata_history_searchQuery, fields, al.IDX_METADATA_HISTORY_COLLECTION, al.FIND);	
		if(DbObj==null)
			Reporter.log("Passed : Member "+username+ " successfully deleted from MongoDB Collection ="+al.IDX_METADATA_HISTORY_COLLECTION);
		else
			fail("Member "+username+" still exist in MongoDB Collection ="+al.IDX_METADATA_HISTORY_COLLECTION);
		Reporter.log(" ");

		Reporter.log("Passed : Member "+username+ " successfully deleted from all MongoDB Collections");
		return true;
	}
	
}
