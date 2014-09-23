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

/******************************************************************************
 * NBCIDX Member.Logout API - TC4332_MemberLogout_DirectLogout_Email. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Aug 18, 2014
 *****************************************************************************/

public class TC4332_MemberLogout_DirectLogout_Email {

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
	public void memberLogout_email() throws Exception {
	
		String name=TC4330_MemberLogout_DirectLogout_UUID.randomMemberName;
		String pw=TC4330_MemberLogout_DirectLogout_UUID.randomPassword;
		String email=TC4330_MemberLogout_DirectLogout_UUID.randomEmail; 
		String uuid = TC4330_MemberLogout_DirectLogout_UUID.uuid; 
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		// member.logout with email
		Reporter.log("1) member.login direct login using the email of a member");
		String myJsonBody ="id="+email+"&password="+pw;
		JsonObject mylogin = ma.memberLOGINResponse(api, al, myJsonBody, surfBrandId, name, mydb);
		if(mylogin ==null)
			fail("Error/Null Response from API call");
		Reporter.log("-- X --");
		Reporter.log("");
		
		// member.logout with email
		Reporter.log("2) member.logout with the email of a member");
		myJsonBody ="id="+email;
		JsonObject mylogout = ma.memberLOGOUTResponse(api, al, myJsonBody, surfBrandId, name, mydb);
		if(mylogout ==null)
			fail("Error/Null Response from API call");
		Reporter.log("-- X --");
		Reporter.log("");
		
		//member.remove to remove member from idx
		Reporter.log("3) member.remove to remove member from IDX");
		myJsonBody = "id="+uuid;
		int removeReturnCode = ma.memberREMOVEResponseCode(api, al, myJsonBody, surfBrandId, name, uuid, mydb);
		if(removeReturnCode != 204)
			fail("Failed : HTTP error code : "+ removeReturnCode);
		Reporter.log("-- X --");
		Reporter.log("");
	}
}
