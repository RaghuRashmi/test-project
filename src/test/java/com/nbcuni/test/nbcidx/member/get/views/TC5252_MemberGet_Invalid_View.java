package com.nbcuni.test.nbcidx.member.get.views;

import static org.testng.AssertJUnit.fail;

import java.net.Proxy;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.mongodb.DB;
import com.nbcuni.test.nbcidx.AppLib;
import com.nbcuni.test.nbcidx.MemberAPIs;
import com.nbcuni.test.webdriver.API;
import com.nbcuni.test.webdriver.CustomWebDriver;

/**************************************************************************
 * NBCIDX Member.Get API - TC5252_MemberGet_Invalid_View. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC5252_MemberGet_Invalid_View {

	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private Proxy proxy=null;
	private static MemberAPIs ma;
	private static DB mydb;
	
	/**
	 * Instantiate the TestNG Before Class Method.
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
     */
	@Test(groups = {"full"})
	public void memberGet_view_invalidValue() throws Exception {
					
		String name=TC01_Create_ComplexMember_With_AttributeData.randomMemberUserName;
		String uuid= TC01_Create_ComplexMember_With_AttributeData.sfinalUUID;
		
		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
				
		Reporter.log("1) Validating member.get API Response with view=<invalid value> and id=username");
		String params = "view=xyz&id="+name;

		int responseCode = ma.memberGetResponseCode(api, al, params, surfBrandId);
		if(responseCode != 200)
		{
			Reporter.log("Passed : ERROR code from member.get : "+responseCode);
			Reporter.log(" ");
		}
		else
			fail("Failed : HTTP error code : "+ responseCode);
		Reporter.log("-- X --");
		Reporter.log("");		
		
		//Removing member from IDX
		String jsonBody = "id="+uuid;
						
		int removeReturnCode = ma.memberREMOVEResponseCode(api, al, jsonBody, surfBrandId, name, uuid, mydb);		
		if(removeReturnCode != 204)
			fail("Failed : HTTP error code : "+ removeReturnCode);		
	}
}
