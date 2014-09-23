package com.nbcuni.test.nbcidx.sanityWorkflow;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/*******************************************************************************
 * NBCIDX Sanity Test Cases - SC06_TC01_MemberRemove_API_Verification. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Feb 7, 2014
 *******************************************************************************/

public class SC06_TC01_MemberRemove_API_Verification {

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
	public void memberRemove_verifyAPI() throws Exception {
	
		Reporter.log(" ");
		Reporter.log("/************************************************************************ MEMBER.REMOVE ************************************************************************/");
		Reporter.log(" ");
				
		
		// Generate the API Call 
		String apicall = al.getApiURL()+"/member/remove?API_KEY="+al.getDefaultApiKey()+"&BRAND_ID="+al.getSurfBrandId();
		
		String name=SC01_Create_RandomMemberInfo.randomMemberName;
		
		String myJsonBody ="id="+name;
		Reporter.log("JSON Body for member.remove : "+ myJsonBody);
		
		// Generate the Content Type for POST 
		String myContentType = "application/x-www-form-urlencoded";
		
		// Send member.remove POST Request 
		int	responseCode = al.postHTTPReponseCode(apicall, myJsonBody, myContentType);
		
		Reporter.log("Validating member.remove Response Code");
		Reporter.log(" ");
		if(responseCode == 204)
		{
			Reporter.log("Response code from member.remove : "+responseCode);
			Reporter.log(" ");
			Reporter.log("Passed : member.remove has successfully deleted the Member : "+name);
			Reporter.log(" ");
		}
		else
		{
			Reporter.log("Failed : HTTP error code : "+ responseCode);
			Reporter.log(" ");
			fail("Failed : HTTP error code : "+ responseCode);
		}
	}			
}
