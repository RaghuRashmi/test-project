package com.nbcuni.test.nbcidx.member.put.errorcodes;

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

/**************************************************************************
 * NBCIDX Member.Put API - TC5045_MemberPut_Error_400_AllBrands. Copyright. 
 * 
 * @author Rashmi Sale
 * @version 1.0 Date: July 4, 2014
 **************************************************************************/

public class TC5045_MemberPut_Error_400_AllBrands {
	
	private CustomWebDriver cs;
	private AppLib al;
	private API api;
	private MemberAPIs ma;
	private Proxy proxy=null;

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
			} catch (Exception e) {
				fail(e.toString());
				}
	}
	
	/**
     * Instantiate the TestNG Test Method.
     */
	@Test(groups = {"full"})
	public void memberPut_error400_allBrands() throws Exception {
	
		String apicall=null;
		int myResponseCode=0;

		Reporter.log("");
		Reporter.log("1) Validating member.put 400 error code for All Brands");
		Reporter.log("");
		
		//Generate the JSON Body in proper format.
		String jsonBody="{'username': 'moody5','suffix': 'ABC','firstname': 'Beeee','middlename': '-','lastname': 'Weeee','phone': [{'number': '9876543214','primary': true},{'mobile': true,'number': '8765432109','primary': false}],'prefix': 'Sir','brand_data': {'Mydata': 'Any-Brand'},'gender': 'm','avatar': 'http://www.mbc.com/','screenname': 'Guru'}";
		String myJsonBodyAfterFormat=al.convertIntoJsonFormat(jsonBody);
		Reporter.log("JSON Body for POST = " +myJsonBodyAfterFormat);
		
		Reporter.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MEMBER.PUT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Reporter.log("Validating API call Response");
		Reporter.log("");
		
		//Generate the Content Type for POST. 
		String myContentType = "application/json";
		
		//Generate the API call for member.put.
		apicall = al.getApiURL()+"/member/put?API_KEY="+al.getDefaultApiKey()+"&BRAND_ID=*";
				
		//Send member.put POST Request. 
		myResponseCode = al.postHTTPReponseCode(apicall, myJsonBodyAfterFormat, myContentType);

		if(myResponseCode == 400)
		{
			Reporter.log("Passed : Response code from member.put : "+myResponseCode);
			Reporter.log(" ");
		}
		else
			fail("Failed : HTTP code : "+ myResponseCode);
	}
}
