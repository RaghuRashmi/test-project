package com.nbcuni.test.nbcidx.member.link.social;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;
import java.net.URLEncoder;

import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.mongodb.DB;
import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.nbcidx.MemberAPIs;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/**************************************************************************
 * NBCIDX Member.Link API - TC4992_MemberLink_GooglePlus. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Sep 10, 2014
 **************************************************************************/

public class TC4992_MemberLink_GooglePlus {
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private DB mydb;
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
			mydb = al.getMongoDbConnection();
			api = new API();
			api.setProxy(proxy);
			ma = new MemberAPIs();
			} catch (Exception e) {
				fail(e.toString());
				}
	}
	
	/**
     * Instantiate the TestNG After Class Method.
     * 
     * @throws Exception - error
     */
    @AfterClass(alwaysRun = true)
    public void stopEnvironment() {
    	try {
        al.closeMongoDbConnection();
    	} catch (Exception e) {
			fail(e.toString());
			}
    }	

	@Test(groups = {"full"})
	public void memberLink_GooglePlus() throws Exception {

		String uuid  = TC4988_MemberLink_Facebook.uuid; 
		String email = TC4988_MemberLink_Facebook.email;
		String googlePlusUID = URLEncoder.encode(al.getGooglePlusUID(), "ISO-8859-1").toString();
		String googlePlusProviderUID =URLEncoder.encode(al.getGooglePlusProviderUID(), "ISO-8859-1").toString();
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		Reporter.log("1) Linking with Gigya");
		// Generate Post body to link with Gigya.
		String postBody ="id="+email+"&provider=gigya&provider_id="+googlePlusUID;
		
		int count = 1; int mylink = 0;
		do{
			mylink = ma.memberLINKResponseCode(api, al, postBody, surfBrandId);
			count++;
        	Thread.sleep(4000);
		}while((count<5) || (mylink == 200));
		Reporter.log("-- X --");
		Reporter.log("");
		
		if(mylink!=200)
			fail("Linking with Gigya is Not successful");
					
		Reporter.log("2) Linking with GooglePlus");
		// Generate Post body to link with GooglePlus.
		postBody="";
		postBody ="id="+email+"&provider=googleplus&provider_id="+googlePlusProviderUID;

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
        	fail("Linking with GooglePlus is Not successful");
        
        Reporter.log("3) Unlinking with GooglePlus");	
        // Generate Post body to Unlink with GooglePlus.
		postBody ="id="+email+"&provider=googleplus&provider_id="+googlePlusProviderUID;

		mylink = ma.memberUNLINKResponseCode(api, al, postBody, surfBrandId);
		if (mylink==200)
			Reporter.log("Unlinking with GooglePlus is successful");
		else
			Reporter.log("Unlinking with GooglePlus is Not successful");
		Reporter.log("-- X --");
		Reporter.log("");		
	}
}
