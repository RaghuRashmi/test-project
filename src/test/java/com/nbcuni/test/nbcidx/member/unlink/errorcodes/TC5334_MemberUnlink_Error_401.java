package com.nbcuni.test.nbcidx.member.unlink.errorcodes;

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
 * NBCIDX Member.Unlink API - TC5334_MemberUnlink_Error_401. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Sep 10, 2014
 **************************************************************************/

public class TC5334_MemberUnlink_Error_401 {
	
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
	public void memberUnlink_error401() throws Exception {

		String uuid=TC5333_MemberUnlink_Error_400.uuid;

		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		//Generate the API call for member.unlink.
		String apicall = al.getApiURL()+"/member/unlink?API_KEY=6543216543216543216543216543216#&BRAND_ID="+al.getSurfBrandId();

		Reporter.log("");
		Reporter.log("1) Member.unlink with invalid API_KEY : ");
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.UNLINK ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");

		//Generate the JSON Body.
		String myJsonBody ="id="+uuid+"&provider=twitter&provider_id=1002470365";
		Reporter.log("POST Body= " +myJsonBody);
				
		//Send member.unlink POST Request. 
		int myResponseCode = al.postHTTPReponseCode(apicall, myJsonBody, myContentType);
		if(myResponseCode == 401)
			Reporter.log("Passed : Member.unlink Response code ="+myResponseCode);
		else
			fail("Member.unlink Response code ="+myResponseCode);
		Reporter.log("-- X --");
		Reporter.log("");			
	}
	
}
