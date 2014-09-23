package com.nbcuni.test.nbcidx.member.logout.direct;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;

import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.mongodb.DB;
import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.nbcidx.MemberAPIs;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/*********************************************************************************
 * NBCIDX Member.Logout API - TC4331_MemberLogout_DirectLogout_Username. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Aug 18, 2014
 ********************************************************************************/

public class TC4331_MemberLogout_DirectLogout_Username {

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
	public void memberLogout_username() throws Exception {

		String name=TC4330_MemberLogout_DirectLogout_UUID.randomMemberName;
		String pw=TC4330_MemberLogout_DirectLogout_UUID.randomPassword;
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();

		//member.login with username
		Reporter.log("1) member.login direct login using the username of a member");
		String myJsonBody ="id="+name+"&password="+pw;
		JsonObject mylogin = ma.memberLOGINResponse(api, al, myJsonBody, surfBrandId, name, mydb);
		if(mylogin ==null)
			fail("Error/Null Response from API call");
		Reporter.log("-- X --");
		Reporter.log("");
		
		//member.logout with username
		Reporter.log("2) member.logout with the username of a member");
		myJsonBody ="id="+name;
		JsonObject mylogout = ma.memberLOGOUTResponse(api, al, myJsonBody, surfBrandId, name, mydb);
		if(mylogout ==null)
			fail("Error/Null Response from API call");
		Reporter.log("-- X --");
		Reporter.log("");
	}
	
}
