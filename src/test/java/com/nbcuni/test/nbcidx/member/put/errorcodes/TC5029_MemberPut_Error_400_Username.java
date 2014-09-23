package com.nbcuni.test.nbcidx.member.put.errorcodes;

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
 * NBCIDX Member.Put API - TC5029_MemberPut_Error_400_Username. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC5029_MemberPut_Error_400_Username {
	
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
	public void memberPut_error400_username() throws Exception {
		
		// Create Random Member
		String UUID=TC01_Create_RandomMemberInfo.sfinalUUID;
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		Reporter.log("1) Validating member.put 400 error code for invalid '_username' field");
		//Generate the JSON Body for POST 
		String jsonBody ="{'_username':'ToTest'}";

		int responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 400)
		{
			Reporter.log("Passed : Response code from member.put : "+responseCode);
			Reporter.log(" ");
		}
		else
			fail("Failed : HTTP code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log(" ");	
		
		Reporter.log("2) Validating member.put 400 error code for 'username' field with boolean value");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','username':false}";
						
		responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 400)
		{
			Reporter.log("Passed : Response code from member.put : "+responseCode);
			Reporter.log(" ");
		}
		else
			fail("Failed : HTTP code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log(" ");
		
		Reporter.log("3) Validating member.put 400 error code for invalid JSON with ,");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','username':'String',}";
						
		responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 400)
		{
			Reporter.log("Passed : Response code from member.put : "+responseCode);
			Reporter.log(" ");
		}
		else
			fail("Failed : HTTP code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log(" ");	
		

		Reporter.log("4) Validating member.put 400 error code for 'username' field with uppercase");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','USERNAME':'String'}";
						
		responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 400)
		{
			Reporter.log("Passed : Response code from member.put : "+responseCode);
			Reporter.log(" ");
		}
		else
			fail("Failed : HTTP code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log(" ");		
	}
}
