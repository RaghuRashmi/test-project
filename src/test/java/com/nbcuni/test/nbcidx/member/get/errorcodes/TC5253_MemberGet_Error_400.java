package com.nbcuni.test.nbcidx.member.get.errorcodes;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.nbcidx.MemberAPIs;

import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/**************************************************************************
 * NBCIDX Member.Get API - TC02_MemberGet_Error_400. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC5253_MemberGet_Error_400 {
	
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
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
			ma = new MemberAPIs();
			} catch (Exception e) {
				fail(e.toString());
				}
	}
	
	/**
     * Instantiate the TestNG Test Method.
     */
	@Test(groups = {"full"})
	public void memberGet_error400() throws Exception {
				
		String name=TC01_FindMemberFromDB.finalUsername;
		String brand=TC01_FindMemberFromDB.finalBrands;
		
		Reporter.log("1) Validating member.get ERROR 400 with id=<valid username> and BRAND_ID=*");
		String params = "id="+name;
				
		int responseCode = ma.memberGetResponseCode(api, al, params, "*");
		if(responseCode == 400)
		{
			Reporter.log("Passed : Response code from member.get : "+responseCode);
			Reporter.log(" ");
		}
		else
			fail("Failed : HTTP code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log("");
		
		Reporter.log("2) Validating member.get API ERROR 400 with wrong parameter 'i' and BRAND_ID=<valid brand>");
		params = "";
		params = "i="+name;

		responseCode = ma.memberGetResponseCode(api, al, params, brand);
		if(responseCode == 400)
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
