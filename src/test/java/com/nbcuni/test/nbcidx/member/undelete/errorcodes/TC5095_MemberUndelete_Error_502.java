package com.nbcuni.test.nbcidx.member.undelete.errorcodes;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;

import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.mongodb.DB;
import com.mongodb.DBObject;
import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.nbcidx.MemberAPIs;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/**************************************************************************
 * NBCIDX Member.Undelete API - TC5095_MemberUndelete_Error_502. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Sep 12, 2014
 **************************************************************************/

public class TC5095_MemberUndelete_Error_502 {
	
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private DB mydb;
	private Proxy proxy=null;
	
	String UUID;
	DBObject myObj;
	String dbresponse;

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

	/**
     * Instantiate the TestNG Test Method.
     * 
     * @throws Exception - error
     */

	@Test(groups = {"full1"})
	public void memberUndelete_error502() throws Exception {

		String name=TC5091_MemberUndelete_Error_400.randomMemberName;
		String uuid=TC5091_MemberUndelete_Error_400.uuid; 
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
				
		//Generate the Content Type for POST. 
		String myContentType = "application/x-www-form-urlencoded";
		
		//Generate the API call for member.put.
		String apicall =  al.getApiURL()+"/member/remove?API_KEY=65432165432165432165432165432165&BRAND_ID=fffff99b56d8d0567d3465876e8579c2";
		
		Reporter.log("");
		Reporter.log("1) Member.remove with 'Delay 6' Brand");
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.REMOVE ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");

		String myJsonBody ="id="+name;
		//Generate the JSON Body in proper format.
		Reporter.log("POST Body= " +myJsonBody);
		int myResponseCode = al.postHTTPReponseCode(apicall, myJsonBody, myContentType);
		if(myResponseCode != 502)
			fail("Member.remove Response code ="+myResponseCode);
		Reporter.log("-- X --");
		Reporter.log("");
		
		// 2) member.remove with uuid 
		Reporter.log("2) member.remove to remove member from IDX");
		myJsonBody= "id="+uuid;
		int removeReturnCode = ma.memberREMOVEResponseCode(api, al, myJsonBody, surfBrandId, name, uuid, mydb);
		if(removeReturnCode != 204)
			fail("Failed : HTTP error code : "+ removeReturnCode);
		Reporter.log("-- X --");
		Reporter.log("");		
	}
	
}
