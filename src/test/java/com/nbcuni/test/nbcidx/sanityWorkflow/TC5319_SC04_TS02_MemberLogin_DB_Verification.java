package com.nbcuni.test.nbcidx.sanityWorkflow;

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
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/*******************************************************************************
 * NBCIDX Sanity Test Cases - SC04_TC02_MemberLogin_DB_Verification. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Feb 7, 2014
 *******************************************************************************/

public class TC5319_SC04_TS02_MemberLogin_DB_Verification {
	
	private CustomWebDriver cs;
	private AppLib al;
	private DB mydb;
	private API api;
	private Proxy proxy=null;
	
	private JsonObject dbresponse;

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
			api = new API();
			api.setProxy(proxy);
			mydb = al.getMongoDbConnection();
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
	@Test(groups = {"sanity", "DB"})
	public void memberLogin_verifyDB() throws Exception {
		
		Reporter.log(" ");
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log(" ");
	
		String name=TC5313_SC01_Create_RandomMemberInfo.randomMemberName;
		
		// Get the DB Response 				
		Reporter.log("Validating logged_in status for Member " +name+ " in MongoDB");
		Reporter.log(" ");
		
		//Find and display
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("metadata.username.value", name);
				
		BasicDBObject fields = new BasicDBObject();
		fields.put("_id", 1);
		fields.put("metadata.username.value", 1);	
		fields.put("metadata.email.value.address", 1);
		fields.put("metadata._logged_in.value", 1);
					
		DBObject DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			fail("MongoDB Cursor is empty. No Data found from Database");
		else
		{
			dbresponse = al.JsonObjectFromMongoDBObject(DbObj);	
			
			//Check for _logged_in status = true in MongoDB
			ArrayList<HashMap<String, String>> dbLoginStatus = api.convertJsonArrayToHashMap(api.getJsonArray(api.getAsJsonObject(dbresponse, "metadata"),"_logged_in"));
			boolean bloginStatus=dbLoginStatus.get(0).toString().equals("{value=true}");
			Assert.assertEquals(bloginStatus, true);
			
			Reporter.log("Passed : _logged_in status for Member "+ name + " is true in MongoDB Database");
			Reporter.log(" ");
		}
	}
}
