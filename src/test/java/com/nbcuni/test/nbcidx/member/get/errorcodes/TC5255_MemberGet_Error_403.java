package com.nbcuni.test.nbcidx.member.get.errorcodes;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.nbcuni.test.nbcidx.AppLib;

import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/**************************************************************************
 * NBCIDX Member.Get API - TC5255_MemberGet_Error_403. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC5255_MemberGet_Error_403 {
	
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private Proxy proxy=null;
	

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
			} catch (Exception e) {
				fail(e.toString());
				}
	}
	
	/**
     * Instantiate the TestNG Test Method.
     */
	@Test(groups = {"full"})
	public void memberGet_error403() throws Exception {
		
		String apicall;
				
		String name=TC01_FindMemberFromDB.finalUsername;
				
		Reporter.log("1) Validating member.get API Response with id=<valid username>, BRAND_ID=<valid brand> and API_KEY=<valid but incompatible API key>");
		
		//Generate member.get API call with invalid API_KEY
		apicall = al.getApiURL()+"/member/get?API_KEY=65432165432165432165432165432165"+"&BRAND_ID=5876e8579c2f422e99b56d8d0567d347&id="+name;
		
		//Send the member.get GET request
		int responseCode = al.getHTTPResponseCode(apicall);
		if(responseCode == 403)
		{
			Reporter.log("Passed : Response code from member.get : "+responseCode);
			Reporter.log(" ");
		}
		else
			fail("Failed : HTTP code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log("");
	}
}
