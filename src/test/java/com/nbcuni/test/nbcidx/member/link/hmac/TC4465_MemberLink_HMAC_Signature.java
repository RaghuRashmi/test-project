package com.nbcuni.test.nbcidx.member.link.hmac;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.gson.JsonArray;
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
 * NBCIDX Member.Link API - TC4465_MemberLink_HMAC_Signature. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Aug 8, 2014
 ************************************************************************************/

public class TC4465_MemberLink_HMAC_Signature {
	
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private DB mydb;
	private Proxy proxy=null;
	
	private String randomMemberUserName = "";
	private String sfinalUUID="";
	private String surfBrandId;
	private String siteId;
	private String secretKey;
	private String provider="automation_test_provider";
	private String providerId;
	private String timeStampUTC;
	private String SignatureHMACCode;
	
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
			api = new API();
			api.setProxy(proxy);
			ma = new MemberAPIs();
			mydb = al.getMongoDbConnection();
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
     */
	@Test(groups = {"full"})
	public void memberLink_HMAC() throws Exception {
			
		// 1) Creating new member using member.put API and 'username' field
		String timeStamp = al.getCurrentTimestamp();
		randomMemberUserName = "test_"+timeStamp;
		Reporter.log("1) Creating new member using member.put API and 'username' field");
		
		//Generate the JSON Body for POST 
		String jsonBody ="{'username':'"+randomMemberUserName+"'}";
				
		// Get Surf Example Site Brand Id
		surfBrandId = al.getSurfBrandId();
		
		//Send member.put POST Request 
		JsonObject response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
		
		// Fetch the UUID from 1st POST 
		JsonElement id = response.get("_id");
		String sUUID = id.toString();
		sfinalUUID = sUUID.substring(1, sUUID.length()-1);
				
		Reporter.log(" ");
		Reporter.log("UUID of created member = "+sfinalUUID);
		
		
		//Get the DB Response 
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating Member present in MongoDB");
		Reporter.log("");
		
		// Find and display
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("metadata.username.value", randomMemberUserName);
				
		BasicDBObject fields = new BasicDBObject();
		fields.put("_id", 1);
		fields.put("metadata.username.value", 1);	
		
		DBObject DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");

		String dbUUID =  DbObj.get("_id").toString();
		String dbresponse = DbObj.toString();				

		boolean bMembername = dbresponse.contains(randomMemberUserName);
		Assert.assertEquals(bMembername, true);
		Assert.assertEquals(sfinalUUID, dbUUID);
			
		Reporter.log("Passed : Member = " +randomMemberUserName+ " present in Mongo Database with UUID = " +dbUUID);
		Reporter.log("-- X --");
		Reporter.log("");
		
		// 2) Getting Brand information using brand.get API
		Reporter.log("2) Getting Brand information using brand.get API");
		
		//Send member.put POST Request 
		response = ma.brandGETResponse(api, al, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
		
		Reporter.log("API - postRestRequest :: Response from Server after <GET>" +response.toString());
		Reporter.log("");
		
		// Get the "sites" array from API response
		JsonArray sites = api.getJsonArray(response, "sites");

		// Convert this "sites" array into Hashmap ArrayList
		ArrayList<HashMap<String, String>> siteData = api.convertJsonArrayToHashMap(sites);
		
		HashMap<String,String> siteWithSecretKey = al.findFromArrayListHashMapResults(siteData, "secret_key");
		if (siteWithSecretKey != null)
		{
			for (Map.Entry<String, String> mapHeadersEntry  : siteWithSecretKey.entrySet()) {
				if(mapHeadersEntry.getKey().equals("secret_key"))
					secretKey= mapHeadersEntry.getValue();
				
				if(mapHeadersEntry.getKey().equals("_id"))
					siteId= mapHeadersEntry.getValue();
			}
			Reporter.log("Found Site with secretKey=" +secretKey +" and siteId =" +siteId);
		}
		else
			fail("Site with secret_key could not found for this brand = "+surfBrandId);
		Reporter.log("-- X --");
		Reporter.log("");
		
		// 3) Generating HMAC for member.link 
		Reporter.log("3) Generating HMAC for member.link with below parameters :");
		providerId = "test_provider"+timeStamp;
		timeStampUTC = al.getUTCtimeStamp();
					
		String data = surfBrandId+","+siteId+","+provider+","+providerId+","+timeStampUTC;
		SignatureHMACCode = al.calculateHMAC(data, secretKey);
		
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ HMAC ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("");
		Reporter.log("brand_id : " + surfBrandId);
		Reporter.log("site_id : " + siteId);
		Reporter.log("secretKey : " + secretKey);
		Reporter.log("provider : " + provider);
		Reporter.log("provider_id : " + providerId);
		Reporter.log("timeStamp : " + timeStampUTC);
		Reporter.log("");
		Reporter.log("Genrated HMAC = "+SignatureHMACCode);
				
		Reporter.log("-- X --");
		Reporter.log(" ");
		
		// 4) Linking member with arbitary provider using member.link API and HMAC Signature
		String myJsonBody ="id="+sfinalUUID+"&provider_id="+providerId+"&provider="+provider+"&site_id="+siteId+"&timestamp="+timeStampUTC+"&signature="+SignatureHMACCode;
		Reporter.log("4) Linking member with arbitary provider using member.link API and HMAC Signature");
		JsonObject linkResponse = ma.memberLINKResponse(api, al, myJsonBody, surfBrandId, sfinalUUID, mydb);
		JsonElement myProviderPair = linkResponse.getAsJsonObject("_provider");
		
		String expectedProviderPair = "{\""+provider+"\":\""+providerId+"\"}";
		boolean bProvider = myProviderPair.toString().equals(expectedProviderPair);
		Assert.assertEquals(bProvider, true);
		Reporter.log("");
		Reporter.log("member.link response contains expected provider-provider_id Pair = "+expectedProviderPair );
		Reporter.log("-- X --");
		Reporter.log("");

		// 5) Removing the member from IDX
		Reporter.log("5) Removing the member from IDX");
		
		//Generate Post Body.
		myJsonBody = "id="+sfinalUUID;
		int removeReturnCode = ma.memberREMOVEResponseCode(api, al, myJsonBody, surfBrandId, randomMemberUserName, sfinalUUID, mydb);
		if(removeReturnCode != 204)
			fail("Failed : HTTP error code : "+ removeReturnCode);
	}
	
}

