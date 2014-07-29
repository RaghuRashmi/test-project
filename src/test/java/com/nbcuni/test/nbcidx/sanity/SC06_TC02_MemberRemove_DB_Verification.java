package com.nbcuni.test.nbcidx.sanity;

import static org.testng.AssertJUnit.fail;

import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.webdriver.CustomWebDriver;

/*******************************************************************************
 * NBCIDX Sanity Test Cases - SC06_TC02_MemberRemove_DB_Verification. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Feb 7, 2014
 *******************************************************************************/

public class SC06_TC02_MemberRemove_DB_Verification {
	
	private CustomWebDriver cs;
	private AppLib al;
	private DB mydb;
	String UUID;
	DBObject myObj;
	String dbresponse;

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
     * @throws Exception - error
     */
	@Test(groups = {"sanity", "DB"})
	public void memberRemove_verifyDB() throws Exception {
	
		// Get the details of Random Member 
		String name=SC01_Create_RandomMemberInfo.randomMemberName;
		
		// Get the DB Response 
		Reporter.log("Validating Member present in MongoDB");
		Reporter.log(" ");
						
		// Find and display 
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("metadata.username.value", name);
				
		BasicDBObject fields = new BasicDBObject();
		fields.put("_id", 1);
		fields.put("metadata.username.value", 1);	
		fields.put("metadata.email.value.address", 1);
		
			
		DBObject DbObj = al.getMongoDbResponse(mydb, searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
		{
			Reporter.log("No Data found for Member "+name+" from Database");
			Reporter.log(" ");
			Reporter.log("Passed : Member "+name+ " successfully deleted from MongoDB");
			Reporter.log(" ");
		}
		else
		{
			Reporter.log("Member "+name+" still exist in MongoDB Database");
			fail("Member "+name+" still exist in Database");
			
		}			
	}
}
