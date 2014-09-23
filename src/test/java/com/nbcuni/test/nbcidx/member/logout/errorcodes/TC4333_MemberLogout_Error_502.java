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
 * NBCIDX Member.Logout API - TC4333_MemberLogout_Error_502. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Aug 18, 2014
 **************************************************************************/

public class TC4333_MemberLogout_Error_502 {
	
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
	public void memberLogout_error502() throws Exception {

		String name=TC4325_MemberLogout_Error_400.randomMemberName;
		String uuid=TC4325_MemberLogout_Error_400.uuid; 
		
		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		//Generate the API call for member.put.
		String apicall =  al.getApiURL()+"/member/logout?API_KEY=65432165432165432165432165432165&BRAND_ID=fffff99b56d8d0567d3465876e8579c2";
		
		Reporter.log("");
		Reporter.log("1) Member.logout with 'Delay 6' Brand");
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.LOGOUT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		String myJsonBody ="id="+uuid;
		//Generate the JSON Body in proper format.
		Reporter.log("POST Body= " +myJsonBody);
				
		//Send member.put POST Request. 
		int myResponseCode = al.postHTTPReponseCode(apicall, myJsonBody, myContentType);
		if(myResponseCode == 502)
			Reporter.log("Member.logout Response code ="+myResponseCode);
		else
			fail("Member.logout Response code ="+myResponseCode);
		Reporter.log("-- X --");
		Reporter.log("");
		
		//member.remove to remove member from idx 
		Reporter.log("2) Member.remove to remove member from IDX");
		myJsonBody= "id="+uuid;
		int removeReturnCode = ma.memberREMOVEResponseCode(api, al, myJsonBody, surfBrandId, name, uuid, mydb);
		if(removeReturnCode != 204)
			fail("Failed : HTTP error code : "+ removeReturnCode);	
		Reporter.log("-- X --");
		Reporter.log("");
	}	
}
