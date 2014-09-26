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
import com.nbcuni.test.nbcidx.MemberAPIs;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/*****************************************************************************
 * NBCIDX Sanity Test Cases - SC02_TC01_MemberPut_API_Verification. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Feb 7, 2014
 *****************************************************************************/

public class TC5314_SC02_TS01_MemberPut_API_Verification {
	
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private Proxy proxy=null;

	/**
	 * Instantiate the TestNG Before Class Method.
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
			ma = new MemberAPIs();
			proxy = al.getHttpProxy();
			api = new API();
			api.setProxy(proxy);
			} catch (Exception e) {
				fail(e.toString());
				}
	}
	
	/**
     * Instantiate the TestNG Test Method.
     * @throws Exception - error
     */
	@Test(groups = {"sanity", "API"})
	public void memberPut_verifyAPI() throws Exception {

		// Get the details of Random Member 
		String name=TC5313_SC01_Create_RandomMemberInfo.randomMemberName;
		String pw=TC5313_SC01_Create_RandomMemberInfo.randomPassword;
		String email=TC5313_SC01_Create_RandomMemberInfo.randomEmail;
						
		// Generate the JSON Body for POST 
		String jsonBody ="{\"username\":\""+name+"\",\"password\":\""+pw+"\",\"email\":\""+email+"\"}";
	
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		//Send member.put POST Request 
		JsonObject response = ma.memberPUTResponse(api, al, jsonBody, surfBrandId);
		
		JsonElement membername = response.get("username");
		Reporter.log(" ");
		Reporter.log("membername fetched from API Response ="+membername.toString());
		
		JsonElement email_address = response.getAsJsonArray("email").get(0).getAsJsonObject().get("address");
		Reporter.log("email -> address fetched from API Response =" + email_address.toString());
		Reporter.log(" ");
		
		boolean bMembername = membername.toString().contains(name);
		Assert.assertEquals(bMembername, true);
		
		boolean bEmail = email_address.toString().contains(email);
		Assert.assertEquals(bEmail, true);
		
		Reporter.log("Passed : API Response contains Member = " +name+ " with Email-Id = "+email);
		Reporter.log(" ");						
	}
}
