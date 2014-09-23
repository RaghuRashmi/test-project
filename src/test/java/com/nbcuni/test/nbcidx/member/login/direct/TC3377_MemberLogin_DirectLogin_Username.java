package com.nbcuni.test.nbcidx.member.login.direct;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;

import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.nbcidx.MemberAPIs;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/******************************************************************************
 * NBCIDX Member.Login API - TC3377_MemberLogin_DirectLogin_Username. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: June 16, 2014
 *****************************************************************************/

public class TC3377_MemberLogin_DirectLogin_Username {
	
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
	public void memberLogin_username() throws Exception {

		String name=TC3375_MemberLogin_DirectLogin_UUID.randomMemberName;
		String pw=TC3375_MemberLogin_DirectLogin_UUID.randomPassword;
		String uuid=TC3375_MemberLogin_DirectLogin_UUID.uuid;
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();

		// 1) member.login with username
		Reporter.log("1) member.login direct login using the username of a member");
		String myJsonBody ="id="+name+"&password="+pw;
		JsonObject mylogin = ma.memberLOGINResponse(api, al, myJsonBody, surfBrandId, name, mydb);
		if(mylogin ==null)
			fail("Error/Null Response from API call");
		Reporter.log("-- X --");
		Reporter.log("");
		
		// 2) member.logout with username
		Reporter.log("2) member.logout with the username of a member");
		myJsonBody ="id="+name;
		JsonObject mylogout = ma.memberLOGOUTResponse(api, al, myJsonBody, surfBrandId, name, mydb);
		if(mylogout ==null)
			fail("Error/Null Response from API call");
		Reporter.log("-- X --");
		Reporter.log("");
		
		// 3) member.remove with uuid 
		Reporter.log("3) member.remove to remove member from IDX");
		String jsonBody = "id="+uuid;
		int removeReturnCode = ma.memberREMOVEResponseCode(api, al, jsonBody, surfBrandId, name, uuid, mydb);
		if(removeReturnCode != 204)
			fail("Failed : HTTP error code : "+ removeReturnCode);
		Reporter.log("-- X --");
		Reporter.log("");		
	}	
}
