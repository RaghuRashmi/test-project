package com.nbcuni.test.nbcidx.member.link_noverify.validData;

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

/********************************************************************************
 * NBCIDX Member.Link_noverify API - TC5341_MemberLinkNoVerify_uuid. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Sep 10, 2014
 *******************************************************************************/

public class TC5341_MemberLinkNoVerify_uuid {
	
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private Proxy proxy=null;
	
	public static String username = "";
	public static String pw = "";
	public static String email = "";
	public static String uuid;
	public static String UIDSignature="";
	public static String signatureTimestamp;
	
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
	public void memberLinkNoVerify_uuid() throws Exception {

		String uuid = TC5339_MemberLinkNoVerify_username.uuid;
					
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		// 1) member.link_noverify
		String timeStamp = al.getCurrentTimestamp();
		String providerID = "test_provider"+timeStamp;
		
		Reporter.log("");
		Reporter.log("1) Calling member.link_noverify with uuid, provider = testProvider and providerID = "+providerID);
		Reporter.log("");
		
		// Generate Post body to link with uuid.
		String postBody ="id="+uuid+"&provider=testProvider&provider_id="+providerID;
		int mylink = ma.memberLINKNoVerifyResponseCode(api, al, postBody, surfBrandId);	
		if (mylink ==200)
			Reporter.log("Linking uuid with link_noverify is successful");
		else
        	fail("Linking is Not successful");
		Reporter.log("-- X --");
		Reporter.log("");
	}
}
