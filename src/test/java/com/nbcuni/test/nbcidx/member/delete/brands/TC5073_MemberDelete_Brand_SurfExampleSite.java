package com.nbcuni.test.nbcidx.member.delete.brands;

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

/**********************************************************************************
 * NBCIDX Member.Delete API - TC5073_MemberDelete_Brand_SurfExampleSite. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Sep 11, 2014
 *********************************************************************************/

public class TC5073_MemberDelete_Brand_SurfExampleSite {

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
	@Test(groups = {"full"})
	public void memberDelete_SurfExampleSite() throws Exception {
		
		Reporter.log("Validating member.delete with brand = Surf Example Site");
		Reporter.log("");
		Reporter.log("1) Creating member using member.put");
		
		String username = al.getCurrentTimestamp();
				
		//Generate the JSON Body for POST 
		String jsonBody ="{'username': '"+username+"','suffix': 'Phd','firstname': '"+username+"+_firstname','middlename': '-','lastname': '"+username+"+_lastname','phone': [{'number': '9876543214','primary': true},{'mobile': true,'number': '8765432109','primary': false}],'prefix': 'Sir','brand_data': {'Mydata': 'Any-Brand'},'gender': 'm','avatar': 'http://www.mbc.com/','screenname': 'Captain'}";
		
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
		int responseCode = ma.memberDELETEResponseCode(api, al, sApiUUID, surfBrandId, mydb);
		if(responseCode != 204)
			fail("Failed : HTTP error code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log("");
		
		Reporter.log("3) Verifying the member.get does not return the member");
		jsonBody= "id="+sApiUUID;
		responseCode = ma.memberGetResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 404)
			Reporter.log("Passed :member.get does not return the member and gives ERROR : "+ responseCode);
		else 
			fail("Failed : HTTP code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log("");			
		
		Reporter.log("4) Removing the member from IDX using member.remove");
		jsonBody= "id="+sApiUUID;
		int removeReturnCode = ma.memberREMOVEResponseCode(api, al, jsonBody, surfBrandId, username, sApiUUID, mydb);
		if(removeReturnCode != 204)
			fail("Failed : HTTP error code : "+ removeReturnCode);
		Reporter.log("-- X --");
		Reporter.log("");	
	}
}
