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
 * NBCIDX Member.Put API - TC5046_MemberPut_Error_401. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC5046_MemberPut_Error_401 {
	
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
	public void memberPut_error401() throws Exception {
		
		// Create Random Member
		String UUID=TC01_Create_RandomMemberInfo.sfinalUUID;
		
		Reporter.log("1) Validating member.put 401 error code with no API_KEY");
		
		String apicall=null;
		int myResponseCode=0;
		
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.PUT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the Content Type for POST. 
		String myContentType = "application/json";
		
		//Generate the API call for member.put.
		apicall = al.getApiURL()+"/member/put?&BRAND_ID="+al.getSurfBrandId();
		
		//Generate the JSON Body for POST 
		String jsonBody="";
		jsonBody ="{'_id':'"+UUID+"','username': 'test123456789'}";
		
		//Generate the JSON Body in proper format.
		String myJsonBodyAfterFormat=al.convertIntoJsonFormat(jsonBody);
		Reporter.log("JSON Body for POST = " +myJsonBodyAfterFormat);
				
		//Send member.put POST Request. 
		myResponseCode = al.postHTTPReponseCode(apicall, myJsonBodyAfterFormat, myContentType);
		if(myResponseCode == 401)
		{
			Reporter.log("Passed : Response code from member.put : "+myResponseCode);
			Reporter.log(" ");
		}
		else
			fail("Failed : HTTP code : "+ myResponseCode);
		Reporter.log("-- X --");
		Reporter.log(" ");
		
		
		Reporter.log("2) Validating member.put 401 error code with invalid API_KEY");
		apicall = "";
		apicall = al.getApiURL()+"/member/put?API_KEY=65432165432165432123456789012345"+"&BRAND_ID="+al.getSurfBrandId();
						
		//Send member.put POST Request. 
		myResponseCode = al.postHTTPReponseCode(apicall, myJsonBodyAfterFormat, myContentType);
		if(myResponseCode == 401)
		{
			Reporter.log("Passed : Response code from member.put : "+myResponseCode);
			Reporter.log(" ");
		}
		else
			fail("Failed : HTTP code : "+ myResponseCode);
		Reporter.log("-- X --");
		
	}
}
