package com.nbcuni.test.nbcidx.member.put.inputFields;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;

import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.nbcidx.MemberAPIs;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/**************************************************************************
 * NBCIDX Member.Put API - TC5021_MemberPut_Birthdate. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC5021_MemberPut_Birthdate {

	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private DB mydb;
	private Proxy proxy=null;
	
	JsonObject response=null;
	int responseCode;
	
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
	public void memberPut_birthdate() throws Exception {
		
		// Get the 'username' for Random Member
		String sApiUUID = TC01_Create_RandomMemberInfo.sfinalUUID;
		String username = TC01_Create_RandomMemberInfo.randomMemberUserName;
		int day = TC01_Create_RandomMemberInfo.birthdateDay;
		int month = TC01_Create_RandomMemberInfo.birthdateMonth;
		int year = TC01_Create_RandomMemberInfo.birthdateYear;
				
		Reporter.log("1) Creating Birthdate field for member "+username+ " using member.put API and 'birthdate' field");
		
		//Generate the JSON Body for POST 
		String jsonBody ="{'_id':'"+sApiUUID+"','birthdate':{'month':"+month+",'day':"+day+",'year':"+year+"}}";
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		//Send member.put POST Request 
		JsonObject response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");

		//Get the DB Response 
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating Birthdate of Member in MongoDB");
		Reporter.log("");
		
		// Find and display
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("_id", sApiUUID);
						
		BasicDBObject fields = new BasicDBObject();
		fields.put("_id", 1);
		fields.put("metadata.status.value", 1);
		fields.put("metadata.username.value", 1);
		fields.put("metadata.firstname.value", 1);
		fields.put("metadata.middlename.value", 1);
		fields.put("metadata.password.value", 1);
		fields.put("metadata.email.value.address", 1);
		fields.put("metadata.gender.value", 1);
		fields.put("metadata.birthdate.value", 1);
						
		DBObject DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
		
		JsonObject DBResponse =  al.JsonObjectFromMongoDBObject(DbObj);
		ArrayList<HashMap<String, String>> birthdateArrlst = api.convertJsonArrayToHashMap(api.getJsonArray(api.getAsJsonObject(DBResponse, "metadata"),"birthdate"));
		Reporter.log("Latest value for BirthDate of this member is = " +birthdateArrlst.get(0).toString());
		Reporter.log("Passed : Birthdate field created for Member = " +username+ " in MongoDB with value Birthdate = "+birthdateArrlst.get(0).toString());
		Reporter.log("-- X --");				
		Reporter.log("");

		Reporter.log("2) Updating only 'month' field of Birthdate for member "+username+ " using member.put API and 'birthdate' field");
		
		//Generate the JSON Body for POST 
		month=1;
		jsonBody ="{'_id':'"+sApiUUID+"','birthdate':{'month':"+month+"}}";
		
		//Send member.put POST Request 
		response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
		Reporter.log("-- X --");		
		Reporter.log("");

		Reporter.log("3) Updating only 'day' field of Birthdate for member "+username+ " using member.put API and 'birthdate' field");
		
		//Generate the JSON Body for POST 
		day=31;
		jsonBody ="{'_id':'"+sApiUUID+"','birthdate':{'day':"+day+"}}";
		
		//Send member.put POST Request 
		response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
		Reporter.log("-- X --");		
		Reporter.log("");

		Reporter.log("4) Updating only 'year' field of Birthdate for member "+username+ " using member.put API and 'birthdate' field");
		
		//Generate the JSON Body for POST 
		year = 2004;
		jsonBody ="{'_id':'"+sApiUUID+"','birthdate':{'year':"+year+"}}";
		
		//Send member.put POST Request 
		response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
		Reporter.log("-- X --");
		Reporter.log("");

		day=0;
		month=0;
		year=0;
		Reporter.log("5) Updating Birthdate for member "+username+ " with value = 0 using member.put API and 'birthdate' field");
		Reporter.log("");
		
		jsonBody = "";
		jsonBody ="{'_id':'"+sApiUUID+"','birthdate':{'month':0,'day':0,'year':0}}";	

		//Send member.put POST Request 
		int responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);		
		if(responseCode == 400)
			Reporter.log("Passed : member.put is not accepting 0 value for 'Birthdate' field");
		else
			fail("Failed with Response Code = "+responseCode);
		Reporter.log("-- X --");
		Reporter.log("");

		Reporter.log("6) Updating 'year' field of Birthdate with 2 digits for member "+username+ " using member.put API and 'birthdate' field");
		Reporter.log("");
		
		//Generate the JSON Body for POST 
		year = 74;
		jsonBody ="{'_id':'"+sApiUUID+"','birthdate':{'year':"+year+"}}";

		//Send member.put POST Request 
		responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 400)
			Reporter.log("Passed : member.put is not accepting 2 digit value for 'year' field of Birthdate.");
		else
			fail("Failed with Response Code = "+responseCode);
		Reporter.log("-- X --");		
		Reporter.log("");
		
		Reporter.log("7) Updating Birthdate for member "+username+ " with 'invalid string value' using member.put API and 'birthdate' field");
		Reporter.log("");
		
		jsonBody = "";
		jsonBody ="{'_id':'"+sApiUUID+"','birthdate':{'month':'#12','day':'~)','year':'qwer'}}";	

		//Send member.put POST Request 
		responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 400)
			Reporter.log("Passed : member.put is not accepting 'invalid string value' value for 'Birthdate' field");
		else
			fail("Failed with Response Code = "+responseCode);
		Reporter.log("-- X --");
				
	}
}
