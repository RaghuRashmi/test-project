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
 * NBCIDX Sanity Test Cases - SC05_TC01_MemberLogout_API_Verification. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Feb 7, 2014
 *******************************************************************************/

public class SC05_TC01_MemberLogout_API_Verification {
	
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
	public void memberLogout_verifyAPI() throws Exception {
	
		Reporter.log(" ");
		Reporter.log("/************************************************************************ MEMBER.LOGOUT ************************************************************************/");
		Reporter.log(" ");
						
		// Generate the API Call 
		String apicall, responseString;
		apicall = al.getApiURL()+"/member/logout?API_KEY="+al.getDefaultApiKey()+"&BRAND_ID="+al.getSurfBrandId();
		
		String name=SC01_Create_RandomMemberInfo.randomMemberName;
		
		String myJsonBody ="id="+name;
		System.out.println("JSON Body for member.logout : "+ myJsonBody);
		Reporter.log("JSON Body for member.logout : "+ myJsonBody);
		
		// Generate the Content Type for POST 
		String myContentType = "application/x-www-form-urlencoded";
				
		// Send member.logout POST Request 
		JsonObject response=null;
		response = api.getRootObjectsFromResponseBody(api.postRestRequest(apicall, myJsonBody, myContentType));
		responseString = response.toString();
		Reporter.log(" ");
		Reporter.log("API Call Response : " + responseString);
		Reporter.log(" ");
		
		JsonElement login_status = response.get("_logged_in");
		Reporter.log("login_status fetched from API Response ="+login_status.toString());
		Reporter.log(" ");
		
		boolean blogin = login_status.toString().contains("false");
		Assert.assertEquals(blogin, true);
		
		Reporter.log("Passed : _logged_in status in API Response for Member "+ name + " is "+login_status.toString());
		Reporter.log(" ");
	}
}
