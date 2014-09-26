package com.nbcuni.test.nbcidx.member.put.inputFields;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;

import org.testng.Assert;
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
 * NBCIDX Member.Put API - TC5022_MemberPut_Phone. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC5022_MemberPut_Phone {

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
	public void memberPut_phone() throws Exception {
		
		// Get the 'username' for Random Member
		String sApiUUID = TC01_Create_RandomMemberInfo.sfinalUUID;
		String username = TC01_Create_RandomMemberInfo.randomMemberUserName;
		String phone = TC01_Create_RandomMemberInfo.randomPhone;
				
		Reporter.log("1) Creating Phone field for member "+username+ " with '10 digit string' using member.put API and 'phone' field");
		
		//Generate the JSON Body for POST 
		String jsonBody ="{'_id':'"+sApiUUID+"','phone': [{'number': '"+phone+"','primary': true,'mobile': false}]}";
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		//Send member.put POST Request 
		JsonObject response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");

		//Get the DB Response 
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating PhoneNumber of Member in MongoDB");
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
		fields.put("metadata.phone.value.number", 1);
		
						
		DBObject DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
		Reporter.log("Latest value for Phone Number of this member is = " +phone);
		Reporter.log("-- X --");
		Reporter.log("");
		
			
		Reporter.log("2) Updating Phone field for member "+username+ " with '10 digit string' using member.put API and 'phone' field");
		//Generate the JSON Body for POST 
		phone="2024567890";
		jsonBody ="{'_id':'"+sApiUUID+"','phone':[{'number': '"+phone+"'}]}";
		
		//Send member.put POST Request 
		response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
		
		//Get the DB Response
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating PhoneNumber of Member in MongoDB");
		Reporter.log("");
		
		DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
		Reporter.log("Latest value for Phone Number of this member is = " +phone);
		Reporter.log("-- X --");
		Reporter.log("");


		Reporter.log("3) Updating Phone field for member "+username+ " with 'string more than 10 digit' using member.put API and 'phone' field");
		
		//Generate the JSON Body for POST 
		phone="202456789077979797898779798798797";
		jsonBody ="{'_id':'"+sApiUUID+"','phone':[{'number': '"+phone+"'}]}";
		
		//Send member.put POST Request 
		response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
		
		//Get the DB Response 
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating PhoneNumber of Member in MongoDB");
		Reporter.log("");
		
		DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
		Reporter.log("Latest value for Phone Number of this member is = " +phone);
		Reporter.log("-- X --");						
		Reporter.log("");

		Reporter.log("4) Updating Phone field for member "+username+ " with '2 phone numbers' using member.put API and 'phone' field");
		
		//Generate the JSON Body for POST 
		phone="8469012347";
		String phoneSec = "64623457447";
		jsonBody ="{'_id':'"+sApiUUID+"','phone':[{'number': '"+phone+"'},{'number': '"+phoneSec+"'}]}";
		
		//Send member.put POST Request 
		response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		if(response ==null)
			fail("Error/Null Response from API call");
		
		//Get the DB Response 
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating PhoneNumber of Member in MongoDB");
		Reporter.log("");
		
		DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
		Reporter.log("Latest value for Phone Number of this member is = " +phone);
		Reporter.log("-- X --");

		Reporter.log("5) Updating Phone field for member "+username+ " with 'integer value' using member.put API and 'phone' field");
		Reporter.log("");
		
		//Generate the JSON Body for POST 
		jsonBody = "";
		jsonBody ="{'_id':'"+sApiUUID+"','phone':[{'number': 205467786}]}";

		//Send member.put POST Request 
		int responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		
		if(responseCode == 400)
			Reporter.log("Passed : member.put is not accepting integer value for 'phone' field");
		else
			fail("Failed with Response Code = "+responseCode);
		Reporter.log("-- X --");		
		Reporter.log("");

		Reporter.log("6) Updating Phone field for member "+username+ " with 'string without any digit' using member.put API and 'phone' field");
		Reporter.log("");
		
		//Generate the JSON Body for POST 
		phone="anyphonenumber";
		jsonBody ="{'_id':'"+sApiUUID+"','phone':[{'number': '"+phone+"'}]}";
				
		//Send member.put POST Request 
		responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		
		if(responseCode == 400)
			Reporter.log("Passed : member.put is not accepting 'string without any digit' for 'phone' field");
		else
			fail("Failed with Response Code = "+responseCode);
		Reporter.log("-- X --");				
		Reporter.log("");

		Reporter.log("7) Updating Phone field for member "+username+ " with 'string mix of digits and -' using member.put API and 'phone' field");
		Reporter.log("");
		
		//Generate the JSON Body for POST 
		phone="201-567-7447";
		jsonBody ="{'_id':'"+sApiUUID+"','phone':[{'number': '"+phone+"'}]}";
				
		//Send member.put POST Request 
		responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		
		if(responseCode == 400)
			Reporter.log("Passed : member.put is not accepting 'string mix of digits and -' for 'phone' field");
		else
			fail("Failed with Response Code = "+responseCode);
		Reporter.log("-- X --");		
		Reporter.log("");
	}
}
