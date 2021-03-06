package com.nbcuni.test.nbcidx.member.login.errorcodes;

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
 * NBCIDX Member.Login API - TC3370_MemberLogin_Error_401. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: June 16, 2014
 **************************************************************************/

public class TC3370_MemberLogin_Error_401 {
	
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
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
			ma = new MemberAPIs();
			} catch (Exception e) {
				fail(e.toString());
				}
	}

	/**
     * Instantiate the TestNG Test Method.
     * 
     * @throws Exception - error
     */
	@Test(groups = {"full"})
	public void memberLogin_error401() throws Exception {

		String name=TC3369_MemberLogin_Error_400.randomMemberName;
		String pw=TC3369_MemberLogin_Error_400.randomPassword;

		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		//Generate the API call for member.put.
		String apicall = al.getApiURL()+"/member/login?API_KEY=6543216543216543216543216543216#&BRAND_ID="+al.getSurfBrandId();

		Reporter.log("");
		Reporter.log("1) Member.login with invalid API_KEY : ");
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.LOGIN ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");

		String myJsonBody ="id="+name+"&password="+pw;
		//Generate the JSON Body in proper format.
		Reporter.log("POST Body= " +myJsonBody);
				
		//Send member.put POST Request. 
		int myResponseCode = al.postHTTPReponseCode(apicall, myJsonBody, myContentType);
		if(myResponseCode == 401)
			Reporter.log("Member.login Response code ="+myResponseCode);
		else
			fail("Member.login Response code ="+myResponseCode);		
		Reporter.log("-- X --");
		Reporter.log("");	
	}	
}
