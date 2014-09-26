package com.nbcuni.test.nbcidx.sanityWorkflow;

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

public class TC5323_SC06_TS02_MemberRemove_DB_Verification {
	
	private CustomWebDriver cs;
	private AppLib al;
	private DB mydb;
	
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
		
		Reporter.log(" ");
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MongoDB ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log(" ");
	
		// Get the details of Random Member 
		String name=TC5313_SC01_Create_RandomMemberInfo.randomMemberName;
		String uuid=TC5315_SC02_TS02_MemberPut_DB_Verification.UUID;
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		// Get the DB Response 
		Reporter.log("Validating Member present in MongoDB");
		Reporter.log(" ");
						
		// Build search query for idx_members collection.
		BasicDBObject idx_members_searchQuery = new BasicDBObject();
		idx_members_searchQuery.put("metadata.username.value", name);
		idx_members_searchQuery.put("metadata.username.brand_id",surfBrandId);
				
		BasicDBObject fields = null;
		
		// Check idx_members collection.
		DBObject DbObj = null;
		DbObj = al.getMongoDbResponse(mydb, idx_members_searchQuery, fields, al.IDX_MEMBERS_COLLECTION, al.FIND);	
		if(DbObj==null)
			Reporter.log("Passed : Member "+name+ " successfully deleted from MongoDB Collection ="+al.IDX_MEMBERS_COLLECTION);
		else
			fail("Member "+name+" still exist in MongoDB Collection ="+al.IDX_MEMBERS_COLLECTION);
		Reporter.log(" ");

	}
}
