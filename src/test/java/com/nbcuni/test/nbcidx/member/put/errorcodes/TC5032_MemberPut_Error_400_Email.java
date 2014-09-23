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
 * NBCIDX Member.Put API - TC5032_MemberPut_Error_400_Email. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC5032_MemberPut_Error_400_Email {
	
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
	public void memberPut_error400_email() throws Exception {
		
		// Create Random Member
		String UUID=TC01_Create_RandomMemberInfo.sfinalUUID;

		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();	
		
		Reporter.log("1) Validating member.put 400 error code for 'email' field with uppercase letter");
		//Generate the JSON Body for POST 
		String jsonBody ="{'Email':'ToTest@gmail.com'}";
						
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
		

		Reporter.log("2) Validating member.put 400 error code for 'email' field for invalid JSON with ;");
		//Generate the JSON Body for POST 
		jsonBody = "";
		jsonBody ="{'email':'ToTest@gmail.com';}";
						
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

		
		Reporter.log("3) Validating member.put 400 error code for 'email' field for invalid JSON with ;");
		//Generate the JSON Body for POST 
		jsonBody ="{'email';'ToTest@gmail.com'}";
						
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
		

		Reporter.log("4) Validating member.put 400 error code for 'email' field with invalid case for boolan values");
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"', 'email': [{'verified': False,'primary': True,'address': 'toTest@gmail.com'}]}";
		
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
		
		Reporter.log("5) Validating member.put 400 error code for 'email' field with uppercase for 'Verified' and 'Primary'");
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"', 'email': [{'Verified': false,'Primary': true,'address': 'toTest@gmail.com'}]}";
		
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
		

		Reporter.log("6) Validating member.put 400 error code for 'email' field with uppercase for 'Address'");
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"', 'email': [{'verified': false,'primary': true,'Address': 'toTest@gmail.com'}]}";
		
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

		
		Reporter.log("7) Validating member.put 400 error code for 'email' field with two primary emails");
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"', 'email': [{'verified': false,'primary': true,'address': 'toTest_1@gmail.com'},{'verified': true,'primary': true,'address': 'toTest_2@gmail.com'}]}";
		
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
