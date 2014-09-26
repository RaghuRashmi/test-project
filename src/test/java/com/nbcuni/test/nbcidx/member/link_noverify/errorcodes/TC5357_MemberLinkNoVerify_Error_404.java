package com.nbcuni.test.nbcidx.member.link_noverify.errorcodes;

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

/************************************************************************************
 * NBCIDX Member.Link_noverify API - TC5357_MemberLinkNoVerify_Error_404. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Sep 10, 2014
 ************************************************************************************/

public class TC5357_MemberLinkNoVerify_Error_404 {
	
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
	public void memberLinkNoVerify_error404() throws Exception {
		
		Reporter.log("");
		Reporter.log("1) Member.link_noverify with non-existent member");
		String myJsonBody ="id=&provider=twitter&provider_id=1002470365";

		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		int mylink = ma.memberLINKNoVerifyResponseCode(api, al, myJsonBody, surfBrandId);
		if(mylink == 404)
			Reporter.log("Passed : Member.link_noverify Response code ="+mylink);
		else
			fail("Member.link_noverify Response code ="+mylink);
		Reporter.log("-- X --");
		Reporter.log("");
	}
}
