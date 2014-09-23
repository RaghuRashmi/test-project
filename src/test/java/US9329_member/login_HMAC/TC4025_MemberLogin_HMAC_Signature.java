package US9329_member.login_HMAC;

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

/**************************************************************************
 * NBCIDX Member.Login API - TC4661_MemberUnlink_ArbitraryProviders. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC4025_MemberLogin_HMAC_Signature {
	
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private DB mydb;
	private Proxy proxy=null;
	
	public static String randomMemberUserName = "";
	public static String uuid="";
	public static String provider="automation_test_provider";
	public static String providerID;
	public static String brandID;
	public static String siteID;
	public static String secretKey;
	private static String signatureHMAC;
	
	
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
	public void memberLogin_HMAC() throws Exception {
		
		// 1) Creating new member using member.put API and 'username' field
		String timeStamp = al.getCurrentTimestamp();
		randomMemberUserName = "test_"+timeStamp;
		Reporter.log("1) Creating new member using member.put API and 'username' field");
		
		//Generate the JSON Body for POST 
		String jsonBody ="{'username':'"+randomMemberUserName+"'}";
				
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		//Send member.put POST Request 
		JsonObject response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
				
		// Fetch the UUID from 1st POST 
		JsonElement id = response.get("_id");
		String sUUID = id.toString();
		uuid = sUUID.substring(1, sUUID.length()-1);
		Reporter.log(" ");
		Reporter.log("UUID of created member = "+uuid);
				
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
		Assert.assertEquals(uuid, dbUUID);
		Reporter.log("Passed : Member = " +randomMemberUserName+ " present in Mongo Database with UUID = " +dbUUID);
		Reporter.log("-- X --");
		Reporter.log("");
		
		// 2) member.link_noverify
		timeStamp = al.getCurrentTimestamp();
		providerID = "test_provider"+timeStamp;
		
		Reporter.log("");
		Reporter.log("2) Calling member.link_noverify with provider = "+provider + " and providerID = "+providerID);
		Reporter.log("");
		// Generate Post body to link with Gigya.
		String postBody ="id="+uuid+"&provider="+provider+"&provider_id="+providerID;
		int mylink = ma.memberLINKNoVerifyResponse(api, al, postBody, surfBrandId);	
		if (mylink ==200)
			Reporter.log("Linking is successful");
		else
        	fail("Linking is Not successful");
		Reporter.log("-- X --");
		Reporter.log("");
		
		// 3) Getting Brand information using brand.get API
		Reporter.log("3) Getting Brand information using brand.get API");
		
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
					siteID= mapHeadersEntry.getValue();
			}
			Reporter.log("Found Site with secretKey=" +secretKey +" and siteId =" +siteID);
		}
		else
			fail("Site with secret_key could not found for this brand = "+surfBrandId);
		Reporter.log("-- X --");
		Reporter.log("");
		
		// 4) Generating HMAC for member.login 
		String timeStampUTC = al.getUTCtimeStamp();
		String data = surfBrandId+","+siteID+","+provider+","+providerID+","+timeStampUTC;
		signatureHMAC = al.calculateHMAC(data, secretKey);
		
		Reporter.log("4) Generating HMAC for member.login with below parameters :");
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ HMAC ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("");
		Reporter.log("brand_id : " + surfBrandId);
		Reporter.log("site_id : " + siteID);
		Reporter.log("secretKey : " + secretKey);
		Reporter.log("provider : " + provider);
		Reporter.log("provider_id : " + providerID);
		Reporter.log("timeStamp : " + timeStampUTC);
		Reporter.log("");
		Reporter.log("Genrated HMAC = "+signatureHMAC);				
		Reporter.log("-- X --");
		Reporter.log(" ");
		
		// 5) member.login with hmac
		Reporter.log("5) Passwordless member.login with HMAC using below parameters :");
		jsonBody ="id="+providerID+"&provider="+provider+"&site_id="+siteID+"&timestamp="+timeStampUTC+"&signature="+signatureHMAC;
		JsonObject mylogin = ma.memberLOGINResponse(api, al, jsonBody, surfBrandId, randomMemberUserName, mydb);
		if(mylogin ==null)
			fail("Error/Null Response from API call");
		Reporter.log("-- X --");
		Reporter.log(" ");
	}
}

