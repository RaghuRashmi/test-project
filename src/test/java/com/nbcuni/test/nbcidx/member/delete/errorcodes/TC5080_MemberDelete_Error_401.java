package com.nbcuni.test.nbcidx.member.delete.errorcodes;

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
 * NBCIDX Member.Delete API - TC5080_MemberDelete_Error_401. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Sep 11, 2014
 **************************************************************************/

public class TC5080_MemberDelete_Error_401 {
	
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
	public void memberDelete_error401() throws Exception {

		String name=TC5079_MemberDelete_Error_400.randomMemberName;
		String uuid=TC5079_MemberDelete_Error_400.uuid; 
		
		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		//Generate the API call for member.put.
		String apicall = al.getApiURL()+"/member/delete?API_KEY=6543216543216543216543216543216&BRAND_ID="+al.getSurfBrandId();

		Reporter.log("");
		Reporter.log("1) Member.delete with invalid API_KEY : ");
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.DELETE ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");

		String myJsonBody ="id="+uuid;
		//Generate the JSON Body in proper format.
		Reporter.log("POST Body= " +myJsonBody);
				
		//Send member.put POST Request. 
		int myResponseCode = al.postHTTPReponseCode(apicall, myJsonBody, myContentType);
		if(myResponseCode != 401)
			fail("Member.delete Response code ="+myResponseCode);
		Reporter.log("--X--");		
		
		//Generate the API call for member.put.
		apicall = al.getApiURL()+"/member/delete?API_KEY=&BRAND_ID="+al.getSurfBrandId();

		Reporter.log("");
		Reporter.log("2) Member.delete with blank API_KEY : ");
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.DELETE ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");

		myJsonBody ="id="+uuid;
		//Generate the JSON Body in proper format.
		Reporter.log("POST Body= " +myJsonBody);
				
		//Send member.put POST Request. 
		myResponseCode = al.postHTTPReponseCode(apicall, myJsonBody, myContentType);
		if(myResponseCode != 401)
			fail("Member.delete Response code ="+myResponseCode);
		Reporter.log("--X--");
				
		//Generate the API call for member.put.
		apicall = al.getApiURL()+"/member/delete?API_KEY=6543216543216543216543216543216#&BRAND_ID="+al.getSurfBrandId();

		Reporter.log("");
		Reporter.log("3) Member.delete with invalid API_KEY : ");
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.DELETE ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");

		//Generate the JSON Body in proper format.
		myJsonBody ="id="+uuid;
		Reporter.log("POST Body= " +myJsonBody);
				
		//Send member.delete POST Request. 
		myResponseCode = al.postHTTPReponseCode(apicall, myJsonBody, myContentType);
		if(myResponseCode != 401)
			fail("Member.delete Response code ="+myResponseCode);
		Reporter.log("-- X --");
		Reporter.log("");		
	}	
}
