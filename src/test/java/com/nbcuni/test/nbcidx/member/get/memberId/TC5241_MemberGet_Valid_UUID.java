package com.nbcuni.test.nbcidx.member.get.memberId;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;

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
 * NBCIDX Member.Get API - TC5241_MemberGet_Valid_UUID. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC5241_MemberGet_Valid_UUID {

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
	public void memberGet_uuid() throws Exception {
		
		String params, responseString;
				
		String uuid=TC01_FindMemberFromDB.finalUUID;
		
		Reporter.log("1) Validating member.get API Response with id=<valid UUID>");
		params = "id="+uuid;
		
		JsonObject response = ma.memberGetResponse(api, al, params, "*");
		if(response ==null)
			fail("Error/Null Response from API call");
		
		responseString = response.toString();
		Reporter.log("RESPONSE BODY: " +responseString);
		Reporter.log("-- X --");
		Reporter.log("");		
	}    
}
