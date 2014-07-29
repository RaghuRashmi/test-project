package com.nbcuni.test.nbcidx.sanity;

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

/****************************************************************************
 * NBCIDX Sanity Test Cases - SC03_TC01_MemberGet_API_Verification. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Feb 7, 2014
 ****************************************************************************/

public class SC03_TC01_MemberGet_API_Verification {
	
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
	public void memberGet_verifyAPI() throws Exception {
		
		Reporter.log(" ");
		Reporter.log("/************************************************************************ MEMBER.GET ************************************************************************/");
			
		
		// Get the API Response 
		String apicall, responseString;
				
		String name=SC01_Create_RandomMemberInfo.randomMemberName;
		String email=SC01_Create_RandomMemberInfo.randomEmail;
		
		apicall = al.getApiURL()+"/member/get?API_KEY="+al.getDefaultApiKey()+"&BRAND_ID="+al.getSurfBrandId()+"&id="+name;
		
		int responseCode = api.getHTTPResponseCode(apicall, api.REQUEST_GET, api.REQUEST_CONTENTTYPE_JSON);
		JsonObject response = api.getRootObjectsFromResponseBody(api.getRestRequest(apicall, api.REQUEST_CONTENTTYPE_JSON));
		
		Reporter.log(" ");	
		if(responseCode == 200)
		{
			Reporter.log("Passed : Response code from member.get : "+responseCode);
			Reporter.log(" ");
		}
		else
			fail("Failed : HTTP error code : "+ responseCode);
	
				
		Reporter.log("Validating member.get API Response");
				
		responseString = response.toString();
		Reporter.log(" ");
		Reporter.log("API Call Response : " + responseString);
		Reporter.log(" ");
		
		JsonElement membername = response.get("username");
		//Reporter.log("membername="+membername.toString());
		
		JsonElement email_address = response.getAsJsonArray("email").get(0).getAsJsonObject().get("address");
		//Reporter.log("email -> address=" + email_address.toString());
		
		boolean bMembername = membername.toString().contains(name);
		Assert.assertEquals(bMembername, true);
		
		boolean bEmail = email_address.toString().contains(email);
		Assert.assertEquals(bEmail, true);
		
		Reporter.log("Passed : member.get API Response contains Member = " +name+ " with Email-Id = "+email);
		Reporter.log(" ");
		
	}
	
}
