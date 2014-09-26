package com.nbcuni.test.nbcidx.member.undelete.brands;

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

/*********************************************************************************
 * NBCIDX Member.Delete API - TC5087_MemberUndelete_Brand_INTERNALf645. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Sep 12, 2014
 ********************************************************************************/

public class TC5087_MemberUndelete_Brand_INTERNALf645 {

	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private DB mydb;
	private Proxy proxy=null;
	
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
	
	/**
	 * Instantiate the TestNG After Class Method.
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
     */
	@Test(groups = {"QA_Internal"})
	public void memberUndelete_INTERNALf645() throws Exception {
		
		Reporter.log("Validating member.undelete with brand = INTERNAL f645");
		Reporter.log("");
		Reporter.log("1) Creating member using member.put");
		
		String username = al.getCurrentTimestamp();
				
		//Generate the JSON Body for POST 
		String jsonBody ="{'username': '"+username+"','suffix': 'Phd','phone': [{'mobile': true,'number': '3053496333','primary': false},{'number': '4096789304'}],'prefix': 'Sir','brand_data': {'Mydata': 'Any-Brand'},'avatar': 'http://www.nbc.com/','address': [{'address1': '150','address2': 'Jackson Avenue','city': 'Parlin','state': 'NJ','postalcode': '08859','country': 'US','primary': true,'type': 'home'}],'screenname': 'Captain'}";
		
		// Get f645 Brand Id
		String internalBrandId = "4ae4a1d71eae4e90a6f9c98ccf71f645";
		
		//Send member.put POST Request 
		JsonObject response = ma.memberPUTResponse(api, al, jsonBody, internalBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
		
		// Fetch the UUID from 1st POST 
		JsonElement id = response.get("_id");
		String sUUID = id.toString();
		String sApiUUID = sUUID.substring(1, sUUID.length()-1);
		
		//Get the DB Response 		
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating brand_data in MongoDB");
		Reporter.log("");
		
		// Find and display
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("metadata.username.value", username);
				
		BasicDBObject fields = new BasicDBObject();
		fields.put("_id", 1);
		fields.put("metadata.status.value", 1);
		fields.put("metadata.username.value", 1);	
		fields.put("metadata.password.value", 1);
		fields.put("metadata.suffix.value", 1);
		fields.put("metadata.prefix.value", 1);
		fields.put("metadata.screenname.value", 1);
		fields.put("metadata.brand_data.value", 1);
				
		DBObject DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
		
		String dbUUID =  DbObj.get("_id").toString();
		String dbresponse = DbObj.toString();				
	
		boolean bMembername = dbresponse.contains(username);
		Assert.assertEquals(bMembername, true);
		Assert.assertEquals(sApiUUID, dbUUID);
		Reporter.log("Passed : Member = " +username+ " present in Mongo Database with UUID = " +dbUUID);
		Reporter.log("-- X --");
		Reporter.log("");
	
		Reporter.log("2) Deleting the member from IDX using member.delete");
		jsonBody= "id="+sApiUUID;
		int responseCode = ma.memberDELETEResponseCode(api, al, sApiUUID, internalBrandId, mydb);
		if(responseCode != 204)
			fail("Failed : HTTP error code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log("");
		
		Reporter.log("3) Verifying the member.get does not return the member");
		jsonBody= "id="+sApiUUID;
		responseCode = ma.memberGetResponseCode(api, al, jsonBody, internalBrandId);
		if(responseCode == 404)
			Reporter.log("Passed :member.get does not return the member and gives ERROR : "+ responseCode);
		else 
			fail("Failed : HTTP code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log("");				
		
		Reporter.log("4) Undeleting the member from IDX using member.undelete");
		jsonBody= "id="+sApiUUID;
		responseCode = ma.memberUNDELETEResponseCode(api, al, sApiUUID, internalBrandId, mydb);
		if(responseCode != 200)
			fail("Failed : HTTP error code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log("");
		
		Reporter.log("5) Verifying the member.get now returns the member again");
		jsonBody= "id="+sApiUUID;
		responseCode = ma.memberGetResponseCode(api, al, jsonBody, internalBrandId);
		if(responseCode == 200)
			Reporter.log("Passed : member.get is now returning the member again");
		else 
			fail("Failed : HTTP code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log("");		
		
		Reporter.log("6) Removing the member from IDX using member.remove");
		jsonBody= "id="+sApiUUID;
		int removeReturnCode = ma.memberREMOVEResponseCode(api, al, jsonBody, internalBrandId, username, sApiUUID, mydb);
		if(removeReturnCode != 204)
			fail("Failed : HTTP error code : "+ removeReturnCode);
		Reporter.log("-- X --");
		Reporter.log("");
	
	}
}
