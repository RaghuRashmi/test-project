package com.nbcuni.test.nbcidx.member.unlink.social;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;
import java.net.URLEncoder;

import org.testng.Assert;
import org.testng.Reporter;
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

/**************************************************************************
 * NBCIDX Member.Unlink API - TC5328_MemberUnLink_Facebook. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Sep 22, 2014
 **************************************************************************/

public class TC5328_MemberUnLink_Facebook {
	
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private DB mydb;
	private Proxy proxy=null;
	
	public static String username = "";
	public static String pw = "";
	public static String email = "";
	public static String uuid="";
	public static String UIDSignature="";
	public static String signatureTimestamp;
	
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
     * Instantiate the TestNG Test Method.
     * 
     * @throws Exception - error
     */
	@Test(groups = {"full"})
	public void memberUnlink_Facebook() throws Exception {
		
		username = al.getSocialUsername();
		pw = al.getSocialPassword();
		email = al.getSocialValidEmail();
						
		Reporter.log(" ");
		Reporter.log("/************************ Member Fetched from Config File ********************************/");
		Reporter.log(" ");
		
		Reporter.log("1) Random Member generated with below information =>");
		Reporter.log(" ");
		Reporter.log("Member Name : " + username);
		Reporter.log("Email-Id : " + email);
		Reporter.log("Password : " + pw);
		
		//Generate the JSON Body for POST 
		String jsonBody ="{'username':'"+username+"','password':'"+pw+"','status': 'active','phone': [{'mobile': false,'number': '2128502384','primary': true}],'address': [{'city': 'Jersey City','country': 'US','address2': null,'primary': true,'state': 'NJ','address1': null,'postalcode': '07310','type': null}],'brand_data': {'email_optin': 'true','custom_data_1': 'custom data'},'gender': 'f','birthdate': {'month': 5,'day': 22,'year': 1947},'email': [{'verified': true,'primary': true,'address': '"+email+"'}]}";
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		//Send member.put POST Request 
		JsonObject response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response == null)
		{
			fail("Error/Null Response from API call");
		}		
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
		searchQuery.put("_id", uuid);
				
		BasicDBObject fields = new BasicDBObject();
		fields.put("_id", 1);
		fields.put("metadata.username.value", 1);	
		
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
			
		String facebookUID=URLEncoder.encode(al.getFacebookUID(), "ISO-8859-1").toString();
		String facebookProviderUID=URLEncoder.encode(al.getFacebookProviderUID(), "ISO-8859-1").toString();
		
		Reporter.log("2) Linking with Gigya");
		// Generate Post body to link with Gigya.
		String postBody ="id="+email+"&provider=gigya&provider_id="+facebookUID;

		int count = 1; int mylink = 0;
		do{
			mylink = ma.memberLINKResponseCode(api, al, postBody, surfBrandId);
			count++;
        	Thread.sleep(4000);
		}while((count<5) || (mylink == 200));
		Reporter.log("-- X --");
		Reporter.log("");
							
		if(mylink!=200)
			Reporter.log("Linking with Gigya is Not successful");

		Reporter.log("3) Linking with Facebook");
		// Generate Post body to link with Facebook.
		postBody="";
		postBody ="id="+email+"&provider=facebook&provider_id="+facebookProviderUID;

		count = 1;
		mylink = 0;
        do{        
        	mylink = ma.memberLINKResponseCode(api, al, postBody, surfBrandId);
			count++;
        	Thread.sleep(4000);
		}while((count<5) || (mylink == 200));
		Reporter.log("-- X --");
		Reporter.log("");
        
        if(count>4 && mylink!=200)
        	Reporter.log("Linking with Facebook is Not successful");
        
		Reporter.log("4) Unlinking with Facebook");			
        // Generate Post body to Unlink with Facebook.
		postBody ="id="+email+"&provider=facebook&provider_id="+facebookProviderUID;
		
		mylink = ma.memberUNLINKResponseCode(api, al, postBody, surfBrandId);
		if (mylink==200)
			Reporter.log("Unlinking with Facebook is successful");
		else
			fail("Unlinking with Facebook is Not successful");
	}
}
