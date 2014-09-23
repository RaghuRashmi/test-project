package com.nbcuni.test.nbcidx.member.link.errorcodes;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;
import java.net.URLEncoder;
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
import com.mongodb.DB;
import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.nbcidx.MemberAPIs;
import com.nbcuni.test.nbcidx.member.link.social.TC4988_MemberLink_Facebook;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/**************************************************************************
 * NBCIDX Member.Link API - TC4997_MemberLink_Error_409. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Sep 10, 2014
 **************************************************************************/

public class TC4997_MemberLink_Error_409 {
	
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private DB mydb;
	private Proxy proxy=null;
	
	private String randomMemberUserNameOne = "";
	private String randomMemberUserNameTwo = "";
	private String sfinalUUIDOne="";
	private String sfinalUUIDTwo="";
	private String surfBrandId;
	private String siteId;
	private String secretKey;
	private String provider="automation_test_provider";
	private String providerId;
	private String timeStampUTC;
	private String SignatureHMACCode;
	
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
     * 
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
     * 
     * @throws Exception - error
     */

	@Test(groups = {"full"})
	public void memberLink_error409() throws Exception {

		// 1) Creating new member using member.put API and 'username' field
		String timeStamp = al.getCurrentTimestamp();
		randomMemberUserNameOne = "test1_"+timeStamp;
		Reporter.log("1) Creating 2 new members using member.put API and 'username' field");
		
		//Generate the JSON Body for POST 
		String jsonBody ="{'username':'"+randomMemberUserNameOne+"'}";
				
		// Get Surf Example Site Brand Id
		surfBrandId = al.getSurfBrandId();
		
		//Send member.put POST Request 
		JsonObject response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
		
		// Fetch the UUID from 1st POST 
		JsonElement id = response.get("_id");
		String sUUID = id.toString();
		sfinalUUIDOne = sUUID.substring(1, sUUID.length()-1);
		
		randomMemberUserNameTwo = "test2_"+timeStamp;
		jsonBody = null;
		jsonBody ="{'username':'"+randomMemberUserNameTwo+"'}";
		
		//Send member.put POST Request 
		response = null;
		response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
				
		// Fetch the UUID from 1st POST 
		id = response.get("_id");
		sUUID = id.toString();
		sfinalUUIDTwo = sUUID.substring(1, sUUID.length()-1);
				
		Reporter.log(" ");
		Reporter.log("UUID of created members, member1 = "+sfinalUUIDOne+ "and member2 = "+sfinalUUIDTwo);
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
		
		// Trying to link two members with same arbitary provider/provider-id
		// Linking first member with arbitary provider using member.link API and HMAC Signature
		String myJsonBody ="id="+sfinalUUIDOne+"&provider_id="+providerId+"&provider="+provider+"&site_id="+siteId+"&timestamp="+timeStampUTC+"&signature="+SignatureHMACCode;

		Reporter.log("4) Trying to link two members with same arbitary provider/provider-id");
		Reporter.log(" ");
		Reporter.log("Linking first member with arbitary provider using member.link API and HMAC Signature");
		JsonObject linkResponse = ma.memberLINKResponse(api, al, myJsonBody, surfBrandId, sfinalUUIDOne, mydb);
		JsonElement myProviderPair = linkResponse.getAsJsonObject("_provider");
		
		String expectedProviderPair = "{\""+provider+"\":\""+providerId+"\"}";
		boolean bProvider = myProviderPair.toString().equals(expectedProviderPair);
		Assert.assertEquals(bProvider, true);
		Reporter.log("");
		Reporter.log("member.link response contains expected provider-provider_id Pair = "+expectedProviderPair );
		
		Reporter.log("-- X --");
		Reporter.log(" ");		
		
		// Linking second member with arbitary provider using member.link API and HMAC Signature
		myJsonBody ="id="+sfinalUUIDTwo+"&provider_id="+providerId+"&provider="+provider+"&site_id="+siteId+"&timestamp="+timeStampUTC+"&signature="+SignatureHMACCode;
		Reporter.log("Linking second member with arbitary provider using member.link API and HMAC Signature");
		int linkResponseCode = ma.memberLINKResponseCode(api, al, myJsonBody, surfBrandId);
		if(linkResponseCode == 409)
			Reporter.log("Member.link Response code ="+linkResponseCode);
		else
			fail("Member.link Response code ="+linkResponseCode);
		
		Reporter.log("-- X --");
		Reporter.log("");	
	}	
}
