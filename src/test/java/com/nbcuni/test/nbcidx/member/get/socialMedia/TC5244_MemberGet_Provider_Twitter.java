package com.nbcuni.test.nbcidx.member.get.socialMedia;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;

import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.nbcidx.MemberAPIs;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/**************************************************************************
 * NBCIDX Member.Get API - TC5244_MemberGet_Provider_Twitter. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC5244_MemberGet_Provider_Twitter {
	
	private CustomWebDriver cs;
	private static AppLib al;
	private static API api;
	private static TC01_FindMemberFromDB member;
	private static DB mydb;
	private static Proxy proxy=null;
	private static MemberAPIs ma;
	
	private static String finalUUID;
	private static String finalProviderId;
	private static String finalUsername;
	private static String finalEmail;
	private static String finalBrands;
	private static String sUUID;
	private static String sProviderId;
	private static String sUsername;
	private static String sEmail;
	private static String sBrands;
	private static boolean bUsername=false;
	private static boolean bEmail=false;
		
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
			mydb = al.getMongoDbConnection();
			api = new API();
			api.setProxy(proxy);
			ma = new MemberAPIs();
			member = new TC01_FindMemberFromDB();
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
	@Test(groups = {"full","social"})
	public static void memberGet_providerTwitter() throws Exception {
		
		//Find the twitter member from MongoDB
		DBObject DbObj = member.findRandomUUIDWithSocialInfo(mydb, "twitter");
		JsonObject DBResponse =  al.JsonObjectFromMongoDBObject(DbObj);
		
		JsonElement UUID = DBResponse.get("_id");
	    sUUID= UUID.toString();
	    finalUUID=sUUID.substring(1, sUUID.length()-1);
	    Reporter.log("Fetched UUID from MongoDB for Twitter member = "+finalUUID);
	    
        JsonElement brands = api.getJsonArray(DBResponse, "brands").get(0);
        sBrands= brands.toString();
        finalBrands=sBrands.substring(1, sBrands.length()-1);
        Reporter.log("Fetched corresponding Brand = "+finalBrands);
		
		JsonObject metadata = api.getAsJsonObject(DBResponse, "metadata");
		
		JsonElement provider = api.getJsonArray(metadata, "_provider").get(0).getAsJsonObject().get("value").getAsJsonObject().get("twitter");
		sProviderId=provider.toString();
	    finalProviderId=sProviderId.substring(1, sProviderId.length()-1);
	    Reporter.log("Fetched corresponding Twitter provider id = " +finalProviderId );
				
		if(metadata.has("username"))
		{
			bUsername = true;
			JsonElement username = api.getJsonArray(metadata, "username").get(0).getAsJsonObject().get("value");
			sUsername = username.toString();
            finalUsername=sUsername.substring(1, sUsername.length()-1);
            Reporter.log("Fetched corresponding username = " + finalUsername);
		}
		else
			Reporter.log("Username is not registered for this member");
				
		if(metadata.has("email"))
		{
			bEmail = true;
			JsonElement email = api.getJsonArray(metadata, "email").get(0).getAsJsonObject().get("value").getAsJsonArray().get(0).getAsJsonObject().get("address");
			sEmail = email.toString();
	        finalEmail=sEmail.substring(1, sEmail.length()-1);
	        Reporter.log("Fetched corresponding email id = " +finalEmail );
		}
		else
			Reporter.log("Email id is not registered for this member");
		Reporter.log("-- X --");
		Reporter.log("");

        Reporter.log("2) Verifying member.get API Response with id=<twitter id> & provider=twitter ");
        String params = "id="+finalProviderId+"&provider=twitter";

		int responseCode =  ma.memberGetResponseCode(api, al, params, finalBrands);
		if(responseCode != 200)
			fail("Error/Null Response from API call");
		Reporter.log("-- X --");
		Reporter.log("");      
                
        Reporter.log("3) Verifying member.get with id=<UUID> & provider=twitter ");
        params = "id="+finalUUID+"&provider=twitter";
		
        responseCode = ma.memberGetResponseCode(api, al, params, finalBrands);
        if(responseCode == 404)
        {
            Reporter.log("Passed : Response code from member.get : "+responseCode);
            Reporter.log(" ");
        }
        else
            fail("Failed : HTTP error code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log("");         
 
        if(bUsername)
        {
            Reporter.log("4) Verifying member.get with id=<username> & provider=twitter ");
            params = "id="+sUsername+"&provider=twitter";

            responseCode = ma.memberGetResponseCode(api, al, params, finalBrands);
            if(responseCode == 404)
            {
                Reporter.log("Passed : Response code from member.get : "+responseCode);
                Reporter.log(" ");
            }
            else
                fail("Failed : HTTP error code : "+ responseCode);
          }
		Reporter.log("-- X --");
		Reporter.log("");
                        
        if(bEmail)
        {
            Reporter.log("5) Verifying member.get with id=<Email> & provider=twitter ");
            params = "id="+finalEmail+"&provider=twitter";

            responseCode = ma.memberGetResponseCode(api, al, params, finalBrands);
            if(responseCode == 404)
            {
                Reporter.log("Passed : Response code from member.get : "+responseCode);
                Reporter.log(" ");
            }
            else
                fail("Failed : HTTP error code : "+ responseCode);            
        }
		Reporter.log("-- X --");
		Reporter.log(""); 	
	}		
}


