package com.nbcuni.test.nbcidx.member.remove.brands;

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

/*****************************************************************************
 * NBCIDX Member.Delete API - TC5272_MemberRemove_Brand_INTERNALc1ab. Copyright. 
 * Run these internal brand tests only in QA.
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Sep 11, 2014
 *****************************************************************************/

public class TC5272_MemberRemove_Brand_INTERNALc1ab {

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
	public void memberRemove_INTERNALc1ab() throws Exception {
				
		Reporter.log("Validating member.put with brand = INTERNAL c1ab");
		Reporter.log("");
		Reporter.log("1) Creating member using member.put in brand = INTERNAL c1ab");
		
		String username = al.getCurrentTimestamp();
				
		//Generate the JSON Body for POST 
		String jsonBody ="{'username': '"+username+"','suffix': 'MS','firstname': '"+username+"+_firstname','middlename': '-','lastname': '"+username+"+_lastname','phone': [{'mobile': true,'number': '8884444333','primary': true}],'prefix': 'Dr.','address': [{'city': 'Harwood Heights','address1': '300 Universal City Plaza','address2': 'Bldg 1660','primary': true,'state': 'IL','country': 'US','postalcode': '60706-3333','type': 'home'}],'brand_data': {'Mydata': 'Any-Brand'},'screenname': 'Captain','gender': 'm','avatar': 'http://www.mbc.com/'}";
		
		// Get c1ab Brand Id
		String internalBrandId = "48f3b0af48c74e778f96a2b9acd9c1ab";
		
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
		
		Reporter.log("2) Removing the member from IDX INTERNALc1ab brand using member.remove");
		jsonBody= "id="+sApiUUID;
		int removeReturnCode = ma.memberREMOVEResponseCode(api, al, jsonBody, internalBrandId, username, sApiUUID, mydb);
		if(removeReturnCode != 204)
			fail("Failed : HTTP error code : "+ removeReturnCode);
		Reporter.log("-- X --");
		Reporter.log("");
	}
}
