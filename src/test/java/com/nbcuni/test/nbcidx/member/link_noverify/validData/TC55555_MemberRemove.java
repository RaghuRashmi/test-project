package com.nbcuni.test.nbcidx.member.link_noverify.validData;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;

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
 * NBCIDX Member.Link_noverify API - TC49992A_MemberRemove. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Sep 10, 2014
 **************************************************************************/

public class TC55555_MemberRemove {
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
	public void memberRemove() throws Exception {

		String username = TC5339_MemberLinkNoVerify_username.username;
		String uuid=TC5339_MemberLinkNoVerify_username.uuid;
	
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		Reporter.log("1) Removing member from IDX");
		//Generate Post Body.
		String postBody = "id="+uuid;

		//Send member.remove POST Request 
		int removeReturnCode = ma.memberREMOVEResponseCode(api, al, postBody, surfBrandId, username, uuid, mydb);
		if(removeReturnCode != 204)
			fail("Failed : HTTP error code : "+ removeReturnCode);
	}
}
