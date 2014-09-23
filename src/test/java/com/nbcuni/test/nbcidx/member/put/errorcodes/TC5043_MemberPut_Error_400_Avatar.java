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
 * NBCIDX Member.Put API - TC5043_MemberPut_Error_400_Avatar. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC5043_MemberPut_Error_400_Avatar {
	
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
	public void memberPut_error400_avatar() throws Exception {
		
		// Create Random Member
		String UUID=TC01_Create_RandomMemberInfo.sfinalUUID;
		
		Reporter.log("1) Validating member.put 400 error code for 'avatar' field with non-relative URL");
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		//Generate the JSON Body for POST 
		String jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','avatar': '../mbsafdar/ej/radioman.jpg'}";
						
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
		
		
		Reporter.log("2) Validating member.put 400 error code for 'avatar' field with non-relative URL");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','avatar':'random.jpg'}";
						
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
		
		
		Reporter.log("3) Validating member.put 400 error code for 'avatar' field with uppercase letter");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','Avatar':'http://mysite.verizon.net/mbsafdar/ej/radioman.jpg'}";
						
		responseCode = ma.memberPUTResponseCode(api, al, jsonBody, surfBrandId);
		if(responseCode == 400)
		{
			Reporter.log("Passed : Response code from member.put : "+responseCode);
			Reporter.log(" ");
		}
		else
			fail("Failed : HTTP code : "+ responseCode);
		Reporter.log("-- X --");
		
	}
}
