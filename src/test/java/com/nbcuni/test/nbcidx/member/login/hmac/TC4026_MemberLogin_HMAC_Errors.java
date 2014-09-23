package com.nbcuni.test.nbcidx.member.login.hmac;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;
import java.util.Date;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.mongodb.DB;
import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.nbcidx.MemberAPIs;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/*******************************************************************************
 * NBCIDX Member.Login API - TC4026_MemberLogin_HMAC_Errors. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 *******************************************************************************/
 
public class TC4026_MemberLogin_HMAC_Errors {

	private CustomWebDriver cs;
	private static AppLib al;
	private static API api;
	private static MemberAPIs ma;
	private static DB mydb;
	private Proxy proxy=null;
	
	private static String stimeStamp;
	private static String signatureHMAC;
	
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
			mydb = al.getMongoDbConnection();
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
	public static void memberLogin_HMAC_errors() throws Exception{
		
		String brandId = TC4025_MemberLogin_HMAC_Signature.brandID;
		String siteId = TC4025_MemberLogin_HMAC_Signature.siteID;
		String secretKey = TC4025_MemberLogin_HMAC_Signature.secretKey;
		String provider = TC4025_MemberLogin_HMAC_Signature.provider;
		String providerId = TC4025_MemberLogin_HMAC_Signature.providerID;
		String uuid=TC4025_MemberLogin_HMAC_Signature.uuid; 
		String name=TC4025_MemberLogin_HMAC_Signature.randomMemberUserName;
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		Reporter.log("1) Genrating HMAC with 6 mins Older TimeStamp");
		Reporter.log(" ");
		
		Date now = new Date();
		long afterSubstractingMins = new Date(now.getTime() - (6 * 60000)).getTime();
	    long timeStamp =afterSubstractingMins/1000;
	    stimeStamp = Long.toString(timeStamp);
		  	
		String data = surfBrandId+","+siteId+","+provider+","+providerId+","+stimeStamp;
		signatureHMAC = al.calculateHMAC(data, secretKey);
	   			
		Reporter.log("Generating HMAC with below parameters =>");
		Reporter.log("brandId : " + brandId);
		Reporter.log("siteId : " + siteId);
		Reporter.log("secretKey : " + secretKey);
		Reporter.log("provider : " + provider);
		Reporter.log("providerId : " + providerId);
		Reporter.log("timeStamp : " + stimeStamp);
		Reporter.log("");
		Reporter.log("Genrated HMAC = "+signatureHMAC);
		Reporter.log("-- X --");
		Reporter.log("");
		
		Reporter.log("2) member.login with 6 mins Older timestamp & corresponding HMAC.");
		String myJsonBody ="id="+providerId+"&provider="+provider+"&site_id="+siteId+"&timestamp="+stimeStamp+"&signature="+signatureHMAC;
		
		int mylogin = ma.memberLOGINResponseCode(api, al, myJsonBody, surfBrandId);		
		if(mylogin == 400)
			Reporter.log("Member.login Response code ="+mylogin);
		else
			fail("Member.login Response code ="+mylogin);
		Reporter.log("-- X --");
		Reporter.log("");
		
		Reporter.log("3) Genrating HMAC with 6 mins Newer TimeStamp");
		Reporter.log(" ");		
		now = new Date();
		afterSubstractingMins = new Date(now.getTime() + (6 * 60000)).getTime();
	    timeStamp =afterSubstractingMins/1000;
	    stimeStamp = Long.toString(timeStamp);
		  						
		data = surfBrandId+","+siteId+","+provider+","+providerId+","+stimeStamp;
		signatureHMAC = al.calculateHMAC(data, secretKey);
			
		Reporter.log("Generating HMAC with below parameters =>");
		Reporter.log("brandId : " + brandId);
		Reporter.log("siteId : " + siteId);
		Reporter.log("secretKey : " + secretKey);
		Reporter.log("provider : " + provider);
		Reporter.log("providerId : " + providerId);
		Reporter.log("timeStamp : " + stimeStamp);
		Reporter.log("");
		Reporter.log("Genrated HMAC = "+signatureHMAC);
		Reporter.log("-- X --");
		Reporter.log("");
		
		Reporter.log("4) member.login with 6 mins Newer timestamp & corresponding HMAC.");
		myJsonBody ="id="+providerId+"&provider="+provider+"&site_id="+siteId+"&timestamp="+stimeStamp+"&signature="+signatureHMAC;
		
		mylogin = ma.memberLOGINResponseCode(api, al, myJsonBody, surfBrandId);
		if(mylogin == 400)
			Reporter.log("Member.login Response code ="+mylogin);
		else
			fail("Member.login Response code ="+mylogin);
		Reporter.log("-- X --");
		Reporter.log("");
		
		Reporter.log("5) member.remove to remove member from IDX.");
		myJsonBody= "id="+uuid;
		int removeReturnCode = ma.memberREMOVEResponseCode(api, al, myJsonBody, surfBrandId, name, uuid, mydb);
		if(removeReturnCode != 204)
			fail("Failed : HTTP error code : "+ removeReturnCode);
		Reporter.log("-- X --");
		Reporter.log("");
	}
}


