package com.nbcuni.test.nbcidx.member.remove.errorcodes;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;

import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.mongodb.DB;
import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.nbcidx.MemberAPIs;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/**************************************************************************
 * NBCIDX Member.Remove API - TC4935_MemberRemove_Error_404. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Aug 31, 2014
 **************************************************************************/

public class TC4935_MemberRemove_Error_404 {
	
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private DB mydb;
	private Proxy proxy=null;
	
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
	@Test(groups = {"full"})
	public void memberRemove_error404() throws Exception {

		String name="test"+TC4932_MemberRemove_Error_400.randomMemberName;
		String actualName=TC4932_MemberRemove_Error_400.randomMemberName;
		String uuid=TC4932_MemberRemove_Error_400.uuid; 
		
		Reporter.log("");
		Reporter.log("1) Member.remove with non-existent member");
		String myJsonBody ="id="+name;

		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		int myResonseCode = ma.memberREMOVEResponseCode(api, al, myJsonBody, surfBrandId, name, uuid, mydb);
		if(myResonseCode != 404)
			fail("Member.remove Response code ="+myResonseCode);
		Reporter.log("--X--");
		Reporter.log("");		

		Reporter.log("2) Member.remove with existent member but different brand");
		myJsonBody ="id="+uuid;

		// Get Surf Example Site Brand Id
		String apiExplBrandId = al.getAPIExplBrandId();
		
		myResonseCode = ma.memberREMOVEResponseCode(api, al, myJsonBody, apiExplBrandId, actualName, uuid, mydb);
		if(myResonseCode != 404)
			fail("Member.remove Response code ="+myResonseCode);
		Reporter.log("--X--");
		Reporter.log("");
	}
}
