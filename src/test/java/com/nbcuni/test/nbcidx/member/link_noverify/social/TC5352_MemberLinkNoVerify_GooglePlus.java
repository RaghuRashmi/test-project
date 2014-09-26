package com.nbcuni.test.nbcidx.member.link_noverify.social;

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

/***********************************************************************************
 * NBCIDX Member.Link_noverify API - TC5352_MemberLinkNoVerify_GooglePlus. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Sep 10, 2014
 ***********************************************************************************/

public class TC5352_MemberLinkNoVerify_GooglePlus {
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private Proxy proxy=null;
	
	public static String gigya_secret_key="";
	public static String gigya_api_key="";
	
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

	@Test(groups = {"full"})
	public void memberLinkNoVerify_GooglePlus() throws Exception {


		String uuid=TC5348_MemberLinkNoVerify_Facebook.uuid; 
		String email = TC5348_MemberLinkNoVerify_Facebook.email;
		String username = TC5348_MemberLinkNoVerify_Facebook.username;
				
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		// 1) member.link_noverify
		String timeStamp = al.getCurrentTimestamp();
		String providerID = "test_provider"+timeStamp;
		
		Reporter.log("");
		Reporter.log("1) Calling member.link_noverify with uuid, provider = googleplus and providerID = "+providerID);
		Reporter.log("");
		
		// Generate Post body to link with googleplus.
		String postBody ="id="+uuid+"&provider=googleplus&provider_id="+providerID;
		int mylink = ma.memberLINKNoVerifyResponseCode(api, al, postBody, surfBrandId);	
		if (mylink ==200)
			Reporter.log("Linking uuid with googleplus is successful");
		else
        	fail("Linking is Not successful");
		Reporter.log("-- X --");
		Reporter.log("");	
		
		Reporter.log("2) Calling member.link_noverify with emailid, provider = googleplus and providerID = "+providerID);
		Reporter.log("");
		
		// Generate Post body to link with googleplus.
		postBody ="id="+email+"&provider=googleplus&provider_id="+providerID;
		mylink = ma.memberLINKNoVerifyResponseCode(api, al, postBody, surfBrandId);	
		if (mylink ==200)
			Reporter.log("Linking emailid with googleplus is successful");
		else
        	fail("Linking is Not successful");
		Reporter.log("-- X --");
		Reporter.log("");	
		
		Reporter.log("3) Calling member.link_noverify with username, provider = googleplus and providerID = "+providerID);
		Reporter.log("");
		
		// Generate Post body to link with googleplus.
		postBody ="id="+username+"&provider=googleplus&provider_id="+providerID;
		mylink = ma.memberLINKNoVerifyResponseCode(api, al, postBody, surfBrandId);	
		if (mylink ==200)
			Reporter.log("Linking username with googleplus is successful");
		else
        	fail("Linking is Not successful");
		Reporter.log("-- X --");
		Reporter.log("");	
	}
}
