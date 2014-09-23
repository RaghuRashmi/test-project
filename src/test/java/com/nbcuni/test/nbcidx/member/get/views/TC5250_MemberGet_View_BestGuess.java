package com.nbcuni.test.nbcidx.member.get.views;

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
 * NBCIDX Member.Get API - TC5250_MemberGet_View_BestGuess. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC5250_MemberGet_View_BestGuess {
	
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private Proxy proxy=null;
	private static MemberAPIs ma;
	
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
	public void memberGet_view_bestGuess() throws Exception {
		
		String params, responseString;
				
		String name=TC01_Create_ComplexMember_With_AttributeData.randomMemberUserName;
		String uuid = TC01_Create_ComplexMember_With_AttributeData.sfinalUUID;
		
		Reporter.log("1) Validating member.get API Response with view=best-guess and id=username");
		params = "view=best-guess&id="+name;

		JsonObject response = ma.memberGetResponse(api, al, params, al.getSurfBrandId());	
		if(response ==null)
			fail("Error/Null Response from API call");		
		responseString = response.toString();
		Reporter.log("RESPONSE BODY: " +responseString);
		Reporter.log("-- X --");
		Reporter.log("");
				
		Reporter.log("2) Validating member.get API Response with view=best-guess and id=UUID");
		params = "";
		params = "view=best-guess&id="+uuid;

		response=null;
		response = ma.memberGetResponse(api, al, params, al.getSurfBrandId());	
		if(response ==null)
			fail("Error/Null Response from API call");		
		responseString = "";
		responseString = response.toString();
		Reporter.log("RESPONSE BODY: " +responseString);
		Reporter.log("-- X --");
		Reporter.log("");
	}

}
