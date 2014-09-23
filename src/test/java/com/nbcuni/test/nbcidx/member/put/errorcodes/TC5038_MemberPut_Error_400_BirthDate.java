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
 * NBCIDX Member.Put API - TC5038_MemberPut_Error_400_BirthDate. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC5038_MemberPut_Error_400_BirthDate {
	
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
	public void memberPut_error400_birthDate() throws Exception {
		
		// Create Random Member
		String UUID=TC01_Create_RandomMemberInfo.sfinalUUID;
		
		Reporter.log("1) Validating member.put 400 error code for 'birthdate' field with string values");
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		//Generate the JSON Body for POST 
		String jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','birthdate':{'month':'4','day':'22','year':'1984'}}";
		
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
		
		
		Reporter.log("2) Validating member.put 400 error code for 'birthdate' field with String value for 'year'");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','birthdate':{'month':4,'day':22,'year':'1984'}}";
							
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
		
		
		Reporter.log("3) Validating member.put 400 error code for 'birthdate' field with invalid object structure");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','birthdate':[{'month':4,'day':22,'year':1984}]}";
							
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
		
		
		Reporter.log("4) Validating member.put 400 error code for 'birthdate' field with invalid object structure");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','birthdate':{'month':4,'date':22,'year':1984}}";
							
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
		
		
		Reporter.log("5) Validating member.put 400 error code for 'birthdate' field with 2 digit value for 'year'");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','birthdate':{'month':4,'day':22,'year':74}}";
							
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
		
		
		Reporter.log("6) Validating member.put 400 error code for 'birthdate' field with 'year' value < 1900");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','birthdate':{'month':4,'day':22,'year':1874}}";
							
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
		
		
		Reporter.log("7) Validating member.put 400 error code for 'birthdate' field with 'month' value > 12");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','birthdate':{'month':13,'day':3,'year':1974}}";
							
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
		
		
		Reporter.log("8) Validating member.put 400 error code for 'birthdate' field with invalid calendar date");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','birthdate':{'month':2,'day':30,'year':1974}}";
							
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
		
		
		Reporter.log("9) Validating member.put 400 error code for 'birthdate' field with 'day' value > 31");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','birthdate':{'month':5,'day':32,'year':2013}}";
							
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
		
		
		Reporter.log("10) Validating member.put 400 error code for 'birthdate' field with invalid calendar date");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','birthdate':{'month':2,'day':29,'year':1979}}";
							
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
		
		
		Reporter.log("11) Validating member.put 400 error code for 'birthdate' field with uppercase letter");
		//Generate the JSON Body for POST 
		jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','Birthdate':{'month':4,'day':28,'year':2004}}";
							
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
