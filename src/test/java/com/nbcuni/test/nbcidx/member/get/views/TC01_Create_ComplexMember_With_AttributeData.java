package com.nbcuni.test.nbcidx.member.get.views;

import java.net.Proxy;

import static org.testng.AssertJUnit.fail;
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

/***********************************************************************************
 * NBCIDX Member.Get API - TC01_Create_ComplexMember_With_AttributeData. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **********************************************************************************/

public class TC01_Create_ComplexMember_With_AttributeData {

	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private DB mydb;
	private Proxy proxy=null;
	private static MemberAPIs ma;
	
	public static String randomMemberUserName = "";
	public static  String randomPassword = "";
	public static  String randomEmail= "";
	public static String sfinalUUID;

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
	@Test(groups = {"full"})
	public void createRandomMember() throws Exception {
		
		Reporter.log(" ");
		Reporter.log("/************************ Creating Random Member ********************************/");
		Reporter.log(" ");
		
		String timeStamp = al.getCurrentTimestamp();
		randomMemberUserName = "test_"+timeStamp;
		randomPassword = timeStamp;
		randomEmail = "test"+timeStamp+"@gmail.com";
		
		Reporter.log("1) Random Member Created with below information =>");
		Reporter.log("Member User Name : " + randomMemberUserName);
		Reporter.log("Password : " + randomPassword);
		Reporter.log("Email-Id : " + randomEmail);
		Reporter.log(" ");
		Reporter.log("-- X --");
		Reporter.log("");
	
		// Generate the JSON Body for POST 
		String myJsonBody ="{'username':'"+randomMemberUserName+"','password':'"+randomPassword+"','email':'"+randomEmail+"'}";

		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		// Send member.put POST Request 
		JsonObject response=null;
		response = ma.memberPUTResponse(api, al, myJsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
	
		// Fetch the UUID from 1st POST
		JsonElement id = response.get("_id");
		String sUUID = id.toString();
		sfinalUUID=sUUID.substring(1, sUUID.length()-1);
	
		// Generate the JSON Body for POST using UUID and different Attribution data 
		Reporter.log("Update the member with complex and different Attribution data");
		String twitterAttrJsonBody = "[{'_id':'"+sfinalUUID+"','username':'"+randomMemberUserName+"'},{'username':{'source':'online_action','subsource':'form_editprofile','attribution':'twitter','derivation':'declared','modality':'predictive','site_id':null,'custom_tags':'Summer 2018, Permutation 8, Format 8'}}]";
		twitterAttrJsonBody=al.convertIntoJsonFormat(twitterAttrJsonBody);
	
		String facebookAttrJsonBody = "[{'_id':'"+sfinalUUID+"','username':'"+randomMemberUserName+"'},{'username':{'source':'unspecified','subsource':'form_editprofile','attribution':'facebook','derivation':'unspecified','modality':'predictive','site_id':null,'custom_tags':'Summer 2016, Permutation 6, Format 6'}}]";
		facebookAttrJsonBody=al.convertIntoJsonFormat(facebookAttrJsonBody);
	
		String linkedinAttrJsonBody = "[{'_id':'"+sfinalUUID+"','username':'"+randomMemberUserName+"'},{'username':{'source':'offline_purchase','subsource':'process_linkmember','attribution':'linkedin','derivation':'declared','modality':'predictive','site_id':null,'custom_tags':'Summer 2018, Permutation 8, Format 8'}}]";
		linkedinAttrJsonBody=al.convertIntoJsonFormat(linkedinAttrJsonBody);
	
		String yahooAttrJsonBody = "[{'_id':'"+sfinalUUID+"','username':'"+randomMemberUserName+"'},{'username':{'source':'online_action','subsource':'form_editprofile','attribution':'yahoo','derivation':'declared','modality':'descriptive','site_id':null,'custom_tags':'Summer 2016, Permutation 6, Format 6'}}]";
		yahooAttrJsonBody=al.convertIntoJsonFormat(yahooAttrJsonBody);
	
		String googleAttrJsonBody = "[{'_id':'"+sfinalUUID+"','username':'"+randomMemberUserName+"'},{'username':{'source':'online_view','subsource':'form_editprofile','attribution':'google','derivation':'inferred','modality':'predictive','site_id':null,'custom_tags':'Summer 2018, Permutation 8, Format 8'}}]";
		googleAttrJsonBody=al.convertIntoJsonFormat(googleAttrJsonBody);
	
		String facebookSecondAttrJsonBody = "[{'_id':'"+sfinalUUID+"','username':'"+randomMemberUserName+"'},{'username':{'source':'online_purchase','subsource':'form_editprofile','attribution':'facebook','derivation':'inferred','modality':'predictive','site_id':null,'custom_tags':'Summer 2012, Permutation 2, Format 2'}}]";
		facebookSecondAttrJsonBody=al.convertIntoJsonFormat(facebookSecondAttrJsonBody);
	
		String twitterSecondAttrJsonBody = "[{'_id':'"+sfinalUUID+"','username':'"+randomMemberUserName+"'},{'username':{'source':'online_view','subsource':'form_contest','attribution':'twitter','derivation':'unspecified','modality':'descriptive','site_id':null,'custom_tags':'Summer 2013, Permutation 3, Format 3'}}]";
		twitterSecondAttrJsonBody=al.convertIntoJsonFormat(twitterSecondAttrJsonBody);
	
		String userAttrJsonBody = "[{'_id':'"+sfinalUUID+"','username':'"+randomMemberUserName+"'},{'username':{'source':'online_action','subsource':'form_registration','attribution':'user','derivation':'declared','modality':'descriptive','site_id':null,'custom_tags':'Summer 2011, Permutation 1, Format 1'}}]";
		userAttrJsonBody=al.convertIntoJsonFormat(userAttrJsonBody);
		
		// Send member.put POST Request 
		ma.memberPUTResponse(api, al, twitterAttrJsonBody, surfBrandId);
		ma.memberPUTResponse(api, al, facebookAttrJsonBody, surfBrandId);
		ma.memberPUTResponse(api, al, linkedinAttrJsonBody, surfBrandId);
		ma.memberPUTResponse(api, al, yahooAttrJsonBody, surfBrandId);
		ma.memberPUTResponse(api, al, googleAttrJsonBody, surfBrandId);
		ma.memberPUTResponse(api, al, facebookSecondAttrJsonBody, surfBrandId);
		ma.memberPUTResponse(api, al, twitterSecondAttrJsonBody, surfBrandId);
		ma.memberPUTResponse(api, al, userAttrJsonBody, surfBrandId);
		
		// Display the generated Complex Member from MongoDB 
		Reporter.log("Generated Complex Member from MongoDB => ");
		BasicDBObject searchQuery = new BasicDBObject();
		BasicDBObject fields = new BasicDBObject();
	
		searchQuery.put("_id", sfinalUUID);
		DBObject DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
		Reporter.log("-- X --");
		Reporter.log("");	
	}	
}
