package com.nbcuni.test.nbcidx.member.link.errorcodes;

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
 * NBCIDX Member.Link API - TC4995_MemberLink_Error_403. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Sep 10, 2014
 **************************************************************************/

public class TC4995_MemberLink_Error_403 {

	private CustomWebDriver cs;
	private AppLib al;
	private API api;
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
	public void memberLink_error403() throws Exception {

		String uuid=TC4993_MemberLink_Error_400.uuid; 
		
		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		Reporter.log("");
		Reporter.log("1) Member.link with unauthorized API_KEY : ");
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.LINK ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the API call for member.link.
		String apicall = al.getApiURL()+"/member/link?API_KEY=65432165432165432165432165432165&BRAND_ID=5876e8579c2f422e99b56d8d0567d347";

		String myJsonBody ="id="+uuid+"&provider=twitter&provider_id=1002470365";
		//Generate the JSON Body in proper format.
		Reporter.log("POST Body= " +myJsonBody);
				
		//Send member.put POST Request. 
		int myResponseCode = al.postHTTPReponseCode(apicall, myJsonBody, myContentType);
		if(myResponseCode == 403)
			Reporter.log("Passed : Member.link Response code ="+myResponseCode);
		else
			fail("Member.link Response code ="+myResponseCode);
		Reporter.log("-- X --");
		Reporter.log("");	
	}
}
