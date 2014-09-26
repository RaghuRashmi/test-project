package com.nbcuni.test.nbcidx.member.link.social;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.gigya.socialize.GSRequest;
import com.gigya.socialize.GSResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.DB;
import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.nbcidx.MemberAPIs;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/**************************************************************************
 * NBCIDX Member.Link API - TC49992A_MemberRemove. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Sep 10, 2014
 **************************************************************************/

public class TC49992A_MemberRemove {
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private DB mydb;
	private Proxy proxy=null;
	
	public static String gigya_secret_key="";
	public static String gigya_api_key="";
	
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

	@Test(groups = {"full"})
	public void memberRemove() throws Exception {

		String username = TC4988_MemberLink_Facebook.username;
		String uuid=TC4988_MemberLink_Facebook.uuid;
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		Reporter.log("1) Removing member from IDX");
		//Generate Post Body.
		String postBody = "id="+uuid;

		//Send member.remove POST Request 
		int removeReturnCode = ma.memberREMOVEResponseCode(api, al, postBody, surfBrandId, username, uuid, mydb);
		if(removeReturnCode != 204)
			fail("Failed : HTTP error code : "+ removeReturnCode);	
			
		/*
		// Getting Brand information using brand.get API
		Reporter.log("2) Getting Brand information using brand.get API");
	
		//Send member.put POST Request 
		JsonObject response = ma.brandGETResponse(api, al, surfBrandId);
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
					if(mapHeadersEntry.getKey().equals("gigya_api_key"))
						gigya_api_key= mapHeadersEntry.getValue();
			
					if(mapHeadersEntry.getKey().equals("gigya_secret_key"))
						gigya_secret_key= mapHeadersEntry.getValue();
				}
				Reporter.log("Found gigya_api_key=" +gigya_api_key +" and gigya_secret_key =" +gigya_secret_key);
			}
		else
			fail("Site with secret_key could not found for this brand = "+surfBrandId);
		Reporter.log("-- X --");
		Reporter.log("");
		
		
		//Removing member from Gigya.
		Reporter.log("3) Removing member from gigya with below information =>");
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Gigya socialize.notifyLogin ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("gigya_api_key : " + gigya_api_key);
		Reporter.log("gigya_secret_key : " + gigya_secret_key);
		Reporter.log("uuid : " + uuid);
		Reporter.log(" ");
				
		// Defining the request
		String method = "socialize.deleteAccount";
		GSRequest request = new GSRequest(gigya_api_key, gigya_secret_key, method);
		request.setProxy(proxy);

		// Adding parameters
		request.setParam("UID", uuid);  // set the "uid" parameter to user's ID
					
		// Sending the request
		GSResponse gResponse = request.send();

		// Handling the response.
		if(gResponse.getErrorCode()==0)
		{   // SUCCESS! response status = OK
			Reporter.log("Response from Gigya socialize.deleteAccount : "+gResponse.toString());
			Reporter.log("");
			Reporter.log("Successfully removed member " +uuid+ " from Gigya");
			Reporter.log("-- X --");
			Reporter.log("");
		}
		else
		{  // Error
			fail("Got error code: " + gResponse.getErrorCode() +" with error details : " + gResponse.getErrorMessage());
		}
	*/
	}
}
