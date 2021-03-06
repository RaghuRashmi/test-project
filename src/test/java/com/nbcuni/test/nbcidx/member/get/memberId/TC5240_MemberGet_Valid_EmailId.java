package com.nbcuni.test.nbcidx.member.get.memberId;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;

//import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.nbcidx.MemberAPIs;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/**************************************************************************
 * NBCIDX Member.Get API - TC5240_MemberGet_Valid_EmailId. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC5240_MemberGet_Valid_EmailId {

	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private Proxy proxy=null;
	private MemberAPIs ma;

	/**
	 * Instantiate the TestNG Before Class Method.
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
     */
	@Test(groups = {"full"})
	public void memberGet_emailid() throws Exception {
		
		String params, responseString;
				
		String email=TC01_FindMemberFromDB.finalEmail;
		
		Reporter.log("1) Validating member.get API Response with id=<valid Email-Id>");
		params = "id="+email;
		
		JsonObject response = ma.memberGetResponse(api, al, params, "*");
		if(response ==null)
			fail("Error/Null Response from API call");
		
		responseString = response.toString();
		Reporter.log("RESPONSE BODY: " +responseString);
		Reporter.log("-- X --");
		Reporter.log("");	
	}    
}
