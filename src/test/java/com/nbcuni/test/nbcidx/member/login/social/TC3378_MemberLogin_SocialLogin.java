package com.nbcuni.test.nbcidx.member.login.social;

import static org.testng.AssertJUnit.fail;

/**************************************************************************
 * NBCIDX Member.Login API - TC3378_MemberLogin_SocialLogin. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Aug 16, 2014
 **************************************************************************/

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

import com.gigya.socialize.GSRequest;
import com.gigya.socialize.GSResponse;
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

/*******************************************************************************
 * NBCIDX Member.Login API - TC3378_MemberLogin_SocialLogin. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 *******************************************************************************/

public class TC3378_MemberLogin_SocialLogin {

	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private DB mydb;
	private Proxy proxy=null;
	
	private static String randomMemberName = "";
	private static String randomPassword = "";
	private static String randomEmail= "";
	private static String uuid="";
	private static String gigya_secret_key="";
	private static String gigya_api_key="";
	private static String UIDSignature="";
	private static String signatureTimestamp;
	
	JsonObject response=null;
	
	/**
	 * Instantiate the TestNG Before Class Method.
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
     * 
     * @throws Exception - error
     */
	@Test(groups = {"full"})
	public void memberLogin_SocialLogin() throws Exception {
	
		String timeStamp = al.getCurrentTimestamp();
		randomMemberName = "test_"+timeStamp;
		randomPassword = timeStamp;
		randomEmail = timeStamp+"@gmail.com";
	
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
	
		Reporter.log("1) Created Random Member with below information =>");
		Reporter.log(" ");
		Reporter.log("Member Name : " + randomMemberName);
		Reporter.log("Password : " + randomPassword);
		Reporter.log("Email-Id : " + randomEmail);
		
		//Generate the JSON Body for POST 
		String jsonBody ="{'username': '"+randomMemberName+"','password':'"+randomPassword+"','status': 'active','phone': [{'mobile': false,'number': '2128502384','primary': true}],'address': [{'city': 'Jersey City','country': 'US','address2': null,'primary': true,'state': 'NJ','address1': null,'postalcode': '07310','type': null}],'brand_data': {'email_optin': 'true','custom_data_1': 'custom data'},'gender': 'f','birthdate': {'month': 5,'day': 22,'year': 1947},'email': [{'verified': true,'primary': true,'address': '"+randomEmail+"'}]}";
	
		//Send member.put POST Request 
		response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
			
		// Fetch the UUID from 1st POST 
		JsonElement id = response.get("_id");
		String sUUID = id.toString();
		String sApiUUID = sUUID.substring(1, sUUID.length()-1);
		uuid=sApiUUID;
	
		//Get the DB Response
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating Member present in MongoDB");
		Reporter.log("");
	
		// Find and display
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("metadata.username.value", randomMemberName);
			
		BasicDBObject fields = new BasicDBObject();
		fields.put("_id", 1);
		fields.put("metadata.username.value", 1);	
	
		DBObject DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");

		String dbUUID =  DbObj.get("_id").toString();
		String dbresponse = DbObj.toString();				

		boolean bMembername = dbresponse.contains(randomMemberName);
		Assert.assertEquals(bMembername, true);
		Assert.assertEquals(sApiUUID, dbUUID);
		Reporter.log("Passed : Member = " +randomMemberName+ " present in Mongo Database with UUID = " +dbUUID);
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
	
		// 3) Communicating with Gigya.
		Reporter.log("3) Communicating with Gigya with below information =>");
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Gigya socialize.notifyLogin ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("gigya_api_key : " + gigya_api_key);
		Reporter.log("gigya_secret_key : " + gigya_secret_key);
		Reporter.log("uuid : " + uuid);
		Reporter.log(" ");
		
		// Defining the request
		String method = "socialize.notifyLogin";
		GSRequest request = new GSRequest(gigya_api_key, gigya_secret_key, method);
		request.setProxy(proxy);

		// Adding parameters
		request.setParam("siteUID", uuid);  // set the "uid" parameter to user's ID
	
		// Sending the request
		GSResponse response = request.send();

		// Handling the response.
		if(response.getErrorCode()==0)
		{   // SUCCESS! response status = OK  
			Reporter.log("Response from Gigya socialize.notifyLogin : "+response.toString());
			Reporter.log("");
			UIDSignature = URLEncoder.encode(response.getString("UIDSignature", ""), "ISO-8859-1").toString();
			signatureTimestamp = response.getString("signatureTimestamp", "");
			Reporter.log("Fetched UIDSignature = " +UIDSignature + " & signatureTimestamp = " +signatureTimestamp+ " from Gigya");
			Reporter.log("-- X --");
			Reporter.log("");
		}
		else
		{  // Error
			fail("Got error code: " + response.getErrorCode() +" with error details : " + response.getErrorMessage());
		}
		
		// 4) member.login for Social login.
		Reporter.log("4) Sending member.login request for Social login with Gigya's UIDSignature and signatureTimestamp");
		String myJsonBody ="id="+uuid+"&UIDSignature="+UIDSignature+"&signatureTimestamp="+signatureTimestamp;
		JsonObject mylogin = ma.memberLOGINResponse(api, al, myJsonBody, surfBrandId, randomMemberName, mydb);
		if(mylogin ==null)
			fail("Error/Null Response from API call");
		Reporter.log(mylogin.toString());
		Reporter.log("-- X --");
		Reporter.log("");
		
		//5) Removing member from Gigya.
		Reporter.log("5) Removing member from Gigya.");
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Gigya socialize.deleteAccount ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		method = "socialize.deleteAccount";
		request = new GSRequest(gigya_api_key, gigya_secret_key, method);
		request.setProxy(proxy);

		// Adding parameters
		request.setParam("UID", uuid);  // set the "uid" parameter to user's ID
	
		// Sending the request
		response = request.send();

		// Handling the response.
		if(response.getErrorCode()==0)
		{   // SUCCESS! response status = OK
			Reporter.log("Response from Gigya socialize.deleteAccount : "+response.toString());
			Reporter.log("");
			Reporter.log("Successfully removed member " +uuid+ " from Gigya");
			Reporter.log("-- X --");
			Reporter.log("");
		}
		else
		{  // Error
			fail("Got error code: " + response.getErrorCode() +" with error details : " + response.getErrorMessage());
		}
					
		// 6) member.remove to remove member from IDX
		Reporter.log(" ");
		Reporter.log("6) member.remove to remove member from IDX");
		myJsonBody = "id="+uuid;
		int removeReturnCode = ma.memberREMOVEResponseCode(api, al, myJsonBody, surfBrandId, randomMemberName, uuid, mydb);
		if(removeReturnCode != 204)
			fail("Failed : HTTP error code : "+ removeReturnCode);
		Reporter.log("-- X --");
		Reporter.log("");
	}
}
