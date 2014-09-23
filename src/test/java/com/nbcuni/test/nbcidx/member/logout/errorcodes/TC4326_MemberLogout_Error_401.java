package com.nbcuni.test.nbcidx.member.logout.errorcodes;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.mongodb.DBObject;
import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.nbcidx.MemberAPIs;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/**************************************************************************
 * NBCIDX Member.Logout API - TC4326_MemberLogout_Error_401. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Aug 18, 2014
 **************************************************************************/

public class TC4326_MemberLogout_Error_401 {
	
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
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
			api = new API();
			api.setProxy(proxy);
			ma = new MemberAPIs();
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
	public void memberLogout_error401() throws Exception {

		String name=TC4325_MemberLogout_Error_400.randomMemberName;
		
		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		Reporter.log("");
		Reporter.log("1) Member.logout with invalid API_KEY : ");
		Reporter.log("");
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.LOGOUT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the API call for member.put.
		String apicall = al.getApiURL()+"/member/logout?API_KEY=abc#&BRAND_ID="+al.getSurfBrandId();

		String myJsonBody ="id="+name;
		//Generate the JSON Body in proper format.
		Reporter.log("POST Body= " +myJsonBody);
				
		//Send member.put POST Request. 
		int myResponseCode = al.postHTTPReponseCode(apicall, myJsonBody, myContentType);
		if(myResponseCode == 401)
			Reporter.log("Member.logout Response code ="+myResponseCode);
		else
			fail("Member.logout Response code ="+myResponseCode);
		Reporter.log("-- X --");
		Reporter.log("");
		
		Reporter.log("2) Member.logout with empty API_KEY : ");
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.LOGOUT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		apicall = al.getApiURL()+"/member/logout?API_KEY=&BRAND_ID="+al.getSurfBrandId();

		myJsonBody ="id="+name;
		//Generate the JSON Body in proper format.
		Reporter.log("POST Body= " +myJsonBody);
				
		//Send member.put POST Request. 
		myResponseCode = al.postHTTPReponseCode(apicall, myJsonBody, myContentType);
		if(myResponseCode == 401)
			Reporter.log("Member.logout Response code ="+myResponseCode);
		else
			fail("Member.logout Response code ="+myResponseCode);
		Reporter.log("-- X --");
		Reporter.log("");
	}
}
