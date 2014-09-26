package com.nbcuni.test.nbcidx.member.unlink.social;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;
import java.net.URLEncoder;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.nbcidx.MemberAPIs;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/**************************************************************************
 * NBCIDX Member.Unlink API - TC5331_MemberUnlink_LinkedIn. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Sep 10, 2014
 **************************************************************************/

public class TC5331_MemberUnlink_LinkedIn {
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
	public void memberUnlink_LinkedIn() throws Exception {

		String uuid=TC5328_MemberUnLink_Facebook.uuid;
		String email = TC5328_MemberUnLink_Facebook.email;
		String LinkedinUID=URLEncoder.encode(al.getLinkedInUID(), "ISO-8859-1").toString();
		String LinkedinProviderUID=URLEncoder.encode(al.getLinkedInProviderUID(), "ISO-8859-1").toString();
		
		Reporter.log("1) Linking with Gigya");
		// Generate Post body to link with Gigya.
		String postBody ="id="+email+"&provider=gigya&provider_id="+LinkedinUID;

		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		int count = 1; int mylink = 0;
		do{
			mylink = ma.memberLINKResponseCode(api, al, postBody, surfBrandId);
			count++;
        	Thread.sleep(4000);
		}while((count<5) || (mylink == 200));
		Reporter.log("-- X --");
		Reporter.log("");
		
		if(mylink!=200)
			Reporter.log("Linking with Gigya is Not successful");
		Reporter.log("-- X --");
		Reporter.log("");
		
		Reporter.log("2) Linking with Linkedin");
		// Generate Post body to link with Linkedin.
		postBody="";
		postBody ="id="+email+"&provider=linkedin&provider_id="+LinkedinProviderUID;
		
		count = 1;
		mylink = 0;
        do{ 
			mylink = ma.memberLINKResponseCode(api, al, postBody, surfBrandId);
			count++;
        	Thread.sleep(4000);
		}while((count<5) || (mylink == 200));
		Reporter.log("-- X --");
		Reporter.log("");
        
        if(count>4 && mylink!=200)
        	Reporter.log("Linking with LinkedIn is Not successful");
        
		Reporter.log("3) Unlinking with Linkedin");	
        // Generate Post body to Unlink with Linkedin.
        postBody="";
    	postBody ="id="+email+"&provider=linkedin&provider_id="+LinkedinProviderUID;

		mylink = ma.memberUNLINKResponseCode(api, al, postBody, surfBrandId);
		if (mylink==200)
			Reporter.log("Unlinking with Linkedin is successful");
		else
			fail("Unlinking with Linkedin is Not successful");			
	}
}
