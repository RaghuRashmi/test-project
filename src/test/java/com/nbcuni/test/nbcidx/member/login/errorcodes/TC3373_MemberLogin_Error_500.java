package com.nbcuni.test.nbcidx.member.login.errorcodes;

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
 * NBCIDX Member.Login API - TC3373_MemberLogin_Error_500. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: June 16, 2014
 **************************************************************************/

public class TC3373_MemberLogin_Error_500 {
	
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
	public void memberLogin_error500() throws Exception {

		String name=TC3369_MemberLogin_Error_400.randomMemberName;
				
		Reporter.log("");
		Reporter.log("1) Checking member.login with invalid 'signatureTimestamp'");
		String myJsonBody ="id="+name+"&signatureTimestamp=unknown &UIDSignature=140190921";

		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		int mylogin = ma.memberLOGINResponseCode(api, al, myJsonBody, surfBrandId);
		if(mylogin == 500)
			Reporter.log("Member.login Response code ="+mylogin);
		else
			fail("Member.login Response code ="+mylogin);
		Reporter.log("-- X --");
		Reporter.log("");			
	}	
}
