package com.nbcuni.test.nbcidx.member.undelete.errorcodes;

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
 * NBCIDX Member.Undelete API - TC5094_MemberUndelete_Error_404. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: Sep 12, 2014
 **************************************************************************/

public class TC5094_MemberUndelete_Error_404 {
	
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private DB mydb;
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
	@Test(groups = {"full"})
	public void memberUndelete_error404() throws Exception {

		String UUID = al.createRandomUUID();
		String uuid=TC5091_MemberUndelete_Error_400.uuid; 
		
		Reporter.log("");
		Reporter.log("1) Member.undelete with non-existent member");

		// Get Surf Example Site Brand Id
		String surfBrandId = al.getSurfBrandId();
		
		int myResonseCode = ma.memberUNDELETEResponseCode(api, al, UUID, surfBrandId, mydb);
		if(myResonseCode != 404)
			fail("Member.undelete Response code ="+myResonseCode);
		Reporter.log("--X--");		
		Reporter.log("");		
	}
}
