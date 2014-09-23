package com.nbcuni.test.nbcidx.member.logout.errorcodes;

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
 * NBCIDX Member.Logout API - TC4329_MemberLogout_Error_404. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Aug 18, 2014
 **************************************************************************/

public class TC4329_MemberLogout_Error_404 {
	
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
	public void memberLogout_error404() throws Exception {
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();

		Reporter.log("");
		Reporter.log("1) Member.logout with non existent member");
		String myJsonBody ="id=jssmith74";

		int mylogin = ma.memberLOGOUTResponseCode(api, al, myJsonBody, surfBrandId);
		if(mylogin == 404)
			Reporter.log("Member.login Response code ="+mylogin);
		else
			fail("Member.login Response code ="+mylogin);
		Reporter.log("-- X --");
		Reporter.log("");
				
		Reporter.log("2) Member.logout with non existent member and params 'gigya_api_key' & 'site_id'");
		myJsonBody ="id=jssmith74&maintain_social_login=true&gigya_api_key=456&site_id=123";
		mylogin = ma.memberLOGOUTResponseCode(api, al, myJsonBody, surfBrandId);
		if(mylogin == 404)
			Reporter.log("Member.login Response code ="+mylogin);
		else
			fail("Member.login Response code ="+mylogin);
		Reporter.log("-- X --");
		Reporter.log("");
	}	
}
