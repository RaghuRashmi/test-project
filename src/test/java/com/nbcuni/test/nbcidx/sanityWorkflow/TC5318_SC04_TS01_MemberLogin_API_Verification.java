package com.nbcuni.test.nbcidx.sanityWorkflow;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;


import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/*******************************************************************************
 * NBCIDX Sanity Test Cases - SC04_TC01_MemberLogin_API_Verification. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Feb 7, 2014
 *******************************************************************************/

public class TC5318_SC04_TS01_MemberLogin_API_Verification {
	
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
	@Test(groups = {"sanity", "API"})
	public void memberLogin_verifyAPI() throws Exception {
	
		Reporter.log(" ");
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.LOGIN ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log(" ");				
		
		// Generate the API Call 
		String apicall, responseString;
		apicall = al.getApiURL()+"/member/login?API_KEY="+al.getDefaultApiKey()+"&BRAND_ID="+al.getSurfBrandId();
		
		String name=TC5313_SC01_Create_RandomMemberInfo.randomMemberName;
		String pw=TC5313_SC01_Create_RandomMemberInfo.randomPassword;
				
		String myJsonBody ="id="+name+"&password="+pw;
		Reporter.log("JSON Body for member.login : "+ myJsonBody);
		
		// Generate the Content Type for POST 
		String myContentType = "application/x-www-form-urlencoded";
		
		JsonObject response=null;
		
		// Send member.login POST Request 
		response = api.getRootObjectsFromResponseBody(api.postRestRequest(apicall, myJsonBody, myContentType));
		responseString = response.toString();
		Reporter.log(" ");
		Reporter.log("API Call Response : " + responseString);
		Reporter.log(" ");
		
		JsonElement login_status = response.get("_logged_in");
		Reporter.log("Latest _logged_in status fetched from API Response ="+login_status.toString());
		Reporter.log(" ");
		
		boolean blogin = login_status.toString().contains("true");
		Assert.assertEquals(blogin, true);
		
		Reporter.log("Passed : _logged_in status in API Response for Member "+ name + " is "+login_status.toString());
		Reporter.log(" ");			
	}	
}
